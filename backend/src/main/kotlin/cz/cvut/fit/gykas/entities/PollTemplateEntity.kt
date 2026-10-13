package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class PollTemplateEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollTemplateEntity>(PollTemplates)

    val pollCreator by PollCreatorEntity referencedOn PollTemplates.pollCreators
    var name by PollTemplates.name
    var isGlobal by PollTemplates.isGlobal
}