import { Router, Request, Response } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requirePermission } from '../middleware/permission.middleware';
import { BIT_OPERATIONS } from '../config/permissionBits';
import {
  getOperations, createOperation, getOperation, updateOperation, deleteOperation,
  createOperationTime, updateOperationTime, deleteOperationTime,
  getOperationReport, upsertOperationReport,
  getDocuments, uploadDocument, uploadMiddleware, downloadDocument, deleteDocument,
  getPersonnel, addPersonnel, updatePersonnel, deletePersonnel,
} from '../controllers/operation.controller';
import { generateOperationReport, generatePersonnelSheet } from '../services/report-generator.service';
import { sendSuccess, sendError } from '../utils/response';

const router = Router();
router.use(authenticate);
router.use(requirePermission(BIT_OPERATIONS));

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
router.get('/:id/documents', getDocuments);
router.post('/:id/documents', uploadMiddleware, uploadDocument);
router.get('/:id/documents/:docId/download', downloadDocument);
router.delete('/:id/documents/:docId', deleteDocument);

// Personnel
router.get('/:id/personnel', getPersonnel);
router.post('/:id/personnel', addPersonnel);
router.put('/:id/personnel/:personnelId', updatePersonnel);
router.delete('/:id/personnel/:personnelId', deletePersonnel);

// Report generation
router.post('/:id/generate-report', async (req: Request, res: Response) => {
  try {
    const result = await generateOperationReport(parseInt(req.params.id));
    sendSuccess(res, result, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
});

router.post('/:id/generate-personnel-sheet', async (req: Request, res: Response) => {
  try {
    const { vehicleFilter } = req.body;
    const result = await generatePersonnelSheet(parseInt(req.params.id), vehicleFilter);
    sendSuccess(res, result, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
});

export default router;
