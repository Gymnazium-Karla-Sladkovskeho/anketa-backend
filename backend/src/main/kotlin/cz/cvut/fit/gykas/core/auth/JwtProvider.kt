package cz.cvut.fit.gykas.core.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import io.ktor.server.auth.roles
import io.ktor.server.config.ApplicationConfig
import java.util.Date

interface JwtProvider {
    fun generateToken(authUser: AuthUser): String
    fun getUser(token: String): AuthUser?
}

class JwtProviderImpl(val config: ApplicationConfig) : JwtProvider {
    private val secret = config.property("jwt.secret").getString()
    private val durationSeconds = 3600
    private val algorithm = Algorithm.HMAC256(secret)

    override fun generateToken(authUser: AuthUser): String {
        return JWT.create()
            .withSubject(authUser.username)
            .withClaim("roles", authUser.roles.joinToString(","))
            .withIssuedAt(Date(System.currentTimeMillis() + durationSeconds * 1000))
            .withIssuer(config.property("jwt.issuer").getString())
            .withAudience(config.property("jwt.audience").getString())
            .sign(algorithm)
    }

    override fun getUser(token: String): AuthUser? =
        try {
            val verifiedRes = JWT.require(algorithm).build().verify(token)
            val name = verifiedRes.subject
            val roles = verifiedRes.getClaim("roles").asString().split(',')

            AuthUser(name, roles.toSet())
        } catch (_: JWTVerificationException) {
            null
        }
}