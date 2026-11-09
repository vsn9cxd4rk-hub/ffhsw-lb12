# Skill: s2-lagebericht-lagekarte

S2-Unterstützung im Führungsstab FF Heusweiler: Lagebericht (Excel-Vorlage RV Saarbrücken V1_02)
und Lagekarte Heusweiler (PowerPoint-Vorlage V1.2) mit taktischen Zeichen der Hilfstabelle RV (Rev. 01.00).

## Aufbau
- `SKILL.md` – Anweisungen für Claude (wird von Claude Code / Desktop-App aus `.claude/skills/` geladen)
- `vorlagen/` – Original-Vorlagen RV Saarbrücken
- `tools/lagebericht_fill.py` – füllt die xltx per XML-Patch (Dropdowns, bed. Formatierung, Zeichen bleiben erhalten)
- `tools/lagekarte_build.py` – setzt Zeichen/Flächen/Linien per UTM32- oder Lat/Lon-Koordinate auf die Karte
- `tools/tz_extract.py` – erzeugt `zeichen/` neu aus „03b Übersicht Taktische Zeichen für Lagekarte.pdf“
- `zeichen/` – Zeichen als PNG + `index.csv`
- `beispiele/` – Test-JSON und gerenderte Beispielausgaben

## Voraussetzungen (macOS)
```
brew install poppler librsvg
brew install --cask libreoffice
python3 -m venv ~/.venvs/s2
~/.venvs/s2/bin/pip install -r .claude/skills/s2-lagebericht-lagekarte/requirements.txt
```

## Manuell testen
```
cd .claude/skills/s2-lagebericht-lagekarte
~/.venvs/s2/bin/python tools/lagebericht_fill.py "vorlagen/Lagebericht_Kommune_-_DDhhmmMMMJJ_V1_02.xltx" beispiele/bericht.json /tmp/Lagebericht.xlsx
~/.venvs/s2/bin/python tools/lagekarte_build.py vorlagen/Lagekarte_Heusweiler_V1-2.potx zeichen beispiele/lage.json /tmp/Lagekarte.pptx
```

## Bekannte Lücken (siehe Projekt-Doc „S2-Lage – offene Punkte an RV“)
Kein Zeichen für MASSNAHME_05/06, STELLE_29/30; GEFAHR_03 doppelt vergeben (GEFAHR_03_b = Elektrizität).

## Datenschutz
Keine echten Einsatzdaten (Namen, Adressen Betroffener) einchecken – `beispiele/` enthält nur fiktive Lagen.
