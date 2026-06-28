package io.legado.app.model.localBook;

import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.TxtTocRule;
import io.legado.app.help.DefaultData;
import io.legado.app.utils.EncodingDetect;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.StringUtils;
import io.legado.app.utils.Utf8BomUtils;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: TextFile.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/localBook/TextFile.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\"\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002J,\u0010\r\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u00102\b\b\u0002\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0014H\u0002J\u0016\u0010\u0016\u001a\u0012\u0012\u0004\u0012\u00020\u000f0\u000ej\b\u0012\u0004\u0012\u00020\u000f`\u0010J\u0012\u0010\u0017\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\bX\u0082D¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lio/legado/app/model/localBook/TextFile;", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "(Lio/legado/app/data/entities/Book;)V", "blank", PackageDocumentBase.PREFIX_OPF, "bufferSize", PackageDocumentBase.PREFIX_OPF, "charset", "Ljava/nio/charset/Charset;", "maxLengthWithNoToc", "maxLengthWithToc", "analyze", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "pattern", "Ljava/util/regex/Pattern;", "fileStart", PackageDocumentBase.PREFIX_OPF, "fileEnd", "getChapterList", "getTocRule", "content", PackageDocumentBase.PREFIX_OPF, "getTocRules", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/TxtTocRule;", "Companion", "reader-pro"})
public final class TextFile {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

    @NotNull
    private final Book book;
    private final byte blank;
    private final int bufferSize;
    private final int maxLengthWithNoToc;
    private final int maxLengthWithToc;

    @NotNull
    private Charset charset;

    public TextFile(@NotNull Book book) {
        Intrinsics.checkNotNullParameter(book, "book");
        this.book = book;
        this.blank = (byte) 10;
        this.bufferSize = 512000;
        this.maxLengthWithNoToc = 10240;
        this.maxLengthWithToc = 102400;
        this.charset = this.book.fileCharset();
    }

