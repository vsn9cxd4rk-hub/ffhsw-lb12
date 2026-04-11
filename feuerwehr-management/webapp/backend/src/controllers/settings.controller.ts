import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError } from '../utils/response';

const uploadDir = process.env.UPLOAD_PATH || './uploads';

const templateStorage = multer.diskStorage({
  destination: (_req, _file, cb) => {
    const dir = path.join(uploadDir, 'templates');
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  },
});

export const templateUpload = multer({
  storage: templateStorage,
  limits: { fileSize: 20 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    const allowed = ['application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
    if (allowed.includes(file.mimetype)) cb(null, true);
    else cb(new Error('Nur PDF- und Word-Dateien sind erlaubt'));
  },
}).single('file');

export async function getSettings(_req: Request, res: Response): Promise<void> {
  try {
    const settings = await prisma.setting.findMany({ orderBy: { key: 'asc' } });
    const map = Object.fromEntries(settings.map(s => [s.key, s.value]));
    sendSuccess(res, map);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateSettings(req: Request, res: Response): Promise<void> {
  try {
    const updates: Record<string, string> = req.body;
    await Promise.all(
      Object.entries(updates).map(([key, value]) =>
        prisma.setting.upsert({
          where: { key },
          update: { value: String(value) },
          create: { key, value: String(value) },
        })
      )
    );
    sendSuccess(res, { message: 'Einstellungen gespeichert' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Ranks
export async function getRanks(_req: Request, res: Response): Promise<void> {
  try {
    const ranks = await prisma.rank.findMany({ orderBy: { sortOrder: 'asc' } });
    sendSuccess(res, ranks);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createRank(req: Request, res: Response): Promise<void> {
  try {
    const rank = await prisma.rank.create({ data: req.body });
    sendSuccess(res, rank, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateRank(req: Request, res: Response): Promise<void> {
  try {
    const rank = await prisma.rank.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, rank);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteRank(req: Request, res: Response): Promise<void> {
  try {
    await prisma.rank.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Dienstgrad gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Years
export async function getYears(_req: Request, res: Response): Promise<void> {
  try {
    const years = await prisma.year.findMany({ orderBy: { year: 'desc' } });
    sendSuccess(res, years);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createYear(req: Request, res: Response): Promise<void> {
  try {
    const y = await prisma.year.create({ data: req.body });
    sendSuccess(res, y, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateYear(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    if (req.body.isActive) {
      await prisma.year.updateMany({ where: { id: { not: id } }, data: { isActive: false } });
    }
    const y = await prisma.year.update({ where: { id }, data: req.body });
    sendSuccess(res, y);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Templates
export async function getTemplates(_req: Request, res: Response): Promise<void> {
  try {
    const templates = await prisma.template.findMany({ orderBy: { name: 'asc' } });
    sendSuccess(res, templates);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function uploadTemplate(req: Request, res: Response): Promise<void> {
  try {
    if (!req.file) { sendError(res, 'Keine Datei hochgeladen', 400); return; }
    const name = req.body.name?.trim();
    if (!name) { sendError(res, 'Name ist erforderlich', 400); return; }

    const template = await prisma.template.create({
      data: {
        name,
        filePath: req.file.path,
        fileSize: req.file.size,
        mimeType: req.file.mimetype,
        createdBy: req.user?.username || 'System',
      },
    });

    await prisma.templateHistory.create({
      data: { templateId: template.id, action: 'Erstellt', changedBy: req.user?.username || 'System' },
    });

    sendSuccess(res, template, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateTemplate(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const existing = await prisma.template.findUnique({ where: { id } });
    if (!existing) { sendError(res, 'Template nicht gefunden', 404); return; }

    const data: Record<string, unknown> = { updatedBy: req.user?.username || 'System' };
    if (req.body.name) data.name = req.body.name.trim();

    if (req.file) {
      if (fs.existsSync(existing.filePath)) fs.unlinkSync(existing.filePath);
      data.filePath = req.file.path;
      data.fileSize = req.file.size;
      data.mimeType = req.file.mimetype;
    }

    const template = await prisma.template.update({ where: { id }, data });

    await prisma.templateHistory.create({
      data: { templateId: id, action: req.file ? 'Datei ersetzt' : 'Aktualisiert', changedBy: req.user?.username || 'System' },
    });

    sendSuccess(res, template);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function downloadTemplate(req: Request, res: Response): Promise<void> {
  try {
    const template = await prisma.template.findUnique({ where: { id: parseInt(req.params.id) } });
    if (!template) { sendError(res, 'Template nicht gefunden', 404); return; }
    if (!fs.existsSync(template.filePath)) { sendError(res, 'Datei nicht gefunden', 404); return; }

    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(template.name + path.extname(template.filePath))}"`);
    res.setHeader('Content-Type', template.mimeType);
    fs.createReadStream(template.filePath).pipe(res);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteTemplate(req: Request, res: Response): Promise<void> {
  try {
    const template = await prisma.template.findUnique({ where: { id: parseInt(req.params.id) } });
    if (!template) { sendError(res, 'Template nicht gefunden', 404); return; }
    if (fs.existsSync(template.filePath)) fs.unlinkSync(template.filePath);
    await prisma.template.delete({ where: { id: template.id } });
    sendSuccess(res, { message: 'Template gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getTemplateHistory(req: Request, res: Response): Promise<void> {
  try {
    const history = await prisma.templateHistory.findMany({
      where: { templateId: parseInt(req.params.id) },
      orderBy: { changedAt: 'desc' },
    });
    sendSuccess(res, history);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Device Classes / Geräteklassen
export async function getDeviceClasses(_req: Request, res: Response): Promise<void> {
  try {
    const classes = await prisma.deviceClass.findMany({
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
    sendSuccess(res, classes);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createDeviceClass(req: Request, res: Response): Promise<void> {
  try {
    const dc = await prisma.deviceClass.create({ data: { name: req.body.name, sortOrder: req.body.sortOrder || 0 } });
    sendSuccess(res, dc, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateDeviceClass(req: Request, res: Response): Promise<void> {
  try {
    const dc = await prisma.deviceClass.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, dc);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteDeviceClass(req: Request, res: Response): Promise<void> {
  try {
    await prisma.deviceClass.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Geräteklasse gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createSubclass(req: Request, res: Response): Promise<void> {
  try {
    const classId = parseInt(req.params.classId);
    const sc = await prisma.deviceSubclass.create({
      data: { deviceClassId: classId, name: req.body.name, sortOrder: req.body.sortOrder || 0 },
    });
    sendSuccess(res, sc, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateSubclass(req: Request, res: Response): Promise<void> {
  try {
    const sc = await prisma.deviceSubclass.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, sc);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteSubclass(req: Request, res: Response): Promise<void> {
  try {
    await prisma.deviceSubclass.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Unterklasse gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createCriterion(req: Request, res: Response): Promise<void> {
  try {
    const subclassId = parseInt(req.params.subclassId);
    const criterion = await prisma.inspectionCriterion.create({
      data: { deviceSubclassId: subclassId, name: req.body.name, sortOrder: req.body.sortOrder || 0 },
    });
    sendSuccess(res, criterion, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateCriterion(req: Request, res: Response): Promise<void> {
  try {
    const criterion = await prisma.inspectionCriterion.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, criterion);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteCriterion(req: Request, res: Response): Promise<void> {
  try {
    await prisma.inspectionCriterion.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Prüfkriterium gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// CSV Import - Articles
export async function importArticles(req: Request, res: Response): Promise<void> {
  try {
    const rows: Array<Record<string, string>> = req.body.articles;
    if (!Array.isArray(rows) || rows.length === 0) {
      sendError(res, 'Keine Daten zum Importieren', 400);
      return;
    }

    // Pre-load lookup tables
    const warehouses = await prisma.warehouse.findMany();
    const subclasses = await prisma.deviceSubclass.findMany();
    const warehouseMap = new Map(warehouses.map(w => [w.name.toLowerCase(), w.id]));
    const subclassMap = new Map(subclasses.map(s => [s.name.toLowerCase(), s.id]));

    const errors: Array<{ row: number; message: string }> = [];
    let imported = 0;

    await prisma.$transaction(async (tx) => {
      for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const rowNum = i + 2; // +2: header=1, 0-indexed

        if (!row.name || !row.name.trim()) {
          errors.push({ row: rowNum, message: 'Bezeichnung (name) ist ein Pflichtfeld' });
          continue;
        }

        // Resolve warehouse by name
        let warehouseId: number | null = null;
        if (row.warehouse?.trim()) {
          warehouseId = warehouseMap.get(row.warehouse.trim().toLowerCase()) ?? null;
          if (!warehouseId) {
            errors.push({ row: rowNum, message: `Lagerort "${row.warehouse}" nicht gefunden` });
            continue;
          }
        }

        // Resolve device subclass by name
        let deviceSubclassId: number | null = null;
        if (row.deviceSubclass?.trim()) {
          deviceSubclassId = subclassMap.get(row.deviceSubclass.trim().toLowerCase()) ?? null;
          if (!deviceSubclassId) {
            errors.push({ row: rowNum, message: `Unterklasse "${row.deviceSubclass}" nicht gefunden` });
            continue;
          }
        }

        // Validate date
        let manufacturingDate: Date | null = null;
        if (row.manufacturingDate?.trim()) {
          manufacturingDate = new Date(row.manufacturingDate.trim());
          if (isNaN(manufacturingDate.getTime())) {
            errors.push({ row: rowNum, message: `Ungültiges Herstellerdatum "${row.manufacturingDate}"` });
            continue;
          }
        }

        try {
          await tx.article.create({
            data: {
              name: row.name.trim(),
              inventoryNumber: row.inventoryNumber?.trim() || null,
              manufacturer: row.manufacturer?.trim() || null,
              articleType: row.articleType?.trim() || null,
              description: row.description?.trim() || null,
              inspectionInterval: row.inspectionInterval ? parseInt(row.inspectionInterval) || null : null,
              value: row.value ? parseFloat(row.value) || null : null,
              ean: row.ean?.trim() || null,
              serialNumber: row.serialNumber?.trim() || null,
              din: row.din?.trim() || null,
              specification: row.specification?.trim() || null,
              manufacturingDate,
              warehouseId,
              deviceSubclassId,
              isDecommissioned: false,
            },
          });
          imported++;
        } catch (err: unknown) {
          const msg = (err as { code?: string; meta?: { target?: string[] } }).code === 'P2002'
            ? `Inventarnummer "${row.inventoryNumber}" existiert bereits`
            : (err as Error).message;
          errors.push({ row: rowNum, message: msg });
        }
      }
    });

    sendSuccess(res, { imported, errors, total: rows.length });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// CSV Import - Inspections
export async function importInspections(req: Request, res: Response): Promise<void> {
  try {
    const rows: Array<Record<string, string>> = req.body.inspections;
    if (!Array.isArray(rows) || rows.length === 0) {
      sendError(res, 'Keine Daten zum Importieren', 400);
      return;
    }

    const errors: Array<{ row: number; message: string }> = [];
    let imported = 0;

    // Pre-load articles by inventory number
    const articles = await prisma.article.findMany({
      where: { inventoryNumber: { not: null } },
      select: { id: true, inventoryNumber: true, inspectionInterval: true },
    });
    const articleMap = new Map(articles.filter(a => a.inventoryNumber).map(a => [a.inventoryNumber!, a]));

    await prisma.$transaction(async (tx) => {
      for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const rowNum = i + 2;

        // Validate required fields
        if (!row.inventoryNumber?.trim()) {
          errors.push({ row: rowNum, message: 'Inventarnummer ist ein Pflichtfeld' });
          continue;
        }
        if (!row.inspectedAt?.trim()) {
          errors.push({ row: rowNum, message: 'Prüfdatum ist ein Pflichtfeld' });
          continue;
        }
        if (!row.inspectedBy?.trim()) {
          errors.push({ row: rowNum, message: 'Prüfer ist ein Pflichtfeld' });
          continue;
        }
        if (!row.result?.trim() || !['passed', 'failed'].includes(row.result.trim().toLowerCase())) {
          errors.push({ row: rowNum, message: 'Ergebnis muss "passed" oder "failed" sein' });
          continue;
        }

        // Resolve article
        const article = articleMap.get(row.inventoryNumber.trim());
        if (!article) {
          errors.push({ row: rowNum, message: `Artikel mit Inventarnummer "${row.inventoryNumber}" nicht gefunden` });
          continue;
        }

        // Validate date
        const inspectedAt = new Date(row.inspectedAt.trim());
        if (isNaN(inspectedAt.getTime())) {
          errors.push({ row: rowNum, message: `Ungültiges Prüfdatum "${row.inspectedAt}"` });
          continue;
        }

        // Calculate next due date
        let nextDueDate: Date | null = null;
        if (article.inspectionInterval) {
          nextDueDate = new Date(inspectedAt);
          nextDueDate.setMonth(nextDueDate.getMonth() + article.inspectionInterval);
        }

        try {
          await tx.articleInspection.create({
            data: {
              articleId: article.id,
              inspectedAt,
              inspectedBy: row.inspectedBy.trim(),
              result: row.result.trim().toLowerCase(),
              notes: row.notes?.trim() || null,
              nextDueDate,
            },
          });
          imported++;
        } catch (err: unknown) {
          errors.push({ row: rowNum, message: (err as Error).message });
        }
      }
    });

    sendSuccess(res, { imported, errors, total: rows.length });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
