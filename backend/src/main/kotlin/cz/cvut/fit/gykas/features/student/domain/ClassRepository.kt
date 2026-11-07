package cz.cvut.fit.gykas.features.student.domain

interface ClassRepository {
    suspend fun findAll(): Array<Class>
    suspend fun findByName(name: String): Class?
    suspend fun create(name: String): Class
    suspend fun delete(id: Int): Boolean
    suspend fun deleteByName(name: String): Boolean
}