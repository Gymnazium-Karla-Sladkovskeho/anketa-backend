package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import cz.cvut.fit.gykas.features.student.domain.Student
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Students : IdTable<Int>("students") {
    override val id = integer("id").entityId().references(Users.id)
    val classesId = reference("classes_id", Classes)
    override val primaryKey = PrimaryKey(id, name = "pk_students")
}

class StudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentEntity>(Students)
    var schoolClass by ClassEntity referencedOn Students.classesId
    var user by UserEntity referencedOn Students.id
}

fun StudentEntity.toDomain() = Student(user.id.value,
    user.firstName,
    user.lastName,
    schoolClass.name,
    user.uuid)