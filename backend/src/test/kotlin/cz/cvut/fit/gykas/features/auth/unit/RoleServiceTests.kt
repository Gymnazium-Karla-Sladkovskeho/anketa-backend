package cz.cvut.fit.gykas.features.auth.unit

import cz.cvut.fit.gykas.core.auth.JwtProvider
import cz.cvut.fit.gykas.core.auth.JwtToken
import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_STUDENT
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.application.RoleService
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class RoleServiceTests {
    val usr1 = User(1,
        "Petr",
        "Svetr",
        "hashstudent",
        "petr.svetr",
        isAdmin = false,
        isPollCreator = false,
        isTeacher = false,
        isStudent = true
    )

    val usr2 = User(2,
        "Marek",
        "Párek",
        "hashteacher",
        "marek.parek",
        isAdmin = false,
        isPollCreator = true,
        isTeacher = true,
        isStudent = false
    )

    val usr3 = User(3,
        "Ondra",
        "Odra",
        "hashadmin",
        "ondra.odra",
        isAdmin = true,
        isPollCreator = true,
        isTeacher = false,
        isStudent = false
    )

    val usr4 = User(3,
        "Bídák",
        "CoNemáRole",
        "hashbídák",
        "bídák.conemárole",
        isAdmin = false,
        isPollCreator = false,
        isTeacher = false,
        isStudent = false
    )

    @Test
    fun hasCorrectRoles() = runBlocking {
        val ur = mockk<UserRepository>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns usr2
        coEvery { ur.findById(3) } returns usr3
        coEvery { ur.findById(4) } returns usr4

        val roleService = RoleService(ur)

        assertEquals(setOf(ROLE_STUDENT), roleService.getRoles(1))
        assertEquals(setOf(ROLE_TEACHER, ROLE_POLL_CREATOR), roleService.getRoles(2))
        assertEquals(setOf(ROLE_ADMIN, ROLE_POLL_CREATOR), roleService.getRoles(3))
        assertEquals(setOf(), roleService.getRoles(4))
    }
}