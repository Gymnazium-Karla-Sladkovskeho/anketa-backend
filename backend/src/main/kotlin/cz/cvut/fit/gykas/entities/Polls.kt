package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Polls : IntIdTable("polls") {
    val pollCreators = reference("poll_creators_id", PollCreators)
    val studentLists = reference("student_lists_id", StudentLists)
    val name = varchar("name", length = 256)
    val startDate = datetimeValue("start_date")
    val endDate = datetimeValue("end_date")
}