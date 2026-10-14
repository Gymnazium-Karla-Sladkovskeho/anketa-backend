package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object Classes : IntIdTable("classes", "id") {
    val name = text("name")
}

class ClassEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ClassEntity>(Classes)
    var name by Classes.name
}
