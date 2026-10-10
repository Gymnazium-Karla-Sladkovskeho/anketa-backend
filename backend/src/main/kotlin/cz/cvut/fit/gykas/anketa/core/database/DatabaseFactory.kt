package cz.cvut.fit.gykas.anketa.core.database

import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Central place that owns the Exposed [Database] connection.
 *
 * Connection settings are read from the `database` block of `application.yaml`.
 * Feature tables are passed in by the caller (the composition root), so this
 * class stays independent of any feature.
 */
object DatabaseFactory {

    fun init(config: ApplicationConfig, vararg tables: Table) {
        val database = Database.connect(
            url = config.property("database.url").getString(),
            driver = config.property("database.driver").getString(),
            user = config.property("database.user").getString(),
            password = config.property("database.password").getString(),
        )

        // Creates tables that do not exist yet (no-op for existing ones).
        transaction(database) {
            SchemaUtils.create(*tables)
        }
    }
}
