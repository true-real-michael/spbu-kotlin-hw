package homeworks.hw3.task1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class AVLTest {
    @Test
    fun `valid put get test`() {
        val avl: AVL<Int, String> = avlOf(1 to "one", 2 to "two") as AVL<Int, String>
        assertEquals("one", avl[1])
        assertEquals("two", avl.put(2, "two2"))
        assertEquals("two2", avl[2])
    }

    @Test
    fun `invalid get test`() {
        val avl = avlOf(1 to "1", 2 to "2") as AVL<Int, String>
        assertEquals(null, avl[3])
    }

    @Test
    fun `test putAll correctness`() {
        val avl1 = avlOf(1 to "1", 2 to "2") as AVL<Int, String>
        val avl2 = AVL<Int, String>()
        avl2[1] = "1"
        avl2[2] = "2"
        for (e in avl1.entries)
            assertEquals(e.value, avl2[e.key])
        for (e in avl2.entries)
            assertEquals(e.value, avl1[e.key])
    }

    @Test
    fun `remove test`() {
        val avl = avlOf(1 to "1", 2 to "2") as AVL<Int, String>
        assertEquals("2", avl.remove(2))
        assertEquals(setOf(1), avl.keys)
        assertEquals(listOf("1"), avl.values)
        assertEquals("1", avl[1])
        assertEquals(null, avl[2])
    }

    @Test
    fun `clear test`() {
        val avl = avlOf(1 to "1", 2 to "2") as AVL<Int, String>
        assertEquals(false, avl.isEmpty())
        avl.clear()
        assertEquals(true, avl.isEmpty())
        assertEquals(emptySet<Int>(), avl.keys)
        assertEquals(emptyList<Int>(), avl.values)
    }

    @Test
    fun `contains test`() {
        val avl = avlOf(1 to "1", 2 to "2") as AVL<Int, String>
        assertEquals(true, avl.containsKey(1))
        assertEquals(false, avl.containsKey(3))
        assertEquals(true, avl.containsValue("1"))
        assertEquals(false, avl.containsValue("3"))
    }
}
