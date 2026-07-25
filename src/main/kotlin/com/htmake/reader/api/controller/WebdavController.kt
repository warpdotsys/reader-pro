package com.htmake.reader.api.controller

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.asJsonObject
import com.htmake.reader.utils.deleteRecursively
import com.htmake.reader.utils.genEncryptedPassword
import com.htmake.reader.utils.getStorage
import com.htmake.reader.utils.gson
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Base64
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mu.KotlinLogging
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.logger {}

class WebdavController(
    coroutineContext: CoroutineContext,
    router: Router,
    onHandlerError: (RoutingContext, Exception) -> Unit,
) : BaseController(coroutineContext) {
    init {
        router.route("/reader3/webdav*").handler { context ->
            configureResponse(context)
            val method = context.request().rawMethod()
            if (!checkAuthorization(context)) {
                if (method in setOf(
                        "PROPFIND",
                        "MKCOL",
                        "PUT",
                        "GET",
                        "DELETE",
                        "MOVE",
                        "COPY",
                        "LOCK",
                        "UNLOCK",
                    ) ||
                    (method == "OPTIONS" && context.request().getHeader("Authorization") != null)
                ) {
                    context.response().setStatusCode(401).end()
                    return@handler
                }
            }

            when (method) {
                "PROPFIND" -> launchWebdav(context, onHandlerError) { webdavList(context) }
                "MKCOL" -> launchWebdav(context, onHandlerError) { webdavMkdir(context) }
                "PUT" -> launchWebdav(context, onHandlerError) { webdavUpload(context) }
                "GET" -> launchWebdav(context, onHandlerError) { webdavDownload(context) }
                "DELETE" -> launchWebdav(context, onHandlerError) { webdavDelete(context) }
                "MOVE" -> launchWebdav(context, onHandlerError) { webdavMove(context) }
                "COPY" -> launchWebdav(context, onHandlerError) { webdavCopy(context) }
                "LOCK" -> launchWebdav(context, onHandlerError) { webdavLock(context) }
                "UNLOCK" -> launchWebdav(context, onHandlerError) { webdavUnLock(context) }
                "OPTIONS" -> context.response().setStatusCode(200).end()
                else -> context.response().setStatusCode(405).end()
            }
        }
    }

    fun checkAuthorization(context: RoutingContext): Boolean {
        if (!appConfig.secure) return true
        val header = context.request().getHeader("Authorization") ?: return false
        if (!header.startsWith("Basic ", ignoreCase = true)) return false
        val parts = runCatching {
            String(Base64.getDecoder().decode(header.replace("Basic", "", ignoreCase = true))).split(":")
        }.getOrNull() ?: return false
        if (parts.size < 2) return false

        val users = getStorage("data", "users").asJsonObject()?.map ?: return false
        val user = users[parts[0]]?.let { gson.fromJson(gson.toJson(it), User::class.java) } ?: return false
        if (user.password != genEncryptedPassword(parts[1], user.salt) || !user.enable_webdav) return false
        context.put("username", user.username)
        return true
    }

    suspend fun webdavList(context: RoutingContext) {
        val file = webdavFile(context)
        if (!file.exists()) return context.response().setStatusCode(404).end()

        val requestUrl = context.request().absoluteURI()
        val body = if (file.isFile) {
            multistatus(webdavEntry(file, requestUrl, isFile = true))
        } else if (file.isDirectory) {
            val directoryUrl = if (requestUrl.endsWith('/')) requestUrl else "$requestUrl/"
            val entries = buildString {
                append(webdavEntry(file, directoryUrl, isFile = false))
                file.listFiles()!!.forEach { entry ->
                    append(webdavEntry(entry, directoryUrl + URLEncoder.encode(entry.name, "UTF-8"), entry.isFile))
                }
            }
            multistatus(entries)
        } else {
            return context.response().setStatusCode(404).end()
        }
        context.response().setStatusCode(207).end(body)
    }

    suspend fun webdavMkdir(context: RoutingContext) {
        val file = webdavFile(context)
        if (file.exists()) return context.response().setStatusCode(201).end()
        try {
            file.mkdirs()
            context.response().setStatusCode(201).end()
        } catch (_: Exception) {
            context.response().setStatusCode(500).end()
        }
    }

    suspend fun webdavUpload(context: RoutingContext) {
        val file = webdavFile(context)
        if (!file.parentFile.exists()) return context.response().setStatusCode(409).end()
        if (file.isDirectory) return context.response().setStatusCode(405).end()
        try {
            if (file.exists()) file.delete()
            file.writeBytes(context.body!!.bytes)
            context.response().setStatusCode(201).end()
        } catch (_: Exception) {
            context.response().setStatusCode(500).end()
        }
    }

    suspend fun webdavDownload(context: RoutingContext) {
        val file = webdavFile(context)
        if (!file.exists()) return context.response().setStatusCode(404).end()
        if (file.isDirectory) return context.response().setStatusCode(405).end()
        context.response()
            .putHeader("Cache-Control", "86400")
            .putHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.name, "UTF-8"))
            .sendFile(file.toString())
    }

    suspend fun webdavDelete(context: RoutingContext) {
        val file = webdavFile(context)
        if (!file.exists()) return context.response().setStatusCode(404).end()
        file.deleteRecursively()
        context.response().setStatusCode(200).end()
    }

    suspend fun webdavMove(context: RoutingContext) = moveCopy(context, move = true)

    suspend fun webdavCopy(context: RoutingContext) = moveCopy(context, move = false)

    suspend fun webdavLock(context: RoutingContext) {
        val lockToken = "urn:uuid:${UUID.randomUUID()}"
        val timeout = context.request().getHeader("Timeout") ?: "Second-3600"
        val body = """
            <?xml version="1.0" encoding="utf-8"?>
            <D:prop xmlns:D="DAV:">
                <D:lockdiscovery>
                    <D:activelock>
                        <D:locktype><write /></D:locktype>
                        <D:lockscope><exclusive /></D:lockscope>
                        <D:locktoken><D:href>%s</D:href></D:locktoken>
                        <D:lockroot><D:href>%s</D:href></D:lockroot>
                        <D:depth>infinity</D:depth>
                        <D:owner><a:href xmlns:a="DAV:">http://www.apple.com/webdav_fs/</a:href></D:owner>
                        <D:timeout>%s</D:timeout>
                    </D:activelock>
                </D:lockdiscovery>
            </D:prop>
        """.trimIndent().format(lockToken, context.request().absoluteURI(), timeout)
        context.response().putHeader("Lock-Token", lockToken).setStatusCode(200).end(body)
    }

    suspend fun webdavUnLock(context: RoutingContext) {
        val lockToken = context.request().getHeader("Lock-Token")
            ?: return context.response().setStatusCode(400).end()
        context.response().putHeader("Lock-Token", lockToken).setStatusCode(204).end()
    }

    suspend fun backupToWebdav(context: RoutingContext): ReturnData {
        val result = ReturnData()
        if (!checkAuth(context)) return result.setData("NEED_LOGIN").setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528")
        if (appConfig.secure && context.get<User>("userInfo")?.enable_webdav != true) {
            return result.setErrorMsg("\u672a\u5f00\u542fwebdav\u529f\u80fd")
        }
        val userNameSpace = getUserNameSpace(context)
        val controller = BookController(coroutineContext)
        val previousBackup = controller.getLastBackFileFromWebdav(userNameSpace)
        return if (controller.saveToWebdav(userNameSpace, previousBackup)) {
            result.setData("")
        } else {
            result.setErrorMsg("backup failed")
        }
    }

    private fun launchWebdav(
        context: RoutingContext,
        onHandlerError: (RoutingContext, Exception) -> Unit,
        handler: suspend () -> Unit,
    ) {
        launch(Dispatchers.IO) {
            try {
                handler()
            } catch (error: Exception) {
                logger.warn(error) { "webdav route failed" }
                onHandlerError(context, error)
            }
        }
    }

    private fun configureResponse(context: RoutingContext) {
        context.response()
            .putHeader("DAV", "1,2")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Access-Control-Expose-Headers", "DAV, content-length, Allow")
            .putHeader("MS-Author-Via", "DAV")
            .putHeader("Allow", "OPTIONS,DELETE,GET,PUT,PROPFIND,MKCOL,MOVE,COPY,LOCK,UNLOCK")
        if (appConfig.secure) context.response().putHeader("WWW-Authenticate", "Basic realm=\"Default realm\"")
    }

    private fun moveCopy(context: RoutingContext, move: Boolean) {
        val source = webdavFile(context)
        if (!source.exists()) return context.response().setStatusCode(412).end()
        val destinationHeader = context.request().getHeader("Destination")
            ?: return context.response().setStatusCode(400).end()
        val destinationPath = URL(destinationHeader).path
            .replace("/reader3/webdav/", "/", ignoreCase = true)
        val destination = File(getUserWebdavHome(context) + URLDecoder.decode(destinationPath, "UTF-8"))
        if (destination.exists()) {
            val overwrite = context.request().getHeader("Overwrite")
            if (overwrite.isNullOrEmpty()) return context.response().setStatusCode(412).end()
            destination.deleteRecursively()
        }
        if (move) source.renameTo(destination) else source.copyRecursively(destination)
        context.response().setStatusCode(201).end()
    }

    private fun webdavFile(context: RoutingContext): File {
        val path = URLDecoder.decode(
            context.request().path().replace("/reader3/webdav/", "/", ignoreCase = true),
            "UTF-8",
        )
        return File(getUserWebdavHome(context) + path)
    }

    private fun multistatus(entries: String): String = """
        <?xml version="1.0" encoding="utf-8"?>
        <D:multistatus xmlns:D="DAV:">
            %s
        </D:multistatus>
    """.trimIndent().format(entries)

    private fun webdavEntry(file: File, href: String, isFile: Boolean): String {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).format(Date(file.lastModified()))
        return if (isFile) {
            """
                <D:response>
                    <D:href>%s</D:href>
                    <D:propstat>
                        <D:status>HTTP/1.1 200 OK</D:status>
                        <D:prop>
                            <D:getlastmodified>%s</D:getlastmodified>
                            <D:creationdate>%s</D:creationdate>
                            <D:resourcetype />
                            <D:displayname>%s</D:displayname>
                            <D:getcontentlength>%s</D:getcontentlength>
                            <D:getcontenttype>%s</D:getcontenttype>
                        </D:prop>
                    </D:propstat>
                </D:response>
            """.trimIndent().format(href, date, date, file.name, file.length(), "application/octet-stream")
        } else {
            """
                <D:response>
                    <D:href>%s</D:href>
                    <D:propstat>
                        <D:status>HTTP/1.1 200 OK</D:status>
                        <D:prop>
                            <D:getlastmodified>%s</D:getlastmodified>
                            <D:creationdate>%s</D:creationdate>
                            <D:resourcetype><D:collection /></D:resourcetype>
                            <D:displayname>%s</D:displayname>
                        </D:prop>
                    </D:propstat>
                </D:response>
            """.trimIndent().format(href, date, date, file.name)
        }
    }
}
