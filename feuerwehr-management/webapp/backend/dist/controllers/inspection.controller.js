"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getInspections = getInspections;
exports.getDueInspections = getDueInspections;
exports.getArticleInspections = getArticleInspections;
exports.getInspectionCriteria = getInspectionCriteria;
exports.createInspection = createInspection;
exports.updateInspection = updateInspection;
exports.getInspectionReport = getInspectionReport;
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
const pagination_1 = require("../utils/pagination");
// All inspections (paginated, filterable by result, deviceClassId, year)
async function getInspections(req, res) {
    try {
        const { skip, take, page, limit } = (0, pagination_1.getPagination)(req);
        const result = req.query.result;
        const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId) : undefined;
        const year = req.query.year ? parseInt(req.query.year) : undefined;
        const where = {};
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
            database_1.prisma.articleInspection.findMany({
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
            database_1.prisma.articleInspection.count({ where }),
        ]);
        (0, response_1.sendPaginated)(res, inspections, total, page, limit);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Articles that are due for inspection
async function getDueInspections(req, res) {
    try {
        const now = new Date();
        const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId) : undefined;
        const deviceSubclassId = req.query.deviceSubclassId ? parseInt(req.query.deviceSubclassId) : undefined;
        const articleWhere = {
            inspectionInterval: { not: null },
            isDecommissioned: false,
        };
        if (deviceSubclassId) {
            articleWhere.deviceSubclassId = deviceSubclassId;
        }
        else if (deviceClassId) {
            articleWhere.deviceSubclass = { deviceClassId };
        }
        const articles = await database_1.prisma.article.findMany({
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
            if (article.inspections.length === 0)
                return true;
            const lastInspection = article.inspections[0];
            return lastInspection.nextDueDate && lastInspection.nextDueDate <= now;
        });
        (0, response_1.sendSuccess)(res, dueArticles);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Inspection history for a specific article
async function getArticleInspections(req, res) {
    try {
        const articleId = parseInt(req.params.articleId);
        const inspections = await database_1.prisma.articleInspection.findMany({
            where: { articleId },
            include: {
                criterionResults: {
                    include: { criterion: true },
                    orderBy: { criterion: { sortOrder: 'asc' } },
                },
            },
            orderBy: { inspectedAt: 'desc' },
        });
        (0, response_1.sendSuccess)(res, inspections);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Get inspection criteria for an article based on its subclass
async function getInspectionCriteria(req, res) {
    try {
        const articleId = parseInt(req.params.articleId);
        const article = await database_1.prisma.article.findUnique({
            where: { id: articleId },
            select: { deviceSubclassId: true },
        });
        if (!article) {
            (0, response_1.sendError)(res, 'Artikel nicht gefunden', 404);
            return;
        }
        if (!article.deviceSubclassId) {
            (0, response_1.sendSuccess)(res, []);
            return;
        }
        const criteria = await database_1.prisma.inspectionCriterion.findMany({
            where: { deviceSubclassId: article.deviceSubclassId },
            orderBy: { sortOrder: 'asc' },
        });
        (0, response_1.sendSuccess)(res, criteria);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Create inspection with criterion results and auto-calculated result
async function createInspection(req, res) {
    try {
        const { articleId, inspectedAt, inspectedBy, notes, criterionResults } = req.body;
        const article = await database_1.prisma.article.findUnique({
            where: { id: articleId },
            include: { deviceSubclass: { include: { criteria: true } } },
        });
        if (!article) {
            (0, response_1.sendError)(res, 'Artikel nicht gefunden', 404);
            return;
        }
        // Auto-compute result from criterion results
        let computedResult = 'passed';
        if (criterionResults && criterionResults.length > 0) {
            // Validate all criteria of the subclass are present
            if (article.deviceSubclass) {
                const requiredIds = article.deviceSubclass.criteria.map((c) => c.id);
                const providedIds = criterionResults.map((cr) => cr.criterionId);
                const missing = requiredIds.filter((id) => !providedIds.includes(id));
                if (missing.length > 0) {
                    (0, response_1.sendError)(res, 'Nicht alle Prüfkriterien wurden bewertet', 400);
                    return;
                }
            }
            const hasNio = criterionResults.some((cr) => cr.result === 'nio');
            computedResult = hasNio ? 'failed' : 'passed';
        }
        let nextDueDate = null;
        if (article.inspectionInterval) {
            nextDueDate = new Date(inspectedAt);
            nextDueDate.setMonth(nextDueDate.getMonth() + article.inspectionInterval);
        }
        const inspection = await database_1.prisma.$transaction(async (tx) => {
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
                    data: criterionResults.map((cr) => ({
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
        (0, response_1.sendSuccess)(res, inspection, 201);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Update inspection
async function updateInspection(req, res) {
    try {
        const id = parseInt(req.params.id);
        const { inspectedAt, inspectedBy, notes, criterionResults } = req.body;
        const existing = await database_1.prisma.articleInspection.findUnique({
            where: { id },
            include: { article: true },
        });
        if (!existing) {
            (0, response_1.sendError)(res, 'Prüfung nicht gefunden', 404);
            return;
        }
        let nextDueDate = existing.nextDueDate;
        if (inspectedAt && existing.article.inspectionInterval) {
            nextDueDate = new Date(inspectedAt);
            nextDueDate.setMonth(nextDueDate.getMonth() + existing.article.inspectionInterval);
        }
        // Auto-compute result if criterion results provided
        let computedResult = existing.result;
        if (criterionResults && criterionResults.length > 0) {
            const hasNio = criterionResults.some((cr) => cr.result === 'nio');
            computedResult = hasNio ? 'failed' : 'passed';
        }
        const inspection = await database_1.prisma.$transaction(async (tx) => {
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
                    data: criterionResults.map((cr) => ({
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
        (0, response_1.sendSuccess)(res, inspection);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
// Report endpoint: full inspection details for PDF generation
async function getInspectionReport(req, res) {
    try {
        const deviceClassId = req.query.deviceClassId ? parseInt(req.query.deviceClassId) : undefined;
        const year = req.query.year ? parseInt(req.query.year) : undefined;
        const where = {};
        if (deviceClassId) {
            where.article = { deviceSubclass: { deviceClassId } };
        }
        if (year) {
            where.inspectedAt = {
                gte: new Date(`${year}-01-01`),
                lt: new Date(`${year + 1}-01-01`),
            };
        }
        const inspections = await database_1.prisma.articleInspection.findMany({
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
        (0, response_1.sendSuccess)(res, inspections);
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=inspection.controller.js.map