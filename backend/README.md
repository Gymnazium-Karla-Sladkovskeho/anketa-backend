# anketa-backend

A minimal template backend using **Ktor + Exposed ORM**, structured with
**feature-based clean architecture** following the official Ktor recommendations:
<https://ktor.io/docs/server-application-structure.html>

## Tech stack

| Component | Version | Notes |
|---|---|---|
| Kotlin | 2.4.20 | `jvmToolchain(21)` |
| Ktor | 3.6.0 | Netty engine, YAML config, ContentNegotiation + kotlinx.serialization JSON |
| Exposed | 1.5.1 | `exposed-core` + `exposed-jdbc` (DSL style, no DAO artifact) |
| Database | H2 2.3.232 | embedded, in-memory by default (configurable) |
| Gradle | 8.14.4 | wrapper included, no local Gradle install needed |

## How this project was created

```bash
# 1. Project files were written by hand (no generator):
#    settings.gradle.kts, build.gradle.kts, gradle.properties,
#    src/main/kotlin/**, src/main/resources/**, src/test/kotlin/**

# 2. Gradle wrapper was generated with a locally installed Gradle 8.14.4:
gradle wrapper --gradle-version 8.14.4

# 3. Verify everything:
./gradlew build   # compiles + runs tests
./gradlew run     # starts the server on http://localhost:8080
```

The only prerequisite is a **JDK 21** (`JAVA_HOME` set or `java` on `PATH`);
the Gradle wrapper takes care of everything else.

## What it does

Exposes a small JSON REST API for one example feature (`user`), backed by an
Exposed-managed H2 database. The schema is created automatically at startup.

| Method | Path | Description | Responses |
|---|---|---|---|
| `GET` | `/users` | list all users | `200` + JSON array |
| `GET` | `/users/{id}` | get one user | `200` + JSON, `400` invalid id, `404` not found |
| `POST` | `/users` | create a user (`{"name":"...","email":"..."}`) | `201` + JSON, `400` invalid input |

Try it:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Ada","email":"ada@example.com"}'

curl http://localhost:8080/users
curl http://localhost:8080/users/1
```

## Project structure (feature-based clean architecture)

Each feature is a self-contained vertical slice holding all its layers —
routes, service, repository port + implementation, domain model, DTOs and the
Exposed table — exactly the "feature-based modules" style from the Ktor docs,
with clean-architecture layering inside each slice. Shared, feature-independent
code lives in `core/`.

```
src/main/kotlin/cz/cvut/fit/gykas/anketa/
├── Application.kt                        # entry point + composition root (manual DI)
├── core/                                 # shared, feature-independent code
│   ├── database/
│   │   ├── DatabaseFactory.kt            # Exposed connection + schema creation (reads application.yaml)
│   │   └── Transactions.kt               # dbQuery {} – suspending JDBC transactions on Dispatchers.IO
│   └── plugins/
│       └── Serialization.kt              # ContentNegotiation + JSON
└── features/
    └── user/                             # example feature, one file per layer
        ├── User.kt                       # domain model (no framework dependencies)
        ├── UserDto.kt                    # @Serializable request/response DTOs + mapping
        ├── UserTable.kt                  # Exposed table definition (IntIdTable)
        ├── UserRepository.kt             # repository interface = domain "port"
        ├── ExposedUserRepository.kt      # Exposed implementation of the port
        ├── UserService.kt                # business logic, depends only on the port
        └── UserRoutes.kt                 # HTTP layer, service injected
src/main/resources/
├── application.yaml                      # ktor modules + port, database connection
└── logback.xml                           # logging configuration
src/test/kotlin/cz/cvut/fit/gykas/anketa/
└── ApplicationTest.kt                    # end-to-end test via ktor-server-test-host
```

Dependency direction: `UserRoutes` → `UserService` → `UserRepository` (interface)
← `ExposedUserRepository`. The interface layers never depend on the
infrastructure ones; wiring happens only in `Application.module()`.

## Decisions made (and why)

- **Feature-based modules per Ktor docs** — each feature keeps its routes,
  service, DTOs and domain logic together, so features can grow or be split
  into microservices independently.
- **Manual dependency injection at the module boundary** — `Application.module()`
  builds `ExposedUserRepository → UserService` and injects the service into the
  routes, as the Ktor docs recommend; no DI framework needed at this size.
- **Repository as an interface** — the service layer depends on the
  `UserRepository` port only, so the persistence technology can be swapped
  without touching business logic.
- **Exposed DSL (`exposed-core` + `exposed-jdbc`) instead of the DAO artifact** —
  the smallest dependency set; tables map to domain models explicitly.
- **Suspending transactions** — Exposed JDBC is blocking, so every query runs via
  `dbQuery {}`, which wraps `suspendTransaction` in `Dispatchers.IO`
  (Ktor's event loop is never blocked).
- **Config-driven database** — URL/driver/credentials live in
  `application.yaml`; `DatabaseFactory.init(config, UserTable)` gets feature
  tables passed in from the composition root, so `core/` never imports features.
- **H2 in-memory for the template** — zero setup, clean state on every restart
  (the test uses its own isolated `jdbc:h2:mem:test` database).
- **YAML config + `EngineMain`** — the module is loaded from `application.yaml`
  (`cz.cvut.fit.gykas.anketa.ApplicationKt.module`), no custom `main()` needed.

## How to continue

### Add a new feature (e.g. `survey`)

1. Create `features/survey/` mirroring the `user` package:
   `Survey.kt`, `SurveyDto.kt`, `SurveyTable.kt`, `SurveyRepository.kt`,
   `ExposedSurveyRepository.kt`, `SurveyService.kt`, `SurveyRoutes.kt`.
2. In `Application.module()`:
   - add `SurveyTable` to `DatabaseFactory.init(environment.config, UserTable, SurveyTable)`,
   - instantiate `ExposedSurveyRepository` + `SurveyService`,
   - add `surveyRoutes(surveyService)` inside the `routing {}` block.

### Switch to a persistent/real database

For a file-based H2, change the URL in `application.yaml` to
`jdbc:h2:./data/anketa`. For PostgreSQL:

```kotlin
// build.gradle.kts
implementation("org.postgresql:postgresql:42.7.8")
```

```yaml
# application.yaml
database:
  url: "jdbc:postgresql://localhost:5432/anketa"
  driver: "org.postgresql.Driver"
  user: "anketa"
  password: "secret"
```

## Useful Gradle tasks

| Task | Description |
|---|---|
| `./gradlew run` | start the server (http://localhost:8080) |
| `./gradlew test` | run tests |
| `./gradlew build` | compile + test + package |
| `./gradlew buildFatJar` | build a self-contained executable fat JAR (provided by the Ktor Gradle plugin) |
