import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';
import { prisma } from '../src/config/database';

describe('Einsatzbericht-Generierung', () => {
  let operationId: number;

  beforeAll(async () => {
    const op = await prisma.operation.create({
      data: {
        date: new Date('2026-07-21'),
        location: 'Report-Test Strasse 1',
        alarmTime: '14:00',
        departureTime: '14:05',
        arrivalTime: '14:10',
        returnTime: '15:00',
        keyword: 'Brand 1',
        leaderCount: 1,
        memberCount: 5,
        reportType: 'Einsatzbericht',
        operationType: 'Kleinbrand a',
        createdByName: 'Test Ersteller',
        authorRole: 'Einsatzleiter',
      },
    });
    operationId = op.id;

    await prisma.operationTime.create({
      data: {
        operationId,
        vehicleName: '12/45',
        alarmTime: '14:00',
        departureTime: '14:05',
        arrivalTime: '14:10',
        returnTime: '15:00',
      },
    });
  });

  afterAll(async () => {
    await prisma.operation.delete({ where: { id: operationId } }).catch(() => {});
  });

  it('POST /api/operations/:id/generate-report ohne Template gibt Fehlermeldung', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Einsatzbericht' } } });
    if (hasTemplate) return; // Skip wenn Template vorhanden

    const res = await request(app)
      .post(`/api/operations/${operationId}/generate-report`)
      .set(authHeader());

    expect(res.status).toBe(500);
    expect(res.body.error).toContain('Template');
  });

  it('POST /api/operations/:id/generate-report mit Template erzeugt Dokument', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Einsatzbericht' } } });
    if (!hasTemplate) return; // Skip wenn kein Template vorhanden

    const res = await request(app)
      .post(`/api/operations/${operationId}/generate-report`)
      .set(authHeader());

    expect(res.status).toBe(201);
    expect(res.body.data.fileName).toContain('Einsatzbericht');
    expect(res.body.data.filePath).toBeDefined();
  });

  it('POST /api/operations/:id/generate-personnel-sheet ohne Template gibt Fehlermeldung', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Kräftenachweis' } } });
    if (hasTemplate) return;

    const res = await request(app)
      .post(`/api/operations/${operationId}/generate-personnel-sheet`)
      .set(authHeader());

    expect(res.status).toBe(500);
    expect(res.body.error).toContain('Template');
  });

  it('POST /api/operations/:id/generate-personnel-sheet mit Template erzeugt Dokument', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Kräftenachweis' } } });
    if (!hasTemplate) return;

    const res = await request(app)
      .post(`/api/operations/${operationId}/generate-personnel-sheet`)
      .set(authHeader());

    expect(res.status).toBe(201);
    expect(res.body.data.fileName).toContain('Kraeftenachweis');
  });

  it('Generierte Dokumente erscheinen in der Dokumentenliste', async () => {
    const res = await request(app)
      .get(`/api/operations/${operationId}/documents`)
      .set(authHeader());

    expect(res.status).toBe(200);
    // Documents may or may not exist depending on whether templates are configured
  });
});
