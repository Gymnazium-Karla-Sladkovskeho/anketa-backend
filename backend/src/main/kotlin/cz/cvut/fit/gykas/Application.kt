package cz.cvut.fit.gykas

import cz.cvut.fit.gykas.core.database.DatabaseFactory
import cz.cvut.fit.gykas.core.plugins.configureHttp
import cz.cvut.fit.gykas.core.plugins.configureRouting
import cz.cvut.fit.gykas.core.plugins.configureSecurity
import cz.cvut.fit.gykas.core.plugins.configureSerialization
import cz.cvut.fit.gykas.core.plugins.configureStatusPages
import cz.cvut.fit.gykas.features.auth.infrastructure.UserRepositoryImpl
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.RoleService
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.auth.presentation.authModule
import cz.cvut.fit.gykas.features.auth.presentation.loginModule
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.module() {
    DatabaseFactory.init(environment.config)

    dependencies {
        provide { RoleService(resolve()) }
        provide<UserRepository> { UserRepositoryImpl() }
        provide { AuthService(resolve(), resolve(), resolve()) }
    }

    configureSerialization()
    configureStatusPages()
    configureSecurity()
    configureRouting()
    configureHttp()

    val userService: AuthService by dependencies
    val roleService: RoleService by dependencies

    routing {
        loginModule(userService, roleService)

        authenticate("auth") {
            route("/auth") {
                authModule(userService)
            }
        }
    }
}
