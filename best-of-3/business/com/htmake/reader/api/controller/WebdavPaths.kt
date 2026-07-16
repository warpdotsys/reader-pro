/** Business rewrite from reader-pro-3.2.14.jar — phase7. */

package com.htmake.reader.api.controller

import java.io.File
import java.net.URI
import java.net.URLDecoder

/**
 * Resolve WebDAV Destination header the same way as jar:
 * Destination is absolute URL → take path → strip /reader3/webdav → join under home.
 */
object WebdavPaths {

    fun pathFromRequest(requestPath: String): String {
        var path = requestPath
        path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
        path = URLDecoder.decode(path, "UTF-8")
        if (!path.startsWith("/")) path = "/$path"
        return path
    }

    fun resolveUnderHome(home: File, relativeWebPath: String): File {
        val rel = relativeWebPath.trimStart('/')
        val target = File(home, rel).canonicalFile
        require(target.path.startsWith(home.canonicalPath)) { "非法路径" }
        return target
    }

    /**
     * @param destinationHeader full URL or path from Destination header
     * @return path relative to webdav root starting with /
     */
    fun destinationToRelativePath(destinationHeader: String): String? {
        return try {
            val uri = URI(destinationHeader)
            var path = uri.path ?: return null
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        } catch (_: Exception) {
            // not a valid URI — treat as path
            var path = destinationHeader
            path = path.replace(Regex("https?://[^/]+", RegexOption.IGNORE_CASE), "")
            path = path.replace(Regex("/reader3/webdav", RegexOption.IGNORE_CASE), "")
            if (!path.startsWith("/")) path = "/$path"
            URLDecoder.decode(path, "UTF-8")
        }
    }
}
