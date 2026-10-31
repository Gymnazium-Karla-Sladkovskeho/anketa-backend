package cz.cvut.fit.gykas.entities

import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreators
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object StudentLists : IntIdTable("student_lists", "id") {
    val pollCreator = reference("poll_creators_id", PollCreators)
    val name = text("name").nullable()
}

class StudentListEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentListEntity>(StudentLists)
    var pollCreator by PollCreatorEntity referencedOn StudentLists.pollCreator
    var name by StudentLists.name
}
