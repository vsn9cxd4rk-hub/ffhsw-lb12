import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getInspections,
  getDueInspections,
  getArticleInspections,
<<<<<<< HEAD
=======
  getInspectionCriteria,
  getInspectionReport,
>>>>>>> a9dc7840 (Added New FW Management system)
  createInspection,
  updateInspection,
} from '../controllers/inspection.controller';

const router = Router();

router.use(authenticate);

router.get('/', getInspections);
router.get('/due', getDueInspections);
<<<<<<< HEAD
=======
router.get('/report', getInspectionReport);
router.get('/criteria/:articleId', getInspectionCriteria);
>>>>>>> a9dc7840 (Added New FW Management system)
router.get('/article/:articleId', getArticleInspections);
router.post('/', createInspection);
router.put('/:id', updateInspection);

export default router;
