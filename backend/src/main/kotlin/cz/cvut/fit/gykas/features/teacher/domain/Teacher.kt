package cz.cvut.fit.gykas.features.teacher.domain

data class Teacher(val id: Int,
                   val name: String,
                   val lastname: String,
                   val username: String,
                   val active: Boolean)
