package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object CloseQuestionOptions : IntIdTable("close_question_options") {
    val questions = reference("questions_id", Questions)
    val optionText = varchar("option_text", length = 256)
}