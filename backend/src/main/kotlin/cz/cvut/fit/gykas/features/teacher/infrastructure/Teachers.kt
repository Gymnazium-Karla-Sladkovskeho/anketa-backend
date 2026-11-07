package cz.cvut.fit.gykas.features.teacher.infrastructure

import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreators
import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import cz.cvut.fit.gykas.features.student.infrastructure.Students
import cz.cvut.fit.gykas.features.teacher.domain.Teacher
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

// Weak entity: ID is also a foreign key to PollCreators.
object Teachers : IdTable<Int>("teachers") {
    override val id = integer("id").entityId()
        .references(PollCreators.id)
        .references(Users.id)
    val isActive = bool("is_active")
    override val primaryKey = PrimaryKey(id, name = "pk_teachers")
}

class TeacherEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TeacherEntity>(Teachers)
    var isActive by Teachers.isActive
    var user by UserEntity referencedOn Teachers.id

    fun toDomain() = Teacher(
        id.value,
        user.firstName,
        user.lastName,
        user.uuid,
        isActive)
}
