import bcrypt from 'bcryptjs';
import { prisma } from '../config/database';
import {
  signAccessToken,
  signRefreshToken,
  verifyRefreshToken,
  getRefreshTokenExpiry,
  JwtPayload,
} from '../config/jwt';
import crypto from 'crypto';

export class AuthService {
  async login(username: string, password: string) {
    const user = await prisma.user.findUnique({
      where: { username },
      include: { group: true },
    });

    if (!user || !user.isActive) {
      throw new Error('Ungültiger Benutzername oder Passwort');
    }

    const isValid = await bcrypt.compare(password, user.password);
    if (!isValid) {
      throw new Error('Ungültiger Benutzername oder Passwort');
    }

    const payload: JwtPayload = {
      userId: user.id,
      username: user.username,
      isAdmin: user.isAdmin,
      groupId: user.groupId,
    };

    const accessToken = signAccessToken(payload);
    const refreshTokenValue = crypto.randomBytes(64).toString('hex');

    await prisma.refreshToken.create({
      data: {
        token: refreshTokenValue,
        userId: user.id,
        expiresAt: getRefreshTokenExpiry(),
      },
    });

    // Build permissions
    const permissions: Record<string, boolean> = {};
    if (user.group) {
      for (let i = 0; i <= 75; i++) {
        permissions[`br${i}`] = (user.group as Record<string, unknown>)[`br${i}`] as boolean ?? false;
      }
    }

    return {
      accessToken,
      refreshToken: refreshTokenValue,
      user: {
        id: user.id,
        username: user.username,
        email: user.email,
        name: user.name,
        isAdmin: user.isAdmin,
        groupId: user.groupId,
        permissions,
      },
    };
  }

  async refreshToken(token: string) {
    const stored = await prisma.refreshToken.findUnique({
      where: { token },
      include: { user: { include: { group: true } } },
    });

    if (!stored || stored.expiresAt < new Date()) {
      if (stored) {
        await prisma.refreshToken.delete({ where: { id: stored.id } });
      }
      throw new Error('Refresh token ungültig oder abgelaufen');
    }

    if (!stored.user.isActive) {
      throw new Error('Benutzer deaktiviert');
    }

    const payload: JwtPayload = {
      userId: stored.user.id,
      username: stored.user.username,
      isAdmin: stored.user.isAdmin,
      groupId: stored.user.groupId,
    };

    const accessToken = signAccessToken(payload);
    return { accessToken };
  }

  async logout(userId: number, token: string) {
    await prisma.refreshToken.deleteMany({
      where: { userId, token },
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

    const hashed = await bcrypt.hash(newPassword, 12);
    await prisma.user.update({
      where: { id: userId },
      data: { password: hashed },
    });

    // Revoke all refresh tokens
    await this.logoutAll(userId);
  }

  async hashPassword(password: string): Promise<string> {
    return bcrypt.hash(password, 12);
  }
}

export const authService = new AuthService();
