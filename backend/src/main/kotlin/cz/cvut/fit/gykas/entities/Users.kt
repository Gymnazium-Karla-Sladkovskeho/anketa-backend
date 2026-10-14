package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Users : IntIdTable("users", "id") {
    val firstName = text("first_name")
    val lastName = text("last_name")
    val passwordHash = text("password_hash")
}

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)
    var firstName by Users.firstName
    var lastName by Users.lastName
    var passwordHash by Users.passwordHash
}
