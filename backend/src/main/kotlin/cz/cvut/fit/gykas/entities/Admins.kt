package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Admins : IntIdTable("admins") {
    val pollCreators = reference("id", PollCreators)
}