package cz.cvut.fit.gykas.features.auth.presentation

import cz.cvut.fit.gykas.core.auth.AuthUser
import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.withRoles
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.RoleService
import cz.cvut.fit.gykas.features.auth.presentation.dto.LoginRequest
import cz.cvut.fit.gykas.features.auth.presentation.dto.ResetOthersPasswordResponse
import cz.cvut.fit.gykas.features.auth.presentation.dto.SetPasswordRequest
import cz.cvut.fit.gykas.features.auth.presentation.dto.toLoggedInUser
import cz.cvut.fit.gykas.features.auth.presentation.dto.toLoginResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.RoutingRoot
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Route.loginModule(authService: AuthService, roleService: RoleService) {
    post {
        val request = call.receive<LoginRequest>()

        val user = authService.getByUuid(request.username)
            ?: return@post call.respond(HttpStatusCode.Unauthorized)

        if (!authService.verifyPassword(user.id, request.password))
            return@post call.respond(HttpStatusCode.Unauthorized)

        val roles = roleService.getRoles(user.id)
        val response = authService.createToken(request.username, roles).toLoginResponse()

        call.respond(HttpStatusCode.Created, response)
    }
}

fun Route.authModule(authService: AuthService) {
    get("/me") {
        val user = call.principal<AuthUser>()
            ?: return@get call.respond(HttpStatusCode.Unauthorized)

        return@get authService.getByUuid(user.uuid)?.toLoggedInUser()?.let {
            call.respond(HttpStatusCode.OK, it)
        } ?: call.respond(HttpStatusCode.Unauthorized)
    }

    withRoles(ROLE_ADMIN) {
        post("password/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@post call.response.status(HttpStatusCode.BadRequest)

            val user = authService.getById(id)
                ?: return@post call.response.status(HttpStatusCode.NotFound)

            val passwd = authService.resetPassword(id)
                ?: return@post call.response.status(HttpStatusCode.NotFound)

            val resp = ResetOthersPasswordResponse(user.firstName, passwd)

            call.respond(HttpStatusCode.Created, resp)
        }
    }

    patch("/password") {
        val uuid = (call.principal<AuthUser>()
            ?: return@patch call.respond(HttpStatusCode.Unauthorized)).uuid

        val user = authService.getByUuid(uuid)
            ?: return@patch call.respond(HttpStatusCode.Unauthorized)

        val request = call.receive<SetPasswordRequest>()
        authService.setPassword(user.id, request.password)
            ?: return@patch call.response.status(HttpStatusCode.BadRequest)

        return@patch call.respond(HttpStatusCode.NoContent)
    }
}