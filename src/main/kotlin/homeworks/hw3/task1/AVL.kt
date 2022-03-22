package homeworks.hw3.task1

@Suppress("TooManyFunctions")
class AVL<K : Comparable<K>, V> : MutableMap<K, V> {
    private var root: AVLNode<K, V>? = null

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

    private var _values: MutableCollection<V> = mutableListOf()
    override val values: MutableCollection<V>
        get() = _values

    override fun isEmpty(): Boolean = root == null

    override fun containsKey(key: K): Boolean = keys.contains(key)

    override fun containsValue(value: V): Boolean = values.contains(value)

    override fun remove(key: K): V? {
        if (root == null) return null
        var v: AVLNode<K, V>? = root
        var parent: AVLNode<K, V>? = root
        var delNode: AVLNode<K, V>? = null
        var child: AVLNode<K, V>? = root
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

    private fun addNode(newAVLNode: AVLNode<K, V>) {
        _size++
        _entries.add(newAVLNode)
        _keys.add(newAVLNode.key)
        _values.add(newAVLNode.value)
    }

    override fun put(key: K, value: V): V? {
        if (root == null) {
            root = AVLNode(key, value, null)
            addNode(root!!)
            return null
        }
        var v: AVLNode<K, V>? = root
        var parent: AVLNode<K, V>? = null
        var goLeft = false

        while (v != null && v.key != key) {
            parent = v
            goLeft = v.key > key
            v = if (goLeft) v.left else v.right
        }

        val newNode = AVLNode(key, value, parent)
        val toReturn = v?.value
        if (v != null)
            delEntry(v)

        addNode(newNode)

        if (parent != null) {
            if (goLeft)
                parent.left = newNode
            else
                parent.right = newNode
            rebalance(parent)
        }
        return toReturn
    }

    override fun get(key: K): V? = get(key, root)

    private fun get(key: K, v: AVLNode<K, V>?): V? {
        if (v == null || v.key == key) return v?.value
        return get(key, if (v.key > key) v.left else v.right)
    }

    private fun rebalance(v: AVLNode<K, V>) {
        v.updateBalance()
        var vv = v
        if (vv.balance == NEGATIVE_BALANCE_BOUND) {
            if ((vv.left!!.left?.height() ?: -1) < (vv.left!!.right?.height() ?: -1))
                vv.left = rotateLeft(vv.left!!)
            vv = rotateRight(vv)
        } else if (vv.balance == POSITIVE_BALANCE_BOUND) {
            if ((vv.right!!.right?.height() ?: -1) < (vv.right!!.left?.height() ?: -1))
                vv.right = rotateRight(vv.right!!)
            vv = rotateLeft(vv)
        }
        if (vv.parent != null) rebalance(vv.parent!!)
        else root = vv
    }

    private fun rotateRight(a: AVLNode<K, V>): AVLNode<K, V> {
        require(a.left != null) { "illegal right rotation, left child cannot be null" }
        val b: AVLNode<K, V> = a.left!!
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

    private fun rotateLeft(a: AVLNode<K, V>): AVLNode<K, V> {
        require(a.right != null) { "illegal left rotation, right child cannot be null" }
        val b: AVLNode<K, V> = a.right!!
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

    private fun delEntry(v: AVLNode<K, V>) {
        _size--
        _entries.remove(v)
        _keys.remove(v.key)
        _values.remove(v.value)
    }
}

fun <K : Comparable<K>, V> avlOf(vararg data: Pair<K, V>): MutableMap<K, V> =
    AVL<K, V>().apply { this.putAll(pairs = data) }
