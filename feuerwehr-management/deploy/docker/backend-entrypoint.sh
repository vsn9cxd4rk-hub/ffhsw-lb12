#!/bin/sh
# =============================================================================
# Entrypoint für das Backend-Image: gleicht das DB-Schema ab und startet
# danach den eigentlichen Prozess (CMD aus dem Dockerfile).
# =============================================================================
set -e

echo "Synchronisiere Datenbankschema (prisma db push)..."
npx prisma db push --skip-generate

exec "$@"
