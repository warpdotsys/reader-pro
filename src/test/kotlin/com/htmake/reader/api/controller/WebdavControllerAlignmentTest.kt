package com.htmake.reader.api.controller

import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class WebdavControllerAlignmentTest {
    @Test
    fun `webdav controller exposes target methods`() {
        assertTrue(WebdavController::class.java.constructors.any { it.parameterTypes.size == 3 })
        assertTrue(WebdavController::class.java.declaredMethods.any { it.name == "checkAuthorization" && it.returnType == Boolean::class.javaPrimitiveType })
        listOf("webdavList", "webdavMkdir", "webdavUpload", "webdavDownload", "webdavDelete", "webdavMove", "webdavCopy", "webdavLock", "webdavUnLock", "backupToWebdav").forEach { method ->
            assertTrue(WebdavController::class.java.declaredMethods.any { it.name == method })
        }
    }
}
