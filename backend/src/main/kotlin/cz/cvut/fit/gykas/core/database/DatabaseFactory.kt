package cz.cvut.fit.gykas.core.database

import cz.cvut.fit.gykas.features.user.UserTable
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {

    lateinit var database: Database
        private set

    /**
     * Connects to PostgreSQL using the `postgres.*` keys from application.yaml
     * (which can be overridden by the DB_URL / DB_USER / DB_PASSWORD environment
     * variables) and creates the tables.
     *
     * Template shortcut: [SchemaUtils.create] on startup.
     * For a real project use migrations (e.g. Flyway) instead.
     */
    fun init(config: ApplicationConfig) {
        database = Database.connect(
            url = config.property("postgres.url").getString(),
            driver = "org.postgresql.Driver",
            user = config.property("postgres.user").getString(),
            password = config.property("postgres.password").getString(),
        )

        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }
}

/** Runs [block] in a suspend transaction against the connected database. */
suspend fun <T> dbQuery(block: suspend JdbcTransaction.() -> T): T =
    suspendTransaction(DatabaseFactory.database) { block() }
