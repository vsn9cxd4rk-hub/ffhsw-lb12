import client from './client';
import { Article, ArticleInspection, InspectionCriterion, PaginatedResponse } from '../types';

export const inspectionsApi = {
  getAll: (params?: { page?: number; limit?: number; result?: string; deviceClassId?: number; year?: number }) =>
    client.get<{ data: ArticleInspection[]; pagination: PaginatedResponse<ArticleInspection>['pagination'] }>('/inspections', { params }),

  getDue: (params?: { deviceClassId?: number; deviceSubclassId?: number }) =>
    client.get<{ data: Article[] }>('/inspections/due', { params }),

  getArticleInspections: (articleId: number) =>
    client.get<{ data: ArticleInspection[] }>(`/inspections/article/${articleId}`),

  getCriteria: (articleId: number) =>
    client.get<{ data: InspectionCriterion[] }>(`/inspections/criteria/${articleId}`),

  create: (data: {
    articleId: number;
    inspectedAt: string;
    inspectedBy: string;
    notes?: string;
    criterionResults?: Array<{ criterionId: number; result: 'io' | 'nio' }>;
  }) => client.post('/inspections', data),

  update: (id: number, data: Record<string, unknown>) =>
    client.put(`/inspections/${id}`, data),

  getReport: (params?: { deviceClassId?: number; year?: number }) =>
    client.get<{ data: ArticleInspection[] }>('/inspections/report', { params }),
};
