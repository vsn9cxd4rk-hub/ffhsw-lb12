"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.changePasswordValidation = exports.loginValidation = void 0;
exports.login = login;
exports.logout = logout;
exports.refresh = refresh;
exports.getMe = getMe;
exports.changePassword = changePassword;
const express_validator_1 = require("express-validator");
const auth_service_1 = require("../services/auth.service");
const response_1 = require("../utils/response");
const jwt_1 = require("../config/jwt");
const database_1 = require("../config/database");
const COOKIE_OPTIONS = {
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'lax',
    maxAge: 7 * 24 * 60 * 60 * 1000,
    path: '/api/auth',
};
exports.loginValidation = [
    (0, express_validator_1.body)('username').trim().notEmpty().withMessage('Benutzername ist erforderlich'),
    (0, express_validator_1.body)('password').notEmpty().withMessage('Passwort ist erforderlich'),
];
async function login(req, res) {
    try {
        const { username, password } = req.body;
        const result = await auth_service_1.authService.login(username, password);
        res.cookie('refreshToken', result.refreshToken, COOKIE_OPTIONS);
        (0, response_1.sendSuccess)(res, {
            accessToken: result.accessToken,
            user: result.user,
        });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message, 401);
    }
}
async function logout(req, res) {
    try {
        const refreshToken = req.cookies.refreshToken;
        if (refreshToken && req.user) {
            await auth_service_1.authService.logout(req.user.id, refreshToken);
        }
        res.clearCookie('refreshToken', { path: '/api/auth' });
        (0, response_1.sendSuccess)(res, { message: 'Erfolgreich abgemeldet' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function refresh(req, res) {
    try {
        const refreshToken = req.cookies.refreshToken;
        if (!refreshToken) {
            (0, response_1.sendError)(res, 'Kein Refresh Token', 401);
            return;
        }
        // Verify token format first
        try {
            (0, jwt_1.verifyRefreshToken)(refreshToken);
        }
        catch {
            res.clearCookie('refreshToken', { path: '/api/auth' });
            (0, response_1.sendError)(res, 'Ungültiger Refresh Token', 401);
            return;
        }
        const result = await auth_service_1.authService.refreshToken(refreshToken);
        (0, response_1.sendSuccess)(res, { accessToken: result.accessToken });
    }
    catch (err) {
        res.clearCookie('refreshToken', { path: '/api/auth' });
        (0, response_1.sendError)(res, err.message, 401);
    }
}
async function getMe(req, res) {
    try {
        const user = await database_1.prisma.user.findUnique({
            where: { id: req.user.id },
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
            (0, response_1.sendError)(res, 'Benutzer nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, { ...user, permissions: req.user.permissions });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
exports.changePasswordValidation = [
    (0, express_validator_1.body)('oldPassword').notEmpty().withMessage('Aktuelles Passwort ist erforderlich'),
    (0, express_validator_1.body)('newPassword')
        .isLength({ min: 8 })
        .withMessage('Neues Passwort muss mindestens 8 Zeichen haben')
        .matches(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/)
        .withMessage('Passwort muss Groß-/Kleinbuchstaben und eine Zahl enthalten'),
];
async function changePassword(req, res) {
    try {
        const { oldPassword, newPassword } = req.body;
        await auth_service_1.authService.changePassword(req.user.id, oldPassword, newPassword);
        res.clearCookie('refreshToken', { path: '/api/auth' });
        (0, response_1.sendSuccess)(res, { message: 'Passwort erfolgreich geändert. Bitte neu anmelden.' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message, 400);
    }
}
//# sourceMappingURL=auth.controller.js.map