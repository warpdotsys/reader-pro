package com.htmake.reader.utils

import kotlinx.coroutines.sync.Mutex
import java.util.concurrent.ConcurrentHashMap

object UserMutex {
    private val locks = ConcurrentHashMap<String, Mutex>()
    fun getLocker(key: String): Mutex = locks.computeIfAbsent(key) { Mutex() }
}
