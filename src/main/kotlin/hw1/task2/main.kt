package hw1.task2

import kotlin.math.floor
import kotlin.math.sqrt

fun eratosthenes(n: Int): List<Int> {
    val sieve = BooleanArray(n + 2) { true }
    for (i in 2..((floor(sqrt(n.toDouble())) + 1).toInt())) {
        if (sieve[i]) {
            var j = 2 * i
            while (j <= n) {
                sieve[j] = false
                j += i
            }
        }
    }
    val res = MutableList<Int>(0) { 0 }
    for (i in 2..n)
        if (sieve[i])
            res.add(i)
    return res
}

fun main() {
    val n = readln().toIntOrNull()
    if (n == null || n < 1) println("Illegal Input. Positive integers only are accepted.")
    else if (n < 2) println("No prime numbers there :)")
    else println(eratosthenes(n).joinToString())
}