package cz.cvut.fit.gykas.features.pollcreator.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.features.auth.infrastructure.UserEntity
import cz.cvut.fit.gykas.features.pollcreator.domain.PollCreator
import cz.cvut.fit.gykas.features.pollcreator.domain.PollCreatorRepository

class PollCreatorRepositoryImpl : PollCreatorRepository {
    override suspend fun create(id: Int): PollCreator = dbQuery {
        PollCreatorEntity.new {
            this.user = UserEntity[id]
        }.toDomain()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        PollCreatorEntity.findById(id)?.also { it.delete() } != null
    }
}