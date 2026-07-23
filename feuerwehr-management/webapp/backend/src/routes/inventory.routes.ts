import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requirePermission } from '../middleware/permission.middleware';
import { BIT_EQUIPMENT } from '../config/permissionBits';
import {
  getWarehouses, createWarehouse, updateWarehouse, deleteWarehouse,
  getNextInventoryNumber,
  getArticles, createArticle, getArticle, updateArticle, deleteArticle,
  assignArticle, updateAssignment, deleteAssignment,
  articleDocUpload, getArticleDocuments, uploadArticleDocument, downloadArticleDocument, deleteArticleDocument,
  getArticleStandards, createArticleStandard, updateArticleStandard, deleteArticleStandard,
  getArticleSchedules, createArticleSchedule, updateArticleSchedule, deleteArticleSchedule,
  getDefects, getDefect, createDefect, updateDefect, deleteDefect,
  getRepairs, createRepair, updateRepair, deleteRepair,
} from '../controllers/inventory.controller';

const router = Router();
router.use(authenticate);
router.use(requirePermission(BIT_EQUIPMENT));

router.get('/warehouses', getWarehouses);
router.post('/warehouses', createWarehouse);
router.put('/warehouses/:id', updateWarehouse);
router.delete('/warehouses/:id', deleteWarehouse);

router.get('/articles/next-number', getNextInventoryNumber);
router.get('/articles', getArticles);
router.post('/articles', createArticle);

// Article documents (static prefix routes before parametric :id)
router.get('/articles/documents/:docId/download', downloadArticleDocument);
router.delete('/articles/documents/:docId', deleteArticleDocument);

// Article inspection standards (static prefix routes before parametric :id)
router.put('/articles/standards/:stdId', updateArticleStandard);
router.delete('/articles/standards/:stdId', deleteArticleStandard);

// Article inspection schedules (static prefix routes before parametric :id)
router.put('/articles/schedules/:schedId', updateArticleSchedule);
router.delete('/articles/schedules/:schedId', deleteArticleSchedule);

router.get('/articles/:id', getArticle);
router.put('/articles/:id', updateArticle);
router.delete('/articles/:id', deleteArticle);
router.post('/articles/:id/assign', assignArticle);
router.put('/articles/:id/assignments/:assignId', updateAssignment);
router.delete('/articles/:id/assignments/:assignId', deleteAssignment);

// Article documents (nested under :id)
router.get('/articles/:id/documents', getArticleDocuments);
router.post('/articles/:id/documents', articleDocUpload, uploadArticleDocument);

// Article inspection standards (nested under :id)
router.get('/articles/:id/standards', getArticleStandards);
router.post('/articles/:id/standards', createArticleStandard);

// Article inspection schedules (nested under :id)
router.get('/articles/:id/schedules', getArticleSchedules);
router.post('/articles/:id/schedules', createArticleSchedule);

// Defects (Mängelmeldesystem)
router.get('/defects', getDefects);
router.get('/defects/:id', getDefect);
router.post('/defects', createDefect);
router.put('/defects/:id', updateDefect);
router.delete('/defects/:id', deleteDefect);

// Repairs (Reparaturdokumentation)
router.get('/repairs', getRepairs);
router.post('/repairs', createRepair);
router.put('/repairs/:id', updateRepair);
router.delete('/repairs/:id', deleteRepair);

export default router;
