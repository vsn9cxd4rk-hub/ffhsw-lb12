#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Backup-Skript
# Behält: 30 Tages-Backups, 12 Monats-Backups
# Cron: 0 2 * * * /var/www/fuerwehr/scripts/backup.sh >> /var/log/fuerwehr/backup.log 2>&1
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${GREEN}[OK]${NC}    $*"; }
warning() { echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${YELLOW}[WARN]${NC}  $*"; }
error()   { echo -e "$(date '+%Y-%m-%d %H:%M:%S') ${RED}[ERROR]${NC} $*"; exit 1; }

# --------------------------------------------------------------------------
# Konfiguration (aus .env laden falls vorhanden)
# --------------------------------------------------------------------------
APP_DIR="/var/www/feuerwehrmanagement"
BACKUP_DIR="${APP_DIR}/backups"
LOG_DIR="/var/log/feuerwehrmanagement"

if [[ -f "${APP_DIR}/backend/.env" ]]; then
  set -a
  source "${APP_DIR}/backend/.env"
  set +a
fi

# DB-Verbindung aus DATABASE_URL parsen (mysql://user:pass@host:port/dbname)
if [[ -n "${DATABASE_URL:-}" ]]; then
  DB_USER=$(echo "$DATABASE_URL" | sed -E 's|mysql://([^:]+):.*|\1|')
  DB_PASS=$(echo "$DATABASE_URL" | sed -E 's|mysql://[^:]+:([^@]+)@.*|\1|')
  DB_HOST=$(echo "$DATABASE_URL" | sed -E 's|.*@([^:]+):.*|\1|')
  DB_PORT=$(echo "$DATABASE_URL" | sed -E 's|.*:([0-9]+)/.*|\1|')
  DB_NAME=$(echo "$DATABASE_URL" | sed -E 's|.*/([^?]+).*|\1|')
else
  # Fallbacks
  DB_USER="${DB_USER:-fuerwehr}"
  DB_PASS="${DB_PASS:-}"
  DB_HOST="${DB_HOST:-localhost}"
  DB_PORT="${DB_PORT:-3306}"
  DB_NAME="${DB_NAME:-fuerwehr}"
fi

KEEP_DAILY=30
KEEP_MONTHLY=12
DATE=$(date +%Y-%m-%d)
MONTH=$(date +%Y-%m)
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_NAME="fuerwehr_${TIMESTAMP}"
BACKUP_PATH="${BACKUP_DIR}/${BACKUP_NAME}"
TMP_DIR=$(mktemp -d)

# --------------------------------------------------------------------------
# Verzeichnisse sicherstellen
# --------------------------------------------------------------------------
mkdir -p "${BACKUP_DIR}/daily" "${BACKUP_DIR}/monthly"

# --------------------------------------------------------------------------
# Backup durchführen
# --------------------------------------------------------------------------
info "Starte Backup: ${BACKUP_NAME}"

# 1. Datenbank sichern
info "MySQL-Dump wird erstellt..."
if MYSQL_PWD="${DB_PASS}" mysqldump \
  -u "${DB_USER}" \
  -h "${DB_HOST}" \
  -P "${DB_PORT}" \
  --single-transaction \
  --routines \
  --triggers \
  --events \
  "${DB_NAME}" > "${TMP_DIR}/database.sql" 2>/dev/null; then
  gzip -9 "${TMP_DIR}/database.sql"
  DB_SIZE=$(du -sh "${TMP_DIR}/database.sql.gz" | cut -f1)
  success "MySQL-Dump erstellt (${DB_SIZE})"
else
  error "MySQL-Dump fehlgeschlagen"
fi

# 2. Uploads sichern
info "Uploads werden gesichert..."
if [[ -d "${APP_DIR}/uploads" ]]; then
  tar -czf "${TMP_DIR}/uploads.tar.gz" -C "${APP_DIR}" uploads 2>/dev/null || true
  UPLOADS_SIZE=$(du -sh "${TMP_DIR}/uploads.tar.gz" | cut -f1)
  success "Uploads gesichert (${UPLOADS_SIZE})"
else
  warning "Kein uploads-Verzeichnis gefunden"
  touch "${TMP_DIR}/uploads.tar.gz"
fi

# 3. Konfiguration sichern (sensible Daten maskieren)
info "Konfiguration wird gesichert..."
mkdir -p "${TMP_DIR}/config"
if [[ -f "${APP_DIR}/backend/.env" ]]; then
  cp "${APP_DIR}/backend/.env" "${TMP_DIR}/config/.env"
fi
if [[ -f "${APP_DIR}/ecosystem.config.js" ]]; then
  cp "${APP_DIR}/ecosystem.config.js" "${TMP_DIR}/config/ecosystem.config.js"
fi
tar -czf "${TMP_DIR}/config.tar.gz" -C "${TMP_DIR}" config 2>/dev/null || true

# 4. Alles in ein Archiv packen
info "Archiv wird erstellt..."
tar -czf "${BACKUP_DIR}/daily/${BACKUP_NAME}.tar.gz" \
  -C "${TMP_DIR}" \
  database.sql.gz \
  uploads.tar.gz \
  config.tar.gz

TOTAL_SIZE=$(du -sh "${BACKUP_DIR}/daily/${BACKUP_NAME}.tar.gz" | cut -f1)
success "Backup erstellt: ${BACKUP_NAME}.tar.gz (${TOTAL_SIZE})"

# --------------------------------------------------------------------------
# Monats-Backup (erstes Backup des Monats)
# --------------------------------------------------------------------------
MONTHLY_FILE="${BACKUP_DIR}/monthly/fuerwehr_${MONTH}.tar.gz"
if [[ ! -f "${MONTHLY_FILE}" ]]; then
  cp "${BACKUP_DIR}/daily/${BACKUP_NAME}.tar.gz" "${MONTHLY_FILE}"
  success "Monats-Backup erstellt: fuerwehr_${MONTH}.tar.gz"
fi

# --------------------------------------------------------------------------
# Alte Backups löschen
# --------------------------------------------------------------------------
info "Alte Backups werden bereinigt..."

# Tages-Backups (älter als KEEP_DAILY Tage)
DELETED_DAILY=$(find "${BACKUP_DIR}/daily" -name "fuerwehr_*.tar.gz" -mtime +${KEEP_DAILY} -print -delete 2>/dev/null | wc -l)
if [[ $DELETED_DAILY -gt 0 ]]; then
  info "  ${DELETED_DAILY} alte Tages-Backup(s) gelöscht"
fi

# Monats-Backups (älter als KEEP_MONTHLY Monate = ~365 Tage)
KEEP_MONTHLY_DAYS=$(( KEEP_MONTHLY * 31 ))
DELETED_MONTHLY=$(find "${BACKUP_DIR}/monthly" -name "fuerwehr_*.tar.gz" -mtime +${KEEP_MONTHLY_DAYS} -print -delete 2>/dev/null | wc -l)
if [[ $DELETED_MONTHLY -gt 0 ]]; then
  info "  ${DELETED_MONTHLY} alte Monats-Backup(s) gelöscht"
fi

REMAINING=$(find "${BACKUP_DIR}/daily" -name "*.tar.gz" | wc -l)
success "Bereinigung abgeschlossen (${REMAINING} Tages-Backups verbleiben)"

# --------------------------------------------------------------------------
# Aufräumen
# --------------------------------------------------------------------------
rm -rf "${TMP_DIR}"

# --------------------------------------------------------------------------
# Zusammenfassung
# --------------------------------------------------------------------------
TOTAL_BACKUP_SIZE=$(du -sh "${BACKUP_DIR}" | cut -f1)
success "Backup erfolgreich abgeschlossen"
info "  Datei:        ${BACKUP_NAME}.tar.gz"
info "  Größe:        ${TOTAL_SIZE}"
info "  Gesamtplatz:  ${TOTAL_BACKUP_SIZE}"
