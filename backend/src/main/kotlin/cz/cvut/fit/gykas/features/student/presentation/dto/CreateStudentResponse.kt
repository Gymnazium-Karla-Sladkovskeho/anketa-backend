package cz.cvut.fit.gykas.features.student.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateStudentResponse(val username: String, val password: String)
