import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import {
  getVehicles, createVehicle, getVehicle, updateVehicle, deleteVehicle,
  upsertVehicleInspection,
  getLogbook, createLogbookEntry, updateLogbookEntry,
  getEquipmentInspections, createEquipmentInspection, updateEquipmentInspection,
} from '../controllers/vehicle.controller';

const router = Router();

router.use(authenticate);

router.get('/', getVehicles);
router.post('/', createVehicle);
router.get('/:id', getVehicle);
router.put('/:id', updateVehicle);
router.delete('/:id', deleteVehicle);
router.put('/:id/inspection', upsertVehicleInspection);
router.get('/:id/logbook', getLogbook);
router.post('/:id/logbook', createLogbookEntry);
router.put('/:id/logbook/:entryId', updateLogbookEntry);
router.get('/:id/equipment-inspections', getEquipmentInspections);
router.post('/:id/equipment-inspections', createEquipmentInspection);
router.put('/:id/equipment-inspections/:inspId', updateEquipmentInspection);

export default router;
