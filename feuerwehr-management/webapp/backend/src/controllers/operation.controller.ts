import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

// Multer configuration for document uploads
const uploadDir = process.env.UPLOAD_PATH || './uploads';

const storage = multer.diskStorage({
  destination: (req, _file, cb) => {
    const dir = path.join(uploadDir, 'operations', req.params.id);
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    const uniqueName = `${Date.now()}-${file.originalname}`;
    cb(null, uniqueName);
  },
});

export const uploadMiddleware = multer({
  storage,
  limits: { fileSize: 20 * 1024 * 1024 }, // 20MB
  fileFilter: (_req, file, cb) => {
    const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (allowed.includes(file.mimetype)) {
      cb(null, true);
    } else {
      cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
    }
  },
}).single('file');

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
    const op = await prisma.operation.create({ data: req.body });
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
      include: { times: true, reports: true },
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
    const op = await prisma.operation.update({ where: { id }, data: req.body });
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
export async function createOperationTime(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const t = await prisma.operationTime.create({ data: { ...req.body, operationId } });
    sendSuccess(res, t, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateOperationTime(req: Request, res: Response): Promise<void> {
  try {
    const t = await prisma.operationTime.update({ where: { id: parseInt(req.params.timeId) }, data: req.body });
    sendSuccess(res, t);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteOperationTime(req: Request, res: Response): Promise<void> {
  try {
    await prisma.operationTime.delete({ where: { id: parseInt(req.params.timeId) } });
    sendSuccess(res, { message: 'Zeiteintrag gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Reports
export async function getOperationReport(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const report = await prisma.operationReport.findFirst({ where: { operationId } });
    sendSuccess(res, report);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function upsertOperationReport(req: Request, res: Response): Promise<void> {
  try {
    const operationId = parseInt(req.params.id);
    const existing = await prisma.operationReport.findFirst({ where: { operationId } });

    let report;
    if (existing) {
      report = await prisma.operationReport.update({
        where: { id: existing.id },
        data: { content: req.body.content },
      });
    } else {
      report = await prisma.operationReport.create({
        data: { operationId, content: req.body.content, createdBy: req.user!.username },
      });
    }
    sendSuccess(res, report);
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

    const doc = await prisma.operationDocument.create({
      data: {
        operationId,
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

export async function downloadDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.operationDocument.findUnique({
      where: { id: parseInt(req.params.docId) },
    });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }

    if (!fs.existsSync(doc.filePath)) {
      sendError(res, 'Datei nicht gefunden', 404);
      return;
    }

    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(doc.fileName)}"`);
    res.setHeader('Content-Type', doc.mimeType);
    fs.createReadStream(doc.filePath).pipe(res);
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

    if (fs.existsSync(doc.filePath)) {
      fs.unlinkSync(doc.filePath);
    }

    await prisma.operationDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
