package cz.cvut.fit.gykas.anketa.features.user

/**
 * Repository interface (port) of the `user` feature.
 * The service depends only on this abstraction; the Exposed-based
 * implementation lives in the infrastructure side of the feature
 * (see [ExposedUserRepository]).
 */
interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun create(name: String, email: String): User
}
