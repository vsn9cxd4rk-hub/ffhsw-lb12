import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';

const client: AxiosInstance = axios.create({
  baseURL: '/api',
  withCredentials: true,
});

// Request interceptor: attach access token, but skip if already logged out
client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let isRefreshing = false;
let isLoggedOut = false;
let failedQueue: Array<{ resolve: (value: string) => void; reject: (error: unknown) => void }> = [];

function processQueue(error: unknown, token: string | null = null) {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) reject(error);
    else resolve(token!);
  });
  failedQueue = [];
}

function forceLogout() {
  if (isLoggedOut) return;
  isLoggedOut = true;
  localStorage.removeItem('accessToken');
  // Clear the persisted zustand auth store
  localStorage.removeItem('fuerwehr-auth');
  window.location.href = '/login';
}

// Response interceptor: handle 401 and refresh
client.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    const originalRequest = error.config;

    // Already logged out — reject immediately, don't retry
    if (isLoggedOut) {
      return Promise.reject(error);
    }

    // Auth endpoints must not trigger the refresh/forceLogout flow: a 401 from
    // /auth/login means "wrong credentials" and a 401 from /auth/refresh means
    // "no valid session" — both should surface to the caller (e.g. the login
    // form) instead of being swallowed by a token refresh and page reload.
    const requestUrl: string = originalRequest?.url || '';
    const isAuthEndpoint = requestUrl.includes('/auth/login') || requestUrl.includes('/auth/refresh');

    if (error.response?.status === 401 && !originalRequest._retry && !isAuthEndpoint) {
      if (isRefreshing) {
        return new Promise<string>((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return client(originalRequest);
          })
          .catch((err) => Promise.reject(err));
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const response = await axios.post('/api/auth/refresh', {}, { withCredentials: true });
        const { accessToken } = response.data.data;
        localStorage.setItem('accessToken', accessToken);
        processQueue(null, accessToken);
        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return client(originalRequest);
      } catch (refreshError) {
        processQueue(refreshError, null);
        forceLogout();
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default client;
