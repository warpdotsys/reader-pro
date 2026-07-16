# -*- coding: utf-8 -*-
"""Phase 4: EpubFile, AnalyzeByJSoup operators, multi-search concurrency, TxtTocRule."""
from pathlib import Path
import os
import json
import shutil

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
RES = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\resources")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase4. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)

# Copy default txt toc rules into business resources
src_rules = RES / "defaultData" / "txtTocRule.json"
dst_rules = BIZ / "resources" / "defaultData" / "txtTocRule.json"
if src_rules.exists():
    dst_rules.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src_rules, dst_rules)
    print("copied txtTocRule.json")

# ---------------------------------------------------------------------------
# EpubFile
# ---------------------------------------------------------------------------
w("io/legado/app/model/localBook/EpubFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.SpineReference
import me.ag2s.epublib.domain.TOCReference
import me.ag2s.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.File
import java.nio.charset.Charset
import java.util.zip.ZipFile

/**
 * EPUB via epublib (same stack as jar).
 * Chapter list prefers NCX/TOC; falls back to spine order.
 */
class EpubFile(private val book: Book) {
    private var epubBook: EpubBook? = null
    private val charset: Charset = Charset.forName("UTF-8")

    private fun readEpub(): EpubBook? {
        if (epubBook != null) return epubBook
        return try {
            val zf = ZipFile(book.localFile())
            EpubReader().readEpubLazy(zf, "utf-8").also { epubBook = it }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getChapterList(): ArrayList<BookChapter> {
        val epub = readEpub() ?: return arrayListOf()
        // Prefer TOC unique resources
        val toc = epub.tableOfContents
        val fromToc = ArrayList<BookChapter>()
        if (toc != null) {
            val resources = toc.allUniqueResources
            resources?.forEachIndexed { index, res ->
                fromToc += toChapter(index, res)
            }
        }
        if (fromToc.isNotEmpty()) {
            applyBookMeta(fromToc)
            return fromToc
        }
        return getChapterListBySpine()
    }

    fun getChapterListBySpine(): ArrayList<BookChapter> {
        val epub = readEpub() ?: return arrayListOf()
        val list = ArrayList<BookChapter>()
        val refs: List<SpineReference> = epub.spine?.spineReferences ?: return list
        refs.forEachIndexed { index, ref ->
            val res = ref.resource ?: return@forEachIndexed
            list += toChapter(index, res).also {
                if (index == 0 && it.title.isEmpty()) it.title = "封面"
            }
        }
        applyBookMeta(list)
        return list
    }

    fun getContent(chapter: BookChapter): String? {
        if (chapter.url.contains("titlepage.xhtml")) {
            return """<img src="cover.jpeg" />"""
        }
        val epub = readEpub() ?: return null
        val href = chapter.url.substringBefore('#')
        val startId = chapter.url.substringAfter('#', "").ifEmpty { null }
        val elements = org.jsoup.select.Elements()
        var collecting = false
        for (res in epub.contents) {
            val rh = res.href ?: continue
            if (rh == href) {
                elements.add(getBody(res, startId, null))
                collecting = true
                break // single resource; multi-resource span simplified
            } else if (collecting) {
                break
            }
        }
        if (elements.isEmpty()) {
            // try resources by href
            val res = epub.resources?.getByHref(href) ?: return null
            elements.add(getBody(res, startId, null))
        }
        var html = elements.outerHtml()
        // strip ruby optional
        html = Regex("""<ruby>\s?([\u4e00-\u9fa5])\s?.*?</ruby>""").replace(html, "$1")
        return formatKeepImg(html)
    }

    private fun toChapter(index: Int, res: Resource): BookChapter {
        var title = res.title
        if (title.isNullOrEmpty()) {
            try {
                val data = res.data
                val titles = Jsoup.parse(String(data, charset)).getElementsByTag("title")
                if (titles.isNotEmpty()) title = titles[0].text()
            } catch (_: Exception) {
            }
        }
        return BookChapter(
            url = res.href ?: "",
            title = title ?: "",
            index = index,
            bookUrl = book.bookUrl
        )
    }

    private fun getBody(res: Resource, startFragmentId: String?, endFragmentId: String?): Element {
        val body = Jsoup.parse(String(res.data, charset)).body()
        if (!startFragmentId.isNullOrBlank()) {
            body.getElementById(startFragmentId)?.previousElementSiblings()?.remove()
        }
        if (!endFragmentId.isNullOrBlank() && endFragmentId != startFragmentId) {
            body.getElementById(endFragmentId)?.let {
                it.nextElementSiblings().remove()
                it.remove()
            }
        }
        body.select("script").remove()
        body.select("style").remove()
        return body
    }

    private fun applyBookMeta(chapters: List<BookChapter>) {
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        // fill metadata from epub if book name empty
        readEpub()?.metadata?.let { md ->
            if (book.name.isEmpty()) book.name = md.firstTitle ?: book.name
            if (book.author.isEmpty()) {
                book.author = md.authors?.firstOrNull()?.toString() ?: ""
            }
        }
    }

    private fun formatKeepImg(html: String): String {
        // HtmlFormatter.formatKeepImg simplified: keep img tags, normalize whitespace lightly
        return html
    }

    companion object {
        fun getChapterList(book: Book): ArrayList<BookChapter> = EpubFile(book).getChapterList()
        fun getContent(book: Book, chapter: BookChapter): String? = EpubFile(book).getContent(chapter)
    }
}
''')

# Cbz / Pdf / Umd improved
w("io/legado/app/model/localBook/CbzFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.util.zip.ZipFile

/** CBZ = zip of images; each image is a "chapter" page. */
object CbzFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val f = book.localFile()
        if (!f.isFile) return list
        ZipFile(f).use { zf ->
            val names = zf.entries().asSequence()
                .filter { !it.isDirectory && it.name.matches(Regex(".*\\.(jpe?g|png|webp|gif)$", RegexOption.IGNORE_CASE)) }
                .map { it.name }
                .sorted()
                .toList()
            names.forEachIndexed { i, name ->
                list += BookChapter(
                    url = name,
                    title = "第${i + 1}页",
                    index = i,
                    bookUrl = book.bookUrl
                )
            }
        }
        book.totalChapterNum = list.size
        book.latestChapterTitle = list.lastOrNull()?.title
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        // return img tag pointing at zip entry path for web layer to resolve
        return """<img src="${chapter.url}" />"""
    }
}
''')

w("io/legado/app/model/localBook/PdfFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument

/** PDF: one chapter per page (pdfbox). */
object PdfFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val f = book.localFile()
        if (!f.isFile) return list
        try {
            PDDocument.load(f).use { doc ->
                val n = doc.numberOfPages
                for (i in 0 until n) {
                    list += BookChapter(
                        url = "page:${i + 1}",
                        title = "第${i + 1}页",
                        index = i,
                        bookUrl = book.bookUrl
                    )
                }
                book.totalChapterNum = n
                book.latestChapterTitle = list.lastOrNull()?.title
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        val page = chapter.url.removePrefix("page:").toIntOrNull() ?: (chapter.index + 1)
        // Full text extraction optional; jar may render images via convertPdfToImage
        return try {
            PDDocument.load(book.localFile()).use { doc ->
                val stripper = org.apache.pdfbox.text.PDFTextStripper()
                stripper.startPage = page
                stripper.endPage = page
                stripper.getText(doc)
            }
        } catch (_: Exception) {
            """<p>PDF 第${page}页</p>"""
        }
    }
}
''')

w("io/legado/app/model/localBook/UmdFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import me.ag2s.umdlib.umd.UmdReader
import java.io.FileInputStream

/** UMD via umdlib (same as jar). */
object UmdFile {
    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        try {
            val umd = UmdReader().read(FileInputStream(book.localFile()))
            val titles = umd?.chapters?.titles ?: return list
            titles.forEachIndexed { i, t ->
                list += BookChapter(
                    url = i.toString(),
                    title = t?.toString() ?: "章节${i + 1}",
                    index = i,
                    bookUrl = book.bookUrl
                )
            }
            book.totalChapterNum = list.size
            book.latestChapterTitle = list.lastOrNull()?.title
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun getContent(book: Book, chapter: BookChapter): String? {
        return try {
            val umd = UmdReader().read(FileInputStream(book.localFile()))
            val idx = chapter.index
            umd?.chapters?.getContent(idx)?.toString()
        } catch (_: Exception) {
            null
        }
    }
}
''')

# Update LocalBook to use companion objects correctly
w("io/legado/app/model/localBook/LocalBook.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.exception.TocEmptyException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.regex.Pattern

object LocalBook {
    private val nameAuthorPatterns = arrayOf(
        Pattern.compile("(?i)(.*?)[\\[【](.+?)[\\]】]"),
        Pattern.compile("(?i)(.*?)-{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)_{1,2}(.+)"),
        Pattern.compile("(?i)(.*?)\\s+作者[:：]?\\s*(.+)")
    )

    fun getBookInputStream(book: Book): InputStream {
        val file = book.localFile()
        if (!file.exists()) throw FileNotFoundException("${book.name} 文件不存在")
        return FileInputStream(file)
    }

    fun getChapterList(book: Book): ArrayList<BookChapter> {
        val chapters: ArrayList<BookChapter> = when {
            book.isEpub -> EpubFile.getChapterList(book)
            book.isUmd -> UmdFile.getChapterList(book)
            book.isCbz -> CbzFile.getChapterList(book)
            book.isPdf -> PdfFile.getChapterList(book)
            else -> TextFile(book).getChapterList()
        }
        if (chapters.isEmpty()) throw TocEmptyException("Chapterlist is empty  ${book.localFile()}")
        return chapters
    }

    fun getContent(book: Book, chapter: BookChapter): String? = when {
        book.isEpub -> EpubFile.getContent(book, chapter)
        book.isUmd -> UmdFile.getContent(book, chapter)
        book.isCbz -> CbzFile.getContent(book, chapter)
        book.isPdf -> PdfFile.getContent(book, chapter)
        else -> TextFile(book).getContent(chapter)
    }

    fun analyzeNameAuthor(fileName: String): Pair<String, String> {
        val temp = fileName.substringBeforeLast('.')
        for (p in nameAuthorPatterns) {
            val m = p.matcher(temp)
            if (m.find()) {
                val g1 = m.group(1)?.trim().orEmpty()
                val g2 = m.group(2)?.trim().orEmpty()
                return if (g2.isNotEmpty()) g1 to g2 else temp to ""
            }
        }
        return temp to ""
    }

    fun deleteBook(book: Book) {
        val f = book.localFile()
        if ((book.isLocalTxt || book.isUmd) && f.exists()) f.delete()
        if (book.isEpub) f.parentFile?.deleteRecursively()
    }
}

fun Book.localFile(): File {
    val path = when {
        bookUrl.startsWith("file:") -> bookUrl.removePrefix("file://").removePrefix("file:")
        else -> bookUrl
    }
    val base = rootDir?.let { File(it) } ?: File(".")
    val f = File(path)
    return if (f.isAbsolute) f else File(base, path)
}
''')

# ---------------------------------------------------------------------------
# AnalyzeByJSoup with && || %%
# ---------------------------------------------------------------------------
w("io/legado/app/model/analyzeRule/AnalyzeByJSoup.kt", r'''
package io.legado.app.model.analyzeRule

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * CSS / JSoup mode with && (concat) || (first hit) %% (zip) via RuleAnalyzer.
 */
class AnalyzeByJSoup(content: Any?) {
    private val doc: Element? = when (content) {
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> content?.toString()?.let { Jsoup.parse(it) }
    }

    fun getString(content: Any?, rule: String): String {
        if (rule.isEmpty()) return ""
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||")
        if (rules.size == 1) return getStringSingle(content, rules[0])
        val parts = ArrayList<String>()
        for (rl in rules) {
            val s = getStringSingle(content, rl)
            if (s.isNotEmpty()) {
                parts += s
                if (analyzer.elementsType == "||") break
            }
        }
        return parts.joinToString("\n")
    }

    fun getStringList(content: Any?, rule: String): List<String> {
        if (rule.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getStringListSingle(content, rules[0])
        val results = ArrayList<List<String>>()
        for (rl in rules) {
            val part = getStringListSingle(content, rl)
            if (part.isNotEmpty()) {
                results += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(results) else results.flatten()
    }

    fun getElements(content: Any?, rule: String): List<Any> {
        if (rule.isEmpty()) return emptyList()
        val analyzer = RuleAnalyzer(rule)
        val rules = analyzer.splitRule("&&", "||", "%%")
        if (rules.size == 1) return getElementsSingle(content, rules[0])
        val results = ArrayList<List<Element>>()
        for (rl in rules) {
            val part = getElementsSingle(content, rl).filterIsInstance<Element>()
            if (part.isNotEmpty()) {
                results += part
                if (analyzer.elementsType == "||") break
            }
        }
        if (results.isEmpty()) return emptyList()
        return if (analyzer.elementsType == "%%") zipByIndex(results) else results.flatten()
    }

    private fun getStringSingle(content: Any?, rule: String): String {
        val el = elementOf(content) ?: return ""
        val (css, attr) = splitAttr(rule)
        val selected = if (css.isEmpty()) el else el.selectFirst(css) ?: return ""
        return readAttr(selected, attr)
    }

    private fun getStringListSingle(content: Any?, rule: String): List<String> {
        val el = elementOf(content) ?: return emptyList()
        val (css, attr) = splitAttr(rule)
        val els: Elements = if (css.isEmpty()) Elements(el) else el.select(css)
        return els.map { readAttr(it, attr) }
    }

    private fun getElementsSingle(content: Any?, rule: String): List<Any> {
        val el = elementOf(content) ?: return emptyList()
        val (css, _) = splitAttr(rule)
        return el.select(css.ifEmpty { "*" }).toList()
    }

    private fun readAttr(el: Element, attr: String): String = when (attr) {
        "", "text" -> el.text()
        "textNodes" -> el.textNodes().joinToString("\n") { it.text() }
        "ownText" -> el.ownText()
        "html", "innerHtml" -> el.html()
        "outerHtml", "all" -> el.outerHtml()
        else -> el.attr(attr)
    }

    private fun elementOf(content: Any?): Element? = when (content) {
        null -> doc
        is Element -> content
        is String -> Jsoup.parse(content)
        else -> doc
    }

    private fun splitAttr(rule: String): Pair<String, String> {
        // last @xxx is attribute; avoid @@ default prefix already stripped by AnalyzeRule
        val idx = rule.lastIndexOf('@')
        if (idx <= 0) return rule to "text"
        // @text / @html / @href
        return rule.substring(0, idx) to rule.substring(idx + 1)
    }

    private fun <T> zipByIndex(results: List<List<T>>): List<T> {
        if (results.isEmpty()) return emptyList()
        val out = ArrayList<T>()
        val max = results.maxOf { it.size }
        for (i in 0 until max) {
            for (list in results) if (i < list.size) out += list[i]
        }
        return out
    }
}
''')

# ---------------------------------------------------------------------------
# DefaultData + TxtTocRule + TextFile integration
# ---------------------------------------------------------------------------
w("io/legado/app/data/entities/TxtTocRule.kt", r'''
package io.legado.app.data.entities

data class TxtTocRule(
    var id: Long = 0,
    var enable: Boolean = true,
    var name: String = "",
    var rule: String = "",
    var serialNumber: Int = 0
)
''')

w("io/legado/app/help/DefaultData.kt", r'''
package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.legado.app.data.entities.TxtTocRule
import java.io.File

object DefaultData {
    const val txtTocRuleFileName = "txtTocRule.json"

    val txtTocRules: List<TxtTocRule> by lazy { loadTxtTocRules() }

    private fun loadTxtTocRules(): List<TxtTocRule> {
        val candidates = listOf(
            File("defaultData/txtTocRule.json"),
            File("resources/defaultData/txtTocRule.json"),
            File(System.getProperty("user.dir"), "defaultData/txtTocRule.json"),
        )
        for (f in candidates) {
            if (f.isFile) {
                return parse(f.readText())
            }
        }
        // classpath
        val stream = DefaultData::class.java.classLoader
            ?.getResourceAsStream("defaultData/txtTocRule.json")
            ?: DefaultData::class.java.getResourceAsStream("/defaultData/txtTocRule.json")
        if (stream != null) {
            return parse(stream.bufferedReader().readText())
        }
        return defaultBuiltin()
    }

    private fun parse(json: String): List<TxtTocRule> {
        val type = object : TypeToken<List<TxtTocRule>>() {}.type
        return Gson().fromJson(json, type) ?: defaultBuiltin()
    }

    private fun defaultBuiltin(): List<TxtTocRule> = listOf(
        TxtTocRule(
            id = -1, enable = true, name = "目录",
            rule = "^\\s*第[0-9零一二三四五六七八九十百千万]+[章节回卷].{0,30}$",
            serialNumber = 0
        ),
        TxtTocRule(
            id = -6, enable = true, name = "数字 分隔符 标题",
            rule = "^[ 　\\t]{0,4}\\d{1,5}[：:,.， 、_—\\-].{1,30}$",
            serialNumber = 1
        ),
    )
}
''')

# Rewrite TextFile to use DefaultData
w("io/legado/app/model/localBook/TextFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.help.DefaultData
import io.legado.app.utils.MD5Utils
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

class TextFile(private val book: Book) {
    private val bufferSize = 512_000
    private var charset: Charset = Charset.forName(book.charset ?: "UTF-8")

    fun getChapterList(): ArrayList<BookChapter> {
        val file = book.localFile()
        if (!file.isFile) return arrayListOf()

        if (book.charset.isNullOrBlank() || book.tocUrl.isBlank()) {
            val len = minOf(bufferSize.toLong(), file.length()).toInt().coerceAtLeast(1)
            val buf = ByteArray(len)
            file.inputStream().use { it.read(buf) }
            if (book.charset.isNullOrBlank()) {
                book.charset = detectCharset(buf) ?: "UTF-8"
            }
            charset = Charset.forName(book.charset)
            if (book.tocUrl.isBlank()) {
                val head = String(buf, charset)
                book.tocUrl = selectTocRule(head)
            }
        } else {
            charset = Charset.forName(book.charset)
        }

        val pattern = Pattern.compile(book.tocUrl, Pattern.MULTILINE)
        val chapters = analyze(file, pattern)
        chapters.forEachIndexed { i, ch ->
            ch.index = i
            ch.bookUrl = book.bookUrl
            ch.url = MD5Utils.md5Encode16("${book.name}$i${ch.title}")
        }
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        return chapters
    }

    fun getContent(chapter: BookChapter): String? {
        val file = book.localFile()
        if (!file.isFile) return null
        // re-scan chapter offsets
        val chapters = getChapterList()
        val idx = chapters.indexOfFirst { it.index == chapter.index || it.title == chapter.title }
            .takeIf { it >= 0 } ?: return null
        val start = chapters[idx].byteStart
        val end = chapters.getOrNull(idx + 1)?.byteStart ?: file.length()
        if (end < start) return null
        val bytes = ByteArray((end - start).toInt())
        file.inputStream().use { ins ->
            ins.skip(start)
            var off = 0
            while (off < bytes.size) {
                val n = ins.read(bytes, off, bytes.size - off)
                if (n <= 0) break
                off += n
            }
        }
        return String(bytes, charset).trim()
    }

    private fun analyze(file: File, pattern: Pattern): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        // char-index based for simplicity; store as byteStart approx via string offset mapping
        val text = file.readText(charset)
        val m = pattern.matcher(text)
        var first = true
        while (m.find()) {
            val start = m.start()
            if (first && start > 0) {
                list += BookChapter(title = "前言", index = 0).also { it.byteStart = 0L }
            }
            first = false
            val title = m.group()?.trim()?.take(80) ?: "章节"
            // approximate byte offset: for UTF-8 not exact but ok for many files; better map via charset encoder
            val byteStart = text.substring(0, start).toByteArray(charset).size.toLong()
            list += BookChapter(title = title).also { it.byteStart = byteStart }
        }
        if (list.isEmpty()) {
            list += BookChapter(title = book.name.ifEmpty { "正文" }).also { it.byteStart = 0L }
        }
        return list
    }

    /** Pick best enabled rule from DefaultData by match count on sample. */
    private fun selectTocRule(sample: String): String {
        val rules = DefaultData.txtTocRules.filter { it.enable && it.rule.isNotBlank() }
        var best = DEFAULT_TOC
        var bestCount = 0
        for (r in rules) {
            try {
                val n = Pattern.compile(r.rule, Pattern.MULTILINE).matcher(sample)
                    .results().count().toInt()
                if (n in 3..500 && n > bestCount) {
                    bestCount = n
                    best = r.rule
                }
            } catch (_: Exception) {
            }
        }
        return best
    }

    private fun detectCharset(buf: ByteArray): String? {
        if (buf.size >= 3 && buf[0] == 0xEF.toByte() && buf[1] == 0xBB.toByte() && buf[2] == 0xBF.toByte()) {
            return "UTF-8"
        }
        return "UTF-8"
    }

    companion object {
        const val DEFAULT_TOC =
            "^\\s*第[0-9零一二三四五六七八九十百千万]+[章节回卷].{0,30}$"
    }
}

var BookChapter.byteStart: Long
    get() = tag?.toLongOrNull() ?: 0L
    set(v) { tag = v.toString() }
''')

# ---------------------------------------------------------------------------
# Multi-search with jar defaults concurrentCount 36 / 24 SSE
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/BookControllerExtras.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.SearchBook
import io.legado.app.model.webBook.WebBook
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Multi-source search defaults from jar:
 * - searchBookMulti concurrentCount default **36**
 * - searchBookMultiSSE concurrentCount default **24**
 * Per-source timeout 15s to avoid hung sources blocking the batch.
 */

suspend fun BookController.exploreBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val url = context.queryParam("url").firstOrNull()
        ?: context.bodyAsJson?.getString("url")
        ?: return rd.setErrorMsg("url 不能为空")
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("page") ?: 1
    val ns = getUserNameSpace(context)
    val sourceUrl = context.queryParam("bookSourceUrl").firstOrNull()
        ?: context.bodyAsJson?.getString("bookSourceUrl")
    val sourceStr = sourceUrl?.let { getBookSourceStringBySourceURLOpt(it, ns) }
        ?: return rd.setErrorMsg("书源信息错误")
    val list = withTimeoutOrNull(30_000L) {
        WebBook(sourceStr, getAppConfig().debugLog, null, ns).exploreBook(url, page)
    } ?: emptyList()
    return rd.setData(list)
}

suspend fun BookController.searchBookMulti(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val key = paramKey(context) ?: return rd.setErrorMsg("请输入关键字")
    val page = paramInt(context, "page") ?: 1
    val concurrent = (paramInt(context, "concurrentCount") ?: 36).coerceIn(1, 64)
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val results = searchMultiInternal(ns, sources, key, page, concurrent, perSourceTimeoutMs = 15_000L)
    return rd.setData(results)
}

suspend fun BookController.searchBookMultiSSE(context: RoutingContext) {
    val key = context.queryParam("key").firstOrNull()
        ?: context.bodyAsJson?.getString("key")
        ?: ""
    val page = context.queryParam("page").firstOrNull()?.toIntOrNull() ?: 1
    val concurrent = (context.queryParam("concurrentCount").firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger("concurrentCount")
        ?: 24).coerceIn(1, 64)
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    val resp = context.response()
        .putHeader("Content-Type", "text/event-stream; charset=utf-8")
        .putHeader("Cache-Control", "no-cache")
        .setChunked(true)
    coroutineScope {
        enabled.chunked(concurrent).forEach { batch ->
            batch.map { o ->
                async {
                    val src = o.encode()
                    val origin = o.getString("bookSourceUrl") ?: ""
                    try {
                        val list = withTimeoutOrNull(15_000L) {
                            WebBook(src, false, null, ns).searchBook(key, page)
                        } ?: emptyList()
                        val payload = JsonObject()
                            .put("origin", origin)
                            .put("name", o.getString("bookSourceName"))
                            .put("data", JsonArray(list.map { JsonObject.mapFrom(it) }))
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("data: ${payload.encode()}\n\n")
                        }
                    } catch (e: Exception) {
                        val err = JsonObject().put("origin", origin).put("error", e.message)
                        synchronized(resp) {
                            if (!resp.ended()) resp.write("event: error\ndata: ${err.encode()}\n\n")
                        }
                    }
                }
            }.awaitAll()
        }
    }
    if (!resp.ended()) resp.write("event: end\ndata: []\n\n").end()
}

private suspend fun BookController.searchMultiInternal(
    ns: String,
    sources: JsonArray,
    key: String,
    page: Int,
    concurrent: Int,
    perSourceTimeoutMs: Long
): List<SearchBook> = coroutineScope {
    val out = ArrayList<SearchBook>()
    val lock = Mutex()
    val enabled = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i) ?: return@mapNotNull null
        if (o.getBoolean("enabled", true) == false) null else o
    }
    enabled.chunked(concurrent).forEach { batch ->
        batch.map { o ->
            async {
                try {
                    withTimeoutOrNull(perSourceTimeoutMs) {
                        WebBook(o.encode(), false, null, ns).searchBook(key, page)
                    } ?: emptyList()
                } catch (_: Exception) {
                    emptyList()
                }
            }
        }.awaitAll().forEach { list ->
            lock.withLock { out.addAll(list) }
        }
    }
    out.distinctBy { it.bookUrl.ifEmpty { "${it.name}|${it.author}|${it.origin}" } }
}

private fun paramKey(context: RoutingContext): String? =
    context.queryParam("key").firstOrNull() ?: context.bodyAsJson?.getString("key")

private fun paramInt(context: RoutingContext, name: String): Int? =
    context.queryParam(name).firstOrNull()?.toIntOrNull()
        ?: context.bodyAsJson?.getInteger(name)

fun BookController.searchBookSource(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val name = context.queryParam("name").firstOrNull()
        ?: context.bodyAsJson?.getString("name") ?: ""
    val ns = getUserNameSpace(context)
    val sources = ExtKt.asJsonArray(getUserStorage(ns, "bookSource")) ?: JsonArray()
    val hits = (0 until sources.size()).mapNotNull { i ->
        val o = sources.getJsonObject(i)
        val n = o.getString("bookSourceName") ?: ""
        if (name.isEmpty() || n.contains(name, true)) o else null
    }
    return rd.setData(hits)
}

fun BookController.searchBookSourceSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true).end()
}

