package cz.cvut.fit.gykas.features.student.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class EditStudentRequest(val name: String, val lastname: String, val `class`: String)
