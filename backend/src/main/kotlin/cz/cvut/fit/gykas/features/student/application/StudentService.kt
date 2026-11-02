package cz.cvut.fit.gykas.features.student.application

import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentRepository
import cz.cvut.fit.gykas.features.student.domain.StudentWithPassword
import io.ktor.util.reflect.TypeInfo

class StudentService(
    private val userRepository: UserRepository,
    private val passwordHashProvider: PasswordHashProvider,
    private val studentRepository: StudentRepository,
    private val classService: ClassService
) {
    private suspend fun removeClassIfNoMember(classId: Int) =
        with(studentRepository.findAllByClasses(arrayOf(classId))) {
            if (isNotEmpty()) return@with

            require(classService.delete(classId))
        }

    suspend fun findAllByClassesName(classes: Array<String>) =
        studentRepository.findAllByClassesName(classes)

    suspend fun findById(id: Int) = studentRepository.findById(id)

    suspend fun create(name: String, lastName: String, className: String, username: String): StudentWithPassword? {
        if (userRepository.findByUuid(username) != null) return null

        val newPasswd = passwordHashProvider.getRandomPassword(PasswordHashProvider.STUDENT_PASSWORD_LEN)
        val hashedPasswd = passwordHashProvider.hashPassword(newPasswd)
        val classId = classService.getByName(className)?.id ?: classService.create(className).id
        val newUser = userRepository.create(name, lastName, username, hashedPasswd)

        return StudentWithPassword(studentRepository.create(newUser.id, classId), newPasswd)
    }

    suspend fun update(id: Int, name: String, lastname: String, className: String): Boolean {
        val stud = studentRepository.findById(id) ?: return false
        val oldClassName = stud.className

        val newName = name.trim().let { if (stud.name != it) it else null }
        val newLastName = lastname.trim().let { if (stud.lastname != it) it else null }
        val newClassName = className.trim().let { if (stud.className != it) it else null }

        userRepository.update(id, newName, newLastName, null) ?: return false

        if (newClassName != null) {
            val newClass = classService.getByName(newClassName) ?: classService.create(newClassName)

            classService.getByName(oldClassName)!!.id.run {
                removeClassIfNoMember(this)
            }

            require(studentRepository.update(id, newClass.id) != null)
        }

        return true
    }

    suspend fun delete(id: Int): Boolean {
        val stud = studentRepository.findById(id) ?: return false
        val classId = classService.getByName(stud.className)?.id
        require(classId != null)
        removeClassIfNoMember(classId)

        require(studentRepository.delete(id))
        require(userRepository.delete(id))
        return true
    }
}