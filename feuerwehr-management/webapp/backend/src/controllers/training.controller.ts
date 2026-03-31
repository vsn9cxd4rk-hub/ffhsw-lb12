import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';

export async function getCourses(req: Request, res: Response): Promise<void> {
  try {
    const { skip, take, page, limit } = getPagination(req);
    const { memberId, categoryId, status, search } = req.query;

    const where: Record<string, unknown> = {};
    if (memberId) where.memberId = parseInt(memberId as string);
    if (categoryId) where.categoryId = parseInt(categoryId as string);
    if (status) where.status = status as string;
    if (search) {
      where.member = {
        OR: [
          { firstName: { contains: search as string } },
          { lastName: { contains: search as string } },
        ],
      };
    }

    const [courses, total] = await Promise.all([
      prisma.course.findMany({
        where,
        skip,
        take,
        include: {
          category: true,
          member: { select: { id: true, firstName: true, lastName: true, rank: true } },
        },
        orderBy: { createdAt: 'desc' },
      }),
      prisma.course.count({ where }),
    ]);

    sendPaginated(res, courses, total, page, limit);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createCourse(req: Request, res: Response): Promise<void> {
  try {
    const course = await prisma.course.create({
      data: req.body,
      include: { category: true, member: { select: { id: true, firstName: true, lastName: true } } },
    });
    sendSuccess(res, course, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getCourse(req: Request, res: Response): Promise<void> {
  try {
    const course = await prisma.course.findUnique({
      where: { id: parseInt(req.params.id) },
      include: { category: true, member: true },
    });
    if (!course) { sendError(res, 'Lehrgang nicht gefunden', 404); return; }
    sendSuccess(res, course);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateCourse(req: Request, res: Response): Promise<void> {
  try {
    const course = await prisma.course.update({
      where: { id: parseInt(req.params.id) },
      data: req.body,
      include: { category: true },
    });
    sendSuccess(res, course);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteCourse(req: Request, res: Response): Promise<void> {
  try {
    await prisma.course.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Lehrgang gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

// Categories
export async function getCategories(_req: Request, res: Response): Promise<void> {
  try {
    const cats = await prisma.courseCategory.findMany({ orderBy: { name: 'asc' } });
    sendSuccess(res, cats);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function createCategory(req: Request, res: Response): Promise<void> {
  try {
    const cat = await prisma.courseCategory.create({ data: req.body });
    sendSuccess(res, cat, 201);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function updateCategory(req: Request, res: Response): Promise<void> {
  try {
    const cat = await prisma.courseCategory.update({
      where: { id: parseInt(req.params.id) },
      data: req.body,
    });
    sendSuccess(res, cat);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteCategory(req: Request, res: Response): Promise<void> {
  try {
    await prisma.courseCategory.delete({ where: { id: parseInt(req.params.id) } });
    sendSuccess(res, { message: 'Kategorie gelöscht' });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
