package homeworks.hw7.task2

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlin.random.Random.Default.nextInt
import kotlin.system.measureTimeMillis


typealias Matrix = Array<IntArray>

operator fun Matrix.times(other: Matrix): Matrix {
    val r1 = this.size
    val c1 = this[0].size
    val r2 = other.size
    val c2 = other[0].size
    require(c1 == r2)
    val result = Array(r1) { IntArray(c2) }
    for (i in 0 until r1)
        for (j in 0 until c2)
            for (k in 0 until c1)
                result[i][j] += this[i][k] * other[k][j]
    return result
}


suspend fun multiplyByRows(m1: Matrix, m2: Matrix): Matrix = coroutineScope {
    val r1 = m1.size
    val c1 = m1[0].size
    val r2 = m2.size
    val c2 = m2[0].size
    require(c1 == r2)
    val result = Array(r1) { IntArray(c2) }
    val jobs = arrayOfNulls<Job>(r1)

    for (i in 0 until r1)
        jobs[i] = launch {
            for (j in 0 until c2)
                for (k in 0 until c1)
                    result[i][j] += m1[i][k] * m2[k][j]
        }
    for (job in jobs)
        job!!.join()

    return@coroutineScope result
}

suspend fun multiplyByRowsCopying(m1: Matrix, m2: Matrix): Matrix = coroutineScope {
    val r1 = m1.size
    val c1 = m1[0].size
    val r2 = m2.size
    val c2 = m2[0].size
    require(c1 == r2)
    val result = Array(r1) { IntArray(c2) }
    val jobs = arrayOfNulls<Job>(r1)

    for (i in 0 until r1)
        jobs[i] = launch {
            val row = IntArray(c2)
            for (j in 0 until c2)
                for (k in 0 until c1)
                    row[j] += m1[i][k] * m2[k][j]
            result[i] = row
        }
    for (job in jobs)
        job!!.join()

    return@coroutineScope result
}


fun genMatrix(r: Int, c: Int): Matrix {
    val result = Array(r) { IntArray(c) }
    for (i in 0 until r)
        for (j in 0 until c)
            result[i][j] = nextInt()
    return result
}


fun main(): Unit = runBlocking {
    val m1 = genMatrix(1500, 1500)
    val m2 = genMatrix(1500, 1500)
    println(measureTimeMillis { m1 * m2 })
    println(measureTimeMillis { multiplyByRows(m1, m2) })
    println(measureTimeMillis { multiplyByRowsCopying(m1, m2) })
}
