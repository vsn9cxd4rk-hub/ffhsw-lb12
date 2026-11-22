#!/usr/bin/env python3
"""Adresse in der Gemeinde Heusweiler -> UTM 32N (für lage.json der S2-Lagekarte).
Daten (Projekt s2-lage/geo/, OpenStreetMap © OSM-Mitwirkende, ODbL, Abruf 15.11.2026):
  strassen_heusweiler.csv      ortsteil;strasse;utm_e;utm_n;e_min;e_max;n_min;n_max;anz_adressen
  hnr_bereiche_heusweiler.csv  ortsteil;strasse;von-bis:E,N …  (E/N in 10 m, N minus 546000)
Aufruf:  python3 geo_lookup.py "Schillerstr. 67" [Ortsteil]
         python3 geo_lookup.py --datei liste.txt      (eine Adresse pro Zeile, optional ';Ortsteil')
Ausgabe: eingabe;ortsteil;strasse;hausnr;utm_e;utm_n;genauigkeit
Genauigkeit: HAUSNR (Bereich ≤60 m) | NAECHSTE_HNR x | STRASSENMITTE | NICHT_GEFUNDEN (+Vorschläge)
Hinweis: Ortsteil ist aus dem nächstgelegenen Ortsmittelpunkt abgeleitet (OSM führt in Heusweiler
keinen Ortsteil in der Adresse) – an Ortsteilgrenzen kann er abweichen (z. B. Kirschhof/Eiweiler)."""
import csv, os, re, sys, difflib

HERE = os.path.dirname(os.path.abspath(__file__))
def _pfad(name):
    for d in (HERE, os.path.join(HERE, '..', 'geo'), os.path.join(HERE, 'geo'), '.'):
        p = os.path.join(d, name)
        if os.path.exists(p): return p
    sys.exit(f'{name} nicht gefunden (neben dem Skript oder in ../geo/ ablegen)')

def norm(s):
    s = s.lower().strip().replace('ß', 'ss')
    s = re.sub(r'str\.?(?=\s|$)', 'strasse', s)
    s = re.sub(r'\bstr\.?\b', 'strasse', s)
    return re.sub(r'[^a-z0-9äöü]', '', s)

S = list(csv.DictReader(open(_pfad('strassen_heusweiler.csv'), encoding='utf-8'), delimiter=';'))
H = {}   # (ortsteil, strasse) -> [(von, bis, E, N)]
for z in open(_pfad('hnr_bereiche_heusweiler.csv'), encoding='utf-8'):
    if z.startswith('#') or not z.strip(): continue
    ot, st, rest = z.rstrip('\n').split(';', 2)
    L = []
    for t in rest.split():
        lab, xy = t.split(':'); e, n = xy.split(',')
        v, _, b = lab.partition('-')
        L.append((int(v), int(b or v), int(e) * 10, (int(n) + 546000) * 10))
    H[(ot, st)] = L
NN = {norm(s['strasse']): s['strasse'] for s in S}

def finde(text, ortsteil=None):
    m = re.match(r'^(.*?)[\s,]+(\d+)\s*([a-zA-Z]?)\s*$', text.strip())
    strasse, hn = (m.group(1), int(m.group(2))) if m else (text.strip(), None)
    hn_txt = (m.group(2) + m.group(3)) if m else ''
    q = norm(strasse); name = NN.get(q)
    if not name:
        starts = [n for k, n in NN.items() if k.startswith(q) or q.startswith(k)]
        name = starts[0] if len(starts) == 1 else None
    if not name:
        vor = difflib.get_close_matches(q, NN.keys(), n=4, cutoff=0.75)
        if len(vor) == 1: name = NN[vor[0]]
        else:
            return [(ortsteil or '', strasse, hn_txt, '', '',
                     'NICHT_GEFUNDEN' + (' Vorschläge: ' + ', '.join(NN[v] for v in vor) if vor else ''))]
    ss = [s for s in S if s['strasse'] == name]
    if ortsteil:
        ss = [s for s in ss if s['ortsteil'].lower().startswith(ortsteil.lower())] or ss
    erg = []
    for s in ss:
        L = H.get((s['ortsteil'], name), [])
        if hn is not None and L:
            treffer = [r for r in L if r[0] <= hn <= r[1] and r[0] % 2 == hn % 2]
            if treffer:
                r = treffer[0]; erg.append((s['ortsteil'], name, hn_txt, r[2], r[3], 'HAUSNR')); continue
            r = min(L, key=lambda r: (min(abs(hn - r[0]), abs(hn - r[1])), r[0] % 2 != hn % 2))
            erg.append((s['ortsteil'], name, hn_txt, r[2], r[3],
                        f"NAECHSTE_HNR {r[0]}" + (f"-{r[1]}" if r[1] != r[0] else ''))); continue
        erg.append((s['ortsteil'], name, hn_txt, s['utm_e'], s['utm_n'], 'STRASSENMITTE'))
    if len(erg) > 1:
        erg = [e[:5] + (e[5] + ' (MEHRDEUTIG – Ortsteil angeben)',) for e in erg]
    return erg

if __name__ == '__main__':
    if len(sys.argv) > 2 and sys.argv[1] == '--datei':
        zeilen = [z.strip() for z in open(sys.argv[2], encoding='utf-8') if z.strip()]
    else:
        zeilen = [';'.join(sys.argv[1:3])] if len(sys.argv) > 1 else []
    if not zeilen: sys.exit(__doc__)
    print('eingabe;ortsteil;strasse;hausnr;utm_e;utm_n;genauigkeit')
    for z in zeilen:
        t = z.split(';')
        for e in finde(t[0], t[1] if len(t) > 1 and t[1] else None):
            print(';'.join([t[0]] + [str(x) for x in e]))
