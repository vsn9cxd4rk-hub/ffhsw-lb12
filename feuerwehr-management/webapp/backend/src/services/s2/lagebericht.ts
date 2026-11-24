/**
 * Lagebericht (Vorlage "Lagebericht Kommune - DDhhmmMMMJJ V1_02", RV Saarbrücken) aus den
 * Berichtsdaten befüllen. TypeScript port of the S2 skill's lagebericht_fill.py.
 *
 * Works directly on the sheet XML: the template has x14 drop-downs, conditional formatting,
 * comments and WMF symbols that a spreadsheet library (exceljs/openpyxl) drops on save.
 */
import fs from 'fs';
import path from 'path';
import PizZip from 'pizzip';
import { S2_ASSETS, checkTaktZeit } from './common';

const SHEET = 'xl/worksheets/sheet1.xml';
export const LAGEBERICHT_VORLAGE = path.join(S2_ASSETS, 'lagebericht-vorlage.xltx');

export const SCHADEN_ROWS: Record<string, number> = {
  verletzt: 5, verschuettet: 6, vermisst: 7, verstorben: 8, betreuung: 9,
  tiere_betroffen: 10, tiere_verstorben: 11, gebaeude_beschaedigt: 12,
  gebaeude_teilzerstoert: 13, gebaeude_zerstoert: 14,
};
const KRAEFTE: Record<string, string> = { alarmiert: 'B5', bereitschaft: 'B8', grundschutz: 'B13', einsatz: 'B15', ruhe: 'B25' };
// Festlegung Gemeinde 13.11.2026: nur Gesamtzahl (E31); Teilstärken E33-E36 bleiben leer
const STAERKE: Record<string, string> = { gesamt: 'E31' };
export const BEURT_ROWS: Record<string, number> = { schadenslage: 39, fuehrung: 40, kraefte: 41, logistik: 42, kommunikation: 43 };
const BEURT_COL: Record<number, string> = { 1: 'E', 2: 'K', 3: 'R' };
export const AUFGABEN = ['menschenrettung', 'evakuierung', 'brandschutz', 'technische_hilfe', 'cbrn', 'sanitaet',
  'betreuung', 'wasserrettung', 'psnv', 'sonstige']; // Zeilen 47..56
const FREITEXT: Record<string, string> = {
  probleme: 'A60', kurzfassung: 'A66', massnahmen: 'A74', sperrungen: 'A103', presse: 'A107', bemerkungen: 'A111',
};
export const PROGNOSEN = ['Entspannung', 'Stagnation', 'Verschärfung'];
export const KRITIS_SEKTOREN = ['Wasser', 'Energie', 'Ernährung', 'Finanz-& Versicherungswesen', 'Gesundheit',
  'IT & Telekommunikation', 'Siedlungsabfallentsorgung', 'Medien & Kultur', 'Staat & Verwaltung', 'Transport & Verkehr'];
export const ORGS = ['FW', 'RD/eRD', 'THW', 'ASB', 'DLRG', 'DRK', 'JUH', 'MHD', 'NKS', 'BW'];

export interface Beurteilung { stufe?: number | null; prognose?: string | null }
export interface Aufgabe { text?: string; schwerpunkt?: boolean; bezeichnung?: string }

/** Report content, same structure as bericht.json of the S2 skill. */
export interface BerichtDaten {
  kommune?: string;
  zeit: string;
  lfd_nr: number;
  kraefte?: Partial<Record<'alarmiert' | 'bereitschaft' | 'grundschutz' | 'einsatz' | 'ruhe', string>>;
  schaden?: Record<string, [number, number] | number>; // [unbestätigt ?, bestätigt !]
  umwelt?: string;
  kritis_bedroht?: { sektor: string; text?: string }[];
  kritis_ausgefallen?: { sektor: string; text?: string }[];
  staerke?: { gesamt?: number | null; [teil: string]: number | null | undefined };
  verpflegung?: { portionen?: number | null; zeitpunkt?: string | null };
  beurteilung?: Record<string, Beurteilung>;
  aufgaben?: Record<string, Aufgabe | undefined>;
  raeume?: { nr: number; text: string; schwerpunkt?: boolean }[];
  probleme?: string;
  kurzfassung?: string;
  massnahmen?: string;
  sperrungen?: string;
  presse?: string;
  bemerkungen?: string;
  einheiten?: { org: string; formation: string; bezeichnung?: string }[];
}

