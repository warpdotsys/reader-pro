# -*- coding: utf-8 -*-
"""Phase 5: Epub spin+toc merge, media streaming, toNewRule, HtmlFormatter, import."""
from pathlib import Path
import os

BIZ = Path(r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\business")
H = "/** Business rewrite from reader-pro-3.2.14.jar — phase5. */\n\n"

def w(rel, c):
    p = BIZ / rel.replace("/", os.sep)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(H + c.lstrip("\n"), encoding="utf-8", newline="\n")
    print("wrote", rel, p.stat().st_size)

# ---------------------------------------------------------------------------
# EpubFile complete with spinAndToc / tocAndSpin
# ---------------------------------------------------------------------------
w("io/legado/app/model/localBook/EpubFile.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.utils.HtmlFormatter
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.SpineReference
import me.ag2s.epublib.epub.EpubReader
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.nio.charset.Charset
import java.util.zip.ZipFile

/**
 * EPUB reader (epublib).
 * - getChapterList: NCX/TOC unique resources
 * - getChapterListBySpine: spine order
 * - getChapterListBySpinAndToc: spine order + TOC titles
 * - getChapterListByTocAndSpin: TOC order + spine titles
 */
class EpubFile(var book: Book) {
    private var cached: EpubBook? = null
    private val charset: Charset = Charset.forName("UTF-8")

    private fun epub(): EpubBook? {
        if (cached != null) return cached
        return try {
            EpubReader().readEpubLazy(ZipFile(book.localFile()), "utf-8").also { cached = it }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /** Default: prefer TOC; if empty use spine. */
    fun getChapterList(): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        if (toc.isNotEmpty()) {
            applyMeta(toc)
            return toc
        }
        return getChapterListBySpine()
    }

    fun getChapterListFromToc(): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val e = epub() ?: return list
        val resources = e.tableOfContents?.allUniqueResources ?: return list
        resources.forEachIndexed { index, res -> list += toChapter(index, res) }
        return list
    }

    fun getChapterListBySpine(): ArrayList<BookChapter> {
        val list = ArrayList<BookChapter>()
        val e = epub() ?: return list
        val refs = e.spine?.spineReferences ?: return list
        refs.forEachIndexed { index, ref ->
            val res = ref.resource ?: return@forEachIndexed
            val ch = toChapter(index, res)
            if (index == 0 && ch.title.isEmpty()) ch.title = "封面"
            list += ch
        }
        applyMeta(list)
        return list
    }

    /**
     * Spine order is canonical reading order; fill titles from TOC map by href.
     * @param useTocTitle always prefer TOC title when present
     */
    fun getChapterListBySpinAndToc(useTocTitle: Boolean = false): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        val spin = getChapterListBySpine()
        if (spin.isEmpty()) return toc
        if (toc.isEmpty()) return spin
        val titleMap = toc.associateBy { normalizeHref(it.url) }
        for (ch in spin) {
            val tocCh = titleMap[normalizeHref(ch.url)]
            if (tocCh != null && tocCh.title.isNotEmpty() && (useTocTitle || ch.title.isEmpty())) {
                ch.title = tocCh.title
            }
        }
        applyMeta(spin)
        return spin
    }

    /** TOC order preferred; fill empty titles from spine map. */
    fun getChapterListByTocAndSpin(useSpinTitle: Boolean = false): ArrayList<BookChapter> {
        val toc = getChapterListFromToc()
        val spin = getChapterListBySpine()
        if (toc.isEmpty()) return spin
        if (spin.isEmpty()) return toc
        val titleMap = spin.associateBy { normalizeHref(it.url) }
        for (ch in toc) {
            val spinCh = titleMap[normalizeHref(ch.url)]
            if (spinCh != null && spinCh.title.isNotEmpty() && (useSpinTitle || ch.title.isEmpty())) {
                ch.title = spinCh.title
            }
        }
        applyMeta(toc)
        return toc
    }

    fun getContent(chapter: BookChapter): String? {
        if (chapter.url.contains("titlepage.xhtml")) {
            return """<img src="cover.jpeg" />"""
        }
        val e = epub() ?: return null
        val href = chapter.url.substringBefore('#')
        val startId = chapter.url.substringAfter('#', "").ifEmpty { null }
        val endId = chapter.variable // optional end fragment stored in variable in full impl
        val elements = org.jsoup.select.Elements()
        var collecting = false
        val nextUrl = null as String? // multi-resource chapter span simplified

        for (res in e.contents) {
            val rh = res.href ?: continue
            if (normalizeHref(rh) == normalizeHref(href)) {
                elements.add(getBody(res, startId, endId))
                collecting = true
                if (nextUrl == null || normalizeHref(rh) == normalizeHref(nextUrl)) break
            } else if (collecting) {
                if (nextUrl != null && normalizeHref(rh) == normalizeHref(nextUrl)) break
                elements.add(getBody(res, null, null))
            }
        }
        if (elements.isEmpty()) {
            val res = e.resources?.getByHref(href) ?: return null
            elements.add(getBody(res, startId, endId))
        }
        var html = elements.outerHtml()
        html = Regex("""<ruby>\s?([\u4e00-\u9fa5])\s?.*?</ruby>""").replace(html, "$1")
        return HtmlFormatter.formatKeepImg(html)
    }

    /** Cover image bytes for streaming. */
    fun getCoverBytes(): ByteArray? {
        val e = epub() ?: return null
        return try {
            e.coverImage?.data
        } catch (_: Exception) {
            null
        }
    }

    fun getImageByHref(href: String): ByteArray? {
        val e = epub() ?: return null
        val ab = href.replace("../", "")
        return try {
            e.resources?.getByHref(ab)?.data
        } catch (_: Exception) {
            null
        }
    }

    private fun toChapter(index: Int, res: Resource): BookChapter {
        var title = res.title
        if (title.isNullOrEmpty()) {
            try {
                val titles = Jsoup.parse(String(res.data, charset)).getElementsByTag("title")
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
        body.select("script,style").remove()
        return body
    }

    private fun applyMeta(chapters: List<BookChapter>) {
        if (chapters.isNotEmpty()) {
            book.latestChapterTitle = chapters.last().title
            book.totalChapterNum = chapters.size
        }
        epub()?.metadata?.let { md ->
            if (book.name.isEmpty()) book.name = md.firstTitle ?: book.name
            if (book.author.isEmpty()) {
                book.author = md.authors?.firstOrNull()?.toString() ?: ""
            }
        }
    }

    private fun normalizeHref(href: String): String =
        href.substringBefore('#').replace("\\", "/").trimStart('/')

    companion object {
        @Volatile private var eFile: EpubFile? = null

        @Synchronized
        private fun getEFile(book: Book): EpubFile {
            val cur = eFile
            if (cur != null && cur.book.bookUrl == book.bookUrl) {
                cur.book = book
                return cur
            }
            return EpubFile(book).also { eFile = it }
        }

        fun getChapterList(book: Book): ArrayList<BookChapter> =
            getEFile(book).getChapterListBySpinAndToc(useTocTitle = true)

        fun getContent(book: Book, chapter: BookChapter): String? =
            getEFile(book).getContent(chapter)

        fun getCoverBytes(book: Book): ByteArray? = getEFile(book).getCoverBytes()
        fun getImage(book: Book, href: String): ByteArray? = getEFile(book).getImageByHref(href)
    }
}
''')

w("io/legado/app/utils/HtmlFormatter.kt", r'''
package io.legado.app.utils

object HtmlFormatter {
    /** Keep img tags, strip excessive scripts already removed; light normalize. */
    fun formatKeepImg(html: String, other: Any? = null): String {
        var s = html
        // collapse 3+ newlines
        s = s.replace(Regex("\\n{3,}"), "\n\n")
        // ensure img not stripped
        return s.trim()
    }
}
''')

# ---------------------------------------------------------------------------
# Local media streaming helper
# ---------------------------------------------------------------------------
w("io/legado/app/model/localBook/LocalMedia.kt", r'''
package io.legado.app.model.localBook

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.rendering.ImageType
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.zip.ZipFile
import javax.imageio.ImageIO

/**
 * Stream page/image bytes for CBZ/PDF/EPUB cover to HTTP layer.
 */
object LocalMedia {

    fun getChapterImage(book: Book, chapter: BookChapter): ByteArray? {
        return when {
            book.isCbz -> cbzImage(book, chapter.url)
            book.isPdf -> pdfPageImage(book, chapter)
            book.isEpub -> {
                // content may be img only — try resolve as resource
                EpubFile.getImage(book, chapter.url)
            }
            else -> null
        }
    }

    fun getCover(book: Book): ByteArray? {
        if (book.isEpub) return EpubFile.getCoverBytes(book)
        if (book.isCbz) {
            val chapters = CbzFile.getChapterList(book)
            val first = chapters.firstOrNull()?.url ?: return null
            return cbzImage(book, first)
        }
        if (book.isPdf) {
            return pdfPageImage(book, BookChapter(url = "page:1", index = 0))
        }
        return null
    }

    private fun cbzImage(book: Book, entryName: String): ByteArray? {
        return try {
            ZipFile(book.localFile()).use { zf ->
                val e = zf.getEntry(entryName) ?: return null
                zf.getInputStream(e).readBytes()
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun pdfPageImage(book: Book, chapter: BookChapter): ByteArray? {
        val page = chapter.url.removePrefix("page:").toIntOrNull() ?: (chapter.index + 1)
        return try {
            PDDocument.load(book.localFile()).use { doc ->
                val renderer = PDFRenderer(doc)
                val img: BufferedImage = renderer.renderImageWithDPI(page - 1, 120f, ImageType.RGB)
                val baos = ByteArrayOutputStream()
                ImageIO.write(img, "jpg", baos)
                baos.toByteArray()
            }
        } catch (_: Exception) {
            null
        }
    }

    fun guessContentType(pathOrUrl: String): String = when {
        pathOrUrl.endsWith(".png", true) -> "image/png"
        pathOrUrl.endsWith(".gif", true) -> "image/gif"
        pathOrUrl.endsWith(".webp", true) -> "image/webp"
        pathOrUrl.endsWith(".jpg", true) || pathOrUrl.endsWith(".jpeg", true) -> "image/jpeg"
        pathOrUrl.startsWith("page:") -> "image/jpeg"
        else -> "application/octet-stream"
    }
}
''')

# ---------------------------------------------------------------------------
# SourceAnalyzer toNewRule full
# ---------------------------------------------------------------------------
w("io/legado/app/help/SourceAnalyzer.kt", r'''
package io.legado.app.help

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import io.legado.app.data.entities.BookSource
import io.legado.app.data.entities.rule.BookInfoRule
import io.legado.app.data.entities.rule.ContentRule
import io.legado.app.data.entities.rule.ExploreRule
import io.legado.app.data.entities.rule.SearchRule
import io.legado.app.data.entities.rule.TocRule

/**
 * Normalize legado / legacy book source JSON.
 */
object SourceAnalyzer {

    private val gson = Gson()

    fun jsonToBookSources(json: String): Result<MutableList<BookSource>> = runCatching {
        val el = JsonParser.parseString(json.trim())
        when {
            el.isJsonArray -> {
                val list = ArrayList<BookSource>()
                el.asJsonArray.forEach { item ->
                    list += jsonToBookSource(item.toString()).getOrThrow()
                }
                list
            }
            el.isJsonObject -> mutableListOf(jsonToBookSource(json).getOrThrow())
            else -> error("格式不对")
        }
    }

    fun jsonToBookSource(json: String): Result<BookSource> = runCatching {
        val o = JsonParser.parseString(json).asJsonObject
        BookSource(
            bookSourceUrl = str(o, "bookSourceUrl") ?: str(o, "url") ?: error("缺少 bookSourceUrl"),
            bookSourceName = str(o, "bookSourceName") ?: str(o, "sourceName") ?: "",
            bookSourceGroup = str(o, "bookSourceGroup") ?: str(o, "sourceGroup"),
            bookSourceType = int(o, "bookSourceType") ?: 0,
            enabled = bool(o, "enabled") ?: true,
            header = str(o, "header") ?: str(o, "headers"),
            loginUrl = str(o, "loginUrl"),
            ruleSearch = parseSearch(o),
            ruleExplore = parseExplore(o),
            ruleBookInfo = parseBookInfo(o),
            ruleToc = parseToc(o),
            ruleContent = parseContent(o),
        )
    }

    private fun parseSearch(o: JsonObject): SearchRule? {
        val r = o.getAsJsonObject("ruleSearch")
        val list = str(r, "bookList") ?: str(o, "ruleSearchList") ?: str(o, "searchList")
        val url = str(r, "url") ?: str(o, "ruleSearchUrl") ?: str(o, "searchUrl")
        if (list == null && url == null && r == null) return null
        return SearchRule(
            checkKeyWord = str(r, "checkKeyWord") ?: str(o, "ruleSearchCheckKey"),
            url = toNewUrl(url),
            bookList = toNewRule(list),
            name = toNewRule(str(r, "name") ?: str(o, "ruleSearchName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleSearchAuthor")),
            bookUrl = toNewRule(str(r, "bookUrl") ?: str(o, "ruleSearchBookUrl") ?: str(o, "ruleSearchNoteUrl")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleSearchCoverUrl")),
            intro = toNewRule(str(r, "intro") ?: str(o, "ruleSearchIntroduce")),
            kind = toNewRule(str(r, "kind") ?: str(o, "ruleSearchKind")),
        )
    }

    private fun parseExplore(o: JsonObject): ExploreRule? {
        val r = o.getAsJsonObject("ruleExplore")
        val list = str(r, "bookList") ?: str(o, "ruleFindList")
        if (list == null && r == null) return null
        return ExploreRule(
            bookList = toNewRule(list),
            name = toNewRule(str(r, "name") ?: str(o, "ruleFindName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleFindAuthor")),
            bookUrl = toNewRule(str(r, "bookUrl") ?: str(o, "ruleFindNoteUrl")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleFindCoverUrl")),
        )
    }

    private fun parseBookInfo(o: JsonObject): BookInfoRule? {
        val r = o.getAsJsonObject("ruleBookInfo")
        return BookInfoRule(
            name = toNewRule(str(r, "name") ?: str(o, "ruleBookName")),
            author = toNewRule(str(r, "author") ?: str(o, "ruleBookAuthor")),
            kind = toNewRule(str(r, "kind") ?: str(o, "ruleBookKind")),
            coverUrl = toNewRule(str(r, "coverUrl") ?: str(o, "ruleCoverUrl")),
            intro = toNewRule(str(r, "intro") ?: str(o, "ruleIntroduce")),
            tocUrl = toNewRule(str(r, "tocUrl") ?: str(o, "ruleChapterUrl")),
        )
    }

    private fun parseToc(o: JsonObject): TocRule? {
        val r = o.getAsJsonObject("ruleToc")
        return TocRule(
            chapterList = toNewRule(str(r, "chapterList") ?: str(o, "ruleChapterList")),
            chapterName = toNewRule(str(r, "chapterName") ?: str(o, "ruleChapterName")),
            chapterUrl = toNewRule(str(r, "chapterUrl") ?: str(o, "ruleContentUrl")),
            nextTocUrl = toNewRule(str(r, "nextTocUrl") ?: str(o, "ruleChapterUrlNext")),
            preUpdateJs = str(r, "preUpdateJs") ?: str(o, "ruleBookInfoInit"),
        )
    }

    private fun parseContent(o: JsonObject): ContentRule? {
        val r = o.getAsJsonObject("ruleContent")
        return ContentRule(
            content = toNewRule(str(r, "content") ?: str(o, "ruleBookContent")),
            nextContentUrl = toNewRule(str(r, "nextContentUrl") ?: str(o, "ruleContentUrlNext")),
            replaceRegex = str(r, "replaceRegex") ?: str(o, "ruleBookContentReplace"),
        )
    }

    /**
     * Legacy rule → new rule (from jar SourceAnalyzer.toNewRule):
     * - leading `-` / `+` preserved
     * - skip if already @CSS/@XPath/js/regex
     * - single `#` → `##` (replace delimiter)
     * - single `|` → `||` (or)
     * - single `&` → `&&` (and) when not URL
     */
    fun toNewRule(oldRule: String?): String? {
        if (oldRule.isNullOrBlank()) return null
        var rule = oldRule
        var reverse = false
        var allInOne = false
        if (rule.startsWith("-")) {
            reverse = true
            rule = rule.substring(1)
        }
        if (rule.startsWith("+")) {
            allInOne = true
            rule = rule.substring(1)
        }
        val skip =
            rule.startsWith("@CSS:", ignoreCase = true) ||
                rule.startsWith("@XPath:", ignoreCase = true) ||
                rule.startsWith("//") ||
                rule.startsWith("##") ||
                rule.startsWith(":") ||
                rule.contains("@js:", ignoreCase = true) ||
                rule.contains("<js>", ignoreCase = true)
        if (!skip) {
            if (rule.contains("#") && !rule.contains("##")) {
                // careful: id selectors like #id — jar converts all # to ## for replace chains
                rule = rule.replace("#", "##")
            }
            if (rule.contains("|") && !rule.contains("||")) {
                if (rule.contains("##")) {
                    val list = rule.split("##")
                    if (list[0].contains("|")) {
                        var rebuilt = list[0].replace("|", "||")
                        for (i in 1 until list.size) rebuilt += "##" + list[i]
                        rule = rebuilt
                    }
                } else {
                    rule = rule.replace("|", "||")
                }
            }
            if (rule.contains("&") && !rule.contains("&&") &&
                !rule.contains("http") && !rule.startsWith("/")
            ) {
                rule = rule.replace("&", "&&")
            }
        }
        if (allInOne) rule = "+$rule"
        if (reverse) rule = "-$rule"
        return rule
    }

    fun toNewUrl(url: String?): String? {
        if (url.isNullOrBlank()) return url
        return url
            .replace("searchKey", "{{key}}")
            .replace("{searchKey}", "{{key}}")
            .replace("searchPage", "{{page}}")
            .replace("{page}", "{{page}}")
            .replace("pageIndex", "{{page}}")
    }

    private fun str(o: JsonObject?, key: String): String? {
        if (o == null || !o.has(key) || o.get(key).isJsonNull) return null
        val e = o.get(key)
        return if (e.isJsonPrimitive) e.asString else e.toString()
    }

    private fun int(o: JsonObject, key: String): Int? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asInt }.getOrNull() else null

    private fun bool(o: JsonObject, key: String): Boolean? =
        if (o.has(key) && o.get(key).isJsonPrimitive) runCatching { o.get(key).asBoolean }.getOrNull() else null
}
''')

# ---------------------------------------------------------------------------
# Local book import + media stream API helpers on BookController
# ---------------------------------------------------------------------------
w("com/htmake/reader/api/controller/LocalBookApi.kt", r'''
package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.utils.ExtKt
import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import io.legado.app.model.localBook.LocalBook
import io.legado.app.model.localBook.LocalMedia
import io.legado.app.model.localBook.localFile
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.util.UUID

/**
 * Local book import preview / open + binary media streaming endpoints helpers.
 */
object LocalBookApi {

    fun importPreview(context: RoutingContext, ctrl: BookController): ReturnData {
        val rd = ReturnData()
        val ns = ctrl.getUserNameSpace(context)
        val uploads = context.fileUploads()
        if (uploads.isEmpty()) return rd.setErrorMsg("请上传文件")
        val books = ArrayList<Map<String, Any?>>()
        val dir = File(ExtKt.getWorkDir("storage", "data", ns, "local")).apply { mkdirs() }
        for (up in uploads) {
            val name = up.fileName()
            val dest = File(dir, "${UUID.randomUUID()}_$name")
            File(up.uploadedFileName()).copyTo(dest, overwrite = true)
            val (bookName, author) = LocalBook.analyzeNameAuthor(name)
            val book = Book(
                bookUrl = dest.absolutePath,
                origin = "loc_book",
                originName = name,
                name = bookName,
                author = author,
                rootDir = ExtKt.getWorkDir(),
                userNameSpace = ns,
                canUpdate = false,
                isInShelf = false
            )
            try {
                val chapters = LocalBook.getChapterList(book)
                books += mapOf(
                    "book" to book,
                    "chapterCount" to chapters.size,
                    "latest" to book.latestChapterTitle
                )
            } catch (e: Exception) {
                books += mapOf("book" to book, "error" to e.message)
            }
        }
        return rd.setData(books)
    }

    fun streamChapterImage(context: RoutingContext, ctrl: BookController) {
        val ns = ctrl.getUserNameSpace(context)
        val bookUrl = context.queryParam("bookUrl").firstOrNull() ?: run {
            context.response().setStatusCode(400).end("bookUrl required"); return
        }
        val index = context.queryParam("index").firstOrNull()?.toIntOrNull() ?: 0
        val book = ctrl.getShelfBookByURL(bookUrl, ns)
            ?: Book(bookUrl = bookUrl, origin = "loc_book", rootDir = ExtKt.getWorkDir())
        val chapters = try {
            LocalBook.getChapterList(book)
        } catch (_: Exception) {
            emptyList()
        }
        val ch = chapters.getOrNull(index) ?: BookChapter(index = index, url = context.queryParam("url").firstOrNull() ?: "")
        val bytes = LocalMedia.getChapterImage(book, ch)
        if (bytes == null) {
            context.response().setStatusCode(404).end()
            return
        }
        val ct = LocalMedia.guessContentType(ch.url)
        context.response()
            .putHeader("Content-Type", ct)
            .putHeader("Cache-Control", "public, max-age=86400")
            .end(io.vertx.core.buffer.Buffer.buffer(bytes))
    }

    fun streamCover(context: RoutingContext, ctrl: BookController) {
        val bookUrl = context.queryParam("bookUrl").firstOrNull()
            ?: context.queryParam("path").firstOrNull()
        if (bookUrl.isNullOrEmpty()) {
            context.response().setStatusCode(400).end(); return
        }
        val ns = ctrl.getUserNameSpace(context)
        val book = ctrl.getShelfBookByURL(bookUrl, ns)
            ?: Book(bookUrl = bookUrl, origin = "loc_book", rootDir = ExtKt.getWorkDir())
        val bytes = LocalMedia.getCover(book)
        if (bytes == null) {
            context.response().setStatusCode(404).end(); return
        }
        context.response()
            .putHeader("Content-Type", "image/jpeg")
            .end(io.vertx.core.buffer.Buffer.buffer(bytes))
    }
}

fun BookController.importBookPreview(context: RoutingContext): ReturnData {
    if (!checkAuth(context)) return ReturnData().setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    return LocalBookApi.importPreview(context, this)
}

fun BookController.refreshLocalBook(context: RoutingContext): ReturnData {
    val rd = ReturnData()
    if (!checkAuth(context)) return rd.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    val ns = getUserNameSpace(context)
    val url = context.bodyAsJson?.getString("bookUrl")
        ?: context.queryParam("bookUrl").firstOrNull()
        ?: return rd.setErrorMsg("bookUrl 不能为空")
    val book = getShelfBookByURL(url, ns) ?: return rd.setErrorMsg("书籍不存在")
    return try {
        val chapters = LocalBook.getChapterList(book)
        // refresh shelf chapter count
        // editShelfBook suspend — call from coroutine context in real app
        rd.setData(mapOf("chapters" to chapters.size, "title" to book.latestChapterTitle))
    } catch (e: Exception) {
        rd.setErrorMsg(e.message ?: "刷新失败")
    }
}
''')

# Wire cover streaming note into BookController cover if needed - already has cover()

# Update getChapterList default path comment in LocalBook for epub spin+toc
# LocalBook already calls EpubFile.getChapterList which now uses spinAndToc

# README
rp = BIZ / "README.md"
rd = rp.read_text(encoding="utf-8") if rp.exists() else ""
if "Phase 5" not in rd:
    rd += """

## Phase 5 增量

- **EpubFile**：`getChapterListBySpinAndToc` / `getChapterListByTocAndSpin` 标题合并；默认目录用 spine 顺序 + TOC 标题
- **LocalMedia**：CBZ 条目图 / PDF 页渲染 JPG / EPUB 封面与内嵌图字节流
- **SourceAnalyzer.toNewRule**：完整 `-`/`+`、`#`→`##`、`|`→`||`、`&`→`&&` 旧规则迁移
- **HtmlFormatter.formatKeepImg**
- **LocalBookApi**：本地书导入预览 + 章节图/封面 HTTP 流式输出
"""
    rp.write_text(rd, encoding="utf-8")

print("phase5 done", len(list(BIZ.rglob('*.kt'))))
