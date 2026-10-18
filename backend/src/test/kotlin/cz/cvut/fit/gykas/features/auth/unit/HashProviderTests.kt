package cz.cvut.fit.gykas.features.auth.unit

import cz.cvut.fit.gykas.features.auth.infrastructure.ArgonHashProvider
import de.mkammerer.argon2.Argon2Factory
import kotlin.test.Test
import kotlin.test.assertEquals

class HashProviderTests {
    val provider = ArgonHashProvider(Argon2Factory.create(), 10, 65536, 1)

    @Test
    fun returnsValidHash() {
        val hash = provider.hashPassword("abcdef")

        assert(hash.isNotEmpty())
    }

    @Test
    fun unhashes() {
        val passwd = "abdefghijkl"
        val hash = provider.hashPassword(passwd)

        assert(provider.verify(hash, passwd))
    }

    @Test
    fun getRandomPasswordStructure() {
        for (i in 3 .. 50) {
            val passwd = provider.getRandomPassword(i)

            val alfaNum = passwd.filter { it.isLetterOrDigit() }
            val special = passwd.filter { !it.isLetterOrDigit() }

            assertEquals(i - 1, alfaNum.length)
            assertEquals(1, special.length)
        }
    }
}