package gauge.execution.runner

class TestsCache {
    private val idCache: MutableMap<String, Int> = mutableMapOf()
    private var id: Int = 0

    fun getId(key: String): Int? {
        return idCache[key]
    }

    fun getCurrentId(): Int {
        return id
    }

    fun setId(key: String, id: Int) {
        idCache[key] = id
    }

    fun setId(key: String) {
        idCache[key] = ++id
    }
}
