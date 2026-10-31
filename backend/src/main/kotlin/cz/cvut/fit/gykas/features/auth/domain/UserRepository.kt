package cz.cvut.fit.gykas.features.auth.domain

interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun findByUuid(uuid: String): User?
    suspend fun update(id: Int,
                       firstName: String?,
                       lastName: String?,
                       passwordHash: String?): User?
    suspend fun create(firstName: String, lastName: String, passwordHash: String): User
    suspend fun delete(id: Int): Boolean
}