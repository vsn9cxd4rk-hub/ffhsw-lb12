# =============================================================================
# Feuerwehr Management System - Entwicklungs-Setup
# Ziel: Windows 10/11 (Entwicklungsumgebung, kein Webserver)
# Ausführen als Administrator in PowerShell:
#   Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned -Force
#   .\install-windows.ps1
# =============================================================================
#Requires -Version 5.1
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

# --------------------------------------------------------------------------
# Hilfsfunktionen
# --------------------------------------------------------------------------
function Write-Info    { param($msg) Write-Host "[INFO]  $msg" -ForegroundColor Cyan }
function Write-Ok      { param($msg) Write-Host "[OK]    $msg" -ForegroundColor Green }
function Write-Warn    { param($msg) Write-Host "[WARN]  $msg" -ForegroundColor Yellow }
function Write-Err     { param($msg) Write-Host "[ERROR] $msg" -ForegroundColor Red; exit 1 }

function Test-Command {
    param($Name)
    return $null -ne (Get-Command $Name -ErrorAction SilentlyContinue)
}

# Warten bis ein Dienst den Status "Running" hat
function Wait-Service {
    param($ServiceName, [int]$TimeoutSec = 30)
    $elapsed = 0
    while ((Get-Service -Name $ServiceName -ErrorAction SilentlyContinue).Status -ne "Running") {
        if ($elapsed -ge $TimeoutSec) { Write-Err "Dienst '$ServiceName' hat nicht rechtzeitig gestartet." }
        Start-Sleep -Seconds 2
        $elapsed += 2
    }
}

# Befehle nach einer winget-Installation im PATH verfügbar machen
function Update-SessionPath {
    $machinePath = [System.Environment]::GetEnvironmentVariable("Path", "Machine")
    $userPath    = [System.Environment]::GetEnvironmentVariable("Path", "User")
    $env:PATH    = "$machinePath;$userPath"
}

# MariaDB-Bin-Verzeichnis suchen (winget installiert nach C:\Program Files\MariaDB x.y\bin)
function Get-MariaDbBin {
    $candidates = @(
        (Get-Item "C:\Program Files\MariaDB*\bin" -ErrorAction SilentlyContinue | Sort-Object Name -Descending | Select-Object -First 1),
        (Get-Item "C:\Program Files (x86)\MariaDB*\bin" -ErrorAction SilentlyContinue | Sort-Object Name -Descending | Select-Object -First 1)
    ) | Where-Object { $_ -ne $null }
    return ($candidates | Select-Object -First 1)?.FullName
}

# --------------------------------------------------------------------------
# Konfiguration
# --------------------------------------------------------------------------
$ScriptDir   = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectDir  = Split-Path -Parent $ScriptDir
$BackendDir  = Join-Path $ProjectDir "webapp\backend"
$FrontendDir = Join-Path $ProjectDir "webapp\frontend"

$DbNameDefault = "FFWVSLB12"
$DbUserDefault = "FFWVSLB12"
$DbPassDefault = "Ffw#VSLB12!25"

# --------------------------------------------------------------------------
# Administrator-Prüfung
# --------------------------------------------------------------------------
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()
).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Err "Dieses Skript muss als Administrator ausgeführt werden.`nPowerShell als Administrator öffnen und erneut ausführen."
}

Write-Host ""
Write-Host "==================================================================" -ForegroundColor White
Write-Host "  Feuerwehr Management System - Entwicklungs-Setup (Windows)" -ForegroundColor White
Write-Host "==================================================================" -ForegroundColor White
Write-Host ""

# --------------------------------------------------------------------------
# Konfigurationsabfragen
# --------------------------------------------------------------------------
$mysqlRootPass = Read-Host "MariaDB root Passwort (leer = noch kein Passwort gesetzt)"

$dbName = Read-Host "Datenbankname [$DbNameDefault]"
if ([string]::IsNullOrWhiteSpace($dbName)) { $dbName = $DbNameDefault }

$dbUser = Read-Host "Datenbankbenutzer [$DbUserDefault]"
if ([string]::IsNullOrWhiteSpace($dbUser)) { $dbUser = $DbUserDefault }

$dbPass = Read-Host "Datenbankpasswort für '$dbUser' [$DbPassDefault]"
if ([string]::IsNullOrWhiteSpace($dbPass)) { $dbPass = $DbPassDefault }

$jwtAccess  = Read-Host "JWT Access Secret (Enter für Zufallsschlüssel)"
$jwtRefresh = Read-Host "JWT Refresh Secret (Enter für Zufallsschlüssel)"
if ([string]::IsNullOrWhiteSpace($jwtAccess))  { $jwtAccess  = [Convert]::ToBase64String((1..64 | ForEach-Object { [byte](Get-Random -Max 256) })) }
if ([string]::IsNullOrWhiteSpace($jwtRefresh)) { $jwtRefresh = [Convert]::ToBase64String((1..64 | ForEach-Object { [byte](Get-Random -Max 256) })) }

