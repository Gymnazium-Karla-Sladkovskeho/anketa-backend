package cz.cvut.fit.gykas.features.user.presentation.dto

import kotlinx.serialization.Serializable

/** Body of POST /users and PUT /users/{id}. */
@Serializable
data class UserRequest(
    val name: String,
    val email: String,
)