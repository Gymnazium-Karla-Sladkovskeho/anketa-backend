package cz.cvut.fit.gykas.features.teacher.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeacherRequest(val name: String, val lastname: String, val username: String)
