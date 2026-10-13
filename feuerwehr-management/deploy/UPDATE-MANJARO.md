# Update-Anleitung: Version 1.3.x auf 1.4.0 (Manjaro Linux)

Gilt für Produktiv- und Backup-System. Pfade in dieser Anleitung: Installation unter
`/var/www/feuerwehrmanagement`, neuer Quellcode (entpacktes Release-Paket oder Git-Checkout)
unter `/pfad/zum/projekt`. Dauer ca. 15–20 Minuten, das Backend ist dabei ca. 1 Minute nicht
erreichbar.

## Was sich ändert

Für die Benutzer:

- Neue Berechtigungen „Veranstaltungen pflegen“ und „Ausbildung pflegen“. Gruppen mit
  Einsatz-Berechtigung bekommen sie automatisch, alle anderen sehen Veranstaltungen und
  Ausbildung nur noch lesend.
- Vom Administrator vergebene Passwörter und das frühere Standardpasswort `Admin123!` müssen
  beim nächsten Login geändert werden.
- Die Anmeldung endet erst nach 7 Tagen **ohne Nutzung** (vorher fest 7 Tage nach dem Login,
  auch mitten in der Arbeit). Lange offene Dialoge (z.B. Geräteprüfung) verlieren ihre
  Eingaben nicht mehr, wenn der Server kurz nicht erreichbar ist.
- Einsätze: Alarmzeit, Ausgerückt, Eingetroffen und Eingerückt werden nur noch pro Fahrzeug
  im Tab „Fahrzeugzeiten“ erfasst. Im Einsatzbericht ist das Feld „Einsatzleiter“ jetzt
  befüllt; ist der Ersteller nur Einheitenführer, wird der Einsatzleiter auf der
  Details-Seite eingetragen. Bestehende Einsätze mit Rolle „Einheitenführer“ zeigen das neue
  Feld leer an und lassen sich erst nach dem Eintragen wieder speichern.
- Der Tab „Bericht“ beim Einsatz entfällt (der Einsatzbericht wird aus den Details erzeugt).
  Wer dort Freitext gepflegt hat: Die Texte bleiben in der Datenbank erhalten, siehe
  Prüfabfrage in Schritt 1.
- **Browser:** Chrome/Edge ab 111, Firefox ab 128, Safari ab 16.4. Rechner mit Windows 7/8
  (Chrome 109) zeigen die Oberfläche nicht mehr korrekt an.

Für den Betrieb:

| Bereich | Änderung | Wer erledigt es |
| --- | --- | --- |
| Node.js | mindestens Version 20 | Schritt 2 (manuell, falls nötig) |
| `.env` | `JWT_ACCESS_SECRET` muss zufällig und mindestens 32 Zeichen lang sein, sonst startet das Backend nicht. `JWT_REFRESH_SECRET` wird nicht verwendet und kann entfallen. | Schritt 3 (manuell) |
| Datenbank | neue Spalten `users.mustChangePassword` und `operations.commanderName` | `update.sh` (`deploy/migrate-1.3.x-to-1.4.0.sql`) |
| Daten | Berechtigungen br4/br5 setzen, Konten mit Standardpasswort markieren, Upload-Pfade relativ zu `UPLOAD_PATH` speichern | automatisch beim ersten Backend-Start |
| nginx | `/uploads/` wird nicht mehr direkt ausgeliefert (nur noch angemeldet über die API), zusätzliche Security-Header | Schritt 5 (manuell) |
| E2E-Tests | Admin-Passwort kommt aus `E2E_ADMIN_PASSWORD` | nur wer Playwright nutzt |

## Produktivsystem

### Schritt 1: Backup erstellen

```bash
sudo bash /var/www/feuerwehrmanagement/scripts/backup.sh
ls -lh /var/www/feuerwehrmanagement/backups/ | tail -3
```

`update.sh` legt in Schritt 4 zusätzlich ein Backup an. Das hier ist der Stand für ein Rollback.

Optional: Gibt es Freitexte aus dem entfallenden Tab „Bericht“? Ausgabe `0` heißt, nichts
geht verloren; sonst die Texte bei Bedarf in die Einsatzdetails übernehmen.

```bash
mysql -u FFWVSLB12 -p FFWVSLB12 -e "SELECT COUNT(*) FROM operation_reports WHERE TRIM(content) <> '';"
```

### Schritt 2: Node.js prüfen

```bash
node -v
```

Ausgabe `v20.x` oder höher: weiter mit Schritt 3. Sonst aktualisieren:

