---
name: "s2-lagebericht-lagekarte"
description: "S2 im Führungsstab FF Heusweiler – Lagebericht (Excel-Vorlage RV Saarbrücken) und Lagekarte Heusweiler (PowerPoint-Vorlage) mit taktischen Zeichen erstellen und stündlich fortschreiben."
---

# S2 Lagebericht & Lagekarte – Gemeinde Heusweiler (Führungsstufe D, RV Saarbrücken)

Nutzer: S2 im Führungsstab der FF Heusweiler. Aufgabe bei Großschadenslagen: Lagebericht (xlsx) + Lagekarte (pptx/PDF) für die Führungseinrichtung der Gemeinde (TEL) an den Führungsstab RV Saarbrücken. Antworten auf Deutsch, knapp, im Stabsjargon (TEL, EA, BR, MK, KRITIS, Stärke).

## 0. Festlegungen Gemeinde Heusweiler (Stand 13.11.2026) – haben Vorrang
- **Priorität 1 ist der interne Überblick** der TEL: analoge Lagekarte an der Wand und Lagedarstellung mit Kräfteübersicht am Whiteboard. Der Skill unterstützt, ersetzt das nicht und darf es nicht aufhalten.
- **Priorität 2:** Lagebericht + Lagekarte **stündlich** als E-Mail-Anhang an den Stab RV. Detailtiefe zweitrangig – der RV-Stab liest nicht im Detail. Lieber pünktlich und knapp als vollständig und zu spät.
- **Fallback (laut RV-Vorgabe zulässig):** Reicht die Zeit für die PPT-Lagekarte nicht, wird ein **Foto der analog geführten Karte** versendet. Dann nur Lagebericht erzeugen und Mailentwurf mit Hinweis „Lagekarte als Foto“ vorbereiten.
- **Stärke:** im Lagebericht **nur die Gesamtzahl** (E31). Teilstärken FW/KatS/THW/Sonstige bleiben leer.
- **Alte Vorlage 02a** wird ignoriert; nur V1_02 verwenden.
- **Randkästchen der Lagekarte:** Darstellung der **Einsatzabschnitte und Gefahren** mit Kurztext (Vorbild: RV-Beispiel „03a Lagekarte Übungsbeispiel“ – falls im Projekt vorhanden, vorher ansehen und Stil übernehmen).
- **Fehler in den RV-Unterlagen** (fehlende/doppelte Zeichen, Tippfehler, Beispiel-Rechenfehler) werden **nicht jetzt** geklärt, sondern in die Nachbereitung aufgenommen (Projektdoc „S2-Lage – offene Punkte an RV“). Im Einsatz/in der Übung nicht darüber diskutieren, sondern umgehen (Text/MARKIERUNG statt fehlendem Zeichen).
- **Datenschutz** (Übernahme von SecurCAD-Daten in KI) wird im Nachgang über die Wehrführung geklärt. Bis dahin nur Übungsdaten bzw. keine personenbezogenen Daten (keine Namen von Betroffenen) verarbeiten.

## 1. Rahmen (Schulung „Lageberichts- und Meldewesen FüSt D“, H. Neubert, StA 37, 27.08.2026)
- Aktivierung durch die untere KatS-Behörde. Danach unverzüglich 1. Lagebericht + Lagekarte, dann **stündlich zur vollen Stunde** (sofern nichts anderes bestimmt) per E-Mail an **fuehrungsstab@saarbruecken.de**.
- Lagekarte ist **jedem** Lagebericht als Anlage beizufügen (PPT-Vorlage als PDF, Foto der Handkarte oder FüUSoftware-Ausdruck).
- Inhalt Lagekarte, ebenengerecht: räumliche Gliederung (EA, Einsatzschwerpunkte, BR, Meldeköpfe, Verpflegungs-/Betreuungsstellen), Gefahren-/Schadenslage, Maßnahmen. **Keine Kräfte/Einheiten auf der Karte** – Kräfte stehen im Lagebericht (Summen), Details beim S1.
- Grundregeln (Vortrag Lagedarstellung): Zeichen nach SKK DV 102, keine eigenen Abkürzungen; Karte nicht zupflastern – Details in Schadenskonten/Bericht; vom Platz des Einsatzleiters muss die Übersicht erkennbar sein.
- Leitungsprinzip: nur melden, was auf Ebene TEL **nicht** gelöst werden kann (Probleme/Bedarfe max. 3). Konkrete Nachforderungen zusätzlich immer per Funk/Draht.
- Datenquellen des S2: (a) Nachrichtenvordrucke (4-fach-Vordruck; Ausfertigung „S2/Lagekarte“ kommt vom Sichter mit Nachweisungs-Nr.), (b) Schadenskonten der **Taktischen Arbeitswand RVSB V1.1** (je EA, BR, Grundschutz: Stärke, Auftrag, Lagebeurteilung, Prognose, Probleme/Bedarfe), (c) Kräfteübersicht des S1 („Kräfteübersicht mit Zeiten“), (d) Meldungen aus Einsatzleitsoftware (z. B. SecurCAD) – nur wenn dienstlich/datenschutzrechtlich freigegeben (Personendaten).
- Referenz für Inhalt und Stil: Projektdatei „02b Lagebericht Übungsbeispiel.xlsx“ (RV, 27.08.2026). Achtung: älteres Layout; dort Summenfehler Stärke (142 ≠ 110+18+22).

