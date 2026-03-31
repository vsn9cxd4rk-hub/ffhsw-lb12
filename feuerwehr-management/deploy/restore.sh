#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Restore-Skript
# Stellt das System aus einem Backup-Archiv wieder her
# Ausführen als: sudo bash restore.sh [backup-datei.tar.gz]
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
warning() { echo -e "${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

APP_DIR="/var/www/feuerwehrmanagement"
BACKUP_DIR="${APP_DIR}/backups"
APP_USER="lb12admin"

[[ $EUID -ne 0 ]] && error "Dieses Script muss als root ausgeführt werden: sudo bash restore.sh"

echo ""
echo "=================================================================="
echo "  Feuerwehr Management System - Restore"
echo "=================================================================="
echo ""

# --------------------------------------------------------------------------
# Backup-Datei bestimmen
# --------------------------------------------------------------------------
if [[ -n "${1:-}" ]]; then
  BACKUP_FILE="$1"
else
  # Letzte Backups anzeigen
  echo "Verfügbare Backups (neueste zuerst):"
  echo ""
  ls -1t "${BACKUP_DIR}/daily/"*.tar.gz 2>/dev/null | head -10 | while read f; do
    SIZE=$(du -sh "$f" | cut -f1)
    DATE=$(basename "$f" | sed 's/fuerwehr_\([0-9_-]*\)\.tar\.gz/\1/' | sed 's/_/ /')
    echo "  ${DATE}  (${SIZE})  $(basename $f)"
  done
  echo ""
  read -p "Backup-Datei (vollständiger Pfad oder Dateiname aus ${BACKUP_DIR}/daily/): " BACKUP_INPUT

  if [[ -f "$BACKUP_INPUT" ]]; then
    BACKUP_FILE="$BACKUP_INPUT"
  elif [[ -f "${BACKUP_DIR}/daily/${BACKUP_INPUT}" ]]; then
    BACKUP_FILE="${BACKUP_DIR}/daily/${BACKUP_INPUT}"
  else
    error "Backup-Datei nicht gefunden: $BACKUP_INPUT"
  fi
fi

[[ ! -f "$BACKUP_FILE" ]] && error "Backup-Datei nicht gefunden: $BACKUP_FILE"

BACKUP_SIZE=$(du -sh "$BACKUP_FILE" | cut -f1)
info "Backup-Datei: $BACKUP_FILE ($BACKUP_SIZE)"
echo ""
echo -e "${RED}WARNUNG: Diese Aktion überschreibt die aktuelle Datenbank und Uploads!${NC}"
echo ""
read -p "Wirklich wiederherstellen? [j/N]: " CONFIRM
[[ "${CONFIRM,,}" != "j" && "${CONFIRM,,}" != "y" ]] && { info "Abgebrochen."; exit 0; }
echo ""

# --------------------------------------------------------------------------
# .env laden für DB-Verbindung
# --------------------------------------------------------------------------
if [[ -f "${APP_DIR}/backend/.env" ]]; then
  set -a
  source "${APP_DIR}/backend/.env"
  set +a
fi

if [[ -n "${DATABASE_URL:-}" ]]; then
  DB_USER=$(echo "$DATABASE_URL" | sed -E 's|mysql://([^:]+):.*|\1|')
  DB_PASS=$(echo "$DATABASE_URL" | sed -E 's|mysql://[^:]+:([^@]+)@.*|\1|')
  DB_HOST=$(echo "$DATABASE_URL" | sed -E 's|.*@([^:]+):.*|\1|')
  DB_PORT=$(echo "$DATABASE_URL" | sed -E 's|.*:([0-9]+)/.*|\1|')
  DB_NAME=$(echo "$DATABASE_URL" | sed -E 's|.*/([^?]+).*|\1|')
else
  read -p "Datenbankbenutzer: " DB_USER
  read -p "Datenbankpasswort: " DB_PASS
  read -p "Datenbankname: " DB_NAME
  DB_HOST="localhost"
  DB_PORT="3306"
fi

# --------------------------------------------------------------------------
# Entpacken in temporäres Verzeichnis
# --------------------------------------------------------------------------
TMP_DIR=$(mktemp -d)
info "Backup wird entpackt..."
tar -xzf "$BACKUP_FILE" -C "$TMP_DIR"
success "Entpackt nach $TMP_DIR"

# --------------------------------------------------------------------------
# Backend stoppen
# --------------------------------------------------------------------------
info "Backend wird gestoppt..."
pm2 stop feuerwehr-backend 2>/dev/null || \
  su -c "pm2 stop feuerwehr-backend" "${APP_USER}" 2>/dev/null || \
  warning "Backend war nicht aktiv"

# --------------------------------------------------------------------------
# Datenbank wiederherstellen
# --------------------------------------------------------------------------
info "Datenbank wird wiederhergestellt..."

# Entpacke SQL-Dump
if [[ -f "${TMP_DIR}/database.sql.gz" ]]; then
  gunzip -c "${TMP_DIR}/database.sql.gz" > "${TMP_DIR}/database.sql"
elif [[ -f "${TMP_DIR}/database.sql" ]]; then
  cp "${TMP_DIR}/database.sql" "${TMP_DIR}/database.sql"
else
  error "Kein Datenbank-Dump im Backup gefunden"
fi

# Datenbank leeren und neu einspielen
MYSQL_PWD="${DB_PASS}" mysql -u "${DB_USER}" -h "${DB_HOST}" -P "${DB_PORT}" "${DB_NAME}" < "${TMP_DIR}/database.sql" 2>/dev/null || \
  error "Datenbank-Restore fehlgeschlagen"

success "Datenbank wiederhergestellt"

# --------------------------------------------------------------------------
# Uploads wiederherstellen
# --------------------------------------------------------------------------
if [[ -f "${TMP_DIR}/uploads.tar.gz" ]]; then
  info "Uploads werden wiederhergestellt..."
  rm -rf "${APP_DIR}/uploads"
  tar -xzf "${TMP_DIR}/uploads.tar.gz" -C "${APP_DIR}" 2>/dev/null || true
  chown -R "${APP_USER}:${APP_USER}" "${APP_DIR}/uploads" 2>/dev/null || true
  success "Uploads wiederhergestellt"
fi

# --------------------------------------------------------------------------
# Konfiguration wiederherstellen (optional)
# --------------------------------------------------------------------------
if [[ -f "${TMP_DIR}/config.tar.gz" ]]; then
  echo ""
  read -p "Konfiguration (.env) aus Backup wiederherstellen? [j/N]: " RESTORE_CONFIG
  if [[ "${RESTORE_CONFIG,,}" == "j" || "${RESTORE_CONFIG,,}" == "y" ]]; then
    tar -xzf "${TMP_DIR}/config.tar.gz" -C "${TMP_DIR}" 2>/dev/null || true
    if [[ -f "${TMP_DIR}/config/.env" ]]; then
      cp "${TMP_DIR}/config/.env" "${APP_DIR}/backend/.env"
      chmod 600 "${APP_DIR}/backend/.env"
      success ".env aus Backup wiederhergestellt"
    fi
  fi
fi

# --------------------------------------------------------------------------
# Backend starten
# --------------------------------------------------------------------------
info "Backend wird gestartet..."
su -c "cd ${APP_DIR} && pm2 start ecosystem.config.js" "${APP_USER}" 2>/dev/null || \
  pm2 start "${APP_DIR}/ecosystem.config.js" --update-env
success "Backend gestartet"

# --------------------------------------------------------------------------
# Aufräumen
# --------------------------------------------------------------------------
rm -rf "${TMP_DIR}"

echo ""
echo "=================================================================="
echo -e "${GREEN}  Restore erfolgreich abgeschlossen!${NC}"
echo "=================================================================="
echo ""
info "Backend-Status:"
pm2 status feuerwehr-backend 2>/dev/null || true
echo ""
