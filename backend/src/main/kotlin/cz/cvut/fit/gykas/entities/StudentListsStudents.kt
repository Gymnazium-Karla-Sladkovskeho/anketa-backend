package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

object StudentListsStudents : IntIdTable("student_lists_students") {
    val studentLists = reference("student_lists_id", StudentLists)
    val students = reference("students_id", Students)
}