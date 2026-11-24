/**
 * S2 Lage: Lagen, stündliche Lageberichte, Einsatzabschnitte, Kräfte, Objekte der Lagekarte und
 * Meldungseingang. Several people work on one Lage at the same time: every update carries the
 * `version` the client has seen; an outdated version is rejected with 409 and the current row,
 * so nobody silently overwrites a colleague's change. The clients poll GET /lagen/:id.
 */
import { Request, Response } from 'express';
import fs from 'fs';
import path from 'path';
import { prisma } from '../config/database';
import { Prisma } from '../generated/prisma/client';
import { sendError, sendSuccess } from '../utils/response';
import { param } from '../utils/params';
import { categoryUploadDir } from '../utils/files';
import { convertToPdf } from '../services/report-generator.service';
import { KRAFT_STATUS, isTaktZeit, kraefteUebersicht, naechsteVolleStunde, staerkeGesamt, taktZeit } from '../services/s2/common';
import { ORGS, erzeugeLagebericht } from '../services/s2/lagebericht';
import { AREA, LINE, ZEICHEN_DIR, erzeugeLagekarte, imKartenausschnitt } from '../services/s2/lagekarte';
import { berichtDaten, dateiname, lageKarte, mail, pruefung } from '../services/s2/export';
import { finde } from '../services/s2/geo';

function httpError(status: number, message: string): Error {
  return Object.assign(new Error(message), { statusCode: status });
}

function userName(req: Request): string {
  return (req.user?.name || req.user?.username || '').slice(0, 100);
}

function idParam(req: Request, name = 'id'): number {
  const v = param(req, name);
  if (!/^\d+$/.test(v)) throw httpError(400, 'Ungültige ID');
  return Number(v);
}

// ----------------------------------------------------------------------------
// Field validation (explicit whitelist per entity; unknown fields are ignored)
// ----------------------------------------------------------------------------

type Field =
  | { t: 'str'; max: number; req?: boolean }
  | { t: 'text'; req?: boolean }
  | { t: 'int'; min: number; max: number; req?: boolean; nullable?: boolean }
  | { t: 'bool' }
  | { t: 'enum'; values: readonly string[]; req?: boolean }
  | { t: 'date' }
  | { t: 'points'; nullable?: boolean }
  | { t: 'object' };
type Spec = Record<string, Field>;

function parseBody(body: Record<string, unknown>, spec: Spec, create: boolean): Record<string, unknown> {
  const out: Record<string, unknown> = {};
  for (const [key, f] of Object.entries(spec)) {
    const present = Object.prototype.hasOwnProperty.call(body, key);
    const v = body[key];
    const required = 'req' in f && f.req;
    if (!present) {
      if (create && required) throw httpError(400, `Feld "${key}" fehlt`);
      continue;
    }
    switch (f.t) {
      case 'str':
      case 'text': {
        const s = v == null ? '' : String(v).trim();
        if (!s) {
          if (required) throw httpError(400, `Feld "${key}" darf nicht leer sein`);
          out[key] = null;
        } else {
          if (f.t === 'str' && s.length > f.max) throw httpError(400, `Feld "${key}" ist zu lang (max. ${f.max} Zeichen)`);
          if (f.t === 'text' && s.length > 20000) throw httpError(400, `Feld "${key}" ist zu lang`);
          out[key] = s;
        }
        break;
      }
      case 'int': {
        if ((v == null || v === '') && f.nullable) { out[key] = null; break; }
        const n = Number(v);
        if (!Number.isInteger(n) || n < f.min || n > f.max) throw httpError(400, `Feld "${key}" muss eine Zahl von ${f.min} bis ${f.max} sein`);
        out[key] = n;
        break;
      }
      case 'bool':
        out[key] = v === true || v === 'true' || v === 1;
        break;
      case 'enum':
        if (!f.values.includes(String(v))) throw httpError(400, `Feld "${key}": ungültiger Wert "${String(v)}"`);
        out[key] = String(v);
        break;
      case 'date': {
        const d = new Date(String(v));
        if (Number.isNaN(d.getTime())) throw httpError(400, `Feld "${key}": ungültiges Datum`);
        out[key] = d;
        break;
      }
      case 'points': {
        if (v == null && f.nullable) { out[key] = Prisma.DbNull; break; }
        if (!Array.isArray(v) || v.length > 200
          || !v.every((p) => Array.isArray(p) && p.length === 2 && p.every((x) => typeof x === 'number' && Number.isFinite(x)))) {
          throw httpError(400, `Feld "${key}": Liste von Koordinaten [Ost, Nord] erwartet`);
        }
        const pts = (v as number[][]).map(([e, n]) => [Math.round(e), Math.round(n)]);
        if (pts.some(([e, n]) => !imKartenausschnitt(e, n))) throw httpError(400, 'Koordinate außerhalb des Kartenausschnitts Heusweiler');
        out[key] = pts;
        break;
      }
      case 'object':
        if (!v || typeof v !== 'object' || Array.isArray(v)) throw httpError(400, `Feld "${key}": Objekt erwartet`);
        if (JSON.stringify(v).length > 200_000) throw httpError(400, `Feld "${key}" ist zu groß`);
        out[key] = v;
        break;
    }
  }
  return out;
}

