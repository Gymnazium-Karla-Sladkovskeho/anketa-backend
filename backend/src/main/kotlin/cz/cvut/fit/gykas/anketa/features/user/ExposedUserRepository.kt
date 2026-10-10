package cz.cvut.fit.gykas.anketa.features.user

import cz.cvut.fit.gykas.anketa.core.database.dbQuery
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll

/**
 * Exposed ORM implementation of [UserRepository].
 * All queries run inside suspending JDBC transactions via [dbQuery].
 */
class ExposedUserRepository : UserRepository {

    override suspend fun findAll(): List<User> = dbQuery {
        UserTable.selectAll().map { it.toUser() }
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        UserTable.selectAll()
            .where { UserTable.id eq id }
            .singleOrNull()
            ?.toUser()
    }

    override suspend fun create(name: String, email: String): User = dbQuery {
        val id = UserTable.insertAndGetId {
            it[UserTable.name] = name
            it[UserTable.email] = email
        }.value
        User(id = id, name = name, email = email)
    }

    private fun ResultRow.toUser() = User(
        id = this[UserTable.id].value,
        name = this[UserTable.name],
        email = this[UserTable.email],
    )
}
