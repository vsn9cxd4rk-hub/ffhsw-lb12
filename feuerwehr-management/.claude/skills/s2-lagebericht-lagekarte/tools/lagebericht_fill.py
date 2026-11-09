#!/usr/bin/env python3
"""Lagebericht (Vorlage Lagebericht_Kommune_-_DDhhmmMMMJJ_V1_02.xltx) aus JSON befüllen.
Arbeitet direkt auf dem XML, damit Dropdowns (x14), bedingte Formatierung, Kommentare und
WMF-Zeichen erhalten bleiben. NICHT mit openpyxl speichern – das zerstört die Vorlage.
Aufruf: python3 lagebericht_fill.py <vorlage.xltx> <bericht.json> <ausgabe.xlsx>"""
import sys, json, re, zipfile
from xml.sax.saxutils import escape

SHEET = 'xl/worksheets/sheet1.xml'
SCHADEN_ROWS = {'verletzt': 5, 'verschuettet': 6, 'vermisst': 7, 'verstorben': 8, 'betreuung': 9,
                'tiere_betroffen': 10, 'tiere_verstorben': 11, 'gebaeude_beschaedigt': 12,
                'gebaeude_teilzerstoert': 13, 'gebaeude_zerstoert': 14}
KRAEFTE = {'alarmiert': 'B5', 'bereitschaft': 'B8', 'grundschutz': 'B13', 'einsatz': 'B15', 'ruhe': 'B25'}
STAERKE = {'gesamt': 'E31', 'fw': 'E33', 'kats_hiorg': 'E34', 'thw': 'E35', 'sonstige': 'E36'}
BEURT_ROWS = {'schadenslage': 39, 'fuehrung': 40, 'kraefte': 41, 'logistik': 42, 'kommunikation': 43}
BEURT_COL = {1: 'E', 2: 'K', 3: 'R'}          # 1 = geringfügig/keine, 2 = erheblich/kompensiert, 3 = kritisch
AUFGABEN = ['menschenrettung', 'evakuierung', 'brandschutz', 'technische_hilfe', 'cbrn', 'sanitaet',
            'betreuung', 'wasserrettung', 'psnv', 'sonstige']          # Zeilen 47..56
FREITEXT = {'probleme': 'A60', 'kurzfassung': 'A66', 'massnahmen': 'A74', 'sperrungen': 'A103',
            'presse': 'A107', 'bemerkungen': 'A111'}
LISTEN = {
    'prognose': {'Entspannung', 'Stagnation', 'Verschärfung'},
    'kritis': {'Wasser', 'Energie', 'Ernährung', 'Finanz-& Versicherungswesen', 'Gesundheit',
               'IT & Telekommunikation', 'Siedlungsabfallentsorgung', 'Medien & Kultur',
               'Staat & Verwaltung', 'Transport & Verkehr'},
    'org': {'FW', 'RD/eRD', 'THW', 'ASB', 'DLRG', 'DRK', 'JUH', 'MHD', 'NKS', 'BW'},
}

MONATE = 'jan feb mar apr may jun jul aug sep oct nov dec'.split()   # amerikanisch (Unterlegekarton)
TZ_RE = re.compile(r'^(\d{2})(\d{2})(\d{2})([A-Za-z]{3})(\d{2})$')
vals, red, warn = {}, set(), []


def check_tz(v, feld):
    m = TZ_RE.match(str(v))
    if not m or m.group(4).lower() not in MONATE or not (1 <= int(m.group(1)) <= 31) \
            or int(m.group(2)) > 23 or int(m.group(3)) > 59:
        raise ValueError(f'{feld}: "{v}" ist keine taktische Zeit DDhhmmMMMJJ mit amerikanischem Monat '
                         f'(z. B. 091400nov26)')


def plausi(b):
    st = b.get('staerke', {})
    teile = [st.get(k) for k in ('fw', 'kats_hiorg', 'thw', 'sonstige')]
    if isinstance(st.get('gesamt'), int) and all(isinstance(t, int) or t is None for t in teile):
        s = sum(t or 0 for t in teile)
        if s != st['gesamt']:
            warn.append(f"Stärke: gesamt {st['gesamt']} ≠ Summe der Teile {s}")
    for k, v in b.get('schaden', {}).items():
        if isinstance(v, list) and any(x < 0 for x in v):
            warn.append(f'Schaden {k}: negative Zahl')
    sw = [k for k, e in b.get('aufgaben', {}).items() if e.get('schwerpunkt')]
    if len(sw) > 1: warn.append(f'mehr als ein aufgabenbezogener Schwerpunkt: {sw}')
    sr = [e['nr'] for e in b.get('raeume', []) if e.get('schwerpunkt')]
    if len(sr) > 1: warn.append(f'mehr als ein raumbezogener Schwerpunkt: {sr}')
    krit = [k for k, e in b.get('beurteilung', {}).items()
            if e.get('stufe') == 3 or e.get('prognose') == 'Verschärfung']
    if krit and not b.get('probleme'):
        warn.append(f'Beurteilung kritisch/Verschärfung ({", ".join(krit)}) aber keine Probleme/Bedarfe')


