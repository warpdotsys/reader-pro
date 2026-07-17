package me.ag2s.epublib.util.commons.io

import java.io.IOException

fun interface IOConsumer<T> {
    @Throws(IOException::class)
    fun accept(value: T)

    fun andThen(after: IOConsumer<T>): IOConsumer<T> = IOConsumer { value -> accept(value); after.accept(value) }
}
