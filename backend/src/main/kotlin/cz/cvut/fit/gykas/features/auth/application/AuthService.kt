package cz.cvut.fit.gykas.features.auth.application

import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository

class AuthService(private val repository: UserRepository) {
    suspend fun getById(id: Int): User? = repository.findById(id)

    suspend fun resetPassword(id: Int): String? =
        with("") {
            repository.update(id, null, null, this).let {
                if (it == null) return@with null
            }

            this
        }

    suspend fun setPassword(id: Int, password: String): User? =
        with("") {
            repository.update(id, null, null, this)
        }
}
