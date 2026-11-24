# Changelog

Neueste Version zuerst. Die Einträge für Anwender stehen zusätzlich in
`webapp/frontend/src/config/changelog.ts` (Dialog „Über diese Anwendung“ → Klick auf die
Versionsnummer); beide Stellen gemeinsam pflegen. Update-Schritte für Produktiv- und
Backup-System: `deploy/UPDATE-MANJARO.md`.

## 1.5.0 – in Entwicklung (gegenüber 1.4.1)

Neues Modul **S2 Lage** und Umstieg auf die aktuellen Hauptversionen aller Bibliotheken.
Enthält alle Korrekturen aus 1.4.1 (`support/1.4.0`).

### Neu: S2 Lage

Der S2-Skill (Lagebericht nach Excel-Vorlage RV Saarbrücken, Lagekarte mit taktischen Zeichen,
Lagemeldung, Mail) als Modul der Web-App, für mehrere Bearbeiter gleichzeitig.

- **Lagen** (Einsatz/Übung) mit Empfänger und Erreichbarkeit der TEL; Status aktiv/beendet.
- **Meldungseingang** mit Zeitstempel, Nachweisungs-Nr., Absender, Ort; „eingearbeitet“ abhaken.
- **Einsatzabschnitte** EA 1–10 mit EA-Grenze auf der Karte, genau ein raumbezogener Schwerpunkt (wird beim Setzen an anderen EA entfernt).
- **Kräfte** mit Organisation/Formation (Listen der RV-Vorlage), Status (alarmiert, Bereitschaft, Grundschutz, Einsatz, Ruhe, entlassen), EA-Zuordnung und Stärke im gewohnten Schema. Gesamtzahl: Ist die letzte Zahl die Summe der übrigen, ist sie die Gesamtstärke (1/8/9 = 9), sonst Summe (1/6 = 7, 2/5/24 = 31). Daraus die Kräftezeilen B5–B25, Stärke E31 und die Einheitenliste.
- **Lagekarte im Browser** auf dem Kartenbild der Vorlage (UTM-Kalibrierung wie `lagekarte_build.py`): 97 taktische Zeichen, Flächen, Linien, EA-Grenzen; Orte per Klick, Gitterwert (351,2/5466,8) oder Adresse aus dem Adressverzeichnis Heusweiler (OSM, offline). Zeichen wahlweise im Randkästchen mit Pfeil (RV-Beispiel 03a) oder direkt auf der Karte.
- **Lagebericht** mit stündlicher Fortschreibung (lfd. Nr. + 1, alle Werte übernommen, Zeit = nächste volle Stunde als taktische Zeit), alle Felder der Vorlage V1_02, Einsatzabschnitte automatisch aus den EA.
- **Export:** Lagebericht `.xlsx`, Lagekarte `.pptx` und `.pdf` (nur Folie 1), Mailtext nach `Mail_Vorlage.md` (Übungskennzeichnung, nächster Bericht). Die App versendet nichts selbst.
- **Plausibilitätsprüfung** wie Abschnitt 7 des Skills (u.a. kritisch/Verschärfung ohne Probleme, mehrere Schwerpunkte, EA ohne Grenze, Verstorbene gesunken, Stärkesprung, zu viele Randkästchen).
- **Mehrbenutzer:** Alle Clients laden den Stand alle 5 s. Jede Zeile hat eine `version`; ein Update auf veraltetem Stand wird mit 409 und dem aktuellen Stand abgelehnt („zwischenzeitlich von X geändert“), der Lagebericht-Editor zeigt fremde Änderungen an, solange eigene ungespeichert sind.
- **Berechtigung** „S2-Lage führen“ (br6); ohne sie kein Zugriff, auch nicht lesend.
- Exporte sind TypeScript-Ports von `lagebericht_fill.py`, `lagekarte_build.py` und `geo_lookup.py` (direkt auf dem XML der Vorlagen, wie die Skripte); geprüft gegen die Ausgabe der Python-Skripte. Kein Python auf dem Server nötig.

### Geändert