    /* JADX INFO: compiled from: TextFile.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/model/localBook/TextFile$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0005¨\u0006\f"}, d2 = {"Lio/legado/app/model/localBook/TextFile$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "getChapterList", "Ljava/util/ArrayList;", "Lio/legado/app/data/entities/BookChapter;", "Lkotlin/collections/ArrayList;", "book", "Lio/legado/app/data/entities/Book;", "getContent", PackageDocumentBase.PREFIX_OPF, "bookChapter", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final ArrayList<BookChapter> getChapterList(@NotNull Book book) throws FileNotFoundException {
            Intrinsics.checkNotNullParameter(book, "book");
            return new TextFile(book).getChapterList();
        }

        @NotNull
        public final String getContent(@NotNull Book book, @NotNull BookChapter bookChapter) throws FileNotFoundException {
            Intrinsics.checkNotNullParameter(book, "book");
            Intrinsics.checkNotNullParameter(bookChapter, "bookChapter");
            Long end = bookChapter.getEnd();
            Intrinsics.checkNotNull(end);
            long jLongValue = end.longValue();
            Long start = bookChapter.getStart();
            Intrinsics.checkNotNull(start);
            int count = (int) (jLongValue - start.longValue());
            byte[] buffer = new byte[count];
            InputStream bookInputStream = LocalBook.INSTANCE.getBookInputStream(book);
            Throwable th = (Throwable) null;
            try {
                try {
                    InputStream bis = bookInputStream;
                    Long start2 = bookChapter.getStart();
                    Intrinsics.checkNotNull(start2);
                    bis.skip(start2.longValue());
                    bis.read(buffer);
                    CloseableKt.closeFinally(bookInputStream, th);
                    if (book.getCharset() == null) {
                        book.setCharset(EncodingDetect.INSTANCE.getEncode(book.getLocalFile()));
                    }
                    return new Regex("^[\\n\\s]+").replace(StringsKt.substringAfter$default(new String(buffer, book.fileCharset()), bookChapter.getTitle(), (String) null, 2, (Object) null), "\u3000\u3000");
                } finally {
                }
            } catch (Throwable th2) {
                CloseableKt.closeFinally(bookInputStream, th);
                throw th2;
            }
        }
    }

    @NotNull
    public final ArrayList<BookChapter> getChapterList() throws FileNotFoundException {
        String str;
        if (this.book.getCharset() == null || StringsKt.isBlank(this.book.getTocUrl())) {
            InputStream bookInputStream = LocalBook.INSTANCE.getBookInputStream(this.book);
            Throwable th = (Throwable) null;
            try {
                try {
                    InputStream bis = bookInputStream;
                    byte[] buffer = new byte[this.bufferSize];
                    int length = bis.read(buffer);
                    String charset = this.book.getCharset();
                    if (charset == null || StringsKt.isBlank(charset)) {
                        Book book = this.book;
                        EncodingDetect encodingDetect = EncodingDetect.INSTANCE;
                        byte[] bArrCopyOf = Arrays.copyOf(buffer, length);
                        Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "java.util.Arrays.copyOf(this, newSize)");
                        book.setCharset(encodingDetect.getEncode(bArrCopyOf));
                    }
                    this.charset = this.book.fileCharset();
                    if (StringsKt.isBlank(this.book.getTocUrl())) {
                        String blockContent = new String(buffer, 0, length, this.charset);
                        Book book2 = this.book;
                        Pattern tocRule = getTocRule(blockContent);
                        if (tocRule == null) {
                            str = PackageDocumentBase.PREFIX_OPF;
                        } else {
                            String strPattern = tocRule.pattern();
                            str = strPattern == null ? PackageDocumentBase.PREFIX_OPF : strPattern;
                        }
                        book2.setTocUrl(str);
                    }
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(bookInputStream, th);
                } finally {
                }
            } catch (Throwable th2) {
                CloseableKt.closeFinally(bookInputStream, th);
                throw th2;
            }
        }
        Pattern patternCompile = Pattern.compile(this.book.getTocUrl(), 8);
        Intrinsics.checkNotNullExpressionValue(patternCompile, "java.util.regex.Pattern.compile(this, flags)");
        ArrayList<BookChapter> arrayListAnalyze = analyze(patternCompile);
        ArrayList<BookChapter> $this$forEachIndexed$iv = arrayListAnalyze;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int index = index$iv;
            index$iv++;
            if (index < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            BookChapter bookChapter = (BookChapter) item$iv;
            bookChapter.setIndex(index);
            bookChapter.setBookUrl(this.book.getBookUrl());
            bookChapter.setUrl(MD5Utils.INSTANCE.md5Encode16(this.book.getOriginName() + index + bookChapter.getTitle()));
        }
        this.book.setLatestChapterTitle(((BookChapter) CollectionsKt.last(arrayListAnalyze)).getTitle());
        this.book.setTotalChapterNum(arrayListAnalyze.size());
        return arrayListAnalyze;
    }

    private final ArrayList<BookChapter> analyze(Pattern pattern) throws FileNotFoundException {
        Long start;
        int length;
        String strPattern = pattern == null ? null : pattern.pattern();
        if (strPattern == null || strPattern.length() == 0) {
            return analyze$default(this, 0L, 0L, 3, null);
        }
        if (pattern == null) {
            return analyze$default(this, 0L, 0L, 3, null);
        }
        ArrayList<BookChapter> arrayList = new ArrayList<>();
        InputStream bookInputStream = LocalBook.INSTANCE.getBookInputStream(this.book);
        Throwable th = (Throwable) null;
        try {
            InputStream bis = bookInputStream;
            long curOffset = 0;
            byte[] buffer = new byte[this.bufferSize];
            int bufferStart = 3;
            bis.read(buffer, 0, 3);
            if (Utf8BomUtils.INSTANCE.hasBom(buffer)) {
                bufferStart = 0;
                curOffset = 3;
            }
            while (true) {
                int it = bis.read(buffer, bufferStart, this.bufferSize - bufferStart);
                Unit unit = Unit.INSTANCE;
                if (it <= 0) {
                    Unit unit2 = Unit.INSTANCE;
                    CloseableKt.closeFinally(bookInputStream, th);
                    System.gc();
                    System.runFinalization();
                    return arrayList;
                }
                int end = bufferStart + it;
                if (end == this.bufferSize) {
                    int i = (bufferStart + it) - 1;
                    if (0 <= i) {
                        while (true) {
                            int i2 = i;
                            i--;
                            if (buffer[i2] == this.blank) {
                                end = i2;
                                break;
                            }
                            if (0 > i) {
                                break;
                            }
                        }
                    }
                }
                String blockContent = new String(buffer, 0, end, this.charset);
                ArraysKt.copyInto(buffer, buffer, 0, end, bufferStart + it);
                bufferStart = (bufferStart + it) - end;
                int length2 = end;
                int seekPos = 0;
                Matcher matcher = pattern.matcher(blockContent);
                Intrinsics.checkNotNullExpressionValue(matcher, "pattern.matcher(blockContent)");
                while (matcher.find()) {
                    int chapterStart = matcher.start();
                    String chapterContent = blockContent.substring(seekPos, chapterStart);
                    Intrinsics.checkNotNullExpressionValue(chapterContent, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                    Charset charset = this.charset;
                    if (chapterContent == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    byte[] bytes = chapterContent.getBytes(charset);
                    Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                    int chapterLength = bytes.length;
                    BookChapter bookChapter = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                    long jLongValue = (bookChapter == null || (start = bookChapter.getStart()) == null) ? curOffset : start.longValue();
                    long lastStart = jLongValue;
                    if (this.book.getSplitLongChapter() && (curOffset + ((long) chapterLength)) - lastStart > this.maxLengthWithToc) {
                        BookChapter it2 = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                        if (it2 != null) {
                            it2.setEnd(it2.getStart());
                            Unit unit3 = Unit.INSTANCE;
                            Unit unit4 = Unit.INSTANCE;
                        }
                        BookChapter bookChapter2 = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                        String lastTitle = bookChapter2 == null ? null : bookChapter2.getTitle();
                        if (lastTitle == null) {
                            length = 0;
                        } else {
                            byte[] bytes2 = lastTitle.getBytes(this.charset);
                            Intrinsics.checkNotNullExpressionValue(bytes2, "(this as java.lang.String).getBytes(charset)");
                            length = bytes2 == null ? 0 : bytes2.length;
                        }
                        int lastTitleLength = length;
                        Iterable iterableAnalyze = analyze(lastStart + ((long) lastTitleLength), curOffset + ((long) chapterLength));
                        if (lastTitle != null) {
                            Iterable $this$forEachIndexed$iv = iterableAnalyze;
                            int index$iv = 0;
                            for (Object item$iv : $this$forEachIndexed$iv) {
                                int index = index$iv;
                                index$iv++;
                                if (index < 0) {
                                    CollectionsKt.throwIndexOverflow();
                                }
                                BookChapter bookChapter3 = (BookChapter) item$iv;
                                bookChapter3.setTitle(new StringBuilder().append((Object) lastTitle).append('(').append(index + 1).append(')').toString());
                            }
                            Unit unit5 = Unit.INSTANCE;
                            Unit unit6 = Unit.INSTANCE;
                        }
                        arrayList.addAll((Collection) iterableAnalyze);
                        BookChapter curChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        String strGroup = matcher.group();
                        Intrinsics.checkNotNullExpressionValue(strGroup, "matcher.group()");
                        curChapter.setTitle(strGroup);
                        curChapter.setStart(Long.valueOf(curOffset + ((long) chapterLength)));
                        arrayList.add(curChapter);
                    } else if (seekPos != 0 || chapterStart == 0) {
                        if (!arrayList.isEmpty()) {
                            BookChapter lastChapter = (BookChapter) CollectionsKt.last(arrayList);
                            lastChapter.setVolume(StringsKt.isBlank(StringsKt.substringAfter$default(chapterContent, lastChapter.getTitle(), (String) null, 2, (Object) null)));
                            Long start2 = lastChapter.getStart();
                            Intrinsics.checkNotNull(start2);
                            long jLongValue2 = start2.longValue();
                            Charset charset2 = this.charset;
                            if (chapterContent == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            byte[] bytes3 = chapterContent.getBytes(charset2);
                            Intrinsics.checkNotNullExpressionValue(bytes3, "(this as java.lang.String).getBytes(charset)");
                            lastChapter.setEnd(Long.valueOf(jLongValue2 + ((long) bytes3.length)));
                            BookChapter curChapter2 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                            String strGroup2 = matcher.group();
                            Intrinsics.checkNotNullExpressionValue(strGroup2, "matcher.group()");
                            curChapter2.setTitle(strGroup2);
                            curChapter2.setStart(lastChapter.getEnd());
                            arrayList.add(curChapter2);
                        } else {
                            BookChapter curChapter3 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                            String strGroup3 = matcher.group();
                            Intrinsics.checkNotNullExpressionValue(strGroup3, "matcher.group()");
                            curChapter3.setTitle(strGroup3);
                            curChapter3.setStart(Long.valueOf(curOffset));
                            curChapter3.setEnd(Long.valueOf(curOffset));
                            arrayList.add(curChapter3);
                        }
                    } else if (arrayList.isEmpty()) {
                        if (StringUtils.INSTANCE.trim(chapterContent).length() > 0) {
                            BookChapter qyChapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                            qyChapter.setTitle("前言");
                            qyChapter.setStart(Long.valueOf(curOffset));
                            qyChapter.setEnd(Long.valueOf(chapterLength));
                            arrayList.add(qyChapter);
                        }
                        BookChapter curChapter4 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        String strGroup4 = matcher.group();
                        Intrinsics.checkNotNullExpressionValue(strGroup4, "matcher.group()");
                        curChapter4.setTitle(strGroup4);
                        curChapter4.setStart(Long.valueOf(chapterLength));
                        arrayList.add(curChapter4);
                    } else {
                        BookChapter lastChapter2 = (BookChapter) CollectionsKt.last(arrayList);
                        lastChapter2.setVolume(StringsKt.isBlank(StringsKt.substringAfter$default(chapterContent, lastChapter2.getTitle(), (String) null, 2, (Object) null)));
                        Long end2 = lastChapter2.getEnd();
                        Intrinsics.checkNotNull(end2);
                        lastChapter2.setEnd(Long.valueOf(end2.longValue() + ((long) chapterLength)));
                        BookChapter curChapter5 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        String strGroup5 = matcher.group();
                        Intrinsics.checkNotNullExpressionValue(strGroup5, "matcher.group()");
                        curChapter5.setTitle(strGroup5);
                        curChapter5.setStart(lastChapter2.getEnd());
                        arrayList.add(curChapter5);
                    }
                    seekPos += chapterContent.length();
                }
                curOffset += (long) length2;
                BookChapter bookChapter4 = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                if (bookChapter4 != null) {
                    bookChapter4.setEnd(Long.valueOf(curOffset));
                }
            }
        } catch (Throwable th2) {
            CloseableKt.closeFinally(bookInputStream, th);
            throw th2;
        }
    }

    static /* synthetic */ ArrayList analyze$default(TextFile textFile, long j, long j2, int i, Object obj) {
        if ((i & 1) != 0) {
            j = 0;
        }
        if ((i & 2) != 0) {
            j2 = Long.MAX_VALUE;
        }
        return textFile.analyze(j, j2);
    }

