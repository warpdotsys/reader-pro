package io.legado.app.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

object FileUtils {
    const val GB = 1073741824L
    const val MB = 1048576L
    const val KB = 1024L

    const val BY_NAME_ASC = 0
    const val BY_NAME_DESC = 1
    const val BY_TIME_ASC = 2
    const val BY_TIME_DESC = 3
    const val BY_SIZE_ASC = 4
    const val BY_SIZE_DESC = 5
    const val BY_EXTENSION_ASC = 6
    const val BY_EXTENSION_DESC = 7

    fun exists(root: File, vararg subDirFiles: String): Boolean {
        return getFile(root, *subDirFiles).exists()
    }

    fun createFileIfNotExist(root: File, vararg subDirFiles: String): File {
        val filePath = getPath(root, *subDirFiles)
        return createFileIfNotExist(filePath)
    }

    fun createFolderIfNotExist(root: File, vararg subDirs: String): File {
        val filePath = getPath(root, *subDirs)
        return createFolderIfNotExist(filePath)
    }

    fun createFolderIfNotExist(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    @Synchronized
    fun createFileIfNotExist(filePath: String): File {
        val file = File(filePath)
        try {
            if (!file.exists()) {
                file.parent?.let {
                    createFolderIfNotExist(it)
                }
                file.createNewFile()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    fun createFileWithReplace(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            file.parent?.let {
                createFolderIfNotExist(it)
            }
            file.createNewFile()
        } else {
            file.delete()
            file.createNewFile()
        }
        return file
    }

    fun getFile(root: File, vararg subDirFiles: String): File {
        val filePath = getPath(root, *subDirFiles)
        return File(filePath)
    }

    fun getPath(root: File, vararg subDirFiles: String): String {
        val path = StringBuilder(root.absolutePath)
        subDirFiles.forEach {
            if (it.isNotEmpty()) {
                path.append(File.separator).append(it)
            }
        }
        return path.toString()
    }

    @Synchronized
    fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            if (file.isDirectory) {
                file.listFiles()?.forEach {
                    deleteFile(it.path)
                }
            }
            file.delete()
        }
    }

    fun getCachePath(): String {
        throw Exception("Not implemented")
    }

    fun separator(path: String): String {
        var path1 = path.replace("\\", File.separator)
        if (!path1.endsWith(File.separator)) {
            path1 += File.separator
        }
        return path1
    }

    fun closeSilently(c: Closeable?) {
        if (c != null) {
            try {
                c.close()
            } catch (e: IOException) {
            }
        }
    }

    @JvmOverloads
    fun listDirs(
        startDirPath: String,
        excludeDirs: Array<String>? = null,
        sortType: Int = BY_NAME_ASC
    ): Array<File> {
        var excludeDirs1 = excludeDirs
        val dirList = ArrayList<File>()
        val startDir = File(startDirPath)
        if (!startDir.isDirectory) {
            return arrayOf()
        }
        val dirs = startDir.listFiles { f ->
            f?.isDirectory ?: false
        } ?: return arrayOf()
        if (excludeDirs == null) {
            excludeDirs1 = emptyArray()
        }
        for (dir in dirs) {
            val file = dir.absoluteFile
            if (!excludeDirs1.contentDeepToString().contains(file.name)) {
                dirList.add(file)
            }
        }
        when (sortType) {
            BY_NAME_ASC -> dirList.sortWith(SortByName())
            BY_NAME_DESC -> {
                dirList.sortWith(SortByName())
                dirList.reverse()
            }
            BY_TIME_ASC -> dirList.sortWith(SortByTime())
            BY_TIME_DESC -> {
                dirList.sortWith(SortByTime())
                dirList.reverse()
            }
            BY_SIZE_ASC -> dirList.sortWith(SortBySize())
            BY_SIZE_DESC -> {
                dirList.sortWith(SortBySize())
                dirList.reverse()
            }
            BY_EXTENSION_ASC -> dirList.sortWith(SortByExtension())
            BY_EXTENSION_DESC -> {
                dirList.sortWith(SortByExtension())
                dirList.reverse()
            }
        }
        return dirList.toTypedArray()
    }

    @JvmOverloads
    fun listDirsAndFiles(startDirPath: String, allowExtensions: Array<String>? = null): Array<File>? {
        val files = if (allowExtensions == null) listFiles(startDirPath) else listFiles(startDirPath, allowExtensions)
        val dirs = listDirs(startDirPath)
        return files?.let {
            dirs + it
        }
    }

    @JvmOverloads
    fun listFiles(
        startDirPath: String,
        filterPattern: Pattern? = null,
        sortType: Int = BY_NAME_ASC
    ): Array<File> {
        val fileList = ArrayList<File>()
        val f = File(startDirPath)
        if (!f.isDirectory) {
            return arrayOf()
        }
        val files = f.listFiles { file ->
            if (file == null) {
                return@listFiles false
            }
            if (file.isDirectory) {
                return@listFiles false
            }
            if (filterPattern == null) {
                return@listFiles true
            }
            val matcher = filterPattern.matcher(file.name)
            matcher?.find() ?: true
        } ?: return arrayOf()
        for (file in files) {
            fileList.add(file.absoluteFile)
        }
        when (sortType) {
            BY_NAME_ASC -> fileList.sortWith(SortByName())
            BY_NAME_DESC -> {
                fileList.sortWith(SortByName())
                fileList.reverse()
            }
            BY_TIME_ASC -> fileList.sortWith(SortByTime())
            BY_TIME_DESC -> {
                fileList.sortWith(SortByTime())
                fileList.reverse()
            }
            BY_SIZE_ASC -> fileList.sortWith(SortBySize())
            BY_SIZE_DESC -> {
                fileList.sortWith(SortBySize())
                fileList.reverse()
            }
            BY_EXTENSION_ASC -> fileList.sortWith(SortByExtension())
            BY_EXTENSION_DESC -> {
                fileList.sortWith(SortByExtension())
                fileList.reverse()
            }
        }
        return fileList.toTypedArray()
    }

    fun listFiles(startDirPath: String, allowExtensions: Array<String>?): Array<File>? {
        val file = File(startDirPath)
        return file.listFiles { _, name ->
            val extension = getExtension(name)
            (allowExtensions?.contentDeepToString()?.contains(extension) ?: false) || allowExtensions == null
        }
    }

    fun listFiles(startDirPath: String, allowExtension: String?): Array<File>? {
        return if (allowExtension == null) {
            listFiles(startDirPath, null as String?)
        } else {
            listFiles(startDirPath, arrayOf(allowExtension))
        }
    }

    fun exist(path: String): Boolean {
        val file = File(path)
        return file.exists()
    }

    @JvmOverloads
    fun delete(file: File, deleteRootDir: Boolean = false): Boolean {
        var result = false
        if (file.isFile) {
            result = deleteResolveEBUSY(file)
        } else {
            val files = file.listFiles() ?: return false
            if (files.isNotEmpty()) {
                for (f in files) {
                    delete(f, deleteRootDir)
                    result = deleteResolveEBUSY(f)
                }
            } else {
                result = deleteRootDir && deleteResolveEBUSY(file)
            }
            if (deleteRootDir) {
                result = deleteResolveEBUSY(file)
            }
        }
        return result
    }

    private fun deleteResolveEBUSY(file: File): Boolean {
        val to = File(file.absolutePath + System.currentTimeMillis())
        file.renameTo(to)
        return to.delete()
    }

    @JvmOverloads
    fun delete(path: String, deleteRootDir: Boolean = false): Boolean {
        val file = File(path)
        return if (file.exists()) {
            delete(file, deleteRootDir)
        } else {
            false
        }
    }

    fun copy(src: String, tar: String): Boolean {
        val srcFile = File(src)
        return srcFile.exists() && copy(srcFile, File(tar))
    }

    fun copy(src: File, tar: File): Boolean {
        try {
            if (src.isFile) {
                val `is` = FileInputStream(src)
                val op = FileOutputStream(tar)
                val bis = BufferedInputStream(`is`)
                val bos = BufferedOutputStream(op)
                val bt = ByteArray(8192)
                while (true) {
                    val len = bis.read(bt)
                    if (len == -1) {
                        bis.close()
                        bos.close()
                        break
                    }
                    bos.write(bt, 0, len)
                }
            } else if (src.isDirectory) {
                tar.mkdirs()
                src.listFiles()?.forEach {
                    copy(it.absoluteFile, File(tar.absoluteFile, it.name))
                }
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun move(src: String, tar: String): Boolean {
        return move(File(src), File(tar))
    }

    fun move(src: File, tar: File): Boolean {
        return rename(src, tar)
    }

    fun rename(oldPath: String, newPath: String): Boolean {
        return rename(File(oldPath), File(newPath))
    }

    fun rename(src: File, tar: File): Boolean {
        return src.renameTo(tar)
    }

    @JvmOverloads
    fun readText(filepath: String, charset: String = "utf-8"): String {
        try {
            val data = readBytes(filepath)
            if (data != null) {
                return String(data, Charset.forName(charset)).trim()
            }
        } catch (e: UnsupportedEncodingException) {
        }
        return ""
    }

    fun readBytes(filepath: String): ByteArray? {
        var fis: FileInputStream? = null
        try {
            fis = FileInputStream(filepath)
            val baos = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            while (true) {
                val len = fis.read(buffer, 0, buffer.size)
                if (len == -1) {
                    val data = baos.toByteArray()
                    baos.close()
                    return data
                }
                baos.write(buffer, 0, len)
            }
        } catch (e: IOException) {
            return null
        } finally {
            closeSilently(fis)
        }
    }

    @JvmOverloads
    fun writeText(filepath: String, content: String, charset: String = "utf-8"): Boolean {
        return try {
            writeBytes(filepath, content.toByteArray(Charset.forName(charset)))
        } catch (e: UnsupportedEncodingException) {
            false
        }
    }

    fun writeBytes(filepath: String, data: ByteArray): Boolean {
        val file = File(filepath)
        var fos: FileOutputStream? = null
        return try {
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            fos = FileOutputStream(filepath)
            fos.write(data)
            true
        } catch (e: IOException) {
            false
        } finally {
            closeSilently(fos)
        }
    }

    fun writeInputStream(filepath: String, data: InputStream): Boolean {
        val file = File(filepath)
        return writeInputStream(file, data)
    }

    fun writeInputStream(file: File, data: InputStream): Boolean {
        var fos: FileOutputStream? = null
        return try {
            if (!file.exists()) {
                file.parentFile?.mkdirs()
                file.createNewFile()
            }
            val buffer = ByteArray(4096)
            fos = FileOutputStream(file)
            while (true) {
                val len = data.read(buffer, 0, buffer.size)
                if (len == -1) {
                    data.close()
                    fos.flush()
                    break
                }
                fos.write(buffer, 0, len)
            }
            true
        } catch (e: IOException) {
            false
        } finally {
            closeSilently(fos)
        }
    }

    fun appendText(path: String, content: String): Boolean {
        val file = File(path)
        var writer: FileWriter? = null
        return try {
            if (!file.exists()) {
                file.createNewFile()
            }
            writer = FileWriter(file, true)
            writer.write(content)
            true
        } catch (e: IOException) {
            false
        } finally {
            closeSilently(writer)
        }
    }

    fun getLength(path: String): Long {
        val file = File(path)
        return if (file.isFile && file.exists()) file.length() else 0L
    }

    fun getName(pathOrUrl: String?): String {
        if (pathOrUrl == null) {
            return ""
        }
        val pos = pathOrUrl.lastIndexOf('/')
        return if (0 <= pos) {
            pathOrUrl.substring(pos + 1)
        } else {
            System.currentTimeMillis().toString() + "." + getExtension(pathOrUrl)
        }
    }

    fun getNameExcludeExtension(path: String): String {
        return try {
            var fileName = File(path).name
            val lastIndexOf = fileName.lastIndexOf(".")
            if (lastIndexOf != -1) {
                fileName = fileName.substring(0, lastIndexOf)
            }
            fileName
        } catch (e: Exception) {
            ""
        }
    }

    fun getSize(path: String): String {
        val fileSize = getLength(path)
        return toFileSizeString(fileSize)
    }

    fun toFileSizeString(fileSize: Long): String {
        val df = DecimalFormat("0.00")
        val fileSizeString: String = when {
            fileSize < KB -> "" + fileSize + "B"
            fileSize < MB -> df.format(fileSize / KB.toDouble()) + "K"
            fileSize < GB -> df.format(fileSize / MB.toDouble()) + "M"
            else -> df.format(fileSize / GB.toDouble()) + "G"
        }
        return fileSizeString
    }

    fun getExtension(pathOrUrl: String): String {
        val dotPos = pathOrUrl.lastIndexOf('.')
        return if (0 <= dotPos) {
            pathOrUrl.substring(dotPos + 1)
        } else {
            "ext"
        }
    }

    @JvmOverloads
    fun getFileExtetion(url: String, defaultExt: String = ""): String {
        return try {
            val file = url.split("?", ignoreCase = true, limit = 2)[0].split("/").last()
            val dotPos = file.lastIndexOf('.')
            if (0 <= dotPos) {
                file.substring(dotPos + 1)
            } else {
                defaultExt
            }
        } catch (e: Exception) {
            defaultExt
        }
    }

    fun getMimeType(pathOrUrl: String): String {
        throw Exception("Not implemented")
    }

    @JvmOverloads
    fun getDateTime(path: String, format: String = "yyyy\u5e74MM\u6708dd\u65e5HH:mm"): String {
        val file = File(path)
        return getDateTime(file, format)
    }

    fun getDateTime(file: File, format: String): String {
        val cal = Calendar.getInstance()
        cal.timeInMillis = file.lastModified()
        return SimpleDateFormat(format, Locale.PRC).format(cal.time)
    }

    fun compareLastModified(path1: String, path2: String): Int {
        val stamp1 = File(path1).lastModified()
        val stamp2 = File(path2).lastModified()
        return if (stamp1 > stamp2) 1 else if (stamp1 < stamp2) -1 else 0
    }

    fun makeDirs(path: String): Boolean {
        return makeDirs(File(path))
    }

    fun makeDirs(file: File): Boolean {
        return file.mkdirs()
    }

    class SortByExtension : Comparator<File> {
        override fun compare(f1: File?, f2: File?): Int {
            return if (f1 != null && f2 != null) {
                when {
                    f1.isDirectory && f2.isFile -> -1
                    f1.isFile && f2.isDirectory -> 1
                    else -> f1.name.compareTo(f2.name, true)
                }
            } else {
                if (f1 == null) -1 else 1
            }
        }
    }

    class SortByName : Comparator<File> {
        private var caseSensitive: Boolean

        constructor(caseSensitive: Boolean) {
            this.caseSensitive = caseSensitive
        }

        constructor() {
            this.caseSensitive = false
        }

        override fun compare(f1: File?, f2: File?): Int {
            if (f1 != null && f2 != null) {
                return when {
                    f1.isDirectory && f2.isFile -> -1
                    f1.isFile && f2.isDirectory -> 1
                    else -> {
                        val s1 = f1.name
                        val s2 = f2.name
                        if (caseSensitive) {
                            s1.compareTo(s2, false)
                        } else {
                            s1.compareTo(s2, true)
                        }
                    }
                }
            } else {
                return if (f1 == null) -1 else 1
            }
        }
    }

    class SortBySize : Comparator<File> {
        override fun compare(f1: File?, f2: File?): Int {
            return if (f1 != null && f2 != null) {
                when {
                    f1.isDirectory && f2.isFile -> -1
                    f1.isFile && f2.isDirectory -> 1
                    f1.length() < f2.length() -> -1
                    else -> 1
                }
            } else {
                if (f1 == null) -1 else 1
            }
        }
    }

    class SortByTime : Comparator<File> {
        override fun compare(f1: File?, f2: File?): Int {
            return if (f1 != null && f2 != null) {
                when {
                    f1.isDirectory && f2.isFile -> -1
                    f1.isFile && f2.isDirectory -> 1
                    f1.lastModified() > f2.lastModified() -> -1
                    else -> 1
                }
            } else {
                if (f1 == null) -1 else 1
            }
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    annotation class SortType
}
