package cz.cvut.fit.gykas.anketa.core.plugins

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

/**
 * JSON (de)serialization for request/response bodies via kotlinx.serialization.
 */
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
