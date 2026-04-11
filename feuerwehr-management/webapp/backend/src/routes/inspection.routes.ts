import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getInspections,
  getDueInspections,
  getArticleInspections,
  getInspectionCriteria,
  getInspectionReport,
  createInspection,
  updateInspection,
} from '../controllers/inspection.controller';

const router = Router();

router.use(authenticate);

router.get('/', getInspections);
router.get('/due', getDueInspections);
router.get('/report', getInspectionReport);
router.get('/criteria/:articleId', getInspectionCriteria);
router.get('/article/:articleId', getArticleInspections);
router.post('/', createInspection);
router.put('/:id', updateInspection);

export default router;
