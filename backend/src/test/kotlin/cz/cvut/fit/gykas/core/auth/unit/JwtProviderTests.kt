package cz.cvut.fit.gykas.core.auth.unit

import cz.cvut.fit.gykas.core.auth.ALL_ROLES
import cz.cvut.fit.gykas.core.auth.AuthUser
import cz.cvut.fit.gykas.core.auth.JwtProviderImpl
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class JwtProviderTests {
    val testConfig = MapApplicationConfig(
        "jwt.issuer" to "test-issuer",
        "jwt.audience" to "test-audience",
        "jwt.secret" to "test-secret",
        "jwt.realm" to "/"
    )
    val jwtProviderImpl = JwtProviderImpl(testConfig)

    @Test
    fun returnsValidBearerToken() = testApplication {
        environment {
            config = testConfig
        }

        val token = jwtProviderImpl.generateToken(AuthUser("pepík", setOf("user")))

        assert(token.accessToken.isNotEmpty())
        assertEquals("Bearer",token.tokenType)
        assertEquals(jwtProviderImpl.durationSeconds, token.expiresIn)
    }

    @Test
    fun returnsValidUsernameRoles() = testApplication {
        val usrname = "fsdisuihfisdfsdfd"
        val roles = ALL_ROLES

        environment {
            config = testConfig
        }

        val token = jwtProviderImpl.generateToken(AuthUser(usrname, roles))
        val usr = jwtProviderImpl.getUser(token.accessToken)

        assert(usr != null)
        assertEquals(usrname, usr!!.uuid)
        assertEquals(roles, usr.roles)
    }
}