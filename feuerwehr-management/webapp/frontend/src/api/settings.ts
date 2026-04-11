import client from './client';
<<<<<<< HEAD
import { Rank } from '../types';
=======
import { Rank, Template, TemplateHistory, DeviceClass } from '../types';
>>>>>>> a9dc7840 (Added New FW Management system)

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
<<<<<<< HEAD
=======

  getTemplates: () => client.get<{ data: Template[] }>('/settings/templates'),
  uploadTemplate: (name: string, file: File) => {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('file', file);
    return client.post('/settings/templates', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
  },
  updateTemplate: (id: number, file?: File, name?: string) => {
    const formData = new FormData();
    if (name) formData.append('name', name);
    if (file) formData.append('file', file);
    return client.put(`/settings/templates/${id}`, formData, { headers: { 'Content-Type': 'multipart/form-data' } });
  },
  downloadTemplate: (id: number) => client.get(`/settings/templates/${id}/download`, { responseType: 'blob' }),
  deleteTemplate: (id: number) => client.delete(`/settings/templates/${id}`),
  getTemplateHistory: (id: number) => client.get<{ data: TemplateHistory[] }>(`/settings/templates/${id}/history`),

  // Device Classes / Geräteklassen
  getDeviceClasses: () => client.get<{ data: DeviceClass[] }>('/settings/device-classes'),
  createDeviceClass: (data: { name: string; sortOrder?: number }) => client.post('/settings/device-classes', data),
  updateDeviceClass: (id: number, data: { name?: string; sortOrder?: number }) => client.put(`/settings/device-classes/${id}`, data),
  deleteDeviceClass: (id: number) => client.delete(`/settings/device-classes/${id}`),
  createSubclass: (classId: number, data: { name: string; sortOrder?: number }) => client.post(`/settings/device-classes/${classId}/subclasses`, data),
  updateSubclass: (id: number, data: { name?: string; sortOrder?: number }) => client.put(`/settings/device-subclasses/${id}`, data),
  deleteSubclass: (id: number) => client.delete(`/settings/device-subclasses/${id}`),
  createCriterion: (subclassId: number, data: { name: string; sortOrder?: number }) => client.post(`/settings/device-subclasses/${subclassId}/criteria`, data),
  updateCriterion: (id: number, data: { name?: string; sortOrder?: number }) => client.put(`/settings/inspection-criteria/${id}`, data),
  deleteCriterion: (id: number) => client.delete(`/settings/inspection-criteria/${id}`),

  // CSV Import
  importArticles: (articles: Array<Record<string, string>>) =>
    client.post<{ data: { imported: number; errors: Array<{ row: number; message: string }>; total: number } }>('/settings/import/articles', { articles }),
  importInspections: (inspections: Array<Record<string, string>>) =>
    client.post<{ data: { imported: number; errors: Array<{ row: number; message: string }>; total: number } }>('/settings/import/inspections', { inspections }),
>>>>>>> a9dc7840 (Added New FW Management system)
};
