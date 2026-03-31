import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError } from '../utils/response';

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
