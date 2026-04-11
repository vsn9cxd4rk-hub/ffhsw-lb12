import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

const uploadDir = process.env.UPLOAD_PATH || './uploads';

const eventDocStorage = multer.diskStorage({
  destination: (req, _file, cb) => {
    const dir = path.join(uploadDir, 'events', req.params.id);
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  },
});

export const eventDocUpload = multer({
  storage: eventDocStorage,
  limits: { fileSize: 20 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (allowed.includes(file.mimetype)) cb(null, true);
    else cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
  },
}).single('file');

export async function getEvents(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const { year, category, dateFrom, dateTo } = req.query;

    const where: Record<string, unknown> = {};
    if (year) {
      const y = parseInt(year as string);
      where.date = { gte: new Date(y, 0, 1), lt: new Date(y + 1, 0, 1) };
    } else if (dateFrom || dateTo) {
      const f: Record<string, Date> = {};
      if (dateFrom) f.gte = new Date(dateFrom as string);
      if (dateTo) f.lte = new Date(dateTo as string);
      where.date = f;
    }
    if (category) where.category = parseInt(category as string);

    const [events, total] = await Promise.all([
      prisma.event.findMany({ where, skip, take, orderBy: { date: 'desc' } }),
      prisma.event.count({ where }),
    ]);

    sendPaginated(res, events, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createEvent(req: Request, res: Response): Promise<void> {
  try {
    const data = { ...req.body };
    if (data.date && !data.date.includes('T')) {
      data.date = new Date(data.date).toISOString();
    }
    const event = await prisma.event.create({ data });
    sendSuccess(res, event, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getEvent(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const event = await prisma.event.findUnique({
      where: { id },
      include: {
        attendances: {
          include: {
            member: { select: { id: true, firstName: true, lastName: true, rank: true } },
          },
        },
      },
    });
    if (!event) { sendError(res, 'Veranstaltung nicht gefunden', 404); return; }
    sendSuccess(res, event);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateEvent(req: Request, res: Response): Promise<void> {
  try {
    const event = await prisma.event.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, event);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteEvent(req: Request, res: Response): Promise<void> {
  try {
    await prisma.event.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Veranstaltung gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Attendance
export async function getAttendance(req: Request, res: Response): Promise<void> {
  try {
    const eventId = parseInt(req.params.id);
    const event = await prisma.event.findUnique({ where: { id: eventId } });
    if (!event) { sendError(res, 'Veranstaltung nicht gefunden', 404); return; }

    const year = event.date.getFullYear();

    // Get all active members
    const members = await prisma.member.findMany({
      where: { isInactive: false, deletedAt: null },
      select: { id: true, firstName: true, lastName: true, rank: true, groupId: true, group: { select: { name: true } } },
      orderBy: [{ lastName: 'asc' }, { firstName: 'asc' }],
    });

    // Get existing attendance records
    const existing = await prisma.attendance.findMany({
      where: { eventId },
    });

    const attendanceMap = new Map(existing.map(a => [a.memberId, a.status]));

    const result = members.map(m => ({
      memberId: m.id,
      member: m,
      status: attendanceMap.get(m.id) || null,
    }));

    sendSuccess(res, { event, year, members: result });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateAttendance(req: Request, res: Response): Promise<void> {
  try {
    const eventId = parseInt(req.params.id);
    const event = await prisma.event.findUnique({ where: { id: eventId } });
    if (!event) { sendError(res, 'Veranstaltung nicht gefunden', 404); return; }

    const year = event.date.getFullYear();
    const records: Array<{ memberId: number; status: string }> = req.body.records;

    // Upsert all records
    await Promise.all(
      records.map(({ memberId, status }) =>
        prisma.attendance.upsert({
          where: { eventId_memberId: { eventId, memberId } },
          update: { status, year },
          create: { eventId, memberId, status, year },
        })
      )
    );

    sendSuccess(res, { message: `${records.length} Anwesenheitseinträge gespeichert` });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Fire watches
export async function getFireWatches(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const year = req.query.year ? parseInt(req.query.year as string) : undefined;

    const where: Record<string, unknown> = {};
    if (year) where.date = { gte: new Date(year, 0, 1), lt: new Date(year + 1, 0, 1) };

    const [watches, total] = await Promise.all([
      prisma.fireWatch.findMany({ where, skip, take, orderBy: { date: 'desc' } }),
      prisma.fireWatch.count({ where }),
    ]);
    sendPaginated(res, watches, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createFireWatch(req: Request, res: Response): Promise<void> {
  try {
    const fw = await prisma.fireWatch.create({ data: req.body });
    sendSuccess(res, fw, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getFireWatch(req: Request, res: Response): Promise<void> {
  try {
    const fw = await prisma.fireWatch.findUnique({ where: { id: parseInt(req.params.id) } });
    if (!fw) { sendError(res, 'Brandsicherheitswache nicht gefunden', 404); return; }
    sendSuccess(res, fw);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateFireWatch(req: Request, res: Response): Promise<void> {
  try {
    const fw = await prisma.fireWatch.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, fw);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteFireWatch(req: Request, res: Response): Promise<void> {
  try {
    await prisma.fireWatch.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Brandsicherheitswache gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Event Documents
export async function getEventDocuments(req: Request, res: Response): Promise<void> {
  try {
    const docs = await prisma.eventDocument.findMany({
      where: { eventId: parseInt(req.params.id) },
      orderBy: { createdAt: 'desc' },
    });
    sendSuccess(res, docs);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function uploadEventDocument(req: Request, res: Response): Promise<void> {
  try {
    if (!req.file) { sendError(res, 'Keine Datei hochgeladen', 400); return; }
    const doc = await prisma.eventDocument.create({
      data: {
        eventId: parseInt(req.params.id),
        fileName: req.file.originalname,
        filePath: req.file.path,
        fileSize: req.file.size,
        mimeType: req.file.mimetype,
        uploadedBy: req.user?.username || 'System',
      },
    });
    sendSuccess(res, doc, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function downloadEventDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.eventDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (!fs.existsSync(doc.filePath)) { sendError(res, 'Datei nicht gefunden', 404); return; }
    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(doc.fileName)}"`);
    res.setHeader('Content-Type', doc.mimeType);
    fs.createReadStream(doc.filePath).pipe(res);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteEventDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.eventDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (fs.existsSync(doc.filePath)) fs.unlinkSync(doc.filePath);
    await prisma.eventDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
