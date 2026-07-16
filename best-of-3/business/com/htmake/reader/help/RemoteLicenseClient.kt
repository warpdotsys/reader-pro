package com.htmake.reader.help

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Optional remote license activation against vendor API.
 * Default endpoint mirrors jar: https://r.htmake.com/reader3/activateLicense
 */
object RemoteLicenseClient {
    const val DEFAULT_URL = "https://r.htmake.com/reader3/activateLicense"

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * POST JSON body to remote; returns response body or error map.
     * Does not throw — callers use local activate as fallback.
     */
    fun activateRemote(bodyJson: String, url: String = DEFAULT_URL): Map<String, Any?> {
        return try {
            val req = Request.Builder()
                .url(url)
                .post(bodyJson.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull()))
                .header("User-Agent", "reader-pro-rebuild/3.2.14")
                .build()
            client.newCall(req).execute().use { resp ->
                val text = resp.body?.string().orEmpty()
                mapOf(
                    "ok" to resp.isSuccessful,
                    "httpCode" to resp.code,
                    "body" to text.take(2000),
                    "url" to url
                )
            }
        } catch (e: Exception) {
            mapOf("ok" to false, "error" to (e.message ?: "network"), "url" to url)
        }
    }
}
