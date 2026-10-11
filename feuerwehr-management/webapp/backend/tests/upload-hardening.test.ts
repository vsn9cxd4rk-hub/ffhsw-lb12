import { describe, it, expect, afterAll } from 'vitest';
import fs from 'fs';
import path from 'path';
import request from 'supertest';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { recordUploadDir } from '../src/utils/files';
import { decodeOriginalName } from '../src/utils/upload';
import { authHeader } from './setup';

// Uploads are stored under random names and must really be the declared type (utils/upload.ts).
// Before, a .html declared as application/pdf was accepted and stored under its original name.

const createdEvents: number[] = [];

async function createEvent(): Promise<number> {
  const event = await prisma.event.create({ data: { name: `Upload-Test-${Date.now()}`, date: new Date('2026-01-05'), category: 1 } });
  createdEvents.push(event.id);
  return event.id;
}

function filesIn(eventId: number): string[] {
  const dir = recordUploadDir('events', String(eventId));
  return fs.existsSync(dir) ? fs.readdirSync(dir) : [];
}

describe('Upload-Härtung', () => {
  afterAll(async () => {
    for (const id of createdEvents) {
      fs.rmSync(recordUploadDir('events', String(id)), { recursive: true, force: true });
    }
    await prisma.eventDocument.deleteMany({ where: { eventId: { in: createdEvents } } });
    await prisma.event.deleteMany({ where: { id: { in: createdEvents } } });
  });

  it('HTML mit Content-Type application/pdf wird abgelehnt und nicht gespeichert', async () => {
    const eventId = await createEvent();
    const res = await request(app)
      .post(`/api/events/${eventId}/documents`)
      .set(authHeader())
      .attach('file', Buffer.from('<html><script>alert(1)</script></html>'), { filename: 'bericht.html', contentType: 'application/pdf' });

    expect(res.status).toBe(400);
    expect(filesIn(eventId)).toHaveLength(0);
    expect(await prisma.eventDocument.count({ where: { eventId } })).toBe(0);
  });

  it('Nicht erlaubter MIME-Typ wird abgelehnt', async () => {
    const eventId = await createEvent();
    const res = await request(app)
      .post(`/api/events/${eventId}/documents`)
      .set(authHeader())
      .attach('file', Buffer.from('<html></html>'), { filename: 'x.html', contentType: 'text/html' });

    expect(res.status).toBe(400);
    expect(filesIn(eventId)).toHaveLength(0);
  });

  it('Echte PDF wird unter zufälligem Namen gespeichert, Originalname bleibt für die Anzeige', async () => {
    const eventId = await createEvent();
    const res = await request(app)
      .post(`/api/events/${eventId}/documents`)
      .set(authHeader())
      .attach('file', Buffer.from('%PDF-1.4\n%test'), { filename: '$(touch x) Prüfbericht.pdf', contentType: 'application/pdf' });

    expect(res.status).toBe(201);
    const stored = path.basename(res.body.data.filePath);
    expect(stored).toMatch(/^[0-9a-f-]{36}\.pdf$/);
    expect(res.body.data.fileName).toBe('$(touch x) Prüfbericht.pdf');
  });

  it('Upload zu nicht existierender Veranstaltung hinterlässt keine Datei', async () => {
    const missingId = 2_000_000_000 - Math.floor(Date.now() % 1_000_000);
    createdEvents.push(missingId); // only for directory cleanup
    const res = await request(app)
      .post(`/api/events/${missingId}/documents`)
      .set(authHeader())
      .attach('file', Buffer.from('%PDF-1.4'), { filename: 'x.pdf', contentType: 'application/pdf' });

    expect(res.status).toBe(404);
    expect(filesIn(missingId)).toHaveLength(0);
  });

  it('decodeOriginalName repariert latin1-dekodierte Umlaute und lässt korrekte Namen in Ruhe', () => {
    const mangled = Buffer.from('Prüfprotokoll.pdf', 'utf8').toString('latin1');
    expect(decodeOriginalName(mangled)).toBe('Prüfprotokoll.pdf');
    expect(decodeOriginalName('Prüfprotokoll.pdf')).toBe('Prüfprotokoll.pdf');
    expect(decodeOriginalName('plain.pdf')).toBe('plain.pdf');
  });
});
