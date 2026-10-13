import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';
import { convertDates } from '../utils/dates';
import { pickWritable } from '../utils/body';
import { recordUploadDir, removeUpload, sendUpload, toStoredPath } from '../utils/files';
import { createUpload, discardUpload } from '../utils/upload';
import { AUTHOR_ROLE_COMMANDER } from '../services/report-generator.service';

const OPERATION_DATE_FIELDS = ['date'];

// vehicles/leaderCount/memberCount are derived from the Fahrzeugzeiten (syncOperationAggregates).
// The per-operation departure/arrival/return times are no longer maintained: the report and
// the Kräftenachweis take them per vehicle. alarmTime stays writable as the initial value from
// the "Neuer Einsatz" dialog until a vehicle brings its own.
const OPERATION_DERIVED_FIELDS = ['vehicles', 'leaderCount', 'memberCount', 'departureTime', 'arrivalTime', 'returnTime'];

export const uploadMiddleware = createUpload({
  destination: (req) => recordUploadDir('operations', req.params.id),
  kinds: ['pdf', 'doc', 'docx'],
  rejectMessage: 'Nur PDF- und Word-Dateien sind erlaubt',
});

export async function getOperations(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const { year, dateFrom, dateTo, keyword, location } = req.query;

    const where: Record<string, unknown> = {};
    if (year) {
      const y = parseInt(year as string);
      where.date = { gte: new Date(y, 0, 1), lt: new Date(y + 1, 0, 1) };
    } else if (dateFrom || dateTo) {
      const dateFilter: Record<string, Date> = {};
      if (dateFrom) dateFilter.gte = new Date(dateFrom as string);
      if (dateTo) dateFilter.lte = new Date(dateTo as string);
      where.date = dateFilter;
    }
    if (keyword) where.keyword = { contains: keyword as string };
    if (location) where.location = { contains: location as string };

    const [ops, total] = await Promise.all([
      prisma.operation.findMany({
        where,
        skip,
        take,
        orderBy: [{ date: 'desc' }, { alarmTime: 'desc' }],
      }),
      prisma.operation.count({ where }),
    ]);

    sendPaginated(res, ops, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createOperation(req: Request, res: Response): Promise<void> {
  try {
    const op = await prisma.operation.create({
      data: convertDates(pickWritable('Operation', req.body, OPERATION_DERIVED_FIELDS), OPERATION_DATE_FIELDS) as any,
    });
    sendSuccess(res, op, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getOperation(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const op = await prisma.operation.findUnique({
      where: { id },
      include: { times: true },
    });
    if (!op) { sendError(res, 'Einsatz nicht gefunden', 404); return; }
    sendSuccess(res, op);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateOperation(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const data = convertDates(pickWritable('Operation', req.body, OPERATION_DERIVED_FIELDS), OPERATION_DATE_FIELDS) as any;
    // A separate commander only exists when the author was not in charge themselves
    if (data.authorRole === AUTHOR_ROLE_COMMANDER) data.commanderName = null;
    const op = await prisma.operation.update({ where: { id }, data });
    sendSuccess(res, op);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteOperation(req: Request, res: Response): Promise<void> {
  try {
    await prisma.operation.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Einsatz gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Times
/** Earliest of the vehicles' alarm times ("HH:MM"), or null if none is set. */
export function earliestAlarmTime(times: Array<{ alarmTime: string | null }>): string | null {
  const valid = times.map(t => t.alarmTime?.trim() || '').filter(t => /^\d{1,2}:\d{2}$/.test(t));
  if (valid.length === 0) return null;
  return valid.reduce((a, b) => (a.padStart(5, '0') <= b.padStart(5, '0') ? a : b));
}

/**
 * Hält Operation.vehicles/leaderCount/memberCount als abgeleitete Summe über alle
 * Fahrzeugzeiten-Einträge aktuell, damit Übersichtsliste/Dashboard (die diese
 * Operation-Felder lesen) weiterhin den Gesamteinsatz korrekt zeigen, obwohl
 * Fahrzeug und Stärke jetzt pro Fahrzeug im Fahrzeugzeiten-Tab gepflegt werden.
 * Die Alarmzeit des Einsatzes (Einsatzbericht, Kräftenachweis, Liste, Statistik) ist die
 * früheste Fahrzeug-Alarmzeit; ohne Fahrzeug-Alarmzeit bleibt der Wert aus dem Anlegen.
 */
async function syncOperationAggregates(operationId: number): Promise<void> {
  const times = await prisma.operationTime.findMany({ where: { operationId } });
  const vehicleNames = [...new Set(times.map(t => t.vehicleName.trim()).filter(Boolean))];
  const leaderCount = times.reduce((sum, t) => sum + t.leaderCount, 0);
  const memberCount = times.reduce((sum, t) => sum + t.memberCount, 0);
  const alarmTime = earliestAlarmTime(times);
  await prisma.operation.update({
    where: { id: operationId },
    data: { vehicles: vehicleNames.join(', '), leaderCount, memberCount, ...(alarmTime ? { alarmTime } : {}) },
  });
}

export async function createOperationTime(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const t = await prisma.operationTime.create({
      data: { ...pickWritable('OperationTime', req.body, ['operationId']), operationId } as any,
    });
    await syncOperationAggregates(operationId);
    sendSuccess(res, t, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateOperationTime(req: Request, res: Response): Promise<void> {
  try {
    const t = await prisma.operationTime.update({
      where: { id: parseInt(req.params.timeId) },
      data: pickWritable('OperationTime', req.body, ['operationId']),
    });
    await syncOperationAggregates(t.operationId);
    sendSuccess(res, t);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteOperationTime(req: Request, res: Response): Promise<void> {
  try {
    const t = await prisma.operationTime.delete({ where: { id: parseInt(req.params.timeId) } });
    await syncOperationAggregates(t.operationId);
    sendSuccess(res, { message: 'Zeiteintrag gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Documents
export async function getDocuments(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const documents = await prisma.operationDocument.findMany({
      where: { operationId },
      orderBy: { createdAt: 'desc' },
    });
    sendSuccess(res, documents);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function uploadDocument(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    if (!req.file) {
      sendError(res, 'Keine Datei hochgeladen', 400);
      return;
    }
    if (!(await prisma.operation.findUnique({ where: { id: operationId }, select: { id: true } }))) {
      discardUpload(req.file);
      sendError(res, 'Einsatz nicht gefunden', 404);
      return;
    }

    const doc = await prisma.operationDocument.create({
      data: {
        operationId,
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

export async function downloadDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.operationDocument.findUnique({
      where: { id: parseInt(req.params.docId) },
    });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    sendUpload(res, doc.filePath, doc.fileName, doc.mimeType);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.operationDocument.findUnique({
      where: { id: parseInt(req.params.docId) },
    });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }

    removeUpload(doc.filePath);
    await prisma.operationDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Personnel
export async function getPersonnel(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const personnel = await prisma.operationPersonnel.findMany({
      where: { operationId },
      include: {
        member: {
          select: {
            id: true, firstName: true, lastName: true,
            qualAGT: true, qualGruppenfuehrer: true, qualZugfuehrer: true,
            qualLicenseC: true, qualLicenseB: true,
          },
        },
      },
      orderBy: [{ section: 'asc' }, { id: 'asc' }],
    });
    sendSuccess(res, personnel);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function addPersonnel(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const { memberId, vehicleName, function: fn, section } = req.body;
    const p = await prisma.operationPersonnel.create({
      data: { operationId, memberId, vehicleName, function: fn, section: section || 'deployed' },
      include: {
        member: {
          select: {
            id: true, firstName: true, lastName: true,
            qualAGT: true, qualGruppenfuehrer: true, qualZugfuehrer: true,
            qualLicenseC: true, qualLicenseB: true,
          },
        },
      },
    });
    sendSuccess(res, p, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updatePersonnel(req: Request, res: Response): Promise<void> {
  try {
    const p = await prisma.operationPersonnel.update({
      where: { id: parseInt(req.params.personnelId) },
      data: pickWritable('OperationPersonnel', req.body, ['operationId']),
      include: {
        member: {
          select: {
            id: true, firstName: true, lastName: true,
            qualAGT: true, qualGruppenfuehrer: true, qualZugfuehrer: true,
            qualLicenseC: true, qualLicenseB: true,
          },
        },
      },
    });
    sendSuccess(res, p);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deletePersonnel(req: Request, res: Response): Promise<void> {
  try {
    await prisma.operationPersonnel.delete({ where: { id: parseInt(req.params.personnelId) } });
    sendSuccess(res, { message: 'Einsatzkraft entfernt' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
