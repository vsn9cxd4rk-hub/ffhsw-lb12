#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Entwicklungs-Setup
# Ziel: macOS 13 (Ventura) oder neuer, Apple Silicon und Intel
# Ausführen als normaler Benutzer (KEIN sudo): bash install-macos.sh
# =============================================================================
set -e

# --------------------------------------------------------------------------
# Farbausgabe
# --------------------------------------------------------------------------
RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warning() { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

# --------------------------------------------------------------------------
# Konfiguration
# --------------------------------------------------------------------------
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
BACKEND_DIR="$PROJECT_DIR/webapp/backend"
FRONTEND_DIR="$PROJECT_DIR/webapp/frontend"

# Datenbank-Defaults
DB_NAME_DEFAULT="FFWVSLB12"
DB_USER_DEFAULT="FFWVSLB12"
DB_PASS_DEFAULT="Ffw#VSLB12!25"

# --------------------------------------------------------------------------
# Root-Prüfung (macOS Dev-Setup läuft als normaler Benutzer)
# --------------------------------------------------------------------------
[[ $EUID -eq 0 ]] && error "Dieses Script NICHT als root ausführen. Einfach: bash install-macos.sh"

echo ""
echo "=================================================================="
echo "  Feuerwehr Management System - Entwicklungs-Setup (macOS)"
echo "=================================================================="
echo ""

# --------------------------------------------------------------------------
# Konfigurationsabfragen
# --------------------------------------------------------------------------
read -p "MariaDB root Passwort (leer = noch kein Passwort gesetzt): " MYSQL_ROOT_PASS

read -p "Datenbankname [$DB_NAME_DEFAULT]: " DB_NAME
DB_NAME="${DB_NAME:-$DB_NAME_DEFAULT}"

read -p "Datenbankbenutzer [$DB_USER_DEFAULT]: " DB_USER
DB_USER="${DB_USER:-$DB_USER_DEFAULT}"

read -p "Datenbankpasswort für '$DB_USER' [$DB_PASS_DEFAULT]: " DB_PASS
DB_PASS="${DB_PASS:-$DB_PASS_DEFAULT}"

read -p "JWT Access Secret (Enter für Zufallsschlüssel): " JWT_ACCESS
read -p "JWT Refresh Secret (Enter für Zufallsschlüssel): " JWT_REFRESH
[[ -z "$JWT_ACCESS" ]]  && JWT_ACCESS=$(openssl rand -base64 64 | tr -d '\n')
[[ -z "$JWT_REFRESH" ]] && JWT_REFRESH=$(openssl rand -base64 64 | tr -d '\n')

echo ""
info "Starte Setup mit:"
info "  DB:       $DB_NAME@localhost"
info "  Backend:  $BACKEND_DIR"
info "  Frontend: $FRONTEND_DIR"
echo ""
read -p "Fortfahren? [j/N]: " CONFIRM
[[ "${CONFIRM,,}" != "j" && "${CONFIRM,,}" != "y" ]] && { info "Setup abgebrochen."; exit 0; }
echo ""

# --------------------------------------------------------------------------
# Xcode Command Line Tools (Voraussetzung für Homebrew)
# --------------------------------------------------------------------------
if ! xcode-select -p &>/dev/null; then
  info "Xcode Command Line Tools werden installiert..."
  info "Es öffnet sich ein Dialog – bitte 'Installieren' klicken und warten."
  xcode-select --install
  # Warten bis die Installation abgeschlossen ist
  until xcode-select -p &>/dev/null; do sleep 5; done
  success "Xcode Command Line Tools installiert"
else
  success "Xcode Command Line Tools bereits vorhanden ($(xcode-select -p))"
fi

# --------------------------------------------------------------------------
# Homebrew installieren
# --------------------------------------------------------------------------
# Hilfsfunktion: Homebrew-Pfad in aktuelle Shell-Session aufnehmen
_brew_shellenv() {
  if [[ -f /opt/homebrew/bin/brew ]]; then
    eval "$(/opt/homebrew/bin/brew shellenv)"   # Apple Silicon
  elif [[ -f /usr/local/bin/brew ]]; then
    eval "$(/usr/local/bin/brew shellenv)"      # Intel
  fi
}

if ! command -v brew &>/dev/null; then
  _brew_shellenv  # ggf. schon vorhanden aber nicht im PATH
fi

if ! command -v brew &>/dev/null; then
  info "Homebrew wird installiert (Passwort für sudo wird benötigt)..."
  NONINTERACTIVE=1 /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
  _brew_shellenv
  command -v brew &>/dev/null || error "Homebrew-Installation fehlgeschlagen – bitte manuell installieren: https://brew.sh"
  success "Homebrew $(brew --version | head -1) installiert"

  # Shellprofil dauerhaft aktualisieren
  SHELL_PROFILE=""
  if [[ -f "$HOME/.zprofile" ]]; then
    SHELL_PROFILE="$HOME/.zprofile"
  elif [[ -f "$HOME/.bash_profile" ]]; then
    SHELL_PROFILE="$HOME/.bash_profile"
  fi
  if [[ -n "$SHELL_PROFILE" ]] && ! grep -q "brew shellenv" "$SHELL_PROFILE" 2>/dev/null; then
    if [[ -f /opt/homebrew/bin/brew ]]; then
      echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> "$SHELL_PROFILE"
    else
      echo 'eval "$(/usr/local/bin/brew shellenv)"' >> "$SHELL_PROFILE"
    fi
    info "Homebrew-PATH in $SHELL_PROFILE eingetragen (für neue Terminals)"
  fi
else
  success "Homebrew $(brew --version | head -1) bereits installiert"
fi

# --------------------------------------------------------------------------
# Node.js 20 installieren
# --------------------------------------------------------------------------
if ! command -v node &>/dev/null || [[ "$(node -v | cut -d'v' -f2 | cut -d'.' -f1)" -lt 18 ]]; then
  info "Node.js 20 wird installiert..."
  brew install node@20
  brew link --overwrite --force node@20
  success "Node.js $(node -v) installiert"
else
  success "Node.js $(node -v) bereits installiert"
fi

# --------------------------------------------------------------------------
# MariaDB installieren und starten
# --------------------------------------------------------------------------
if ! brew list mariadb &>/dev/null 2>&1; then
  info "MariaDB wird installiert..."
  brew install mariadb
  success "MariaDB installiert"
else
  success "MariaDB bereits installiert"
fi

if ! brew services list | grep -q "mariadb.*started"; then
  info "MariaDB wird gestartet..."
  brew services start mariadb
  # kurz warten bis MariaDB bereit ist
  sleep 3
  success "MariaDB gestartet"
else
  success "MariaDB läuft bereits"
fi

# --------------------------------------------------------------------------
# Datenbank initialisieren (via init-db.sql)
# --------------------------------------------------------------------------
info "Datenbank wird initialisiert..."

SQL_FILE="${SCRIPT_DIR}/init-db.sql"
TMP_SQL=$(mktemp /tmp/fuerwehr-init-XXXXXX.sql)

# Platzhalter im SQL durch tatsächliche Werte ersetzen
sed \
  -e "s/FFWVSLB12/${DB_NAME}/g" \
  -e "s/'FFWVSLB12'@'localhost'/'${DB_USER}'@'localhost'/g" \
  -e "s/Ffw#VSLB12!25/${DB_PASS}/g" \
  "$SQL_FILE" > "$TMP_SQL"

if [[ -n "$MYSQL_ROOT_PASS" ]]; then
  mariadb -u root -p"$MYSQL_ROOT_PASS" < "$TMP_SQL" 2>/dev/null || \
    error "Datenbankinitialisierung fehlgeschlagen (init-db.sql)"
else
  mariadb -u root < "$TMP_SQL" 2>/dev/null || \
    error "Datenbankinitialisierung fehlgeschlagen (init-db.sql) – ggf. Root-Passwort prüfen"
fi

rm -f "$TMP_SQL"
success "Datenbank '${DB_NAME}' und Benutzer '${DB_USER}' angelegt"

# --------------------------------------------------------------------------
# .env Datei für Backend erstellen
# --------------------------------------------------------------------------
if [[ ! -d "$BACKEND_DIR" ]]; then
  error "Backend-Verzeichnis nicht gefunden: $BACKEND_DIR"
fi

info "Backend .env wird erstellt..."
# URL-encode special characters in DB password (e.g. # -> %23, ! -> %21)
DB_PASS_ENCODED=$(python3 -c "import urllib.parse; print(urllib.parse.quote('${DB_PASS}', safe=''))" 2>/dev/null || \
  echo "${DB_PASS}" | sed 's/#/%23/g; s/!/%21/g; s/@/%40/g; s/\$/%24/g; s/&/%26/g; s/+/%2B/g')

mkdir -p "$BACKEND_DIR/uploads" "$BACKEND_DIR/logs"

cat > "$BACKEND_DIR/.env" <<EOF
NODE_ENV=development
PORT=3001

DATABASE_URL="mysql://${DB_USER}:${DB_PASS_ENCODED}@localhost:3306/${DB_NAME}"

JWT_ACCESS_SECRET=${JWT_ACCESS}
JWT_REFRESH_SECRET=${JWT_REFRESH}
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
EOF
chmod 600 "$BACKEND_DIR/.env"
success ".env erstellt"

# --------------------------------------------------------------------------
# Backend: Dependencies, Prisma, Seed
# --------------------------------------------------------------------------
info "Backend-Abhängigkeiten werden installiert..."
cd "$BACKEND_DIR"
npm install
success "npm install abgeschlossen"

info "Prisma-Client wird generiert..."
npm run prisma:generate
success "Prisma-Client generiert"

info "Datenbankschema wird synchronisiert (prisma db push)..."
npm run prisma:push
success "Schema synchronisiert"

info "Initialdaten werden eingerichtet (Admin-Benutzer)..."
if npm run prisma:seed 2>/dev/null; then
  success "Seed-Daten eingespielt"
else
  warning "Seed fehlgeschlagen – bitte manuell ausführen: cd $BACKEND_DIR && npm run prisma:seed"
fi

# --------------------------------------------------------------------------
# Frontend: Dependencies
# --------------------------------------------------------------------------
if [[ -d "$FRONTEND_DIR" ]]; then
  info "Frontend-Abhängigkeiten werden installiert..."
  cd "$FRONTEND_DIR"
  npm install
  success "Frontend bereit"
else
  warning "Frontend-Verzeichnis nicht gefunden: $FRONTEND_DIR"
fi

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
echo ""
echo "=================================================================="
echo -e "${GREEN}  Setup erfolgreich abgeschlossen!${NC}"
echo "=================================================================="
echo ""
echo "  Entwicklungsserver starten (je ein Terminal):"
echo ""
echo "    Terminal 1 – Backend:"
echo "      cd $BACKEND_DIR"
echo "      npm run dev"
echo ""
echo "    Terminal 2 – Frontend:"
echo "      cd $FRONTEND_DIR"
echo "      npm run dev"
echo ""
echo "  Browser öffnen: http://localhost:3000"
echo ""
echo "  Standard-Anmeldedaten:"
echo "    Benutzername: admin"
echo "    Passwort:     Admin123!"
echo ""
echo -e "  ${RED}WICHTIG: Bitte das Passwort sofort nach der ersten Anmeldung ändern!${NC}"
echo ""
echo "  Weitere nützliche Befehle:"
echo "    brew services list            - Status aller Dienste"
echo "    brew services stop mariadb    - MariaDB stoppen"
echo "    cd $BACKEND_DIR && npx prisma studio"
echo "                                  - Datenbank im Browser ansehen (Port 5555)"
echo "=================================================================="
