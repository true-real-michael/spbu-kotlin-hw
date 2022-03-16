package homeworks.hw3.task1

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class CombinedAVLTest {
    @Test
    fun `valid input test`() {
        val map = CombinedAVL<Int, String>()
        assertEquals(true, map.isEmpty())
        map[1] = "one"
        assertEquals(true, map.containsKey(1))
        assertEquals(true, map.containsValue("one"))
    }

    @Test
    fun `valid merge`() {
        val map0 = CombinedAVL<Int, String>()
        val map1: Map<Int, String> = mapOf(1 to "one", 2 to "two", 3 to "three")
        val map2: Map<Int, String> = mapOf(3 to "three_", 4 to "four")
        map0.putAll(map1)
        map0.putAll(map2)
        assertEquals("one", map0[1])
        assertEquals("two", map0[2])
        assertEquals("three_", map0[3])
        assertEquals("four", map0[4])
    }
}
