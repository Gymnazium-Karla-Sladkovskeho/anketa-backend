package cz.cvut.fit.gykas.entities

import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

object Answers : CompositeIdTable("answers") {
    val question = integer("id").entityId().references(Questions.id)
    val studentId = integer("students_id")
    val user = reference("users_id", Users)
    val poll = reference("polls_id", Polls)
    val answer = text("answer")
    override val primaryKey = PrimaryKey(question, studentId, user, poll)
}

class AnswerEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<AnswerEntity>(Answers)
    var question by QuestionEntity referencedOn Answers.question
    var studentId by Answers.studentId
    var user by UserEntity referencedOn Answers.user
    var poll by PollEntity referencedOn Answers.poll
    var answer by Answers.answer
}
