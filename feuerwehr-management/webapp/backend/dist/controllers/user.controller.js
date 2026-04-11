"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createUserValidation = void 0;
exports.getUsers = getUsers;
exports.createUser = createUser;
exports.getUser = getUser;
exports.updateUser = updateUser;
exports.deleteUser = deleteUser;
exports.getPermissionGroups = getPermissionGroups;
exports.createPermissionGroup = createPermissionGroup;
exports.updatePermissionGroup = updatePermissionGroup;
exports.deletePermissionGroup = deletePermissionGroup;
const express_validator_1 = require("express-validator");
const database_1 = require("../config/database");
const auth_service_1 = require("../services/auth.service");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
exports.createUserValidation = [
    (0, express_validator_1.body)('username').trim().isLength({ min: 3 }).withMessage('Benutzername min. 3 Zeichen'),
    (0, express_validator_1.body)('password').isLength({ min: 8 }).withMessage('Passwort min. 8 Zeichen'),
    (0, express_validator_1.body)('email').optional().isEmail().withMessage('Ungültige E-Mail-Adresse'),
];
async function getUsers(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const search = req.query.search;
        const where = search
            ? { OR: [{ username: { contains: search } }, { name: { contains: search } }, { email: { contains: search } }] }
            : {};
        const [users, total] = await Promise.all([
            database_1.prisma.user.findMany({
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
            database_1.prisma.user.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, users, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createUser(req, res) {
    try {
        const { username, email, password, name, isAdmin, isActive, groupId } = req.body;
        const existing = await database_1.prisma.user.findFirst({
            where: { OR: [{ username }, ...(email ? [{ email }] : [])] },
        });
        if (existing) {
            (0, response_1.sendError)(res, 'Benutzername oder E-Mail bereits vergeben', 409);
            return;
        }
        if (!groupId) {
            (0, response_1.sendError)(res, 'Berechtigungsgruppe ist erforderlich', 400);
            return;
        }
        const hashed = await auth_service_1.authService.hashPassword(password);
        const user = await database_1.prisma.user.create({
            data: {
                username,
                email: email || null,
                password: hashed,
                name: name || null,
                isAdmin: isAdmin ?? false,
                isActive: isActive ?? true,
                groupId,
            },
            select: {
                id: true, username: true, email: true, name: true,
                isAdmin: true, isActive: true, groupId: true, createdAt: true,
            },
        });
        (0, response_1.sendSuccess)(res, user, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getUser(req, res) {
    try {
        const user = await database_1.prisma.user.findUnique({
            where: { id: parseInt(req.params.id) },
            select: {
                id: true, username: true, email: true, name: true,
                isAdmin: true, isActive: true, groupId: true,
                group: true, createdAt: true,
            },
        });
        if (!user) {
            (0, response_1.sendError)(res, 'Benutzer nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, user);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateUser(req, res) {
    try {
        const id = parseInt(req.params.id);
        const { email, name, isAdmin, isActive, groupId, password } = req.body;
        const data = {};
        if (email !== undefined)
            data.email = email;
        if (name !== undefined)
            data.name = name;
        if (isAdmin !== undefined)
            data.isAdmin = isAdmin;
        if (isActive !== undefined)
            data.isActive = isActive;
        if (groupId !== undefined) {
            if (!groupId) {
                (0, response_1.sendError)(res, 'Berechtigungsgruppe ist erforderlich', 400);
                return;
            }
            data.groupId = groupId;
        }
        if (password)
            data.password = await auth_service_1.authService.hashPassword(password);
        const user = await database_1.prisma.user.update({
            where: { id },
            data,
            select: {
                id: true, username: true, email: true, name: true,
                isAdmin: true, isActive: true, groupId: true, createdAt: true,
            },
        });
        (0, response_1.sendSuccess)(res, user);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteUser(req, res) {
    try {
        const id = parseInt(req.params.id);
        if (id === req.user.id) {
            (0, response_1.sendError)(res, 'Sie können sich nicht selbst löschen', 400);
            return;
        }
        await database_1.prisma.user.delete({ where: { id } });
        (0, response_1.sendSuccess)(res, { message: 'Benutzer gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Permission groups
async function getPermissionGroups(_req, res) {
    try {
        const groups = await database_1.prisma.permissionGroup.findMany({ orderBy: { name: 'asc' } });
        (0, response_1.sendSuccess)(res, groups);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createPermissionGroup(req, res) {
    try {
        const group = await database_1.prisma.permissionGroup.create({ data: req.body });
        (0, response_1.sendSuccess)(res, group, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updatePermissionGroup(req, res) {
    try {
        const id = parseInt(req.params.id);
        const group = await database_1.prisma.permissionGroup.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, group);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deletePermissionGroup(req, res) {
    try {
        const id = parseInt(req.params.id);
        await database_1.prisma.permissionGroup.delete({ where: { id } });
        (0, response_1.sendSuccess)(res, { message: 'Gruppe gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=user.controller.js.map