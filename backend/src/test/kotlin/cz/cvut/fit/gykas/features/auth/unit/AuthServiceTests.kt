package cz.cvut.fit.gykas.features.auth.unit

import cz.cvut.fit.gykas.core.auth.JwtProvider
import cz.cvut.fit.gykas.core.auth.JwtToken
import cz.cvut.fit.gykas.core.auth.ROLE_STUDENT
import cz.cvut.fit.gykas.features.auth.application.AuthService
import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthServiceTests {
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
        isPollCreator = false,
        isTeacher = true,
        isStudent = false
    )

    @Test
    fun returnsUserById() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns null

        val authService = AuthService(ur, hp, jwtp)
        assertEquals(usr1, authService.getById(1))
        assertEquals(null, authService.getById(2))
    }

    @Test
    fun returnsUserByUuid() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findByUuid("petr.svetr") } returns usr1
        coEvery { ur.findByUuid("petr.metr") } returns null

        val authService = AuthService(ur, hp, jwtp)
        assertEquals(usr1, authService.getByUuid("petr.svetr"))
        assertEquals(null, authService.getByUuid("petr.metr"))
    }

    @Test
    fun resetsPassword() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns usr2
        coEvery { ur.update(1, any(), any(), any()) } returns usr1
        coEvery { ur.update(2, any(), any(), any()) } returns usr2
        coEvery { hp.getRandomPassword(7) } returns "aaaaaaa"
        coEvery { hp.getRandomPassword(10) } returns "aaaaaaaaaa"
        coEvery { hp.hashPassword("aaaaaaa") } returns "hashed"
        coEvery { hp.hashPassword("aaaaaaaaaa") } returns "hashed long"

        val authService = AuthService(ur, hp, jwtp)
        val pwdResetResStudent = authService.resetPassword(1)
        val pwdResetResTeacher = authService.resetPassword(2)

        assertEquals("aaaaaaa", pwdResetResStudent)
        assertEquals("aaaaaaaaaa", pwdResetResTeacher)
    }

    @Test
    fun setsPassword() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns usr2
        coEvery { ur.update(1, any(), any(), "hashedstud") } returns
                usr1.copy(passwordHash = "hashedstud")
        coEvery { ur.update(2, any(), any(), "hashedteach") } returns
                usr2.copy(passwordHash = "hashedteach")
        coEvery { hp.hashPassword("stud") } returns "hashedstud"
        coEvery { hp.hashPassword("teach") } returns "hashedteach"

        val authService = AuthService(ur, hp, jwtp)
        val pwdResetResStudent = authService.setPassword(1, "stud")
        val pwdResetResTeacher = authService.setPassword(2, "teach")

        assertEquals("hashedstud", pwdResetResStudent?.passwordHash)
        assertEquals("hashedteach", pwdResetResTeacher?.passwordHash)
    }

    @Test
    fun verifiesPassword() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns usr2
        coEvery { hp.verify("hashstudent", "student") } returns true
        coEvery { hp.verify("hashteacher", "teacher") } returns true

        val authService = AuthService(ur, hp, jwtp)
        assert(authService.verifyPassword(1, "student"))
        assert(authService.verifyPassword(2, "teacher"))
    }

    @Test
    fun createsToken() = runBlocking {
        val ur = mockk<UserRepository>()
        val hp = mockk<PasswordHashProvider>()
        val jwtp = mockk<JwtProvider>()
        coEvery { ur.findById(1) } returns usr1
        coEvery { ur.findById(2) } returns usr2
        coEvery { jwtp.generateToken(any()) } returns
                JwtToken("token", "Bearer", 123456)

        val authService = AuthService(ur, hp, jwtp)
        val token = authService.createToken("petr.svetr", setOf(ROLE_STUDENT))

        assertEquals("token", token.accessToken)
        assertEquals("Bearer", token.tokenType)
        assertEquals(123456, token.expiresIn)
    }
}