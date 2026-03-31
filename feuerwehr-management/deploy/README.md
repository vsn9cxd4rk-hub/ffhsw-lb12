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
