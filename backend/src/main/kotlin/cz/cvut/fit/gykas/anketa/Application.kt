package cz.cvut.fit.gykas.anketa

import cz.cvut.fit.gykas.anketa.core.database.DatabaseFactory
import cz.cvut.fit.gykas.anketa.core.plugins.configureSerialization
import cz.cvut.fit.gykas.anketa.features.user.ExposedUserRepository
import cz.cvut.fit.gykas.anketa.features.user.UserService
import cz.cvut.fit.gykas.anketa.features.user.UserTable
import cz.cvut.fit.gykas.anketa.features.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

/**
 * Application entry point, referenced from `application.yaml`.
 *
 * Acts as the composition root: infrastructure is created here and
 * dependencies are injected into feature modules at the module boundary,
 * as recommended in https://ktor.io/docs/server-application-structure.html
 */
fun Application.module() {
    // Infrastructure: connect to the database and create schema for feature tables
    DatabaseFactory.init(environment.config, UserTable)

    // Manual dependency injection: repository -> service -> routes
    val userRepository = ExposedUserRepository()
    val userService = UserService(userRepository)

    // Plugins
    configureSerialization()

    // Feature routes
    routing {
        userRoutes(userService)
    }
}