Write-Host ""
Write-Info "Starte Setup mit:"
Write-Info "  DB:       $dbName@localhost"
Write-Info "  Backend:  $BackendDir"
Write-Info "  Frontend: $FrontendDir"
Write-Host ""
$confirm = Read-Host "Fortfahren? [j/N]"
if ($confirm -notmatch "^[jJyY]$") { Write-Info "Setup abgebrochen."; exit 0 }
Write-Host ""

# --------------------------------------------------------------------------
# winget prüfen
# --------------------------------------------------------------------------
if (-not (Test-Command "winget")) {
    Write-Err "winget (Windows Package Manager) nicht gefunden.`nBitte über den Microsoft Store installieren: App-Installer`nOder Node.js und MariaDB manuell installieren (siehe INSTALL-WINDOWS.md)."
}
Write-Ok "winget verfügbar"

# --------------------------------------------------------------------------
# Node.js installieren
# --------------------------------------------------------------------------
$nodeOk = Test-Command "node"
if ($nodeOk) {
    $nodeVer = [int](node -v).TrimStart("v").Split(".")[0]
    $nodeOk  = $nodeVer -ge 18
}

if (-not $nodeOk) {
    Write-Info "Node.js LTS wird installiert..."
    winget install --id OpenJS.NodeJS.LTS --accept-source-agreements --accept-package-agreements --silent
    Update-SessionPath
    if (-not (Test-Command "node")) {
        Write-Err "Node.js-Installation fehlgeschlagen. Bitte manuell von https://nodejs.org installieren."
    }
    Write-Ok "Node.js $(node -v) installiert"
} else {
    Write-Ok "Node.js $(node -v) bereits installiert"
}

# --------------------------------------------------------------------------
# MariaDB installieren
# --------------------------------------------------------------------------
$mariaService = Get-Service -Name "MariaDB" -ErrorAction SilentlyContinue
if ($null -eq $mariaService) {
    Write-Info "MariaDB wird installiert..."
    Write-Warn "Der Installer fragt nach einem Root-Passwort – das eingegebene Passwort merken!"
    winget install --id MariaDB.Server --accept-source-agreements --accept-package-agreements
    Update-SessionPath
    $mariaService = Get-Service -Name "MariaDB" -ErrorAction SilentlyContinue
    if ($null -eq $mariaService) {
        Write-Err "MariaDB-Dienst nicht gefunden. Bitte MariaDB manuell von https://mariadb.org installieren."
    }
    Write-Ok "MariaDB installiert"
} else {
    Write-Ok "MariaDB bereits installiert"
}

# Dienst starten falls nicht aktiv
if ($mariaService.Status -ne "Running") {
    Write-Info "MariaDB-Dienst wird gestartet..."
    Start-Service -Name "MariaDB"
    Wait-Service -ServiceName "MariaDB"
    Write-Ok "MariaDB gestartet"
} else {
    Write-Ok "MariaDB läuft bereits"
}

# MariaDB-Bin-Pfad in aktuelle Session aufnehmen
$mariaDbBin = Get-MariaDbBin
if ($null -ne $mariaDbBin -and $env:PATH -notlike "*$mariaDbBin*") {
    $env:PATH += ";$mariaDbBin"
}
if (-not (Test-Command "mysql")) {
    Write-Err "mysql.exe nicht im PATH gefunden. MariaDB-Bin-Verzeichnis prüfen: $mariaDbBin"
}

# --------------------------------------------------------------------------
# Datenbank initialisieren (via init-db.sql)
# --------------------------------------------------------------------------
Write-Info "Datenbank wird initialisiert..."

$sqlFile = Join-Path $ScriptDir "init-db.sql"
if (-not (Test-Path $sqlFile)) { Write-Err "init-db.sql nicht gefunden: $sqlFile" }

# Platzhalter ersetzen (Dateiname und Benutzer)
$sqlContent = Get-Content $sqlFile -Raw -Encoding UTF8
$sqlContent = $sqlContent -replace [regex]::Escape("'FFWVSLB12'@'localhost'"), "'$dbUser'@'localhost'"
$sqlContent = $sqlContent -replace "FFWVSLB12", $dbName
$sqlContent = $sqlContent -replace [regex]::Escape("Ffw#VSLB12!25"), $dbPass

$tmpSql = Join-Path $env:TEMP "feuerwehr-init-$([System.IO.Path]::GetRandomFileName()).sql"
$sqlContent | Out-File -FilePath $tmpSql -Encoding UTF8