```bash
sudo pacman -Syu nodejs npm
node -v
```

`update.sh` bricht mit einer Meldung ab, wenn die Version zu alt ist.

### Schritt 3: `.env` prüfen

```bash
sudo nano /var/www/feuerwehrmanagement/backend/.env
```

1. **`JWT_ACCESS_SECRET`** muss gesetzt, mindestens 32 Zeichen lang und kein Platzhalter sein
   (enthält nicht `change-me` oder `change-in-production`). Länge prüfen:

   ```bash
   sudo grep '^JWT_ACCESS_SECRET=' /var/www/feuerwehrmanagement/backend/.env | cut -d= -f2- | tr -d '\n' | wc -c
   ```

   Ist der Wert ungültig, ein neues Secret erzeugen und eintragen:

   ```bash
   openssl rand -base64 48
   ```

   Ein neues Secret meldet nur die gerade laufenden Access-Tokens ab (15 Minuten Gültigkeit);
   die Benutzer bleiben über ihre Sitzung angemeldet.

2. **`JWT_REFRESH_SECRET`**: Zeile löschen (wird nicht verwendet, stört aber auch nicht).

3. **`NODE_ENV=production`** muss gesetzt sein, sonst sind die Anmelde-Sperren gegen
   Passwort-Raten aus.

4. **`UPLOAD_PATH`** zeigt auf den Upload-Ordner, üblicherweise
   `/var/www/feuerwehrmanagement/uploads`. Bitte nicht ändern: Die Pfade in der Datenbank
   werden in Schritt 4 relativ zu diesem Ordner umgestellt.

5. Optional `COOKIE_SECURE=false` ergänzen (Standard). Nur auf `true` setzen, wenn ein
   TLS-Proxy davor steht, der `X-Forwarded-Proto` nicht weitergibt.

### Schritt 4: Anwendung aktualisieren

```bash
sudo bash /var/www/feuerwehrmanagement/scripts/update.sh /pfad/zum/projekt
```

Das Skript prüft Node.js, legt ein Backup an, installiert die Abhängigkeiten exakt nach
Lockfile (`npm ci`), baut Backend und Frontend, führt `deploy/migrate-1.3.x-to-1.4.0.sql`
aus (idempotent) und startet das Backend neu.

> Ältere `update.sh`-Versionen (1.3.x) fallen bei der Migration auf `prisma db push` zurück,
> das an `events.bswData` scheitert. Das Skript aktualisiert sich zwar selbst, aber erst am
> Ende des Laufs. Deshalb das neue Skript aus dem Quellcode verwenden:
>
> ```bash
> sudo cp /pfad/zum/projekt/deploy/scripts/update.sh /var/www/feuerwehrmanagement/scripts/update.sh
> ```
>
> und danach den Befehl oben ausführen.

Beim ersten Start von 1.4.0 laufen einmalig automatisch:

- Gruppen mit Einsatz-Berechtigung (br2) erhalten „Veranstaltungen pflegen“ (br4) und
  „Ausbildung pflegen“ (br5).
- Konten mit dem früheren Standardpasswort werden für den Passwortwechsel markiert.
- Dateipfade der Uploads werden relativ zu `UPLOAD_PATH` gespeichert, z.B.
  `templates/123-Vorlage.docx` statt `/var/www/feuerwehrmanagement/uploads/templates/...`.
  Damit funktioniert ein Datenbank-Dump auf jedem System, auf das der Upload-Ordner
  mitkopiert wird (Backup-System, Docker, lokale Entwicklung).

Jede Datenmigration wird in der Tabelle `settings` vermerkt und läuft nie ein zweites Mal.

### Schritt 5: nginx anpassen

```bash
# Security-Header-Snippet installieren
sudo mkdir -p /etc/nginx/snippets
sudo cp /pfad/zum/projekt/deploy/nginx/security-headers.conf /etc/nginx/snippets/feuerwehr-security-headers.conf

# Vorhandene Konfiguration finden (feuerwehrmanagement.conf oder feuerwehr.conf)
ls /etc/nginx/conf.d/
```

Die vorhandene Konfigurationsdatei durch `deploy/nginx/feuerwehr.conf` ersetzen (Dateiname
beibehalten, damit nicht zwei Konfigurationen Port 80 belegen) und `server_name` wieder
eintragen:

```bash
sudo cp /etc/nginx/conf.d/feuerwehrmanagement.conf /root/feuerwehrmanagement.conf.bak
sudo cp /pfad/zum/projekt/deploy/nginx/feuerwehr.conf /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nano /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nginx -t && sudo systemctl reload nginx
```

