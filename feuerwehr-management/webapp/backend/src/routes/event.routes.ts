import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getEvents, createEvent, getEvent, updateEvent, deleteEvent,
  getAttendance, updateAttendance,
  getFireWatches, createFireWatch, getFireWatch, updateFireWatch, deleteFireWatch,
<<<<<<< HEAD
=======
  getEventDocuments, uploadEventDocument, eventDocUpload, downloadEventDocument, deleteEventDocument,
>>>>>>> a9dc7840 (Added New FW Management system)
} from '../controllers/event.controller';

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
<<<<<<< HEAD
=======
router.get('/:id/documents', getEventDocuments);
router.post('/:id/documents', eventDocUpload, uploadEventDocument);
router.get('/:id/documents/:docId/download', downloadEventDocument);
router.delete('/:id/documents/:docId', deleteEventDocument);
>>>>>>> a9dc7840 (Added New FW Management system)

export default router;
