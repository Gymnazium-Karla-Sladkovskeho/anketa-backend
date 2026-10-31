package cz.cvut.fit.gykas.features.auth.infrastructure

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object PollCreators : IdTable<Int>("poll_creators") {
    override val id = integer("id").entityId().references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "pk_poll_creators")
}

class PollCreatorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollCreatorEntity>(PollCreators)
    var user by UserEntity referencedOn PollCreators.id
}
