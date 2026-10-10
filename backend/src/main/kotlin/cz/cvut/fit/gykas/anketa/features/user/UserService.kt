package cz.cvut.fit.gykas.anketa.features.user

/**
 * Business logic of the `user` feature.
 * Knows nothing about HTTP or SQL; talks only to the [UserRepository] port.
 */
class UserService(private val repository: UserRepository) {

    suspend fun getAll(): List<User> = repository.findAll()

    suspend fun getById(id: Int): User? = repository.findById(id)

    suspend fun create(name: String, email: String): User {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(email.isNotBlank()) { "E-mail must not be blank" }
        return repository.create(name = name, email = email)
    }
}
