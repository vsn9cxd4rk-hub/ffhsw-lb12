#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Installationsskript
# Ziel: Manjaro Linux (aktuell)
# Ausführen als: sudo bash install-manjaro.sh
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
APP_NAME="feuerwehrmanagement"
APP_USER="lb12admin"
APP_DIR="/var/www/feuerwehrmanagement"
LOG_DIR="/var/log/feuerwehrmanagement"
NODE_VERSION_MIN="18"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

# Datenbank-Defaults (können bei den Abfragen überschrieben werden)
DB_NAME_DEFAULT="FFWVSLB12"
DB_USER_DEFAULT="FFWVSLB12"
DB_PASS_DEFAULT="Ffw#VSLB12!25"

# --------------------------------------------------------------------------
# Root-Prüfung
# --------------------------------------------------------------------------
[[ $EUID -ne 0 ]] && error "Dieses Script muss als root ausgeführt werden: sudo bash install-manjaro.sh"

echo ""
echo "=================================================================="
echo "  Feuerwehr Management System - Installation (Manjaro Linux)"
echo "=================================================================="
echo ""

# --------------------------------------------------------------------------
# Konfigurationsabfragen
# --------------------------------------------------------------------------
read -p "App-Domain oder IP-Adresse (z.B. feuerwehr.local oder 192.168.1.100): " APP_DOMAIN
[[ -z "$APP_DOMAIN" ]] && APP_DOMAIN="localhost"

read -p "MariaDB root Passwort (wird für die Installation benötigt): " MYSQL_ROOT_PASS
[[ -z "$MYSQL_ROOT_PASS" ]] && error "MariaDB root Passwort ist erforderlich"

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

read -p "SMTP-Host (Enter = E-Mail deaktiviert): " SMTP_HOST
if [[ -n "$SMTP_HOST" ]]; then
  read -p "SMTP-Port [587]: " SMTP_PORT; SMTP_PORT="${SMTP_PORT:-587}"
  read -p "SMTP-Benutzer: " SMTP_USER
  read -p "SMTP-Passwort: " SMTP_PASS
  read -p "Absender-E-Mail: " SMTP_FROM
fi

echo ""
info "Starte Installation mit:"
info "  Domain:   $APP_DOMAIN"
info "  DB:       $DB_NAME@localhost"
info "  App-Dir:  $APP_DIR"
echo ""
read -p "Fortfahren? [j/N]: " CONFIRM
[[ "${CONFIRM,,}" != "j" && "${CONFIRM,,}" != "y" ]] && { info "Installation abgebrochen."; exit 0; }
echo ""

# --------------------------------------------------------------------------
# System aktualisieren
# --------------------------------------------------------------------------
info "System wird aktualisiert..."
pacman -Syu --noconfirm --quiet
pacman -S --noconfirm --needed curl wget ca-certificates git base-devel unzip
success "System aktualisiert"

# --------------------------------------------------------------------------
# Node.js installieren
# --------------------------------------------------------------------------
if ! command -v node &>/dev/null || [[ "$(node -v | cut -d'v' -f2 | cut -d'.' -f1)" -lt "$NODE_VERSION_MIN" ]]; then
  info "Node.js wird installiert..."
  pacman -S --noconfirm --needed nodejs npm
  success "Node.js $(node -v) installiert"
else
  success "Node.js $(node -v) bereits installiert"
fi

# --------------------------------------------------------------------------
# MariaDB installieren und einrichten
# --------------------------------------------------------------------------
if ! command -v mysql &>/dev/null && ! command -v mariadb &>/dev/null; then
  info "MariaDB wird installiert..."
  pacman -S --noconfirm --needed mariadb
  # Datenverzeichnis initialisieren (erforderlich bei Erstinstallation)
  mariadb-install-db --user=mysql --basedir=/usr --datadir=/var/lib/mysql
  systemctl start mariadb
  systemctl enable mariadb
  success "MariaDB installiert und gestartet"
else
  # Dienst starten falls noch nicht aktiv
  systemctl is-active --quiet mariadb || systemctl start mariadb
  systemctl enable mariadb 2>/dev/null || true
  success "MariaDB bereits installiert"
