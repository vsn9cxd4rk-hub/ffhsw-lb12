import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';
import { authHeader } from './setup';

// CSV-Import (Einstellungen > Datenimport): Das Frontend schickt die geparsten Zeilen als JSON.
// Geprüft werden Pflichtfelder, Nachschlagen per Name, Duplikate und die Zählung.

const S = `${process.pid}-${Date.now()}`;
const INV = (n: number) => `VT${S}-${n}`;
const LAST_NAME = `Vitest-Import-${S}`;

let warehouseId: number;
let deviceClassId: number;
let inspectionTypeId: number;
let memberGroupId: number;
let nonAdminId: number;
let nonAdminToken: string;

const post = (path: string, body: unknown) =>
  request(app).post(`/api/settings/import/${path}`).set(authHeader()).send(body);

describe('CSV-Import', () => {
  beforeAll(async () => {
    warehouseId = (await prisma.warehouse.create({ data: { name: `Vitest-Lager ${S}` } })).id;
    deviceClassId = (await prisma.deviceClass.create({
      data: { name: `Vitest-Importklasse ${S}`, subclasses: { create: { name: `Vitest-Importunterklasse ${S}` } } },
    })).id;
    inspectionTypeId = (await prisma.inspectionType.create({ data: { name: `Vitest-Importprüfart ${S}` } })).id;
    memberGroupId = (await prisma.memberGroup.create({ data: { name: `Vitest-Importgruppe ${S}` } })).id;

    const user = await prisma.user.create({ data: { username: `import-test-${S}`, password: 'x', groupId: 1 } });
    nonAdminId = user.id;
    nonAdminToken = signAccessToken({ userId: user.id, username: user.username, isAdmin: false, groupId: 1 });
  });

  afterAll(async () => {
    await prisma.article.deleteMany({ where: { inventoryNumber: { startsWith: `VT${S}-` } } });
    await prisma.member.deleteMany({ where: { lastName: LAST_NAME } });
    await prisma.memberGroup.deleteMany({ where: { id: memberGroupId } });
    await prisma.inspectionType.deleteMany({ where: { id: inspectionTypeId } });
    await prisma.deviceClass.deleteMany({ where: { id: deviceClassId } });
    await prisma.warehouse.deleteMany({ where: { id: warehouseId } });
    await prisma.user.deleteMany({ where: { id: nonAdminId } });
  });

  it('ist nur für Administratoren erlaubt', async () => {
    for (const path of ['articles', 'inspections', 'members']) {
      const res = await request(app).post(`/api/settings/import/${path}`)
        .set('Authorization', `Bearer ${nonAdminToken}`).send({ [path]: [{}] });
      expect(res.status).toBe(403);
    }
  });

  it('lehnt leere Dateien ab', async () => {
    expect((await post('articles', { articles: [] })).status).toBe(400);
    expect((await post('inspections', {})).status).toBe(400);
    expect((await post('members', { members: 'kein Array' })).status).toBe(400);
  });

  describe('Artikel', () => {
    it('importiert gültige Zeilen und löst Lagerort und Geräteklasse per Name auf', async () => {
      const res = await post('articles', {
        articles: [{
          name: '  Vitest-Schlauch  ',
          inventoryNumber: INV(1),
          warehouse: `VITEST-LAGER ${S}`, // Groß-/Kleinschreibung egal
          deviceClass: `Vitest-Importklasse ${S}`,
          deviceSubclass: `Vitest-Importunterklasse ${S}`,
          inspectionInterval: '12',
          manufacturingDate: '2020-05-01',
          value: '149.90',
        }],
      });
      expect(res.status).toBe(200);
      expect(res.body.data).toMatchObject({ imported: 1, skipped: 0, errors: [], total: 1 });

      const article = await prisma.article.findUniqueOrThrow({
        where: { inventoryNumber: INV(1) },
        include: { deviceSubclass: true },
      });
      expect(article.name).toBe('Vitest-Schlauch');
      expect(article.warehouseId).toBe(warehouseId);
      expect(article.deviceSubclass?.deviceClassId).toBe(deviceClassId);
      expect(article.inspectionInterval).toBe(12);
      expect(article.designationLB).toBe('LB12');
      expect(article.isDecommissioned).toBe(false);
    });

    it('meldet fehlerhafte Zeilen mit Zeilennummer und importiert den Rest', async () => {
      const res = await post('articles', {
        articles: [
          { name: '', inventoryNumber: INV(10) },
          { name: 'Vitest A', inventoryNumber: INV(11), warehouse: 'Gibt es nicht' },
          { name: 'Vitest B', inventoryNumber: INV(12), deviceClass: `Vitest-Importklasse ${S}` },
          { name: 'Vitest C', inventoryNumber: INV(13), deviceClass: 'Gibt es nicht', deviceSubclass: 'X' },
          { name: 'Vitest D', inventoryNumber: INV(14), deviceSubclass: 'Gibt es nicht' },
          { name: 'Vitest E', inventoryNumber: INV(15), commissionedDate: '31.12.2020' },
          { name: 'Vitest F', inventoryNumber: INV(16), decommissionedDate: '2024-01-01' },
        ],
      });
      expect(res.status).toBe(200);
      const { imported, errors } = res.body.data;
      expect(imported).toBe(1);
      // Kopfzeile = Zeile 1, die Daten beginnen in Zeile 2
      expect(errors.map((e: { row: number }) => e.row)).toEqual([2, 3, 4, 5, 6, 7]);
      expect(errors[0].message).toMatch(/Pflichtfeld/);
      expect(errors[1].message).toMatch(/Lagerort/);
      expect(errors[2].message).toMatch(/keine Unterklasse/);
      expect(errors[5].message).toMatch(/Ungültiges Datum/);

      const decommissioned = await prisma.article.findUniqueOrThrow({ where: { inventoryNumber: INV(16) } });
      expect(decommissioned.isDecommissioned).toBe(true);
    });

    it('überspringt vorhandene und in der Datei doppelte Inventarnummern', async () => {
      const res = await post('articles', {
        articles: [
          { name: 'Schon vorhanden', inventoryNumber: INV(1).toLowerCase() },
          { name: 'Neu', inventoryNumber: INV(20) },
          { name: 'Doppelt in der Datei', inventoryNumber: INV(20) },
          { name: `Vitest ohne Inventarnummer ${S}` },
        ],
      });
      expect(res.status).toBe(200);
      expect(res.body.data).toMatchObject({ imported: 2, skipped: 2, errors: [], total: 4 });
      expect((await prisma.article.findUniqueOrThrow({ where: { inventoryNumber: INV(20) } })).name).toBe('Neu');
      await prisma.article.deleteMany({ where: { name: `Vitest ohne Inventarnummer ${S}`, inventoryNumber: null } });
    });
  });

  describe('Prüfungen', () => {
    it('importiert Prüfungen und berechnet die nächste Fälligkeit', async () => {
      const res = await post('inspections', {
        inspections: [{
          inventoryNumber: INV(1),
          inspectedAt: '2026-03-01',
          inspectedBy: ' Vitest ',
          result: 'PASSED',
          inspectionType: `vitest-importprüfart ${S}`,
          notes: 'ok',
        }],
      });
      expect(res.status).toBe(200);
      expect(res.body.data).toMatchObject({ imported: 1, errors: [], total: 1 });

      const inspection = await prisma.articleInspection.findFirstOrThrow({ where: { article: { inventoryNumber: INV(1) } } });
      expect(inspection.result).toBe('passed');
      expect(inspection.inspectedBy).toBe('Vitest');
      expect(inspection.inspectionTypeId).toBe(inspectionTypeId);
      expect(inspection.nextDueDate?.toISOString()).toBe('2027-03-01T00:00:00.000Z');
    });

    it('meldet fehlende Pflichtfelder, unbekannte Artikel und ungültige Werte', async () => {
      const base = { inventoryNumber: INV(1), inspectedAt: '2026-03-01', inspectedBy: 'Vitest', result: 'failed' };
      const res = await post('inspections', {
        inspections: [
          { ...base, inventoryNumber: '' },
          { ...base, inspectedAt: '' },
          { ...base, inspectedBy: '' },
          { ...base, result: 'ok' },
          { ...base, inventoryNumber: 'gibt-es-nicht' },
          { ...base, inspectionType: 'Gibt es nicht' },
          { ...base, inspectedAt: 'gestern' },
        ],
      });
      expect(res.status).toBe(200);
      expect(res.body.data.imported).toBe(0);
      const messages = res.body.data.errors.map((e: { message: string }) => e.message);
      expect(messages).toHaveLength(7);
      expect(messages[0]).toMatch(/Inventarnummer ist ein Pflichtfeld/);
      expect(messages[3]).toMatch(/passed.*failed/);
      expect(messages[4]).toMatch(/nicht gefunden/);
      expect(messages[5]).toMatch(/Prüfart/);
      expect(messages[6]).toMatch(/Ungültiges Prüfdatum/);
    });
  });

  describe('Mitglieder', () => {
    it('importiert Mitglieder mit Gruppe und Qualifikationen', async () => {
      const res = await post('members', {
        members: [{
          firstName: 'Anna', lastName: LAST_NAME, group: `vitest-importgruppe ${S}`,
          birthDate: '1990-04-12', qualAGT: 'ja', qualMachinist: 'x', qualTH1: '0', email: ' anna@example.org ',
        }],
      });
      expect(res.status).toBe(200);
      expect(res.body.data).toMatchObject({ imported: 1, skipped: 0, errors: [], total: 1 });

      const member = await prisma.member.findFirstOrThrow({ where: { lastName: LAST_NAME, firstName: 'Anna' } });
      expect(member.groupId).toBe(memberGroupId);
      expect(member.qualAGT).toBe(true);
      expect(member.qualMachinist).toBe(true);
      expect(member.qualTH1).toBe(false);
      expect(member.email).toBe('anna@example.org');
      expect(member.birthDate?.toISOString().slice(0, 10)).toBe('1990-04-12');
    });

    it('überspringt vorhandene und doppelte Namen, meldet Pflichtfelder und unbekannte Gruppen', async () => {
      const res = await post('members', {
        members: [
          { firstName: 'ANNA', lastName: LAST_NAME.toLowerCase() },
          { firstName: 'Ben', lastName: LAST_NAME },
          { firstName: 'Ben', lastName: LAST_NAME },
          { firstName: '', lastName: LAST_NAME },
          { firstName: 'Carla', lastName: LAST_NAME, group: 'Gibt es nicht' },
        ],
      });
      expect(res.status).toBe(200);
      expect(res.body.data).toMatchObject({ imported: 1, skipped: 2, total: 5 });
      expect(res.body.data.errors.map((e: { row: number }) => e.row)).toEqual([5, 6]);
      expect(await prisma.member.count({ where: { lastName: LAST_NAME } })).toBe(2);
    });
  });
});
