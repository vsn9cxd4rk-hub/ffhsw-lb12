import client from './client';
import { Course, CourseCategory } from '../types';

export const trainingApi = {
  getCourses: (params?: Record<string, unknown>) => client.get('/training/courses', { params }),
  getById: (id: number) => client.get<{ data: Course }>(`/training/courses/${id}`),
  create: (data: Partial<Course>) => client.post<{ data: Course }>('/training/courses', data),
  update: (id: number, data: Partial<Course>) => client.put<{ data: Course }>(`/training/courses/${id}`, data),
  delete: (id: number) => client.delete(`/training/courses/${id}`),

  uploadCertificate: (courseId: number, file: File) => {
    const formData = new FormData();
    formData.append('certificate', file);
    return client.post(`/training/courses/${courseId}/certificate`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  downloadCertificate: (courseId: number) =>
    client.get(`/training/courses/${courseId}/certificate`, { responseType: 'blob' }),
  deleteCertificate: (courseId: number) =>
    client.delete(`/training/courses/${courseId}/certificate`),

  getCategories: () => client.get<{ data: CourseCategory[] }>('/training/categories'),
  createCategory: (data: Partial<CourseCategory>) => client.post('/training/categories', data),
  updateCategory: (id: number, data: Partial<CourseCategory>) =>
    client.put(`/training/categories/${id}`, data),
  deleteCategory: (id: number) => client.delete(`/training/categories/${id}`),
};
