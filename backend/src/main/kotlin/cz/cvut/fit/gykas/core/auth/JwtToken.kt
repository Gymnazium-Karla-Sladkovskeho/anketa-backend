package cz.cvut.fit.gykas.core.auth

data class JwtToken(val accessToken: String, val tokenType: String, val expiresIn: Int)
