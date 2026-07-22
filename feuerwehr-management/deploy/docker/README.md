# Feuerwehr Management System – Docker

Alternativer Verteilungsweg zur bare-metal-Installation unter `deploy/install-*.sh`.
Läuft als drei Container (nginx+Frontend, Backend, MySQL) über Docker Compose.

## Voraussetzungen

- Docker Engine mit dem `docker compose`-Plugin (v2)
- Empfehlung für den Docker-Host: Debian/Ubuntu Server (siehe Begründung im
  Projekt-Chat-Verlauf bzw. Architekturplan). Von RancherOS wird abgeraten –
  das Projekt ist seit ca. 2020 archiviert, es gibt keine Updates mehr.

## Erststart

```bash
cd deploy/docker
cp .env.example .env
# .env öffnen und mindestens DB_PASSWORD, DB_ROOT_PASSWORD,
# JWT_ACCESS_SECRET, JWT_REFRESH_SECRET setzen (min. 32 Zeichen für die JWT-Secrets)

./build.sh
docker compose up -d

# Warten, bis alle drei Services "healthy" sind:
docker compose ps

# Einmalig Initialdaten anlegen (Admin-User, Stammdaten, DGUV-Gerätebaum):
docker compose --profile tools run --rm backend-seed
```

Danach ist die Anwendung unter `http://<host>:${HTTP_PORT:-80}` erreichbar.
Login: `admin` / `Admin123!` – **sofort nach dem ersten Login ändern.**

## Architektur

- **frontend** (`nginx:1.27-alpine`): serviert die gebaute React-SPA, proxied
  `/api/` zum `backend`-Service und `/uploads/` auf ein gemeinsames Volume.
  Einziger Service mit veröffentlichtem Host-Port.
- **backend** (`node:22-bookworm-slim`): Express-API, kein veröffentlichter
  Port – nur über das interne Docker-Netzwerk erreichbar. Beim Start gleicht
  der Entrypoint das DB-Schema per `prisma db push` ab.
- **db** (`mysql:8.4`): persistiert nach Volume `db_data`.

Named Volumes: `db_data`, `uploads_data` (geteilt zwischen `backend` r/w und
`frontend` r/o), `backend_logs`.

## Update auf eine neue Version

```bash
git pull
cd deploy/docker
./build.sh
docker compose up -d
```

Der Entrypoint des Backends gleicht das Schema bei jedem Start automatisch ab
(`prisma db push`, ohne `--accept-data-loss`). Bei destruktiven
Schemaänderungen bricht der Start mit Fehler ab, statt Daten zu verwerfen –
in dem Fall `docker compose logs backend` prüfen.

## Bekannte Einschränkungen (bewusst nicht Teil dieser Umsetzung)

- Kein TLS/HTTPS – Compose liefert nur Port 80. Für HTTPS entweder Zertifikate
  in `nginx.conf` einbinden (analog zum auskommentierten Certbot-Block in
  `deploy/nginx/fuerwehr.conf`) oder einen Reverse-Proxy wie Caddy/Traefik
  davorsetzen.
- Kein automatisiertes Docker-Backup. `deploy/scripts/backup.sh`/`restore.sh`
  kennen nur die bare-metal-Pfade. Für ein manuelles DB-Backup:
  `docker compose exec db mysqldump -u root -p"$DB_ROOT_PASSWORD" "$DB_NAME" > backup.sql`
- Kein Registry-Push/CI-Pipeline – Images werden nur lokal gebaut.
