package com.htmake.reader.api.controller

import io.vertx.core.json.JsonArray
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class BookSourceControllerAlignmentTest {

    @Test
    fun `book source controller exposes the target jar contract`() {
        assertEquals(
            "io.vertx.ext.web.client.WebClient",
            BookSourceController::class.java.getDeclaredField("webClient").type.name,
        )
        assertDescriptor(
            BookSourceController::class.java,
            "getUserBookSourceJsonOpt",
            "(Ljava/lang/String;Ljava/util/Set;Ljava/util/Set;)Lio/vertx/core/json/JsonArray;",
        )
        assertDescriptor(
            BookSourceController::class.java,
            "saveBookSources",
            "(Lio/vertx/ext/web/RoutingContext;Lio/vertx/core/json/JsonArray;)Lcom/htmake/reader/api/ReturnData;",
        )
        assertDescriptor(
            BookSourceController::class.java,
            "saveUserBookSources",
            "(Ljava/lang/String;Lcom/htmake/reader/entity/User;Lio/vertx/core/json/JsonArray;)Lcom/htmake/reader/api/ReturnData;",
        )
        assertDescriptor(
            BookSourceController::class.java,
            "generateBookSourceMap",
            "(Ljava/lang/String;Lio/vertx/core/json/JsonArray;)Ljava/util/Map;",
        )
        listOf(
            "saveBookSource",
            "saveBookSources",
            "getBookSource",
            "getBookSources",
            "deleteBookSource",
            "deleteBookSources",
            "deleteAllBookSources",
            "setAsDefaultBookSources",
            "readSourceFile",
            "deleteUserBookSource",
            "deleteBookSourcesFile",
        ).forEach { method ->
            assertDescriptor(
                BookSourceController::class.java,
                method,
                "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            )
        }
        assertDescriptor(
            BookSourceController::class.java,
            "saveFromRemoteSource",
            "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertDescriptor(
            BookSourceController::class.java,
            "updateRemoteSourceSub",
            "(Ljava/lang/String;Lcom/htmake/reader/entity/User;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
    }

    @Test
    fun `book source map defaults and Kotlin facade are retained`() {
        assertTrue(
            BookSourceController::class.java.declaredMethods.any {
                it.name == "generateBookSourceMap\$default" && it.parameterTypes[1] == String::class.java
            },
        )
        assertTrue(
            BookSourceController::class.java.declaredMethods.any {
                it.name == "getUserBookSourceJsonOpt\$default"
            },
        )
        assertEquals(
            "mu.KLogger",
            Class.forName("com.htmake.reader.api.controller.BookSourceControllerKt")
                .getDeclaredMethod("access\$getLogger\$p")
                .returnType.name,
        )
        assertEquals(JsonArray::class.java, BookSourceController::class.java.getDeclaredMethod(
            "getUserBookSourceJson",
            String::class.java,
        ).returnType)
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
}
