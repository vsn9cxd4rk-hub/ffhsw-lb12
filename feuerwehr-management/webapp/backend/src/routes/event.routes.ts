import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getEvents, createEvent, getEvent, updateEvent, deleteEvent,
  getAttendance, updateAttendance,
  getFireWatches, createFireWatch, getFireWatch, updateFireWatch, deleteFireWatch,
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

export default router;
