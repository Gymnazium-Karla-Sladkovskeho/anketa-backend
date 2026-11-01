package cz.cvut.fit.gykas.features.student.presentation.dto

data class StudentResponse(val id: Int,
                           val name: String,
                           val lastname: String,
                           val `class`: String,
                           val username: String)