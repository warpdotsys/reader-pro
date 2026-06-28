package io.legado.app.model.localBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.exception.TocEmptyException;
import io.legado.app.help.BookHelp;
import io.legado.app.utils.FileUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: LocalBook.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/localBook/LocalBook.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u000b\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u000e\u001a\u00020\u000fJ\u001e\u0010\u0012\u001a\u0012\u0012\u0004\u0012\u00020\u00140\u0013j\b\u0012\u0004\u0012\u00020\u0014`\u00152\u0006\u0010\u000e\u001a\u00020\u000fJ\u0018\u0010\u0016\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0017\u001a\u00020\u0014R\u001e\u0010\u0003\u001a\u0010\u0012\f\u0012\n \u0006*\u0004\u0018\u00010\u00050\u00050\u0004X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u0007¨\u0006\u0018"}, d2 = {"Lio/legado/app/model/localBook/LocalBook;", PackageDocumentBase.PREFIX_OPF, "()V", "nameAuthorPatterns", PackageDocumentBase.PREFIX_OPF, "Ljava/util/regex/Pattern;", "kotlin.jvm.PlatformType", "[Ljava/util/regex/Pattern;", "analyzeNameAuthor", "Lkotlin/Pair;", PackageDocumentBase.PREFIX_OPF, "fileName", "deleteBook", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "getBookInputStream", "Ljava/io/InputStream;", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "getContent", NCXDocumentV2.NCXAttributeValues.chapter, "reader-pro"})
public final class LocalBook {

    @NotNull
    public static final LocalBook INSTANCE = new LocalBook();

    @NotNull
    private static final Pattern[] nameAuthorPatterns = {Pattern.compile("(.*?)《([^《》]+)》.*?作者：(.*)"), Pattern.compile("(.*?)《([^《》]+)》(.*)"), Pattern.compile("(^)(.+) 作者：(.+)$"), Pattern.compile("(^)(.+) by (.+)$")};

    private LocalBook() {
    }

    @NotNull
    public final InputStream getBookInputStream(@NotNull Book book) throws SecurityException, FileNotFoundException {
        Intrinsics.checkNotNullParameter(book, "book");
        File file = book.getLocalFile();
        if (file.exists()) {
            return new FileInputStream(file);
        }
        throw new FileNotFoundException(Intrinsics.stringPlus(book.getName(), " 文件不存在"));
    }

    @NotNull
    public final ArrayList<BookChapter> getChapterList(@NotNull Book book) throws Exception {
        ArrayList<BookChapter> chapterList;
        Intrinsics.checkNotNullParameter(book, "book");
        if (book.isEpub()) {
            chapterList = EpubFile.INSTANCE.getChapterList(book);
        } else if (book.isUmd()) {
            chapterList = UmdFile.INSTANCE.getChapterList(book);
        } else if (book.isCbz()) {
            chapterList = CbzFile.INSTANCE.getChapterList(book);
        } else if (book.isPdf()) {
            chapterList = PdfFile.INSTANCE.getChapterList(book);
        } else {
            chapterList = TextFile.INSTANCE.getChapterList(book);
        }
        ArrayList<BookChapter> arrayList = chapterList;
        if (arrayList.isEmpty()) {
            throw new TocEmptyException(Intrinsics.stringPlus("Chapterlist is empty  ", book.getLocalFile()));
        }
        return arrayList;
    }

    @Nullable
    public final String getContent(@NotNull Book book, @NotNull BookChapter chapter) {
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(chapter, NCXDocumentV2.NCXAttributeValues.chapter);
        if (book.isEpub()) {
            return EpubFile.INSTANCE.getContent(book, chapter);
        }
        if (book.isUmd()) {
            return UmdFile.INSTANCE.getContent(book, chapter);
        }
        if (book.isCbz()) {
            return CbzFile.INSTANCE.getContent(book, chapter);
        }
        if (book.isPdf()) {
            return PdfFile.INSTANCE.getContent(book, chapter);
        }
        return TextFile.INSTANCE.getContent(book, chapter);
    }

    @NotNull
    public final Pair<String, String> analyzeNameAuthor(@NotNull String fileName) {
        Intrinsics.checkNotNullParameter(fileName, "fileName");
        String tempFileName = StringsKt.substringBeforeLast$default(fileName, ".", (String) null, 2, (Object) null);
        Pattern[] patternArr = nameAuthorPatterns;
        int i = 0;
        int length = patternArr.length;
        while (i < length) {
            Pattern pattern = patternArr[i];
            i++;
            Matcher it = pattern.matcher(tempFileName);
            Matcher $this$analyzeNameAuthor_u24lambda_u2d1 = it.find() ? it : null;
            if ($this$analyzeNameAuthor_u24lambda_u2d1 != null) {
                Object name = $this$analyzeNameAuthor_u24lambda_u2d1.group(2);
                Intrinsics.checkNotNull(name);
                String strGroup = $this$analyzeNameAuthor_u24lambda_u2d1.group(1);
                String group1 = strGroup == null ? PackageDocumentBase.PREFIX_OPF : strGroup;
                String strGroup2 = $this$analyzeNameAuthor_u24lambda_u2d1.group(3);
                String group3 = strGroup2 == null ? PackageDocumentBase.PREFIX_OPF : strGroup2;
                Object author = BookHelp.INSTANCE.formatBookAuthor(Intrinsics.stringPlus(group1, group3));
                return new Pair<>(name, author);
            }
        }
        String bookName = BookHelp.INSTANCE.formatBookName(tempFileName);
        String it2 = BookHelp.INSTANCE.formatBookAuthor(StringsKt.replace$default(tempFileName, bookName, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null));
        Object obj = it2.length() != tempFileName.length() ? it2 : null;
        Object author2 = obj == null ? PackageDocumentBase.PREFIX_OPF : obj;
        return new Pair<>(bookName, author2);
    }

    public final void deleteBook(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "book");
        try {
            Result.Companion companion = Result.Companion;
            File bookFile = book.getLocalFile();
            if ((book.isLocalTxt() || book.isUmd()) && bookFile.exists()) {
                bookFile.delete();
            }
            if (book.isEpub()) {
                File bookFile2 = bookFile.getParentFile();
                Intrinsics.checkNotNullExpressionValue(bookFile2, "bookFile.parentFile");
                if (bookFile2.exists()) {
                    FileUtils.INSTANCE.delete(bookFile2, true);
                }
            }
            Result.constructor-impl(Unit.INSTANCE);
        } catch (Throwable th) {
            Result.Companion companion2 = Result.Companion;
            Result.constructor-impl(ResultKt.createFailure(th));
        }
    }
}
