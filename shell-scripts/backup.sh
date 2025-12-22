#!/bin/bash
SCREEN_NAME="backup-script"

sudo screen -S $SCREEN_NAME -A bash -c '
while true; do
    START_TIME=$(date +%s)
    WAIT_TIME=43200

    SOURCE_DIR="/home/MCServer/Weihnachtsminecraft Welt"
    BACKUP_DIR="/home/backups/"
    TIMESTAMP=$(date +"%Y-%m-%d_%H-%M")
    BACKUP_NAME="backup_$TIMESTAMP.tar.gz"

    mkdir -p "$BACKUP_DIR"

    echo "Starte Backup um $(date +"%Y-%m-%d %H:%M:%S")"

    tar -czf "$BACKUP_DIR/$BACKUP_NAME" -C "$SOURCE_DIR" .
    find "$BACKUP_DIR" -type f -name "backup_*.tar.gz" -mtime +1 -delete

    echo "Backup erfolgreich erstellt: $BACKUP_NAME"

    END_TIME=$(date +%s)
    ELAPSED_TIME=$((END_TIME - START_TIME))

    if [ $ELAPSED_TIME -le 20 ]; then
        echo "Backup dauerte $ELAPSED_TIME Sekunden. Beende..."
        exit 0
    else
        echo "Backup dauerte $ELAPSED_TIME Sekunden. Starte in $WAIT_TIME Sekunden neu..."
        sleep $WAIT_TIME
    fi
done
'

echo "Backup-Script wurde in der Screen-Session '$SCREEN_NAME' gestartet."

