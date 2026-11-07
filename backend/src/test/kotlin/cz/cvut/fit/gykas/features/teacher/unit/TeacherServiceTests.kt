package cz.cvut.fit.gykas.features.teacher.unit

import cz.cvut.fit.gykas.core.pagination.Page
import cz.cvut.fit.gykas.core.pagination.PageRequest
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.teacher.application.TeacherService
import cz.cvut.fit.gykas.features.teacher.domain.Teacher
import cz.cvut.fit.gykas.features.teacher.domain.TeacherRepository
import cz.cvut.fit.gykas.features.teacher.domain.TeacherWithPassword
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TeacherServiceTests {
    private val users = arrayOf(
        User(
            1,
            "jenda",
            "pepa",
            "jenda.hash",
            "jenda.pepa",
            isAdmin = true,
            isPollCreator = true,
            isTeacher = true,
            isStudent = false
        ), User(
            2,
            "lubomir",
            "oldrich",
            "lubomir.hash",
            "luboldr",
            isAdmin = false,
            isPollCreator = false,
            isTeacher = true,
            isStudent = false
        )
    )

    private val teacher = Teacher(
        2, users[1].firstName, users[1].lastName, users[1].uuid, true
    )

    private val newTeach = teacher.copy(
        id = users[0].id,
        name = users[0].firstName,
        lastname = users[0].lastName,
        username = "new"
    )

    private val teacherService: TeacherService

    init {
        val ur = mockk<UserRepository>()
        val php = mockk<PasswordHashProvider>()
        val tr = mockk<TeacherRepository>()

        coEvery { ur.findAll() } returns users.toList()
        coEvery { ur.findById(any()) } returns null
        coEvery { ur.findByUuid(any()) } returns null
        coEvery {
            ur.create(newTeach.name, newTeach.lastname, newTeach.username, "rndpasswd.hash")
        } returns users[0].copy(uuid = newTeach.username)
        for (us in users) {
            coEvery { ur.delete(us.id) } returns true
            coEvery { ur.create(us.firstName, us.lastName, us.uuid, us.passwordHash) } returns us
            us.copy(
                firstName = us.firstName + "edit", lastName = us.lastName + "edit"
            ).let {
                coEvery { ur.update(it.id, it.firstName, it.lastName, null) } returns it
            }
            coEvery { ur.findById(us.id) } returns us
            coEvery { ur.findByUuid(us.uuid) } returns us
        }

        for (us in users) {
            coEvery { php.hashPassword(us.passwordHash.removeSuffix(".hash")) } returns us.passwordHash
            coEvery { php.getRandomPassword(PasswordHashProvider.TEACHER_PASSWORD_LEN) } returns "rndpasswd"
        }
        coEvery { php.hashPassword("rndpasswd") } returns "rndpasswd.hash"

        coEvery { tr.findPaginated(PageRequest(1L, 25)) } returns Page(1L, 1, 25, listOf(teacher))
        coEvery { tr.findPaginated(PageRequest(2L, 25)) } returns Page(2L, 1, 25, listOf())
        coEvery { tr.findById(any()) } returns null
        coEvery { tr.findById(teacher.id) } returns teacher
        coEvery { tr.update(teacher.id, true) } returns teacher.copy(active = true)
        coEvery { tr.update(teacher.id, false) } returns teacher.copy(active = false)
        coEvery { tr.create(newTeach.id) } returns newTeach
        coEvery { tr.delete(teacher.id) } returns true

        teacherService = TeacherService(
            ur, php, tr
        )
    }

    @Test
    fun createsTeacher() = runBlocking {


        assertEquals(
            TeacherWithPassword(
                newTeach,
                "rndpasswd"
            ), teacherService.create(newTeach.name, newTeach.lastname, newTeach.username)
        )

        assertEquals<TeacherWithPassword?>(null,
            teacherService.create(teacher.name, teacher.lastname, users[1].uuid)
        )
    }

    @Test
    fun getsTeachersById() = runBlocking {
        assertEquals(teacher, teacherService.findById(teacher.id))
        assert(teacherService.findById(99) == null)
    }

    @Test
    fun getsPaginated() = runBlocking {
        assertEquals(
            Page(1L, 1, 25, listOf(teacher)), teacherService.findAllPaginated(PageRequest(1L, 25))
        )
        assertEquals(
            Page(2L, 1, 25, listOf()), teacherService.findAllPaginated(PageRequest(2L, 25))
        )
    }

    @Test
    fun deletesTeacherById() = runBlocking {
        assert(teacherService.delete(teacher.id))
        assert(!teacherService.delete(99))
    }

    @Test
    fun updatesTeacherById() = runBlocking {
        teacher.copy(
            name = teacher.name + "edit", lastname = teacher.lastname + "edit", active = false
        ).let {
            assert(teacherService.update(teacher.id, it.name, it.lastname, false))
        }

        teacher.copy(
            name = teacher.name + "edit", lastname = teacher.lastname + "edit", active = false
        ).let {
            assert(!teacherService.update(99, it.name, it.lastname, true))
        }
    }
}