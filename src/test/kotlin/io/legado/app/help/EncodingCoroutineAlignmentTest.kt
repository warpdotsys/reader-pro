package io.legado.app.help

import io.legado.app.help.coroutine.CompositeCoroutine
import io.legado.app.help.coroutine.Coroutine
import io.legado.app.help.http.EncodeConverter
import io.legado.app.utils.EncodingDetect
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import retrofit2.Converter
import java.lang.reflect.Modifier
import java.nio.charset.Charset
import kotlin.reflect.full.declaredFunctions

class EncodingCoroutineAlignmentTest {

    @Test
    fun encodingDetectRetainsJarNullableMetadata() {
        val getHtmlEncode = EncodingDetect::class.declaredFunctions.single {
            it.name == "getHtmlEncode"
        }
        val getFileBytes = EncodingDetect::class.declaredFunctions.single {
            it.name == "getFileBytes"
        }

        assertTrue(getHtmlEncode.returnType.isMarkedNullable)
        assertTrue(getFileBytes.parameters.last().type.isMarkedNullable)
    }

    @Test
    fun encodingDetectorRetainsJarClassShapeAndCommonResults() {
        assertFalse(Modifier.isPublic(BytesEncodingDetect::class.java.modifiers))
        assertFalse(Modifier.isPublic(Encoding::class.java.modifiers))

        val detector = BytesEncodingDetect()
        assertEquals(Encoding.ASCII, detector.detectEncoding("plain ascii".toByteArray()))
        assertEquals(
            Encoding.UTF8,
            detector.detectEncoding("中文编码检测需要足够的样本。".repeat(12).toByteArray())
        )
        assertEquals(
            "windows-1252",
            EncodingDetectHelp.getHtmlEncode(
                "<html><head><meta charset=\"windows-1252\"></head></html>".toByteArray()
            )
        )
        assertEquals(
            "UTF-16LE",
            EncodingDetectHelp.getJavaEncode(
                byteArrayOf(0xFF.toByte(), 0xFE.toByte(), 'A'.code.toByte(), 0)
            )
        )
    }

    @Test
    fun encodeConverterUsesJvmOverrideAndPreservesExplicitEncoding() {
        val bytes = byteArrayOf(
            0xEF.toByte(),
            0xBB.toByte(),
            0xBF.toByte(),
            'c'.code.toByte(),
            'a'.code.toByte(),
            'f'.code.toByte(),
            0xE9.toByte()
        )
        val factory: Converter.Factory = EncodeConverter("ISO-8859-1")
        val converter = factory.responseBodyConverter(
            String::class.java,
            emptyArray(),
            retrofit2.Retrofit.Builder().baseUrl("https://example.com/").build()
        )

        assertEquals(
            "café",
            converter!!.convert(
                bytes.toResponseBody("text/plain; charset=UTF-8".toMediaType())
            )
        )

        val gbk = Charset.forName("GBK")
        val headerConverter = EncodeConverter().responseBodyConverter(null, null, null)
        assertEquals(
            "编码",
            headerConverter!!.convert(
                "编码".toByteArray(gbk)
                    .toResponseBody("text/plain; charset=GBK".toMediaType())
            )
        )
    }

    @Test
    fun compositeCoroutineMatchesAddDeleteRemoveAndClearSemantics() {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        try {
            val firstGate = CompletableDeferred<Unit>()
            val secondGate = CompletableDeferred<Unit>()
            val thirdGate = CompletableDeferred<Unit>()
            val first = Coroutine(scope) {
                firstGate.await()
            }
            val second = Coroutine(scope) {
                secondGate.await()
            }
            val third = Coroutine(scope) {
                thirdGate.await()
            }
            val composite = CompositeCoroutine(first)

            assertEquals(1, composite.size)
            assertFalse(composite.isEmpty)
            assertFalse(composite.add(first))
            assertTrue(composite.add(second))
            assertTrue(composite.delete(second))
            assertFalse(second.isCancelled)
            assertTrue(composite.add(second))
            assertTrue(composite.remove(first))
            assertTrue(first.isCancelled)
            assertEquals(1, composite.size)

            assertTrue(composite.add(third))
            composite.clear()
            assertTrue(composite.isEmpty)
            assertTrue(second.isCancelled)
            assertTrue(third.isCancelled)
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun coroutineDispatchesSuccessAfterDeferredWorkCompletes() {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        try {
            val gate = CompletableDeferred<Unit>()
            val result = CompletableDeferred<Int>()
            val coroutine = Coroutine(scope) {
                gate.await()
                42
            }.onSuccess {
                result.complete(it)
            }

            gate.complete(Unit)

            kotlinx.coroutines.runBlocking {
                assertEquals(42, withTimeout(5_000) { result.await() })
                withTimeout(5_000) {
                    while (!coroutine.isCompleted) {
                        kotlinx.coroutines.yield()
                    }
                }
            }
        } finally {
            scope.cancel()
        }
    }
}
