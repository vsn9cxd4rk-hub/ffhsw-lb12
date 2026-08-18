import { Request, Response } from 'express';
import path from 'path';
import fs from 'fs';
import multer from 'multer';
import { prisma } from '../config/database';
import { sendSuccess, sendError, sendPaginated } from '../utils/response';
import { getPagination } from '../utils/pagination';
import { convertDates } from '../utils/dates';

const COURSE_DATE_FIELDS = ['startDate', 'endDate'];

const uploadDir = process.env.UPLOAD_PATH || './uploads';

const certificateStorage = multer.diskStorage({
  destination: (_req, _file, cb) => {
    const dir = path.join(uploadDir, 'certificates');
    fs.mkdirSync(dir, { recursive: true });
    cb(null, dir);
  },
  filename: (_req, file, cb) => {
    cb(null, `${Date.now()}-${file.originalname}`);
  },
});

export const certificateUpload = multer({
  storage: certificateStorage,
  limits: { fileSize: 20 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    const allowed = ['application/pdf', 'image/jpeg', 'image/png'];
    if (allowed.includes(file.mimetype)) cb(null, true);
    else cb(new Error('Nur PDF- und Bilddateien sind erlaubt'));
  },
}).single('certificate');

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
      data: convertDates(req.body, COURSE_DATE_FIELDS) as any,
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
      data: convertDates(req.body, COURSE_DATE_FIELDS) as any,
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

// Certificate upload
export async function uploadCertificate(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    if (!req.file) { sendError(res, 'Keine Datei hochgeladen', 400); return; }

    const course = await prisma.course.findUnique({ where: { id } });
    if (!course) { sendError(res, 'Lehrgang nicht gefunden', 404); return; }

    // Delete old certificate if exists
    if (course.certificatePath && fs.existsSync(course.certificatePath)) {
      fs.unlinkSync(course.certificatePath);
    }

    const updated = await prisma.course.update({
      where: { id },
      data: { certificatePath: req.file.path },
      include: { category: true, member: { select: { id: true, firstName: true, lastName: true, rank: true } } },
    });
    sendSuccess(res, updated);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function downloadCertificate(req: Request, res: Response): Promise<void> {
  try {
    const course = await prisma.course.findUnique({ where: { id: parseInt(req.params.id) } });
    if (!course || !course.certificatePath) { sendError(res, 'Keine Urkunde vorhanden', 404); return; }

    if (!fs.existsSync(course.certificatePath)) {
      sendError(res, 'Datei nicht gefunden', 404);
      return;
    }

    const fileName = path.basename(course.certificatePath);
    res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(fileName)}"`);
    fs.createReadStream(course.certificatePath).pipe(res);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function deleteCertificate(req: Request, res: Response): Promise<void> {
  try {
    const id = parseInt(req.params.id);
    const course = await prisma.course.findUnique({ where: { id } });
    if (!course) { sendError(res, 'Lehrgang nicht gefunden', 404); return; }

    if (course.certificatePath && fs.existsSync(course.certificatePath)) {
      fs.unlinkSync(course.certificatePath);
    }

    await prisma.course.update({ where: { id }, data: { certificatePath: null } });
    sendSuccess(res, { message: 'Urkunde gelöscht' });
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
