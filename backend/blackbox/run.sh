#!/usr/bin/env bash
# Blackbox tests: create the test DB if needed, reset+seed it, run hurl.
#
# Prerequisites: psql, hurl, the postgres container running, and the app
# started with the test environment:
#
#     cd backend && ./gradlew run -Penv=test     # port 8081, database gykas_test
#
# The script only ever touches the database named in test.env (gykas_test).
set -euo pipefail
cd "$(dirname "$0")"

# Same layering as the app: shared repo-root .env first, test.env overrides.
# (tr strips CR — the shared .env may have CRLF line endings.)
set -a
# shellcheck source=../../.env
source ../../.env
# shellcheck source=test.env
source ./test.env
set +a

# Use variable from CI or fallback to .env/default
POSTGRES_HOST="${POSTGRES_HOST:-localhost}"
POSTGRES_PORT="${POSTGRES_PORT:-5432}"
POSTGRES_USER="${POSTGRES_USER:-postgres}"
POSTGRES_DB="${POSTGRES_DB:-gykas_test}"

PSQL=(psql -h "$POSTGRES_HOST" -p "$POSTGRES_PORT" -U "$POSTGRES_USER" -v ON_ERROR_STOP=1)

# Safety: refuse to run against anything but the dedicated test database.
if [[ "$POSTGRES_DB" != gykas_test ]]; then
    echo "Refusing to reset database '$POSTGRES_DB' — expected 'gykas_test' (see test.env)." >&2
    exit 1
fi

# Create the test database on first run.
if ! "${PSQL[@]}" -d postgres -tAc \
        "SELECT 1 FROM pg_database WHERE datname = '$POSTGRES_DB'" | grep -q 1; then
    echo "Creating database $POSTGRES_DB"
    "${PSQL[@]}" -d postgres -c "CREATE DATABASE \"$POSTGRES_DB\""
fi

# The schema is created by the app on startup (SchemaUtils.create).
if ! "${PSQL[@]}" -d "$POSTGRES_DB" -tAc \
        "SELECT 1 FROM information_schema.tables WHERE table_name = 'users'" | grep -q 1; then
    echo "No schema in $POSTGRES_DB yet — start the app first: ./gradlew run -Penv=test" >&2
    exit 1
fi

echo "Resetting and seeding $POSTGRES_DB"
"${PSQL[@]}" -d "$POSTGRES_DB" -q -f seed.sql

mapfile -t files < <(find hurl -name '*.hurl' | sort)
echo "Running ${#files[@]} hurl files against $(grep '^baseUrl=' vars.env | cut -d= -f2)"
hurl --test --jobs 1 --variables-file vars.env "${files[@]}"
