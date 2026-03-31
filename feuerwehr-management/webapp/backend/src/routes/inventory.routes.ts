import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getWarehouses, createWarehouse, updateWarehouse, deleteWarehouse,
  getArticles, createArticle, getArticle, updateArticle, deleteArticle,
  assignArticle, updateAssignment, deleteAssignment,
} from '../controllers/inventory.controller';

const router = Router();
router.use(authenticate);

router.get('/warehouses', getWarehouses);
router.post('/warehouses', createWarehouse);
router.put('/warehouses/:id', updateWarehouse);
router.delete('/warehouses/:id', deleteWarehouse);

router.get('/articles', getArticles);
router.post('/articles', createArticle);
router.get('/articles/:id', getArticle);
router.put('/articles/:id', updateArticle);
router.delete('/articles/:id', deleteArticle);
router.post('/articles/:id/assign', assignArticle);
router.put('/articles/:id/assignments/:assignId', updateAssignment);
router.delete('/articles/:id/assignments/:assignId', deleteAssignment);

export default router;
