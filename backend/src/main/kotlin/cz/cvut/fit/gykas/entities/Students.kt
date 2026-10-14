package cz.cvut.fit.gykas.entities

import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

object Students : CompositeIdTable("students") {
    val schoolClass = integer("id").entityId().references(Classes.id)
    val user = integer("users_id").entityId().references(Users.id)
    override val primaryKey = PrimaryKey(schoolClass, user)
}

class StudentEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<StudentEntity>(Students)
    var schoolClass by ClassEntity referencedOn Students.schoolClass
    var user by UserEntity referencedOn Students.user
}
