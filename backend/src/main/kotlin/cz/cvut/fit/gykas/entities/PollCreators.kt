package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object PollCreators : IntIdTable("poll_creators") {
    val users = reference("id", Users)
}