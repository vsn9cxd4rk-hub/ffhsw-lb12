import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';
import { prisma } from '../src/config/database';

describe('Einsatz-Kräfte (Personnel) API', () => {
  let operationId: number;
  let memberId: number;
  let personnelId: number;

  beforeAll(async () => {
    const op = await prisma.operation.create({
      data: { date: new Date('2026-07-21'), location: 'Personnel-Test', leaderCount: 1, memberCount: 5 },
    });
    operationId = op.id;

    const member = await prisma.member.findFirst({ where: { isInactive: false } });
    if (member) memberId = member.id;
  });

  afterAll(async () => {
    await prisma.operation.delete({ where: { id: operationId } }).catch(() => {});
  });

  it('GET /api/operations/:id/personnel gibt leere Liste', async () => {
    const res = await request(app)
      .get(`/api/operations/${operationId}/personnel`)
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data).toEqual([]);
  });

  it('POST /api/operations/:id/personnel fügt Einsatzkraft hinzu', async () => {
    if (!memberId) return;

    const res = await request(app)
      .post(`/api/operations/${operationId}/personnel`)
      .set(authHeader())
      .send({
        memberId,
        vehicleName: '12/45',
        function: 'Gruppenfuehrer',
        section: 'deployed',
      });

    expect(res.status).toBe(201);
    expect(res.body.data.function).toBe('Gruppenfuehrer');
    expect(res.body.data.member).toBeDefined();
    expect(res.body.data.member.firstName).toBeDefined();
    personnelId = res.body.data.id;
  });

  it('GET /api/operations/:id/personnel enthält hinzugefügte Kraft', async () => {
    if (!memberId) return;

    const res = await request(app)
      .get(`/api/operations/${operationId}/personnel`)
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data.length).toBe(1);
    expect(res.body.data[0].vehicleName).toBe('12/45');
    expect(res.body.data[0].member.qualAGT).toBeDefined();
    expect(res.body.data[0].member.qualLicenseB).toBeDefined();
  });

  it('PUT /api/operations/:id/personnel/:personnelId aktualisiert', async () => {
    if (!personnelId) return;

    const res = await request(app)
      .put(`/api/operations/${operationId}/personnel/${personnelId}`)
      .set(authHeader())
      .send({ function: 'Maschinist' });

    expect(res.status).toBe(200);
    expect(res.body.data.function).toBe('Maschinist');
  });

  it('DELETE /api/operations/:id/personnel/:personnelId entfernt Kraft', async () => {
    if (!personnelId) return;

    const res = await request(app)
      .delete(`/api/operations/${operationId}/personnel/${personnelId}`)
      .set(authHeader());

    expect(res.status).toBe(200);
  });
});
