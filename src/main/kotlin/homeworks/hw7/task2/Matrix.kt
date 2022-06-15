package homeworks.hw7.task2

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


open class Matrix(private val matrix: List<List<Int>>) {
//    constructor(nRows_: Int, nCols_: Int) : this(List<List<Int>>(nRows_) { List<Int>(nCols_) { 0 } })

    private val nRows: Int = matrix.size
    private val nCols: Int = matrix[0].size


    init {
        for (row in matrix)
            if (row.size != nCols)
                throw IllegalArgumentException("n of cols is not equal between rows")
    }

    operator fun get(rowIndex: Int, colIndex: Int): Int = matrix[rowIndex][colIndex]

    override operator fun equals(other: Any?): Boolean {
        if (other !is Matrix || nRows != other.nRows || nCols != other.nCols)
            return false
        for (i in 0 until nRows)
            for (j in 0 until nCols)
                if (this[i, j] != other[i, j])
                    return false
        return true
    }

    fun transposed() = Matrix(List(nCols) { col -> List(nRows) { row -> this[col, row] } })

    fun timesLinear(other: Matrix): Matrix {
        require(nCols == other.nRows) { "cannot multiply. this.cols != other.rows" }

        val result: MutableMatrix = MutableMatrix(nRows, other.nCols)
        for (i in 0 until nRows)
            for (j in 0 until other.nCols)
                for (k in 0 until nCols)
                    result[i, j] += this[i, k] * other[k, j]

        return result
    }

    fun timesParallel(other: Matrix): Matrix {
        require(nCols == other.nRows) { "cannot multiply. this.cols != other.rows" }

        val result: MutableMatrix = MutableMatrix(nRows, other.nCols)
        runBlocking {
            for (i in 0 until nRows)
                for (j in 0 until other.nCols)
                    launch {
                        for (k in 0 until nCols)
                            result[i, j] += get(i, k) * other[k, j]
                    }
        }

        return result
    }
}


class MutableMatrix(private val matrix: MutableList<MutableList<Int>>) : Matrix(matrix) {
    constructor(
        nRows_: Int,
        nCols_: Int
    ) : this(MutableList<MutableList<Int>>(nRows_) { MutableList<Int>(nCols_) { 0 } })

    operator fun set(rowIndex: Int, colIndex: Int, newValue: Int) {
        matrix[rowIndex][colIndex] = newValue
    }
}


fun matrixOf(vararg rows: List<Int>): Matrix = Matrix(rows.asList())
//fun mutableMatrixOf(vararg rows: List<Int>): Matrix = Matrix(rows.List())/
