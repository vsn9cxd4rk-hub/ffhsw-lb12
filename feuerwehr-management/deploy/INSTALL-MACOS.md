# Feuerwehr Management System – Entwicklungs-Setup unter macOS

Diese Anleitung beschreibt, wie das Projekt **lokal auf einem Mac
entwickelt** wird (Hot-Reload, Debug, ohne nginx und PM2).
Für den produktiven Server-Betrieb siehe `INSTALL-MANJARO.md` bzw.
`README.md` in diesem Ordner.

---

## Voraussetzungen

- macOS 13 (Ventura) oder neuer – Apple Silicon und Intel werden unterstützt
- Adminrechte (für Homebrew-Installationen)
- Internetzugang
- Empfohlen: aktueller Browser (Safari, Chrome, Firefox)

---

## 1. Homebrew installieren

Falls Homebrew noch nicht vorhanden ist:

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

Anschließend prüfen:

```bash
brew --version
```

> **Apple Silicon (M1/M2/M3/M4):** Homebrew installiert nach `/opt/homebrew`.
> Falls `brew` nach der Installation nicht gefunden wird, einmal ausführen:
> ```bash
> echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
> eval "$(/opt/homebrew/bin/brew shellenv)"
> ```

---

## 2. Pakete installieren

```bash
# Node.js 20, MySQL/MariaDB, Git
brew install node@20 mariadb git

# node@20 in PATH hängen
brew link --overwrite --force node@20

# Versionen prüfen
node --version    # sollte v20.x.x zeigen
npm --version
git --version
```

Optional aber empfohlen:

```bash
brew install --cask visual-studio-code   # oder dein Lieblings-Editor
brew install gh                          # GitHub CLI (für Push aus dem Terminal)
```

---

## 3. MariaDB starten

MariaDB ist auf macOS der praktischere Drop-in-Ersatz für MySQL und das
Schema (Prisma-Provider `mysql`) funktioniert problemlos.

```bash
# Dienst starten und beim Login automatisch starten lassen
brew services start mariadb

# Status prüfen
brew services list | grep mariadb

# Optional: Sicherheitskonfiguration durchlaufen
mariadb-secure-installation
```

> **Hinweis:** Beim ersten `mariadb-secure-installation` einfach `Enter`
> drücken, um das (leere) Root-Passwort zu akzeptieren, dann ein neues
> Passwort vergeben.

---

## 4. Datenbank und Benutzer anlegen

Das fertige Init-Skript aus `deploy/init-db.sql` einspielen:

```bash
# In das Projektverzeichnis wechseln
cd /pfad/zum/projekt/feuerwehr-management

# Init-Skript einspielen (Root-Passwort wird abgefragt)
mariadb -u root -p < deploy/init-db.sql
```

Das Skript legt an:

- Datenbank `FFWVSLB12` (utf8mb4 / utf8mb4_unicode_ci)
- Benutzer `FFWVSLB12@localhost` mit Passwort `Ffw#VSLB12!25`

Verbindung testen:

```bash
mariadb -u FFWVSLB12 -p'Ffw#VSLB12!25' FFWVSLB12 -e "SHOW TABLES;"
```

---

## 5. Backend einrichten

```bash
cd /pfad/zum/projekt/feuerwehr-management/webapp/backend

# Abhängigkeiten installieren (inkl. devDependencies)
npm install
```

### `.env` anlegen

```bash
cp .env.example .env
```

Datei `.env` öffnen und für die lokale Entwicklung anpassen:

```env
NODE_ENV=development
PORT=3001

# Sonderzeichen (#) im Passwort URL-codieren: # -> %23
DATABASE_URL="mysql://FFWVSLB12:Ffw%23VSLB12!25@localhost:3306/FFWVSLB12"

# Lokal sind beliebige 32+ Zeichen okay – fuer Produktion echte Zufallswerte!
# Erzeugen mit:  openssl rand -hex 32
JWT_ACCESS_SECRET=dev-access-secret-bitte-mindestens-32-zeichen-lang
JWT_REFRESH_SECRET=dev-refresh-secret-bitte-mindestens-32-zeichen-lang
JWT_ACCESS_EXPIRES=15m
JWT_REFRESH_EXPIRES=7d

# Frontend laeuft im Dev-Server auf Port 3000
CORS_ORIGIN=http://localhost:3000

# Lokale Pfade (innerhalb des Projekts)
UPLOAD_PATH=./uploads
LOG_LEVEL=debug
LOG_PATH=./logs

# SMTP optional – leer lassen, dann werden keine Mails versendet
SMTP_HOST=
SMTP_PORT=587
SMTP_SECURE=false
SMTP_USER=
SMTP_PASS=
SMTP_FROM=noreply@feuerwehr.local
```

