import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getOperations, createOperation, getOperation, updateOperation, deleteOperation,
  createOperationTime, updateOperationTime, deleteOperationTime,
  getOperationReport, upsertOperationReport,
<<<<<<< HEAD
=======
  getDocuments, uploadDocument, uploadMiddleware, downloadDocument, deleteDocument,
>>>>>>> a9dc7840 (Added New FW Management system)
} from '../controllers/operation.controller';

const router = Router();
router.use(authenticate);

router.get('/', getOperations);
router.post('/', createOperation);
router.get('/:id', getOperation);
router.put('/:id', updateOperation);
router.delete('/:id', deleteOperation);
router.post('/:id/times', createOperationTime);
router.put('/:id/times/:timeId', updateOperationTime);
router.delete('/:id/times/:timeId', deleteOperationTime);
router.get('/:id/report', getOperationReport);
router.put('/:id/report', upsertOperationReport);
<<<<<<< HEAD
=======
router.get('/:id/documents', getDocuments);
router.post('/:id/documents', uploadMiddleware, uploadDocument);
router.get('/:id/documents/:docId/download', downloadDocument);
router.delete('/:id/documents/:docId', deleteDocument);
>>>>>>> a9dc7840 (Added New FW Management system)

export default router;
