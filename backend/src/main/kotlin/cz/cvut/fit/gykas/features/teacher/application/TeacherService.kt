package cz.cvut.fit.gykas.features.teacher.application

import cz.cvut.fit.gykas.core.pagination.PageRequest
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.teacher.domain.TeacherRepository
import cz.cvut.fit.gykas.features.teacher.domain.TeacherWithPassword

class TeacherService(
    private val userRepository: UserRepository,
    private val passwordHashProvider: PasswordHashProvider,
    private val teacherRepository: TeacherRepository
) {
    suspend fun findAllPaginated(page: PageRequest) = teacherRepository.findPaginated(page)

    suspend fun findById(id: Int) = teacherRepository.findById(id)

    suspend fun create(name: String, lastName: String, username: String): TeacherWithPassword? {
        if (userRepository.findByUuid(username) != null) return null

        val newPasswd = passwordHashProvider.getRandomPassword(PasswordHashProvider.TEACHER_PASSWORD_LEN)
        val hashedPasswd = passwordHashProvider.hashPassword(newPasswd)
        val newUser = userRepository.create(name, lastName, username, hashedPasswd)

        return TeacherWithPassword(teacherRepository.create(newUser.id), newPasswd)
    }

    suspend fun update(id: Int, name: String, lastname: String, active: Boolean): Boolean {
        val teach = teacherRepository.findById(id) ?: return false

        val newName = name.trim().let { if (teach.name != it) it else null }
        val newLastName = lastname.trim().let { if (teach.lastname != it) it else null }

        userRepository.update(id, newName, newLastName, null) ?: return false
        if (teach.active != active) teacherRepository.update(id, active) ?: return false

        return true
    }

    suspend fun delete(id: Int): Boolean {
        teacherRepository.findById(id) ?: return false

        require(teacherRepository.delete(id))
        require(userRepository.delete(id))

        return true
    }
}