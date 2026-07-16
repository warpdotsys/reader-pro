package com.htmake.reader.api

import com.htmake.reader.utils.ExtKt
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.io.File

/**
 * Generate OpenAPI 3.0 document from registered /reader3 routes.
 */
object OpenApiDocs {

    /** Load paths from classpath fixture or scan YueduApi source if present. */
    fun loadPaths(): List<String> {
        val fromCp = javaClass.classLoader.getResourceAsStream("reader3-routes.txt")
            ?.bufferedReader()?.readLines()
            ?.map { it.trim() }
            ?.filter { it.startsWith("/reader3/") }
        if (!fromCp.isNullOrEmpty()) return fromCp.distinct().sorted()

        val src = File("src/main/kotlin/com/htmake/reader/api/YueduApi.kt")
        if (src.isFile) {
            val re = Regex(""""(/reader3/[^"]+)"""")
            return re.findAll(src.readText()).map { it.groupValues[1] }.distinct().sorted().toList()
        }
        val fixture = File("src/test/resources/reader3-routes.txt")
        if (fixture.isFile) {
            return fixture.readLines().map { it.trim() }.filter { it.startsWith("/reader3/") }.distinct().sorted()
        }
        return emptyList()
    }

    fun openApiJson(version: String = "3.2.14-rebuild"): JsonObject {
        val paths = JsonObject()
        loadPaths().forEach { p ->
            val item = JsonObject()
            // most endpoints accept GET and/or POST in this app
            val getOp = JsonObject()
                .put("summary", p.removePrefix("/reader3/"))
                .put("tags", JsonArray().add(tagOf(p)))
                .put(
                    "responses",
                    JsonObject().put(
                        "200",
                        JsonObject().put("description", "ReturnData JSON")
                    )
                )
            item.put("get", getOp)
            item.put("post", JsonObject(getOp.encode()))
            paths.put(p, item)
        }
        return JsonObject()
            .put("openapi", "3.0.3")
            .put(
                "info",
                JsonObject()
                    .put("title", "reader-pro rebuild API")
                    .put("version", version)
                    .put("description", "Auto-generated from /reader3 route table. Auth: session or accessToken=user:token when secure=true.")
            )
            .put("servers", JsonArray().add(JsonObject().put("url", "/")))
            .put("paths", paths)
            .put(
                "components",
                JsonObject().put(
                    "securitySchemes",
                    JsonObject().put(
                        "accessToken",
                        JsonObject()
                            .put("type", "apiKey")
                            .put("in", "query")
                            .put("name", "accessToken")
                    )
                )
            )
    }

    fun markdownIndex(paths: List<String> = loadPaths()): String {
        val sb = StringBuilder()
        sb.appendLine("# reader-pro API Routes")
        sb.appendLine()
        sb.appendLine("Generated from route table, **${paths.size}** unique `/reader3/*` paths.")
        sb.appendLine()
        sb.appendLine("| Path | Tag |")
        sb.appendLine("|------|-----|")
        paths.forEach { p ->
            sb.appendLine("| `$p` | ${tagOf(p)} |")
        }
        sb.appendLine()
        sb.appendLine("Machine-readable: `GET /reader3/openapi.json`")
        return sb.toString()
    }

    fun htmlDocs(): String {
        val paths = loadPaths()
        val rows = paths.joinToString("\n") { p ->
            "<tr><td><code>$p</code></td><td>${tagOf(p)}</td></tr>"
        }
        return """
            <!DOCTYPE html>
            <html><head><meta charset="utf-8"/><title>reader-pro API</title>
            <style>
              body{font-family:system-ui,sans-serif;margin:24px;background:#fafafa;color:#222}
              h1{font-size:1.4rem} table{border-collapse:collapse;width:100%;background:#fff}
              td,th{border:1px solid #ddd;padding:6px 10px;font-size:13px}
              th{background:#f0f0f0;text-align:left} code{font-size:12px}
              a{color:#06c}
            </style></head><body>
            <h1>reader-pro rebuild API</h1>
            <p>${paths.size} routes · <a href="/reader3/openapi.json">openapi.json</a> · <a href="/web/">Web UI</a></p>
            <table><thead><tr><th>Path</th><th>Tag</th></tr></thead>
            <tbody>$rows</tbody></table>
            </body></html>
        """.trimIndent()
    }

    private fun tagOf(path: String): String {
        val name = path.removePrefix("/reader3/")
        return when {
            name.contains("Rss", true) || name.contains("rss") -> "rss"
            name.contains("Webdav", true) || name.contains("webdav") || name.contains("Mongo", true) || name.contains("Backup", true) -> "backup"
            name.contains("License", true) || name.contains("activate", true) -> "license"
            name.contains("User", true) || name.contains("login", true) || name.contains("logout", true) -> "user"
            name.contains("Source", true) || name.contains("source", true) -> "bookSource"
            name.contains("Group", true) || name.contains("Bookmark", true) || name.contains("Replace", true) -> "rules"
            name.contains("TTS", true) || name.contains("tts", true) || name.contains("Speak", true) -> "tts"
            name.contains("webdav", true) -> "webdav"
            name.contains("File", true) || name.contains("upload", true) || name.contains("assets", true) -> "file"
            else -> "book"
        }
    }
}
