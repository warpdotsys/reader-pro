package com.htmake.reader.lib.tts.exceptions

class TtsException private constructor(message: String) : RuntimeException(message) {
    companion object {
        @JvmStatic
        fun of(message: String): TtsException = TtsException(message)
    }
}
