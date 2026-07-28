#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Update-Skript
# Führt ein Update auf die neueste Version durch (mit vorherigem Backup)
# Ausführen als: sudo bash /var/www/feuerwehr/scripts/update.sh
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warning() { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

APP_DIR="/var/www/feuerwehrmanagement"
LOG_DIR="/var/log/feuerwehrmanagement"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

[[ $EUID -ne 0 ]] && error "Dieses Script muss als root ausgeführt werden: sudo bash update.sh"

# App-Benutzer ermitteln: bevorzugt den in INSTALL-MANJARO.md vorgesehenen
# Service-User "lb12admin". Läuft eine Installation stattdessen unter einem
# anderen Benutzer (z.B. weil dieser Setup-Schritt übersprungen wurde), wird
# stattdessen der tatsächliche Besitzer von APP_DIR verwendet. Vorher war
# APP_USER hart auf "lb12admin" codiert - chown/su schlugen dann mit
# "invalid user" fehl, sobald dieser Systembenutzer nicht existiert, und
# npm/prisma liefen als root weiter, was root-eigene Dateien in APP_DIR
# hinterließ, auf die der eigentliche App-Benutzer keinen Zugriff mehr hatte.
if id "lb12admin" &>/dev/null; then
  APP_USER="lb12admin"
elif [[ -d "$APP_DIR" ]]; then
  APP_USER="$(stat -c '%U' "$APP_DIR")"
else
  APP_USER="${SUDO_USER:-root}"
fi
[[ -z "$APP_USER" || "$APP_USER" == "root" ]] && warning "App-Benutzer konnte nicht sicher ermittelt und läuft als 'root' - besser vorher lb12admin gemäß INSTALL-MANJARO.md anlegen."
info "App-Benutzer: ${APP_USER}"

echo ""
echo "=================================================================="
echo "  Feuerwehr Management System - Update"
echo "=================================================================="
echo ""

# --------------------------------------------------------------------------
# Quell-Verzeichnis bestimmen
# --------------------------------------------------------------------------
# Versuche den Projektordner zu finden (wo update.sh liegt)
# Bei der Ausführung aus /var/www/feuerwehr/scripts/ nehmen wir einen
# zusätzlichen Parameter entgegen oder fragen nach
if [[ -n "${1:-}" && -d "${1}" ]]; then
  SOURCE_DIR="$1"
else
  read -p "Pfad zum neuen Quellcode (Projektverzeichnis): " SOURCE_DIR
  [[ -z "$SOURCE_DIR" ]] && error "Kein Quellverzeichnis angegeben"
  [[ ! -d "$SOURCE_DIR" ]] && error "Verzeichnis '$SOURCE_DIR' nicht gefunden"
fi

info "Quellverzeichnis: $SOURCE_DIR"
echo ""
read -p "Update durchführen? Vorher wird ein Backup erstellt. [j/N]: " CONFIRM
[[ "${CONFIRM,,}" != "j" && "${CONFIRM,,}" != "y" ]] && { info "Abgebrochen."; exit 0; }
echo ""

# --------------------------------------------------------------------------
# Pre-Update-Backup
# --------------------------------------------------------------------------
info "Pre-Update-Backup wird erstellt..."
if [[ -f "${APP_DIR}/scripts/backup.sh" ]]; then
  bash "${APP_DIR}/scripts/backup.sh" && success "Pre-Update-Backup erstellt"
else
  warning "Backup-Skript nicht gefunden, überspringe Backup"
fi

# --------------------------------------------------------------------------
# Backend aktualisieren
# --------------------------------------------------------------------------
info "Backend wird aktualisiert..."

# Kopiere neuen Backend-Code
rsync -a --delete \
  --exclude=".env" \
  --exclude="node_modules" \
  --exclude="dist" \
  "${SOURCE_DIR}/webapp/backend/" "${APP_DIR}/backend/"

cd "${APP_DIR}/backend"

info "  npm-Abhängigkeiten werden installiert..."
npm ci --omit=dev 2>/dev/null || npm install --production

info "  Prisma Client wird generiert..."
npx prisma generate

info "  TypeScript wird kompiliert..."
npm run build

info "  Datenbankmigrationen werden ausgeführt..."
npx prisma migrate deploy 2>/dev/null || npx prisma db push

success "Backend aktualisiert"

# --------------------------------------------------------------------------
# Frontend aktualisieren
# --------------------------------------------------------------------------
if [[ -d "${SOURCE_DIR}/webapp/frontend" ]]; then
  info "Frontend wird aktualisiert..."

  FRONTEND_SRC="${SOURCE_DIR}/webapp/frontend"
  cd "${FRONTEND_SRC}"

  info "  npm-Abhängigkeiten werden installiert..."
  npm ci 2>/dev/null || npm install

  info "  Frontend wird gebaut..."
  npm run build

  info "  Frontend wird deployed..."
  rm -rf "${APP_DIR}/frontend"/*
  cp -r dist/. "${APP_DIR}/frontend/"

  success "Frontend aktualisiert"
else
  warning "Frontend-Quellcode nicht gefunden, Frontend wird nicht aktualisiert"
fi

# --------------------------------------------------------------------------
# Hilfsskripte aktualisieren
# --------------------------------------------------------------------------
if [[ -d "${SOURCE_DIR}/deploy" ]]; then
  info "Hilfsskripte werden aktualisiert..."
  for script in backup.sh update.sh restore.sh check-health.sh; do
    if [[ -f "${SOURCE_DIR}/deploy/scripts/${script}" ]]; then
      cp "${SOURCE_DIR}/deploy/scripts/${script}" "${APP_DIR}/scripts/${script}"
      chmod +x "${APP_DIR}/scripts/${script}"
    fi
  done
  chown "${APP_USER}:${APP_USER}" "${APP_DIR}/scripts/"*.sh 2>/dev/null || true
  success "Hilfsskripte aktualisiert"
fi

# --------------------------------------------------------------------------
# Berechtigungen setzen
# --------------------------------------------------------------------------
chown -R "${APP_USER}:${APP_USER}" "${APP_DIR}/backend"
chown -R "${APP_USER}:${APP_USER}" "${APP_DIR}/frontend"
chmod 600 "${APP_DIR}/backend/.env"

# --------------------------------------------------------------------------
# Backend neustarten
# --------------------------------------------------------------------------
info "Backend wird neugestartet..."
if su -c "pm2 reload feuerwehr-backend --update-env" "${APP_USER}" 2>/dev/null; then
  success "Backend neu gestartet (reload)"
elif pm2 reload feuerwehr-backend --update-env 2>/dev/null; then
  success "Backend neu gestartet (reload, root)"
else
  warning "PM2 reload fehlgeschlagen, versuche restart..."
  pm2 restart feuerwehr-backend 2>/dev/null || \
    su -c "cd ${APP_DIR} && pm2 start ecosystem.config.js" "${APP_USER}"
  success "Backend neugestartet"
fi

# Nginx neu laden (für den Fall, dass sich die Config geändert hat)
if [[ -f "${SOURCE_DIR}/deploy/nginx/feuerwehr.conf" ]]; then
  info "Nginx-Konfiguration wird geprüft..."
  nginx -t 2>/dev/null && systemctl reload nginx && success "Nginx neu geladen"
fi

pm2 save >/dev/null 2>&1 || true

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
echo ""
echo "=================================================================="
echo -e "${GREEN}  Update erfolgreich abgeschlossen!${NC}"
echo "=================================================================="
echo ""
info "Backend-Status:"
pm2 status feuerwehr-backend 2>/dev/null || true
echo ""
info "Bei Problemen: pm2 logs feuerwehr-backend"
info "Rollback:      ${APP_DIR}/scripts/restore.sh"
echo ""