```bash
# Lokale Verzeichnisse anlegen
mkdir -p uploads logs
```

### Prisma-Client generieren und Datenbank befüllen

```bash
# TypeScript-Client fuer Prisma erzeugen
npm run prisma:generate

# Schema in die Datenbank schieben (idempotent, nicht-destruktiv)
npm run prisma:push

# Seed-Daten einspielen (Admin-Benutzer, Berechtigungsgruppen)
npm run prisma:seed
```

### Backend testweise starten

```bash
npm run dev
```

Erwartete Ausgabe enthält etwa:

```
Server running on port 3001
```

Mit `Ctrl+C` beenden.

---

## 6. Frontend einrichten

In einem **zweiten Terminal**:

```bash
cd /pfad/zum/projekt/feuerwehr-management/webapp/frontend

npm install
npm run dev
```

Der Vite-Dev-Server startet auf `http://localhost:3000` und leitet alle
Anfragen an `/api/*` automatisch an das Backend auf Port 3001 weiter
(siehe `vite.config.ts`).

---

## 7. Entwicklungs-Workflow

Für eine produktive Sitzung brauchst du **drei Terminals**:

| Terminal | Befehl | Was es tut |
|---|---|---|
| 1 (Backend) | `cd webapp/backend && npm run dev` | TypeScript-Server mit Auto-Reload (`ts-node-dev`) |
| 2 (Frontend) | `cd webapp/frontend && npm run dev` | Vite-Dev-Server mit Hot-Module-Replacement |
| 3 (DB / Tools) | frei für `mariadb`, `npx prisma studio`, Git, Tests |

Im Browser öffnen: <http://localhost:3000>

### Erstanmeldung

| Feld | Wert |
|---|---|
| Benutzername | `admin` |
| Passwort | `Admin123!` |

> **Direkt nach dem ersten Login das Passwort ändern.**

### Datenbank visuell ansehen

```bash
cd webapp/backend
npx prisma studio
```

Öffnet einen Browser-Tab auf <http://localhost:5555> mit einem
Tabellen-Browser für alle Prisma-Modelle.

### Schema-Änderungen

Wenn du `prisma/schema.prisma` änderst:

```bash
cd webapp/backend
npm run prisma:push      # Schema-Änderung in lokale DB schieben
npm run prisma:generate  # TypeScript-Typen aktualisieren
```

> Für Produktions-Migrationen würde stattdessen `prisma migrate dev`
> bzw. `prisma migrate deploy` eingesetzt – im lokalen Dev ist `push`
> aber bequemer und reicht aus.

---

## 8. Tests ausführen

End-to-End-Tests (Playwright) laufen gegen das laufende Frontend:

```bash
cd webapp/frontend

# Browsers einmalig herunterladen
npx playwright install

# Backend + Frontend müssen laufen, dann:
npm run test:e2e          # headless
npm run test:e2e:ui       # interaktiver UI-Modus
```

---

## 9. Editor-Setup (optional)

### Visual Studio Code

Empfohlene Erweiterungen:

- **ESLint** (`dbaeumer.vscode-eslint`)
- **Prettier** (`esbenp.prettier-vscode`)
- **Prisma** (`Prisma.prisma`) – Syntax-Highlighting fürs Schema
- **Tailwind CSS IntelliSense** (`bradlc.vscode-tailwindcss`)
- **GitLens** (`eamodio.gitlens`)

Alle vier auf einmal installieren:

```bash
code --install-extension dbaeumer.vscode-eslint \
     --install-extension esbenp.prettier-vscode \
     --install-extension Prisma.prisma \
     --install-extension bradlc.vscode-tailwindcss \
     --install-extension eamodio.gitlens
```

### JetBrains WebStorm/IntelliJ

