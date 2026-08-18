import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError } from '../utils/response';
import { convertDates } from '../utils/dates';

const LOGBOOK_DATE_FIELDS = ['date'];
const EQUIPMENT_INSPECTION_DATE_FIELDS = ['lastInspection', 'nextInspection'];

export async function getVehicles(req: Request, res: Response): Promise<void> {
  try {
    const isRetired = req.query.isRetired === 'true' ? true : req.query.isRetired === 'false' ? false : undefined;
    const where = isRetired !== undefined ? { isRetired } : {};

    const vehicles = await prisma.vehicle.findMany({
      where,
      include: { inspection: true },
      orderBy: { sortOrder: 'asc' },
    });
    sendSuccess(res, vehicles);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createVehicle(req: Request, res: Response): Promise<void> {
  try {
    const vehicle = await prisma.vehicle.create({
      data: req.body,
      include: { inspection: true },
    });
    sendSuccess(res, vehicle, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getVehicle(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const vehicle = await prisma.vehicle.findUnique({
      where: { id },
      include: {
        inspection: true,
        equipmentInspections: true,
        warehouses: true,
        logbook: { orderBy: { date: 'desc' }, take: 20 },
      },
    });
    if (!vehicle) { sendError(res, 'Fahrzeug nicht gefunden', 404); return; }
    sendSuccess(res, vehicle);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateVehicle(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const vehicle = await prisma.vehicle.update({
      where: { id },
      data: req.body,
      include: { inspection: true },
    });
    sendSuccess(res, vehicle);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteVehicle(req: Request, res: Response): Promise<void> {
  try {
    await prisma.vehicle.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Fahrzeug gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function upsertVehicleInspection(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = parseInt(req.params.id);
    const data: Record<string, unknown> = {};
    const dateFields = ['tuevDate', 'spDate', 'serviceDate'];
    const boolFields = ['notifyTuev', 'notifySp', 'notifyService'];

    for (const f of dateFields) {
      if (f in req.body) {
        data[f] = req.body[f] ? new Date(req.body[f]) : null;
      }
    }
    for (const f of boolFields) {
      if (f in req.body) data[f] = req.body[f];
    }
    if ('notes' in req.body) data.notes = req.body.notes || null;

    const inspection = await prisma.vehicleInspection.upsert({
      where: { vehicleId },
      update: data,
      create: { ...data, vehicleId },
    });
    sendSuccess(res, inspection);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getLogbook(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = parseInt(req.params.id);
    const page = parseInt(req.query.page as string) || 1;
    const limit = 20;

    const [entries, total] = await Promise.all([
      prisma.logbookEntry.findMany({
        where: { vehicleId },
        orderBy: { date: 'desc' },
        skip: (page - 1) * limit,
        take: limit,
      }),
      prisma.logbookEntry.count({ where: { vehicleId } }),
    ]);
    sendSuccess(res, { data: entries, total, page, pages: Math.ceil(total / limit) });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createLogbookEntry(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = parseInt(req.params.id);
    const entry = await prisma.logbookEntry.create({
      data: { ...convertDates(req.body, LOGBOOK_DATE_FIELDS), vehicleId } as any,
    });
    sendSuccess(res, entry, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateLogbookEntry(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.entryId);
    const entry = await prisma.logbookEntry.update({
      where: { id },
      data: convertDates(req.body, LOGBOOK_DATE_FIELDS) as any,
    });
    sendSuccess(res, entry);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getEquipmentInspections(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = parseInt(req.params.id);
    const inspections = await prisma.equipmentInspection.findMany({
      where: { vehicleId },
      orderBy: { type: 'asc' },
    });
    sendSuccess(res, inspections);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createEquipmentInspection(req: Request, res: Response): Promise<void> {
  try {
    const vehicleId = parseInt(req.params.id);
    const inspection = await prisma.equipmentInspection.create({
      data: { ...convertDates(req.body, EQUIPMENT_INSPECTION_DATE_FIELDS), vehicleId } as any,
    });
    sendSuccess(res, inspection, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateEquipmentInspection(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.inspId);
    const inspection = await prisma.equipmentInspection.update({
      where: { id },
      data: convertDates(req.body, EQUIPMENT_INSPECTION_DATE_FIELDS) as any,
    });
    sendSuccess(res, inspection);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
