# gykas — backend

Minimal, **working** template backend: **Ktor 3 + Exposed (DAO/ORM) + PostgreSQL**, organized with a
**feature-based clean architecture** following the official Ktor recommendations:
<https://ktor.io/docs/server-application-structure.html>

One example feature (`user`) is implemented end-to-end (routes → service → repository → PostgreSQL)
and serves as the template for new features. Starter plugins are wired in minimally: DI
(`ktor-server-di`), Status Pages, JWT auth, Swagger UI, static content, and a smoke test.

---

## Quickstart

Prerequisites: JDK 21 (Gradle auto-provisions a toolchain via the foojay resolver if needed) and a
PostgreSQL database. The database runs from the compose file **one folder above** this project
(`../docker-compose.yml`, credentials in `../.env` — the backend reads the same file automatically
via a Gradle dotenv plugin, nothing to export):

```bash
# from the repository root (one folder up)
docker compose up -d postgres

# from this folder (backend/)
./gradlew run
```

Server starts at <http://localhost:8080>. Try it:

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com"}'   # 201 Created
curl http://localhost:8080/users                      # 200 OK, JSON list
```

API docs (Swagger UI) are at <http://localhost:8080/openapi>, static content demo at
<http://localhost:8080/static/index.html>.

---

## How this project was created (exact commands)

1. **Generated** with the [Ktor Project Generator](https://start.ktor.io)
   (group `cz.cvut.fit`, artifact `gykas`, Gradle Kotlin DSL, Netty, YAML config; features:
   ContentNegotiation, kotlinx.serialization, Exposed, PostgreSQL — plus some extras that were later
   removed, see *Decisions*).

2. **Rollback point committed before any changes** (branch `chore/be-structure`):

   ```bash
   git add -A
   git commit -m "chore(be): commit start.ktor.io generated template as rollback point"
   # -> commit 4f7db88
   # To undo everything done after this point:
   git reset --hard 4f7db88
   ```

3. **Baseline build check** of the generated template: `./gradlew build` ✅

4. **Removed the generated sample code** (flat package `cz.cvut.fit`) and extras:

   ```bash
   rm -f src/main/kotlin/*.kt src/main/resources/documentation.yaml
   rm -rf src/main/resources/static src/test
   mkdir -p src/main/kotlin/cz/cvut/fit/gykas/core/database \
            src/main/kotlin/cz/cvut/fit/gykas/core/plugins \
            src/main/kotlin/cz/cvut/fit/gykas/features/user
   ```

   Deleted: `CitySchema.kt` (raw-JDBC sample), `Postgres.kt`, `Exposed.kt`, `UsersService.kt`
   (H2/R2DBC samples), `DI.kt` + `GreetingService.kt` (DI-plugin sample), `Security.kt` (JWT sample),
   `Http.kt` (Swagger sample), `StatusPages.kt`, `main.kt`, static content, OpenAPI spec, sample test.

5. **Added the feature-based structure** described below, trimmed `build.gradle.kts` and
   `gradle/libs.versions.toml`, rewrote `src/main/resources/application.yaml`. The starter **plugins**
   (DI, Status Pages, JWT auth, Swagger UI, static content, test scaffolding) were then **restored** —
   wired minimally into `core/plugins/` instead of the flat demo files; only the generated *demo code*
   (H2/R2DBC samples, raw-JDBC `CitySchema`, `GreetingService`) stayed removed.

6. **Verified** (build + runtime, see *Verification*): `./gradlew build` (runs the test),
   `./gradlew run`, curl CRUD + plugin endpoints.

---

## Project structure (feature-based clean architecture)

Per the Ktor docs, each **feature is a self-contained vertical slice** (routes, service, DTOs,
domain logic); shared/cross-cutting code lives in `core/`:

```
src/main/kotlin/cz/cvut/fit/gykas/
├─ Application.kt                   # fun Application.module() — composition root: wiring + DI
├─ core/                            # shared, feature-independent concerns
│  ├─ database/
│  │  └─ DatabaseFactory.kt         # Exposed Database.connect + schema creation + dbQuery helper
│  └─ plugins/                      # one file per cross-cutting Ktor plugin
│     ├─ Serialization.kt           # ContentNegotiation with kotlinx.serialization JSON
│     ├─ StatusPages.kt             # uncaught exceptions → 500
│     ├─ Security.kt                # JWT bearer auth ("auth-jwt"), configured from application.yaml
│     ├─ Http.kt                    # Swagger UI at /openapi (spec generated from the routing tree)
│     └─ Routing.kt                 # GET / smoke-test route (no DB) + /static
└─ features/
   └─ user/                         # one vertical slice; copy this to add a new feature
      ├─ User.kt                    # domain model
      ├─ UserDto.kt                 # UserRequest / UserResponse (+ domain → DTO mapping)
      ├─ UserTable.kt               # Exposed table object + DAO entity (the ORM mapping)
      ├─ UserRepository.kt          # persistence: PostgreSQL via Exposed DAO (ORM) API
      ├─ UserService.kt             # business logic (place for validation/rules)
      └─ UserRoutes.kt              # fun Application.userModule(service) — /users CRUD routes

src/main/resources/
├─ application.yaml                 # ktor modules + postgres.* and jwt.* config (env-overridable)
├─ logback.xml                      # console logging
└─ static/index.html                # served at /static/index.html

src/test/kotlin/cz/cvut/fit/gykas/
└─ ApplicationTest.kt               # smoke test for GET / (runs without a database)
```

Request flow for `GET /users/1`:

```
UserRoutes → UserService → UserRepository → dbQuery → PostgreSQL
```

---

## Code added / changed

### `Application.kt` — composition root

A single Ktor module listed in `application.yaml`. Dependencies are registered in **Ktor's built-in
DI plugin (`ktor-server-di`)** and injected at module boundaries — feature modules still receive
their dependencies explicitly, as the Ktor docs recommend ("a module has no knowledge of how its
dependencies are created"):

```kotlin
fun Application.module() {
    DatabaseFactory.init(environment.config)          // infrastructure

    dependencies {                                    // ktor-server-di registry
        provide { UserRepository() }
        provide { UserService(resolve()) }
    }

    configureSerialization()                          // cross-cutting plugins
    configureStatusPages()
    configureSecurity()
    configureRouting()
    configureHttp()

    val userService: UserService by dependencies      // resolved from the registry
    userModule(userService)                           // feature modules
}
```

### `core/database/DatabaseFactory.kt`

- Composes the JDBC url from `postgres.host` / `postgres.port` / `postgres.db` (Ktor yaml
  substitution is whole-value only) and connects via `org.jetbrains.exposed.v1.jdbc.Database.connect`
  with `postgres.user` / `postgres.password`.
- Creates tables on startup with `SchemaUtils.create(UserTable)` — **template shortcut**; use a
  migration tool (e.g. Flyway) in a real project.
- Exposes `dbQuery { ... }`, a thin wrapper over Exposed's coroutine-safe
  `suspendTransaction(database) { ... }`, so repositories stay one-liners:

```kotlin
suspend fun <T> dbQuery(block: suspend JdbcTransaction.() -> T): T =
    suspendTransaction(DatabaseFactory.database) { block() }
```

### `core/plugins/*` — cross-cutting Ktor plugins (one file each)

- **`Serialization.kt`** — installs `ContentNegotiation { json() }`.
- **`StatusPages.kt`** — `StatusPages` plugin: any uncaught route exception becomes a `500` response.
- **`Security.kt`** — `Authentication` plugin with a named JWT provider (`jwt("auth-jwt")`). Config
  (`secret`, `issuer`, `audience`, `realm`) comes from the `jwt.*` keys in `application.yaml`
  (env-overridable), not hardcoded. **No routes are protected by default** — wrap routes in
  `authenticate("auth-jwt") { ... }` to require a valid bearer token.
- **`Http.kt`** — `swaggerUI(path = "openapi")`. With no explicit source configured, Ktor generates
  the OpenAPI document from the application's routing tree (`ktor-server-routing-openapi`), so the
  docs at <http://localhost:8080/openapi> always match the registered routes with zero maintenance.
- **`Routing.kt`** — the classic `GET / → "Hello, World!"` smoke-test endpoint (no DB needed) and
  `staticResources("/static", "static")` serving `resources/static` under `/static`.

### `src/test/kotlin/.../ApplicationTest.kt` — smoke test

Uses `ktor-server-test-host` (`testApplication`). It overrides the config with an empty
`MapApplicationConfig` so `application.yaml` (and therefore the full module with its PostgreSQL
connection) is **not** loaded — the test installs only `configureRouting()` and asserts
`GET / → 200 "Hello, World!"`. Runs as part of `./gradlew build`, no database required.

### `features/user/*` — the example feature slice

- **`User.kt`** — plain domain model (`data class User(id, name, email)`), independent of HTTP and DB.
- **`UserTable.kt`** — Exposed `IntIdTable` object (`users` table) **and** the DAO entity
  `UserEntity : IntEntity` with `toDomain()` mapping. This is the ORM part of Exposed.
- **`UserDto.kt`** — `@Serializable UserRequest` (POST/PUT body) and `UserResponse` (API output) so
  the domain model never leaks into the API.
- **`UserRepository.kt`** — persistence (`findAll`, `findById`, `create`, `update`, `delete`) using
  the Exposed DAO API inside `dbQuery`, e.g.:

  ```kotlin
  suspend fun create(name: String, email: String): User = dbQuery {
      UserEntity.new {
          this.name = name
          this.email = email
      }.toDomain()
  }
  ```

- **`UserService.kt`** — business logic layer (currently a pass-through; add validation/rules here).
- **`UserRoutes.kt`** — `fun Application.userModule(service: UserService)` with REST CRUD under
  `/users`; maps domain results to status codes (201/200/204/400/404).

### `src/main/resources/application.yaml`

```yaml
ktor:
  deployment:
    port: "$BACKEND_PORT"
  application:
    modules:
      - cz.cvut.fit.gykas.ApplicationKt.module

postgres:
  host: "$POSTGRES_HOST"
  port: "$POSTGRES_PORT"
  db: "$POSTGRES_DB"
  user: "$POSTGRES_USER"
  password: "$POSTGRES_PASSWORD"

jwt:
  secret: "$JWT_SECRET:secret"
  issuer: "$JWT_ISSUER:http://localhost:8080/"
  audience: "$JWT_AUDIENCE:gykas"
  realm: "$JWT_REALM:gykas"
```

**Single source of truth: the `.env` file in the repository root** (the same file docker-compose
reads). The [io.github.dsdolzhenko.dotenv](https://plugins.gradle.org/plugin/io.github.dsdolzhenko.dotenv)
Gradle plugin loads `../.env` into the environment of the `run`/`test` tasks (see
`build.gradle.kts`), so every `"$VAR"` above resolves from `.env` when started via `./gradlew run`.
Every `$VAR` name here exists in `.env` — real environment variables still override it in
deployment. Note: Ktor substitutes only **whole-value** `"$VAR"` references (no mid-string
interpolation), which is why the JDBC url is composed from parts in `DatabaseFactory`.

### `build.gradle.kts` (changed)

The `io.github.dsdolzhenko.dotenv` Gradle plugin is applied and pointed at `../.env`
(`dotenv { files.set(listOf("../.env")) }`), which loads it into the environment of the `run`/`test`
tasks. Dependencies: Ktor + Exposed + PostgreSQL, plus the starter plugins, minimally wired:

```kotlin
dependencies {
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.netty)
    implementation(ktorLibs.server.config.yaml)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.serialization.kotlinx.json)
    implementation(ktorLibs.server.di)
    implementation(ktorLibs.server.statusPages)
    implementation(ktorLibs.server.auth)
    implementation(ktorLibs.server.auth.jwt)
    implementation(ktorLibs.server.swagger)
    implementation(ktorLibs.server.routingOpenapi)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)

    implementation(libs.logback.classic)

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
```

### `gradle/libs.versions.toml` (changed)

Removed entries that are no longer used (`exposed-r2dbc`, `h2database-h2`, `h2database-r2dbc`).
Ktor artifacts come from the Ktor version catalog (`io.ktor:ktor-version-catalog:3.6.0`, declared in
`settings.gradle.kts`).

---

## Decisions (and why)

- **Feature-based structure** — recommended by the Ktor docs for medium-sized apps; each feature is a
  vertical slice that can evolve independently. Cross-cutting code (plugins, DB) is in `core/`.
- **Dependency injection via Ktor's built-in `ktor-server-di`** — dependencies are registered in the
  `dependencies { provide { ... } }` registry in `Application.module()` and resolved at module
  boundaries (`val service: UserService by dependencies`), keeping feature-module signatures
  explicit per the Ktor docs. First-party and version-aligned with Ktor (ships from the Ktor version
  catalog, no extra version to manage); the project is expected to grow, so DI is in place from the
  start. (Third-party alternatives like Koin would work the same way, with more features but an
  extra dependency.)
- **Single `UserRepository` class, no interface + implementation split** — with exactly one
  persistence backend, an interface would be indirection without payoff (YAGNI), so the repository is
  one concrete class. If you later need a second backend or fakes for tests, extract an interface
  from it (a one-click IDE refactoring).
- **Exposed DAO = the ORM** — Exposed ships two APIs: the SQL-like DSL and the DAO (ORM-style entity)
  API. The template uses the **DAO API** (`IntEntity`, `EntityClass`) since the goal is ORM usage,
  with the classic blocking **JDBC** driver wrapped in `suspendTransaction` for coroutine safety.
  The R2DBC variant was removed to keep a single, simple stack.
- **Single source of truth for environment values: `../.env`** — loaded into the `run`/`test` task
  environment by the `io.github.dsdolzhenko.dotenv` Gradle plugin, so the backend and
  docker-compose always agree (Ktor itself has no dotenv mechanism; its yaml substitution only sees
  process env vars). Defaults after `:` exist only where a sensible dev fallback exists (JWT); the
  database and port have none — if `.env` is missing, startup fails fast and loudly. Running outside
  Gradle (`java -jar`) needs real env vars instead (e.g. `env_file:` in compose).
- **Schema auto-creation on startup** (`SchemaUtils.create`) — keeps the template self-contained and
  runnable with zero setup; documented in-code that migrations (Flyway) belong in real projects.
- **All starter plugins are kept, minimally wired** — DI, Status Pages, JWT auth, Swagger UI, static
  content, and test scaffolding all live in `core/plugins/` (plus `ApplicationTest.kt`), configured
  but not enforced (e.g. no route requires JWT until wrapped in `authenticate("auth-jwt")`). What was
  permanently removed is only the generator's *demo code*: the H2/R2DBC `UsersService` sample, the
  raw-JDBC `CityService` sample, `GreetingService`, and the H2/R2DBC driver dependencies — they
  duplicate or conflict with the real PostgreSQL + Exposed DAO stack.
- **Swagger docs are generated from the routing tree** — `swaggerUI(path = "openapi")` with no
  explicit source makes Ktor build the OpenAPI document from the registered routes, so the docs never
  drift from the code and no spec file has to be maintained. (A static spec file can be supplied
  later via `swaggerFile` if richer descriptions are needed.)
- **JWT config lives in `application.yaml`, not code** — with development defaults and env-var
  overrides, same pattern as the database config. The default `secret` is for local development only.

## HTTP API

| Method | Path         | Body          | Success          | Errors        |
|--------|--------------|---------------|------------------|---------------|
| GET    | `/`          | —             | 200 text         | —             |
| GET    | `/static/*`  | —             | 200 file         | 404           |
| GET    | `/openapi`   | —             | 200 Swagger UI   | —             |
| GET    | `/users`     | —             | 200 JSON list    | —             |
| GET    | `/users/{id}`| —             | 200 `UserResponse`| 400, 404     |
| POST   | `/users`     | `UserRequest` | 201 `UserResponse`| 400          |
| PUT    | `/users/{id}`| `UserRequest` | 200 `UserResponse`| 400, 404     |
| DELETE | `/users/{id}`| —             | 204              | 400, 404      |

`UserRequest` = `{"name": "...", "email": "..."}`,
`UserResponse` = `{"id": 1, "name": "...", "email": "..."}`.

## Verification (commands actually run, with results)

```bash
# build (compiles and runs the smoke test — no database needed)
./gradlew build                                   # BUILD SUCCESSFUL, test PASSED

# database (the compose service one folder up; on a machine without docker compose,
# the equivalent plain container command was used for verification):
docker compose up -d postgres                     # or:
podman run --name gykas-postgres \
  -e POSTGRES_USER=user -e POSTGRES_PASSWORD=password -e POSTGRES_DB=gykasDB \
  -p 127.0.0.1:5432:5432 -d postgres:17

# run
./gradlew run                                     # Responding at http://0.0.0.0:8080

# plugins (all verified OK)
curl -s localhost:8080/                                  # 200 Hello, World!
curl -s localhost:8080/static/index.html                 # 200 <h1>Hello Ktor!</h1>
curl -s localhost:8080/openapi                           # 200 text/html (Swagger UI)
curl -s localhost:8080/openapi/documentation.yaml        # 200 generated OpenAPI spec of all routes

# CRUD smoke test (all verified OK)
curl -s -X POST localhost:8080/users -H 'Content-Type: application/json' \
     -d '{"name":"Alice","email":"alice@example.com"}'   # 201 {"id":1,...}
curl -s localhost:8080/users                             # 200 [ {...}, {...} ]
curl -s localhost:8080/users/1                           # 200 {"id":1,...}
curl -s -X PUT localhost:8080/users/1 -H 'Content-Type: application/json' \
     -d '{"name":"Alice Updated","email":"alice2@example.com"}'  # 200 updated
curl -s -X DELETE localhost:8080/users/1                 # 204
curl -s localhost:8080/users/1                           # 404
curl -s localhost:8080/users/abc                         # 400
```

The `users` table was also inspected in PostgreSQL (`\d users`): columns `id` (serial PK),
`name` (varchar 50), `email` (varchar 255, unique) — created automatically by `SchemaUtils.create`.

## Versions

| Component        | Version  |
|------------------|----------|
| Kotlin           | 2.4.0    |
| Ktor             | 3.6.0    |
| dotenv (Gradle plugin) | 0.1.0 |
| Exposed          | 1.3.1    |
| PostgreSQL JDBC  | 42.7.13  |
| Logback          | 1.5.37   |
| Gradle (wrapper) | 9.5.1    |
| JVM toolchain    | 21       |
| PostgreSQL (compose) | 17   |

## Useful Gradle tasks

| Task             | Description      |
|------------------|------------------|
| `./gradlew build`| Build the project|
| `./gradlew run`  | Run the server   |
