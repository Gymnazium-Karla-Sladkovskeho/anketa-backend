# ADR-0002: Generování databázových migrací pomocí Exposed

- **Status:** Proposed
- **Date:** 2026-11-01
- **Related:** ADR-0001

## Context

Databázové schéma projektu je definováno pomocí tabulek v Exposed. Doposud se při inicializaci databáze používá `SchemaUtils.create`, které vytváří chybějící tabulky, ale neřeší změny již existujícího schématu.

Při vývoji je potřeba bezpečně přenášet změny databázového schématu mezi vývojovým, testovacím a produkčním prostředím.

Exposed poskytuje `MigrationUtils.generateMigrationScript`, který dokáže porovnat aktuální databázové schéma s definicemi tabulek a vygenerovat SQL skript obsahující potřebné změny.

## Decision

Prozatím používáme vestavěné nástroje Exposed pro generování databázových migrací.

- Migrace se generují explicitním spuštěním samostatného Ktor modulu `runMigrations()`.
- Modul inicializuje databázové připojení pomocí stávajícího `DatabaseFactory.init(environment.config)`.
- Pomocí `MigrationUtils.generateMigrationScript` generuje SQL změny pro všechny projektové tabulky.
- Vygenerované skripty se ukládají do `src/main/resources/db/migration`.
- Generování migrací není součástí běžného startu backendu.
- Vygenerované SQL skripty je potřeba před použitím ručně zkontrolovat a následně aplikovat na cílovou databázi.

K aplikování migrací zatím nepoužíváme žádný specializovaný nástroj. Migrace jsou spravovány manuálně.

## Consequences

- Definice databázového schématu zůstávají na jednom místě – v Exposed modelech.
- Není potřeba ručně psát SQL pro každou změnu schématu.
- Generování migrací je oddělené od běžného spuštění aplikace.
- Migrace je možné verzovat společně se zdrojovým kódem v Gitu.
- Vygenerované SQL může obsahovat destruktivní operace, proto je nutná jeho ruční kontrola.
- Aplikování migrací a evidence již provedených migrací zatím nejsou automatizovány. Je na vývojáři, aby zajistil jejich správné pořadí a aplikování pouze jednou.
- Inicializace databáze pomocí `SchemaUtils.create` zůstává zachována pro vytváření chybějících tabulek.
- Do budoucna lze zvážit zavedení nástroje pro správu a automatické aplikování migrací, například Flyway.
