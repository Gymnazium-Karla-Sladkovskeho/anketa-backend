package cz.cvut.fit.gykas.core.pagination.infrastructure

import cz.cvut.fit.gykas.core.pagination.Page
import cz.cvut.fit.gykas.core.pagination.PageRequest
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.jdbc.Query

fun <T>Query.paginate(page: PageRequest, mapToDomain: ResultRow.() -> T): Page<T> {
    val count = count()
    val total = count / page.size + if (count % page.size != 0L) 1L else 0L

    val content = limit(page.size)
        .offset(page.offset)
        .toList()
        .map(mapToDomain)

    return Page(
        count, total, page.size, content
    )
}