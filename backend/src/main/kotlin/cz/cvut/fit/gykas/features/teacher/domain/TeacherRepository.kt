package cz.cvut.fit.gykas.features.teacher.domain

import cz.cvut.fit.gykas.core.pagination.Page
import cz.cvut.fit.gykas.core.pagination.PageRequest

interface TeacherRepository {
    suspend fun findById(id: Int): Teacher?
    suspend fun findPaginated(page: PageRequest): Page<Teacher>
    suspend fun create(userId: Int): Teacher
    suspend fun delete(id: Int): Boolean
    suspend fun update(userId: Int, active: Boolean): Teacher?
}