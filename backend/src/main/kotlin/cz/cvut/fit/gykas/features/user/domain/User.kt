package cz.cvut.fit.gykas.features.user.domain

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val passwordHash: String
)
