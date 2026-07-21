#!/bin/bash
# =============================================================================
# Backend-Tests ausführen (Vitest)
# Für Manjaro Linux / Backup-System (Nightly nach Restore)
#
# Dieses Script läuft auf dem Backup-System nach dem nächtlichen Restore.
# Es prüft:
#   - DB-Verbindung und Schema-Integrität
#   - API-Endpoints (Auth, CRUD, Report-Generierung)
#   - Backup-Konsistenz (fehlende Dateien, verwaiste Referenzen)
#   - Migration-Status (alle erwarteten Spalten/Tabellen vorhanden)
#
# Verwendung:
#   ./scripts/run-backend-tests.sh                    # Standard
#   ./scripts/run-backend-tests.sh /pfad/zum/projekt  # Custom-Pfad
#   ./scripts/run-backend-tests.sh --notify           # E-Mail bei Fehler
#
# Cron-Beispiel (05:00 nach Restore um 04:30):
#   0 5 * * * /var/www/feuerwehrmanagement/scripts/run-backend-tests.sh --notify
# =============================================================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PROJECT_DIR="${1:-$(cd "$(dirname "$0")/.." && pwd)}"
BACKEND_DIR="$PROJECT_DIR/webapp/backend"
LOG_DIR="/var/log/feuerwehrmanagement"
RESULT_FILE="$LOG_DIR/test-results.xml"
LOG_FILE="$LOG_DIR/backend-tests.log"
NOTIFY=false

# Parse arguments
for arg in "$@"; do
  case $arg in
    --notify) NOTIFY=true ;;
    /*) PROJECT_DIR="$arg"; BACKEND_DIR="$PROJECT_DIR/webapp/backend" ;;
  esac
done

mkdir -p "$LOG_DIR"

echo -e "${YELLOW}=== Backend-Tests ($(date '+%Y-%m-%d %H:%M:%S')) ===${NC}" | tee "$LOG_FILE"
echo "Projektverzeichnis: $PROJECT_DIR" | tee -a "$LOG_FILE"

# Voraussetzungen prüfen
command -v node >/dev/null 2>&1 || { echo -e "${RED}Node.js nicht gefunden!${NC}" | tee -a "$LOG_FILE"; exit 1; }
command -v mysql >/dev/null 2>&1 || { echo -e "${RED}mysql-Client nicht gefunden!${NC}" | tee -a "$LOG_FILE"; exit 1; }

# DB-Verbindung testen (schneller Pre-Check)
echo -e "${YELLOW}[1/4] Datenbank-Verbindung prüfen...${NC}" | tee -a "$LOG_FILE"
if [ -f "$BACKEND_DIR/.env" ]; then
  DB_URL=$(grep DATABASE_URL "$BACKEND_DIR/.env" | cut -d'"' -f2)
  # Extract host, user, pass, db from URL
  DB_USER=$(echo "$DB_URL" | sed -n 's|mysql://\([^:]*\):.*|\1|p')
  DB_PASS=$(echo "$DB_URL" | sed -n 's|mysql://[^:]*:\([^@]*\)@.*|\1|p' | sed 's/%23/#/g')
  DB_HOST=$(echo "$DB_URL" | sed -n 's|.*@\([^:]*\):.*|\1|p')
  DB_NAME=$(echo "$DB_URL" | sed -n 's|.*/\([^?]*\).*|\1|p')

  if ! mysql -u"$DB_USER" -p"$DB_PASS" -h"$DB_HOST" "$DB_NAME" -e "SELECT 1" > /dev/null 2>&1; then
    echo -e "${RED}DB-Verbindung fehlgeschlagen!${NC}" | tee -a "$LOG_FILE"
    if [ "$NOTIFY" = true ]; then
      send_notification "FEHLER: DB-Verbindung fehlgeschlagen"
    fi
    exit 1
  fi
  echo -e "  ${GREEN}DB erreichbar${NC}" | tee -a "$LOG_FILE"
else
  echo -e "  ${YELLOW}Keine .env gefunden, überspringe DB-Pre-Check${NC}" | tee -a "$LOG_FILE"
fi

# Dependencies prüfen
echo -e "${YELLOW}[2/4] Dependencies prüfen...${NC}" | tee -a "$LOG_FILE"
cd "$BACKEND_DIR"
if [ ! -d "node_modules" ]; then
  echo "  npm install..." | tee -a "$LOG_FILE"
  npm install --silent 2>&1 | tee -a "$LOG_FILE"
fi

# Prisma Client generieren
echo -e "${YELLOW}[3/4] Prisma Client generieren...${NC}" | tee -a "$LOG_FILE"
npx prisma generate --schema=prisma/schema.prisma 2>&1 | tee -a "$LOG_FILE" || true

# Tests ausführen
echo -e "${YELLOW}[4/4] Backend-Tests ausführen...${NC}" | tee -a "$LOG_FILE"
echo "" | tee -a "$LOG_FILE"

TEST_EXIT=0
npx vitest run --reporter=junit --outputFile="$RESULT_FILE" --reporter=default 2>&1 | tee -a "$LOG_FILE" || TEST_EXIT=$?

echo "" | tee -a "$LOG_FILE"

if [ $TEST_EXIT -eq 0 ]; then
  echo -e "${GREEN}=== ALLE BACKEND-TESTS BESTANDEN ===${NC}" | tee -a "$LOG_FILE"
  echo "Ergebnis: PASSED ($(date '+%Y-%m-%d %H:%M:%S'))" >> "$LOG_FILE"
else
  echo -e "${RED}=== BACKEND-TESTS FEHLGESCHLAGEN (Exit: $TEST_EXIT) ===${NC}" | tee -a "$LOG_FILE"
  echo "Ergebnis: FAILED ($(date '+%Y-%m-%d %H:%M:%S'))" >> "$LOG_FILE"
  echo "JUnit-Report: $RESULT_FILE" | tee -a "$LOG_FILE"

  if [ "$NOTIFY" = true ]; then
    # E-Mail-Benachrichtigung über das Backend (nodemailer)
    SUBJECT="[FFW] Backend-Tests FEHLGESCHLAGEN ($(date '+%d.%m.%Y'))"
    BODY="Die nächtlichen Backend-Tests auf dem Backup-System sind fehlgeschlagen.\n\nZeit: $(date '+%Y-%m-%d %H:%M:%S')\nExit-Code: $TEST_EXIT\nLog: $LOG_FILE\nJUnit: $RESULT_FILE\n\nBitte prüfen!"

    # Simple notification via node script
    node -e "
      const nm = require('nodemailer');
      require('dotenv').config();
      if (!process.env.SMTP_HOST) { console.log('SMTP nicht konfiguriert, keine E-Mail'); process.exit(0); }
      const t = nm.createTransport({ host: process.env.SMTP_HOST, port: +process.env.SMTP_PORT || 587, secure: process.env.SMTP_SECURE === 'true', auth: { user: process.env.SMTP_USER, pass: process.env.SMTP_PASS } });
      t.sendMail({ from: process.env.SMTP_FROM, to: process.env.SMTP_FROM, subject: '$SUBJECT', text: '$BODY' }).then(() => console.log('Benachrichtigung gesendet')).catch(e => console.error('Mail-Fehler:', e.message));
    " 2>&1 | tee -a "$LOG_FILE" || true
  fi
fi

exit $TEST_EXIT
