package cz.cvut.fit.gykas.features.student.domain

interface StudentRepository {
    suspend fun findAllByClasses(classIds: Array<Int>): Array<Student>
    suspend fun findById(id: Int): Student?
    suspend fun create(userId: Int, classId: Int): Student
    suspend fun update(userId: Int, classId: Int): Student?
    suspend fun delete(userId: Int): Boolean
}