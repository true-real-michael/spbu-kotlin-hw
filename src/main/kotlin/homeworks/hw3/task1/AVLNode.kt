package homeworks.hw3.task1

class AVLNode<K, V>(override var key: K, override var value: V, var parent: AVLNode<K, V>?) :
    MutableMap.MutableEntry<K, V> {
    var balance: Int = 0
    var left: AVLNode<K, V>? = null
    var right: AVLNode<K, V>? = null

    override fun setValue(newValue: V): V {
        val prevValue = value
        value = newValue
        return prevValue
    }

    fun height(): Int =
        1 + Integer.max(this.left?.height() ?: -1, this.right?.height() ?: -1)

    fun updateBalance() {
        this.balance = (this.right?.height() ?: -1) - (this.left?.height() ?: -1)
    }
}
