#!/bin/bash

BACKUP_DIR="/var/www/feuerwehr-management/backups/daily"
DEST_USER="lb12admin"
DEST_HOST="pcffwhswlb12-3.local"
DEST_PATH="/var/www/feuerwehr-management/backups/daily"
LOG="/var/log/scp_transfer.log"

LATEST=$(ls -t "$BACKUP_DIR"/*.tgz 2>/dev/null | head -n 1)

if [ -z "$LATEST" ]; then
    echo "$(date): FEHLER - Keine Backup-Datei gefunden in $BACKUP_DIR" >> "$LOG"
    exit 1
fi

echo "$(date): Übertrage $(basename "$LATEST")" >> "$LOG"

scp -i ~/.ssh/id_ed25519 -o BatchMode=yes -o ConnectTimeout=10 \
    "$LATEST" "$DEST_USER@$DEST_HOST:$DEST_PATH" >> "$LOG" 2>&1

if [ $? -eq 0 ]; then
    echo "$(date): OK - $(basename "$LATEST") übertragen" >> "$LOG"
else
    echo "$(date): FEHLER - Übertragung fehlgeschlagen" >> "$LOG"
    exit 1
fi