- React 19, React DOM 19, Headless UI 2, tailwind-merge 3, Vite 8 (Rolldown), @vitejs/plugin-react 6, TypeScript 7 (Frontend und Backend).
- Express 5 (Routenparameter, leerer Body), express-rate-limit 8 (IPv6-Subnetze), dotenv 18, bcryptjs 3, vitest 5.
- Prisma 7: Rust-freier Client mit MariaDB-Treiberadapter, Datenbank-URL in `prisma.config.ts`.
- axios wieder aktuell (1.21.x). Der Pin auf 1.20.0 in 1.4.1 war unnötig: Die E2E-Tests luden direkt nach dem Speichern neu, bevor axios 1.21 (asynchrones Lesen des XSRF-Cookies) den Request abgeschickt hatte. Die Tests warten jetzt auf die Antwort.

### Behoben

- `prisma/seed.ts` und das E2E-Aufräumskript liefen mit Prisma 7 nicht (Client ohne Treiberadapter); damit wäre auf Neuinstallationen kein Admin angelegt worden.
- E2E-Tests: Klick auf Platzhalterzeilen während des Ladens (Tabellen setzen jetzt `aria-busy`), PDF-Tests warteten nicht auf die Erzeugung.

### Für Administratoren

- **Node.js ≥ 22.12** (Vite 8, Prisma 7); `update.sh` prüft das.
- **SQL-Migration** `deploy/migrate-1.4.1-to-1.5.0.sql` (sechs neue Tabellen `s2_*`, idempotent); `update.sh` führt sie aus. `init-db.sql` enthält die Tabellen für Neuinstallationen.
- Beim ersten Start erhalten Gruppen mit „Gruppenführer“ im Namen die Berechtigung br6 (Datenmigration, einmalig).
- Neue Dateien im Backend unter `webapp/backend/assets/s2/` (Vorlagen, Zeichen, Adressverzeichnis); das Docker-Image kopiert sie mit.
- PDF der Lagekarte über LibreOffice (A0, ~8 s).

## 1.4.1 – 2026-11-22 (gegenüber 1.4.0)

Wartungs-Update mit zwei Fehlerkorrekturen, ohne Änderung am Datenbankschema.

### Geändert

- **Bestandsliste, letzte Prüfung:** Neue Spalte „Letzte Prüfung“ (Datum, io/nio) und gleiche Spalte im PDF-Export. `GET /api/inventory/articles` liefert je Artikel `lastInspection` (jüngste nach `inspectedAt`, sonst `null`). Der CSV-Export bleibt unverändert, weil er dem Importformat entspricht. Nach einer Prüfung lädt die Liste neu.
- **Einsatzbericht, Stärke:** Die Fahrzeugtabelle zeigt die Stärke als Gesamtzahl (`9` statt `1/8/9`). Erfasst wird weiter getrennt nach GF/FM; Kräftenachweis und Gesamtzeit (Einsatzzeit × Stärke) sind unverändert.

### Sicherheit

- `npm audit`: Backend und Frontend wieder 0 Meldungen (vorher Backend 11, Frontend 4). Per `npm update` innerhalb der bestehenden Versionsbereiche angehoben, u.a. multer 2.5.0 (DoS über `fileFilter`), nodemailer 10.6.1 (Adressparser), qs, follow-redirects 1.16.3 (Credential-Header bei Redirects), dompurify (über jsPDF), Vite 6.4.6 (`server.fs.deny`).
- Override `glob` 13 → 14 (über `exceljs` → `archiver` 5; behebt glob/minimatch-Meldungen).
- Neue Override `effect` ^3.22.5 (ReDoS, über `prisma` → `@prisma/config`). `npm audit fix` wollte stattdessen das Prisma-CLI auf 6.12.0 zurückstufen, passend zum Client bleibt es auf 6.19.3.
- **axios bleibt auf 1.20.0 (fest gepinnt):** Mit axios 1.21.x gingen in den E2E-Tests kurz nach dem Laden einer Seite Eingaben und Navigation verloren (BSW-Formular, AGT-Übung, Klick in der Bestandsliste); mit 1.20.0 stabil (Bisektion über alle Paket-Updates). Die Sicherheitskorrektur steckt in `follow-redirects` 1.16.3, das axios 1.20.0 bereits nutzt. Ursache wird für 1.5.0 untersucht.
- Frontend: `zod`, `react-hook-form` und `@hookform/resolvers` entfernt; sie wurden nirgends importiert. Damit entfällt auch die Zod-Meldung (Fix nur in Zod 4).

### Für Administratoren

