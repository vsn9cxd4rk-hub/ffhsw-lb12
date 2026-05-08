# Update-Anleitung: Mängel/Reparaturen für Fahrzeuge (Manjaro Linux)

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
