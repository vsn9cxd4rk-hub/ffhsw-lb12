import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';
import { convertDates } from '../utils/dates';
import { pickWritable } from '../utils/body';
import { recordUploadDir, removeUpload, sendUpload, toStoredPath } from '../utils/files';
import { createUpload, discardUpload } from '../utils/upload';

const EVENT_DATE_FIELDS = ['date'];
const FIREWATCH_DATE_FIELDS = ['date'];
const CATEGORY_UEBUNG = 5;

export const eventDocUpload = createUpload({
  destination: (req) => recordUploadDir('events', req.params.id),
  kinds: ['pdf', 'doc', 'docx'],
  rejectMessage: 'Nur PDF- und Word-Dateien sind erlaubt',
});

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
    const event = await prisma.event.create({ data: convertDates(pickWritable('Event', req.body), EVENT_DATE_FIELDS) as any });
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
    const event = await prisma.event.update({
      where: { id: parseInt(req.params.id) },
      data: convertDates(pickWritable('Event', req.body), EVENT_DATE_FIELDS) as any,
    });
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

    // Get active members; for Übungen (category 5) only the aktive Einsatzabteilung takes part
    const members = await prisma.member.findMany({
      where: {
        isInactive: false,
        deletedAt: null,
        ...(event.category === CATEGORY_UEBUNG ? { group: { name: 'Einsatzabteilung' } } : {}),
      },
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
    const raw = req.body.records;
    if (!Array.isArray(raw)) { sendError(res, 'records muss eine Liste sein', 400); return; }
    const records = raw.map((r: { memberId?: unknown; status?: unknown }) => ({
      memberId: Number(r?.memberId),
      status: String(r?.status ?? ''),
    }));
    if (records.some(r => !Number.isInteger(r.memberId))) { sendError(res, 'Ungültige memberId', 400); return; }

    // Upsert all records atomically so a failure doesn't leave a half-saved list
    await prisma.$transaction(
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
    const fw = await prisma.fireWatch.create({ data: convertDates(pickWritable('FireWatch', req.body), FIREWATCH_DATE_FIELDS) as any });
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
    const fw = await prisma.fireWatch.update({
      where: { id: parseInt(req.params.id) },
      data: convertDates(pickWritable('FireWatch', req.body), FIREWATCH_DATE_FIELDS) as any,
    });
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
    const eventId = parseInt(req.params.id);
    if (!(await prisma.event.findUnique({ where: { id: eventId }, select: { id: true } }))) {
      discardUpload(req.file);
      sendError(res, 'Veranstaltung nicht gefunden', 404);
      return;
    }
    const doc = await prisma.eventDocument.create({
      data: {
        eventId,
        fileName: req.file.originalname,
        filePath: toStoredPath(req.file.path),
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
    sendUpload(res, doc.filePath, doc.fileName, doc.mimeType);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteEventDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.eventDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    removeUpload(doc.filePath);
    await prisma.eventDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
