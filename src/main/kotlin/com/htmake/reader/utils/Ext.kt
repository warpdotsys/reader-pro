package com.htmake.reader.utils

import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.htmake.reader.config.AppConfig
import com.htmake.reader.entity.License
import com.htmake.reader.entity.MongoFile
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import io.legado.app.data.entities.Book
import io.legado.app.utils.Base64
import io.legado.app.utils.EncoderUtils
import io.legado.app.utils.FileUtils
import io.legado.app.utils.MD5Utils
import io.legado.app.utils.MapDeserializerDoubleAsIntFix
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import mu.KotlinLogging
import okhttp3.HttpUrl.Companion.toHttpUrl
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.reflect.Array as ReflectArray
import java.net.Socket
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import java.util.Base64 as JavaBase64
import java.util.LinkedHashMap
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream
import javax.net.SocketFactory
import javax.net.ssl.SSLSocketFactory
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

val logger = KotlinLogging.logger {}

val gson = GsonBuilder()
    .registerTypeAdapter(
        object : TypeToken<Map<String?, Any?>?>() {}.type,
        MapDeserializerDoubleAsIntFix()
    )
    .registerTypeAdapter(Int::class.java, IntTypeAdapter())
    .registerTypeAdapter(Long::class.java, LongTypeAdapter())
    .disableHtmlEscaping()
    .create()

val prettyGson = GsonBuilder()
    .registerTypeAdapter(
        object : TypeToken<Map<String?, Any?>?>() {}.type,
        MapDeserializerDoubleAsIntFix()
    )
    .registerTypeAdapter(Int::class.java, IntTypeAdapter())
    .registerTypeAdapter(Long::class.java, LongTypeAdapter())
    .disableHtmlEscaping()
    .setPrettyPrinting()
    .create()

var storageFinalPath = ""
var workDirPath = ""
var workDirInit = false

private const val MAX_CACHE_SIZE = 1000

private val lockMap = object : LinkedHashMap<String, ReadWriteLock>() {
    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, ReadWriteLock>?): Boolean =
        size > MAX_CACHE_SIZE
}

var _licenseValid = true

fun String.url(): String {
    if (startsWith("//")) {
        return "http:$this".toHttpUrl().toString()
    }
    if (startsWith("http")) {
        return toHttpUrl().toString()
    }
    return this
}

fun String.toDir(absolute: Boolean = false): String {
    var path = this
    if (path.endsWith("/")) {
        path = path.substring(0, path.length - 1)
    }
    if (absolute && !path.startsWith("/")) {
        path = "/$path"
    }
    return path
}

fun File.deleteRecursively() {
    if (!exists()) {
        return
    }
    if (isFile) {
        delete()
        return
    }
    listFiles()!!.forEach { it.deleteRecursively() }
    delete()
}

fun File.listFilesRecursively(): List<File> {
    val list = ArrayList<File>()
    if (!exists()) {
        return list
    }
    if (isFile) {
        list.add(this)
        return list
    }
    listFiles()!!.forEach {
        list.add(it)
        if (it.isDirectory) {
            list.addAll(it.listFilesRecursively())
        }
    }
    return list
}

