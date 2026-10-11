import bcrypt from 'bcryptjs';
import { prisma } from '../config/database';
import {
  signAccessToken,
  getRefreshTokenExpiry,
  JwtPayload,
} from '../config/jwt';
import crypto from 'crypto';

type UserWithGroup = NonNullable<Awaited<ReturnType<typeof findUserWithGroup>>>;

function findUserWithGroup(where: { id: number } | { username: string }) {
  return prisma.user.findUnique({ where, include: { group: true } });
}

// Refresh tokens are opaque random values; only their SHA-256 is stored so a DB dump or
// backup does not contain usable session tokens.
export function hashRefreshToken(token: string): string {
  return crypto.createHash('sha256').update(token).digest('hex');
}

export function buildPermissions(group: UserWithGroup['group']): Record<string, boolean> {
  const permissions: Record<string, boolean> = {};
  if (group) {
    for (let i = 0; i <= 75; i++) {
      permissions[`br${i}`] = (group as Record<string, unknown>)[`br${i}`] as boolean ?? false;
    }
  }
  return permissions;
}

function buildSession(user: UserWithGroup) {
  const payload: JwtPayload = {
    userId: user.id,
    username: user.username,
    isAdmin: user.isAdmin,
    groupId: user.groupId,
  };

  return {
    accessToken: signAccessToken(payload),
    user: {
      id: user.id,
      username: user.username,
      email: user.email,
      name: user.name,
      isAdmin: user.isAdmin,
      groupId: user.groupId,
      memberId: user.memberId,
      mustChangePassword: user.mustChangePassword,
      permissions: buildPermissions(user.group),
    },
  };
}

async function issueRefreshToken(userId: number): Promise<string> {
  const value = crypto.randomBytes(64).toString('hex');
  await prisma.refreshToken.create({
    data: {
      token: hashRefreshToken(value),
      userId,
      expiresAt: getRefreshTokenExpiry(),
    },
  });
  return value;
}

export class AuthService {
  async login(username: string, password: string) {
    const user = await findUserWithGroup({ username });

    if (!user || !user.isActive) {
      throw new Error('Ungültiger Benutzername oder Passwort');
    }

    const isValid = await bcrypt.compare(password, user.password);
    if (!isValid) {
      throw new Error('Ungültiger Benutzername oder Passwort');
    }

    // Each login is its own session (several devices per user are fine);
    // expired sessions of this user are cleaned up on the way.
    await prisma.refreshToken.deleteMany({ where: { userId: user.id, expiresAt: { lt: new Date() } } });
    const refreshToken = await issueRefreshToken(user.id);

    return { ...buildSession(user), refreshToken };
  }

  // The refresh token is not rotated: all tabs of a browser share the cookie and may refresh
  // concurrently, which stays harmless this way. Sessions end via logout, password change,
  // admin password reset, deactivation or expiry.
  async refreshToken(token: string) {
    const stored = await prisma.refreshToken.findUnique({
      where: { token: hashRefreshToken(token) },
    });

    const now = new Date();
    if (!stored || stored.expiresAt < now) {
      if (stored) {
        await prisma.refreshToken.delete({ where: { id: stored.id } });
      }
      throw new Error('Refresh token ungültig oder abgelaufen');
    }

    // Reload the user so a page reload/refresh always carries current rights.
    const user = await findUserWithGroup({ id: stored.userId });
    if (!user || !user.isActive) {
      await prisma.refreshToken.deleteMany({ where: { userId: stored.userId } });
      throw new Error('Benutzer deaktiviert');
    }

    return buildSession(user);
  }

  async logout(token: string) {
    await prisma.refreshToken.deleteMany({
      where: { token: hashRefreshToken(token) },
    });
  }

  async logoutAll(userId: number) {
    await prisma.refreshToken.deleteMany({ where: { userId } });
  }

  async changePassword(userId: number, oldPassword: string, newPassword: string) {
    const user = await prisma.user.findUnique({ where: { id: userId } });
    if (!user) throw new Error('Benutzer nicht gefunden');

    const isValid = await bcrypt.compare(oldPassword, user.password);
    if (!isValid) throw new Error('Aktuelles Passwort ist falsch');
    if (oldPassword === newPassword) throw new Error('Das neue Passwort muss sich vom bisherigen unterscheiden');

    const hashed = await bcrypt.hash(newPassword, 12);
    await prisma.user.update({
      where: { id: userId },
      data: { password: hashed, mustChangePassword: false },
    });

    // Revoke all refresh tokens
    await this.logoutAll(userId);
  }

  async hashPassword(password: string): Promise<string> {
    return bcrypt.hash(password, 12);
  }
}

export const authService = new AuthService();
