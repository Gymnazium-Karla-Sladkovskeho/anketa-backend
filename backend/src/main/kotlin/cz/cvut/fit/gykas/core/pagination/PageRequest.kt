package cz.cvut.fit.gykas.core.pagination

data class PageRequest(
    val page: Long = 1,
    val size: Int = 25,
) {
    val offset = (page - 1) * size
}