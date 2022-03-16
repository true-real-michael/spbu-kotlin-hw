package homeworks.hw3.task1

import kotlin.math.max

class AVL<K : Comparable<K>, V> : Map<K, V> {
    private var root: Node<K, V>? = null

    companion object {
        const val POSITIVE_BALANCE_BOUND = 2
        const val NEGATIVE_BALANCE_BOUND = -2
    }

    private var _size = 0
    override val size: Int
        get() = _size

    private var _entries: MutableSet<Map.Entry<K, V>> = mutableSetOf()
    override val entries: Set<Map.Entry<K, V>>
        get() = _entries

    private var _keys: MutableSet<K> = mutableSetOf()
    override val keys: Set<K>
        get() = _keys

    private var _values: MutableCollection<V> = mutableListOf() // ?
    override val values: Collection<V>
        get() = _values

    override fun isEmpty(): Boolean = root == null

    override fun containsKey(key: K): Boolean = keys.contains(key)

    override fun get(key: K): V? = get(key, root)

    override fun containsValue(value: V): Boolean = values.contains(value)

    private class Node<K, V>(override var key: K, override var value: V, var parent: Node<K, V>?) : Map.Entry<K, V> {
        var balance: Int = 0
        var left: Node<K, V>? = null
        var right: Node<K, V>? = null
    }

    private fun Node<K, V>.updateBalance() {
        this.balance = this.right.height() - this.left.height()
    }

    private fun Node<K, V>?.height(): Int = if (this == null) -1 else (1 + max(this.left.height(), this.right.height()))

    private fun get(key: K, from: Node<K, V>?): V? {
        if (from == null) return null
        return get(key, if (from.key > key) from.left else from.right)
    }

    fun put(key: K, value: V) {
        if (root == null) {
            root = Node(key, value, null)
            _size++
            _entries.add(root!!)
            _keys.add(key)
            _values.add(value)
            return
        }
        var v: Node<K, V>? = root
        var parent: Node<K, V>
        while (v != null) {
            require(v.key != key) { "A node with this key already exists" }
            parent = v
            val goLeft = v.key > key
            v = if (goLeft) v.left else v.right
            if (v == null) {
                val newNode = Node(key, value, parent)
                _size++
                _entries.add(newNode)
                _keys.add(key)
                _values.add(value)

                if (goLeft)
                    parent.left = newNode
                else
                    parent.right = newNode
                rebalance(parent)
            }
        }
    }

    private fun rebalance(v: Node<K, V>) {
        v.updateBalance()
        var vv = v // a moment I do not understand
        if (vv.balance == NEGATIVE_BALANCE_BOUND) {
            if (vv.left!!.left.height() < vv.left!!.right.height())
                vv.left = rotateLeft(vv.left!!)
            vv = rotateRight(vv)
        } else if (vv.balance == POSITIVE_BALANCE_BOUND) {
            if (vv.right!!.right.height() < vv.right!!.left.height())
                vv.right = rotateRight(vv.right!!)
            vv = rotateLeft(vv)
        }
        if (vv.parent != null) rebalance(vv.parent!!)
        else root = vv
    }

    private fun rotateRight(a: Node<K, V>): Node<K, V> {
        require(a.left != null) { "illegal right rotation, left child cannot be null" }
        val b: Node<K, V> = a.left!!
        b.parent = a.parent
        a.left = b.right
        a.left?.parent = a
        b.right = a
        a.parent = b
        if (b.parent?.right == a)
            b.parent?.right = b
        else
            b.parent?.left = b
        a.updateBalance()
        b.updateBalance()
        return b
    }

    private fun rotateLeft(a: Node<K, V>): Node<K, V> {
        require(a.right != null) { "illegal left rotation, right child cannot be null" }
        val b: Node<K, V> = a.right!!
        b.parent = a.parent
        a.right = b.left
        a.right?.parent = a
        b.left = a
        a.parent = b
        if (b.parent?.right == a)
            b.parent?.right = b
        else
            b.parent?.left = b
        a.updateBalance()
        b.updateBalance()
        return b
    }

    fun delete(delKey: K) {
        if (root == null) return
        var v: Node<K, V>? = root
        var parent: Node<K, V>? = root
        var delNode: Node<K, V>? = null
        var child: Node<K, V>? = root
        while (child != null) {
            parent = v
            v = child
            child = if (delKey >= v.key) v.right else v.left
            if (delKey == v.key) delNode = v
        }
        if (delNode != null) {
            _size--
            _entries.remove(delNode)
            _keys.remove(delNode.key)
            _values.remove(delNode.value)

            delNode.key = v!!.key
            child = if (v.left != null) v.left else v.right
            if (0 == root!!.key.compareTo(delKey)) {
                root = child

                if (null != root) {
                    root!!.parent = null
                }
            } else {
                if (parent!!.left == v)
                    parent.left = child
                else
                    parent.right = child

                if (null != child) {
                    child.parent = parent
                }

                rebalance(parent)
            }
        }
    }
}
