#!/bin/bash

DB_USER="ahmed"
DB_PASSWORD="123"
DB_NAME="dm_case_study"
BACKUP_DIR="./"
TIMESTAMP=$(date +%Y%m%d%H%M%S)
BACKUP_FILE="$BACKUP_DIR/$DB_NAME-$TIMESTAMP.sql"
MYSQL_DUMP_COMMAND="mysqldump -u$DB_USER -p'$DB_PASSWORD' $DB_NAME > $BACKUP_FILE"
eval $MYSQL_DUMP_COMMAND
if [ $? -eq 0 ]; then
    echo "Backup completed successfully. File: $BACKUP_FILE"
else
    echo "Error: Backup failed."
fi
