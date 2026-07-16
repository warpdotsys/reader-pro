// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.utils;

import java.lang.annotation.RetentionPolicy;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import java.util.regex.Matcher;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import java.text.DecimalFormat;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.regex.Pattern;
import kotlin.jvm.JvmOverloads;
import java.util.Collection;
import kotlin.collections.CollectionsKt;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlin.collections.ArraysKt;
import java.util.ArrayList;
import org.jetbrains.annotations.Nullable;
import java.io.Closeable;
import kotlin.text.StringsKt;
import java.io.IOException;
import java.util.Arrays;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b'\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u00c6\u0002\u0018\u00002\u00020\u0001:\u0005abcdeB\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013J\u0010\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0016\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0013J\u0016\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0013J'\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013?\u0006\u0002\u0010$J\u000e\u0010 \u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u000e\u0010&\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J'\u0010'\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013?\u0006\u0002\u0010$J\u000e\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u001a\u0010)\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001e2\b\b\u0002\u0010+\u001a\u00020\u0011H\u0007J\u001a\u0010)\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010+\u001a\u00020\u0011H\u0007J\u000e\u0010,\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\u0013J\u0010\u0010-\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001eH\u0002J\u000e\u0010.\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J'\u0010/\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013?\u0006\u0002\u00100J\u0006\u00101\u001a\u00020\u0013J\u0016\u00102\u001a\u00020\u00132\u0006\u0010*\u001a\u00020\u001e2\u0006\u00103\u001a\u00020\u0013J\u001a\u00102\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u00103\u001a\u00020\u0013H\u0007J\u000e\u00104\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u0013J'\u00106\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013?\u0006\u0002\u0010$J\u0018\u00107\u001a\u00020\u00132\u0006\u00108\u001a\u00020\u00132\b\b\u0002\u00109\u001a\u00020\u0013J\u000e\u0010:\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010;\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u0013J\u0010\u0010<\u001a\u00020\u00132\b\u00105\u001a\u0004\u0018\u00010\u0013J\u000e\u0010=\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J'\u0010>\u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013?\u0006\u0002\u0010?J\u000e\u0010@\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J7\u0010A\u001a\b\u0012\u0004\u0012\u00020\u001e0#2\u0006\u0010B\u001a\u00020\u00132\u0010\b\u0002\u0010C\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#2\b\b\u0002\u0010D\u001a\u00020\u0004H\u0007?\u0006\u0002\u0010EJ/\u0010F\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\u0010\b\u0002\u0010G\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#H\u0007?\u0006\u0002\u0010HJ1\u0010I\u001a\b\u0012\u0004\u0012\u00020\u001e0#2\u0006\u0010B\u001a\u00020\u00132\n\b\u0002\u0010J\u001a\u0004\u0018\u00010K2\b\b\u0002\u0010D\u001a\u00020\u0004H\u0007?\u0006\u0002\u0010LJ+\u0010I\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\u000e\u0010G\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#?\u0006\u0002\u0010HJ%\u0010I\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\b\u0010M\u001a\u0004\u0018\u00010\u0013?\u0006\u0002\u0010NJ\u000e\u0010O\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001eJ\u000e\u0010O\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010P\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010P\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0013J\u0010\u0010Q\u001a\u0004\u0018\u00010R2\u0006\u0010S\u001a\u00020\u0013J\u001a\u0010T\u001a\u00020\u00132\u0006\u0010S\u001a\u00020\u00132\b\b\u0002\u0010U\u001a\u00020\u0013H\u0007J\u0016\u0010V\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010V\u001a\u00020\u00112\u0006\u0010W\u001a\u00020\u00132\u0006\u0010X\u001a\u00020\u0013J\u000e\u0010Y\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010Z\u001a\u00020\u00132\u0006\u0010[\u001a\u00020\rJ\u0016\u0010\\\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010]\u001a\u00020RJ\u0016\u0010^\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001e2\u0006\u0010]\u001a\u00020_J\u0016\u0010^\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010]\u001a\u00020_J\"\u0010`\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010U\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0086T?\u0006\u0002\n\u0000：\u0006f" }, d2 = { "Lio/legado/app/utils/FileUtils;", "", "()V", "BY_EXTENSION_ASC", "", "BY_EXTENSION_DESC", "BY_NAME_ASC", "BY_NAME_DESC", "BY_SIZE_ASC", "BY_SIZE_DESC", "BY_TIME_ASC", "BY_TIME_DESC", "GB", "", "KB", "MB", "appendText", "", "path", "", "content", "closeSilently", "", "c", "Ljava/io/Closeable;", "compareLastModified", "path1", "path2", "copy", "src", "Ljava/io/File;", "tar", "createFileIfNotExist", "root", "subDirFiles", "", "(Ljava/io/File;[Ljava/lang/String;)Ljava/io/File;", "filePath", "createFileWithReplace", "createFolderIfNotExist", "subDirs", "delete", "file", "deleteRootDir", "deleteFile", "deleteResolveEBUSY", "exist", "exists", "(Ljava/io/File;[Ljava/lang/String;)Z", "getCachePath", "getDateTime", "format", "getExtension", "pathOrUrl", "getFile", "getFileExtetion", "url", "defaultExt", "getLength", "getMimeType", "getName", "getNameExcludeExtension", "getPath", "(Ljava/io/File;[Ljava/lang/String;)Ljava/lang/String;", "getSize", "listDirs", "startDirPath", "excludeDirs", "sortType", "(Ljava/lang/String;[Ljava/lang/String;I)[Ljava/io/File;", "listDirsAndFiles", "allowExtensions", "(Ljava/lang/String;[Ljava/lang/String;)[Ljava/io/File;", "listFiles", "filterPattern", "Ljava/util/regex/Pattern;", "(Ljava/lang/String;Ljava/util/regex/Pattern;I)[Ljava/io/File;", "allowExtension", "(Ljava/lang/String;Ljava/lang/String;)[Ljava/io/File;", "makeDirs", "move", "readBytes", "", "filepath", "readText", "charset", "rename", "oldPath", "newPath", "separator", "toFileSizeString", "fileSize", "writeBytes", "data", "writeInputStream", "Ljava/io/InputStream;", "writeText", "SortByExtension", "SortByName", "SortBySize", "SortByTime", "SortType", "reader-pro" })
public final class FileUtils
{
    @NotNull
    public static final FileUtils INSTANCE;
    public static final long GB = 1073741824L;
    public static final long MB = 1048576L;
    public static final long KB = 1024L;
    public static final int BY_NAME_ASC = 0;
    public static final int BY_NAME_DESC = 1;
    public static final int BY_TIME_ASC = 2;
    public static final int BY_TIME_DESC = 3;
    public static final int BY_SIZE_ASC = 4;
    public static final int BY_SIZE_DESC = 5;
    public static final int BY_EXTENSION_ASC = 6;
    public static final int BY_EXTENSION_DESC = 7;
    
