package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

class StudentListsStudentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StudentListsStudentEntity>(StudentListsStudents)

    val studentList by StudentListEntity referencedOn StudentListsStudents.studentLists
    val student by StudentEntity referencedOn StudentListsStudents.students
}