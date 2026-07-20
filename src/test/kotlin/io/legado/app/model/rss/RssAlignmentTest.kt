package io.legado.app.model.rss

import io.legado.app.data.entities.RssSource
import io.legado.app.data.entities.BookSource
import io.legado.app.exception.NoStackTraceException
import io.legado.app.model.DebugLog
import io.legado.app.model.Debugger
import io.legado.app.model.analyzeRule.RuleData
import io.legado.app.model.webBook.WebBook
import kotlinx.coroutines.runBlocking
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.xmlpull.v1.XmlPullParserException

class RssAlignmentTest {

    @Test
    fun `restored classes expose target jar descriptors`() {
        assertDescriptor(
            Debugger::class.java,
            "startDebug",
            "(Lio/legado/app/model/webBook/WebBook;Ljava/lang/String;" +
                "Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertDescriptor(
            Rss::class.java,
            "getArticles",
            "(Ljava/lang/String;Ljava/lang/String;Lio/legado/app/data/entities/RssSource;I" +
                "Lio/legado/app/model/DebugLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertDescriptor(
            Rss::class.java,
            "getContent",
            "(Lio/legado/app/data/entities/RssArticle;Ljava/lang/String;" +
                "Lio/legado/app/data/entities/RssSource;Lio/legado/app/model/DebugLog;" +
                "Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"
        )
        assertDescriptor(
            RssParserByRule::class.java,
            "parseXML",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;" +
                "Lio/legado/app/data/entities/RssSource;" +
                "Lio/legado/app/model/analyzeRule/RuleData;Lio/legado/app/model/DebugLog;)" +
                "Lkotlin/Pair;"
        )
        assertDescriptor(
            RssParserDefault::class.java,
            "parseXML",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;" +
                "Lio/legado/app/model/DebugLog;)Lkotlin/Pair;"
        )
        assertTrue(
            Debugger::class.java.getDeclaredMethod("getLogMsg").returnType.name ==
                "kotlin.jvm.functions.Function1"
        )
    }

    @Test
    fun `debugger formats messages and ignores incomplete source logs`() {
        val messages = mutableListOf<String>()
        val debugger = Debugger(messages::add)

        debugger.log(null, "ignored", false)
        debugger.log("source", null, false)
        debugger.log("source", "plain")
        debugger.log("wire")
        debugger.log("source", "<p>Hello</p>", true)

        assertEquals(3, messages.size)
        assertTrue(messages[0].matches(Regex("^\\[\\d{2}:\\d{2}\\.\\d{3}] plain$")))
        assertTrue(messages[1].endsWith(" wire"))
        assertTrue(messages[2].contains("Hello"))
        assertFalse(messages[2].contains("<p>"))
    }

    @Test
    fun `debugger emits the jar content result framing`() = runBlocking {
        val messages = mutableListOf<String>()
        val debugger = Debugger(messages::add)
        val source = BookSource(bookSourceUrl = "https://source.test")
        val chapterUrl = "https://example.test/chapter"

        debugger.startDebug(WebBook(source, debugLog = false), "--$chapterUrl")

        val payloads = messages.map { message ->
            message.replace(Regex("^\\[\\d{2}:\\d{2}\\.\\d{3}] "), "")
        }
        assertEquals(
            listOf(
                "⇒开始访正文页:$chapterUrl",
                "︾开始解析正文页",
                "⇒正文规则为空,使用章节链接: $chapterUrl",
                "┌正文内容",
                "└$chapterUrl",
                "︽正文页解析完成"
            ),
            payloads
        )
    }

    @Test
    fun `default parser preserves the jar malformed provider declaration failure`() {
        val logger = RecordingDebugLog()
        val xml = """
            <rss><channel>
              <item>
                <title> First article </title>
                <link> https://example.test/first </link>
                <description><![CDATA[Summary <img src=" https://img.test/cover.jpg ">]]></description>
                <pubDate> 2026-07-19 </pubDate>
              </item>
              <item><title>Second</title><link>/second</link><time>T2</time></item>
            </channel></rss>
        """.trimIndent()

        val error = assertFailsWith<XmlPullParserException> {
            RssParserDefault.parseXML("News", xml, "feed", logger)
        }

        assertTrue(error.message.orEmpty().contains("No valid parser classes found"))
        assertTrue(error.message.orEmpty().contains("org.kxml2.io.KXmlParser"))
        assertTrue(error.message.orEmpty().contains("org.kxml2.io.KXmlSerializer"))
        assertTrue(logger.entries.isEmpty())
    }

    @Test
    fun `rule parser rejects blank content and falls back when list rule is empty`() {
        val source = RssSource(sourceUrl = "https://example.test/feed")
        val error = assertFailsWith<NoStackTraceException> {
            RssParserByRule.parseXML("News", source.sourceUrl, "  ", source, RuleData(), null)
        }
        assertEquals("error_get_web_content: ${source.sourceUrl}", error.message)

        val logger = RecordingDebugLog()
        val xml = "<rss><channel><item><title>Fallback</title><link>/item</link></item></channel></rss>"
        val parserError = assertFailsWith<XmlPullParserException> {
            RssParserByRule.parseXML(
                "News",
                source.sourceUrl,
                xml,
                source,
                RuleData(),
                logger
            )
        }

        assertTrue(parserError.message.orEmpty().contains("No valid parser classes found"))
        assertEquals("⇒列表规则为空, 使用默认规则解析", logger.entries.first().second)
    }

    private fun assertDescriptor(type: Class<*>, name: String, expected: String) {
        val descriptors = type.declaredMethods
            .filter { it.name == name }
            .map(::descriptor)
        assertTrue(expected in descriptors, "$type.$name descriptors were $descriptors")
    }

    private fun descriptor(method: java.lang.reflect.Method): String =
        method.parameterTypes.joinToString(separator = "", prefix = "(", postfix = ")") {
            descriptor(it)
        } + descriptor(method.returnType)

    private fun descriptor(type: Class<*>): String = when {
        type === Void.TYPE -> "V"
        type === Boolean::class.javaPrimitiveType -> "Z"
        type === Byte::class.javaPrimitiveType -> "B"
        type === Char::class.javaPrimitiveType -> "C"
        type === Short::class.javaPrimitiveType -> "S"
        type === Int::class.javaPrimitiveType -> "I"
        type === Long::class.javaPrimitiveType -> "J"
        type === Float::class.javaPrimitiveType -> "F"
        type === Double::class.javaPrimitiveType -> "D"
        type.isArray -> "[${descriptor(type.componentType)}"
        else -> "L${type.name.replace('.', '/')};"
    }

    private class RecordingDebugLog : DebugLog {
        val entries = mutableListOf<Triple<String?, String?, Boolean>>()

        override fun log(sourceUrl: String?, msg: String?, isHtml: Boolean) {
            entries += Triple(sourceUrl, msg, isHtml)
        }
    }
}
