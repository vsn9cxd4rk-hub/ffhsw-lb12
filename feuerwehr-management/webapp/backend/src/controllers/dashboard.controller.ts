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
        select: {
          id: true,
          date: true,
          keyword: true,
          alarmTime: true,
          district: true,
          operationResult: true,
          wasActivelyInvolved: true,
          leaderCount: true,
          memberCount: true,
        },
        orderBy: { date: 'asc' },
      });

      // --- byMonth (number[]) ---
      const byMonth = Array.from({ length: 12 }, (_, i) =>
        operations.filter(op => op.date.getMonth() === i).length
      );

      // --- byKeyword ---
      const byKeyword = operations.reduce<Record<string, number>>((acc, op) => {
        const key = op.keyword || 'Unbekannt';
        acc[key] = (acc[key] || 0) + 1;
        return acc;
      }, {});

      // --- byDistrict ---
      const districtCounts = operations.reduce<Record<string, number>>((acc, op) => {
        const key = op.district || 'Unbekannt';
        acc[key] = (acc[key] || 0) + 1;
        return acc;
      }, {});
      const byDistrict = Object.entries(districtCounts)
        .map(([district, count]) => ({
          district,
          count,
          percent: operations.length > 0 ? Math.round((count / operations.length) * 10000) / 100 : 0,
        }))
        .sort((a, b) => b.count - a.count);

      // --- byResult ---
      const resultCounts = operations.reduce<Record<string, number>>((acc, op) => {
        const key = op.operationResult || 'Unbekannt';
        acc[key] = (acc[key] || 0) + 1;
        return acc;
      }, {});
      const byResult = Object.entries(resultCounts)
        .map(([result, count]) => ({
          result,
          count,
          percent: operations.length > 0 ? Math.round((count / operations.length) * 10000) / 100 : 0,
        }))
        .sort((a, b) => b.count - a.count);

      // --- byTimeOfDay ---
      type TimeInterval = 'FRUEH' | 'MITTAG' | 'SPAET';
      const getInterval = (alarmTime: string | null): TimeInterval | null => {
        if (!alarmTime) return null;
        const parts = alarmTime.split(':');
        if (parts.length < 2) return null;
        const hour = parseInt(parts[0], 10);
        if (isNaN(hour)) return null;
        if (hour >= 6 && hour < 14) return 'FRUEH';
        if (hour >= 14 && hour < 22) return 'MITTAG';
        return 'SPAET';
      };

      const intervalData: Record<TimeInterval, { count: number; totalPersonnel: number }> = {
        FRUEH: { count: 0, totalPersonnel: 0 },
        MITTAG: { count: 0, totalPersonnel: 0 },
        SPAET: { count: 0, totalPersonnel: 0 },
      };

      for (const op of operations) {
        const interval = getInterval(op.alarmTime);
        if (!interval) continue;
        intervalData[interval].count++;
        intervalData[interval].totalPersonnel += (op.leaderCount + op.memberCount);
      }

      const getRisk = (avg: number): string => {
        if (avg < 6) return 'KRITISCH';
        if (avg <= 8) return 'AKZEPTABEL';
        return 'KEIN RISIKO';
      };

      const byTimeOfDay = (['FRUEH', 'MITTAG', 'SPAET'] as TimeInterval[]).map(interval => {
        const data = intervalData[interval];
        const avgPersonnel = data.count > 0 ? Math.round((data.totalPersonnel / data.count) * 100) / 100 : 0;
        return {
          interval,
          count: data.count,
          avgPersonnel,
          risk: data.count > 0 ? getRisk(avgPersonnel) : 'KEIN RISIKO',
        };
      });

      // --- byDayOfWeek ---
      const dayNames = ['Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag'];
      const dayOrder = ['Montag', 'Dienstag', 'Mittwoch', 'Donnerstag', 'Freitag', 'Samstag', 'Sonntag'];
      const dayIntervalCounts: Record<string, { frueh: number; mittag: number; spaet: number; total: number }> = {};
      for (const day of dayOrder) {
        dayIntervalCounts[day] = { frueh: 0, mittag: 0, spaet: 0, total: 0 };
      }

      for (const op of operations) {
        const dayIndex = op.date.getDay(); // 0=Sunday
        const day = dayNames[dayIndex];
        const interval = getInterval(op.alarmTime);
        dayIntervalCounts[day].total++;
        if (interval === 'FRUEH') dayIntervalCounts[day].frueh++;
        else if (interval === 'MITTAG') dayIntervalCounts[day].mittag++;
        else if (interval === 'SPAET') dayIntervalCounts[day].spaet++;
      }

      const byDayOfWeek = dayOrder.map(day => ({
        day,
        frueh: dayIntervalCounts[day].frueh,
        mittag: dayIntervalCounts[day].mittag,
        spaet: dayIntervalCounts[day].spaet,
        total: dayIntervalCounts[day].total,
      }));

      // --- byPersonnel ---
      const personnelRecords = await prisma.operationPersonnel.findMany({
        where: { operation: { date: { gte: yearStart, lt: yearEnd } } },
        select: {
          memberId: true,
          function: true,
          member: { select: { firstName: true, lastName: true } },
        },
      });

      const personnelMap: Record<number, { name: string; total: number; positions: Record<string, number> }> = {};
      for (const rec of personnelRecords) {
        if (!personnelMap[rec.memberId]) {
          personnelMap[rec.memberId] = {
            name: `${rec.member.firstName} ${rec.member.lastName}`,
            total: 0,
            positions: { GF: 0, MA: 0, ME: 0, AT: 0, WT: 0, ST: 0 },
          };
        }
        personnelMap[rec.memberId].total++;

        // Map function strings to position abbreviations
        const fnUpper = rec.function.toUpperCase();
        if (fnUpper.includes('GRUPPENFUEHRER') || fnUpper.includes('GRUPPENFÜHRER')) {
          personnelMap[rec.memberId].positions.GF++;
        } else if (fnUpper.includes('MASCHINIST')) {
          personnelMap[rec.memberId].positions.MA++;
        } else if (fnUpper.includes('MELDER')) {
          personnelMap[rec.memberId].positions.ME++;
        } else if (fnUpper.includes('ATEMSCHUTZ') || fnUpper.includes('ANGRIFFSTRUPP')) {
          personnelMap[rec.memberId].positions.AT++;
        } else if (fnUpper.includes('WASSERTRUPP')) {
          personnelMap[rec.memberId].positions.WT++;
        } else if (fnUpper.includes('SCHLAUCHTRUPP')) {
          personnelMap[rec.memberId].positions.ST++;
        }
      }

      const byPersonnel = Object.values(personnelMap)
        .sort((a, b) => b.total - a.total);

      // --- activeInvolved ---
      const activeYes = operations.filter(op => op.wasActivelyInvolved === true).length;
      const activeNo = operations.filter(op => op.wasActivelyInvolved === false).length;
      const activeInvolved = { yes: activeYes, no: activeNo };

      // --- totalReal / totalFalse ---
      const totalReal = operations.filter(op => op.operationResult && op.operationResult.includes('Real')).length;
      const totalFalse = operations.filter(op => op.operationResult && op.operationResult.includes('Fehl')).length;

      sendSuccess(res, {
        total: operations.length,
        totalReal,
        totalFalse,
        activeInvolved,
        byMonth,
        byKeyword: Object.entries(byKeyword)
          .map(([keyword, count]) => ({ keyword, count }))
          .sort((a, b) => b.count - a.count),
        byDistrict,
        byResult,
        byTimeOfDay,
        byDayOfWeek,
        byPersonnel,
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
