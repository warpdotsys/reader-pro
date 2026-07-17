#!/usr/bin/env bash
# Entrypoint aligned with original jar CLI flags / Spring relaxed binding.
set -euo pipefail

WORKDIR="${READER_APP_WORKDIR:-/data}"
PORT="${READER_SERVER_PORT:-${SERVER_PORT:-8080}}"

mkdir -p "${WORKDIR}/storage" "${WORKDIR}/logs" \
  "${WORKDIR}/storage/data" "${WORKDIR}/storage/assets" "${WORKDIR}/storage/cache"

# Spring Boot relaxed env: READER_APP_* → reader.app.*
# Also pass classic -D for non-Spring consumers (ExtKt via AppConfig).
exec java ${JAVA_OPTS:-} \
  -Dfile.encoding=UTF-8 \
  -Duser.timezone="${TZ:-Asia/Shanghai}" \
  -Dreader.app.workDir="${WORKDIR}" \
  -Dreader.server.port="${PORT}" \
  -Dserver.port="${PORT}" \
  -jar /app/reader-pro.jar \
  "$@"
