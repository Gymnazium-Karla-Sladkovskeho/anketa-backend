package cz.cvut.fit.gykas.features.auth.domain

interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun findByFirstAndLastName(firstName: String,
                                       lastName: String,
                                       caseSensitive: Boolean): List<User>
    suspend fun update(id: Int,
                       firstName: String?,
                       lastName: String?,
                       passwordHash: String?): User?
    suspend fun create(firstName: String, lastName: String, passwordHash: String): User
    suspend fun delete(id: Int): Boolean
}