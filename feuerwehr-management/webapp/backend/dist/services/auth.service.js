"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.authService = exports.AuthService = void 0;
const bcryptjs_1 = __importDefault(require("bcryptjs"));
const database_1 = require("../config/database");
const jwt_1 = require("../config/jwt");
const crypto_1 = __importDefault(require("crypto"));
class AuthService {
    async login(username, password) {
        const user = await database_1.prisma.user.findUnique({
            where: { username },
            include: { group: true },
        });
        if (!user || !user.isActive) {
            throw new Error('Ungültiger Benutzername oder Passwort');
        }
        const isValid = await bcryptjs_1.default.compare(password, user.password);
        if (!isValid) {
            throw new Error('Ungültiger Benutzername oder Passwort');
        }
        const payload = {
            userId: user.id,
            username: user.username,
            isAdmin: user.isAdmin,
            groupId: user.groupId,
        };
        const accessToken = (0, jwt_1.signAccessToken)(payload);
        const refreshTokenValue = crypto_1.default.randomBytes(64).toString('hex');
        await database_1.prisma.refreshToken.create({
            data: {
                token: refreshTokenValue,
                userId: user.id,
                expiresAt: (0, jwt_1.getRefreshTokenExpiry)(),
            },
        });
        // Build permissions
        const permissions = {};
        if (user.group) {
            for (let i = 0; i <= 75; i++) {
                permissions[`br${i}`] = user.group[`br${i}`] ?? false;
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
    async refreshToken(token) {
        const stored = await database_1.prisma.refreshToken.findUnique({
            where: { token },
            include: { user: { include: { group: true } } },
        });
        if (!stored || stored.expiresAt < new Date()) {
            if (stored) {
                await database_1.prisma.refreshToken.delete({ where: { id: stored.id } });
            }
            throw new Error('Refresh token ungültig oder abgelaufen');
        }
        if (!stored.user.isActive) {
            throw new Error('Benutzer deaktiviert');
        }
        const payload = {
            userId: stored.user.id,
            username: stored.user.username,
            isAdmin: stored.user.isAdmin,
            groupId: stored.user.groupId,
        };
        const accessToken = (0, jwt_1.signAccessToken)(payload);
        return { accessToken };
    }
    async logout(userId, token) {
        await database_1.prisma.refreshToken.deleteMany({
            where: { userId, token },
        });
    }
    async logoutAll(userId) {
        await database_1.prisma.refreshToken.deleteMany({ where: { userId } });
    }
    async changePassword(userId, oldPassword, newPassword) {
        const user = await database_1.prisma.user.findUnique({ where: { id: userId } });
        if (!user)
            throw new Error('Benutzer nicht gefunden');
        const isValid = await bcryptjs_1.default.compare(oldPassword, user.password);
        if (!isValid)
            throw new Error('Aktuelles Passwort ist falsch');
        const hashed = await bcryptjs_1.default.hash(newPassword, 12);
        await database_1.prisma.user.update({
            where: { id: userId },
            data: { password: hashed },
        });
        // Revoke all refresh tokens
        await this.logoutAll(userId);
    }
    async hashPassword(password) {
        return bcryptjs_1.default.hash(password, 12);
    }
}
exports.AuthService = AuthService;
exports.authService = new AuthService();
//# sourceMappingURL=auth.service.js.map