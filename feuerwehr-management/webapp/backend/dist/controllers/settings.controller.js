"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.templateUpload = void 0;
exports.getSettings = getSettings;
exports.updateSettings = updateSettings;
exports.getRanks = getRanks;
exports.createRank = createRank;
exports.updateRank = updateRank;
exports.deleteRank = deleteRank;
exports.getYears = getYears;
exports.createYear = createYear;
exports.updateYear = updateYear;
exports.getTemplates = getTemplates;
exports.uploadTemplate = uploadTemplate;
exports.updateTemplate = updateTemplate;
exports.downloadTemplate = downloadTemplate;
exports.deleteTemplate = deleteTemplate;
exports.getTemplateHistory = getTemplateHistory;
exports.getDeviceClasses = getDeviceClasses;
exports.createDeviceClass = createDeviceClass;
exports.updateDeviceClass = updateDeviceClass;
exports.deleteDeviceClass = deleteDeviceClass;
exports.createSubclass = createSubclass;
exports.updateSubclass = updateSubclass;
exports.deleteSubclass = deleteSubclass;
exports.createCriterion = createCriterion;
exports.updateCriterion = updateCriterion;
exports.deleteCriterion = deleteCriterion;
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
const multer_1 = __importDefault(require("multer"));
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const uploadDir = process.env.UPLOAD_PATH || './uploads';
const templateStorage = multer_1.default.diskStorage({
    destination: (_req, _file, cb) => {
        const dir = path_1.default.join(uploadDir, 'templates');
        fs_1.default.mkdirSync(dir, { recursive: true });
        cb(null, dir);
    },
    filename: (_req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});
exports.templateUpload = (0, multer_1.default)({
    storage: templateStorage,
    limits: { fileSize: 20 * 1024 * 1024 },
    fileFilter: (_req, file, cb) => {
        const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
        if (allowed.includes(file.mimetype))
            cb(null, true);
        else
            cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
    },
}).single('file');
async function getSettings(_req, res) {
    try {
        const settings = await database_1.prisma.setting.findMany({ orderBy: { key: 'asc' } });
        const map = Object.fromEntries(settings.map(s => [s.key, s.value]));
        (0, response_1.sendSuccess)(res, map);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateSettings(req, res) {
    try {
        const updates = req.body;
        await Promise.all(Object.entries(updates).map(([key, value]) => database_1.prisma.setting.upsert({
            where: { key },
            update: { value: String(value) },
            create: { key, value: String(value) },
        })));
        (0, response_1.sendSuccess)(res, { message: 'Einstellungen gespeichert' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Ranks
async function getRanks(_req, res) {
    try {
        const ranks = await database_1.prisma.rank.findMany({ orderBy: { sortOrder: 'asc' } });
        (0, response_1.sendSuccess)(res, ranks);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createRank(req, res) {
    try {
        const rank = await database_1.prisma.rank.create({ data: req.body });
        (0, response_1.sendSuccess)(res, rank, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateRank(req, res) {
    try {
        const rank = await database_1.prisma.rank.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, rank);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteRank(req, res) {
    try {
        await database_1.prisma.rank.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Dienstgrad gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Years
async function getYears(_req, res) {
    try {
        const years = await database_1.prisma.year.findMany({ orderBy: { year: 'desc' } });
        (0, response_1.sendSuccess)(res, years);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createYear(req, res) {
    try {
        const y = await database_1.prisma.year.create({ data: req.body });
        (0, response_1.sendSuccess)(res, y, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateYear(req, res) {
    try {
        const id = parseInt(req.params.id);
        if (req.body.isActive) {
            await database_1.prisma.year.updateMany({ where: { id: { not: id } }, data: { isActive: false } });
        }
        const y = await database_1.prisma.year.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, y);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Templates
async function getTemplates(_req, res) {
    try {
        const templates = await database_1.prisma.template.findMany({ orderBy: { name: 'asc' } });
        (0, response_1.sendSuccess)(res, templates);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function uploadTemplate(req, res) {
    try {
        if (!req.file) {
            (0, response_1.sendError)(res, 'Keine Datei hochgeladen', 400);
            return;
        }
        const name = req.body.name?.trim();
        if (!name) {
            (0, response_1.sendError)(res, 'Name ist erforderlich', 400);
            return;
        }
        const template = await database_1.prisma.template.create({
            data: {
                name,
                filePath: req.file.path,
                fileSize: req.file.size,
                mimeType: req.file.mimetype,
                createdBy: req.user?.username || 'System',
            },
        });
        await database_1.prisma.templateHistory.create({
            data: { templateId: template.id, action: 'Erstellt', changedBy: req.user?.username || 'System' },
        });
        (0, response_1.sendSuccess)(res, template, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateTemplate(req, res) {
    try {
        const id = parseInt(req.params.id);
        const existing = await database_1.prisma.template.findUnique({ where: { id } });
        if (!existing) {
            (0, response_1.sendError)(res, 'Template nicht gefunden', 404);
            return;
        }
        const data = { updatedBy: req.user?.username || 'System' };
        if (req.body.name)
            data.name = req.body.name.trim();
        if (req.file) {
            if (fs_1.default.existsSync(existing.filePath))
                fs_1.default.unlinkSync(existing.filePath);
            data.filePath = req.file.path;
            data.fileSize = req.file.size;
            data.mimeType = req.file.mimetype;
        }
        const template = await database_1.prisma.template.update({ where: { id }, data });
        await database_1.prisma.templateHistory.create({
            data: { templateId: id, action: req.file ? 'Datei ersetzt' : 'Aktualisiert', changedBy: req.user?.username || 'System' },
        });
        (0, response_1.sendSuccess)(res, template);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function downloadTemplate(req, res) {
    try {
        const template = await database_1.prisma.template.findUnique({ where: { id: parseInt(req.params.id) } });
        if (!template) {
            (0, response_1.sendError)(res, 'Template nicht gefunden', 404);
            return;
        }
        if (!fs_1.default.existsSync(template.filePath)) {
            (0, response_1.sendError)(res, 'Datei nicht gefunden', 404);
            return;
        }
        res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(template.name + path_1.default.extname(template.filePath))}"`);
        res.setHeader('Content-Type', template.mimeType);
        fs_1.default.createReadStream(template.filePath).pipe(res);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteTemplate(req, res) {
    try {
        const template = await database_1.prisma.template.findUnique({ where: { id: parseInt(req.params.id) } });
        if (!template) {
            (0, response_1.sendError)(res, 'Template nicht gefunden', 404);
            return;
        }
        if (fs_1.default.existsSync(template.filePath))
            fs_1.default.unlinkSync(template.filePath);
        await database_1.prisma.template.delete({ where: { id: template.id } });
        (0, response_1.sendSuccess)(res, { message: 'Template gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getTemplateHistory(req, res) {
    try {
        const history = await database_1.prisma.templateHistory.findMany({
            where: { templateId: parseInt(req.params.id) },
            orderBy: { changedAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, history);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Device Classes / Geräteklassen
async function getDeviceClasses(_req, res) {
    try {
        const classes = await database_1.prisma.deviceClass.findMany({
            orderBy: { sortOrder: 'asc' },
            include: {
                subclasses: {
                    orderBy: { sortOrder: 'asc' },
                    include: {
                        criteria: { orderBy: { sortOrder: 'asc' } },
                    },
                },
            },
        });
        (0, response_1.sendSuccess)(res, classes);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createDeviceClass(req, res) {
    try {
        const dc = await database_1.prisma.deviceClass.create({ data: { name: req.body.name, sortOrder: req.body.sortOrder || 0 } });
        (0, response_1.sendSuccess)(res, dc, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateDeviceClass(req, res) {
    try {
        const dc = await database_1.prisma.deviceClass.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, dc);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteDeviceClass(req, res) {
    try {
        await database_1.prisma.deviceClass.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Geräteklasse gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createSubclass(req, res) {
    try {
        const classId = parseInt(req.params.classId);
        const sc = await database_1.prisma.deviceSubclass.create({
            data: { deviceClassId: classId, name: req.body.name, sortOrder: req.body.sortOrder || 0 },
        });
        (0, response_1.sendSuccess)(res, sc, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateSubclass(req, res) {
    try {
        const sc = await database_1.prisma.deviceSubclass.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, sc);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteSubclass(req, res) {
    try {
        await database_1.prisma.deviceSubclass.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Unterklasse gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createCriterion(req, res) {
    try {
        const subclassId = parseInt(req.params.subclassId);
        const criterion = await database_1.prisma.inspectionCriterion.create({
            data: { deviceSubclassId: subclassId, name: req.body.name, sortOrder: req.body.sortOrder || 0 },
        });
        (0, response_1.sendSuccess)(res, criterion, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateCriterion(req, res) {
    try {
        const criterion = await database_1.prisma.inspectionCriterion.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, criterion);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteCriterion(req, res) {
    try {
        await database_1.prisma.inspectionCriterion.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Prüfkriterium gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=settings.controller.js.map