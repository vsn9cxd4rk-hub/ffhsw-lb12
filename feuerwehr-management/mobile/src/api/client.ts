import type { DashboardStats, Session, User } from './types';

// Same session model as the web frontend (webapp/frontend/src/api/client.ts): the access token
// lives only in memory, the refresh token is the httpOnly cookie set by /api/auth/login. On iOS
// and Android the native HTTP stack keeps that cookie, so /auth/refresh works after an app restart.

export class ApiError extends Error {
  constructor(
    message: string,
    readonly status: number,
    readonly code?: string,
  ) {
    super(message);
  }
}

let apiBase = '';
let accessToken: string | null = null;
let onSessionEnded: (() => void) | null = null;
let refreshPromise: Promise<Session> | null = null;

/** Accepts "192.168.1.20:3001", "https://fenix.example.de/" or ".../api" and returns the API root. */
export function normalizeServerUrl(input: string): string {
  let url = input.trim().replace(/\/+$/, '');
  if (!/^https?:\/\//i.test(url)) url = `http://${url}`;
  return url.replace(/\/api$/i, '');
}

export function setServerUrl(serverUrl: string) {
  apiBase = `${normalizeServerUrl(serverUrl)}/api`;
}

/** Registered by the SessionProvider: called when the backend rejects the session. */
export function setSessionEndedHandler(handler: () => void) {
  onSessionEnded = handler;
}

async function parse<T>(response: Response): Promise<T> {
  const body = await response.json().catch(() => null);
  if (!response.ok || !body?.success) {
    throw new ApiError(body?.error ?? `HTTP ${response.status}`, response.status, body?.code);
  }
  return body.data as T;
}

function send(path: string, init: RequestInit = {}): Promise<Response> {
  const headers: Record<string, string> = { 'Content-Type': 'application/json' };
  if (accessToken) headers.Authorization = `Bearer ${accessToken}`;
  return fetch(`${apiBase}${path}`, { ...init, credentials: 'include', headers });
}

/** All requests that run into a 401 at the same time share one refresh request. */
function refresh(): Promise<Session> {
  if (!refreshPromise) {
    refreshPromise = send('/auth/refresh', { method: 'POST' })
      .then((response) => parse<Session>(response))
      .then((session) => {
        accessToken = session.accessToken;
        return session;
      })
      .finally(() => {
        refreshPromise = null;
      });
  }
  return refreshPromise;
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const tokenUsed = accessToken;
  let response = await send(path, init);
  if (response.status === 401) {
    try {
      // A parallel request may already have refreshed while this one was in flight
      if (accessToken === tokenUsed) await refresh();
    } catch (error) {
      // Only a 401 from /auth/refresh ends the session; network errors are temporary
      if (error instanceof ApiError && error.status === 401) {
        accessToken = null;
        onSessionEnded?.();
      }
      throw error;
    }
    response = await send(path, init);
  }
  return parse<T>(response);
}

export const authApi = {
  async login(username: string, password: string): Promise<User> {
    const session = await parse<Session>(
      await send('/auth/login', { method: 'POST', body: JSON.stringify({ username, password }) }),
    );
    accessToken = session.accessToken;
    return session.user;
  },

  /** Restores the session from the refresh cookie; resolves to null if there is none. */
  async restore(): Promise<User | null> {
    try {
      return (await refresh()).user;
    } catch (error) {
      if (error instanceof ApiError && error.status === 401) return null;
      throw error;
    }
  },

  async logout(): Promise<void> {
    try {
      await send('/auth/logout', { method: 'POST' });
    } finally {
      accessToken = null;
    }
  },
};

export const dashboardApi = {
  getStats: () => request<DashboardStats>('/dashboard/stats'),
};