const LAGE_SPEC: Spec = {
  name: { t: 'str', max: 200, req: true },
  kommune: { t: 'str', max: 100 },
  isExercise: { t: 'bool' },
  status: { t: 'enum', values: ['aktiv', 'beendet'] },
  recipient: { t: 'str', max: 200 },
  contact: { t: 'str', max: 200 },
};
const BERICHT_SPEC: Spec = {
  zeit: { t: 'str', max: 11 },
  bearbeiter: { t: 'str', max: 100 },
  karteAlsFoto: { t: 'bool' },
  status: { t: 'enum', values: ['entwurf', 'versendet'] },
  data: { t: 'object' },
};

const ZEICHEN_IDS = new Set(fs.existsSync(ZEICHEN_DIR)
  ? fs.readdirSync(ZEICHEN_DIR).filter((f) => f.endsWith('.png')).map((f) => f.slice(0, -4)) : []);

interface Delegate {
  create(args: { data: Record<string, unknown> }): Promise<{ id: number }>;
  findUnique(args: { where: { id: number } }): Promise<({ id: number; lageId: number; version: number } & Record<string, unknown>) | null>;
  updateMany(args: { where: Record<string, unknown>; data: Record<string, unknown> }): Promise<{ count: number }>;
  delete(args: { where: { id: number } }): Promise<unknown>;
}

interface Kind {
  label: string;
  delegate: () => Delegate;
  spec: Spec;
  check?: (data: Record<string, unknown>, current: Record<string, unknown> | null, lageId: number) => Promise<void>;
}

