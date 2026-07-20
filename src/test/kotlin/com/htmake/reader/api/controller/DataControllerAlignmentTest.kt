package com.htmake.reader.api.controller

import com.htmake.reader.db.DB
import io.legado.app.data.entities.BookGroup
import io.legado.app.data.entities.Bookmark
import io.legado.app.data.entities.ReplaceRule
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import sun.misc.Unsafe

class DataControllerAlignmentTest {

    @Test
    fun `data controller descriptors match the target jar`() {
        assertDescriptor(
            BookGroupController::class.java,
            "onList",
            "(Lio/vertx/core/json/JsonArray;Ljava/lang/String;)Lio/vertx/core/json/JsonArray;",
        )
        assertDescriptor(
            RssSourceController::class.java,
            "getRssSourceByURL",
            "(Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/RssSource;",
        )
        listOf(
            RssSourceController::class.java to "getRssSources",
            RssSourceController::class.java to "saveRssSource",
            RssSourceController::class.java to "saveRssSources",
            RssSourceController::class.java to "deleteRssSource",
            RssSourceController::class.java to "getRssArticles",
            RssSourceController::class.java to "getRssContent",
            BookGroupController::class.java to "saveBookGroupOrder",
        ).forEach { (type, method) ->
            assertDescriptor(
                type,
                method,
                "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
            )
        }
    }

    @Test
    fun `book groups allocate the first unused positive bit and next order`() {
        val controller = uninitialized<BookGroupController>()
        val groups = JsonArray()
            .add(JsonObject().put("groupId", -1).put("order", 9))
            .add(JsonObject().put("groupId", 1).put("order", 3))
            .add(JsonObject().put("groupId", 4).put("order", 12))
        val candidate = BookGroup(groupName = "custom")

        controller.onCheckEnd(candidate, false, groups)

        assertEquals(2, candidate.groupId)
        assertEquals(13, candidate.order)
        assertTrue(controller.checker(JsonObject().put("groupId", 2), candidate))
        assertFalse(controller.checker(JsonObject().put("groupId", 3), candidate))
    }

    @Test
    fun `bookmark and replacement rule checks use the jar identities and validation`() {
        val bookmarkController = uninitialized<BookmarkController>()
        val ruleController = uninitialized<ReplaceRuleController>()

        val bookmark = Bookmark(time = 123L)
        assertTrue(bookmarkController.checker(JsonObject().put("time", 123L), bookmark))
        assertFalse(bookmarkController.checker(JsonObject().put("time", 124L), bookmark))
        assertEquals(
            "书签信息错误",
            bookmarkController.beforeSave(Bookmark(), DB("tenant", "bookmark"))?.errorMsg,
        )
        assertNull(
            bookmarkController.beforeSave(Bookmark(bookName = "book"), DB("tenant", "bookmark")),
        )

        val rule = ReplaceRule(name = "clean", pattern = "foo")
        assertTrue(ruleController.checker(JsonObject().put("name", "clean"), rule))
        assertFalse(ruleController.checker(JsonObject().put("name", "other"), rule))
        assertEquals(
            "名称不能为空",
            ruleController.beforeSave(ReplaceRule(), DB("tenant", "replaceRule"))?.errorMsg,
        )
        assertEquals(
            "规则不能为空",
            ruleController.beforeSave(ReplaceRule(name = "clean"), DB("tenant", "replaceRule"))?.errorMsg,
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
