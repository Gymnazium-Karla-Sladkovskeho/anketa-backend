package cz.cvut.fit.gykas.features.teacher.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeacherResponse(val id: Int,
                           val name: String,
                           val lastname: String,
                           val active: Boolean,
                           val username: String)
