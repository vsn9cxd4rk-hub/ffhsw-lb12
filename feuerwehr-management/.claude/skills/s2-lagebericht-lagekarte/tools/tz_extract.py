#!/usr/bin/env python3
"""Schneidet die taktischen Zeichen aus der Hilfstabelle (RV Saarbrücken, Rev. 01.00)
als transparente PNGs aus. Aufruf: python3 tz_extract.py <hilfstabelle.pdf> <zielordner>
Ergebnis: <ID>.png (bei doppelter ID: <ID>_b.png) + index.csv (ID;Datei;Erläuterung)."""
import sys, os, re, csv, subprocess, tempfile
import pdfplumber
from PIL import Image

pdf_path, out = sys.argv[1], sys.argv[2]
os.makedirs(out, exist_ok=True)
DPI = 600
SCALE = DPI / 72
ID_RE = re.compile(r'^[A-ZÄÖÜ]+_\d{2}$')
rows, seen = [], {}

with pdfplumber.open(pdf_path) as pdf, tempfile.TemporaryDirectory() as tmp:
    for pno, page in enumerate(pdf.pages, start=1):
        words = page.extract_words(keep_blank_chars=False)
        ids = [w for w in words if ID_RE.match(w['text'])]
        if not ids:
            continue
        # Spalte "Takt. Zeichen": Kopf "Takt." -> Spaltengrenzen aus senkrechten Rahmenlinien
        borders = sorted({round(r['top'], 1) for r in page.rects
                          if r['height'] < 1.5 and r['x0'] < 240 and r['x1'] > 300})
        col_x0, col_x1 = 235.5, 308.5
        png = os.path.join(tmp, f'p{pno}')
        subprocess.run(['pdftoppm', '-r', str(DPI), '-f', str(pno), '-l', str(pno),
                        '-png', '-singlefile', pdf_path, png], check=True)
        img = Image.open(png + '.png').convert('RGBA')
        for w in ids:
            top = max([b for b in borders if b <= w['top']], default=w['top'] - 20)
            bot = min([b for b in borders if b > w['bottom']], default=w['bottom'] + 30)
            # Erläuterung = Wörter rechts der Symbolspalte innerhalb der Zeile
            expl = ' '.join(x['text'] for x in words
                            if x['x0'] > 310 and top < x['top'] < bot)
            box = [int(v * SCALE) for v in (col_x0, top + 1.5, col_x1, bot - 1.5)]
            cell = img.crop(box)
            # Weiß -> transparent, dann auf Inhalt beschneiden
            px = cell.load()
            for y in range(cell.height):
                for x in range(cell.width):
                    r, g, b, a = px[x, y]
                    if r > 245 and g > 245 and b > 245:
                        px[x, y] = (255, 255, 255, 0)
            bbox = cell.getbbox()
            if bbox:
                pad = 8
                cell = cell.crop((max(bbox[0]-pad, 0), max(bbox[1]-pad, 0),
                                  min(bbox[2]+pad, cell.width), min(bbox[3]+pad, cell.height)))
            sid = w['text']
            seen[sid] = seen.get(sid, 0) + 1
            fname = sid + ('' if seen[sid] == 1 else '_b') + '.png'
            cell.save(os.path.join(out, fname))
            rows.append((sid, fname, expl.strip()))

with open(os.path.join(out, 'index.csv'), 'w', newline='', encoding='utf-8') as f:
    csv.writer(f, delimiter=';').writerows([('ID', 'Datei', 'Erläuterung')] + rows)
print(f'{len(rows)} Zeichen nach {out} geschrieben')
