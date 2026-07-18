package com.htmake.reader.init

import io.legado.app.adapters.ReaderAdapterHelper

object appCtx {
    val cacheDir: String by lazy {
        ReaderAdapterHelper.getAdapter().cacheDir
    }
}
