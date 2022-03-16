package homeworks.hw3.task1

import kotlin.math.max

class MutableAVL<K : Comparable<K>, V> : MutableMap<K, V>, AVL<K, V>() {
    private var root: MutableNode<K, V>? = null

    companion object {
        const val POSITIVE_BALANCE_BOUND = 2
        const val NEGATIVE_BALANCE_BOUND = -2
    }

    private var _size = 0
    override val size: Int
        get() = _size

    private var _entries: MutableSet<MutableMap.MutableEntry<K, V>> = mutableSetOf()
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = _entries

    private var _keys: MutableSet<K> = mutableSetOf()
    override val keys: MutableSet<K>
        get() = _keys

    private var _values: MutableCollection<V> = mutableListOf() // ?
    override val values: MutableCollection<V>
        get() = _values

    override fun remove(key: K): V? {
        if (root == null) return null
        var v: MutableNode<K, V>? = root
        var parent: MutableNode<K, V>? = root
        var delNode: MutableNode<K, V>? = null
        var child: MutableNode<K, V>? = root
        while (child != null) {
            parent = v
            v = child
            child = if (key >= v.key) v.right else v.left
            if (key == v.key) delNode = v
        }
        val toReturn = delNode?.value
        if (delNode != null) {
            delNode.key = v!!.key
            child = if (v.left != null) v.left else v.right
            if (0 == root!!.key.compareTo(key)) {
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

            delEntry(delNode)
        }
        return toReturn
    }

    override fun clear() {
        while (root != null)
            remove(root!!.key)
    }

    override fun putAll(from: Map<out K, V>) {
        for (v in from.entries)
            put(v.key, v.value)
    }

    override fun put(key: K, value: V): V? {
        if (root == null) {
            root = MutableNode(key, value, null)

            _size++
            _entries.add(root!!)
            _keys.add(key)
            _values.add(value)

            return null
        }
        var v: MutableNode<K, V>? = root
        var parent: MutableNode<K, V>? = null
        var goLeft = false

        while (v != null && v.key != key) {
            parent = v
            goLeft = v.key > key
            v = if (goLeft) v.left else v.right
        }

        val newNode = MutableNode(key, value, parent)
        val toReturn = v?.value
        if (v != null)
            delEntry(v)

        _size++
        _entries.add(newNode)
        _keys.add(key)
        _values.add(value)

        if (parent != null) {
            if (goLeft)
                parent.left = newNode
            else
                parent.right = newNode
            rebalance(parent)
        }
        return toReturn
    }

    private class MutableNode<K, V>(override var key: K, override var value: V, var parent: MutableNode<K, V>?) :
        MutableMap.MutableEntry<K, V> {
        var balance: Int = 0
        var left: MutableNode<K, V>? = null
        var right: MutableNode<K, V>? = null

        override fun setValue(newValue: V): V {
            val prevValue = value
            value = newValue
            return prevValue
        }
    }

    private fun MutableNode<K, V>?.height(): Int =
        if (this == null) -1 else (1 + max(this.left.height(), this.right.height()))

    private fun MutableNode<K, V>.updateBalance() {
        this.balance = this.right.height() - this.left.height()
    }

    private fun rebalance(v: MutableNode<K, V>) {
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

    private fun rotateRight(a: MutableNode<K, V>): MutableNode<K, V> {
        require(a.left != null) { "illegal right rotation, left child cannot be null" }
        val b: MutableNode<K, V> = a.left!!
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

    private fun rotateLeft(a: MutableNode<K, V>): MutableNode<K, V> {
        require(a.right != null) { "illegal left rotation, right child cannot be null" }
        val b: MutableNode<K, V> = a.right!!
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

    private fun delEntry(v: MutableNode<K, V>) {
        _size--
        _entries.remove(v)
        _keys.remove(v.key)
        _values.remove(v.value)
    }
}
