package com.htmake.reader

import com.htmake.reader.api.ReturnData
import com.htmake.reader.api.YueduApi
import io.legado.app.help.SourceAnalyzer
import io.legado.app.model.analyzeRule.AnalyzeByJSoup
import io.legado.app.model.analyzeRule.AnalyzeByXPath
import io.legado.app.model.analyzeRule.AnalyzeRule
import io.legado.app.utils.MD5Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class SmokeTest {

    @Test
    fun returnData_successAndError() {
        val ok = ReturnData().setData(mapOf("a" to 1))
        assertTrue(ok.isSuccess)
        val err = ReturnData().setErrorMsg("fail")
        assertFalse(err.isSuccess)
        assertEquals("fail", err.errorMsg)
    }

    @Test
    fun md5_stable() {
        assertEquals(32, MD5Utils.md5Encode("hello").length)
        assertEquals(16, MD5Utils.md5Encode16("hello").length)
    }

    @Test
    fun jsoup_css_text() {
        val html = """<div class="list"><a href="/b1">书名A</a><a href="/b2">书名B</a></div>"""
        val a = AnalyzeByJSoup(html)
        val titles = a.getStringList(html, "div.list a@text")
        assertEquals(listOf("书名A", "书名B"), titles)
        val href = a.getString(html, "div.list a@href")
        assertEquals("/b1", href)
    }

    @Test
    fun xpath_basic() {
        val html = """
            <html><body>
              <div id="list">
                <a href="/1">One</a>
                <a href="/2">Two</a>
              </div>
            </body></html>
        """.trimIndent()
        val xp = AnalyzeByXPath(html)
        val texts = xp.getStringList(null, "//div[@id='list']/a/text()")
        assertTrue(texts.contains("One") || texts.any { it.contains("One") })
        val els = xp.getElements(null, "//a")
        assertTrue(els.size >= 2)
    }

    @Test
    fun analyzeRule_modes() {
        val html = """<ul><li class="c">Hello</li></ul>"""
        val rule = AnalyzeRule()
        rule.setContent(html, "http://example.com")
        val s = rule.getString("li.c@text")
        assertEquals("Hello", s)
    }

    @Test
    fun sourceAnalyzer_legadoJson() {
        val json = """
        {
          "bookSourceUrl": "https://example.com",
          "bookSourceName": "示例源",
          "enabled": true,
          "ruleSearch": {
            "url": "https://example.com/search?q={{key}}",
            "bookList": ".item",
            "name": "a@text",
            "bookUrl": "a@href"
          }
        }
        """.trimIndent()
        val src = SourceAnalyzer.jsonToBookSource(json).getOrThrow()
        assertEquals("https://example.com", src.bookSourceUrl)
        assertEquals("示例源", src.bookSourceName)
        assertEquals(".item", src.ruleSearch?.bookList)
    }

    @Test
    fun yueduApi_routes_cover_core() {
        val text = File("src/main/kotlin/com/htmake/reader/api/YueduApi.kt").readText()
        val cores = listOf(
            "/reader3/getSystemInfo",
            "/reader3/getBookshelf",
            "/reader3/searchBook",
            "/reader3/getBookContent",
            "/reader3/cacheBookSSE",
            "/reader3/exportBook",
            "/reader3/book/tts",
            "/reader3/login",
            "/reader3/backupToWebdav"
        )
        cores.forEach { assertTrue(text.contains(it), "missing route $it") }
        // roughly full surface
        val count = Regex("""/reader3/[a-zA-Z0-9_/{}.-]+""").findAll(text).map { it.value }.toSet().size
        assertTrue(count >= 80, "expected many routes, got $count")
    }

    @Test
    fun umd_utils_unicode_roundtrip_style() {
        // little-endian UTF-16 style used by UMD
        val s = "章节"
        val bytes = ByteArray(s.length * 2)
        for (i in s.indices) {
            val c = s[i].code
            bytes[i * 2] = (c and 0xFF).toByte()
            bytes[i * 2 + 1] = (c shr 8).toByte()
        }
        val back = me.ag2s.umdlib.tool.UmdUtils.unicodeBytesToString(bytes)
        assertEquals(s, back)
    }

    @Test
    fun sourceLogin_extracts_js_from_loginUrl() {
        val src = io.legado.app.data.entities.BookSource(
            bookSourceUrl = "https://x.com",
            loginUrlValue = "@js:java.putLoginHeader('{\"Cookie\":\"a=1\"}')"
        )
        val js = io.legado.app.help.SourceLogin.run {
            src.getLoginJs()
        }
        assertTrue(js!!.contains("putLoginHeader") || js.contains("Cookie"))
    }

    @Test
    fun epub_spine_toc_minimal() {
        val tmp = File.createTempFile("test", ".epub")
        try {
            ZipOutputStream(tmp.outputStream()).use { zos ->
                fun put(name: String, content: String) {
                    zos.putNextEntry(ZipEntry(name))
                    zos.write(content.toByteArray())
                    zos.closeEntry()
                }
                put(
                    "META-INF/container.xml",
                    """<?xml version="1.0"?><container><rootfiles>
                    <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>
                    </rootfiles></container>"""
                )
                put(
                    "OEBPS/content.opf",
                    """<?xml version="1.0"?>
                    <package>
                      <metadata>
                        <dc:title xmlns:dc="http://purl.org/dc/elements/1.1/">Demo Book</dc:title>
                        <dc:creator xmlns:dc="http://purl.org/dc/elements/1.1/">Author</dc:creator>
                      </metadata>
                      <manifest>
                        <item id="c1" href="ch1.xhtml" media-type="application/xhtml+xml"/>
                        <item id="c2" href="ch2.xhtml" media-type="application/xhtml+xml"/>
                        <item id="ncx" href="toc.ncx" media-type="application/x-dtbncx+xml"/>
                      </manifest>
                      <spine toc="ncx">
                        <itemref idref="c1"/><itemref idref="c2"/>
                      </spine>
                    </package>"""
                )
                put(
                    "OEBPS/toc.ncx",
                    """<?xml version="1.0"?><ncx>
                    <navMap>
                      <navPoint><navLabel><text>第一章</text></navLabel><content src="ch1.xhtml"/></navPoint>
                      <navPoint><navLabel><text>第二章</text></navLabel><content src="ch2.xhtml"/></navPoint>
                    </navMap></ncx>"""
                )
                put("OEBPS/ch1.xhtml", "<html><body><p>hello1</p></body></html>")
                put("OEBPS/ch2.xhtml", "<html><body><p>hello2</p></body></html>")
            }
            val book = io.legado.app.data.entities.Book(
                bookUrl = tmp.absolutePath,
                origin = "loc_book"
            )
            val chapters = io.legado.app.model.localBook.EpubFile.getChapterList(book)
            assertEquals(2, chapters.size)
            assertTrue(chapters[0].title.contains("一") || chapters[0].title.contains("ch1"))
            val content = io.legado.app.model.localBook.EpubFile.getContent(book, chapters[0])
            assertTrue(content!!.contains("hello1"))
        } finally {
            tmp.delete()
        }
    }
}
