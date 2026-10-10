package cz.cvut.fit.gykas.core.plugins

import io.ktor.server.application.Application
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing

fun Application.configureHttp() {
    routing {
        // Serves Swagger UI at /openapi (spec at /openapi/documentation.yaml).
        // With no explicit source, the OpenAPI document is generated from the
        // application's routing tree (ktor-server-routing-openapi).
        swaggerUI(path = "openapi")
    }
}
