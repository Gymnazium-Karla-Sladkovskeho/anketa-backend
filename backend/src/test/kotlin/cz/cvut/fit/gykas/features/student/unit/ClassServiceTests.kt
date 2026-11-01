package cz.cvut.fit.gykas.features.student.unit

import cz.cvut.fit.gykas.features.student.application.ClassService
import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.ClassRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ClassServiceTests {
    val classes = arrayOf(
        Class(1, "3.A"),
        Class(2, "4.A"),
        Class(3, "5.A")
    )

    @Test
    fun getAll() = runBlocking {
        val cr = mockk<ClassRepository>()
        coEvery { cr.findAll() } returns classes

        val roleService = ClassService(cr)

        assertEquals(roleService.getAll(), classes)
    }

    @Test
    fun getByName() = runBlocking {
        val cr = mockk<ClassRepository>()
        coEvery { cr.findByName("3.A") } returns classes[0]
        coEvery { cr.findByName("4.A") } returns classes[1]
        coEvery { cr.findByName("5.A") } returns classes[2]

        val roleService = ClassService(cr)

        for (cl in classes) {
            assertEquals(roleService.getByName(cl.name), cl)
        }
    }

    @Test
    fun deleteById() = runBlocking {
        val cr = mockk<ClassRepository>()
        coEvery { cr.delete(1) } returns true
        coEvery { cr.delete(2) } returns true
        coEvery { cr.delete(3) } returns true
        coEvery { cr.delete(4) } returns false

        val roleService = ClassService(cr)

        for ((id) in classes) {
            assert(roleService.delete(id))
        }

        assert(!roleService.delete(4))
    }

    @Test
    fun deleteByName() = runBlocking {
        val cr = mockk<ClassRepository>()
        coEvery { cr.deleteByName("3.A") } returns true
        coEvery { cr.deleteByName("4.A") } returns true
        coEvery { cr.deleteByName("5.A") } returns true
        coEvery { cr.deleteByName("6.A") } returns false

        val roleService = ClassService(cr)

        for ((_, name) in classes) {
            assert(roleService.deleteByName(name))
        }

        assert(!roleService.deleteByName("6.A"))
    }

    @Test
    fun createClass() = runBlocking {
        val cr = mockk<ClassRepository>()

        for (cl in classes) {
            coEvery { cr.create(cl.name) } returns cl
        }

        val roleService = ClassService(cr)

        for (cl in classes) {
            assertEquals(roleService.create(cl.name), cl)
        }
    }
}