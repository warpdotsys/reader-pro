package com.htmake.reader.api.controller

import java.io.File
import java.net.URI
import java.net.URLDecoder

object WebdavPaths {
    fun pathFromRequest(requestPath: String): String {
        var path = requestPath.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        path = URLDecoder.decode(path, "UTF-8")
        // normalize // and reject traversal tokens early
        path = path.replace('\\', '/').replace(Regex("/{2,}"), "/")
        if (!path.startsWith("/")) path = "/$path"
        return path
    }

    fun resolveUnderHome(home: File, relativeWebPath: String): File {
        val homeCanon = home.canonicalFile
        val rel = relativeWebPath.trimStart('/').replace('\\', '/')
        // block obvious traversal before resolve
        val parts = rel.split('/').filter { it.isNotEmpty() && it != "." }
        require(parts.none { it == ".." }) { "非法路径" }
        val target = File(homeCanon, parts.joinToString(File.separator)).canonicalFile
        val prefix = homeCanon.path.let { if (it.endsWith(File.separator)) it else it + File.separator }
        require(target.path == homeCanon.path || target.path.startsWith(prefix)) { "非法路径" }
        return target
    }

    fun destinationToRelativePath(destinationHeader: String): String? {
        return try {
            val uri = URI(destinationHeader)
            var path = uri.path ?: return null
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        } catch (_: Exception) {
            var path = destinationHeader
            path = path.replace(Regex("https?://[^/]+", RegexOption.IGNORE_CASE), "")
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        }
    }
}
