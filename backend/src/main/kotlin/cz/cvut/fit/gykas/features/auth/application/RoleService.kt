package cz.cvut.fit.gykas.features.auth.application

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_POLL_CREATOR
import cz.cvut.fit.gykas.core.auth.ROLE_STUDENT
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.features.auth.domain.UserRepository

class RoleService(private val userRepository: UserRepository) {
    suspend fun getRoles(id: Int): Set<String> {
        val user = userRepository.findById(id) ?: return emptySet()

        return with(mutableListOf<String>()) {
            if (user.isAdmin)
                add(ROLE_ADMIN)
            if (user.isPollCreator)
                add(ROLE_POLL_CREATOR)
            if (user.isTeacher)
                add(ROLE_TEACHER)
            if (user.isStudent)
                add(ROLE_STUDENT)

            this
        }.toSet()
    }
}