## 2. Arbeitsablauf pro Bericht
1. **Material finden** (in dieser Reihenfolge): Ordner dieses Skills (`vorlagen/`, `tools/`, `zeichen/` neben dieser SKILL.md, im Repo unter `feuerwehr-management/.claude/skills/s2-lagebericht-lagekarte/`) → verbundener Ordner `…/feuerwehr-management/s2-lage/` → angehängte Dateien → Projektdateien. Vorlagen: `Lagebericht Kommune - DDhhmmMMMJJ V1_02.xltx` (Stand 31.08.2026, maßgeblich; „02a Lagebericht Vorlage.xltx“ ist der ältere Stand vom 25.08. – nicht verwenden) und `Lagekarte_Heusweiler_V1-2.potx`; beide liegen als Projektdateien (`project_read(original=true)`). Fehlen sie, danach fragen – nicht nachbauen.
2. **Skripte**: liegen in `tools/` des Skill-Ordners (bzw. `s2-lage/tools/`). Fehlen sie, die drei Skripte aus Abschnitt 8 unverändert in ein eigenes Arbeitsverzeichnis schreiben. Abhängigkeiten: `pip install python-pptx pdfplumber pillow --break-system-packages`, `pdftoppm`, `soffice`. **Auf dem Mac** die Skripte mit dem venv-Interpreter starten, falls vorhanden: `~/.venvs/s2/bin/python tools/…` (siehe README); sonst `python3`. LibreOffice liegt dort ggf. unter `/Applications/LibreOffice.app/Contents/MacOS/soffice`.
3. **Zeichenbibliothek**: `zeichen/` des Skill-Ordners (bzw. `s2-lage/zeichen/`) (PNG je ID + `index.csv`). Fehlt sie: Projektdatei „03b Übersicht Taktische Zeichen für Lagekarte.pdf“ mit `project_read(original=true)` holen und `python3 tz_extract.py <pdf> zeichen/` ausführen (97 Zeichen, ~20 s). BRAND_05 (Ankerpunkt) ist im PDF leer: Projektdatei „BRAND_05 - Ankerpunkt.SVG“ lesen, weiße Hintergrund-`rect` entfernen, mit `rsvg-convert -w 1300` rendern, auf Inhalt beschneiden, als `zeichen/BRAND_05.png` ablegen. Alle Hilfstabellen-Zeichen liegen zusätzlich als Original-SVG im Projekt (`<ID> - <Erläuterung>.SVG`).
4. **Lage aufnehmen**: Freitext/Meldungen/Funkprotokoll/Nachrichtenvordrucke des Nutzers in die JSON-Strukturen (Abschnitt 5/6) übersetzen. Je Meldung festhalten: Nachweisungs-Nr., takt. Zeit, Absender, Ort, Inhalt. Mehrere Meldungen zur selben Schadenstelle zusammenführen (nicht doppelt zählen); bestätigt (!) / unbestätigt (?) aus dem Wortlaut, im Zweifel ?. Was nur die Führung entscheidet (Lagebeurteilung, Prognose, EA-Gliederung, Schwerpunkte, Probleme/Bedarfe) **nicht** aus Meldungen ableiten, sondern beim Nutzer abfragen bzw. aus den Schadenskonten übernehmen. Vorherigen Bericht (falls vorhanden) als Basis nehmen: `lfd_nr` +1, Werte übernehmen, nur Änderungen einarbeiten, Änderungen dem Nutzer kurz auflisten.
5. **Plausibilität prüfen (Abschnitt 7)** – fehlende Angaben **nie erfinden**, sondern leer lassen (Platzhalter bleiben grau stehen).
   **Schnellmodus (Standard im Einsatz/in der Übung):** höchstens **eine** gebündelte Rückfrage pro Bericht, dann erzeugen. Pflicht sind nur: Zeit, lfd. Nr., Kurzfassung, Lagebeurteilung + Prognose, Probleme/Bedarfe, Stärke gesamt. Alles andere aus dem Vorbericht übernehmen oder leer lassen. Fehlende Punkte nach der Ausgabe in einer Zeile nennen. Ziel: Dateien spätestens 5 Minuten vor der vollen Stunde fertig.
   **Fotos als Eingang:** Fotos der analogen Lagekarte oder des Whiteboards (Kräfteübersicht) dürfen als Quelle dienen. Abgelesene Werte (Einheiten, Stärken, EA, Schäden) als Liste zurückgeben und bestätigen lassen, bevor sie in den Bericht gehen – Handschrift wird leicht falsch gelesen.
