package hw1.task3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PerformedCommandStorageTest {

    @Test
    fun `pushback test`() {
        val storage = PerformedCommandStorage()
        storage.pushBack(0)
        storage.pushBack(2)
        assertEquals(listOf(0, 2), storage.toList())
    }

    @Test
    fun `pushfront test`() {
        val storage = PerformedCommandStorage()
        storage.pushFront(1)
        storage.pushFront(9)

        assertEquals(listOf(9, 1), storage.toList())
    }

    @Test
    fun `undo nothing`() {
        val storage = PerformedCommandStorage()
        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> { storage.undo() }
        assertEquals("no commands on stack now", exception.message)
    }

    @Test
    fun `valid move`() {
        val storage = PerformedCommandStorage()
        storage.pushBack(0)
        storage.pushBack(1)
        storage.pushBack(2)
        storage.move(0, 1)
        assertEquals(listOf(1, 0, 2), storage.toList())
    }

    @Test
    fun `invalid move`() {
        val storage = PerformedCommandStorage()
        storage.pushFront(0)
        storage.pushFront(1)
        storage.pushFront(2)
        val exception = org.junit.jupiter.api.assertThrows<IllegalArgumentException> { storage.move(-1, 1) }
        assertEquals("at least one of the indexes is out of bounds", exception.message)
    }
}