fi

# --------------------------------------------------------------------------
# Nginx installieren
# --------------------------------------------------------------------------
if ! command -v nginx &>/dev/null; then
  info "Nginx wird installiert..."
  pacman -S --noconfirm --needed nginx
  systemctl enable nginx
  success "Nginx installiert"
else
  success "Nginx bereits installiert"
fi

# --------------------------------------------------------------------------
# PM2 installieren
# --------------------------------------------------------------------------
if ! command -v pm2 &>/dev/null; then
  info "PM2 wird installiert..."
  npm install -g pm2 --quiet
  success "PM2 $(pm2 -v) installiert"
else
  success "PM2 $(pm2 -v) bereits installiert"
fi

# --------------------------------------------------------------------------
# App-Benutzer anlegen
# --------------------------------------------------------------------------
if ! id "$APP_USER" &>/dev/null; then
  info "Benutzer '$APP_USER' wird angelegt..."
  useradd -r -m -s /bin/bash "$APP_USER"
  success "Benutzer '$APP_USER' angelegt"
else
  success "Benutzer '$APP_USER' bereits vorhanden"
fi

# --------------------------------------------------------------------------
# Verzeichnisse anlegen
# --------------------------------------------------------------------------
info "Verzeichnisstruktur wird erstellt..."
mkdir -p "$APP_DIR"/{backend,frontend,uploads,backups,scripts}
mkdir -p "$LOG_DIR"
chown -R "$APP_USER:$APP_USER" "$APP_DIR"
chown -R "$APP_USER:$APP_USER" "$LOG_DIR"
success "Verzeichnisse angelegt"

# --------------------------------------------------------------------------
# MariaDB Datenbank, Benutzer und Schema einrichten (via init-db.sql)
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

mysql -u root -p"$MYSQL_ROOT_PASS" < "$TMP_SQL" 2>/dev/null || \
  error "Datenbankinitialisierung fehlgeschlagen (init-db.sql)"

rm -f "$TMP_SQL"
success "Datenbank '${DB_NAME}', Benutzer '${DB_USER}' und alle Tabellen angelegt"

# --------------------------------------------------------------------------
# .env Datei erstellen
# --------------------------------------------------------------------------
info "Konfigurationsdatei wird erstellt..."
# URL-encode special characters in DB password (e.g. # -> %23, ! -> %21)
DB_PASS_ENCODED=$(python3 -c "import urllib.parse; print(urllib.parse.quote('${DB_PASS}', safe=''))" 2>/dev/null || \
  echo "${DB_PASS}" | sed 's/#/%23/g; s/!/%21/g; s/@/%40/g; s/\$/%24/g; s/&/%26/g; s/+/%2B/g')
cat > "$APP_DIR/backend/.env" <<EOF
NODE_ENV=production
PORT=3001

DATABASE_URL="mysql://${DB_USER}:${DB_PASS_ENCODED}@localhost:3306/${DB_NAME}"

JWT_ACCESS_SECRET=${JWT_ACCESS}
JWT_REFRESH_SECRET=${JWT_REFRESH}
JWT_ACCESS_EXPIRES=15m
JWT_REFRESH_EXPIRES=7d

CORS_ORIGIN=http://${APP_DOMAIN}

UPLOAD_PATH=${APP_DIR}/uploads
LOG_LEVEL=info
LOG_PATH=${LOG_DIR}

SMTP_HOST=${SMTP_HOST:-}
SMTP_PORT=${SMTP_PORT:-587}
SMTP_SECURE=false
SMTP_USER=${SMTP_USER:-}
SMTP_PASS=${SMTP_PASS:-}
SMTP_FROM=${SMTP_FROM:-noreply@feuerwehr.local}
EOF
chmod 600 "$APP_DIR/backend/.env"
success ".env Datei erstellt"

# --------------------------------------------------------------------------
# Anwendung kopieren und installieren
# --------------------------------------------------------------------------
info "Backend wird installiert..."
cp -r "$PROJECT_DIR/webapp/backend/." "$APP_DIR/backend/"
cd "$APP_DIR/backend"
npm install 2>/dev/null || npm install --legacy-peer-deps
npx prisma generate
npm run build
npm prune --production
success "Backend installiert und kompiliert"

