package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class CloseQuestionOptionEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CloseQuestionOptionEntity>(CloseQuestionOptions)

    val question by QuestionEntity referencedOn CloseQuestionOptions.questions
    var optionText by CloseQuestionOptions.optionText
}