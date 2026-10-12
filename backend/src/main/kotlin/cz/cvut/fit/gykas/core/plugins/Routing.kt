package cz.cvut.fit.gykas.core.plugins

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        // Smoke-test endpoint that does not need the database.
        get("/") {
            call.respondText("Hello, World!")
        }
        // Static files from resources/static served under /static.
        staticResources("/static", "static")
    }
}
