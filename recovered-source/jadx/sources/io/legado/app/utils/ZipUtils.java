package io.legado.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: ZipUtils.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/ZipUtils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0004\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0012\u0010\u0007\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0018\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006J\u0016\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\nJ\u0012\u0010\r\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u0018\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006J\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\nJ\u0012\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\nH\u0002J6\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00062\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\nH\u0002J\u001e\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006J\u001e\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ,\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u00062\b\u0010\u001e\u001a\u0004\u0018\u00010\nJ(\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\n2\b\u0010\u001e\u001a\u0004\u0018\u00010\nJ*\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J(\u0010\u000b\u001a\u00020\u00042\b\u0010\u001f\u001a\u0004\u0018\u00010\u00062\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\nH\u0007J\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nJ\u001e\u0010\u000b\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010#\u001a\u00020\nJ.\u0010%\u001a\u00020\u00042\u000e\u0010&\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010'2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\nH\u0007J'\u0010%\u001a\u00020\u00042\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0'2\u0006\u0010\f\u001a\u00020\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010(J5\u0010%\u001a\u00020\u00042\u000e\u0010)\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010'2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010*\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006+"}, d2 = {"Lio/legado/app/utils/ZipUtils;", PackageDocumentBase.PREFIX_OPF, "()V", "createOrExistsDir", PackageDocumentBase.PREFIX_OPF, "file", "Ljava/io/File;", "createOrExistsFile", "getComments", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "zipFile", "zipFilePath", "getFileByPath", "filePath", "getFilesPath", "isSpace", "s", "unzipChildFile", "destDir", "files", PackageDocumentBase.PREFIX_OPF, "zip", "Ljava/util/zip/ZipFile;", "entry", "Ljava/util/zip/ZipEntry;", "name", "unzipFile", "destDirPath", "unzipFileByKeyword", "keyword", "srcFile", "rootPath", "zos", "Ljava/util/zip/ZipOutputStream;", "comment", "srcFilePath", "zipFiles", "srcFiles", PackageDocumentBase.PREFIX_OPF, "(Ljava/util/Collection;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "srcFilePaths", "(Ljava/util/Collection;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro"})
public final class ZipUtils {

    @NotNull
    public static final ZipUtils INSTANCE = new ZipUtils();

    @JvmOverloads
    public final boolean zipFiles(@Nullable Collection<? extends File> srcFiles, @Nullable File zipFile) throws IOException {
        return zipFiles$default(this, srcFiles, zipFile, null, 4, null);
    }

    @JvmOverloads
    public final boolean zipFile(@Nullable File srcFile, @Nullable File zipFile) throws IOException {
        return zipFile$default(this, srcFile, zipFile, null, 4, null);
    }

    private ZipUtils() {
    }

    @Nullable
    public final Object zipFiles(@NotNull Collection<String> srcFiles, @NotNull String zipFilePath, @NotNull Continuation<? super Boolean> $completion) {
        return zipFiles(srcFiles, zipFilePath, null, $completion);
    }

    /* JADX INFO: renamed from: io.legado.app.utils.ZipUtils$zipFiles$3, reason: invalid class name */
    /* JADX INFO: compiled from: ZipUtils.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/ZipUtils$zipFiles$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "ZipUtils.kt", l = {}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "io.legado.app.utils.ZipUtils$zipFiles$3")
    static final class AnonymousClass3 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Boolean>, Object> {
        int label;
        final /* synthetic */ Collection<String> $srcFilePaths;
        final /* synthetic */ String $zipFilePath;
        final /* synthetic */ String $comment;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass3(Collection<String> $srcFilePaths, String $zipFilePath, String $comment, Continuation<? super AnonymousClass3> $completion) {
            super(2, $completion);
            this.$srcFilePaths = $srcFilePaths;
            this.$zipFilePath = $zipFilePath;
            this.$comment = $comment;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new AnonymousClass3(this.$srcFilePaths, this.$zipFilePath, this.$comment, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Boolean> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    if (this.$srcFilePaths == null || this.$zipFilePath == null) {
                        return Boxing.boxBoolean(false);
                    }
                    ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(this.$zipFilePath));
                    Collection<String> collection = this.$srcFilePaths;
                    String str = this.$comment;
                    Throwable th = (Throwable) null;
                    try {
                        ZipOutputStream it = zipOutputStream;
                        for (String srcFile : collection) {
                            ZipUtils zipUtils = ZipUtils.INSTANCE;
                            File fileByPath = ZipUtils.INSTANCE.getFileByPath(srcFile);
                            Intrinsics.checkNotNull(fileByPath);
                            if (!zipUtils.zipFile(fileByPath, PackageDocumentBase.PREFIX_OPF, it, str)) {
                                Boolean boolBoxBoolean = Boxing.boxBoolean(false);
                                CloseableKt.closeFinally(zipOutputStream, th);
                                return boolBoxBoolean;
                            }
                        }
                        Boolean boolBoxBoolean2 = Boxing.boxBoolean(true);
                        CloseableKt.closeFinally(zipOutputStream, th);
                        return boolBoxBoolean2;
                    } catch (Throwable th2) {
                        CloseableKt.closeFinally(zipOutputStream, th);
                        throw th2;
                    }
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    @Nullable
    public final Object zipFiles(@Nullable Collection<String> srcFilePaths, @Nullable String zipFilePath, @Nullable String comment, @NotNull Continuation<? super Boolean> $completion) {
        return BuildersKt.withContext(Dispatchers.getIO(), new AnonymousClass3(srcFilePaths, zipFilePath, comment, null), $completion);
    }

    public static /* synthetic */ boolean zipFiles$default(ZipUtils zipUtils, Collection collection, File file, String str, int i, Object obj) throws IOException {
        if ((i & 4) != 0) {
            str = null;
        }
        return zipUtils.zipFiles((Collection<? extends File>) collection, file, str);
    }

    @JvmOverloads
    public final boolean zipFiles(@Nullable Collection<? extends File> srcFiles, @Nullable File zipFile, @Nullable String comment) throws IOException {
        if (srcFiles == null || zipFile == null) {
            return false;
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        Throwable th = (Throwable) null;
        try {
            try {
                ZipOutputStream it = zipOutputStream;
                for (File srcFile : srcFiles) {
                    if (!INSTANCE.zipFile(srcFile, PackageDocumentBase.PREFIX_OPF, it, comment)) {
                        CloseableKt.closeFinally(zipOutputStream, th);
                        return false;
                    }
                }
                CloseableKt.closeFinally(zipOutputStream, th);
                return true;
            } catch (Throwable th2) {
                th = th2;
                throw th2;
            }
        } catch (Throwable th3) {
            CloseableKt.closeFinally(zipOutputStream, th);
            throw th3;
        }
    }

    public final boolean zipFile(@NotNull String srcFilePath, @NotNull String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter(srcFilePath, "srcFilePath");
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), (String) null);
    }

    public final boolean zipFile(@NotNull String srcFilePath, @NotNull String zipFilePath, @NotNull String comment) throws IOException {
        Intrinsics.checkNotNullParameter(srcFilePath, "srcFilePath");
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter(comment, "comment");
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), comment);
    }

    public static /* synthetic */ boolean zipFile$default(ZipUtils zipUtils, File file, File file2, String str, int i, Object obj) throws IOException {
        if ((i & 4) != 0) {
            str = null;
        }
        return zipUtils.zipFile(file, file2, str);
    }

    @JvmOverloads
    public final boolean zipFile(@Nullable File srcFile, @Nullable File zipFile, @Nullable String comment) throws IOException {
        if (srcFile == null || zipFile == null) {
            return false;
        }
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        Throwable th = (Throwable) null;
        try {
            try {
                ZipOutputStream zos = zipOutputStream;
                boolean zZipFile = INSTANCE.zipFile(srcFile, PackageDocumentBase.PREFIX_OPF, zos, comment);
                CloseableKt.closeFinally(zipOutputStream, th);
                return zZipFile;
            } catch (Throwable th2) {
                th = th2;
                throw th2;
            }
        } catch (Throwable th3) {
            CloseableKt.closeFinally(zipOutputStream, th);
            throw th3;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean zipFile(File srcFile, String rootPath, ZipOutputStream zos, String comment) throws IOException {
        if (!srcFile.exists()) {
            return true;
        }
        String str = rootPath + ((Object) (isSpace(rootPath) ? PackageDocumentBase.PREFIX_OPF : File.separator)) + ((Object) srcFile.getName());
        if (srcFile.isDirectory()) {
            File[] fileList = srcFile.listFiles();
            if (fileList != null) {
                if (!(fileList.length == 0)) {
                    int i = 0;
                    int length = fileList.length;
                    while (i < length) {
                        File file = fileList[i];
                        i++;
                        Intrinsics.checkNotNullExpressionValue(file, "file");
                        if (!zipFile(file, str, zos, comment)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            ZipEntry entry = new ZipEntry(Intrinsics.stringPlus(str, TableOfContents.DEFAULT_PATH_SEPARATOR));
            entry.setComment(comment);
            zos.putNextEntry(entry);
            zos.closeEntry();
            return true;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
        Throwable th = (Throwable) null;
        try {
            try {
                BufferedInputStream is = bufferedInputStream;
                ZipEntry entry2 = new ZipEntry(str);
                entry2.setComment(comment);
                zos.putNextEntry(entry2);
                zos.write(ByteStreamsKt.readBytes(is));
                zos.closeEntry();
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(bufferedInputStream, th);
                return true;
            } catch (Throwable th2) {
                th = th2;
                throw th2;
            }
        } catch (Throwable th3) {
            CloseableKt.closeFinally(bufferedInputStream, th);
            throw th3;
        }
    }

    @Nullable
    public final List<File> unzipFile(@NotNull String zipFilePath, @NotNull String destDirPath) throws IOException {
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter(destDirPath, "destDirPath");
        return unzipFileByKeyword(zipFilePath, destDirPath, (String) null);
    }

    @Nullable
    public final List<File> unzipFile(@NotNull File zipFile, @NotNull File destDir) throws IOException {
        Intrinsics.checkNotNullParameter(zipFile, "zipFile");
        Intrinsics.checkNotNullParameter(destDir, "destDir");
        return unzipFileByKeyword(zipFile, destDir, (String) null);
    }

    @Nullable
    public final List<File> unzipFileByKeyword(@NotNull String zipFilePath, @NotNull String destDirPath, @Nullable String keyword) throws IOException {
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter(destDirPath, "destDirPath");
        return unzipFileByKeyword(getFileByPath(zipFilePath), getFileByPath(destDirPath), keyword);
    }

    @Nullable
    public final List<File> unzipFileByKeyword(@Nullable File zipFile, @Nullable File destDir, @Nullable String keyword) throws IOException {
        if (zipFile == null || destDir == null) {
            return null;
        }
        ArrayList files = new ArrayList();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> enumerationEntries = zip.entries();
        ZipFile zipFile2 = zip;
        Throwable th = (Throwable) null;
        try {
            if (INSTANCE.isSpace(keyword)) {
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                    if (zipEntryNextElement == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                    }
                    ZipEntry entry = zipEntryNextElement;
                    String entryName = entry.getName();
                    Intrinsics.checkNotNullExpressionValue(entryName, "entryName");
                    if (StringsKt.contains$default(entryName, "../", false, 2, (Object) null)) {
                        ZipUtilsKt.logger.error("ZipUtils entryName: " + ((Object) entryName) + " is dangerous!");
                    } else if (!INSTANCE.unzipChildFile(destDir, files, zip, entry, entryName)) {
                        ArrayList arrayList = files;
                        CloseableKt.closeFinally(zipFile2, th);
                        return arrayList;
                    }
                }
            } else {
                while (enumerationEntries.hasMoreElements()) {
                    ZipEntry zipEntryNextElement2 = enumerationEntries.nextElement();
                    if (zipEntryNextElement2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                    }
                    ZipEntry entry2 = zipEntryNextElement2;
                    String entryName2 = entry2.getName();
                    Intrinsics.checkNotNullExpressionValue(entryName2, "entryName");
                    if (StringsKt.contains$default(entryName2, "../", false, 2, (Object) null)) {
                        ZipUtilsKt.logger.error("ZipUtils entryName: " + ((Object) entryName2) + " is dangerous!");
                    } else {
                        Intrinsics.checkNotNull(keyword);
                        if (StringsKt.contains$default(entryName2, keyword, false, 2, (Object) null) && !INSTANCE.unzipChildFile(destDir, files, zip, entry2, entryName2)) {
                            ArrayList arrayList2 = files;
                            CloseableKt.closeFinally(zipFile2, th);
                            return arrayList2;
                        }
                    }
                }
            }
            Unit unit = Unit.INSTANCE;
            CloseableKt.closeFinally(zipFile2, th);
            return files;
        } catch (Throwable th2) {
            CloseableKt.closeFinally(zipFile2, th);
            throw th2;
        }
    }

    private final boolean unzipChildFile(File destDir, List<File> files, ZipFile zip, ZipEntry entry, String name) throws IOException {
        File file = new File(destDir, name);
        files.add(file);
        if (entry.isDirectory()) {
            return createOrExistsDir(file);
        }
        if (!createOrExistsFile(file)) {
            return false;
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zip.getInputStream(entry));
        Throwable th = (Throwable) null;
        try {
            BufferedInputStream in = bufferedInputStream;
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            Throwable th2 = (Throwable) null;
            try {
                try {
                    BufferedOutputStream out = bufferedOutputStream;
                    out.write(ByteStreamsKt.readBytes(in));
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(bufferedOutputStream, th2);
                    Unit unit2 = Unit.INSTANCE;
                    CloseableKt.closeFinally(bufferedInputStream, th);
                    return true;
                } finally {
                }
            } catch (Throwable th3) {
                CloseableKt.closeFinally(bufferedOutputStream, th2);
                throw th3;
            }
        } catch (Throwable th4) {
            CloseableKt.closeFinally(bufferedInputStream, th);
            throw th4;
        }
    }

    @Nullable
    public final List<String> getFilesPath(@NotNull String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        return getFilesPath(getFileByPath(zipFilePath));
    }

    @Nullable
    public final List<String> getFilesPath(@Nullable File zipFile) throws IOException {
        if (zipFile == null) {
            return null;
        }
        ArrayList paths = new ArrayList();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> enumerationEntries = zip.entries();
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
            if (zipEntryNextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            String entryName = zipEntryNextElement.getName();
            Intrinsics.checkNotNullExpressionValue(entryName, "entryName");
            if (StringsKt.contains$default(entryName, "../", false, 2, (Object) null)) {
                ZipUtilsKt.logger.error("ZipUtils entryName: " + ((Object) entryName) + " is dangerous!");
                paths.add(entryName);
            } else {
                paths.add(entryName);
            }
        }
        zip.close();
        return paths;
    }

    @Nullable
    public final List<String> getComments(@NotNull String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        return getComments(getFileByPath(zipFilePath));
    }

    @Nullable
    public final List<String> getComments(@Nullable File zipFile) throws IOException {
        if (zipFile == null) {
            return null;
        }
        ArrayList comments = new ArrayList();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<? extends ZipEntry> enumerationEntries = zip.entries();
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
            if (zipEntryNextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            ZipEntry entry = zipEntryNextElement;
            comments.add(entry.getComment());
        }
        zip.close();
        return comments;
    }

    private final boolean createOrExistsDir(File file) {
        if (file != null) {
            if (file.exists() ? file.isDirectory() : file.mkdirs()) {
                return true;
            }
        }
        return false;
    }

    private final boolean createOrExistsFile(File file) {
        boolean zCreateNewFile;
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            zCreateNewFile = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            zCreateNewFile = false;
        }
        return zCreateNewFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final File getFileByPath(String filePath) {
        if (isSpace(filePath)) {
            return null;
        }
        return new File(filePath);
    }

    private final boolean isSpace(String s) {
        if (s == null) {
            return true;
        }
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
