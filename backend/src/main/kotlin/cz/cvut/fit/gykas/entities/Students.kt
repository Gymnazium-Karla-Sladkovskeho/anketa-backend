package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object Students : IntIdTable("students") {
    val classes = reference("classes_id", Classes)
    val users = reference("users_id", Users)
}