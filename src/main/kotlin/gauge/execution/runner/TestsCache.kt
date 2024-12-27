package gauge.execution.runner

class TestsCache {
    private val idCache: MutableMap<String, Int> = HashMap()
    var currentId: Int = 0
        private set

    fun getId(key: String): Int? {
        return idCache[key]
    }

    fun setId(key: String) {
        idCache[key] = ++currentId
    }
}