import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// Bestandsliste: Die Artikelliste liefert je Gerät die letzte Prüfung (Spalte „Letzte Prüfung“).

const SUFFIX = `${process.pid}-${Date.now()}`;
const NAME = `Vitest-Bestand ${SUFFIX}`;
let withInspections: number;
let withoutInspection: number;

async function listed(): Promise<Record<string, unknown>[]> {
  const res = await request(app).get('/api/inventory/articles').set(authHeader()).query({ search: NAME });
  expect(res.status).toBe(200);
  return res.body.data;
}

describe('Bestandsliste – letzte Prüfung', () => {
  beforeAll(async () => {
    withInspections = (await prisma.article.create({ data: { name: `${NAME} A`, inspectionInterval: 12 } })).id;
    withoutInspection = (await prisma.article.create({ data: { name: `${NAME} B` } })).id;
    // Entered out of order: the latest by date must win, not the last created
    await prisma.articleInspection.createMany({
      data: [
        { articleId: withInspections, inspectedAt: new Date('2026-03-10'), inspectedBy: 'Vitest', result: 'passed' },
        { articleId: withInspections, inspectedAt: new Date('2026-09-01'), inspectedBy: 'Vitest', result: 'failed',
          nextDueDate: new Date('2027-09-01') },
        { articleId: withInspections, inspectedAt: new Date('2025-11-20'), inspectedBy: 'Vitest', result: 'passed' },
      ],
    });
  });

  afterAll(async () => {
    await prisma.article.deleteMany({ where: { id: { in: [withInspections, withoutInspection] } } });
  });

  it('liefert die jüngste Prüfung nach Prüfdatum', async () => {
    const a = (await listed()).find(x => x.id === withInspections)!;
    expect(a.lastInspection).toMatchObject({ result: 'failed' });
    const li = a.lastInspection as { inspectedAt: string; nextDueDate: string };
    expect(li.inspectedAt.slice(0, 10)).toBe('2026-09-01');
    expect(li.nextDueDate.slice(0, 10)).toBe('2027-09-01');
  });

  it('liefert null für Geräte ohne Prüfung', async () => {
    const b = (await listed()).find(x => x.id === withoutInspection)!;
    expect(b.lastInspection).toBeNull();
  });

  it('liefert keine vollständige Prüfhistorie in der Liste', async () => {
    const a = (await listed()).find(x => x.id === withInspections)!;
    expect(a).not.toHaveProperty('inspections');
  });
});
