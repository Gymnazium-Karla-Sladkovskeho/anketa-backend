package cz.cvut.fit.gykas.features.student.presentation.dto

import cz.cvut.fit.gykas.features.student.domain.Class

fun Class.toClassResponse() = ClassResponse(this.name)