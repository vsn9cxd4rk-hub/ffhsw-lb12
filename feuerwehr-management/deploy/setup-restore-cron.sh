#!/bin/bash
# =============================================================================
# Richtet den automatischen nächtlichen Restore auf dem Backup-Host ein.
# Ausführen als: sudo bash setup-restore-cron.sh
# =============================================================================
set -e

RED='\033[0;31m'; GREEN='\033[0;32m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

[[ $EUID -ne 0 ]] && error "Bitte als root ausführen: sudo bash setup-restore-cron.sh"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TARGET="/usr/local/sbin/feuerwehr-restore-auto.sh"
CRON_FILE="/etc/cron.d/feuerwehr-restore-auto"
LOG_DIR="/var/log/feuerwehrmanagement"

# --------------------------------------------------------------------------
# Skript installieren
# --------------------------------------------------------------------------
info "Installiere restore-auto.sh nach ${TARGET}..."
cp "${SCRIPT_DIR}/restore-auto.sh" "$TARGET"
chmod 700 "$TARGET"
success "Skript installiert"

# --------------------------------------------------------------------------
# Log-Verzeichnis sicherstellen
# --------------------------------------------------------------------------
mkdir -p "$LOG_DIR"
success "Log-Verzeichnis: ${LOG_DIR}"

# --------------------------------------------------------------------------
# Cron-Job anlegen (läuft täglich um 03:30 Uhr)
# --------------------------------------------------------------------------
cat > "$CRON_FILE" << 'EOF'
# Feuerwehr Management System - automatischer nächtlicher Restore
# Läuft täglich 03:30 Uhr – nach dem Transfer vom Produktivsystem (03:00 Uhr)
SHELL=/bin/bash
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

30 3 * * * root bash /usr/local/sbin/feuerwehr-restore-auto.sh >> /var/log/feuerwehrmanagement/restore-auto.log 2>&1
EOF

chmod 644 "$CRON_FILE"
success "Cron-Job angelegt: ${CRON_FILE}"

# --------------------------------------------------------------------------
# Zusammenfassung
# --------------------------------------------------------------------------
echo ""
echo "=================================================================="
success "Einrichtung abgeschlossen"
echo ""
info "  Skript   : ${TARGET}"
info "  Cron     : ${CRON_FILE}  (täglich 03:30 Uhr)"
info "  Log      : ${LOG_DIR}/restore-auto.log"
echo ""
info "Manueller Test:"
info "  sudo bash ${TARGET}"
info "  tail -f ${LOG_DIR}/restore-auto.log"
echo ""
info "Cron-Job entfernen:"
info "  sudo rm ${CRON_FILE}"
echo "=================================================================="
