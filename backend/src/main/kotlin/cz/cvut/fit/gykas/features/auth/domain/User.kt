package cz.cvut.fit.gykas.features.auth.domain

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val passwordHash: String
)