    private FileUtils() {
    }
    
    public final boolean exists(@NotNull final File root, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        return this.getFile(root, (String[])Arrays.copyOf(subDirFiles, subDirFiles.length)).exists();
    }
    
    @NotNull
    public final File createFileIfNotExist(@NotNull final File root, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final String filePath = this.getPath(root, (String[])Arrays.copyOf(subDirFiles, subDirFiles.length));
        return this.createFileIfNotExist(filePath);
    }
    
    @NotNull
    public final File createFolderIfNotExist(@NotNull final File root, @NotNull final String... subDirs) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        Intrinsics.checkNotNullParameter((Object)subDirs, "subDirs");
        final String filePath = this.getPath(root, (String[])Arrays.copyOf(subDirs, subDirs.length));
        return this.createFolderIfNotExist(filePath);
    }
    
    @NotNull
    public final File createFolderIfNotExist(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        final File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    @NotNull
    public final synchronized File createFileIfNotExist(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        final File file = new File(filePath);
        try {
            if (!file.exists()) {
                final String parent = file.getParent();
                if (parent != null) {
                    final String it = parent;
                    final int n = 0;
                    FileUtils.INSTANCE.createFolderIfNotExist(it);
                }
                file.createNewFile();
            }
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    @NotNull
    public final File createFileWithReplace(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        final File file = new File(filePath);
        if (!file.exists()) {
            final String parent = file.getParent();
            if (parent != null) {
                final String it = parent;
                final int n = 0;
                FileUtils.INSTANCE.createFolderIfNotExist(it);
            }
            file.createNewFile();
        }
        else {
            file.delete();
            file.createNewFile();
        }
        return file;
    }
    
    @NotNull
    public final File getFile(@NotNull final File root, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final String filePath = this.getPath(root, (String[])Arrays.copyOf(subDirFiles, subDirFiles.length));
        return new File(filePath);
    }
    
    @NotNull
    public final String getPath(@NotNull final File root, @NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final StringBuilder path = new StringBuilder(root.getAbsolutePath());
        final Object[] $this$forEach$iv = subDirFiles;
        final int $i$f$forEach = 0;
        for (final String it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n = 0;
            if (it.length() > 0) {
                path.append(File.separator).append(it);
            }
        }
        final String string = path.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "path.toString()");
        return string;
    }
    
    public final synchronized void deleteFile(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        final File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            final File[] listFiles;
            final File[] files = listFiles = file.listFiles();
            if (listFiles != null) {
                final Object[] $this$forEach$iv = listFiles;
                final int $i$f$forEach = 0;
                for (final File subFile : $this$forEach$iv) {
                    final Object element$iv = subFile;
                    final int n = 0;
                    final String path = subFile.getPath();
                    final FileUtils instance = FileUtils.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue((Object)path, "path");
                    instance.deleteFile(path);
                }
            }
        }
        file.delete();
    }
    
    @NotNull
    public final String getCachePath() {
        throw new Exception("Not implemented");
    }
    
    @NotNull
    public final String separator(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        String path2 = path;
        final String separator = File.separator;
        final String s = path2;
        final String s2 = "\\";
        Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
        path2 = StringsKt.replace$default(s, s2, separator, false, 4, (Object)null);
        if (!StringsKt.endsWith$default(path2, separator, false, 2, (Object)null)) {
            path2 = Intrinsics.stringPlus(path2, (Object)separator);
        }
        return path2;
    }
    
    public final void closeSilently(@Nullable final Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        }
        catch (final IOException ex) {}
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull final String startDirPath, @Nullable final String[] excludeDirs, final int sortType) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        String[] excludeDirs2 = excludeDirs;
        final ArrayList dirList = new ArrayList();
        final File startDir = new File(startDirPath);
        if (!startDir.isDirectory()) {
            return new File[0];
        }
        final File[] listFiles = startDir.listFiles(FileUtils::listDirs$lambda-4);
        if (listFiles == null) {
            return new File[0];
        }
        final File[] dirs = listFiles;
        if (excludeDirs2 == null) {
            excludeDirs2 = new String[0];
        }
        final File[] array = dirs;
        int i = 0;
        while (i < array.length) {
            final File dir = array[i];
            ++i;
            final File file = dir.getAbsoluteFile();
            final CharSequence charSequence = ArraysKt.contentDeepToString((Object[])excludeDirs2);
            final String name = file.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "file.name");
            if (!StringsKt.contains$default(charSequence, (CharSequence)name, false, 2, (Object)null)) {
                dirList.add(file);
            }
        }
        switch (sortType) {
            case 0: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByName());
                break;
            }
            case 1: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByName());
                CollectionsKt.reverse((List)dirList);
                break;
            }
            case 2: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByTime());
                break;
            }
            case 3: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByTime());
                CollectionsKt.reverse((List)dirList);
                break;
            }
            case 4: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortBySize());
                break;
            }
            case 5: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortBySize());
                CollectionsKt.reverse((List)dirList);
                break;
            }
            case 6: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByExtension());
                break;
            }
            case 7: {
                Collections.sort((List<Object>)dirList, (Comparator<? super Object>)new SortByExtension());
                CollectionsKt.reverse((List)dirList);
                break;
            }
        }
        final Collection $this$toTypedArray$iv = dirList;
        final int $i$f$toTypedArray = 0;
        final Collection thisCollection$iv = $this$toTypedArray$iv;
        final File[] array2 = thisCollection$iv.toArray(new File[0]);
        if (array2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return array2;
    }
    
    public static /* synthetic */ File[] listDirs$default(final FileUtils fileUtils, final String startDirPath, String[] excludeDirs, int sortType, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            excludeDirs = null;
        }
        if ((n & 0x4) != 0x0) {
            sortType = 0;
        }
        return fileUtils.listDirs(startDirPath, excludeDirs, sortType);
    }
    
    @JvmOverloads
    @Nullable
    public final File[] listDirsAndFiles(@NotNull final String startDirPath, @Nullable final String[] allowExtensions) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        File[] dirs = null;
        final File[] files = (allowExtensions == null) ? listFiles$default(this, startDirPath, null, 0, 6, null) : this.listFiles(startDirPath, allowExtensions);
        dirs = listDirs$default(this, startDirPath, null, 0, 6, null);
        if (files == null) {
            return null;
        }
        return (File[])ArraysKt.plus((Object[])dirs, (Object[])files);
    }
    
    public static /* synthetic */ File[] listDirsAndFiles$default(final FileUtils fileUtils, final String startDirPath, String[] allowExtensions, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            allowExtensions = null;
        }
        return fileUtils.listDirsAndFiles(startDirPath, allowExtensions);
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull final String startDirPath, @Nullable final Pattern filterPattern, final int sortType) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        final ArrayList fileList = new ArrayList();
        final File f = new File(startDirPath);
        if (!f.isDirectory()) {
            return new File[0];
        }
        final File[] listFiles = f.listFiles(FileUtils::listFiles$lambda-5);
        if (listFiles == null) {
            return new File[0];
        }
        final File[] array;
        final File[] files = array = listFiles;
        int i = 0;
        while (i < array.length) {
            final File file = array[i];
            ++i;
            fileList.add(file.getAbsoluteFile());
        }
        switch (sortType) {
            case 0: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByName());
                break;
            }
            case 1: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByName());
                CollectionsKt.reverse((List)fileList);
                break;
            }
            case 2: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByTime());
                break;
            }
            case 3: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByTime());
                CollectionsKt.reverse((List)fileList);
                break;
            }
            case 4: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortBySize());
                break;
            }
            case 5: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortBySize());
                CollectionsKt.reverse((List)fileList);
                break;
            }
            case 6: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByExtension());
                break;
            }
            case 7: {
                Collections.sort((List<Object>)fileList, (Comparator<? super Object>)new SortByExtension());
                CollectionsKt.reverse((List)fileList);
                break;
            }
        }
        final Collection $this$toTypedArray$iv = fileList;
        final int $i$f$toTypedArray = 0;
        final Collection thisCollection$iv = $this$toTypedArray$iv;
        final File[] array2 = thisCollection$iv.toArray(new File[0]);
        if (array2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return array2;
    }
    
    public static /* synthetic */ File[] listFiles$default(final FileUtils fileUtils, final String startDirPath, Pattern filterPattern, int sortType, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            filterPattern = null;
        }
        if ((n & 0x4) != 0x0) {
            sortType = 0;
        }
        return fileUtils.listFiles(startDirPath, filterPattern, sortType);
    }
    
    @Nullable
    public final File[] listFiles(@NotNull final String startDirPath, @Nullable final String[] allowExtensions) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        final File file = new File(startDirPath);
        return file.listFiles(FileUtils::listFiles$lambda-6);
    }
    
    @Nullable
    public final File[] listFiles(@NotNull final String startDirPath, @Nullable final String allowExtension) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return (allowExtension == null) ? this.listFiles(startDirPath, (String)null) : this.listFiles(startDirPath, new String[] { allowExtension });
    }
    
    public final boolean exist(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        final File file = new File(path);
        return file.exists();
    }
    
    @JvmOverloads
    public final boolean delete(@NotNull final File file, final boolean deleteRootDir) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        boolean result = false;
        if (file.isFile()) {
            result = this.deleteResolveEBUSY(file);
        }
        else {
            final File[] listFiles = file.listFiles();
            if (listFiles == null) {
                return false;
            }
            final File[] files = listFiles;
            if (files.length == 0) {
                result = (deleteRootDir && this.deleteResolveEBUSY(file));
            }
            else {
                final File[] array = files;
                int i = 0;
                while (i < array.length) {
                    final File f = array[i];
                    ++i;
                    Intrinsics.checkNotNullExpressionValue((Object)f, "f");
                    this.delete(f, deleteRootDir);
                    result = this.deleteResolveEBUSY(f);
                }
            }
            if (deleteRootDir) {
                result = this.deleteResolveEBUSY(file);
            }
        }
        return result;
    }
    
    public static /* synthetic */ boolean delete$default(final FileUtils fileUtils, final File file, boolean deleteRootDir, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            deleteRootDir = false;
        }
        return fileUtils.delete(file, deleteRootDir);
    }
    
    private final boolean deleteResolveEBUSY(final File file) {
        final File to = new File(Intrinsics.stringPlus(file.getAbsolutePath(), (Object)System.currentTimeMillis()));
        file.renameTo(to);
        return to.delete();
    }
    
    @JvmOverloads
    public final boolean delete(@NotNull final String path, final boolean deleteRootDir) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        final File file = new File(path);
        return file.exists() && this.delete(file, deleteRootDir);
    }
    
    public static /* synthetic */ boolean delete$default(final FileUtils fileUtils, final String path, boolean deleteRootDir, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            deleteRootDir = false;
        }
        return fileUtils.delete(path, deleteRootDir);
    }
    
    public final boolean copy(@NotNull final String src, @NotNull final String tar) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        Intrinsics.checkNotNullParameter((Object)tar, "tar");
        final File srcFile = new File(src);
        return srcFile.exists() && this.copy(srcFile, new File(tar));
    }
    
    public final boolean copy(@NotNull final File src, @NotNull final File tar) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        Intrinsics.checkNotNullParameter((Object)tar, "tar");
        try {
            if (src.isFile()) {
                final FileInputStream is = new FileInputStream(src);
                final FileOutputStream op = new FileOutputStream(tar);
                final BufferedInputStream bis = new BufferedInputStream(is);
                final BufferedOutputStream bos = new BufferedOutputStream(op);
                final byte[] bt = new byte[8192];
                while (true) {
                    final int len = bis.read(bt);
                    if (len == -1) {
                        break;
                    }
                    bos.write(bt, 0, len);
                }
                bis.close();
                bos.close();
            }
            else if (src.isDirectory()) {
                tar.mkdirs();
                final File[] listFiles = src.listFiles();
                if (listFiles != null) {
                    final Object[] $this$forEach$iv = listFiles;
                    final int $i$f$forEach = 0;
                    for (final File file : $this$forEach$iv) {
                        final Object element$iv = file;
                        final int n = 0;
                        final FileUtils instance = FileUtils.INSTANCE;
                        final File absoluteFile = file.getAbsoluteFile();
                        Intrinsics.checkNotNullExpressionValue((Object)absoluteFile, "file.absoluteFile");
                        instance.copy(absoluteFile, new File(tar.getAbsoluteFile(), file.getName()));
                    }
                }
            }
            return true;
        }
        catch (final Exception e) {
            return false;
        }
    }
    
    public final boolean move(@NotNull final String src, @NotNull final String tar) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        Intrinsics.checkNotNullParameter((Object)tar, "tar");
        return this.move(new File(src), new File(tar));
    }
    
    public final boolean move(@NotNull final File src, @NotNull final File tar) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        Intrinsics.checkNotNullParameter((Object)tar, "tar");
        return this.rename(src, tar);
    }
    
    public final boolean rename(@NotNull final String oldPath, @NotNull final String newPath) {
        Intrinsics.checkNotNullParameter((Object)oldPath, "oldPath");
        Intrinsics.checkNotNullParameter((Object)newPath, "newPath");
        return this.rename(new File(oldPath), new File(newPath));
    }
    
    public final boolean rename(@NotNull final File src, @NotNull final File tar) {
        Intrinsics.checkNotNullParameter((Object)src, "src");
        Intrinsics.checkNotNullParameter((Object)tar, "tar");
        return src.renameTo(tar);
    }
    
    @JvmOverloads
    @NotNull
    public final String readText(@NotNull final String filepath, @NotNull final String charset) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc_w           "filepath"
        //     4: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     7: aload_2         /* charset */
        //     8: ldc_w           "charset"
        //    11: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    14: nop            
        //    15: aload_0         /* this */
        //    16: aload_1         /* filepath */
        //    17: invokevirtual   io/legado/app/utils/FileUtils.readBytes:(Ljava/lang/String;)[B
        //    20: astore_3        /* data */
        //    21: aload_3         /* data */
        //    22: ifnull          201
        //    25: aload_2         /* charset */
        //    26: invokestatic    java/nio/charset/Charset.forName:(Ljava/lang/String;)Ljava/nio/charset/Charset;
        //    29: astore          4
        //    31: aload           4
        //    33: ldc_w           "forName(charset)"
        //    36: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    39: aload           4
        //    41: astore          4
        //    43: iconst_0       
        //    44: istore          5
        //    46: new             Ljava/lang/String;
        //    49: dup            
        //    50: aload_3         /* data */
        //    51: aload           4
        //    53: invokespecial   java/lang/String.<init>:([BLjava/nio/charset/Charset;)V
        //    56: astore          4
        //    58: nop            
        //    59: iconst_0       
        //    60: istore          $i$f$trim
        //    62: aload           $this$trim$iv
        //    64: checkcast       Ljava/lang/CharSequence;
        //    67: astore          $this$trim$iv$iv
        //    69: iconst_0       
        //    70: istore          $i$f$trim
        //    72: iconst_0       
        //    73: istore          startIndex$iv$iv
        //    75: aload           $this$trim$iv$iv
        //    77: invokeinterface java/lang/CharSequence.length:()I
        //    82: iconst_1       
        //    83: isub           
        //    84: istore          endIndex$iv$iv
        //    86: iconst_0       
        //    87: istore          startFound$iv$iv
        //    89: iload           startIndex$iv$iv
        //    91: iload           endIndex$iv$iv
        //    93: if_icmpgt       183
        //    96: iload           startFound$iv$iv
        //    98: ifne            106
        //   101: iload           startIndex$iv$iv
        //   103: goto            108
        //   106: iload           endIndex$iv$iv
        //   108: istore          index$iv$iv
        //   110: aload           $this$trim$iv$iv
        //   112: iload           index$iv$iv
        //   114: invokeinterface java/lang/CharSequence.charAt:(I)C
        //   119: istore          it
        //   121: iconst_0       
        //   122: istore          $i$a$-trim-FileUtils$readText$1
        //   124: iload           it
        //   126: bipush          32
        //   128: invokestatic    kotlin/jvm/internal/Intrinsics.compare:(II)I
        //   131: ifgt            138
        //   134: iconst_1       
        //   135: goto            139
        //   138: iconst_0       
        //   139: istore          match$iv$iv
        //   141: iload           startFound$iv$iv
        //   143: ifne            166
        //   146: iload           match$iv$iv
        //   148: ifne            157
        //   151: iconst_1       
        //   152: istore          startFound$iv$iv
        //   154: goto            180
        //   157: iload           startIndex$iv$iv
        //   159: iconst_1       
        //   160: iadd           
        //   161: istore          startIndex$iv$iv
        //   163: goto            180
        //   166: iload           match$iv$iv
        //   168: ifne            174
        //   171: goto            183
        //   174: iload           endIndex$iv$iv
        //   176: iconst_1       
        //   177: isub           
        //   178: istore          endIndex$iv$iv
        //   180: goto            89
        //   183: aload           $this$trim$iv$iv
        //   185: iload           startIndex$iv$iv
        //   187: iload           endIndex$iv$iv
        //   189: iconst_1       
        //   190: iadd           
        //   191: invokeinterface java/lang/CharSequence.subSequence:(II)Ljava/lang/CharSequence;
        //   196: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   199: areturn        
        //   200: astore_3        /* data */
        //   201: ldc_w           ""
        //   204: areturn        
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  filepath  
        //  charset   
        //    StackMapTable: 00 0C FF 00 59 00 0B 07 00 02 07 00 56 07 00 56 07 01 B6 07 00 56 01 07 00 63 01 01 01 01 00 00 10 41 01 FE 00 1D 01 01 01 40 01 11 08 07 05 F8 00 02 FF 00 10 00 03 07 00 02 07 00 56 07 00 56 00 01 07 01 C6 FC 00 00 07 00 04
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                  
        //  -----  -----  -----  -----  --------------------------------------
        //  14     200    200    201    Ljava/io/UnsupportedEncodingException;
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static /* synthetic */ String readText$default(final FileUtils fileUtils, final String filepath, String charset, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            charset = "utf-8";
        }
        return fileUtils.readText(filepath, charset);
    }
    
    @Nullable
    public final byte[] readBytes(@NotNull final String filepath) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final byte[] buffer = new byte[1024];
            while (true) {
                final int len = fis.read(buffer, 0, buffer.length);
                if (len == -1) {
                    break;
                }
                baos.write(buffer, 0, len);
            }
            final byte[] data = baos.toByteArray();
            baos.close();
            return data;
        }
        catch (final IOException e) {
            return null;
        }
        finally {
            this.closeSilently(fis);
        }
    }
    
    @JvmOverloads
    public final boolean writeText(@NotNull final String filepath, @NotNull final String content, @NotNull final String charset) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        Intrinsics.checkNotNullParameter((Object)charset, "charset");
        boolean writeBytes;
        try {
            final Charset forName = Charset.forName(charset);
            Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
            final byte[] bytes = content.getBytes(forName);
            Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
            writeBytes = this.writeBytes(filepath, bytes);
        }
        catch (final UnsupportedEncodingException e) {
            writeBytes = false;
        }
        return writeBytes;
    }
    
    public static /* synthetic */ boolean writeText$default(final FileUtils fileUtils, final String filepath, final String content, String charset, final int n, final Object o) {
        if ((n & 0x4) != 0x0) {
            charset = "utf-8";
        }
        return fileUtils.writeText(filepath, content, charset);
    }
    
    public final boolean writeBytes(@NotNull final String filepath, @NotNull final byte[] data) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        Intrinsics.checkNotNullParameter((Object)data, "data");
        final File file = new File(filepath);
        FileOutputStream fos = null;
        boolean b;
        try {
            if (!file.exists()) {
                final File parentFile = file.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            fos = new FileOutputStream(filepath);
            fos.write(data);
            b = true;
        }
        catch (final IOException e) {
            b = false;
        }
        finally {
            this.closeSilently(fos);
        }
        return b;
    }
    
    public final boolean writeInputStream(@NotNull final String filepath, @NotNull final InputStream data) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        Intrinsics.checkNotNullParameter((Object)data, "data");
        final File file = new File(filepath);
        return this.writeInputStream(file, data);
    }
    
    public final boolean writeInputStream(@NotNull final File file, @NotNull final InputStream data) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        Intrinsics.checkNotNullParameter((Object)data, "data");
        FileOutputStream fos = null;
        boolean b;
        try {
            if (!file.exists()) {
                final File parentFile = file.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            final byte[] buffer = new byte[4096];
            fos = new FileOutputStream(file);
            while (true) {
                final int len = data.read(buffer, 0, buffer.length);
                if (len == -1) {
                    break;
                }
                fos.write(buffer, 0, len);
            }
            data.close();
            fos.flush();
            b = true;
        }
        catch (final IOException e) {
            b = false;
        }
        finally {
            this.closeSilently(fos);
        }
        return b;
    }
    
    public final boolean appendText(@NotNull final String path, @NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        final File file = new File(path);
        FileWriter writer = null;
        boolean b;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file, true);
            writer.write(content);
            b = true;
        }
        catch (final IOException e) {
            b = false;
        }
        finally {
            this.closeSilently(writer);
        }
        return b;
    }
    
    public final long getLength(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        final File file = new File(path);
        return (!file.isFile() || !file.exists()) ? 0L : file.length();
    }
    
    @NotNull
    public final String getName(@Nullable final String pathOrUrl) {
        if (pathOrUrl == null) {
            return "";
        }
        final int pos = StringsKt.lastIndexOf$default((CharSequence)pathOrUrl, '/', 0, false, 6, (Object)null);
        String s;
        if (0 <= pos) {
            Intrinsics.checkNotNullExpressionValue((Object)(s = pathOrUrl.substring(pos + 1)), "(this as java.lang.String).substring(startIndex)");
        }
        else {
            s = new StringBuilder().append(System.currentTimeMillis()).append('.').append(this.getExtension(pathOrUrl)).toString();
        }
        return s;
    }
    
    @NotNull
    public final String getNameExcludeExtension(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        String s3;
        try {
            final String name;
            String fileName = name = new File(path).getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "fileName");
            final int lastIndexOf = StringsKt.lastIndexOf$default((CharSequence)name, ".", 0, false, 6, (Object)null);
            if (lastIndexOf != -1) {
                final String s = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s, "fileName");
                final String substring = s.substring(0, lastIndexOf);
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                fileName = substring;
            }
            final String s2 = fileName;
            Intrinsics.checkNotNullExpressionValue((Object)s2, "{\n            var fileName = File(path).name\n            val lastIndexOf = fileName.lastIndexOf(\".\")\n            if (lastIndexOf != -1) {\n                fileName = fileName.substring(0, lastIndexOf)\n            }\n            fileName\n        }");
            s3 = s2;
        }
        catch (final Exception e) {
            s3 = "";
        }
        return s3;
    }
    
    @NotNull
    public final String getSize(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        final long fileSize = this.getLength(path);
        return this.toFileSizeString(fileSize);
    }
    
    @NotNull
    public final String toFileSizeString(final long fileSize) {
        final DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = null;
        fileSizeString = ((fileSize < 1024L) ? new StringBuilder().append(fileSize).append('B').toString() : ((fileSize < 1048576L) ? Intrinsics.stringPlus(df.format(fileSize / (double)1024L), (Object)"K") : ((fileSize < 1073741824L) ? Intrinsics.stringPlus(df.format(fileSize / (double)1048576L), (Object)"M") : Intrinsics.stringPlus(df.format(fileSize / (double)1073741824L), (Object)"G"))));
        return fileSizeString;
    }
    
    @NotNull
    public final String getExtension(@NotNull final String pathOrUrl) {
        Intrinsics.checkNotNullParameter((Object)pathOrUrl, "pathOrUrl");
        final int dotPos = StringsKt.lastIndexOf$default((CharSequence)pathOrUrl, '.', 0, false, 6, (Object)null);
        String substring;
        if (0 <= dotPos) {
            Intrinsics.checkNotNullExpressionValue((Object)(substring = pathOrUrl.substring(dotPos + 1)), "(this as java.lang.String).substring(startIndex)");
        }
        else {
            substring = "ext";
        }
        return substring;
    }
    
    @NotNull
    public final String getFileExtetion(@NotNull final String url, @NotNull final String defaultExt) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)defaultExt, "defaultExt");
        try {
            final List seqs = StringsKt.split((CharSequence)url, new String[] { "?" }, true, 2);
            final String file = (String)CollectionsKt.last(StringsKt.split$default((CharSequence)seqs.get(0), new String[] { "/" }, false, 0, 6, (Object)null));
            final int dotPos = StringsKt.lastIndexOf$default((CharSequence)file, '.', 0, false, 6, (Object)null);
            String substring;
            if (0 <= dotPos) {
                final String s = file;
                final int beginIndex = dotPos + 1;
                final String s2 = s;
                if (s2 == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                Intrinsics.checkNotNullExpressionValue((Object)(substring = s2.substring(beginIndex)), "(this as java.lang.String).substring(startIndex)");
            }
            else {
                substring = defaultExt;
            }
            return substring;
        }
        catch (final Exception e) {
            return defaultExt;
        }
    }
    
    @NotNull
    public final String getMimeType(@NotNull final String pathOrUrl) {
        Intrinsics.checkNotNullParameter((Object)pathOrUrl, "pathOrUrl");
        throw new Exception("Not implemented");
    }
    
    @JvmOverloads
    @NotNull
    public final String getDateTime(@NotNull final String path, @NotNull final String format) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter((Object)format, "format");
        final File file = new File(path);
        return this.getDateTime(file, format);
    }
    
    public static /* synthetic */ String getDateTime$default(final FileUtils fileUtils, final String path, String format, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            format = "yyyy\u5e74MM\u6708dd\u65e5HH:mm";
        }
        return fileUtils.getDateTime(path, format);
    }
    
    @NotNull
    public final String getDateTime(@NotNull final File file, @NotNull final String format) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        Intrinsics.checkNotNullParameter((Object)format, "format");
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(file.lastModified());
        final String format2 = new SimpleDateFormat(format, Locale.PRC).format(cal.getTime());
        Intrinsics.checkNotNullExpressionValue((Object)format2, "SimpleDateFormat(format, Locale.PRC).format(cal.time)");
        return format2;
    }
    
    public final int compareLastModified(@NotNull final String path1, @NotNull final String path2) {
        Intrinsics.checkNotNullParameter((Object)path1, "path1");
        Intrinsics.checkNotNullParameter((Object)path2, "path2");
        final long stamp1 = new File(path1).lastModified();
        final long stamp2 = new File(path2).lastModified();
        return (stamp1 > stamp2) ? 1 : ((stamp1 < stamp2) ? -1 : 0);
    }
    
    public final boolean makeDirs(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        return this.makeDirs(new File(path));
    }
    
    public final boolean makeDirs(@NotNull final File file) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        return file.mkdirs();
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull final String startDirPath, @Nullable final String[] excludeDirs) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return listDirs$default(this, startDirPath, excludeDirs, 0, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull final String startDirPath) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return listDirs$default(this, startDirPath, null, 0, 6, null);
    }
    
    @JvmOverloads
    @Nullable
    public final File[] listDirsAndFiles(@NotNull final String startDirPath) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return listDirsAndFiles$default(this, startDirPath, null, 2, null);
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull final String startDirPath, @Nullable final Pattern filterPattern) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return listFiles$default(this, startDirPath, filterPattern, 0, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull final String startDirPath) {
        Intrinsics.checkNotNullParameter((Object)startDirPath, "startDirPath");
        return listFiles$default(this, startDirPath, null, 0, 6, null);
    }
    
    @JvmOverloads
    public final boolean delete(@NotNull final File file) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        return delete$default(this, file, false, 2, null);
    }
    
    @JvmOverloads
    public final boolean delete(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        return delete$default(this, path, false, 2, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String readText(@NotNull final String filepath) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        return readText$default(this, filepath, null, 2, null);
    }
    
    @JvmOverloads
    public final boolean writeText(@NotNull final String filepath, @NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)filepath, "filepath");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        return writeText$default(this, filepath, content, null, 4, null);
    }
    
    @JvmOverloads
    @NotNull
    public final String getDateTime(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        return getDateTime$default(this, path, null, 2, null);
    }
    
    private static final boolean listDirs$lambda-4(final File f) {
        return f != null && f.isDirectory();
    }
    
    private static final boolean listFiles$lambda-5(final Pattern $filterPattern, final File file) {
        if (file == null) {
            return false;
        }
        if (file.isDirectory()) {
            return false;
        }
        boolean b;
        if ($filterPattern == null) {
            b = true;
        }
        else {
            final Matcher matcher = $filterPattern.matcher(file.getName());
            b = (matcher == null || matcher.find());
        }
        return b;
    }
    
    private static final boolean listFiles$lambda-6(final String[] $allowExtensions, final File $noName_0, final String name) {
        final FileUtils instance = FileUtils.INSTANCE;
        Intrinsics.checkNotNullExpressionValue((Object)name, "name");
        final String extension = instance.getExtension(name);
        boolean b;
        if ($allowExtensions == null) {
            b = false;
        }
        else {
            final String contentDeepToString = ArraysKt.contentDeepToString((Object[])$allowExtensions);
            b = (contentDeepToString != null && StringsKt.contains$default((CharSequence)contentDeepToString, (CharSequence)extension, false, 2, (Object)null));
        }
        return b || $allowExtensions == null;
    }
    
    static {
        INSTANCE = new FileUtils();
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005?\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016：\u0006\b" }, d2 = { "Lio/legado/app/utils/FileUtils$SortByExtension;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", "", "f1", "f2", "reader-pro" })
    public static final class SortByExtension implements Comparator<File>
    {
        @Override
        public int compare(@Nullable final File f1, @Nullable final File f2) {
            int compareTo;
            if (f1 == null || f2 == null) {
                compareTo = ((f1 == null) ? -1 : 1);
            }
            else if (f1.isDirectory() && f2.isFile()) {
                compareTo = -1;
            }
            else if (f1.isFile() && f2.isDirectory()) {
                compareTo = 1;
            }
            else {
                final String name = f1.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name, "f1.name");
                final String s = name;
                final String name2 = f2.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name2, "f2.name");
                compareTo = StringsKt.compareTo(s, name2, true);
            }
            return compareTo;
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004?\u0006\u0002\u0010\u0005B\u0007\b\u0016?\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u00022\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e?\u0006\u0002\n\u0000：\u0006\u000b" }, d2 = { "Lio/legado/app/utils/FileUtils$SortByName;", "Ljava/util/Comparator;", "Ljava/io/File;", "caseSensitive", "", "(Z)V", "()V", "compare", "", "f1", "f2", "reader-pro" })
    public static final class SortByName implements Comparator<File>
    {
        private boolean caseSensitive;
        
        public SortByName(final boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }
        
        public SortByName() {
            this.caseSensitive = false;
        }
        
        @Override
        public int compare(@Nullable final File f1, @Nullable final File f2) {
            if (f1 == null || f2 == null) {
                return (f1 == null) ? -1 : 1;
            }
            int n;
            if (f1.isDirectory() && f2.isFile()) {
                n = -1;
            }
            else if (f1.isFile() && f2.isDirectory()) {
                n = 1;
            }
            else {
                final String s1 = f1.getName();
                final String s2 = f2.getName();
                if (this.caseSensitive) {
                    Intrinsics.checkNotNullExpressionValue((Object)s1, "s1");
                    final String s3 = s1;
                    Intrinsics.checkNotNullExpressionValue((Object)s2, "s2");
                    n = StringsKt.compareTo(s3, s2, false);
                }
                else {
                    Intrinsics.checkNotNullExpressionValue((Object)s1, "s1");
                    final String s4 = s1;
                    Intrinsics.checkNotNullExpressionValue((Object)s2, "s2");
                    n = StringsKt.compareTo(s4, s2, true);
                }
            }
            return n;
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005?\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016：\u0006\b" }, d2 = { "Lio/legado/app/utils/FileUtils$SortBySize;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", "", "f1", "f2", "reader-pro" })
    public static final class SortBySize implements Comparator<File>
    {
        @Override
        public int compare(@Nullable final File f1, @Nullable final File f2) {
            return (f1 == null || f2 == null) ? ((f1 == null) ? -1 : 1) : ((f1.isDirectory() && f2.isFile()) ? -1 : ((f1.isFile() && f2.isDirectory()) ? 1 : ((f1.length() < f2.length()) ? -1 : 1)));
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005?\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016：\u0006\b" }, d2 = { "Lio/legado/app/utils/FileUtils$SortByTime;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", "", "f1", "f2", "reader-pro" })
    public static final class SortByTime implements Comparator<File>
    {
        @Override
        public int compare(@Nullable final File f1, @Nullable final File f2) {
            return (f1 == null || f2 == null) ? ((f1 == null) ? -1 : 1) : ((f1.isDirectory() && f2.isFile()) ? -1 : ((f1.isFile() && f2.isDirectory()) ? 1 : ((f1.lastModified() > f2.lastModified()) ? -1 : 1)));
        }
    }
    
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0000：\u0006\u0002" }, d2 = { "Lio/legado/app/utils/FileUtils$SortType;", "", "reader-pro" })
    public @interface SortType {
    }
}
