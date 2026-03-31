import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getMembers, createMember, getMember, updateMember, deleteMember, getMemberHistory,
  createMemberFamily, updateMemberFamily, deleteMemberFamily,
  upsertMemberWork, upsertMemberBank, upsertMemberExamination,
  createMemberAvailability,
  getMemberCourses, createMemberCourse, updateMemberCourse,
  getMemberGroups, createMemberGroup, updateMemberGroup,
} from '../controllers/member.controller';

const router = Router();

router.use(authenticate);

router.get('/groups', getMemberGroups);
router.post('/groups', createMemberGroup);
router.put('/groups/:groupId', updateMemberGroup);

router.get('/', getMembers);
router.post('/', createMember);
router.get('/:id', getMember);
router.put('/:id', updateMember);
router.delete('/:id', deleteMember);
router.get('/:id/history', getMemberHistory);

router.post('/:id/family', createMemberFamily);
router.put('/:id/family/:familyId', updateMemberFamily);
router.delete('/:id/family/:familyId', deleteMemberFamily);

router.put('/:id/work', upsertMemberWork);
router.put('/:id/bank', upsertMemberBank);
router.put('/:id/examination', upsertMemberExamination);
router.post('/:id/availability', createMemberAvailability);

router.get('/:id/courses', getMemberCourses);
router.post('/:id/courses', createMemberCourse);
router.put('/:id/courses/:courseId', updateMemberCourse);

export default router;
