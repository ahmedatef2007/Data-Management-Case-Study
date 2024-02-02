#!/bin/bash

threshold=40
disk_usage=$(df -h / | awk 'NR==2 {print $6}' | tr -d '%' | cut -d'G' -f1)
echo $disk_usage

log_dir="H:/iTi/Casestudy/Backup"
log_file="$log_dir/disk_space_alert.log"

if [ "$disk_usage" -ge "$threshold" ]; then
    echo "Warning: Disk space usage is above $threshold%. Consider freeing up space." >> "$log_file"
else
    echo "Disk space usage is within acceptable limits." >> "$log_file"
fi
