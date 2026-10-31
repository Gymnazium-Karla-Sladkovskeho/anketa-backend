# Blackbox tests

[Hurl](https://hurl.dev) black-box tests for the HTTP API. They run against a
real app + a real, **dedicated test database** (`gykas_test`) and are
completely disconnected from the Gradle build — no hurl/psql logic lives in
`build.gradle.kts`.

## How it works

- The app is started with `./gradlew run -Penv=test`. The dotenv Gradle plugin
  then layers `blackbox/test.env` on top of the repo-root `.env` (later files
  override earlier ones), so the test app uses `POSTGRES_DB=gykas_test` and
  `BACKEND_PORT=8081` — side-by-side with a dev app on 8080, never touching
  the dev database.
- The schema is created by the app itself on startup (`SchemaUtils.create` in
  `DatabaseFactory`) — there is no DDL to keep in sync here.
- `run.sh` resets and re-seeds `gykas_test` via `psql` (`seed.sql`: truncate +
  fixtures) and then runs all `hurl/**/*.hurl` files in order (`--jobs 1`).
  It reads the same `test.env` + repo-root `.env`, so there is one source of
  truth for the test environment. It refuses to run against any database not
  named `gykas_test`.

## Prerequisites

- `hurl` and `psql` installed
- the postgres container running (`docker compose up -d postgres` from the
  repo root)

## Running

```bash
# terminal 1 — start the app with the test environment
cd backend
./gradlew run -Penv=test

# terminal 2 — reset the DB and run the tests (repeatable)
backend/blackbox/run.sh
```

The fixture state after seeding: users `jenda` (student), `kramny` (admin),
`jindra` (teacher) — all with password `p2%123a` (see `seed.sql`; all people
are invented fixtures).

Do not add `--very-verbose` to the default run — it dumps bearer tokens and
passwords into the output.
