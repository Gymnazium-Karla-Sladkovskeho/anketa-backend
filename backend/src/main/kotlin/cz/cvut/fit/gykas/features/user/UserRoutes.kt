package cz.cvut.fit.gykas.features.user

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

/** Feature module: receives its dependencies, knows nothing about how they are
 *  created (see https://ktor.io/docs/server-application-structure.html). */
fun Application.userModule(service: UserService) {
    routing {
        route("/users") {
            get {
                call.respond(service.getAll().map(User::toResponse))
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val user = service.getById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)
                call.respond(user.toResponse())
            }

            post {
                val request = call.receive<UserRequest>()
                val created = service.create(request)
                call.respond(HttpStatusCode.Created, created.toResponse())
            }

            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respond(HttpStatusCode.BadRequest)
                val request = call.receive<UserRequest>()
                val updated = service.update(id, request)
                    ?: return@put call.respond(HttpStatusCode.NotFound)
                call.respond(updated.toResponse())
            }

            delete("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (service.delete(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
