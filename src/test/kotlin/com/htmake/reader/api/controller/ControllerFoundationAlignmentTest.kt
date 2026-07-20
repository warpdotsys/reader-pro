package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.config.AppConfig
import com.htmake.reader.db.DB
import com.htmake.reader.entity.User
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class ControllerFoundationAlignmentTest {

    @Test
    fun `base controller exposes target jar descriptors`() {
        assertDescriptor(
            BaseController::class.java,
            "saveUserSession",
            "(Lio/vertx/ext/web/RoutingContext;Lcom/htmake/reader/entity/User;" +
                "ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertDescriptor(
            BaseController::class.java,
            "checkAuth",
            "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertDescriptor(
            BaseController::class.java,
            "getUserStorage",
            "(Ljava/lang/Object;[Ljava/lang/String;)Ljava/lang/String;",
        )
        assertDescriptor(
            BaseController::class.java,
            "saveUserStorage",
            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V",
        )
        assertDescriptor(
            BaseController::class.java,
            "limitConcurrent",
            "(IIILkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function2;" +
                "Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertEquals(AppConfig::class.java, BaseController::class.java.getDeclaredMethod("getAppConfig").returnType)
        assertEquals(User::class.java, BaseController::class.java.getDeclaredMethod("getUserInfoClass", String::class.java).returnType)
        assertEquals(
            "mu.KLogger",
            Class.forName("com.htmake.reader.api.controller.BaseControllerKt")
                .getDeclaredMethod("access\$getLogger\$p")
                .returnType.name,
        )
    }

    @Test
    fun `curd defaults map and parse entities like the target jar`() {
        val curd = TestCurd()
        val item = curd.convertToEntity(JsonObject().put("id", 7).put("name", "one"))
        val items = curd.convertToEntityList("[{\"id\":8,\"name\":\"two\"}]")
        val listed = JsonArray().add(JsonObject().put("id", 9))

        assertEquals(Item(7, "one"), item)
        assertEquals(arrayOf(Item(8, "two")).toList(), items.toList())
        assertEquals(listed, curd.onList(listed, "tenant"))
        assertDescriptor(
            CURD::class.java,
            "deleteMulti",
            "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;",
        )
        assertTrue(CURD::class.java.getDeclaredMethod("getEntityClass").returnType == Class::class.java)
        assertTrue(Class.forName("com.htmake.reader.api.controller.CURDKt").declaredFields.any { it.name == "logger" })
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

    private data class Item(val id: Int = 0, val name: String = "")

    private class TestCurd : CURD<Item> {
        override val tableName = "items"
        override val entityClass = Item::class.java

        override fun checker(item: JsonObject, value: Item): Boolean = item.getInteger("id") == value.id

        override suspend fun checkUserAuth(context: RoutingContext): Boolean = true

        override fun getUserNS(context: RoutingContext): String = "tenant"

        override fun beforeSave(value: Item, db: DB<Item>): ReturnData? = null
    }
}
