// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.model.localBook;

import kotlin.Result$Companion;
import kotlin.ResultKt;
import kotlin.Unit;
import io.legado.app.utils.FileUtils;
import kotlin.Result;
import java.util.regex.Matcher;
import io.legado.app.help.BookHelp;
import kotlin.text.StringsKt;
import kotlin.Pair;
import org.jetbrains.annotations.Nullable;
import io.legado.app.exception.TocEmptyException;
import io.legado.app.data.entities.BookChapter;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import kotlin.jvm.internal.Intrinsics;
import java.io.InputStream;
import io.legado.app.data.entities.Book;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u00152\u0006\u0010\u000e\u001a\u00020\u000fJ\u0018\u0010\u0016\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0014R\u001e\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004?\u0006\u0004\n\u0002\u0010\u0007¡§\u0006\u0018" }, d2 = { "Lio/legado/app/model/localBook/LocalBook;", "", "()V", "nameAuthorPatterns", "", "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "[Ljava/util/regex/Pattern;", "analyzeNameAuthor", "Lkotlin/Pair;", "", "fileName", "deleteBook", "", "book", "Lio/legado/app/data/entities/Book;", "getBookInputStream", "Ljava/io/InputStream;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "getContent", "chapter", "reader-pro" })
public final class LocalBook
{
    @NotNull
    public static final LocalBook INSTANCE;
    @NotNull
    private static final Pattern[] nameAuthorPatterns;
    
    private LocalBook() {
    }
    
    @NotNull
    public final InputStream getBookInputStream(@NotNull final Book book) throws FileNotFoundException, SecurityException {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final File file = book.getLocalFile();
        if (file.exists()) {
            return new FileInputStream(file);
        }
        throw new FileNotFoundException(Intrinsics.stringPlus(book.getName(), (Object)" \u6587\u4ef6\u4e0d\u5b58\u5728"));
    }
    
    @NotNull
    public final ArrayList<BookChapter> getChapterList(@NotNull final Book book) throws Exception {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final ArrayList chapters = book.isEpub() ? EpubFile.Companion.getChapterList(book) : (book.isUmd() ? UmdFile.Companion.getChapterList(book) : (book.isCbz() ? CbzFile.Companion.getChapterList(book) : (book.isPdf() ? PdfFile.Companion.getChapterList(book) : TextFile.Companion.getChapterList(book))));
        if (chapters.isEmpty()) {
            throw new TocEmptyException(Intrinsics.stringPlus("Chapterlist is empty  ", (Object)book.getLocalFile()));
        }
        return chapters;
    }
    
    @Nullable
    public final String getContent(@NotNull final Book book, @NotNull final BookChapter chapter) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)chapter, "chapter");
        return book.isEpub() ? EpubFile.Companion.getContent(book, chapter) : (book.isUmd() ? UmdFile.Companion.getContent(book, chapter) : (book.isCbz() ? CbzFile.Companion.getContent(book, chapter) : (book.isPdf() ? PdfFile.Companion.getContent(book, chapter) : TextFile.Companion.getContent(book, chapter))));
    }
    
    @NotNull
    public final Pair<String, String> analyzeNameAuthor(@NotNull final String fileName) {
        Intrinsics.checkNotNullParameter((Object)fileName, "fileName");
        final String tempFileName = StringsKt.substringBeforeLast$default(fileName, ".", (String)null, 2, (Object)null);
        Object name = null;
        Object author = null;
        final Pattern[] nameAuthorPatterns = LocalBook.nameAuthorPatterns;
        int i = 0;
        while (i < nameAuthorPatterns.length) {
            final Pattern pattern = nameAuthorPatterns[i];
            ++i;
            final Matcher it = pattern.matcher(tempFileName);
            final int n = 0;
            final Matcher matcher = it.find() ? it : null;
            if (matcher == null) {
                continue;
            }
            final Matcher $this$analyzeNameAuthor_u24lambda_u2d1 = matcher;
            final int n2 = 0;
            final String group3 = $this$analyzeNameAuthor_u24lambda_u2d1.group(2);
            Intrinsics.checkNotNull((Object)group3);
            name = group3;
            final String group4 = $this$analyzeNameAuthor_u24lambda_u2d1.group(1);
            final String group1 = (group4 == null) ? "" : group4;
            final String group5 = $this$analyzeNameAuthor_u24lambda_u2d1.group(3);
            final String group2 = (group5 == null) ? "" : group5;
            author = BookHelp.INSTANCE.formatBookAuthor(Intrinsics.stringPlus(group1, (Object)group2));
            return (Pair<String, String>)new Pair(name, author);
        }
        name = BookHelp.INSTANCE.formatBookName(tempFileName);
        final String it2 = BookHelp.INSTANCE.formatBookAuthor(StringsKt.replace$default(tempFileName, (String)name, "", false, 4, (Object)null));
        final int n3 = 0;
        final String s = (it2.length() != tempFileName.length()) ? it2 : null;
        author = ((s == null) ? "" : s);
        return (Pair<String, String>)new Pair(name, author);
    }
    
    public final void deleteBook(@NotNull final Book book) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        try {
            final Result$Companion companion = Result.Companion;
            final int n = 0;
            File bookFile = book.getLocalFile();
            if ((book.isLocalTxt() || book.isUmd()) && bookFile.exists()) {
                bookFile.delete();
            }
            if (book.isEpub()) {
                final File parentFile = bookFile.getParentFile();
                Intrinsics.checkNotNullExpressionValue((Object)parentFile, "bookFile.parentFile");
                bookFile = parentFile;
                if (bookFile.exists()) {
                    FileUtils.INSTANCE.delete(bookFile, true);
                }
            }
            Result.constructor-impl((Object)Unit.INSTANCE);
        }
        catch (final Throwable t) {
            final Result$Companion companion2 = Result.Companion;
            Result.constructor-impl(ResultKt.createFailure(t));
        }
    }
    
    static {
        INSTANCE = new LocalBook();
        nameAuthorPatterns = new Pattern[] { Pattern.compile("(.*?)\u300a([^\u300a\u300b]+)\u300b.*?\u4f5c\u8005\uff1a(.*)"), Pattern.compile("(.*?)\u300a([^\u300a\u300b]+)\u300b(.*)"), Pattern.compile("(^)(.+) \u4f5c\u8005\uff1a(.+)$"), Pattern.compile("(^)(.+) by (.+)$") };
    }
}
