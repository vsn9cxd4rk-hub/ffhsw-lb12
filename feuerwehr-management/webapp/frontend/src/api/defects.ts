import client from './client';
import { ArticleDefect, PaginatedResponse } from '../types';

export const defectsApi = {
  getAll: (params?: { page?: number; limit?: number; status?: string; severity?: string; articleId?: number; articleIds?: string; search?: string }) =>
    client.get<{ data: ArticleDefect[]; pagination: PaginatedResponse<ArticleDefect>['pagination'] }>('/inventory/defects', { params }),

  getById: (id: number) =>
    client.get<{ data: ArticleDefect }>(`/inventory/defects/${id}`),

  create: (data: {
    articleId: number;
    reportedBy: string;
    reportedAt: string;
    description: string;
    severity: string;
    notes?: string;
  }) => client.post('/inventory/defects', data),

  update: (id: number, data: Record<string, unknown>) =>
    client.put(`/inventory/defects/${id}`, data),

  delete: (id: number) =>
    client.delete(`/inventory/defects/${id}`),
};