def put(ref, v):
    vals[ref] = v


def build(b):
    put('D2', b.get('kommune', 'Gemeinde Heusweiler'))
    check_tz(b['zeit'], 'zeit'); plausi(b)
    put('R2', b['zeit']); put('AA2', b['lfd_nr'])
    for k, ref in KRAEFTE.items():
        if b.get('kraefte', {}).get(k): put(ref, b['kraefte'][k])
    for k, row in SCHADEN_ROWS.items():
        v = b.get('schaden', {}).get(k)
        if v is None: continue
        q, s = (v + [0, 0])[:2] if isinstance(v, list) else (0, v)   # [unbestätigt ?, bestätigt !]
        put(f'W{row}', q); put(f'Y{row}', s); put(f'AA{row}', ('=', q + s))
    if b.get('umwelt'): put('W15', b['umwelt'])
    for key, start in (('kritis_bedroht', 17), ('kritis_ausgefallen', 27)):
        for i, e in enumerate(b.get(key, [])[:5]):
            if e['sektor'] not in LISTEN['kritis']: raise ValueError(f"KRITIS-Sektor unbekannt: {e['sektor']}")
            put(f'W{start + 2*i}', e['sektor'])
            if e.get('text'): put(f'W{start + 2*i + 1}', e['text'])
    for k, ref in STAERKE.items():
        if b.get('staerke', {}).get(k): put(ref, b['staerke'][k])
    vp = b.get('verpflegung', {})
    if vp.get('portionen') is not None: put('L33', vp['portionen'])
    if vp.get('zeitpunkt'):
        check_tz(vp['zeitpunkt'], 'verpflegung.zeitpunkt'); put('L35', vp['zeitpunkt'])
    for k, row in BEURT_ROWS.items():
        e = b.get('beurteilung', {}).get(k)
        if not e: continue
        put(f"{BEURT_COL[e['stufe']]}{row}", 'X')
        if e.get('prognose'):
            if e['prognose'] not in LISTEN['prognose']: raise ValueError(f"Prognose unbekannt: {e['prognose']}")
            put(f'X{row}', e['prognose'])
    for i, k in enumerate(AUFGABEN):
        e = b.get('aufgaben', {}).get(k)
        if not e: continue
        row = 47 + i
        put(f'D{row}', 'X')
        if e.get('text'): put(f'I{row}', e['text'])
        if k == 'sonstige' and e.get('bezeichnung'): put(f'E{row}', e['bezeichnung'])
        if e.get('schwerpunkt'): red.update({f'D{row}', f'E{row}'})
    for e in b.get('raeume', [])[:10]:
        row = 46 + e['nr']
        put(f'S{row}', e['text'])
        if e.get('schwerpunkt'): red.update({f'R{row}', f'S{row}'})
    for k, ref in FREITEXT.items():
        if b.get(k): put(ref, b[k])
    ein = b.get('einheiten', [])
    if len(ein) > 36: raise ValueError('max. 36 Einheiten im Formular – Rest in Bemerkungen/Anlage')
    for i, e in enumerate(ein):
        row = 83 + (i % 18); c = ('A', 'D', 'I') if i < 18 else ('O', 'R', 'W')
        if e['org'] not in LISTEN['org']: raise ValueError(f"Organisation unbekannt: {e['org']}")
        put(f'{c[0]}{row}', e['org']); put(f'{c[1]}{row}', e['formation'])
        if e.get('bezeichnung'): put(f'{c[2]}{row}', e['bezeichnung'])


def cell_xml(ref, s_attr, v):
    if isinstance(v, tuple):                       # Formel behalten, Ergebnis vorbelegen
        return None
    if isinstance(v, (int, float)) and not isinstance(v, bool):
        return f'<c r="{ref}"{s_attr}><v>{v}</v></c>'
    t = escape(str(v))                            # Zeilenumbrüche bleiben (xml:space=preserve)
    return f'<c r="{ref}"{s_attr} t="inlineStr"><is><t xml:space="preserve">{t}</t></is></c>'


def patch_sheet(xml, styles_map):
    for ref, v in vals.items():
        m = re.search(rf'<c r="{ref}"(?P<a>[^>]*?)(?:/>|>(?P<b>.*?)</c>)', xml, re.S)
        if not m: raise KeyError(f'Zelle {ref} nicht in Vorlage')
        attrs = m.group('a')
        sm = re.search(r' s="(\d+)"', attrs)
        s_idx = sm.group(1) if sm else None
        if ref in red and s_idx is not None: s_idx = styles_map[s_idx]
        s_attr = f' s="{s_idx}"' if s_idx is not None else ''
        if isinstance(v, tuple):                   # Summenformel AA: gecachten Wert setzen
            f = re.search(r'<f[^>]*?(?:/>|>.*?</f>)', m.group('b') or '').group(0)
            new = f'<c r="{ref}"{s_attr}>{f}<v>{v[1]}</v></c>'
        else:
            new = cell_xml(ref, s_attr, v)
        xml = xml[:m.start()] + new + xml[m.end():]
    for ref in red - set(vals):                    # nur einfärben
        m = re.search(rf'<c r="{ref}"([^>]*?) s="(\d+)"', xml)
        xml = xml[:m.start(2)] + styles_map[m.group(2)] + xml[m.end(2):]
    return xml


