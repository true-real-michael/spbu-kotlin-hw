package homeworks.hw7.task2

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MatrixTest {
    @Test
    fun `matrix generation test`() {
        val mtx = matrixOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
        )
        assertEquals(1, mtx[0, 0])
        assertEquals(2, mtx[0, 1])
    }
}