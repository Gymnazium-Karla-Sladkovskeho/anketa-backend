package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Teachers : IntIdTable("teachers") {
    val pollCreators = reference("id", PollCreators)
    val isActive = bool("is_active")
}