package cz.cvut.fit.gykas.features.auth.presentation.dto

import kotlinx.serialization.Serializable

@Serializable
data class SetPasswordRequest(val password: String)