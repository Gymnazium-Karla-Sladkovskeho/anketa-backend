package cz.cvut.fit.gykas

import cz.cvut.fit.gykas.core.auth.JwtProvider
import cz.cvut.fit.gykas.core.auth.JwtProviderImpl
import cz.cvut.fit.gykas.core.database.DatabaseFactory
import cz.cvut.fit.gykas.core.plugins.configureHttp
import cz.cvut.fit.gykas.core.plugins.configureRouting
import cz.cvut.fit.gykas.core.plugins.configureSecurity
import cz.cvut.fit.gykas.core.plugins.configureSerialization
import cz.cvut.fit.gykas.core.plugins.configureStatusPages
import cz.cvut.fit.gykas.features.auth.infrastructure.UserRepositoryImpl
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.infrastructure.ArgonHashProvider
import cz.cvut.fit.gykas.features.auth.presentation.authModule
import de.mkammerer.argon2.Argon2Factory
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.*

// Loaded from application.yaml: cz.cvut.fit.gykas.ApplicationKt.module
fun Application.module() {
    // Infrastructure
    DatabaseFactory.init(environment.config)

    // Dependency injection via Ktor's built-in DI plugin (ktor-server-di):
    // dependencies are registered once here and injected at module boundaries.
    dependencies {
        provide { UserRepositoryImpl() }
        provide { AuthService(resolve(), resolve()) }
    }

    // Cross-cutting plugins
    configureSerialization()
    configureStatusPages()
    val jwtProv: JwtProvider by dependencies
    configureSecurity(jwtProv)
    configureRouting()
    configureHttp()

    // Feature modules — resolved from the registry, still passed explicitly
    val userService: AuthService by dependencies
    authModule(userService)
}
