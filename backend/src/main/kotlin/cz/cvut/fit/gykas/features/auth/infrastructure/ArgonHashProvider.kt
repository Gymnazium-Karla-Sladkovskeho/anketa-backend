package cz.cvut.fit.gykas.features.auth.infrastructure

import cz.cvut.fit.gykas.features.auth.application.PasswordHashProvider
import de.mkammerer.argon2.Argon2

class ArgonHashProvider(val argon2: Argon2,
                        val iterations: Int,
                        val memory: Int,
                        val parallelism: Int) : PasswordHashProvider
{
    override fun getRandomPassword(len: Int): String {
        val nums = '0' .. '9'
        val letters = 'a' .. 'z'
        val bigLetters = 'A' .. 'Z'
        val alphaNum = arrayOf(nums, letters, bigLetters)
        val special = arrayOf('@','&', '-', '_')

        val arr = Array(len) { 'a' }
        arr[0] = special.random()
        for (i in 1 until len) {
            arr[i] = alphaNum.random().random()
        }

        arr.shuffle()
        return arr.joinToString("")
    }

    override fun hashPassword(password: String): String =
        argon2.hash(iterations,
            memory,
            parallelism,
            password.toCharArray()
        )

    override fun verify(hash: String, password: String): Boolean =
        argon2.verify(hash, password.toCharArray())
}