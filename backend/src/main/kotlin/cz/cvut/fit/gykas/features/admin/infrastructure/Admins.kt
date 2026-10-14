package cz.cvut.fit.gykas.features.admin.infrastructure

import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreators
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

// Weak entity: ID is also a foreign key to PollCreators.
object Admins : IdTable<Int>("admins") {
    override val id = integer("id").entityId().references(PollCreators.id)
    override val primaryKey = PrimaryKey(id, name = "pk_admins")
}

class AdminEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AdminEntity>(Admins)
    var pollCreator by PollCreatorEntity referencedOn Admins.id
}
