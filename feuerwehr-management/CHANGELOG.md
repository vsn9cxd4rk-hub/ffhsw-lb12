# Changelog

Neueste Version zuerst. Die Einträge für Anwender stehen zusätzlich in
`webapp/frontend/src/config/changelog.ts` (Dialog „Über diese Anwendung“ → Klick auf die
Versionsnummer); beide Stellen gemeinsam pflegen. Update-Schritte für Produktiv- und
Backup-System: `deploy/UPDATE-MANJARO.md`.

## 1.4.0 – 2026-10-12 (gegenüber 1.3.2)

Sicherheits-Update: Berechtigungen für Veranstaltungen und Ausbildung, Schutz von
Gesundheitsdaten, erzwungener Passwortwechsel, robustere Sitzungen, aktualisierte
Bibliotheken. Befunde und Nacharbeit: `REVIEW-2026-10-11.md`.

### Neu

- Berechtigungen „Veranstaltungen pflegen“ (br4) und „Ausbildung pflegen“ (br5); ohne sie sind Veranstaltungen, Anwesenheit, BSW-Formulare und Lehrgänge schreibgeschützt.
- Erzwungener Passwortwechsel (`users.mustChangePassword`) für vom Administrator vergebene/zurückgesetzte Passwörter und das frühere Standardpasswort.
- Zufälliges Startpasswort für „admin“ bei der Erstinstallation.
- Änderungsübersicht im Dialog „Über diese Anwendung“.

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
- E2E-Tests brauchen `E2E_ADMIN_PASSWORD`.

### Tests

- Backend: 22 Testdateien, u.a. überfällige Termine im Dashboard, Folgeblätter im Kräftenachweis, Monatsende-Berechnung, Sitzungen (inkl. gleitendem Ablauf), Mass Assignment, Upload-Härtung, Upload-Pfade, Berechtigungen, Gesundheitsdaten. Die Generator-Tests laufen nur, wenn die Vorlagen-Datei vorhanden ist; fehlende Dateien listet `backup-integrity.test.ts`.
