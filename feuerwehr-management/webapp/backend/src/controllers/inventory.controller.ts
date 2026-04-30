import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

// Multer config for article documents
const uploadDir = process.env.UPLOAD_PATH || './uploads';

const articleDocStorage = multer.diskStorage({
  destination: (_req, _file, cb) => {
    const dir = path.join(uploadDir, 'article-documents');
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  },
});

export const articleDocUpload = multer({
  storage: articleDocStorage,
  limits: { fileSize: 20 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    if (file.mimetype === 'application/pdf') cb(null, true);
    else cb(new Error('Nur PDF-Dateien sind erlaubt'));
  },
}).single('file');

// Warehouses
export async function getWarehouses(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = req.query.vehicleId ? parseInt(req.query.vehicleId as string) : undefined;

    // Auto-create warehouse entries for vehicles that don't have one yet
    if (vehicleId === undefined) {
      const vehiclesWithoutWarehouse = await prisma.vehicle.findMany({
        where: {
          isRetired: false,
          warehouses: { none: {} },
        },
        select: { id: true, name: true },
      });
      if (vehiclesWithoutWarehouse.length > 0) {
        await prisma.warehouse.createMany({
          data: vehiclesWithoutWarehouse.map(v => ({ name: v.name, vehicleId: v.id })),
        });
      }
    }

    const warehouses = await prisma.warehouse.findMany({
      where: vehicleId !== undefined ? { vehicleId } : {},
      include: { vehicle: { select: { id: true, name: true } } },
      orderBy: { name: 'asc' },
    });
    sendSuccess(res, warehouses);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createWarehouse(req: Request, res: Response): Promise<void> {
  try {
    const w = await prisma.warehouse.create({ data: req.body });
    sendSuccess(res, w, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateWarehouse(req: Request, res: Response): Promise<void> {
  try {
    const w = await prisma.warehouse.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, w);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteWarehouse(req: Request, res: Response): Promise<void> {
  try {
    await prisma.warehouse.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Lager gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Next inventory number
export async function getNextInventoryNumber(_req: Request, res: Response): Promise<void> {
  try {
    const articles = await prisma.article.findMany({
      where: { inventoryNumber: { not: null } },
      select: { inventoryNumber: true },
    });
    let maxNum = 0;
    for (const a of articles) {
      const match = a.inventoryNumber?.match(/(\d+)/);
      if (match) {
        const num = parseInt(match[match.length - 1]);
        if (num > maxNum) maxNum = num;
      }
    }
    sendSuccess(res, { next: String(maxNum + 1) });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Articles
export async function getArticles(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const search = req.query.search as string;
    const warehouseId = req.query.warehouseId ? parseInt(req.query.warehouseId as string) : undefined;

    const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId as string) : undefined;
    const deviceSubclassId = req.query.deviceSubclassId ? parseInt(req.query.deviceSubclassId as string) : undefined;

    const where: Record<string, unknown> = {};
    if (search) {
      where.OR = [
        { name: { contains: search } },
        { manufacturer: { contains: search } },
        { ean: { contains: search } },
        { inventoryNumber: { contains: search } },
        { communityInventoryNumber: { contains: search } },
        { mpFeuerInventoryNumber: { contains: search } },
      ];
    }
    if (warehouseId !== undefined) {
      where.warehouseId = warehouseId;
    }
    if (deviceSubclassId !== undefined) {
      where.deviceSubclassId = deviceSubclassId;
    } else if (deviceClassId !== undefined) {
      where.deviceSubclass = { deviceClassId };
    }

    const [articles, total] = await Promise.all([
      prisma.article.findMany({
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
      prisma.article.count({ where }),
    ]);

    sendPaginated(res, articles, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createArticle(req: Request, res: Response): Promise<void> {
  try {
    const {
      deviceSubclassId, manufacturingDate, specification, serialNumber, din, isDecommissioned,
      commissionedDate, decommissionedDate, designationLB, communityInventoryNumber,
      mpFeuerInventoryNumber, retirementPeriodMonths,
      ...rest
    } = req.body;
    const article = await prisma.article.create({
      data: {
        ...rest,
        deviceSubclassId: deviceSubclassId || null,
        manufacturingDate: manufacturingDate ? new Date(manufacturingDate) : null,
        commissionedDate: commissionedDate ? new Date(commissionedDate) : null,
        decommissionedDate: decommissionedDate ? new Date(decommissionedDate) : null,
        specification: specification || null,
        serialNumber: serialNumber || null,
        din: din || null,
        isDecommissioned: decommissionedDate ? true : (isDecommissioned || false),
        designationLB: designationLB ?? 'LB12',
        communityInventoryNumber: communityInventoryNumber || null,
        mpFeuerInventoryNumber: mpFeuerInventoryNumber || null,
        retirementPeriodMonths: retirementPeriodMonths ? parseInt(retirementPeriodMonths) : null,
      },
      include: { warehouse: true, deviceSubclass: { include: { deviceClass: true } } },
    });
    sendSuccess(res, article, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getArticle(req: Request, res: Response): Promise<void> {
  try {
    const article = await prisma.article.findUnique({
      where: { id: parseInt(req.params.id) },
      include: {
        warehouse: true,
        deviceSubclass: { include: { deviceClass: true } },
        assignments: { include: { warehouse: true } },
        documents: true,
        inspectionStandards: true,
        inspectionSchedules: { include: { inspectionType: true } },
        defects: { orderBy: { reportedAt: 'desc' }, take: 10 },
        repairs: { orderBy: { repairedAt: 'desc' }, take: 10 },
      },
    });
    if (!article) { sendError(res, 'Artikel nicht gefunden', 404); return; }
    sendSuccess(res, article);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateArticle(req: Request, res: Response): Promise<void> {
  try {
    const {
      deviceSubclassId, manufacturingDate, commissionedDate, decommissionedDate,
      retirementPeriodMonths, ...rest
    } = req.body;
    const data: Record<string, unknown> = { ...rest };
    if (deviceSubclassId !== undefined) data.deviceSubclassId = deviceSubclassId || null;
    if (manufacturingDate !== undefined) data.manufacturingDate = manufacturingDate ? new Date(manufacturingDate) : null;
    if (commissionedDate !== undefined) data.commissionedDate = commissionedDate ? new Date(commissionedDate) : null;
    if (decommissionedDate !== undefined) {
      data.decommissionedDate = decommissionedDate ? new Date(decommissionedDate) : null;
      data.isDecommissioned = !!decommissionedDate;
    }
    if (retirementPeriodMonths !== undefined) data.retirementPeriodMonths = retirementPeriodMonths ? parseInt(retirementPeriodMonths) : null;

    const article = await prisma.article.update({
      where: { id: parseInt(req.params.id) },
      data,
      include: { warehouse: true, deviceSubclass: { include: { deviceClass: true } } },
    });
    sendSuccess(res, article);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteArticle(req: Request, res: Response): Promise<void> {
  try {
    await prisma.article.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Artikel gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Assignments
export async function assignArticle(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const assignment = await prisma.articleAssignment.create({
      data: { ...req.body, articleId },
      include: { warehouse: true },
    });
    sendSuccess(res, assignment, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateAssignment(req: Request, res: Response): Promise<void> {
  try {
    const a = await prisma.articleAssignment.update({
      where: { id: parseInt(req.params.assignId) },
      data: req.body,
      include: { warehouse: true },
    });
    sendSuccess(res, a);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteAssignment(req: Request, res: Response): Promise<void> {
  try {
    await prisma.articleAssignment.delete({ where: { id: parseInt(req.params.assignId) } });
    sendSuccess(res, { message: 'Zuweisung gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Article Documents
export async function getArticleDocuments(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const documents = await prisma.articleDocument.findMany({
      where: { articleId },
      orderBy: { createdAt: 'desc' },
    });
    sendSuccess(res, documents);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function uploadArticleDocument(req: Request, res: Response): Promise<void> {
  try {
    if (!req.file) { sendError(res, 'Keine Datei hochgeladen', 400); return; }
    const articleId = parseInt(req.params.id);
    const doc = await prisma.articleDocument.create({
      data: {
        articleId,
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

export async function downloadArticleDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.articleDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (!fs.existsSync(doc.filePath)) { sendError(res, 'Datei nicht gefunden', 404); return; }

    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(doc.fileName)}"`);
    res.setHeader('Content-Type', doc.mimeType);
    fs.createReadStream(doc.filePath).pipe(res);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteArticleDocument(req: Request, res: Response): Promise<void> {
  try {
    const doc = await prisma.articleDocument.findUnique({ where: { id: parseInt(req.params.docId) } });
    if (!doc) { sendError(res, 'Dokument nicht gefunden', 404); return; }
    if (fs.existsSync(doc.filePath)) fs.unlinkSync(doc.filePath);
    await prisma.articleDocument.delete({ where: { id: doc.id } });
    sendSuccess(res, { message: 'Dokument gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Article Inspection Standards
export async function getArticleStandards(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const standards = await prisma.articleInspectionStandard.findMany({
      where: { articleId },
      orderBy: { name: 'asc' },
    });
    sendSuccess(res, standards);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createArticleStandard(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const standard = await prisma.articleInspectionStandard.create({
      data: { articleId, ...req.body },
    });
    sendSuccess(res, standard, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateArticleStandard(req: Request, res: Response): Promise<void> {
  try {
    const standard = await prisma.articleInspectionStandard.update({
      where: { id: parseInt(req.params.stdId) },
      data: req.body,
    });
    sendSuccess(res, standard);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteArticleStandard(req: Request, res: Response): Promise<void> {
  try {
    await prisma.articleInspectionStandard.delete({ where: { id: parseInt(req.params.stdId) } });
    sendSuccess(res, { message: 'Prüfnorm gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Article Inspection Schedules
export async function getArticleSchedules(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const schedules = await prisma.articleInspectionSchedule.findMany({
      where: { articleId },
      include: { inspectionType: true },
    });
    sendSuccess(res, schedules);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createArticleSchedule(req: Request, res: Response): Promise<void> {
  try {
    const articleId = parseInt(req.params.id);
    const schedule = await prisma.articleInspectionSchedule.create({
      data: { articleId, ...req.body },
      include: { inspectionType: true },
    });
    sendSuccess(res, schedule, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateArticleSchedule(req: Request, res: Response): Promise<void> {
  try {
    const schedule = await prisma.articleInspectionSchedule.update({
      where: { id: parseInt(req.params.schedId) },
      data: req.body,
      include: { inspectionType: true },
    });
    sendSuccess(res, schedule);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteArticleSchedule(req: Request, res: Response): Promise<void> {
  try {
    await prisma.articleInspectionSchedule.delete({ where: { id: parseInt(req.params.schedId) } });
    sendSuccess(res, { message: 'Prüfplan gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Defects (Mängelmeldesystem)
export async function getDefects(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const status = req.query.status as string | undefined;
    const severity = req.query.severity as string | undefined;
    const articleId = req.query.articleId ? parseInt(req.query.articleId as string) : undefined;

    const where: Record<string, unknown> = {};
    if (status) where.status = status;
    if (severity) where.severity = severity;
    if (articleId !== undefined) where.articleId = articleId;

    const [defects, total] = await Promise.all([
      prisma.articleDefect.findMany({
        where,
        skip,
        take,
        include: { article: true },
        orderBy: { reportedAt: 'desc' },
      }),
      prisma.articleDefect.count({ where }),
    ]);

    sendPaginated(res, defects, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getDefect(req: Request, res: Response): Promise<void> {
  try {
    const defect = await prisma.articleDefect.findUnique({
      where: { id: parseInt(req.params.id) },
      include: { article: true, repairs: true },
    });
    if (!defect) { sendError(res, 'Mangel nicht gefunden', 404); return; }
    sendSuccess(res, defect);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createDefect(req: Request, res: Response): Promise<void> {
  try {
    const { reportedAt, ...rest } = req.body;
    const defect = await prisma.articleDefect.create({
      data: {
        ...rest,
        reportedAt: reportedAt ? new Date(reportedAt) : new Date(),
      },
      include: { article: true },
    });
    sendSuccess(res, defect, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateDefect(req: Request, res: Response): Promise<void> {
  try {
    const { reportedAt, resolvedAt, ...rest } = req.body;
    const data: Record<string, unknown> = { ...rest };
    if (reportedAt !== undefined) data.reportedAt = reportedAt ? new Date(reportedAt) : null;
    if (resolvedAt !== undefined) data.resolvedAt = resolvedAt ? new Date(resolvedAt) : null;

    const defect = await prisma.articleDefect.update({
      where: { id: parseInt(req.params.id) },
      data,
      include: { article: true },
    });
    sendSuccess(res, defect);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteDefect(req: Request, res: Response): Promise<void> {
  try {
    await prisma.articleDefect.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Mangel gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Repairs (Reparaturdokumentation)
export async function getRepairs(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const articleId = req.query.articleId ? parseInt(req.query.articleId as string) : undefined;

    const where: Record<string, unknown> = {};
    if (articleId !== undefined) where.articleId = articleId;

    const [repairs, total] = await Promise.all([
      prisma.articleRepair.findMany({
        where,
        skip,
        take,
        include: { article: true, defect: true },
        orderBy: { repairedAt: 'desc' },
      }),
      prisma.articleRepair.count({ where }),
    ]);

    sendPaginated(res, repairs, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createRepair(req: Request, res: Response): Promise<void> {
  try {
    const { repairedAt, ...rest } = req.body;
    const repair = await prisma.articleRepair.create({
      data: {
        ...rest,
        repairedAt: repairedAt ? new Date(repairedAt) : new Date(),
      },
      include: { article: true, defect: true },
    });
    sendSuccess(res, repair, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateRepair(req: Request, res: Response): Promise<void> {
  try {
    const { repairedAt, ...rest } = req.body;
    const data: Record<string, unknown> = { ...rest };
    if (repairedAt !== undefined) data.repairedAt = repairedAt ? new Date(repairedAt) : null;

    const repair = await prisma.articleRepair.update({
      where: { id: parseInt(req.params.id) },
      data,
      include: { article: true, defect: true },
    });
    sendSuccess(res, repair);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteRepair(req: Request, res: Response): Promise<void> {
  try {
    await prisma.articleRepair.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Reparatur gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
