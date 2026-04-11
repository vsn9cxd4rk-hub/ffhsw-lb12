import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

// All inspections (paginated, filterable by result, deviceClassId, year)
export async function getInspections(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const result = req.query.result as string | undefined;
    const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId as string) : undefined;
    const year = req.query.year ? parseInt(req.query.year as string) : undefined;

    const where: Record<string, unknown> = {};
    if (result) {
      where.result = result;
    }
    if (deviceClassId) {
      where.article = { deviceSubclass: { deviceClassId } };
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
          criterionResults: {
            include: { criterion: true },
            orderBy: { criterion: { sortOrder: 'asc' } },
          },
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

    const articleWhere: Record<string, unknown> = {
      inspectionInterval: { not: null },
      isDecommissioned: false,
    };
    if (deviceSubclassId) {
      articleWhere.deviceSubclassId = deviceSubclassId;
    } else if (deviceClassId) {
      articleWhere.deviceSubclass = { deviceClassId };
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
        criterionResults: {
          include: { criterion: true },
          orderBy: { criterion: { sortOrder: 'asc' } },
        },
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

// Create inspection with criterion results and auto-calculated result
export async function createInspection(req: Request, res: Response): Promise<void> {
  try {
    const { articleId, inspectedAt, inspectedBy, notes, criterionResults } = req.body;

    const article = await prisma.article.findUnique({
      where: { id: articleId },
      include: { deviceSubclass: { include: { criteria: true } } },
    });
    if (!article) { sendError(res, 'Artikel nicht gefunden', 404); return; }

    // Auto-compute result from criterion results
    let computedResult = 'passed';
    if (criterionResults && criterionResults.length > 0) {
      // Validate all criteria of the subclass are present
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

    let nextDueDate: Date | null = null;
    if (article.inspectionInterval) {
      nextDueDate = new Date(inspectedAt);
      nextDueDate.setMonth(nextDueDate.getMonth() + article.inspectionInterval);
    }

    const inspection = await prisma.$transaction(async (tx) => {
      const insp = await tx.articleInspection.create({
        data: {
          articleId,
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
    const { inspectedAt, inspectedBy, notes, criterionResults } = req.body;

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

    // Auto-compute result if criterion results provided
    let computedResult = existing.result;
    if (criterionResults && criterionResults.length > 0) {
      const hasNio = criterionResults.some((cr: { result: string }) => cr.result === 'nio');
      computedResult = hasNio ? 'failed' : 'passed';
    }

    const inspection = await prisma.$transaction(async (tx) => {
      const insp = await tx.articleInspection.update({
        where: { id },
        data: {
          inspectedAt: inspectedAt ? new Date(inspectedAt) : undefined,
          inspectedBy,
          result: computedResult,
          notes: notes !== undefined ? notes || null : undefined,
          nextDueDate,
        },
      });

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

    const where: Record<string, unknown> = {};
    if (deviceClassId) {
      where.article = { deviceSubclass: { deviceClassId } };
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
