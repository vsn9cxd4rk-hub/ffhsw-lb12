import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

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
    const { deviceSubclassId, manufacturingDate, specification, serialNumber, din, isDecommissioned, ...rest } = req.body;
    const article = await prisma.article.create({
      data: {
        ...rest,
        deviceSubclassId: deviceSubclassId || null,
        manufacturingDate: manufacturingDate ? new Date(manufacturingDate) : null,
        specification: specification || null,
        serialNumber: serialNumber || null,
        din: din || null,
        isDecommissioned: isDecommissioned || false,
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
    const { deviceSubclassId, manufacturingDate, ...rest } = req.body;
    const data: Record<string, unknown> = { ...rest };
    if (deviceSubclassId !== undefined) data.deviceSubclassId = deviceSubclassId || null;
    if (manufacturingDate !== undefined) data.manufacturingDate = manufacturingDate ? new Date(manufacturingDate) : null;

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
