# ADR-0001: Blackbox testy (hurl) mimo Gradle build, s vlastní testovací databází

- **Status:** Accepted
- **Date:** 2026-10-31
- **Confirmed:** potvrzeno uživatelem v chatu 2026-10-31 („Then I agree for starters on everything…")
- **Related:** OPS-1, §8 (citlivá data v logu)

## Context

První implementace authu obsahovala hurl (blackbox) testy zabudované přímo v
`backend/build.gradle.kts`:

- build skript obsahoval **vlastní parser `.env`** — duplicitní k řešení přes
  Gradle plugin `io.github.dsdolzhenko.dotenv`, který `.env` řeší pro run/test tasky;
- úlohy `backupTestDb` / `loadTestDump` / `restoreTestDb` / `hurlTest` vyžadovaly
  nainstalované `hurl`, `psql`, `pg_dump`, `pg_restore` a běžící server;
- testy běžely **proti vývojářské databázi** (`gykasDB`) — před během se zálohovala
  a po běhu obnovovala, což je křehké a nebezpečné;
- `hurlTest` běžel s `--very-verbose`, což vypisuje bearer tokeny a hesla do logu
  (konflikt s §8).

Cíl: hurl testy vyndat ze zdrojáků i z buildu do složky `backend/blackbox/`, aby si
testovací prostředí připravovaly samy a byly zcela odpojené od build procesu.

## Options considered

1. **Test-support endpoint v aplikaci** (např. `POST /test-support/reset`, zapínaný
   konfiguračním příznakem, defaultně vypnutý + kontrola názvu databáze) — hurl by si
   prostředí resetoval přes HTTP.
   + žádná závislost na `psql`; − test-only kód a endpoint v produkční aplikaci,
     nutnost hlídat, aby se nikdy nezapnul v nasazení; objevil by se v generovaném
     OpenAPI.
2. **Skript v `blackbox/` + `psql` a dedikovaná testovací databáze** (zvoleno) —
   žádná změna API aplikace; `run.sh` udělá `createdb`/`truncate`+seed a spustí hurl.
   + jednoduché, nulový dopad do aplikace; − prerekvizita `psql` (tu ale vývojář
     stejně potřebuje pro práci s databází).
3. **Docker-only** (samostatná compose služba `postgres-test` s initdb.d seedem) —
   nejsilnější izolace, ale další compose konfigurace a delší běh; zatím zbytečné.

## Decision

Zvolili jsme variantu 2 (návrh uživatele):

- Nová složka `backend/blackbox/` obsahuje `hurl/` (přesunuto z `src/test/hurl/`),
  `seed.sql` (pouze TRUNCATE + INSERT fixture dat, **bez DDL**), `run.sh`,
  `test.env` a `vars.env`. Z `build.gradle.kts` se maže celá hurl/psql/env-parser
  logika; zůstává jen dotenv plugin.
- Aplikace se pro testy spouští přes `./gradlew run -Penv=test`: dotenv plugin
  nahlásí `blackbox/test.env` **po** `../.env` (pozdější soubor přebíjí), takže
  testovací aplikace běží na portu **8081** proti databázi **gykas_test** —
  vedle vývojové aplikace a nikdy ne proti vývojové databázi.
- Schéma v prázdné testovací databázi vytvoří sama aplikace — opravujeme
  `DatabaseFactory`, aby `SchemaUtils.create` zakládal **všechny** tabulky
  (doposud jen `users`, takže aplikace neuměla naběhnout na prázdné databázi).
  DDL tak existuje jen v Exposed modelech, `seed.sql` obsahuje jen data.
- `run.sh` čte stejné `test.env` + `../.env` (jeden zdroj pravdy), odmítne běžet
  proti jiné databázi než `gykas_test` a bez nainstalovaného hurl/psql neběží —
  návod je v `blackbox/README.md`.
- Ve výchozím běhu se nepoužívá `--very-verbose` (tokeny/hesla v logu, §8).

## Consequences

- `./gradlew build` opět nevyžaduje hurl, psql ani běžící databázi/server.
- Vývojová databáze už není testy ohrožena (žádné zálohy/obnovy).
- Fixture hesla jsou v `seed.sql` jako hotové Argon2 hashe; parametry Argon2 jsou
  uložené přímo v řetězci hashe, takže hashe fungují i po změně kalibrace provideru.
- Omezení: opakovatelnost seedu závisí na Exposed schématu (dokud nepřejdeme na
  migrace, např. Flyway — viz template poznámka v `DatabaseFactory`).
- Do budoucna: CI job pro blackbox testy (GitLab CI je zatím prázdné — napřed
  domluvit), případně varianta 3 jako izolace pro CI.
