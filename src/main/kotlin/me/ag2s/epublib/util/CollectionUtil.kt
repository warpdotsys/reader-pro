package me.ag2s.epublib.util

import java.util.Enumeration
import java.util.Iterator

object CollectionUtil {
    @JvmStatic fun createEnumerationFromIterator(iterator: Iterator<*>): Enumeration<Any?> = object : Enumeration<Any?> { override fun hasMoreElements() = iterator.hasNext(); override fun nextElement(): Any? = iterator.next() }
    @JvmStatic fun first(list: List<*>?): Any? = if (list.isNullOrEmpty()) null else list[0]
    @JvmStatic fun isEmpty(collection: Collection<*>?): Boolean = collection.isNullOrEmpty()
}