Wer die bestehende Datei lieber von Hand anpasst: den Block `location /uploads/ { ... }`
entfernen und die `include`-Zeilen für das Snippet wie in der Vorlage ergänzen.

### Schritt 6: Prüfen

```bash
# Backend läuft
pm2 status feuerwehr-backend

# Datenmigrationen beim Start: drei Zeilen "Datenmigration migration...."
pm2 logs feuerwehr-backend --lines 100 --nostream | grep Datenmigration

# Security-Header werden gesetzt
curl -sI http://localhost/ | grep -i content-security-policy

# Kein /uploads/-Block mehr vorhanden: Ausgabe muss überall 0 sein
sudo grep -c "location /uploads" /etc/nginx/conf.d/*.conf

# Uploads sind ohne Anmeldung nicht mehr erreichbar: erwartet 404
curl -s -o /dev/null -w '%{http_code}\n' http://localhost/uploads/
```

Erwartet wird bei den Upload-Pfaden z.B.
`Datenmigration migration.uploadPaths.relative: umgestellt (templates: 8, operation_documents: 11, ...)`.

Im Browser (Strg+F5, damit die neue Oberfläche geladen wird):

1. Anmelden. Konten mit dem früheren Standardpasswort werden zum Passwortwechsel aufgefordert.
2. Ein vorhandenes Dokument herunterladen (z.B. Einsatzbericht oder Prüfprotokoll).
3. Einen Einsatzbericht oder Kräftenachweis erzeugen (prüft die Vorlagen-Pfade).
4. Einstellungen → Berechtigungsgruppen: Gruppen mit Einsatz-Berechtigung haben auch
   „Veranstaltungen pflegen“ und „Ausbildung pflegen“.
5. Im Dialog „Über diese Anwendung“ steht Version 1.4.0, ein Klick auf die Versionsnummer
   zeigt die Änderungen.

Startet das Backend nicht, steht der Grund im Log (`pm2 logs feuerwehr-backend`),
typischerweise `JWT_ACCESS_SECRET ist ...` (zurück zu Schritt 3).

### Optional: Backend-Tests auf dem Server

```bash
sudo -u lb12admin bash /var/www/feuerwehrmanagement/scripts/run-backend-tests.sh /pfad/zum/projekt
```

`backup-integrity.test.ts` listet Datenbankeinträge, deren Datei im Upload-Ordner fehlt.

## Backup-System

Der automatische Restore überträgt Datenbank, Uploads und `.env` vom Produktivsystem, aber
weder den Programmcode noch die nginx-Konfiguration. Auf dem Backup-Rechner deshalb:

1. Schritt 2 (Node.js prüfen).
2. Schritt 4 (Anwendung aktualisieren, gegebenenfalls vorher `update.sh` kopieren).
3. Schritt 5 (nginx anpassen).

Schritt 3 entfällt, sobald nach dem Update des Produktivsystems ein Restore gelaufen ist:
Secret, neue Spalte und umgestellte Pfade kommen mit dem Backup. Weicht `UPLOAD_PATH` auf
dem Backup-System ab, ist das ab 1.4.0 kein Problem mehr.

Spielt ein Restore noch einen Dump von **vor** dem Update ein (absolute Pfade, kein
Migrationsvermerk), stellt der nächste Backend-Start die Pfade dort ebenfalls um. Auch ohne
Umstellung funktionieren Downloads: Absolute Altpfade werden zur Laufzeit über die
Ordnerstruktur (`templates/`, `operations/<id>/`, ...) auf das lokale `UPLOAD_PATH`
abgebildet.

## Rollback

```bash
# Backup aus Schritt 1 wiederherstellen (Datenbank und Uploads)
sudo bash /var/www/feuerwehrmanagement/scripts/restore.sh

# nginx-Konfiguration zurücksetzen
sudo cp /root/feuerwehrmanagement.conf.bak /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nginx -t && sudo systemctl reload nginx
```

Den alten Programmstand anschließend mit `update.sh` aus dem 1.3.x-Quellcode einspielen.

Der Datenbank-Restore ist Pflicht: 1.3.x versteht die relativen Upload-Pfade nicht
(Downloads und Vorlagen würden mit „Datei nicht gefunden“ scheitern). Die Spalte
`mustChangePassword` stört 1.3.x nicht.

---

# Ältere Update-Anleitung: Mängel/Reparaturen für Fahrzeuge (Manjaro Linux)

