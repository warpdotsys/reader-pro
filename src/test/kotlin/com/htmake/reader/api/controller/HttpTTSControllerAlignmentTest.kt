package com.htmake.reader.api.controller

import com.htmake.reader.db.DB
import io.legado.app.data.entities.HttpTTS
import io.vertx.core.json.JsonObject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import sun.misc.Unsafe

class HttpTTSControllerAlignmentTest {

    @Test
    fun `descriptors match the target jar`() {
        assertDescriptor(
            HttpTTSController::class.java,
            "convertToEntity",
            "(Lio/vertx/core/json/JsonObject;)Lio/legado/app/data/entities/HttpTTS;",
        )
        assertDescriptor(
            HttpTTSController::class.java,
            "convertToEntityList",
            "(Ljava/lang/String;)[Lio/legado/app/data/entities/HttpTTS;",
        )
        listOf("list", "save", "saveMulti", "delete", "deleteMulti").forEach { method ->
            assertDescriptor(
                HttpTTSController::class.java,
                method,
                "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            )
        }
    }

    @Test
    fun `parsing validation and duplicate identity match the target jar`() {
        val controller = uninitialized<HttpTTSController>()
        val parsed = controller.convertToEntity(JsonObject().put("name", "voice").put("url", "https://tts.example"))
        val parsedList = controller.convertToEntityList(
            """[{"name":"voice","url":"https://tts.example"}]""",
        )

        assertEquals("voice", parsed.name)
        assertEquals("https://tts.example", parsed.url)
        assertEquals(1, parsedList.size)
        assertEquals("voice", parsedList.single().name)
        assertEquals("https://tts.example", parsedList.single().url)
        assertTrue(controller.checker(JsonObject().put("name", "voice"), parsed))
        assertFalse(controller.checker(JsonObject().put("name", "other"), parsed))
        assertEquals(
            "名称不能为空",
            controller.beforeSave(HttpTTS(), DB("tenant", "httpTTS"))?.errorMsg,
        )
        assertEquals(
            "链接不能为空",
            controller.beforeSave(HttpTTS(name = "voice"), DB("tenant", "httpTTS"))?.errorMsg,
        )
        assertNull(
            controller.beforeSave(HttpTTS(name = "voice", url = "https://tts.example"), DB("tenant", "httpTTS")),
        )
    }

    private fun assertDescriptor(type: Class<*>, name: String, expected: String) {
        val descriptors = type.declaredMethods
            .filter { it.name == name }
            .map(::descriptor)
        assertTrue(expected in descriptors, "$type.$name descriptors were $descriptors")
    }

    private fun descriptor(method: java.lang.reflect.Method): String =
        method.parameterTypes.joinToString(separator = "", prefix = "(", postfix = ")") { descriptor(it) } +
            descriptor(method.returnType)

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

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T> uninitialized(): T =
        unsafe.allocateInstance(T::class.java) as T

    private companion object {
        val unsafe: Unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").run {
            isAccessible = true
            get(null) as Unsafe
        }
    }
}
