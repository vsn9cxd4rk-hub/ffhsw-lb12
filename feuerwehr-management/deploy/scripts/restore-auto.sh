#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Automatischer Restore (nicht-interaktiv)
# Nimmt das aktuellste Backup aus dem daily-Verzeichnis und spielt es ein.
# .env wird ohne Rückfrage überschrieben.
#
# Cron (root):
#   30 3 * * * root bash /var/www/feuerwehrmanagement/scripts/restore-auto.sh >> /var/log/feuerwehrmanagement/restore-auto.log 2>&1
#
# Manuell:
#   sudo bash restore-auto.sh
#   sudo bash restore-auto.sh /pfad/zu/backup.tar.gz
# =============================================================================
set -e

# --------------------------------------------------------------------------
# Konfiguration
# --------------------------------------------------------------------------
APP_DIR="/var/www/feuerwehrmanagement"
BACKUP_DIR="${APP_DIR}/backups/daily"
APP_USER="lb12admin"
DB_PASS="Ffw#VSLB12!25"
LOCKFILE="/var/run/feuerwehr-restore-auto.lock"

# --------------------------------------------------------------------------
# Logging (ohne ANSI-Farben – sauber in Log-Dateien)
# --------------------------------------------------------------------------
info()    { echo "$(date '+%Y-%m-%d %H:%M:%S') [INFO]  $*"; }
success() { echo "$(date '+%Y-%m-%d %H:%M:%S') [OK]    $*"; }
warning() { echo "$(date '+%Y-%m-%d %H:%M:%S') [WARN]  $*"; }
error()   { echo "$(date '+%Y-%m-%d %H:%M:%S') [ERROR] $*"; exit 1; }

# --------------------------------------------------------------------------
# Root-Check
# --------------------------------------------------------------------------
[[ $EUID -ne 0 ]] && error "Script muss als root ausgeführt werden: sudo bash restore-auto.sh"

# --------------------------------------------------------------------------
# Lockfile – verhindert parallele Ausführungen
# --------------------------------------------------------------------------
if [ -e "$LOCKFILE" ]; then
    LOCK_PID=$(cat "$LOCKFILE" 2>/dev/null || echo "?")
    error "Restore läuft bereits (PID $LOCK_PID). Lockfile: $LOCKFILE"
fi
echo $$ > "$LOCKFILE"
trap "rm -f ${LOCKFILE}" EXIT

# --------------------------------------------------------------------------
# Backup-Datei bestimmen
# --------------------------------------------------------------------------
if [[ -n "${1:-}" ]]; then
    BACKUP_FILE="$1"
    [[ ! -f "$BACKUP_FILE" ]] && error "Angegebene Backup-Datei nicht gefunden: $BACKUP_FILE"
else
    BACKUP_FILE=$(ls -t "${BACKUP_DIR}"/*.tar.gz 2>/dev/null | head -n 1)
    [[ -z "$BACKUP_FILE" ]] && error "Keine Backup-Datei gefunden in ${BACKUP_DIR}"
fi

BACKUP_SIZE=$(du -sh "$BACKUP_FILE" | cut -f1)
info "=================================================================="
info "Automatischer Restore gestartet"
info "Backup-Datei : $(basename "$BACKUP_FILE") (${BACKUP_SIZE})"
info "=================================================================="

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
    DB_HOST=$(echo "$DATABASE_URL" | sed -E 's|.*@([^:]+):.*|\1|')
    DB_PORT=$(echo "$DATABASE_URL" | sed -E 's|.*:([0-9]+)/.*|\1|')
    DB_NAME=$(echo "$DATABASE_URL" | sed -E 's|.*/([^?]+).*|\1|')
else
    DB_USER="${DB_USER:-FFWVSLB12}"
    DB_HOST="${DB_HOST:-localhost}"
    DB_PORT="${DB_PORT:-3306}"
    DB_NAME="${DB_NAME:-FFWVSLB12}"
fi

# --------------------------------------------------------------------------
# Entpacken
# --------------------------------------------------------------------------
TMP_DIR=$(mktemp -d)
trap "rm -rf ${TMP_DIR}; rm -f ${LOCKFILE}" EXIT

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

if [[ -f "${TMP_DIR}/database.sql.gz" ]]; then
    gunzip -c "${TMP_DIR}/database.sql.gz" > "${TMP_DIR}/database.sql"
elif [[ ! -f "${TMP_DIR}/database.sql" ]]; then
    error "Kein Datenbank-Dump im Backup gefunden"
fi

MYSQL_PWD="${DB_PASS}" mysql \
    -u "${DB_USER}" \
    -h "${DB_HOST}" \
    -P "${DB_PORT}" \
    "${DB_NAME}" < "${TMP_DIR}/database.sql" 2>/dev/null \
    || error "Datenbank-Restore fehlgeschlagen"

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
# Konfiguration (.env) wiederherstellen – ohne Rückfrage
# --------------------------------------------------------------------------
if [[ -f "${TMP_DIR}/config.tar.gz" ]]; then
    info ".env wird aus Backup wiederhergestellt..."
    tar -xzf "${TMP_DIR}/config.tar.gz" -C "${TMP_DIR}" 2>/dev/null || true
    if [[ -f "${TMP_DIR}/config/.env" ]]; then
        cp "${TMP_DIR}/config/.env" "${APP_DIR}/backend/.env"
        chmod 600 "${APP_DIR}/backend/.env"
        success ".env überschrieben"
    else
        warning ".env nicht im Backup enthalten – behalte vorhandene Datei"
    fi
else
    warning "Kein config.tar.gz im Backup – .env bleibt unverändert"
fi

# --------------------------------------------------------------------------
# Backend starten
# --------------------------------------------------------------------------
info "Backend wird gestartet..."
su -c "cd ${APP_DIR} && pm2 start ecosystem.config.js --update-env" "${APP_USER}" 2>/dev/null || \
    pm2 start "${APP_DIR}/ecosystem.config.js" --update-env
success "Backend gestartet"

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
info "=================================================================="
success "Automatischer Restore erfolgreich abgeschlossen"
info "Backup : $(basename "$BACKUP_FILE")"
info "=================================================================="