try {
    if ([string]::IsNullOrWhiteSpace($mysqlRootPass)) {
        Get-Content $tmpSql | mysql -u root 2>&1
    } else {
        Get-Content $tmpSql | mysql -u root -p"$mysqlRootPass" 2>&1
    }
    if ($LASTEXITCODE -ne 0) { Write-Err "Datenbankinitialisierung fehlgeschlagen (init-db.sql)" }
} finally {
    Remove-Item $tmpSql -ErrorAction SilentlyContinue
}

Write-Ok "Datenbank '$dbName' und Benutzer '$dbUser' angelegt"

# --------------------------------------------------------------------------
# .env Datei für Backend erstellen
# --------------------------------------------------------------------------
if (-not (Test-Path $BackendDir)) { Write-Err "Backend-Verzeichnis nicht gefunden: $BackendDir" }

Write-Info "Backend .env wird erstellt..."

# Sonderzeichen im DB-Passwort URL-encoden
Add-Type -AssemblyName System.Web
$dbPassEncoded = [System.Web.HttpUtility]::UrlEncode($dbPass)

$envPath = Join-Path $BackendDir ".env"
@"
NODE_ENV=development
PORT=3001

DATABASE_URL="mysql://${dbUser}:${dbPassEncoded}@localhost:3306/${dbName}"

JWT_ACCESS_SECRET=$jwtAccess
JWT_REFRESH_SECRET=$jwtRefresh
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
"@ | Out-File -FilePath $envPath -Encoding UTF8

Write-Ok ".env erstellt ($envPath)"

# --------------------------------------------------------------------------
# Backend: Dependencies, Prisma, Seed
# --------------------------------------------------------------------------
Write-Info "Backend-Abhängigkeiten werden installiert..."
Set-Location $BackendDir
New-Item -ItemType Directory -Force -Path "uploads", "logs" | Out-Null
npm install
if ($LASTEXITCODE -ne 0) { Write-Err "npm install (Backend) fehlgeschlagen" }
Write-Ok "npm install (Backend) abgeschlossen"

Write-Info "Prisma-Client wird generiert..."
npm run prisma:generate
if ($LASTEXITCODE -ne 0) { Write-Err "prisma:generate fehlgeschlagen" }
Write-Ok "Prisma-Client generiert"

Write-Info "Datenbankschema wird synchronisiert (prisma db push)..."
npm run prisma:push
if ($LASTEXITCODE -ne 0) { Write-Err "prisma:push fehlgeschlagen" }
Write-Ok "Schema synchronisiert"

Write-Info "Initialdaten werden eingespielt (Admin-Benutzer)..."
npm run prisma:seed
if ($LASTEXITCODE -ne 0) {
    Write-Warn "Seed fehlgeschlagen – bitte manuell ausführen:"
    Write-Warn "  cd `"$BackendDir`" && npm run prisma:seed"
} else {
    Write-Ok "Seed-Daten eingespielt"
}

# --------------------------------------------------------------------------
# Frontend: Dependencies
# --------------------------------------------------------------------------
if (Test-Path $FrontendDir) {
    Write-Info "Frontend-Abhängigkeiten werden installiert..."
    Set-Location $FrontendDir
    npm install
    if ($LASTEXITCODE -ne 0) { Write-Err "npm install (Frontend) fehlgeschlagen" }
    Write-Ok "Frontend bereit"
} else {
    Write-Warn "Frontend-Verzeichnis nicht gefunden: $FrontendDir"
}

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
Write-Host ""
Write-Host "==================================================================" -ForegroundColor White
Write-Host "  Setup erfolgreich abgeschlossen!" -ForegroundColor Green
Write-Host "==================================================================" -ForegroundColor White
Write-Host ""
Write-Host "  Entwicklungsserver starten (je ein Terminal):" -ForegroundColor White
Write-Host ""
Write-Host "    Terminal 1 - Backend:" -ForegroundColor Cyan
Write-Host "      cd `"$BackendDir`""
Write-Host "      npm run dev"
Write-Host ""
Write-Host "    Terminal 2 - Frontend:" -ForegroundColor Cyan
Write-Host "      cd `"$FrontendDir`""
Write-Host "      npm run dev"
Write-Host ""
Write-Host "  Browser öffnen: http://localhost:3000" -ForegroundColor White
Write-Host ""
Write-Host "  Standard-Anmeldedaten:" -ForegroundColor White
Write-Host "    Benutzername: admin"
Write-Host "    Passwort:     Admin123!"
Write-Host ""
Write-Host "  WICHTIG: Bitte das Passwort sofort nach der ersten Anmeldung ändern!" -ForegroundColor Red
Write-Host ""
Write-Host "  Weitere nützliche Befehle:" -ForegroundColor White
Write-Host "    Get-Service MariaDB               - Dienststatus"
Write-Host "    Start-Service MariaDB             - Dienst starten"
Write-Host "    cd `"$BackendDir`" ; npx prisma studio"
Write-Host "                                      - Datenbank im Browser (Port 5555)"
Write-Host "=================================================================="
