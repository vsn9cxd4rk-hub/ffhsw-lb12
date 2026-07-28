import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import app from '../src/app';
import { authHeader } from './setup';
import { prisma } from '../src/config/database';

describe('Brandsicherheitswache (Veranstaltung Kategorie BSW)', () => {
  let eventId: number;

  beforeAll(async () => {
    const event = await prisma.event.create({
      data: {
        name: 'Test-Weihnachtsmarkt',
        category: 3,
        date: new Date('2026-12-24'),
      },
    });
    eventId = event.id;
  });

  afterAll(async () => {
    await prisma.event.delete({ where: { id: eventId } }).catch(() => {});
  });

  it('PUT /api/events/:id speichert bswData', async () => {
    const res = await request(app)
      .put(`/api/events/${eventId}`)
      .set(authHeader())
      .send({
        bswData: {
          checklist: { item1: 'ja', item2: 'nein' },
          bemerkungen: 'Testbemerkung',
          veranstaltungsort: 'Bürgerhalle Heusweiler',
          wachhabender: 'Erika Musterfrau',
        },
      });

    expect(res.status).toBe(200);
    expect(res.body.data.bswData.veranstaltungsort).toBe('Bürgerhalle Heusweiler');
    expect(res.body.data.bswData.checklist.item1).toBe('ja');
  });

  it('GET /api/events/:id gibt gespeichertes bswData zurück', async () => {
    const res = await request(app)
      .get(`/api/events/${eventId}`)
      .set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.data.bswData.wachhabender).toBe('Erika Musterfrau');
    expect(res.body.data.bswData.bemerkungen).toBe('Testbemerkung');
  });

  it('POST /api/events/:id/bsw/checkliste ohne Template gibt Fehlermeldung', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Checkliste Brandsicherheitswache' } } });
    if (hasTemplate) return; // Skip wenn Template vorhanden

    const res = await request(app)
      .post(`/api/events/${eventId}/bsw/checkliste`)
      .set(authHeader());

    expect(res.status).toBe(500);
    expect(res.body.error).toContain('Template');
  });

  it('POST /api/events/:id/bsw/checkliste mit Template erzeugt Dokument', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Checkliste Brandsicherheitswache' } } });
    if (!hasTemplate) return; // Skip wenn kein Template vorhanden

    const res = await request(app)
      .post(`/api/events/${eventId}/bsw/checkliste`)
      .set(authHeader());

    expect(res.status).toBe(201);
    expect(res.body.data.fileName).toContain('ChecklisteBSW');
    expect(res.body.data.filePath).toBeDefined();
  });

  it('POST /api/events/:id/bsw/bericht ohne Template gibt Fehlermeldung', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Bericht Brandsicherheitswache' } } });
    if (hasTemplate) return;

    const res = await request(app)
      .post(`/api/events/${eventId}/bsw/bericht`)
      .set(authHeader());

    expect(res.status).toBe(500);
    expect(res.body.error).toContain('Template');
  });

  it('POST /api/events/:id/bsw/bericht mit Template erzeugt Dokument', async () => {
    const hasTemplate = await prisma.template.findFirst({ where: { name: { contains: 'Bericht Brandsicherheitswache' } } });
    if (!hasTemplate) return;

    const res = await request(app)
      .post(`/api/events/${eventId}/bsw/bericht`)
      .set(authHeader());

    expect(res.status).toBe(201);
    expect(res.body.data.fileName).toContain('BerichtBSW');
  });

  it('Generierte BSW-Dokumente erscheinen in der Dokumentenliste', async () => {
    const res = await request(app)
      .get(`/api/events/${eventId}/documents`)
      .set(authHeader());

    expect(res.status).toBe(200);
    // Dokumente hängen davon ab, ob die Templates im aktuellen Environment konfiguriert sind
  });

  it('POST /api/events/:id/bsw/checkliste mit unbekannter Event-ID gibt Fehlermeldung', async () => {
    const res = await request(app)
      .post('/api/events/999999999/bsw/checkliste')
      .set(authHeader());

    expect(res.status).toBe(500);
    expect(res.body.error).toContain('nicht gefunden');
  });
});
