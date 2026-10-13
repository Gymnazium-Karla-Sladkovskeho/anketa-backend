package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Users : IntIdTable("users") {
    val firstName = varchar("first_name", length = 256)
    val lastName = varchar("last_name", length = 256)
    val passwordHash = text("password_hash")
}