- **Node.js ≥ 22** (Node 20 ist seit 30.04.2026 End-of-Life). `update.sh`, Installationsskripte und `engines` in den `package.json` prüfen das; Neuinstallationen nutzen Node 24 LTS.
- Docker: `node:24-bookworm-slim`, `nginx:1.30-alpine` (vorher `node:22`, `nginx:1.27`).
- Keine SQL-Migration nötig.

## 1.4.0 – 2026-10-14 (gegenüber 1.3.2)

Sicherheits-Update: Berechtigungen für Veranstaltungen und Ausbildung, Schutz von
Gesundheitsdaten, erzwungener Passwortwechsel, robustere Sitzungen, aktualisierte
Bibliotheken. Befunde und Nacharbeit: `REVIEW-2026-10-11.md`.

### Neu

- Berechtigungen „Veranstaltungen pflegen“ (br4) und „Ausbildung pflegen“ (br5); ohne sie sind Veranstaltungen, Anwesenheit, BSW-Formulare und Lehrgänge schreibgeschützt.
- Erzwungener Passwortwechsel (`users.mustChangePassword`) für vom Administrator vergebene/zurückgesetzte Passwörter und das frühere Standardpasswort.
- Zufälliges Startpasswort für „admin“ bei der Erstinstallation.
- Änderungsübersicht im Dialog „Über diese Anwendung“.
- Prüfdialog: Button „Alle offenen auf io setzen“ unter den Prüfkriterien. Er setzt nur noch nicht bewertete Kriterien auf io, ein bereits gesetztes nio bleibt stehen; bei teilweise bewerteten Kriterien zeigt er die Zahl der offenen an.

### Geändert

