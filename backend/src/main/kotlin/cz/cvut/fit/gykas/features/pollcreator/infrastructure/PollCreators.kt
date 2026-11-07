package cz.cvut.fit.gykas.features.pollcreator.infrastructure

import cz.cvut.fit.gykas.features.admin.infrastructure.AdminEntity
import cz.cvut.fit.gykas.features.admin.infrastructure.Admins
import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import cz.cvut.fit.gykas.features.pollcreator.domain.PollCreator
import cz.cvut.fit.gykas.features.teacher.infrastructure.TeacherEntity
import cz.cvut.fit.gykas.features.teacher.infrastructure.Teachers
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object PollCreators : IdTable<Int>("poll_creators") {
    override val id = integer("id").entityId().references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "pk_poll_creators")
}

class PollCreatorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollCreatorEntity>(PollCreators)
    var user by UserEntity.Companion referencedOn PollCreators.id
    val teacher by TeacherEntity optionalBackReferencedOn Teachers.id
    val admin by AdminEntity optionalBackReferencedOn Admins.id
}

fun PollCreatorEntity.toDomain(): PollCreator = PollCreator(id.value)
