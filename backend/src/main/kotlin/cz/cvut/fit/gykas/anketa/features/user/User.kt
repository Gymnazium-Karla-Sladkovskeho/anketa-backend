package cz.cvut.fit.gykas.anketa.features.user

/**
 * Domain model of the `user` feature.
 * Independent of HTTP (Ktor) and persistence (Exposed) concerns.
 */
data class User(
    val id: Int,
    val name: String,
    val email: String,
)
