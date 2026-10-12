package cz.cvut.fit.gykas.features.user.presentation.dto

import kotlinx.serialization.Serializable

/** JSON representation of a user returned by the API. */
@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
)