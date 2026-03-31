import client from './client';
import { User, PermissionGroup } from '../types';

export const usersApi = {
  getAll: (params?: Record<string, unknown>) => client.get('/users', { params }),
  getById: (id: number) => client.get<{ data: User }>(`/users/${id}`),
  create: (data: Partial<User> & { password: string }) => client.post<{ data: User }>('/users', data),
  update: (id: number, data: Partial<User> & { password?: string }) =>
    client.put<{ data: User }>(`/users/${id}`, data),
  delete: (id: number) => client.delete(`/users/${id}`),

  getGroups: () => client.get<{ data: PermissionGroup[] }>('/users/groups'),
  createGroup: (data: Partial<PermissionGroup>) => client.post('/users/groups', data),
  updateGroup: (id: number, data: Partial<PermissionGroup>) => client.put(`/users/groups/${id}`, data),
  deleteGroup: (id: number) => client.delete(`/users/groups/${id}`),
};
