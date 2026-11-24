/**
 * Lagekarte Heusweiler (Vorlage Lagekarte_Heusweiler_V1-2.potx) befüllen. TypeScript port of the
 * S2 skill's lagekarte_build.py: writes DrawingML directly into slide 1 instead of python-pptx.
 *
 * Aufbau nach RV-Beispiel 03a: EA-Grenzen, Schadensflächen, Linien und Markierungen auf der Karte;
 * EA-Nummern und Gefahren-/Maßnahmen-/Stellenzeichen groß in den Randkästchen, je mit schwarzem
 * Pfeil zum Ort. Koordinaten ETRS89/UTM 32N wie das Kartengitter (oder WGS84 lat/lon).
 */
import fs from 'fs';
import path from 'path';
import PizZip from 'pizzip';
import { S2_ASSETS } from './common';

export const LAGEKARTE_VORLAGE = path.join(S2_ASSETS, 'lagekarte-vorlage.potx');
export const ZEICHEN_DIR = path.join(S2_ASSETS, 'zeichen');

// --- Kalibrierung Kartenbild (Layout "Bildplatzhalter 30", media/image1.jpeg 3490x2467 px) ---
const MAP_OFF_X = 5345723;
const MAP_OFF_Y = 4006152; // EMU, position of the map picture on the slide
const CROP_L_PX = 0.03986 * 3490; // srcRect l=3,986 %
const EMU_PER_PX = 32289526 / (3490 * (1 - 2 * 0.03986));
export const PX_PER_KM = (3160 - 287) / 11.0; // grid lines 345000 .. 356000
export const E0_PX = 287;
export const E0 = 345000;
export const N0_PX = 184;
export const N0 = 5472000;
export const MAP_E_MIN = 345000 - 0.4e3;
export const MAP_E_MAX = 356000 + 0.4e3;
export const MAP_N_MIN = 5464000 - 0.3e3;
export const MAP_N_MAX = 5472000 + 0.4e3;

export const AREA: Record<string, string> = { // colours from slide 2 of the template
  brand: 'E4221B', kontaminiert: 'FFED00', ueberschwemmt: '0090D6',
  versorgung: 'CD5FA0', duerre: '9A6732', sonstiges: 'F49B19',
};
export const LINE: Record<string, string> = { rettungsweg: '00B050', riegelstellung: '0070C0', brandausbreitung: 'FF0000' };

type Pt = [number, number];
interface Ort { utm?: Pt; latlon?: Pt }
export interface LageKarte {
  bearbeiter: string;
  takt_zeit: string;
  symbol_mm?: number;
  hinterlegung?: boolean;
  symbole?: (Ort & { id: string; text?: string; mm?: number })[];
  flaechen?: { typ: string; utm?: Pt[]; latlon?: Pt[]; text?: string; text_pos?: 'S'; deckkraft?: number }[];
  linien?: { typ: string; utm?: Pt[]; latlon?: Pt[] }[];
  kaestchen?: (Ort & { id: string; text?: string })[];
}

export function latlonToUtm32(lat: number, lon: number): Pt {
  const a = 6378137.0;
  const f = 1 / 298.257222101;
  const k0 = 0.9996;
  const lon0 = (9 * Math.PI) / 180;
  const e2 = f * (2 - f);
  const ep2 = e2 / (1 - e2);
  const phi = (lat * Math.PI) / 180;
  const lam = (lon * Math.PI) / 180;
  const N = a / Math.sqrt(1 - e2 * Math.sin(phi) ** 2);
  const T = Math.tan(phi) ** 2;
  const C = ep2 * Math.cos(phi) ** 2;
  const A = Math.cos(phi) * (lam - lon0);
  const M = a * ((1 - e2 / 4 - (3 * e2 ** 2) / 64 - (5 * e2 ** 3) / 256) * phi
    - ((3 * e2) / 8 + (3 * e2 ** 2) / 32 + (45 * e2 ** 3) / 1024) * Math.sin(2 * phi)
    + ((15 * e2 ** 2) / 256 + (45 * e2 ** 3) / 1024) * Math.sin(4 * phi)
    - ((35 * e2 ** 3) / 3072) * Math.sin(6 * phi));
  const E = k0 * N * (A + ((1 - T + C) * A ** 3) / 6 + ((5 - 18 * T + T ** 2 + 72 * C - 58 * ep2) * A ** 5) / 120) + 500000;
  const Nn = k0 * (M + N * Math.tan(phi) * (A ** 2 / 2 + ((5 - T + 9 * C + 4 * C ** 2) * A ** 4) / 24
    + ((61 - 58 * T + T ** 2 + 600 * C - 330 * ep2) * A ** 6) / 720));
  return [E, Nn];
}

