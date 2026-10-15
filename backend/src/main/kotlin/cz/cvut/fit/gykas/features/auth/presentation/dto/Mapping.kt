package cz.cvut.fit.gykas.features.auth.presentation.dto

import cz.cvut.fit.gykas.core.auth.ROLE_ADMIN
import cz.cvut.fit.gykas.core.auth.ROLE_STUDENT
import cz.cvut.fit.gykas.core.auth.ROLE_TEACHER
import cz.cvut.fit.gykas.core.auth.JwtToken
import cz.cvut.fit.gykas.features.auth.domain.User

fun User.toLoggedInUser(): LoggedInUserResponse {
    val role =
        if (isAdmin) ROLE_ADMIN
        else if (isTeacher) ROLE_TEACHER
        else if (isStudent) ROLE_STUDENT
        else "guest"

    return LoggedInUserResponse(this.firstName, this.lastName, role)
}

fun JwtToken.toLoginResponse(): LoginResponse =
    LoginResponse(accessToken, tokenType, expiresIn)