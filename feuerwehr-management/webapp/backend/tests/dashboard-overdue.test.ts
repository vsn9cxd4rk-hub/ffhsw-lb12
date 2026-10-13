import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// The dashboard queries had a lower bound "gte: now", so a date dropped off the dashboard
// the moment it became overdue: an expired TÜV or G26 was simply no longer shown.

const STAMP = Date.now();
const DAY = 24 * 3600 * 1000;
const overdue = new Date(Date.now() - 40 * DAY);
const soon = new Date(Date.now() + 10 * DAY);

const vehicleIds: number[] = [];
const memberIds: number[] = [];

interface Inspection { type: string; entityName: string; status: string }
interface Exam { memberName: string; examType: string; status: string }

describe('Dashboard zeigt überfällige Termine', () => {
  let inspections: Inspection[];
  let exams: Exam[];

  beforeAll(async () => {
    const vehicle = (name: string, isRetired: boolean) => prisma.vehicle.create({
      data: {
        name: `${name}-${STAMP}`,
        isRetired,
        inspection: { create: { tuevDate: overdue, spDate: soon } },
        equipmentInspections: { create: { type: `Winde-${STAMP}`, nextInspection: overdue } },
      },
    });
    vehicleIds.push((await vehicle('Aktiv', false)).id, (await vehicle('Ausgemustert', true)).id);

    const member = (lastName: string, extra: object) => prisma.member.create({
      data: {
        firstName: 'Dash', lastName: `${lastName}-${STAMP}`, ...extra,
        examination: { create: { g26Date: overdue, g25Date: soon } },
      },
    });
    memberIds.push(
      (await member('Aktiv', {})).id,
      (await member('Inaktiv', { isInactive: true })).id,
      (await member('Geloescht', { deletedAt: new Date() })).id,
    );

    const res = await request(app).get('/api/dashboard/stats').set(authHeader());
    expect(res.status).toBe(200);
    inspections = res.body.data.upcomingInspections;
    exams = res.body.data.upcomingMedicalExams;
  });

  afterAll(async () => {
    await prisma.memberExamination.deleteMany({ where: { memberId: { in: memberIds } } });
    await prisma.member.deleteMany({ where: { id: { in: memberIds } } });
    await prisma.vehicle.deleteMany({ where: { id: { in: vehicleIds } } });
  });

  it('überfälliger TÜV erscheint rot, anstehende SP gelb', () => {
    const own = inspections.filter((i) => i.entityName === `Aktiv-${STAMP}`);
    expect(own.find((i) => i.type === 'TÜV')?.status).toBe('red');
    expect(own.find((i) => i.type === 'SP-Prüfung')?.status).toBe('yellow');
  });

  it('überfällige Geräteprüfung am Fahrzeug erscheint rot', () => {
    const own = inspections.find((i) => i.entityName === `Aktiv-${STAMP} - Winde-${STAMP}`);
    expect(own?.status).toBe('red');
  });

  it('ausgemusterte Fahrzeuge erscheinen nicht', () => {
    expect(inspections.some((i) => i.entityName.startsWith(`Ausgemustert-${STAMP}`))).toBe(false);
  });

  it('überfällige Untersuchung erscheint mit Status rot, anstehende gelb', () => {
    const own = exams.filter((e) => e.memberName === `Dash Aktiv-${STAMP}`);
    expect(own.find((e) => e.examType === 'G26')?.status).toBe('red');
    expect(own.find((e) => e.examType === 'G25')?.status).toBe('yellow');
  });

  it('inaktive und gelöschte Mitglieder erscheinen nicht', () => {
    expect(exams.some((e) => e.memberName.includes(`Inaktiv-${STAMP}`))).toBe(false);
    expect(exams.some((e) => e.memberName.includes(`Geloescht-${STAMP}`))).toBe(false);
  });
});
