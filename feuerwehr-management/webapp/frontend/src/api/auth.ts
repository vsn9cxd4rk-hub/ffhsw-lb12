import client from './client';
import { User } from '../types';

export const authApi = {
  login: (username: string, password: string) =>
    client.post<{ data: { accessToken: string; user: User } }>('/auth/login', { username, password }),

  logout: () => client.post('/auth/logout'),

  refresh: () => client.post<{ data: { accessToken: string } }>('/auth/refresh'),

  getMe: () => client.get<{ data: User }>('/auth/me'),

  changePassword: (oldPassword: string, newPassword: string) =>
    client.put('/auth/change-password', { oldPassword, newPassword }),
};
