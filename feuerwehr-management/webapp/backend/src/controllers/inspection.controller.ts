import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

const uploadDir = process.env.UPLOAD_PATH || './uploads';

const inspectionDocStorage = multer.diskStorage({
  destination: (_req, _file, cb) => {
    const dir = path.join(uploadDir, 'inspection-documents');
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  },
});

export const inspectionDocUpload = multer({
  storage: inspectionDocStorage,
  limits: { fileSize: 20 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    const allowed = ['application/pdf'];
    if (allowed.includes(file.mimetype)) cb(null, true);
    else cb(new Error('Nur PDF-Dateien sind erlaubt'));
  },
}).single('file');

// All inspections (paginated, filterable by result, deviceClassId, year, search)
export async function getInspections(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const result = req.query.result as string | undefined;
    const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId as string) : undefined;
    const year = req.query.year ? parseInt(req.query.year as string) : undefined;
    const search = req.query.search as string | undefined;

    const where: Record<string, unknown> = {};
    if (result) {
      where.result = result;
    }

    const articleConditions: Record<string, unknown>[] = [];
    if (deviceClassId) {
      articleConditions.push({ deviceSubclass: { deviceClassId } });
    }
    if (search) {
      articleConditions.push({
        OR: [
          { name: { contains: search } },
          { inventoryNumber: { contains: search } },
          { communityInventoryNumber: { contains: search } },
          { mpFeuerInventoryNumber: { contains: search } },
        ],
      });
    }
    if (articleConditions.length > 0) {
      where.article = articleConditions.length === 1 ? articleConditions[0] : { AND: articleConditions };
    }

    if (year) {
      where.inspectedAt = {
        gte: new Date(`${year}-01-01`),
        lt: new Date(`${year + 1}-01-01`),
      };
    }

    const [inspections, total] = await Promise.all([
      prisma.articleInspection.findMany({
        where,
        skip,
        take,
        include: {
          article: {
            include: {
              warehouse: true,
              deviceSubclass: { include: { deviceClass: true } },
            },
          },
          inspectionType: true,
          criterionResults: {
            include: { criterion: true },
            orderBy: { criterion: { sortOrder: 'asc' } },
          },
          documents: true,
        },
        orderBy: { inspectedAt: 'desc' },
      }),
      prisma.articleInspection.count({ where }),
    ]);

    sendPaginated(res, inspections, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Articles that are due for inspection
export async function getDueInspections(req: Request, res: Response): Promise<void> {
  try {
    const now = new Date();
    const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId as string) : undefined;
    const deviceSubclassId = req.query.deviceSubclassId ? parseInt(req.query.deviceSubclassId as string) : undefined;
    const search = req.query.search as string | undefined;

    const articleWhere: Record<string, unknown> = {
      inspectionInterval: { not: null },
      isDecommissioned: false,
    };
    if (deviceSubclassId) {
      articleWhere.deviceSubclassId = deviceSubclassId;
    } else if (deviceClassId) {
      articleWhere.deviceSubclass = { deviceClassId };
    }
    if (search) {
      articleWhere.OR = [
        { name: { contains: search } },
        { inventoryNumber: { contains: search } },
        { communityInventoryNumber: { contains: search } },
        { mpFeuerInventoryNumber: { contains: search } },
      ];
    }

    const articles = await prisma.article.findMany({
      where: articleWhere,
      include: {
        warehouse: true,
        deviceSubclass: { include: { deviceClass: true } },
        inspections: {
          orderBy: { inspectedAt: 'desc' },
          take: 1,
        },
      },
      orderBy: { name: 'asc' },
    });

    const dueArticles = articles.filter(article => {
      if (article.inspections.length === 0) return true;
      const lastInspection = article.inspections[0];
      return lastInspection.nextDueDate && lastInspection.nextDueDate <= now;
    });

    sendSuccess(res, dueArticles);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Inspection history for a specific article
export async function getArticleInspections(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.articleId);
    const inspections = await prisma.articleInspection.findMany({
      where: { articleId },
      include: {
        inspectionType: true,
        criterionResults: {
          include: { criterion: true },
          orderBy: { criterion: { sortOrder: 'asc' } },
        },
        documents: true,
      },
      orderBy: { inspectedAt: 'desc' },
    });
    sendSuccess(res, inspections);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Get inspection criteria for an article based on its subclass
export async function getInspectionCriteria(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.articleId);
    const article = await prisma.article.findUnique({
      where: { id: articleId },
      select: { deviceSubclassId: true },
    });
    if (!article) { sendError(res, 'Artikel nicht gefunden', 404); return; }
    if (!article.deviceSubclassId) { sendSuccess(res, []); return; }

    const criteria = await prisma.inspectionCriterion.findMany({
      where: { deviceSubclassId: article.deviceSubclassId },
      orderBy: { sortOrder: 'asc' },
    });
    sendSuccess(res, criteria);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Create inspection with criterion results, inspection type, and auto-calculated result
export async function createInspection(req: Request, res: Response): Promise<void> {
  try {
    const { articleId, inspectedAt, inspectedBy, inspectionTypeId, notes, criterionResults } = req.body;

    const article = await prisma.article.findUnique({
      where: { id: articleId },
      include: { deviceSubclass: { include: { criteria: true } } },
    });
    if (!article) { sendError(res, 'Artikel nicht gefunden', 404); return; }

    let computedResult = 'passed';
    if (criterionResults && criterionResults.length > 0) {
      if (article.deviceSubclass) {
        const requiredIds = article.deviceSubclass.criteria.map((c: { id: number }) => c.id);
        const providedIds = criterionResults.map((cr: { criterionId: number }) => cr.criterionId);
        const missing = requiredIds.filter((id: number) => !providedIds.includes(id));
        if (missing.length > 0) {
          sendError(res, 'Nicht alle Prüfkriterien wurden bewertet', 400);
          return;
        }
      }
      const hasNio = criterionResults.some((cr: { result: string }) => cr.result === 'nio');
      computedResult = hasNio ? 'failed' : 'passed';
    }

    // Determine interval: check type-specific schedule first, then fallback to article.inspectionInterval
    let intervalMonths = article.inspectionInterval;
    if (inspectionTypeId) {
      const schedule = await prisma.articleInspectionSchedule.findUnique({
        where: { articleId_inspectionTypeId: { articleId, inspectionTypeId } },
      });
      if (schedule) intervalMonths = schedule.intervalMonths;
    }

    let nextDueDate: Date | null = null;
    if (intervalMonths) {
      nextDueDate = new Date(inspectedAt);
      nextDueDate.setMonth(nextDueDate.getMonth() + intervalMonths);
    }

    const inspection = await prisma.$transaction(async (tx) => {
      const insp = await tx.articleInspection.create({
        data: {
          articleId,
          inspectionTypeId: inspectionTypeId || null,
          inspectedAt: new Date(inspectedAt),
          inspectedBy,
          result: computedResult,
          notes: notes || null,
          nextDueDate,
        },
      });

      if (criterionResults && criterionResults.length > 0) {
        await tx.inspectionCriterionResult.createMany({
          data: criterionResults.map((cr: { criterionId: number; result: string }) => ({
            inspectionId: insp.id,
            criterionId: cr.criterionId,
            result: cr.result,
          })),
        });
      }

      return tx.articleInspection.findUnique({
        where: { id: insp.id },
        include: {
          article: {
            include: {
              warehouse: true,
              deviceSubclass: { include: { deviceClass: true } },
            },
          },
          inspectionType: true,
          criterionResults: {
            include: { criterion: true },
            orderBy: { criterion: { sortOrder: 'asc' } },
          },
        },
      });
    });

    sendSuccess(res, inspection, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Update inspection
export async function updateInspection(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const { inspectedAt, inspectedBy, inspectionTypeId, notes, criterionResults } = req.body;

    const existing = await prisma.articleInspection.findUnique({
      where: { id },
      include: { article: true },
    });
    if (!existing) { sendError(res, 'Prüfung nicht gefunden', 404); return; }

    let nextDueDate = existing.nextDueDate;
    if (inspectedAt && existing.article.inspectionInterval) {
      nextDueDate = new Date(inspectedAt);
      nextDueDate.setMonth(nextDueDate.getMonth() + existing.article.inspectionInterval);
    }

    let computedResult = existing.result;
    if (criterionResults && criterionResults.length > 0) {
      const hasNio = criterionResults.some((cr: { result: string }) => cr.result === 'nio');
      computedResult = hasNio ? 'failed' : 'passed';
    }

    const inspection = await prisma.$transaction(async (tx) => {
      const data: Record<string, unknown> = {
        inspectedAt: inspectedAt ? new Date(inspectedAt) : undefined,
        inspectedBy,
        result: computedResult,
        notes: notes !== undefined ? notes || null : undefined,
        nextDueDate,
      };
      if (inspectionTypeId !== undefined) data.inspectionTypeId = inspectionTypeId || null;

      const insp = await tx.articleInspection.update({ where: { id }, data });

      if (criterionResults && criterionResults.length > 0) {
        await tx.inspectionCriterionResult.deleteMany({ where: { inspectionId: id } });
        await tx.inspectionCriterionResult.createMany({
          data: criterionResults.map((cr: { criterionId: number; result: string }) => ({
            inspectionId: id,
            criterionId: cr.criterionId,
            result: cr.result,
          })),
        });
      }

      return tx.articleInspection.findUnique({
        where: { id: insp.id },
        include: {
          article: { include: { warehouse: true } },
          inspectionType: true,
          criterionResults: {
            include: { criterion: true },
            orderBy: { criterion: { sortOrder: 'asc' } },
          },
        },
      });
    });

    sendSuccess(res, inspection);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Report endpoint: full inspection details for PDF generation
export async function getInspectionReport(req: Request, res: Response): Promise<void> {
  try {
    const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId as string) : undefined;
    const year = req.query.year ? parseInt(req.query.year as string) : undefined;
    const search = req.query.search as string | undefined;

    const where: Record<string, unknown> = {};

    const articleConditions: Record<string, unknown>[] = [];
    if (deviceClassId) {
      articleConditions.push({ deviceSubclass: { deviceClassId } });
    }
    if (search) {
      articleConditions.push({
        OR: [
          { name: { contains: search } },
          { inventoryNumber: { contains: search } },
          { communityInventoryNumber: { contains: search } },
          { mpFeuerInventoryNumber: { contains: search } },
        ],
      });
    }
    if (articleConditions.length > 0) {
      where.article = articleConditions.length === 1 ? articleConditions[0] : { AND: articleConditions };
    }

    if (year) {
      where.inspectedAt = {
        gte: new Date(`${year}-01-01`),
        lt: new Date(`${year + 1}-01-01`),
      };
    }

    const inspections = await prisma.articleInspection.findMany({
      where,
      include: {
        article: {
          include: {
            warehouse: true,
            deviceSubclass: { include: { deviceClass: true } },
          },
        },
        inspectionType: true,
        criterionResults: {
          include: { criterion: true },
          orderBy: { criterion: { sortOrder: 'asc' } },
        },
      },
      orderBy: [
        { article: { name: 'asc' } },
        { inspectedAt: 'desc' },
      ],
    });

    sendSuccess(res, inspections);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Inspection document endpoints
export async function getInspectionDocuments(req: Request, res: Response): Promise<void> {
  try {
    const inspectionId = parseInt(req.params.id);
    const docs = await prisma.inspectionDocument.findMany({
      where: { inspectionId },
      orderBy: { createdAt: 'desc' },
    });
    sendSuccess(res, docs);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function uploadInspectionDocument(req: Request, res: Response): Promise<void> {
  try {
    if (!req.file) { sendError(res, 'Keine Datei hochgeladen', 400); return; }
    const inspectionId = parseInt(req.params.id);

    const inspection = await prisma.articleInspection.findUnique({ where: { id: inspectionId } });
    if (!inspection) { sendError(res, 'Prüfung nicht gefunden', 404); return; }

    const doc = await prisma.inspectionDocument.create({
      data: {
        inspectionId,
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

export async function downloadInspectionDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.inspectionDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (!fs.existsSync(doc.filePath)) { sendError(res, 'Datei nicht gefunden', 404); return; }

    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(doc.fileName)}"`);
    res.setHeader('Content-Type', doc.mimeType);
    fs.createReadStream(doc.filePath).pipe(res);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteInspectionDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.inspectionDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (fs.existsSync(doc.filePath)) fs.unlinkSync(doc.filePath);
    await prisma.inspectionDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
