import client from './client';
import { Operation, OperationTime } from '../types';

export const operationsApi = {
  getAll: (params?: Record<string, unknown>) => client.get('/operations', { params }),
  getById: (id: number) => client.get<{ data: Operation }>(`/operations/${id}`),
  create: (data: Partial<Operation>) => client.post<{ data: Operation }>('/operations', data),
  update: (id: number, data: Partial<Operation>) => client.put<{ data: Operation }>(`/operations/${id}`, data),
  delete: (id: number) => client.delete(`/operations/${id}`),

  createTime: (operationId: number, data: Partial<OperationTime>) =>
    client.post(`/operations/${operationId}/times`, data),
  updateTime: (operationId: number, timeId: number, data: Partial<OperationTime>) =>
    client.put(`/operations/${operationId}/times/${timeId}`, data),
  deleteTime: (operationId: number, timeId: number) =>
    client.delete(`/operations/${operationId}/times/${timeId}`),

  getReport: (id: number) => client.get(`/operations/${id}/report`),
  updateReport: (id: number, content: string) =>
    client.put(`/operations/${id}/report`, { content }),

  getDocuments: (id: number) => client.get(`/operations/${id}/documents`),
  uploadDocument: (id: number, file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return client.post(`/operations/${id}/documents`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  downloadDocument: (operationId: number, docId: number) =>
    client.get(`/operations/${operationId}/documents/${docId}/download`, { responseType: 'blob' }),
  deleteDocument: (operationId: number, docId: number) =>
    client.delete(`/operations/${operationId}/documents/${docId}`),
};
