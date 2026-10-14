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

/** From /dashboard/notifications (webapp/frontend/src/components/layout/Header.tsx) */
export interface Notification {
  id: string;
  type: 'inspection' | 'defect' | 'vehicle' | 'medical' | (string & {});
  severity: 'red' | 'yellow' | 'info';
  title: string;
  message: string;
  link?: string;
}

// ==================== Gerätewart: Bestand, Prüfungen, Mängel ====================

export interface DeviceClass {
  id: number;
  name: string;
  sortOrder: number;
}

export interface DeviceSubclass {
  id: number;
  deviceClassId: number;
  name: string;
  sortOrder: number;
  deviceClass?: DeviceClass;
}

export interface Warehouse {
  id: number;
  name: string;
  description: string | null;
  vehicleId: number | null;
}

export interface ArticleAssignment {
  id: number;
  articleId: number;
  warehouseId: number;
  quantity: number;
  assignedTo: string | null;
  notes: string | null;
  warehouse?: Warehouse;
}

export interface ArticleDocument {
  id: number;
  articleId: number;
  fileName: string;
  fileSize: number;
  mimeType: string;
  uploadedBy: string;
  createdAt: string;
}

export interface Article {
  id: number;
  name: string;
  manufacturer: string | null;
  articleType: string | null;
  description: string | null;
  inspectionInterval: number | null;
  inventoryNumber: string | null;
  communityInventoryNumber: string | null;
  mpFeuerInventoryNumber: string | null;
  serialNumber: string | null;
  specification: string | null;
  din: string | null;
  manufacturingDate: string | null;
  commissionedDate: string | null;
  isDecommissioned: boolean;
  decommissionedDate: string | null;
  warehouseId: number | null;
  deviceSubclassId: number | null;
  warehouse?: Warehouse | null;
  deviceSubclass?: DeviceSubclass | null;
  assignments?: ArticleAssignment[];
  documents?: ArticleDocument[];
  defects?: ArticleDefect[];
}

/** From /inspections/due: the article with its latest inspection (if any) */
export type DueArticle = Article & { inspections: ArticleInspection[] };

export interface InspectionCriterion {
  id: number;
  deviceSubclassId: number;
  name: string;
  sortOrder: number;
}

export type CriterionResult = 'io' | 'nio';

export interface InspectionCriterionResult {
  id: number;
  inspectionId: number;
  criterionId: number;
  result: CriterionResult;
  criterion?: InspectionCriterion;
}

export interface InspectionType {
  id: number;
  name: string;
  description: string | null;
}

export interface ArticleInspection {
  id: number;
  articleId: number;
  inspectionTypeId: number | null;
  inspectedAt: string;
  inspectedBy: string;
  /** 'passed' | 'failed' (computed by the backend from the criteria) */
  result: string;
  notes: string | null;
  nextDueDate: string | null;
  createdAt: string;
  inspectionType?: InspectionType | null;
  criterionResults?: InspectionCriterionResult[];
}

export interface Inspector {
  id: number;
  firstName: string;
  lastName: string;
  rank: string | null;
  name: string;
}

export type DefectSeverity = 'low' | 'medium' | 'high' | 'critical';
export type DefectStatus = 'open' | 'in_progress' | 'resolved' | 'closed';

export interface ArticleDefect {
  id: number;
  articleId: number | null;
  /** Free-text subject for defects not tied to an article (vehicle, "Sonstige") */
  subject: string | null;
  reportedBy: string;
  reportedAt: string;
  description: string;
  severity: DefectSeverity;
  status: DefectStatus;
  resolvedAt: string | null;
  resolvedBy: string | null;
  notes: string | null;
  createdAt: string;
  article?: Article | null;
}

// ==================== Veranstaltungen ====================

/** 1=Einsatz, 2=Dienstabend, 3=BSW, 4=Sonstige, 5=Übung */
export type EventCategory = 1 | 2 | 3 | 4 | 5;

export interface Event {
  id: number;
  name: string;
  name2: string | null;
  category: EventCategory;
  date: string;
  startTime: string | null;
  endTime: string | null;
  notes: string | null;
}

export type AttendanceStatus = 'present' | 'absent' | 'excused';

/** From /events/:id/attendance: every active member with the recorded status (null = not recorded) */
export interface AttendanceList {
  event: Event;
  year: number;
  members: {
    memberId: number;
    member: { id: number; firstName: string; lastName: string; rank: string | null; group: { name: string } | null };
    status: AttendanceStatus | null;
  }[];
}

// ==================== Fahrzeuge ====================

export interface VehicleInspection {
  id: number;
  vehicleId: number;
  tuevDate: string | null;
  spDate: string | null;
  serviceDate: string | null;
  notes: string | null;
}

export interface LogbookEntry {
  id: number;
  vehicleId: number;
  date: string;
  driver: string;
  startMileage: number;
  endMileage: number;
  purpose: string;
  destination: string | null;
  notes: string | null;
}

export interface Vehicle {
  id: number;
  name: string;
  description: string | null;
  licensePlate: string | null;
  callSign: string | null;
  seats: number | null;
  licenseClass: string | null;
  isRetired: boolean;
  isTrailer: boolean;
  sortOrder: number;
  inspection?: VehicleInspection | null;
  /** Only in the detail: the 20 latest entries */
  logbook?: LogbookEntry[];
}

// ==================== Einsätze ====================

export interface Operation {
  id: number;
  operationNumber: string | null;
  date: string;
  alarmTime: string | null;
  location: string;
  keyword: string | null;
  description: string | null;
  /** Derived from the vehicle times by the backend (comma-separated names and summed strength) */
  vehicles: string | null;
  leaderCount: number;
  memberCount: number;
  times?: OperationTime[];
}

export interface OperationTime {
  id: number;
  operationId: number;
  vehicleId: number | null;
  vehicleName: string;
  leaderCount: number;
  memberCount: number;
  alarmTime: string | null;
  departureTime: string | null;
  arrivalTime: string | null;
  returnTime: string | null;
}

export type PersonnelSection = 'deployed' | 'reinforcement';

export interface OperationPersonnel {
  id: number;
  operationId: number;
  memberId: number;
  vehicleName: string;
  function: string;
  section: PersonnelSection;
  member: { id: number; firstName: string; lastName: string };
}

/** Reduced member entry for pickers (no contact or health data) */
export interface MemberOption {
  id: number;
  firstName: string;
  lastName: string;
  rank: string | null;
}
