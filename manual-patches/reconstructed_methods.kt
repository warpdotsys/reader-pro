// Manual reconstructions from CFR + class bytecode (reader-pro-3.2.14.jar)
// These replace Vineflower "Couldn't be decompiled" method bodies in best-of-3.
// Style: sequential suspend Kotlin (coroutine state machine desugared for readability).

// =============================================================================
// io.legado.app.help.BookHelp.saveImage
// =============================================================================
/*
public suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
    // wait if same src is already downloading
    while (downloadImages.contains(src)) {
        delay(100L)
    }
    if (getImage(book, src).exists()) return
    downloadImages.add(src)
    try {
        val analyzeUrl = AnalyzeUrl(
            src, null, null, null, null, null,
            bookSource, null, null, null, null
        )
        val bytes = analyzeUrl.getByteArrayAwait()
        val name = MD5Utils.md5Encode16(src) + '.' + getImageSuffix(src)
        FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", name)
            .writeBytes(bytes)
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        downloadImages.remove(src)
    }
}
*/

// =============================================================================
// UserController.logout
// =============================================================================
/*
public suspend fun logout(context: RoutingContext): ReturnData {
    val returnData = ReturnData()
    if (!checkAuth(context)) {
        return returnData.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
    }
    if (!appConfig.secure) {
        return returnData.setErrorMsg("不支持的操作")
    }
    val username = context.session().get<String>("username") ?: ""
    context.session().destroy()
    var accessToken = context.queryParam("accessToken").firstOrNull() ?: ""
    if (accessToken.isNotEmpty()) {
        val tmp = accessToken.split(":", limit = 2)
        if (tmp.size >= 2) {
            accessToken = tmp[1]
            userMutex.lock()
            try {
                val userMapJson = asJsonObject(getStorage("data", "users"))
                val userMap: MutableMap<String, MutableMap<String, Any>> =
                    (userMapJson?.map as? MutableMap<String, MutableMap<String, Any>>)
                        ?: linkedMapOf()
                val currentUser = userMap[username]
                    ?: return returnData.setErrorMsg("系统错误")
                val tokenMapVal = currentUser["token_map"]
                if (tokenMapVal != null) {
                    val tokenMap = tokenMapVal as MutableMap<*, *>
                    tokenMap.remove(accessToken)
                    currentUser["token_map"] = tokenMap
                }
                if (currentUser.getOrDefault("token", "") == accessToken) {
                    currentUser["token"] = ""
                }
                userMap[username] = currentUser
                saveStorage(arrayOf("data", "users"), Json.encode(userMap))
            } finally {
                userMutex.unlock()
            }
        }
    }
    return returnData.setErrorMsg("请重新登录").setData("NEED_LOGIN")
}
*/

// =============================================================================
// BookController.editShelfBook
// =============================================================================
/*
public suspend fun editShelfBook(
    book: Book,
    userNameSpace: String,
    handler: (Book) -> Book
): Book? {
    val mutex = UserMutex.getLocker(userNameSpace + "@bookshelf")
    logger.info("wait for lock {}", userNameSpace + "@bookshelf")
    mutex.lock()
    try {
        logger.info("lock success")
        var bookshelf = asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: JsonArray()
        var existIndex = -1
        for (i in 0 until bookshelf.size()) {
            val _book = bookshelf.getJsonObject(i).mapTo(Book::class.java)
            if (book.bookUrl.isNotEmpty() && _book.bookUrl == book.bookUrl) {
                existIndex = i; break
            }
            if (book.name.isNotEmpty() && _book.name == book.name
                && book.author.isNotEmpty() && _book.author == book.author) {
                existIndex = i; break
            }
        }
        if (existIndex >= 0) {
            val bookList = bookshelf.list
            var existBook = bookshelf.getJsonObject(existIndex).mapTo(Book::class.java)
            existBook = handler(existBook)
            bookList[existIndex] = JsonObject.mapFrom(existBook)
            bookshelf = JsonArray(bookList)
            saveUserStorage(userNameSpace, "bookshelf", bookshelf)
            return existBook
        }
        return null
    } finally {
        mutex.unlock()
    }
}
*/

