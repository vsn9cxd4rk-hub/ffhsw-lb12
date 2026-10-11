import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requireAdmin, requirePermissionForWrite } from '../middleware/permission.middleware';
import { BIT_TRAINING } from '../config/permissionBits';
import {
  getCourses, createCourse, getCourse, updateCourse, deleteCourse,
  uploadCertificate, certificateUpload, downloadCertificate, deleteCertificate,
  getCategories, createCategory, updateCategory, deleteCategory,
} from '../controllers/training.controller';

const router = Router();
router.use(authenticate);

// Categories are master data maintained under Einstellungen (admin only); reading stays open
router.get('/categories', getCategories);
router.post('/categories', requireAdmin, createCategory);
router.put('/categories/:id', requireAdmin, updateCategory);
router.delete('/categories/:id', requireAdmin, deleteCategory);

// Everyone may view courses/certificates; changes need BIT_TRAINING
router.use(requirePermissionForWrite(BIT_TRAINING));

router.get('/courses', getCourses);
router.post('/courses', createCourse);
router.get('/courses/:id', getCourse);
router.put('/courses/:id', updateCourse);
router.delete('/courses/:id', deleteCourse);
router.post('/courses/:id/certificate', certificateUpload, uploadCertificate);
router.get('/courses/:id/certificate', downloadCertificate);
router.delete('/courses/:id/certificate', deleteCertificate);

export default router;
