package cz.cvut.fit.gykas.features.user.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.user.domain.User
import cz.cvut.fit.gykas.features.user.domain.UserRepository

/** Persistence of the user feature: PostgreSQL via the Exposed DAO (ORM-style) API.
 *  If you ever need a second backend or fakes for tests, extract an interface from this class. */
class UserRepositoryImpl : UserRepository {

    override suspend fun findAll(): List<User> = dbQuery {
        UserEntity.all().map(UserEntity::toDomain)
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        UserEntity.findById(id)?.toDomain()
    }

    override suspend fun create(name: String, email: String): User = dbQuery {
        UserEntity.new {
            this.name = name
            this.email = email
        }.toDomain()
    }

    override suspend fun update(id: Int, name: String, email: String): User? = dbQuery {
        UserEntity.findByIdAndUpdate(id) {
            it.name = name
            it.email = email
        }?.toDomain()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        UserEntity.findById(id)?.also { it.delete() } != null
    }
}