const KINDS: Record<string, Kind> = {
  abschnitte: {
    label: 'Einsatzabschnitt',
    delegate: () => prisma.s2Abschnitt as unknown as Delegate,
    spec: {
      nr: { t: 'int', min: 1, max: 10, req: true },
      name: { t: 'str', max: 100, req: true },
      lage: { t: 'text' },
      schwerpunkt: { t: 'bool' },
      flaeche: { t: 'points', nullable: true },
    },
    check: async (d) => {
      if (Array.isArray(d.flaeche) && d.flaeche.length < 3) throw httpError(400, 'EA-Grenze braucht mindestens 3 Ecken');
    },
  },
  kraefte: {
    label: 'Kraft',
    delegate: () => prisma.s2Kraft as unknown as Delegate,
    spec: {
      org: { t: 'enum', values: ORGS, req: true },
      formation: { t: 'str', max: 100 },
      bezeichnung: { t: 'str', max: 100, req: true },
      staerke: { t: 'str', max: 20 },
      status: { t: 'enum', values: KRAFT_STATUS },
      abschnittId: { t: 'int', min: 1, max: 2 ** 31 - 1, nullable: true },
      bemerkung: { t: 'str', max: 255 },
    },
    check: async (d, _cur, lageId) => {
      if (typeof d.staerke === 'string' && staerkeGesamt(d.staerke) === null) {
        throw httpError(400, `Stärke "${d.staerke}" nicht lesbar – z. B. 1/8/9, 1/6 oder 12`);
      }
      if (typeof d.abschnittId === 'number') {
        const a = await prisma.s2Abschnitt.findUnique({ where: { id: d.abschnittId } });
        if (!a || a.lageId !== lageId) throw httpError(400, 'Einsatzabschnitt gehört nicht zu dieser Lage');
      }
    },
  },
  objekte: {
    label: 'Kartenobjekt',
    delegate: () => prisma.s2Objekt as unknown as Delegate,
    spec: {
      art: { t: 'enum', values: ['zeichen', 'flaeche', 'linie'], req: true },
      typ: { t: 'str', max: 30, req: true },
      punkte: { t: 'points' },
      text: { t: 'str', max: 100 },
      randkaestchen: { t: 'bool' },
    },
    check: async (d, cur) => {
      const art = (d.art ?? cur?.art) as string;
      const typ = (d.typ ?? cur?.typ) as string;
      const pts = (d.punkte ?? cur?.punkte) as unknown[] | undefined;
      if (!pts) throw httpError(400, 'Feld "punkte" fehlt');
      if (art === 'zeichen' && !ZEICHEN_IDS.has(typ)) throw httpError(400, `Taktisches Zeichen ${typ} unbekannt`);
      if (art === 'flaeche' && !AREA[typ]) throw httpError(400, `Flächentyp ${typ} unbekannt`);
      if (art === 'linie' && !LINE[typ]) throw httpError(400, `Linientyp ${typ} unbekannt`);
      const min = art === 'zeichen' ? 1 : art === 'flaeche' ? 3 : 2;
      if (pts.length < min || (art === 'zeichen' && pts.length !== 1)) {
        throw httpError(400, art === 'zeichen' ? 'Zeichen braucht genau einen Ort' : `mindestens ${min} Punkte nötig`);
      }
    },
  },
  meldungen: {
    label: 'Meldung',
    delegate: () => prisma.s2Meldung as unknown as Delegate,
    spec: {
      nr: { t: 'str', max: 20 },
      eingangAt: { t: 'date' },
      absender: { t: 'str', max: 100 },
      ort: { t: 'str', max: 200 },
      inhalt: { t: 'text', req: true },
      erledigt: { t: 'bool' },
    },
  },
};

function kindParam(req: Request): Kind {
  const k = KINDS[param(req, 'kind')];
  if (!k) throw httpError(404, 'Route nicht gefunden');
  return k;
}

function versionOf(body: Record<string, unknown>): number {
  const v = Number(body.version);
  if (!Number.isInteger(v)) throw httpError(400, 'Feld "version" fehlt (Stand, auf dem die Änderung beruht)');
  return v;
}

/** 409 with the current row, so the client can show who changed it and reload. */
function conflict(res: Response, label: string, current: Record<string, unknown>): void {
  const wer = current.updatedBy ? ` von ${String(current.updatedBy)}` : '';
  res.status(409).json({
    success: false,
    error: `${label} wurde zwischenzeitlich${wer} geändert. Bitte neu laden und die Änderung erneut eintragen.`,
    data: current,
  });
}

async function lageOr404(id: number) {
  const lage = await prisma.s2Lage.findUnique({ where: { id } });
  if (!lage) throw httpError(404, 'Lage nicht gefunden');
  return lage;
}

/** Only one EA may be the raumbezogener Schwerpunkt: setting it removes the flag elsewhere. */
async function einSchwerpunkt(lageId: number, keepId: number, user: string): Promise<void> {
  await prisma.s2Abschnitt.updateMany({
    where: { lageId, schwerpunkt: true, NOT: { id: keepId } },
    data: { schwerpunkt: false, version: { increment: 1 }, updatedBy: user },
  });
}

// ----------------------------------------------------------------------------
// Lagen
// ----------------------------------------------------------------------------

export async function getLagen(_req: Request, res: Response): Promise<void> {
  const lagen = await prisma.s2Lage.findMany({
    orderBy: [{ status: 'asc' }, { startedAt: 'desc' }],
    include: {
      _count: { select: { berichte: true, kraefte: true, meldungen: true } },
      berichte: { orderBy: { lfdNr: 'desc' }, take: 1, select: { lfdNr: true, zeit: true, status: true } },
    },
  });
  sendSuccess(res, lagen.map(({ berichte, ...l }) => ({ ...l, letzterBericht: berichte[0] ?? null })));
}

