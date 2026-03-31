import { Request, Response } from 'express';
import { body } from 'express-validator';
import { prisma } from '../config/database';
import { authService } from '../services/auth.service';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

export const createUserValidation = [
  body('username').trim().isLength({ min: 3 }).withMessage('Benutzername min. 3 Zeichen'),
  body('password').isLength({ min: 8 }).withMessage('Passwort min. 8 Zeichen'),
  body('email').optional().isEmail().withMessage('Ungültige E-Mail-Adresse'),
];

export async function getUsers(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const search = req.query.search as string;

    const where = search
      ? { OR: [{ username: { contains: search } }, { name: { contains: search } }, { email: { contains: search } }] }
      : {};

    const [users, total] = await Promise.all([
      prisma.user.findMany({
        where,
        skip,
        take,
        select: {
          id: true, username: true, email: true, name: true,
          isAdmin: true, isActive: true, groupId: true,
          group: { select: { id: true, name: true } },
          createdAt: true,
        },
        orderBy: { username: 'asc' },
      }),
      prisma.user.count({ where }),
    ]);

    sendPaginated(res, users, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createUser(req: Request, res: Response): Promise<void> {
  try {
    const { username, email, password, name, isAdmin, isActive, groupId } = req.body;

    const existing = await prisma.user.findFirst({
      where: { OR: [{ username }, ...(email ? [{ email }] : [])] },
    });
    if (existing) {
      sendError(res, 'Benutzername oder E-Mail bereits vergeben', 409);
      return;
    }

    const hashed = await authService.hashPassword(password);
    const user = await prisma.user.create({
      data: {
        username,
        email: email || null,
        password: hashed,
        name: name || null,
        isAdmin: isAdmin ?? false,
        isActive: isActive ?? true,
        groupId: groupId || null,
      },
      select: {
        id: true, username: true, email: true, name: true,
        isAdmin: true, isActive: true, groupId: true, createdAt: true,
      },
    });

    sendSuccess(res, user, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getUser(req: Request, res: Response): Promise<void> {
  try {
    const user = await prisma.user.findUnique({
      where: { id: parseInt(req.params.id) },
      select: {
        id: true, username: true, email: true, name: true,
        isAdmin: true, isActive: true, groupId: true,
        group: true, createdAt: true,
      },
    });
    if (!user) { sendError(res, 'Benutzer nicht gefunden', 404); return; }
    sendSuccess(res, user);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateUser(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const { email, name, isAdmin, isActive, groupId, password } = req.body;

    const data: Record<string, unknown> = {};
    if (email !== undefined) data.email = email;
    if (name !== undefined) data.name = name;
    if (isAdmin !== undefined) data.isAdmin = isAdmin;
    if (isActive !== undefined) data.isActive = isActive;
    if (groupId !== undefined) data.groupId = groupId;
    if (password) data.password = await authService.hashPassword(password);

    const user = await prisma.user.update({
      where: { id },
      data,
      select: {
        id: true, username: true, email: true, name: true,
        isAdmin: true, isActive: true, groupId: true, createdAt: true,
      },
    });
    sendSuccess(res, user);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteUser(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    if (id === req.user!.id) {
      sendError(res, 'Sie können sich nicht selbst löschen', 400);
      return;
    }
    await prisma.user.delete({ where: { id } });
    sendSuccess(res, { message: 'Benutzer gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Permission groups
export async function getPermissionGroups(_req: Request, res: Response): Promise<void> {
  try {
    const groups = await prisma.permissionGroup.findMany({ orderBy: { name: 'asc' } });
    sendSuccess(res, groups);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createPermissionGroup(req: Request, res: Response): Promise<void> {
  try {
    const group = await prisma.permissionGroup.create({ data: req.body });
    sendSuccess(res, group, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updatePermissionGroup(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const group = await prisma.permissionGroup.update({ where: { id }, data: req.body });
    sendSuccess(res, group);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deletePermissionGroup(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    await prisma.permissionGroup.delete({ where: { id } });
    sendSuccess(res, { message: 'Gruppe gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