6. Erzeugen: `python3 lagebericht_fill.py <xltx> bericht.json Lagebericht_Heusweiler_<Zeit>_Nr<lfd>.xlsx` und `python3 lagekarte_build.py <potx> zeichen/ lage.json Lagekarte_Heusweiler_<Zeit>.pptx`.
7. **Prüfen**: beide mit `soffice --headless --convert-to pdf` rendern, `pdftoppm -r 50` ansehen (Lagebericht 2 Seiten, Lagekarte nur Folie 1). Kontrolle: Felder an richtiger Stelle, Zeichen auf richtigem Ort, nichts abgeschnitten.
8. Ausgeben: xlsx + Lagekarte als **PDF nur Folie 1** (`pdfseparate -f 1 -l 1`; Folie 2 ist die Zeichenpalette der Vorlage) + pptx zum Weiterarbeiten. Dateinamen nach Muster der Vorlage: `Lagebericht_Heusweiler_DDhhmmMMMJJ_V<lfd>`. Mail-Entwurf an fuehrungsstab@saarbruecken.de anbieten (Betreff „Lagebericht Gemeinde Heusweiler Nr. X – DDhhmmMMMJJ“; in Übungen die von der Übungsleitung genannte Adresse verwenden, falls abweichend). Nicht selbst versenden. Bei Foto-Fallback im Mailtext vermerken: „Lagekarte als Foto der analog geführten Karte“.
9. **Nachbereitung mitschreiben:** Auffälligkeiten an Vorlagen, Zeichen und Ablauf (z. B. fehlendes Zeichen, Feld passt nicht, Zeitdruck) knapp sammeln und nach der Lage als Liste ausgeben; im Projekt an „S2-Lage – offene Punkte an RV“ anhängen, wenn der Nutzer zustimmt.

**Nie** die xltx mit openpyxl speichern: openpyxl verwirft die x14-Dropdowns, bedingten Formatierungen und WMF-Zeichen. `lagebericht_fill.py` patcht das XML direkt – nur dieses Skript verwenden.

## 3. Taktische Zeit
Format `DDhhmmMMMJJ`, Monat in **amerikanischer Schreibweise, erste 3 Buchstaben** (Unterlegekarton zum Nachrichtenvordruck): jan feb mar apr may jun jul aug sep oct nov dec. Klein wie im RV-Übungsbeispiel (`271900aug26`); die Vorlage zeigt groß (`011200APR25`) – beides gültig. Gilt auch für den Verpflegungs-Zeitpunkt (L35). Zeit = Erstellungszeitpunkt des Berichts, Ortszeit. `lagebericht_fill.py` lehnt ungültige Zeiten (z. B. `okt`, `dez`) ab.

