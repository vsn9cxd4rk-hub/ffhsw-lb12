# Feuerwehr Management System – Deployment-Dokumentation

## Inhaltsverzeichnis

1. [Systemvoraussetzungen](#systemvoraussetzungen)
2. [Erstinstallation](#erstinstallation)
3. [Verzeichnisstruktur](#verzeichnisstruktur)
4. [Konfiguration](#konfiguration)
5. [Betrieb und Verwaltung](#betrieb-und-verwaltung)
6. [Backup & Restore](#backup--restore)
7. [Updates](#updates)
8. [Migration vom Java-System](#migration-vom-java-system)
9. [Nginx & HTTPS](#nginx--https)
10. [Fehlerbehebung](#fehlerbehebung)

---

## Systemvoraussetzungen

| Komponente | Version | Zweck |
|---|---|---|
| Ubuntu | 22.04 LTS | Empfohlenes Betriebssystem |
| Debian | 12 | Alternative |
| Node.js | 20.x | Backend-Laufzeitumgebung |
| MySQL | 8.x | Datenbank |
| Nginx | aktuell | Web-/Proxy-Server |
| PM2 | aktuell | Prozessmanagement |

**Mindestanforderungen Hardware:**
- CPU: 2 Kerne
- RAM: 2 GB (4 GB empfohlen)
- Festplatte: 20 GB (für Daten und Backups)

---

## Erstinstallation

### 1. Skript ausführen

```bash
# Projekt auf den Server übertragen
scp -r /pfad/zum/projekt/ benutzer@server:/tmp/fuerwehr/

# Auf dem Server als root
sudo bash /tmp/fuerwehr/deploy/install.sh
```

Das Installationsskript führt folgende Schritte automatisch durch:

- System aktualisieren
- Node.js 20, MySQL, Nginx, PM2 installieren
- Systembenutzer `fuerwehr` anlegen
- Verzeichnisstruktur erstellen
- MySQL-Datenbank und Benutzer einrichten
- `.env`-Konfigurationsdatei erstellen
- Backend bauen und starten
- Frontend bauen und deployen
- Nginx konfigurieren
- Cron-Jobs einrichten

### 2. Standard-Anmeldedaten

Nach der Installation:

| Feld | Wert |
|---|---|
| Benutzername | `admin` |
| Passwort | `Admin123!` |

> **WICHTIG:** Das Passwort sofort nach der ersten Anmeldung ändern!

---

## Verzeichnisstruktur

```
/var/www/fuerwehr/
├── backend/          # Node.js-Backend (kompiliert)
│   ├── dist/         # Kompilierter TypeScript-Code
│   ├── node_modules/ # npm-Abhängigkeiten
│   ├── prisma/       # Datenbankschema und Migrationen
│   └── .env          # Konfigurationsdatei (chmod 600)
├── frontend/         # Gebaute React-App (statische Dateien)
├── uploads/          # Hochgeladene Dateien
├── backups/          # Backup-Archive
│   ├── daily/        # Tägliche Backups (30 Tage)
│   └── monthly/      # Monatliche Backups (12 Monate)
├── scripts/          # Verwaltungsskripte
│   ├── backup.sh
│   ├── update.sh
│   ├── restore.sh
│   └── check-health.sh
└── ecosystem.config.js  # PM2-Konfiguration

/var/log/fuerwehr/
├── pm2-out.log       # Backend-Ausgabe
├── pm2-error.log     # Backend-Fehler
├── backup.log        # Backup-Protokoll
└── health.log        # Health-Check-Protokoll
```

---

## Konfiguration

Die gesamte Konfiguration befindet sich in `/var/www/fuerwehr/backend/.env`:

```env
NODE_ENV=production
PORT=3001

DATABASE_URL="mysql://user:pass@localhost:3306/fuerwehr"

JWT_ACCESS_SECRET=<zufälliger-schlüssel>
JWT_REFRESH_SECRET=<zufälliger-schlüssel>
JWT_ACCESS_EXPIRES=15m
JWT_REFRESH_EXPIRES=7d

CORS_ORIGIN=http://ihre-domain.de

UPLOAD_PATH=/var/www/fuerwehr/uploads
LOG_LEVEL=info
LOG_PATH=/var/log/fuerwehr

# E-Mail (optional)
SMTP_HOST=smtp.example.com
SMTP_PORT=587
SMTP_SECURE=false
SMTP_USER=benutzer@example.com
SMTP_PASS=passwort
SMTP_FROM=noreply@feuerwehr.de
```

Nach Änderungen an `.env` das Backend neu starten:

```bash
sudo -u fuerwehr pm2 reload fuerwehr-backend --update-env
```

---

## Betrieb und Verwaltung

### Backend-Status prüfen

```bash
pm2 status fuerwehr-backend
pm2 monit
```

### Logs anzeigen

```bash
# Echtzeit-Logs
pm2 logs fuerwehr-backend

# Letzte 100 Zeilen
pm2 logs fuerwehr-backend --lines 100

# Fehlerlog
tail -f /var/log/fuerwehr/pm2-error.log
```

### Backend neu starten

```bash
# Graceful reload (ohne Downtime)
sudo -u fuerwehr pm2 reload fuerwehr-backend --update-env

# Harter Neustart
sudo -u fuerwehr pm2 restart fuerwehr-backend
```

### Nginx-Status

```bash
systemctl status nginx
nginx -t           # Konfiguration testen
systemctl reload nginx
```

### MySQL-Verbindung

```bash
# Mit Datenbankbenutzer aus .env
source /var/www/fuerwehr/backend/.env
mysql -u $(echo $DATABASE_URL | sed -E 's|mysql://([^:]+):.*|\1|') \
      -p $(echo $DATABASE_URL | sed -E 's|.*:([^@]+)@.*|\1|') \
      fuerwehr
```

---

## Backup & Restore

### Manuelles Backup

```bash
sudo -u fuerwehr /var/www/fuerwehr/scripts/backup.sh
```

Backups werden gespeichert in:
- `/var/www/fuerwehr/backups/daily/` – 30 Tages-Backups
- `/var/www/fuerwehr/backups/monthly/` – 12 Monats-Backups

### Automatisches Backup (Cron)

Täglich um 02:00 Uhr automatisch – konfiguriert durch `install.sh`.

Prüfen mit:
```bash
crontab -l -u fuerwehr
```

### Restore

```bash
# Interaktiv (zeigt verfügbare Backups)
sudo bash /var/www/fuerwehr/scripts/restore.sh

# Direkt mit Backup-Datei
sudo bash /var/www/fuerwehr/scripts/restore.sh \
  /var/www/fuerwehr/backups/daily/fuerwehr_20251201_020000.tar.gz
```

> **Achtung:** Der Restore überschreibt die aktuelle Datenbank und Uploads!

---

## Updates

### Aus Quellcode aktualisieren

```bash
sudo bash /var/www/fuerwehr/scripts/update.sh /pfad/zum/neuen/quellcode
```

Das Update-Skript führt folgendes aus:
1. Pre-Update-Backup erstellen
2. Backend-Code aktualisieren
3. npm-Abhängigkeiten installieren
4. TypeScript kompilieren
5. Datenbankmigrationen ausführen
6. Frontend neu bauen und deployen
7. Hilfsskripte aktualisieren
8. Backend per `pm2 reload` neu starten (ohne Downtime)

### Datenbankmigrationen manuell ausführen

Bei einem Update auf eine Version mit neuen Datenbankfeldern muss die Migration eingespielt werden:

```bash
mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-add-report-fields.sql
mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-add-bsw-report.sql
```

Beide Migrationen sind idempotent (können mehrfach ausgeführt werden).

**Direktes Update von 1.1.0 auf 1.2.0:** Version 1.1.1 enthielt keine
Datenbankänderungen, daher deckt die Sammel-Migration
`deploy/migrate-1.1.0-to-1.2.0.sql` (Zusammenfassung der beiden obigen
Skripte) den kompletten Sprung von 1.1.0 auf 1.2.0 in einem Schritt ab:

```bash
mysql -u FFWVSLB12 -p FFWVSLB12 < deploy/migrate-1.1.0-to-1.2.0.sql
```

Danach zusätzlich (manueller Schritt, kein SQL): die beiden Vorlagen
`data/Templates/ChecklisteBrandsicherheitswacheLB12.docx` und
`data/Templates/BerichtBrandsicherheitswacheLB12.docx` unter
Einstellungen → Templates hochladen.

---

## Funktionsübersicht (FENIX-Erweiterungen)

### Einsatzberichte & Kräftenachweis

Der Einsatzbereich enthält ein vollständiges Berichtsystem:

**Einsatzdetails (erweiterte Felder):**

| Feld | Beschreibung |
|---|---|
| Berichtsart | "Einsatzbericht" oder "Tätigkeitsbericht" |
| ILS Auftragsnummer | Auftragsnummer der Integrierten Leitstelle |
| Meldender | Name & Erreichbarkeit des Anrufers |
| Polizei | Zuständige Inspektion & Sachbearbeiter |
| Lage bei Eintreffen | Freitext-Beschreibung der Lage |
| Durchgeführte Maßnahmen | Freitext |
| Verbrauchte Einsatzmittel | Freitext |
| Einsatzart | Checkbox-Auswahl (Brand, THL, Gefahrstoff, etc.) |
| Statistische Angaben | Gerettete Personen, Verletzte/Tote FW, Tote Personen |
| Ersteller / Rolle | Name + Rolle (Einsatzleiter / Einheitenführer) |

**Kräftenachweis (operation_personnel):**

Pro Einsatz wird das eingesetzte Personal erfasst mit:
- Mitglied (aus Mitgliederstamm)
- Fahrzeug (Zuordnung)
- Funktion (Gruppenführer, Maschinist, AT-Führer, AT-Mann, WT-Führer, WT-Mann, etc.)
- Sektion (Eingesetzte Kräfte / Nachgerückte Kräfte)

**Dokumentengenerierung:**

Über Buttons im Einsatz-Detail können automatisch generiert werden:
- **Einsatzbericht** (Word-Dokument aus Template)
- **Kräftenachweis** (Excel-Dokument mit Personalaufstellung)

Die generierten Dokumente werden als Download bereitgestellt und erscheinen in der Dokumentenliste des Einsatzes.

### Berechtigungskonzept

Jeder Login-Benutzer gehört zu genau einer Berechtigungsgruppe (`groupId`). Eine Gruppe ist aber kein starrer Rollenname, sondern eine frei kombinierbare Sammlung von **Fähigkeiten-Bits** (`br0`-`br75` in der `permission_groups`-Tabelle). Aktuell sind drei Bits mit fester Bedeutung belegt:

| Bit | Fähigkeit | Freigeschaltete Bereiche |
|-----|-----------|--------------------------|
| `br1` | Fahrzeuge | Fahrzeuge, Fahrtenbuch |
| `br2` | Einsätze | Einsätze, Statistik, Personalliste für Kräftenachweis |
| `br3` | Gerätewart-Bereich | Bestandsliste, Prüfbuch, Mängel |

Ein Administrator (`isAdmin = true`) umgeht alle Bit-Prüfungen und hat immer Vollzugriff, inklusive Personal (Mitgliederakte) und Einstellungen — das ist weiterhin **nicht** über Bits, sondern separat an `isAdmin` gebunden, weil dort sensible Daten (Gesundheitsdaten, Bankverbindung, Familienkontakte) liegen. Veranstaltungen und Ausbildung sind für alle angemeldeten Benutzer offen.

Mitgelieferte Standardgruppen (Einstellungen → Berechtigungsgruppen):

| Gruppe | br1 (Fahrzeuge) | br2 (Einsätze) | br3 (Gerätewart) |
|--------|:---:|:---:|:---:|
| Administrator | - (isAdmin) | - (isAdmin) | - (isAdmin) |
| Gerätewarte | ✓ | | ✓ |
| Benutzer | | | |
| Maschinisten | ✓ | | |
| Gruppenführer | | ✓ | |
| Gerätewart + Gruppenführer | ✓ | ✓ | ✓ |

Weil die Bits pro Gruppe frei kombinierbar sind, lassen sich beliebige Rollenkombinationen abbilden (z.B. ein Mitglied, das sowohl Gerätewart als auch Gruppenführer ist) — dafür einfach eine neue Gruppe mit den passenden Häkchen anlegen (oder die mitgelieferte Gruppe "Gerätewart + Gruppenführer" verwenden) und den Benutzer dieser einen Gruppe zuordnen. Ein Benutzer bleibt weiterhin genau einer Gruppe zugeordnet — die Gruppe selbst trägt die Kombination.

### User-Member-Verknüpfung

Jeder System-Benutzer (Login-Konto) kann optional einem Mitglied (Personalstammdaten) zugeordnet werden. Die Zuordnung erfolgt in der Admin-Oberfläche unter **Benutzerverwaltung** via Dropdown "Verknüpftes Mitglied".

- Beziehung ist 1:1 (ein Mitglied hat max. einen User-Account)
- Nicht jeder User muss ein Mitglied sein (z.B. reine Admin-Accounts)
- Nicht jedes Mitglied muss einen User haben (passive Mitglieder)

### Lehrgänge → Qualifikationen (automatisch)

Unter **Einstellungen → Lehrgänge** kann jeder Lehrgangskategorie eine Qualifikation zugeordnet werden. Wenn ein Lehrgang den Status "Abgeschlossen" erhält, wird die entsprechende Qualifikation automatisch beim Mitglied auf `true` gesetzt.

Beispiel-Zuordnungen (standardmäßig vorkonfiguriert):
| Lehrgangskategorie | Qualifikation |
|---|---|
| Atemschutzgeräteträger | qualAGT |
| Truppführer | qualTruppfuehrer |
| Gruppenführer | qualGruppenfuehrer |
| Sprechfunker | qualRadioOperator |
| Maschinisten | qualMachinist |

### Einsatzstatistik

Unter dem Menüpunkt **Statistik** (nur für Administratoren und Gruppenführer sichtbar) werden die Einsatzdaten eines Jahres automatisch ausgewertet — als Ersatz für die bisher manuell gepflegte Excel-Statistik.

**Auswertungen:**

| Diagramm | Beschreibung |
|---|---|
| KPI-Übersicht | Gesamt-Einsätze, Real-Einsätze, Fehlalarme, aktiv tätig |
| Monatsverlauf | Liniendiagramm der Einsätze pro Monat |
| Ortsteile | Balkendiagramm: welcher Stadtteil wie oft betroffen |
| Einsatzstichworte | Häufigkeit der Alarmstichwörter (ILS) |
| Einsatzresultate | Kreisdiagramm: Brand Real/Fehl, THL Real/Fehl, BMA Fehl |
| Tageszeit-Analyse | Verteilung FRÜH/MITTAG/SPÄT + Ø-Stärke + Risikoampel |
| Wochentage | Gestapelte Balken: Mo–So × Tageszeit-Intervall |
| Personal Top-10 | Horizontale Balken: meisteingesetzte Kräfte mit Funktionsaufteilung |

**Risikoanalyse (Tageszeit):**
- 🔴 KRITISCH: Durchschnittsstärke < 6 (unter Staffel-Minimum)
- 🟡 AKZEPTABEL: 6–8 (Staffel+)
- 🟢 KEIN RISIKO: ≥ 9 (Gruppe)

**Zusätzliche Felder in Einsatz-Details:**

| Feld | Beschreibung |
|---|---|
| Einsatzresultat | Dropdown: Brand Real, THL Real, Brand Fehl, THL Fehl, BMA Fehl |
| LB aktiv tätig gewesen? | Dropdown: Ja / Nein |

Die Statistik-Seite nutzt die Bibliothek **Recharts** für interaktive Diagramme und bietet eine Jahresauswahl (aktuelles Jahr als Standard, 5 Jahre zurück).

### AGT-Tauglichkeit

Im Bereich **Mitgliederakte → Untersuchungen** gibt es eine laufende Tabelle "AGT-Tauglichkeit" für jeden AGT. Dort werden erfasst:

| Typ | Beschreibung |
|---|---|
| G26 Untersuchung | Arbeitsmed. Untersuchung (mit Ergebnis: geeignet/nicht geeignet) |
| AGT Belastungsübung | Jährliche Belastungsübung auf der Atemschutzstrecke |
| AGT Einsatzübung | Praktische Einsatzübung unter Atemschutz |
| AGT Einsatz | Realer Einsatz unter Atemschutz |

Die **Tauglichkeit wird automatisch berechnet** nach folgenden Regeln:

```
NICHT TAUGLICH wenn:
- Alter < 50 UND G26 älter als 3 Jahre
- Alter >= 50 UND G26 älter als 1 Jahr
- Belastungsübung älter als 1 Jahr
- Einsatzübung UND Einsatz beide älter als 1 Jahr
```

Die Ampel (grün/rot) zeigt den aktuellen Status mit Begründung an.

### Brandsicherheitswache (Checkliste + Bericht)

Für Veranstaltungen der Kategorie **BSW** gibt es unter **Veranstaltungen → Detail → Tab "BSW"** ein Formular für die 18-Punkte-Checkliste und den Wachbericht (Veranstaltungsort, Wachzeiten, Wachhabender/Wachposten, Mängel, Vorkommnisse). "Formular speichern" sichert den Stand an der Veranstaltung; die beiden Buttons "Als PDF erzeugen" füllen die Word-Vorlagen mit den gespeicherten Daten und legen das PDF im Tab "Dokumente" ab (technisch identisch zur Einsatzbericht-Erzeugung: PizZip + docxtemplater + LibreOffice-Konvertierung).

**Voraussetzung:** die beiden Vorlagen `data/Templates/ChecklisteBrandsicherheitswacheLB12.docx` und `data/Templates/BerichtBrandsicherheitswacheLB12.docx` müssen einmalig unter **Einstellungen → Templates** hochgeladen werden — mit Namen, die "Checkliste Brandsicherheitswache" bzw. "Bericht Brandsicherheitswache" enthalten (Namenssuche ist ein Teilstring-Match).

---

## Migration vom Java-System

Für die Migration von der alten Java-Swing-Anwendung:

```bash
# Stellen Sie sicher, dass die neue DB-Schema eingespielt ist
# (passiert automatisch durch install.sh)

sudo bash /var/www/fuerwehr/scripts/migrate-from-java.sh
```

Das Migrationsskript überträgt:
- Berechtigungsgruppen (BR0–BR75)
- Mitglieder (Stammdaten)
- Dienstgrade
- Benutzer (Passwörter werden **nicht** migriert, alle erhalten ein Standard-Passwort)
- Lager und Ausbildungskategorien
- Abwesenheitsgründe
- Einsätze (Grunddaten)
- Veranstaltungen (Grunddaten)
- Anwesenheitseinträge

> **Hinweis:** Alle migrierten Benutzer erhalten ein temporäres Standard-Passwort
> und werden beim ersten Login zur Änderung aufgefordert.

---

## Nginx & HTTPS

### HTTPS mit Let's Encrypt (Certbot)

```bash
# Certbot installieren
apt-get install -y certbot python3-certbot-nginx

# Zertifikat ausstellen und Nginx automatisch konfigurieren
certbot --nginx -d ihre-domain.de

# Automatische Verlängerung testen
certbot renew --dry-run
```

### Nginx-Konfiguration anpassen

```bash
# Konfigurationsdatei bearbeiten
nano /etc/nginx/sites-available/fuerwehr

# Syntax prüfen und neu laden
nginx -t && systemctl reload nginx
```

Eine Vorlage für die Nginx-Konfiguration befindet sich unter `deploy/nginx/fuerwehr.conf`.

---

## Fehlerbehebung

### Backend startet nicht

```bash
# Logs prüfen
pm2 logs fuerwehr-backend --lines 50

# .env prüfen
cat /var/www/fuerwehr/backend/.env

# Manuell testen
cd /var/www/fuerwehr/backend
node dist/server.js
```

### Datenbankverbindung schlägt fehl

```bash
# MySQL-Status prüfen
systemctl status mysql

# Verbindung testen
mysql -u fuerwehr -p fuerwehr -e "SELECT 1"

# Datenbankbenutzer prüfen
mysql -u root -p -e "SHOW GRANTS FOR 'fuerwehr'@'localhost'"
```

### Frontend-Fehler (404 bei Seitenaufruf)

Nginx-SPA-Fallback prüfen:
```bash
# Konfiguration prüfen
grep -A3 "location /" /etc/nginx/sites-available/fuerwehr
# Muss enthalten: try_files $uri $uri/ /index.html;

# Frontend-Dateien vorhanden?
ls /var/www/fuerwehr/frontend/
```

### Backend-Port belegt

```bash
# Welcher Prozess nutzt Port 3001?
lsof -i :3001
# oder
ss -tlnp | grep 3001
```

### Disk-Space

```bash
# Gesamtüberblick
df -h

# Backup-Größe
du -sh /var/www/fuerwehr/backups/

# Logs-Größe
du -sh /var/log/fuerwehr/

# Alte Logs manuell löschen
pm2 flush fuerwehr-backend
```

---

## Hilfreiche Befehle

| Befehl | Beschreibung |
|---|---|
| `pm2 status` | Übersicht aller Prozesse |
| `pm2 monit` | Interaktives Monitoring |
| `pm2 logs fuerwehr-backend` | Backend-Logs (Echtzeit) |
| `pm2 reload fuerwehr-backend` | Graceful Reload |
| `systemctl status nginx` | Nginx-Status |
| `systemctl reload nginx` | Nginx neu laden |
| `systemctl status mysql` | MySQL-Status |
| `/var/www/fuerwehr/scripts/backup.sh` | Manuelles Backup |
| `/var/www/fuerwehr/scripts/update.sh` | System aktualisieren |
| `/var/www/fuerwehr/scripts/restore.sh` | Aus Backup wiederherstellen |
