import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { authHeader } from './setup';

// Fahrtenbuch: Der km-Stand am Ende darf nicht unter dem am Start liegen.

let vehicleId: number;
const entry = { date: '2026-10-01', driver: 'Vitest', purpose: 'Übungsfahrt' };

describe('Fahrtenbuch', () => {
  beforeAll(async () => {
    vehicleId = (await prisma.vehicle.create({ data: { name: `Vitest-Fahrzeug ${process.pid}-${Date.now()}` } })).id;
  });

  afterAll(async () => {
    await prisma.vehicle.deleteMany({ where: { id: vehicleId } });
  });

  it('lehnt einen Eintrag mit km-Stand Ende < Start ab', async () => {
    const res = await request(app).post(`/api/vehicles/${vehicleId}/logbook`).set(authHeader())
      .send({ ...entry, startMileage: 12000, endMileage: 11999 });
    expect(res.status).toBe(400);
    expect(res.body.error).toMatch(/km-Stand/);
    expect(await prisma.logbookEntry.count({ where: { vehicleId } })).toBe(0);
  });

  it('erlaubt gleiche und steigende km-Stände', async () => {
    const same = await request(app).post(`/api/vehicles/${vehicleId}/logbook`).set(authHeader())
      .send({ ...entry, startMileage: 12000, endMileage: 12000 });
    expect(same.status).toBe(201);
    const more = await request(app).post(`/api/vehicles/${vehicleId}/logbook`).set(authHeader())
      .send({ ...entry, startMileage: 12000, endMileage: 12042 });
    expect(more.status).toBe(201);
  });

  it('prüft beim Bearbeiten auch gegen den gespeicherten Wert', async () => {
    const created = await request(app).post(`/api/vehicles/${vehicleId}/logbook`).set(authHeader())
      .send({ ...entry, startMileage: 500, endMileage: 600 });
    const url = `/api/vehicles/${vehicleId}/logbook/${created.body.data.id}`;

    expect((await request(app).put(url).set(authHeader()).send({ endMileage: 499 })).status).toBe(400);
    expect((await request(app).put(url).set(authHeader()).send({ startMileage: 601 })).status).toBe(400);
    expect((await request(app).put(url).set(authHeader()).send({ purpose: 'Nur Zweck geändert' })).status).toBe(200);

    const ok = await request(app).put(url).set(authHeader()).send({ endMileage: 650 });
    expect(ok.status).toBe(200);
    expect(ok.body.data.endMileage).toBe(650);
  });
});