type CellValue = string | number | { formulaResult: number };

/** Plausibility warnings of the original script ("PRÜFEN:"), reported to the user. */
export function plausi(b: BerichtDaten): string[] {
  const warn: string[] = [];
  const st = b.staerke ?? {};
  if (['fw', 'kats_hiorg', 'thw', 'sonstige'].some((k) => st[k])) {
    warn.push('Stärke: nur Gesamtzahl wird eingetragen, Teilstärken (fw/kats_hiorg/thw/sonstige) ignoriert');
  }
  if (st.gesamt != null && !Number.isInteger(st.gesamt)) warn.push(`Stärke gesamt ist keine Ganzzahl: ${st.gesamt}`);
  for (const [k, v] of Object.entries(b.schaden ?? {})) {
    if (Array.isArray(v) && v.some((x) => x < 0)) warn.push(`Schaden ${k}: negative Zahl`);
  }
  const sw = Object.entries(b.aufgaben ?? {}).filter(([, e]) => e?.schwerpunkt).map(([k]) => k);
  if (sw.length > 1) warn.push(`mehr als ein aufgabenbezogener Schwerpunkt: ${sw.join(', ')}`);
  const sr = (b.raeume ?? []).filter((e) => e.schwerpunkt).map((e) => e.nr);
  if (sr.length > 1) warn.push(`mehr als ein raumbezogener Schwerpunkt: EA ${sr.join(', ')}`);
  const krit = Object.entries(b.beurteilung ?? {})
    .filter(([, e]) => e && (e.stufe === 3 || e.prognose === 'Verschärfung')).map(([k]) => k);
  if (krit.length > 0 && !b.probleme?.trim()) {
    warn.push(`Beurteilung kritisch/Verschärfung (${krit.join(', ')}) aber keine Probleme/Bedarfe`);
  }
  return warn;
}

/** Cell values and red (Schwerpunkt) cells for the report. Throws on invalid list values. */
export function buildCells(b: BerichtDaten): { vals: Map<string, CellValue>; red: Set<string> } {
  const vals = new Map<string, CellValue>();
  const red = new Set<string>();
  const put = (ref: string, v: CellValue) => vals.set(ref, v);

  put('D2', b.kommune || 'Gemeinde Heusweiler');
  put('R2', checkTaktZeit(b.zeit, 'zeit'));
  put('AA2', b.lfd_nr);
  for (const [k, ref] of Object.entries(KRAEFTE)) {
    const v = b.kraefte?.[k as keyof typeof b.kraefte];
    if (v) put(ref, v);
  }
  for (const [k, row] of Object.entries(SCHADEN_ROWS)) {
    const v = b.schaden?.[k];
    if (v == null) continue;
    const [q, s] = Array.isArray(v) ? [Number(v[0] ?? 0), Number(v[1] ?? 0)] : [0, Number(v)];
    put(`W${row}`, q); put(`Y${row}`, s); put(`AA${row}`, { formulaResult: q + s });
  }
  if (b.umwelt) put('W15', b.umwelt);
  for (const [key, start] of [['kritis_bedroht', 17], ['kritis_ausgefallen', 27]] as const) {
    (b[key] ?? []).slice(0, 5).forEach((e, i) => {
      if (!KRITIS_SEKTOREN.includes(e.sektor)) throw new Error(`KRITIS-Sektor unbekannt: ${e.sektor}`);
      put(`W${start + 2 * i}`, e.sektor);
      if (e.text) put(`W${start + 2 * i + 1}`, e.text);
    });
  }
  for (const [k, ref] of Object.entries(STAERKE)) {
    const v = b.staerke?.[k];
    if (v) put(ref, v);
  }
  const vp = b.verpflegung ?? {};
  if (vp.portionen != null) put('L33', vp.portionen);
  if (vp.zeitpunkt) put('L35', checkTaktZeit(vp.zeitpunkt, 'verpflegung.zeitpunkt'));
  for (const [k, row] of Object.entries(BEURT_ROWS)) {
    const e = b.beurteilung?.[k];
    if (!e?.stufe) continue;
    const col = BEURT_COL[e.stufe];
    if (!col) throw new Error(`Beurteilung ${k}: Stufe ${e.stufe} unbekannt (1-3)`);
    put(`${col}${row}`, 'X');
    if (e.prognose) {
      if (!PROGNOSEN.includes(e.prognose)) throw new Error(`Prognose unbekannt: ${e.prognose}`);
      put(`X${row}`, e.prognose);
    }
  }
  AUFGABEN.forEach((k, i) => {
    const e = b.aufgaben?.[k];
    if (!e) return;
    const row = 47 + i;
    put(`D${row}`, 'X');
    if (e.text) put(`I${row}`, e.text);
    if (k === 'sonstige' && e.bezeichnung) put(`E${row}`, e.bezeichnung);
    if (e.schwerpunkt) { red.add(`D${row}`); red.add(`E${row}`); }
  });
  for (const e of (b.raeume ?? []).slice(0, 10)) {
    if (!(e.nr >= 1 && e.nr <= 10)) throw new Error(`EA-Nummer ${e.nr} außerhalb 1-10`);
    const row = 46 + e.nr;
    put(`S${row}`, e.text);
    if (e.schwerpunkt) { red.add(`R${row}`); red.add(`S${row}`); }
  }
  for (const [k, ref] of Object.entries(FREITEXT)) {
    const v = b[k as keyof BerichtDaten];
    if (typeof v === 'string' && v.trim()) put(ref, v);
  }
  const ein = b.einheiten ?? [];
  if (ein.length > 36) throw new Error('max. 36 Einheiten im Formular – Rest in Bemerkungen/Anlage');
  ein.forEach((e, i) => {
    const row = 83 + (i % 18);
    const c = i < 18 ? ['A', 'D', 'I'] : ['O', 'R', 'W'];
    if (!ORGS.includes(e.org)) throw new Error(`Organisation unbekannt: ${e.org}`);
    put(`${c[0]}${row}`, e.org); put(`${c[1]}${row}`, e.formation);
    if (e.bezeichnung) put(`${c[2]}${row}`, e.bezeichnung);
  });
  return { vals, red };
}

