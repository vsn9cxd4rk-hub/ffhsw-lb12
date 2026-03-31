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
