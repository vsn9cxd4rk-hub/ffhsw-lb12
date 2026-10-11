import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios';
import { useAuthStore } from '../store/auth.store';
import { User } from '../types';

const client: AxiosInstance = axios.create({
  baseURL: '/api',
  withCredentials: true,
});

// Request interceptor: attach the in-memory access token
client.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = useAuthStore.getState().accessToken;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// --- Session handling across tabs -------------------------------------------------------
// Every tab keeps its own access token in memory; the refresh cookie is shared by all tabs.
// Logins/logouts are broadcast so all tabs of the browser follow the same session (important
// on shared PCs in the Gerätehaus: logging out in one tab must not leave another tab open).
type SessionMessage = { type: 'login' } | { type: 'logout' };
const channel = typeof BroadcastChannel !== 'undefined' ? new BroadcastChannel('fuerwehr-auth') : null;

function broadcast(message: SessionMessage) {
  channel?.postMessage(message);
}

let onSessionEnded: (() => void) | null = null;

/** Registered by the app to drop cached query data when the session ends. */
export function setSessionEndedHandler(handler: () => void) {
  onSessionEnded = handler;
}

function endSessionLocally() {
  useAuthStore.getState().clearAuth();
  onSessionEnded?.();
}

channel?.addEventListener('message', (event: MessageEvent<SessionMessage>) => {
  if (event.data.type === 'logout') {
    endSessionLocally();
  } else if (event.data.type === 'login' && useAuthStore.getState().status !== 'authenticated') {
    // Another tab logged in: pick up the shared cookie instead of showing the login form
    void restoreSession();
  }
});

/** Call after a successful login so other open tabs switch to the new session. */
export function announceLogin() {
  broadcast({ type: 'login' });
}

/** Ends the session server-side and in every tab of this browser. */
export async function logoutEverywhere() {
  try {
    await axios.post('/api/auth/logout', {}, { withCredentials: true });
  } catch {
    // the local logout below still happens
  }
  endSessionLocally();
  broadcast({ type: 'logout' });
}

// --- Token refresh --------------------------------------------------------------------
// All requests that run into a 401 at the same time share one refresh request.
let refreshPromise: Promise<string> | null = null;

function refreshAccessToken(): Promise<string> {
  if (!refreshPromise) {
    refreshPromise = axios
      .post<{ data: { accessToken: string; user: User } }>('/api/auth/refresh', {}, { withCredentials: true })
      .then((response) => {
        const { accessToken, user } = response.data.data;
        // Another tab may have logged in as someone else (the cookie is shared): drop the
        // previous user's cached data before switching.
        const previous = useAuthStore.getState().user;
        if (previous && previous.id !== user.id) onSessionEnded?.();
        // The refresh also returns the current user, so changed permissions apply without re-login
        useAuthStore.getState().setAuth(user, accessToken);
        return accessToken;
      })
      .finally(() => {
        refreshPromise = null;
      });
  }
  return refreshPromise;
}

/** Restores the session from the refresh cookie on page load; resolves to true if logged in. */
export async function restoreSession(): Promise<boolean> {
  try {
    await refreshAccessToken();
    return true;
  } catch {
    useAuthStore.getState().clearAuth();
    return false;
  }
}

// Response interceptor: on 401 refresh once and retry
client.interceptors.response.use(
  (response: AxiosResponse) => response,
  async (error) => {
    const originalRequest = error.config;

    // Auth endpoints must not trigger the refresh flow: a 401 from /auth/login means
    // "wrong credentials" and should surface to the login form.
    const requestUrl: string = originalRequest?.url || '';
    const isAuthEndpoint = requestUrl.includes('/auth/login') || requestUrl.includes('/auth/refresh');

    if (error.response?.status === 401 && originalRequest && !originalRequest._retry && !isAuthEndpoint) {
      originalRequest._retry = true;
      const tokenUsed = (originalRequest.headers?.Authorization as string | undefined)?.substring(7);
      try {
        // A parallel request may already have refreshed while this one was in flight
        const current = useAuthStore.getState().accessToken;
        const token = current && current !== tokenUsed ? current : await refreshAccessToken();
        originalRequest.headers.Authorization = `Bearer ${token}`;
        return client(originalRequest);
      } catch (refreshError) {
        endSessionLocally();
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default client;
