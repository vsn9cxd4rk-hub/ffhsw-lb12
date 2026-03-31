export interface User {
  id: number;
  username: string;
  email: string | null;
  name: string | null;
  isAdmin: boolean;
  isActive: boolean;
  groupId: number | null;
  group?: PermissionGroup;
  permissions: Record<string, boolean>;
  createdAt: string;
}

export interface PermissionGroup {
  id: number;
  name: string;
  description: string | null;
  [key: string]: boolean | number | string | null;
}

export interface MemberGroup {
  id: number;
  name: string;
  nextEmployeeNumber: number;
}

export interface Member {
  id: number;
  groupId: number | null;
  group?: MemberGroup;
  salutation: string | null;
  lastName: string;
  firstName: string;
  street: string | null;
  city: string | null;
  phonePrivate: string | null;
  phoneMobile: string | null;
  phoneWork: string | null;
  telegramId: string | null;
  email: string | null;
  email2: string | null;
  occupation: string | null;
  nationality: string | null;
  rank: string | null;
  isInactive: boolean;
  memberSince: string | null;
  memberUntil: string | null;
  birthDate: string | null;
  marriageDate: string | null;
  comment: string | null;
  driverLicenseNo: string | null;
  serviceCardNo: string | null;
  healthInsurance: string | null;
  medications: string | null;
  conditions: string | null;
  qualLicenseC: boolean;
  qualLicenseB: boolean;
  qualFirstAid: boolean;
  qualRadioOperator: boolean;
  qualMachinist: boolean;
  qualTruppmann: boolean;
  qualTruppfuehrer: boolean;
  qualGruppenfuehrer: boolean;
  qualZugfuehrer: boolean;
  qualRettSan: boolean;
  qualFwSan: boolean;
  qualVerbandfuehrer: boolean;
  qualAGT: boolean;
  qualTH1: boolean;
  family?: MemberFamily[];
  work?: MemberWork | null;
  bank?: MemberBank | null;
  examination?: MemberExamination | null;
  courses?: Course[];
  createdAt: string;
  updatedAt: string;
}

export interface MemberFamily {
  id: number;
  memberId: number;
  name: string;
  phone: string | null;
  phone2: string | null;
  email: string | null;
  street: string | null;
  city: string | null;
  relationship: string | null;
}

export interface MemberWork {
  id: number;
  memberId: number;
  employer: string | null;
  street: string | null;
  city: string | null;
  phone: string | null;
  contactPerson: string | null;
  email: string | null;
}

export interface MemberBank {
  id: number;
  memberId: number;
  iban: string | null;
  bic: string | null;
}

export interface MemberExamination {
  id: number;
  memberId: number;
  g25Date: string | null;
  g26Date: string | null;
  g30Date: string | null;
  agtTrainingDate: string | null;
  lkwLicenseExpiry: string | null;
}

export interface Vehicle {
  id: number;
  name: string;
  description: string | null;
  licensePlate: string | null;
  callSign: string | null;
  seats: number | null;
  minCrew: number | null;
  maxCrew: number | null;
  licenseClass: string | null;
  isRetired: boolean;
  isTrailer: boolean;
  sortOrder: number;
  inspection?: VehicleInspection | null;
  equipmentInspections?: EquipmentInspection[];
  warehouses?: Warehouse[];
}

export interface VehicleInspection {
  id: number;
  vehicleId: number;
  tuevDate: string | null;
  spDate: string | null;
  serviceDate: string | null;
  notifyTuev: boolean;
  notifySp: boolean;
  notifyService: boolean;
  notes: string | null;
}

export interface EquipmentInspection {
  id: number;
  vehicleId: number;
  type: string;
  lastInspection: string | null;
  nextInspection: string | null;
  notes: string | null;
  notifyDue: boolean;
}

export interface ArticleInspection {
  id: number;
  articleId: number;
  inspectedAt: string;
  inspectedBy: string;
  result: string;
  notes: string | null;
  nextDueDate: string | null;
  createdAt: string;
  article?: Article;
}

export interface LogbookEntry {
  id: number;
  vehicleId: number;
  date: string;
  driver: string;
  startMileage: number;
  endMileage: number;
  purpose: string;
  notes: string | null;
}

export interface Warehouse {
  id: number;
  name: string;
  description: string | null;
  vehicleId: number | null;
  vehicle?: Pick<Vehicle, 'id' | 'name'> | null;
}

export interface Article {
  id: number;
  name: string;
  manufacturer: string | null;
  articleType: string | null;
  description: string | null;
  inspectionInterval: number | null;
  value: number | null;
  ean: string | null;
  isExtinguisher: boolean;
  inventoryNumber: string | null;
  warehouseId: number | null;
  warehouse?: Warehouse | null;
  assignments?: ArticleAssignment[];
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

export interface Operation {
  id: number;
  operationNumber: string | null;
  officialNumber: string | null;
  date: string;
  alarmTime: string | null;
  departureTime: string | null;
  arrivalTime: string | null;
  returnTime: string | null;
  location: string;
  district: string | null;
  keyword: string | null;
  vehicles: string | null;
  description: string | null;
  leaderCount: number;
  memberCount: number;
  times?: OperationTime[];
  reports?: OperationReport[];
}

export interface OperationTime {
  id: number;
  operationId: number;
  vehicleId: number | null;
  vehicleName: string;
  alarmTime: string | null;
  departureTime: string | null;
  arrivalTime: string | null;
  returnTime: string | null;
}

export interface OperationReport {
  id: number;
  operationId: number;
  content: string;
  createdBy: string;
  createdAt: string;
}

export interface Event {
  id: number;
  name: string;
  name2: string | null;
  category: number;
  date: string;
  startTime: string | null;
  endTime: string | null;
  hasVehicleAssignment: boolean;
  infoSent: boolean;
  notes: string | null;
  attendances?: EventAttendance[];
}

export interface EventAttendance {
  memberId: number;
  member: Pick<Member, 'id' | 'firstName' | 'lastName' | 'rank'>;
  status: string | null;
}

export interface FireWatch {
  id: number;
  name: string;
  date: string;
  startTime: string | null;
  endTime: string | null;
  location: string | null;
  notes: string | null;
}

export interface CourseCategory {
  id: number;
  name: string;
  description: string | null;
}

export interface Course {
  id: number;
  memberId: number;
  categoryId: number;
  status: string;
  startDate: string | null;
  endDate: string | null;
  location: string | null;
  notes: string | null;
  category?: CourseCategory;
  member?: Pick<Member, 'id' | 'firstName' | 'lastName' | 'rank'>;
}

export interface Rank {
  id: number;
  name: string;
  abbreviation: string;
  sortOrder: number;
}

export interface DashboardStats {
  activeMembers: number;
  vehicles: number;
  operationsThisYear: number;
  recentOperations: Array<{ id: number; date: string; location: string; keyword: string | null; leaderCount: number; memberCount: number }>;
  upcomingInspections: Array<{ type: string; entityName: string; dueDate: string }>;
  upcomingMedicalExams: Array<{ memberName: string; examType: string; dueDate: string }>;
}

export interface PaginatedResponse<T> {
  data: T[];
  pagination: {
    total: number;
    page: number;
    limit: number;
    pages: number;
  };
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  error?: string;
}
