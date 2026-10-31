package cz.cvut.fit.gykas.features.student.application

import cz.cvut.fit.gykas.features.student.domain.ClassRepository

class ClassService(private val classRepository: ClassRepository) {
    suspend fun getAll() = classRepository.getAll()
}