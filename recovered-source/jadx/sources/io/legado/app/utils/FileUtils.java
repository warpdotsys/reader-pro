package io.legado.app.utils;

import io.legado.app.constant.RSSKeywords;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.RetentionPolicy;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmOverloads;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: FilesUtil.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b'\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0012\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0007\bÆ\u0002\u0018\u00002\u00020\u0001:\u0005abcdeB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013J\u0010\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0016\u0010\u0019\u001a\u00020\u00042\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0013J\u0016\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010\u001c\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0013J'\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013¢\u0006\u0002\u0010$J\u000e\u0010 \u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u000e\u0010&\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J'\u0010'\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010(\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013¢\u0006\u0002\u0010$J\u000e\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\u0013J\u001a\u0010)\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001e2\b\b\u0002\u0010+\u001a\u00020\u0011H\u0007J\u001a\u0010)\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010+\u001a\u00020\u0011H\u0007J\u000e\u0010,\u001a\u00020\u00162\u0006\u0010%\u001a\u00020\u0013J\u0010\u0010-\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001eH\u0002J\u000e\u0010.\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J'\u0010/\u001a\u00020\u00112\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013¢\u0006\u0002\u00100J\u0006\u00101\u001a\u00020\u0013J\u0016\u00102\u001a\u00020\u00132\u0006\u0010*\u001a\u00020\u001e2\u0006\u00103\u001a\u00020\u0013J\u001a\u00102\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u00103\u001a\u00020\u0013H\u0007J\u000e\u00104\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u0013J'\u00106\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013¢\u0006\u0002\u0010$J\u0018\u00107\u001a\u00020\u00132\u0006\u00108\u001a\u00020\u00132\b\b\u0002\u00109\u001a\u00020\u0013J\u000e\u0010:\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010;\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u0013J\u0010\u0010<\u001a\u00020\u00132\b\u00105\u001a\u0004\u0018\u00010\u0013J\u000e\u0010=\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J'\u0010>\u001a\u00020\u00132\u0006\u0010!\u001a\u00020\u001e2\u0012\u0010\"\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00130#\"\u00020\u0013¢\u0006\u0002\u0010?J\u000e\u0010@\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J7\u0010A\u001a\b\u0012\u0004\u0012\u00020\u001e0#2\u0006\u0010B\u001a\u00020\u00132\u0010\b\u0002\u0010C\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#2\b\b\u0002\u0010D\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010EJ/\u0010F\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\u0010\b\u0002\u0010G\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#H\u0007¢\u0006\u0002\u0010HJ1\u0010I\u001a\b\u0012\u0004\u0012\u00020\u001e0#2\u0006\u0010B\u001a\u00020\u00132\n\b\u0002\u0010J\u001a\u0004\u0018\u00010K2\b\b\u0002\u0010D\u001a\u00020\u0004H\u0007¢\u0006\u0002\u0010LJ+\u0010I\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\u000e\u0010G\u001a\n\u0012\u0004\u0012\u00020\u0013\u0018\u00010#¢\u0006\u0002\u0010HJ%\u0010I\u001a\n\u0012\u0004\u0012\u00020\u001e\u0018\u00010#2\u0006\u0010B\u001a\u00020\u00132\b\u0010M\u001a\u0004\u0018\u00010\u0013¢\u0006\u0002\u0010NJ\u000e\u0010O\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001eJ\u000e\u0010O\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010P\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010P\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u00132\u0006\u0010\u001f\u001a\u00020\u0013J\u0010\u0010Q\u001a\u0004\u0018\u00010R2\u0006\u0010S\u001a\u00020\u0013J\u001a\u0010T\u001a\u00020\u00132\u0006\u0010S\u001a\u00020\u00132\b\b\u0002\u0010U\u001a\u00020\u0013H\u0007J\u0016\u0010V\u001a\u00020\u00112\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eJ\u0016\u0010V\u001a\u00020\u00112\u0006\u0010W\u001a\u00020\u00132\u0006\u0010X\u001a\u00020\u0013J\u000e\u0010Y\u001a\u00020\u00132\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010Z\u001a\u00020\u00132\u0006\u0010[\u001a\u00020\rJ\u0016\u0010\\\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010]\u001a\u00020RJ\u0016\u0010^\u001a\u00020\u00112\u0006\u0010*\u001a\u00020\u001e2\u0006\u0010]\u001a\u00020_J\u0016\u0010^\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010]\u001a\u00020_J\"\u0010`\u001a\u00020\u00112\u0006\u0010S\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010U\u001a\u00020\u0013H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\rX\u0086T¢\u0006\u0002\n\u0000¨\u0006f"}, d2 = {"Lio/legado/app/utils/FileUtils;", PackageDocumentBase.PREFIX_OPF, "()V", "BY_EXTENSION_ASC", PackageDocumentBase.PREFIX_OPF, "BY_EXTENSION_DESC", "BY_NAME_ASC", "BY_NAME_DESC", "BY_SIZE_ASC", "BY_SIZE_DESC", "BY_TIME_ASC", "BY_TIME_DESC", "GB", PackageDocumentBase.PREFIX_OPF, "KB", "MB", "appendText", PackageDocumentBase.PREFIX_OPF, "path", PackageDocumentBase.PREFIX_OPF, "content", "closeSilently", PackageDocumentBase.PREFIX_OPF, "c", "Ljava/io/Closeable;", "compareLastModified", "path1", "path2", "copy", NCXDocumentV2.NCXAttributes.src, "Ljava/io/File;", "tar", "createFileIfNotExist", "root", "subDirFiles", PackageDocumentBase.PREFIX_OPF, "(Ljava/io/File;[Ljava/lang/String;)Ljava/io/File;", "filePath", "createFileWithReplace", "createFolderIfNotExist", "subDirs", "delete", "file", "deleteRootDir", "deleteFile", "deleteResolveEBUSY", "exist", "exists", "(Ljava/io/File;[Ljava/lang/String;)Z", "getCachePath", "getDateTime", PackageDocumentBase.DCTags.format, "getExtension", "pathOrUrl", "getFile", "getFileExtetion", RSSKeywords.RSS_ITEM_URL, "defaultExt", "getLength", "getMimeType", "getName", "getNameExcludeExtension", "getPath", "(Ljava/io/File;[Ljava/lang/String;)Ljava/lang/String;", "getSize", "listDirs", "startDirPath", "excludeDirs", "sortType", "(Ljava/lang/String;[Ljava/lang/String;I)[Ljava/io/File;", "listDirsAndFiles", "allowExtensions", "(Ljava/lang/String;[Ljava/lang/String;)[Ljava/io/File;", "listFiles", "filterPattern", "Ljava/util/regex/Pattern;", "(Ljava/lang/String;Ljava/util/regex/Pattern;I)[Ljava/io/File;", "allowExtension", "(Ljava/lang/String;Ljava/lang/String;)[Ljava/io/File;", "makeDirs", "move", "readBytes", PackageDocumentBase.PREFIX_OPF, "filepath", "readText", "charset", "rename", "oldPath", "newPath", "separator", "toFileSizeString", "fileSize", "writeBytes", "data", "writeInputStream", "Ljava/io/InputStream;", "writeText", "SortByExtension", "SortByName", "SortBySize", "SortByTime", "SortType", "reader-pro"})
public final class FileUtils {

