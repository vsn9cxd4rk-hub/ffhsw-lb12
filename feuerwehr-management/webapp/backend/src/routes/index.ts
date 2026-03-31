import { Router } from 'express';
import authRoutes from './auth.routes';
import userRoutes from './user.routes';
import memberRoutes from './member.routes';
import vehicleRoutes from './vehicle.routes';
import inventoryRoutes from './inventory.routes';
import operationRoutes from './operation.routes';
import eventRoutes from './event.routes';
import trainingRoutes from './training.routes';
import settingsRoutes from './settings.routes';
import dashboardRoutes from './dashboard.routes';
import inspectionRoutes from './inspection.routes';

const router = Router();

router.use('/auth', authRoutes);
router.use('/users', userRoutes);
router.use('/members', memberRoutes);
router.use('/vehicles', vehicleRoutes);
router.use('/inventory', inventoryRoutes);
router.use('/operations', operationRoutes);
router.use('/events', eventRoutes);
router.use('/training', trainingRoutes);
router.use('/settings', settingsRoutes);
router.use('/dashboard', dashboardRoutes);
router.use('/inspections', inspectionRoutes);

export default router;
