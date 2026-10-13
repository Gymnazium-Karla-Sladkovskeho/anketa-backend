package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class AnswerEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AnswerEntity>(Answers)

    val student by StudentEntity referencedOn Answers.students
    val question by QuestionEntity referencedOn Answers.questions
    val poll by PollEntity referencedOn Answers.polls
    var answer by Answers.answer
}