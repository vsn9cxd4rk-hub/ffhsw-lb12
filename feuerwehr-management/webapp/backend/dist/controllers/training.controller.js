"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.certificateUpload = void 0;
exports.getCourses = getCourses;
exports.createCourse = createCourse;
exports.getCourse = getCourse;
exports.updateCourse = updateCourse;
exports.deleteCourse = deleteCourse;
exports.uploadCertificate = uploadCertificate;
exports.downloadCertificate = downloadCertificate;
exports.deleteCertificate = deleteCertificate;
exports.getCategories = getCategories;
exports.createCategory = createCategory;
exports.updateCategory = updateCategory;
exports.deleteCategory = deleteCategory;
const path_1 = __importDefault(require("path"));
const fs_1 = __importDefault(require("fs"));
const multer_1 = __importDefault(require("multer"));
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
const uploadDir = process.env.UPLOAD_PATH || './uploads';
const certificateStorage = multer_1.default.diskStorage({
    destination: (_req, _file, cb) => {
        const dir = path_1.default.join(uploadDir, 'certificates');
        fs_1.default.mkdirSync(dir, { recursive: true });
        cb(null, dir);
    },
    filename: (_req, file, cb) => {
        cb(null, `${Date.now()}-${file.originalname}`);
    },
});
exports.certificateUpload = (0, multer_1.default)({
    storage: certificateStorage,
    limits: { fileSize: 20 * 1024 * 1024 },
    fileFilter: (_req, file, cb) => {
        const allowed = ['application/pdf', 'image/jpeg', 'image/png'];
        if (allowed.includes(file.mimetype))
            cb(null, true);
        else
            cb(new Error('Nur PDF- und Bilddateien sind erlaubt'));
    },
}).single('certificate');
async function getCourses(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const { memberId, categoryId, status, search } = req.query;
        const where = {};
        if (memberId)
            where.memberId = parseInt(memberId);
        if (categoryId)
            where.categoryId = parseInt(categoryId);
        if (status)
            where.status = status;
        if (search) {
            where.member = {
                OR: [
                    { firstName: { contains: search } },
                    { lastName: { contains: search } },
                ],
            };
        }
        const [courses, total] = await Promise.all([
            database_1.prisma.course.findMany({
                where,
                skip,
                take,
                include: {
                    category: true,
                    member: { select: { id: true, firstName: true, lastName: true, rank: true } },
                },
                orderBy: { createdAt: 'desc' },
            }),
            database_1.prisma.course.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, courses, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createCourse(req, res) {
    try {
        const course = await database_1.prisma.course.create({
            data: req.body,
            include: { category: true, member: { select: { id: true, firstName: true, lastName: true } } },
        });
        (0, response_1.sendSuccess)(res, course, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getCourse(req, res) {
    try {
        const course = await database_1.prisma.course.findUnique({
            where: { id: parseInt(req.params.id) },
            include: { category: true, member: true },
        });
        if (!course) {
            (0, response_1.sendError)(res, 'Lehrgang nicht gefunden', 404);
            return;
        }
        (0, response_1.sendSuccess)(res, course);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateCourse(req, res) {
    try {
        const course = await database_1.prisma.course.update({
            where: { id: parseInt(req.params.id) },
            data: req.body,
            include: { category: true },
        });
        (0, response_1.sendSuccess)(res, course);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteCourse(req, res) {
    try {
        await database_1.prisma.course.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Lehrgang gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Certificate upload
async function uploadCertificate(req, res) {
    try {
        const id = parseInt(req.params.id);
        if (!req.file) {
            (0, response_1.sendError)(res, 'Keine Datei hochgeladen', 400);
            return;
        }
        const course = await database_1.prisma.course.findUnique({ where: { id } });
        if (!course) {
            (0, response_1.sendError)(res, 'Lehrgang nicht gefunden', 404);
            return;
        }
        // Delete old certificate if exists
        if (course.certificatePath && fs_1.default.existsSync(course.certificatePath)) {
            fs_1.default.unlinkSync(course.certificatePath);
        }
        const updated = await database_1.prisma.course.update({
            where: { id },
            data: { certificatePath: req.file.path },
            include: { category: true, member: { select: { id: true, firstName: true, lastName: true, rank: true } } },
        });
        (0, response_1.sendSuccess)(res, updated);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function downloadCertificate(req, res) {
    try {
        const course = await database_1.prisma.course.findUnique({ where: { id: parseInt(req.params.id) } });
        if (!course || !course.certificatePath) {
            (0, response_1.sendError)(res, 'Keine Urkunde vorhanden', 404);
            return;
        }
        if (!fs_1.default.existsSync(course.certificatePath)) {
            (0, response_1.sendError)(res, 'Datei nicht gefunden', 404);
            return;
        }
        const fileName = path_1.default.basename(course.certificatePath);
        res.setHeader('Content-Disposition', `attachment; filename="${encodeURIComponent(fileName)}"`);
        fs_1.default.createReadStream(course.certificatePath).pipe(res);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteCertificate(req, res) {
    try {
        const id = parseInt(req.params.id);
        const course = await database_1.prisma.course.findUnique({ where: { id } });
        if (!course) {
            (0, response_1.sendError)(res, 'Lehrgang nicht gefunden', 404);
            return;
        }
        if (course.certificatePath && fs_1.default.existsSync(course.certificatePath)) {
            fs_1.default.unlinkSync(course.certificatePath);
        }
        await database_1.prisma.course.update({ where: { id }, data: { certificatePath: null } });
        (0, response_1.sendSuccess)(res, { message: 'Urkunde gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Categories
async function getCategories(_req, res) {
    try {
        const cats = await database_1.prisma.courseCategory.findMany({ orderBy: { name: 'asc' } });
        (0, response_1.sendSuccess)(res, cats);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function createCategory(req, res) {
    try {
        const cat = await database_1.prisma.courseCategory.create({ data: req.body });
        (0, response_1.sendSuccess)(res, cat, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function updateCategory(req, res) {
    try {
        const cat = await database_1.prisma.courseCategory.update({
            where: { id: parseInt(req.params.id) },
            data: req.body,
        });
        (0, response_1.sendSuccess)(res, cat);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function deleteCategory(req, res) {
    try {
        await database_1.prisma.courseCategory.delete({ where: { id: parseInt(req.params.id) } });
        (0, response_1.sendSuccess)(res, { message: 'Kategorie gelöscht' });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=training.controller.js.map