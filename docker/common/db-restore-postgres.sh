#!/bin/bash

source ./.env

CONFIG="-f ./docker-compose.yml"

if [ -z "$1" ]; then
  echo "File name required!"
  exit 1
fi

echo "[$(date +%F_%H-%M-%S)] Restore a postgres database backup..."
gzip -d "$1" | docker-compose ${CONFIG} exec -T postgres sh -c "psql -U ${DATABASE_USER} ${DATABASE_NAME}"
echo "[$(date +%F_%H-%M-%S)] Restore a postgres database backup done"

exit 0
