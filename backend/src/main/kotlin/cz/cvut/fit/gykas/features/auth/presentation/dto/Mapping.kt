package cz.cvut.fit.gykas.features.auth.presentation.dto

import cz.cvut.fit.gykas.features.user.domain.User

fun User.toLoggedInUser(): LoggedInUserResponse {
    return LoggedInUserResponse(this.firstName, this.lastName, "role")
}