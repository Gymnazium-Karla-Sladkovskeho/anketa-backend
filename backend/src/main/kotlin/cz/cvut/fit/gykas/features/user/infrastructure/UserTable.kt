package cz.cvut.fit.gykas.features.user.infrastructure

import cz.cvut.fit.gykas.features.user.domain.User
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserTable : IntIdTable("users") {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val passwordHash = text("password_hash")
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(UserTable)

    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var passwordHash by UserTable.passwordHash

    fun toDomain() = User(id.value, firstName, lastName, passwordHash)
}
