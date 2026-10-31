package cz.cvut.fit.gykas.entities

import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreators
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass
import org.jetbrains.exposed.v1.datetime.date

object Polls : IntIdTable("polls", "id") {
    val pollCreator = reference("poll_creators_id", PollCreators)
    val studentList = reference("student_lists_id", StudentLists)
    val name = text("name")
    val startDate = date("start_date")
    val endDate = date("end_date")
}

class PollEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollEntity>(Polls)
    var pollCreator by PollCreatorEntity referencedOn Polls.pollCreator
    var studentList by StudentListEntity referencedOn Polls.studentList
    var name by Polls.name
    var startDate by Polls.startDate
    var endDate by Polls.endDate
}
