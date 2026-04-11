import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getCourses, createCourse, getCourse, updateCourse, deleteCourse,
  uploadCertificate, certificateUpload, downloadCertificate, deleteCertificate,
  getCategories, createCategory, updateCategory, deleteCategory,
} from '../controllers/training.controller';

const router = Router();
router.use(authenticate);

router.get('/categories', getCategories);
router.post('/categories', createCategory);
router.put('/categories/:id', updateCategory);
router.delete('/categories/:id', deleteCategory);

router.get('/courses', getCourses);
router.post('/courses', createCourse);
router.get('/courses/:id', getCourse);
router.put('/courses/:id', updateCourse);
router.delete('/courses/:id', deleteCourse);
router.post('/courses/:id/certificate', certificateUpload, uploadCertificate);
router.get('/courses/:id/certificate', downloadCertificate);
router.delete('/courses/:id/certificate', deleteCertificate);

export default router;
