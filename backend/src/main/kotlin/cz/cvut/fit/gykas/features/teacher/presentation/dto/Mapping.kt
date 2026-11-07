package cz.cvut.fit.gykas.features.teacher.presentation.dto

import cz.cvut.fit.gykas.features.student.presentation.dto.CreateStudentResponse
import cz.cvut.fit.gykas.features.teacher.domain.Teacher
import cz.cvut.fit.gykas.features.teacher.domain.TeacherWithPassword

fun TeacherWithPassword.toCreateTeacherResponse() = CreateStudentResponse(
    this.teacher.username, this.password
)

fun Teacher.toTeacherResponse() = TeacherResponse(
    this.id, this.name, this.lastname, this.active, this.username
)
