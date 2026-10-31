package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.ClassRepository

class ClassRepositoryImpl : ClassRepository {
    override suspend fun getAll(): Array<Class> =
        dbQuery {
            ClassEntity.all().map { it.toDomain() }.toTypedArray()
        }
}