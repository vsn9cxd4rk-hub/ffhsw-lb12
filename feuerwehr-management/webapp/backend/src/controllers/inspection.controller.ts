import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

// All inspections (paginated, filterable)
export async function getInspections(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const result = req.query.result as string | undefined;

    const where: Record<string, unknown> = {};
    if (result) {
      where.result = result;
    }

    const [inspections, total] = await Promise.all([
      prisma.articleInspection.findMany({
        where,
        skip,
        take,
        include: {
          article: {
            include: { warehouse: true },
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
export async function getDueInspections(_req: Request, res: Response): Promise<void> {
  try {
    const now = new Date();

    // Get all articles with an inspection interval
    const articles = await prisma.article.findMany({
      where: {
        inspectionInterval: { not: null },
      },
      include: {
        warehouse: true,
        inspections: {
          orderBy: { inspectedAt: 'desc' },
          take: 1,
        },
      },
      orderBy: { name: 'asc' },
    });

    // Filter: due if never inspected OR nextDueDate <= now
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
      orderBy: { inspectedAt: 'desc' },
    });
    sendSuccess(res, inspections);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Create inspection with auto-calculated nextDueDate
export async function createInspection(req: Request, res: Response): Promise<void> {
  try {
    const { articleId, inspectedAt, inspectedBy, result, notes } = req.body;

    // Get article to calculate next due date
    const article = await prisma.article.findUnique({ where: { id: articleId } });
    if (!article) { sendError(res, 'Artikel nicht gefunden', 404); return; }

    let nextDueDate: Date | null = null;
    if (article.inspectionInterval) {
      nextDueDate = new Date(inspectedAt);
      nextDueDate.setMonth(nextDueDate.getMonth() + article.inspectionInterval);
    }

    const inspection = await prisma.articleInspection.create({
      data: {
        articleId,
        inspectedAt: new Date(inspectedAt),
        inspectedBy,
        result,
        notes: notes || null,
        nextDueDate,
      },
      include: {
        article: { include: { warehouse: true } },
      },
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
    const { inspectedAt, inspectedBy, result, notes } = req.body;

    // Recalculate nextDueDate if inspectedAt changed
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

    const inspection = await prisma.articleInspection.update({
      where: { id },
      data: {
        inspectedAt: inspectedAt ? new Date(inspectedAt) : undefined,
        inspectedBy,
        result,
        notes: notes !== undefined ? notes || null : undefined,
        nextDueDate,
      },
      include: {
        article: { include: { warehouse: true } },
      },
    });

    sendSuccess(res, inspection);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
