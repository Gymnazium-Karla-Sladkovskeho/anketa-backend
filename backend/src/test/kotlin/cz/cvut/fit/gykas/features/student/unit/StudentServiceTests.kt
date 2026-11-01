package cz.cvut.fit.gykas.features.student.unit

import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.student.application.ClassService
import cz.cvut.fit.gykas.features.student.application.StudentService
import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class StudentServiceTests {
    private val users = arrayOf(
        User(1, "jenda", "pepa", "jenda.hash", "jenda.pepa",
            isAdmin = true,
            isPollCreator = true,
            isTeacher = true,
            isStudent = false
        ),
        User(2, "lubomir", "oldrich", "lubomir.hash", "luboldr",
            isAdmin = false,
            isPollCreator = false,
            isTeacher = false,
            isStudent = true
        )
    )

    private val classes = arrayOf(
        Class(1, "3.A"),
        Class(2, "4.A"),
        Class(3, "5.A")
    )

    private val newClass = Class(4, "new")

    private val stud = Student(
        users[1].id,
        users[1].firstName,
        users[1].lastName,
        classes[0].name,
        users[1].uuid
    )

    private val studentService: StudentService

    init {
        val ur = mockk<UserRepository>()
        val php = mockk<PasswordHashProvider>()
        val sr = mockk<StudentRepository>()
        val cs = mockk<ClassService>()

        coEvery { ur.findAll() } returns users.toList()
        for (us in users) {
            coEvery { ur.delete(us.id) } returns true
            coEvery { ur.create(us.firstName, us.lastName, us.uuid, us.passwordHash) } returns us
            us.copy(
                firstName = us.firstName + "edit",
                lastName = us.lastName + "edit"
            ).let {
                coEvery { ur.update(it.id, it.firstName, it.lastName, null) } returns it
            }
            coEvery { ur.findById(us.id) } returns us
            coEvery { ur.findByUuid(us.uuid) } returns us
        }

        for (us in users) {
            coEvery { php.hashPassword(us.passwordHash.removeSuffix(".hash")) } returns us.passwordHash
            coEvery { php.getRandomPassword(PasswordHashProvider.STUDENT_PASSWORD_LEN) } returns "rndpasswd"
        }

        coEvery { sr.findById(any()) } returns null
        coEvery { sr.findById(stud.id) } returns stud
        coEvery { sr.update(stud.id, classes[1].id) } returns stud.copy(className = classes[1].name)
        coEvery { sr.update(stud.id, newClass.id) } returns stud.copy(className = newClass.name)
        coEvery { sr.create(stud.id, classes[0].id) } returns stud
        coEvery { sr.delete(stud.id) } returns true
        coEvery { sr.findAllByClasses(arrayOf(classes[0].id)) } returns arrayOf(stud)

        coEvery { cs.getAll() } returns classes
        coEvery { cs.getByName(any()) } returns null
        for (cl in classes) {
            coEvery { cs.create(cl.name) } returns cl
            coEvery { cs.deleteByName(cl.name) } returns true
            coEvery { cs.delete(cl.id) } returns true
            coEvery { cs.getByName(cl.name) } returns cl
        }

        coEvery { cs.getByName(newClass.name) } returns null
        coEvery { cs.create(newClass.name) } returns newClass

        studentService = StudentService(
            ur,
            php,
            sr,
            cs
        )
    }

    @Test
    fun getsStudentById() = runBlocking {
        assertEquals(studentService.findById(stud.id),stud)
        assert(studentService.findById(99) == null)
    }

    @Test
    fun getsAll() = runBlocking {
        assertContains(
            studentService.findAllByClasses(arrayOf(classes[0].id)),
            stud
        )
    }

    @Test
    fun deletesStudentById() = runBlocking {
        assert(studentService.delete(stud.id))
        assert(!studentService.delete(99))
    }

    @Test
    fun updatesStudentById() = runBlocking {
        stud.copy(
            name = stud.name + "edit",
            lastname = stud.lastname + "edit",
            className = classes[1].name
        ).let {
            assert(studentService.update(stud.id, it.name, it.lastname, it.className))
        }

        stud.copy(
            name = stud.name + "edit",
            lastname = stud.lastname + "edit",
            className = "new"
        ).let {
            assert(studentService.update(stud.id, it.name, it.lastname, it.className))
        }

        stud.copy(
            name = stud.name + "edit",
            lastname = stud.lastname + "edit",
            className = "new"
        ).let {
            assert(!studentService.update(99, it.name, it.lastname, it.className))
        }
    }
}