import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';
import { BIT_OPERATIONS } from '../src/config/permissionBits';
import { authHeader } from './setup';

// GET /api/members (personnel picker, BIT_OPERATIONS) used to return the complete member
// record incl. medications/conditions/health insurance, and the dashboard listed G25/G26/G30
// dates with names to every user. Both are health data (Art. 9 DSGVO).

const STAMP = Date.now();
const SENSITIVE = ['medications', 'conditions', 'healthInsurance', 'birthDate', 'driverLicenseNo',
  'email', 'phoneMobile', 'phonePrivate', 'street', 'city', 'comment'];

let groupId: number;
let userId: number;
let token: string;
let memberId: number;

describe('Gesundheitsdaten nur für Admins', () => {
  beforeAll(async () => {
    groupId = (await prisma.permissionGroup.create({ data: { name: `hd-${STAMP}`, [BIT_OPERATIONS]: true } })).id;
    const user = await prisma.user.create({ data: { username: `hd-${STAMP}`, password: 'x', groupId } });
    userId = user.id;
    token = signAccessToken({ userId, username: user.username, isAdmin: false, groupId });

    const in10Days = new Date(Date.now() + 10 * 24 * 3600 * 1000);
    const member = await prisma.member.create({
      data: {
        firstName: 'Hd', lastName: `Test-${STAMP}`, medications: 'Geheim', conditions: 'Geheim',
        healthInsurance: 'AOK', birthDate: new Date('1980-01-01'), email: 'hd@example.org',
        examination: { create: { g26Date: in10Days } },
      },
    });
    memberId = member.id;
  });

  afterAll(async () => {
    await prisma.memberExamination.deleteMany({ where: { memberId } });
    await prisma.member.deleteMany({ where: { id: memberId } });
    await prisma.user.deleteMany({ where: { id: userId } });
    await prisma.permissionGroup.deleteMany({ where: { id: groupId } });
  });

  it('Mitgliederliste enthält für Nicht-Admins keine sensiblen Felder', async () => {
    const res = await request(app).get('/api/members').query({ search: `Test-${STAMP}` }).set({ Authorization: `Bearer ${token}` });
    expect(res.status).toBe(200);
    const row = res.body.data.find((m: { id: number }) => m.id === memberId);
    expect(row).toBeDefined();
    expect(row.lastName).toBe(`Test-${STAMP}`);
    for (const field of SENSITIVE) expect(row).not.toHaveProperty(field);
  });

  it('Nicht-Admins können nicht über Kontaktfelder suchen', async () => {
    const res = await request(app).get('/api/members').query({ search: 'hd@example.org' }).set({ Authorization: `Bearer ${token}` });
    expect(res.status).toBe(200);
    expect(res.body.data.some((m: { id: number }) => m.id === memberId)).toBe(false);
  });

  it('Admins erhalten weiterhin alle Felder', async () => {
    const res = await request(app).get('/api/members').query({ search: `Test-${STAMP}` }).set(authHeader());
    const row = res.body.data.find((m: { id: number }) => m.id === memberId);
    expect(row.medications).toBe('Geheim');
  });

  it('Dashboard zeigt Untersuchungstermine nur Admins', async () => {
    const user = await request(app).get('/api/dashboard/stats').set({ Authorization: `Bearer ${token}` });
    expect(user.status).toBe(200);
    expect(user.body.data.upcomingMedicalExams).toEqual([]);

    const admin = await request(app).get('/api/dashboard/stats').set(authHeader());
    expect(admin.body.data.upcomingMedicalExams.some((e: { memberName: string }) => e.memberName.includes(`Test-${STAMP}`))).toBe(true);
  });

  it('Benachrichtigungen enthalten für Nicht-Admins keine Untersuchungen', async () => {
    const res = await request(app).get('/api/dashboard/notifications').set({ Authorization: `Bearer ${token}` });
    expect(res.status).toBe(200);
    expect(res.body.data.some((n: { type: string }) => n.type === 'medical')).toBe(false);
  });
});
