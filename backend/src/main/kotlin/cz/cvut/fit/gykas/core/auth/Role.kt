package cz.cvut.fit.gykas.core.auth

const val ROLE_ADMIN: String = "ROLE_ADMIN"
const val ROLE_STUDENT: String = "ROLE_STUDENT"
const val ROLE_TEACHER: String = "ROLE_TEACHER"
const val ROLE_POLL_CREATOR: String = "ROLE_POLL_CREATOR"

val ALL_ROLES = setOf(ROLE_ADMIN, ROLE_STUDENT, ROLE_TEACHER, ROLE_POLL_CREATOR)