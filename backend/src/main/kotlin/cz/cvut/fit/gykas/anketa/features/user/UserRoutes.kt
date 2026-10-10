package cz.cvut.fit.gykas.anketa.features.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * HTTP routes of the `user` feature.
 * The service is injected, keeping this layer free of construction logic.
 *
 *   GET  /users       -> 200 + list of users
 *   GET  /users/{id}  -> 200 + user | 400 invalid id | 404 not found
 *   POST /users       -> 201 + created user | 400 invalid body
 */
fun Route.userRoutes(service: UserService) {
    route("/users") {
        get {
            call.respond(service.getAll().map(User::toResponse))
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid or missing id")

            val user = service.getById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound, "User not found")

            call.respond(user.toResponse())
        }

        post {
            val request = call.receive<CreateUserRequest>()
            val user = runCatching { service.create(request.name, request.email) }
                .getOrElse { return@post call.respond(HttpStatusCode.BadRequest, it.message.orEmpty()) }

            call.respond(HttpStatusCode.Created, user.toResponse())
        }
    }
}
