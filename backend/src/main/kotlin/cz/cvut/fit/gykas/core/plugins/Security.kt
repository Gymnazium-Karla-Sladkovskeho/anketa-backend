package cz.cvut.fit.gykas.core.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

/**
 * JWT bearer authentication, configured from the `jwt.*` keys in application.yaml
 * (overridable via the JWT_SECRET / JWT_ISSUER / JWT_AUDIENCE / JWT_REALM env vars).
 *
 * No routes are protected by default — wrap routes in `authenticate("auth-jwt") { ... }`
 * to require a valid token.
 */
fun Application.configureSecurity() {
    val jwtSecret = environment.config.property("jwt.secret").getString()
    val jwtIssuer = environment.config.property("jwt.issuer").getString()
    val jwtAudience = environment.config.property("jwt.audience").getString()
    val jwtRealm = environment.config.property("jwt.realm").getString()

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtIssuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
        }
    }
}
