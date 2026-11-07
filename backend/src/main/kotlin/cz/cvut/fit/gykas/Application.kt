package cz.cvut.fit.gykas

import cz.cvut.fit.gykas.core.database.DatabaseFactory
import cz.cvut.fit.gykas.core.plugins.configureHttp
import cz.cvut.fit.gykas.core.plugins.configureRouting
import cz.cvut.fit.gykas.core.plugins.configureSecurity
import cz.cvut.fit.gykas.core.plugins.configureSerialization
import cz.cvut.fit.gykas.core.plugins.configureStatusPages
import cz.cvut.fit.gykas.features.auth.infrastructure.UserRepositoryImpl
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.RoleService
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.auth.presentation.authModule
import cz.cvut.fit.gykas.features.auth.presentation.loginModule
import cz.cvut.fit.gykas.features.student.application.ClassService
import cz.cvut.fit.gykas.features.student.application.StudentService
import cz.cvut.fit.gykas.features.student.domain.ClassRepository
import cz.cvut.fit.gykas.features.student.domain.StudentRepository
import cz.cvut.fit.gykas.features.student.infrastructure.ClassRepositoryImpl
import cz.cvut.fit.gykas.features.student.infrastructure.StudentRepositoryImpl
import cz.cvut.fit.gykas.features.student.presentation.classModule
import cz.cvut.fit.gykas.features.student.presentation.studentModule
import cz.cvut.fit.gykas.features.teacher.application.TeacherService
import cz.cvut.fit.gykas.features.teacher.domain.TeacherRepository
import cz.cvut.fit.gykas.features.teacher.infrastructure.TeacherRepositoryImpl
import cz.cvut.fit.gykas.features.teacher.presentation.teacherModule
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.di.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.module() {
    DatabaseFactory.init(environment.config)

    dependencies {
        provide { ClassService(resolve()) }
        provide { RoleService(resolve()) }
        provide {
            StudentService(
                resolve(),
                resolve(),
                resolve(),
                resolve()
            )
        }
        provide {
            TeacherService(
                resolve(),
                resolve(),
                resolve()
            )
        }
        provide<TeacherRepository> { TeacherRepositoryImpl() }
        provide<UserRepository> { UserRepositoryImpl() }
        provide<ClassRepository> { ClassRepositoryImpl() }
        provide<StudentRepository> { StudentRepositoryImpl() }
        provide { AuthService(resolve(), resolve(), resolve()) }
    }

    configureSerialization()
    configureStatusPages()
    configureSecurity()
    configureRouting()
    configureHttp()

    val authService: AuthService by dependencies
    val roleService: RoleService by dependencies
    val classService: ClassService by dependencies
    val studentService: StudentService by dependencies
    val teacherService: TeacherService by dependencies

    routing {
        route("/auth") {
            loginModule(authService, roleService)

            authenticate("auth") {
                authModule(authService)
            }
        }
        route("/class") {
            authenticate("auth") {
                classModule(classService)
            }
        }
        route("/student") {
            authenticate("auth") {
                studentModule(studentService, authService)
            }
        }
        route("/teacher") {
            authenticate("auth") {
                teacherModule(teacherService, authService)
            }
        }
    }
}