fun File.unzip(descDir: String): Boolean {
    if (!exists()) {
        return false
    }
    val buffer = ByteArray(1024)
    var outputStream: OutputStream? = null
    var inputStream: InputStream? = null
    return try {
        val zipFile = ZipFile(toString())
        val entries = zipFile.entries()
        while (entries.hasMoreElements()) {
            val zipEntry = entries.nextElement()
            val descFilePath = descDir + File.separator + zipEntry.name
            if (zipEntry.isDirectory) {
                createDir(descFilePath)
            } else {
                val entryInputStream = zipFile.getInputStream(zipEntry)
                inputStream = entryInputStream
                val descFile = createFile(descFilePath)
                val entryOutputStream = FileOutputStream(descFile)
                outputStream = entryOutputStream
                while (true) {
                    val length = entryInputStream.read(buffer)
                    if (length <= 0) {
                        entryInputStream.close()
                        entryOutputStream.close()
                        break
                    }
                    entryOutputStream.write(buffer, 0, length)
                }
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}

fun File.zip(zipFilePath: String): Boolean {
    if (!exists()) {
        return false
    }
    return if (isDirectory) {
        zip(listFiles()!!.toList(), zipFilePath)
    } else {
        zip(arrayListOf(this), zipFilePath)
    }
}

fun zip(files: List<File>, zipFilePath: String): Boolean {
    if (files.isEmpty()) {
        return false
    }
    val zipFile = createFile(zipFilePath)
    val buffer = ByteArray(1024)
    var zipOutputStream: ZipOutputStream? = null
    var inputStream: FileInputStream? = null
    return try {
        val openedZipOutputStream = ZipOutputStream(FileOutputStream(zipFile))
        zipOutputStream = openedZipOutputStream
        files.forEach { file ->
            if (file.exists()) {
                openedZipOutputStream.putNextEntry(ZipEntry(file.name))
                val fileInputStream = FileInputStream(file)
                inputStream = fileInputStream
                while (true) {
                    val length = fileInputStream.read(buffer)
                    if (length <= 0) {
                        openedZipOutputStream.closeEntry()
                        break
                    }
                    openedZipOutputStream.write(buffer, 0, length)
                }
            }
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    } finally {
        inputStream?.close()
        zipOutputStream?.close()
    }
}

fun createDir(filePath: String): File {
    logger.debug("createDir filePath {}", filePath)
    val file = File(filePath)
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

fun createFile(filePath: String): File {
    logger.debug("createFile filePath {}", filePath)
    val file = File(filePath)
    val parentFile = file.parentFile!!
    if (!parentFile.exists()) {
        parentFile.mkdirs()
    }
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

fun getWorkDir(subPath: String = ""): String {
    if (!workDirInit && workDirPath.isEmpty()) {
        val appConfig = SpringContextUtils.getBean("appConfig", AppConfig::class.java)
        if (appConfig != null && appConfig.workDir.isNotEmpty() && appConfig.workDir != ".") {
            val workDirFile = File(appConfig.workDir)
            if (workDirFile.exists() && !workDirFile.isDirectory) {
                logger.error("reader.app.workDir={} is not a directory", appConfig.workDir)
            } else {
                if (!workDirFile.exists()) {
                    logger.info("reader.app.workDir={} not exists, creating", appConfig.workDir)
                    workDirFile.mkdirs()
                }
                workDirPath = workDirFile.absolutePath
            }
        }
        if (workDirPath.isEmpty()) {
            val osName = System.getProperty("os.name")
            val currentDir = System.getProperty("user.dir")
            logger.info("osName: {} currentDir: {}", osName, currentDir)
            workDirPath = if (osName.startsWith("Mac OS", true) && !currentDir.startsWith("/Users/")) {
                Paths.get(System.getProperty("user.home"), ".reader").toString()
            } else {
                currentDir
            }
        }
        logger.info("Using workdir: {}", workDirPath)
        workDirInit = true
    }
    return Paths.get(workDirPath, subPath).toString()
}

fun getWorkDir(vararg subDirFiles: String): String = getWorkDir(getRelativePath(*subDirFiles))

fun getRelativePath(vararg subDirFiles: String): String {
    val path = StringBuilder("")
    subDirFiles.forEach {
        if (it.isNotEmpty()) {
            path.append(File.separator).append(it)
        }
    }
    return path.toString().let {
        if (it.startsWith('/')) it.substring(1) else it
    }
}

fun getStoragePath(): String {
    if (storageFinalPath.isNotEmpty()) {
        return storageFinalPath
    }
    val appConfig = SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    val storagePath = if (appConfig != null) {
        getWorkDir("storage").also { storageFinalPath = it }
    } else {
        File("storage").path
    }
    logger.info("Using storagePath: {}", storagePath)
    return storagePath
}

fun saveStorage(vararg name: String, value: Any, pretty: Boolean = false, ext: String = ".json") {
    val toJson = when (value) {
        is String -> value
        is JsonObject, is JsonArray -> value.toString()
        else -> if (pretty) prettyGson.toJson(value) else gson.toJson(value)
    }
    val storagePath = getStoragePath()
    val storageDir = File(storagePath)
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    val filename = name.last()
    val path = getRelativePath(*name.copyOfRange(0, name.size - 1), "$filename$ext")
    val file = File(storagePath + File.separator + path)
    logger.info("Save file to storage name: {} path: {}", name, file.absoluteFile)
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    val nameWithoutExtension = file.absoluteFile.nameWithoutExtension
    val lock = synchronized(lockMap) {
        lockMap.getOrPut(file.absolutePath) { ReentrantReadWriteLock() }
    }
    var locked = false
    try {
        locked = lock.writeLock().tryLock(10, TimeUnit.SECONDS)
        if (!locked) {
            throw Exception("保存文件超时:${file.absolutePath}")
        }
        val tempFile = Files.createTempFile(
            Paths.get(file.parentFile!!.path).toAbsolutePath(),
            nameWithoutExtension,
            ".temp"
        )
        Files.write(tempFile, toJson.toByteArray(Charsets.UTF_8))
        val filePath = Paths.get(file.path)
        val backupPath = Paths.get(file.parentFile!!.path, "$nameWithoutExtension.backup.json").toAbsolutePath()
        if (Files.exists(filePath)) {
            Files.move(filePath, backupPath, StandardCopyOption.ATOMIC_MOVE)
        }
        Files.move(tempFile, filePath, StandardCopyOption.ATOMIC_MOVE)
        Files.deleteIfExists(tempFile)
        if (nameWithoutExtension.length >= 32) {
            Files.deleteIfExists(backupPath)
        }
        if (nameWithoutExtension == "users") {
            val userCount = toJson.countOccurrences("username")
            val keyPath = getRelativePath(*name.copyOfRange(0, name.size - 1), ".$nameWithoutExtension.key")
            val keyFile = File(storagePath + File.separator + keyPath)
            if (!keyFile.exists()) {
                keyFile.createNewFile()
            }
            keyFile.writeText(MD5Utils.md5Encode("userCount=$userCount").toString().substring(16))
        }
        saveMongoFile(path, toJson)
    } catch (e: Exception) {
        logger.error("保存文件失败:", e)
        throw Exception("保存文件失败:${file.absolutePath}")
    } finally {
        if (locked) {
            lock.writeLock().unlock()
        }
    }
}

fun getStorageFile(vararg name: String, ext: String = ".json"): File {
    val storagePath = getStoragePath()
    val storageDir = File(storagePath)
    if (!storageDir.exists()) {
        storageDir.mkdirs()
    }
    val filename = name.last()
    val path = getRelativePath(*name.copyOfRange(0, name.size - 1), "$filename$ext")
    return File(storagePath + File.separator + path)
}

fun getStorage(vararg name: String, ext: String = ".json"): String? {
    val storagePath = getStoragePath()
    val filename = name.last()
    val path = getRelativePath(*name.copyOfRange(0, name.size - 1), "$filename$ext")
    val file = getStorageFile(*name, ext = ext)
    logger.info("Read file from storage name: {} path: {}", name, file.absoluteFile)
    if (!file.exists()) {
        return readMongoFile(path)?.let { content ->
            if (content.isNotEmpty()) {
                if (!file.parentFile.exists()) {
                    file.parentFile.mkdirs()
                }
                file.createNewFile()
                file.writeText(content)
            }
            content
        }
    }
    val lock = synchronized(lockMap) {
        lockMap.getOrPut(file.absolutePath) { ReentrantReadWriteLock() }
    }
    var locked = false
    var result: String? = null
    try {
        locked = lock.readLock().tryLock(10, TimeUnit.SECONDS)
        if (!locked) {
            throw Exception("读取文件超时:${file.absolutePath}")
        }
        FileReader(file).use { reader ->
            var content = reader.readText()
            if (content.isEmpty()) {
                content = readMongoFile(path)?.let { mongoContent ->
                    if (mongoContent.isNotEmpty()) {
                        if (!file.parentFile.exists()) {
                            file.parentFile.mkdirs()
                        }
                        file.createNewFile()
                        file.writeText(mongoContent)
                    }
                    mongoContent
                } ?: content
            }
            if (filename == "users") {
                val keyPath = getRelativePath(*name.copyOfRange(0, name.size - 1), ".$filename.key")
                val keyFile = File(storagePath + File.separator + keyPath)
                if (keyFile.exists()) {
                    val key = keyFile.readText()
                    val userCount = content.countOccurrences("username")
                    val expected = MD5Utils.md5Encode("userCount=$userCount").toString().substring(16)
                    if (key != expected) {
                        throw Exception("用户数据被篡改，请联系开发者修复")
                    }
                }
            }
            result = content
        }
    } catch (e: Exception) {
        logger.error("读取文件失败:", e)
        throw Exception("读取文件失败:${file.absolutePath}")
    } finally {
        if (locked) {
            lock.readLock().unlock()
        }
    }
    return result
}

fun getMongoFileStorage(): MongoCollection<MongoFile>? {
    val appConfig = SpringContextUtils.getBean("appConfig", AppConfig::class.java)
    return MongoManager.fileStorage(appConfig.mongoDbName, "storage")
}

fun readMongoFile(path: String): String? {
    if (MongoManager.isInit()) {
        logger.info("Get mongoFile {}", path)
        val doc = getMongoFileStorage()?.find(Filters.eq("path", path))?.first()
        if (doc != null) {
            return doc.content
        }
    }
    return null
}

fun saveMongoFile(path: String, content: String): Boolean {
    if (MongoManager.isInit()) {
        logger.info("Save mongoFile {}", path)
        val result = getMongoFileStorage()
        val doc = result?.find(Filters.eq("path", path))?.first()
        if (doc != null) {
            doc.content = content
            doc.updated_at = System.currentTimeMillis()
            val updateResult = getMongoFileStorage()?.replaceOne(
                Filters.eq("path", path),
                doc,
                ReplaceOptions().upsert(true)
            )
            return updateResult != null && updateResult.modifiedCount > 0
        }
        try {
            getMongoFileStorage()?.insertOne(MongoFile(path, content))
            return true
        } catch (e: Exception) {
            logger.info("Save mongoFile {} failed", path)
            e.printStackTrace()
        }
    }
    return false
}

fun String.countOccurrences(subStr: String): Int {
    var count = 0
    var startIndex = 0
    while (startIndex < length) {
        val index = indexOf(subStr, startIndex)
        if (index == -1) {
            break
        }
        count++
        startIndex = index + subStr.length
    }
    return count
}

fun Any?.asJsonArray(): JsonArray? = when (this) {
    is JsonArray -> this
    is String -> try {
        JsonArray(this)
    } catch (e: Exception) {
        logger.error("解析内容出错: {}  内容: \n{}", e, this)
        throw e
    }
    else -> null
}

fun parseJsonStringList(
    file: File,
    fields: Set<String>? = null,
    exclude: Set<String>? = null,
    startIndex: Int = 0,
    endIndex: Int = Int.MAX_VALUE,
    checkNotEmpty: Set<String>? = null,
    filter: ((ObjectNode) -> Boolean)? = null
): JsonArray? {
    if (!file.exists()) {
        return null
    }
    try {
        val factory = ObjectMapper().factory
        val resultList = JsonArray()
        var currentIndex = -1
        factory.createParser(file).use { parser ->
            if (parser.nextToken() == JsonToken.START_ARRAY) {
                while (parser.nextToken() != JsonToken.END_ARRAY) {
                    if (parser.currentToken() != JsonToken.START_OBJECT) {
                        continue
                    }
                    if (fields.isNullOrEmpty()) {
                        if (filter == null) {
                            currentIndex++
                            if (currentIndex < startIndex) {
                                parser.skipChildren()
                            } else {
                                if (currentIndex > endIndex) {
                                    break
                                }
                                val objectNode: ObjectNode = parser.readValueAsTree()
                                exclude?.takeIf { it.isNotEmpty() }?.forEach { objectNode.remove(it) }
                                resultList.add(objectNode.toString())
                            }
                        } else {
                            val objectNode: ObjectNode = parser.readValueAsTree()
                            if (filter(objectNode)) {
                                currentIndex++
                            }
                            if (currentIndex >= startIndex) {
                                if (currentIndex > endIndex) {
                                    break
                                }
                                resultList.add(objectNode.toString())
                            }
                        }
                    } else {
                        currentIndex++
                        if (currentIndex < startIndex) {
                            parser.skipChildren()
                        } else {
                            if (currentIndex > endIndex) {
                                break
                            }
                            val item = JsonObject()
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                val fieldName = parser.currentName
                                parser.nextToken()
                                when {
                                    fields.contains(fieldName) -> item.put(fieldName, parser.valueAsString)
                                    checkNotEmpty != null && checkNotEmpty.contains(fieldName) ->
                                        item.put(fieldName, !parser.valueAsString.isNullOrEmpty())
                                    else -> parser.skipChildren()
                                }
                            }
                            resultList.add(item.toString())
                        }
                    }
                }
            }
        }
        return resultList
    } catch (e: Exception) {
        logger.error("解析文件内容出错: {}  文件: \n{}", e, file)
        throw e
    }
}

fun Any?.asJsonObject(): JsonObject? = when (this) {
    is JsonObject -> this
    is String -> try {
        JsonObject(this)
    } catch (e: Exception) {
        logger.error("解析内容出错: {}  内容: \n{}", e, this)
        throw e
    }
    else -> null
}

fun <T> T.serializeToMap(): Map<String, Any> = convert()

fun <T> T.toMap(): Map<String, Any> = convert()

inline fun <reified T> Map<String, Any>.toDataClass(): T = convert()

inline fun <I, reified O> I.convert(): O {
    val json = if (this is String) this else gson.toJson(this)
    return gson.fromJson(json, object : TypeToken<O>() {}.type)
}

@Suppress("UNCHECKED_CAST", "EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T> Class<T>.arrayType(): Class<Array<T>> =
    ReflectArray.newInstance(this, 0).javaClass as Class<Array<T>>

@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.memberProperties
        .first { it.name == propertyName } as KProperty1<Any, *>
    return property.get(instance) as R
}

fun setInstanceProperty(instance: Any, propertyName: String, propertyValue: Any) {
    val property = instance::class.memberProperties.first { it.name == propertyName }
    if (property is KMutableProperty<*>) {
        property.setter.call(instance, propertyValue)
    }
}

fun Book.fillData(newBook: Book, keys: List<String>): Book {
    keys.forEach { key ->
        val current = readInstanceProperty<String?>(this, key)
        if (current.isNullOrEmpty()) {
            val cacheValue = readInstanceProperty<String?>(newBook, key)
            if (!cacheValue.isNullOrEmpty()) {
                setInstanceProperty(this, key, cacheValue)
            }
        }
    }
    return this
}

fun getRandomString(length: Int): String {
    val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    return (1..length).map { allowedChars.random() }.joinToString("")
}

fun genEncryptedPassword(password: String, salt: String): String =
    MD5Utils.md5Encode(MD5Utils.md5Encode(password + salt).toString() + salt).toString()

fun jsonEncode(value: Any, pretty: Boolean = false): String =
    if (pretty) prettyGson.toJson(value) else gson.toJson(value)

fun File.deepListFiles(allowExtensions: Array<String>?): List<File> {
    val fileList = ArrayList<File>()
    listFiles()!!.forEach {
        if (it.isDirectory) {
            fileList.addAll(it.deepListFiles(allowExtensions))
        } else {
            val extension = FileUtils.getExtension(it.name)
            if ((allowExtensions?.contentDeepToString()?.contains(extension) ?: false) || allowExtensions == null) {
                fileList.add(it)
            }
        }
    }
    return fileList
}

fun getTraceId(): String = UUID.randomUUID().toString().subSequence(0, 8).toString()

fun setLicenseValid(isValid: Boolean) {
    _licenseValid = isValid
}

fun getInstalledLicense(ignoreInvalid: Boolean = false): License {
    val licenseKeyString = getStorage("data", "license", ext = ".key")
    if (licenseKeyString.isNullOrEmpty()) {
        return License()
    }
    if (!ignoreInvalid && !_licenseValid) {
        return License()
    }
    val license = decryptToLicense(licenseKeyString)
    logger.info("license: {}", license)
    return if (license?.verified == true) license else License()
}

fun decryptToLicense(content: String): License? {
    if (content.isEmpty()) {
        return null
    }
    return decryptData(content)?.let { it.toMap().toDataClass<License>() }
}

fun decryptData(content: String): String? {
    val publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj0G3qEPjVTvVd7pXFUVYZFHT8KaoG4onc5rLUKqFQ2DCh/5hFK9t2nKh2XB+C2Jp/GSK2ONwD7ceXenmA6uvr90uCK/gp6j62XFVRvc8sIm0d/bGbzZFJRk3HKtxEckBmASduPObY691DVVixxNtUrSJktx/TZaB42pUQk4j+7FuOVNNPra44hDdnyGhmYBBf2B4kjXVMjL+0NCblFIN1+qjmcol44k6NFKFF54q05bjR3CRyYdAnNTCOyt9va0oB6lDlKHplSZmAOH9JGMUki/HDJbABESXMnyIpux27w9SQ8aJStYttnJWHALO1hiFJsxbz5KUkldH6Ny1p/2W5QIDAQAB"
    val publicKey = KeyFactory.getInstance("RSA").generatePublic(
        X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.NO_WRAP))
    )
    return EncoderUtils.decryptSegmentByPublicKey(content, publicKey)
}

fun validateEmail(email: String): Boolean = Regex(
    "^[A-Za-z0-9._%+-]+@(163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud).com$"
).matches(email)

fun sendEmail(toEmail: String, subject: String, body: String): Boolean {
    val host = "smtp.qiye.aliyun.com"
    val port = 465
    val sendCommand = { writer: OutputStreamWriter, reader: BufferedReader, command: Pair<String, Int> ->
        val (cmd, code) = command
        logger.debug("Send command {}, expect code {}", cmd.trim(), code)
        writer.write(cmd.toString())
        writer.flush()
        val response = reader.readLine()
        logger.debug("Response {}", response)
        if (!response.isNullOrEmpty()) {
            if (!response.startsWith("$code")) {
                logger.error("Error response from SMTP server.")
                false
            } else {
                true
            }
        } else {
            logger.error("SMTP server no response.")
            false
        }
    }
    return try {
        val sslSocketFactory = SSLSocketFactory.getDefault() as SocketFactory
        val socket: Socket = sslSocketFactory.createSocket(host, port)
        val writer = OutputStreamWriter(socket.getOutputStream())
        val reader = BufferedReader(InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")))
        val response = reader.readLine()
        if (!response.startsWith("220")) {
            logger.error("Error connecting to the SMTP server.")
            false
        } else {
            val commandList = getCommand(arrayListOf(toEmail), subject, body)
            var result = false
            for (command in commandList) {
                result = sendCommand(writer, reader, command)
                if (!result) {
                    break
                }
            }
            writer.close()
            reader.close()
            socket.close()
            result
        }
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun getCommand(to: List<String>, subject: String, body: String): List<Pair<String, Int>> {
    val username = "no-reply@onmy.top"
    val password = "no-reply@1."
    val from = "no-reply@onmy.top"
    val fromname = "Reader"
    val separator = "----=_Part_${System.currentTimeMillis()}${UUID.randomUUID()}"
    val command = mutableListOf(Pair("HELO sendmail\r\n", 250))
    if (username.isNotEmpty()) {
        command.add(Pair("AUTH LOGIN\r\n", 334))
        command.add(Pair("${encodeBase64(username)}\r\n", 334))
        command.add(Pair("${encodeBase64(password)}\r\n", 235))
    }
    command.add(Pair("MAIL FROM: <$from>\r\n", 250))
    var header = "FROM: $fromname<$from>\r\n"
    if (to.isNotEmpty()) {
        if (to.size == 1) {
            command.add(Pair("RCPT TO: <${to[0]}>\r\n", 250))
            header += "TO: <${to[0]}>\r\n"
        } else {
            to.forEachIndexed { index, email ->
                command.add(Pair("RCPT TO: <$email>\r\n", 250))
                header += when {
                    index == 0 -> "TO: <$email>"
                    index + 1 == to.size -> ",<$email>\r\n"
                    else -> ",<$email>"
                }
            }
        }
    }
    header += "Subject: =?UTF-8?B?${encodeBase64(subject)}?=\r\n"
    header += "Content-Type: multipart/alternative;\r\n"
    header += "\tboundary=\"$separator\""
    header += "\r\nMIME-Version: 1.0\r\n"
    header += "\r\n--$separator\r\n"
    header += "Content-Type:text/html; charset=utf-8\r\n"
    header += "Content-Transfer-Encoding: base64\r\n\r\n"
    header += "${encodeBase64(body)}\r\n"
    header += "--$separator\r\n"
    header += "\r\n.\r\n"
    command.add(Pair("DATA\r\n", 354))
    command.add(Pair(header, 250))
    command.add(Pair("QUIT\r\n", 221))
    return command
}

fun encodeBase64(text: String): String = JavaBase64.getEncoder().encodeToString(text.toByteArray())
