package io.legado.app.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

private val logger = KotlinLogging.logger {}

object ZipUtils {

    suspend fun zipFiles(srcFiles: Collection<String>, zipFilePath: String): Boolean =
        zipFiles(srcFiles, zipFilePath, null)

    /**
     * 将多个文件压缩到一个压缩包
     */
    suspend fun zipFiles(
        srcFilePaths: Collection<String>?,
        zipFilePath: String?,
        comment: String?
    ): Boolean = withContext(Dispatchers.IO) {
        if (srcFilePaths != null && zipFilePath != null) {
            ZipOutputStream(FileOutputStream(zipFilePath)).use {
                for (srcFile in srcFilePaths) {
                    if (!zipFile(getFileByPath(srcFile)!!, "", it, comment)) {
                        return@withContext false
                    }
                }
                return@withContext true
            }
        } else {
            return@withContext false
        }
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun zipFiles(srcFiles: Collection<File>?, zipFile: File?, comment: String? = null): Boolean {
        if (srcFiles == null || zipFile == null) {
            return false
        }
        ZipOutputStream(FileOutputStream(zipFile)).use {
            for (srcFile in srcFiles) {
                if (!zipFile(srcFile, "", it, comment)) {
                    return false
                }
            }
            return true
        }
    }

    @Throws(IOException::class)
    fun zipFile(srcFilePath: String, zipFilePath: String): Boolean {
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), null)
    }

    @Throws(IOException::class)
    fun zipFile(srcFilePath: String, zipFilePath: String, comment: String): Boolean {
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), comment)
    }

    @JvmOverloads
    @Throws(IOException::class)
    fun zipFile(srcFile: File?, zipFile: File?, comment: String? = null): Boolean {
        if (srcFile == null || zipFile == null) {
            return false
        }
        ZipOutputStream(FileOutputStream(zipFile)).use { zos ->
            return zipFile(srcFile, "", zos, comment)
        }
    }

    @Throws(IOException::class)
    private fun zipFile(srcFile: File, rootPath: String, zos: ZipOutputStream, comment: String?): Boolean {
        if (!srcFile.exists()) {
            return true
        }
        val rootPath1 = rootPath + (if (isSpace(rootPath)) "" else File.separator) + srcFile.name
        if (srcFile.isDirectory) {
            val fileList = srcFile.listFiles()
            if (fileList != null) {
                if (fileList.isNotEmpty()) {
                    for (file in fileList) {
                        if (!zipFile(file, rootPath1, zos, comment)) {
                            return false
                        }
                    }
                    return true
                }
            }
            val entry = ZipEntry("$rootPath1/")
            entry.comment = comment
            zos.putNextEntry(entry)
            zos.closeEntry()
        } else {
            BufferedInputStream(FileInputStream(srcFile)).use { `is` ->
                val entry = ZipEntry(rootPath1)
                entry.comment = comment
                zos.putNextEntry(entry)
                zos.write(`is`.readBytes())
                zos.closeEntry()
            }
        }
        return true
    }

    @Throws(IOException::class)
    fun unzipFile(zipFilePath: String, destDirPath: String): List<File>? {
        return unzipFileByKeyword(zipFilePath, destDirPath, null)
    }

    @Throws(IOException::class)
    fun unzipFile(zipFile: File, destDir: File): List<File>? {
        return unzipFileByKeyword(zipFile, destDir, null)
    }

    @Throws(IOException::class)
    fun unzipFileByKeyword(zipFilePath: String, destDirPath: String, keyword: String?): List<File>? {
        return unzipFileByKeyword(getFileByPath(zipFilePath), getFileByPath(destDirPath), keyword)
    }

    @Throws(IOException::class)
    fun unzipFileByKeyword(zipFile: File?, destDir: File?, keyword: String?): List<File>? {
        if (zipFile == null || destDir == null) {
            return null
        }
        val files = ArrayList<File>()
        val zip = ZipFile(zipFile)
        val entries = zip.entries()
        zip.use {
            if (isSpace(keyword)) {
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement() as ZipEntry
                    val entryName = entry.name
                    if (entryName.contains("../")) {
                        logger.error("ZipUtils entryName: $entryName is dangerous!")
                    } else if (!unzipChildFile(destDir, files, zip, entry, entryName)) {
                        return files
                    }
                }
            } else {
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement() as ZipEntry
                    val entryName = entry.name
                    if (entryName.contains("../")) {
                        logger.error("ZipUtils entryName: $entryName is dangerous!")
                    } else if (entryName.contains(keyword!!) && !unzipChildFile(destDir, files, zip, entry, entryName)) {
                        return files
                    }
                }
            }
            return files
        }
    }

    @Throws(IOException::class)
    private fun unzipChildFile(
        destDir: File,
        files: MutableList<File>,
        zip: ZipFile,
        entry: ZipEntry,
        name: String
    ): Boolean {
        val file = File(destDir, name)
        files.add(file)
        if (entry.isDirectory) {
            return createOrExistsDir(file)
        }
        if (!createOrExistsFile(file)) {
            return false
        }
        BufferedInputStream(zip.getInputStream(entry)).use { `in` ->
            BufferedOutputStream(FileOutputStream(file)).use { out ->
                out.write(`in`.readBytes())
            }
        }
        return true
    }

    @Throws(IOException::class)
    fun getFilesPath(zipFilePath: String): List<String>? {
        return getFilesPath(getFileByPath(zipFilePath))
    }

    @Throws(IOException::class)
    fun getFilesPath(zipFile: File?): List<String>? {
        if (zipFile == null) {
            return null
        }
        val paths = ArrayList<String>()
        val zip = ZipFile(zipFile)
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entryName = (entries.nextElement() as ZipEntry).name
            if (entryName.contains("../")) {
                logger.error("ZipUtils entryName: $entryName is dangerous!")
                paths.add(entryName)
            } else {
                paths.add(entryName)
            }
        }
        zip.close()
        return paths
    }

    @Throws(IOException::class)
    fun getComments(zipFilePath: String): List<String?>? {
        return getComments(getFileByPath(zipFilePath))
    }

    @Throws(IOException::class)
    fun getComments(zipFile: File?): List<String?>? {
        if (zipFile == null) {
            return null
        }
        val comments = ArrayList<String?>()
        val zip = ZipFile(zipFile)
        val entries = zip.entries()
        while (entries.hasMoreElements()) {
            val entry = entries.nextElement() as ZipEntry
            comments.add(entry.comment)
        }
        zip.close()
        return comments
    }

    /**
     * 判断文件夹是否存在, 不存在则创建
     */
    private fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 判断文件是否存在, 不存在则创建
     */
    private fun createOrExistsFile(file: File?): Boolean {
        if (file == null) {
            return false
        }
        if (file.exists()) {
            return file.isFile
        }
        if (!createOrExistsDir(file.parentFile)) {
            return false
        }
        return try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 根据文件路径获取文件, 路径为空返回null
     */
    private fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    /**
     * 判断字符串是否为null或全为空白字符
     */
    private fun isSpace(s: String?): Boolean {
        if (s == null) {
            return true
        }
        for (i in 0 until s.length) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
        }
        return true
    }
}
