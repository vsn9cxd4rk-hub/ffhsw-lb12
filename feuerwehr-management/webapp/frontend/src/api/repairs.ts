import client from './client';
import { ArticleRepair, PaginatedResponse } from '../types';

export const repairsApi = {
  getAll: (params?: { page?: number; limit?: number; articleId?: number }) =>
    client.get<{ data: ArticleRepair[]; pagination: PaginatedResponse<ArticleRepair>['pagination'] }>('/inventory/repairs', { params }),

  create: (data: {
    articleId: number;
    defectId?: number;
    repairedAt: string;
    repairedBy: string;
    description: string;
    cost?: number;
    notes?: string;
  }) => client.post('/inventory/repairs', data),

  update: (id: number, data: Record<string, unknown>) =>
    client.put(`/inventory/repairs/${id}`, data),

  delete: (id: number) =>
    client.delete(`/inventory/repairs/${id}`),
};
