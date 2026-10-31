package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.ClassRepository

class ClassRepositoryImpl : ClassRepository {
    override suspend fun getAll(): Array<Class> =
        ClassEntity.all().map { it.toDomain() }.toTypedArray()
}