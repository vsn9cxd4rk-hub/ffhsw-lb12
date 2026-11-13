#!/usr/bin/env python3
"""Lagekarte Heusweiler (Vorlage Lagekarte_Heusweiler_V1-2.potx) aus JSON befüllen.
Aufruf: python3 lagekarte_build.py <vorlage.potx> <zeichen_ordner> <lage.json> <ausgabe.pptx>

lage.json (alle Schlüssel optional außer bearbeiter/takt_zeit):
{
  "bearbeiter": "S2 Pört",
  "takt_zeit": "081300NOV26",
  "symbol_mm": 22,                       # Kantenlänge Zeichen auf der Karte
  "symbole": [ {"id":"STELLE_03","utm":[349600,5467200],"text":"BR Sportplatz"},
               {"id":"BRAND_02","latlon":[49.3410,6.9310]} ],
  "flaechen": [ {"typ":"brand","utm":[[E,N],[E,N],...],"text":"EA 1"} ],
  "linien":   [ {"typ":"rettungsweg","utm":[[E,N],...]} ],
  "kaestchen": [{"id":"EA_01","utm":[350600,5466500]}]  # Randkästchen mit Pfeil zum Ort (RV-Beispiel 03a)
}
Flächentypen: ea_grenze, brand, kontaminiert, ueberschwemmt, versorgung, duerre, sonstiges
Linientypen: rettungsweg, riegelstellung, brandausbreitung
Koordinaten: ETRS89/UTM Zone 32N (wie Kartengitter) oder WGS84 lat/lon.
"""
import sys, os, json, math, zipfile, tempfile, copy
from pptx import Presentation
from pptx.util import Emu, Pt
from pptx.dml.color import RGBColor
from pptx.enum.dml import MSO_LINE_DASH_STYLE
from pptx.oxml.ns import qn
from lxml import etree
from PIL import Image

# --- Kalibrierung Kartenbild (Layout "Bildplatzhalter 30", media/image1.jpeg 3490x2467 px) ---
MAP_OFF_X, MAP_OFF_Y = 5345723, 4006152          # EMU, Lage des Kartenbilds auf der Folie
CROP_L_PX = 0.03986 * 3490                        # srcRect l=3,986 %
EMU_PER_PX = 32289526 / (3490 * (1 - 2 * 0.03986))
PX_PER_KM = (3160 - 287) / 11.0                   # Gitterlinien 345000 .. 356000
E0_PX, E0 = 287, 345000
N0_PX, N0 = 184, 5472000
MAP_E_MIN, MAP_E_MAX = 345000 - 0.4e3, 356000 + 0.4e3
MAP_N_MIN, MAP_N_MAX = 5464000 - 0.3e3, 5472000 + 0.4e3

AREA = {  # aus Folie 2 der Vorlage
    'brand': 'E4221B', 'kontaminiert': 'FFED00', 'ueberschwemmt': '0090D6',
    'versorgung': 'CD5FA0', 'duerre': '9A6732', 'sonstiges': 'F49B19'}
LINE = {'rettungsweg': '00B050', 'riegelstellung': '0070C0', 'brandausbreitung': 'FF0000'}


def latlon_to_utm32(lat, lon):
    a, f = 6378137.0, 1 / 298.257222101
    k0, lon0 = 0.9996, math.radians(9)
    e2 = f * (2 - f); ep2 = e2 / (1 - e2)
    phi, lam = math.radians(lat), math.radians(lon)
    N = a / math.sqrt(1 - e2 * math.sin(phi) ** 2)
    T = math.tan(phi) ** 2; C = ep2 * math.cos(phi) ** 2
    A = math.cos(phi) * (lam - lon0)
    M = a * ((1 - e2/4 - 3*e2**2/64 - 5*e2**3/256) * phi
             - (3*e2/8 + 3*e2**2/32 + 45*e2**3/1024) * math.sin(2*phi)
             + (15*e2**2/256 + 45*e2**3/1024) * math.sin(4*phi)
             - (35*e2**3/3072) * math.sin(6*phi))
    E = k0 * N * (A + (1-T+C)*A**3/6 + (5-18*T+T**2+72*C-58*ep2)*A**5/120) + 500000
    Nn = k0 * (M + N*math.tan(phi)*(A**2/2 + (5-T+9*C+4*C**2)*A**4/24
                                   + (61-58*T+T**2+600*C-330*ep2)*A**6/720))
    return E, Nn