function escapeXml(s: string): string {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function cellRe(ref: string): RegExp {
  return new RegExp(`<c r="${ref}"([^>]*?)(?:/>|>([\\s\\S]*?)</c>)`);
}

function patchSheet(xml: string, vals: Map<string, CellValue>, red: Set<string>, styleMap: Map<string, string>): string {
  for (const [ref, v] of vals) {
    const m = cellRe(ref).exec(xml);
    if (!m) throw new Error(`Zelle ${ref} nicht in Vorlage`);
    const sm = / s="(\d+)"/.exec(m[1]);
    let sIdx = sm ? sm[1] : null;
    if (red.has(ref) && sIdx !== null) sIdx = styleMap.get(sIdx)!;
    const sAttr = sIdx !== null ? ` s="${sIdx}"` : '';
    let neu: string;
    if (typeof v === 'object') { // Summenformel in AA: Formel behalten, Ergebnis vorbelegen
      const f = /<f[^>]*?(?:\/>|>[\s\S]*?<\/f>)/.exec(m[2] ?? '');
      if (!f) throw new Error(`Zelle ${ref}: Formel fehlt in der Vorlage`);
      neu = `<c r="${ref}"${sAttr}>${f[0]}<v>${v.formulaResult}</v></c>`;
    } else if (typeof v === 'number') {
      neu = `<c r="${ref}"${sAttr}><v>${v}</v></c>`;
    } else { // line breaks stay (xml:space=preserve)
      neu = `<c r="${ref}"${sAttr} t="inlineStr"><is><t xml:space="preserve">${escapeXml(v)}</t></is></c>`;
    }
    xml = xml.slice(0, m.index) + neu + xml.slice(m.index + m[0].length);
  }
  for (const ref of red) { // only colour cells that got no value
    if (vals.has(ref)) continue;
    const m = new RegExp(`<c r="${ref}"([^>]*?) s="(\\d+)"`).exec(xml);
    if (!m) continue;
    const start = m.index + m[0].length - m[2].length - 1;
    xml = xml.slice(0, start) + styleMap.get(m[2]) + xml.slice(start + m[2].length);
  }
  return xml;
}

/** For every cell to be marked red a copy of its cell format with red fill and white bold font. */
function patchStyles(styles: string, sheet: string, red: Set<string>): { styles: string; map: Map<string, string> } {
  const fills = /<fills count="(\d+)">([\s\S]*?)<\/fills>/.exec(styles)!;
  const redFillId = Number(fills[1]);
  styles = styles.slice(0, fills.index)
    + `<fills count="${redFillId + 1}">${fills[2]}<fill><patternFill patternType="solid"><fgColor rgb="FFFF0000"/><bgColor indexed="64"/></patternFill></fill></fills>`
    + styles.slice(fills.index + fills[0].length);

  const fonts = /<fonts count="(\d+)"([^>]*)>([\s\S]*?)<\/fonts>/.exec(styles)!;
  const fontList = fonts[3].match(/<font>[\s\S]*?<\/font>|<font\/>/g) ?? [];
  const xfs = /<cellXfs count="(\d+)">([\s\S]*?)<\/cellXfs>/.exec(styles)!;
  const xfList = xfs[2].match(/<xf [^>]*?(?:\/>|>[\s\S]*?<\/xf>)/g) ?? [];
  const fontMap = new Map<string, string>();
  const newFonts: string[] = [];
  const map = new Map<string, string>();
  const extra: string[] = [];
  for (const ref of red) {
    const sm = new RegExp(`<c r="${ref}"[^>]*? s="(\\d+)"`).exec(sheet);
    if (!sm || map.has(sm[1])) continue;
    let xf = xfList[Number(sm[1])].replace(/fillId="\d+"/, `fillId="${redFillId}"`);
    const fid = /fontId="(\d+)"/.exec(xf)![1];
    if (!fontMap.has(fid)) {
      const f = fontList[Number(fid)].replace(/<color [^>]*\/>/g, '').replace('<b/>', '')
        .replace('<font>', '<font><b/><color rgb="FFFFFFFF"/>');
      fontMap.set(fid, String(fontList.length + newFonts.length));
      newFonts.push(f);
    }
    xf = xf.replace(/fontId="\d+"/, `fontId="${fontMap.get(fid)}"`);
    xf = xf.includes('applyFont') ? xf.replace(/applyFont="\d"/, 'applyFont="1"') : xf.replace('<xf ', '<xf applyFont="1" ');
    xf = xf.includes('applyFill') ? xf.replace(/applyFill="\d"/, 'applyFill="1"') : xf.replace('<xf ', '<xf applyFill="1" ');
    map.set(sm[1], String(xfList.length + extra.length));
    extra.push(xf);
  }
  const fontsNow = /<fonts count="(\d+)"([^>]*)>([\s\S]*?)<\/fonts>/.exec(styles)!;
  styles = styles.slice(0, fontsNow.index)
    + `<fonts count="${fontList.length + newFonts.length}"${fontsNow[2]}>${fontsNow[3]}${newFonts.join('')}</fonts>`
    + styles.slice(fontsNow.index + fontsNow[0].length);
  const xfsNow = /<cellXfs count="(\d+)">([\s\S]*?)<\/cellXfs>/.exec(styles)!;
  styles = styles.slice(0, xfsNow.index)
    + `<cellXfs count="${xfList.length + extra.length}">${xfsNow[2]}${extra.join('')}</cellXfs>`
    + styles.slice(xfsNow.index + xfsNow[0].length);
  return { styles, map };
}

/** Fills the template; returns the xlsx and the plausibility warnings. */
export function erzeugeLagebericht(b: BerichtDaten, vorlage = LAGEBERICHT_VORLAGE): { buffer: Buffer; warnungen: string[] } {
  const warnungen = plausi(b);
  const { vals, red } = buildCells(b);
  const zip = new PizZip(fs.readFileSync(vorlage));
  const sheet = zip.file(SHEET)!.asText();
  let map = new Map<string, string>();
  if (red.size > 0) {
    const patched = patchStyles(zip.file('xl/styles.xml')!.asText(), sheet, red);
    zip.file('xl/styles.xml', patched.styles);
    map = patched.map;
  }
  zip.file(SHEET, patchSheet(sheet, vals, red, map));
  const ct = zip.file('[Content_Types].xml')!.asText();
  zip.file('[Content_Types].xml', ct.replace('spreadsheetml.template.main+xml', 'spreadsheetml.sheet.main+xml'));
  const wb = zip.file('xl/workbook.xml')!.asText();
  zip.file('xl/workbook.xml', wb.replace(/<calcPr ([^/]*)\/>/, '<calcPr $1 fullCalcOnLoad="1"/>'));
  return { buffer: zip.generate({ type: 'nodebuffer', compression: 'DEFLATE' }), warnungen };
}
