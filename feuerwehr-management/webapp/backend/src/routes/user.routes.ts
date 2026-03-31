import { Router } from 'express';
import { authenticate } from '../middleware/auth.middleware';
import { requireAdmin } from '../middleware/permission.middleware';
import {
  getUsers, createUser, getUser, updateUser, deleteUser,
  getPermissionGroups, createPermissionGroup, updatePermissionGroup, deletePermissionGroup,
  createUserValidation,
} from '../controllers/user.controller';
import { validate } from '../middleware/validate.middleware';

const router = Router();

router.use(authenticate);

router.get('/', requireAdmin, getUsers);
router.post('/', requireAdmin, createUserValidation, validate, createUser);
router.get('/groups', getPermissionGroups);
router.post('/groups', requireAdmin, createPermissionGroup);
router.put('/groups/:id', requireAdmin, updatePermissionGroup);
router.delete('/groups/:id', requireAdmin, deletePermissionGroup);
router.get('/:id', requireAdmin, getUser);
router.put('/:id', requireAdmin, updateUser);
router.delete('/:id', requireAdmin, deleteUser);

export default router;
