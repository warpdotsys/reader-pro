/** Business rewrite from reader-pro-3.2.14.jar — phase10. */

package io.legado.app.help

import java.util.concurrent.ConcurrentHashMap

/**
 * 书源 JS `cache` 绑定：put/get 带可选 TTL 秒。
 */
class CacheManager(private val userNameSpace: String) {
    private val map = store.computeIfAbsent(userNameSpace) { ConcurrentHashMap() }

    fun put(key: String, value: String, saveTime: Int = 0) {
        val exp = if (saveTime > 0) System.currentTimeMillis() + saveTime * 1000L else 0L
        map[key] = exp to value
    }

    fun get(key: String): String? {
        val p = map[key] ?: return null
        if (p.first > 0 && System.currentTimeMillis() > p.first) {
            map.remove(key)
            return null
        }
        return p.second
    }

    fun delete(key: String) {
        map.remove(key)
    }

    companion object {
        private val store = ConcurrentHashMap<String, ConcurrentHashMap<String, Pair<Long, String>>>()
    }
}