## 4. Taktische Zeichen (Hilfstabelle „Taktische Zeichen“, Lagedarstellungssystem RV Saarbrücken, Rev. 01.00, Stand 28.08.2026)
IDs = Dateinamen in `zeichen/`. Für die Lagekarte NUR diese IDs verwenden.
- **EA_01…EA_10**: Einsatzabschnitt 1–10 (gelber Kreis mit Ziffer). Nummer = Nr. im Lagebericht „raumbezogen ①–⑩“.
- **MARKIERUNG_01…10**: markante Stelle 1–10 (orangene Fahne mit Ziffer) – Erläuterung in Kurzfassung/Bemerkungen.
- **STELLE_**: 01 Führungsstelle · 02 Bereitstellungsraum · 03 BR mit Meldekopf · 04 Lotsenstelle/Abholpunkt · 05 Hubschrauberlandezone · 06 Anlegestelle Boote · 07 Stelle mit IuK-Verbindung · 08 Mobile TETRA-Basisstation · 09 Sammelstelle Spontanhelfer · 10 Verpflegungsstelle · 11 Verpflegungszubereitungsstelle · 12 Versorgungsstelle Betriebsstoffe/Verbrauchsgüter · 13 Instandhaltung/Instandsetzung · 14 Wasserentnahmestelle · 15 Sandsackbefüllung · 16 Trinkwasserausgabestelle · 17 Sanitäre Einrichtung Dusch/Wasch · 18 Toiletten · 19 Entsorgungsstelle · 20 Patientenablage · 21 Behandlungsplatz · 22 Ladezone · 23 Rettungsmittelhalteplatz · 24 Anlaufstelle Betroffene · 25 Betreuungsstelle · 26 Notunterkunft · 27 Notfallinformationspunkt · 28 Krankenhaus · 29 Pflegeeinrichtung* · 30 Bildungseinrichtung*
- **GEFAHR_**: 01 A-Gefahren · 02 B-Gefahren · 03 C-Gefahren · `GEFAHR_03_b` Gefahr durch Elektrizität (in der Quelle doppelt als GEFAHR_03 geführt)
- **BRAND_**: 01 Entstehungsbrand · 02 Fortentwickelter Brand · 03 Vollbrand · 04 Spotfeuer · 05 Ankerpunkt · 06 Safety-/Sicherheitszone
- **ZERSTÖRUNG_**: 01 angeschlagen/beschädigt · 02 teilzerstört · 03 total zerstört
- **BEFAHRBARKEIT_**: 01 gesperrt · 02 Einbahnstraßenregelung · 03 schwierig befahrbar/teilblockiert · 04 nicht befahrbar/blockiert
- **SITUATION_**: 01 akut/bestätigt (!) · 02 Vermutung/unbestätigt (?)
- **TENDENZ_**: 01 steigend/ausdehnend · 02 gleichbleibend · 03 fallend/abnehmend (neben Schadenszeichen setzen)
- **MASSNAHME_**: 01 Erkunden · 02 Einsatz Drehflügler · 03 Warnen Sirene · 04 Warnen Lautsprecher · 05 Räumen* · 06 Evakuieren* · 07 Brandbekämpfung · 08 Luftgestützte Brandbekämpfung · 09 Löschwasserförderung · 10 Löschwasserförderung mit Fahrzeugen · 11 Technische Hilfe · 12 Räumen mit Maschine · 13 Baggerarbeiten · 14 Motorsägenarbeiten · 15 Handwerkzeuge · 16 Stromversorgung · 17 Pumpen/Lenzen · 18 Sandsackverbau · 19 Abwehr Wassergefahren/Deichverteidigung · 20 Wasserrettung · 21 CBRN-Abwehr · 22 Messen/Spüren/Detektieren · 23 Dekontaminieren · 24 Umweltschäden auf Gewässern · 25 Entschärfen/Kampfmittelräumung

\* = **weder im PDF noch als SVG vorhanden** (Hilfstabelle Rev. 01.00, SVG-Satz Stand 09.11.2026). Nicht verwenden; Nutzer informieren und als Text/MARKIERUNG darstellen, bis der RV nachliefert. Das BBK-Werk „Taktische Zeichen im Bevölkerungsschutz“ (Projektdatei + SVGs 1.x–N.x) dient nur als Nachschlagewerk, nicht als Ersatz auf der FüSt-D-Karte.

