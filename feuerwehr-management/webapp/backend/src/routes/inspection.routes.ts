import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requirePermission } from '../middleware/permission.middleware';
import { BIT_EQUIPMENT } from '../config/permissionBits';
import {
  getInspections,
  getDueInspections,
  getArticleInspections,
  getInspectionCriteria,
  getInspectionReport,
  createInspection,
  updateInspection,
  getInspectionDocuments,
  inspectionDocUpload,
  uploadInspectionDocument,
  downloadInspectionDocument,
  deleteInspectionDocument,
} from '../controllers/inspection.controller';

const router = Router();

router.use(authenticate);
router.use(requirePermission(BIT_EQUIPMENT));

router.get('/', getInspections);
router.get('/due', getDueInspections);
router.get('/report', getInspectionReport);
router.get('/criteria/:articleId', getInspectionCriteria);
router.get('/article/:articleId', getArticleInspections);
router.post('/', createInspection);
router.put('/:id', updateInspection);

// Inspection documents
router.get('/:id/documents', getInspectionDocuments);
router.post('/:id/documents', inspectionDocUpload, uploadInspectionDocument);
router.get('/documents/:docId/download', downloadInspectionDocument);
router.delete('/documents/:docId', deleteInspectionDocument);

export default router;
