// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.io.BufferedOutputStream;
import java.util.Enumeration;
import kotlin.text.StringsKt;
import java.util.zip.ZipFile;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.io.ByteStreamsKt;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.JvmOverloads;
import java.io.IOException;
import java.util.Iterator;
import kotlin.io.CloseableKt;
import java.util.zip.ZipOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Closeable;
import java.io.File;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.Dispatchers;
import kotlin.coroutines.CoroutineContext;
import org.jetbrains.annotations.Nullable;
import kotlin.coroutines.Continuation;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0004\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0012\u0010\u0007\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0002J\u0018\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006J\u0016\u0010\b\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\nJ\u0012\u0010\r\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u000e\u001a\u00020\nH\u0002J\u0018\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006J\u0016\u0010\u000f\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\nJ\u0012\u0010\u0010\u001a\u00020\u00042\b\u0010\u0011\u001a\u0004\u0018\u00010\nH\u0002J6\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u00062\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00060\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\nH\u0002J\u001e\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006J\u001e\u0010\u001b\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\nJ,\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u00062\b\u0010\u001e\u001a\u0004\u0018\u00010\nJ(\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\t2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\n2\b\u0010\u001e\u001a\u0004\u0018\u00010\nJ*\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0002J(\u0010\u000b\u001a\u00020\u00042\b\u0010\u001f\u001a\u0004\u0018\u00010\u00062\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\nH\u0007J\u0016\u0010\u000b\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\nJ\u001e\u0010\u000b\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\n2\u0006\u0010#\u001a\u00020\nJ.\u0010%\u001a\u00020\u00042\u000e\u0010&\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010'2\b\u0010\u000b\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\nH\u0007J'\u0010%\u001a\u00020\u00042\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\n0'2\u0006\u0010\f\u001a\u00020\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010(J5\u0010%\u001a\u00020\u00042\u000e\u0010)\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010'2\b\u0010\f\u001a\u0004\u0018\u00010\n2\b\u0010#\u001a\u0004\u0018\u00010\nH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010*\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006+" }, d2 = { "Lio/legado/app/utils/ZipUtils;", "", "()V", "createOrExistsDir", "", "file", "Ljava/io/File;", "createOrExistsFile", "getComments", "", "", "zipFile", "zipFilePath", "getFileByPath", "filePath", "getFilesPath", "isSpace", "s", "unzipChildFile", "destDir", "files", "", "zip", "Ljava/util/zip/ZipFile;", "entry", "Ljava/util/zip/ZipEntry;", "name", "unzipFile", "destDirPath", "unzipFileByKeyword", "keyword", "srcFile", "rootPath", "zos", "Ljava/util/zip/ZipOutputStream;", "comment", "srcFilePath", "zipFiles", "srcFiles", "", "(Ljava/util/Collection;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "srcFilePaths", "(Ljava/util/Collection;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "reader-pro" })
public final class ZipUtils
{
    @NotNull
    public static final ZipUtils INSTANCE;
    
    private ZipUtils() {
    }
    
    @Nullable
    public final Object zipFiles(@NotNull final Collection<String> srcFiles, @NotNull final String zipFilePath, @NotNull final Continuation<? super Boolean> $completion) {
        return this.zipFiles(srcFiles, zipFilePath, null, $completion);
    }
    
    @Nullable
    public final Object zipFiles(@Nullable final Collection<String> srcFilePaths, @Nullable final String zipFilePath, @Nullable final String comment, @NotNull final Continuation<? super Boolean> $completion) {
        return BuildersKt.withContext((CoroutineContext)Dispatchers.getIO(), (Function2)new ZipUtils$zipFiles.ZipUtils$zipFiles$3((Collection)srcFilePaths, zipFilePath, comment, (Continuation)null), (Continuation)$completion);
    }
    
    @JvmOverloads
    public final boolean zipFiles(@Nullable final Collection<? extends File> srcFiles, @Nullable final File zipFile, @Nullable final String comment) throws IOException {
        if (srcFiles == null || zipFile == null) {
            return false;
        }
        final Closeable closeable = new ZipOutputStream(new FileOutputStream(zipFile));
        Throwable t = null;
        try {
            final ZipOutputStream it = (ZipOutputStream)closeable;
            final int n = 0;
            for (final File srcFile : srcFiles) {
                if (!ZipUtils.INSTANCE.zipFile(srcFile, "", it, comment)) {
                    return false;
                }
            }
            return true;
        }
        catch (final Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            CloseableKt.closeFinally(closeable, t);
        }
    }
    
    public static /* synthetic */ boolean zipFiles$default(final ZipUtils zipUtils, final Collection srcFiles, final File zipFile, String comment, final int n, final Object o) throws IOException {
        if ((n & 0x4) != 0x0) {
            comment = null;
        }
        return zipUtils.zipFiles(srcFiles, zipFile, comment);
    }
    
    public final boolean zipFile(@NotNull final String srcFilePath, @NotNull final String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter((Object)srcFilePath, "srcFilePath");
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        return this.zipFile(this.getFileByPath(srcFilePath), this.getFileByPath(zipFilePath), null);
    }
    
    public final boolean zipFile(@NotNull final String srcFilePath, @NotNull final String zipFilePath, @NotNull final String comment) throws IOException {
        Intrinsics.checkNotNullParameter((Object)srcFilePath, "srcFilePath");
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter((Object)comment, "comment");
        return this.zipFile(this.getFileByPath(srcFilePath), this.getFileByPath(zipFilePath), comment);
    }
    
    @JvmOverloads
    public final boolean zipFile(@Nullable final File srcFile, @Nullable final File zipFile, @Nullable final String comment) throws IOException {
        if (srcFile == null || zipFile == null) {
            return false;
        }
        final Closeable closeable = new ZipOutputStream(new FileOutputStream(zipFile));
        Throwable t = null;
        try {
            final ZipOutputStream zos = (ZipOutputStream)closeable;
            final int n = 0;
            return ZipUtils.INSTANCE.zipFile(srcFile, "", zos, comment);
        }
        catch (final Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            CloseableKt.closeFinally(closeable, t);
        }
    }
    
    public static /* synthetic */ boolean zipFile$default(final ZipUtils zipUtils, final File srcFile, final File zipFile, String comment, final int n, final Object o) throws IOException {
        if ((n & 0x4) != 0x0) {
            comment = null;
        }
        return zipUtils.zipFile(srcFile, zipFile, comment);
    }
    
    private final boolean zipFile(final File srcFile, final String rootPath, final ZipOutputStream zos, final String comment) throws IOException {
        Object rootPath2 = null;
        rootPath2 = rootPath;
        if (!srcFile.exists()) {
            return true;
        }
        rootPath2 = (String)rootPath2 + (Object)(this.isSpace((String)rootPath2) ? "" : File.separator) + (Object)srcFile.getName();
        if (srcFile.isDirectory()) {
            final File[] fileList = srcFile.listFiles();
            if (fileList == null || fileList.length == 0) {
                final ZipEntry entry = new ZipEntry(Intrinsics.stringPlus((String)rootPath2, (Object)"/"));
                entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            }
            else {
                final File[] array = fileList;
                int i = 0;
                while (i < array.length) {
                    final File file = array[i];
                    ++i;
                    Intrinsics.checkNotNullExpressionValue((Object)file, "file");
                    if (!this.zipFile(file, (String)rootPath2, zos, comment)) {
                        return false;
                    }
                }
            }
        }
        else {
            final Closeable closeable = new BufferedInputStream(new FileInputStream(srcFile));
            Throwable t = null;
            try {
                final BufferedInputStream is = (BufferedInputStream)closeable;
                final int n = 0;
                final ZipEntry entry2 = new ZipEntry((String)rootPath2);
                entry2.setComment(comment);
                zos.putNextEntry(entry2);
                zos.write(ByteStreamsKt.readBytes((InputStream)is));
                zos.closeEntry();
                final Unit instance = Unit.INSTANCE;
            }
            catch (final Throwable t2) {
                t = t2;
                throw t2;
            }
            finally {
                CloseableKt.closeFinally(closeable, t);
            }
        }
        return true;
    }
    
    @Nullable
    public final List<File> unzipFile(@NotNull final String zipFilePath, @NotNull final String destDirPath) throws IOException {
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter((Object)destDirPath, "destDirPath");
        return this.unzipFileByKeyword(zipFilePath, destDirPath, null);
    }
    
    @Nullable
    public final List<File> unzipFile(@NotNull final File zipFile, @NotNull final File destDir) throws IOException {
        Intrinsics.checkNotNullParameter((Object)zipFile, "zipFile");
        Intrinsics.checkNotNullParameter((Object)destDir, "destDir");
        return this.unzipFileByKeyword(zipFile, destDir, null);
    }
    
    @Nullable
    public final List<File> unzipFileByKeyword(@NotNull final String zipFilePath, @NotNull final String destDirPath, @Nullable final String keyword) throws IOException {
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        Intrinsics.checkNotNullParameter((Object)destDirPath, "destDirPath");
        return this.unzipFileByKeyword(this.getFileByPath(zipFilePath), this.getFileByPath(destDirPath), keyword);
    }
    
    @Nullable
    public final List<File> unzipFileByKeyword(@Nullable final File zipFile, @Nullable final File destDir, @Nullable final String keyword) throws IOException {
        if (zipFile == null || destDir == null) {
            return null;
        }
        final ArrayList files = new ArrayList();
        final ZipFile zip = new ZipFile(zipFile);
        final Enumeration entries = zip.entries();
        final Closeable closeable = zip;
        Throwable t = null;
        try {
            final ZipFile it = (ZipFile)closeable;
            final int n = 0;
            if (ZipUtils.INSTANCE.isSpace(keyword)) {
                while (entries.hasMoreElements()) {
                    final ZipEntry nextElement = entries.nextElement();
                    if (nextElement == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                    }
                    final ZipEntry entry = nextElement;
                    final String entryName = entry.getName();
                    Intrinsics.checkNotNullExpressionValue((Object)entryName, "entryName");
                    if (StringsKt.contains$default((CharSequence)entryName, (CharSequence)"../", false, 2, (Object)null)) {
                        ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: " + (Object)entryName + " is dangerous!");
                    }
                    else {
                        if (!ZipUtils.INSTANCE.unzipChildFile(destDir, files, zip, entry, entryName)) {
                            return files;
                        }
                        continue;
                    }
                }
            }
            else {
                while (entries.hasMoreElements()) {
                    final ZipEntry nextElement2 = entries.nextElement();
                    if (nextElement2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                    }
                    final ZipEntry entry = nextElement2;
                    final String entryName = entry.getName();
                    Intrinsics.checkNotNullExpressionValue((Object)entryName, "entryName");
                    if (StringsKt.contains$default((CharSequence)entryName, (CharSequence)"../", false, 2, (Object)null)) {
                        ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: " + (Object)entryName + " is dangerous!");
                    }
                    else {
                        final CharSequence charSequence = entryName;
                        Intrinsics.checkNotNull((Object)keyword);
                        if (StringsKt.contains$default(charSequence, (CharSequence)keyword, false, 2, (Object)null) && !ZipUtils.INSTANCE.unzipChildFile(destDir, files, zip, entry, entryName)) {
                            return files;
                        }
                        continue;
                    }
                }
            }
            final Unit instance = Unit.INSTANCE;
        }
        catch (final Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            CloseableKt.closeFinally(closeable, t);
        }
        return files;
    }
    
    private final boolean unzipChildFile(final File destDir, final List<File> files, final ZipFile zip, final ZipEntry entry, final String name) throws IOException {
        final File file = new File(destDir, name);
        files.add(file);
        if (entry.isDirectory()) {
            return this.createOrExistsDir(file);
        }
        if (!this.createOrExistsFile(file)) {
            return false;
        }
        final Closeable closeable = new BufferedInputStream(zip.getInputStream(entry));
        Throwable t = null;
        try {
            final BufferedInputStream in = (BufferedInputStream)closeable;
            final int n = 0;
            final Closeable closeable2 = new BufferedOutputStream(new FileOutputStream(file));
            Throwable t2 = null;
            try {
                final BufferedOutputStream out = (BufferedOutputStream)closeable2;
                final int n2 = 0;
                out.write(ByteStreamsKt.readBytes((InputStream)in));
                final Unit instance = Unit.INSTANCE;
            }
            catch (final Throwable t3) {
                t2 = t3;
                throw t3;
            }
            finally {
                CloseableKt.closeFinally(closeable2, t2);
            }
            final Unit instance2 = Unit.INSTANCE;
        }
        catch (final Throwable t4) {
            t = t4;
            throw t4;
        }
        finally {
            CloseableKt.closeFinally(closeable, t);
        }
        return true;
    }
    
    @Nullable
    public final List<String> getFilesPath(@NotNull final String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        return this.getFilesPath(this.getFileByPath(zipFilePath));
    }
    
    @Nullable
    public final List<String> getFilesPath(@Nullable final File zipFile) throws IOException {
        if (zipFile == null) {
            return null;
        }
        final ArrayList paths = new ArrayList();
        final ZipFile zip = new ZipFile(zipFile);
        final Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry nextElement = entries.nextElement();
            if (nextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            final String entryName = nextElement.getName();
            Intrinsics.checkNotNullExpressionValue((Object)entryName, "entryName");
            if (StringsKt.contains$default((CharSequence)entryName, (CharSequence)"../", false, 2, (Object)null)) {
                ZipUtilsKt.access$getLogger$p().error("ZipUtils entryName: " + (Object)entryName + " is dangerous!");
                paths.add(entryName);
            }
            else {
                paths.add(entryName);
            }
        }
        zip.close();
        return paths;
    }
    
    @Nullable
    public final List<String> getComments(@NotNull final String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        return this.getComments(this.getFileByPath(zipFilePath));
    }
    
    @Nullable
    public final List<String> getComments(@Nullable final File zipFile) throws IOException {
        if (zipFile == null) {
            return null;
        }
        final ArrayList comments = new ArrayList();
        final ZipFile zip = new ZipFile(zipFile);
        final Enumeration entries = zip.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry nextElement = entries.nextElement();
            if (nextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            final ZipEntry entry = nextElement;
            comments.add(entry.getComment());
        }
        zip.close();
        return comments;
    }
    
    private final boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }
    
    private final boolean createOrExistsFile(final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.isFile();
        }
        if (!this.createOrExistsDir(file.getParentFile())) {
            return false;
        }
        boolean newFile;
        try {
            newFile = file.createNewFile();
        }
        catch (final IOException e) {
            e.printStackTrace();
            newFile = false;
        }
        return newFile;
    }
    
    private final File getFileByPath(final String filePath) {
        return this.isSpace(filePath) ? null : new File(filePath);
    }
    
    private final boolean isSpace(final String s) {
        if (s == null) {
            return true;
        }
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    @JvmOverloads
    public final boolean zipFiles(@Nullable final Collection<? extends File> srcFiles, @Nullable final File zipFile) throws IOException {
        return zipFiles$default(this, srcFiles, zipFile, null, 4, null);
    }
    
    @JvmOverloads
    public final boolean zipFile(@Nullable final File srcFile, @Nullable final File zipFile) throws IOException {
        return zipFile$default(this, srcFile, zipFile, null, 4, null);
    }
    
    static {
        INSTANCE = new ZipUtils();
    }
}
