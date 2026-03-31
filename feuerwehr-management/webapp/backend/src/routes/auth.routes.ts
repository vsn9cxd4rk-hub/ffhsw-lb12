import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { validate } from '../middleware/validate.middleware';
import {
  login, logout, refresh, getMe, changePassword,
  loginValidation, changePasswordValidation,
} from '../controllers/auth.controller';

const router = Router();

router.post('/login', loginValidation, validate, login);
router.post('/logout', authenticate, logout);
router.post('/refresh', refresh);
router.get('/me', authenticate, getMe);
router.put('/change-password', authenticate, changePasswordValidation, validate, changePassword);

export default router;
