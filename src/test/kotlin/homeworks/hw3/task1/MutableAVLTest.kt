package homeworks.hw3.task1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MutableAVLTest {
    @Test
    fun `valid input test`() {
        val map = MutableAVL<Int, String>()
        assertEquals(true, map.isEmpty())
        map[1] = "one"
        assertEquals(true, map.containsKey(1))
        assertEquals(true, map.containsValue("one"))
        assertEquals("one", map[1])
    }

    @Test
    fun `valid merge`() {
        val map1: MutableAVL<Int, String> = mapOf(1 to "one", 2 to "two", 3 to "three") as MutableAVL<Int, String>
        val map2: Map<Int, String> = mapOf(3 to "three_", 4 to "four")
        map1.putAll(map2)
        assertEquals(
            mapOf(1 to "one", 2 to "two", 3 to "three_", 4 to "four") as MutableAVL<Int, String>,
            map1
        )
    }
}