**Flächen** (Farben aus Folie 2 der Vorlage, halbtransparent): `ea_grenze` schwarz strichpunktiert · `brand` rot E4221B · `kontaminiert` gelb FFED00 · `ueberschwemmt` blau 0090D6 · `versorgung` (Einschränkung/Ausfall Versorgung) magenta CD5FA0 · `duerre` braun 9A6732 · `sonstiges` orange F49B19.
**Linien**: `rettungsweg` grün 00B050 · `riegelstellung` blau 0070C0 · `brandausbreitung` rot FF0000.

**Hintergrundwissen DV 102 (Vortrag Lagedarstellung)** – zum Erklären/Prüfen, nicht für die Karte FüSt D:
Grundzeichen: Gebäude, Person, Stelle/Einrichtung (Kreis), Führungsstelle, taktische Formation (Rechteck mit Fähnchen), Fahrzeug, Schaden (Dreieck Spitze oben), Maßnahme (Dreieck Spitze unten, blau), Gebiet/Fläche. Organisationsfarben: rot FW · blau THW · weiß HiOrg (+Kürzel, z. B. MHD) · grün Polizei/BPol · braun Bundeswehr · gelb Führung · orange sonstige Gefahrenabwehr · grau zivile Kräfte. Fachaufgaben-Symbole: Verteiler = Brandbekämpfung, Spreizer = TH, Retorte = ABC, Kreuz = San/RD, Dach = Betreuung, Balken unten = Logistik/Versorgung, Ochsenkopf = Veterinär, Überordnung = Führung. Größenordnung: mehr Symbole = größer; Bereitschaft 2–5 Züge, Abteilung 2–5 Bereitschaften, Großverband 2–5 Abteilungen. Schadensränder: Personen schwarz, allgemein schwarz/rot, Wasser blau. Führungsstufen: A Gruppe (taktisches Arbeitsblatt) · B Zug (Arbeitsblatt/-tafel) · C Verband/Führungsgruppe (Arbeitstafel/-wand) · D Stab (taktische Arbeitswand).

## 5. Lagekarte – `lage.json`
Vorlage: A0 quer, Folie 1 = Karte Heusweiler (LVGL 2026, UTM-Gitter 1 km), 25 Randfelder „Taktische Zeichen“, Fußzeile Bearbeiter/Takt. Zeit. Folie 2 = Palette (EA-Beschriftung, Markierungen, Flächen, Linien).
Kartenausschnitt (ETRS89/UTM 32N): **E 345000–356000, N 5464000–5472000**. Koordinaten außerhalb → Skript bricht ab; Ort dann als Text in den Bericht. Ortsangaben des Nutzers (Straße, Objekt) in Koordinaten übersetzen: per Websuche/Geodienst Lat/Lon ermitteln (`latlon`), sonst Nutzer nach Gitterfeld/Koordinate fragen. Keine Position raten – geratene Lage auf der Karte ist schlimmer als keine.
```json
{"bearbeiter":"S2 <Name>","takt_zeit":"081300NOV26","symbol_mm":22,
 "symbole":[{"id":"STELLE_03","utm":[348300,5467900],"text":"BR Sportplatz"},
            {"id":"BRAND_02","latlon":[49.3410,6.9310]}],
 "flaechen":[{"typ":"ueberschwemmt","utm":[[E,N],[E,N],[E,N]],"text":"Köllerbach"},
             {"typ":"ea_grenze","utm":[[E,N],...]}],
 "linien":[{"typ":"rettungsweg","utm":[[E,N],[E,N]]}],
 "kaestchen":[{"id":"EA_01","text":"EA 1 Dilsburg: Überflutung, Pumpen im Einsatz"},
              {"id":"GEFAHR_03","text":"C-Gefahr: Chloraustritt Freibad"}]}
```
**Randkästchen (`kaestchen`, max. 25):** Einsatzabschnitte (EA_xx) zuerst, dann Gefahren/Schäden, je mit Kurztext (EA-Name + Lage in einer Zeile, ≤ 12 Wörter; Schwerpunkt-EA mit „(Schwerpunkt)“). Fehlt `kaestchen`, nimmt das Skript automatisch alle verwendeten EA_-Zeichen und danach GEFAHR_/BRAND_/ZERSTÖRUNG_/BEFAHRBARKEIT_/SITUATION_-Zeichen mit ihrer Standarderläuterung. Stellen und Maßnahmen kommen nicht in die Kästchen. `hinterlegung` (Standard true) legt jedes Kartenzeichen auf ein weißes Feld, damit dünne Linienzeichen (Brand, Pumpen, gesperrt) auf der Karte lesbar bleiben. Flächentexte stehen über dem nördlichsten Punkt der Fläche. Im Render auf überlappende Texte prüfen und Texte kürzen bzw. Positionen leicht versetzen. Texte an Zeichen kurz halten (≤ 3 Wörter). EA-Zeichen in jede EA-Fläche setzen, Nummern identisch mit Bericht.

