package cz.cvut.fit.gykas.core.pagination

import kotlinx.serialization.Serializable

@Serializable
data class Page<T>(
    val page: Long,
    val totalPages: Long,
    val pageSize: Int,
    val payload: List<T>
)