info "Prisma-Client wird mit bestehender Datenbank synchronisiert..."
# Die Tabellen wurden bereits durch init-db.sql korrekt angelegt und entsprechen
# exakt dem Prisma-Schema. prisma db push wird daher übersprungen – es würde
# versuchen, MySQL-FK-Backing-Indizes umzubenennen, was zu einem Fehler führt
# ("Cannot drop index ... needed in a foreign key constraint").
success "Schema bereits via init-db.sql synchronisiert – db push wird übersprungen"

info "Initialdaten werden eingerichtet (Admin-Benutzer, bcrypt-Hash)..."
if npx ts-node --transpile-only prisma/seed.ts 2>/dev/null; then
  success "Initialdaten eingerichtet"
else
  warning "Seed fehlgeschlagen - bitte manuell ausführen: cd ${APP_DIR}/backend && npx ts-node --transpile-only prisma/seed.ts"
fi

info "Frontend wird gebaut..."
if [[ -d "$PROJECT_DIR/webapp/frontend" ]]; then
  cd "$PROJECT_DIR/webapp/frontend"
  npm ci 2>/dev/null || npm install
  npm run build
  cp -r dist/. "$APP_DIR/frontend/"
  success "Frontend gebaut"
else
  warning "Frontend-Quellcode nicht gefunden - bitte manuell bauen"
fi

# --------------------------------------------------------------------------
# Hilfsskripte installieren
# --------------------------------------------------------------------------
info "Hilfsskripte werden installiert..."
cp "$SCRIPT_DIR/backup.sh"       "$APP_DIR/scripts/backup.sh"
cp "$SCRIPT_DIR/update.sh"       "$APP_DIR/scripts/update.sh"
cp "$SCRIPT_DIR/check-health.sh" "$APP_DIR/scripts/check-health.sh" 2>/dev/null || true
chmod +x "$APP_DIR/scripts/"*.sh
chown "$APP_USER:$APP_USER" "$APP_DIR/scripts/"*.sh
success "Hilfsskripte installiert"

# --------------------------------------------------------------------------
# PM2 Ecosystem-Konfiguration
# --------------------------------------------------------------------------
info "PM2-Konfiguration wird erstellt..."
cat > "$APP_DIR/ecosystem.config.js" <<EOF
module.exports = {
  apps: [{
    name: 'feuerwehr-backend',
    script: '${APP_DIR}/backend/dist/server.js',
    cwd: '${APP_DIR}/backend',
    instances: 1,
    autorestart: true,
    watch: false,
    max_memory_restart: '500M',
    env: { NODE_ENV: 'production', PORT: 3001 },
    error_file: '${LOG_DIR}/pm2-error.log',
    out_file: '${LOG_DIR}/pm2-out.log',
    log_date_format: 'YYYY-MM-DD HH:mm:ss',
    merge_logs: true,
  }]
};
EOF

# --------------------------------------------------------------------------
# PM2 starten und autostart einrichten
# --------------------------------------------------------------------------
info "Backend wird gestartet..."
su -c "cd $APP_DIR && pm2 start ecosystem.config.js" "$APP_USER" 2>/dev/null || \
  pm2 start "$APP_DIR/ecosystem.config.js" --update-env
pm2 save
env PATH=$PATH:/usr/bin pm2 startup systemd -u "$APP_USER" --hp "/home/$APP_USER" 2>/dev/null | tail -1 | bash 2>/dev/null || true
success "Backend gestartet"

# --------------------------------------------------------------------------
# Nginx konfigurieren
# Manjaro verwendet conf.d/ statt sites-available/sites-enabled
# --------------------------------------------------------------------------
info "Nginx wird konfiguriert..."
mkdir -p /etc/nginx/conf.d

