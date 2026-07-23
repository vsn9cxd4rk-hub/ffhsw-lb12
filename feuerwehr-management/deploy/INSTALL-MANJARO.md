# Feuerwehr Management System - Installationsanleitung Manjaro Linux

---

## Voraussetzungen

- Manjaro Linux (aktuell)
- Root-/sudo-Zugriff
- Internetzugang (fuer Paketinstallation)

---

## 1. Systempakete installieren

```bash
# System aktualisieren
sudo pacman -Syu

# Benoetigte Pakete installieren
sudo pacman -S nodejs npm mysql nginx git base-devel

# Node.js Version pruefen (mindestens v18, empfohlen v20+)
node --version
npm --version
```

Falls Node.js 20 nicht verfuegbar ist:

```bash
# Alternative: nvm (Node Version Manager)
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash
source ~/.bashrc
nvm install 20
nvm use 20
```

---

## 2. MySQL / MariaDB einrichten

```bash
# MariaDB installieren und starten (Manjaro bevorzugt MariaDB)
sudo pacman -S mariadb
sudo mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
sudo systemctl start mariadb
sudo systemctl enable mariadb

# Sicherheitskonfiguration
sudo mysql_secure_installation
```

### Datenbank und Benutzer anlegen

```bash
sudo mysql -u root -p
```

```sql
-- Datenbank anlegen
CREATE DATABASE IF NOT EXISTS FFWVSLB12
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Benutzer anlegen
CREATE USER IF NOT EXISTS 'FFWVSLB12'@'localhost' IDENTIFIED BY 'Ffw#VSLB12!25';
GRANT ALL PRIVILEGES ON FFWVSLB12.* TO 'FFWVSLB12'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### Datenbankschema importieren

```bash
sudo mysql -u root -p < /pfad/zum/projekt/deploy/init-db.sql
```

---

## 3. Anwendungsbenutzer und Verzeichnisse

```bash
# Benutzer anlegen (optional, fuer Produktionsbetrieb empfohlen)
sudo useradd -r -m -s /bin/bash lb12admin

# Verzeichnisstruktur
sudo mkdir -p /var/www/feuerwehrmanagement/{backend,frontend,uploads,backups}
sudo mkdir -p /var/log/feuerwehrmanagement

# Rechte setzen
sudo chown -R lb12admin:lb12admin /var/www/feuerwehrmanagement
sudo chown -R lb12admin:lb12admin /var/log/feuerwehrmanagement
```

---

## 4. Backend installieren

```bash
# Als lb12admin arbeiten
sudo su - lb12admin