## Überblick

Dieses Update erweitert das Mängel- und Reparatursystem:
- Mängel und Reparaturen können jetzt auch für **Fahrzeuge** und **"Sonstige"** erfasst werden
- Neues Feld `subject` speichert den Fahrzeugnamen oder "Sonstige"
- `articleId` wird nullable (bestehende Daten bleiben erhalten)

---

## Voraussetzungen

- Zugriff auf den Produktivserver (SSH)
- MySQL/MariaDB Root- oder FFWVSLB12-Zugang
- Die aktualisierte Codebasis (git pull oder manueller Upload)

---

## Schritt 1: Backup erstellen

```bash
# WICHTIG: Vor jedem Update ein Backup!
mysqldump -u FFWVSLB12 -p FFWVSLB12 > /var/www/feuerwehrmanagement/backups/backup-pre-migration-$(date +%Y%m%d-%H%M).sql
echo "Backup erstellt: $(ls -la /var/www/feuerwehrmanagement/backups/backup-pre-migration-*.sql | tail -1)"
```

---

## Schritt 2: Datenbank-Migration ausführen

```bash
# Migrationsskript ausführen
mysql -u FFWVSLB12 -p FFWVSLB12 < /pfad/zum/projekt/deploy/migrate_defects_nullable.sql
```

Erwartete Ausgabe (Verifizierung):
```
+------------------+--------+-------------+-------------+
| tabelle          | anzahl | mit_artikel | mit_subject |
+------------------+--------+-------------+-------------+
| article_defects  |      5 |           5 |           0 |
| article_repairs  |      2 |           2 |           0 |
+------------------+--------+-------------+-------------+
```

- `mit_artikel`: Alle bestehenden Einträge behalten ihre articleId
- `mit_subject`: 0 bei bestehenden Daten (neue Fahrzeug-Mängel werden hier gezählt)

---

## Schritt 3: Backend aktualisieren

```bash
# Als lb12admin oder mit sudo
cd /var/www/feuerwehrmanagement/backend

# Neuen Quellcode kopieren/pullen
cp -r /pfad/zum/projekt/webapp/backend/src/* ./src/
cp -r /pfad/zum/projekt/webapp/backend/prisma/* ./prisma/

# Dependencies aktualisieren (falls package.json geändert)
npm install

# Prisma Client neu generieren (Schema hat sich geändert)
npm run prisma:generate

# TypeScript kompilieren
npm run build

# Backend neustarten
pm2 restart feuerwehr-backend
pm2 logs feuerwehr-backend --lines 5
```

---

## Schritt 4: Frontend aktualisieren

```bash
cd /pfad/zum/projekt/webapp/frontend

# Dependencies installieren
npm install

# Produktions-Build erstellen
npm run build

# Build-Output kopieren
cp -r dist/* /var/www/feuerwehrmanagement/frontend/
```

---

## Schritt 5: Verifizierung

1. Browser öffnen → Einloggen
2. **Mängel-Seite** aufrufen → "Neuer Mangel" klicken
3. Im Artikel-Auswahl-Dialog prüfen:
   - Geräteklasse-Filter funktioniert
   - Unterklasse-Filter funktioniert
   - "Ohne Geräteklasse" zeigt ungefilterte Artikel
   - **Fahrzeuge** sind als eigene Optgroup sichtbar
   - **Sonstige** ist als Option sichtbar
4. Einen Mangel für ein Fahrzeug anlegen → Speichern
5. In der Mängel-Liste prüfen: Fahrzeugname wird angezeigt
6. **Bestehende Mängel** (für Artikel) sind weiterhin sichtbar und bearbeitbar

---

## Rollback (falls nötig)

```bash
# Backup wiederherstellen
mysql -u FFWVSLB12 -p FFWVSLB12 < /var/www/feuerwehrmanagement/backups/backup-pre-migration-YYYYMMDD-HHMM.sql

# Alten Code wiederherstellen und neu bauen
# (Git: git checkout <alter-commit>)
```

---

## Was ändert sich für den Benutzer?

| Vorher | Nachher |
|--------|---------|
| Mängel nur für Bestandsliste-Artikel | Mängel auch für Fahrzeuge und "Sonstige" |
| Einfaches Dropdown (400+ Artikel) | Filter nach Geräteklasse + Unterklasse |
| - | Fahrzeuge als eigene Auswahl-Kategorie |
| - | "Sonstige" als Freitext-Option |

---

*Erstellt: April 2026*
