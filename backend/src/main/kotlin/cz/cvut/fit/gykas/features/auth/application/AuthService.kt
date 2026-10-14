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

    suspend fun getByUsername(username: String): User? {
        val spl = username.split('.')
        val name = spl
            .take(2)

        val users = repository.findByFirstAndLastName(
            name[0],
            name[1],
            caseSensitive = false)

        if (users.isEmpty()) return null
        if (users.size == 1) return users.first()
        if (spl.size == 2) return users.first()

        return repository.findById(spl[2].toIntOrNull() ?: return null)
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
