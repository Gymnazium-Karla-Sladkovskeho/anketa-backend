package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Questions : IntIdTable("questions") {
    val polls = reference("polls_id", Polls)
    val title = varchar("title", length = 256)
    val isMandatory = bool("is_mandatory")
    val type = integer("type")
}