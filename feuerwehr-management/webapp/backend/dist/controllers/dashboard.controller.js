"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getDashboardStats = getDashboardStats;
exports.getStatistics = getStatistics;
const database_1 = require("../config/database");
const response_1 = require("../utils/response");
async function getDashboardStats(_req, res) {
    try {
        const currentYear = new Date().getFullYear();
        const yearStart = new Date(currentYear, 0, 1);
        const yearEnd = new Date(currentYear + 1, 0, 1);
        const reminderDays = 30;
        const reminderDate = new Date();
        reminderDate.setDate(reminderDate.getDate() + reminderDays);
        const [activeMembers, vehiclesCount, operationsThisYear, recentOperations, upcomingVehicleInspections, upcomingEquipmentInspections, upcomingMedicalExams, articlesWithInterval,] = await Promise.all([
            database_1.prisma.member.count({ where: { isInactive: false, deletedAt: null } }),
            database_1.prisma.vehicle.count({ where: { isRetired: false } }),
            database_1.prisma.operation.count({ where: { date: { gte: yearStart, lt: yearEnd } } }),
            database_1.prisma.operation.findMany({
                orderBy: [{ date: 'desc' }, { alarmTime: 'desc' }],
                take: 5,
                select: { id: true, date: true, location: true, keyword: true, leaderCount: true, memberCount: true },
            }),
            database_1.prisma.vehicleInspection.findMany({
                where: {
                    OR: [
                        { tuevDate: { lte: reminderDate, gte: new Date() } },
                        { spDate: { lte: reminderDate, gte: new Date() } },
                        { serviceDate: { lte: reminderDate, gte: new Date() } },
                    ],
                },
                include: { vehicle: { select: { name: true } } },
            }),
            database_1.prisma.equipmentInspection.findMany({
                where: { nextInspection: { lte: reminderDate, gte: new Date() } },
                include: { vehicle: { select: { name: true } } },
            }),
            database_1.prisma.memberExamination.findMany({
                where: {
                    OR: [
                        { g25Date: { lte: reminderDate, gte: new Date() } },
                        { g26Date: { lte: reminderDate, gte: new Date() } },
                        { g30Date: { lte: reminderDate, gte: new Date() } },
                        { lkwLicenseExpiry: { lte: reminderDate, gte: new Date() } },
                    ],
                },
                include: { member: { select: { firstName: true, lastName: true } } },
            }),
            // Articles with inspection interval and their latest inspection
            database_1.prisma.article.findMany({
                where: { inspectionInterval: { not: null } },
                include: {
                    inspections: { orderBy: { inspectedAt: 'desc' }, take: 1 },
                },
            }),
        ]);
        // Build upcoming inspections list
        const upcomingInspections = [];
        // Article inspections: due if never inspected or nextDueDate <= reminderDate
        const now = new Date();
        for (const article of articlesWithInterval) {
            if (article.inspections.length === 0) {
                // Never inspected — due immediately
                upcomingInspections.push({
                    type: 'Geräteprüfung',
                    entityName: article.name,
                    dueDate: now.toISOString(),
                });
            }
            else {
                const lastInsp = article.inspections[0];
                if (lastInsp.nextDueDate && lastInsp.nextDueDate <= reminderDate) {
                    upcomingInspections.push({
                        type: 'Geräteprüfung',
                        entityName: article.name,
                        dueDate: lastInsp.nextDueDate.toISOString(),
                    });
                }
            }
        }
        for (const insp of upcomingVehicleInspections) {
            if (insp.tuevDate && insp.tuevDate <= reminderDate) {
                upcomingInspections.push({ type: 'TÜV', entityName: insp.vehicle.name, dueDate: insp.tuevDate.toISOString() });
            }
            if (insp.spDate && insp.spDate <= reminderDate) {
                upcomingInspections.push({ type: 'SP-Prüfung', entityName: insp.vehicle.name, dueDate: insp.spDate.toISOString() });
            }
            if (insp.serviceDate && insp.serviceDate <= reminderDate) {
                upcomingInspections.push({ type: 'Service', entityName: insp.vehicle.name, dueDate: insp.serviceDate.toISOString() });
            }
        }
        for (const insp of upcomingEquipmentInspections) {
            if (insp.nextInspection) {
                upcomingInspections.push({
                    type: insp.type,
                    entityName: `${insp.vehicle.name} - ${insp.type}`,
                    dueDate: insp.nextInspection.toISOString(),
                });
            }
        }
        // Build upcoming medical exams list
        const upcomingMedicalExamsList = [];
        for (const exam of upcomingMedicalExams) {
            const memberName = `${exam.member.firstName} ${exam.member.lastName}`;
            if (exam.g25Date && exam.g25Date <= reminderDate) {
                upcomingMedicalExamsList.push({ memberName, examType: 'G25', dueDate: exam.g25Date.toISOString() });
            }
            if (exam.g26Date && exam.g26Date <= reminderDate) {
                upcomingMedicalExamsList.push({ memberName, examType: 'G26', dueDate: exam.g26Date.toISOString() });
            }
            if (exam.g30Date && exam.g30Date <= reminderDate) {
                upcomingMedicalExamsList.push({ memberName, examType: 'G30', dueDate: exam.g30Date.toISOString() });
            }
            if (exam.lkwLicenseExpiry && exam.lkwLicenseExpiry <= reminderDate) {
                upcomingMedicalExamsList.push({ memberName, examType: 'LKW-Führerschein', dueDate: exam.lkwLicenseExpiry.toISOString() });
            }
        }
        (0, response_1.sendSuccess)(res, {
            activeMembers,
            vehicles: vehiclesCount,
            operationsThisYear,
            recentOperations,
            upcomingInspections: upcomingInspections.sort((a, b) => a.dueDate.localeCompare(b.dueDate)).slice(0, 10),
            upcomingMedicalExams: upcomingMedicalExamsList.sort((a, b) => a.dueDate.localeCompare(b.dueDate)).slice(0, 10),
        });
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
async function getStatistics(req, res) {
    try {
        const { type } = req.params;
        const year = req.query.year ? parseInt(req.query.year) : new Date().getFullYear();
        const yearStart = new Date(year, 0, 1);
        const yearEnd = new Date(year + 1, 0, 1);
        if (type === 'members') {
            const [byGroup, byRank, totalActive, totalInactive] = await Promise.all([
                database_1.prisma.member.groupBy({ by: ['groupId'], _count: true, where: { deletedAt: null } }),
                database_1.prisma.member.groupBy({ by: ['rank'], _count: true, where: { deletedAt: null } }),
                database_1.prisma.member.count({ where: { isInactive: false, deletedAt: null } }),
                database_1.prisma.member.count({ where: { isInactive: true, deletedAt: null } }),
            ]);
            (0, response_1.sendSuccess)(res, { byGroup, byRank, totalActive, totalInactive });
        }
        else if (type === 'operations') {
            const operations = await database_1.prisma.operation.findMany({
                where: { date: { gte: yearStart, lt: yearEnd } },
                select: { id: true, date: true, keyword: true, leaderCount: true, memberCount: true },
                orderBy: { date: 'asc' },
            });
            const byMonth = Array.from({ length: 12 }, (_, i) => ({
                month: i + 1,
                count: operations.filter(op => op.date.getMonth() === i).length,
            }));
            const byKeyword = operations.reduce((acc, op) => {
                const key = op.keyword || 'Unbekannt';
                acc[key] = (acc[key] || 0) + 1;
                return acc;
            }, {});
            (0, response_1.sendSuccess)(res, {
                total: operations.length,
                byMonth,
                byKeyword: Object.entries(byKeyword)
                    .map(([keyword, count]) => ({ keyword, count }))
                    .sort((a, b) => b.count - a.count),
            });
        }
        else if (type === 'training') {
            const courses = await database_1.prisma.course.findMany({
                include: { category: true },
                orderBy: { createdAt: 'desc' },
            });
            const byCategory = courses.reduce((acc, c) => {
                const key = c.category.name;
                if (!acc[key])
                    acc[key] = { total: 0, completed: 0 };
                acc[key].total++;
                if (c.status === 'completed')
                    acc[key].completed++;
                return acc;
            }, {});
            (0, response_1.sendSuccess)(res, {
                total: courses.length,
                byCategory: Object.entries(byCategory).map(([name, stats]) => ({ name, ...stats })),
            });
        }
        else {
            (0, response_1.sendError)(res, 'Unbekannter Statistiktyp', 400);
        }
    }
    catch (err) {
        (0, response_1.sendError)(res, err.message);
    }
}
//# sourceMappingURL=dashboard.controller.js.map