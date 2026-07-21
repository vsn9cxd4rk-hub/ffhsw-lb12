import { describe, it, expect } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';

describe('Einsätze (Operations) API', () => {
  let createdId: number;

  it('GET /api/operations gibt paginierte Liste', async () => {
    const res = await request(app)
      .get('/api/operations')
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data).toBeDefined();
    expect(res.body.pagination).toBeDefined();
  });

  it('POST /api/operations erstellt neuen Einsatz', async () => {
    const res = await request(app)
      .post('/api/operations')
      .set(authHeader())
      .send({
        date: '2026-07-21T00:00:00.000Z',
        location: 'Testort - Teststrasse 99',
        alarmTime: '14:00',
        departureTime: '14:05',
        arrivalTime: '14:10',
        returnTime: '15:00',
        keyword: 'Brand 1',
        leaderCount: 1,
        memberCount: 5,
        reportType: 'Einsatzbericht',
        operationType: 'Kleinbrand a',
        ilsOrderNumber: 'TEST-12345',
        callerInfo: 'Testmelder',
        policeInfo: 'PI Test',
        situationOnArrival: 'Testsituation',
        actionsTaken: 'Testmaßnahmen',
        resourcesUsed: 'keine',
        createdByName: 'Test User',
        authorRole: 'Einsatzleiter',
      });

    expect(res.status).toBe(201);
    expect(res.body.data.id).toBeDefined();
    expect(res.body.data.location).toBe('Testort - Teststrasse 99');
    expect(res.body.data.reportType).toBe('Einsatzbericht');
    expect(res.body.data.operationType).toBe('Kleinbrand a');
    expect(res.body.data.ilsOrderNumber).toBe('TEST-12345');
    createdId = res.body.data.id;
  });

  it('GET /api/operations/:id gibt Einsatz-Details', async () => {
    const res = await request(app)
      .get(`/api/operations/${createdId}`)
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data.location).toBe('Testort - Teststrasse 99');
    expect(res.body.data.callerInfo).toBe('Testmelder');
    expect(res.body.data.actionsTaken).toBe('Testmaßnahmen');
  });

  it('PUT /api/operations/:id aktualisiert Einsatz', async () => {
    const res = await request(app)
      .put(`/api/operations/${createdId}`)
      .set(authHeader())
      .send({
        location: 'Geänderter Ort',
        situationOnArrival: 'Geänderte Lage',
        rescuedPersons: 2,
      });

    expect(res.status).toBe(200);
    expect(res.body.data.location).toBe('Geänderter Ort');
    expect(res.body.data.situationOnArrival).toBe('Geänderte Lage');
    expect(res.body.data.rescuedPersons).toBe(2);
  });

  it('POST /api/operations/:id/times erstellt Fahrzeugzeit', async () => {
    const res = await request(app)
      .post(`/api/operations/${createdId}/times`)
      .set(authHeader())
      .send({
        vehicleName: '12/45',
        alarmTime: '14:00',
        departureTime: '14:05',
        arrivalTime: '14:10',
        returnTime: '15:00',
      });

    expect(res.status).toBe(201);
    expect(res.body.data.vehicleName).toBe('12/45');
  });

  it('GET /api/operations/:id enthält Fahrzeugzeiten', async () => {
    const res = await request(app)
      .get(`/api/operations/${createdId}`)
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data.times).toBeDefined();
    expect(res.body.data.times.length).toBeGreaterThan(0);
  });

  it('Jahresfilter funktioniert', async () => {
    const res = await request(app)
      .get('/api/operations?year=2026')
      .set(authHeader());

    expect(res.status).toBe(200);
  });

  it('DELETE /api/operations/:id löscht Einsatz', async () => {
    const res = await request(app)
      .delete(`/api/operations/${createdId}`)
      .set(authHeader());

    expect(res.status).toBe(200);
  });

  it('GET /api/operations/:id nach Löschung gibt 404', async () => {
    const res = await request(app)
      .get(`/api/operations/${createdId}`)
      .set(authHeader());

    expect(res.status).toBe(404);
  });
});
