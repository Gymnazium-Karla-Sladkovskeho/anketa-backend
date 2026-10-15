package cz.cvut.fit.gykas.features.auth.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoggedInUserResponse(val name: String, val lastName: String, val role: String)