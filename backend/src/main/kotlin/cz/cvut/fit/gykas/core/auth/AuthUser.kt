package cz.cvut.fit.gykas.core.auth

data class AuthUser(val username: String, val roles : Set<String>)
