import { describe, it, expect } from 'vitest';
import fs from 'fs';
import path from 'path';
import PizZip from 'pizzip';
import { LAGEBERICHT_VORLAGE, erzeugeLagebericht, type BerichtDaten } from '../src/services/s2/lagebericht';
import { erzeugeLagekarte, latlonToUtm32, type LageKarte } from '../src/services/s2/lagekarte';
import { isTaktZeit, kraefteUebersicht, staerkeGesamt, taktZeit } from '../src/services/s2/common';
import { finde } from '../src/services/s2/geo';
import { mail, naechsterBericht, pruefung } from '../src/services/s2/export';

// S2 Lage: TypeScript port of the S2 skill's Python scripts (lagebericht_fill.py,
// lagekarte_build.py, geo_lookup.py). Fixtures = the skill's examples (beispiele/).
const fixture = <T>(name: string): T => JSON.parse(fs.readFileSync(path.join(__dirname, 'fixtures', name), 'utf8')) as T;

function cell(sheet: string, ref: string): string {
  return new RegExp(`<c r="${ref}"[^>]*?(?:/>|>[\\s\\S]*?</c>)`).exec(sheet)?.[0] ?? '';
}

describe('S2: Stärke und taktische Zeit', () => {
  it('liest das Erfassungsschema als Gesamtzahl', () => {
    expect(staerkeGesamt('1/8/9')).toBe(9); // Führer/Mannschaft/Gesamt
    expect(staerkeGesamt('0/1/2/6/9')).toBe(9);
    expect(staerkeGesamt('1/6')).toBe(7);
    expect(staerkeGesamt('2/5/24')).toBe(31); // letzte Zahl ist nicht die Summe -> addieren
    expect(staerkeGesamt(' 12 ')).toBe(12);
    expect(staerkeGesamt('')).toBeNull();
    expect(staerkeGesamt('ca. 10')).toBeNull();
  });

  it('taktische Zeit in Ortszeit mit amerikanischem Monat', () => {
    expect(taktZeit(new Date('2026-11-14T10:00:00Z'))).toBe('141100nov26'); // MEZ
    expect(taktZeit(new Date('2026-07-01T22:30:00Z'))).toBe('020030jul26'); // MESZ, Tageswechsel
    expect(isTaktZeit('141100nov26')).toBe(true);
    expect(isTaktZeit('141100NOV26')).toBe(true);
    expect(isTaktZeit('141100okt26')).toBe(false); // deutscher Monat
    expect(isTaktZeit('142400nov26')).toBe(false);
    expect(naechsterBericht('312330dec26')).toBe('010000jan27');
  });

  it('fasst Kräfte für den Lagebericht zusammen (Grundschutz ist Teil der Bereitschaft)', () => {
    const u = kraefteUebersicht([
      { org: 'FW', formation: 'Löschbezirk', bezeichnung: 'LB 111', staerke: '1/8/9', status: 'einsatz' },
      { org: 'FW', formation: 'Löschbezirk', bezeichnung: 'LB 118', staerke: '1/6', status: 'grundschutz' },
      { org: 'THW', formation: 'TZ (Technischer Zug)', bezeichnung: 'OV SB', staerke: '2/5/24', status: 'bereitschaft' },
      { org: 'DRK', formation: null, bezeichnung: 'RTW 1', staerke: null, status: 'einsatz' },
      { org: 'FW', formation: 'Löschbezirk', bezeichnung: 'LB 115', staerke: '1/8/9', status: 'entlassen' },
    ]);
    expect(u.kraefte.einsatz).toBe('FW: LB 111\nDRK: RTW 1');
    expect(u.kraefte.bereitschaft).toBe('FW: LB 118\nTHW: OV SB');
    expect(u.kraefte.grundschutz).toBe('FW: LB 118');
    expect(u.kraefte.ruhe).toBe('./.');
    expect(u.staerkeGesamt).toBe(9 + 7 + 31);
    expect(u.ohneStaerke).toEqual(['RTW 1']);
    expect(u.einheiten.map((e) => e.bezeichnung)).toEqual(['LB 111', 'LB 118', 'OV SB']);
  });
});

