import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class MainKtTest {
    @ParameterizedTest(name = "case {index}: {2} {1}")
    @MethodSource("vaildInput")
    fun `valid input test`(n: Int, correct: List<Int>) {
        assertEquals(correct, eratosthenes(n))
    }

    @ParameterizedTest(name = "case {index}: {1}")
    @MethodSource("invalidInput")
    fun `invalid input test`(n: Int) {
        val exception = assertThrows<IllegalArgumentException> { eratosthenes(n) }
        assertEquals("n < 2 are not accepted", exception.message)
    }

    companion object {
        @JvmStatic
        fun vaildInput() = listOf(
            Arguments.of(50, listOf(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47)),
            Arguments.of(3, listOf(2, 3)),
            Arguments.of(2, listOf(2))
        )

        @JvmStatic
        fun invalidInput() = listOf(
            Arguments.of(-1),
            Arguments.of(0),
            Arguments.of(1)
        )
    }
}
