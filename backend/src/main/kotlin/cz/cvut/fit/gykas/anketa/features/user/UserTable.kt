package cz.cvut.fit.gykas.anketa.features.user

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

/**
 * Exposed table definition: how the feature is persisted.
 * `IntIdTable` provides an auto-incremented `id` primary key column.
 */
object UserTable : IntIdTable("users") {
    val name = varchar("name", length = 255)
    val email = varchar("email", length = 255).uniqueIndex()
}
