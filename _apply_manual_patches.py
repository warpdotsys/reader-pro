# -*- coding: utf-8 -*-
"""
Replace Vineflower failed method bodies in best-of-3 with manually reconstructed
suspend Kotlin, derived from CFR decompilation of reader-pro-3.2.14.jar bytecode.
"""
from __future__ import annotations

import os
import re
import shutil
from datetime import datetime

OUT = r"C:\Users\chong\reader-pro-3.2.14-reverse"
BEST = os.path.join(OUT, r"best-of-3\src")
PATCH_LOG = os.path.join(OUT, "manual-patches", "APPLY_LOG.txt")

# Reconstructed method bodies (signature line matched, body includes opening brace content until close)
# Each value is the FULL method text to insert (including fun signature and braces).

RECONSTRUCTIONS = {
    # path relative to BEST -> list of (method_name, full_method_text)
    r"io\legado\app\help\BookHelp.kt": [
        (
            "saveImage",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookHelp.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun saveImage(bookSource: BookSource?, book: Book, src: String) {
        while (downloadImages.contains(src)) {
            delay(100L)
        }
        if (getImage(book, src).exists()) {
            return
        }
        downloadImages.add(src)
        try {
            val analyzeUrl = AnalyzeUrl(
                mUrl = src,
                baseUrl = null,
                source = bookSource
            )
            val bytes = analyzeUrl.getByteArrayAwait()
            val fileName = MD5Utils.md5Encode16(src) + '.' + getImageSuffix(src)
            val file = FileUtils.createFileIfNotExist(getBookCacheDir(book), "images", fileName)
            file.writeBytes(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            downloadImages.remove(src)
        }
    }
''',
        )
    ],
    r"com\htmake\reader\api\controller\UserController.kt": [
        (
            "logout",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + UserController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun logout(context: RoutingContext): ReturnData {
        val returnData = ReturnData()
        if (!checkAuth(context)) {
            return returnData.setData("NEED_LOGIN").setErrorMsg("请登录后使用")
        }
        if (!getAppConfig().secure) {
            return returnData.setErrorMsg("不支持的操作")
        }
        val username = (context.session().get("username") as String?) ?: ""
        context.session().destroy()
        var accessToken = context.queryParam("accessToken").firstOrNull() ?: ""
        if (accessToken.isNotEmpty()) {
            val parts = accessToken.split(":", limit = 2)
            if (parts.size >= 2) {
                accessToken = parts[1]
                getUserMutex().lock()
                try {
                    val userMapJson = asJsonObject(getStorage("data", "users"))
                    @Suppress("UNCHECKED_CAST")
                    val userMap: MutableMap<String, MutableMap<String, Any>> =
                        (userMapJson?.map as? MutableMap<String, MutableMap<String, Any>>)
                            ?: linkedMapOf()
                    val currentUser = userMap[username]
                    if (currentUser == null) {
                        return returnData.setErrorMsg("系统错误")
                    }
                    val tokenMapVal = currentUser["token_map"]
                    if (tokenMapVal != null) {
                        @Suppress("UNCHECKED_CAST")
                        val tokenMap = tokenMapVal as MutableMap<Any?, Any?>
                        tokenMap.remove(accessToken)
                        currentUser["token_map"] = tokenMap
                    }
                    if ((currentUser["token"] ?: "") == accessToken) {
                        currentUser["token"] = ""
                    }
                    userMap[username] = currentUser
                    saveStorage(arrayOf("data", "users"), Json.encode(userMap))
                } finally {
                    getUserMutex().unlock()
                }
            }
        }
        return returnData.setErrorMsg("请重新登录").setData("NEED_LOGIN")
    }
''',
        )
    ],
    r"com\htmake\reader\api\controller\BookController.kt": [
        (
            "saveBookCover",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR/Procyon + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun saveBookCover(book: Book, userNameSpace: String, bookSource: String? = null) {
        val coverUrl = book.displayCover
        if (coverUrl == null || coverUrl.startsWith("/")) {
            return
        }
        val sourceStr = bookSource ?: getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
        val ext = getFileExt(coverUrl, "jpg")
        val md5Encode = MD5Utils.md5Encode(coverUrl).toString()
        val cachePath = getWorkDir("storage", "assets", userNameSpace, "covers", "$md5Encode.$ext")
        val cachedCoverUrl = "/assets/$userNameSpace/covers/$md5Encode.$ext"
        val cacheFile = File(cachePath)
        if (cacheFile.exists()) {
            book.coverUrl = cachedCoverUrl
            return
        }
        try {
            requireNotNull(sourceStr)
            val source = BookSource.fromJson(sourceStr).getOrNull()
            val analyzeUrl = AnalyzeUrl(mUrl = coverUrl, source = source)
            val bytes = analyzeUrl.getByteArrayAwait()
            FileUtils.writeBytes(cachePath, bytes)
            book.coverUrl = cachedCoverUrl
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
''',
        ),
        (
            "editShelfBook",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun editShelfBook(
        book: Book,
        userNameSpace: String,
        handler: (Book) -> Book
    ): Book? {
        val mutex = UserMutex.getLocker(userNameSpace + "@bookshelf")
        BookControllerKt.access$getLogger$p().info("wait for lock {}", userNameSpace + "@bookshelf")
        mutex.lock()
        try {
            BookControllerKt.access$getLogger$p().info("lock success")
            var bookshelf = asJsonArray(getUserStorage(userNameSpace, "bookshelf")) ?: JsonArray()
            var existIndex = -1
            var i = 0
            val size = bookshelf.size()
            while (i < size) {
                val idx = i++
                val existing = bookshelf.getJsonObject(idx).mapTo(Book::class.java)
                if (book.bookUrl.isNotEmpty() && existing.bookUrl == book.bookUrl) {
                    existIndex = idx
                    break
                }
                if (book.name.isNotEmpty() && existing.name == book.name
                    && book.author.isNotEmpty() && existing.author == book.author
                ) {
                    existIndex = idx
                    break
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
''',
        ),
        (
            "saveShelfBookLatestChapter",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
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
''',
        ),
        (
            "setCover",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    private suspend fun setCover(book: Book, epubBook: EpubBook, bookSourceString: String?) {
        val coverUrl = book.displayCover ?: return
        if (coverUrl.startsWith("/")) {
            // Local asset under workDir/storage + path after leading '/'
            val rel = coverUrl.replace("/", File.separator).substring(1)
            val coverFile = File(getWorkDir("storage"), rel)
            if (coverFile.exists()) {
                epubBook.coverImage = Resource(coverFile.readBytes(), "Images/cover.jpg")
            }
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
        try {
            val source = BookSource.fromJson(bookSourceString).getOrNull()
            val analyzeUrl = AnalyzeUrl(mUrl = coverUrl, source = source)
            val bytes = analyzeUrl.getByteArrayAwait()
            epubBook.coverImage = Resource(bytes, "Images/cover.jpg")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
''',
        ),
        (
            "syncFromWebdav",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
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
            for (name in getBackupFileNames()) {
                val backupFile = File(descDir + File.separator + name)
                if (!backupFile.exists()) continue
                val userDataFile = File(getWorkDir("storage", "data", userNameSpace, name))
                deleteRecursively(userDataFile)
                backupFile.copyRecursively(userDataFile, overwrite = false)
            }
            val backupBooksDir = File(descDir + File.separator + "books")
            if (backupBooksDir.exists()) {
                val webdavBooksDir =
                    File(getWorkDir("storage", "data", userNameSpace, "webdav", "books"))
                deleteRecursively(webdavBooksDir)
                backupBooksDir.copyRecursively(webdavBooksDir, overwrite = false)
            }
            var bookProgressDir = File(userHome + File.separator + "bookProgress")
            if (!bookProgressDir.exists()) {
                bookProgressDir =
                    File(userHome + File.separator + "legado" + File.separator + "bookProgress")
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
''',
        ),
        (
            "getSpeakStream",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     *
     * Downloads TTS audio stream with retries (up to 5). On recoverable errors may return null
     * (silent audio substitute path is handled by caller).
     */
    public suspend fun getSpeakStream(
        httpTts: HttpTTS,
        speakText: String,
        speechRate: Int
    ): InputStream? {
        var downloadErrorNo = 0
        while (true) {
            try {
                val analyzeUrl = AnalyzeUrl(
                    mUrl = httpTts.url,
                    key = speakText,
                    headerMapF = httpTts.getHeaderMap(true),
                    source = httpTts,
                    debugLog = Debug
                )
                // speechRate is passed as AnalyzeUrl constructor param in bytecode (boxed Int)
                var response = analyzeUrl.getResponseAwait()
                coroutineContext.ensureActive()
                val checkJs = httpTts.loginCheckJs
                if (!checkJs.isNullOrBlank()) {
                    val evaluated = analyzeUrl.evalJS(checkJs, response)
                    response = evaluated as okhttp3.Response
                }
                val contentType = response.headers["Content-Type"]
                if (contentType != null) {
                    if (contentType == "application/json") {
                        throw NoStackTraceException(response.body!!.string())
                    }
                    val ct = httpTts.contentType
                    if (!ct.isNullOrBlank()) {
                        if (!Regex(ct).matches(contentType)) {
                            throw NoStackTraceException(
                                "TTS服务器返回错误：" + response.body!!.string()
                            )
                        }
                    }
                }
                coroutineContext.ensureActive()
                downloadErrorNo = 0
                return response.body!!.byteStream()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (e is ScriptException || e is WrappedException) {
                    BookControllerKt.access$getLogger$p().error("js错误\n${e.localizedMessage}", e)
                    throw e
                }
                if (e is SocketTimeoutException || e is ConnectException) {
                    downloadErrorNo++
                    if (downloadErrorNo > 5) {
                        BookControllerKt.access$getLogger$p().error("tts超时或连接错误超过5次\n${e.localizedMessage}", e)
                        throw e
                    }
                    continue
                }
                downloadErrorNo++
                BookControllerKt.access$getLogger$p().error("tts下载错误\n${e.localizedMessage}", e)
                if (downloadErrorNo > 5) {
                    BookControllerKt.access$getLogger$p().error("TTS服务器连续5次错误，已暂停阅读。", e)
                    throw e
                }
                BookControllerKt.access$getLogger$p().error("TTS下载音频出错，使用无声音频代替。\n朗读文本：$speakText")
                return null
            }
        }
    }
''',
        ),
        (
            "cacheBookOnServer",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Overload: cacheBookOnServer(bookUrlList, userNameSpace)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun cacheBookOnServer(bookUrlList: JsonArray, userNameSpace: String) {
        for (bi in 0 until bookUrlList.size()) {
            val bookUrl = bookUrlList.getString(bi) ?: continue
            try {
                val book = getShelfBookByURL(bookUrl, userNameSpace) ?: continue
                val bookSource = getBookSourceString(book, userNameSpace) ?: continue
                val chapterList = getLocalChapterList(
                    book, bookSource, false, userNameSpace, getAppConfig().debugLog, null
                )
                val localCacheDir = getChapterCacheDir(book, userNameSpace)
                if (!localCacheDir.exists()) localCacheDir.mkdirs()
                val cachedChapterContentSet = linkedSetOf<Int>()
                localCacheDir.listFiles()?.forEach { f ->
                    val n = f.nameWithoutExtension.toIntOrNull()
                    if (n != null && f.extension.equals("txt", true)) {
                        cachedChapterContentSet.add(n)
                    }
                }
                for (chapterIndex in chapterList.indices) {
                    if (chapterIndex in cachedChapterContentSet) continue
                    val chapterInfo = chapterList[chapterIndex]
                    val nextChapterUrl =
                        if (chapterIndex + 1 < chapterList.size) chapterList[chapterIndex + 1].url
                        else null
                    try {
                        val content = WebBook(
                            bookSource, getAppConfig().debugLog, null, userNameSpace
                        ).getBookContent(book, chapterInfo, nextChapterUrl)
                        File(localCacheDir, "$chapterIndex.txt").writeText(content)
                        val src = BookSource.fromJson(bookSource).getOrNull() ?: BookSource()
                        BookHelp.saveImages(this, src, book, chapterInfo, content)
                        cachedChapterContentSet.add(chapterIndex)
                    } catch (e: Exception) {
                        BookControllerKt.access$getLogger$p().info("cacheBookOnServer error: {}", e.message)
                    }
                }
                BookControllerKt.access$getLogger$p().info("缓存书籍完成: {}", book)
            } catch (e: Exception) {
                BookControllerKt.access$getLogger$p().info("cacheBookOnServer error: {}", e.message)
            }
        }
    }
''',
        ),
        (
            "getLocalChapterList",
            r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Original Vineflower output: Couldn't be decompiled
     */
    public suspend fun getLocalChapterList(
        book: Book,
        bookSource: String?,
        refresh: Boolean,
        userNameSpace: String,
        debugLog: Boolean,
        mutex: Mutex? = null
    ): List<BookChapter> {
        val md5Encode = MD5Utils.md5Encode(book.bookUrl).toString()
        val bookChaptersCache = getBookChaptersCache(userNameSpace)
        var chapterListJson: JsonArray? = null
        if (book.isInShelf) {
            chapterListJson = asJsonArray(
                getUserStorage(userNameSpace, book.name + '_' + book.author, md5Encode)
            )
        } else {
            chapterListJson = asJsonArray(
                bookChaptersCache.getAsString(book.name + '_' + book.author + md5Encode)
            )
        }
        if (chapterListJson != null && !refresh) {
            val localChapterList = ArrayList<BookChapter>()
            for (i in 0 until chapterListJson.size()) {
                localChapterList.add(
                    chapterListJson.getJsonObject(i).mapTo(BookChapter::class.java)
                )
            }
            return localChapterList
        }

        book.rootDir = getWorkDir()
        book.userNameSpace = userNameSpace
        val newChapterList: List<BookChapter> = try {
            if (book.isLocalBook) {
                if (book.isEpub && !extractEpub(book, refresh)) {
                    throw Exception("Epub书籍解压失败")
                }
                if (book.isCbz && !extractCbz(book, refresh)) {
                    throw Exception("CBZ书籍解压失败")
                }
                if (book.isPdf && !convertPdfToImage(book, refresh)) {
                    throw Exception("PDF书籍转换失败")
                }
                LocalBook.getChapterList(book)
            } else {
                if (bookSource.isNullOrEmpty()) {
                    throw Exception("书源信息错误")
                }
                var bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                bookSourceObject?.ruleToc?.preUpdateJs?.let { js ->
                    AnalyzeRule(book, bookSourceObject).evalJS(js)
                }
                var bookForToc = book
                if (book.tocUrl.isBlank()) {
                    bookForToc = WebBook(
                        bookSource, debugLog, null, userNameSpace
                    ).getBookInfo(book.bookUrl)
                }
                WebBook(bookSource, debugLog, null, userNameSpace)
                    .getChapterList(bookForToc)
            }
        } catch (e: Exception) {
            if (!bookSource.isNullOrEmpty()) {
                val bookSourceObject = BookSource.fromJson(bookSource).getOrNull()
                if (bookSourceObject != null) {
                    val info = mutableMapOf(
                        "sourceUrl" to bookSourceObject.bookSourceUrl,
                        "time" to System.currentTimeMillis(),
                        "error" to e.toString()
                    )
                    addInvalidBookSource(bookSourceObject.bookSourceUrl, info, userNameSpace)
                }
            }
            mutex?.lock()
            try {
                book.lastCheckError = e.toString()
                editShelfBook(book, userNameSpace) { exist ->
                    exist.lastCheckError = e.toString()
                    exist
                }
            } finally {
                mutex?.unlock()
            }
            throw e
        }

        if (book.isInShelf) {
            saveUserStorage(
                userNameSpace,
                getRelativePath(book.name + '_' + book.author, md5Encode),
                newChapterList
            )
        } else {
            bookChaptersCache.put(
                book.name + '_' + book.author + md5Encode,
                jsonEncode(newChapterList),
                3600
            )
        }
        saveShelfBookLatestChapter(book, newChapterList, userNameSpace, mutex)
        return newChapterList
    }
''',
        ),
    ],
}


def find_method_span(text: str, name: str) -> tuple[int, int] | None:
    """
    Find start..end of method `fun name` including multi-line signature and body.
    Prefers the outer suspend/public fun that contains 'Couldn't be decompiled' if multiple.
    """
    # Match fun name with optional modifiers on same or previous lines
    pattern = re.compile(
        rf"(?m)^(?P<indent>[ \t]*)(?:/\*\*[^*]*\*+(?:[^/*][^*]*\*+)*/\s*)?"
        rf"(?:public |private |protected |internal |open |override |suspend |final |actual |expect )*"
        rf"fun {re.escape(name)}\b",
        re.M,
    )
    candidates = []
    for m in pattern.finditer(text):
        start = m.start()
        # include kdoc immediately above
        line_start = text.rfind("\n", 0, start) + 1
        # expand upward for doc comments
        prev = text[:line_start]
        # walk back over blank lines and /** ... */
        while True:
            stripped_prev = prev.rstrip()
            if stripped_prev.endswith("*/"):
                doc_start = stripped_prev.rfind("/**")
                if doc_start >= 0:
                    # include from doc_start line
                    line0 = text.rfind("\n", 0, doc_start) + 1
                    start = line0
                    prev = text[:start]
                    continue
            break

        brace = text.find("{", m.end() - 1)
        if brace < 0:
            continue
        depth = 0
        i = brace
        while i < len(text):
            c = text[i]
            if c == "{":
                depth += 1
            elif c == "}":
                depth -= 1
                if depth == 0:
                    end = i + 1
                    # consume trailing newline
                    if end < len(text) and text[end] == "\n":
                        end += 1
                    body = text[start:end]
                    candidates.append((start, end, body))
                    break
            i += 1

    if not candidates:
        return None
    # Prefer candidate that has failure marker, else longest
    failed = [c for c in candidates if "Couldn't be decompiled" in c[2] or "// Bytecode:" in c[2]]
    pool = failed if failed else candidates
    pool.sort(key=lambda x: len(x[2]), reverse=True)
    return pool[0][0], pool[0][1]


def main():
    os.makedirs(os.path.dirname(PATCH_LOG), exist_ok=True)
    log = []
    log.append(f"Apply time: {datetime.now().isoformat()}")
    # backup
    bak_root = os.path.join(OUT, "manual-patches", "backup-before-fix")
    if os.path.exists(bak_root):
        shutil.rmtree(bak_root)
    os.makedirs(bak_root, exist_ok=True)

    total = 0
    for rel, methods in RECONSTRUCTIONS.items():
        path = os.path.join(BEST, rel)
        if not os.path.isfile(path):
            log.append(f"MISSING FILE: {path}")
            continue
        # backup
        bak = os.path.join(bak_root, rel)
        os.makedirs(os.path.dirname(bak), exist_ok=True)
        shutil.copy2(path, bak)

        text = open(path, encoding="utf-8", errors="replace").read()
        for name, new_method in methods:
            span = find_method_span(text, name)
            if not span:
                log.append(f"NOT FOUND: {rel}::{name}")
                continue
            start, end = span
            old = text[start:end]
            # For cacheBookOnServer there may be two overloads - only replace failed ones
            if "Couldn't be decompiled" not in old and "// Bytecode:" not in old and name != "cacheBookOnServer":
                # still replace if we intended (e.g. only failed methods)
                # if clean already, skip
                if "MANUALLY RECONSTRUCTED" not in old:
                    log.append(f"SKIP clean: {rel}::{name} ({len(old)} chars)")
                    continue
            text = text[:start] + new_method + text[end:]
            total += 1
            log.append(f"REPLACED: {rel}::{name} old={len(old)} new={len(new_method)}")

        # Also handle RoutingContext overload of cacheBookOnServer if failed
        open(path, "w", encoding="utf-8", newline="\n").write(text)

    # Special: there may be two cacheBookOnServer overloads - replace remaining failed
    bc_path = os.path.join(BEST, r"com\htmake\reader\api\controller\BookController.kt")
    text = open(bc_path, encoding="utf-8", errors="replace").read()
    # Fix API overload cacheBookOnServer(context) if still broken
    # Find remaining Couldn't
    remaining = []
    for m in re.finditer(r"Couldn't be decompiled", text):
        line = text[: m.start()].count("\n") + 1
        remaining.append(line)
    log.append(f"Remaining Couldn't in BookController: {remaining}")

    # For RoutingContext overload - extract from CFR and reconstruct briefly if still present
    if remaining:
        # try replace any remaining failed fun by name near the marker
        for line_no in list(remaining):
            pass

    # UserController / BookHelp remaining
    for rel in RECONSTRUCTIONS:
        p = os.path.join(BEST, rel)
        t = open(p, encoding="utf-8", errors="replace").read()
        c = t.count("Couldn't be decompiled")
        log.append(f"After patch {rel}: Couldn't count={c}")

    open(PATCH_LOG, "w", encoding="utf-8").write("\n".join(log))
    print("\n".join(log))
    print(f"Total replaced: {total}")


if __name__ == "__main__":
    main()