def to_utm(item):
    if 'utm' in item: return item['utm']
    return latlon_to_utm32(*item['latlon'])


def utm_to_emu(E, N):
    if not (MAP_E_MIN <= E <= MAP_E_MAX and MAP_N_MIN <= N <= MAP_N_MAX):
        raise ValueError(f'Koordinate außerhalb des Kartenausschnitts: {E:.0f} / {N:.0f}')
    px = E0_PX + (E - E0) / 1000 * PX_PER_KM
    py = N0_PX + (N0 - N) / 1000 * PX_PER_KM
    return int(MAP_OFF_X + (px - CROP_L_PX) * EMU_PER_PX), int(MAP_OFF_Y + py * EMU_PER_PX)


def potx_to_pptx(src, dst):
    with zipfile.ZipFile(src) as zi, zipfile.ZipFile(dst, 'w', zipfile.ZIP_DEFLATED) as zo:
        for i in zi.infolist():
            d = zi.read(i.filename)
            if i.filename == '[Content_Types].xml':
                d = d.replace(b'presentationml.template.main+xml',
                              b'presentationml.presentation.main+xml')
            zo.writestr(i, d)


def set_alpha(shape, pct):
    sf = shape.fill._xPr.find(qn('a:solidFill'))
    clr = sf[0]
    a = etree.SubElement(clr, qn('a:alpha')); a.set('val', str(int(pct * 1000)))


def add_picture_fit(slide, path, cx, cy, box):
    im = Image.open(path); w, h = im.size
    s = box / max(w, h)
    return slide.shapes.add_picture(path, Emu(int(cx - w*s/2)), Emu(int(cy - h*s/2)),
                                    Emu(int(w*s)), Emu(int(h*s)))


def add_text(slide, text, x, y, w, size=28, bold=True, center=True, hinterlegt=True):
    tb = slide.shapes.add_textbox(Emu(int(x)), Emu(int(y)), Emu(int(w)), Emu(500000))
    tf = tb.text_frame; tf.word_wrap = hinterlegt          # EA-Beschriftung (ohne Hintergrund) einzeilig
    p = tf.paragraphs[0]; p.text = text
    p.font.size = Pt(size); p.font.bold = bold
    if center:
        from pptx.enum.text import PP_ALIGN; p.alignment = PP_ALIGN.CENTER
    p.font.name = 'Arial'
    if hinterlegt:
        tb.fill.solid(); tb.fill.fore_color.rgb = RGBColor(0xFF, 0xFF, 0xFF); set_alpha(tb, 70)
    return tb


def pfeil(slide, x1, y1, x2, y2):
    """Schwarzer Pfeil (6 pt) vom Kästchen zum Ort, Spitze am Ort – wie im RV-Beispiel."""
    from pptx.enum.shapes import MSO_CONNECTOR
    c = slide.shapes.add_connector(MSO_CONNECTOR.STRAIGHT, Emu(int(x1)), Emu(int(y1)), Emu(int(x2)), Emu(int(y2)))
    c.line.color.rgb = RGBColor(0, 0, 0); c.line.width = Emu(76200)
    ln = c.line._get_or_add_ln()
    te = etree.SubElement(ln, qn('a:tailEnd')); te.set('type', 'triangle')
    return c


def polyline(slide, pts, closed):
    xy = [utm_to_emu(*p) for p in pts]
    fb = slide.shapes.build_freeform(xy[0][0], xy[0][1], scale=1.0)
    fb.add_line_segments(xy[1:], close=closed)
    return fb.convert_to_shape()


