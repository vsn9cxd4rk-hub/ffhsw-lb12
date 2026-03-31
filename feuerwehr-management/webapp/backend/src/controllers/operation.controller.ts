import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

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
