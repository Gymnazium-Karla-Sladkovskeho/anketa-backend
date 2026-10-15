package cz.cvut.fit.gykas.features.auth.application

import cz.cvut.fit.gykas.core.auth.AuthUser
import cz.cvut.fit.gykas.core.auth.JwtProvider
import cz.cvut.fit.gykas.core.auth.JwtToken
import cz.cvut.fit.gykas.features.auth.domain.User
import cz.cvut.fit.gykas.features.auth.domain.UserRepository

class AuthService(private val repository: UserRepository,
                  private val hashProvider: PasswordHashProvider,
                  private val jwtProvider: JwtProvider
)
{
    suspend fun getById(id: Int): User? = repository.findById(id)

    suspend fun getByUuid(uuid: String): User? {
        val user = repository.findByUuid(uuid) ?: return null

        return repository.findById(user.id ?: return null)
    }

    suspend fun resetPassword(id: Int, len: Int): String? =
        with(hashProvider.getRandomPassword(len)) {
            repository.update(id, null, null, this).let {
                if (it == null) return@with null
            }

            this
        }

    suspend fun setPassword(id: Int, password: String): User? =
        with(hashProvider.hashPassword(password)) {
            repository.update(id, null, null, this)
        }

    suspend fun verifyPassword(id: Int, password: String): Boolean =
        with(getById(id)?.passwordHash ?: return false) {
            hashProvider.verify(this, password)
        }

    fun createToken(username: String, roles: Set<String>): JwtToken =
        jwtProvider.generateToken(AuthUser(username, roles))
}
