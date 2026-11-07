package cz.cvut.fit.gykas.core.pagination

import kotlin.test.Test
import kotlin.test.assertEquals

class PaginationTests {
    @Test
    fun returnsValidOffset() {
        assertEquals(0, PageRequest(1, 250).offset)
        assertEquals(250, PageRequest(2, 250).offset)
    }
}