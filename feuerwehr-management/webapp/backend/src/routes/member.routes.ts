import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requireAdmin, requireAnyPermission } from '../middleware/permission.middleware';
import { BIT_OPERATIONS, BIT_TRAINING } from '../config/permissionBits';
import {
  getMembers, createMember, getMember, updateMember, deleteMember, getMemberHistory,
  createMemberFamily, updateMemberFamily, deleteMemberFamily,
  upsertMemberWork, upsertMemberBank, upsertMemberExamination,
  createMemberAvailability,
  getMemberCourses, createMemberCourse, updateMemberCourse,
  getMemberGroups, createMemberGroup, updateMemberGroup,
  getAgtRecords, createAgtRecord, deleteAgtRecord,
  createMemberAbsence, updateMemberAbsence, deleteMemberAbsence,
} from '../controllers/member.controller';

const router = Router();

router.use(authenticate);

// Read access: the member list is needed for the Einsätze/Kräftenachweis personnel picker
// (see OperationDetailPage). For non-admins getMembers returns only name, rank, group and
// qualifications (PICKER_SELECT) – no contact, birth, health or licence data.
// Full member records (incl. medical exam dates, IBAN, family contacts) stay admin-only.
// (also used by the Lehrgang form, hence BIT_TRAINING)
router.get('/groups', requireAnyPermission(BIT_OPERATIONS, BIT_TRAINING), getMemberGroups);
router.get('/', requireAnyPermission(BIT_OPERATIONS, BIT_TRAINING), getMembers);
router.get('/:id', requireAdmin, getMember);
router.get('/:id/history', requireAdmin, getMemberHistory);
router.get('/:id/courses', requireAdmin, getMemberCourses);

// Write access: Admins only
router.post('/groups', requireAdmin, createMemberGroup);
router.put('/groups/:groupId', requireAdmin, updateMemberGroup);
router.post('/', requireAdmin, createMember);
router.put('/:id', requireAdmin, updateMember);
router.delete('/:id', requireAdmin, deleteMember);

router.post('/:id/family', requireAdmin, createMemberFamily);
router.put('/:id/family/:familyId', requireAdmin, updateMemberFamily);
router.delete('/:id/family/:familyId', requireAdmin, deleteMemberFamily);

router.put('/:id/work', requireAdmin, upsertMemberWork);
router.put('/:id/bank', requireAdmin, upsertMemberBank);
router.put('/:id/examination', requireAdmin, upsertMemberExamination);
router.post('/:id/availability', requireAdmin, createMemberAvailability);

router.post('/:id/courses', requireAdmin, createMemberCourse);
router.put('/:id/courses/:courseId', requireAdmin, updateMemberCourse);

// AGT Records
router.get('/:id/agt-records', requireAdmin, getAgtRecords);
router.post('/:id/agt-records', requireAdmin, createAgtRecord);
router.delete('/:id/agt-records/:recordId', requireAdmin, deleteAgtRecord);

// Absences
router.post('/:id/absences', requireAdmin, createMemberAbsence);
router.put('/:id/absences/:absenceId', requireAdmin, updateMemberAbsence);
router.delete('/:id/absences/:absenceId', requireAdmin, deleteMemberAbsence);

export default router;