- Gruppen mit Einsatz-Berechtigung erhalten beim Update automatisch br4/br5.
- Lehrgangskategorien nur noch für Administratoren; G25/G26/G30-Termine nur noch für Administratoren sichtbar; Mitgliederauswahl zeigt nur Name, Dienstgrad, Gruppe und Qualifikationen.
- Uploads unter zufälligem Dateinamen, Originalname bleibt für die Anzeige.
- PDF-Erzeugung asynchron und ohne Shell (`execFile` statt `execSync`).
- **Sitzungen laufen gleitend ab:** Jeder Refresh verlängert die Sitzung um `JWT_REFRESH_EXPIRES` (Standard 7 Tage) und erneuert das Cookie. Vorher endete sie fest 7 Tage nach dem Login, auch mitten in der Arbeit.
- Schlägt der Token-Refresh nur vorübergehend fehl (Netzwerk, Backend-Neustart, 429), bleibt die Sitzung im Browser bestehen: Seite und ungespeicherte Eingaben (z.B. Prüfdialog) bleiben erhalten. Nur ein 401 von `/auth/refresh` beendet die Sitzung.
- Rate-Limit: `/auth/refresh` und `/auth/logout` zählen nicht gegen das allgemeine Limit; Anfragen mit abgelaufenem Access-Token zählen pro Benutzer statt gegen die gemeinsame IP des Gerätehauses.
- **Upload-Pfade relativ zu `UPLOAD_PATH`:** Neue Dateien werden relativ gespeichert, die einmalige Datenmigration `migration.uploadPaths.relative` stellt bestehende absolute Pfade um. Absolute Altpfade aus fremden Dumps werden zur Laufzeit über die Ordnerstruktur auf das lokale `UPLOAD_PATH` abgebildet. Damit funktionieren Backup-System, Docker und lokale Entwicklung mit eigenem Upload-Ordner.
- Frontend: Tailwind CSS 3 → 4 (Vite-Plugin statt PostCSS/Autoprefixer), Recharts 2 → 3.
- **Einsatz-Details:** Alarmzeit, Ausgerückt, Eingetroffen und Eingerückt entfallen auf Einsatzebene; gepflegt wird nur noch pro Fahrzeug. `operations.alarmTime` wird aus der frühesten Fahrzeug-Alarmzeit abgeleitet (`syncOperationAggregates`), ohne Fahrzeug-Alarmzeit bleibt der Wert aus „Neuer Einsatz“. `departureTime`/`arrivalTime`/`returnTime` der Tabelle `operations` werden nicht mehr geschrieben (Bericht und Kräftenachweis nutzen die Fahrzeugzeiten).
- **Bugfix Einsatzleiter im Einsatzbericht:** Das Feld blieb immer leer (`commanderId` wurde nirgends gesetzt). Jetzt: Rolle „Einsatzleiter“ → Ersteller; Rolle „Einheitenführer“ → neues Pflichtfeld `operations.commanderName` (Freitext mit Vorschlägen aus der Mitgliederliste).
- **Tab „Bericht“ entfernt:** Der Freitext-Bericht wurde nirgends weiterverwendet (der Einsatzbericht entsteht aus den Details). Oberfläche und API (`GET/PUT /api/operations/:id/report`) sind entfernt; die Tabelle `operation_reports` bleibt für Altdaten und ein Rollback erhalten.
- **Bugfix Dashboard, überfällige Termine:** Fahrzeugtermine (TÜV, SP, Service), Geräteprüfungen am Fahrzeug und Untersuchungen (G25/G26/G30, LKW-Führerschein) verschwanden vom Dashboard, sobald sie überfällig waren (untere Grenze `gte: now` in den Abfragen). Sie bleiben jetzt als „Überfällig“ stehen, bis der Termin erneuert ist. Ausgemusterte Fahrzeuge sowie inaktive und gelöschte Mitglieder werden nicht aufgeführt. Die Untersuchungsliste liefert einen Status (`red`/`yellow`) und ist nicht mehr auf 10 Einträge gekappt; das Dashboard zeigt je Liste 5 Einträge und die Zahl der weiteren.
- **Bugfix Kräftenachweis mit mehr als 9 Kräften:** Das Formular hat je Abschnitt 9 Zeilen, alle weiteren Kräfte entfielen ohne Hinweis. Sie kommen jetzt auf Folgeblätter (eigene Dateien mit `_Blatt2`, `_Blatt3`, …). Die Vorlage bleibt unverändert.
- **Bugfix Fälligkeit am Monatsende:** Die nächste Geräteprüfung wurde mit `Date#setMonth` berechnet; eine Prüfung am 31.08. mit 6 Monaten Intervall war dadurch erst am 02./03.03. fällig. Jetzt gilt der letzte Tag des Zielmonats (`addMonths` in `utils/dates.ts`, im Frontend `date-fns`). Bereits gespeicherte Fälligkeiten werden nicht umgerechnet.
- **Bugfix Fälligkeit über die Sommerzeitumstellung:** `addMonths` rechnete in lokaler Zeit auf UTC-Mitternacht; 01.03. + 6 Monate wurde als `2026-08-31T23:00Z` gespeichert. Die Anzeige stimmte, das Bearbeiten-Feld der nächsten Prüfung (Artikel → Historie) war aber einen Tag zu früh vorbelegt. `addMonths` rechnet jetzt in UTC; das Feld liest das Datum lokal aus (`toDateInputValue`), damit stimmen auch bereits gespeicherte Werte.
- **Bugfix Benutzer ohne E-Mail:** Neue Benutzer ließen sich in der Oberfläche nur mit E-Mail-Adresse anlegen (das leere Feld wurde als „Ungültige E-Mail-Adresse“ abgelehnt).
- **Vorschlag „nächste Inventarnummer“:** Folgt der eigenen Nummernfolge (häufigste Länge rein numerischer Nummern, z.B. 121007) statt der höchsten Zahl im Feld; Hersteller- und Seriennummern wie `23642003365` hatten den Vorschlag verschoben, bei `LB12-0045` zählte die 12.
- CSV-Artikelimport: Steht eine Inventarnummer mehrfach in derselben Datei, wird die zweite Zeile übersprungen statt als Fehler gemeldet (wie beim Mitgliederimport).
- Fahrtenbuch: Ein km-Stand Ende unter dem km-Stand Start wird abgelehnt (Formular und API, auch bei Teil-Updates).
- Benutzerverwaltung: Löschen nur nach Rückfrage; beim eigenen Konto gibt es keinen Löschen-Button.
- **Bugfix Dienstgrad:** Der Dienstgrad wurde über Vor- *oder* Nachname gesucht und konnte von einem Namensvetter stammen; jetzt nur bei exakt passendem Vor- und Nachnamen (Ersteller und Einsatzleiter).

### Sicherheit

