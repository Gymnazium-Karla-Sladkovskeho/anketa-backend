package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object StudentLists : IntIdTable("student_lists") {
    val pollCreators = reference("poll_creators_id", PollCreators)
    val name = varchar("name", length = 256).nullable()
}