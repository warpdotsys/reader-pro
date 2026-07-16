# -*- coding: utf-8 -*-
"""Replace remaining failed Function3 / getBookShelfBooks in BookController.kt"""
import re

p = r"C:\Users\chong\reader-pro-3.2.14-reverse\best-of-3\src\com\htmake\reader\api\controller\BookController.kt"
t = open(p, encoding="utf-8", errors="replace").read()

# Locate getBookShelfBooks method containing the failed VF marker
marker = "$VF: Couldn't be decompiled"
if marker not in t:
    marker = "Couldn't be decompiled"
assert marker in t, "no remaining failure marker"

# Find the start of the enclosing method: look backward for "fun getBookShelfBooks"
idx = t.find(marker)
before = t[:idx]
m = None
for mm in re.finditer(r"(?m)^[ \t]*(?:public |private |protected |open |override |suspend )*[^\n]*fun getBookShelfBooks\b", before):
    m = mm
assert m, "getBookShelfBooks not found before marker"
start = m.start()
# expand for kdoc
line_start = t.rfind("\n", 0, start) + 1
# find body end
brace = t.find("{", m.end())
depth = 0
i = brace
while i < len(t):
    if t[i] == "{":
        depth += 1
    elif t[i] == "}":
        depth -= 1
        if depth == 0:
            end = i + 1
            if end < len(t) and t[end] == "\n":
                end += 1
            break
    i += 1
else:
    raise SystemExit("brace match failed")

old = t[start:end]
print("Replacing getBookShelfBooks, old len", len(old), "lines", old.count("\n") + 1)

new_method = r'''    /**
     * MANUALLY RECONSTRUCTED from CFR + BookController.class (reader-pro-3.2.14.jar)
     * Includes concurrent per-book refresh via limitConcurrent (16 workers).
     * Original Vineflower: anonymous Function3 decompilation failed (NPE in VarDefinitionHelper)
     */
    public suspend fun getBookShelfBooks(refresh: Boolean = false, userNameSpace: String): List<Book> {
        val bookshelf = asJsonArray(getUserStorage(userNameSpace, "bookshelf"))
            ?: return ArrayList()
        if (bookshelf.size() == 0) {
            return ArrayList()
        }
        val bookList = ArrayList<Book>()
        val mutex = Mutex(false)
        val syncMutex = Mutex(false)
        // Parallel-ish over indices 0 until size, max 16 concurrent (limitConcurrent)
        limitConcurrent(16, 0, bookshelf.size()) { _, index ->
            val book = bookshelf.getJsonObject(index).mapTo(Book::class.java)
            book.isInShelf = true
            if (!book.isLocalBook && book.canUpdate && refresh) {
                val bookSource = getBookSourceStringBySourceURLOpt(book.origin, userNameSpace)
                if (bookSource != null) {
                    try {
                        withContext(Dispatchers.IO) {
                            getLocalChapterList(
                                book, bookSource, refresh, userNameSpace, false, mutex
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            syncMutex.lock()
            try {
                bookList.add(book)
            } finally {
                syncMutex.unlock()
            }
            true
        }
        return bookList
    }
'''

t2 = t[:start] + new_method + t[end:]
open(p, "w", encoding="utf-8", newline="\n").write(t2)

# verify
t3 = open(p, encoding="utf-8", errors="replace").read()
print("Remaining Couldn't:", t3.count("Couldn't be decompiled"))
print("Remaining // Bytecode:", t3.count("// Bytecode:"))
print("MANUALLY RECONSTRUCTED:", t3.count("MANUALLY RECONSTRUCTED"))
print("lines:", t3.count("\n") + 1)
