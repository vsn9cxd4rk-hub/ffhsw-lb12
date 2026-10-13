// Subset of webapp/frontend/src/types/index.ts; keep both in sync when the API changes.

export interface User {
  id: number;
  username: string;
  email: string | null;
  name: string | null;
  isAdmin: boolean;
  isActive: boolean;
  groupId: number | null;
  memberId?: number | null;
  permissions: Record<string, boolean>;
  /** Initial or admin-reset password: the backend blocks everything except the password change */
  mustChangePassword?: boolean;
  createdAt: string;
}

export interface DashboardStats {
  activeMembers: number;
  vehicles: number;
  operationsThisYear: number;
  recentOperations: {
    id: number;
    date: string;
    location: string;
    keyword: string | null;
    leaderCount: number;
    memberCount: number;
  }[];
  upcomingInspections: {
    type: string;
    entityName: string;
    dueDate: string;
    status: 'green' | 'yellow' | 'red';
    articleId?: number;
  }[];
  upcomingMedicalExams: { memberName: string; examType: string; dueDate: string }[];
}

export interface Session {
  accessToken: string;
  user: User;
}
