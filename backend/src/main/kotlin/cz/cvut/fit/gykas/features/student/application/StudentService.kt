package cz.cvut.fit.gykas.features.student.application

import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentRepository

class StudentService(private val userRepository: UserRepository,
                     private val authService: AuthService,
                     private val studentRepository: StudentRepository) {

    suspend fun create(name: String, lastName: String, className: String, username: String): Student? {
        if (userRepository.findByUuid(username) != null) return null

        val newUser = userRepository.create(name, lastName, username, "")
        val newPasswd = authService.resetPassword(newUser.id)
        val classId = 0
        return studentRepository.create(newUser.id, classId)

    }
}