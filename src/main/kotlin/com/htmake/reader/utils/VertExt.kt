package com.htmake.reader.utils

import java.io.File
import java.nio.file.Paths

var workDirPath = ""
var workDirInit = false

fun getWorkDir(subPath: String = ""): String {
    if (!workDirInit && workDirPath.isEmpty()) {
        val osName = System.getProperty("os.name")
        val currentDir = System.getProperty("user.dir")
        workDirPath = if (osName.startsWith("Mac OS", true) && !currentDir.startsWith("/Users/")) {
            Paths.get(System.getProperty("user.home"), ".reader").toString()
        } else {
            currentDir
        }
        workDirInit = true
    }
    return Paths.get(workDirPath, subPath).toString()
}

fun getWorkDir(vararg subDirFiles: String): String = getWorkDir(getRelativePath(*subDirFiles))

fun getRelativePath(vararg subDirFiles: String): String {
    val path = subDirFiles.filter { it.isNotEmpty() }.joinToString(File.separator, prefix = File.separator)
    return path.removePrefix(File.separator)
}