fun BookController.getAvailableBookSource(context: RoutingContext): ReturnData =
    searchBookSource(context)

fun BookController.setBookSource(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.bookSourceDebugSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true)
        .write("data: {\"msg\":\"debug start\"}\n\n").end()
}

fun BookController.getInvalidBookSources(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.importBookPreview(context: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("books" to emptyList<Any>()))

fun BookController.refreshLocalBook(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.getTxtTocRules(context: RoutingContext): ReturnData {
    return ReturnData().setData(io.legado.app.help.DefaultData.txtTocRules)
}

fun BookController.getChapterListByRule(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    // body: book + rule string — re-split txt with custom rule
    return rd.setData(emptyList<Any>())
}

fun BookController.cacheBookSSE(context: RoutingContext) {
    context.response().putHeader("Content-Type", "text/event-stream").setChunked(true).end()
}

fun BookController.getShelfBookWithCacheInfo(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.deleteBookCache(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.searchBookContent(context: RoutingContext): ReturnData =
    ReturnData().setData(emptyList<Any>())

fun BookController.saveBookConfig(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.saveBookContent(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.saveBookGroupId(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.addBookGroupMulti(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.removeBookGroupMulti(context: RoutingContext): ReturnData =
    ReturnData().setData(true)

fun BookController.tts(context: RoutingContext): ReturnData =
    ReturnData().setData(mapOf("note" to "stream audio via getSpeakStream"))

fun BookController.backupToMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.backupUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}

fun BookController.restoreFromMongodb(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    val uri = getAppConfig().mongoUri
    if (uri.isBlank()) return rd.setErrorMsg("未配置 mongoUri")
    return rd.setData(MongoBackup.restoreUser(getUserNameSpace(context), uri, getAppConfig().mongoDbName))
}
''')

# README
rp = BIZ / "README.md"
rd = rp.read_text(encoding="utf-8") if rp.exists() else ""
if "Phase 4" not in rd:
    rd += """

## Phase 4 增量

- **EpubFile**：epublib 读 spine/TOC + 章节 HTML（Jsoup 去 script/style）
- **CbzFile / PdfFile / UmdFile**：分页/条目目录与正文
- **AnalyzeByJSoup**：`&&` / `||` / `%%` 与属性 `@text/@html/@href`
- **searchBookMulti**：默认并发 **36**，SSE **24**，单源超时 **15s**
- **DefaultData + txtTocRule.json**：内置目录正则库；TextFile 自动选最优规则
"""
    rp.write_text(rd, encoding="utf-8")

print("phase4 complete kt=", len(list(BIZ.rglob('*.kt'))))
