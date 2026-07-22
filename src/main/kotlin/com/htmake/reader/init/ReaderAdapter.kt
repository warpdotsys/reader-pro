package com.htmake.reader.init

import com.htmake.reader.utils.RemoteWebview

import io.legado.app.adapters.ReaderAdapterInterface
import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import java.io.File


object ReaderAdapter : ReaderAdapterInterface {

    override fun getWorkDir(subPath: String): String = com.htmake.reader.utils.getWorkDir(subPath)



    override fun getWorkDir(vararg subDirFiles: String): String = com.htmake.reader.utils.getWorkDir(getRelativePath(*subDirFiles))

    fun getRelativePath(vararg subDirFiles: String): String {

        val path = StringBuilder("")
        subDirFiles.forEach {
            if (it.isNotEmpty()) {
                path.append(File.separator).append(it)
            }
        }
        return path.toString().let {
            if (it.startsWith("/")) {
                it.substring(1)
            } else {
                it
            }
        }
    }


    override fun getCacheDir(): String = getWorkDir("storage", "cache")


    override suspend fun getStrResponseByRemoteWebview(
        url: String?,
        html: String?,
        encode: String?,
        tag: String?,
        headerMap: Map<String, String>?,
        sourceRegex: String?,
        javaScript: String?,
        proxy: String?,
        post: Boolean,
        body: String?,
        userNameSpace: String,
        debugLog: DebugLog?
    ): StrResponse {
        var encodeStr = encode
        if (encode.isNullOrEmpty()) {
            encodeStr = headerMap?.get("charset")
        }
        return RemoteWebview.getStrResponse(url, html, encodeStr, tag, headerMap, sourceRegex,
            javaScript,
            proxy,
            post,
            body,
            userNameSpace,
            debugLog)
    }
}
