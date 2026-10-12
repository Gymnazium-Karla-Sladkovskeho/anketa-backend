package cz.cvut.fit.gykas

import cz.cvut.fit.gykas.core.plugins.configureRouting
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    @Test
    fun `GET root returns Hello World`() = testApplication {
        environment {
            // Do not load application.yaml: it would start the full module,
            // including the PostgreSQL connection.
            config = MapApplicationConfig()
        }
        application {
            configureRouting()
        }

        val response = client.get("/")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, World!", response.bodyAsText())
    }
}
