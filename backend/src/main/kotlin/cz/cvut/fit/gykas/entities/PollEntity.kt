package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class PollEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollEntity>(Polls)

    val pollCreator by PollCreatorEntity referencedOn Polls.pollCreators
    val studentList by StudentListEntity referencedOn Polls.studentLists
    var name by Polls.name
    var startDate by Polls.startDate
    var endDate by Polls.endDate
}