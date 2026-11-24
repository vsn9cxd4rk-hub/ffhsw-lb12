/**
 * Adresse in der Gemeinde Heusweiler -> UTM 32N, offline. Port of the S2 skill's geo_lookup.py.
 * Daten: OpenStreetMap © OSM-Mitwirkende (ODbL), Abruf 15.11.2026, assets/s2/geo/.
 *   strassen_heusweiler.csv      ortsteil;strasse;utm_e;utm_n;e_min;e_max;n_min;n_max;anz_adressen
 *   hnr_bereiche_heusweiler.csv  ortsteil;strasse;von-bis:E,N …  (E/N in 10 m, N minus 546000)
 * Der Ortsteil ist aus dem nächstgelegenen Ortsmittelpunkt abgeleitet (OSM führt in Heusweiler
 * keinen Ortsteil in der Adresse) und kann an Ortsteilgrenzen abweichen.
 */
import fs from 'fs';
import path from 'path';
import { S2_ASSETS } from './common';

export type Genauigkeit = 'HAUSNR' | 'NAECHSTE_HNR' | 'STRASSENMITTE' | 'NICHT_GEFUNDEN';

export interface GeoTreffer {
  ortsteil: string;
  strasse: string;
  hausnr: string;
  utm: [number, number] | null;
  genauigkeit: Genauigkeit;
  hinweis?: string; // e.g. "nächste Hausnummer 12-14", "mehrdeutig – Ortsteil angeben"
  vorschlaege?: string[];
}

interface Strasse { ortsteil: string; strasse: string; e: number; n: number }
type Bereich = [von: number, bis: number, e: number, n: number];

let data: { S: Strasse[]; H: Map<string, Bereich[]>; NN: Map<string, string> } | null = null;

export function norm(s: string): string {
  let t = s.toLowerCase().trim().replace(/ß/g, 'ss');
  t = t.replace(/str\.?(?=\s|$)/g, 'strasse');
  t = t.replace(/\bstr\.?\b/g, 'strasse');
  return t.replace(/[^a-z0-9äöü]/g, '');
}

function load() {
  if (data) return data;
  const dir = path.join(S2_ASSETS, 'geo');
  const lines = fs.readFileSync(path.join(dir, 'strassen_heusweiler.csv'), 'utf8').split(/\r?\n/).filter(Boolean);
  const head = lines[0].split(';');
  const S = lines.slice(1).map((l) => {
    const r = Object.fromEntries(l.split(';').map((v, i) => [head[i], v]));
    return { ortsteil: r.ortsteil, strasse: r.strasse, e: Number(r.utm_e), n: Number(r.utm_n) };
  });
  const H = new Map<string, Bereich[]>();
  for (const z of fs.readFileSync(path.join(dir, 'hnr_bereiche_heusweiler.csv'), 'utf8').split(/\r?\n/)) {
    if (z.startsWith('#') || !z.trim()) continue;
    const [ot, st, ...restParts] = z.split(';');
    const L: Bereich[] = [];
    for (const t of restParts.join(';').split(/\s+/).filter(Boolean)) {
      const [lab, xy] = t.split(':');
      const [e, n] = xy.split(',');
      const [v, b] = lab.split('-');
      L.push([Number(v), Number(b || v), Number(e) * 10, (Number(n) + 546000) * 10]);
    }
    H.set(`${ot}|${st}`, L);
  }
  const NN = new Map(S.map((s) => [norm(s.strasse), s.strasse]));
  data = { S, H, NN };
  return data;
}

