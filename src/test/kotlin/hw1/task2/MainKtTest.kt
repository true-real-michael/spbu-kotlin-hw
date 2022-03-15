package hw1.task2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MainKtTest {
    @Test
    fun `prime nums le 50`() {
        assertEquals(listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47), eratosthenes(50))
    }

    @Test
    fun `prime nums le 3`() {
        assertEquals(listOf(2, 3), eratosthenes(3))
    }

    @Test
    fun `edge case 2`() {
        assertEquals(listOf(2), eratosthenes(2))
    }
}