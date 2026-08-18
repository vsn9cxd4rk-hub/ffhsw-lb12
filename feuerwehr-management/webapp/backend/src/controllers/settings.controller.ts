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
    const allowed = [
      'application/pdf',
      'application/msword',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
      'application/vnd.ms-excel',
      'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
      'application/vnd.ms-excel.sheet.macroEnabled.12',
    ];
    if (allowed.includes(file.mimetype)) cb(null, true);
    else cb(new Error('Nur PDF-, Word- und Excel-Dateien sind erlaubt'));
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

// Absence Reasons
export async function getAbsenceReasons(_req: Request, res: Response): Promise<void> {
  try {
    const reasons = await prisma.absenceReason.findMany({ orderBy: { id: 'asc' } });
    sendSuccess(res, reasons);
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
    const id = parseInt(req.params.id);
    await prisma.$transaction(async (tx) => {
      const criteria = await tx.inspectionCriterion.findMany({ where: { deviceSubclassId: id }, select: { id: true } });
      const criterionIds = criteria.map(c => c.id);
      if (criterionIds.length > 0) {
        await tx.inspectionCriterionResult.deleteMany({ where: { criterionId: { in: criterionIds } } });
        await tx.inspectionCriterion.deleteMany({ where: { deviceSubclassId: id } });
      }
      await tx.deviceSubclass.delete({ where: { id } });
    });
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
    const id = parseInt(req.params.id);
    await prisma.$transaction(async (tx) => {
      await tx.inspectionCriterionResult.deleteMany({ where: { criterionId: id } });
      await tx.inspectionCriterion.delete({ where: { id } });
    });
    sendSuccess(res, { message: 'Prüfkriterium gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Inspection Types / Prüfungsarten
export async function getInspectionTypes(_req: Request, res: Response): Promise<void> {
  try {
    const types = await prisma.inspectionType.findMany({ orderBy: { name: 'asc' } });
    sendSuccess(res, types);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createInspectionType(req: Request, res: Response): Promise<void> {
  try {
    const t = await prisma.inspectionType.create({ data: { name: req.body.name, description: req.body.description || null } });
    sendSuccess(res, t, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateInspectionType(req: Request, res: Response): Promise<void> {
  try {
    const t = await prisma.inspectionType.update({ where: { id: parseInt(req.params.id) }, data: req.body });
    sendSuccess(res, t);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteInspectionType(req: Request, res: Response): Promise<void> {
  try {
    await prisma.inspectionType.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Prüfungsart gelöscht' });
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
    const deviceClasses = await prisma.deviceClass.findMany();
    const subclasses = await prisma.deviceSubclass.findMany({ include: { deviceClass: true } });
    const existingArticles = await prisma.article.findMany({ where: { inventoryNumber: { not: null } }, select: { inventoryNumber: true } });
    const existingInvNumbers = new Set(existingArticles.map(a => a.inventoryNumber!.toLowerCase()));
    const warehouseMap = new Map(warehouses.map(w => [w.name.toLowerCase(), w.id]));
    const classMap = new Map(deviceClasses.map(c => [c.name.toLowerCase(), c.id]));
    const subclassMap = new Map(subclasses.map(s => [s.name.toLowerCase(), s.id]));
    const subclassWithClassMap = new Map(subclasses.map(s => [`${s.deviceClassId}:${s.name.toLowerCase()}`, s.id]));

    const errors: Array<{ row: number; message: string }> = [];
    let imported = 0;
    let skipped = 0;

    await prisma.$transaction(async (tx) => {
      for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const rowNum = i + 2; // +2: header=1, 0-indexed

        if (!row.name || !row.name.trim()) {
          errors.push({ row: rowNum, message: 'Bezeichnung (name) ist ein Pflichtfeld' });
          continue;
        }

        if (row.inventoryNumber?.trim() && existingInvNumbers.has(row.inventoryNumber.trim().toLowerCase())) {
          skipped++;
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

        // Resolve device class + subclass by name
        let deviceSubclassId: number | null = null;
        const classNameLower = row.deviceClass?.trim().toLowerCase();
        const subclassNameLower = row.deviceSubclass?.trim().toLowerCase();

        if (subclassNameLower) {
          // If both class and subclass given, use precise lookup
          if (classNameLower) {
            const classId = classMap.get(classNameLower);
            if (!classId) {
              errors.push({ row: rowNum, message: `Geräteklasse "${row.deviceClass}" nicht gefunden` });
              continue;
            }
            deviceSubclassId = subclassWithClassMap.get(`${classId}:${subclassNameLower}`) ?? null;
            if (!deviceSubclassId) {
              errors.push({ row: rowNum, message: `Unterklasse "${row.deviceSubclass}" nicht in Geräteklasse "${row.deviceClass}" gefunden` });
              continue;
            }
          } else {
            // Only subclass name given - fallback to plain name lookup
            deviceSubclassId = subclassMap.get(subclassNameLower) ?? null;
            if (!deviceSubclassId) {
              errors.push({ row: rowNum, message: `Unterklasse "${row.deviceSubclass}" nicht gefunden` });
              continue;
            }
          }
        } else if (classNameLower) {
          errors.push({ row: rowNum, message: `Geräteklasse "${row.deviceClass}" angegeben, aber keine Unterklasse` });
          continue;
        }

        // Validate dates
        const parseDateField = (val: string | undefined, label: string): Date | null | 'error' => {
          if (!val?.trim()) return null;
          const d = new Date(val.trim());
          if (isNaN(d.getTime())) {
            errors.push({ row: rowNum, message: `Ungültiges Datum für ${label}: "${val}"` });
            return 'error';
          }
          return d;
        };

        const manufacturingDate = parseDateField(row.manufacturingDate, 'Herstellerdatum');
        if (manufacturingDate === 'error') continue;

        const commissionedDate = parseDateField(row.commissionedDate, 'Indienststellung');
        if (commissionedDate === 'error') continue;

        const decommissionedDate = parseDateField(row.decommissionedDate, 'Außerdienststellung');
        if (decommissionedDate === 'error') continue;

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
              designationLB: row.designationLB?.trim() || 'LB12',
              commissionedDate,
              decommissionedDate,
              communityInventoryNumber: row.communityInventoryNumber?.trim() || null,
              mpFeuerInventoryNumber: row.mpFeuerInventoryNumber?.trim() || null,
              retirementPeriodMonths: row.retirementPeriodMonths ? parseInt(row.retirementPeriodMonths) || null : null,
              isDecommissioned: !!decommissionedDate,
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

    sendSuccess(res, { imported, skipped, errors, total: rows.length });
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

    // Pre-load lookup tables
    const articles = await prisma.article.findMany({
      where: { inventoryNumber: { not: null } },
      select: { id: true, inventoryNumber: true, inspectionInterval: true },
    });
    const articleMap = new Map(articles.filter(a => a.inventoryNumber).map(a => [a.inventoryNumber!, a]));

    const inspectionTypes = await prisma.inspectionType.findMany();
    const inspTypeMap = new Map(inspectionTypes.map(t => [t.name.toLowerCase(), t.id]));

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

        // Resolve inspection type by name
        let inspectionTypeId: number | null = null;
        if (row.inspectionType?.trim()) {
          inspectionTypeId = inspTypeMap.get(row.inspectionType.trim().toLowerCase()) ?? null;
          if (!inspectionTypeId) {
            errors.push({ row: rowNum, message: `Prüfart "${row.inspectionType}" nicht gefunden` });
            continue;
          }
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
              inspectionTypeId,
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

// CSV Import - Members
export async function importMembers(req: Request, res: Response): Promise<void> {
  try {
    const rows: Array<Record<string, string>> = req.body.members;
    if (!Array.isArray(rows) || rows.length === 0) {
      sendError(res, 'Keine Daten zum Importieren', 400);
      return;
    }

    const memberGroups = await prisma.memberGroup.findMany();
    const groupMap = new Map(memberGroups.map(g => [g.name.toLowerCase(), g.id]));

    const existingMembers = await prisma.member.findMany({
      where: { deletedAt: null },
      select: { firstName: true, lastName: true },
    });
    const existingSet = new Set(existingMembers.map(m => `${m.firstName.toLowerCase()}|${m.lastName.toLowerCase()}`));

    const errors: Array<{ row: number; message: string }> = [];
    let imported = 0;
    let skipped = 0;

    const boolFields = [
      'qualLicenseC', 'qualLicenseB', 'qualFirstAid', 'qualRadioOperator',
      'qualMachinist', 'qualTruppmann', 'qualTruppfuehrer', 'qualGruppenfuehrer',
      'qualZugfuehrer', 'qualRettSan', 'qualFwSan', 'qualVerbandfuehrer', 'qualAGT', 'qualTH1',
    ];

    await prisma.$transaction(async (tx) => {
      for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const rowNum = i + 2;

        if (!row.lastName?.trim() || !row.firstName?.trim()) {
          errors.push({ row: rowNum, message: 'Nachname und Vorname sind Pflichtfelder' });
          continue;
        }

        const key = `${row.firstName.trim().toLowerCase()}|${row.lastName.trim().toLowerCase()}`;
        if (existingSet.has(key)) {
          skipped++;
          continue;
        }

        let groupId: number | null = null;
        if (row.group?.trim()) {
          groupId = groupMap.get(row.group.trim().toLowerCase()) ?? null;
          if (!groupId) {
            errors.push({ row: rowNum, message: `Mitgliedergruppe "${row.group}" nicht gefunden` });
            continue;
          }
        }

        const parseDateField = (val: string | undefined): Date | null => {
          if (!val?.trim()) return null;
          const d = new Date(val.trim());
          return isNaN(d.getTime()) ? null : d;
        };

        const data: Record<string, unknown> = {
          lastName: row.lastName.trim(),
          firstName: row.firstName.trim(),
          groupId,
          salutation: row.salutation?.trim() || null,
          street: row.street?.trim() || null,
          city: row.city?.trim() || null,
          phonePrivate: row.phonePrivate?.trim() || null,
          phoneMobile: row.phoneMobile?.trim() || null,
          phoneWork: row.phoneWork?.trim() || null,
          email: row.email?.trim() || null,
          email2: row.email2?.trim() || null,
          occupation: row.occupation?.trim() || null,
          nationality: row.nationality?.trim() || null,
          rank: row.rank?.trim() || null,
          memberSince: parseDateField(row.memberSince),
          birthDate: parseDateField(row.birthDate),
          driverLicenseNo: row.driverLicenseNo?.trim() || null,
          serviceCardNo: row.serviceCardNo?.trim() || null,
          healthInsurance: row.healthInsurance?.trim() || null,
          comment: row.comment?.trim() || null,
        };

        for (const bf of boolFields) {
          if (row[bf] !== undefined) {
            const val = row[bf].trim().toLowerCase();
            data[bf] = val === '1' || val === 'true' || val === 'ja' || val === 'yes' || val === 'x';
          }
        }

        try {
          await tx.member.create({ data: data as { lastName: string; firstName: string; [key: string]: unknown } });
          existingSet.add(key);
          imported++;
        } catch (err: unknown) {
          errors.push({ row: rowNum, message: (err as Error).message });
        }
      }
    });

    sendSuccess(res, { imported, skipped, errors, total: rows.length });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
