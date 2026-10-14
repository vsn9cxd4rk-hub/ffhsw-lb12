import type {
  Article,
  ArticleDefect,
  AttendanceList,
  AttendanceStatus,
  Event,
  LogbookEntry,
  MemberOption,
  Operation,
  OperationPersonnel,
  OperationTime,
  PersonnelSection,
  Vehicle,
  ArticleInspection,
  CriterionResult,
  DashboardStats,
  DefectSeverity,
  DefectStatus,
  DueArticle,
  InspectionCriterion,
  InspectionType,
  Inspector,
  Notification,
  Session,
  User,
} from './types';

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
    // Validation errors (422) carry the readable message in details[0].msg
    const message = body?.details?.[0]?.msg ?? body?.error ?? `HTTP ${response.status}`;
    throw new ApiError(message, response.status, body?.code);
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

/** "?a=1&b=x" from the defined values, "" when there are none. */
function query(params: Record<string, string | number | undefined>): string {
  const pairs = Object.entries(params).filter(([, value]) => value !== undefined && value !== '');
  if (pairs.length === 0) return '';
  return `?${pairs.map(([key, value]) => `${key}=${encodeURIComponent(String(value))}`).join('&')}`;
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

  /** The backend revokes all refresh tokens afterwards; the caller has to sign in again. */
  async changePassword(oldPassword: string, newPassword: string): Promise<void> {
    await request('/auth/change-password', {
      method: 'PUT',
      body: JSON.stringify({ oldPassword, newPassword }),
    });
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
  getNotifications: () => request<Notification[]>('/dashboard/notifications'),
};

// Gerätewart area (BIT_EQUIPMENT); payloads match webapp/frontend/src/api/{inspections,defects}.ts

export const inventoryApi = {
  /** Searches name, manufacturer, EAN and all three inventory numbers */
  searchArticles: (search: string, limit = 50) =>
    request<Article[]>(`/inventory/articles${query({ search, limit })}`),
  getArticle: (id: number) => request<Article>(`/inventory/articles/${id}`),
};

export const inspectionsApi = {
  getDue: (search?: string) => request<DueArticle[]>(`/inspections/due${query({ search })}`),
  getArticleInspections: (articleId: number) =>
    request<ArticleInspection[]>(`/inspections/article/${articleId}`),
  getCriteria: (articleId: number) =>
    request<InspectionCriterion[]>(`/inspections/criteria/${articleId}`),
  getInspectors: () => request<Inspector[]>('/inspections/inspectors'),
  getTypes: () => request<InspectionType[]>('/settings/inspection-types'),
  create: (data: {
    articleId: number;
    /** YYYY-MM-DD */
    inspectedAt: string;
    inspectedBy: string;
    inspectionTypeId?: number;
    notes?: string;
    criterionResults?: { criterionId: number; result: CriterionResult }[];
  }) => request<ArticleInspection>('/inspections', { method: 'POST', body: JSON.stringify(data) }),
};

export const defectsApi = {
  list: (params: { status?: DefectStatus; articleId?: number; limit?: number } = {}) =>
    request<ArticleDefect[]>(`/inventory/defects${query({ limit: 100, ...params })}`),
  get: (id: number) => request<ArticleDefect>(`/inventory/defects/${id}`),
  create: (data: {
    articleId: number;
    reportedBy: string;
    /** YYYY-MM-DD */
    reportedAt: string;
    description: string;
    severity: DefectSeverity;
    notes?: string;
  }) => request<ArticleDefect>('/inventory/defects', { method: 'POST', body: JSON.stringify(data) }),
  update: (
    id: number,
    data: { status?: DefectStatus; resolvedBy?: string; resolvedAt?: string; notes?: string | null },
  ) => request<ArticleDefect>(`/inventory/defects/${id}`, { method: 'PUT', body: JSON.stringify(data) }),
};

// Veranstaltungen: reading is open to every user, changes need BIT_EVENTS

export const eventsApi = {
  /** dateFrom/dateTo as "YYYY-MM-DD"; the backend sorts by date descending */
  list: (params: { dateFrom?: string; dateTo?: string; limit?: number }) =>
    request<Event[]>(`/events${query({ limit: 50, ...params })}`),
  get: (id: number) => request<Event>(`/events/${id}`),
  getAttendance: (id: number) => request<AttendanceList>(`/events/${id}/attendance`),
  saveAttendance: (id: number, records: { memberId: number; status: AttendanceStatus }[]) =>
    request<{ message: string }>(`/events/${id}/attendance`, { method: 'POST', body: JSON.stringify({ records }) }),
};

// Fahrzeuge (BIT_VEHICLES)

export const vehiclesApi = {
  list: () => request<Vehicle[]>('/vehicles?isRetired=false'),
  get: (id: number) => request<Vehicle>(`/vehicles/${id}`),
  addLogbookEntry: (
    id: number,
    data: {
      /** YYYY-MM-DD */
      date: string;
      driver: string;
      purpose: string;
      destination?: string;
      startMileage: number;
      endMileage: number;
      notes?: string;
    },
  ) => request<LogbookEntry>(`/vehicles/${id}/logbook`, { method: 'POST', body: JSON.stringify(data) }),
};

// Einsätze (BIT_OPERATIONS); payloads match webapp/frontend/src/pages/operations/*

type OperationTimeInput = Omit<OperationTime, 'id' | 'operationId' | 'vehicleId'>;

export const operationsApi = {
  list: (limit = 50) => request<Operation[]>(`/operations${query({ limit })}`),
  get: (id: number) => request<Operation>(`/operations/${id}`),
  create: (data: { date: string; alarmTime?: string; location: string; keyword?: string }) =>
    request<Operation>('/operations', { method: 'POST', body: JSON.stringify(data) }),
  update: (id: number, data: { date?: string; location?: string; keyword?: string | null; description?: string | null }) =>
    request<Operation>(`/operations/${id}`, { method: 'PUT', body: JSON.stringify(data) }),

  addTime: (id: number, data: OperationTimeInput) =>
    request<OperationTime>(`/operations/${id}/times`, { method: 'POST', body: JSON.stringify(data) }),
  updateTime: (id: number, timeId: number, data: OperationTimeInput) =>
    request<OperationTime>(`/operations/${id}/times/${timeId}`, { method: 'PUT', body: JSON.stringify(data) }),
  deleteTime: (id: number, timeId: number) =>
    request<{ message: string }>(`/operations/${id}/times/${timeId}`, { method: 'DELETE' }),

  getPersonnel: (id: number) => request<OperationPersonnel[]>(`/operations/${id}/personnel`),
  addPersonnel: (id: number, data: { memberId: number; vehicleName: string; function: string; section: PersonnelSection }) =>
    request<OperationPersonnel>(`/operations/${id}/personnel`, { method: 'POST', body: JSON.stringify(data) }),
  deletePersonnel: (id: number, personnelId: number) =>
    request<{ message: string }>(`/operations/${id}/personnel/${personnelId}`, { method: 'DELETE' }),
};

export const membersApi = {
  /**
   * Active members for pickers. Only the picker fields are kept: for admins the backend returns
   * full records, which must not sit in the app's cache.
   */
  async listOptions(): Promise<MemberOption[]> {
    const members = await request<MemberOption[]>(`/members${query({ limit: 500, isInactive: 'false' })}`);
    return members.map(({ id, firstName, lastName, rank }) => ({ id, firstName, lastName, rank }));
  },
};
