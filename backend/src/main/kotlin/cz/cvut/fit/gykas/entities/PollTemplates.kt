package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object PollTemplates : IntIdTable("poll_templates") {
    val pollCreators = reference("poll_creators_id", PollCreators)
    val name = varchar("name", length = 256)
    val isGlobal = bool("is_global")
}