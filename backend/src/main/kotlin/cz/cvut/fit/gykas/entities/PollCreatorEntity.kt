package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class PollCreatorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollCreatorEntity>(PollCreators)

    
}