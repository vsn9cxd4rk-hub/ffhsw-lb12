import client from './client';
import { Event, FireWatch } from '../types';

export const eventsApi = {
  getAll: (params?: Record<string, unknown>) => client.get('/events', { params }),
  getById: (id: number) => client.get<{ data: Event }>(`/events/${id}`),
  create: (data: Partial<Event>) => client.post<{ data: Event }>('/events', data),
  update: (id: number, data: Partial<Event>) => client.put<{ data: Event }>(`/events/${id}`, data),
  delete: (id: number) => client.delete(`/events/${id}`),

  getAttendance: (id: number) => client.get(`/events/${id}/attendance`),
  updateAttendance: (id: number, records: Array<{ memberId: number; status: string }>) =>
    client.post(`/events/${id}/attendance`, { records }),

  getFireWatches: (params?: Record<string, unknown>) => client.get('/events/firewatches', { params }),
  createFireWatch: (data: Partial<FireWatch>) => client.post('/events/firewatches', data),
  getFireWatch: (id: number) => client.get<{ data: FireWatch }>(`/events/firewatches/${id}`),
  updateFireWatch: (id: number, data: Partial<FireWatch>) =>
    client.put(`/events/firewatches/${id}`, data),
  deleteFireWatch: (id: number) => client.delete(`/events/firewatches/${id}`),
};