/** Ratcliff/Obershelp similarity like Python's difflib.SequenceMatcher.ratio(). */
function ratio(a: string, b: string): number {
  const matches = (s1: string, s2: string): number => {
    if (!s1 || !s2) return 0;
    let best = 0;
    let i1 = 0;
    let i2 = 0;
    for (let i = 0; i < s1.length; i++) {
      for (let j = 0; j < s2.length; j++) {
        let k = 0;
        while (i + k < s1.length && j + k < s2.length && s1[i + k] === s2[j + k]) k++;
        if (k > best) { best = k; i1 = i; i2 = j; }
      }
    }
    if (best === 0) return 0;
    return best + matches(s1.slice(0, i1), s2.slice(0, i2)) + matches(s1.slice(i1 + best), s2.slice(i2 + best));
  };
  return a.length + b.length === 0 ? 1 : (2 * matches(a, b)) / (a.length + b.length);
}

function closeMatches(q: string, keys: string[], n = 4, cutoff = 0.75): string[] {
  return keys.map((k) => [k, ratio(q, k)] as const).filter(([, r]) => r >= cutoff)
    // like heapq.nlargest over (score, key): ties go to the larger key
    .sort((x, y) => y[1] - x[1] || (y[0] > x[0] ? 1 : y[0] < x[0] ? -1 : 0)).slice(0, n).map(([k]) => k);
}

/** Looks up "Straße Nr" (optionally restricted to an Ortsteil). Several results = ambiguous street. */
export function finde(text: string, ortsteil?: string | null): GeoTreffer[] {
  const { S, H, NN } = load();
  const m = /^(.*?)[\s,]+(\d+)\s*([a-zA-Z]?)\s*$/.exec(text.trim());
  const strasse = m ? m[1] : text.trim();
  const hn = m ? Number(m[2]) : null;
  const hnTxt = m ? m[2] + m[3] : '';
  const q = norm(strasse);
  let name = NN.get(q);
  if (!name && q) {
    const starts = [...NN].filter(([k]) => k.startsWith(q) || q.startsWith(k)).map(([, v]) => v);
    name = starts.length === 1 ? starts[0] : undefined;
  }
  if (!name) {
    const vor = closeMatches(q, [...NN.keys()]);
    if (vor.length === 1) name = NN.get(vor[0]);
    else {
      return [{ ortsteil: ortsteil ?? '', strasse, hausnr: hnTxt, utm: null, genauigkeit: 'NICHT_GEFUNDEN', vorschlaege: vor.map((v) => NN.get(v)!) }];
    }
  }
  let ss = S.filter((s) => s.strasse === name);
  if (ortsteil) {
    const filtered = ss.filter((s) => s.ortsteil.toLowerCase().startsWith(ortsteil.toLowerCase()));
    if (filtered.length > 0) ss = filtered;
  }
  const erg: GeoTreffer[] = ss.map((s) => {
    const L = H.get(`${s.ortsteil}|${name}`) ?? [];
    if (hn !== null && L.length > 0) {
      const treffer = L.find((r) => r[0] <= hn && hn <= r[1] && r[0] % 2 === hn % 2);
      if (treffer) return { ortsteil: s.ortsteil, strasse: name!, hausnr: hnTxt, utm: [treffer[2], treffer[3]], genauigkeit: 'HAUSNR' };
      const dist = (r: Bereich) => Math.min(Math.abs(hn - r[0]), Math.abs(hn - r[1]));
      const r = [...L].sort((a, b) => dist(a) - dist(b) || Number(a[0] % 2 !== hn % 2) - Number(b[0] % 2 !== hn % 2))[0];
      return {
        ortsteil: s.ortsteil, strasse: name!, hausnr: hnTxt, utm: [r[2], r[3]], genauigkeit: 'NAECHSTE_HNR',
        hinweis: `nächste Hausnummer ${r[0]}${r[1] !== r[0] ? `-${r[1]}` : ''}`,
      };
    }
    return { ortsteil: s.ortsteil, strasse: name!, hausnr: hnTxt, utm: [s.e, s.n], genauigkeit: 'STRASSENMITTE' };
  });
  if (erg.length > 1) for (const e of erg) e.hinweis = [e.hinweis, 'mehrdeutig – Ortsteil angeben'].filter(Boolean).join(', ');
  return erg;
}
