package cz.cvut.fit.gykas.core.auth

data class AuthUser(val uuid: String, val roles : Set<String>)