function toUtm(o: Ort): Pt {
  if (o.utm) return o.utm;
  if (o.latlon) return latlonToUtm32(o.latlon[0], o.latlon[1]);
  throw new Error('Ort ohne Koordinate');
}

export function imKartenausschnitt(E: number, N: number): boolean {
  return E >= MAP_E_MIN && E <= MAP_E_MAX && N >= MAP_N_MIN && N <= MAP_N_MAX;
}

export function utmToEmu(E: number, N: number): Pt {
  if (!imKartenausschnitt(E, N)) {
    throw new Error(`Koordinate außerhalb des Kartenausschnitts: ${E.toFixed(0)} / ${N.toFixed(0)}`);
  }
  const px = E0_PX + ((E - E0) / 1000) * PX_PER_KM;
  const py = N0_PX + ((N0 - N) / 1000) * PX_PER_KM;
  return [Math.trunc(MAP_OFF_X + (px - CROP_L_PX) * EMU_PER_PX), Math.trunc(MAP_OFF_Y + py * EMU_PER_PX)];
}

function esc(s: string): string {
  return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function pngSize(buf: Buffer): Pt {
  if (buf.readUInt32BE(12) !== 0x49484452) throw new Error('Zeichen ist keine PNG-Datei');
  return [buf.readUInt32BE(16), buf.readUInt32BE(20)];
}

const MM = 36000; // EMU per mm
const PT = 12700; // EMU per pt

interface Box { idx: number; left: number; top: number; width: number; height: number }

class SlideBuilder {
  shapes: string[] = [];
  private nextId = 2000;
  private media = new Map<string, { rid: string; size: Pt }>();
  rels: string[] = [];

  constructor(private zip: PizZip, private zeichenDir: string) {}

  private id(): number { return this.nextId++; }

  private xfrm(x: number, y: number, cx: number, cy: number, extra = ''): string {
    return `<a:xfrm${extra}><a:off x="${Math.round(x)}" y="${Math.round(y)}"/><a:ext cx="${Math.round(cx)}" cy="${Math.round(cy)}"/></a:xfrm>`;
  }

  private solid(rgb: string, alphaPct?: number): string {
    const alpha = alphaPct === undefined ? '' : `<a:alpha val="${Math.round(alphaPct * 1000)}"/>`;
    return `<a:solidFill><a:srgbClr val="${rgb}">${alpha}</a:srgbClr></a:solidFill>`;
  }

  /** Adds the PNG of a taktisches Zeichen once to the package; returns relationship id and size. */
  zeichen(id: string): { rid: string; size: Pt } {
    let m = this.media.get(id);
    if (m) return m;
    if (!/^[A-ZÄÖÜ]+_\d{2}(_b)?$/.test(id)) throw new Error(`Ungültige Zeichen-ID ${id}`);
    const file = path.join(this.zeichenDir, `${id}.png`);
    if (!fs.existsSync(file)) throw new Error(`Zeichen ${id} fehlt in der Zeichenbibliothek`);
    const buf = fs.readFileSync(file);
    const rid = `rIdS2z${this.media.size + 1}`;
    const target = `s2_${this.media.size + 1}.png`;
    this.zip.file(`ppt/media/${target}`, buf);
    this.rels.push(`<Relationship Id="${rid}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="../media/${target}"/>`);
    m = { rid, size: pngSize(buf) };
    this.media.set(id, m);
    return m;
  }

  pictureFit(id: string, cx: number, cy: number, box: number): void {
    const { rid, size: [w, h] } = this.zeichen(id);
    const s = box / Math.max(w, h);
    const n = this.id();
    this.shapes.push(`<p:pic><p:nvPicPr><p:cNvPr id="${n}" name="Zeichen ${esc(id)}"/><p:cNvPicPr><a:picLocks noChangeAspect="1"/></p:cNvPicPr><p:nvPr/></p:nvPicPr>`
      + `<p:blipFill><a:blip r:embed="${rid}"/><a:stretch><a:fillRect/></a:stretch></p:blipFill>`
      + `<p:spPr>${this.xfrm(Math.trunc(cx - (w * s) / 2), Math.trunc(cy - (h * s) / 2), Math.trunc(w * s), Math.trunc(h * s))}<a:prstGeom prst="rect"><a:avLst/></a:prstGeom></p:spPr></p:pic>`);
  }

  text(text: string, x: number, y: number, w: number, size = 28, bold = true, hinterlegt = true): void {
    const n = this.id();
    const fill = hinterlegt ? this.solid('FFFFFF', 70) : '<a:noFill/>';
    const paras = text.split('\n').map((line) => `<a:p><a:pPr algn="ctr"/><a:r><a:rPr lang="de-DE" sz="${size * 100}" b="${bold ? 1 : 0}" dirty="0"><a:latin typeface="Arial"/><a:cs typeface="Arial"/></a:rPr><a:t>${esc(line)}</a:t></a:r></a:p>`).join('');
    this.shapes.push(`<p:sp><p:nvSpPr><p:cNvPr id="${n}" name="Text ${n}"/><p:cNvSpPr txBox="1"/><p:nvPr/></p:nvSpPr>`
      + `<p:spPr>${this.xfrm(x, y, w, 500000)}<a:prstGeom prst="rect"><a:avLst/></a:prstGeom>${fill}</p:spPr>`
      + `<p:txBody><a:bodyPr wrap="${hinterlegt ? 'square' : 'none'}" rtlCol="0"><a:spAutoFit/></a:bodyPr><a:lstStyle/>${paras}</p:txBody></p:sp>`);
  }

  roundRectBg(x: number, y: number, size: number): void {
    const n = this.id();
    this.shapes.push(`<p:sp><p:nvSpPr><p:cNvPr id="${n}" name="Hinterlegung ${n}"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr>`
      + `<p:spPr>${this.xfrm(x - size * 0.6, y - size * 0.6, size * 1.2, size * 1.2)}<a:prstGeom prst="roundRect"><a:avLst/></a:prstGeom>`
      + `${this.solid('FFFFFF', 85)}<a:ln w="${PT}">${this.solid('808080')}</a:ln></p:spPr></p:sp>`);
  }

  /** Black arrow (6 pt) from the box to the place, head at the place – as in the RV example. */
  pfeil(x1: number, y1: number, x2: number, y2: number): void {
    const n = this.id();
    const flip = `${x2 < x1 ? ' flipH="1"' : ''}${y2 < y1 ? ' flipV="1"' : ''}`;
    this.shapes.push(`<p:cxnSp><p:nvCxnSpPr><p:cNvPr id="${n}" name="Pfeil ${n}"/><p:cNvCxnSpPr/><p:nvPr/></p:nvCxnSpPr>`
      + `<p:spPr>${this.xfrm(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1), flip)}<a:prstGeom prst="line"><a:avLst/></a:prstGeom>`
      + `<a:ln w="76200">${this.solid('000000')}<a:tailEnd type="triangle"/></a:ln></p:spPr></p:cxnSp>`);
  }

  freeform(pts: Pt[], closed: boolean, fill: string, ln: string): void {
    const xy = pts.map(([e, nn]) => utmToEmu(e, nn));
    const xs = xy.map((p) => p[0]);
    const ys = xy.map((p) => p[1]);
    const x0 = Math.min(...xs);
    const y0 = Math.min(...ys);
    const w = Math.max(1, Math.max(...xs) - x0);
    const h = Math.max(1, Math.max(...ys) - y0);
    const ptXml = (p: Pt) => `<a:pt x="${p[0] - x0}" y="${p[1] - y0}"/>`;
    const path = `<a:moveTo>${ptXml(xy[0])}</a:moveTo>${xy.slice(1).map((p) => `<a:lnTo>${ptXml(p)}</a:lnTo>`).join('')}${closed ? '<a:close/>' : ''}`;
    const n = this.id();
    this.shapes.push(`<p:sp><p:nvSpPr><p:cNvPr id="${n}" name="Freihandform ${n}"/><p:cNvSpPr/><p:nvPr/></p:nvSpPr>`
      + `<p:spPr>${this.xfrm(x0, y0, w, h)}<a:custGeom><a:avLst/><a:gdLst/><a:ahLst/><a:cxnLst/><a:rect l="0" t="0" r="r" b="b"/>`
      + `<a:pathLst><a:path w="${w}" h="${h}">${path}</a:path></a:pathLst></a:custGeom>${fill}${ln}</p:spPr></p:sp>`);
  }

  area(typ: string, pts: Pt[], deckkraft = 45): void {
    if (typ === 'ea_grenze') {
      // Strich-Zwei-Punkt wie im RV-Beispiel
      this.freeform(pts, true, '<a:noFill/>', `<a:ln w="76200">${this.solid('000000')}<a:prstDash val="lgDashDotDot"/></a:ln>`);
      return;
    }
    const rgb = AREA[typ];
    if (!rgb) throw new Error(`Flächentyp unbekannt: ${typ}`);
    this.freeform(pts, true, this.solid(rgb, deckkraft), `<a:ln w="${3 * PT}">${this.solid(rgb)}</a:ln>`);
  }

  line(typ: string, pts: Pt[]): void {
    const rgb = LINE[typ];
    if (!rgb) throw new Error(`Linientyp unbekannt: ${typ}`);
    this.freeform(pts, false, '<a:noFill/>', `<a:ln w="123825">${this.solid(rgb)}</a:ln>`);
  }
}

/** Positions of the picture placeholders ("Taktische Zeichen" boxes) from the slide layout. */
function placeholderBoxes(layoutXml: string): Map<number, Box> {
  const boxes = new Map<number, Box>();
  for (const sp of layoutXml.match(/<p:sp>[\s\S]*?<\/p:sp>/g) ?? []) {
    const idx = /<p:ph [^>]*idx="(\d+)"/.exec(sp);
    const off = /<a:off x="(\d+)" y="(\d+)"\/><a:ext cx="(\d+)" cy="(\d+)"/.exec(sp);
    if (!idx || !off || !/type="pic"/.test(sp)) continue;
    boxes.set(Number(idx[1]), { idx: Number(idx[1]), left: +off[1], top: +off[2], width: +off[3], height: +off[4] });
  }
  return boxes;
}

/** Kopf-/Fußtabelle "Tabelle 128": Zeile 0 = Bearbeiter, Zeile 1 = Takt. Zeit, jeweils Spalte 1. */
function fillTable(slide: string, bearbeiter: string, zeit: string): string {
  const frame = /<p:graphicFrame>(?:(?!<\/p:graphicFrame>)[\s\S])*?name="Tabelle 128"[\s\S]*?<\/p:graphicFrame>/.exec(slide);
  if (!frame) throw new Error('Tabelle Bearbeiter/Takt. Zeit fehlt in der Vorlage');
  let i = 0;
  const filled = frame[0].replace(/<a:tc>([\s\S]*?)<\/a:tc>/g, (tc, inner: string) => {
    const cell = i++;
    const text = cell === 1 ? bearbeiter : cell === 3 ? zeit : null;
    if (text === null) return tc;
    const body = `<a:txBody><a:bodyPr wrap="none"/><a:lstStyle/><a:p><a:pPr><a:lnSpc><a:spcPct val="70000"/></a:lnSpc></a:pPr><a:r><a:rPr lang="de-DE" sz="1300" b="0" dirty="0"><a:solidFill><a:srgbClr val="000000"/></a:solidFill></a:rPr><a:t>${esc(text)}</a:t></a:r></a:p></a:txBody>`;
    const rest = inner.replace(/<a:txBody>[\s\S]*?<\/a:txBody>/, '').replace(/<a:tcPr([^>]*?)(\/?)>/, (_m, attrs: string, close: string) => {
      const a = attrs.replace(/ mar[LR]="\d+"/g, '');
      return `<a:tcPr${a} marL="0" marR="0"${close}>`;
    });
    return `<a:tc>${body}${rest}</a:tc>`;
  });
  return slide.slice(0, frame.index) + filled + slide.slice(frame.index + frame[0].length);
}

interface KItem { id: string; text?: string; pt: Pt | null; side: 'L' | 'R' | 'T' }

/** Builds the pptx. `nurFolie1` removes slide 2 (palette of the template), used for the PDF. */
export function erzeugeLagekarte(lage: LageKarte, opts: { nurFolie1?: boolean; vorlage?: string; zeichenDir?: string } = {}):
  { buffer: Buffer; warnungen: string[] } {
  const warnungen: string[] = [];
  const zip = new PizZip(fs.readFileSync(opts.vorlage ?? LAGEKARTE_VORLAGE));
  const b = new SlideBuilder(zip, opts.zeichenDir ?? ZEICHEN_DIR);
  const box = (lage.symbol_mm ?? 22) * MM;

  let slide = fillTable(zip.file('ppt/slides/slide1.xml')!.asText(), lage.bearbeiter ?? '', lage.takt_zeit ?? '');

  // Flächen zuerst (liegen unter den Zeichen)
  for (const fl of lage.flaechen ?? []) {
    const pts: Pt[] = fl.utm ?? (fl.latlon ?? []).map(([la, lo]) => latlonToUtm32(la, lo));
    if (pts.length < 3) throw new Error(`Fläche ${fl.text ?? fl.typ}: mindestens 3 Ecken nötig`);
    b.area(fl.typ, pts, fl.deckkraft);
    if (fl.text) {
      // Beschriftung mittig über der Nordkante ('S' = unter der Südkante)
      const es = pts.map((p) => p[0]);
      const ns = pts.map((p) => p[1]);
      const unten = fl.text_pos === 'S';
      const [x, y] = utmToEmu((Math.min(...es) + Math.max(...es)) / 2, unten ? Math.min(...ns) : Math.max(...ns));
      if (fl.typ === 'ea_grenze') { // wie RV-Beispiel: „EA 1: links der Saar“, 36 pt fett, ohne Hintergrund
        b.text(fl.text, x - 75 * MM, unten ? y + MM : y - 22 * MM, 150 * MM, 36, true, false);
      } else {
        b.text(fl.text, x - 60 * MM, unten ? y + MM : y - 16 * MM, 120 * MM, 24);
      }
    }
  }

  for (const ln of lage.linien ?? []) {
    const pts: Pt[] = ln.utm ?? (ln.latlon ?? []).map(([la, lo]) => latlonToUtm32(la, lo));
    if (pts.length < 2) throw new Error(`Linie ${ln.typ}: mindestens 2 Punkte nötig`);
    b.line(ln.typ, pts);
  }

  for (const sy of lage.symbole ?? []) {
    const [x, y] = utmToEmu(...toUtm(sy));
    const size = (sy.mm ?? lage.symbol_mm ?? 22) * MM;
    if (lage.hinterlegung ?? true) b.roundRectBg(x, y, size); // white backing keeps thin line symbols readable
    b.pictureFit(sy.id, x, y, size);
    if (sy.text) b.text(sy.text, x - 50 * MM, y + box / 2 + MM, 100 * MM, 24);
  }

  // Randkästchen: westlich der Kartenmitte -> linke Spalte, östlich -> rechte; Überlauf -> andere
  // Spalte, dann obere Reihe
  const layoutBoxes = placeholderBoxes(zip.file('ppt/slideLayouts/slideLayout1.xml')!.asText());
  const onSlide = new Set([...slide.matchAll(/<p:ph type="pic"[^>]*idx="(\d+)"/g)].map((m) => Number(m[1])));
  const pick = (ids: number[]) => ids.filter((i) => onSlide.has(i) && layoutBoxes.has(i)).map((i) => layoutBoxes.get(i)!);
  const cols: Record<'L' | 'R' | 'T', Box[]> = {
    L: pick([11, 17, 18, 19, 20, 21, 22]),
    R: pick([34, 35, 36, 37, 38, 39, 40]),
    T: pick([24, 25, 26, 27, 28, 29, 30, 31, 32]),
  };
  const E_MID = (MAP_E_MIN + MAP_E_MAX) / 2;
  const items: KItem[] = (lage.kaestchen ?? []).map((k) => {
    const hasPt = !!(k.utm || k.latlon);
    const pt = hasPt ? utmToEmu(...toUtm(k)) : null;
    const e = hasPt ? toUtm(k)[0] : null;
    return { id: k.id, text: k.text, pt, side: e === null || e < E_MID ? 'L' : 'R' };
  });
  const px = (i: KItem) => (i.pt ? i.pt[0] : 0);
  for (const [side, other] of [['L', 'R'], ['R', 'L']] as const) { // overflow: places nearest the middle switch
    const mine = items.filter((i) => i.side === side);
    mine.sort((a, c) => (side === 'L' ? px(c) - px(a) : px(a) - px(c)));
    for (const i of mine.slice(0, Math.max(0, mine.length - cols[side].length))) {
      i.side = items.filter((j) => j.side === other).length < cols[other].length ? other : 'T';
    }
  }
  const usedPh: number[] = [];
  for (const side of ['L', 'R', 'T'] as const) {
    const boxes = cols[side];
    let mine = items.filter((i) => i.side === side);
    const key = (i: KItem) => (i.pt ? (side === 'T' ? i.pt[0] : i.pt[1]) : 0);
    mine.sort((a, c) => key(a) - key(c));
    if (mine.length > boxes.length) {
      warnungen.push(`${mine.length - boxes.length} Kästchen ohne Platz – weggelassen`);
      mine = mine.slice(0, boxes.length);
    }
    // in order to the nearest free box (no crossing arrows)
    let start = 0;
    mine.forEach((it, n) => {
      const rest = mine.length - n - 1;
      const cand: number[] = [];
      for (let j = start; j < boxes.length - rest; j++) cand.push(j);
      let bi = cand[0];
      if (it.pt) {
        const c = (bx: Box) => (side === 'T' ? bx.left + bx.width / 2 : bx.top + bx.height / 2);
        const v = side === 'T' ? it.pt[0] : it.pt[1];
        bi = cand.reduce((best, j) => (Math.abs(c(boxes[j]) - v) < Math.abs(c(boxes[best]) - v) ? j : best), cand[0]);
      }
      start = bi + 1;
      const ph = boxes[bi];
      usedPh.push(ph.idx);
      const hatText = !!it.text;
      const cy = ph.top + Math.trunc(ph.height * (hatText ? 0.42 : 0.5));
      b.pictureFit(it.id, ph.left + Math.trunc(ph.width / 2), cy, Math.trunc(ph.width * (hatText ? 0.62 : 0.78)));
      if (hatText) b.text(it.text!, ph.left + 2 * MM, ph.top + Math.trunc(ph.height * 0.76), ph.width - 4 * MM, 20, false);
      if (it.pt) {
        let x1: number;
        let y1: number;
        if (side === 'L') { x1 = ph.left + ph.width; y1 = ph.top + Math.trunc(ph.height / 2); }
        else if (side === 'R') { x1 = ph.left; y1 = ph.top + Math.trunc(ph.height / 2); }
        else { x1 = ph.left + Math.trunc(ph.width / 2); y1 = ph.top + ph.height; }
        b.pfeil(x1, y1, it.pt[0], it.pt[1]);
      }
    });
  }
  // filled boxes: remove the empty picture placeholder ("Bild durch Klicken hinzufügen")
  for (const idx of usedPh) {
    slide = slide.replace(new RegExp(`<p:sp>(?:(?!<p:sp>)[\\s\\S])*?<p:ph type="pic"[^>]*idx="${idx}"[\\s\\S]*?</p:sp>`), '');
  }

  slide = slide.replace('</p:spTree>', `${b.shapes.join('')}</p:spTree>`);
  if (!/xmlns:r=/.test(slide.slice(0, 500))) {
    slide = slide.replace('<p:sld ', '<p:sld xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships" ');
  }
  zip.file('ppt/slides/slide1.xml', slide);
  if (b.rels.length > 0) {
    const rels = zip.file('ppt/slides/_rels/slide1.xml.rels')!.asText();
    zip.file('ppt/slides/_rels/slide1.xml.rels', rels.replace('</Relationships>', `${b.rels.join('')}</Relationships>`));
  }

  let ct = zip.file('[Content_Types].xml')!.asText()
    .replace('presentationml.template.main+xml', 'presentationml.presentation.main+xml');
  if (!/Extension="png"/i.test(ct)) ct = ct.replace('<Types ', '<Types ').replace(/(<Types[^>]*>)/, '$1<Default Extension="png" ContentType="image/png"/>');
  if (opts.nurFolie1) {
    // Folie 2 ist die Zeichenpalette der Vorlage: für das PDF an den RV nur Folie 1
    const pres = zip.file('ppt/presentation.xml')!.asText();
    const presRels = zip.file('ppt/_rels/presentation.xml.rels')!.asText();
    const rel = /<Relationship Id="([^"]+)" Type="[^"]*\/slide" Target="slides\/slide2\.xml"\/>/.exec(presRels);
    if (rel) {
      zip.file('ppt/presentation.xml', pres.replace(new RegExp(`<p:sldId id="\\d+" r:id="${rel[1]}"/>`), ''));
      zip.file('ppt/_rels/presentation.xml.rels', presRels.replace(rel[0], ''));
      zip.remove('ppt/slides/slide2.xml');
      zip.remove('ppt/slides/_rels/slide2.xml.rels');
      ct = ct.replace(/<Override PartName="\/ppt\/slides\/slide2\.xml"[^>]*\/>/, '');
    }
  }
  zip.file('[Content_Types].xml', ct);
  return { buffer: zip.generate({ type: 'nodebuffer', compression: 'DEFLATE' }), warnungen };
}
