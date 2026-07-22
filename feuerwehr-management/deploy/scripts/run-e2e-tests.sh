#!/bin/bash
# =============================================================================
# E2E-Tests ausführen (Playwright)
# Für Manjaro Linux / Produktionsserver
#
# Voraussetzungen:
#   - Node.js 20+ installiert
#   - MariaDB läuft mit Datenbank FFWVSLB12
#   - Projekt liegt unter /var/www/feuerwehrmanagement oder wird per Argument übergeben
#
# Verwendung:
#   ./scripts/run-e2e-tests.sh                    # Standard-Pfade
#   ./scripts/run-e2e-tests.sh /pfad/zum/projekt  # Custom-Pfad
# =============================================================================

set -e

# Farben
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PROJECT_DIR="${1:-$(cd "$(dirname "$0")/../.." && pwd)}"
BACKEND_DIR="$PROJECT_DIR/webapp/backend"
FRONTEND_DIR="$PROJECT_DIR/webapp/frontend"

echo -e "${YELLOW}=== Feuerwehr Management - E2E Tests ===${NC}"
echo "Projektverzeichnis: $PROJECT_DIR"
echo ""

# Prüfe Voraussetzungen
command -v node >/dev/null 2>&1 || { echo -e "${RED}Node.js nicht gefunden!${NC}"; exit 1; }
command -v npm >/dev/null 2>&1 || { echo -e "${RED}npm nicht gefunden!${NC}"; exit 1; }

echo -e "${YELLOW}[1/6] Node.js Version: $(node --version)${NC}"

# Backend Dependencies prüfen
echo -e "${YELLOW}[2/6] Backend vorbereiten...${NC}"
cd "$BACKEND_DIR"
if [ ! -d "node_modules" ]; then
  echo "  npm install (Backend)..."
  npm install --silent
fi
npm run prisma:generate --silent 2>/dev/null || true

# Frontend Dependencies prüfen
echo -e "${YELLOW}[3/6] Frontend vorbereiten...${NC}"
cd "$FRONTEND_DIR"
if [ ! -d "node_modules" ]; then
  echo "  npm install (Frontend)..."
  npm install --silent
fi

# Playwright Browsers installieren falls nötig
if [ ! -d "$HOME/.cache/ms-playwright" ] && [ ! -d "/ms-playwright" ]; then
  echo "  Playwright-Browser installieren..."
  npx playwright install chromium --with-deps
fi

# Backend starten (im Hintergrund)
echo -e "${YELLOW}[4/6] Backend starten...${NC}"
cd "$BACKEND_DIR"
PORT=3001 npm run dev > /tmp/e2e-backend.log 2>&1 &
BACKEND_PID=$!

# Auf Backend warten
echo -n "  Warte auf Backend"
for i in $(seq 1 30); do
  if curl -s http://localhost:3001/api/health > /dev/null 2>&1; then
    echo -e " ${GREEN}OK${NC}"
    break
  fi
  echo -n "."
  sleep 1
  if [ $i -eq 30 ]; then
    echo -e " ${RED}TIMEOUT${NC}"
    echo "Backend-Log:"
    tail -20 /tmp/e2e-backend.log
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
  fi
done

# Frontend starten (im Hintergrund)
echo -e "${YELLOW}[5/6] Frontend starten...${NC}"
cd "$FRONTEND_DIR"
npm run dev > /tmp/e2e-frontend.log 2>&1 &
FRONTEND_PID=$!

# Auf Frontend warten
echo -n "  Warte auf Frontend"
for i in $(seq 1 30); do
  if curl -s http://localhost:3000 > /dev/null 2>&1; then
    echo -e " ${GREEN}OK${NC}"
    break
  fi
  echo -n "."
  sleep 1
  if [ $i -eq 30 ]; then
    echo -e " ${RED}TIMEOUT${NC}"
    echo "Frontend-Log:"
    tail -20 /tmp/e2e-frontend.log
    kill $FRONTEND_PID 2>/dev/null || true
    kill $BACKEND_PID 2>/dev/null || true
    exit 1
  fi
done

# Tests ausführen
echo -e "${YELLOW}[6/6] Playwright-Tests ausführen...${NC}"
echo ""
cd "$FRONTEND_DIR"
TEST_EXIT=0
npx playwright test --reporter=list || TEST_EXIT=$?

# Aufräumen
echo ""
echo -e "${YELLOW}Aufräumen...${NC}"
kill $FRONTEND_PID 2>/dev/null || true
kill $BACKEND_PID 2>/dev/null || true
wait $FRONTEND_PID 2>/dev/null || true
wait $BACKEND_PID 2>/dev/null || true

# Ergebnis
echo ""
if [ $TEST_EXIT -eq 0 ]; then
  echo -e "${GREEN}=== ALLE TESTS BESTANDEN ===${NC}"
else
  echo -e "${RED}=== TESTS FEHLGESCHLAGEN (Exit: $TEST_EXIT) ===${NC}"
  echo "HTML-Report: $FRONTEND_DIR/playwright-report/index.html"
  echo "  Öffnen mit: npx playwright show-report"
fi

exit $TEST_EXIT
