package cz.cvut.fit.gykas.features.student.unit

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_STUDENT
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.features.auth.application.RoleService
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import cz.cvut.fit.gykas.features.student.application.ClassService
import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.ClassRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassServiceTests {
    val classes = arrayOf(Class(1, "3.A"),
        Class(2, "4.A"),
        Class(4, "5.A"))

    @Test
    fun getAll() = runBlocking {
        val cr = mockk<ClassRepository>()
        coEvery { cr.getAll() } returns classes

        val roleService = ClassService(cr)

        assertEquals(roleService.getAll(), classes)
    }
}