import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { getDashboardStats, getStatistics } from '../controllers/dashboard.controller';

const router = Router();
router.use(authenticate);

router.get('/stats', getDashboardStats);
router.get('/statistics/:type', getStatistics);

export default router;
