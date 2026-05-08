#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Health-Check-Skript
# Prüft ob Backend erreichbar ist und startet ggf. automatisch neu
# Cron: */5 * * * * /var/www/feuerwehr/scripts/check-health.sh >> /var/log/feuerwehr/health.log 2>&1
# =============================================================================

APP_DIR="/var/www/feuerwehrmanagement"
APP_USER="lb12admin"
BACKEND_URL="http://127.0.0.1:3001/api/health"
LOG_DIR="/var/log/feuerwehrmanagement"
MAX_RETRIES=3

log() { echo "$(date '+%Y-%m-%d %H:%M:%S') $*"; }

# --------------------------------------------------------------------------
# Backend-Erreichbarkeit prüfen
# --------------------------------------------------------------------------
HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "${BACKEND_URL}" 2>/dev/null || echo "000")

if [[ "$HTTP_STATUS" == "200" ]]; then
  # Alles in Ordnung - kein Log (verhindert Log-Spam)
  exit 0
fi

log "[WARN] Backend nicht erreichbar (HTTP ${HTTP_STATUS})"

# --------------------------------------------------------------------------
# PM2-Prozess prüfen und ggf. neu starten
# --------------------------------------------------------------------------
PM2_STATUS=$(su -c "pm2 jlist 2>/dev/null" "${APP_USER}" 2>/dev/null | \
  python3 -c "import sys,json; procs=json.load(sys.stdin); p=[x for x in procs if x.get('name')=='feuerwehr-backend']; print(p[0]['pm2_env']['status'] if p else 'not_found')" 2>/dev/null || echo "unknown")

log "[INFO] PM2-Status: ${PM2_STATUS}"

RESTARTED=false
case "$PM2_STATUS" in
  "online")
    log "[INFO] PM2 meldet 'online' aber Backend antwortet nicht - reload wird versucht"
    su -c "pm2 reload feuerwehr-backend --update-env" "${APP_USER}" 2>/dev/null && RESTARTED=true
    ;;
  "stopped"|"errored"|"not_found")
    log "[WARN] PM2-Prozess ist ${PM2_STATUS} - wird neugestartet"
    if [[ -f "${APP_DIR}/ecosystem.config.js" ]]; then
      su -c "cd ${APP_DIR} && pm2 start ecosystem.config.js" "${APP_USER}" 2>/dev/null && RESTARTED=true
    else
      su -c "pm2 start feuerwehr-backend" "${APP_USER}" 2>/dev/null && RESTARTED=true
    fi
    ;;
  *)
    log "[WARN] Unbekannter PM2-Status: ${PM2_STATUS} - restart wird versucht"
    su -c "pm2 restart feuerwehr-backend" "${APP_USER}" 2>/dev/null && RESTARTED=true
    ;;
esac

# --------------------------------------------------------------------------
# Nach Neustart prüfen
# --------------------------------------------------------------------------
if [[ "$RESTARTED" == "true" ]]; then
  sleep 5

  for i in $(seq 1 $MAX_RETRIES); do
    HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" --max-time 5 "${BACKEND_URL}" 2>/dev/null || echo "000")
    if [[ "$HTTP_STATUS" == "200" ]]; then
      log "[OK]   Backend wieder erreichbar nach Neustart (Versuch ${i})"
      exit 0
    fi
    sleep 3
  done

  log "[ERROR] Backend nach Neustart immer noch nicht erreichbar (HTTP ${HTTP_STATUS})"
  log "[INFO]  Letzte PM2-Logs:"
  su -c "pm2 logs feuerwehr-backend --nostream --lines 20 2>/dev/null" "${APP_USER}" 2>/dev/null | tail -20 >> "${LOG_DIR}/health.log" 2>/dev/null || true
  exit 1
else
  log "[ERROR] Neustart fehlgeschlagen"
  exit 1
fi
