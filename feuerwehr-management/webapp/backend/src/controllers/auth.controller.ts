import { Request, Response, CookieOptions } from 'express';
import { body } from 'express-validator';
import { authService } from '../services/auth.service';
import { sendSuccess, sendError } from '../utils/response';
import { getRefreshTokenExpiry } from '../config/jwt';
import { prisma } from '../config/database';
import { passwordRules } from '../utils/password';

const COOKIE_PATH = '/api/auth';

// `secure` follows the actual request scheme (nginx sets X-Forwarded-Proto, trust proxy is on):
// a Secure cookie is silently dropped by the browser on plain-HTTP LAN installs, which would
// make every session end with the first access-token expiry. COOKIE_SECURE=true forces it.
function refreshCookieOptions(req: Request): CookieOptions {
  return {
    httpOnly: true,
    secure: process.env.COOKIE_SECURE === 'true' || req.secure,
    sameSite: 'lax',
    expires: getRefreshTokenExpiry(),
    path: COOKIE_PATH,
  };
}

function clearRefreshCookie(req: Request, res: Response): void {
  const { expires: _expires, ...options } = refreshCookieOptions(req);
  res.clearCookie('refreshToken', options);
}

export const loginValidation = [
  body('username').trim().notEmpty().withMessage('Benutzername ist erforderlich'),
  body('password').notEmpty().withMessage('Passwort ist erforderlich'),
];

export async function login(req: Request, res: Response): Promise<void> {
  try {
    const { username, password } = req.body;
    const result = await authService.login(username, password);

    res.cookie('refreshToken', result.refreshToken, refreshCookieOptions(req));
    sendSuccess(res, {
      accessToken: result.accessToken,
      user: result.user,
    });
  } catch (err) {
    sendError(res, (err as Error).message, 401);
  }
}

// Works without a valid access token: logging out after the access token expired must still
// revoke the session, otherwise the refresh token stays usable for its full lifetime.
export async function logout(req: Request, res: Response): Promise<void> {
  try {
    const refreshToken = req.cookies.refreshToken;
    if (refreshToken) {
      await authService.logout(refreshToken);
    }
    clearRefreshCookie(req, res);
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

    // Also returns the current user so clients pick up changed rights without a new login.
    const session = await authService.refreshToken(refreshToken);
    // Same token, new expiry: the cookie follows the extended session
    res.cookie('refreshToken', refreshToken, refreshCookieOptions(req));
    sendSuccess(res, session);
  } catch (err) {
    clearRefreshCookie(req, res);
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
        mustChangePassword: true,
        groupId: true,
        memberId: true,
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
  passwordRules(body('newPassword')),
];

export async function changePassword(req: Request, res: Response): Promise<void> {
  try {
    const { oldPassword, newPassword } = req.body;
    await authService.changePassword(req.user!.id, oldPassword, newPassword);
    clearRefreshCookie(req, res);
    sendSuccess(res, { message: 'Passwort erfolgreich geändert. Bitte neu anmelden.' });
  } catch (err) {
    sendError(res, (err as Error).message, 400);
  }
}
