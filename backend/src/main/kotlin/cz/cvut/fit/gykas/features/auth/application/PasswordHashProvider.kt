package cz.cvut.fit.gykas.features.auth.application

interface PasswordHashProvider {
    fun getRandomPassword(len: Int): String
    fun hashPassword(password: String): String
    fun verify(hash: String, password: String): Boolean

    companion object {
        const val STUDENT_PASSWORD_LEN: Int = 7
        const val TEACHER_PASSWORD_LEN: Int = 10
    }
}