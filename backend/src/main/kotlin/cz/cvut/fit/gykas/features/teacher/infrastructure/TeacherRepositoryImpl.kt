package cz.cvut.fit.gykas.features.teacher.infrastructure

import cz.cvut.fit.gykas.core.database.dbQuery
import cz.cvut.fit.gykas.core.pagination.Page
import cz.cvut.fit.gykas.core.pagination.PageRequest
import cz.cvut.fit.gykas.core.pagination.infrastructure.paginate
import cz.cvut.fit.gykas.features.pollcreator.infrastructure.PollCreatorEntity
import cz.cvut.fit.gykas.features.student.infrastructure.StudentEntity
import cz.cvut.fit.gykas.features.teacher.domain.Teacher
import cz.cvut.fit.gykas.features.teacher.domain.TeacherRepository
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.selectAll

class TeacherRepositoryImpl : TeacherRepository {
    override suspend fun findById(id: Int): Teacher? = dbQuery {
        TeacherEntity.findById(id)?.toDomain()
    }

    override suspend fun findPaginated(page: PageRequest): Page<Teacher> = dbQuery {
        Teachers.selectAll().paginate(page) {
            TeacherEntity.wrapRow(this).toDomain()
        }
    }

    override suspend fun create(userId: Int): Teacher = dbQuery {
        TeacherEntity.new {
            this.pollCreator = PollCreatorEntity[userId]
            isActive = true
        }.toDomain()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        Teachers.deleteWhere {
            Teachers.id eq id
        } > 0
        //StudentEntity.findById(id)?.also { it.delete() } != null
    }

    override suspend fun update(
        userId: Int,
        active: Boolean
    ): Teacher? = dbQuery {
        val entity = TeacherEntity.findById(userId) ?: return@dbQuery null

        entity.isActive = active

        Teacher(
            entity.id.value,
            entity.pollCreator.user.firstName,
            entity.pollCreator.user.lastName,
            entity.pollCreator.user.uuid,
            entity.isActive
        )

        /*TeacherEntity.findById(userId)?.apply {
            this.isActive = active
        }?.toDomain()*/
    }
}