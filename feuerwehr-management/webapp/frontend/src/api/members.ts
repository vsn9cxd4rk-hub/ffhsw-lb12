import client from './client';
import { Member, MemberFamily, MemberWork, MemberBank, MemberExamination, Course, MemberGroup, Absence } from '../types';

export const membersApi = {
  getAll: (params?: Record<string, unknown>) => client.get('/members', { params }),
  getById: (id: number) => client.get<{ data: Member }>(`/members/${id}`),
  create: (data: Partial<Member>) => client.post<{ data: Member }>('/members', data),
  update: (id: number, data: Partial<Member>) => client.put<{ data: Member }>(`/members/${id}`, data),
  delete: (id: number) => client.delete(`/members/${id}`),
  getHistory: (id: number) => client.get(`/members/${id}/history`),

  // Family
  createFamily: (memberId: number, data: Partial<MemberFamily>) =>
    client.post(`/members/${memberId}/family`, data),
  updateFamily: (memberId: number, familyId: number, data: Partial<MemberFamily>) =>
    client.put(`/members/${memberId}/family/${familyId}`, data),
  deleteFamily: (memberId: number, familyId: number) =>
    client.delete(`/members/${memberId}/family/${familyId}`),

  // Work, Bank, Examination
  updateWork: (memberId: number, data: Partial<MemberWork>) =>
    client.put(`/members/${memberId}/work`, data),
  updateBank: (memberId: number, data: Partial<MemberBank>) =>
    client.put(`/members/${memberId}/bank`, data),
  updateExamination: (memberId: number, data: Partial<MemberExamination>) =>
    client.put(`/members/${memberId}/examination`, data),

  // Courses
  getCourses: (memberId: number) => client.get(`/members/${memberId}/courses`),
  createCourse: (memberId: number, data: Partial<Course>) =>
    client.post(`/members/${memberId}/courses`, data),
  updateCourse: (memberId: number, courseId: number, data: Partial<Course>) =>
    client.put(`/members/${memberId}/courses/${courseId}`, data),

  // AGT Records
  getAgtRecords: (memberId: number) => client.get(`/members/${memberId}/agt-records`),
  createAgtRecord: (memberId: number, data: { type: string; date: string; result?: string; notes?: string }) =>
    client.post(`/members/${memberId}/agt-records`, data),
  deleteAgtRecord: (memberId: number, recordId: number) =>
    client.delete(`/members/${memberId}/agt-records/${recordId}`),

  // Absences
  createAbsence: (memberId: number, data: Partial<Absence>) =>
    client.post(`/members/${memberId}/absences`, data),
  updateAbsence: (memberId: number, absenceId: number, data: Partial<Absence>) =>
    client.put(`/members/${memberId}/absences/${absenceId}`, data),
  deleteAbsence: (memberId: number, absenceId: number) =>
    client.delete(`/members/${memberId}/absences/${absenceId}`),

  // Groups
  getGroups: () => client.get<{ data: MemberGroup[] }>('/members/groups'),
  createGroup: (data: Partial<MemberGroup>) => client.post('/members/groups', data),
  updateGroup: (id: number, data: Partial<MemberGroup>) => client.put(`/members/groups/${id}`, data),
};