Funktioniert ohne Zusatzaufwand. „Open" auf `webapp/` zeigen, IntelliJ
erkennt Backend und Frontend als zwei npm-Module.

---

## 10. Wartung & nützliche Befehle

### MariaDB verwalten

```bash
brew services start mariadb     # Starten
brew services stop mariadb      # Stoppen
brew services restart mariadb   # Neustarten
brew services list              # Status aller brew-Dienste
```

### Backend / Frontend bereinigen

```bash
# Wenn etwas seltsam ist: node_modules wegwerfen und neu installieren
cd webapp/backend  && rm -rf node_modules dist && npm install
cd webapp/frontend && rm -rf node_modules dist && npm install
```

### Backend produktiv bauen (zur Probe)

```bash
cd webapp/backend
npm run build     # erzeugt dist/
npm start         # startet kompilierte Version
```

### Datenbank-Dump für Backup

```bash
mariadb-dump -u FFWVSLB12 -p'Ffw#VSLB12!25' FFWVSLB12 \
  > ~/Desktop/ffwvslb12-$(date +%Y%m%d).sql
```

### Datenbank zurücksetzen

```bash
cd webapp/backend
mariadb -u root -p -e "DROP DATABASE FFWVSLB12; CREATE DATABASE FFWVSLB12 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mariadb -u root -p < ../../deploy/init-db.sql
npm run prisma:push
npm run prisma:seed
```

---

## Ports im Dev-Setup

| Dienst | Port | Aufruf |
|---|---|---|
| Frontend (Vite) | 3000 | <http://localhost:3000> |
| Backend (Express) | 3001 | <http://localhost:3001/api/health> |
| MariaDB | 3306 | `mariadb -u FFWVSLB12 -p` |
| Prisma Studio | 5555 | <http://localhost:5555> (nur wenn manuell gestartet) |

---

## Fehlerbehebung

### `EADDRINUSE :::3001` – Port belegt

```bash
# Welcher Prozess hängt auf 3001?
lsof -i :3001

# Beenden (PID aus der Ausgabe einsetzen)
kill -9 <PID>
```

### Prisma-Fehler `P1001: Can't reach database server`

```bash
# Läuft MariaDB?
brew services list | grep mariadb

# Verbindung manuell testen
mariadb -u FFWVSLB12 -p'Ffw#VSLB12!25' FFWVSLB12 -e "SELECT 1;"
```

### Login schlägt fehl / Admin existiert nicht

```bash
cd webapp/backend
npm run prisma:seed
```

### `node-gyp` / native Module wollen nicht bauen

```bash
# Xcode Command Line Tools installieren
xcode-select --install
```

### CORS-Fehler im Browser

`CORS_ORIGIN` in `webapp/backend/.env` muss exakt `http://localhost:3000`
sein (kein Trailing Slash). Backend nach Änderung neu starten.

### Umlaute werden falsch gespeichert

Datenbank muss auf `utf8mb4` stehen. Prüfen mit:

```bash
mariadb -u FFWVSLB12 -p'Ffw#VSLB12!25' -e \
  "SELECT @@character_set_database, @@collation_database;" FFWVSLB12
```

Erwartet: `utf8mb4` / `utf8mb4_unicode_ci`.

---

## Verzeichnisstruktur (Dev-Setup)

```
feuerwehr-management/
├── webapp/
│   ├── backend/
│   │   ├── src/              # TypeScript-Quelltext (Express, Routen, Services)
│   │   ├── prisma/           # schema.prisma, seed.ts
│   │   ├── uploads/          # lokale Uploads (im .gitignore)
│   │   ├── logs/             # lokale Logs (im .gitignore)
│   │   ├── dist/             # erst nach 'npm run build' (im .gitignore)
│   │   ├── .env              # NICHT committen!
│   │   └── package.json
│   └── frontend/
│       ├── src/              # React + TypeScript
│       ├── public/
│       ├── tests/            # Playwright E2E
│       ├── dist/             # erst nach 'npm run build'
│       └── package.json
├── src/java/                 # alte Java-Anwendung (Archiv, nicht aktiv)
├── pom.xml                   # Maven-Build für die Java-Variante
└── deploy/                   # Server-Deployment-Skripte (Linux)
```

---

*Feuerwehr Management System – Entwicklungs-Setup für macOS*
*Erstellt: Mai 2026*
