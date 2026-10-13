package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Questions : IntIdTable("questions", "id") {
    val poll = reference("polls_id", Polls)
    val title = text("title")
    val isMandatory = bool("is_mandatory")
    val type = integer("type")
}

class QuestionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestionEntity>(Questions)
    var poll by PollEntity referencedOn Questions.poll
    var title by Questions.title
    var isMandatory by Questions.isMandatory
    var type by Questions.type
}
