import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';

const client: AxiosInstance = axios.create({
  baseURL: '/api',
  withCredentials: true,
  headers: { 'Content-Type': 'application/json' },
});

<<<<<<< HEAD
// Request interceptor: attach access token
=======
// Request interceptor: attach access token, but skip if already logged out
>>>>>>> a9dc7840 (Added New FW Management system)
client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

let isRefreshing = false;
<<<<<<< HEAD
=======
let isLoggedOut = false;
>>>>>>> a9dc7840 (Added New FW Management system)
let failedQueue: Array<{ resolve: (value: string) => void; reject: (error: unknown) => void }> = [];

function processQueue(error: unknown, token: string | null = null) {
  failedQueue.forEach(({ resolve, reject }) => {
    if (error) reject(error);
    else resolve(token!);
  });
  failedQueue = [];
}

<<<<<<< HEAD
=======
function forceLogout() {
  if (isLoggedOut) return;
  isLoggedOut = true;
  localStorage.removeItem('accessToken');
  // Clear the persisted zustand auth store
  localStorage.removeItem('fuerwehr-auth');
  window.location.href = '/login';
}

>>>>>>> a9dc7840 (Added New FW Management system)
// Response interceptor: handle 401 and refresh
client.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    const originalRequest = error.config;

<<<<<<< HEAD
=======
    // Already logged out — reject immediately, don't retry
    if (isLoggedOut) {
      return Promise.reject(error);
    }

>>>>>>> a9dc7840 (Added New FW Management system)
    if (error.response?.status === 401 && !originalRequest._retry) {
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
<<<<<<< HEAD
        localStorage.removeItem('accessToken');
        window.location.href = '/login';
=======
        forceLogout();
>>>>>>> a9dc7840 (Added New FW Management system)
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default client;
