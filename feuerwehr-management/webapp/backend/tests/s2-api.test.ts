import { describe, it, expect, beforeAll, afterAll } from 'vitest';
import request from 'supertest';
import PizZip from 'pizzip';
import app from '../src/app';
import { prisma } from '../src/config/database';
import { signAccessToken } from '../src/config/jwt';
import { BIT_OPERATIONS, BIT_S2 } from '../src/config/permissionBits';
import { authHeader } from './setup';

// S2 Lage: module only for holders of the S2 bit (Gruppenführer); several people edit one Lage,
// an update based on an outdated version must be rejected instead of overwriting.

const STAMP = Date.now();
const groupIds: number[] = [];
const userIds: number[] = [];
let gfToken: string;
let otherToken: string;
let lageId: number;

const bearer = (t: string) => ({ Authorization: `Bearer ${t}` });
const binary = (res: request.Response, cb: (err: Error | null, body: Buffer) => void) => {
  const chunks: Buffer[] = [];
  res.on('data', (c: Buffer) => chunks.push(c));
  res.on('end', () => cb(null, Buffer.concat(chunks)));
};

async function makeUser(name: string, perms: Record<string, boolean>): Promise<string> {
  const group = await prisma.permissionGroup.create({ data: { name: `s2-${name}-${STAMP}`, ...perms } });
  groupIds.push(group.id);
  const user = await prisma.user.create({ data: { username: `s2-${name}-${STAMP}`, name: `GF ${name}`, password: 'x', groupId: group.id } });
  userIds.push(user.id);
  return signAccessToken({ userId: user.id, username: user.username, isAdmin: false, groupId: group.id });
}

