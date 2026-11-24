/**
 * Builds the export inputs (bericht.json / lage.json of the S2 skill) from the database rows,
 * plus the plausibility check and the mail text (vorlagen/Mail_Vorlage.md of the skill).
 */
import { BerichtDaten, plausi } from './lagebericht';
import { LageKarte, imKartenausschnitt } from './lagekarte';
import { MONATE, mittelpunkt } from './common';

type Pt = [number, number];

export interface LageRow { name: string; kommune: string; isExercise: boolean; recipient: string; contact: string | null }
export interface BerichtRow { lfdNr: number; zeit: string; bearbeiter: string | null; karteAlsFoto: boolean; data: unknown }
export interface AbschnittRow { nr: number; name: string; schwerpunkt: boolean; flaeche: unknown }
export interface ObjektRow { art: string; typ: string; punkte: unknown; text: string | null; randkaestchen: boolean }

function punkte(v: unknown): Pt[] {
  if (!Array.isArray(v)) return [];
  return v.filter((p): p is Pt => Array.isArray(p) && p.length === 2 && p.every((x) => typeof x === 'number'));
}

/** "Gemeinde Heusweiler" -> "Heusweiler" for file names. */
export function ortsname(kommune: string): string {
  return kommune.replace(/^(Gemeinde|Stadt|Gemeindeverband)\s+/i, '').replace(/[^\p{L}\p{N}-]+/gu, '_') || 'Kommune';
}

export function dateiname(lage: LageRow, bericht: BerichtRow, art: 'Lagebericht' | 'Lagekarte', ext: string): string {
  const folie = art === 'Lagekarte' && ext === 'pdf' ? '_Folie1' : '';
  return `${art}_${ortsname(lage.kommune)}_${bericht.zeit}_V${bericht.lfdNr}${folie}.${ext}`;
}

/** Report data as stored, with head fields and the Einsatzabschnitte (raumbezogen) from the Lage. */
export function berichtDaten(lage: LageRow, bericht: BerichtRow, abschnitte: AbschnittRow[]): BerichtDaten {
  const data = (bericht.data && typeof bericht.data === 'object' ? bericht.data : {}) as Partial<BerichtDaten>;
  return {
    ...data,
    kommune: lage.kommune,
    zeit: bericht.zeit,
    lfd_nr: bericht.lfdNr,
    // EA im Bericht = EA der Lagekarte, Nummern identisch (Plausibilität S2-Skill)
    raeume: [...abschnitte].sort((a, b) => a.nr - b.nr).map((a) => ({ nr: a.nr, text: a.name, schwerpunkt: a.schwerpunkt })),
  };
}

export function lageKarte(bericht: BerichtRow, abschnitte: AbschnittRow[], objekte: ObjektRow[]): LageKarte {
  const karte: Required<Pick<LageKarte, 'flaechen' | 'linien' | 'symbole' | 'kaestchen'>> & LageKarte = {
    bearbeiter: bericht.bearbeiter ?? '', takt_zeit: bericht.zeit, flaechen: [], linien: [], symbole: [], kaestchen: [],
  };
  for (const a of [...abschnitte].sort((x, y) => x.nr - y.nr)) {
    const pts = punkte(a.flaeche);
    if (pts.length >= 3) {
      karte.flaechen.push({ typ: 'ea_grenze', text: `EA ${a.nr}: ${a.name}`, utm: pts });
      // EA-Nummer als Kästchen mit Pfeil auf die Mitte der Fläche
      karte.kaestchen.push({ id: `EA_${String(a.nr).padStart(2, '0')}`, utm: mittelpunkt(pts) });
    }
  }
  for (const o of objekte) {
    const pts = punkte(o.punkte);
    const text = o.text?.trim() || undefined;
    if (o.art === 'flaeche') karte.flaechen.push({ typ: o.typ, utm: pts, text });
    else if (o.art === 'linie') karte.linien.push({ typ: o.typ, utm: pts });
    else if (o.art === 'zeichen' && pts[0]) {
      if (o.randkaestchen) karte.kaestchen.push({ id: o.typ, utm: pts[0], text });
      else karte.symbole.push({ id: o.typ, utm: pts[0], text });
    }
  }
  return karte;
}

