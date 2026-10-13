import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';
import { BIT_EVENTS, BIT_TRAINING } from '../src/config/permissionBits';

// Events and training had only `authenticate`: every user could change/delete events, fake
// attendance, edit courses and trigger report generation. Reading stays open to everyone,
// writing needs BIT_EVENTS / BIT_TRAINING (or admin).

const STAMP = Date.now();
let readerGroupId: number;
let writerGroupId: number;
const userIds: number[] = [];
let readerToken: string;
let writerToken: string;
let eventId: number;
let courseId: number;
let ownMemberId: number | null = null;

async function makeUser(name: string, groupId: number): Promise<string> {
  const user = await prisma.user.create({ data: { username: `${name}-${STAMP}`, password: 'x', groupId } });
  userIds.push(user.id);
  return signAccessToken({ userId: user.id, username: user.username, isAdmin: false, groupId });
}

const bearer = (t: string) => ({ Authorization: `Bearer ${t}` });

describe('Berechtigungen Veranstaltungen/Ausbildung', () => {
  beforeAll(async () => {
    readerGroupId = (await prisma.permissionGroup.create({ data: { name: `perm-reader-${STAMP}` } })).id;
    writerGroupId = (await prisma.permissionGroup.create({
      data: { name: `perm-writer-${STAMP}`, [BIT_EVENTS]: true, [BIT_TRAINING]: true },
    })).id;
    readerToken = await makeUser('perm-reader', readerGroupId);
    writerToken = await makeUser('perm-writer', writerGroupId);

    eventId = (await prisma.event.create({ data: { name: `Perm-Test-${STAMP}`, date: new Date('2026-02-01'), category: 2 } })).id;
    // Own fixtures: on a fresh installation there are no members (and maybe no categories)
    const member = await prisma.member.create({ data: { firstName: 'Perm', lastName: `Test-${STAMP}` } });
    ownMemberId = member.id;
    const category = (await prisma.courseCategory.findFirst())
      ?? await prisma.courseCategory.create({ data: { name: `perm-cat-${STAMP}` } });
    courseId = (await prisma.course.create({ data: { memberId: member.id, categoryId: category.id } })).id;
  });

  afterAll(async () => {
    await prisma.event.deleteMany({ where: { name: { startsWith: `Perm-Test-${STAMP}` } } });
    if (courseId) await prisma.course.deleteMany({ where: { id: courseId } });
    if (ownMemberId) await prisma.member.deleteMany({ where: { id: ownMemberId } });
    await prisma.courseCategory.deleteMany({ where: { name: `perm-cat-${STAMP}` } });
    await prisma.user.deleteMany({ where: { id: { in: userIds } } });
    await prisma.permissionGroup.deleteMany({ where: { id: { in: [readerGroupId, writerGroupId] } } });
  });

  it('Ohne Bit: Veranstaltungen lesen erlaubt', async () => {
    expect((await request(app).get('/api/events').set(bearer(readerToken))).status).toBe(200);
    expect((await request(app).get(`/api/events/${eventId}`).set(bearer(readerToken))).status).toBe(200);
    expect((await request(app).get(`/api/events/${eventId}/attendance`).set(bearer(readerToken))).status).toBe(200);
  });

  it('Ohne Bit: Veranstaltungen anlegen/ändern/löschen, Anwesenheit und Berichte verboten', async () => {
    const r = bearer(readerToken);
    expect((await request(app).post('/api/events').set(r).send({ name: `Perm-Test-${STAMP}-x`, date: '2026-02-02' })).status).toBe(403);
    expect((await request(app).put(`/api/events/${eventId}`).set(r).send({ name: 'hacked' })).status).toBe(403);
    expect((await request(app).post(`/api/events/${eventId}/attendance`).set(r).send({ records: [] })).status).toBe(403);
    expect((await request(app).post(`/api/events/${eventId}/bsw/checkliste`).set(r)).status).toBe(403);
    expect((await request(app).delete(`/api/events/${eventId}`).set(r)).status).toBe(403);

    const event = await prisma.event.findUnique({ where: { id: eventId } });
    expect(event?.name).toBe(`Perm-Test-${STAMP}`);
  });

  it('Mit BIT_EVENTS: Veranstaltung ändern erlaubt', async () => {
    const res = await request(app).put(`/api/events/${eventId}`).set(bearer(writerToken)).send({ notes: 'ok' });
    expect(res.status).toBe(200);
  });

  it('Ohne Bit: Lehrgänge lesen erlaubt, ändern verboten', async () => {
    const r = bearer(readerToken);
    expect((await request(app).get('/api/training/courses').set(r)).status).toBe(200);
    expect((await request(app).get('/api/training/categories').set(r)).status).toBe(200);
    expect((await request(app).put(`/api/training/courses/${courseId}`).set(r).send({ notes: 'x' })).status).toBe(403);
    expect((await request(app).delete(`/api/training/courses/${courseId}`).set(r)).status).toBe(403);
  });

  it('Mit BIT_TRAINING: Lehrgang ändern erlaubt, Kategorien bleiben Admin-Sache', async () => {
    const w = bearer(writerToken);
    expect((await request(app).put(`/api/training/courses/${courseId}`).set(w).send({ notes: 'ok' })).status).toBe(200);
    expect((await request(app).post('/api/training/categories').set(w).send({ name: 'x' })).status).toBe(403);
  });
});