    private final ArrayList<BookChapter> analyze(long fileStart, long fileEnd) throws FileNotFoundException {
        Long lValueOf;
        Boolean boolValueOf;
        int it;
        Long lValueOf2;
        ArrayList<BookChapter> arrayList = new ArrayList<>();
        InputStream bookInputStream = LocalBook.INSTANCE.getBookInputStream(this.book);
        Throwable th = (Throwable) null;
        try {
            InputStream bis = bookInputStream;
            int blockPos = 0;
            long curOffset = 0;
            int chapterPos = 0;
            byte[] buffer = new byte[this.bufferSize];
            int bufferStart = 3;
            if (fileStart == 0) {
                bis.read(buffer, 0, 3);
                if (Utf8BomUtils.INSTANCE.hasBom(buffer)) {
                    bufferStart = 0;
                    curOffset = 3;
                }
            } else {
                bis.skip(fileStart);
                curOffset = fileStart;
                bufferStart = 0;
            }
            while ((fileEnd - curOffset) - ((long) bufferStart) > 0 && (it = bis.read(buffer, bufferStart, (int) Math.min(this.bufferSize - bufferStart, (fileEnd - curOffset) - ((long) bufferStart)))) > 0) {
                blockPos++;
                int chapterOffset = 0;
                int length = it + bufferStart;
                int strLength = length;
                chapterPos = 0;
                while (strLength > 0) {
                    chapterPos++;
                    if (strLength > this.maxLengthWithNoToc) {
                        int end = length;
                        int i = chapterOffset + this.maxLengthWithNoToc;
                        if (i < length) {
                            while (true) {
                                int i2 = i;
                                i++;
                                if (buffer[i2] == this.blank) {
                                    end = i2;
                                    break;
                                }
                                if (i >= length) {
                                    break;
                                }
                            }
                        }
                        BookChapter chapter = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                        chapter.setTitle((char) 31532 + blockPos + "章(" + chapterPos + ')');
                        BookChapter bookChapter = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                        if (bookChapter == null) {
                            lValueOf2 = Long.valueOf(curOffset);
                        } else {
                            Long end2 = bookChapter.getEnd();
                            lValueOf2 = end2 == null ? Long.valueOf(curOffset) : end2;
                        }
                        chapter.setStart(lValueOf2);
                        Long start = chapter.getStart();
                        Intrinsics.checkNotNull(start);
                        chapter.setEnd(Long.valueOf((start.longValue() + ((long) end)) - ((long) chapterOffset)));
                        arrayList.add(chapter);
                        strLength -= end - chapterOffset;
                        chapterOffset = end;
                    } else {
                        ArraysKt.copyInto(buffer, buffer, 0, length - strLength, length);
                        length -= strLength;
                        bufferStart = strLength;
                        strLength = 0;
                    }
                }
                curOffset += (long) length;
            }
            if (bufferStart > 100 || arrayList.isEmpty()) {
                BookChapter chapter2 = new BookChapter(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
                chapter2.setTitle((char) 31532 + blockPos + "章(" + chapterPos + ')');
                BookChapter bookChapter2 = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                if (bookChapter2 == null) {
                    lValueOf = Long.valueOf(curOffset);
                } else {
                    Long end3 = bookChapter2.getEnd();
                    lValueOf = end3 == null ? Long.valueOf(curOffset) : end3;
                }
                chapter2.setStart(lValueOf);
                Long start2 = chapter2.getStart();
                Intrinsics.checkNotNull(start2);
                chapter2.setEnd(Long.valueOf(start2.longValue() + ((long) bufferStart)));
                boolValueOf = Boolean.valueOf(arrayList.add(chapter2));
            } else {
                BookChapter it2 = (BookChapter) CollectionsKt.lastOrNull(arrayList);
                if (it2 == null) {
                    boolValueOf = null;
                } else {
                    Long end4 = it2.getEnd();
                    Intrinsics.checkNotNull(end4);
                    it2.setEnd(Long.valueOf(end4.longValue() + ((long) bufferStart)));
                    boolValueOf = Unit.INSTANCE;
                }
            }
            return arrayList;
        } finally {
            CloseableKt.closeFinally(bookInputStream, th);
        }
    }

    private final Pattern getTocRule(String content) {
        int cs;
        List<TxtTocRule> rules = CollectionsKt.reversed(getTocRules());
        int maxCs = 1;
        Pattern tocPattern = null;
        for (TxtTocRule tocRule : rules) {
            Pattern pattern = Pattern.compile(tocRule.getRule(), 8);
            Intrinsics.checkNotNullExpressionValue(pattern, "java.util.regex.Pattern.compile(this, flags)");
            Matcher matcher = pattern.matcher(content);
            int i = 0;
            while (true) {
                cs = i;
                if (!matcher.find()) {
                    break;
                }
                i = cs + 1;
            }
            if (cs >= maxCs) {
                maxCs = cs;
                tocPattern = pattern;
            }
        }
        return tocPattern;
    }

    private final List<TxtTocRule> getTocRules() {
        Iterable $this$filter$iv = DefaultData.INSTANCE.getTxtTocRules();
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filter$iv) {
            TxtTocRule it = (TxtTocRule) element$iv$iv;
            if (it.getEnable()) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        return (List) destination$iv$iv;
    }
}