export async function createLage(req: Request, res: Response): Promise<void> {
  const data = parseBody(req.body, LAGE_SPEC, true);
  const lage = await prisma.s2Lage.create({
    data: { ...(data as Prisma.S2LageCreateInput), createdBy: userName(req), updatedBy: userName(req) },
  });
  sendSuccess(res, lage, 201);
}

/** Complete state of a Lage; polled every few seconds by every open client. */
export async function getLage(req: Request, res: Response): Promise<void> {
  const id = idParam(req);
  const lage = await prisma.s2Lage.findUnique({
    where: { id },
    include: {
      berichte: {
        orderBy: { lfdNr: 'asc' },
        select: { id: true, lfdNr: true, zeit: true, bearbeiter: true, status: true, karteAlsFoto: true, version: true, updatedBy: true, updatedAt: true },
      },
      abschnitte: { orderBy: { nr: 'asc' } },
      kraefte: { orderBy: [{ org: 'asc' }, { bezeichnung: 'asc' }] },
      objekte: { orderBy: { id: 'asc' } },
      meldungen: { orderBy: { eingangAt: 'desc' } },
    },
  });
  if (!lage) throw httpError(404, 'Lage nicht gefunden');
  sendSuccess(res, { ...lage, kraefteUebersicht: kraefteUebersicht(lage.kraefte) });
}

export async function updateLage(req: Request, res: Response): Promise<void> {
  const id = idParam(req);
  const version = versionOf(req.body);
  const data = parseBody(req.body, LAGE_SPEC, false);
  if (data.status === 'beendet') data.endedAt = new Date();
  if (data.status === 'aktiv') data.endedAt = null;
  const r = await prisma.s2Lage.updateMany({
    where: { id, version },
    data: { ...data, version: { increment: 1 }, updatedBy: userName(req) },
  });
  if (r.count === 0) return conflict(res, 'Die Lage', await lageOr404(id));
  sendSuccess(res, await prisma.s2Lage.findUnique({ where: { id } }));
}

export async function deleteLage(req: Request, res: Response): Promise<void> {
  const id = idParam(req);
  await lageOr404(id);
  await prisma.s2Lage.delete({ where: { id } });
  sendSuccess(res, { message: 'Lage gelöscht' });
}

// ----------------------------------------------------------------------------
// Lageberichte (stündliche Fortschreibung)
// ----------------------------------------------------------------------------

/**
 * Neuer Lagebericht: lfd. Nr. + 1, Zeit = nächste volle Stunde. Ab Nr. 2 werden alle Werte des
 * letzten Berichts übernommen (Fortschreibung: nur Änderungen einarbeiten).
 */
export async function createBericht(req: Request, res: Response): Promise<void> {
  const lageId = idParam(req);
  await lageOr404(lageId);
  const letzter = await prisma.s2Bericht.findFirst({ where: { lageId }, orderBy: { lfdNr: 'desc' } });
  try {
    const bericht = await prisma.s2Bericht.create({
      data: {
        lageId,
        lfdNr: (letzter?.lfdNr ?? 0) + 1,
        zeit: taktZeit(naechsteVolleStunde(new Date())),
        bearbeiter: letzter?.bearbeiter ?? (userName(req) ? `S2 ${userName(req)}` : null),
        karteAlsFoto: letzter?.karteAlsFoto ?? false,
        data: (letzter?.data ?? {}) as Prisma.InputJsonValue,
        createdBy: userName(req),
        updatedBy: userName(req),
      },
    });
    sendSuccess(res, bericht, 201);
  } catch (err) {
    if (err instanceof Prisma.PrismaClientKnownRequestError && err.code === 'P2002') {
      throw httpError(409, `Lagebericht Nr. ${(letzter?.lfdNr ?? 0) + 1} wurde gerade von jemand anderem angelegt. Bitte neu laden.`);
    }
    throw err;
  }
}

