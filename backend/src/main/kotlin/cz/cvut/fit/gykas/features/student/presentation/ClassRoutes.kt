package cz.cvut.fit.gykas.features.student.presentation

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.core.auth.withRoles
import cz.cvut.fit.gykas.features.student.application.ClassService
import cz.cvut.fit.gykas.features.student.presentation.dto.toClassResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get


fun Route.classModule(classService: ClassService) {
    withRoles(ROLE_POLL_CREATOR, ROLE_TEACHER, ROLE_ADMIN) {
        get {
            return@get call.respond(
                HttpStatusCode.OK,
                classService.getAll().map { it.toClassResponse() })
        }
    }
}
