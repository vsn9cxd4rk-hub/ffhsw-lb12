import client from './client';
import { Rank } from '../types';

export const settingsApi = {
  get: () => client.get<{ data: Record<string, string> }>('/settings'),
  update: (data: Record<string, string>) => client.put('/settings', data),

  getRanks: () => client.get<{ data: Rank[] }>('/settings/ranks'),
  createRank: (data: Partial<Rank>) => client.post('/settings/ranks', data),
  updateRank: (id: number, data: Partial<Rank>) => client.put(`/settings/ranks/${id}`, data),
  deleteRank: (id: number) => client.delete(`/settings/ranks/${id}`),

  getYears: () => client.get('/settings/years'),
  createYear: (year: number) => client.post('/settings/years', { year }),
  updateYear: (id: number, data: { isActive?: boolean }) => client.put(`/settings/years/${id}`, data),
};