def patch_styles(styles, sheet):
    """Für jede rot zu markierende Zelle eine Kopie ihres Zellformats mit roter Füllung anlegen."""
    fills = re.search(r'<fills count="(\d+)">(.*?)</fills>', styles, re.S)
    red_fill_id = int(fills.group(1))
    new_fills = (f'<fills count="{red_fill_id + 1}">{fills.group(2)}'
                 '<fill><patternFill patternType="solid"><fgColor rgb="FFFF0000"/>'
                 '<bgColor indexed="64"/></patternFill></fill></fills>')
    styles = styles[:fills.start()] + new_fills + styles[fills.end():]
    fonts = re.search(r'<fonts count="(\d+)"([^>]*)>(.*?)</fonts>', styles, re.S)
    font_list = re.findall(r'<font>.*?</font>|<font/>', fonts.group(3), re.S)
    font_map, new_fonts = {}, []
    xfs = re.search(r'<cellXfs count="(\d+)">(.*?)</cellXfs>', styles, re.S)
    xf_list = re.findall(r'<xf [^>]*?(?:/>|>.*?</xf>)', xfs.group(2), re.S)
    mapping, extra = {}, []
    for ref in red:
        s_idx = re.search(rf'<c r="{ref}"[^>]*? s="(\d+)"', sheet).group(1)
        if s_idx in mapping: continue
        xf = xf_list[int(s_idx)]
        xf = re.sub(r'fillId="\d+"', f'fillId="{red_fill_id}"', xf)
        fid = re.search(r'fontId="(\d+)"', xf).group(1)
        if fid not in font_map:                    # gleiche Schrift, aber weiß + fett
            f = font_list[int(fid)]
            f = re.sub(r'<color [^>]*/>', '', f).replace('<b/>', '')
            f = f.replace('<font>', '<font><b/><color rgb="FFFFFFFF"/>', 1)
            font_map[fid] = str(len(font_list) + len(new_fonts)); new_fonts.append(f)
        xf = re.sub(r'fontId="\d+"', f'fontId="{font_map[fid]}"', xf)
        xf = xf.replace('<xf ', '<xf applyFont="1" ', 1) if 'applyFont' not in xf else \
            re.sub(r'applyFont="\d"', 'applyFont="1"', xf)
        xf = xf.replace('<xf ', '<xf applyFill="1" ', 1) if 'applyFill' not in xf else \
            re.sub(r'applyFill="\d"', 'applyFill="1"', xf)
        mapping[s_idx] = str(len(xf_list) + len(extra)); extra.append(xf)
    n = len(xf_list) + len(extra)
    nf = len(font_list) + len(new_fonts)
    styles = styles[:fonts.start()] + f'<fonts count="{nf}"{fonts.group(2)}>{fonts.group(3)}{"".join(new_fonts)}</fonts>' + styles[fonts.end():]
    xfs = re.search(r'<cellXfs count="(\d+)">(.*?)</cellXfs>', styles, re.S)
    styles = styles[:xfs.start()] + f'<cellXfs count="{n}">{xfs.group(2)}{"".join(extra)}</cellXfs>' + styles[xfs.end():]
    return styles, mapping


def main(tpl, js, out):
    build(json.load(open(js, encoding='utf-8')))
    with zipfile.ZipFile(tpl) as zi, zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED) as zo:
        sheet = zi.read(SHEET).decode('utf-8')
        styles, mapping = patch_styles(zi.read('xl/styles.xml').decode('utf-8'), sheet) if red else (None, {})
        for i in zi.infolist():
            d = zi.read(i.filename)
            if i.filename == SHEET:
                d = patch_sheet(sheet, mapping).encode('utf-8')
            elif i.filename == 'xl/styles.xml' and styles:
                d = styles.encode('utf-8')
            elif i.filename == '[Content_Types].xml':
                d = d.replace(b'spreadsheetml.template.main+xml', b'spreadsheetml.sheet.main+xml')
            elif i.filename == 'xl/workbook.xml':
                d = re.sub(rb'<calcPr ([^/]*)/>', rb'<calcPr \1 fullCalcOnLoad="1"/>', d)
            zo.writestr(i, d)
    print('gespeichert:', out, f'({len(vals)} Felder, {len(red)} rot markiert)')
    for w in warn:
        print('PRÜFEN:', w)


if __name__ == '__main__':
    main(*sys.argv[1:4])
