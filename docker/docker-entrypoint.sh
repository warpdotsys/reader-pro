#!/usr/bin/env bash
# Compatible with both:
#   A) hectorqin/reader compose:  ./storage:/storage  ./logs:/logs  (workDir=/)
#   B) rebuild default:           ./data:/data                      (workDir=/data)
set -euo pipefail

# Detect classic hectorqin volume layout unless user overrides
if [ -n "${READER_APP_WORKDIR:-}" ]; then
  WORKDIR="${READER_APP_WORKDIR}"
elif [ -d /storage ] || [ -d /logs ]; then
  WORKDIR="/"
else
  WORKDIR="/data"
fi

PORT="${READER_SERVER_PORT:-${SERVER_PORT:-8080}}"

mkdir -p "${WORKDIR}/storage" "${WORKDIR}/logs" \
  "${WORKDIR}/storage/data" "${WORKDIR}/storage/assets" "${WORKDIR}/storage/cache" \
  /storage /logs 2>/dev/null || true

# If workDir is / and host mounted ./storage -> /storage, paths already match.
# If workDir is /data, storage lives at /data/storage.

export READER_APP_WORKDIR="${WORKDIR}"

exec java ${JAVA_OPTS:-} \
  -Dfile.encoding=UTF-8 \
  -Duser.timezone="${TZ:-Asia/Shanghai}" \
  -Dreader.app.workDir="${WORKDIR}" \
  -Dreader.server.port="${PORT}" \
  -Dserver.port="${PORT}" \
  -jar /app/reader-pro.jar \
  "$@"
