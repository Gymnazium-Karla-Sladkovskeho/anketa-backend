package cz.cvut.fit.gykas.features.student.presentation

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.core.auth.withRoles
import cz.cvut.fit.gykas.features.student.application.StudentService
import cz.cvut.fit.gykas.features.student.presentation.dto.CreateStudentRequest
import cz.cvut.fit.gykas.features.student.presentation.dto.EditStudentRequest
import cz.cvut.fit.gykas.features.student.presentation.dto.StudentResponse
import cz.cvut.fit.gykas.features.student.presentation.dto.toCreateStudentResponse
import cz.cvut.fit.gykas.features.student.presentation.dto.toStudentResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import cz.cvut.fit.gykas.core.auth.AuthUser
import cz.cvut.fit.gykas.features.auth.application.AuthService

fun Route.studentModule(studentService: StudentService, authService: AuthService) {
    withRoles(ROLE_ADMIN) {
        post {
            val request = call.receive<CreateStudentRequest>()

            val studWithPasswd = studentService.create(
                request.name, request.lastname, request.`class`, request.username
            ) ?: return@post call.respond(HttpStatusCode.BadRequest)

            return@post call.respond(
                HttpStatusCode.Created, studWithPasswd.toCreateStudentResponse()
            )
        }

        put("{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull()
                ?: return@put call.respond(HttpStatusCode.BadRequest)

            val request = call.receive<EditStudentRequest>()

            if (!studentService.update(id, request.name, request.lastname, request.`class`))
                return@put call.respond(HttpStatusCode.NotFound)

            return@put call.respond(HttpStatusCode.NoContent)

        }

        delete("{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (studentService.delete(id))
                return@delete call.respond(HttpStatusCode.NoContent)

            return@delete call.respond(HttpStatusCode.NotFound)
        }
    }

    withRoles(ROLE_POLL_CREATOR, ROLE_TEACHER, ROLE_ADMIN) {
        get {
            val classes = call.queryParameters["classes"].let {
                if (it == null) return@get call.respond(HttpStatusCode.BadRequest)
                if (it.isEmpty()) return@get call.respond(
                    HttpStatusCode.OK,
                    arrayOf<StudentResponse>()
                )

                it.split(',').map {
                    it.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)
                }.toTypedArray()
            }

            return@get call.respond(
                HttpStatusCode.OK,
                studentService.findAllByClasses(classes).map { it.toStudentResponse() }
            )
        }
    }

    get("{id}") {
        val id = call.pathParameters["id"]?.toIntOrNull()
            ?: return@get call.respond(HttpStatusCode.BadRequest)

        val me = call.principal<AuthUser>() ?: return@get call.respond(HttpStatusCode.Unauthorized)

        val allow =
            arrayOf(ROLE_POLL_CREATOR, ROLE_TEACHER, ROLE_ADMIN).any { it in me.roles }
                    ||
                    authService.getByUuid(me.uuid).let {
                        if (it == null) return@get call.respond(HttpStatusCode.Unauthorized)
                        it.id == id
                    }

        if (!allow) return@get call.respond(HttpStatusCode.Forbidden)

        val studentResponse = studentService.findById(id)?.toStudentResponse()
            ?: return@get call.respond(HttpStatusCode.NotFound)

        return@get call.respond(HttpStatusCode.OK, studentResponse)
    }
}