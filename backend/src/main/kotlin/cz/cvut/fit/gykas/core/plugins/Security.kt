package cz.cvut.fit.gykas.core.plugins

import cz.cvut.fit.gykas.core.auth.JwtProvider
import cz.cvut.fit.gykas.core.auth.JwtProviderImpl
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.infrastructure.ArgonHashProvider
import de.mkammerer.argon2.Argon2Factory
import de.mkammerer.argon2.Argon2Helper
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.di.*


/**
 * JWT bearer authentication, configured from the `jwt.*` keys in application.yaml
 * (overridable via the JWT_SECRET / JWT_ISSUER / JWT_AUDIENCE / JWT_REALM env vars).
 *
 * No routes are protected by default — wrap routes in `authenticate("auth-jwt") { ... }`
 * to require a valid token.
 */
fun Application.configureSecurity() {
    val jwtRealm = environment.config.property("jwt.realm").getString()

    dependencies {
        provide<JwtProvider> { JwtProviderImpl(this@configureSecurity.environment.config) }
        provide<PasswordHashProvider> {
            val argon2 = Argon2Factory.create()
            val memory = 65536
            val parallelism = 1

            val iterations = Argon2Helper.findIterations(argon2, 1000, memory, parallelism)
            ArgonHashProvider(argon2, iterations, memory, parallelism)
        }
    }

    val jwtProvider: JwtProvider by dependencies

    authentication {
        bearer("auth") {
            realm = jwtRealm

            authenticate { tokenCredential ->
                jwtProvider.getUser(tokenCredential.token)
            }
        }
    }
}
