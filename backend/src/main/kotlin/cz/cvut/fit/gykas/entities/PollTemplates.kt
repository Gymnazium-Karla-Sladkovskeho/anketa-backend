package cz.cvut.fit.gykas.entities

import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.PollCreators
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object PollTemplates : IntIdTable("poll_templates", "id") {
    val pollCreator = reference("poll_creators_id", PollCreators)
    val name = text("name")
    val isGlobal = bool("is_global")
}

class PollTemplateEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PollTemplateEntity>(PollTemplates)
    var pollCreator by PollCreatorEntity referencedOn PollTemplates.pollCreator
    var name by PollTemplates.name
    var isGlobal by PollTemplates.isGlobal
}
