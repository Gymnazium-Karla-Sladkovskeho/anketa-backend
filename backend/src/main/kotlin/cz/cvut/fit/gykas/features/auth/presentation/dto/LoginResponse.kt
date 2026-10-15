package cz.cvut.fit.gykas.features.auth.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val accessToken: String, val tokenType: String, val expiresIn: Int)