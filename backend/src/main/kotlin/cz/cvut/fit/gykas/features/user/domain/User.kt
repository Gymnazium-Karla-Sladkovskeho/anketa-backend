package cz.cvut.fit.gykas.features.user.domain

/** Domain model of the user feature. */
data class User(
    val id: Int,
    val name: String,
    val email: String,
)
