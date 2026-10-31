package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.entities.ClassEntity
import cz.cvut.fit.gykas.entities.Classes
import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Students : IdTable<Int>("students") {
    override val id = integer("id").entityId().references(Users.id)
    val classesId = integer("classes_id").references(Classes.id)
    override val primaryKey = PrimaryKey(id, name = "pk_students")
}

class StudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentEntity>(Students)
    var schoolClass by ClassEntity.Companion referencedOn Students.classesId
    var user by UserEntity referencedOn Students.id
}
