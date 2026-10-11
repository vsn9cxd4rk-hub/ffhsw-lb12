# Update-Anleitung: Version 1.3.x auf 1.4.0 (Manjaro Linux)

## Überblick

Version 1.4.0 ist ein Sicherheits-Update. Für den Betrieb ändert sich:

- Das Backend startet nur noch mit einem gültigen `JWT_ACCESS_SECRET` (mindestens 32 Zeichen, kein Platzhalter).
- Neue Spalte `users.mustChangePassword`.
- nginx liefert `/uploads/` nicht mehr direkt aus und setzt zusätzliche Security-Header.
- Konten mit dem früheren Standardpasswort müssen es beim nächsten Login ändern.

Pfade in dieser Anleitung: Installation unter `/var/www/feuerwehrmanagement`, neuer Quellcode unter `/pfad/zum/projekt`.

## Produktivsystem

### Schritt 1: Backup erstellen

```bash
sudo bash /var/www/feuerwehrmanagement/scripts/backup.sh
```

### Schritt 2: JWT-Secret prüfen

```bash
# Länge des Secrets ausgeben, muss mindestens 32 sein
sudo grep '^JWT_ACCESS_SECRET=' /var/www/feuerwehrmanagement/backend/.env | cut -d= -f2- | tr -d '\n' | wc -c
```

Ist der Wert kürzer als 32 Zeichen oder ein Platzhalter (enthält z.B. `change-me` oder `change-in-production`), ein neues Secret erzeugen und in der `.env` eintragen:

```bash
openssl rand -base64 48
sudo nano /var/www/feuerwehrmanagement/backend/.env
```

### Schritt 3: Datenbank-Migration ausführen

```bash
mysql -u FFWVSLB12 -p FFWVSLB12 < /pfad/zum/projekt/deploy/migrate-1.3.x-to-1.4.0.sql
```

Das Skript ist idempotent. `update.sh` gleicht das Schema in Schritt 4 zusätzlich per Prisma ab.

### Schritt 4: Anwendung aktualisieren

```bash
sudo bash /var/www/feuerwehrmanagement/scripts/update.sh /pfad/zum/projekt
```

Das Skript aktualisiert Backend und Frontend und startet das Backend neu. Beim ersten Start von 1.4.0 laufen automatisch:

- Gruppen mit Einsatz-Berechtigung erhalten die neuen Berechtigungen „Veranstaltungen pflegen" und „Ausbildung pflegen".
- Konten mit dem früheren Standardpasswort werden für den Passwortwechsel markiert.

### Schritt 5: nginx anpassen

```bash
# Security-Header-Snippet installieren
sudo mkdir -p /etc/nginx/snippets
sudo cp /pfad/zum/projekt/deploy/nginx/security-headers.conf /etc/nginx/snippets/feuerwehr-security-headers.conf

# Vorhandene Konfiguration finden (feuerwehrmanagement.conf oder feuerwehr.conf)
ls /etc/nginx/conf.d/
```

Die vorhandene Konfigurationsdatei durch `deploy/nginx/feuerwehr.conf` ersetzen (Dateiname beibehalten, damit nicht zwei Konfigurationen Port 80 belegen) und `server_name` wieder eintragen:

```bash
sudo cp /etc/nginx/conf.d/feuerwehrmanagement.conf /root/feuerwehrmanagement.conf.bak
sudo cp /pfad/zum/projekt/deploy/nginx/feuerwehr.conf /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nano /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nginx -t && sudo systemctl reload nginx
```

Wer die bestehende Datei lieber von Hand anpasst: den Block `location /uploads/ { ... }` entfernen und die `include`-Zeilen für das Snippet wie in der Vorlage ergänzen.

### Schritt 6: Verifizierung

```bash
# Backend läuft
pm2 status feuerwehr-backend
pm2 logs feuerwehr-backend --lines 20

# Security-Header werden gesetzt
curl -sI http://localhost/ | grep -i content-security-policy

# Kein /uploads/-Block mehr vorhanden: Ausgabe muss überall 0 sein
sudo grep -c "location /uploads" /etc/nginx/conf.d/*.conf
```

Im Browser:

1. Anmelden. Konten mit dem früheren Standardpasswort werden zum Passwortwechsel aufgefordert.
2. Ein vorhandenes Dokument herunterladen (z.B. Einsatzbericht oder Prüfprotokoll).
3. Im Dialog „Über diese Anwendung" steht Version 1.4.0.

Startet das Backend nicht, steht der Grund im Log, typischerweise `JWT_ACCESS_SECRET ist ...` (zurück zu Schritt 2).

## Backup-System

Der automatische Restore überträgt Datenbank, Uploads und `.env` vom Produktivsystem, aber weder den Programmcode noch die nginx-Konfiguration. Auf dem Backup-Rechner deshalb:

1. Schritt 4 ausführen (Anwendung aktualisieren).
2. Schritt 5 ausführen (nginx anpassen).

Schritt 2 und 3 entfallen dort, sobald ein Restore nach dem Update des Produktivsystems gelaufen ist: Secret und neue Spalte kommen mit dem Backup.

## Rollback

```bash
# Backup aus Schritt 1 wiederherstellen
sudo bash /var/www/feuerwehrmanagement/scripts/restore.sh

# nginx-Konfiguration zurücksetzen
sudo cp /root/feuerwehrmanagement.conf.bak /etc/nginx/conf.d/feuerwehrmanagement.conf
sudo nginx -t && sudo systemctl reload nginx
```

Den alten Programmstand anschließend mit `update.sh` aus dem vorherigen Quellcode einspielen. Die Spalte `mustChangePassword` stört Version 1.3.x nicht und kann bleiben.

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
