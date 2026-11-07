package cz.cvut.fit.gykas.features.student.application

import cz.cvut.fit.gykas.features.student.domain.ClassRepository

class ClassService(private val classRepository: ClassRepository) {
    suspend fun getAll() = classRepository.findAll()
    suspend fun getByName(name: String) = classRepository.findByName(name)
    suspend fun delete(id: Int) = classRepository.delete(id)
    suspend fun deleteByName(name: String) = classRepository.deleteByName(name)
    suspend fun create(name: String) = classRepository.create(name)
}