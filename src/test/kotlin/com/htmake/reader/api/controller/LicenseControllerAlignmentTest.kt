package com.htmake.reader.api.controller

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test

class LicenseControllerAlignmentTest {
    @Test
    fun `license controller exposes target methods`() {
        assertTrue(LicenseController::class.java.constructors.any { it.parameterTypes.size == 1 })
        assertEquals(Array<String>::class.java, LicenseController::class.java.getMethod("getBackupFileNames").returnType)
        listOf("getLicense", "importLicense", "generateKeys", "generateLicense", "isHostValid", "decryptLicense", "activateLicense", "isLicenseValid", "checkLicense", "sendCodeToEmail", "supplyLicense").forEach { name ->
            assertTrue(LicenseController::class.java.declaredMethods.any { it.name == name })
        }
    }
}
