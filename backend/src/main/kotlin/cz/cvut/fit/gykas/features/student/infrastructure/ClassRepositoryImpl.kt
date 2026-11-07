package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.student.domain.Class
import cz.cvut.fit.gykas.features.student.domain.ClassRepository
import org.jetbrains.exposed.v1.core.eq

class ClassRepositoryImpl : ClassRepository {
    override suspend fun findAll(): Array<Class> = dbQuery {
        ClassEntity.all().map { it.toDomain() }.toTypedArray()
    }

    override suspend fun findByName(name: String): Class? = dbQuery {
        ClassEntity.find { Classes.name eq name }.firstOrNull()?.toDomain()
    }

    override suspend fun create(name: String): Class = dbQuery {
        ClassEntity.new { this.name = name }.toDomain()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        ClassEntity.findById(id)?.also { it.delete() } != null
    }

    override suspend fun deleteByName(name: String): Boolean = dbQuery {
        ClassEntity.find { Classes.name eq name }.firstOrNull()?.also { it.delete() } != null
    }
}