package io.legado.app.help;

import io.legado.app.constant.AppPattern;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.utils.FileExtensionsKt;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.NetworkUtils;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Deferred;
import kotlinx.coroutines.DelayKt;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BookHelp.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/BookHelp.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\r\u001a\u00020\u00042\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004J\u000e\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u000e\u001a\u00020\u0004J\u000e\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u0004J\u0010\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0004H\u0002J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\nJ\u0018\u0010\u0016\u001a\u0004\u0018\u00010\u00042\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0016\u0010\u0017\u001a\u00020\u00152\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0004J\u000e\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u0004J9\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010 J+\u0010!\u001a\u00020\b2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u0004H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\"J9\u0010#\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010 J\u001e\u0010$\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u0004R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u0006X\u0082\u0004¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006%"}, d2 = {"Lio/legado/app/help/BookHelp;", PackageDocumentBase.PREFIX_OPF, "()V", "cacheImageFolderName", PackageDocumentBase.PREFIX_OPF, "downloadImages", "Ljava/util/concurrent/CopyOnWriteArraySet;", "delContent", PackageDocumentBase.PREFIX_OPF, "book", "Lio/legado/app/data/entities/Book;", "bookChapter", "Lio/legado/app/data/entities/BookChapter;", "formatAuthor", "author", "formatBookAuthor", "formatBookName", "name", "formatFolderName", "folderName", "getBookCacheDir", "Ljava/io/File;", "getContent", "getImage", NCXDocumentV2.NCXAttributes.src, "getImageSuffix", "saveContent", "scope", "Lkotlinx/coroutines/CoroutineScope;", "bookSource", "Lio/legado/app/data/entities/BookSource;", "content", "(Lkotlinx/coroutines/CoroutineScope;Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveImage", "(Lio/legado/app/data/entities/BookSource;Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveImages", "saveText", "reader-pro"})
public final class BookHelp {

    @NotNull
    private static final String cacheImageFolderName = "images";

    @NotNull
    public static final BookHelp INSTANCE = new BookHelp();

    @NotNull
    private static final CopyOnWriteArraySet<String> downloadImages = new CopyOnWriteArraySet<>();

