import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requireAdmin } from '../middleware/permission.middleware';
import {
  getSettings, updateSettings,
  getRanks, createRank, updateRank, deleteRank,
  getYears, createYear, updateYear,
} from '../controllers/settings.controller';

const router = Router();
router.use(authenticate);

router.get('/', getSettings);
router.put('/', requireAdmin, updateSettings);

router.get('/ranks', getRanks);
router.post('/ranks', requireAdmin, createRank);
router.put('/ranks/:id', requireAdmin, updateRank);
router.delete('/ranks/:id', requireAdmin, deleteRank);

router.get('/years', getYears);
router.post('/years', requireAdmin, createYear);
router.put('/years/:id', requireAdmin, updateYear);

export default router;