## 6. Lagebericht – `bericht.json` (Zellen der Vorlage „Lagebericht Gde > GdeVerb“)
| Schlüssel | Zelle(n) | Regel |
|---|---|---|
| kommune | D2 | exakt „Gemeinde Heusweiler“ |
| zeit, lfd_nr | R2, AA2 | taktische Zeit; fortlaufend ab 1. versendetem Bericht |
| kraefte.alarmiert / bereitschaft / grundschutz / einsatz / ruhe | B5 / B8 / B13 / B15 / B25 | nur **taktische Einheiten** (LB, OV, Fachzüge/-gruppen, Spezialressourcen wie Bergekran), keine Einzelfahrzeuge; „grundschutz“ = davon im BR für Grundschutz zurückgehalten. Je Organisation eine Zeile mit Anzahl + Formation (z. B. „FW: LB 111, 112, 115“ / „HiOrg: DRK …“ / „THW: OV SB (1 TZ)“); leerer Zustand = „./.“ |
| schaden.<art> = [?, !] | W/Y 5–14, Σ in AA | `verletzt, verschuettet, vermisst, verstorben, betreuung, tiere_betroffen, tiere_verstorben, gebaeude_beschaedigt, gebaeude_teilzerstoert, gebaeude_zerstoert`. ? = unbestätigt, ! = gesichert, **nie doppelt zählen**. Momentaufnahme (entlassene/gerettete/aufgefundene raus), außer verstorben = Gesamtzahl. Tiere nur Großvieh. Gebäude: beschädigt = wieder nutzbar, teilzerstört = vorerst nicht, zerstört = nicht mehr nutzbar |
| umwelt | W15 | Schadstoffaustritt, Gewässer, Schutzgebiete |
| kritis_bedroht / kritis_ausgefallen | W17–W26 / W27–W36 | je max. 5 `{"sektor","text"}`; Sektor exakt: Wasser, Energie, Ernährung, Finanz-& Versicherungswesen, Gesundheit, IT & Telekommunikation, Siedlungsabfallentsorgung, Medien & Kultur, Staat & Verwaltung, Transport & Verkehr |
| staerke.gesamt | E31 | **Nur die Gesamtzahl** (Festlegung Gemeinde): Personen aller nichtpolizeilichen Kräfte inkl. RD als Ganzzahl. E33–E36 (FW, KatS/HiOrg, THW, Sonstige) bleiben leer; Teilstärken werden vom Skript ignoriert. Von der Arbeitswand (VF/ZF/GF/M//Ges) nur „Ges“ übernehmen |
| verpflegung.portionen / zeitpunkt | L33 / L35 | nur extern (durch Führungsstab/S4) zu organisierende Portionen; Zeitpunkt als taktische Zeit (z. B. 262200aug26) |
| beurteilung.<bereich> = {"stufe":1-3,"prognose"} | E/K/R 39–43, X39–43 | Bereiche schadenslage, fuehrung, kraefte, logistik, kommunikation. Stufe 1 geringfügig/keine Probleme · 2 erheblich/kompensierte Probleme · 3 kritisch/nicht kompensierbar. Prognose exakt: Entspannung, Stagnation, Verschärfung |
| aufgaben.<art> = {"text","schwerpunkt"} | D/I 47–56 | menschenrettung, evakuierung, brandschutz, technische_hilfe, cbrn, sanitaet, betreuung, wasserrettung, psnv, sonstige(+bezeichnung). TH-Text nach Möglichkeit aus Liste: Bergen/Räumen, Wasserschaden / Pumpen, Notversorgung / -instandsetzung, Trinkwassernotversorgung, Infrastruktur. **Genau ein** Schwerpunkt → rot |
| raeume = [{"nr","text","schwerpunkt"}] | S47–S56 | EA der Lagekarte, nr = EA-Nummer; Text = Bezeichnung des EA ohne Präfix (z. B. „rechts der Saar (St. Johanner Seite)“); Schwerpunkt rot |
| probleme | A60 | max. 3, nur was TEL nicht selbst lösen kann |
| kurzfassung | A66 | Zeile „Allgemein: …“, dann je EA eine Zeile „EA <Name>: …“; Inhalt: Gefährdung Menschen, KRITIS, erhebliche Sachwerte, Umwelt, absehbare Verschärfung |
| massnahmen | A74 | Menschenrettung, Evakuierung/Warnung, Aufrechterhaltung KRITIS |
| einheiten = [{"org","formation","bezeichnung"}] | A/D/I 83–100, dann O/R/W | max. 36. org exakt: FW, RD/eRD, THW, ASB, DLRG, DRK, JUH, MHD, NKS, BW. formation exakt aus Liste: Löschbezirk, FZ Führungsunterstützung, FZ Einsatzzug, FZ Waldbrand, FZ Wasserversorgung, FZ Hochwasserschutz, Drohneneinheit, ABC-Zug (gesamt), ABC-Zug (nur Messkomponente), ABC-Zug (nur Dekonkomponete), PtGr 5 (Patiententransportgruppe), FZ BHP 25 (Behandlungsplatz), FZ BHP 50 (Behandlungsplatz), FZ BP 250 (Betreuungsplatz), FG Verpf 250 (Verpflegungsgruppe), WRZ (Wasserrettungszug), WRGr (Wasserrettungsgruppe), TZ (Technischer Zug), FZ FüKom (Führung und Kommunikation), FZ Log (Logistik), FGr B (Bergung), FGr BrB (Brückenbau), FGr BT (Bergungstauchen), FGr E (Elektroversorgung), FGr I (Infrastruktur), FGr N (Notversorgung und Notinstandsetzung), FGr Öl (Ölschaden), FGr O (Ortung), FGr R (Räumen), FGr SB (Schwere Bergung), FGr Sp (Sprengen), FGr T (Trinkwasserversorgung), FGr W (Wassergefahren), FGr WP (Wasserschaden/Pumpen) |
| sperrungen / presse / bemerkungen | A103 / A107 / A111 | BAB, B-/L-Straßen, Bahn, abgeschnittene Ortsteile / überregionale Presse, Abstimmung mit BuMA / Rest |

Heusweiler-Löschbezirke laufen im RV-Schema unter LB 111–122 (verdecktes Blatt „Ressourcen“).

## 7. Plausibilitätsprüfung vor Ausgabe (kritisch, nicht beschönigen)
- Jede EA im Bericht (raeume) hat ein EA-Zeichen auf der Karte und umgekehrt; Nummern identisch.
- Einheiten in kraefte.* stimmen mit Liste `einheiten` überein; keine Einheit in zwei Zuständen.
- Stärke gesamt passt grob zur Zahl der gemeldeten Einheiten (z. B. ein LB ≈ 9–20 Personen); große Sprünge zum Vorbericht hinterfragen.
- ?/! nicht doppelt; Zahlen gegenüber Vorbericht: Rückgänge bei „verstorben“ unmöglich → Rückfrage.
- Beurteilung „kritisch“/Prognose „Verschärfung“ ohne Eintrag unter Probleme/Bedarfe → Widerspruch melden.
- Genau ein aufgabenbezogener und ein raumbezogener Schwerpunkt.
- Karte nicht überladen: > ~15 Zeichen auf einem km² → zusammenfassen, Details in Kurzfassung.
- Unbestätigte Lagen auf der Karte mit SITUATION_02 kennzeichnen.
- Zeitstempel: Bericht ≤ volle Stunde; Bearbeiter gesetzt.
`lagebericht_fill.py` gibt automatisch `PRÜFEN:`-Warnungen aus (Teilstärken ignoriert, mehrere Schwerpunkte, kritisch/Verschärfung ohne Probleme) – jede Warnung dem Nutzer melden.
Gefundene Widersprüche zuerst nennen, dann die Dateien.

## 8. Skripte (nur verwenden, wenn `tools/` fehlt – unverändert übernehmen; identisch mit `tools/*.py`)

### tz_extract.py
```python
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
```

### lagebericht_fill.py
```python
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
STAERKE = {'gesamt': 'E31'}   # Festlegung Gemeinde 13.11.2026: nur Gesamtzahl; E33–E36 bleiben leer
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
    if any(st.get(k) for k in ('fw', 'kats_hiorg', 'thw', 'sonstige')):
        warn.append('Stärke: nur Gesamtzahl wird eingetragen, Teilstärken (fw/kats_hiorg/thw/sonstige) ignoriert')
    if st.get('gesamt') is not None and not isinstance(st['gesamt'], int):
        warn.append(f"Stärke gesamt ist keine Ganzzahl: {st['gesamt']!r}")
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
```

### lagekarte_build.py
```python
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
  "kaestchen": [{"id":"EA_01","text":"Ortsmitte: Überflutung"}]  # Randfelder: EA + Gefahren mit Kurztext
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


def add_text(slide, text, x, y, w, size=28, bold=True, center=True):
    tb = slide.shapes.add_textbox(Emu(int(x)), Emu(int(y)), Emu(int(w)), Emu(500000))
    tf = tb.text_frame; tf.word_wrap = True
    p = tf.paragraphs[0]; p.text = text
    p.font.size = Pt(size); p.font.bold = bold
    if center:
        from pptx.enum.text import PP_ALIGN; p.alignment = PP_ALIGN.CENTER
    tb.fill.solid(); tb.fill.fore_color.rgb = RGBColor(0xFF, 0xFF, 0xFF); set_alpha(tb, 70)
    return tb


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
            shp.line.color.rgb = RGBColor(0, 0, 0); shp.line.width = Emu(57150)
            shp.line.dash_style = MSO_LINE_DASH_STYLE.DASH_DOT
        else:
            shp.fill.solid(); shp.fill.fore_color.rgb = RGBColor.from_string(AREA[fl['typ']])
            set_alpha(shp, fl.get('deckkraft', 45))
            shp.line.color.rgb = RGBColor.from_string(AREA[fl['typ']]); shp.line.width = Pt(3)
        if fl.get('text'):
            top = max(pts, key=lambda p: p[1])      # Beschriftung über dem nördlichsten Punkt
            x, y = utm_to_emu(*top); add_text(s, fl['text'], x - 60*mm, y - 16*mm, 120*mm, size=24)

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

    # Kästchen am Rand (Festlegung Gemeinde): Einsatzabschnitte und Gefahren mit Kurztext.
    # lage["kaestchen"] = [{"id": "EA_01", "text": "Ortsmitte: Überflutung Köllerbach"}, ...]
    # Fehlt der Schlüssel: automatisch alle verwendeten EA_-Zeichen, danach Gefahren/Schäden.
    import csv
    expl = {}
    idx_csv = os.path.join(symdir, 'index.csv')
    if os.path.exists(idx_csv):
        for row in csv.DictReader(open(idx_csv, encoding='utf-8'), delimiter=';'):
            expl[os.path.splitext(row['Datei'])[0]] = row['Erläuterung']
    kaest = lage.get('kaestchen')
    if kaest is None:
        gef = ('GEFAHR_', 'BRAND_', 'ZERSTÖRUNG_', 'BEFAHRBARKEIT_', 'SITUATION_')
        kaest = [{'id': i} for i in used if i.startswith('EA_')] + \
                [{'id': i} for i in used if i.startswith(gef)]
    phs = sorted([p for p in s.placeholders if p.placeholder_format.type is not None
                  and 'Bild' in p.name], key=lambda p: (p.top, p.left))
    if len(kaest) > len(phs):
        print(f'WARNUNG: {len(kaest)} Kästchen, aber nur {len(phs)} Felder – Rest weggelassen')
    for k, ph in zip(kaest, phs):
        p_ = os.path.join(symdir, k['id'] + '.png')
        if not os.path.exists(p_):
            raise FileNotFoundError(f"Zeichen {k['id']} fehlt in {symdir}")
        add_picture_fit(s, p_, ph.left + ph.width // 2, ph.top + int(ph.height * 0.30), int(ph.width * 0.42))
        add_text(s, k.get('text') or expl.get(k['id'], k['id']), ph.left + 2*mm,
                 ph.top + int(ph.height * 0.56), ph.width - 4*mm, size=22, bold=False)
    for ph in phs[:len(kaest)]:
        ph._element.getparent().remove(ph._element)

    prs.save(out)
    print('gespeichert:', out)


if __name__ == '__main__':
    main(*sys.argv[1:5])
```
