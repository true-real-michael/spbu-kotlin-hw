package homeworks.hw3.task1

open class AVL<K : Comparable<K>, V> : Map<K, V> {
    private var root: Node<K, V>? = null

    private var _size = 0
    override val size: Int
        get() = _size

    private var _entries: Set<Map.Entry<K, V>> = setOf()
    override val entries: Set<Map.Entry<K, V>>
        get() = _entries

    private var _keys: Set<K> = setOf()
    override val keys: Set<K>
        get() = _keys

    private var _values: Collection<V> = listOf()
    override val values: Collection<V>
        get() = _values

    override fun isEmpty(): Boolean = root == null

    override fun containsKey(key: K): Boolean = keys.contains(key)

    override fun get(key: K): V? = get(key, root)

    override fun containsValue(value: V): Boolean = values.contains(value)

    class Node<K, V>(
        override var key: K,
        override var value: V,
    ) : Map.Entry<K, V> {
        var left: Node<K, V>? = null
        var right: Node<K, V>? = null
    }

    private fun get(key: K, from: Node<K, V>?): V? {
        if (from == null) return null
        return get(key, if (from.key > key) from.left else from.right)
    }
}
