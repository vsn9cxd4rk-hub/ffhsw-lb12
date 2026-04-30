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
    const twoMonthsFromNow = new Date();
    twoMonthsFromNow.setMonth(twoMonthsFromNow.getMonth() + 2);

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
      prisma.member.count({ where: { isInactive: false, deletedAt: null, group: { name: 'Einsatzabteilung' } } }),
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
      // Articles with inspection interval (excluding decommissioned) and their latest inspection
      prisma.article.findMany({
        where: {
          inspectionInterval: { not: null },
          isDecommissioned: false,
          decommissionedDate: null,
        },
        include: {
          inspections: { orderBy: { inspectedAt: 'desc' }, take: 1 },
        },
      }),
    ]);

    // Build upcoming inspections list with traffic light status
    const upcomingInspections: Array<{
      type: string;
      entityName: string;
      dueDate: string;
      status: 'red' | 'yellow' | 'green';
      articleId?: number;
    }> = [];

    // Article inspections: compute traffic light status for each article
    const now = new Date();
    for (const article of articlesWithInterval) {
      let dueDate: Date;
      let status: 'red' | 'yellow' | 'green';

      if (article.inspections.length === 0) {
        // Never inspected — red (overdue)
        dueDate = now;
        status = 'red';
      } else {
        const lastInsp = article.inspections[0];
        if (!lastInsp.nextDueDate) {
          continue; // No due date computed, skip
        }
        dueDate = lastInsp.nextDueDate;
        if (dueDate <= now) {
          status = 'red';
        } else if (dueDate <= twoMonthsFromNow) {
          status = 'yellow';
        } else {
          status = 'green';
        }
      }

      // Only include red and yellow items (green = OK, no action needed)
      if (status === 'red' || status === 'yellow') {
        upcomingInspections.push({
          type: 'Geräteprüfung',
          entityName: article.name,
          dueDate: dueDate.toISOString(),
          status,
          articleId: article.id,
        });
      }
    }

    for (const insp of upcomingVehicleInspections) {
      if (insp.tuevDate && insp.tuevDate <= reminderDate) {
        upcomingInspections.push({ type: 'TÜV', entityName: insp.vehicle.name, dueDate: insp.tuevDate.toISOString(), status: insp.tuevDate <= now ? 'red' : 'yellow' });
      }
      if (insp.spDate && insp.spDate <= reminderDate) {
        upcomingInspections.push({ type: 'SP-Prüfung', entityName: insp.vehicle.name, dueDate: insp.spDate.toISOString(), status: insp.spDate <= now ? 'red' : 'yellow' });
      }
      if (insp.serviceDate && insp.serviceDate <= reminderDate) {
        upcomingInspections.push({ type: 'Service', entityName: insp.vehicle.name, dueDate: insp.serviceDate.toISOString(), status: insp.serviceDate <= now ? 'red' : 'yellow' });
      }
    }

    for (const insp of upcomingEquipmentInspections) {
      if (insp.nextInspection) {
        upcomingInspections.push({
          type: insp.type,
          entityName: `${insp.vehicle.name} - ${insp.type}`,
          dueDate: insp.nextInspection.toISOString(),
          status: insp.nextInspection <= now ? 'red' : 'yellow',
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
      upcomingInspections: upcomingInspections.sort((a, b) => {
        const statusOrder: Record<string, number> = { red: 0, yellow: 1, green: 2 };
        const statusDiff = statusOrder[a.status] - statusOrder[b.status];
        if (statusDiff !== 0) return statusDiff;
        return a.dueDate.localeCompare(b.dueDate);
      }),
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

export async function getNotifications(_req: Request, res: Response): Promise<void> {
  try {
    const now = new Date();
    const twoMonths = new Date();
    twoMonths.setMonth(twoMonths.getMonth() + 2);

    const notifications: Array<{ id: string; type: string; severity: 'red' | 'yellow' | 'info'; title: string; message: string; link?: string }> = [];

    // 1. Überfällige Prüfungen (rot)
    const articlesWithInterval = await prisma.article.findMany({
      where: { inspectionInterval: { not: null }, isDecommissioned: false, decommissionedDate: null },
      include: { inspections: { orderBy: { inspectedAt: 'desc' }, take: 1 } },
    });

    let overdueCount = 0;
    let dueCount = 0;
    for (const a of articlesWithInterval) {
      if (a.inspections.length === 0) {
        overdueCount++;
      } else {
        const next = a.inspections[0].nextDueDate;
        if (next && next <= now) overdueCount++;
        else if (next && next <= twoMonths) dueCount++;
      }
    }

    if (overdueCount > 0) {
      notifications.push({
        id: 'insp-overdue', type: 'inspection', severity: 'red',
        title: `${overdueCount} Prüfung${overdueCount > 1 ? 'en' : ''} überfällig`,
        message: 'Geräteprüfungen sind überfällig und müssen dringend durchgeführt werden.',
        link: '/inspections',
      });
    }
    if (dueCount > 0) {
      notifications.push({
        id: 'insp-due', type: 'inspection', severity: 'yellow',
        title: `${dueCount} Prüfung${dueCount > 1 ? 'en' : ''} demnächst fällig`,
        message: 'Geräteprüfungen werden in den nächsten 2 Monaten fällig.',
        link: '/inspections',
      });
    }

    // 2. Offene Mängel (rot für critical/high, gelb für medium)
    const openDefects = await prisma.articleDefect.groupBy({
      by: ['severity'],
      where: { status: { in: ['open', 'in_progress'] } },
      _count: true,
    });

    const criticalDefects = openDefects.filter(d => d.severity === 'critical' || d.severity === 'high').reduce((sum, d) => sum + d._count, 0);
    const mediumDefects = openDefects.filter(d => d.severity === 'medium').reduce((sum, d) => sum + d._count, 0);

    if (criticalDefects > 0) {
      notifications.push({
        id: 'defect-critical', type: 'defect', severity: 'red',
        title: `${criticalDefects} kritische${criticalDefects > 1 ? '' : 'r'} Mangel/Mängel offen`,
        message: 'Es gibt offene Mängel mit hohem oder kritischem Schweregrad.',
        link: '/defects',
      });
    }
    if (mediumDefects > 0) {
      notifications.push({
        id: 'defect-medium', type: 'defect', severity: 'yellow',
        title: `${mediumDefects} Mangel/Mängel offen`,
        message: 'Es gibt offene Mängel mit mittlerem Schweregrad.',
        link: '/defects',
      });
    }

    // 3. Fahrzeug-Prüftermine (TÜV, SP)
    const vehicles = await prisma.vehicle.findMany({
      where: { isRetired: false },
      include: { inspection: true },
    });

    for (const v of vehicles) {
      if (!v.inspection) continue;
      const checks = [
        { name: 'TÜV', date: v.inspection.tuevDate },
        { name: 'SP', date: v.inspection.spDate },
        { name: 'Service', date: v.inspection.serviceDate },
      ];
      for (const check of checks) {
        if (!check.date) continue;
        if (check.date <= now) {
          notifications.push({
            id: `vehicle-${v.id}-${check.name}`, type: 'vehicle', severity: 'red',
            title: `${v.name}: ${check.name} überfällig`,
            message: `Der ${check.name}-Termin für ${v.name} ist abgelaufen.`,
            link: `/vehicles/${v.id}`,
          });
        } else if (check.date <= twoMonths) {
          notifications.push({
            id: `vehicle-${v.id}-${check.name}`, type: 'vehicle', severity: 'yellow',
            title: `${v.name}: ${check.name} demnächst fällig`,
            message: `Der ${check.name}-Termin für ${v.name} steht bevor.`,
            link: `/vehicles/${v.id}`,
          });
        }
      }
    }

    // 4. Ablaufende med. Untersuchungen
    const members = await prisma.member.findMany({
      where: { isInactive: false, deletedAt: null },
      include: { examination: true },
    });

    let examsDue = 0;
    for (const m of members) {
      if (!m.examination) continue;
      const exams = [m.examination.g25Date, m.examination.g26Date, m.examination.g30Date];
      for (const d of exams) {
        if (d && d <= twoMonths) examsDue++;
      }
    }
    if (examsDue > 0) {
      notifications.push({
        id: 'exams-due', type: 'medical', severity: 'info',
        title: `${examsDue} Untersuchung${examsDue > 1 ? 'en' : ''} fällig`,
        message: 'Arbeitsmedizinische Untersuchungen (G25/G26/G30) stehen an.',
        link: '/members',
      });
    }

    // Sort: red first, then yellow, then info
    const order = { red: 0, yellow: 1, info: 2 };
    notifications.sort((a, b) => order[a.severity] - order[b.severity]);

    sendSuccess(res, notifications);
  } catch (err) {
    sendError(res, (err as Error).message);
  }
}
