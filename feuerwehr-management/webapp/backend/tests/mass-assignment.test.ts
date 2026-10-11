import { describe, it, expect, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// Request bodies must not reach Prisma as nested writes or set server-managed file paths
// (see utils/body.ts). Every attempt below used to succeed.

const EVIL_USER = `evil-${Date.now()}`;
const createdEvents: number[] = [];
const createdCourses: number[] = [];

describe('Mass Assignment / Datei-Pfade', () => {
  afterAll(async () => {
    await prisma.event.deleteMany({ where: { id: { in: createdEvents } } });
    await prisma.course.deleteMany({ where: { id: { in: createdCourses } } });
    await prisma.user.deleteMany({ where: { username: EVIL_USER } });
    await prisma.member.deleteMany({ where: { firstName: 'Evil', lastName: EVIL_USER } });
  });

  it('POST /api/events ignoriert verschachtelte Writes (kein Admin-Account über attendances)', async () => {
    const res = await request(app)
      .post('/api/events')
      .set(authHeader())
      .send({
        name: 'MA-Test',
        date: '2026-01-01',
        category: 1,
        attendances: {
          create: {
            year: 2026,
            status: 'x',
            member: {
              create: {
                firstName: 'Evil', lastName: EVIL_USER,
                user: { create: { username: EVIL_USER, password: 'x', isAdmin: true } },
              },
            },
          },
        },
      });

    expect(res.status).toBe(201);
    createdEvents.push(res.body.data.id);
    expect(await prisma.user.findUnique({ where: { username: EVIL_USER } })).toBeNull();
    expect(await prisma.attendance.count({ where: { eventId: res.body.data.id } })).toBe(0);
  });

  it('id/createdAt aus dem Body werden ignoriert', async () => {
    const res = await request(app)
      .post('/api/events')
      .set(authHeader())
      .send({ id: 999999, name: 'MA-Test-Id', date: '2026-01-02', category: 1, createdAt: '2000-01-01T00:00:00.000Z' });

    expect(res.status).toBe(201);
    createdEvents.push(res.body.data.id);
    expect(res.body.data.id).not.toBe(999999);
    expect(new Date(res.body.data.createdAt).getFullYear()).not.toBe(2000);
  });

  it('certificatePath ist nicht per PUT setzbar (kein Lesen beliebiger Dateien)', async () => {
    const member = await prisma.member.findFirst({ where: { deletedAt: null } });
    const category = await prisma.courseCategory.findFirst();
    if (!member || !category) return;

    const created = await request(app)
      .post('/api/training/courses')
      .set(authHeader())
      .send({ memberId: member.id, categoryId: category.id, status: 'planned' });
    expect(created.status).toBe(201);
    createdCourses.push(created.body.data.id);

    await request(app)
      .put(`/api/training/courses/${created.body.data.id}`)
      .set(authHeader())
      .send({ certificatePath: '/etc/hosts', member: { update: { firstName: 'Hacked' } } });

    const course = await prisma.course.findUnique({ where: { id: created.body.data.id }, include: { member: true } });
    expect(course?.certificatePath).toBeNull();
    expect(course?.member.firstName).toBe(member.firstName);

    const dl = await request(app).get(`/api/training/courses/${created.body.data.id}/certificate`).set(authHeader());
    expect(dl.status).toBe(404);
  });

  it('Dateien außerhalb von UPLOAD_PATH werden weder ausgeliefert noch gelöscht', async () => {
    const event = await prisma.event.create({ data: { name: 'MA-Test-Doc', date: new Date('2026-01-03'), category: 1 } });
    createdEvents.push(event.id);
    const doc = await prisma.eventDocument.create({
      data: {
        eventId: event.id, fileName: 'package.json', filePath: `${process.cwd()}/package.json`,
        fileSize: 1, mimeType: 'application/json', uploadedBy: 'test',
      },
    });

    const dl = await request(app).get(`/api/events/${event.id}/documents/${doc.id}/download`).set(authHeader());
    expect(dl.status).toBe(404);

    const del = await request(app).delete(`/api/events/${event.id}/documents/${doc.id}`).set(authHeader());
    expect(del.status).toBe(200);
    const fs = await import('fs');
    expect(fs.existsSync(`${process.cwd()}/package.json`)).toBe(true);
  });

  it('Upload mit manipulierter ID (Path Traversal) wird abgelehnt', async () => {
    const res = await request(app)
      .post('/api/events/..%2F..%2Fevil/documents')
      .set(authHeader())
      .attach('file', Buffer.from('%PDF-1.4'), { filename: 'x.pdf', contentType: 'application/pdf' });

    expect(res.status).toBe(400);
  });

  it('Abgeleitete Einsatzfelder (vehicles/leaderCount) sind nicht direkt setzbar', async () => {
    const res = await request(app)
      .post('/api/operations')
      .set(authHeader())
      .send({ date: '2026-01-04', location: 'MA-Test-Ort', vehicles: 'Fake', leaderCount: 99 });

    expect(res.status).toBe(201);
    expect(res.body.data.vehicles).not.toBe('Fake');
    expect(res.body.data.leaderCount).toBe(0);
    await prisma.operation.delete({ where: { id: res.body.data.id } });
  });
});
