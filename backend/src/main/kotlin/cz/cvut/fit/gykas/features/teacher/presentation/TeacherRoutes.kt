package cz.cvut.fit.gykas.features.teacher.presentation

import cz.cvut.fit.gykas.core.auth.AuthUser
import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.core.auth.withRoles
import cz.cvut.fit.gykas.core.pagination.Page
import cz.cvut.fit.gykas.core.pagination.PageRequest
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.teacher.application.TeacherService
import cz.cvut.fit.gykas.features.teacher.presentation.dto.CreateTeacherRequest
import cz.cvut.fit.gykas.features.teacher.presentation.dto.EditTeacherRequest
import cz.cvut.fit.gykas.features.teacher.presentation.dto.toCreateTeacherResponse
import cz.cvut.fit.gykas.features.teacher.presentation.dto.toTeacherResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlin.text.toIntOrNull

fun Route.teacherModule(teacherService: TeacherService, authService: AuthService) {
    withRoles(ROLE_ADMIN) {
        get {
            val page = call.queryParameters["page"]?.toLongOrNull() ?: 1
            val pageSize = call.queryParameters["pageSize"]?.toIntOrNull() ?: 25

            return@get call.respond(
                HttpStatusCode.OK,
                with(teacherService.findAllPaginated(PageRequest(page, pageSize))) {
                    Page(
                        page,
                        totalPages,
                        pageSize,
                        this.payload.map { it.toTeacherResponse() })
                }
            )
        }

        post {
            val request = call.receive<CreateTeacherRequest>()

            val teachWithPasswd = teacherService.create(
                request.name, request.lastname, request.username
            ) ?: return@post call.respond(HttpStatusCode.BadRequest)

            return@post call.respond(
                HttpStatusCode.Created, teachWithPasswd.toCreateTeacherResponse()
            )
        }

        put("{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)

            val request = call.receive<EditTeacherRequest>()

            if (!teacherService.update(id, request.name, request.lastname, request.active)) return@put call.respond(
                HttpStatusCode.NotFound
            )

            return@put call.respond(HttpStatusCode.NoContent)

        }

        delete("{id}") {
            val id = call.pathParameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)

            if (teacherService.delete(id)) return@delete call.respond(HttpStatusCode.NoContent)

            return@delete call.respond(HttpStatusCode.NotFound)
        }
    }

    /**
    No roles are required here; even [ROLE_TEACHER] can
    call this endpoint. I am either ADMIN or I am
    viewing my own information; otherwise, a 403 error is returned.
     */
    get("{id}") {
        val id = call.pathParameters["id"]?.toIntOrNull() ?: return@get call.respond(HttpStatusCode.BadRequest)

        val me = call.principal<AuthUser>() ?: return@get call.respond(HttpStatusCode.Unauthorized)

        val allow = arrayOf(
            ROLE_ADMIN
        ).any { it in me.roles } || authService.getByUuid(me.uuid).let {
            if (it == null) return@get call.respond(HttpStatusCode.Unauthorized)
            it.id == id
        }

        if (!allow) return@get call.respond(HttpStatusCode.Forbidden)

        val teacherResponse =
            teacherService.findById(id)?.toTeacherResponse() ?: return@get call.respond(HttpStatusCode.NotFound)

        return@get call.respond(HttpStatusCode.OK, teacherResponse)
    }
}