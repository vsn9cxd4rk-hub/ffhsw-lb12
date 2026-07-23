import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requirePermission } from '../middleware/permission.middleware';
import { BIT_OPERATIONS } from '../config/permissionBits';
import { getDashboardStats, getStatistics, getNotifications } from '../controllers/dashboard.controller';

const router = Router();
router.use(authenticate);

router.get('/stats', getDashboardStats);
router.get('/notifications', getNotifications);
router.get('/statistics/:type', requirePermission(BIT_OPERATIONS), getStatistics);

export default router;
