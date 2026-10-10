package cz.cvut.fit.gykas.anketa.features.user

import kotlinx.serialization.Serializable

/**
 * Data transfer objects: the shape of the HTTP API of this feature.
 * Kept separate from the domain model so the API can evolve independently.
 */

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
)

@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
)

fun User.toResponse() = UserResponse(id = id, name = name, email = email)
