package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

// Weak entity: its ID is also a foreign key to Users.
object PollCreators : IdTable<Int>("poll_creators") {
    override val id = integer("id").entityId().references(Users.id)
    override val primaryKey = PrimaryKey(id, name = "pk_poll_creators")
}

class PollCreatorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollCreatorEntity>(PollCreators)
    var user by UserEntity referencedOn PollCreators.id
}
