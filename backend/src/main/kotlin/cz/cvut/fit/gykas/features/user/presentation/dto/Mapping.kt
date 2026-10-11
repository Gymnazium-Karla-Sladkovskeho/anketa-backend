package cz.cvut.fit.gykas.features.user.presentation.dto

import cz.cvut.fit.gykas.features.user.domain.User

fun User.toResponse() = UserResponse(id, name, email)
