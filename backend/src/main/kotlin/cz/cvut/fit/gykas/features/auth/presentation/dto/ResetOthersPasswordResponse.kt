package cz.cvut.fit.gykas.features.auth.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResetOthersPasswordResponse(val username: String, val password: String)