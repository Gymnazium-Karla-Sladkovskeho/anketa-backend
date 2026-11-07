package cz.cvut.fit.gykas.core.database

import cz.cvut.fit.gykas.entities.Answers
import cz.cvut.fit.gykas.entities.CloseQuestionOptions
import cz.cvut.fit.gykas.entities.PollTemplates
import cz.cvut.fit.gykas.entities.Polls
import cz.cvut.fit.gykas.entities.Questions
import cz.cvut.fit.gykas.entities.StudentLists
import cz.cvut.fit.gykas.entities.StudentListsStudents
import cz.cvut.fit.gykas.features.admin.infrastructure.Admins
import cz.cvut.fit.gykas.features.pollcreator.infrastructure.PollCreators
import cz.cvut.fit.gykas.features.auth.infrastructure.Users
import cz.cvut.fit.gykas.features.student.infrastructure.Classes
import cz.cvut.fit.gykas.features.student.infrastructure.Students
import cz.cvut.fit.gykas.features.teacher.infrastructure.Teachers
import io.ktor.server.application.Application
import org.jetbrains.exposed.v1.core.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.migration.jdbc.MigrationUtils
import java.time.LocalDateTime

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun Application.runMigrations() {
    DatabaseFactory.init(environment.config)

    transaction(DatabaseFactory.database) {
        MigrationUtils.generateMigrationScript(
            Users, PollCreators, Admins, Teachers, Classes, Students,
            StudentLists, StudentListsStudents, Polls, PollTemplates,
            Questions, CloseQuestionOptions, Answers,
            scriptDirectory = "src/main/resources/db/migration",
            scriptName = "update_schema${LocalDateTime.now()}"
        )
    }
}