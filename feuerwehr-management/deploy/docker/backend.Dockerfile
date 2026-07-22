# =============================================================================
# Feuerwehr Management System - Backend Docker Image
# Build-Context: feuerwehr-management/ (siehe docker-compose.yml)
# =============================================================================

FROM node:22-bookworm-slim AS builder
WORKDIR /app

COPY webapp/backend/package.json ./
COPY webapp/backend/prisma ./prisma
RUN npm install && npx prisma generate

COPY webapp/backend/tsconfig.json ./
COPY webapp/backend/src ./src
RUN npm run build

FROM node:22-bookworm-slim AS runtime
RUN apt-get update \
    && apt-get install -y --no-install-recommends openssl \
    && rm -rf /var/lib/apt/lists/*

RUN groupadd -r nodeapp && useradd -r -g nodeapp -s /usr/sbin/nologin nodeapp

WORKDIR /app
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/dist ./dist
COPY --from=builder /app/prisma ./prisma
COPY webapp/backend/package.json ./

COPY deploy/docker/backend-entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh \
    && mkdir -p /app/uploads /app/logs \
    && chown -R nodeapp:nodeapp /app

ENV NODE_ENV=production \
    PORT=3001 \
    UPLOAD_PATH=/app/uploads \
    LOG_PATH=/app/logs

USER nodeapp
EXPOSE 3001

HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD node -e "fetch('http://127.0.0.1:'+(process.env.PORT||3001)+'/api/health').then(r=>process.exit(r.ok?0:1)).catch(()=>process.exit(1))"

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["node", "dist/server.js"]