export async function getBericht(req: Request, res: Response): Promise<void> {
  const bericht = await prisma.s2Bericht.findUnique({ where: { id: idParam(req) } });
  if (!bericht) throw httpError(404, 'Lagebericht nicht gefunden');
  sendSuccess(res, bericht);
}

export async function updateBericht(req: Request, res: Response): Promise<void> {
  const id = idParam(req);
  const version = versionOf(req.body);
  const data = parseBody(req.body, BERICHT_SPEC, false);
  if (typeof data.zeit === 'string' && !isTaktZeit(data.zeit)) {
    throw httpError(400, `"${data.zeit}" ist keine taktische Zeit DDhhmmMMMJJ (z. B. 141400nov26)`);
  }
  if (data.zeit === null) throw httpError(400, 'Zeit darf nicht leer sein');
  const r = await prisma.s2Bericht.updateMany({
    where: { id, version },
    data: { ...data, version: { increment: 1 }, updatedBy: userName(req) } as Prisma.S2BerichtUpdateManyMutationInput,
  });
  const current = await prisma.s2Bericht.findUnique({ where: { id } });
  if (!current) throw httpError(404, 'Lagebericht nicht gefunden');
  if (r.count === 0) return conflict(res, `Lagebericht Nr. ${current.lfdNr}`, current);
  sendSuccess(res, current);
}

/** Only the latest draft can be deleted, so the numbering stays without gaps. */
export async function deleteBericht(req: Request, res: Response): Promise<void> {
  const bericht = await prisma.s2Bericht.findUnique({ where: { id: idParam(req) } });
  if (!bericht) throw httpError(404, 'Lagebericht nicht gefunden');
  const neuester = await prisma.s2Bericht.findFirst({ where: { lageId: bericht.lageId }, orderBy: { lfdNr: 'desc' } });
  if (neuester?.id !== bericht.id || bericht.status !== 'entwurf') {
    throw httpError(400, 'Nur der neueste Lagebericht im Entwurf kann gelöscht werden');
  }
  await prisma.s2Bericht.delete({ where: { id: bericht.id } });
  sendSuccess(res, { message: 'Lagebericht gelöscht' });
}

async function exportKontext(req: Request) {
  const bericht = await prisma.s2Bericht.findUnique({ where: { id: idParam(req) } });
  if (!bericht) throw httpError(404, 'Lagebericht nicht gefunden');
  const [lage, abschnitte, objekte, vorbericht] = await Promise.all([
    prisma.s2Lage.findUniqueOrThrow({ where: { id: bericht.lageId } }),
    prisma.s2Abschnitt.findMany({ where: { lageId: bericht.lageId } }),
    prisma.s2Objekt.findMany({ where: { lageId: bericht.lageId }, orderBy: { id: 'asc' } }),
    prisma.s2Bericht.findFirst({ where: { lageId: bericht.lageId, lfdNr: { lt: bericht.lfdNr } }, orderBy: { lfdNr: 'desc' } }),
  ]);
  return { bericht, lage, abschnitte, objekte, vorbericht };
}

/** Plausibilitätsprüfung und Mailtext für den Versand. */
export async function getPruefung(req: Request, res: Response): Promise<void> {
  const { bericht, lage, abschnitte, objekte, vorbericht } = await exportKontext(req);
  sendSuccess(res, {
    warnungen: pruefung(lage, bericht, abschnitte, objekte, vorbericht),
    mail: mail(lage, bericht, abschnitte),
  });
}

