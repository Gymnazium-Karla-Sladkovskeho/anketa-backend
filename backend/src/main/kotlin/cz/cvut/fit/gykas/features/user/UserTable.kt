package cz.cvut.fit.gykas.features.user

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

/** Exposed table definition for the `users` table. */
object UserTable : IntIdTable("users") {
    val name = varchar("name", 50)
    val email = varchar("email", 255).uniqueIndex()
}

/** Exposed DAO entity (the ORM-style API) mapping a row of [UserTable]. */
class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var name by UserTable.name
    var email by UserTable.email

    fun toDomain() = User(id.value, name, email)
}
