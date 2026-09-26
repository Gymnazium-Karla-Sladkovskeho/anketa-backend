package cz.cvut.fit.gykas.features.user.domain

interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun findById(id: Int): User?
    suspend fun create(name: String, email: String): User
    suspend fun update(id: Int, name: String, email: String): User?
    suspend fun delete(id: Int): Boolean
}