cat > "/etc/nginx/conf.d/${APP_NAME}.conf" <<EOF
server {
    listen 80;
    server_name $APP_DOMAIN;

    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;
    add_header X-Content-Type-Options "nosniff" always;

    # Frontend (React SPA)
    root ${APP_DIR}/frontend;
    index index.html;

    # Gzip
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml text/javascript;
    gzip_min_length 1000;

    # Static assets caching
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
        try_files \$uri =404;
    }

    # API proxy
    location /api/ {
        proxy_pass http://127.0.0.1:3001;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_cache_bypass \$http_upgrade;
        proxy_read_timeout 90s;
        client_max_body_size 10M;
    }

    # Uploads
    location /uploads/ {
        alias ${APP_DIR}/uploads/;
        expires 30d;
    }

    # SPA fallback
    location / {
        try_files \$uri \$uri/ /index.html;
    }

    location ~ /\. { deny all; }
    location ~ \.env { deny all; }
}
EOF

# Sicherstellen, dass conf.d in der nginx.conf eingebunden ist
if ! grep -q "conf.d/\*\.conf" /etc/nginx/nginx.conf 2>/dev/null; then
  warning "Bitte prüfen: /etc/nginx/nginx.conf muss 'include /etc/nginx/conf.d/*.conf;' im http-Block enthalten"
fi

# Standard-server-Block in nginx.conf deaktivieren falls er Port 80 belegt
# (Manjaro-Default hat oft einen eingebetteten server-Block)
if grep -q "listen\s*80" /etc/nginx/nginx.conf 2>/dev/null; then
  warning "nginx.conf enthält einen server-Block mit Port 80 - bitte diesen auskommentieren um Konflikte zu vermeiden"
  warning "Datei: /etc/nginx/nginx.conf"
fi

nginx -t && systemctl start nginx && systemctl reload nginx
success "Nginx konfiguriert und gestartet"

# --------------------------------------------------------------------------
# Cron-Jobs einrichten
# --------------------------------------------------------------------------
info "Cron-Jobs werden eingerichtet..."
(crontab -l -u "$APP_USER" 2>/dev/null || echo "") | grep -v "fuerwehr" > /tmp/crontab_new
echo "# Feuerwehr Management System" >> /tmp/crontab_new
echo "0 2 * * * ${APP_DIR}/scripts/backup.sh >> ${LOG_DIR}/backup.log 2>&1" >> /tmp/crontab_new
echo "*/5 * * * * ${APP_DIR}/scripts/check-health.sh >> ${LOG_DIR}/health.log 2>&1" >> /tmp/crontab_new
crontab -u "$APP_USER" /tmp/crontab_new
rm /tmp/crontab_new
success "Cron-Jobs eingerichtet (Backup täglich 02:00, Health-Check alle 5 Min)"

# --------------------------------------------------------------------------
# Firewall (falls ufw vorhanden)
# --------------------------------------------------------------------------
if command -v ufw &>/dev/null; then
  info "Firewall wird konfiguriert..."
  ufw allow 80/tcp >/dev/null 2>&1 || true
  ufw allow 443/tcp >/dev/null 2>&1 || true
  ufw allow 22/tcp >/dev/null 2>&1 || true
  success "Firewall: Ports 80, 443, 22 geöffnet"
fi

# --------------------------------------------------------------------------
# Abschluss
# --------------------------------------------------------------------------
echo ""
echo "=================================================================="
echo -e "${GREEN}  Installation erfolgreich abgeschlossen!${NC}"
echo "=================================================================="
echo ""
echo "  System erreichbar unter: http://$APP_DOMAIN"
echo ""
echo "  Standard-Anmeldedaten:"
echo "    Benutzername: admin"
echo "    Passwort:     Admin123!"
echo ""
echo -e "  ${RED}WICHTIG: Bitte das Passwort sofort nach der ersten Anmeldung ändern!${NC}"
echo ""
echo "  Nützliche Befehle:"
echo "    pm2 status                     - Backend-Status"
echo "    pm2 logs feuerwehr-backend      - Backend-Logs"
echo "    ${APP_DIR}/scripts/backup.sh   - Manuelles Backup"
echo "    ${APP_DIR}/scripts/update.sh   - System aktualisieren"
echo ""
echo "  Log-Dateien: $LOG_DIR"
echo "  Backups:     $APP_DIR/backups"
echo "=================================================================="
