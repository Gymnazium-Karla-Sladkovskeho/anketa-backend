package cz.cvut.fit.gykas.features.auth.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository

class UserRepositoryImpl : UserRepository {

    override suspend fun findAll(): List<User> = dbQuery {
        UserEntity.all().map(UserEntity::toDomain)
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        UserEntity.findById(id)?.toDomain()
    }

    override suspend fun update(
        id: Int,
        firstName: String?,
        lastName: String?,
        passwordHash: String?
    ): User? = dbQuery {
        UserEntity.findById(id)?.apply {
            firstName?.let { this.firstName = it }
            lastName?.let { this.lastName = it }
            passwordHash?.let { this.passwordHash = it }
        }?.toDomain()
    }

    override suspend fun create(firstName: String,
                                lastName: String,
                                passwordHash: String): User = dbQuery {
        UserEntity.new {
            this.firstName = firstName
            this.lastName = lastName
            this.passwordHash = passwordHash
        }.toDomain()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        UserEntity.findById(id)?.also { it.delete() } != null
    }
}
