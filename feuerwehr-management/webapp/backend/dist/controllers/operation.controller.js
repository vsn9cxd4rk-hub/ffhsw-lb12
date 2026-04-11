"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.uploadMiddleware = void 0;
exports.getOperations = getOperations;
exports.createOperation = createOperation;
exports.getOperation = getOperation;
exports.updateOperation = updateOperation;
exports.deleteOperation = deleteOperation;
exports.createOperationTime = createOperationTime;
exports.updateOperationTime = updateOperationTime;
exports.deleteOperationTime = deleteOperationTime;
exports.getOperationReport = getOperationReport;
exports.upsertOperationReport = upsertOperationReport;
exports.getDocuments = getDocuments;
exports.uploadDocument = uploadDocument;
exports.downloadDocument = downloadDocument;
exports.deleteDocument = deleteDocument;
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
const multer_1 = __importDefault(require("multer"));
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
// Multer configuration for document uploads
const uploadDir = process.env.UPLOAD_PATH || './uploads';
const storage = multer_1.default.diskStorage({
    destination: (req, _file, cb) => {
        const dir = path_1.default.join(uploadDir, 'operations', req.params.id);
        fs_1.default.mkdirSync(dir, { recursive: true });
        cb(null, dir);
    },
    filename: (_req, file, cb) => {
        const uniqueName = `${Date.now()}-${file.originalname}`;
        cb(null, uniqueName);
    },
});
exports.uploadMiddleware = (0, multer_1.default)({
    storage,
    limits: { fileSize: 20 * 1024 * 1024 }, // 20MB
    fileFilter: (_req, file, cb) => {
        const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
        if (allowed.includes(file.mimetype)) {
            cb(null, true);
        }
        else {
            cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
        }
    },
}).single('file');
async function getOperations(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const { year, dateFrom, dateTo, keyword, location } = req.query;
        const where = {};
        if (year) {
            const y = parseInt(year);
            where.date = { gte: new Date(y, 0, 1), lt: new Date(y + 1, 0, 1) };
        }
        else if (dateFrom || dateTo) {
            const dateFilter = {};
            if (dateFrom)
                dateFilter.gte = new Date(dateFrom);
            if (dateTo)
                dateFilter.lte = new Date(dateTo);
            where.date = dateFilter;
        }
        if (keyword)
            where.keyword = { contains: keyword };
        if (location)
            where.location = { contains: location };
        const [ops, total] = await Promise.all([
            database_1.prisma.operation.findMany({
                where,
                skip,
                take,
                orderBy: [{ date: 'desc' }, { alarmTime: 'desc' }],
            }),
            database_1.prisma.operation.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, ops, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createOperation(req, res) {
    try {
        const op = await database_1.prisma.operation.create({ data: req.body });
        (0, response_1.sendSuccess)(res, op, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getOperation(req, res) {
    try {
        const id = parseInt(req.params.id);
        const op = await database_1.prisma.operation.findUnique({
            where: { id },
            include: { times: true, reports: true },
        });
        if (!op) {
            (0, response_1.sendError)(res, 'Einsatz nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, op);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateOperation(req, res) {
    try {
        const id = parseInt(req.params.id);
        const op = await database_1.prisma.operation.update({ where: { id }, data: req.body });
        (0, response_1.sendSuccess)(res, op);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteOperation(req, res) {
    try {
        await database_1.prisma.operation.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Einsatz gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Times
async function createOperationTime(req, res) {
    try {
        const operationId = parseInt(req.params.id);
        const t = await database_1.prisma.operationTime.create({ data: { ...req.body, operationId } });
        (0, response_1.sendSuccess)(res, t, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateOperationTime(req, res) {
    try {
        const t = await database_1.prisma.operationTime.update({ where: { id: parseInt(req.params.timeId) }, data: req.body });
        (0, response_1.sendSuccess)(res, t);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteOperationTime(req, res) {
    try {
        await database_1.prisma.operationTime.delete({ where: { id: parseInt(req.params.timeId) } });
        (0, response_1.sendSuccess)(res, { message: 'Zeiteintrag gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Reports
async function getOperationReport(req, res) {
    try {
        const operationId = parseInt(req.params.id);
        const report = await database_1.prisma.operationReport.findFirst({ where: { operationId } });
        (0, response_1.sendSuccess)(res, report);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function upsertOperationReport(req, res) {
    try {
        const operationId = parseInt(req.params.id);
        const existing = await database_1.prisma.operationReport.findFirst({ where: { operationId } });
        let report;
        if (existing) {
            report = await database_1.prisma.operationReport.update({
                where: { id: existing.id },
                data: { content: req.body.content },
            });
        }
        else {
            report = await database_1.prisma.operationReport.create({
                data: { operationId, content: req.body.content, createdBy: req.user.username },
            });
        }
        (0, response_1.sendSuccess)(res, report);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Documents
async function getDocuments(req, res) {
    try {
        const operationId = parseInt(req.params.id);
        const documents = await database_1.prisma.operationDocument.findMany({
            where: { operationId },
            orderBy: { createdAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, documents);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function uploadDocument(req, res) {
    try {
        const operationId = parseInt(req.params.id);
        if (!req.file) {
            (0, response_1.sendError)(res, 'Keine Datei hochgeladen', 400);
            return;
        }
        const doc = await database_1.prisma.operationDocument.create({
            data: {
                operationId,
                fileName: req.file.originalname,
                filePath: req.file.path,
                fileSize: req.file.size,
                mimeType: req.file.mimetype,
                uploadedBy: req.user?.username || 'System',
            },
        });
        (0, response_1.sendSuccess)(res, doc, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function downloadDocument(req, res) {
    try {
        const doc = await database_1.prisma.operationDocument.findUnique({
            where: { id: parseInt(req.params.docId) },
        });
        if (!doc) {
            (0, response_1.sendError)(res, 'Dokument nicht gefunden', 404);
            return;
        }
        if (!fs_1.default.existsSync(doc.filePath)) {
            (0, response_1.sendError)(res, 'Datei nicht gefunden', 404);
            return;
        }
        res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(doc.fileName)}"`);
        res.setHeader('Content-Type', doc.mimeType);
        fs_1.default.createReadStream(doc.filePath).pipe(res);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteDocument(req, res) {
    try {
        const doc = await database_1.prisma.operationDocument.findUnique({
            where: { id: parseInt(req.params.docId) },
        });
        if (!doc) {
            (0, response_1.sendError)(res, 'Dokument nicht gefunden', 404);
            return;
        }
        if (fs_1.default.existsSync(doc.filePath)) {
            fs_1.default.unlinkSync(doc.filePath);
        }
        await database_1.prisma.operationDocument.delete({ where: { id: doc.id } });
        (0, response_1.sendSuccess)(res, { message: 'Dokument gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=operation.controller.js.map