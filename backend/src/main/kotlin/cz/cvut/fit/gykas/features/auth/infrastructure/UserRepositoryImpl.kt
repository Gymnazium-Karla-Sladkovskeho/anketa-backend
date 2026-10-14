package cz.cvut.fit.gykas.features.auth.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository
import org.jetbrains.exposed.v1.core.ComparisonOp
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.QueryParameter
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq

class UserRepositoryImpl : UserRepository {
    class ILikeOp(expr1: Expression<*>, expr2: Expression<*>) : ComparisonOp(expr1, expr2, "ILIKE")

    infix fun ExpressionWithColumnType<String>.iLike(pattern: String): Op<Boolean> =
        ILikeOp(this, QueryParameter(pattern, columnType))

    override suspend fun findAll(): List<User> = dbQuery {
        UserEntity.all().map(UserEntity::toDomain)
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        UserEntity.findById(id)?.toDomain()
    }

    override suspend fun findByFirstAndLastName(
        firstName: String, lastName: String, caseSensitive: Boolean
    ): List<User> =
        dbQuery {
            (if (caseSensitive) UserEntity.find {
                (Users.firstName eq firstName) and (Users.lastName eq lastName)
            } else UserEntity.find {
                (Users.firstName iLike firstName) and (Users.lastName iLike lastName)
            }).toList().map(UserEntity::toDomain)
        }


    override suspend fun update(
        id: Int, firstName: String?, lastName: String?, passwordHash: String?
    ): User? = dbQuery {
        UserEntity.findById(id)?.apply {
            firstName?.let { this.firstName = it }
            lastName?.let { this.lastName = it }
            passwordHash?.let { this.passwordHash = it }
        }?.toDomain()
    }

    override suspend fun create(
        firstName: String, lastName: String, passwordHash: String
    ): User = dbQuery {
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
