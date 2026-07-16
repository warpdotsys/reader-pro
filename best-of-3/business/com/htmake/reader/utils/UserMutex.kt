/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

package com.htmake.reader.utils

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap

object UserMutex {
    private val map = ConcurrentHashMap<String, Mutex>()
    fun getLocker(key: String): Mutex = map.getOrPut(key) { Mutex() }
}
