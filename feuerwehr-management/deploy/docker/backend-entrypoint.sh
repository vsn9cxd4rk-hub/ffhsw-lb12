#!/bin/sh
# =============================================================================
# Entrypoint für das Backend-Image: gleicht das DB-Schema ab und startet
# danach den eigentlichen Prozess (CMD aus dem Dockerfile).
# =============================================================================
set -e

echo "Synchronisiere Datenbankschema (prisma db push)..."
# Prisma 7: db push generiert nicht mehr selbst, --skip-generate entfällt
npx prisma db push

exec "$@"
