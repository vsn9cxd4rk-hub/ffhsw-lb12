#!/bin/bash
# =============================================================================
# Feuerwehr Management System - Release-Paket erstellen
# Packt data/, deploy/ und webapp/ (Stand des letzten Commits) in ein
# tar.gz für die Übertragung auf den Server.
#
# Verwendung:
#   ./create-release.sh
# =============================================================================
set -eo pipefail

RED='\033[0;31m'; GREEN='\033[0;32m'; BLUE='\033[0;34m'; NC='\033[0m'
info()    { echo -e "${BLUE}[INFO]${NC}  $*"; }
success() { echo -e "${GREEN}[OK]${NC}    $*"; }
error()   { echo -e "${RED}[ERROR]${NC} $*"; exit 1; }

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

command -v git >/dev/null 2>&1 || error "git nicht gefunden"

REPO_ROOT="$(git rev-parse --show-toplevel)"
TREE_PATH="$(realpath --relative-to="$REPO_ROOT" "$PROJECT_DIR")"

VERSION=$(grep -m1 '"version"' webapp/backend/package.json | sed -E 's/.*"version": *"([^"]+)".*/\1/')
[[ -z "$VERSION" ]] && error "Version konnte nicht aus webapp/backend/package.json gelesen werden"

RELEASE_NAME="feuerwehr-management-system_v${VERSION}"
OUT_DIR="${PROJECT_DIR}/releases"
ARCHIVE="${OUT_DIR}/${RELEASE_NAME}.tar.gz"

mkdir -p "$OUT_DIR"

info "Version:  $VERSION"
info "Ziel:     $ARCHIVE"

# Nur committete Dateien aufnehmen (respektiert .gitignore automatisch,
# keine node_modules/dist/.env/Uploads/lokale Reste im Paket)
# -C sorgt dafür, dass die Pfadspezifikationen relativ zum Repo-Root
# aufgelöst werden, wie es das Tree-ish "HEAD:${TREE_PATH}" erfordert.
git -C "$REPO_ROOT" archive --format=tar --prefix="${RELEASE_NAME}/" "HEAD:${TREE_PATH}" -- data deploy webapp \
  | gzip -9 > "$ARCHIVE"

success "Release-Paket erstellt: $ARCHIVE"
