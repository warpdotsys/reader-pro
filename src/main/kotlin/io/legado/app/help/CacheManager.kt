package io.legado.app.help

import io.legado.app.adapters.ReaderAdapterHelper
import io.legado.app.model.analyzeRule.QueryTTF
import io.legado.app.utils.ACache
import java.io.File

class CacheManager(val userNameSpace: String) {

    private val queryTTFMap = hashMapOf<String, Pair<Long, QueryTTF>>()
    val cacheInstance = ACache.get(
        File(
            ReaderAdapterHelper.getAdapter().getWorkDir(
                "storage",
                "cache",
                "runtimeCache",
                userNameSpace
            )
        ),
        50_000_000,
        1_000_000
    )

    @JvmOverloads
    fun put(key: String, value: Any, saveTime: Int = 0) {
        if (key.isEmpty()) return

        val deadline = if (saveTime == 0) 0L else System.currentTimeMillis() + saveTime * 1000
        when (value) {
            is QueryTTF -> queryTTFMap[key] = Pair(deadline, value)
            is ByteArray -> cacheInstance.put(key, value, saveTime)
            else -> cacheInstance.put(key, value.toString(), saveTime)
        }
    }

    fun get(key: String): String? {
        if (key.isEmpty()) return null
        return cacheInstance.getAsString(key)
    }

    fun getInt(key: String): Int? = get(key)?.toIntOrNull()

    fun getLong(key: String): Long? = get(key)?.toLongOrNull()

    fun getDouble(key: String): Double? = get(key)?.toDoubleOrNull()

    fun getFloat(key: String): Float? = get(key)?.toFloatOrNull()

    fun getByteArray(key: String): ByteArray? {
        if (key.isEmpty()) return null
        return cacheInstance.getAsBinary(key)
    }

    fun getQueryTTF(key: String): QueryTTF? {
        val cache = queryTTFMap[key] ?: return null
        return if (cache.first == 0L || cache.first > System.currentTimeMillis()) {
            cache.second
        } else {
            null
        }
    }

    fun putFile(key: String, value: String, saveTime: Int = 0) {
        if (key.isEmpty()) return
        cacheInstance.put(key, value, saveTime)
    }

    fun getFile(key: String): String? {
        if (key.isEmpty()) return null
        return cacheInstance.getAsString(key)
    }

    fun delete(key: String) {
        if (key.isEmpty()) return
        cacheInstance.remove(key)
    }
}
