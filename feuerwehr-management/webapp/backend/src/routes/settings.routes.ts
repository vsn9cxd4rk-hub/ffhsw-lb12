import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requireAdmin } from '../middleware/permission.middleware';
import {
  getSettings, updateSettings,
  getRanks, createRank, updateRank, deleteRank,
  getYears, createYear, updateYear,
  getTemplates, uploadTemplate, templateUpload, updateTemplate, downloadTemplate, deleteTemplate, getTemplateHistory,
  getDeviceClasses, createDeviceClass, updateDeviceClass, deleteDeviceClass,
  createSubclass, updateSubclass, deleteSubclass,
  createCriterion, updateCriterion, deleteCriterion,
  importArticles, importInspections,
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

router.get('/templates', getTemplates);
router.post('/templates', requireAdmin, templateUpload, uploadTemplate);
router.put('/templates/:id', requireAdmin, templateUpload, updateTemplate);
router.get('/templates/:id/download', downloadTemplate);
router.delete('/templates/:id', requireAdmin, deleteTemplate);
router.get('/templates/:id/history', getTemplateHistory);

// Device Classes / Geräteklassen
router.get('/device-classes', getDeviceClasses);
router.post('/device-classes', requireAdmin, createDeviceClass);
router.put('/device-classes/:id', requireAdmin, updateDeviceClass);
router.delete('/device-classes/:id', requireAdmin, deleteDeviceClass);
router.post('/device-classes/:classId/subclasses', requireAdmin, createSubclass);
router.put('/device-subclasses/:id', requireAdmin, updateSubclass);
router.delete('/device-subclasses/:id', requireAdmin, deleteSubclass);
router.post('/device-subclasses/:subclassId/criteria', requireAdmin, createCriterion);
router.put('/inspection-criteria/:id', requireAdmin, updateCriterion);
router.delete('/inspection-criteria/:id', requireAdmin, deleteCriterion);

// CSV Import
router.post('/import/articles', requireAdmin, importArticles);
router.post('/import/inspections', requireAdmin, importInspections);

export default router;