function sendFile(res: Response, buffer: Buffer, name: string, type: string): void {
  const ascii = name.replace(/[^\x20-\x7e]/g, '_').replace(/"/g, '');
  res.setHeader('Content-Type', type);
  res.setHeader('Content-Disposition', `attachment; filename="${ascii}"; filename*=UTF-8''${encodeURIComponent(name)}`);
  res.send(buffer);
}

function exportFehler(err: unknown): Error {
  // Builder errors (unknown list value, coordinate outside the map, ...) are input problems
  return httpError(400, `Export nicht möglich: ${(err as Error).message}`);
}

export async function exportLagebericht(req: Request, res: Response): Promise<void> {
  const { bericht, lage, abschnitte } = await exportKontext(req);
  let buffer: Buffer;
  try {
    buffer = erzeugeLagebericht(berichtDaten(lage, bericht, abschnitte)).buffer;
  } catch (err) {
    throw exportFehler(err);
  }
  sendFile(res, buffer, dateiname(lage, bericht, 'Lagebericht', 'xlsx'),
    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
}

export async function exportLagekarte(req: Request, res: Response): Promise<void> {
  const format = param(req, 'format');
  if (format !== 'pptx' && format !== 'pdf') throw httpError(404, 'Route nicht gefunden');
  const { bericht, lage, abschnitte, objekte } = await exportKontext(req);
  let buffer: Buffer;
  try {
    buffer = erzeugeLagekarte(lageKarte(bericht, abschnitte, objekte), { nurFolie1: format === 'pdf' }).buffer;
  } catch (err) {
    throw exportFehler(err);
  }
  if (format === 'pptx') {
    sendFile(res, buffer, dateiname(lage, bericht, 'Lagekarte', 'pptx'),
      'application/vnd.openxmlformats-officedocument.presentationml.presentation');
    return;
  }
  const dir = path.join(categoryUploadDir('temp'), `s2-${process.pid}-${Date.now()}-${Math.random().toString(36).slice(2)}`);
  fs.mkdirSync(dir, { recursive: true });
  try {
    const pptx = path.join(dir, 'lagekarte.pptx');
    fs.writeFileSync(pptx, buffer);
    const pdf = await convertToPdf(pptx);
    sendFile(res, fs.readFileSync(pdf), dateiname(lage, bericht, 'Lagekarte', 'pdf'), 'application/pdf');
  } finally {
    fs.rmSync(dir, { recursive: true, force: true });
  }
}

// ----------------------------------------------------------------------------
// Einsatzabschnitte, Kräfte, Kartenobjekte, Meldungen (generic, versioned)
// ----------------------------------------------------------------------------

export async function createItem(req: Request, res: Response): Promise<void> {
  const kind = kindParam(req);
  const lageId = idParam(req);
  await lageOr404(lageId);
  const data = parseBody(req.body, kind.spec, true);
  await kind.check?.(data, null, lageId);
  const item = await kind.delegate().create({ data: { ...data, lageId, updatedBy: userName(req) } });
  if (kind === KINDS.abschnitte && data.schwerpunkt) await einSchwerpunkt(lageId, item.id, userName(req));
  sendSuccess(res, await kind.delegate().findUnique({ where: { id: item.id } }), 201);
}

export async function updateItem(req: Request, res: Response): Promise<void> {
  const kind = kindParam(req);
  const id = idParam(req);
  const version = versionOf(req.body);
  const current = await kind.delegate().findUnique({ where: { id } });
  if (!current) throw httpError(404, `${kind.label} nicht gefunden`);
  const data = parseBody(req.body, kind.spec, false);
  await kind.check?.(data, current, current.lageId);
  const r = await kind.delegate().updateMany({
    where: { id, version },
    data: { ...data, version: { increment: 1 }, updatedBy: userName(req) },
  });
  if (r.count === 0) return conflict(res, kind.label, (await kind.delegate().findUnique({ where: { id } }))!);
  if (kind === KINDS.abschnitte && data.schwerpunkt) await einSchwerpunkt(current.lageId, id, userName(req));
  sendSuccess(res, await kind.delegate().findUnique({ where: { id } }));
}

export async function deleteItem(req: Request, res: Response): Promise<void> {
  const kind = kindParam(req);
  const id = idParam(req);
  if (!(await kind.delegate().findUnique({ where: { id } }))) throw httpError(404, `${kind.label} nicht gefunden`);
  await kind.delegate().delete({ where: { id } });
  sendSuccess(res, { message: `${kind.label} gelöscht` });
}

/** Adresse -> UTM aus dem Adressverzeichnis Heusweiler (offline, OSM). */
export async function geoLookup(req: Request, res: Response): Promise<void> {
  const q = String(req.query.q ?? '').trim();
  if (!q) { sendError(res, 'Adresse fehlt', 400); return; }
  const ortsteil = String(req.query.ortsteil ?? '').trim() || null;
  sendSuccess(res, finde(q.slice(0, 200), ortsteil));
}
