package io.legado.app.adapters

import io.legado.app.help.http.StrResponse
import io.legado.app.model.DebugLog
import java.io.File
import java.nio.file.Paths

class DefaultAdpater : ReaderAdapterInterface {
    override fun getWorkDir(subPath: String): String {
        val osName = System.getProperty("os.name")
        val currentDir = System.getProperty("user.dir")
        val workDirPath = if (osName.startsWith("Mac OS", true) && !currentDir.startsWith("/Users/")) {
            Paths.get(System.getProperty("user.home"), ".reader").toString()
        } else {
            currentDir
        }
        return Paths.get(workDirPath, subPath).toString()
    }

    override fun getWorkDir(vararg subDirFiles: String): String =
        getWorkDir(getRelativePath(*subDirFiles))

    fun getRelativePath(vararg subDirFiles: String): String =
        subDirFiles.filter { it.isNotEmpty() }
            .joinToString(File.separator, prefix = File.separator)
            .removePrefix(File.separator)

    override val cacheDir: String
        get() = getWorkDir("storage", "cache")

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
    ): StrResponse = throw Exception("不支持webview")
}
