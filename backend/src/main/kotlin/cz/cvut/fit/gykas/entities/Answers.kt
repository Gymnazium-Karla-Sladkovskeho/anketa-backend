package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Answers : IntIdTable("answers") {
    val students = reference("students_id", Students)
    val questions = reference("questions_id", Questions)
    val polls = reference("polls_id", Polls)
    val answer = text("answer")
}