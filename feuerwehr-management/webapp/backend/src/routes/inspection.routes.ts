import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getInspections,
  getDueInspections,
  getArticleInspections,
  createInspection,
  updateInspection,
} from '../controllers/inspection.controller';

const router = Router();

router.use(authenticate);

router.get('/', getInspections);
router.get('/due', getDueInspections);
router.get('/article/:articleId', getArticleInspections);
router.post('/', createInspection);
router.put('/:id', updateInspection);

export default router;