- Uploads nicht mehr ohne Anmeldung über `/uploads/` abrufbar; Magic-Byte-Prüfung der Uploads; Schutz vor Path Traversal und Mass Assignment.
- Druckansichten gegen XSS geschützt; Security-Header (CSP, Frame-Schutz).
- JWT-Secret ohne Fallback, Refresh-Tokens nur als SHA-256 in der DB, Access-Token nur im Speicher.
- `npm audit`: Backend und Frontend 0 Meldungen (vorher u.a. nodemailer, im Frontend 5× `braces` über Tailwind 3).
- Keine als veraltet markierten Pakete mehr. Im Backend kamen `inflight` („leaks memory, do not use“), `glob` 7, `rimraf` 2, `fstream` und `lodash.isequal` über `exceljs` 4.4.0, das keine neuere Version hat; per npm-Override auf `unzipper` 0.12, `fast-csv` 5 und `glob` 13 gehoben. `archiver` bleibt auf 5, weil `exceljs` mit `archiver` 7 beim Streaming-Export bricht. Im Frontend Recharts 2 (Branch eingestellt) → 3.

### Für Administratoren

- Node.js ≥ 20 (nodemailer 10, Tailwind CSS 4); `update.sh` prüft das.
- Browser: Chrome/Edge ≥ 111, Firefox ≥ 128, Safari ≥ 16.4.
- `JWT_ACCESS_SECRET` muss zufällig und ≥ 32 Zeichen sein. `JWT_REFRESH_SECRET` wurde nie verwendet und ist aus `.env.example`, Docker-Konfiguration, Installationsskripten und Doku entfernt.
- Schema: `deploy/migrate-1.3.x-to-1.4.0.sql` (`users.mustChangePassword`, `operations.commanderName`). `update.sh` führt jetzt die SQL-Migrationen per `prisma db execute` aus statt `prisma db push` (scheiterte an `events.bswData` und FK-Indizes), installiert per `npm ci` inklusive devDependencies für den Build und entfernt sie danach.
- nginx: `location /uploads/` entfernen, Security-Header-Snippet einbinden.
- Neuinstallation: `deploy/init-db.sql` war auf dem Stand vor 1.1.0 (es fehlten u.a. `operation_personnel`, die Einsatzbericht-Felder und die Fahrzeugstärke) und ist jetzt auf Schema-Stand 1.4.0; Gruppenführer bekommen br4/br5 wie im Seed. `install-manjaro.sh`/`install-debian.sh` installieren per `npm ci`, führen den Seed mit `tsx` aus (vorher `ts-node`, nicht installiert und durch `npm prune` entfernt, der Seed scheiterte still) und zeigen das Startpasswort an; `npm prune` erst danach.
- Ein Rollback auf 1.3.x braucht den Datenbank-Restore (1.3.x versteht die relativen Pfade nicht).
- E2E-Tests brauchen `E2E_ADMIN_PASSWORD`. Nach jedem Lauf entfernt ein Playwright-`globalTeardown` die angelegten Testdaten (`npm run e2e:cleanup` im Backend, nur exakte Testnamen; `E2E_KEEP_DATA=1` behält sie). Das braucht DB-Zugriff, also einen lokalen Lauf.

### Tests

- Backend: 27 Testdateien (157 Tests), neu: Prüfbuch (Kriterien, Ergebnis, Fälligkeit, Prüfplan, fällige Prüfungen), CSV-Import (Artikel, Prüfungen, Mitglieder), Benutzerverwaltung (letzter Admin, Sitzungsende bei Deaktivierung, Anlegen ohne E-Mail), Inventarnummer-Vorschlag, Fahrtenbuch-km; außerdem u.a. überfällige Termine im Dashboard, Folgeblätter im Kräftenachweis, Monatsende-Berechnung, Sitzungen (inkl. gleitendem Ablauf), Mass Assignment, Upload-Härtung, Upload-Pfade, Berechtigungen, Gesundheitsdaten. Die Generator-Tests laufen nur, wenn die Vorlagen-Datei vorhanden ist; fehlende Dateien listet `backup-integrity.test.ts`.
- E2E: neu Fahrzeuge (inkl. Fahrtenbuch), Benutzerverwaltung, Ausbildung, Statistik und „Alle offenen auf io setzen“. Die Generierungs-Tests im Einsatz arbeiten auf einem eigenen Testeinsatz statt auf dem ersten der Liste (hingen bisher bei jedem Lauf einen Einsatzbericht an einen echten Einsatz). Mehrdeutige Locators korrigiert.