/**
 * Plausibilitätsprüfung vor dem Versand (Abschnitt 7 des S2-Skills). Hinweise, keine Sperre:
 * fehlende Angaben werden nie erfunden, sondern bleiben leer.
 */
export function pruefung(
  lage: LageRow, bericht: BerichtRow, abschnitte: AbschnittRow[], objekte: ObjektRow[], vorbericht: BerichtRow | null,
): string[] {
  const b = berichtDaten(lage, bericht, abschnitte);
  const warn = plausi(b);
  if (!bericht.bearbeiter?.trim()) warn.push('Bearbeiter fehlt');
  if (!b.kurzfassung?.trim()) warn.push('Kurzfassung der Lage fehlt');
  if (!b.beurteilung?.schadenslage?.stufe) warn.push('Lagebeurteilung Schadenslage fehlt');
  if (!b.staerke?.gesamt) warn.push('Stärke gesamt fehlt');
  const sw = Object.values(b.aufgaben ?? {}).filter((e) => e?.schwerpunkt).length;
  if (sw === 0 && Object.keys(b.aufgaben ?? {}).length > 0) warn.push('kein aufgabenbezogener Schwerpunkt markiert');
  if (abschnitte.length > 0 && !abschnitte.some((a) => a.schwerpunkt)) warn.push('kein raumbezogener Schwerpunkt (EA) markiert');
  for (const a of abschnitte) {
    if (punkte(a.flaeche).length < 3) warn.push(`EA ${a.nr} hat keine Grenze auf der Lagekarte`);
  }
  const zeichen = objekte.filter((o) => o.art === 'zeichen');
  const kaestchen = zeichen.filter((o) => o.randkaestchen).length + abschnitte.filter((a) => punkte(a.flaeche).length >= 3).length;
  if (kaestchen > 12) warn.push(`${kaestchen} Randkästchen: Pfeile kreuzen sich, besser zusammenfassen (max. ~12)`);
  for (const o of objekte) {
    if (punkte(o.punkte).some(([e, n]) => !imKartenausschnitt(e, n))) {
      warn.push(`${o.text || o.typ} liegt außerhalb des Kartenausschnitts`);
    }
  }
  if (vorbericht) {
    const vb = (vorbericht.data ?? {}) as Partial<BerichtDaten>;
    const tot = (d: Partial<BerichtDaten>) => {
      const v = d.schaden?.verstorben;
      return Array.isArray(v) ? Number(v[0] ?? 0) + Number(v[1] ?? 0) : Number(v ?? 0);
    };
    if (tot(b) < tot(vb)) warn.push(`Verstorbene gesunken (${tot(vb)} → ${tot(b)}): Gesamtzahl kann nicht zurückgehen`);
    const s0 = vb.staerke?.gesamt;
    const s1 = b.staerke?.gesamt;
    if (s0 && s1 && Math.abs(s1 - s0) / s0 > 0.5) warn.push(`Stärke springt gegenüber Nr. ${vorbericht.lfdNr} (${s0} → ${s1})`);
  }
  return warn;
}

/** Taktische Zeit der nächsten vollen Stunde nach `zeit` (lokale Zeit, wie erfasst). */
export function naechsterBericht(zeit: string): string {
  const m = /^(\d{2})(\d{2})(\d{2})([A-Za-z]{3})(\d{2})$/.exec(zeit);
  if (!m) return '';
  const upper = m[4] === m[4].toUpperCase();
  const d = new Date(Date.UTC(2000 + Number(m[5]), MONATE.indexOf(m[4].toLowerCase()), Number(m[1]), Number(m[2]) + 1, 0));
  const mon = MONATE[d.getUTCMonth()];
  const two = (n: number) => String(n).padStart(2, '0');
  return `${two(d.getUTCDate())}${two(d.getUTCHours())}00${upper ? mon.toUpperCase() : mon}${two(d.getUTCFullYear() % 100)}`;
}

