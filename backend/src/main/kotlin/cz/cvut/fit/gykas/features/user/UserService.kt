package cz.cvut.fit.gykas.features.user

/** Business logic of the user feature. Currently a thin pass-through over the
 *  repository — this is where validation and domain rules should live. */
class UserService(private val repository: UserRepository) {

    suspend fun getAll(): List<User> = repository.findAll()

    suspend fun getById(id: Int): User? = repository.findById(id)

    suspend fun create(request: UserRequest): User =
        repository.create(request.name, request.email)

    suspend fun update(id: Int, request: UserRequest): User? =
        repository.update(id, request.name, request.email)

    suspend fun delete(id: Int): Boolean = repository.delete(id)
}
