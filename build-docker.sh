#!/usr/bin/env bash
set -Eeuo pipefail

# ---- config ----
IMG="${IMG:-tryout-automation}"     # tests image name
ROOT="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"

LOGS_HOST="${LOGS_DIR:-$ROOT/logs}"         # host logs dir
ART_HOST="${ARTIFACTS_DIR:-$ROOT/artifacts}"# host artifacts dir
NET="tryout-net"                             # docker network

# container paths (match your app expectations)
LOGS_CONT="/logs"
ART_CONT="/artifacts"

# ---- sanity checks ----
command -v docker >/dev/null 2>&1 || { echo "[!] Docker not on PATH"; exit 1; }
docker version >/dev/null 2>&1 || { echo "[!] Docker Engine not running"; exit 1; }

mkdir -p "$LOGS_HOST" "$ART_HOST"

# ---- build tests image ----
echo "[*] Building image: $IMG"
docker build -t "$IMG" "$ROOT"

# ---- ensure network ----
docker network create "$NET" >/dev/null 2>&1 || true

# ---- pull & start selenium (visible Chrome with noVNC on :7900) ----
echo "[*] Pulling selenium/standalone-chrome:latest"
docker pull selenium/standalone-chrome:latest

cleanup() { docker stop selenium >/dev/null 2>&1 || true; }
trap cleanup EXIT

echo "[*] Starting selenium/standalone-chrome (noVNC at http://localhost:7900)"
docker run -d --rm \
  --name selenium \
  --network "$NET" \
  --shm-size=2g \
  -p 4444:4444 \
