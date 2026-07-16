package io.legado.app.model

import io.legado.app.data.entities.BookSource
import io.legado.app.model.webBook.WebBook

class Debugger(private val onMsg: (String) -> Unit) : DebugLog {
    override fun log(source: String?, msg: String?) {
        onMsg(msg ?: "")
    }

    suspend fun startDebug(webBook: WebBook, key: String) {
        onMsg("◇开始调试: $key")
        when {
            key.startsWith("http://") || key.startsWith("https://") -> {
                onMsg("◇详情"); val book = webBook.getBookInfo(key); onMsg(book.name)
                onMsg("◇目录"); val toc = webBook.getChapterList(book); onMsg("共${toc.size}章")
                if (toc.isNotEmpty()) {
                    onMsg("◇正文"); val c = webBook.getBookContent(book, toc[0], toc.getOrNull(1)?.url)
                    onMsg(c.take(200))
                }
            }
            key.startsWith("::") -> {
                onMsg("◇发现"); val list = webBook.exploreBook(key.removePrefix("::"), 1); onMsg("结果${list.size}")
            }
            key.startsWith("++") -> onMsg("◇目录调试占位 ${key.removePrefix("++")}")
            key.startsWith("--") -> onMsg("◇正文调试占位 ${key.removePrefix("--")}")
            else -> {
                onMsg("◇搜索"); val list = webBook.searchBook(key, 1); onMsg("结果${list.size}")
                list.take(3).forEach { onMsg("- ${it.name} / ${it.author}") }
            }
        }
        onMsg("◇结束")
    }
}
