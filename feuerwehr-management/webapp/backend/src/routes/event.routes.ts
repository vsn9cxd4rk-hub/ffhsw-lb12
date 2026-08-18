import { Router, Request, Response } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getEvents, createEvent, getEvent, updateEvent, deleteEvent,
  getAttendance, updateAttendance,
  getFireWatches, createFireWatch, getFireWatch, updateFireWatch, deleteFireWatch,
  getEventDocuments, uploadEventDocument, eventDocUpload, downloadEventDocument, deleteEventDocument,
} from '../controllers/event.controller';
import { generateBswChecklist, generateBswReport, generateExerciseAttendance } from '../services/report-generator.service';
import { sendSuccess, sendError } from '../utils/response';

const router = Router();
router.use(authenticate);

router.get('/firewatches', getFireWatches);
router.post('/firewatches', createFireWatch);
router.get('/firewatches/:id', getFireWatch);
router.put('/firewatches/:id', updateFireWatch);
router.delete('/firewatches/:id', deleteFireWatch);

router.get('/', getEvents);
router.post('/', createEvent);
router.get('/:id', getEvent);
router.put('/:id', updateEvent);
router.delete('/:id', deleteEvent);
router.get('/:id/attendance', getAttendance);
router.post('/:id/attendance', updateAttendance);
router.get('/:id/documents', getEventDocuments);
router.post('/:id/documents', eventDocUpload, uploadEventDocument);
router.get('/:id/documents/:docId/download', downloadEventDocument);
router.delete('/:id/documents/:docId', deleteEventDocument);

// Brandsicherheitswache (Kategorie 3): Checkliste/Bericht aus Formulardaten generieren
router.post('/:id/bsw/checkliste', async (req: Request, res: Response) => {
  try {
    const result = await generateBswChecklist(parseInt(req.params.id));
    sendSuccess(res, result, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
});

router.post('/:id/bsw/bericht', async (req: Request, res: Response) => {
  try {
    const result = await generateBswReport(parseInt(req.params.id));
    sendSuccess(res, result, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
});

// Übung (Kategorie 5): Nachweis Übungsteilnahme aus Anwesenheit generieren
router.post('/:id/uebung/besuch', async (req: Request, res: Response) => {
  try {
    const result = await generateExerciseAttendance(parseInt(req.params.id));
    sendSuccess(res, result, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
});

export default router;
