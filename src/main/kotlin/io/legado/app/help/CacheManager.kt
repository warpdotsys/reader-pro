package io.legado.app.help

import java.util.concurrent.ConcurrentHashMap

class CacheManager(private val userNameSpace: String) {
    private val map = store.computeIfAbsent(userNameSpace) { ConcurrentHashMap() }

    fun put(key: String, value: String, saveTime: Int = 0) {
        val exp = if (saveTime > 0) System.currentTimeMillis() + saveTime * 1000L else 0L
        map[key] = exp to value
    }

    fun get(key: String): String? {
        val p = map[key] ?: return null
        if (p.first > 0 && System.currentTimeMillis() > p.first) {
            map.remove(key); return null
        }
        return p.second
    }

    fun delete(key: String) { map.remove(key) }

    companion object {
        private val store = ConcurrentHashMap<String, ConcurrentHashMap<String, Pair<Long, String>>>()
    }
}
