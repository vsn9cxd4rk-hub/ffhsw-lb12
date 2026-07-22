# =============================================================================
# Feuerwehr Management System - Frontend Docker Image
# Build-Context: feuerwehr-management/ (siehe docker-compose.yml)
# =============================================================================

FROM node:22-bookworm-slim AS builder
WORKDIR /app

COPY webapp/frontend/package.json ./
RUN npm install

COPY webapp/frontend/ .
RUN npm run build

FROM nginx:1.27-alpine AS runtime
COPY --from=builder /app/dist /usr/share/nginx/html
COPY deploy/docker/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget -qO- http://127.0.0.1/ >/dev/null || exit 1
