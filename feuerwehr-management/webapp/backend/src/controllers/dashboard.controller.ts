import { Request, Response } from 'express';
import { prisma } from '../config/database';
import { sendSuccess, sendError } from '../utils/response';

export async function getDashboardStats(_req: Request, res: Response): Promise<void> {
  try {
    const currentYear = new Date().getFullYear();
    const yearStart = new Date(currentYear, 0, 1);
    const yearEnd = new Date(currentYear + 1, 0, 1);
    const reminderDays = 30;
    const reminderDate = new Date();
    reminderDate.setDate(reminderDate.getDate() + reminderDays);

    const [
      activeMembers,
      vehiclesCount,
      operationsThisYear,
      recentOperations,
      upcomingVehicleInspections,
      upcomingEquipmentInspections,
      upcomingMedicalExams,
      articlesWithInterval,
    ] = await Promise.all([
      prisma.member.count({ where: { isInactive: false, deletedAt: null } }),
      prisma.vehicle.count({ where: { isRetired: false } }),
      prisma.operation.count({ where: { date: { gte: yearStart, lt: yearEnd } } }),
      prisma.operation.findMany({
        orderBy: [{ date: 'desc' }, { alarmTime: 'desc' }],
        take: 5,
        select: { id: true, date: true, location: true, keyword: true, leaderCount: true, memberCount: true },
      }),
      prisma.vehicleInspection.findMany({
        where: {
          OR: [
            { tuevDate: { lte: reminderDate, gte: new Date() } },
            { spDate: { lte: reminderDate, gte: new Date() } },
            { serviceDate: { lte: reminderDate, gte: new Date() } },
          ],
        },
        include: { vehicle: { select: { name: true } } },
      }),
      prisma.equipmentInspection.findMany({
        where: { nextInspection: { lte: reminderDate, gte: new Date() } },
        include: { vehicle: { select: { name: true } } },
      }),
      prisma.memberExamination.findMany({
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
      prisma.article.findMany({
        where: { inspectionInterval: { not: null } },
        include: {
          inspections: { orderBy: { inspectedAt: 'desc' }, take: 1 },
        },
      }),
    ]);

    // Build upcoming inspections list
    const upcomingInspections: Array<{ type: string; entityName: string; dueDate: string }> = [];

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
      } else {
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
    const upcomingMedicalExamsList: Array<{ memberName: string; examType: string; dueDate: string }> = [];
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

    sendSuccess(res, {
      activeMembers,
      vehicles: vehiclesCount,
      operationsThisYear,
      recentOperations,
      upcomingInspections: upcomingInspections.sort((a, b) => a.dueDate.localeCompare(b.dueDate)).slice(0, 10),
      upcomingMedicalExams: upcomingMedicalExamsList.sort((a, b) => a.dueDate.localeCompare(b.dueDate)).slice(0, 10),
    });
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}

export async function getStatistics(req: Request, res: Response): Promise<void> {
  try {
    const { type } = req.params;
    const year = req.query.year ? parseInt(req.query.year as string) : new Date().getFullYear();
    const yearStart = new Date(year, 0, 1);
    const yearEnd = new Date(year + 1, 0, 1);

    if (type === 'members') {
      const [byGroup, byRank, totalActive, totalInactive] = await Promise.all([
        prisma.member.groupBy({ by: ['groupId'], _count: true, where: { deletedAt: null } }),
        prisma.member.groupBy({ by: ['rank'], _count: true, where: { deletedAt: null } }),
        prisma.member.count({ where: { isInactive: false, deletedAt: null } }),
        prisma.member.count({ where: { isInactive: true, deletedAt: null } }),
      ]);
      sendSuccess(res, { byGroup, byRank, totalActive, totalInactive });

    } else if (type === 'operations') {
      const operations = await prisma.operation.findMany({
        where: { date: { gte: yearStart, lt: yearEnd } },
        select: { id: true, date: true, keyword: true, leaderCount: true, memberCount: true },
        orderBy: { date: 'asc' },
      });

      const byMonth = Array.from({ length: 12 }, (_, i) => ({
        month: i + 1,
        count: operations.filter(op => op.date.getMonth() === i).length,
      }));

      const byKeyword = operations.reduce<Record<string, number>>((acc, op) => {
        const key = op.keyword || 'Unbekannt';
        acc[key] = (acc[key] || 0) + 1;
        return acc;
      }, {});

      sendSuccess(res, {
        total: operations.length,
        byMonth,
        byKeyword: Object.entries(byKeyword)
          .map(([keyword, count]) => ({ keyword, count }))
          .sort((a, b) => b.count - a.count),
      });

    } else if (type === 'training') {
      const courses = await prisma.course.findMany({
        include: { category: true },
        orderBy: { createdAt: 'desc' },
      });

      const byCategory = courses.reduce<Record<string, { total: number; completed: number }>>((acc, c) => {
        const key = c.category.name;
        if (!acc[key]) acc[key] = { total: 0, completed: 0 };
        acc[key].total++;
        if (c.status === 'completed') acc[key].completed++;
        return acc;
      }, {});

      sendSuccess(res, {
        total: courses.length,
        byCategory: Object.entries(byCategory).map(([name, stats]) => ({ name, ...stats })),
      });

    } else {
      sendError(res, 'Unbekannter Statistiktyp', 400);
    }
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
