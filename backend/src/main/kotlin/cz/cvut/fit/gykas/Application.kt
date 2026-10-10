package cz.cvut.fit.gykas

import cz.cvut.fit.gykas.core.database.DatabaseFactory
import cz.cvut.fit.gykas.core.plugins.configureHttp
import cz.cvut.fit.gykas.core.plugins.configureRouting
import cz.cvut.fit.gykas.core.plugins.configureSecurity
import cz.cvut.fit.gykas.core.plugins.configureSerialization
import cz.cvut.fit.gykas.core.plugins.configureStatusPages
import cz.cvut.fit.gykas.features.user.UserRepository
import cz.cvut.fit.gykas.features.user.UserService
import cz.cvut.fit.gykas.features.user.userModule
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.*

// Loaded from application.yaml: cz.cvut.fit.gykas.ApplicationKt.module
fun Application.module() {
    // Infrastructure
    DatabaseFactory.init(environment.config)

    // Dependency injection via Ktor's built-in DI plugin (ktor-server-di):
    // dependencies are registered once here and injected at module boundaries.
    dependencies {
        provide { UserRepository() }
        provide { UserService(resolve()) }
    }

    // Cross-cutting plugins
    configureSerialization()
    configureStatusPages()
    configureSecurity()
    configureRouting()
    configureHttp()

    // Feature modules — resolved from the registry, still passed explicitly
    val userService: UserService by dependencies
    userModule(userService)
}
