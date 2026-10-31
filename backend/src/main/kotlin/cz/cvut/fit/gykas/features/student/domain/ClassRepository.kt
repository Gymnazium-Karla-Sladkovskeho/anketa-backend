package cz.cvut.fit.gykas.features.student.domain

interface ClassRepository {
    suspend fun getAll(): Array<Class>
}