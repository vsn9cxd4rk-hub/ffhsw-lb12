import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';
import { prisma } from '../src/config/database';
import { reportCommander, withRank } from '../src/services/report-generator.service';
import { earliestAlarmTime } from '../src/controllers/operation.controller';

// Einsatzleiter im Einsatzbericht und die aus den Fahrzeugzeiten abgeleitete Alarmzeit.

const SUFFIX = `${process.pid}${Date.now()}`;
const FIRST = `Kommandant${SUFFIX}`;
const LAST = `Tester${SUFFIX}`;
const NAMESAKE_FIRST = `Andere${SUFFIX}`;

describe('Einsatz: Einsatzleiter und Alarmzeit', () => {
  const memberIds: number[] = [];
  let operationId: number;

  beforeAll(async () => {
    const m = await prisma.member.create({ data: { firstName: FIRST, lastName: LAST, rank: 'HBM' } });
    // Same last name, different first name: must not lend its rank
    const namesake = await prisma.member.create({ data: { firstName: NAMESAKE_FIRST, lastName: LAST, rank: 'OFM' } });
    memberIds.push(m.id, namesake.id);

    const op = await prisma.operation.create({
      data: { date: new Date('2026-08-01'), location: `Testort Einsatzleiter ${SUFFIX}`, alarmTime: '10:00' },
    });
    operationId = op.id;
  });

  afterAll(async () => {
    await prisma.operation.deleteMany({ where: { id: operationId } });
    await prisma.member.deleteMany({ where: { id: { in: memberIds } } });
  });

  it('Dienstgrad nur bei exakt passendem Vor- und Nachnamen', async () => {
    expect(await withRank(`${FIRST} ${LAST}`)).toBe(`HBM ${FIRST} ${LAST}`);
    expect(await withRank(`Unbekannt ${LAST}`)).toBe(`Unbekannt ${LAST}`);
    expect(await withRank(LAST)).toBe(LAST);
    expect(await withRank('')).toBe('');
  });

  it('Ersteller ist Einsatzleiter: wird ins Feld Einsatzleiter übernommen', async () => {
    const name = await reportCommander({ authorRole: 'Einsatzleiter', createdByName: `${FIRST} ${LAST}`, commanderName: 'Ignoriert', commanderId: null });
    expect(name).toBe(`HBM ${FIRST} ${LAST}`);
    // Without a role the report treats the author as Einsatzleiter (template default)
    expect(await reportCommander({ authorRole: null, createdByName: 'Max Muster', commanderName: null, commanderId: null })).toBe('Max Muster');
  });

  it('Ersteller ist Einheitenführer: separat eingetragener Einsatzleiter', async () => {
    const name = await reportCommander({
      authorRole: 'Einheitenführer LB Wahlschied', createdByName: 'Max Muster', commanderName: `${FIRST} ${LAST}`, commanderId: null,
    });
    expect(name).toBe(`HBM ${FIRST} ${LAST}`);
    // Einsatzleiter aus einem anderen Löschbezirk (kein Mitglied)
    expect(await reportCommander({
      authorRole: 'Einheitenführer LB Wahlschied', createdByName: 'Max Muster', commanderName: 'Erika Extern', commanderId: null,
    })).toBe('Erika Extern');
  });

  it('PUT speichert den Einsatzleiter und leert ihn, wenn der Ersteller selbst Einsatzleiter ist', async () => {
    const asUnitLeader = await request(app).put(`/api/operations/${operationId}`).set(authHeader())
      .send({ authorRole: 'Einheitenführer LB Wahlschied', createdByName: 'Max Muster', commanderName: 'Erika Extern' });
    expect(asUnitLeader.status).toBe(200);
    expect(asUnitLeader.body.data.commanderName).toBe('Erika Extern');

    const asCommander = await request(app).put(`/api/operations/${operationId}`).set(authHeader())
      .send({ authorRole: 'Einsatzleiter', commanderName: 'Erika Extern' });
    expect(asCommander.status).toBe(200);
    expect(asCommander.body.data.commanderName).toBeNull();
  });

  it('Ausgerückt/Eingetroffen/Eingerückt werden auf Einsatzebene nicht mehr gespeichert', async () => {
    const res = await request(app).put(`/api/operations/${operationId}`).set(authHeader())
      .send({ departureTime: '10:05', arrivalTime: '10:10', returnTime: '11:00' });
    expect(res.status).toBe(200);
    expect(res.body.data.departureTime).toBeNull();
    expect(res.body.data.returnTime).toBeNull();
  });

  it('früheste Alarmzeit der Fahrzeuge wird zur Alarmzeit des Einsatzes', async () => {
    expect(earliestAlarmTime([{ alarmTime: '14:50' }, { alarmTime: '9:05' }, { alarmTime: null }, { alarmTime: '' }])).toBe('9:05');
    expect(earliestAlarmTime([{ alarmTime: null }])).toBeNull();

    await request(app).post(`/api/operations/${operationId}/times`).set(authHeader())
      .send({ vehicleName: '12/45', alarmTime: '09:58' });
    await request(app).post(`/api/operations/${operationId}/times`).set(authHeader())
      .send({ vehicleName: '12/60', alarmTime: '10:02' });
    expect((await prisma.operation.findUnique({ where: { id: operationId } }))!.alarmTime).toBe('09:58');
  });

  it('ohne Fahrzeug-Alarmzeit bleibt die beim Anlegen erfasste Alarmzeit', async () => {
    const op = await prisma.operation.create({
      data: { date: new Date('2026-08-02'), location: `Testort Alarm ${SUFFIX}`, alarmTime: '07:30' },
    });
    try {
      await request(app).post(`/api/operations/${op.id}/times`).set(authHeader()).send({ vehicleName: '12/45' });
      expect((await prisma.operation.findUnique({ where: { id: op.id } }))!.alarmTime).toBe('07:30');
    } finally {
      await prisma.operation.delete({ where: { id: op.id } });
    }
  });
});