const STUFE = ['', 'geringfügig', 'erheblich', 'kritisch'];

/** Mail text from Mail_Vorlage.md; [ÜBUNG] lines only for exercises. Never sent by the app. */
export function mail(lage: LageRow, bericht: BerichtRow, abschnitte: AbschnittRow[]): { an: string; betreff: string; text: string; anhaenge: string[] } {
  const b = berichtDaten(lage, bericht, abschnitte);
  const uebung = lage.isExercise;
  const foto = bericht.karteAlsFoto;
  const anhaenge = [
    dateiname(lage, bericht, 'Lagebericht', 'xlsx'),
    foto ? 'Foto der analog geführten Lagekarte' : dateiname(lage, bericht, 'Lagekarte', 'pdf'),
  ];
  const kurz = (b.kurzfassung ?? '').split('\n').map((z) => z.trim()).filter(Boolean);
  const allgemein = kurz.find((z) => /^allgemein:/i.test(z))?.replace(/^allgemein:\s*/i, '') ?? kurz[0] ?? '';
  const sp = abschnitte.find((a) => a.schwerpunkt);
  const spZeile = sp ? kurz.find((z) => z.toLowerCase().startsWith(`ea ${sp.name}`.toLowerCase()) || z.toLowerCase().startsWith(`ea ${sp.nr}`)) : undefined;
  const lageKurz = [allgemein, spZeile].filter(Boolean).join(' ') || '–';
  const sl = b.beurteilung?.schadenslage;
  const probleme = (b.probleme ?? '').split('\n').map((z) => z.replace(/^\s*(\d+[.)]|[-*•])\s*/, '').trim()).filter(Boolean);
  const kommune = lage.kommune;
  const z: (string | null)[] = [
    uebung ? '+++ ÜBUNG – ÜBUNG – ÜBUNG +++' : null,
    uebung ? '' : null,
    'Sehr geehrte Damen und Herren,',
    '',
    `anbei der Lagebericht Nr. ${bericht.lfdNr} der TEL ${kommune}, Stand ${bericht.zeit}, mit Lagekarte${foto ? ' (als Foto der analog geführten Karte)' : ''}.`,
    '',
    `Lage in Kürze: ${lageKurz}`,
    '',
    `Lagebeurteilung Schadenslage: ${sl?.stufe ? STUFE[sl.stufe] : '–'} – Prognose: ${sl?.prognose ?? '–'}`,
    '',
    'Dringende Probleme / Bedarfe:',
    ...(probleme.length > 0 ? probleme.slice(0, 3).map((p, i) => `${i + 1}. ${p}`) : ['keine']),
    '',
    'Zeitkritische Nachforderungen wurden zusätzlich über Funk abgesetzt.',
    '',
    `Nächster Lagebericht: ${naechsterBericht(bericht.zeit)}`,
    '',
    'Mit freundlichen Grüßen',
    `TEL ${kommune} – S2`,
    bericht.bearbeiter ?? '',
    lage.contact ? `Erreichbar: ${lage.contact}` : null,
    uebung ? '' : null,
    uebung ? '+++ ÜBUNG – ÜBUNG – ÜBUNG +++' : null,
  ];
  return {
    an: lage.recipient,
    betreff: `${uebung ? 'ÜBUNG – ÜBUNG – ÜBUNG | ' : ''}Lagebericht ${kommune} Nr. ${bericht.lfdNr} – ${bericht.zeit}`,
    text: z.filter((x): x is string => x !== null).join('\n'),
    anhaenge,
  };
}
