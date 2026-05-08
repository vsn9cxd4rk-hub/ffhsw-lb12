# Feuerwehr Management System – Entwicklungs-Setup unter Windows

Diese Anleitung beschreibt, wie das Projekt **lokal auf Windows entwickelt** wird
(Hot-Reload, Debug, ohne Webserver und PM2).
Für den produktiven Server-Betrieb siehe `INSTALL-MANJARO.md`.

---

## Voraussetzungen

- Windows 10 (ab Version 1709) oder Windows 11
- PowerShell 5.1 oder neuer (vorinstalliert)
- Administratorrechte (für Paketinstallationen)
- Internetzugang
- `winget` (Windows Package Manager) – ab Windows 10 21H2 / Windows 11 vorinstalliert;
  bei älteren Versionen über den Microsoft Store installieren

---

## Skript ausführen (empfohlen)

Das mitgelieferte PowerShell-Skript erledigt alle Schritte automatisch.

1. **PowerShell als Administrator öffnen**
   - Startmenü → `PowerShell` → Rechtsklick → *Als Administrator ausführen*

2. **Ausführungsrichtlinie einmalig erlauben**
   ```powershell
   Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned -Force
   ```

3. **Skript starten**
   ```powershell
   cd C:\pfad\zum\projekt\feuerwehr-management\deploy
   .\install-windows.ps1
   ```

---

## Manuelle Installation (Schritt für Schritt)

### 1. Node.js installieren

```powershell
winget install OpenJS.NodeJS.LTS
```

Danach neues Terminal öffnen und prüfen:

```powershell
node --version   # sollte v20.x.x oder neuer zeigen
npm --version
```

---

### 2. MariaDB installieren

```powershell
winget install MariaDB.Server
```

Der Installer fragt nach einem Root-Passwort – dieses notieren.

Dienst prüfen:

```powershell
Get-Service -Name "MariaDB" | Select-Object Name, Status
```

Falls der Dienst nicht läuft:

```powershell
Start-Service -Name "MariaDB"
```

---

### 3. Datenbank und Benutzer anlegen

```powershell
# In das Projektverzeichnis wechseln
cd C:\pfad\zum\projekt\feuerwehr-management

# Init-Skript einspielen (Root-Passwort wird abgefragt)
mysql -u root -p < deploy\init-db.sql
```

Verbindung testen:

```powershell
mysql -u FFWVSLB12 -p"Ffw#VSLB12!25" FFWVSLB12 -e "SHOW TABLES;"
```

---

### 4. Backend einrichten

```powershell
cd webapp\backend

# Abhängigkeiten installieren
npm install
```

#### `.env` anlegen

Datei `webapp\backend\.env` mit folgendem Inhalt erstellen:

```env
NODE_ENV=development
PORT=3001

# Sonderzeichen im Passwort URL-encoden: # -> %23
DATABASE_URL="mysql://FFWVSLB12:Ffw%23VSLB12!25@localhost:3306/FFWVSLB12"

# Lokal beliebige 32+ Zeichen – für Produktion echte Zufallswerte verwenden
# Erzeugen mit: node -e "console.log(require('crypto').randomBytes(48).toString('base64'))"
JWT_ACCESS_SECRET=dev-access-secret-bitte-mindestens-32-zeichen-lang
JWT_REFRESH_SECRET=dev-refresh-secret-bitte-mindestens-32-zeichen-lang
JWT_ACCESS_EXPIRES=15m
JWT_REFRESH_EXPIRES=7d

CORS_ORIGIN=http://localhost:3000

UPLOAD_PATH=./uploads
LOG_LEVEL=debug
LOG_PATH=./logs

SMTP_HOST=
SMTP_PORT=587
SMTP_SECURE=false
SMTP_USER=
SMTP_PASS=
SMTP_FROM=noreply@feuerwehr.local
```

#### Prisma und Seed

```powershell
# Lokale Verzeichnisse anlegen
New-Item -ItemType Directory -Force -Path uploads, logs

# Prisma-Client generieren
npm run prisma:generate

# Schema in die Datenbank schieben
npm run prisma:push

# Seed-Daten einspielen (Admin-Benutzer)
npm run prisma:seed
```

