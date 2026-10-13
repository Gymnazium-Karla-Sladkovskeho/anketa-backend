package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

object CloseQuestionOptions : CompositeIdTable("close_question_options") {
    val text = integer("text")
    val question = integer("id").entityId().references(Questions.id)
    override val primaryKey = PrimaryKey(text, question)
}

class CloseQuestionOptionEntity(id: EntityID<org.jetbrains.exposed.v1.core.dao.id.CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<CloseQuestionOptionEntity>(CloseQuestionOptions)
    var text by CloseQuestionOptions.text
    var question by QuestionEntity referencedOn CloseQuestionOptions.question
}
