package cz.cvut.fit.gykas.features.auth.presentation

import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.presentation.dto.LoginRequest
import cz.cvut.fit.gykas.features.auth.presentation.dto.ResetOthersPasswordResponse
import cz.cvut.fit.gykas.features.auth.presentation.dto.SetPasswordRequest
import cz.cvut.fit.gykas.features.auth.presentation.dto.toLoggedInUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.authModule(service: AuthService) {
    routing {
        route("/auth") {
            get("/me") {
                call.respond(service.getById(0)!!.toLoggedInUser())
            }

            post {
                val request = call.receive<LoginRequest>()

                call.respond(HttpStatusCode.Created, 0)
            }

            post("password/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.response.status(HttpStatusCode.BadRequest)

                val type = call.queryParameters["type"] ?:
                    return@post call.response.status(HttpStatusCode.BadRequest)

                val user = service.getById(id)?:
                    return@post call.response.status(HttpStatusCode.NotFound)

                val passwd = service.resetPassword(id) ?:
                    return@post call.response.status(HttpStatusCode.NotFound)

                val resp = ResetOthersPasswordResponse(user.firstName, passwd)

                call.respond(HttpStatusCode.Created, resp)
            }

            patch("/password") {
                val id = 0

                val request = call.receive<SetPasswordRequest>()
                service.setPassword(id, request.password)
                    ?: return@patch call.response.status(HttpStatusCode.BadRequest)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
