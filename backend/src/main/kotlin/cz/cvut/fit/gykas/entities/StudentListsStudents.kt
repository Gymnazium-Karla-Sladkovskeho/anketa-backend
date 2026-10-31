package cz.cvut.fit.gykas.entities

import org.jetbrains.exposed.v1.core.dao.id.CompositeID
import org.jetbrains.exposed.v1.core.dao.id.CompositeIdTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.dao.CompositeEntity
import org.jetbrains.exposed.v1.dao.CompositeEntityClass

object StudentListsStudents : CompositeIdTable("student_lists_students") {
    val studentList = reference("student_lists_id", StudentLists)
    val studentId = integer("students_id")
    override val primaryKey = PrimaryKey(studentList, studentId)
}

class StudentListStudentEntity(id: EntityID<CompositeID>) : CompositeEntity(id) {
    companion object : CompositeEntityClass<StudentListStudentEntity>(StudentListsStudents)
    var studentList by StudentListEntity referencedOn StudentListsStudents.studentList
    var studentId by StudentListsStudents.studentId
}
