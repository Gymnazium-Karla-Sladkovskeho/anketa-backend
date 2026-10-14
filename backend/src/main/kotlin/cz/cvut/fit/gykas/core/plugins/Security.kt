package cz.cvut.fit.gykas.core.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cz.cvut.fit.gykas.core.auth.JwtProvider
import io.ktor.server.application.Application
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import sun.security.util.KeyUtil.validate

/**
 * JWT bearer authentication, configured from the `jwt.*` keys in application.yaml
 * (overridable via the JWT_SECRET / JWT_ISSUER / JWT_AUDIENCE / JWT_REALM env vars).
 *
 * No routes are protected by default — wrap routes in `authenticate("auth-jwt") { ... }`
 * to require a valid token.
 */
fun Application.configureSecurity(jwtProvider: JwtProvider) {
    val jwtRealm = environment.config.property("jwt.realm").getString()

    authentication {
        bearer("auth-bearer") {
            realm = jwtRealm

            authenticate { tokenCredential ->
                jwtProvider.getUser(tokenCredential.token)
            }
        }
    }
}