    /* JADX INFO: renamed from: io.legado.app.help.BookHelp$saveImage$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookHelp.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/BookHelp$saveImage$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookHelp.kt", l = {152, 160}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"this", "bookSource", "book", NCXDocumentV2.NCXAttributes.src}, m = "saveImage", c = "io.legado.app.help.BookHelp")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookHelp.this.saveImage(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: io.legado.app.help.BookHelp$saveImages$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookHelp.kt */
    /* JADX INFO: loaded from: app-classes.jar:io/legado/app/help/BookHelp$saveImages$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookHelp.kt", l = {146}, i = {}, s = {}, n = {}, m = "saveImages", c = "io.legado.app.help.BookHelp")
    static final class C01451 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C01451(Continuation<? super C01451> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookHelp.this.saveImages(null, null, null, null, null, (Continuation) this);
        }
    }

    private BookHelp() {
    }

    private final String formatFolderName(String folderName) {
        return new Regex("[\\\\/:*?\"<>|.]").replace(folderName, PackageDocumentBase.PREFIX_OPF);
    }

    @NotNull
    public final String formatAuthor(@Nullable String author) {
        if (author == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        String strReplace = new Regex("作\\s*者[\\s:：]*").replace(author, PackageDocumentBase.PREFIX_OPF);
        if (strReplace == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        CharSequence $this$trim$iv = new Regex("\\s+").replace(strReplace, " ");
        if ($this$trim$iv == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        CharSequence $this$trim$iv$iv = $this$trim$iv;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
            if (startFound$iv$iv) {
                if (!match$iv$iv) {
                    break;
                }
                endIndex$iv$iv--;
            } else if (match$iv$iv) {
                startIndex$iv$iv++;
            } else {
                startFound$iv$iv = true;
            }
        }
        String string = $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
        return string == null ? PackageDocumentBase.PREFIX_OPF : string;
    }

    @NotNull
    public final String formatBookName(@NotNull String name) {
        Intrinsics.checkNotNullParameter(name, "name");
        CharSequence $this$trim$iv = AppPattern.INSTANCE.getNameRegex().replace(name, PackageDocumentBase.PREFIX_OPF);
        CharSequence $this$trim$iv$iv = $this$trim$iv;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                } else {
                    startIndex$iv$iv++;
                }
            } else {
                if (!match$iv$iv) {
                    break;
                }
                endIndex$iv$iv--;
            }
        }
        return $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
    }

    @NotNull
    public final String formatBookAuthor(@NotNull String author) {
        Intrinsics.checkNotNullParameter(author, "author");
        CharSequence $this$trim$iv = AppPattern.INSTANCE.getAuthorRegex().replace(author, PackageDocumentBase.PREFIX_OPF);
        CharSequence $this$trim$iv$iv = $this$trim$iv;
        int startIndex$iv$iv = 0;
        int endIndex$iv$iv = $this$trim$iv$iv.length() - 1;
        boolean startFound$iv$iv = false;
        while (startIndex$iv$iv <= endIndex$iv$iv) {
            int index$iv$iv = !startFound$iv$iv ? startIndex$iv$iv : endIndex$iv$iv;
            char it = $this$trim$iv$iv.charAt(index$iv$iv);
            boolean match$iv$iv = Intrinsics.compare(it, 32) <= 0;
            if (!startFound$iv$iv) {
                if (!match$iv$iv) {
                    startFound$iv$iv = true;
                } else {
                    startIndex$iv$iv++;
                }
            } else {
                if (!match$iv$iv) {
                    break;
                }
                endIndex$iv$iv--;
            }
        }
        return $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
    }

    @NotNull
    public final File getBookCacheDir(@NotNull Book book) throws Exception {
        Intrinsics.checkNotNullParameter(book, "book");
        String md5Encode = MD5Utils.INSTANCE.md5Encode(book.getBookUrl()).toString();
        String bookDir = book.getBookDir();
        if (bookDir.length() == 0) {
            throw new Exception("bookDir不能为空");
        }
        File localCacheDir = FileExtensionsKt.getFile(new File(bookDir), md5Encode);
        if (!localCacheDir.exists()) {
            localCacheDir.mkdirs();
        }
        return localCacheDir;
    }

    @Nullable
    public final String getContent(@NotNull Book book, @NotNull BookChapter bookChapter) {
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(bookChapter, "bookChapter");
        File bookCacheDir = getBookCacheDir(book);
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objArr = {Integer.valueOf(bookChapter.getIndex())};
        String str = String.format("%d.txt", Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
        File file = FileExtensionsKt.getFile(bookCacheDir, str);
        if (file.exists()) {
            return FilesKt.readText$default(file, (Charset) null, 1, (Object) null);
        }
        if (book.isLocalBook()) {
            String content = LocalBook.INSTANCE.getContent(book, bookChapter);
            if (content != null && book.isEpub()) {
                saveText(book, bookChapter, content);
            }
            return content;
        }
        return null;
    }

    public final void delContent(@NotNull Book book, @NotNull BookChapter bookChapter) throws Exception {
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(bookChapter, "bookChapter");
        FileUtils fileUtils = FileUtils.INSTANCE;
        File bookCacheDir = getBookCacheDir(book);
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objArr = {Integer.valueOf(bookChapter.getIndex())};
        String str = String.format("%d.txt", Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
        fileUtils.createFileIfNotExist(bookCacheDir, str).delete();
    }

    @Nullable
    public final Object saveContent(@NotNull CoroutineScope scope, @NotNull BookSource bookSource, @NotNull Book book, @NotNull BookChapter bookChapter, @NotNull String content, @NotNull Continuation<? super Unit> $completion) throws Exception {
        saveText(book, bookChapter, content);
        Object objSaveImages = saveImages(scope, bookSource, book, bookChapter, content, $completion);
        return objSaveImages == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objSaveImages : Unit.INSTANCE;
    }

    public final void saveText(@NotNull Book book, @NotNull BookChapter bookChapter, @NotNull String content) throws Exception {
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(bookChapter, "bookChapter");
        Intrinsics.checkNotNullParameter(content, "content");
        FileUtils fileUtils = FileUtils.INSTANCE;
        File bookCacheDir = getBookCacheDir(book);
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objArr = {Integer.valueOf(bookChapter.getIndex())};
        String str = String.format("%d.txt", Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
        FilesKt.writeText$default(fileUtils.createFileIfNotExist(bookCacheDir, str), content, (Charset) null, 2, (Object) null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveImages(@NotNull CoroutineScope scope, @NotNull BookSource bookSource, @NotNull Book book, @NotNull BookChapter bookChapter, @NotNull String content, @NotNull Continuation<? super Unit> $completion) {
        C01451 c01451;
        Iterator it;
        String src;
        if ($completion instanceof C01451) {
            c01451 = (C01451) $completion;
            if ((c01451.label & Integer.MIN_VALUE) != 0) {
                c01451.label -= Integer.MIN_VALUE;
            } else {
                c01451 = new C01451($completion);
            }
        }
        Object $result = c01451.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c01451.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                ArrayList awaitList = new ArrayList();
                Iterable $this$forEach$iv = StringsKt.split$default(content, new String[]{"\n"}, false, 0, 6, (Object) null);
                for (Object element$iv : $this$forEach$iv) {
                    String it2 = (String) element$iv;
                    Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(it2);
                    if (matcher.find() && (src = matcher.group(1)) != null) {
                        String mSrc = NetworkUtils.INSTANCE.getAbsoluteURL(bookChapter.getUrl(), src);
                        Deferred req = BuildersKt.async$default(scope, (CoroutineContext) null, (CoroutineStart) null, new BookHelp$saveImages$2$1$req$1(bookSource, book, mSrc, null), 3, (Object) null);
                        Boxing.boxBoolean(awaitList.add(req));
                    }
                }
                ArrayList $this$forEach$iv2 = awaitList;
                it = $this$forEach$iv2.iterator();
                break;
            case 1:
                it = (Iterator) c01451.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        while (it.hasNext()) {
            Object element$iv2 = it.next();
            Deferred it3 = (Deferred) element$iv2;
            c01451.L$0 = it;
            c01451.label = 1;
            if (it3.await(c01451) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x002b  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveImage(@Nullable BookSource bookSource, @NotNull Book book, @NotNull String src, @NotNull Continuation<? super Unit> $completion) {
        AnonymousClass1 anonymousClass1;
        Object byteArrayAwait;
        if ($completion instanceof AnonymousClass1) {
            anonymousClass1 = (AnonymousClass1) $completion;
            if ((anonymousClass1.label & Integer.MIN_VALUE) != 0) {
                anonymousClass1.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass1 = new AnonymousClass1($completion);
            }
        }
        Object $result = anonymousClass1.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            try {
            } catch (Exception e) {
                e.printStackTrace();
                downloadImages.remove(src);
            }
            switch (anonymousClass1.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    while (downloadImages.contains(src)) {
                        anonymousClass1.L$0 = this;
                        anonymousClass1.L$1 = bookSource;
                        anonymousClass1.L$2 = book;
                        anonymousClass1.L$3 = src;
                        anonymousClass1.label = 1;
                        if (DelayKt.delay(100L, anonymousClass1) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    if (!this.getImage(book, src).exists()) {
                        return Unit.INSTANCE;
                    }
                    downloadImages.add(src);
                    AnalyzeUrl analyzeUrl = new AnalyzeUrl(src, null, null, null, null, null, bookSource, null, null, null, null, 1982, null);
                    anonymousClass1.L$0 = book;
                    anonymousClass1.L$1 = src;
                    anonymousClass1.L$2 = null;
                    anonymousClass1.L$3 = null;
                    anonymousClass1.label = 2;
                    byteArrayAwait = analyzeUrl.getByteArrayAwait(anonymousClass1);
                    if (byteArrayAwait == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    byte[] it = (byte[]) byteArrayAwait;
                    FilesKt.writeBytes(FileUtils.INSTANCE.createFileIfNotExist(INSTANCE.getBookCacheDir(book), cacheImageFolderName, MD5Utils.INSTANCE.md5Encode16(src) + '.' + INSTANCE.getImageSuffix(src)), it);
                    downloadImages.remove(src);
                    return Unit.INSTANCE;
                case 1:
                    src = (String) anonymousClass1.L$3;
                    book = (Book) anonymousClass1.L$2;
                    bookSource = (BookSource) anonymousClass1.L$1;
                    this = (BookHelp) anonymousClass1.L$0;
                    ResultKt.throwOnFailure($result);
                    while (downloadImages.contains(src)) {
                    }
                    if (!this.getImage(book, src).exists()) {
                    }
                    break;
                case 2:
                    src = (String) anonymousClass1.L$1;
                    book = (Book) anonymousClass1.L$0;
                    ResultKt.throwOnFailure($result);
                    byteArrayAwait = $result;
                    byte[] it2 = (byte[]) byteArrayAwait;
                    FilesKt.writeBytes(FileUtils.INSTANCE.createFileIfNotExist(INSTANCE.getBookCacheDir(book), cacheImageFolderName, MD5Utils.INSTANCE.md5Encode16(src) + '.' + INSTANCE.getImageSuffix(src)), it2);
                    downloadImages.remove(src);
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            downloadImages.remove(src);
            throw th;
        }
    }

    @NotNull
    public final File getImage(@NotNull Book book, @NotNull String src) {
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        return FileExtensionsKt.getFile(getBookCacheDir(book), cacheImageFolderName, MD5Utils.INSTANCE.md5Encode16(src) + '.' + getImageSuffix(src));
    }

    @NotNull
    public final String getImageSuffix(@NotNull String src) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        String suffix = StringsKt.substringBefore$default(StringsKt.substringAfterLast$default(src, ".", (String) null, 2, (Object) null), ",", (String) null, 2, (Object) null);
        Regex fileSuffixRegex = new Regex("^[a-z0-9]+$", RegexOption.IGNORE_CASE);
        if (suffix.length() > 5 || !fileSuffixRegex.matches(suffix)) {
            suffix = "jpg";
        }
        return suffix;
    }
}
