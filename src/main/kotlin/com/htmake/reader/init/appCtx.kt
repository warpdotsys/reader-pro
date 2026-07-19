package com.htmake.reader.init

import com.htmake.reader.utils.getWorkDir

object appCtx {
    val cacheDir: String by lazy {
        getWorkDir("storage", "cache")
    }
}
