#!/bin/bash
# =============================================================================
# Baut die Docker-Images für Backend und Frontend und taggt sie mit der
# Version aus webapp/backend/package.json (gleiche Quelle wie create-release.sh).
#
# Verwendung:
#   ./build.sh
# =============================================================================
set -eo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
cd "$PROJECT_DIR"

VERSION=$(grep -m1 '"version"' webapp/backend/package.json | sed -E 's/.*"version": *"([^"]+)".*/\1/')
[[ -z "$VERSION" ]] && { echo "Version konnte nicht aus webapp/backend/package.json gelesen werden" >&2; exit 1; }

echo "Baue Images mit Version ${VERSION}..."
APP_VERSION="$VERSION" docker compose -f deploy/docker/docker-compose.yml build

echo "Fertig: feuerwehr-management-backend:${VERSION}, feuerwehr-management-frontend:${VERSION}"
