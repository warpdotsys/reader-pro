// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import java.lang.reflect.Type;
import io.legado.app.utils.MapDeserializerDoubleAsIntFix;
import com.google.gson.GsonBuilder;
import kotlin.jvm.functions.Function0;
import mu.KotlinLogging;
import kotlin.Pair;
import java.net.Socket;
import javax.net.SocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import kotlin.text.Charsets;
import java.io.Reader;
import java.io.OutputStreamWriter;
import javax.net.ssl.SSLSocketFactory;
import kotlin.jvm.functions.Function3;
import kotlin.text.Regex;
import java.security.PublicKey;
import io.legado.app.utils.EncoderUtils;
import java.security.spec.X509EncodedKeySpec;
import io.legado.app.utils.Base64;
import java.security.spec.KeySpec;
import java.security.KeyFactory;
import com.htmake.reader.entity.License;
import java.util.UUID;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.MD5Utils;
import io.legado.app.data.entities.Book;
import java.lang.reflect.Array;
import com.google.gson.reflect.TypeToken;
import java.util.Map;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.JsonFactory;
import kotlin.io.CloseableKt;
import kotlin.Unit;
import io.vertx.core.json.JsonObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import java.io.Closeable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.jvm.functions.Function1;
import java.util.Set;
import io.vertx.core.json.JsonArray;
import com.mongodb.client.result.UpdateResult;
import kotlin.jvm.internal.DefaultConstructorMarker;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import com.htmake.reader.entity.MongoFile;
import com.mongodb.client.MongoCollection;
import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.SpreadBuilder;
import java.util.Arrays;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.htmake.reader.config.AppConfig;
import java.util.Iterator;
import java.io.FileInputStream;
import java.util.zip.ZipOutputStream;
import kotlin.collections.CollectionsKt;
import kotlin.collections.ArraysKt;
import java.util.Enumeration;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import okhttp3.HttpUrl;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Intrinsics;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import mu.KLogger;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 2, xi = 48, d1 = { "\u0000¡ì\u0001\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0010\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\n*\u0001\u000e\u001a\u0012\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010$\u001a\u0004\u0018\u00010%\u001a\u0012\u0010&\u001a\u0004\u0018\u00010'2\b\u0010$\u001a\u0004\u0018\u00010%\u001a\u0016\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\u0017\u001a\u000e\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0017\u001a\u000e\u0010.\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0017\u001a\u0010\u0010/\u001a\u0004\u0018\u00010\u00172\u0006\u00100\u001a\u00020\u0017\u001a\u0010\u00101\u001a\u0004\u0018\u0001022\u0006\u00100\u001a\u00020\u0017\u001a\u000e\u00103\u001a\u00020\u00172\u0006\u00104\u001a\u00020\u0017\u001a\u0016\u00105\u001a\u00020\u00172\u0006\u00106\u001a\u00020\u00172\u0006\u00107\u001a\u00020\u0017\u001a6\u00108\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00010:092\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u0017092\u0006\u0010<\u001a\u00020\u00172\u0006\u0010=\u001a\u00020\u0017\u001a\u0010\u0010>\u001a\u0002022\b\b\u0002\u0010?\u001a\u00020\u0003\u001a\u000e\u0010@\u001a\n\u0012\u0004\u0012\u00020B\u0018\u00010A\u001a\u000e\u0010C\u001a\u00020\u00172\u0006\u0010D\u001a\u00020\u0001\u001a\u001f\u0010E\u001a\u00020\u00172\u0012\u0010F\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u0017?\u0006\u0002\u0010H\u001a+\u0010I\u001a\u0004\u0018\u00010\u00172\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\b\b\u0002\u0010K\u001a\u00020\u0017?\u0006\u0002\u0010L\u001a)\u0010M\u001a\u00020,2\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\b\b\u0002\u0010K\u001a\u00020\u0017?\u0006\u0002\u0010N\u001a\u0006\u0010O\u001a\u00020\u0017\u001a\u0006\u0010P\u001a\u00020\u0017\u001a\u001f\u0010Q\u001a\u00020\u00172\u0012\u0010F\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u0017?\u0006\u0002\u0010H\u001a\u0010\u0010Q\u001a\u00020\u00172\b\b\u0002\u0010R\u001a\u00020\u0017\u001a\u0018\u0010S\u001a\u00020\u00172\u0006\u0010$\u001a\u00020%2\b\b\u0002\u0010T\u001a\u00020\u0003\u001ar\u0010U\u001a\u0004\u0018\u00010#2\u0006\u0010V\u001a\u00020,2\u0010\b\u0002\u0010W\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\u0010\b\u0002\u0010Y\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\b\b\u0002\u0010Z\u001a\u00020\u00012\b\b\u0002\u0010[\u001a\u00020\u00012\u0010\b\u0002\u0010\\\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\u0016\b\u0002\u0010]\u001a\u0010\u0012\u0004\u0012\u00020_\u0012\u0004\u0012\u00020\u0003\u0018\u00010^\u001a!\u0010`\u001a\u0002Ha\"\u0004\b\u0000\u0010a2\u0006\u0010b\u001a\u00020%2\u0006\u0010c\u001a\u00020\u0017?\u0006\u0002\u0010d\u001a\u0010\u0010e\u001a\u0004\u0018\u00010\u00172\u0006\u0010f\u001a\u00020\u0017\u001a\u0016\u0010g\u001a\u00020\u00032\u0006\u0010f\u001a\u00020\u00172\u0006\u00100\u001a\u00020\u0017\u001a;\u0010h\u001a\u00020i2\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\u0006\u0010$\u001a\u00020%2\b\b\u0002\u0010T\u001a\u00020\u00032\b\b\u0002\u0010K\u001a\u00020\u0017?\u0006\u0002\u0010j\u001a\u001e\u0010k\u001a\u00020\u00032\u0006\u0010l\u001a\u00020\u00172\u0006\u0010<\u001a\u00020\u00172\u0006\u0010=\u001a\u00020\u0017\u001a\u001e\u0010m\u001a\u00020i2\u0006\u0010b\u001a\u00020%2\u0006\u0010c\u001a\u00020\u00172\u0006\u0010n\u001a\u00020%\u001a\u000e\u0010o\u001a\u00020i2\u0006\u0010p\u001a\u00020\u0003\u001a\u000e\u0010q\u001a\u00020\u00032\u0006\u0010r\u001a\u00020\u0017\u001a\u001c\u0010s\u001a\u00020\u00032\f\u0010t\u001a\b\u0012\u0004\u0012\u00020,092\u0006\u0010u\u001a\u00020\u0017\u001a)\u0010v\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010%0%0w\"\u0006\b\u0000\u0010x\u0018\u0001*\b\u0012\u0004\u0012\u0002Hx0wH\u0086\b\u001a \u0010y\u001a\u0002Hz\"\u0004\b\u0000\u0010{\"\u0006\b\u0001\u0010z\u0018\u0001*\u0002H{H\u0086\b?\u0006\u0002\u0010|\u001a%\u0010}\u001a\b\u0012\u0004\u0012\u00020,09*\u00020,2\u000e\u0010~\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010G?\u0006\u0002\u0010\u007f\u001a\u000b\u0010\u0080\u0001\u001a\u00020i*\u00020,\u001a&\u0010\u0081\u0001\u001a\u00030\u0082\u0001*\u00030\u0082\u00012\b\u0010\u0083\u0001\u001a\u00030\u0082\u00012\r\u0010\u0084\u0001\u001a\b\u0012\u0004\u0012\u00020\u001709\u001a\u0011\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u00020,09*\u00020,\u001a$\u0010\u0086\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001\"\u0004\b\u0000\u0010x*\u0002Hx?\u0006\u0003\u0010\u0088\u0001\u001a)\u0010\u0089\u0001\u001a\u0002Hx\"\u0006\b\u0000\u0010x\u0018\u0001*\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001H\u0086\b?\u0006\u0003\u0010\u008a\u0001\u001a\u0016\u0010\u008b\u0001\u001a\u00020\u0017*\u00020\u00172\t\b\u0002\u0010\u008c\u0001\u001a\u00020\u0003\u001a$\u0010\u008d\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001\"\u0004\b\u0000\u0010x*\u0002Hx?\u0006\u0003\u0010\u0088\u0001\u001a\u0014\u0010\u008e\u0001\u001a\u00020\u0003*\u00020,2\u0007\u0010\u008f\u0001\u001a\u00020\u0017\u001a\u000b\u0010\u0090\u0001\u001a\u00020\u0017*\u00020\u0017\u001a\u0012\u0010s\u001a\u00020\u0003*\u00020,2\u0006\u0010u\u001a\u00020\u0017\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T?\u0006\u0002\n\u0000\"\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007\"\u0019\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\t?\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0010\u0010\r\u001a\u00020\u000eX\u0082\u0004?\u0006\u0004\n\u0002\u0010\u000f\"\u0011\u0010\u0010\u001a\u00020\u0011?\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0019\u0010\u0014\u001a\n \n*\u0004\u0018\u00010\t0\t?\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\"\u001a\u0010\u001c\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0005\"\u0004\b\u001e\u0010\u0007\"\u001a\u0010\u001f\u001a\u00020\u0017X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0019\"\u0004\b!\u0010\u001b¡§\u0006\u0091\u0001" }, d2 = { "MAX_CACHE_SIZE", "", "_licenseValid", "", "get_licenseValid", "()Z", "set_licenseValid", "(Z)V", "gson", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType", "getGson", "()Lcom/google/gson/Gson;", "lockMap", "com/htmake/reader/utils/ExtKt$lockMap$1", "Lcom/htmake/reader/utils/ExtKt$lockMap$1;", "logger", "Lmu/KLogger;", "getLogger", "()Lmu/KLogger;", "prettyGson", "getPrettyGson", "storageFinalPath", "", "getStorageFinalPath", "()Ljava/lang/String;", "setStorageFinalPath", "(Ljava/lang/String;)V", "workDirInit", "getWorkDirInit", "setWorkDirInit", "workDirPath", "getWorkDirPath", "setWorkDirPath", "asJsonArray", "Lio/vertx/core/json/JsonArray;", "value", "", "asJsonObject", "Lio/vertx/core/json/JsonObject;", "countOccurrences", "str", "subStr", "createDir", "Ljava/io/File;", "filePath", "createFile", "decryptData", "content", "decryptToLicense", "Lcom/htmake/reader/entity/License;", "encodeBase64", "text", "genEncryptedPassword", "password", "salt", "getCommand", "", "Lkotlin/Pair;", "to", "subject", "body", "getInstalledLicense", "ignoreInvalid", "getMongoFileStorage", "Lcom/mongodb/client/MongoCollection;", "Lcom/htmake/reader/entity/MongoFile;", "getRandomString", "length", "getRelativePath", "subDirFiles", "", "([Ljava/lang/String;)Ljava/lang/String;", "getStorage", "name", "ext", "([Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "getStorageFile", "([Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;", "getStoragePath", "getTraceId", "getWorkDir", "subPath", "jsonEncode", "pretty", "parseJsonStringList", "file", "fields", "", "exclude", "startIndex", "endIndex", "checkNotEmpty", "filter", "Lkotlin/Function1;", "Lcom/fasterxml/jackson/databind/node/ObjectNode;", "readInstanceProperty", "R", "instance", "propertyName", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", "readMongoFile", "path", "saveMongoFile", "saveStorage", "", "([Ljava/lang/String;Ljava/lang/Object;ZLjava/lang/String;)V", "sendEmail", "toEmail", "setInstanceProperty", "propertyValue", "setLicenseValid", "isValid", "validateEmail", "email", "zip", "files", "zipFilePath", "arrayType", "Ljava/lang/Class;", "T", "convert", "O", "I", "(Ljava/lang/Object;)Ljava/lang/Object;", "deepListFiles", "allowExtensions", "(Ljava/io/File;[Ljava/lang/String;)Ljava/util/List;", "deleteRecursively", "fillData", "Lio/legado/app/data/entities/Book;", "newBook", "keys", "listFilesRecursively", "serializeToMap", "", "(Ljava/lang/Object;)Ljava/util/Map;", "toDataClass", "(Ljava/util/Map;)Ljava/lang/Object;", "toDir", "absolute", "toMap", "unzip", "descDir", "url", "reader-pro" })
public final class ExtKt
{
    @NotNull
    private static final KLogger logger;
    private static final Gson gson;
    private static final Gson prettyGson;
    @NotNull
    private static String storageFinalPath;
    @NotNull
    private static String workDirPath;
    private static boolean workDirInit;
    private static final int MAX_CACHE_SIZE = 1000;
    @NotNull
    private static final ExtKt$lockMap.ExtKt$lockMap$1 lockMap;
    private static boolean _licenseValid;
    
    @NotNull
    public static final KLogger getLogger() {
        return ExtKt.logger;
    }
    
    public static final Gson getGson() {
        return ExtKt.gson;
    }
    
    public static final Gson getPrettyGson() {
        return ExtKt.prettyGson;
    }
    
    @NotNull
    public static final String getStorageFinalPath() {
        return ExtKt.storageFinalPath;
    }
    
    public static final void setStorageFinalPath(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        ExtKt.storageFinalPath = <set-?>;
    }
    
    @NotNull
    public static final String getWorkDirPath() {
        return ExtKt.workDirPath;
    }
    
    public static final void setWorkDirPath(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        ExtKt.workDirPath = <set-?>;
    }
    
    public static final boolean getWorkDirInit() {
        return ExtKt.workDirInit;
    }
    
    public static final void setWorkDirInit(final boolean <set-?>) {
        ExtKt.workDirInit = <set-?>;
    }
    
    @NotNull
    public static final String url(@NotNull final String $this$url) {
        Intrinsics.checkNotNullParameter((Object)$this$url, "<this>");
        if (StringsKt.startsWith$default($this$url, "//", false, 2, (Object)null)) {
            return HttpUrl.Companion.get(Intrinsics.stringPlus("http:", (Object)$this$url)).toString();
        }
        if (StringsKt.startsWith$default($this$url, "http", false, 2, (Object)null)) {
            return HttpUrl.Companion.get($this$url).toString();
        }
        return $this$url;
    }
    
    @NotNull
    public static final String toDir(@NotNull final String $this$toDir, final boolean absolute) {
        Intrinsics.checkNotNullParameter((Object)$this$toDir, "<this>");
        String path = $this$toDir;
        if (StringsKt.endsWith$default(path, "/", false, 2, (Object)null)) {
            final String substring = path.substring(0, path.length() - 1);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
            path = substring;
        }
        if (absolute && !StringsKt.startsWith$default(path, "/", false, 2, (Object)null)) {
            path = Intrinsics.stringPlus("/", (Object)path);
        }
        return path;
    }
    
    public static final void deleteRecursively(@NotNull final File $this$deleteRecursively) {
        Intrinsics.checkNotNullParameter((Object)$this$deleteRecursively, "<this>");
        if ($this$deleteRecursively.exists()) {
            if ($this$deleteRecursively.isFile()) {
                $this$deleteRecursively.delete();
            }
            else {
                final File[] listFiles = $this$deleteRecursively.listFiles();
                Intrinsics.checkNotNullExpressionValue((Object)listFiles, "this.listFiles()");
                final Object[] $this$forEach$iv = listFiles;
                final int $i$f$forEach = 0;
                for (final Object element$iv : $this$forEach$iv) {
                    final File it = (File)element$iv;
                    final int n = 0;
                    Intrinsics.checkNotNullExpressionValue((Object)it, "it");
                    deleteRecursively(it);
                }
                $this$deleteRecursively.delete();
            }
        }
    }
    
    @NotNull
    public static final List<File> listFilesRecursively(@NotNull final File $this$listFilesRecursively) {
        Intrinsics.checkNotNullParameter((Object)$this$listFilesRecursively, "<this>");
        Object list = null;
        list = new ArrayList();
        if ($this$listFilesRecursively.exists()) {
            if ($this$listFilesRecursively.isFile()) {
                ((ArrayList<File>)list).add($this$listFilesRecursively);
            }
            else {
                final File[] listFiles = $this$listFilesRecursively.listFiles();
                Intrinsics.checkNotNullExpressionValue((Object)listFiles, "this.listFiles()");
                final Object[] $this$forEach$iv = listFiles;
                final int $i$f$forEach = 0;
                for (final Object element$iv : $this$forEach$iv) {
                    final File it = (File)element$iv;
                    final int n = 0;
                    ((ArrayList<File>)list).add(it);
                    if (it.isDirectory()) {
                        final Object o = list;
                        Intrinsics.checkNotNullExpressionValue((Object)it, "it");
                        ((ArrayList)o).addAll(listFilesRecursively(it));
                    }
                }
            }
        }
        return (List)list;
    }
    
    public static final boolean unzip(@NotNull final File $this$unzip, @NotNull final String descDir) {
        Intrinsics.checkNotNullParameter((Object)$this$unzip, "<this>");
        Intrinsics.checkNotNullParameter((Object)descDir, "descDir");
        if (!$this$unzip.exists()) {
            return false;
        }
        final byte[] buffer = new byte[1024];
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            final ZipFile zf = new ZipFile($this$unzip.toString());
            final Enumeration entries = zf.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry nextElement = entries.nextElement();
                if (nextElement == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
                }
                final ZipEntry zipEntry = nextElement;
                final String name = zipEntry.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name, "zipEntry.name");
                final String zipEntryName = name;
                final String descFilePath = descDir + (Object)File.separator + zipEntryName;
                if (zipEntry.isDirectory()) {
                    createDir(descFilePath);
                }
                else {
                    inputStream = zf.getInputStream(zipEntry);
                    final File descFile = createFile(descFilePath);
                    outputStream = new FileOutputStream(descFile);
                    int len = 0;
                    while (true) {
                        final int it = inputStream.read(buffer);
                        final int n = 0;
                        len = it;
                        if (it <= 0) {
                            break;
                        }
                        ((FileOutputStream)outputStream).write(buffer, 0, len);
                    }
                    inputStream.close();
                    ((FileOutputStream)outputStream).close();
                }
            }
            return true;
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        finally {
            try (final InputStream inputStream2 = inputStream) {}
            try (final OutputStream outputStream2 = outputStream) {}
        }
        return false;
    }
    
    public static final boolean zip(@NotNull final File $this$zip, @NotNull final String zipFilePath) {
        Intrinsics.checkNotNullParameter((Object)$this$zip, "<this>");
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        if (!$this$zip.exists()) {
            return false;
        }
        if ($this$zip.isDirectory()) {
            final File[] files = $this$zip.listFiles();
            Intrinsics.checkNotNullExpressionValue((Object)files, "files");
            final List filesList = ArraysKt.toList((Object[])files);
            return zip(filesList, zipFilePath);
        }
        return zip(CollectionsKt.arrayListOf((Object[])new File[] { $this$zip }), zipFilePath);
    }
    
    public static final boolean zip(@NotNull final List<? extends File> files, @NotNull final String zipFilePath) {
        Intrinsics.checkNotNullParameter((Object)files, "files");
        Intrinsics.checkNotNullParameter((Object)zipFilePath, "zipFilePath");
        if (files.isEmpty()) {
            return false;
        }
        final File zipFile = createFile(zipFilePath);
        final byte[] buffer = new byte[1024];
        ZipOutputStream zipOutputStream = null;
        FileInputStream inputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            for (final File file : files) {
                if (!file.exists()) {
                    continue;
                }
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                inputStream = new FileInputStream(file);
                int len = 0;
                while (true) {
                    final int it = inputStream.read(buffer);
                    final int n = 0;
                    len = it;
                    if (it <= 0) {
                        break;
                    }
                    zipOutputStream.write(buffer, 0, len);
                }
                zipOutputStream.closeEntry();
            }
            final boolean b = true;
            try (final FileInputStream fileInputStream = inputStream) {}
            zipOutputStream.close();
            return b;
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        finally {
            try (final FileInputStream fileInputStream2 = inputStream) {}
            try (final ZipOutputStream zipOutputStream2 = zipOutputStream) {}
        }
        return false;
    }
    
    @NotNull
    public static final File createDir(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        ExtKt.logger.debug("createDir filePath {}", (Object)filePath);
        final File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    @NotNull
    public static final File createFile(@NotNull final String filePath) {
        Intrinsics.checkNotNullParameter((Object)filePath, "filePath");
        ExtKt.logger.debug("createFile filePath {}", (Object)filePath);
        final File file = new File(filePath);
        final File parentFile2 = file.getParentFile();
        Intrinsics.checkNotNull((Object)parentFile2);
        final File parentFile = parentFile2;
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
    
    @NotNull
    public static final String getWorkDir(@NotNull final String subPath) {
        Intrinsics.checkNotNullParameter((Object)subPath, "subPath");
        if (!ExtKt.workDirInit && ExtKt.workDirPath.length() == 0) {
            final AppConfig appConfig = (AppConfig)SpringContextUtils.getBean("appConfig", (Class)AppConfig.class);
            if (appConfig != null && appConfig.getWorkDir().length() > 0 && !appConfig.getWorkDir().equals(".")) {
                final File workDirFile = new File(appConfig.getWorkDir());
                if (workDirFile.exists() && !workDirFile.isDirectory()) {
                    ExtKt.logger.error("reader.app.workDir={} is not a directory", (Object)appConfig.getWorkDir());
                }
                else {
                    if (!workDirFile.exists()) {
                        ExtKt.logger.info("reader.app.workDir={} not exists, creating", (Object)appConfig.getWorkDir());
                        workDirFile.mkdirs();
                    }
                    final String absolutePath = workDirFile.getAbsolutePath();
                    Intrinsics.checkNotNullExpressionValue((Object)absolutePath, "workDirFile.absolutePath");
                    ExtKt.workDirPath = absolutePath;
                }
            }
            Label_0333: {
                if (ExtKt.workDirPath.length() == 0) {
                    final String osName = System.getProperty("os.name");
                    final String currentDir = System.getProperty("user.dir");
                    ExtKt.logger.info("osName: {} currentDir: {}", (Object)osName, (Object)currentDir);
                    final String s = osName;
                    Intrinsics.checkNotNullExpressionValue((Object)s, "osName");
                    if (StringsKt.startsWith(s, "Mac OS", true)) {
                        final String s2 = currentDir;
                        Intrinsics.checkNotNullExpressionValue((Object)s2, "currentDir");
                        if (!StringsKt.startsWith$default(s2, "/Users/", false, 2, (Object)null)) {
                            ExtKt.workDirPath = Paths.get(System.getProperty("user.home"), ".reader").toString();
                            break Label_0333;
                        }
                    }
                    final String workDirPath = currentDir;
                    Intrinsics.checkNotNullExpressionValue((Object)workDirPath, "currentDir");
                    ExtKt.workDirPath = workDirPath;
                }
            }
            ExtKt.logger.info("Using workdir: {}", (Object)ExtKt.workDirPath);
            ExtKt.workDirInit = true;
        }
        final Path path = Paths.get(ExtKt.workDirPath, subPath);
        return path.toString();
    }
    
    @NotNull
    public static final String getWorkDir(@NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        return getWorkDir(getRelativePath((String[])Arrays.copyOf(subDirFiles, subDirFiles.length)));
    }
    
    @NotNull
    public static final String getRelativePath(@NotNull final String... subDirFiles) {
        Intrinsics.checkNotNullParameter((Object)subDirFiles, "subDirFiles");
        final StringBuilder path = new StringBuilder("");
        final Object[] $this$forEach$iv = subDirFiles;
        final int $i$f$forEach = 0;
        for (final String it : $this$forEach$iv) {
            final Object element$iv = it;
            final int n = 0;
            if (it.length() > 0) {
                path.append(File.separator).append(it);
            }
        }
        final String it2 = path.toString();
        final int n2 = 0;
        Intrinsics.checkNotNullExpressionValue((Object)it2, "it");
        String substring;
        if (StringsKt.startsWith$default(it2, "/", false, 2, (Object)null)) {
            Intrinsics.checkNotNullExpressionValue((Object)(substring = it2.substring(1)), "(this as java.lang.String).substring(startIndex)");
        }
        else {
            substring = it2;
        }
        return substring;
    }
    
    @NotNull
    public static final String getStoragePath() {
        if (ExtKt.storageFinalPath.length() > 0) {
            return ExtKt.storageFinalPath;
        }
        String storagePath = "";
        final AppConfig appConfig = (AppConfig)SpringContextUtils.getBean("appConfig", (Class)AppConfig.class);
        if (appConfig != null) {
            storagePath = (ExtKt.storageFinalPath = getWorkDir("storage"));
        }
        else {
            final String path = new File("storage").getPath();
            Intrinsics.checkNotNullExpressionValue((Object)path, "File(\"storage\").path");
            storagePath = path;
        }
        ExtKt.logger.info("Using storagePath: {}", (Object)storagePath);
        return storagePath;
    }
    
    public static final void saveStorage(@NotNull final String[] name, @NotNull final Object value, final boolean pretty, @NotNull final String ext) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc_w           "name"
        //     4: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     7: aload_1         /* value */
        //     8: ldc_w           "value"
        //    11: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    14: aload_3         /* ext */
        //    15: ldc_w           "ext"
        //    18: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    21: aload_1         /* value */
        //    22: instanceof      Ljava/lang/String;
        //    25: ifeq            35
        //    28: aload_1         /* value */
        //    29: checkcast       Ljava/lang/String;
        //    32: goto            101
        //    35: aload_1         /* value */
        //    36: instanceof      Lio/vertx/core/json/JsonObject;
        //    39: ifne            49
        //    42: aload_1         /* value */
        //    43: instanceof      Lio/vertx/core/json/JsonArray;
        //    46: ifeq            56
        //    49: aload_1         /* value */
        //    50: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    53: goto            101
        //    56: iload_2         /* pretty */
        //    57: ifeq            82
        //    60: getstatic       com/htmake/reader/utils/ExtKt.prettyGson:Lcom/google/gson/Gson;
        //    63: aload_1         /* value */
        //    64: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //    67: astore          5
        //    69: aload           5
        //    71: ldc_w           "{\n        prettyGson.toJson(value)\n    }"
        //    74: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    77: aload           5
        //    79: goto            101
        //    82: getstatic       com/htmake/reader/utils/ExtKt.gson:Lcom/google/gson/Gson;
        //    85: aload_1         /* value */
        //    86: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //    89: astore          5
        //    91: aload           5
        //    93: ldc_w           "{\n        gson.toJson(value)\n    }"
        //    96: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //    99: aload           5
        //   101: astore          toJson
        //   103: invokestatic    com/htmake/reader/utils/ExtKt.getStoragePath:()Ljava/lang/String;
        //   106: astore          storagePath
        //   108: new             Ljava/io/File;
        //   111: dup            
        //   112: aload           storagePath
        //   114: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   117: astore          storageDir
        //   119: aload           storageDir
        //   121: invokevirtual   java/io/File.exists:()Z
        //   124: ifne            133
        //   127: aload           storageDir
        //   129: invokevirtual   java/io/File.mkdirs:()Z
        //   132: pop            
        //   133: aload_0         /* name */
        //   134: invokestatic    kotlin/collections/ArraysKt.last:([Ljava/lang/Object;)Ljava/lang/Object;
        //   137: checkcast       Ljava/lang/String;
        //   140: astore          filename
        //   142: new             Lkotlin/jvm/internal/SpreadBuilder;
        //   145: dup            
        //   146: iconst_2       
        //   147: invokespecial   kotlin/jvm/internal/SpreadBuilder.<init>:(I)V
        //   150: astore          9
        //   152: aload           9
        //   154: aload_0         /* name */
        //   155: astore          10
        //   157: iconst_0       
        //   158: istore          11
        //   160: aload_0         /* name */
        //   161: arraylength    
        //   162: iconst_1       
        //   163: isub           
        //   164: istore          12
        //   166: iconst_0       
        //   167: istore          13
        //   169: aload           10
        //   171: iload           11
        //   173: iload           12
        //   175: invokestatic    kotlin/collections/ArraysKt.copyOfRange:([Ljava/lang/Object;II)[Ljava/lang/Object;
        //   178: invokevirtual   kotlin/jvm/internal/SpreadBuilder.addSpread:(Ljava/lang/Object;)V
        //   181: aload           9
        //   183: aload           filename
        //   185: aload_3         /* ext */
        //   186: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   189: invokevirtual   kotlin/jvm/internal/SpreadBuilder.add:(Ljava/lang/Object;)V
        //   192: aload           9
        //   194: aload           9
        //   196: invokevirtual   kotlin/jvm/internal/SpreadBuilder.size:()I
        //   199: anewarray       Ljava/lang/String;
        //   202: invokevirtual   kotlin/jvm/internal/SpreadBuilder.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   205: checkcast       [Ljava/lang/String;
        //   208: invokestatic    com/htmake/reader/utils/ExtKt.getRelativePath:([Ljava/lang/String;)Ljava/lang/String;
        //   211: astore          path
        //   213: new             Ljava/io/File;
        //   216: dup            
        //   217: new             Ljava/lang/StringBuilder;
        //   220: dup            
        //   221: invokespecial   java/lang/StringBuilder.<init>:()V
        //   224: aload           storagePath
        //   226: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   229: getstatic       java/io/File.separator:Ljava/lang/String;
        //   232: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   235: aload           path
        //   237: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   240: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   243: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   246: astore          file
        //   248: getstatic       com/htmake/reader/utils/ExtKt.logger:Lmu/KLogger;
        //   251: ldc_w           "Save file to storage name: {} path: {}"
        //   254: aload_0         /* name */
        //   255: aload           file
        //   257: invokevirtual   java/io/File.getAbsoluteFile:()Ljava/io/File;
        //   260: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   265: aload           file
        //   267: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   270: invokevirtual   java/io/File.exists:()Z
        //   273: ifne            285
        //   276: aload           file
        //   278: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   281: invokevirtual   java/io/File.mkdirs:()Z
        //   284: pop            
        //   285: aload           file
        //   287: invokevirtual   java/io/File.getAbsoluteFile:()Ljava/io/File;
        //   290: astore          10
        //   292: aload           10
        //   294: ldc_w           "file.absoluteFile"
        //   297: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   300: aload           10
        //   302: invokestatic    kotlin/io/FilesKt.getNameWithoutExtension:(Ljava/io/File;)Ljava/lang/String;
        //   305: astore          filename
        //   307: getstatic       com/htmake/reader/utils/ExtKt.lockMap:Lcom/htmake/reader/utils/ExtKt$lockMap$1;
        //   310: astore          11
        //   312: iconst_0       
        //   313: istore          12
        //   315: iconst_0       
        //   316: istore          13
        //   318: aload           11
        //   320: monitorenter   
        //   321: nop            
        //   322: iconst_0       
        //   323: istore          $i$a$-synchronized-ExtKt$saveStorage$lock$1
        //   325: getstatic       com/htmake/reader/utils/ExtKt.lockMap:Lcom/htmake/reader/utils/ExtKt$lockMap$1;
        //   328: checkcast       Ljava/util/Map;
        //   331: astore          15
        //   333: aload           file
        //   335: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   338: astore          16
        //   340: aload           16
        //   342: ldc_w           "file.absolutePath"
        //   345: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   348: aload           16
        //   350: astore          key$iv
        //   352: iconst_0       
        //   353: istore          $i$f$getOrPut
        //   355: aload           $this$getOrPut$iv
        //   357: aload           key$iv
        //   359: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   364: astore          value$iv
        //   366: aload           value$iv
        //   368: ifnonnull       403
        //   371: iconst_0       
        //   372: istore          $i$a$-getOrPut-ExtKt$saveStorage$lock$1$1
        //   374: new             Ljava/util/concurrent/locks/ReentrantReadWriteLock;
        //   377: dup            
        //   378: invokespecial   java/util/concurrent/locks/ReentrantReadWriteLock.<init>:()V
        //   381: checkcast       Ljava/util/concurrent/locks/ReadWriteLock;
        //   384: astore          answer$iv
        //   386: aload           $this$getOrPut$iv
        //   388: aload           key$iv
        //   390: aload           answer$iv
        //   392: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   397: pop            
        //   398: aload           answer$iv
        //   400: goto            405
        //   403: aload           value$iv
        //   405: nop            
        //   406: checkcast       Ljava/util/concurrent/locks/ReadWriteLock;
        //   409: nop            
        //   410: astore          13
        //   412: aload           11
        //   414: monitorexit    
        //   415: aload           13
        //   417: goto            428
        //   420: astore          13
        //   422: aload           11
        //   424: monitorexit    
        //   425: aload           13
        //   427: athrow         
        //   428: astore          lock
        //   430: iconst_0       
        //   431: istore          acquired
        //   433: nop            
        //   434: aload           lock
        //   436: invokeinterface java/util/concurrent/locks/ReadWriteLock.writeLock:()Ljava/util/concurrent/locks/Lock;
        //   441: ldc2_w          10
        //   444: getstatic       java/util/concurrent/TimeUnit.SECONDS:Ljava/util/concurrent/TimeUnit;
        //   447: invokeinterface java/util/concurrent/locks/Lock.tryLock:(JLjava/util/concurrent/TimeUnit;)Z
        //   452: istore          acquired
        //   454: iload           acquired
        //   456: ifne            478
        //   459: new             Ljava/lang/Exception;
        //   462: dup            
        //   463: ldc_w           "\u4fdd\u5b58\u6587\u4ef6\u8d85\u65f6: "
        //   466: aload           file
        //   468: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   471: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   474: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   477: athrow         
        //   478: aload           file
        //   480: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   483: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   486: iconst_0       
        //   487: anewarray       Ljava/lang/String;
        //   490: invokestatic    java/nio/file/Paths.get:(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
        //   493: invokeinterface java/nio/file/Path.toAbsolutePath:()Ljava/nio/file/Path;
        //   498: aload           filename
        //   500: ldc_w           ".temp"
        //   503: iconst_0       
        //   504: anewarray       Ljava/nio/file/attribute/FileAttribute;
        //   507: invokestatic    java/nio/file/Files.createTempFile:(Ljava/nio/file/Path;Ljava/lang/String;Ljava/lang/String;[Ljava/nio/file/attribute/FileAttribute;)Ljava/nio/file/Path;
        //   510: astore          tmp
        //   512: aload           tmp
        //   514: aload           toJson
        //   516: astore          13
        //   518: getstatic       kotlin/text/Charsets.UTF_8:Ljava/nio/charset/Charset;
        //   521: astore          14
        //   523: iconst_0       
        //   524: istore          15
        //   526: aload           13
        //   528: dup            
        //   529: ifnonnull       543
        //   532: new             Ljava/lang/NullPointerException;
        //   535: dup            
        //   536: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   539: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   542: athrow         
        //   543: aload           14
        //   545: invokevirtual   java/lang/String.getBytes:(Ljava/nio/charset/Charset;)[B
        //   548: dup            
        //   549: ldc_w           "(this as java.lang.String).getBytes(charset)"
        //   552: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   555: iconst_0       
        //   556: anewarray       Ljava/nio/file/OpenOption;
        //   559: invokestatic    java/nio/file/Files.write:(Ljava/nio/file/Path;[B[Ljava/nio/file/OpenOption;)Ljava/nio/file/Path;
        //   562: pop            
        //   563: aload           file
        //   565: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   568: iconst_0       
        //   569: anewarray       Ljava/lang/String;
        //   572: invokestatic    java/nio/file/Paths.get:(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
        //   575: astore          filePath
        //   577: aload           file
        //   579: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   582: invokevirtual   java/io/File.getPath:()Ljava/lang/String;
        //   585: iconst_1       
        //   586: anewarray       Ljava/lang/String;
        //   589: astore          15
        //   591: aload           15
        //   593: iconst_0       
        //   594: aload           filename
        //   596: ldc_w           ".backup.json"
        //   599: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   602: aastore        
        //   603: aload           15
        //   605: invokestatic    java/nio/file/Paths.get:(Ljava/lang/String;[Ljava/lang/String;)Ljava/nio/file/Path;
        //   608: invokeinterface java/nio/file/Path.toAbsolutePath:()Ljava/nio/file/Path;
        //   613: astore          backupPath
        //   615: aload           filePath
        //   617: iconst_0       
        //   618: anewarray       Ljava/nio/file/LinkOption;
        //   621: invokestatic    java/nio/file/Files.exists:(Ljava/nio/file/Path;[Ljava/nio/file/LinkOption;)Z
        //   624: ifeq            653
        //   627: aload           filePath
        //   629: aload           backupPath
        //   631: iconst_1       
        //   632: anewarray       Ljava/nio/file/CopyOption;
        //   635: astore          15
        //   637: aload           15
        //   639: iconst_0       
        //   640: getstatic       java/nio/file/StandardCopyOption.ATOMIC_MOVE:Ljava/nio/file/StandardCopyOption;
        //   643: checkcast       Ljava/nio/file/CopyOption;
        //   646: aastore        
        //   647: aload           15
        //   649: invokestatic    java/nio/file/Files.move:(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path;
        //   652: pop            
        //   653: aload           tmp
        //   655: aload           filePath
        //   657: iconst_1       
        //   658: anewarray       Ljava/nio/file/CopyOption;
        //   661: astore          15
        //   663: aload           15
        //   665: iconst_0       
        //   666: getstatic       java/nio/file/StandardCopyOption.ATOMIC_MOVE:Ljava/nio/file/StandardCopyOption;
        //   669: checkcast       Ljava/nio/file/CopyOption;
        //   672: aastore        
        //   673: aload           15
        //   675: invokestatic    java/nio/file/Files.move:(Ljava/nio/file/Path;Ljava/nio/file/Path;[Ljava/nio/file/CopyOption;)Ljava/nio/file/Path;
        //   678: pop            
        //   679: aload           tmp
        //   681: invokestatic    java/nio/file/Files.deleteIfExists:(Ljava/nio/file/Path;)Z
        //   684: pop            
        //   685: aload           filename
        //   687: invokevirtual   java/lang/String.length:()I
        //   690: bipush          32
        //   692: if_icmplt       701
        //   695: aload           backupPath
        //   697: invokestatic    java/nio/file/Files.deleteIfExists:(Ljava/nio/file/Path;)Z
        //   700: pop            
        //   701: ldc_w           "users"
        //   704: aload           filename
        //   706: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   709: ifeq            938
        //   712: aload           toJson
        //   714: ldc_w           "username"
        //   717: invokestatic    com/htmake/reader/utils/ExtKt.countOccurrences:(Ljava/lang/String;Ljava/lang/String;)I
        //   720: istore          userCount
        //   722: new             Lkotlin/jvm/internal/SpreadBuilder;
        //   725: dup            
        //   726: iconst_2       
        //   727: invokespecial   kotlin/jvm/internal/SpreadBuilder.<init>:(I)V
        //   730: astore          17
        //   732: aload           17
        //   734: aload_0         /* name */
        //   735: astore          18
        //   737: iconst_0       
        //   738: istore          19
        //   740: aload_0         /* name */
        //   741: arraylength    
        //   742: iconst_1       
        //   743: isub           
        //   744: istore          20
        //   746: iconst_0       
        //   747: istore          21
        //   749: aload           18
        //   751: iload           19
        //   753: iload           20
        //   755: invokestatic    kotlin/collections/ArraysKt.copyOfRange:([Ljava/lang/Object;II)[Ljava/lang/Object;
        //   758: invokevirtual   kotlin/jvm/internal/SpreadBuilder.addSpread:(Ljava/lang/Object;)V
        //   761: aload           17
        //   763: new             Ljava/lang/StringBuilder;
        //   766: dup            
        //   767: invokespecial   java/lang/StringBuilder.<init>:()V
        //   770: bipush          46
        //   772: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   775: aload           filename
        //   777: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   780: ldc_w           ".key"
        //   783: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   786: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   789: invokevirtual   kotlin/jvm/internal/SpreadBuilder.add:(Ljava/lang/Object;)V
        //   792: aload           17
        //   794: aload           17
        //   796: invokevirtual   kotlin/jvm/internal/SpreadBuilder.size:()I
        //   799: anewarray       Ljava/lang/String;
        //   802: invokevirtual   kotlin/jvm/internal/SpreadBuilder.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   805: checkcast       [Ljava/lang/String;
        //   808: invokestatic    com/htmake/reader/utils/ExtKt.getRelativePath:([Ljava/lang/String;)Ljava/lang/String;
        //   811: astore          verifyKeyPath
        //   813: new             Ljava/io/File;
        //   816: dup            
        //   817: new             Ljava/lang/StringBuilder;
        //   820: dup            
        //   821: invokespecial   java/lang/StringBuilder.<init>:()V
        //   824: aload           storagePath
        //   826: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   829: getstatic       java/io/File.separator:Ljava/lang/String;
        //   832: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   835: aload           verifyKeyPath
        //   837: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   840: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   843: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   846: astore          verifyKeyFile
        //   848: aload           verifyKeyFile
        //   850: invokevirtual   java/io/File.exists:()Z
        //   853: ifne            862
        //   856: aload           verifyKeyFile
        //   858: invokevirtual   java/io/File.createNewFile:()Z
        //   861: pop            
        //   862: getstatic       io/legado/app/utils/MD5Utils.INSTANCE:Lio/legado/app/utils/MD5Utils;
        //   865: ldc_w           "userCount="
        //   868: iload           userCount
        //   870: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   873: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   876: invokevirtual   io/legado/app/utils/MD5Utils.md5Encode:(Ljava/lang/String;)Ljava/lang/String;
        //   879: invokevirtual   java/lang/String.toString:()Ljava/lang/String;
        //   882: astore          md5Encode
        //   884: aload           verifyKeyFile
        //   886: aload           md5Encode
        //   888: astore          19
        //   890: aload           md5Encode
        //   892: invokevirtual   java/lang/String.length:()I
        //   895: bipush          16
        //   897: isub           
        //   898: istore          20
        //   900: iconst_0       
        //   901: istore          21
        //   903: aload           19
        //   905: dup            
        //   906: ifnonnull       920
        //   909: new             Ljava/lang/NullPointerException;
        //   912: dup            
        //   913: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   916: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   919: athrow         
        //   920: iload           20
        //   922: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   925: dup            
        //   926: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   929: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   932: aconst_null    
        //   933: iconst_2       
        //   934: aconst_null    
        //   935: invokestatic    kotlin/io/FilesKt.writeText$default:(Ljava/io/File;Ljava/lang/String;Ljava/nio/charset/Charset;ILjava/lang/Object;)V
        //   938: aload           path
        //   940: aload           toJson
        //   942: invokestatic    com/htmake/reader/utils/ExtKt.saveMongoFile:(Ljava/lang/String;Ljava/lang/String;)Z
        //   945: pop            
        //   946: iload           acquired
        //   948: ifeq            963
        //   951: aload           lock
        //   953: invokeinterface java/util/concurrent/locks/ReadWriteLock.writeLock:()Ljava/util/concurrent/locks/Lock;
        //   958: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   963: goto            1025
        //   966: astore          e
        //   968: getstatic       com/htmake/reader/utils/ExtKt.logger:Lmu/KLogger;
        //   971: ldc_w           "\u4fdd\u5b58\u6587\u4ef6\u5931\u8d25: "
        //   974: aload           e
        //   976: checkcast       Ljava/lang/Throwable;
        //   979: invokeinterface mu/KLogger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   984: new             Ljava/lang/Exception;
        //   987: dup            
        //   988: ldc_w           "\u4fdd\u5b58\u6587\u4ef6\u5931\u8d25: "
        //   991: aload           file
        //   993: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   996: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   999: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //  1002: athrow         
        //  1003: astore          null
        //  1005: iload           acquired
        //  1007: ifeq            1022
        //  1010: aload           lock
        //  1012: invokeinterface java/util/concurrent/locks/ReadWriteLock.writeLock:()Ljava/util/concurrent/locks/Lock;
        //  1017: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //  1022: aload           12
        //  1024: athrow         
        //  1025: return         
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  name    
        //  value   
        //  pretty  
        //  ext     
        //    StackMapTable: 00 17 23 0D 06 19 52 07 00 61 FE 00 1F 07 00 61 07 00 61 07 00 78 FF 00 97 00 0E 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 01 DC 01 01 01 00 00 FF 00 75 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 00 78 07 02 D8 01 01 01 07 02 38 07 00 61 01 07 00 04 00 00 41 07 00 04 FF 00 0E 00 0D 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 00 78 07 02 D8 01 00 01 07 01 1D FF 00 07 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 00 78 07 02 D8 01 07 02 42 01 07 02 38 07 00 61 01 07 00 04 00 01 07 02 42 FF 00 31 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 01 07 02 42 01 07 02 38 07 00 61 01 07 00 04 00 00 FF 00 40 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 00 61 07 02 DA 01 07 00 61 01 07 00 04 00 02 07 02 5D 07 00 61 FF 00 6D 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 07 00 89 07 00 61 01 07 00 04 00 00 FF 00 2F 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 07 02 DC 07 00 61 01 07 00 04 00 00 FF 00 A0 00 16 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 01 07 00 61 07 00 78 07 01 DC 01 01 01 00 00 FF 00 39 00 16 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 01 07 00 61 07 00 78 07 00 61 07 00 61 01 01 00 02 07 00 78 07 00 61 FF 00 11 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 00 07 00 61 00 07 00 04 00 00 18 FF 00 02 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 00 07 00 04 00 00 07 00 61 00 07 00 04 00 01 07 00 B7 64 07 01 1D FF 00 12 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 01 1D 07 00 04 00 00 07 00 61 00 07 00 04 00 00 FF 00 02 00 13 07 01 DC 07 00 04 01 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 07 00 78 07 02 42 01 07 02 5D 07 02 5D 07 02 5D 00 07 00 61 00 07 00 04 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  321    412    420    428    Any
        //  420    422    420    428    Any
        //  433    946    966    1003   Ljava/lang/Exception;
        //  433    946    1003   1025   Any
        //  966    1003   1003   1025   Any
        //  1003   1005   1003   1025   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:361)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:504)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visitVariable(StackMappingVisitor.java:474)
        //     at com.strobel.assembler.ir.Instruction.accept(Instruction.java:553)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:403)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final File getStorageFile(@NotNull final String[] name, @NotNull final String ext) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)ext, "ext");
        final String storagePath = getStoragePath();
        final File storageDir = new File(storagePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        final String filename = (String)ArraysKt.last((Object[])name);
        final SpreadBuilder spreadBuilder = new SpreadBuilder(2);
        spreadBuilder.addSpread((Object)ArraysKt.copyOfRange((Object[])name, 0, name.length - 1));
        spreadBuilder.add((Object)Intrinsics.stringPlus(filename, (Object)ext));
        final String path = getRelativePath((String[])spreadBuilder.toArray((Object[])new String[spreadBuilder.size()]));
        return new File(storagePath + (Object)File.separator + path);
    }
    
    @Nullable
    public static final String getStorage(@NotNull final String[] name, @NotNull final String ext) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc_w           "name"
        //     4: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     7: aload_1         /* ext */
        //     8: ldc_w           "ext"
        //    11: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    14: invokestatic    com/htmake/reader/utils/ExtKt.getStoragePath:()Ljava/lang/String;
        //    17: astore_2        /* storagePath */
        //    18: aload_0         /* name */
        //    19: invokestatic    kotlin/collections/ArraysKt.last:([Ljava/lang/Object;)Ljava/lang/Object;
        //    22: checkcast       Ljava/lang/String;
        //    25: astore_3        /* filename */
        //    26: new             Lkotlin/jvm/internal/SpreadBuilder;
        //    29: dup            
        //    30: iconst_2       
        //    31: invokespecial   kotlin/jvm/internal/SpreadBuilder.<init>:(I)V
        //    34: astore          5
        //    36: aload           5
        //    38: aload_0         /* name */
        //    39: astore          6
        //    41: iconst_0       
        //    42: istore          7
        //    44: aload_0         /* name */
        //    45: arraylength    
        //    46: iconst_1       
        //    47: isub           
        //    48: istore          8
        //    50: iconst_0       
        //    51: istore          9
        //    53: aload           6
        //    55: iload           7
        //    57: iload           8
        //    59: invokestatic    kotlin/collections/ArraysKt.copyOfRange:([Ljava/lang/Object;II)[Ljava/lang/Object;
        //    62: invokevirtual   kotlin/jvm/internal/SpreadBuilder.addSpread:(Ljava/lang/Object;)V
        //    65: aload           5
        //    67: aload_3         /* filename */
        //    68: aload_1         /* ext */
        //    69: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //    72: invokevirtual   kotlin/jvm/internal/SpreadBuilder.add:(Ljava/lang/Object;)V
        //    75: aload           5
        //    77: aload           5
        //    79: invokevirtual   kotlin/jvm/internal/SpreadBuilder.size:()I
        //    82: anewarray       Ljava/lang/String;
        //    85: invokevirtual   kotlin/jvm/internal/SpreadBuilder.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //    88: checkcast       [Ljava/lang/String;
        //    91: invokestatic    com/htmake/reader/utils/ExtKt.getRelativePath:([Ljava/lang/String;)Ljava/lang/String;
        //    94: astore          path
        //    96: aload_0         /* name */
        //    97: aload_0         /* name */
        //    98: arraylength    
        //    99: invokestatic    java/util/Arrays.copyOf:([Ljava/lang/Object;I)[Ljava/lang/Object;
        //   102: checkcast       [Ljava/lang/String;
        //   105: aload_1         /* ext */
        //   106: invokestatic    com/htmake/reader/utils/ExtKt.getStorageFile:([Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;
        //   109: astore          file
        //   111: getstatic       com/htmake/reader/utils/ExtKt.logger:Lmu/KLogger;
        //   114: ldc_w           "Read file from storage name: {} path: {}"
        //   117: aload_0         /* name */
        //   118: aload           file
        //   120: invokevirtual   java/io/File.getAbsoluteFile:()Ljava/io/File;
        //   123: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   128: aload           file
        //   130: invokevirtual   java/io/File.exists:()Z
        //   133: ifne            237
        //   136: aload           path
        //   138: invokestatic    com/htmake/reader/utils/ExtKt.readMongoFile:(Ljava/lang/String;)Ljava/lang/String;
        //   141: astore          6
        //   143: aload           6
        //   145: ifnonnull       152
        //   148: aconst_null    
        //   149: goto            236
        //   152: aload           6
        //   154: astore          7
        //   156: iconst_0       
        //   157: istore          8
        //   159: iconst_0       
        //   160: istore          9
        //   162: aload           7
        //   164: astore          content
        //   166: iconst_0       
        //   167: istore          $i$a$-also-ExtKt$getStorage$1
        //   169: aload           content
        //   171: checkcast       Ljava/lang/CharSequence;
        //   174: astore          12
        //   176: iconst_0       
        //   177: istore          13
        //   179: aload           12
        //   181: invokeinterface java/lang/CharSequence.length:()I
        //   186: ifle            193
        //   189: iconst_1       
        //   190: goto            194
        //   193: iconst_0       
        //   194: ifeq            233
        //   197: aload           file
        //   199: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   202: invokevirtual   java/io/File.exists:()Z
        //   205: ifne            217
        //   208: aload           file
        //   210: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   213: invokevirtual   java/io/File.mkdirs:()Z
        //   216: pop            
        //   217: aload           file
        //   219: invokevirtual   java/io/File.createNewFile:()Z
        //   222: pop            
        //   223: aload           file
        //   225: aload           content
        //   227: aconst_null    
        //   228: iconst_2       
        //   229: aconst_null    
        //   230: invokestatic    kotlin/io/FilesKt.writeText$default:(Ljava/io/File;Ljava/lang/String;Ljava/nio/charset/Charset;ILjava/lang/Object;)V
        //   233: nop            
        //   234: aload           7
        //   236: areturn        
        //   237: getstatic       com/htmake/reader/utils/ExtKt.lockMap:Lcom/htmake/reader/utils/ExtKt$lockMap$1;
        //   240: astore          7
        //   242: iconst_0       
        //   243: istore          8
        //   245: iconst_0       
        //   246: istore          9
        //   248: aload           7
        //   250: monitorenter   
        //   251: nop            
        //   252: iconst_0       
        //   253: istore          $i$a$-synchronized-ExtKt$getStorage$lock$1
        //   255: getstatic       com/htmake/reader/utils/ExtKt.lockMap:Lcom/htmake/reader/utils/ExtKt$lockMap$1;
        //   258: checkcast       Ljava/util/Map;
        //   261: astore          11
        //   263: aload           file
        //   265: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   268: astore          12
        //   270: aload           12
        //   272: ldc_w           "file.absolutePath"
        //   275: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   278: aload           12
        //   280: astore          key$iv
        //   282: iconst_0       
        //   283: istore          $i$f$getOrPut
        //   285: aload           $this$getOrPut$iv
        //   287: aload           key$iv
        //   289: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   294: astore          value$iv
        //   296: aload           value$iv
        //   298: ifnonnull       333
        //   301: iconst_0       
        //   302: istore          $i$a$-getOrPut-ExtKt$getStorage$lock$1$1
        //   304: new             Ljava/util/concurrent/locks/ReentrantReadWriteLock;
        //   307: dup            
        //   308: invokespecial   java/util/concurrent/locks/ReentrantReadWriteLock.<init>:()V
        //   311: checkcast       Ljava/util/concurrent/locks/ReadWriteLock;
        //   314: astore          answer$iv
        //   316: aload           $this$getOrPut$iv
        //   318: aload           key$iv
        //   320: aload           answer$iv
        //   322: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   327: pop            
        //   328: aload           answer$iv
        //   330: goto            335
        //   333: aload           value$iv
        //   335: nop            
        //   336: checkcast       Ljava/util/concurrent/locks/ReadWriteLock;
        //   339: nop            
        //   340: astore          9
        //   342: aload           7
        //   344: monitorexit    
        //   345: aload           9
        //   347: goto            358
        //   350: astore          9
        //   352: aload           7
        //   354: monitorexit    
        //   355: aload           9
        //   357: athrow         
        //   358: astore          lock
        //   360: ldc_w           ""
        //   363: astore          content
        //   365: iconst_0       
        //   366: istore          acquired
        //   368: nop            
        //   369: aload           lock
        //   371: invokeinterface java/util/concurrent/locks/ReadWriteLock.readLock:()Ljava/util/concurrent/locks/Lock;
        //   376: ldc2_w          10
        //   379: getstatic       java/util/concurrent/TimeUnit.SECONDS:Ljava/util/concurrent/TimeUnit;
        //   382: invokeinterface java/util/concurrent/locks/Lock.tryLock:(JLjava/util/concurrent/TimeUnit;)Z
        //   387: istore          acquired
        //   389: iload           acquired
        //   391: ifne            413
        //   394: new             Ljava/lang/Exception;
        //   397: dup            
        //   398: ldc_w           "\u8bfb\u53d6\u6587\u4ef6\u8d85\u65f6: "
        //   401: aload           file
        //   403: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   406: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   409: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   412: athrow         
        //   413: new             Ljava/io/FileReader;
        //   416: dup            
        //   417: aload           file
        //   419: invokespecial   java/io/FileReader.<init>:(Ljava/io/File;)V
        //   422: astore          reader
        //   424: nop            
        //   425: aload           reader
        //   427: checkcast       Ljava/io/Reader;
        //   430: invokestatic    kotlin/io/TextStreamsKt.readText:(Ljava/io/Reader;)Ljava/lang/String;
        //   433: astore          content
        //   435: aload           content
        //   437: checkcast       Ljava/lang/CharSequence;
        //   440: astore          10
        //   442: iconst_0       
        //   443: istore          11
        //   445: aload           10
        //   447: invokeinterface java/lang/CharSequence.length:()I
        //   452: ifne            459
        //   455: iconst_1       
        //   456: goto            460
        //   459: iconst_0       
        //   460: ifeq            595
        //   463: aload           path
        //   465: invokestatic    com/htmake/reader/utils/ExtKt.readMongoFile:(Ljava/lang/String;)Ljava/lang/String;
        //   468: astore          10
        //   470: aload           10
        //   472: ifnonnull       480
        //   475: aload           content
        //   477: goto            568
        //   480: aload           10
        //   482: astore          12
        //   484: iconst_0       
        //   485: istore          13
        //   487: iconst_0       
        //   488: istore          14
        //   490: aload           12
        //   492: astore          content
        //   494: iconst_0       
        //   495: istore          $i$a$-also-ExtKt$getStorage$2
        //   497: aload           content
        //   499: checkcast       Ljava/lang/CharSequence;
        //   502: astore          17
        //   504: iconst_0       
        //   505: istore          18
        //   507: aload           17
        //   509: invokeinterface java/lang/CharSequence.length:()I
        //   514: ifle            521
        //   517: iconst_1       
        //   518: goto            522
        //   521: iconst_0       
        //   522: ifeq            561
        //   525: aload           file
        //   527: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   530: invokevirtual   java/io/File.exists:()Z
        //   533: ifne            545
        //   536: aload           file
        //   538: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //   541: invokevirtual   java/io/File.mkdirs:()Z
        //   544: pop            
        //   545: aload           file
        //   547: invokevirtual   java/io/File.createNewFile:()Z
        //   550: pop            
        //   551: aload           file
        //   553: aload           content
        //   555: aconst_null    
        //   556: iconst_2       
        //   557: aconst_null    
        //   558: invokestatic    kotlin/io/FilesKt.writeText$default:(Ljava/io/File;Ljava/lang/String;Ljava/nio/charset/Charset;ILjava/lang/Object;)V
        //   561: nop            
        //   562: aload           12
        //   564: astore          11
        //   566: aload           11
        //   568: astore          10
        //   570: aload           reader
        //   572: invokevirtual   java/io/FileReader.close:()V
        //   575: iload           acquired
        //   577: ifeq            592
        //   580: aload           lock
        //   582: invokeinterface java/util/concurrent/locks/ReadWriteLock.readLock:()Ljava/util/concurrent/locks/Lock;
        //   587: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   592: aload           10
        //   594: areturn        
        //   595: ldc_w           "users"
        //   598: aload_3         /* filename */
        //   599: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   602: ifeq            844
        //   605: new             Lkotlin/jvm/internal/SpreadBuilder;
        //   608: dup            
        //   609: iconst_2       
        //   610: invokespecial   kotlin/jvm/internal/SpreadBuilder.<init>:(I)V
        //   613: astore          11
        //   615: aload           11
        //   617: aload_0         /* name */
        //   618: astore          12
        //   620: iconst_0       
        //   621: istore          13
        //   623: aload_0         /* name */
        //   624: arraylength    
        //   625: iconst_1       
        //   626: isub           
        //   627: istore          14
        //   629: iconst_0       
        //   630: istore          15
        //   632: aload           12
        //   634: iload           13
        //   636: iload           14
        //   638: invokestatic    kotlin/collections/ArraysKt.copyOfRange:([Ljava/lang/Object;II)[Ljava/lang/Object;
        //   641: invokevirtual   kotlin/jvm/internal/SpreadBuilder.addSpread:(Ljava/lang/Object;)V
        //   644: aload           11
        //   646: new             Ljava/lang/StringBuilder;
        //   649: dup            
        //   650: invokespecial   java/lang/StringBuilder.<init>:()V
        //   653: bipush          46
        //   655: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   658: aload_3         /* filename */
        //   659: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   662: ldc_w           ".key"
        //   665: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   668: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   671: invokevirtual   kotlin/jvm/internal/SpreadBuilder.add:(Ljava/lang/Object;)V
        //   674: aload           11
        //   676: aload           11
        //   678: invokevirtual   kotlin/jvm/internal/SpreadBuilder.size:()I
        //   681: anewarray       Ljava/lang/String;
        //   684: invokevirtual   kotlin/jvm/internal/SpreadBuilder.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   687: checkcast       [Ljava/lang/String;
        //   690: invokestatic    com/htmake/reader/utils/ExtKt.getRelativePath:([Ljava/lang/String;)Ljava/lang/String;
        //   693: astore          verifyKeyPath
        //   695: new             Ljava/io/File;
        //   698: dup            
        //   699: new             Ljava/lang/StringBuilder;
        //   702: dup            
        //   703: invokespecial   java/lang/StringBuilder.<init>:()V
        //   706: aload_2         /* storagePath */
        //   707: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   710: getstatic       java/io/File.separator:Ljava/lang/String;
        //   713: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   716: aload           verifyKeyPath
        //   718: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   721: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   724: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   727: astore          verifyKeyFile
        //   729: aload           verifyKeyFile
        //   731: invokevirtual   java/io/File.exists:()Z
        //   734: ifeq            844
        //   737: aload           verifyKeyFile
        //   739: aconst_null    
        //   740: iconst_1       
        //   741: aconst_null    
        //   742: invokestatic    kotlin/io/FilesKt.readText$default:(Ljava/io/File;Ljava/nio/charset/Charset;ILjava/lang/Object;)Ljava/lang/String;
        //   745: astore          verifyKeyContent
        //   747: aload           content
        //   749: ldc_w           "username"
        //   752: invokestatic    com/htmake/reader/utils/ExtKt.countOccurrences:(Ljava/lang/String;Ljava/lang/String;)I
        //   755: istore          userCount
        //   757: getstatic       io/legado/app/utils/MD5Utils.INSTANCE:Lio/legado/app/utils/MD5Utils;
        //   760: ldc_w           "userCount="
        //   763: iload           userCount
        //   765: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   768: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   771: invokevirtual   io/legado/app/utils/MD5Utils.md5Encode:(Ljava/lang/String;)Ljava/lang/String;
        //   774: invokevirtual   java/lang/String.toString:()Ljava/lang/String;
        //   777: astore          md5Encode
        //   779: aload           verifyKeyContent
        //   781: aload           md5Encode
        //   783: astore          15
        //   785: aload           md5Encode
        //   787: invokevirtual   java/lang/String.length:()I
        //   790: bipush          16
        //   792: isub           
        //   793: istore          16
        //   795: iconst_0       
        //   796: istore          17
        //   798: aload           15
        //   800: dup            
        //   801: ifnonnull       815
        //   804: new             Ljava/lang/NullPointerException;
        //   807: dup            
        //   808: ldc_w           "null cannot be cast to non-null type java.lang.String"
        //   811: invokespecial   java/lang/NullPointerException.<init>:(Ljava/lang/String;)V
        //   814: athrow         
        //   815: iload           16
        //   817: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //   820: dup            
        //   821: ldc_w           "(this as java.lang.String).substring(startIndex)"
        //   824: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   827: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   830: ifne            844
        //   833: new             Ljava/lang/Exception;
        //   836: dup            
        //   837: ldc_w           "\u7528\u6237\u6570\u636e\u88ab\u7be1\u6539\uff0c\u8bf7\u8054\u7cfb\u5f00\u53d1\u8005\u4fee\u590d"
        //   840: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   843: athrow         
        //   844: aload           reader
        //   846: invokevirtual   java/io/FileReader.close:()V
        //   849: goto            862
        //   852: astore          10
        //   854: aload           reader
        //   856: invokevirtual   java/io/FileReader.close:()V
        //   859: aload           10
        //   861: athrow         
        //   862: iload           acquired
        //   864: ifeq            879
        //   867: aload           lock
        //   869: invokeinterface java/util/concurrent/locks/ReadWriteLock.readLock:()Ljava/util/concurrent/locks/Lock;
        //   874: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   879: goto            941
        //   882: astore          e
        //   884: getstatic       com/htmake/reader/utils/ExtKt.logger:Lmu/KLogger;
        //   887: ldc_w           "\u8bfb\u53d6\u6587\u4ef6\u5931\u8d25: "
        //   890: aload           e
        //   892: checkcast       Ljava/lang/Throwable;
        //   895: invokeinterface mu/KLogger.error:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   900: new             Ljava/lang/Exception;
        //   903: dup            
        //   904: ldc_w           "\u8bfb\u53d6\u6587\u4ef6\u5931\u8d25: "
        //   907: aload           file
        //   909: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   912: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   915: invokespecial   java/lang/Exception.<init>:(Ljava/lang/String;)V
        //   918: athrow         
        //   919: astore          null
        //   921: iload           acquired
        //   923: ifeq            938
        //   926: aload           lock
        //   928: invokeinterface java/util/concurrent/locks/ReadWriteLock.readLock:()Ljava/util/concurrent/locks/Lock;
        //   933: invokeinterface java/util/concurrent/locks/Lock.unlock:()V
        //   938: aload           9
        //   940: athrow         
        //   941: aload           content
        //   943: areturn        
        //    MethodParameters:
        //  Name  Flags  
        //  ----  -----
        //  name  
        //  ext   
        //    StackMapTable: 00 1F FF 00 98 00 0A 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 01 01 01 00 00 FF 00 28 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 07 00 61 01 01 07 00 61 01 07 01 81 01 00 00 40 01 16 0F FF 00 02 00 0A 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 00 61 00 01 01 00 01 07 00 61 FF 00 00 00 0A 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 01 DC 01 01 01 00 00 FF 00 5F 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 01 DC 07 02 D8 01 01 01 07 02 38 07 00 61 01 07 00 04 00 00 41 07 00 04 FF 00 0E 00 09 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 01 DC 07 02 D8 01 00 01 07 01 1D FF 00 07 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 01 DC 07 02 D8 01 07 02 42 01 07 02 38 07 00 61 01 07 00 04 00 01 07 02 42 FF 00 36 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 42 01 07 02 38 07 00 61 01 07 00 04 00 00 FF 00 2D 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 01 81 01 07 00 61 01 07 00 04 00 00 40 01 FF 00 13 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 61 01 07 00 61 01 07 00 04 00 00 FF 00 28 00 13 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 61 01 07 00 61 01 01 07 00 61 01 07 01 81 01 00 00 40 01 16 0F FF 00 06 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 61 00 07 00 61 01 00 01 07 00 61 17 FF 00 02 00 0F 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 01 81 01 07 00 61 01 07 00 04 00 00 FF 00 DB 00 12 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 61 07 00 78 07 00 61 01 07 00 61 07 00 61 01 01 00 02 07 00 61 07 00 61 FF 00 1C 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 04 00 07 00 04 01 00 00 FF 00 07 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 00 00 07 00 04 01 00 01 07 01 1D FF 00 09 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 04 00 07 00 04 01 00 00 10 FF 00 02 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 00 04 00 00 07 00 04 01 00 01 07 00 B7 64 07 01 1D FF 00 12 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 01 1D 00 00 07 00 04 01 00 00 FF 00 02 00 0E 07 01 DC 07 00 61 07 00 61 07 00 61 07 00 61 07 00 78 07 02 42 07 00 61 01 07 02 F7 07 00 04 00 07 00 04 01 00 00
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  251    342    350    358    Any
        //  350    352    350    358    Any
        //  424    570    852    862    Any
        //  595    844    852    862    Any
        //  852    854    852    862    Any
        //  368    575    882    919    Ljava/lang/Exception;
        //  595    862    882    919    Ljava/lang/Exception;
        //  368    575    919    941    Any
        //  595    862    919    941    Any
        //  882    919    919    941    Any
        //  919    921    919    941    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index -1 out of bounds for length 0
        //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
        //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
        //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:266)
        //     at java.base/java.util.Objects.checkIndex(Objects.java:361)
        //     at java.base/java.util.ArrayList.remove(ArrayList.java:504)
        //     at com.strobel.assembler.ir.StackMappingVisitor.pop(StackMappingVisitor.java:267)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visitVariable(StackMappingVisitor.java:474)
        //     at com.strobel.assembler.ir.Instruction.accept(Instruction.java:553)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:403)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2086)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Nullable
    public static final MongoCollection<MongoFile> getMongoFileStorage() {
        final AppConfig appConfig = (AppConfig)SpringContextUtils.getBean("appConfig", (Class)AppConfig.class);
        return (MongoCollection<MongoFile>)MongoManager.INSTANCE.fileStorage(appConfig.getMongoDbName(), "storage");
    }
    
    @Nullable
    public static final String readMongoFile(@NotNull final String path) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        if (MongoManager.INSTANCE.isInit()) {
            ExtKt.logger.info("Get mongoFile {}", (Object)path);
            final MongoCollection<MongoFile> mongoFileStorage = getMongoFileStorage();
            MongoFile mongoFile;
            if (mongoFileStorage == null) {
                mongoFile = null;
            }
            else {
                final FindIterable find = mongoFileStorage.find(Filters.eq("path", (Object)path));
                mongoFile = ((find == null) ? null : ((MongoFile)find.first()));
            }
            final MongoFile doc = mongoFile;
            if (doc != null) {
                return doc.getContent();
            }
        }
        return null;
    }
    
    public static final boolean saveMongoFile(@NotNull final String path, @NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        if (MongoManager.INSTANCE.isInit()) {
            ExtKt.logger.info("Save mongoFile {}", (Object)path);
            final MongoCollection<MongoFile> mongoFileStorage = getMongoFileStorage();
            MongoFile mongoFile;
            if (mongoFileStorage == null) {
                mongoFile = null;
            }
            else {
                final FindIterable find = mongoFileStorage.find(Filters.eq("path", (Object)path));
                mongoFile = ((find == null) ? null : ((MongoFile)find.first()));
            }
            MongoFile doc = mongoFile;
            if (doc != null) {
                doc.setContent(content);
                doc.setUpdated_at(System.currentTimeMillis());
                final MongoCollection<MongoFile> mongoFileStorage2 = getMongoFileStorage();
                final UpdateResult result = (mongoFileStorage2 == null) ? null : mongoFileStorage2.replaceOne(Filters.eq("path", (Object)path), (Object)doc, new ReplaceOptions().upsert(true));
                return result != null && result.getModifiedCount() > 0L;
            }
            doc = new MongoFile(path, content, 0L, 0L, 12, (DefaultConstructorMarker)null);
            try {
                final MongoCollection<MongoFile> mongoFileStorage3 = getMongoFileStorage();
                if (mongoFileStorage3 != null) {
                    mongoFileStorage3.insertOne((Object)doc);
                }
                return true;
            }
            catch (final Exception e) {
                ExtKt.logger.info("Save mongoFile {} failed", (Object)path);
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public static final int countOccurrences(@NotNull final String str, @NotNull final String subStr) {
        Intrinsics.checkNotNullParameter((Object)str, "str");
        Intrinsics.checkNotNullParameter((Object)subStr, "subStr");
        int count = 0;
        int index;
        for (int startIndex = 0; startIndex < str.length(); startIndex = index + subStr.length()) {
            index = StringsKt.indexOf$default((CharSequence)str, subStr, startIndex, false, 4, (Object)null);
            if (index == -1) {
                break;
            }
            ++count;
        }
        return count;
    }
    
    @Nullable
    public static final JsonArray asJsonArray(@Nullable final Object value) {
        if (value instanceof JsonArray) {
            return (JsonArray)value;
        }
        if (value instanceof String) {
            try {
                return new JsonArray((String)value);
            }
            catch (final Exception e) {
                ExtKt.logger.error("\u89e3\u6790\u5185\u5bb9\u51fa\u9519: {}  \u5185\u5bb9: \n{}", (Object)e, value);
                throw e;
            }
        }
        return null;
    }
    
    @Nullable
    public static final JsonArray parseJsonStringList(@NotNull final File file, @Nullable final Set<String> fields, @Nullable final Set<String> exclude, final int startIndex, final int endIndex, @Nullable final Set<String> checkNotEmpty, @Nullable final Function1<? super ObjectNode, Boolean> filter) {
        Intrinsics.checkNotNullParameter((Object)file, "file");
        if (!file.exists()) {
            return null;
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final JsonFactory factory = objectMapper.getFactory();
            final JsonArray resultList = new JsonArray();
            int currentIndex = 0;
            currentIndex = -1;
            final Closeable closeable = (Closeable)factory.createParser(file);
            Throwable t = null;
            try {
                final JsonParser parser = (JsonParser)closeable;
                final int n = 0;
                if (parser.nextToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() == JsonToken.START_OBJECT) {
                            final Collection collection = fields;
                            if (collection == null || collection.isEmpty()) {
                                if (filter == null) {
                                    ++currentIndex;
                                    if (currentIndex < startIndex) {
                                        parser.skipChildren();
                                    }
                                    else {
                                        if (currentIndex > endIndex) {
                                            break;
                                        }
                                        final TreeNode valueAsTree = parser.readValueAsTree();
                                        Intrinsics.checkNotNullExpressionValue((Object)valueAsTree, "parser.readValueAsTree()");
                                        final JsonNode jsonNode = (JsonNode)valueAsTree;
                                        final ObjectNode objectNode = (ObjectNode)jsonNode;
                                        final Collection collection2 = exclude;
                                        if (collection2 != null && !collection2.isEmpty()) {
                                            final Iterable $this$forEach$iv = exclude;
                                            final int $i$f$forEach = 0;
                                            for (final Object element$iv : $this$forEach$iv) {
                                                final String it = (String)element$iv;
                                                final int n2 = 0;
                                                objectNode.remove(it);
                                            }
                                        }
                                        final String string = objectNode.toString();
                                        Intrinsics.checkNotNullExpressionValue((Object)string, "objectNode.toString()");
                                        final String jsonString = string;
                                        resultList.add(jsonString);
                                    }
                                }
                                else {
                                    final TreeNode valueAsTree2 = parser.readValueAsTree();
                                    Intrinsics.checkNotNullExpressionValue((Object)valueAsTree2, "parser.readValueAsTree()");
                                    final JsonNode jsonNode = (JsonNode)valueAsTree2;
                                    final ObjectNode objectNode = (ObjectNode)jsonNode;
                                    if (filter.invoke((Object)objectNode)) {
                                        ++currentIndex;
                                    }
                                    if (currentIndex < startIndex) {
                                        continue;
                                    }
                                    if (currentIndex > endIndex) {
                                        break;
                                    }
                                    final String string2 = objectNode.toString();
                                    Intrinsics.checkNotNullExpressionValue((Object)string2, "objectNode.toString()");
                                    final String jsonString = string2;
                                    resultList.add(jsonString);
                                }
                            }
                            else {
                                ++currentIndex;
                                if (currentIndex < startIndex) {
                                    parser.skipChildren();
                                }
                                else {
                                    if (currentIndex > endIndex) {
                                        break;
                                    }
                                    final JsonObject item = new JsonObject();
                                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                                        final String fieldName = parser.getCurrentName();
                                        parser.nextToken();
                                        if (fields.contains(fieldName)) {
                                            item.put(fieldName, parser.getValueAsString());
                                        }
                                        else if (checkNotEmpty != null && checkNotEmpty.contains(fieldName)) {
                                            final JsonObject jsonObject = item;
                                            final String s = fieldName;
                                            final CharSequence charSequence = parser.getValueAsString();
                                            jsonObject.put(s, Boolean.valueOf(charSequence != null && charSequence.length() != 0));
                                        }
                                        else {
                                            parser.skipChildren();
                                        }
                                    }
                                    resultList.add(item.toString());
                                }
                            }
                        }
                    }
                }
                parser.close();
                final Unit instance = Unit.INSTANCE;
            }
            catch (final Throwable t2) {
                t = t2;
                throw t2;
            }
            finally {
                CloseableKt.closeFinally(closeable, t);
            }
            return resultList;
        }
        catch (final Exception e) {
            ExtKt.logger.error("\u89e3\u6790\u6587\u4ef6\u5185\u5bb9\u51fa\u9519: {}  \u6587\u4ef6: \n{}", (Object)e, (Object)file);
            throw e;
        }
    }
    
    @Nullable
    public static final JsonObject asJsonObject(@Nullable final Object value) {
        if (value instanceof JsonObject) {
            return (JsonObject)value;
        }
        if (value instanceof String) {
            try {
                return new JsonObject((String)value);
            }
            catch (final Exception e) {
                ExtKt.logger.error("\u89e3\u6790\u5185\u5bb9\u51fa\u9519: {}  \u5185\u5bb9: \n{}", (Object)e, value);
                throw e;
            }
        }
        return null;
    }
    
    @NotNull
    public static final <T> Map<String, Object> serializeToMap(final T $this$serializeToMap) {
        final Object $this$convert$iv = $this$serializeToMap;
        final int $i$f$convert = 0;
        final String json$iv = (String)(($this$convert$iv instanceof String) ? $this$convert$iv : getGson().toJson($this$convert$iv));
        return (Map)getGson().fromJson(json$iv, new TypeToken<Map<String, ?>>() {}.getType());
    }
    
    @NotNull
    public static final <T> Map<String, Object> toMap(final T $this$toMap) {
        final Object $this$convert$iv = $this$toMap;
        final int $i$f$convert = 0;
        final String json$iv = (String)(($this$convert$iv instanceof String) ? $this$convert$iv : getGson().toJson($this$convert$iv));
        return (Map)getGson().fromJson(json$iv, new TypeToken<Map<String, ?>>() {}.getType());
    }
    
    public static final <R> R readInstanceProperty(@NotNull final Object instance, @NotNull final String propertyName) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc_w           "instance"
        //     4: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     7: aload_1         /* propertyName */
        //     8: ldc_w           "propertyName"
        //    11: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    14: aload_0         /* instance */
        //    15: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    18: invokestatic    kotlin/jvm/internal/Reflection.getOrCreateKotlinClass:(Ljava/lang/Class;)Lkotlin/reflect/KClass;
        //    21: invokestatic    kotlin/reflect/full/KClasses.getMemberProperties:(Lkotlin/reflect/KClass;)Ljava/util/Collection;
        //    24: checkcast       Ljava/lang/Iterable;
        //    27: astore_3       
        //    28: nop            
        //    29: iconst_0       
        //    30: istore          $i$f$first
        //    32: aload_3         /* $this$first$iv */
        //    33: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    38: astore          5
        //    40: aload           5
        //    42: invokeinterface java/util/Iterator.hasNext:()Z
        //    47: ifeq            88
        //    50: aload           5
        //    52: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    57: astore          element$iv
        //    59: aload           element$iv
        //    61: checkcast       Lkotlin/reflect/KProperty1;
        //    64: astore          it
        //    66: iconst_0       
        //    67: istore          $i$a$-first-ExtKt$readInstanceProperty$property$1
        //    69: aload           it
        //    71: invokeinterface kotlin/reflect/KProperty1.getName:()Ljava/lang/String;
        //    76: aload_1         /* propertyName */
        //    77: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    80: ifeq            40
        //    83: aload           element$iv
        //    85: goto            102
        //    88: new             Ljava/util/NoSuchElementException;
        //    91: dup            
        //    92: ldc_w           "Collection contains no element matching the predicate."
        //    95: invokespecial   java/util/NoSuchElementException.<init>:(Ljava/lang/String;)V
        //    98: checkcast       Ljava/lang/Throwable;
        //   101: athrow         
        //   102: checkcast       Lkotlin/reflect/KProperty1;
        //   105: astore_2        /* property */
        //   106: aload_2         /* property */
        //   107: aload_0         /* instance */
        //   108: invokeinterface kotlin/reflect/KProperty1.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   113: areturn        
        //    Signature:
        //  <R:Ljava/lang/Object;>(Ljava/lang/Object;Ljava/lang/String;)TR;
        //    MethodParameters:
        //  Name          Flags  
        //  ------------  -----
        //  instance      
        //  propertyName  
        //    StackMapTable: 00 03 FF 00 28 00 06 07 00 04 07 00 61 00 07 03 BD 01 07 01 44 00 00 2F FF 00 0D 00 09 07 00 04 07 00 61 00 07 03 BD 01 07 01 44 07 00 04 07 04 6A 01 00 01 07 00 04
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static final void setInstanceProperty(@NotNull final Object instance, @NotNull final String propertyName, @NotNull final Object propertyValue) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc_w           "instance"
        //     4: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //     7: aload_1         /* propertyName */
        //     8: ldc_w           "propertyName"
        //    11: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    14: aload_2         /* propertyValue */
        //    15: ldc_w           "propertyValue"
        //    18: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullParameter:(Ljava/lang/Object;Ljava/lang/String;)V
        //    21: aload_0         /* instance */
        //    22: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //    25: invokestatic    kotlin/jvm/internal/Reflection.getOrCreateKotlinClass:(Ljava/lang/Class;)Lkotlin/reflect/KClass;
        //    28: invokestatic    kotlin/reflect/full/KClasses.getMemberProperties:(Lkotlin/reflect/KClass;)Ljava/util/Collection;
        //    31: checkcast       Ljava/lang/Iterable;
        //    34: astore          4
        //    36: nop            
        //    37: iconst_0       
        //    38: istore          $i$f$first
        //    40: aload           $this$first$iv
        //    42: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    47: astore          6
        //    49: aload           6
        //    51: invokeinterface java/util/Iterator.hasNext:()Z
        //    56: ifeq            97
        //    59: aload           6
        //    61: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    66: astore          element$iv
        //    68: aload           element$iv
        //    70: checkcast       Lkotlin/reflect/KProperty1;
        //    73: astore          it
        //    75: iconst_0       
        //    76: istore          $i$a$-first-ExtKt$setInstanceProperty$property$1
        //    78: aload           it
        //    80: invokeinterface kotlin/reflect/KProperty1.getName:()Ljava/lang/String;
        //    85: aload_1         /* propertyName */
        //    86: invokestatic    kotlin/jvm/internal/Intrinsics.areEqual:(Ljava/lang/Object;Ljava/lang/Object;)Z
        //    89: ifeq            49
        //    92: aload           element$iv
        //    94: goto            111
        //    97: new             Ljava/util/NoSuchElementException;
        //   100: dup            
        //   101: ldc_w           "Collection contains no element matching the predicate."
        //   104: invokespecial   java/util/NoSuchElementException.<init>:(Ljava/lang/String;)V
        //   107: checkcast       Ljava/lang/Throwable;
        //   110: athrow         
        //   111: checkcast       Lkotlin/reflect/KProperty1;
        //   114: astore_3        /* property */
        //   115: aload_3         /* property */
        //   116: instanceof      Lkotlin/reflect/KMutableProperty;
        //   119: ifeq            155
        //   122: aload_3         /* property */
        //   123: checkcast       Lkotlin/reflect/KMutableProperty;
        //   126: invokeinterface kotlin/reflect/KMutableProperty.getSetter:()Lkotlin/reflect/KMutableProperty$Setter;
        //   131: iconst_2       
        //   132: anewarray       Ljava/lang/Object;
        //   135: astore          4
        //   137: aload           4
        //   139: iconst_0       
        //   140: aload_0         /* instance */
        //   141: aastore        
        //   142: aload           4
        //   144: iconst_1       
        //   145: aload_2         /* propertyValue */
        //   146: aastore        
        //   147: aload           4
        //   149: invokeinterface kotlin/reflect/KMutableProperty$Setter.call:([Ljava/lang/Object;)Ljava/lang/Object;
        //   154: pop            
        //   155: return         
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  instance       
        //  propertyName   
        //  propertyValue  
        //    StackMapTable: 00 04 FF 00 31 00 07 07 00 04 07 00 61 07 00 04 00 07 03 BD 01 07 01 44 00 00 2F FF 00 0D 00 0A 07 00 04 07 00 61 07 00 04 00 07 03 BD 01 07 01 44 07 00 04 07 04 6A 01 00 01 07 00 04 FF 00 2B 00 0A 07 00 04 07 00 61 07 00 04 07 04 6A 07 00 04 01 07 01 44 07 00 04 07 04 6A 01 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final Book fillData(@NotNull final Book $this$fillData, @NotNull final Book newBook, @NotNull final List<String> keys) {
        Intrinsics.checkNotNullParameter((Object)$this$fillData, "<this>");
        Intrinsics.checkNotNullParameter((Object)newBook, "newBook");
        Intrinsics.checkNotNullParameter((Object)keys, "keys");
        final List it = keys;
        final int n = 0;
        for (final String key : it) {
            final String current = readInstanceProperty($this$fillData, key);
            final CharSequence charSequence = current;
            if (charSequence == null || charSequence.length() == 0) {
                final String cacheValue = readInstanceProperty(newBook, key);
                final CharSequence charSequence2 = cacheValue;
                if (charSequence2 == null || charSequence2.length() == 0) {
                    continue;
                }
                setInstanceProperty($this$fillData, key, cacheValue);
            }
        }
        return $this$fillData;
    }
    
    @NotNull
    public static final String getRandomString(final int length) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: astore_1        /* allowedChars */
        //     4: new             Lkotlin/ranges/IntRange;
        //     7: dup            
        //     8: iconst_1       
        //     9: iload_0         /* length */
        //    10: invokespecial   kotlin/ranges/IntRange.<init>:(II)V
        //    13: checkcast       Ljava/lang/Iterable;
        //    16: astore_2       
        //    17: nop            
        //    18: iconst_0       
        //    19: istore_3        /* $i$f$map */
        //    20: aload_2         /* $this$map$iv */
        //    21: astore          4
        //    23: new             Ljava/util/ArrayList;
        //    26: dup            
        //    27: aload_2         /* $this$map$iv */
        //    28: bipush          10
        //    30: invokestatic    kotlin/collections/CollectionsKt.collectionSizeOrDefault:(Ljava/lang/Iterable;I)I
        //    33: invokespecial   java/util/ArrayList.<init>:(I)V
        //    36: checkcast       Ljava/util/Collection;
        //    39: astore          destination$iv$iv
        //    41: iconst_0       
        //    42: istore          $i$f$mapTo
        //    44: aload           $this$mapTo$iv$iv
        //    46: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //    51: astore          7
        //    53: aload           7
        //    55: invokeinterface java/util/Iterator.hasNext:()Z
        //    60: ifeq            123
        //    63: aload           7
        //    65: checkcast       Lkotlin/collections/IntIterator;
        //    68: invokevirtual   kotlin/collections/IntIterator.nextInt:()I
        //    71: istore          item$iv$iv
        //    73: aload           destination$iv$iv
        //    75: iload           item$iv$iv
        //    77: istore          9
        //    79: astore          13
        //    81: iconst_0       
        //    82: istore          $i$a$-map-ExtKt$getRandomString$1
        //    84: aload_1         /* allowedChars */
        //    85: checkcast       Ljava/lang/CharSequence;
        //    88: astore          11
        //    90: iconst_0       
        //    91: istore          12
        //    93: aload           11
        //    95: getstatic       kotlin/random/Random.Default:Lkotlin/random/Random$Default;
        //    98: checkcast       Lkotlin/random/Random;
        //   101: invokestatic    kotlin/text/StringsKt.random:(Ljava/lang/CharSequence;Lkotlin/random/Random;)C
        //   104: nop            
        //   105: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
        //   108: astore          14
        //   110: aload           13
        //   112: aload           14
        //   114: invokeinterface java/util/Collection.add:(Ljava/lang/Object;)Z
        //   119: pop            
        //   120: goto            53
        //   123: aload           destination$iv$iv
        //   125: checkcast       Ljava/util/List;
        //   128: nop            
        //   129: checkcast       Ljava/lang/Iterable;
        //   132: ldc_w           ""
        //   135: checkcast       Ljava/lang/CharSequence;
        //   138: aconst_null    
        //   139: aconst_null    
        //   140: iconst_0       
        //   141: aconst_null    
        //   142: aconst_null    
        //   143: bipush          62
        //   145: aconst_null    
        //   146: invokestatic    kotlin/collections/CollectionsKt.joinToString$default:(Ljava/lang/Iterable;Ljava/lang/CharSequence;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ILjava/lang/CharSequence;Lkotlin/jvm/functions/Function1;ILjava/lang/Object;)Ljava/lang/String;
        //   149: areturn        
        //    MethodParameters:
        //  Name    Flags  
        //  ------  -----
        //  length  
        //    StackMapTable: 00 02 FF 00 35 00 08 01 07 00 61 07 03 BD 01 07 03 BD 07 00 A9 01 07 01 44 00 00 FB 00 45
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @NotNull
    public static final String genEncryptedPassword(@NotNull final String password, @NotNull final String salt) {
        Intrinsics.checkNotNullParameter((Object)password, "password");
        Intrinsics.checkNotNullParameter((Object)salt, "salt");
        return MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(password, (Object)salt)), (Object)salt)).toString();
    }
    
    @NotNull
    public static final String jsonEncode(@NotNull final Object value, final boolean pretty) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (pretty) {
            final String json = ExtKt.prettyGson.toJson(value);
            Intrinsics.checkNotNullExpressionValue((Object)json, "prettyGson.toJson(value)");
            return json;
        }
        final String json2 = ExtKt.gson.toJson(value);
        Intrinsics.checkNotNullExpressionValue((Object)json2, "gson.toJson(value)");
        return json2;
    }
    
    @NotNull
    public static final List<File> deepListFiles(@NotNull final File $this$deepListFiles, @Nullable final String[] allowExtensions) {
        Intrinsics.checkNotNullParameter((Object)$this$deepListFiles, "<this>");
        final ArrayList fileList = new ArrayList();
        final File[] listFiles = $this$deepListFiles.listFiles();
        Intrinsics.checkNotNullExpressionValue((Object)listFiles, "this.listFiles()");
        final Object[] $this$forEach$iv = listFiles;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final File it = (File)element$iv;
            final int n = 0;
            if (it.isDirectory()) {
                final ArrayList list = fileList;
                Intrinsics.checkNotNullExpressionValue((Object)it, "it");
                list.addAll(deepListFiles(it, allowExtensions));
            }
            else {
                final FileUtils instance = FileUtils.INSTANCE;
                final String name = it.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name, "it.name");
                final String extension = instance.getExtension(name);
                boolean b;
                if (allowExtensions == null) {
                    b = false;
                }
                else {
                    final String contentDeepToString = ArraysKt.contentDeepToString((Object[])allowExtensions);
                    b = (contentDeepToString != null && StringsKt.contains$default((CharSequence)contentDeepToString, (CharSequence)extension, false, 2, (Object)null));
                }
                if (b || allowExtensions == null) {
                    fileList.add(it);
                }
            }
        }
        return fileList;
    }
    
    @NotNull
    public static final String getTraceId() {
        return UUID.randomUUID().toString().subSequence(0, 8).toString();
    }
    
    public static final boolean get_licenseValid() {
        return ExtKt._licenseValid;
    }
    
    public static final void set_licenseValid(final boolean <set-?>) {
        ExtKt._licenseValid = <set-?>;
    }
    
    public static final void setLicenseValid(final boolean isValid) {
        ExtKt._licenseValid = isValid;
    }
    
    @NotNull
    public static final License getInstalledLicense(final boolean ignoreInvalid) {
        final String licenseKeyString = getStorage(new String[] { "data", "license" }, ".key");
        final CharSequence charSequence = licenseKeyString;
        if (charSequence == null || charSequence.length() == 0) {
            return new License((String)null, 0, 0L, false, 0L, 0, (String)null, (String)null, (String)null, false, (Long)null, 2047, (DefaultConstructorMarker)null);
        }
        if (!ignoreInvalid && !ExtKt._licenseValid) {
            return new License((String)null, 0, 0L, false, 0L, 0, (String)null, (String)null, (String)null, false, (Long)null, 2047, (DefaultConstructorMarker)null);
        }
        final License license = decryptToLicense(licenseKeyString);
        ExtKt.logger.info("license: {}", (Object)license);
        if (license == null || !license.getVerified()) {
            return new License((String)null, 0, 0L, false, 0L, 0, (String)null, (String)null, (String)null, false, (Long)null, 2047, (DefaultConstructorMarker)null);
        }
        return license;
    }
    
    @Nullable
    public static final License decryptToLicense(@NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)content, "content");
        if (content.length() == 0) {
            return null;
        }
        final String decryptData = decryptData(content);
        License license;
        if (decryptData == null) {
            license = null;
        }
        else {
            final String it = decryptData;
            final int n = 0;
            final Map $this$toDataClass$iv = toMap(it);
            final int $i$f$toDataClass = 0;
            final Object $this$convert$iv$iv = $this$toDataClass$iv;
            final int $i$f$convert = 0;
            final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : getGson().toJson($this$convert$iv$iv));
            final License license2 = (License)getGson().fromJson(json$iv$iv, new TypeToken<License>() {}.getType());
            license = ((license2 == null) ? null : license2);
        }
        return license;
    }
    
    @Nullable
    public static final String decryptData(@NotNull final String content) {
        Intrinsics.checkNotNullParameter((Object)content, "content");
        final String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj0G3qEPjVTvVd7pXFUVYZFHT8KaoG4onc5rLUKqFQ2DCh/5hFK9t2nKh2XB+C2Jp/GSK2ONwD7ceXenmA6uvr90uCK/gp6j62XFVRvc8sIm0d/bGbzZFJRk3HKtxEckBmASduPObY691DVVixxNtUrSJktx/TZaB42pUQk4j+7FuOVNNPra44hDdnyGhmYBBf2B4kjXVMjL+0NCblFIN1+qjmcol44k6NFKFF54q05bjR3CRyYdAnNTCOyt9va0oB6lDlKHplSZmAOH9JGMUki/HDJbABESXMnyIpux27w9SQ8aJStYttnJWHALO1hiFJsxbz5KUkldH6Ny1p/2W5QIDAQAB";
        final PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyString, 2)));
        final EncoderUtils instance = EncoderUtils.INSTANCE;
        Intrinsics.checkNotNullExpressionValue((Object)publicKey, "publicKey");
        return EncoderUtils.decryptSegmentByPublicKey$default(instance, content, publicKey, 0, 4, null);
    }
    
    public static final boolean validateEmail(@NotNull final String email) {
        Intrinsics.checkNotNullParameter((Object)email, "email");
        final Regex regex = new Regex("^[A-Za-z0-9._%+-]+@(163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud).com$");
        return regex.matches((CharSequence)email);
    }
    
    public static final boolean sendEmail(@NotNull final String toEmail, @NotNull final String subject, @NotNull final String body) {
        Intrinsics.checkNotNullParameter((Object)toEmail, "toEmail");
        Intrinsics.checkNotNullParameter((Object)subject, "subject");
        Intrinsics.checkNotNullParameter((Object)body, "body");
        final String host = "smtp.qiye.aliyun.com";
        final int port = 465;
        final Function3 sendCommand = (Function3)ExtKt$sendEmail$sendCommand.ExtKt$sendEmail$sendCommand$1.INSTANCE;
        try {
            final SocketFactory sslSocketFactory = SSLSocketFactory.getDefault();
            final Socket socket = sslSocketFactory.createSocket(host, port);
            final OutputStream outputStream2 = socket.getOutputStream();
            Intrinsics.checkNotNullExpressionValue((Object)outputStream2, "socket.getOutputStream()");
            final OutputStream outputStream = outputStream2;
            final OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            final InputStream inputStream = socket.getInputStream();
            Intrinsics.checkNotNullExpressionValue((Object)inputStream, "socket.getInputStream()");
            final Reader in = new InputStreamReader(inputStream, Charsets.UTF_8);
            final int sz = 8192;
            final BufferedReader reader = (BufferedReader)((in instanceof BufferedReader) ? in : new BufferedReader(in, sz));
            final String line;
            final String response = line = reader.readLine();
            Intrinsics.checkNotNullExpressionValue((Object)line, "response");
            if (!StringsKt.startsWith$default(line, "220", false, 2, (Object)null)) {
                ExtKt.logger.error("Error connecting to the SMTP server.");
                return false;
            }
            final List commandList = getCommand(CollectionsKt.arrayListOf((Object[])new String[] { toEmail }), subject, body);
            boolean res = false;
            int j = 0;
            final int size = commandList.size();
            if (j < size) {
                do {
                    final int i = j;
                    ++j;
                    res = (boolean)sendCommand.invoke((Object)writer, (Object)reader, commandList.get(i));
                    if (!res) {
                        break;
                    }
                } while (j < size);
            }
            writer.close();
            reader.close();
            socket.close();
            return res;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @NotNull
    public static final List<Pair<String, Integer>> getCommand(@NotNull final List<String> to, @NotNull final String subject, @NotNull final String body) {
        Intrinsics.checkNotNullParameter((Object)to, "to");
        Intrinsics.checkNotNullParameter((Object)subject, "subject");
        Intrinsics.checkNotNullParameter((Object)body, "body");
        final String username = "no-reply@onmy.top";
        final String password = "no-reply@1.";
        final String from = "no-reply@onmy.top";
        final String fromname = "Reader";
        final String separator = "----=_Part_" + System.currentTimeMillis() + UUID.randomUUID();
        final List command = CollectionsKt.mutableListOf((Object[])new Pair[] { new Pair((Object)"HELO sendmail\r\n", (Object)250) });
        if (username.length() != 0) {
            command.add(new Pair((Object)"AUTH LOGIN\r\n", (Object)334));
            command.add(new Pair((Object)Intrinsics.stringPlus(encodeBase64(username), (Object)"\r\n"), (Object)334));
            command.add(new Pair((Object)Intrinsics.stringPlus(encodeBase64(password), (Object)"\r\n"), (Object)235));
        }
        command.add(new Pair((Object)("MAIL FROM: <" + from + ">\r\n"), (Object)250));
        String header = "FROM: " + fromname + '<' + from + ">\r\n";
        if (!to.isEmpty()) {
            final int count = to.size();
            if (count == 1) {
                command.add(new Pair((Object)("RCPT TO: <" + to.get(0) + ">\r\n"), (Object)250));
                header = header + "TO: <" + to.get(0) + ">\r\n";
            }
            else {
                int j = 0;
                if (j < count) {
                    do {
                        final int i = j;
                        ++j;
                        command.add(new Pair((Object)("RCPT TO: <" + to.get(i) + ">\r\n"), (Object)250));
                        if (i == 0) {
                            header = header + "TO: <" + to.get(i) + '>';
                        }
                        else if (i + 1 == count) {
                            header = header + ",<" + to.get(i) + ">\r\n";
                        }
                        else {
                            header = header + ",<" + to.get(i) + '>';
                        }
                    } while (j < count);
                }
            }
        }
        header = header + "Subject: =?UTF-8?B?" + encodeBase64(subject) + "?=\r\n";
        header = Intrinsics.stringPlus(header, (Object)"Content-Type: multipart/alternative;\r\n");
        header = header + "\tboundary=\"" + separator + '\"';
        header = Intrinsics.stringPlus(header, (Object)"\r\nMIME-Version: 1.0\r\n");
        header = header + "\r\n--" + separator + "\r\n";
        header = Intrinsics.stringPlus(header, (Object)"Content-Type:text/html; charset=utf-8\r\n");
        header = Intrinsics.stringPlus(header, (Object)"Content-Transfer-Encoding: base64\r\n\r\n");
        header = header + encodeBase64(body) + "\r\n";
        header = header + "--" + separator + "\r\n";
        header = Intrinsics.stringPlus(header, (Object)"\r\n.\r\n");
        command.add(new Pair((Object)"DATA\r\n", (Object)354));
        command.add(new Pair((Object)header, (Object)250));
        command.add(new Pair((Object)"QUIT\r\n", (Object)221));
        return command;
    }
    
    @NotNull
    public static final String encodeBase64(@NotNull final String text) {
        Intrinsics.checkNotNullParameter((Object)text, "text");
        final java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();
        final byte[] bytes = text.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue((Object)bytes, "(this as java.lang.String).getBytes(charset)");
        final String encodeToString = encoder.encodeToString(bytes);
        Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "getEncoder().encodeToString(text.toByteArray())");
        return encodeToString;
    }
    
    static {
        logger = KotlinLogging.INSTANCE.logger((Function0)ExtKt$logger.ExtKt$logger$1.INSTANCE);
        gson = new GsonBuilder().registerTypeAdapter(new ExtKt$gson.ExtKt$gson$1().getType(), (Object)new MapDeserializerDoubleAsIntFix()).registerTypeAdapter((Type)Integer.TYPE, (Object)new IntTypeAdapter()).registerTypeAdapter((Type)Long.TYPE, (Object)new LongTypeAdapter()).disableHtmlEscaping().create();
        prettyGson = new GsonBuilder().registerTypeAdapter(new ExtKt$prettyGson.ExtKt$prettyGson$1().getType(), (Object)new MapDeserializerDoubleAsIntFix()).registerTypeAdapter((Type)Integer.TYPE, (Object)new IntTypeAdapter()).registerTypeAdapter((Type)Long.TYPE, (Object)new LongTypeAdapter()).disableHtmlEscaping().setPrettyPrinting().create();
        ExtKt.storageFinalPath = "";
        ExtKt.workDirPath = "";
        lockMap = new ExtKt$lockMap.ExtKt$lockMap$1();
        ExtKt._licenseValid = true;
    }
}
