package cz.cvut.fit.gykas.core.database

import cz.cvut.fit.gykas.entities.Answers
import cz.cvut.fit.gykas.features.student.infrastructure.Classes
import cz.cvut.fit.gykas.entities.CloseQuestionOptions
import cz.cvut.fit.gykas.entities.PollTemplates
import cz.cvut.fit.gykas.entities.Polls
import cz.cvut.fit.gykas.entities.Questions
import cz.cvut.fit.gykas.entities.StudentLists
import cz.cvut.fit.gykas.entities.StudentListsStudents
import cz.cvut.fit.gykas.features.admin.infrastructure.Admins
import cz.cvut.fit.gykas.features.pollcreator.infrastructure.PollCreators
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import cz.cvut.fit.gykas.features.student.infrastructure.Students
import cz.cvut.fit.gykas.features.teacher.infrastructure.Teachers
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {

    lateinit var database: Database
        private set

    /**
     * Connects to PostgreSQL using the `postgres.*` keys from application.yaml
     * (which resolve from the .env file / environment — see build.gradle.kts)
     * and creates the tables.
     *
     * The JDBC url is composed here because Ktor's yaml substitution only
     * supports whole-value "$VAR" references, not mid-string interpolation.
     *
     * All tables are created so the app can boot against an empty database
     * (e.g. the blackbox test database, see backend/blackbox/).
     *
     * Template shortcut: [SchemaUtils.create] on startup.
     * For a real project use migrations (e.g. Flyway) instead.
     */
    fun init(config: ApplicationConfig) {
        val url = "jdbc:postgresql://" +
            config.property("postgres.host").getString() + ":" +
            config.property("postgres.port").getString() + "/" +
            config.property("postgres.db").getString()

        database = Database.connect(
            url = url,
            driver = "org.postgresql.Driver",
            user = config.property("postgres.user").getString(),
            password = config.property("postgres.password").getString(),
        )

        transaction(database) {
            SchemaUtils.create(
                Users, PollCreators, Admins, Teachers, Classes, Students,
                StudentLists, StudentListsStudents, Polls, PollTemplates,
                Questions, CloseQuestionOptions, Answers,
            )
        }
    }
}

/** Runs [block] in a suspend transaction against the connected database. */
suspend fun <T> dbQuery(block: suspend JdbcTransaction.() -> T): T =
    suspendTransaction(DatabaseFactory.database) { block() }
