package cz.cvut.fit.gykas.features.auth.domain

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val passwordHash: String,
    val isAdmin: Boolean,
    val isPollCreator: Boolean,
    val isTeacher: Boolean,
    val isStudent: Boolean
)
