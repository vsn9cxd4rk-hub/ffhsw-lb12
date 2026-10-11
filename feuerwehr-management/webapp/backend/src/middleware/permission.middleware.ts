import { Request, Response, NextFunction } from 'express';
import { sendError } from '../utils/response';

export function requireAdmin(req: Request, res: Response, next: NextFunction): void {
  if (!req.user?.isAdmin) {
    sendError(res, 'Administratorrechte erforderlich', 403);
    return;
  }
  next();
}

export function requirePermission(bit: string) {
  return (req: Request, res: Response, next: NextFunction): void => {
    if (!req.user) {
      sendError(res, 'Nicht authentifiziert', 401);
      return;
    }
    if (req.user.isAdmin || req.user.permissions[bit]) {
      next();
      return;
    }
    sendError(res, 'Keine Berechtigung', 403);
  };
}

/** Like requirePermission, but any one of the bits is enough. */
export function requireAnyPermission(...bits: string[]) {
  return (req: Request, res: Response, next: NextFunction): void => {
    if (!req.user) {
      sendError(res, 'Nicht authentifiziert', 401);
      return;
    }
    if (req.user.isAdmin || bits.some((bit) => req.user!.permissions[bit])) {
      next();
      return;
    }
    sendError(res, 'Keine Berechtigung', 403);
  };
}

const READ_METHODS = new Set(['GET', 'HEAD', 'OPTIONS']);

/** Reading is open to every authenticated user; writing needs the bit (or admin). */
export function requirePermissionForWrite(bit: string) {
  const check = requirePermission(bit);
  return (req: Request, res: Response, next: NextFunction): void => {
    if (READ_METHODS.has(req.method)) {
      if (!req.user) { sendError(res, 'Nicht authentifiziert', 401); return; }
      next();
      return;
    }
    check(req, res, next);
  };
}
