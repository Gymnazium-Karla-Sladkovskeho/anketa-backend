<<<<<<<< HEAD:backend/src/main/kotlin/cz/cvut/fit/gykas/features/schoolclass/infrastructure/Classes.kt
package cz.cvut.fit.gykas.features.schoolclass.infrastructure
========
package cz.cvut.fit.gykas.features.student.infrastructure
>>>>>>>> feat/class:backend/src/main/kotlin/cz/cvut/fit/gykas/features/student/infrastructure/Classes.kt

import cz.cvut.fit.gykas.features.student.domain.Class
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

    fun toDomain(): Class = Class(this.id.value, this.name)
}