    @NotNull
    public static final FileUtils INSTANCE = new FileUtils();
    public static final long GB = 1073741824;
    public static final long MB = 1048576;
    public static final long KB = 1024;
    public static final int BY_NAME_ASC = 0;
    public static final int BY_NAME_DESC = 1;
    public static final int BY_TIME_ASC = 2;
    public static final int BY_TIME_DESC = 3;
    public static final int BY_SIZE_ASC = 4;
    public static final int BY_SIZE_DESC = 5;
    public static final int BY_EXTENSION_ASC = 6;
    public static final int BY_EXTENSION_DESC = 7;

    /* JADX INFO: compiled from: FilesUtil.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils$SortType.class */
    @Retention(AnnotationRetention.SOURCE)
    @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0087\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, d2 = {"Lio/legado/app/utils/FileUtils$SortType;", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
    public @interface SortType {
    }

    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull String startDirPath, @Nullable String[] excludeDirs) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        return listDirs$default(this, startDirPath, excludeDirs, 0, 4, null);
    }

    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull String startDirPath) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        return listDirs$default(this, startDirPath, null, 0, 6, null);
    }

    @JvmOverloads
    @Nullable
    public final File[] listDirsAndFiles(@NotNull String startDirPath) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        return listDirsAndFiles$default(this, startDirPath, null, 2, null);
    }

    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull String startDirPath, @Nullable Pattern filterPattern) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        return listFiles$default(this, startDirPath, filterPattern, 0, 4, null);
    }

    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull String startDirPath) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        return listFiles$default(this, startDirPath, null, 0, 6, null);
    }

    @JvmOverloads
    public final boolean delete(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        return delete$default(this, file, false, 2, (Object) null);
    }

    @JvmOverloads
    public final boolean delete(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        return delete$default(this, path, false, 2, (Object) null);
    }

    @JvmOverloads
    @NotNull
    public final String readText(@NotNull String filepath) {
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        return readText$default(this, filepath, null, 2, null);
    }

    @JvmOverloads
    public final boolean writeText(@NotNull String filepath, @NotNull String content) {
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        Intrinsics.checkNotNullParameter(content, "content");
        return writeText$default(this, filepath, content, null, 4, null);
    }

    @JvmOverloads
    @NotNull
    public final String getDateTime(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        return getDateTime$default(this, path, null, 2, null);
    }

    private FileUtils() {
    }

    public final boolean exists(@NotNull File root, @NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(root, "root");
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        return getFile(root, (String[]) Arrays.copyOf(subDirFiles, subDirFiles.length)).exists();
    }

    @NotNull
    public final File createFileIfNotExist(@NotNull File root, @NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(root, "root");
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        String filePath = getPath(root, (String[]) Arrays.copyOf(subDirFiles, subDirFiles.length));
        return createFileIfNotExist(filePath);
    }

    @NotNull
    public final File createFolderIfNotExist(@NotNull File root, @NotNull String... subDirs) {
        Intrinsics.checkNotNullParameter(root, "root");
        Intrinsics.checkNotNullParameter(subDirs, "subDirs");
        String filePath = getPath(root, (String[]) Arrays.copyOf(subDirs, subDirs.length));
        return createFolderIfNotExist(filePath);
    }

    @NotNull
    public final File createFolderIfNotExist(@NotNull String filePath) {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @NotNull
    public final synchronized File createFileIfNotExist(@NotNull String filePath) {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                String it = file.getParent();
                if (it != null) {
                    INSTANCE.createFolderIfNotExist(it);
                }
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @NotNull
    public final File createFileWithReplace(@NotNull String filePath) throws IOException {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        File file = new File(filePath);
        if (!file.exists()) {
            String it = file.getParent();
            if (it != null) {
                INSTANCE.createFolderIfNotExist(it);
            }
            file.createNewFile();
        } else {
            file.delete();
            file.createNewFile();
        }
        return file;
    }

    @NotNull
    public final File getFile(@NotNull File root, @NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(root, "root");
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        String filePath = getPath(root, (String[]) Arrays.copyOf(subDirFiles, subDirFiles.length));
        return new File(filePath);
    }

    @NotNull
    public final String getPath(@NotNull File root, @NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(root, "root");
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        StringBuilder path = new StringBuilder(root.getAbsolutePath());
        for (String str : subDirFiles) {
            if (str.length() > 0) {
                path.append(File.separator).append(str);
            }
        }
        String string = path.toString();
        Intrinsics.checkNotNullExpressionValue(string, "path.toString()");
        return string;
    }

    public final synchronized void deleteFile(@NotNull String filePath) {
        File[] files;
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory() && (files = file.listFiles()) != null) {
                for (File file2 : files) {
                    String path = file2.getPath();
                    FileUtils fileUtils = INSTANCE;
                    Intrinsics.checkNotNullExpressionValue(path, "path");
                    fileUtils.deleteFile(path);
                }
            }
            file.delete();
        }
    }

    @NotNull
    public final String getCachePath() throws Exception {
        throw new Exception("Not implemented");
    }

    @NotNull
    public final String separator(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        String separator = File.separator;
        Intrinsics.checkNotNullExpressionValue(separator, "separator");
        String path1 = StringsKt.replace$default(path, "\\", separator, false, 4, (Object) null);
        if (!StringsKt.endsWith$default(path1, separator, false, 2, (Object) null)) {
            path1 = Intrinsics.stringPlus(path1, separator);
        }
        return path1;
    }

    public final void closeSilently(@Nullable Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException e) {
        }
    }

    public static /* synthetic */ File[] listDirs$default(FileUtils fileUtils, String str, String[] strArr, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            strArr = null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return fileUtils.listDirs(str, strArr, i);
    }

    @JvmOverloads
    @NotNull
    public final File[] listDirs(@NotNull String startDirPath, @Nullable String[] excludeDirs, int sortType) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        String[] excludeDirs1 = excludeDirs;
        ArrayList dirList = new ArrayList();
        File startDir = new File(startDirPath);
        if (!startDir.isDirectory()) {
            return new File[0];
        }
        File[] dirs = startDir.listFiles(FileUtils::m284listDirs$lambda4);
        if (dirs == null) {
            return new File[0];
        }
        if (excludeDirs1 == null) {
            excludeDirs1 = new String[0];
        }
        int i = 0;
        int length = dirs.length;
        while (i < length) {
            File dir = dirs[i];
            i++;
            File file = dir.getAbsoluteFile();
            String strContentDeepToString = ArraysKt.contentDeepToString(excludeDirs1);
            String name = file.getName();
            Intrinsics.checkNotNullExpressionValue(name, "file.name");
            if (!StringsKt.contains$default(strContentDeepToString, name, false, 2, (Object) null)) {
                dirList.add(file);
            }
        }
        switch (sortType) {
            case 0:
                Collections.sort(dirList, new SortByName());
                break;
            case 1:
                Collections.sort(dirList, new SortByName());
                CollectionsKt.reverse(dirList);
                break;
            case 2:
                Collections.sort(dirList, new SortByTime());
                break;
            case 3:
                Collections.sort(dirList, new SortByTime());
                CollectionsKt.reverse(dirList);
                break;
            case 4:
                Collections.sort(dirList, new SortBySize());
                break;
            case 5:
                Collections.sort(dirList, new SortBySize());
                CollectionsKt.reverse(dirList);
                break;
            case 6:
                Collections.sort(dirList, new SortByExtension());
                break;
            case 7:
                Collections.sort(dirList, new SortByExtension());
                CollectionsKt.reverse(dirList);
                break;
        }
        ArrayList $this$toTypedArray$iv = dirList;
        Object[] array = $this$toTypedArray$iv.toArray(new File[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (File[]) array;
    }

    /* JADX INFO: renamed from: listDirs$lambda-4, reason: not valid java name */
    private static final boolean m284listDirs$lambda4(File f) {
        if (f == null) {
            return false;
        }
        return f.isDirectory();
    }

    public static /* synthetic */ File[] listDirsAndFiles$default(FileUtils fileUtils, String str, String[] strArr, int i, Object obj) {
        if ((i & 2) != 0) {
            strArr = null;
        }
        return fileUtils.listDirsAndFiles(str, strArr);
    }

    @JvmOverloads
    @Nullable
    public final File[] listDirsAndFiles(@NotNull String startDirPath, @Nullable String[] allowExtensions) {
        File[] fileArrListFiles;
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        if (allowExtensions == null) {
            fileArrListFiles = listFiles$default(this, startDirPath, null, 0, 6, null);
        } else {
            fileArrListFiles = listFiles(startDirPath, allowExtensions);
        }
        File[] files = fileArrListFiles;
        File[] dirs = listDirs$default(this, startDirPath, null, 0, 6, null);
        if (files == null) {
            return null;
        }
        return (File[]) ArraysKt.plus(dirs, files);
    }

    public static /* synthetic */ File[] listFiles$default(FileUtils fileUtils, String str, Pattern pattern, int i, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            pattern = null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        return fileUtils.listFiles(str, pattern, i);
    }

    @JvmOverloads
    @NotNull
    public final File[] listFiles(@NotNull String startDirPath, @Nullable Pattern filterPattern, int sortType) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        ArrayList fileList = new ArrayList();
        File f = new File(startDirPath);
        if (!f.isDirectory()) {
            return new File[0];
        }
        File[] files = f.listFiles((v1) -> {
            return m285listFiles$lambda5(r1, v1);
        });
        if (files == null) {
            return new File[0];
        }
        int i = 0;
        int length = files.length;
        while (i < length) {
            File file = files[i];
            i++;
            fileList.add(file.getAbsoluteFile());
        }
        switch (sortType) {
            case 0:
                Collections.sort(fileList, new SortByName());
                break;
            case 1:
                Collections.sort(fileList, new SortByName());
                CollectionsKt.reverse(fileList);
                break;
            case 2:
                Collections.sort(fileList, new SortByTime());
                break;
            case 3:
                Collections.sort(fileList, new SortByTime());
                CollectionsKt.reverse(fileList);
                break;
            case 4:
                Collections.sort(fileList, new SortBySize());
                break;
            case 5:
                Collections.sort(fileList, new SortBySize());
                CollectionsKt.reverse(fileList);
                break;
            case 6:
                Collections.sort(fileList, new SortByExtension());
                break;
            case 7:
                Collections.sort(fileList, new SortByExtension());
                CollectionsKt.reverse(fileList);
                break;
        }
        ArrayList $this$toTypedArray$iv = fileList;
        Object[] array = $this$toTypedArray$iv.toArray(new File[0]);
        if (array == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T>");
        }
        return (File[]) array;
    }

    /* JADX INFO: renamed from: listFiles$lambda-5, reason: not valid java name */
    private static final boolean m285listFiles$lambda5(Pattern $filterPattern, File file) {
        Matcher matcher;
        if (file == null || file.isDirectory()) {
            return false;
        }
        if ($filterPattern == null || (matcher = $filterPattern.matcher(file.getName())) == null) {
            return true;
        }
        return matcher.find();
    }

    @Nullable
    public final File[] listFiles(@NotNull String startDirPath, @Nullable String[] allowExtensions) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        File file = new File(startDirPath);
        return file.listFiles((v1, v2) -> {
            return m286listFiles$lambda6(r1, v1, v2);
        });
    }

    /* JADX INFO: renamed from: listFiles$lambda-6, reason: not valid java name */
    private static final boolean m286listFiles$lambda6(String[] $allowExtensions, File $noName_0, String name) {
        String strContentDeepToString;
        FileUtils fileUtils = INSTANCE;
        Intrinsics.checkNotNullExpressionValue(name, "name");
        String extension = fileUtils.getExtension(name);
        boolean z = ($allowExtensions == null || (strContentDeepToString = ArraysKt.contentDeepToString($allowExtensions)) == null || !StringsKt.contains$default(strContentDeepToString, extension, false, 2, (Object) null)) ? false : true;
        return z || $allowExtensions == null;
    }

    @Nullable
    public final File[] listFiles(@NotNull String startDirPath, @Nullable String allowExtension) {
        Intrinsics.checkNotNullParameter(startDirPath, "startDirPath");
        if (allowExtension == null) {
            return listFiles(startDirPath, (String) null);
        }
        return listFiles(startDirPath, new String[]{allowExtension});
    }

    public final boolean exist(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        File file = new File(path);
        return file.exists();
    }

    public static /* synthetic */ boolean delete$default(FileUtils fileUtils, File file, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return fileUtils.delete(file, z);
    }

    @JvmOverloads
    public final boolean delete(@NotNull File file, boolean deleteRootDir) {
        Intrinsics.checkNotNullParameter(file, "file");
        boolean result = false;
        if (file.isFile()) {
            result = deleteResolveEBUSY(file);
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                return false;
            }
            if (files.length == 0) {
                result = deleteRootDir && deleteResolveEBUSY(file);
            } else {
                int i = 0;
                int length = files.length;
                while (i < length) {
                    File f = files[i];
                    i++;
                    Intrinsics.checkNotNullExpressionValue(f, "f");
                    delete(f, deleteRootDir);
                    result = deleteResolveEBUSY(f);
                }
            }
            if (deleteRootDir) {
                result = deleteResolveEBUSY(file);
            }
        }
        return result;
    }

    private final boolean deleteResolveEBUSY(File file) {
        File to = new File(Intrinsics.stringPlus(file.getAbsolutePath(), Long.valueOf(System.currentTimeMillis())));
        file.renameTo(to);
        return to.delete();
    }

    public static /* synthetic */ boolean delete$default(FileUtils fileUtils, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return fileUtils.delete(str, z);
    }

    @JvmOverloads
    public final boolean delete(@NotNull String path, boolean deleteRootDir) {
        Intrinsics.checkNotNullParameter(path, "path");
        File file = new File(path);
        if (file.exists()) {
            return delete(file, deleteRootDir);
        }
        return false;
    }

    public final boolean copy(@NotNull String src, @NotNull String tar) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        Intrinsics.checkNotNullParameter(tar, "tar");
        File srcFile = new File(src);
        return srcFile.exists() && copy(srcFile, new File(tar));
    }

    public final boolean copy(@NotNull File src, @NotNull File tar) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        Intrinsics.checkNotNullParameter(tar, "tar");
        try {
            if (src.isFile()) {
                FileInputStream is = new FileInputStream(src);
                FileOutputStream op = new FileOutputStream(tar);
                BufferedInputStream bis = new BufferedInputStream(is);
                BufferedOutputStream bos = new BufferedOutputStream(op);
                byte[] bt = new byte[IOUtil.DEFAULT_BUFFER_SIZE];
                while (true) {
                    int len = bis.read(bt);
                    if (len != -1) {
                        bos.write(bt, 0, len);
                    } else {
                        bis.close();
                        bos.close();
                        return true;
                    }
                }
            } else {
                if (src.isDirectory()) {
                    tar.mkdirs();
                    File[] fileArrListFiles = src.listFiles();
                    if (fileArrListFiles == null) {
                        return true;
                    }
                    for (File file : fileArrListFiles) {
                        FileUtils fileUtils = INSTANCE;
                        File absoluteFile = file.getAbsoluteFile();
                        Intrinsics.checkNotNullExpressionValue(absoluteFile, "file.absoluteFile");
                        fileUtils.copy(absoluteFile, new File(tar.getAbsoluteFile(), file.getName()));
                    }
                    return true;
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public final boolean move(@NotNull String src, @NotNull String tar) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        Intrinsics.checkNotNullParameter(tar, "tar");
        return move(new File(src), new File(tar));
    }

    public final boolean move(@NotNull File src, @NotNull File tar) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        Intrinsics.checkNotNullParameter(tar, "tar");
        return rename(src, tar);
    }

    public final boolean rename(@NotNull String oldPath, @NotNull String newPath) {
        Intrinsics.checkNotNullParameter(oldPath, "oldPath");
        Intrinsics.checkNotNullParameter(newPath, "newPath");
        return rename(new File(oldPath), new File(newPath));
    }

    public final boolean rename(@NotNull File src, @NotNull File tar) {
        Intrinsics.checkNotNullParameter(src, NCXDocumentV2.NCXAttributes.src);
        Intrinsics.checkNotNullParameter(tar, "tar");
        return src.renameTo(tar);
    }

    public static /* synthetic */ String readText$default(FileUtils fileUtils, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "utf-8";
        }
        return fileUtils.readText(str, str2);
    }

    @JvmOverloads
    @NotNull
    public final String readText(@NotNull String filepath, @NotNull String charset) {
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        Intrinsics.checkNotNullParameter(charset, "charset");
        try {
            byte[] data = readBytes(filepath);
            if (data == null) {
                return PackageDocumentBase.PREFIX_OPF;
            }
            Charset charsetForName = Charset.forName(charset);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(charset)");
            CharSequence $this$trim$iv = new String(data, charsetForName);
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
            return $this$trim$iv$iv.subSequence(startIndex$iv$iv, endIndex$iv$iv + 1).toString();
        } catch (UnsupportedEncodingException e) {
            return PackageDocumentBase.PREFIX_OPF;
        }
    }

    @Nullable
    public final byte[] readBytes(@NotNull String filepath) {
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            while (true) {
                int len = fis.read(buffer, 0, buffer.length);
                if (len != -1) {
                    baos.write(buffer, 0, len);
                } else {
                    byte[] data = baos.toByteArray();
                    baos.close();
                    closeSilently(fis);
                    return data;
                }
            }
        } catch (IOException e) {
            closeSilently(fis);
            return null;
        } catch (Throwable th) {
            closeSilently(fis);
            throw th;
        }
    }

    public static /* synthetic */ boolean writeText$default(FileUtils fileUtils, String str, String str2, String str3, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = "utf-8";
        }
        return fileUtils.writeText(str, str2, str3);
    }

    @JvmOverloads
    public final boolean writeText(@NotNull String filepath, @NotNull String content, @NotNull String charset) {
        boolean zWriteBytes;
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        Intrinsics.checkNotNullParameter(content, "content");
        Intrinsics.checkNotNullParameter(charset, "charset");
        try {
            Charset charsetForName = Charset.forName(charset);
            Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
            byte[] bytes = content.getBytes(charsetForName);
            Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
            zWriteBytes = writeBytes(filepath, bytes);
        } catch (UnsupportedEncodingException e) {
            zWriteBytes = false;
        }
        return zWriteBytes;
    }

    public final boolean writeBytes(@NotNull String filepath, @NotNull byte[] data) {
        boolean z;
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        Intrinsics.checkNotNullParameter(data, "data");
        File file = new File(filepath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            fos = new FileOutputStream(filepath);
            fos.write(data);
            z = true;
            closeSilently(fos);
        } catch (IOException e) {
            z = false;
            closeSilently(fos);
        } catch (Throwable th) {
            closeSilently(fos);
            throw th;
        }
        return z;
    }

    public final boolean writeInputStream(@NotNull String filepath, @NotNull InputStream data) {
        Intrinsics.checkNotNullParameter(filepath, "filepath");
        Intrinsics.checkNotNullParameter(data, "data");
        File file = new File(filepath);
        return writeInputStream(file, data);
    }

    public final boolean writeInputStream(@NotNull File file, @NotNull InputStream data) {
        boolean z;
        byte[] buffer;
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(data, "data");
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            buffer = new byte[4096];
            fos = new FileOutputStream(file);
        } catch (IOException e) {
            z = false;
            closeSilently(fos);
        } catch (Throwable th) {
            closeSilently(fos);
            throw th;
        }
        while (true) {
            int len = data.read(buffer, 0, buffer.length);
            if (len == -1) {
                break;
            }
            fos.write(buffer, 0, len);
            return z;
        }
        data.close();
        fos.flush();
        z = true;
        closeSilently(fos);
        return z;
    }

    public final boolean appendText(@NotNull String path, @NotNull String content) {
        boolean z;
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(content, "content");
        File file = new File(path);
        FileWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file, true);
            writer.write(content);
            z = true;
            closeSilently(writer);
        } catch (IOException e) {
            z = false;
            closeSilently(writer);
        } catch (Throwable th) {
            closeSilently(writer);
            throw th;
        }
        return z;
    }

    public final long getLength(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        File file = new File(path);
        if (!file.isFile() || !file.exists()) {
            return 0L;
        }
        return file.length();
    }

    @NotNull
    public final String getName(@Nullable String pathOrUrl) {
        if (pathOrUrl == null) {
            return PackageDocumentBase.PREFIX_OPF;
        }
        int pos = StringsKt.lastIndexOf$default(pathOrUrl, '/', 0, false, 6, (Object) null);
        if (0 <= pos) {
            String strSubstring = pathOrUrl.substring(pos + 1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }
        return System.currentTimeMillis() + '.' + getExtension(pathOrUrl);
    }

    @NotNull
    public final String getNameExcludeExtension(@NotNull String path) {
        String str;
        Intrinsics.checkNotNullParameter(path, "path");
        try {
            String fileName = new File(path).getName();
            Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
            int lastIndexOf = StringsKt.lastIndexOf$default(fileName, ".", 0, false, 6, (Object) null);
            if (lastIndexOf != -1) {
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                String strSubstring = fileName.substring(0, lastIndexOf);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                fileName = strSubstring;
            }
            String str2 = fileName;
            Intrinsics.checkNotNullExpressionValue(str2, "{\n            var fileName = File(path).name\n            val lastIndexOf = fileName.lastIndexOf(\".\")\n            if (lastIndexOf != -1) {\n                fileName = fileName.substring(0, lastIndexOf)\n            }\n            fileName\n        }");
            str = str2;
        } catch (Exception e) {
            str = PackageDocumentBase.PREFIX_OPF;
        }
        return str;
    }

    @NotNull
    public final String getSize(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        long fileSize = getLength(path);
        return toFileSizeString(fileSize);
    }

    @NotNull
    public final String toFileSizeString(long fileSize) {
        String strStringPlus;
        DecimalFormat df = new DecimalFormat("0.00");
        if (fileSize < KB) {
            strStringPlus = new StringBuilder().append(fileSize).append('B').toString();
        } else if (fileSize < MB) {
            strStringPlus = Intrinsics.stringPlus(df.format(fileSize / KB), "K");
        } else {
            strStringPlus = fileSize < GB ? Intrinsics.stringPlus(df.format(fileSize / MB), "M") : Intrinsics.stringPlus(df.format(fileSize / GB), "G");
        }
        String fileSizeString = strStringPlus;
        return fileSizeString;
    }

    @NotNull
    public final String getExtension(@NotNull String pathOrUrl) {
        Intrinsics.checkNotNullParameter(pathOrUrl, "pathOrUrl");
        int dotPos = StringsKt.lastIndexOf$default(pathOrUrl, '.', 0, false, 6, (Object) null);
        if (0 <= dotPos) {
            String strSubstring = pathOrUrl.substring(dotPos + 1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }
        return "ext";
    }

    public static /* synthetic */ String getFileExtetion$default(FileUtils fileUtils, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = PackageDocumentBase.PREFIX_OPF;
        }
        return fileUtils.getFileExtetion(str, str2);
    }

    @NotNull
    public final String getFileExtetion(@NotNull String url, @NotNull String defaultExt) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(defaultExt, "defaultExt");
        try {
            List seqs = StringsKt.split(url, new String[]{"?"}, true, 2);
            String file = (String) CollectionsKt.last(StringsKt.split$default((CharSequence) seqs.get(0), new String[]{TableOfContents.DEFAULT_PATH_SEPARATOR}, false, 0, 6, (Object) null));
            int dotPos = StringsKt.lastIndexOf$default(file, '.', 0, false, 6, (Object) null);
            if (0 <= dotPos) {
                int i = dotPos + 1;
                if (file == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                String strSubstring = file.substring(i);
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                return strSubstring;
            }
            return defaultExt;
        } catch (Exception e) {
            return defaultExt;
        }
    }

    @NotNull
    public final String getMimeType(@NotNull String pathOrUrl) throws Exception {
        Intrinsics.checkNotNullParameter(pathOrUrl, "pathOrUrl");
        throw new Exception("Not implemented");
    }

    public static /* synthetic */ String getDateTime$default(FileUtils fileUtils, String str, String str2, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = "yyyy年MM月dd日HH:mm";
        }
        return fileUtils.getDateTime(str, str2);
    }

    @JvmOverloads
    @NotNull
    public final String getDateTime(@NotNull String path, @NotNull String format) {
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(format, PackageDocumentBase.DCTags.format);
        File file = new File(path);
        return getDateTime(file, format);
    }

    @NotNull
    public final String getDateTime(@NotNull File file, @NotNull String format) {
        Intrinsics.checkNotNullParameter(file, "file");
        Intrinsics.checkNotNullParameter(format, PackageDocumentBase.DCTags.format);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(file.lastModified());
        String str = new SimpleDateFormat(format, Locale.PRC).format(cal.getTime());
        Intrinsics.checkNotNullExpressionValue(str, "SimpleDateFormat(format, Locale.PRC).format(cal.time)");
        return str;
    }

    public final int compareLastModified(@NotNull String path1, @NotNull String path2) {
        Intrinsics.checkNotNullParameter(path1, "path1");
        Intrinsics.checkNotNullParameter(path2, "path2");
        long stamp1 = new File(path1).lastModified();
        long stamp2 = new File(path2).lastModified();
        if (stamp1 > stamp2) {
            return 1;
        }
        if (stamp1 < stamp2) {
            return -1;
        }
        return 0;
    }

    public final boolean makeDirs(@NotNull String path) {
        Intrinsics.checkNotNullParameter(path, "path");
        return makeDirs(new File(path));
    }

    public final boolean makeDirs(@NotNull File file) {
        Intrinsics.checkNotNullParameter(file, "file");
        return file.mkdirs();
    }

    /* JADX INFO: compiled from: FilesUtil.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils$SortByExtension.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\b"}, d2 = {"Lio/legado/app/utils/FileUtils$SortByExtension;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", PackageDocumentBase.PREFIX_OPF, "f1", "f2", "reader-pro"})
    public static final class SortByExtension implements Comparator<File> {
        @Override // java.util.Comparator
        public int compare(@Nullable File f1, @Nullable File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                }
                return 1;
            }
            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if (f1.isFile() && f2.isDirectory()) {
                return 1;
            }
            String name = f1.getName();
            Intrinsics.checkNotNullExpressionValue(name, "f1.name");
            String name2 = f2.getName();
            Intrinsics.checkNotNullExpressionValue(name2, "f2.name");
            return StringsKt.compareTo(name, name2, true);
        }
    }

    /* JADX INFO: compiled from: FilesUtil.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils$SortByName.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u0007\b\u0016¢\u0006\u0002\u0010\u0006J\u001c\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\u00022\b\u0010\n\u001a\u0004\u0018\u00010\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lio/legado/app/utils/FileUtils$SortByName;", "Ljava/util/Comparator;", "Ljava/io/File;", "caseSensitive", PackageDocumentBase.PREFIX_OPF, "(Z)V", "()V", "compare", PackageDocumentBase.PREFIX_OPF, "f1", "f2", "reader-pro"})
    public static final class SortByName implements Comparator<File> {
        private boolean caseSensitive;

        public SortByName(boolean caseSensitive) {
            this.caseSensitive = caseSensitive;
        }

        public SortByName() {
            this.caseSensitive = false;
        }

        @Override // java.util.Comparator
        public int compare(@Nullable File f1, @Nullable File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                }
                return 1;
            }
            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if (f1.isFile() && f2.isDirectory()) {
                return 1;
            }
            String s1 = f1.getName();
            String s2 = f2.getName();
            if (this.caseSensitive) {
                Intrinsics.checkNotNullExpressionValue(s1, "s1");
                Intrinsics.checkNotNullExpressionValue(s2, "s2");
                return StringsKt.compareTo(s1, s2, false);
            }
            Intrinsics.checkNotNullExpressionValue(s1, "s1");
            Intrinsics.checkNotNullExpressionValue(s2, "s2");
            return StringsKt.compareTo(s1, s2, true);
        }
    }

    /* JADX INFO: compiled from: FilesUtil.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils$SortBySize.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\b"}, d2 = {"Lio/legado/app/utils/FileUtils$SortBySize;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", PackageDocumentBase.PREFIX_OPF, "f1", "f2", "reader-pro"})
    public static final class SortBySize implements Comparator<File> {
        @Override // java.util.Comparator
        public int compare(@Nullable File f1, @Nullable File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                }
                return 1;
            }
            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if ((!f1.isFile() || !f2.isDirectory()) && f1.length() < f2.length()) {
                return -1;
            }
            return 1;
        }
    }

    /* JADX INFO: compiled from: FilesUtil.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/utils/FileUtils$SortByTime.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u001c\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00022\b\u0010\u0007\u001a\u0004\u0018\u00010\u0002H\u0016¨\u0006\b"}, d2 = {"Lio/legado/app/utils/FileUtils$SortByTime;", "Ljava/util/Comparator;", "Ljava/io/File;", "()V", "compare", PackageDocumentBase.PREFIX_OPF, "f1", "f2", "reader-pro"})
    public static final class SortByTime implements Comparator<File> {
        @Override // java.util.Comparator
        public int compare(@Nullable File f1, @Nullable File f2) {
            if (f1 == null || f2 == null) {
                if (f1 == null) {
                    return -1;
                }
                return 1;
            }
            if (f1.isDirectory() && f2.isFile()) {
                return -1;
            }
            if ((!f1.isFile() || !f2.isDirectory()) && f1.lastModified() > f2.lastModified()) {
                return -1;
            }
            return 1;
        }
    }
}
