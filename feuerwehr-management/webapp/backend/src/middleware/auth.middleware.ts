import { Request, Response, NextFunction } from 'express';
import { verifyAccessToken } from '../config/jwt';
import { prisma } from '../config/database';
import { sendError } from '../utils/response';

export async function authenticate(req: Request, res: Response, next: NextFunction): Promise<void> {
  try {
    const authHeader = req.headers.authorization;
    if (!authHeader?.startsWith('Bearer ')) {
      sendError(res, 'Nicht authentifiziert', 401);
      return;
    }

    const token = authHeader.substring(7);
    const payload = verifyAccessToken(token);

    const user = await prisma.user.findUnique({
      where: { id: payload.userId },
      include: { group: true },
    });

    if (!user || !user.isActive) {
      sendError(res, 'Benutzer nicht gefunden oder deaktiviert', 401);
      return;
    }

    // Build permissions map from group
    const permissions: Record<string, boolean> = {};
    if (user.group) {
      for (let i = 0; i <= 75; i++) {
        permissions[`br${i}`] = (user.group as Record<string, unknown>)[`br${i}`] as boolean ?? false;
      }
    }

    req.user = {
      id: user.id,
      username: user.username,
      email: user.email,
      name: user.name,
      isAdmin: user.isAdmin,
      groupId: user.groupId,
      memberId: user.memberId,
      permissions,
    };

    next();
  } catch {
    sendError(res, 'Ungültiger oder abgelaufener Token', 401);
  }
}
