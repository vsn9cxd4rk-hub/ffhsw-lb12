import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// Prüfbuch: Ergebnis aus den Prüfkriterien, Fälligkeit aus Intervall bzw. Prüfplan
// und die Liste der fälligen Prüfungen.

const SUFFIX = `${process.pid}-${Date.now()}`;
let deviceClassId: number;
let criterionIds: number[];
let inspectionTypeId: number;
let articleId: number;

// Kalenderdatum in lokaler Zeit, so wie die Oberfläche es anzeigt (formatDate)
const isoDate = (d: Date) =>
  `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`;

async function inspect(body: Record<string, unknown>) {
  return request(app).post('/api/inspections').set(authHeader())
    .send({ articleId, inspectedBy: 'Vitest', ...body });
}

async function dueIds(): Promise<number[]> {
  const res = await request(app).get('/api/inspections/due').set(authHeader())
    .query({ search: `Vitest-Prüfartikel ${SUFFIX}` });
  expect(res.status).toBe(200);
  return res.body.data.map((a: { id: number }) => a.id);
}

describe('Prüfbuch', () => {
  beforeAll(async () => {
    const deviceClass = await prisma.deviceClass.create({
      data: {
        name: `Vitest-Klasse ${SUFFIX}`,
        subclasses: {
          create: {
            name: 'Vitest-Unterklasse',
            criteria: { create: [{ name: 'Sichtprüfung' }, { name: 'Funktionsprüfung', sortOrder: 1 }] },
          },
        },
      },
      include: { subclasses: { include: { criteria: true } } },
    });
    deviceClassId = deviceClass.id;
    const subclass = deviceClass.subclasses[0];
    criterionIds = subclass.criteria.map(c => c.id);

    inspectionTypeId = (await prisma.inspectionType.create({ data: { name: `Vitest-Prüfart ${SUFFIX}` } })).id;

    articleId = (await prisma.article.create({
      data: { name: `Vitest-Prüfartikel ${SUFFIX}`, inspectionInterval: 12, deviceSubclassId: subclass.id },
    })).id;
  });

  afterAll(async () => {
    await prisma.article.deleteMany({ where: { id: articleId } });
    await prisma.inspectionType.deleteMany({ where: { id: inspectionTypeId } });
    await prisma.deviceClass.deleteMany({ where: { id: deviceClassId } });
  });

  it('Artikel ohne Prüfung ist fällig', async () => {
    expect(await dueIds()).toContain(articleId);
  });

  it('lehnt die Prüfung ab, wenn nicht alle Kriterien bewertet sind', async () => {
    const res = await inspect({
      inspectedAt: '2026-01-15',
      criterionResults: [{ criterionId: criterionIds[0], result: 'io' }],
    });
    expect(res.status).toBe(400);
    expect(await prisma.articleInspection.count({ where: { articleId } })).toBe(0);
  });

  it('ein "nio"-Kriterium ergibt "failed", alle "io" ergeben "passed"', async () => {
    const failed = await inspect({
      inspectedAt: '2026-01-15',
      criterionResults: [
        { criterionId: criterionIds[0], result: 'io' },
        { criterionId: criterionIds[1], result: 'nio' },
      ],
    });
    expect(failed.status).toBe(201);
    expect(failed.body.data.result).toBe('failed');
    expect(failed.body.data.criterionResults).toHaveLength(2);

    const passed = await inspect({
      inspectedAt: '2026-01-16',
      criterionResults: criterionIds.map(criterionId => ({ criterionId, result: 'io' })),
    });
    expect(passed.status).toBe(201);
    expect(passed.body.data.result).toBe('passed');
  });

  it('nächste Fälligkeit = Prüfdatum + Prüfintervall des Artikels', async () => {
    const res = await inspect({ inspectedAt: '2026-01-31' });
    expect(res.status).toBe(201);
    expect(isoDate(new Date(res.body.data.nextDueDate))).toBe('2027-01-31');
  });

  it('Prüfplan der Prüfart überschreibt das Intervall des Artikels', async () => {
    await prisma.articleInspectionSchedule.create({ data: { articleId, inspectionTypeId, intervalMonths: 6 } });
    const res = await inspect({ inspectedAt: '2026-03-01', inspectionTypeId });
    expect(res.status).toBe(201);
    expect(isoDate(new Date(res.body.data.nextDueDate))).toBe('2026-09-01');
  });

  it('fällig ist nur, wessen letzte Prüfung abgelaufen ist', async () => {
    const today = new Date();

    const recent = await inspect({ inspectedAt: isoDate(today) });
    expect(recent.status).toBe(201);
    expect(await dueIds()).not.toContain(articleId);

    // Nachträglich erfasste Prüfung von vor über einem Jahr (12 Monate Intervall, also abgelaufen)
    const overdue = await inspect({ inspectedAt: isoDate(new Date(today.getTime() - 400 * 86400000)) });
    expect(overdue.status).toBe(201);
    // Die zuletzt (nach Prüfdatum) durchgeführte Prüfung zählt, nicht die zuletzt erfasste
    expect(await dueIds()).not.toContain(articleId);

    await prisma.articleInspection.deleteMany({ where: { id: recent.body.data.id } });
    expect(await dueIds()).toContain(articleId);
  });

  it('ausgemusterte Artikel sind nie fällig', async () => {
    await prisma.article.update({ where: { id: articleId }, data: { isDecommissioned: true } });
    expect(await dueIds()).not.toContain(articleId);
    await prisma.article.update({ where: { id: articleId }, data: { isDecommissioned: false } });
  });

  it('unbekannter Artikel ergibt 404', async () => {
    const res = await request(app).post('/api/inspections').set(authHeader())
      .send({ articleId: 2147483000, inspectedAt: '2026-01-01', inspectedBy: 'Vitest' });
    expect(res.status).toBe(404);
  });
});
