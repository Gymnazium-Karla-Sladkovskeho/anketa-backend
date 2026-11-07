package cz.cvut.fit.gykas.features.teacher.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTeacherResponse(val username: String, val password: String)