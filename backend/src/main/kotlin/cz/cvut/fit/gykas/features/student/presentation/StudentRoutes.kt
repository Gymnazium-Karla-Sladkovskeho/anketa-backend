package cz.cvut.fit.gykas.features.student.presentation

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.core.auth.withRoles
import cz.cvut.fit.gykas.features.student.application.StudentService
import cz.cvut.fit.gykas.features.student.presentation.dto.CreateStudentRequest
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.studentModule(studentService: StudentService) {
    withRoles(ROLE_ADMIN) {
        post {
            val request = call.receive<CreateStudentRequest>()

            //studentService.create()
        }

        put("{id}") {

        }

        delete("{id}") {

        }
    }

    withRoles(ROLE_POLL_CREATOR, ROLE_TEACHER, ROLE_ADMIN) {
        get {

        }
    }

    get("{id}") {

    }
}