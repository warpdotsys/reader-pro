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
    fun loginUi_parse_array_and_csv() {
        val fields = io.legado.app.help.SourceLogin.parseLoginUi(
            """[{"name":"username","type":"text"},{"name":"password","type":"password"}]"""
        )
        assertEquals(2, fields.size)
        assertEquals("password", fields[1].type)
        val csv = io.legado.app.help.SourceLogin.parseLoginUi("user,pwd")
        assertEquals(2, csv.size)
    }

    @Test
    fun loginUi_payload_and_form_login() {
        val src = io.legado.app.data.entities.BookSource(
            bookSourceUrl = "https://login.example",
            bookSourceName = "demo",
            loginUiValue = """[{"name":"username","type":"text"},{"name":"password","type":"password"}]""",
            loginUrlValue = """@js:java.putLoginHeader(JSON.stringify({Cookie: username+"="+password}))"""
        )
        src.setUserNameSpace("test-login")
        val payload = io.legado.app.help.SourceLogin.getLoginUiPayload(src)
        @Suppress("UNCHECKED_CAST")
        val fields = payload["fields"] as List<*>
        assertEquals(2, fields.size)
        // form login with rhino (username/password bound as top-level names)
        val result = io.legado.app.help.SourceLogin.loginWithForm(
            src, mapOf("username" to "u1", "password" to "p1")
        )
        // may or may not set header depending on JS engine; putLoginInfo must work
        assertTrue(src.getLoginInfo() != null || result["loginInfoKeys"] != null)
        assertTrue(io.legado.app.help.SourceLogin.getLoginInfoMap(src).containsKey("username")
            || src.getLoginInfo()?.contains("u1") == true)
    }

    @Test
    fun umd_wrong_header_throws() {
        val tmp = java.io.File.createTempFile("bad", ".umd")
        try {
            tmp.writeBytes(byteArrayOf(1, 2, 3, 4, 5, 6, 7, 8))
            try {
                java.io.FileInputStream(tmp).use { me.ag2s.umdlib.umd.UmdReader().read(it) }
                assertTrue(false, "should throw")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("header", true) == true || e is IllegalStateException)
            }
        } finally {
            tmp.delete()
        }
    }

    @Test
    fun umd_write_read_golden_roundtrip() {
        val book = me.ag2s.umdlib.domain.UmdBook()
        book.header.title = "测试书"
        book.header.author = "作者甲"
        book.header.bookType = "玄幻"
        book.chapters.addChapter("第一章", "正文内容甲\n第二行")
        book.chapters.addChapter("第二章", "正文内容乙")
        book.cover.coverData = byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte(), 0xD9.toByte()) // mini jpeg-ish

        val tmp = java.io.File.createTempFile("golden", ".umd")
        try {
            java.io.FileOutputStream(tmp).use { book.buildUmd(it) }
            assertTrue(tmp.length() > 32)

            val loaded = java.io.FileInputStream(tmp).use { me.ag2s.umdlib.umd.UmdReader().read(it) }
            assertEquals("测试书", loaded.header.title)
            assertEquals("作者甲", loaded.header.author)
            assertEquals(2, loaded.chapters.getTitles().size)
            assertEquals("第一章", loaded.chapters.getTitle(0))
            assertEquals("第二章", loaded.chapters.getTitle(1))
            assertTrue(loaded.chapters.getContentString(0).contains("正文内容甲"))
            assertTrue(loaded.chapters.getContentString(1).contains("正文内容乙"))
            assertTrue(loaded.cover.coverData != null && loaded.cover.coverData!!.isNotEmpty())

            // UmdFile integration
            val entity = io.legado.app.data.entities.Book(
                bookUrl = tmp.absolutePath,
                origin = "loc_book",
                name = "",
                author = ""
            )
            val chapters = io.legado.app.model.localBook.UmdFile.getChapterList(entity)
            assertEquals(2, chapters.size)
            assertEquals("测试书", entity.name)
            val body = io.legado.app.model.localBook.UmdFile.getContent(entity, chapters[0])
            assertTrue(body!!.contains("正文内容甲"))
        } finally {
            tmp.delete()
        }
    }

    @Test
    fun route_contract_fixture_matches_yuedu_api() {
        val api = File("src/main/kotlin/com/htmake/reader/api/YueduApi.kt").readText()
        val fixture = File("src/test/resources/reader3-routes.txt").readLines()
            .map { it.trim() }.filter { it.startsWith("/reader3/") }.toSet()
        assertTrue(fixture.size >= 100, "fixture too small: ${fixture.size}")
        fixture.forEach { path ->
            assertTrue(api.contains("\"$path\""), "YueduApi missing fixture route $path")
        }
        // critical loginUi routes
        listOf("/reader3/getLoginUi", "/reader3/loginBookSource", "/reader3/logoutBookSource").forEach {
            assertTrue(it in fixture, "fixture missing $it")
        }
    }

    @Test
    fun umd_utils_string_unicode_roundtrip() {
        val s = "Hello中文\u2029line"
        val bytes = me.ag2s.umdlib.tool.UmdUtils.stringToUnicodeBytes(s)
        assertEquals(s, me.ag2s.umdlib.tool.UmdUtils.unicodeBytesToString(bytes))
        val raw = "abcdef".toByteArray()
        val c = me.ag2s.umdlib.tool.UmdUtils.compress(raw)
        assertTrue(c.isNotEmpty())
        assertTrue(me.ag2s.umdlib.tool.UmdUtils.decompress(c).contentEquals(raw))
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

    @Test
    fun contentProcessor_regex_and_book_filter() {
        val book = io.legado.app.data.entities.Book(name = "三体")
        val rules = listOf(
            io.legado.app.help.ContentProcessor.ReplaceRule(
                pattern = "广告",
                replacement = "",
                isRegex = false,
                scope = "content"
            ),
            io.legado.app.help.ContentProcessor.ReplaceRule(
                pattern = "\\s{2,}",
                replacement = " ",
                isRegex = true,
                scope = "all",
                bookName = "regex:三."
            ),
            io.legado.app.help.ContentProcessor.ReplaceRule(
                pattern = "X",
                replacement = "Y",
                isRegex = false,
                bookName = "无关书"
            )
        )
        val out = io.legado.app.help.ContentProcessor.applyRules(
            rules, book, "广告  正文  广告 X", "content"
        )
        assertFalse(out.contains("广告"))
        assertTrue(out.contains("正文"))
        assertTrue(out.contains("X")) // filtered by bookName
        assertTrue(
            io.legado.app.help.ContentProcessor.matchesBook(
                io.legado.app.help.ContentProcessor.ReplaceRule(bookName = "/三体|球状/"),
                book
            )
        )
    }

    @Test
    fun htmlFormatter_keep_img() {
        val html = """<p>hi</p><br/><img src="a.jpg" alt="x"/><div>end</div>"""
        val plain = io.legado.app.utils.HtmlFormatter.format(html)
        assertTrue(plain.contains("hi"))
        assertFalse(plain.contains("<"))
        val keep = io.legado.app.utils.HtmlFormatter.formatKeepImg(html)
        assertTrue(keep.contains("<img"))
        assertTrue(keep.contains("a.jpg"))
        assertFalse(keep.contains("<div"))
    }

    @Test
    fun textFile_chapter_split() {
        val tmp = File.createTempFile("novel", ".txt")
        try {
            tmp.writeText(
                """
                前言废话
                第一章 开始
                内容一
                第二章 继续
                内容二
                """.trimIndent(),
                Charsets.UTF_8
            )
            val book = io.legado.app.data.entities.Book(
                bookUrl = tmp.absolutePath,
                origin = "loc_book",
                name = "测试",
                tocUrl = """^(第[0-9零一二三四五六七八九十百千]+章.*)$"""
            )
            val chapters = io.legado.app.model.localBook.TextFile(book).getChapterList()
            assertTrue(chapters.size >= 2, "chapters=${chapters.map { it.title }}")
            val c0 = io.legado.app.model.localBook.TextFile(book).getContent(chapters.first { it.title.contains("一") })
            assertTrue(c0!!.contains("内容一") || c0.contains("第一章"))
        } finally {
            tmp.delete()
        }
    }

    @Test
    fun webdav_paths_safe_and_dest() {
        val home = File.createTempFile("wdh", "home").apply { delete(); mkdirs() }
        try {
            val f = com.htmake.reader.api.controller.WebdavPaths.resolveUnderHome(home, "/a/b.txt")
            assertTrue(f.absolutePath.startsWith(home.canonicalPath))
            try {
                com.htmake.reader.api.controller.WebdavPaths.resolveUnderHome(home, "/../etc/passwd")
                assertTrue(false, "should reject traversal")
            } catch (e: Exception) {
                assertTrue(e.message?.contains("非法") == true)
            }
            val rel = com.htmake.reader.api.controller.WebdavPaths.destinationToRelativePath(
                "http://host/reader3/webdav/backup/x.zip"
            )
            assertTrue(rel!!.contains("backup") || rel.contains("x.zip"))
            val p = com.htmake.reader.api.controller.WebdavPaths.pathFromRequest("/reader3/webdav/foo//bar")
            assertEquals("/foo/bar", p)
        } finally {
            home.deleteRecursively()
        }
    }

    @Test
    fun cookie_store_merge_and_acache_expire() {
        val ns = "test-cookie-${System.currentTimeMillis()}"
        val store = io.legado.app.help.http.CookieStore(ns)
        store.setCookie("https://a.example.com/path", "a=1")
        store.replaceCookie("https://a.example.com/x", "b=2; a=3")
        val c = store.getCookie("https://www.example.com/")
        assertTrue(c.contains("a=3") || c.contains("a=1"))
        assertTrue(c.contains("b=2"))
        store.applySetCookie("https://example.com", listOf("session=xyz; Path=/", "x=1"))
        assertTrue(store.getCookie("https://example.com").contains("session=xyz"))

        val dir = File(com.htmake.reader.utils.ExtKt.getWorkDir("storage", "cache", "t-acache"))
        dir.deleteRecursively()
        val cache = io.legado.app.utils.ACache.get(dir)
        cache.put("k", "v", saveTimeSec = 1)
        assertEquals("v", cache.getAsString("k"))
        Thread.sleep(1100)
        assertEquals(null, cache.getAsString("k"))
        dir.deleteRecursively()
    }

    @Test
    fun defaultData_loads_txt_toc_rules() {
        val rules = io.legado.app.help.DefaultData.txtTocRules
        assertTrue(rules.size >= 3, "expected bundled rules, got ${rules.size}")
        assertTrue(rules.any { it.rule.isNotBlank() })
    }

    @Test
    fun edgeTts_ssml_escapes() {
        val ssml = com.htmake.reader.lib.tts.EdgeTts.buildSsml("a<b>&c", "zh-CN-XiaoxiaoNeural", "+10%", "0%")
        assertTrue(ssml.contains("&lt;"))
        assertTrue(ssml.contains("&amp;"))
        assertTrue(ssml.contains("zh-CN-XiaoxiaoNeural"))
        assertFalse(ssml.contains("<b>"))
    }

    @Test
    fun analyzeUrl_parses_option_and_expands_key() {
        val au = io.legado.app.model.analyzeRule.AnalyzeUrl(
            mUrl = """https://example.com/search?q={{key}},{"method":"POST","body":{"q":"{{key}}","p":"{{page}}"},"headers":{"X-Test":"1"},"charset":"UTF-8"}""",
            key = "三体",
            page = 2,
            baseUrl = "https://example.com/"
        )
        assertTrue(au.finalUrl.contains("example.com"))
        assertTrue(au.finalUrl.contains("%") || au.finalUrl.contains("三体") || au.finalUrl.contains("search"))
        assertEquals("POST", au.method.uppercase())
        assertTrue(au.getFieldMap().containsKey("q"))
        assertEquals("2", au.getFieldMap()["p"])
        assertTrue(au.getHeaderMap().containsKey("X-Test") || au.getHeaderMap().keys.any { it.equals("X-Test", true) })
        // relative + base
        val rel = io.legado.app.model.analyzeRule.AnalyzeUrl(
            mUrl = "/book/1.html",
            baseUrl = "https://host.com/a/"
        )
        assertTrue(rel.finalUrl.startsWith("https://host.com"))
    }

    @Test
    fun cbz_natural_sort_and_comicinfo() {
        val tmp = File.createTempFile("comic", ".cbz")
        try {
            ZipOutputStream(tmp.outputStream()).use { zos ->
                fun put(name: String, bytes: ByteArray) {
                    zos.putNextEntry(ZipEntry(name))
                    zos.write(bytes)
                    zos.closeEntry()
                }
                // tiny 1x1 png
                val png = java.util.Base64.getDecoder().decode(
                    "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg=="
                )
                put("pages/page10.png", png)
                put("pages/page2.png", png)
                put(
                    "ComicInfo.xml",
                    """<?xml version="1.0"?><ComicInfo><Title>漫画甲</Title><Writer>作者乙</Writer><Genre>冒险</Genre></ComicInfo>""".toByteArray()
                )
            }
            val book = io.legado.app.data.entities.Book(bookUrl = tmp.absolutePath, origin = "loc_book")
            val chapters = io.legado.app.model.localBook.CbzFile.getChapterList(book)
            assertEquals(2, chapters.size)
            // natural order: page2 before page10
            assertTrue(chapters[0].url.contains("page2"), chapters[0].url)
            assertTrue(chapters[1].url.contains("page10"), chapters[1].url)
            assertEquals("漫画甲", book.name)
            assertEquals("作者乙", book.author)
            val img = io.legado.app.model.localBook.CbzFile.getImage(book, chapters[0].url)
            assertTrue(img != null && img.isNotEmpty())
            val media = io.legado.app.model.localBook.LocalMedia.getChapterImage(book, chapters[0])
            assertTrue(media != null && media.isNotEmpty())
        } finally {
            tmp.delete()
        }
    }

    @Test
    fun pdf_text_and_page_image() {
        // minimal PDF with one page of text via PDFBox
        val tmp = File.createTempFile("doc", ".pdf")
        try {
            org.apache.pdfbox.pdmodel.PDDocument().use { doc ->
                val page = org.apache.pdfbox.pdmodel.PDPage()
                doc.addPage(page)
                org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page).use { cs ->
                    cs.beginText()
                    cs.setFont(org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA, 12f)
                    cs.newLineAtOffset(50f, 700f)
                    cs.showText("Hello PDF Page")
                    cs.endText()
                }
                doc.documentInformation.title = "PDF书"
                doc.documentInformation.author = "PDF作者"
                doc.save(tmp)
            }
            val book = io.legado.app.data.entities.Book(bookUrl = tmp.absolutePath, origin = "loc_book")
            val chapters = io.legado.app.model.localBook.PdfFile.getChapterList(book)
            assertEquals(1, chapters.size)
            assertEquals("PDF书", book.name)
            val content = io.legado.app.model.localBook.PdfFile.getContent(book, chapters[0])
            assertTrue(content!!.contains("Hello PDF") || content.contains("PDF"))
            val jpg = io.legado.app.model.localBook.PdfFile.getPageImage(book, 0, 200f)
            assertTrue(jpg != null && jpg.size > 100)
        } finally {
            tmp.delete()
        }
    }
}


