package cz.cvut.fit.gykas.features.student.infrastructure

import cz.cvut.fit.gykas.features.student.domain.Student
import cz.cvut.fit.gykas.features.student.domain.StudentRepository

class StudentRepositoryImpl : StudentRepository {
    override suspend fun findAllByClasses(classIds: Array<Int>): Array<Student> {
        TODO("Not yet implemented")
    }

    override suspend fun findById(id: Int): Student? {
        TODO("Not yet implemented")
    }

    override suspend fun create(
        userId: Int,
        classId: Int
    ): Student {
        TODO("Not yet implemented")
    }

    override suspend fun update(
        userId: Int,
        classId: Int
    ): Student? {
        TODO("Not yet implemented")
    }

    override suspend fun delete(userId: Int): Boolean {
        TODO("Not yet implemented")
    }
}