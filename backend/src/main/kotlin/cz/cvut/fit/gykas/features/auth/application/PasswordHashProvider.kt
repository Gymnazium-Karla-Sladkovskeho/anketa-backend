package cz.cvut.fit.gykas.features.auth.application

interface PasswordHashProvider {
    fun getRandomPassword(len: Int): String
    fun hashPassword(password: String): String
    fun verify(hash: String, password: String): Boolean
}