describe('S2: Lagebericht (Excel-Vorlage V1_02)', () => {
  const bericht = fixture<BerichtDaten>('s2-bericht.json');
  const { buffer, warnungen } = erzeugeLagebericht(bericht);
  const zip = new PizZip(buffer);
  const sheet = zip.file('xl/worksheets/sheet1.xml')!.asText();

  it('füllt Kopf, Stärke (nur Gesamtzahl), Schäden und Freitexte in die Vorlagenzellen', () => {
    expect(warnungen).toEqual([]);
    expect(cell(sheet, 'D2')).toContain('Gemeinde Heusweiler');
    expect(cell(sheet, 'R2')).toContain('141100nov26');
    expect(cell(sheet, 'AA2')).toContain('<v>1</v>');
    expect(cell(sheet, 'E31')).toContain('<v>168</v>');
    // Teilstärken (E33-E36) bleiben wie in der Vorlage
    const vorlage = new PizZip(fs.readFileSync(LAGEBERICHT_VORLAGE)).file('xl/worksheets/sheet1.xml')!.asText();
    for (const ref of ['E33', 'E34', 'E35', 'E36']) expect(cell(sheet, ref)).toBe(cell(vorlage, ref));
    expect(cell(sheet, 'W5')).toContain('<v>0</v>'); // verletzt ? unbestätigt
    expect(cell(sheet, 'Y5')).toContain('<v>2</v>'); // ! bestätigt
    expect(cell(sheet, 'AA5')).toMatch(/<f[^>]*>.*<\/f><v>2<\/v>/); // Summenformel bleibt
    expect(cell(sheet, 'B5')).toContain('THW: FGr WP (OV SB)\nDRK: FZ BP 250');
    expect(cell(sheet, 'A66')).toContain('Allgemein:');
  });

  it('markiert Schwerpunkte rot und bleibt eine gültige Arbeitsmappe', () => {
    const styles = zip.file('xl/styles.xml')!.asText();
    expect(styles).toContain('<fgColor rgb="FFFF0000"/>');
    expect(zip.file('[Content_Types].xml')!.asText()).toContain('spreadsheetml.sheet.main+xml');
    expect(zip.file('xl/workbook.xml')!.asText()).toContain('fullCalcOnLoad="1"');
    // the template's drop-downs (x14 data validations) survive
    expect(sheet).toContain('x14:dataValidation');
  });

  it('lehnt ungültige Listenwerte ab und warnt bei Widersprüchen', () => {
    expect(() => erzeugeLagebericht({ ...bericht, zeit: '141100okt26' })).toThrow(/taktische Zeit/);
    expect(() => erzeugeLagebericht({ ...bericht, beurteilung: { schadenslage: { stufe: 2, prognose: 'besser' } } })).toThrow(/Prognose/);
    expect(() => erzeugeLagebericht({ ...bericht, einheiten: [{ org: 'Polizei', formation: 'x' }] })).toThrow(/Organisation/);
    const w = erzeugeLagebericht({ ...bericht, probleme: '', beurteilung: { kraefte: { stufe: 3, prognose: 'Verschärfung' } } }).warnungen;
    expect(w.join()).toMatch(/kritisch\/Verschärfung/);
  });
});

describe('S2: Lagekarte (PowerPoint-Vorlage Heusweiler)', () => {
  const lage = fixture<LageKarte>('s2-lage.json');

  it('setzt Flächen, Zeichen, Randkästchen mit Pfeil und Bearbeiter/Zeit', () => {
    const { buffer, warnungen } = erzeugeLagekarte(lage);
    expect(warnungen).toEqual([]);
    const zip = new PizZip(buffer);
    const slide = zip.file('ppt/slides/slide1.xml')!.asText();
    const kaestchen = lage.kaestchen!;
    expect((slide.match(/<p:cxnSp>/g) ?? []).length).toBe(kaestchen.filter((k) => k.utm || k.latlon).length);
    expect((slide.match(/<p:pic>/g) ?? []).length).toBe(kaestchen.length + lage.symbole!.length);
    expect((slide.match(/<a:custGeom>/g) ?? []).length).toBe(lage.flaechen!.length);
    expect(slide).toContain('prstDash val="lgDashDotDot"'); // EA-Grenze Strich-Zwei-Punkt
    expect(slide).toContain('S2 Pört');
    expect(slide).toContain('141100nov26');
    // filled boxes lose their empty picture placeholder, the remaining ones stay
    expect((slide.match(/<p:ph type="pic"/g) ?? []).length).toBe(25 - kaestchen.length);
    const rels = zip.file('ppt/slides/_rels/slide1.xml.rels')!.asText();
    expect((rels.match(/relationships\/image/g) ?? []).length).toBe(new Set([...kaestchen, ...lage.symbole!].map((k) => k.id)).size);
    expect(zip.file('[Content_Types].xml')!.asText()).toContain('presentationml.presentation.main+xml');
    expect(zip.file('ppt/slides/slide2.xml')).not.toBeNull();
  });

  it('PDF-Variante enthält nur Folie 1 (Folie 2 ist die Zeichenpalette)', () => {
    const zip = new PizZip(erzeugeLagekarte(lage, { nurFolie1: true }).buffer);
    expect(zip.file('ppt/slides/slide2.xml')).toBeNull();
    expect(zip.file('ppt/presentation.xml')!.asText().match(/<p:sldId /g)).toHaveLength(1);
  });

  it('rät keine Position: Koordinaten außerhalb des Ausschnitts brechen ab', () => {
    expect(() => erzeugeLagekarte({ ...lage, symbole: [{ id: 'BRAND_01', utm: [360000, 5467000] }] })).toThrow(/außerhalb/);
    expect(() => erzeugeLagekarte({ ...lage, symbole: [{ id: 'GIBTS_99', utm: [350000, 5467000] }] })).toThrow(/fehlt/);
  });

  it('rechnet WGS84 nach UTM 32N', () => {
    const [e, n] = latlonToUtm32(49.344, 6.95);
    expect(Math.round(e)).toBeGreaterThan(345000);
    expect(Math.round(e)).toBeLessThan(356000);
    expect(Math.round(n)).toBeGreaterThan(5464000);
    expect(Math.round(n)).toBeLessThan(5472000);
  });
});

