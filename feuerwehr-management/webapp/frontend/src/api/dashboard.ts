import client from './client';
import { DashboardStats } from '../types';

export const dashboardApi = {
  getStats: () => client.get<{ data: DashboardStats }>('/dashboard/stats'),
  getStatistics: (type: string, params?: Record<string, unknown>) =>
    client.get(`/dashboard/statistics/${type}`, { params }),
};
