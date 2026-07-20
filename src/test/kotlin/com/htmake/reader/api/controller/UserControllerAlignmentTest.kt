package com.htmake.reader.api.controller

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class UserControllerAlignmentTest {
    @Test
    fun `user controller exposes the target jar descriptors`() {
        assertEquals(Int::class.javaPrimitiveType, UserController::class.java.getDeclaredField("userMaxCount").type)
        listOf(
            "login", "logout", "getUserList", "addUser", "resetPassword", "deleteUsers",
            "clearInactiveUsers", "updateUser", "getUserInfo", "saveUserConfig", "getUserConfig",
            "uploadFile", "deleteFile", "downloadBackupFile",
        ).forEach { method ->
            assertTrue(UserController::class.java.declaredMethods.any { candidate ->
                candidate.name == method && candidate.parameterTypes.firstOrNull()?.name == "io.vertx.ext.web.RoutingContext"
            })
        }
        assertTrue(UserController::class.java.declaredMethods.any { it.name == "clearInactiveUsers" && it.parameterTypes.firstOrNull() == Int::class.javaPrimitiveType })
        assertTrue(UserController::class.java.declaredMethods.any { it.name == "forEachUser" && it.parameterTypes.size == 2 })
        assertEquals(
            "mu.KLogger",
            Class.forName("com.htmake.reader.api.controller.UserControllerKt").getDeclaredMethod("access\$getLogger\$p").returnType.name,
        )
    }
}