describe('S2: Adressverzeichnis Heusweiler', () => {
  it('findet Hausnummern mit Schreibvarianten', () => {
    expect(finde('Schillerstr. 67')).toEqual([expect.objectContaining({ strasse: 'Schillerstraße', utm: [348860, 5466640], genauigkeit: 'HAUSNR' })]);
    expect(finde('Wengenwald 5')[0]).toMatchObject({ strasse: 'Zum Wengenwald', genauigkeit: 'NAECHSTE_HNR' });
    expect(finde('Im Dorf 13', 'Wahlschied')[0]).toMatchObject({ utm: [354590, 5467470] });
  });

  it('meldet Mehrdeutigkeit und macht Vorschläge statt zu raten', () => {
    const illinger = finde('Illinger Str. 10');
    expect(illinger.length).toBeGreaterThan(1);
    expect(illinger[0].hinweis).toMatch(/mehrdeutig/);
    const nf = finde('Winterscheider Str. 2');
    expect(nf[0]).toMatchObject({ genauigkeit: 'NICHT_GEFUNDEN', utm: null });
    expect(nf[0].vorschlaege).toContain('Winterscheidtstraße');
  });
});

describe('S2: Prüfung und Mailtext', () => {
  const lage = { name: 'Übung', kommune: 'Gemeinde Heusweiler', isExercise: true, recipient: 'uebung@example.org', contact: 'Tel. 0681 123' };
  const data = fixture<BerichtDaten>('s2-bericht.json');
  const bericht = { lfdNr: 2, zeit: '141100nov26', bearbeiter: 'S2 Test', karteAlsFoto: false, data };
  const abschnitte = [{ nr: 1, name: 'Dilsburg/Köllerbach', schwerpunkt: true, flaeche: [[349000, 5466000], [350000, 5466000], [350000, 5467000]] }];

  it('Mail nach Vorlage, Übungskennzeichnung, nächster Bericht zur vollen Stunde', () => {
    const m = mail(lage, bericht, abschnitte);
    expect(m.an).toBe('uebung@example.org');
    expect(m.betreff).toBe('ÜBUNG – ÜBUNG – ÜBUNG | Lagebericht Gemeinde Heusweiler Nr. 2 – 141100nov26');
    expect(m.text.startsWith('+++ ÜBUNG – ÜBUNG – ÜBUNG +++')).toBe(true);
    expect(m.text).toContain('Nächster Lagebericht: 141200nov26');
    expect(m.text).toContain('Erreichbar: Tel. 0681 123');
    expect(m.anhaenge).toEqual(['Lagebericht_Heusweiler_141100nov26_V2.xlsx', 'Lagekarte_Heusweiler_141100nov26_V2_Folie1.pdf']);
    const echt = mail({ ...lage, isExercise: false }, { ...bericht, karteAlsFoto: true }, abschnitte);
    expect(echt.text).not.toContain('ÜBUNG');
    expect(echt.anhaenge[1]).toBe('Foto der analog geführten Lagekarte');
  });

  it('findet Widersprüche zum Vorbericht', () => {
    const vor = { ...bericht, lfdNr: 1, data: { ...data, schaden: { verstorben: [0, 3] }, staerke: { gesamt: 60 } } };
    const w = pruefung(lage, bericht, abschnitte, [], vor as typeof bericht);
    expect(w.join('\n')).toMatch(/Verstorbene gesunken/);
    expect(w.join('\n')).toMatch(/Stärke springt/);
  });
});
