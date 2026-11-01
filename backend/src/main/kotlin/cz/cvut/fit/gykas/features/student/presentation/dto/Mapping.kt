package cz.cvut.fit.gykas.features.student.presentation.dto

import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentWithPassword

fun Class.toClassResponse() = ClassResponse(this.name)

fun StudentWithPassword.toCreateStudentResponse() = CreateStudentResponse(
    this.student.username, this.password
)

fun Student.toStudentResponse() = StudentResponse(
    this.id, this.name, this.lastname, this.className, this.username
)
