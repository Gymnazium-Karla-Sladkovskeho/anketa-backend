package cz.cvut.fit.gykas.anketa

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun `create and list users`() = testApplication {
        // Isolate the test on its own in-memory database
        environment {
            config = MapApplicationConfig(
                "database.url" to "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
                "database.driver" to "org.h2.Driver",
                "database.user" to "sa",
                "database.password" to "",
            )
        }
        application { module() }

        val created = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Ada","email":"ada@example.com"}""")
        }
        assertEquals(HttpStatusCode.Created, created.status)

        val list = client.get("/users")
        assertEquals(HttpStatusCode.OK, list.status)
        assertTrue(list.bodyAsText().contains("ada@example.com"))

        val single = client.get("/users/1")
        assertEquals(HttpStatusCode.OK, single.status)
        assertTrue(single.bodyAsText().contains("Ada"))
    }
}
