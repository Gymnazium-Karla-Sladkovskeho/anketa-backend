package cz.cvut.fit.gykas.features.user

import kotlinx.serialization.Serializable

/** Body of POST /users and PUT /users/{id}. */
@Serializable
data class UserRequest(
    val name: String,
    val email: String,
)

/** JSON representation of a user returned by the API. */
@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
)

fun User.toResponse() = UserResponse(id, name, email)