# Quelldateien kopieren
cp -r /pfad/zum/projekt/webapp/backend/* /var/www/feuerwehrmanagement/backend/
cd /var/www/feuerwehrmanagement/backend

# Dependencies installieren
npm install --production=false

# TypeScript kompilieren
npm run build

# Prisma Client generieren
npm run prisma:generate
```

### Konfiguration (.env)

```bash
cp .env.example .env
chmod 600 .env
nano .env
```

Folgende Werte anpassen:

```env
NODE_ENV=production
PORT=3001

# MySQL - Achtung: Sonderzeichen im Passwort URL-encoden!
# '#' wird zu '%23'
DATABASE_URL="mysql://FFWVSLB12:Ffw%23VSLB12!25@localhost:3306/FFWVSLB12"

# JWT Secrets - mindestens 32 Zeichen, fuer Produktion zufaellig generieren!
# Generieren mit: openssl rand -hex 32
JWT_ACCESS_SECRET=<hier-zufaelligen-hex-string-einfuegen>
JWT_REFRESH_SECRET=<hier-anderen-zufaelligen-hex-string-einfuegen>
JWT_ACCESS_EXPIRES=15m
JWT_REFRESH_EXPIRES=7d

CORS_ORIGIN=http://localhost:3000
UPLOAD_PATH=/var/www/feuerwehrmanagement/uploads
LOG_LEVEL=info
LOG_PATH=/var/log/feuerwehrmanagement
```

### Seed-Daten laden (Admin-Benutzer etc.)

```bash
npm run prisma:seed
```

### Testen ob Backend startet

```bash
npm start
# Sollte "Server running on port 3001" ausgeben
# Mit Ctrl+C beenden
```

---

## 5. Frontend bauen

```bash
cd /pfad/zum/projekt/webapp/frontend

# Dependencies installieren
npm install

# Produktions-Build erstellen
npm run build

# Build-Output kopieren
cp -r dist/* /var/www/feuerwehrmanagement/frontend/
```

### Feuerwehr-Logo kopieren

```bash
cp /pfad/zum/projekt/images/LB12.png /var/www/feuerwehrmanagement/frontend/LB12.png
```

---

## 6. PM2 Prozessmanager

```bash
# PM2 global installieren
sudo npm install -g pm2

# Ecosystem-Config kopieren
cp /pfad/zum/projekt/deploy/ecosystem.config.js /var/www/feuerwehrmanagement/

# Backend starten
cd /var/www/feuerwehrmanagement
pm2 start ecosystem.config.js

# Status pruefen
pm2 status
pm2 logs feuerwehr-backend

# PM2 beim Systemstart automatisch starten
pm2 save
pm2 startup systemd
# Den ausgegebenen Befehl als root ausfuehren!
```

---

## 7. Nginx konfigurieren

Manjaro verwendet standardmaessig **keine** `sites-available/sites-enabled`-Struktur
wie Debian/Ubuntu. Stattdessen wird ueber `conf.d/` eingebunden.

### Schritt 1: Konfigurationsdatei kopieren

```bash
sudo mkdir -p /etc/nginx/conf.d
sudo cp /pfad/zum/projekt/deploy/nginx/fuerwehr.conf /etc/nginx/conf.d/feuerwehr.conf
```

### Schritt 2: server_name anpassen

```bash
sudo nano /etc/nginx/conf.d/feuerwehr.conf
```

`server_name` aendern auf `localhost` oder die eigene Domain/IP.

### Schritt 3: nginx.conf pruefen

```bash
sudo nano /etc/nginx/nginx.conf
```

Sicherstellen, dass im `http { ... }`-Block diese Zeile vorhanden ist:

```nginx
http {
    # ... bestehende Eintraege ...

    include /etc/nginx/conf.d/*.conf;
}
```

**Wichtig:** Falls in der nginx.conf bereits ein `server`-Block mit `listen 80`
existiert (Manjaro-Default), diesen auskommentieren oder entfernen — sonst
gibt es einen Portkonflikt mit der feuerwehr.conf.

### Nginx starten

```bash
# Konfiguration testen
sudo nginx -t

# Starten und aktivieren
sudo systemctl start nginx
sudo systemctl enable nginx
```

---

## 8. Firewall (optional)

```bash
# UFW installieren falls gewuenscht
sudo pacman -S ufw
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw enable
```

---

## 9. SSL/TLS mit Let's Encrypt (optional, fuer oeffentlichen Zugriff)

```bash
sudo pacman -S certbot certbot-nginx
sudo certbot --nginx -d deine-domain.de
```

---

## 10. Erster Login

1. Browser oeffnen: `http://<server-ip>` oder `http://localhost`
2. Anmelden mit:
   - **Benutzername:** `admin`
   - **Passwort:** `Admin123!`
3. **Sofort das Passwort aendern!** (Rechts oben auf Benutzername klicken > Passwort aendern)

---

## Wartung

### Backend neustarten

```bash
pm2 restart feuerwehr-backend
```

### Logs anzeigen

```bash
pm2 logs feuerwehr-backend
# oder
tail -f /var/log/feuerwehrmanagement/pm2-out.log
```

### Datenbank-Backup

```bash
mysqldump -u FFWVSLB12 -p FFWVSLB12 > /var/www/feuerwehrmanagement/backups/backup-$(date +%Y%m%d).sql
```

### Update einspielen

```bash
# 1. Neuen Quellcode holen
# 2. Backend neu bauen
cd /var/www/feuerwehrmanagement/backend
npm install --production=false
npm run build
npm run prisma:generate
pm2 restart feuerwehr-backend

# 3. Frontend neu bauen
cd /pfad/zum/projekt/webapp/frontend
npm run build
cp -r dist/* /var/www/feuerwehrmanagement/frontend/
```

---

## Verzeichnisstruktur (nach Installation)

```
/var/www/feuerwehrmanagement/
  backend/                 # Node.js Backend
    dist/                  # Kompiliertes JavaScript
    node_modules/
    prisma/                # Datenbankschema
    .env                   # Konfiguration (chmod 600!)
  frontend/                # React SPA (statische Dateien)
  uploads/                 # Datei-Uploads
  backups/                 # Datenbank-Backups
  ecosystem.config.js      # PM2 Konfiguration

/var/log/feuerwehrmanagement/
  pm2-out.log              # Backend Stdout
  pm2-error.log            # Backend Stderr
```

---

## Berechtigungsgruppen

| Gruppe | Zugriff |
|--------|---------|
| Administrator | Alles + Benutzerverwaltung |
| Geraetewarte | Dashboard, Fahrzeuge, Bestandsliste, Pruefbuch |
| Benutzer | Dashboard, Personal (eigene), Veranstaltungen, Ausbildung |

---

## Ports

| Dienst | Port |
|--------|------|
| Nginx (HTTP) | 80 |
| Nginx (HTTPS) | 443 |
| Backend (intern) | 3001 |
| MySQL/MariaDB | 3306 |

---

## System-Migration (Klonen auf ein neues System)

Falls ein bestehendes System auf einen neuen Server umgezogen werden soll,
muessen nur drei Dinge vom alten System gesichert werden.
Frontend und Backend werden aus dem Quellcode neu gebaut.

### Was gesichert werden muss

| Was | Pfad | Inhalt |
|-----|------|--------|
| Datenbank-Dump | `mysqldump` | Alle Daten (Mitglieder, Artikel, Pruefungen, etc.) |
| Uploads-Verzeichnis | `/var/www/feuerwehrmanagement/uploads/` | PDFs (Pruefprotokolle, Artikel-Dokumente, Templates) |
| Backend .env | `/var/www/feuerwehrmanagement/backend/.env` | DB-Credentials, JWT-Secrets, Pfade |

### Schritt 1: Backup auf dem ALTEN System

```bash
# Datenbank sichern
mysqldump -u FFWVSLB12 -p FFWVSLB12 > /tmp/db-backup.sql

# Uploads sichern
tar czf /tmp/uploads-backup.tar.gz -C /var/www/feuerwehrmanagement uploads/

# .env sichern
cp /var/www/feuerwehrmanagement/backend/.env /tmp/env-backup
```

### Schritt 2: Dateien auf das NEUE System uebertragen

```bash
scp /tmp/db-backup.sql user@neues-system:/tmp/
scp /tmp/uploads-backup.tar.gz user@neues-system:/tmp/
scp /tmp/env-backup user@neues-system:/tmp/
```

### Schritt 3: Auf dem NEUEN System einspielen

Voraussetzung: Die Grundinstallation (Abschnitte 1-7 dieser Anleitung)
wurde bereits durchgefuehrt.

```bash
# Datenbank importieren (ueberschreibt init-db.sql Daten)
mysql -u FFWVSLB12 -p FFWVSLB12 < /tmp/db-backup.sql

# Uploads wiederherstellen
tar xzf /tmp/uploads-backup.tar.gz -C /var/www/feuerwehrmanagement/
chown -R lb12admin:lb12admin /var/www/feuerwehrmanagement/uploads

# .env einspielen und ggf. anpassen
cp /tmp/env-backup /var/www/feuerwehrmanagement/backend/.env
nano /var/www/feuerwehrmanagement/backend/.env
# Pruefen/anpassen: CORS_ORIGIN (neue IP/Domain), UPLOAD_PATH
```

### Schritt 4: Backend und Frontend neu bauen

```bash
# Backend
cd /var/www/feuerwehrmanagement/backend
npm install
npm run prisma:generate
npm run build
pm2 restart feuerwehr-backend

# Frontend
cd /pfad/zum/projekt/webapp/frontend
npm install
npm run build
cp -r dist/* /var/www/feuerwehrmanagement/frontend/
```

### Schritt 5: Verifizierung

```bash
# Backend laeuft?
pm2 status
curl http://localhost:3001/api/settings

# Nginx laeuft?
sudo systemctl status nginx

# Browser: Login testen mit bestehenden Zugangsdaten
```

### Was NICHT gesichert werden muss

- `node_modules/` — wird durch `npm install` neu erzeugt
- `dist/` — wird durch `npm run build` neu erzeugt
- Frontend-Quellcode — wird aus dem Repository/Quellpaket neu gebaut
- Prisma Client — wird durch `prisma generate` neu erzeugt

---

---

## Update: Modul "Einsatzstatistik" (v1.1.0)

Die Einsatzstatistik bietet interaktive Diagramme und Auswertungen
(Ortsteile, Stichworte, Tageszeiten, Wochentage, Personal-Einsaetze).

### Voraussetzungen

- Migration `deploy/migrate-add-report-fields.sql` muss ausgefuehrt sein
- Frontend: `npm install` (installiert Recharts-Bibliothek)

### Zugriff

Nur **Administratoren** und **Gruppenfuehrer** sehen den Menuepunkt "Statistik".

### Backend-Tests ausfuehren (Nightly auf Backup-System)

```bash
chmod +x scripts/run-backend-tests.sh
./scripts/run-backend-tests.sh --notify

# Als Cron (05:00, nach naechtlichem Restore):
# 0 5 * * * /var/www/feuerwehrmanagement/scripts/run-backend-tests.sh --notify
```

---

*Feuerwehr Management System - Installationsanleitung fuer Manjaro Linux*
*Erstellt: April 2026 | Aktualisiert: Juli 2026*
