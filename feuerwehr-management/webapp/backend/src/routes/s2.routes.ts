import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requirePermission } from '../middleware/permission.middleware';
import { BIT_S2 } from '../config/permissionBits';
import {
  getLagen, createLage, getLage, updateLage, deleteLage,
  createBericht, getBericht, updateBericht, deleteBericht, getPruefung, exportLagebericht, exportLagekarte,
  createItem, updateItem, deleteItem, geoLookup,
} from '../controllers/s2.controller';

const router = Router();
router.use(authenticate);
// The whole module (also reading) is reserved for holders of the S2 permission (Gruppenführer)
router.use(requirePermission(BIT_S2));

router.get('/geo', geoLookup);

router.get('/lagen', getLagen);
router.post('/lagen', createLage);
router.get('/lagen/:id', getLage);
router.put('/lagen/:id', updateLage);
router.delete('/lagen/:id', deleteLage);

router.post('/lagen/:id/berichte', createBericht);
router.get('/berichte/:id', getBericht);
router.put('/berichte/:id', updateBericht);
router.delete('/berichte/:id', deleteBericht);
router.get('/berichte/:id/pruefung', getPruefung);
router.get('/berichte/:id/lagebericht.xlsx', exportLagebericht);
router.get('/berichte/:id/lagekarte.:format', exportLagekarte);

// abschnitte | kraefte | objekte | meldungen
router.post('/lagen/:id/:kind', createItem);
router.put('/:kind/:id', updateItem);
router.delete('/:kind/:id', deleteItem);

export default router;
