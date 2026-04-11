"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getWarehouses = getWarehouses;
exports.createWarehouse = createWarehouse;
exports.updateWarehouse = updateWarehouse;
exports.deleteWarehouse = deleteWarehouse;
exports.getArticles = getArticles;
exports.createArticle = createArticle;
exports.getArticle = getArticle;
exports.updateArticle = updateArticle;
exports.deleteArticle = deleteArticle;
exports.assignArticle = assignArticle;
exports.updateAssignment = updateAssignment;
exports.deleteAssignment = deleteAssignment;
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
// Warehouses
async function getWarehouses(req, res) {
    try {
        const vehicleId = req.query.vehicleId ? parseInt(req.query.vehicleId) : undefined;
        // Auto-create warehouse entries for vehicles that don't have one yet
        if (vehicleId === undefined) {
            const vehiclesWithoutWarehouse = await database_1.prisma.vehicle.findMany({
                where: {
                    isRetired: false,
                    warehouses: { none: {} },
                },
                select: { id: true, name: true },
            });
            if (vehiclesWithoutWarehouse.length > 0) {
                await database_1.prisma.warehouse.createMany({
                    data: vehiclesWithoutWarehouse.map(v => ({ name: v.name, vehicleId: v.id })),
                });
            }
        }
        const warehouses = await database_1.prisma.warehouse.findMany({
            where: vehicleId !== undefined ? { vehicleId } : {},
            include: { vehicle: { select: { id: true, name: true } } },
            orderBy: { name: 'asc' },
        });
        (0, response_1.sendSuccess)(res, warehouses);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createWarehouse(req, res) {
    try {
        const w = await database_1.prisma.warehouse.create({ data: req.body });
        (0, response_1.sendSuccess)(res, w, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateWarehouse(req, res) {
    try {
        const w = await database_1.prisma.warehouse.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, w);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteWarehouse(req, res) {
    try {
        await database_1.prisma.warehouse.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Lager gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Articles
async function getArticles(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const search = req.query.search;
        const warehouseId = req.query.warehouseId ? parseInt(req.query.warehouseId) : undefined;
        const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId) : undefined;
        const deviceSubclassId = req.query.deviceSubclassId ? parseInt(req.query.deviceSubclassId) : undefined;
        const where = {};
        if (search) {
            where.OR = [
                { name: { contains: search } },
                { manufacturer: { contains: search } },
                { ean: { contains: search } },
                { inventoryNumber: { contains: search } },
            ];
        }
        if (warehouseId !== undefined) {
            where.warehouseId = warehouseId;
        }
        if (deviceSubclassId !== undefined) {
            where.deviceSubclassId = deviceSubclassId;
        }
        else if (deviceClassId !== undefined) {
            where.deviceSubclass = { deviceClassId };
        }
        const [articles, total] = await Promise.all([
            database_1.prisma.article.findMany({
                where,
                skip,
                take,
                include: {
                    warehouse: true,
                    deviceSubclass: { include: { deviceClass: true } },
                    assignments: {
                        include: { warehouse: true },
                    },
                },
                orderBy: { name: 'asc' },
            }),
            database_1.prisma.article.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, articles, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createArticle(req, res) {
    try {
        const { deviceSubclassId, manufacturingDate, specification, serialNumber, din, isDecommissioned, ...rest } = req.body;
        const article = await database_1.prisma.article.create({
            data: {
                ...rest,
                deviceSubclassId: deviceSubclassId || null,
                manufacturingDate: manufacturingDate ? new Date(manufacturingDate) : null,
                specification: specification || null,
                serialNumber: serialNumber || null,
                din: din || null,
                isDecommissioned: isDecommissioned || false,
            },
            include: { warehouse: true, deviceSubclass: { include: { deviceClass: true } } },
        });
        (0, response_1.sendSuccess)(res, article, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getArticle(req, res) {
    try {
        const article = await database_1.prisma.article.findUnique({
            where: { id: parseInt(req.params.id) },
            include: {
                warehouse: true,
                deviceSubclass: { include: { deviceClass: true } },
                assignments: { include: { warehouse: true } },
            },
        });
        if (!article) {
            (0, response_1.sendError)(res, 'Artikel nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, article);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateArticle(req, res) {
    try {
        const { deviceSubclassId, manufacturingDate, ...rest } = req.body;
        const data = { ...rest };
        if (deviceSubclassId !== undefined)
            data.deviceSubclassId = deviceSubclassId || null;
        if (manufacturingDate !== undefined)
            data.manufacturingDate = manufacturingDate ? new Date(manufacturingDate) : null;
        const article = await database_1.prisma.article.update({
            where: { id: parseInt(req.params.id) },
            data,
            include: { warehouse: true, deviceSubclass: { include: { deviceClass: true } } },
        });
        (0, response_1.sendSuccess)(res, article);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteArticle(req, res) {
    try {
        await database_1.prisma.article.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Artikel gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Assignments
async function assignArticle(req, res) {
    try {
        const articleId = parseInt(req.params.id);
        const assignment = await database_1.prisma.articleAssignment.create({
            data: { ...req.body, articleId },
            include: { warehouse: true },
        });
        (0, response_1.sendSuccess)(res, assignment, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateAssignment(req, res) {
    try {
        const a = await database_1.prisma.articleAssignment.update({
            where: { id: parseInt(req.params.assignId) },
            data: req.body,
            include: { warehouse: true },
        });
        (0, response_1.sendSuccess)(res, a);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteAssignment(req, res) {
    try {
        await database_1.prisma.articleAssignment.delete({ where: { id: parseInt(req.params.assignId) } });
        (0, response_1.sendSuccess)(res, { message: 'Zuweisung gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=inventory.controller.js.map