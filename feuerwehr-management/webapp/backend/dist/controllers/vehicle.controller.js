"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getVehicles = getVehicles;
exports.createVehicle = createVehicle;
exports.getVehicle = getVehicle;
exports.updateVehicle = updateVehicle;
exports.deleteVehicle = deleteVehicle;
exports.upsertVehicleInspection = upsertVehicleInspection;
exports.getLogbook = getLogbook;
exports.createLogbookEntry = createLogbookEntry;
exports.updateLogbookEntry = updateLogbookEntry;
exports.getEquipmentInspections = getEquipmentInspections;
exports.createEquipmentInspection = createEquipmentInspection;
exports.updateEquipmentInspection = updateEquipmentInspection;
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
async function getVehicles(req, res) {
    try {
        const isRetired = req.query.isRetired === 'true' ? true : req.query.isRetired === 'false' ? false : undefined;
        const where = isRetired !== undefined ? { isRetired } : {};
        const vehicles = await database_1.prisma.vehicle.findMany({
            where,
            include: { inspection: true },
            orderBy: { sortOrder: 'asc' },
        });
        (0, response_1.sendSuccess)(res, vehicles);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createVehicle(req, res) {
    try {
        const vehicle = await database_1.prisma.vehicle.create({
            data: req.body,
            include: { inspection: true },
        });
        (0, response_1.sendSuccess)(res, vehicle, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getVehicle(req, res) {
    try {
        const id = parseInt(req.params.id);
        const vehicle = await database_1.prisma.vehicle.findUnique({
            where: { id },
            include: {
                inspection: true,
                equipmentInspections: true,
                warehouses: true,
                logbook: { orderBy: { date: 'desc' }, take: 20 },
            },
        });
        if (!vehicle) {
            (0, response_1.sendError)(res, 'Fahrzeug nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, vehicle);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateVehicle(req, res) {
    try {
        const id = parseInt(req.params.id);
        const vehicle = await database_1.prisma.vehicle.update({
            where: { id },
            data: req.body,
            include: { inspection: true },
        });
        (0, response_1.sendSuccess)(res, vehicle);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteVehicle(req, res) {
    try {
        await database_1.prisma.vehicle.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Fahrzeug gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function upsertVehicleInspection(req, res) {
    try {
        const vehicleId = parseInt(req.params.id);
        const inspection = await database_1.prisma.vehicleInspection.upsert({
            where: { vehicleId },
            update: req.body,
            create: { ...req.body, vehicleId },
        });
        (0, response_1.sendSuccess)(res, inspection);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getLogbook(req, res) {
    try {
        const vehicleId = parseInt(req.params.id);
        const page = parseInt(req.query.page) || 1;
        const limit = 20;
        const [entries, total] = await Promise.all([
            database_1.prisma.logbookEntry.findMany({
                where: { vehicleId },
                orderBy: { date: 'desc' },
                skip: (page - 1) * limit,
                take: limit,
            }),
            database_1.prisma.logbookEntry.count({ where: { vehicleId } }),
        ]);
        (0, response_1.sendSuccess)(res, { data: entries, total, page, pages: Math.ceil(total / limit) });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createLogbookEntry(req, res) {
    try {
        const vehicleId = parseInt(req.params.id);
        const entry = await database_1.prisma.logbookEntry.create({ data: { ...req.body, vehicleId } });
        (0, response_1.sendSuccess)(res, entry, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateLogbookEntry(req, res) {
    try {
        const id = parseInt(req.params.entryId);
        const entry = await database_1.prisma.logbookEntry.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, entry);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getEquipmentInspections(req, res) {
    try {
        const vehicleId = parseInt(req.params.id);
        const inspections = await database_1.prisma.equipmentInspection.findMany({
            where: { vehicleId },
            orderBy: { type: 'asc' },
        });
        (0, response_1.sendSuccess)(res, inspections);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createEquipmentInspection(req, res) {
    try {
        const vehicleId = parseInt(req.params.id);
        const inspection = await database_1.prisma.equipmentInspection.create({ data: { ...req.body, vehicleId } });
        (0, response_1.sendSuccess)(res, inspection, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateEquipmentInspection(req, res) {
    try {
        const id = parseInt(req.params.inspId);
        const inspection = await database_1.prisma.equipmentInspection.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, inspection);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=vehicle.controller.js.map