#### Backend testweise starten

```powershell
npm run dev
# Erwartete Ausgabe: Server running on port 3001
# Mit Ctrl+C beenden
```

---

### 5. Frontend einrichten

In einem **zweiten Terminal**:

```powershell
cd C:\pfad\zum\projekt\feuerwehr-management\webapp\frontend

npm install
npm run dev
```

Der Vite-Dev-Server startet auf `http://localhost:3000` und leitet alle
`/api/*`-Anfragen automatisch an das Backend auf Port 3001 weiter.

---

## Entwicklungs-Workflow

Für eine produktive Sitzung zwei Terminals öffnen:

| Terminal | Befehl | Was es tut |
|---|---|---|
| 1 (Backend) | `cd webapp\backend && npm run dev` | TypeScript-Server mit Auto-Reload |
| 2 (Frontend) | `cd webapp\frontend && npm run dev` | Vite-Dev-Server mit Hot-Module-Replacement |

Im Browser öffnen: <http://localhost:3000>

### Erstanmeldung

| Feld | Wert |
|---|---|
| Benutzername | `admin` |
| Passwort | `Admin123!` |

> **Direkt nach dem ersten Login das Passwort ändern.**

---

## Nützliche Befehle

### MariaDB verwalten

```powershell
Start-Service MariaDB          # Starten
Stop-Service MariaDB           # Stoppen
Restart-Service MariaDB        # Neustarten
Get-Service MariaDB            # Status
```

### Datenbank visuell ansehen

```powershell
cd webapp\backend
npx prisma studio
# Öffnet Browser-Tab auf http://localhost:5555
```

### node_modules zurücksetzen

```powershell
# Backend
cd webapp\backend
Remove-Item -Recurse -Force node_modules, dist -ErrorAction SilentlyContinue
npm install

# Frontend
cd webapp\frontend
Remove-Item -Recurse -Force node_modules, dist -ErrorAction SilentlyContinue
npm install
```

### Datenbank zurücksetzen

```powershell
mysql -u root -p -e "DROP DATABASE FFWVSLB12; CREATE DATABASE FFWVSLB12 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -u root -p < deploy\init-db.sql
cd webapp\backend
npm run prisma:push
npm run prisma:seed
```

---

## Fehlerbehebung

### `winget` nicht gefunden

`winget` über den Microsoft Store installieren: Suche nach **App-Installer**.
Alternativ Node.js und MariaDB direkt von den offiziellen Webseiten herunterladen.

### Port 3001 / 3000 belegt

```powershell
# Welcher Prozess belegt den Port?
netstat -ano | findstr ":3001"

# Prozess beenden (PID aus der Ausgabe einsetzen)
Stop-Process -Id <PID> -Force
```

### MariaDB-Dienst startet nicht

```powershell
# Ereignisprotokoll prüfen
Get-EventLog -LogName Application -Source "MariaDB" -Newest 10
```

### `mysql` wird nicht gefunden

MariaDB-Bin-Verzeichnis manuell zum PATH hinzufügen:

```powershell
# Pfad anpassen falls abweichend
$env:PATH += ";C:\Program Files\MariaDB 11.4\bin"
```

### EACCES / Berechtigungsfehler bei npm

```powershell
# npm-Cache leeren und neu installieren
npm cache clean --force
npm install
```

### CORS-Fehler im Browser

`CORS_ORIGIN` in `webapp\backend\.env` muss exakt `http://localhost:3000` lauten
(kein abschließender Slash). Backend nach Änderung neu starten.

---

## Ports im Dev-Setup

| Dienst | Port | Aufruf |
|---|---|---|
| Frontend (Vite) | 3000 | <http://localhost:3000> |
| Backend (Express) | 3001 | <http://localhost:3001/api/health> |
| MariaDB | 3306 | `mysql -u FFWVSLB12 -p` |
| Prisma Studio | 5555 | <http://localhost:5555> (nur wenn manuell gestartet) |

---

*Feuerwehr Management System – Entwicklungs-Setup für Windows*
*Erstellt: Mai 2026*
