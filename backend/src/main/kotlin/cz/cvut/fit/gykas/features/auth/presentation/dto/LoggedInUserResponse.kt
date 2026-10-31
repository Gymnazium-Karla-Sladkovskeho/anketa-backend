package cz.cvut.fit.gykas.features.auth.presentation.dto

import kotlinx.serialization.Serializable

@Serializable()
data class LoggedInUserResponse(val name: String, val lastname: String, val role: String)