// =============================================================================
// BookController.saveShelfBookLatestChapter
// =============================================================================
/*
public suspend fun saveShelfBookLatestChapter(
    book: Book,
    bookChapterList: List<BookChapter>,
    userNameSpace: String,
    mutex: Mutex? = null
) {
    mutex?.lock()
    try {
        editShelfBook(book, userNameSpace) { existBook ->
            if (bookChapterList.isNotEmpty()) {
                existBook.latestChapterTitle = bookChapterList.last().title
            }
            val delta = bookChapterList.size - existBook.totalChapterNum
            if (delta > 0) {
                existBook.lastCheckCount = delta
                existBook.lastCheckTime = System.currentTimeMillis()
            }
            existBook.lastCheckError = null
            existBook.totalChapterNum = bookChapterList.size
            book.latestChapterTitle = existBook.latestChapterTitle
            book.lastCheckCount = existBook.lastCheckCount
            book.lastCheckTime = existBook.lastCheckTime
            book.lastCheckError = existBook.lastCheckError
            book.totalChapterNum = existBook.totalChapterNum
            existBook
        }
    } finally {
        mutex?.unlock()
    }
}
*/

// =============================================================================
// BookController.setCover (private)
// =============================================================================
/*
private suspend fun setCover(book: Book, epubBook: EpubBook, bookSourceString: String?) {
    val coverUrl = book.displayCover ?: return
    if (coverUrl.startsWith("/")) {
        val rel = coverUrl.replace("/", File.separator).substring(1)
        val coverFile = File(getWorkDir("storage", rel))
        // note: CFR path construction is messy; intent is read local cover under storage
        val byteArray = coverFile.readBytes()
        epubBook.coverImage = Resource(byteArray, "Images/cover.jpg")
        return
    }
    if (bookSourceString == null) return
    val ext = getFileExt(coverUrl, "jpg")
    val md5Encode = MD5Utils.md5Encode(coverUrl).toString()
    val cachePath = getWorkDir("storage", "cache", "$md5Encode.$ext")
    val cacheFile = File(cachePath)
    if (cacheFile.exists()) {
        epubBook.coverImage = Resource(cacheFile.readBytes(), "Images/cover.jpg")
        return
    }
    val source = BookSource.fromJson(bookSourceString).getOrNull()
    try {
        val analyzeUrl = AnalyzeUrl(coverUrl, baseSource = source)
        val bytes = analyzeUrl.getByteArrayAwait()
        epubBook.coverImage = Resource(bytes, "Images/cover.jpg")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
*/

// =============================================================================
// BookController.syncFromWebdav
// =============================================================================
/*
public suspend fun syncFromWebdav(zipFilePath: String, userNameSpace: String): Boolean {
    val descDir = getWorkDir("storage", "data", userNameSpace, "tmp")
    val descDirFile = File(descDir)
    try {
        val userHome = getUserWebdavHome(userNameSpace)
        val zipFile = File(zipFilePath)
        if (!zipFile.exists()) {
            deleteRecursively(descDirFile)
            return false
        }
        deleteRecursively(descDirFile)
        ZipUtils.unzipFile(zipFile, descDirFile)
        for (it in getBackupFileNames()) {
            val backupFile = File(descDir + File.separator + it)
            if (!backupFile.exists()) continue
            val userDataFile = File(getWorkDir("storage", "data", userNameSpace, it))
            deleteRecursively(userDataFile)
            backupFile.copyRecursively(userDataFile, overwrite = false)
        }
        val backupBooksDir = File(descDir + File.separator + "books")
        if (backupBooksDir.exists()) {
            val webdavBooksDir = File(getWorkDir("storage", "data", userNameSpace, "webdav", "books"))
            deleteRecursively(webdavBooksDir)
            backupBooksDir.copyRecursively(webdavBooksDir, overwrite = false)
        }
        var bookProgressDir = File(userHome + File.separator + "bookProgress")
        if (!bookProgressDir.exists()) {
            bookProgressDir = File(userHome + File.separator + "legado" + File.separator + "bookProgress")
        }
        if (bookProgressDir.exists() && bookProgressDir.isDirectory) {
            bookProgressDir.listFiles()?.forEach { f ->
                syncBookProgressFromWebdav(f, userNameSpace)
            }
        }
        return true
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    } finally {
        deleteRecursively(descDirFile)
    }
}
*/
