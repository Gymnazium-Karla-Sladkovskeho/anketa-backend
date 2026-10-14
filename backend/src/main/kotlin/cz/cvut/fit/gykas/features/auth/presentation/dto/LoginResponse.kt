package cz.cvut.fit.gykas.features.auth.presentation.dto

data class LoginResponse(val accessToken: String, val tokenType: String, val expiresIn: Int)