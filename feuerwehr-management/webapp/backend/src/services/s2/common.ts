import path from 'path';

/** Templates, taktische Zeichen and address data of the S2 skill (RV Saarbrücken, Gemeinde Heusweiler). */
export const S2_ASSETS = path.resolve(__dirname, '../../../assets/s2');

// Taktische Zeit DDhhmmMMMJJ, month in American spelling (Unterlegekarton zum Nachrichtenvordruck)
export const MONATE = ['jan', 'feb', 'mar', 'apr', 'may', 'jun', 'jul', 'aug', 'sep', 'oct', 'nov', 'dec'];
const TZ_RE = /^(\d{2})(\d{2})(\d{2})([A-Za-z]{3})(\d{2})$/;

export function isTaktZeit(value: string): boolean {
  const m = TZ_RE.exec(value);
  if (!m) return false;
  const day = Number(m[1]);
  return MONATE.includes(m[4].toLowerCase()) && day >= 1 && day <= 31
    && Number(m[2]) <= 23 && Number(m[3]) <= 59;
}

export function checkTaktZeit(value: unknown, feld: string): string {
  const v = String(value ?? '');
  if (!isTaktZeit(v)) {
    throw new Error(`${feld}: "${v}" ist keine taktische Zeit DDhhmmMMMJJ mit amerikanischem Monat (z. B. 091400nov26)`);
  }
  return v;
}

/** Local time of the TEL, independent of the server's time zone. */
function berlinParts(date: Date): Record<string, string> {
  const parts = new Intl.DateTimeFormat('de-DE', {
    timeZone: 'Europe/Berlin', year: '2-digit', month: 'numeric', day: '2-digit',
    hour: '2-digit', minute: '2-digit', hourCycle: 'h23',
  }).formatToParts(date);
  return Object.fromEntries(parts.map((p) => [p.type, p.value]));
}

export function taktZeit(date: Date): string {
  const p = berlinParts(date);
  return `${p.day}${p.hour}${p.minute}${MONATE[Number(p.month) - 1]}${p.year}`;
}

/** Reports go out at the full hour: the next full hour after `date`. */
export function naechsteVolleStunde(date: Date): Date {
  const d = new Date(date);
  d.setMinutes(0, 0, 0);
  d.setHours(d.getHours() + 1);
  return d;
}

/**
 * Personenzahl aus der Stärkeangabe. Erfasst wird wie gewohnt, z. B. 1/8/9 (Führer/Mannschaft/
 * Gesamt), 0/1/2/6/9, 1/6 (= 7) oder 2/5/24 (= 31). Ist die letzte Zahl die Summe der übrigen,
 * ist sie die Gesamtstärke, sonst werden alle Zahlen addiert. null bei leerer/ungültiger Angabe.
 */
export function staerkeGesamt(value: string | null | undefined): number | null {
  const text = (value ?? '').trim();
  if (!text) return null;
  if (!/^\d+(\s*\/\s*\d+)*$/.test(text)) return null;
  const n = text.split('/').map((x) => Number(x.trim()));
  if (n.length === 1) return n[0];
  const last = n[n.length - 1];
  const rest = n.slice(0, -1).reduce((a, b) => a + b, 0);
  return n.length >= 3 && last === rest ? last : rest + last;
}

// ----------------------------------------------------------------------------
// Kräfte → Lagebericht (Felder B5-B25, E31, Einheitenliste A83 ff.)
// ----------------------------------------------------------------------------

export const KRAFT_STATUS = ['alarmiert', 'bereitschaft', 'grundschutz', 'einsatz', 'ruhe', 'entlassen'] as const;
export type KraftStatus = (typeof KRAFT_STATUS)[number];

export interface KraftRow {
  org: string;
  formation: string | null;
  bezeichnung: string;
  staerke: string | null;
  status: string;
}

export interface KraefteUebersicht {
  kraefte: { alarmiert: string; bereitschaft: string; grundschutz: string; einsatz: string; ruhe: string };
  staerkeGesamt: number;
  ohneStaerke: string[]; // units whose strength could not be read
  einheiten: { org: string; formation: string; bezeichnung: string }[];
}

function liste(rows: KraftRow[]): string {
  if (rows.length === 0) return './.';
  const byOrg = new Map<string, string[]>();
  for (const r of rows) byOrg.set(r.org, [...(byOrg.get(r.org) ?? []), r.bezeichnung]);
  return [...byOrg].map(([org, names]) => `${org}: ${names.join(', ')}`).join('\n');
}

/**
 * Summary as the report expects it. "davon Grundschutz" (B13) is a part of "Bereitschaft" (B8),
 * so units held back for Grundschutz appear in both lines. Released units (entlassen) count nowhere.
 */
export function kraefteUebersicht(rows: KraftRow[]): KraefteUebersicht {
  const active = rows.filter((r) => r.status !== 'entlassen');
  const of = (...s: string[]) => active.filter((r) => s.includes(r.status));
  const ohneStaerke: string[] = [];
  let summe = 0;
  for (const r of active) {
    const n = staerkeGesamt(r.staerke);
    if (n === null) ohneStaerke.push(r.bezeichnung);
    else summe += n;
  }
  return {
    kraefte: {
      alarmiert: liste(of('alarmiert')),
      bereitschaft: liste(of('bereitschaft', 'grundschutz')),
      grundschutz: liste(of('grundschutz')),
      einsatz: liste(of('einsatz')),
      ruhe: liste(of('ruhe')),
    },
    staerkeGesamt: summe,
    ohneStaerke,
    einheiten: active.filter((r) => r.formation).map((r) => ({ org: r.org, formation: r.formation!, bezeichnung: r.bezeichnung })),
  };
}

/** Mittelpunkt (Schwerpunkt der Eckpunkte) einer Fläche, für den Pfeil des EA-Kästchens. */
export function mittelpunkt(punkte: [number, number][]): [number, number] {
  const e = punkte.reduce((a, p) => a + p[0], 0) / punkte.length;
  const n = punkte.reduce((a, p) => a + p[1], 0) / punkte.length;
  return [Math.round(e), Math.round(n)];
}