def main(tpl, symdir, lage_json, out):
    lage = json.load(open(lage_json, encoding='utf-8'))
    tmp = out + '.tmp.pptx'; potx_to_pptx(tpl, tmp)
    prs = Presentation(tmp); os.remove(tmp)
    s = prs.slides[0]
    mm = Emu(36000)
    box = int(lage.get('symbol_mm', 22) * 36000)

    # Kopf-/Fußtabelle (Tabelle 128: Zeile 0 = Bearbeiter, Zeile 1 = Takt. Zeit, Spalte 1)
    for sh in s.shapes:
        if sh.has_table and sh.name == 'Tabelle 128':
            for r, key in ((0, 'bearbeiter'), (1, 'takt_zeit')):
                cell = sh.table.cell(r, 1)
                cell.text = lage.get(key, '')
                cell.margin_left = cell.margin_right = Emu(0)
                cell.text_frame.word_wrap = False
                f = cell.text_frame.paragraphs[0].runs[0].font if cell.text_frame.paragraphs[0].runs else cell.text_frame.paragraphs[0].font
                f.size = Pt(13); f.bold = False; f.color.rgb = RGBColor(0, 0, 0)

    # Flächen zuerst (liegen unter den Zeichen)
    for fl in lage.get('flaechen', []):
        pts = [tuple(p) if 'utm' in fl else latlon_to_utm32(*p) for p in (fl.get('utm') or fl['latlon'])]
        shp = polyline(s, pts, True)
        if fl['typ'] == 'ea_grenze':
            shp.fill.background()
            shp.line.color.rgb = RGBColor(0, 0, 0); shp.line.width = Emu(76200)
            shp.line.dash_style = MSO_LINE_DASH_STYLE.LONG_DASH_DOT
            shp.line._get_or_add_ln().find(qn('a:prstDash')).set('val', 'lgDashDotDot')   # Strich-Zwei-Punkt wie RV-Beispiel
        else:
            shp.fill.solid(); shp.fill.fore_color.rgb = RGBColor.from_string(AREA[fl['typ']])
            set_alpha(shp, fl.get('deckkraft', 45))
            shp.line.color.rgb = RGBColor.from_string(AREA[fl['typ']]); shp.line.width = Pt(3)
        if fl.get('text'):
            top = max(pts, key=lambda p: p[1])      # Beschriftung über dem nördlichsten Punkt
            x, y = utm_to_emu(*top)
            if fl['typ'] == 'ea_grenze':            # wie RV-Beispiel: „EA 1: links der Saar“, 36 pt fett, ohne Hintergrund
                add_text(s, fl['text'], x - 75*mm, y - 22*mm, 150*mm, size=36, hinterlegt=False)
            else:
                add_text(s, fl['text'], x - 60*mm, y - 16*mm, 120*mm, size=24)

    for ln in lage.get('linien', []):
        pts = [tuple(p) if 'utm' in ln else latlon_to_utm32(*p) for p in (ln.get('utm') or ln['latlon'])]
        shp = polyline(s, pts, False); shp.fill.background()
        shp.line.color.rgb = RGBColor.from_string(LINE[ln['typ']]); shp.line.width = Emu(123825)

    used = []
    for sy in lage.get('symbole', []):
        path = os.path.join(symdir, sy['id'] + '.png')
        if not os.path.exists(path):
            raise FileNotFoundError(f"Zeichen {sy['id']} fehlt in {symdir} (siehe index.csv)")
        x, y = utm_to_emu(*to_utm(sy))
        size = int(sy.get('mm', lage.get('symbol_mm', 22)) * 36000)
        if lage.get('hinterlegung', True):       # weiße Hinterlegung, damit Linienzeichen lesbar bleiben
            from pptx.enum.shapes import MSO_SHAPE
            bg = s.shapes.add_shape(MSO_SHAPE.ROUNDED_RECTANGLE, Emu(int(x - size * 0.6)),
                                    Emu(int(y - size * 0.6)), Emu(int(size * 1.2)), Emu(int(size * 1.2)))
            bg.fill.solid(); bg.fill.fore_color.rgb = RGBColor(0xFF, 0xFF, 0xFF); set_alpha(bg, 85)
            bg.line.color.rgb = RGBColor(0x80, 0x80, 0x80); bg.line.width = Pt(1)
        add_picture_fit(s, path, x, y, size)
        if sy.get('text'):
            add_text(s, sy['text'], x - 50*mm, y + box/2 + 1*mm, 100*mm, size=24)
        if sy['id'] not in used: used.append(sy['id'])

    # Randkästchen nach RV-Beispiel „03a Lagekarte Übungsbeispiel“:
    # Zeichen groß in die Kästchen links/rechts, schwarzer Pfeil vom Kästchen zum Ort auf der Karte.
    # lage["kaestchen"] = [{"id": "EA_01", "utm": [E, N]}, {"id": "GEFAHR_03", "latlon": [..], "text": "optional"}]
    # Westlich der Kartenmitte -> linke Spalte, östlich -> rechte; Überlauf -> andere Spalte, dann obere Reihe.
    kaest = lage.get('kaestchen', [])
    phs = {p.placeholder_format.idx: p for p in s.placeholders if 'Bild' in p.name}
    cols = {'L': [phs[i] for i in (11, 17, 18, 19, 20, 21, 22) if i in phs],
            'R': [phs[i] for i in (34, 35, 36, 37, 38, 39, 40) if i in phs],
            'T': [phs[i] for i in range(24, 33) if i in phs]}
    E_MID = (MAP_E_MIN + MAP_E_MAX) / 2
    items = []
    for k in kaest:
        pt = utm_to_emu(*to_utm(k)) if ('utm' in k or 'latlon' in k) else None
        e = to_utm(k)[0] if pt else None
        items.append({**k, 'pt': pt, 'side': 'L' if (e is None or e < E_MID) else 'R'})
    for side, other in (('L', 'R'), ('R', 'L')):          # Überlauf: die der Mitte nächsten Orte wechseln
        mine = [i for i in items if i['side'] == side]
        mine.sort(key=lambda i: -(i['pt'][0] if i['pt'] else 0) if side == 'L' else (i['pt'][0] if i['pt'] else 0))
        for i in mine[:max(0, len(mine) - len(cols[side]))]:
            i['side'] = other if sum(1 for j in items if j['side'] == other) < len(cols[other]) else 'T'
    used_ph = []
    for side, boxes in cols.items():
        mine = [i for i in items if i['side'] == side]
        key = (lambda i: i['pt'][0] if i['pt'] else 0) if side == 'T' else (lambda i: i['pt'][1] if i['pt'] else 0)
        mine.sort(key=key)
        if len(mine) > len(boxes):
            print(f'WARNUNG: {len(mine) - len(boxes)} Kästchen ohne Platz – weggelassen')
            mine = mine[:len(boxes)]
        # reihenfolgetreu dem nächstgelegenen freien Kästchen zuordnen (keine sich kreuzenden Pfeile)
        start = 0
        for n, it in enumerate(mine):
            rest = len(mine) - n - 1
            cand = range(start, len(boxes) - rest)
            if it['pt']:
                c = (lambda b: b.left + b.width // 2) if side == 'T' else (lambda b: b.top + b.height // 2)
                v = it['pt'][0] if side == 'T' else it['pt'][1]
                bi = min(cand, key=lambda j: abs(c(boxes[j]) - v))
            else:
                bi = cand[0]
            start = bi + 1
            ph = boxes[bi]; used_ph.append(ph)
            path = os.path.join(symdir, it['id'] + '.png')
            if not os.path.exists(path):
                raise FileNotFoundError(f"Zeichen {it['id']} fehlt in {symdir}")
            hat_text = bool(it.get('text'))
            cy = ph.top + int(ph.height * (0.42 if hat_text else 0.5))
            add_picture_fit(s, path, ph.left + ph.width // 2, cy, int(ph.width * (0.62 if hat_text else 0.78)))
            if hat_text:
                add_text(s, it['text'], ph.left + 2*mm, ph.top + int(ph.height * 0.76),
                         ph.width - 4*mm, size=20, bold=False)
            if it['pt']:
                if side == 'L':   x1, y1 = ph.left + ph.width, ph.top + ph.height // 2
                elif side == 'R': x1, y1 = ph.left, ph.top + ph.height // 2
                else:             x1, y1 = ph.left + ph.width // 2, ph.top + ph.height
                pfeil(s, x1, y1, *it['pt'])
    for ph in used_ph:
        ph._element.getparent().remove(ph._element)

    prs.save(out)
    print('gespeichert:', out)


if __name__ == '__main__':
    main(*sys.argv[1:5])
