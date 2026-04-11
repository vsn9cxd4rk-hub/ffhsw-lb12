"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.eventDocUpload = void 0;
exports.getEvents = getEvents;
exports.createEvent = createEvent;
exports.getEvent = getEvent;
exports.updateEvent = updateEvent;
exports.deleteEvent = deleteEvent;
exports.getAttendance = getAttendance;
exports.updateAttendance = updateAttendance;
exports.getFireWatches = getFireWatches;
exports.createFireWatch = createFireWatch;
exports.getFireWatch = getFireWatch;
exports.updateFireWatch = updateFireWatch;
exports.deleteFireWatch = deleteFireWatch;
exports.getEventDocuments = getEventDocuments;
exports.uploadEventDocument = uploadEventDocument;
exports.downloadEventDocument = downloadEventDocument;
exports.deleteEventDocument = deleteEventDocument;
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
const multer_1 = __importDefault(require("multer"));
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
const uploadDir = process.env.UPLOAD_PATH || './uploads';
const eventDocStorage = multer_1.default.diskStorage({
    destination: (req, _file, cb) => {
        const dir = path_1.default.join(uploadDir, 'events', req.params.id);
        fs_1.default.mkdirSync(dir, { recursive: true });
        cb(null, dir);
    },
    filename: (_req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});
exports.eventDocUpload = (0, multer_1.default)({
    storage: eventDocStorage,
    limits: { fileSize: 20 * 1024 * 1024 },
    fileFilter: (_req, file, cb) => {
        const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
        if (allowed.includes(file.mimetype))
            cb(null, true);
        else
            cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
    },
}).single('file');
async function getEvents(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const { year, category, dateFrom, dateTo } = req.query;
        const where = {};
        if (year) {
            const y = parseInt(year);
            where.date = { gte: new Date(y, 0, 1), lt: new Date(y + 1, 0, 1) };
        }
        else if (dateFrom || dateTo) {
            const f = {};
            if (dateFrom)
                f.gte = new Date(dateFrom);
            if (dateTo)
                f.lte = new Date(dateTo);
            where.date = f;
        }
        if (category)
            where.category = parseInt(category);
        const [events, total] = await Promise.all([
            database_1.prisma.event.findMany({ where, skip, take, orderBy: { date: 'desc' } }),
            database_1.prisma.event.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, events, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createEvent(req, res) {
    try {
        const data = { ...req.body };
        if (data.date && !data.date.includes('T')) {
            data.date = new Date(data.date).toISOString();
        }
        const event = await database_1.prisma.event.create({ data });
        (0, response_1.sendSuccess)(res, event, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getEvent(req, res) {
    try {
        const id = parseInt(req.params.id);
        const event = await database_1.prisma.event.findUnique({
            where: { id },
            include: {
                attendances: {
                    include: {
                        member: { select: { id: true, firstName: true, lastName: true, rank: true } },
                    },
                },
            },
        });
        if (!event) {
            (0, response_1.sendError)(res, 'Veranstaltung nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, event);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateEvent(req, res) {
    try {
        const event = await database_1.prisma.event.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, event);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteEvent(req, res) {
    try {
        await database_1.prisma.event.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Veranstaltung gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Attendance
async function getAttendance(req, res) {
    try {
        const eventId = parseInt(req.params.id);
        const event = await database_1.prisma.event.findUnique({ where: { id: eventId } });
        if (!event) {
            (0, response_1.sendError)(res, 'Veranstaltung nicht gefunden', 404);
            return;
        }
        const year = event.date.getFullYear();
        // Get all active members
        const members = await database_1.prisma.member.findMany({
            where: { isInactive: false, deletedAt: null },
            select: { id: true, firstName: true, lastName: true, rank: true, groupId: true, group: { select: { name: true } } },
            orderBy: [{ lastName: 'asc' }, { firstName: 'asc' }],
        });
        // Get existing attendance records
        const existing = await database_1.prisma.attendance.findMany({
            where: { eventId },
        });
        const attendanceMap = new Map(existing.map(a => [a.memberId, a.status]));
        const result = members.map(m => ({
            memberId: m.id,
            member: m,
            status: attendanceMap.get(m.id) || null,
        }));
        (0, response_1.sendSuccess)(res, { event, year, members: result });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateAttendance(req, res) {
    try {
        const eventId = parseInt(req.params.id);
        const event = await database_1.prisma.event.findUnique({ where: { id: eventId } });
        if (!event) {
            (0, response_1.sendError)(res, 'Veranstaltung nicht gefunden', 404);
            return;
        }
        const year = event.date.getFullYear();
        const records = req.body.records;
        // Upsert all records
        await Promise.all(records.map(({ memberId, status }) => database_1.prisma.attendance.upsert({
            where: { eventId_memberId: { eventId, memberId } },
            update: { status, year },
            create: { eventId, memberId, status, year },
        })));
        (0, response_1.sendSuccess)(res, { message: `${records.length} Anwesenheitseinträge gespeichert` });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Fire watches
async function getFireWatches(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const year = req.query.year ? parseInt(req.query.year) : undefined;
        const where = {};
        if (year)
            where.date = { gte: new Date(year, 0, 1), lt: new Date(year + 1, 0, 1) };
        const [watches, total] = await Promise.all([
            database_1.prisma.fireWatch.findMany({ where, skip, take, orderBy: { date: 'desc' } }),
            database_1.prisma.fireWatch.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, watches, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createFireWatch(req, res) {
    try {
        const fw = await database_1.prisma.fireWatch.create({ data: req.body });
        (0, response_1.sendSuccess)(res, fw, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getFireWatch(req, res) {
    try {
        const fw = await database_1.prisma.fireWatch.findUnique({ where: { id: parseInt(req.params.id) } });
        if (!fw) {
            (0, response_1.sendError)(res, 'Brandsicherheitswache nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, fw);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateFireWatch(req, res) {
    try {
        const fw = await database_1.prisma.fireWatch.update({ where: { id: parseInt(req.params.id) }, data: req.body });
        (0, response_1.sendSuccess)(res, fw);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteFireWatch(req, res) {
    try {
        await database_1.prisma.fireWatch.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Brandsicherheitswache gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Event Documents
async function getEventDocuments(req, res) {
    try {
        const docs = await database_1.prisma.eventDocument.findMany({
            where: { eventId: parseInt(req.params.id) },
            orderBy: { createdAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, docs);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function uploadEventDocument(req, res) {
    try {
        if (!req.file) {
            (0, response_1.sendError)(res, 'Keine Datei hochgeladen', 400);
            return;
        }
        const doc = await database_1.prisma.eventDocument.create({
            data: {
                eventId: parseInt(req.params.id),
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
async function downloadEventDocument(req, res) {
    try {
        const doc = await database_1.prisma.eventDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
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
async function deleteEventDocument(req, res) {
    try {
        const doc = await database_1.prisma.eventDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
        if (!doc) {
            (0, response_1.sendError)(res, 'Dokument nicht gefunden', 404);
            return;
        }
        if (fs_1.default.existsSync(doc.filePath))
            fs_1.default.unlinkSync(doc.filePath);
        await database_1.prisma.eventDocument.delete({ where: { id: doc.id } });
        (0, response_1.sendSuccess)(res, { message: 'Dokument gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=event.controller.js.map