describe('S2 Lage API', () => {
  beforeAll(async () => {
    gfToken = await makeUser('gf', { [BIT_S2]: true });
    otherToken = await makeUser('ohne', { [BIT_OPERATIONS]: true });
    const res = await request(app).post('/api/s2/lagen').set(bearer(gfToken))
      .send({ name: `Übung Unwetter ${STAMP}`, isExercise: true, recipient: 'uebung@example.org' });
    expect(res.status).toBe(201);
    lageId = res.body.data.id;
  });

  afterAll(async () => {
    await prisma.s2Lage.deleteMany({ where: { name: { contains: String(STAMP) } } });
    await prisma.user.deleteMany({ where: { id: { in: userIds } } });
    await prisma.permissionGroup.deleteMany({ where: { id: { in: groupIds } } });
  });

  it('ohne S2-Berechtigung kein Zugriff, auch nicht lesend', async () => {
    expect((await request(app).get('/api/s2/lagen').set(bearer(otherToken))).status).toBe(403);
    expect((await request(app).get(`/api/s2/lagen/${lageId}`).set(bearer(otherToken))).status).toBe(403);
    expect((await request(app).get('/api/s2/lagen').set(authHeader())).status).toBe(200); // Admin
  });

  it('Abschnitte, Kräfte und Kartenobjekte; Kräfteübersicht mit Gesamtstärke', async () => {
    const ea1 = await request(app).post(`/api/s2/lagen/${lageId}/abschnitte`).set(bearer(gfToken))
      .send({ nr: 1, name: 'Heusweiler', schwerpunkt: true, flaeche: [[348300, 5466400], [350300, 5466400], [350300, 5467900], [348300, 5467900]] });
    expect(ea1.status).toBe(201);
    const ea2 = await request(app).post(`/api/s2/lagen/${lageId}/abschnitte`).set(bearer(gfToken))
      .send({ nr: 2, name: 'Eiweiler', schwerpunkt: true });
    expect(ea2.status).toBe(201);
    // only one raumbezogener Schwerpunkt
    const state0 = await request(app).get(`/api/s2/lagen/${lageId}`).set(bearer(gfToken));
    expect(state0.body.data.abschnitte.map((a: { schwerpunkt: boolean }) => a.schwerpunkt)).toEqual([false, true]);
    expect((await request(app).post(`/api/s2/lagen/${lageId}/abschnitte`).set(bearer(gfToken)).send({ nr: 1, name: 'doppelt' })).status).toBe(409);

    for (const k of [
      { org: 'FW', formation: 'Löschbezirk', bezeichnung: 'LB 111', staerke: '1/8/9', status: 'einsatz', abschnittId: ea1.body.data.id },
      { org: 'THW', formation: 'TZ (Technischer Zug)', bezeichnung: 'OV SB', staerke: '2/5/24', status: 'bereitschaft' },
    ]) {
      expect((await request(app).post(`/api/s2/lagen/${lageId}/kraefte`).set(bearer(gfToken)).send(k)).status).toBe(201);
    }
    const bad = await request(app).post(`/api/s2/lagen/${lageId}/kraefte`).set(bearer(gfToken))
      .send({ org: 'FW', bezeichnung: 'LB 112', staerke: 'ca. 10' });
    expect(bad.status).toBe(400);
    expect(bad.body.error).toMatch(/Stärke/);

    expect((await request(app).post(`/api/s2/lagen/${lageId}/objekte`).set(bearer(gfToken))
      .send({ art: 'zeichen', typ: 'STELLE_02', punkte: [[349600, 5467200]], text: 'BR Sportplatz' })).status).toBe(201);
    expect((await request(app).post(`/api/s2/lagen/${lageId}/objekte`).set(bearer(gfToken))
      .send({ art: 'flaeche', typ: 'ueberschwemmt', punkte: [[349000, 5466600], [349500, 5466600], [349300, 5466900]] })).status).toBe(201);
    const aussen = await request(app).post(`/api/s2/lagen/${lageId}/objekte`).set(bearer(gfToken))
      .send({ art: 'zeichen', typ: 'BRAND_01', punkte: [[360000, 5467000]] });
    expect(aussen.status).toBe(400);
    expect((await request(app).post(`/api/s2/lagen/${lageId}/objekte`).set(bearer(gfToken))
      .send({ art: 'zeichen', typ: '../../etc', punkte: [[349600, 5467200]] })).status).toBe(400);

    const state = await request(app).get(`/api/s2/lagen/${lageId}`).set(bearer(gfToken));
    expect(state.body.data.kraefte).toHaveLength(2);
    expect(state.body.data.kraefteUebersicht.staerkeGesamt).toBe(9 + 31);
    expect(state.body.data.kraefteUebersicht.kraefte.einsatz).toBe('FW: LB 111');
  });

  it('gleichzeitiges Bearbeiten: veraltete Version wird mit 409 abgelehnt', async () => {
    const state = await request(app).get(`/api/s2/lagen/${lageId}`).set(bearer(gfToken));
    const kraft = state.body.data.kraefte[0];
    const first = await request(app).put(`/api/s2/kraefte/${kraft.id}`).set(bearer(gfToken)).send({ version: kraft.version, status: 'ruhe' });
    expect(first.status).toBe(200);
    expect(first.body.data.version).toBe(kraft.version + 1);
    const second = await request(app).put(`/api/s2/kraefte/${kraft.id}`).set(authHeader()).send({ version: kraft.version, status: 'einsatz' });
    expect(second.status).toBe(409);
    expect(second.body.error).toMatch(/zwischenzeitlich von GF gf geändert/);
    expect(second.body.data.status).toBe('ruhe');
    expect((await request(app).put(`/api/s2/kraefte/${kraft.id}`).set(bearer(gfToken)).send({ status: 'ruhe' })).status).toBe(400);
  });

  it('stündliche Fortschreibung: Nr. 2 übernimmt den Inhalt von Nr. 1', async () => {
    const b1 = await request(app).post(`/api/s2/lagen/${lageId}/berichte`).set(bearer(gfToken));
    expect(b1.status).toBe(201);
    expect(b1.body.data.lfdNr).toBe(1);
    expect(b1.body.data.zeit).toMatch(/^\d{2}\d{2}00[a-z]{3}\d{2}$/); // nächste volle Stunde
    const data = {
      staerke: { gesamt: 40 }, kurzfassung: 'Allgemein: Starkregen\nEA Eiweiler: Keller überflutet',
      beurteilung: { schadenslage: { stufe: 2, prognose: 'Stagnation' } }, probleme: '1. Sandsäcke fehlen',
      aufgaben: { technische_hilfe: { text: 'Wasserschaden / Pumpen', schwerpunkt: true } },
    };
    const upd = await request(app).put(`/api/s2/berichte/${b1.body.data.id}`).set(bearer(gfToken))
      .send({ version: b1.body.data.version, data, zeit: '141100nov26' });
    expect(upd.status).toBe(200);
    expect((await request(app).put(`/api/s2/berichte/${b1.body.data.id}`).set(bearer(gfToken))
      .send({ version: upd.body.data.version, zeit: '141100okt26' })).status).toBe(400);

    const b2 = await request(app).post(`/api/s2/lagen/${lageId}/berichte`).set(bearer(gfToken));
    expect(b2.body.data.lfdNr).toBe(2);
    expect(b2.body.data.data).toEqual(data);
    // only the newest draft may be deleted
    expect((await request(app).delete(`/api/s2/berichte/${b1.body.data.id}`).set(bearer(gfToken))).status).toBe(400);
    expect((await request(app).delete(`/api/s2/berichte/${b2.body.data.id}`).set(bearer(gfToken))).status).toBe(200);
  });

  it('Prüfung, Mailtext und Export Lagebericht/Lagekarte', async () => {
    const state = await request(app).get(`/api/s2/lagen/${lageId}`).set(bearer(gfToken));
    const id = state.body.data.berichte[0].id;
    const p = await request(app).get(`/api/s2/berichte/${id}/pruefung`).set(bearer(gfToken));
    expect(p.status).toBe(200);
    expect(p.body.data.mail.betreff).toContain('ÜBUNG');
    expect(p.body.data.mail.an).toBe('uebung@example.org');
    expect(p.body.data.warnungen.join('\n')).toMatch(/EA 2 hat keine Grenze/);

    const xlsx = await request(app).get(`/api/s2/berichte/${id}/lagebericht.xlsx`).set(bearer(gfToken)).buffer(true).parse(binary);
    expect(xlsx.status).toBe(200);
    expect(xlsx.headers['content-disposition']).toContain('Lagebericht_Heusweiler_141100nov26_V1.xlsx');
    const sheet = new PizZip(xlsx.body).file('xl/worksheets/sheet1.xml')!.asText();
    expect(sheet).toMatch(/<c r="E31"[^>]*><v>40<\/v>/);
    expect(sheet).toMatch(/<c r="S48"[^>]*><is><t xml:space="preserve">Eiweiler<\/t>/); // EA 2 aus den Abschnitten

    const pptx = await request(app).get(`/api/s2/berichte/${id}/lagekarte.pptx`).set(bearer(gfToken)).buffer(true).parse(binary);
    expect(pptx.status).toBe(200);
    const slide = new PizZip(pptx.body).file('ppt/slides/slide1.xml')!.asText();
    expect(slide).toContain('EA 1: Heusweiler');
    expect((slide.match(/<p:cxnSp>/g) ?? []).length).toBe(2); // EA_01 + Bereitstellungsraum
  });
});
