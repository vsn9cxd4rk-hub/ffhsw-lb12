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

  getDocuments: (id: number) => client.get(`/events/${id}/documents`),
  uploadDocument: (id: number, file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return client.post(`/events/${id}/documents`, formData, { headers: { 'Content-Type': 'multipart/form-data' } });
  },
  downloadDocument: (eventId: number, docId: number) =>
    client.get(`/events/${eventId}/documents/${docId}/download`, { responseType: 'blob' }),
  deleteDocument: (eventId: number, docId: number) =>
    client.delete(`/events/${eventId}/documents/${docId}`),

  getFireWatches: (params?: Record<string, unknown>) => client.get('/events/firewatches', { params }),
  createFireWatch: (data: Partial<FireWatch>) => client.post('/events/firewatches', data),
  getFireWatch: (id: number) => client.get<{ data: FireWatch }>(`/events/firewatches/${id}`),
  updateFireWatch: (id: number, data: Partial<FireWatch>) =>
    client.put(`/events/firewatches/${id}`, data),
  deleteFireWatch: (id: number) => client.delete(`/events/firewatches/${id}`),
};
