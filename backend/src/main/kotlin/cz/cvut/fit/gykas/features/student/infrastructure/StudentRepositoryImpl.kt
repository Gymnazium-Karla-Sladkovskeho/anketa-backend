package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.auth.infrastructure.toDomain
import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentRepository
import org.jetbrains.exposed.v1.core.inList

class StudentRepositoryImpl : StudentRepository {
    override suspend fun findAllByClasses(classIds: Array<Int>): Array<Student> = dbQuery {
        with(classIds.toList()) {
            StudentEntity.find { Students.classesId inList this }.map { it.toDomain() }.toTypedArray()
        }
    }

    override suspend fun findById(id: Int): Student? = dbQuery {
        StudentEntity.findById(id)?.toDomain()
    }

    override suspend fun create(
        userId: Int, classId: Int
    ): Student = dbQuery {
        StudentEntity.new {
            this.schoolClass = ClassEntity.findById(classId).let { require(it != null); it }
            this.user = UserEntity.findById(userId).let { require(it != null); it }
        }.toDomain()
    }

    override suspend fun update(
        userId: Int, classId: Int
    ): Student? = dbQuery {
        StudentEntity.findById(userId)?.apply {
            this.schoolClass = ClassEntity.findById(classId).let { require(it != null); it }
        }?.toDomain()
    }

    override suspend fun delete(userId: Int): Boolean = dbQuery {
        StudentEntity.findById(userId)?.also { it.delete() } != null
    }
}