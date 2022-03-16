import kotlin.math.floor
import kotlin.math.sqrt

fun eratosthenes(n: Int): List<Int> {
    require(n >= 2) { "n < 2 are not accepted" }
    var sieve = (2..n).toMutableList()

    for (d in 2..floor(sqrt(n.toDouble())).toInt()) {
        sieve = sieve.filter { it == d || it % d != 0 }.toMutableList()
    }

    return sieve
}

fun main() {
    val n = readln().toIntOrNull()
    if (n == null || n < 1) println("Illegal Input. Positive integers only are accepted.")
    else if (n < 2) println("No prime numbers in the interval 0..$n")
    else println(eratosthenes(n).joinToString())
}
