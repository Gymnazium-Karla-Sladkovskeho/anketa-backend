package cz.cvut.fit.gykas.features.pollcreator.domain

interface PollCreatorRepository {
    suspend fun create(id: Int): PollCreator
    suspend fun delete(id: Int): Boolean
}