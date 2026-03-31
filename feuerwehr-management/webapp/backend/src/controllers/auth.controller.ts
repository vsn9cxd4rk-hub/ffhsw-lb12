import { Request, Response } from 'express';
import { body } from 'express-validator';
import { authService } from '../services/auth.service';
import { sendSuccess, sendError } from '../utils/response';
import { verifyRefreshToken } from '../config/jwt';
import { prisma } from '../config/database';

const COOKIE_OPTIONS = {
  httpOnly: true,
  secure: process.env.NODE_ENV === 'production',
  sameSite: 'lax' as const,
  maxAge: 7 * 24 * 60 * 60 * 1000,
  path: '/api/auth',
};

export const loginValidation = [
  body('username').trim().notEmpty().withMessage('Benutzername ist erforderlich'),
  body('password').notEmpty().withMessage('Passwort ist erforderlich'),
];

export async function login(req: Request, res: Response): Promise<void> {
  try {
    const { username, password } = req.body;
    const result = await authService.login(username, password);

    res.cookie('refreshToken', result.refreshToken, COOKIE_OPTIONS);
    sendSuccess(res, {
      accessToken: result.accessToken,
      user: result.user,
    });
  } catch (err) {
    sendError(res, (err as Error).message, 401);
  }
}

export async function logout(req: Request, res: Response): Promise<void> {
  try {
    const refreshToken = req.cookies.refreshToken;
    if (refreshToken && req.user) {
      await authService.logout(req.user.id, refreshToken);
    }
    res.clearCookie('refreshToken', { path: '/api/auth' });
    sendSuccess(res, { message: 'Erfolgreich abgemeldet' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function refresh(req: Request, res: Response): Promise<void> {
  try {
    const refreshToken = req.cookies.refreshToken;
    if (!refreshToken) {
      sendError(res, 'Kein Refresh Token', 401);
      return;
    }

    // Verify token format first
    try {
      verifyRefreshToken(refreshToken);
    } catch {
      res.clearCookie('refreshToken', { path: '/api/auth' });
      sendError(res, 'Ungültiger Refresh Token', 401);
      return;
    }

    const result = await authService.refreshToken(refreshToken);
    sendSuccess(res, { accessToken: result.accessToken });
  } catch (err) {
    res.clearCookie('refreshToken', { path: '/api/auth' });
    sendError(res, (err as Error).message, 401);
  }
}

export async function getMe(req: Request, res: Response): Promise<void> {
  try {
    const user = await prisma.user.findUnique({
      where: { id: req.user!.id },
      select: {
        id: true,
        username: true,
        email: true,
        name: true,
        isAdmin: true,
        isActive: true,
        groupId: true,
        group: true,
        createdAt: true,
      },
    });

    if (!user) {
      sendError(res, 'Benutzer nicht gefunden', 404);
      return;
    }

    sendSuccess(res, { ...user, permissions: req.user!.permissions });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export const changePasswordValidation = [
  body('oldPassword').notEmpty().withMessage('Aktuelles Passwort ist erforderlich'),
  body('newPassword')
    .isLength({ min: 8 })
    .withMessage('Neues Passwort muss mindestens 8 Zeichen haben')
    .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/)
    .withMessage('Passwort muss Groß-/Kleinbuchstaben und eine Zahl enthalten'),
];

export async function changePassword(req: Request, res: Response): Promise<void> {
  try {
    const { oldPassword, newPassword } = req.body;
    await authService.changePassword(req.user!.id, oldPassword, newPassword);
    res.clearCookie('refreshToken', { path: '/api/auth' });
    sendSuccess(res, { message: 'Passwort erfolgreich geändert. Bitte neu anmelden.' });
  } catch (err) {
    sendError(res, (err as Error).message, 400);
  }
}
