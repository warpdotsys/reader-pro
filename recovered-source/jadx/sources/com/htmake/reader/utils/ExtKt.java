package com.htmake.reader.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.htmake.reader.config.AppConfig;
import com.htmake.reader.entity.License;
import com.htmake.reader.entity.MongoFile;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.result.UpdateResult;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.Book;
import io.legado.app.utils.Base64;
import io.legado.app.utils.EncoderUtils;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.MapDeserializerDoubleAsIntFix;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.IntIterator;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.SpreadBuilder;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.reflect.KMutableProperty;
import kotlin.reflect.KProperty1;
import kotlin.reflect.full.KClasses;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.StringsKt;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import mu.KLogger;
import mu.KotlinLogging;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Ext.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/utils/ExtKt.class */
@Metadata(mv = {1, 5, 1}, k = 2, xi = 48, d1 = {"\u0000§\u0001\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0002\b\u0010\n\u0002\u0010\"\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\n*\u0001\u000e\u001a\u0012\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010$\u001a\u0004\u0018\u00010%\u001a\u0012\u0010&\u001a\u0004\u0018\u00010'2\b\u0010$\u001a\u0004\u0018\u00010%\u001a\u0016\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020\u00172\u0006\u0010*\u001a\u00020\u0017\u001a\u000e\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0017\u001a\u000e\u0010.\u001a\u00020,2\u0006\u0010-\u001a\u00020\u0017\u001a\u0010\u0010/\u001a\u0004\u0018\u00010\u00172\u0006\u00100\u001a\u00020\u0017\u001a\u0010\u00101\u001a\u0004\u0018\u0001022\u0006\u00100\u001a\u00020\u0017\u001a\u000e\u00103\u001a\u00020\u00172\u0006\u00104\u001a\u00020\u0017\u001a\u0016\u00105\u001a\u00020\u00172\u0006\u00106\u001a\u00020\u00172\u0006\u00107\u001a\u00020\u0017\u001a6\u00108\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020\u00010:092\f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u0017092\u0006\u0010<\u001a\u00020\u00172\u0006\u0010=\u001a\u00020\u0017\u001a\u0010\u0010>\u001a\u0002022\b\b\u0002\u0010?\u001a\u00020\u0003\u001a\u000e\u0010@\u001a\n\u0012\u0004\u0012\u00020B\u0018\u00010A\u001a\u000e\u0010C\u001a\u00020\u00172\u0006\u0010D\u001a\u00020\u0001\u001a\u001f\u0010E\u001a\u00020\u00172\u0012\u0010F\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u0017¢\u0006\u0002\u0010H\u001a+\u0010I\u001a\u0004\u0018\u00010\u00172\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\b\b\u0002\u0010K\u001a\u00020\u0017¢\u0006\u0002\u0010L\u001a)\u0010M\u001a\u00020,2\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\b\b\u0002\u0010K\u001a\u00020\u0017¢\u0006\u0002\u0010N\u001a\u0006\u0010O\u001a\u00020\u0017\u001a\u0006\u0010P\u001a\u00020\u0017\u001a\u001f\u0010Q\u001a\u00020\u00172\u0012\u0010F\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u0017¢\u0006\u0002\u0010H\u001a\u0010\u0010Q\u001a\u00020\u00172\b\b\u0002\u0010R\u001a\u00020\u0017\u001a\u0018\u0010S\u001a\u00020\u00172\u0006\u0010$\u001a\u00020%2\b\b\u0002\u0010T\u001a\u00020\u0003\u001ar\u0010U\u001a\u0004\u0018\u00010#2\u0006\u0010V\u001a\u00020,2\u0010\b\u0002\u0010W\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\u0010\b\u0002\u0010Y\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\b\b\u0002\u0010Z\u001a\u00020\u00012\b\b\u0002\u0010[\u001a\u00020\u00012\u0010\b\u0002\u0010\\\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010X2\u0016\b\u0002\u0010]\u001a\u0010\u0012\u0004\u0012\u00020_\u0012\u0004\u0012\u00020\u0003\u0018\u00010^\u001a!\u0010`\u001a\u0002Ha\"\u0004\b\u0000\u0010a2\u0006\u0010b\u001a\u00020%2\u0006\u0010c\u001a\u00020\u0017¢\u0006\u0002\u0010d\u001a\u0010\u0010e\u001a\u0004\u0018\u00010\u00172\u0006\u0010f\u001a\u00020\u0017\u001a\u0016\u0010g\u001a\u00020\u00032\u0006\u0010f\u001a\u00020\u00172\u0006\u00100\u001a\u00020\u0017\u001a;\u0010h\u001a\u00020i2\u0012\u0010J\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00170G\"\u00020\u00172\u0006\u0010$\u001a\u00020%2\b\b\u0002\u0010T\u001a\u00020\u00032\b\b\u0002\u0010K\u001a\u00020\u0017¢\u0006\u0002\u0010j\u001a\u001e\u0010k\u001a\u00020\u00032\u0006\u0010l\u001a\u00020\u00172\u0006\u0010<\u001a\u00020\u00172\u0006\u0010=\u001a\u00020\u0017\u001a\u001e\u0010m\u001a\u00020i2\u0006\u0010b\u001a\u00020%2\u0006\u0010c\u001a\u00020\u00172\u0006\u0010n\u001a\u00020%\u001a\u000e\u0010o\u001a\u00020i2\u0006\u0010p\u001a\u00020\u0003\u001a\u000e\u0010q\u001a\u00020\u00032\u0006\u0010r\u001a\u00020\u0017\u001a\u001c\u0010s\u001a\u00020\u00032\f\u0010t\u001a\b\u0012\u0004\u0012\u00020,092\u0006\u0010u\u001a\u00020\u0017\u001a)\u0010v\u001a\u0010\u0012\f\u0012\n \n*\u0004\u0018\u00010%0%0w\"\u0006\b\u0000\u0010x\u0018\u0001*\b\u0012\u0004\u0012\u0002Hx0wH\u0086\b\u001a \u0010y\u001a\u0002Hz\"\u0004\b\u0000\u0010{\"\u0006\b\u0001\u0010z\u0018\u0001*\u0002H{H\u0086\b¢\u0006\u0002\u0010|\u001a%\u0010}\u001a\b\u0012\u0004\u0012\u00020,09*\u00020,2\u000e\u0010~\u001a\n\u0012\u0004\u0012\u00020\u0017\u0018\u00010G¢\u0006\u0002\u0010\u007f\u001a\u000b\u0010\u0080\u0001\u001a\u00020i*\u00020,\u001a&\u0010\u0081\u0001\u001a\u00030\u0082\u0001*\u00030\u0082\u00012\b\u0010\u0083\u0001\u001a\u00030\u0082\u00012\r\u0010\u0084\u0001\u001a\b\u0012\u0004\u0012\u00020\u001709\u001a\u0011\u0010\u0085\u0001\u001a\b\u0012\u0004\u0012\u00020,09*\u00020,\u001a$\u0010\u0086\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001\"\u0004\b\u0000\u0010x*\u0002Hx¢\u0006\u0003\u0010\u0088\u0001\u001a)\u0010\u0089\u0001\u001a\u0002Hx\"\u0006\b\u0000\u0010x\u0018\u0001*\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001H\u0086\b¢\u0006\u0003\u0010\u008a\u0001\u001a\u0016\u0010\u008b\u0001\u001a\u00020\u0017*\u00020\u00172\t\b\u0002\u0010\u008c\u0001\u001a\u00020\u0003\u001a$\u0010\u008d\u0001\u001a\u000f\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020%0\u0087\u0001\"\u0004\b\u0000\u0010x*\u0002Hx¢\u0006\u0003\u0010\u0088\u0001\u001a\u0014\u0010\u008e\u0001\u001a\u00020\u0003*\u00020,2\u0007\u0010\u008f\u0001\u001a\u00020\u0017\u001a\u000b\u0010\u0090\u0001\u001a\u00020\u0017*\u00020\u0017\u001a\u0012\u0010s\u001a\u00020\u0003*\u00020,2\u0006\u0010u\u001a\u00020\u0017\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007\"\u0019\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\t¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0010\u0010\r\u001a\u00020\u000eX\u0082\u0004¢\u0006\u0004\n\u0002\u0010\u000f\"\u0011\u0010\u0010\u001a\u00020\u0011¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0019\u0010\u0014\u001a\n \n*\u0004\u0018\u00010\t0\t¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\f\"\u001a\u0010\u0016\u001a\u00020\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\"\u001a\u0010\u001c\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0005\"\u0004\b\u001e\u0010\u0007\"\u001a\u0010\u001f\u001a\u00020\u0017X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0019\"\u0004\b!\u0010\u001b¨\u0006\u0091\u0001"}, d2 = {"MAX_CACHE_SIZE", PackageDocumentBase.PREFIX_OPF, "_licenseValid", PackageDocumentBase.PREFIX_OPF, "get_licenseValid", "()Z", "set_licenseValid", "(Z)V", "gson", "Lcom/google/gson/Gson;", "kotlin.jvm.PlatformType", "getGson", "()Lcom/google/gson/Gson;", "lockMap", "com/htmake/reader/utils/ExtKt$lockMap$1", "Lcom/htmake/reader/utils/ExtKt$lockMap$1;", "logger", "Lmu/KLogger;", "getLogger", "()Lmu/KLogger;", "prettyGson", "getPrettyGson", "storageFinalPath", PackageDocumentBase.PREFIX_OPF, "getStorageFinalPath", "()Ljava/lang/String;", "setStorageFinalPath", "(Ljava/lang/String;)V", "workDirInit", "getWorkDirInit", "setWorkDirInit", "workDirPath", "getWorkDirPath", "setWorkDirPath", "asJsonArray", "Lio/vertx/core/json/JsonArray;", "value", PackageDocumentBase.PREFIX_OPF, "asJsonObject", "Lio/vertx/core/json/JsonObject;", "countOccurrences", "str", "subStr", "createDir", "Ljava/io/File;", "filePath", "createFile", "decryptData", "content", "decryptToLicense", "Lcom/htmake/reader/entity/License;", "encodeBase64", NCXDocumentV2.NCXTags.text, "genEncryptedPassword", "password", "salt", "getCommand", PackageDocumentBase.PREFIX_OPF, "Lkotlin/Pair;", "to", PackageDocumentBase.DCTags.subject, NCXDocumentV3.XHTMLTgs.body, "getInstalledLicense", "ignoreInvalid", "getMongoFileStorage", "Lcom/mongodb/client/MongoCollection;", "Lcom/htmake/reader/entity/MongoFile;", "getRandomString", "length", "getRelativePath", "subDirFiles", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;)Ljava/lang/String;", "getStorage", "name", "ext", "([Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", "getStorageFile", "([Ljava/lang/String;Ljava/lang/String;)Ljava/io/File;", "getStoragePath", "getTraceId", "getWorkDir", "subPath", "jsonEncode", "pretty", "parseJsonStringList", "file", "fields", PackageDocumentBase.PREFIX_OPF, "exclude", "startIndex", "endIndex", "checkNotEmpty", "filter", "Lkotlin/Function1;", "Lcom/fasterxml/jackson/databind/node/ObjectNode;", "readInstanceProperty", "R", "instance", "propertyName", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", "readMongoFile", "path", "saveMongoFile", "saveStorage", PackageDocumentBase.PREFIX_OPF, "([Ljava/lang/String;Ljava/lang/Object;ZLjava/lang/String;)V", "sendEmail", "toEmail", "setInstanceProperty", "propertyValue", "setLicenseValid", "isValid", "validateEmail", "email", "zip", "files", "zipFilePath", "arrayType", "Ljava/lang/Class;", "T", "convert", "O", "I", "(Ljava/lang/Object;)Ljava/lang/Object;", "deepListFiles", "allowExtensions", "(Ljava/io/File;[Ljava/lang/String;)Ljava/util/List;", "deleteRecursively", "fillData", "Lio/legado/app/data/entities/Book;", "newBook", "keys", "listFilesRecursively", "serializeToMap", PackageDocumentBase.PREFIX_OPF, "(Ljava/lang/Object;)Ljava/util/Map;", "toDataClass", "(Ljava/util/Map;)Ljava/lang/Object;", "toDir", "absolute", "toMap", "unzip", "descDir", RSSKeywords.RSS_ITEM_URL, "reader-pro"})
public final class ExtKt {
    private static boolean workDirInit;
    private static final int MAX_CACHE_SIZE = 1000;

    @NotNull
    private static final KLogger logger = KotlinLogging.INSTANCE.logger(new Function0<Unit>() { // from class: com.htmake.reader.utils.ExtKt$logger$1
        public final void invoke() {
        }

        /* JADX INFO: renamed from: invoke, reason: collision with other method in class */
        public /* bridge */ /* synthetic */ Object m91invoke() {
            invoke();
            return Unit.INSTANCE;
        }
    });
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, ? extends Object>>() { // from class: com.htmake.reader.utils.ExtKt$gson$1
    }.getType(), new MapDeserializerDoubleAsIntFix()).registerTypeAdapter(Integer.TYPE, new IntTypeAdapter()).registerTypeAdapter(Long.TYPE, new LongTypeAdapter()).disableHtmlEscaping().create();
    private static final Gson prettyGson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, ? extends Object>>() { // from class: com.htmake.reader.utils.ExtKt$prettyGson$1
    }.getType(), new MapDeserializerDoubleAsIntFix()).registerTypeAdapter(Integer.TYPE, new IntTypeAdapter()).registerTypeAdapter(Long.TYPE, new LongTypeAdapter()).disableHtmlEscaping().setPrettyPrinting().create();

    @NotNull
    private static String storageFinalPath = PackageDocumentBase.PREFIX_OPF;

    @NotNull
    private static String workDirPath = PackageDocumentBase.PREFIX_OPF;

    @NotNull
    private static final ExtKt$lockMap$1 lockMap = new LinkedHashMap<String, ReadWriteLock>() { // from class: com.htmake.reader.utils.ExtKt$lockMap$1
        public /* bridge */ boolean containsKey(String key) {
            return super.containsKey((Object) key);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ boolean containsKey(Object key) {
            if (key instanceof String) {
                return containsKey((String) key);
            }
            return false;
        }

        public /* bridge */ boolean containsValue(ReadWriteLock value) {
            return super.containsValue((Object) value);
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ boolean containsValue(Object value) {
            if (value instanceof ReadWriteLock) {
                return containsValue((ReadWriteLock) value);
            }
            return false;
        }

        public /* bridge */ ReadWriteLock get(String key) {
            return (ReadWriteLock) super.get((Object) key);
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ ReadWriteLock get(Object key) {
            if (key instanceof String) {
                return get((String) key);
            }
            return null;
        }

        public /* bridge */ ReadWriteLock getOrDefault(String key, ReadWriteLock defaultValue) {
            return (ReadWriteLock) super.getOrDefault((Object) key, defaultValue);
        }

        public final /* bridge */ ReadWriteLock getOrDefault(Object key, ReadWriteLock defaultValue) {
            return !(key instanceof String) ? defaultValue : getOrDefault((String) key, defaultValue);
        }

        public /* bridge */ ReadWriteLock remove(String key) {
            return (ReadWriteLock) super.remove((Object) key);
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ ReadWriteLock remove(Object key) {
            if (key instanceof String) {
                return remove((String) key);
            }
            return null;
        }

        public /* bridge */ boolean remove(String key, ReadWriteLock value) {
            return super.remove((Object) key, (Object) value);
        }

        @Override // java.util.HashMap, java.util.Map
        public final /* bridge */ boolean remove(Object key, Object value) {
            if ((key instanceof String) && (value instanceof ReadWriteLock)) {
                return remove((String) key, (ReadWriteLock) value);
            }
            return false;
        }

        public /* bridge */ Set<Map.Entry<String, ReadWriteLock>> getEntries() {
            return super.entrySet();
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ Set<Map.Entry<String, ReadWriteLock>> entrySet() {
            return getEntries();
        }

        public /* bridge */ Set<String> getKeys() {
            return super.keySet();
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ Set<String> keySet() {
            return getKeys();
        }

        public /* bridge */ int getSize() {
            return super.size();
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ int size() {
            return getSize();
        }

        public /* bridge */ Collection<ReadWriteLock> getValues() {
            return super.values();
        }

        @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
        public final /* bridge */ Collection<ReadWriteLock> values() {
            return getValues();
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(@Nullable Map.Entry<String, ReadWriteLock> eldest) {
            return size() > 1000;
        }
    };
    private static boolean _licenseValid = true;

    @NotNull
    public static final KLogger getLogger() {
        return logger;
    }

    public static final Gson getGson() {
        return gson;
    }

    public static final Gson getPrettyGson() {
        return prettyGson;
    }

    @NotNull
    public static final String getStorageFinalPath() {
        return storageFinalPath;
    }

    public static final void setStorageFinalPath(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        storageFinalPath = str;
    }

    @NotNull
    public static final String getWorkDirPath() {
        return workDirPath;
    }

    public static final void setWorkDirPath(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        workDirPath = str;
    }

    public static final boolean getWorkDirInit() {
        return workDirInit;
    }

    public static final void setWorkDirInit(boolean z) {
        workDirInit = z;
    }

    @NotNull
    public static final String url(@NotNull String $this$url) {
        Intrinsics.checkNotNullParameter($this$url, "<this>");
        if (StringsKt.startsWith$default($this$url, "//", false, 2, (Object) null)) {
            return HttpUrl.Companion.get(Intrinsics.stringPlus("http:", $this$url)).toString();
        }
        if (StringsKt.startsWith$default($this$url, "http", false, 2, (Object) null)) {
            return HttpUrl.Companion.get($this$url).toString();
        }
        return $this$url;
    }

    public static /* synthetic */ String toDir$default(String str, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return toDir(str, z);
    }

    @NotNull
    public static final String toDir(@NotNull String $this$toDir, boolean absolute) {
        Intrinsics.checkNotNullParameter($this$toDir, "<this>");
        String path = $this$toDir;
        if (StringsKt.endsWith$default(path, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
            String strSubstring = path.substring(0, path.length() - 1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            path = strSubstring;
        }
        if (absolute && !StringsKt.startsWith$default(path, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
            path = Intrinsics.stringPlus(TableOfContents.DEFAULT_PATH_SEPARATOR, path);
        }
        return path;
    }

    public static final void deleteRecursively(@NotNull File $this$deleteRecursively) {
        Intrinsics.checkNotNullParameter($this$deleteRecursively, "<this>");
        if ($this$deleteRecursively.exists()) {
            if ($this$deleteRecursively.isFile()) {
                $this$deleteRecursively.delete();
                return;
            }
            Object[] objArrListFiles = $this$deleteRecursively.listFiles();
            Intrinsics.checkNotNullExpressionValue(objArrListFiles, "this.listFiles()");
            Object[] $this$forEach$iv = objArrListFiles;
            for (Object element$iv : $this$forEach$iv) {
                File it = (File) element$iv;
                Intrinsics.checkNotNullExpressionValue(it, "it");
                deleteRecursively(it);
            }
            $this$deleteRecursively.delete();
        }
    }

    @NotNull
    public static final List<File> listFilesRecursively(@NotNull File $this$listFilesRecursively) {
        Intrinsics.checkNotNullParameter($this$listFilesRecursively, "<this>");
        ArrayList arrayList = new ArrayList();
        if ($this$listFilesRecursively.exists()) {
            if ($this$listFilesRecursively.isFile()) {
                arrayList.add($this$listFilesRecursively);
            } else {
                Object[] objArrListFiles = $this$listFilesRecursively.listFiles();
                Intrinsics.checkNotNullExpressionValue(objArrListFiles, "this.listFiles()");
                Object[] $this$forEach$iv = objArrListFiles;
                for (Object element$iv : $this$forEach$iv) {
                    File it = (File) element$iv;
                    arrayList.add(it);
                    if (it.isDirectory()) {
                        Intrinsics.checkNotNullExpressionValue(it, "it");
                        arrayList.addAll(listFilesRecursively(it));
                    }
                }
            }
        }
        return arrayList;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0157  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0167  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final boolean unzip(@NotNull File $this$unzip, @NotNull String descDir) throws IOException {
        InputStream inputStream;
        OutputStream outputStream;
        ZipFile zf;
        Enumeration<? extends ZipEntry> enumerationEntries;
        Intrinsics.checkNotNullParameter($this$unzip, "<this>");
        Intrinsics.checkNotNullParameter(descDir, "descDir");
        if (!$this$unzip.exists()) {
            return false;
        }
        byte[] buffer = new byte[1024];
        OutputStream outputStream2 = null;
        InputStream inputStream2 = null;
        try {
            try {
                zf = new ZipFile($this$unzip.toString());
                enumerationEntries = zf.entries();
            } catch (Exception e) {
                e.printStackTrace();
                InputStream inputStream3 = inputStream2;
                if (inputStream3 != null) {
                    inputStream3.close();
                }
                OutputStream outputStream3 = outputStream2;
                if (outputStream3 == null) {
                    return false;
                }
                outputStream3.close();
                return false;
            }
        } catch (Throwable th) {
            inputStream = inputStream2;
            if (inputStream != null) {
            }
            outputStream = outputStream2;
            if (outputStream != null) {
            }
            throw th;
        }
        while (enumerationEntries.hasMoreElements()) {
            ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
            if (zipEntryNextElement == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.util.zip.ZipEntry");
            }
            ZipEntry zipEntry = zipEntryNextElement;
            String zipEntryName = zipEntry.getName();
            Intrinsics.checkNotNullExpressionValue(zipEntryName, "zipEntry.name");
            String descFilePath = descDir + ((Object) File.separator) + zipEntryName;
            if (zipEntry.isDirectory()) {
                createDir(descFilePath);
            } else {
                inputStream2 = zf.getInputStream(zipEntry);
                File descFile = createFile(descFilePath);
                outputStream2 = new FileOutputStream(descFile);
                while (true) {
                    int it = inputStream2.read(buffer);
                    if (it <= 0) {
                        break;
                    }
                    ((FileOutputStream) outputStream2).write(buffer, 0, it);
                }
                inputStream2.close();
                ((FileOutputStream) outputStream2).close();
            }
            inputStream = inputStream2;
            if (inputStream != null) {
                inputStream.close();
            }
            outputStream = outputStream2;
            if (outputStream != null) {
                outputStream.close();
            }
            throw th;
        }
        InputStream inputStream4 = inputStream2;
        if (inputStream4 != null) {
            inputStream4.close();
        }
        OutputStream outputStream4 = outputStream2;
        if (outputStream4 != null) {
            outputStream4.close();
        }
        return true;
    }

    public static final boolean zip(@NotNull File $this$zip, @NotNull String zipFilePath) {
        Intrinsics.checkNotNullParameter($this$zip, "<this>");
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        if (!$this$zip.exists()) {
            return false;
        }
        if (!$this$zip.isDirectory()) {
            return zip(CollectionsKt.arrayListOf(new File[]{$this$zip}), zipFilePath);
        }
        File[] files = $this$zip.listFiles();
        Intrinsics.checkNotNullExpressionValue(files, "files");
        List filesList = ArraysKt.toList(files);
        return zip((List<? extends File>) filesList, zipFilePath);
    }

    public static final boolean zip(@NotNull List<? extends File> files, @NotNull String zipFilePath) throws IOException {
        Intrinsics.checkNotNullParameter(files, "files");
        Intrinsics.checkNotNullParameter(zipFilePath, "zipFilePath");
        if (files.isEmpty()) {
            return false;
        }
        File zipFile = createFile(zipFilePath);
        byte[] buffer = new byte[1024];
        ZipOutputStream zipOutputStream = null;
        FileInputStream inputStream = null;
        try {
            try {
                zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
                for (File file : files) {
                    if (file.exists()) {
                        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                        inputStream = new FileInputStream(file);
                        while (true) {
                            int it = inputStream.read(buffer);
                            if (it <= 0) {
                                break;
                            }
                            zipOutputStream.write(buffer, 0, it);
                        }
                        zipOutputStream.closeEntry();
                    }
                }
                FileInputStream fileInputStream = inputStream;
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                zipOutputStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                FileInputStream fileInputStream2 = inputStream;
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                ZipOutputStream zipOutputStream2 = zipOutputStream;
                if (zipOutputStream2 == null) {
                    return false;
                }
                zipOutputStream2.close();
                return false;
            }
        } catch (Throwable th) {
            FileInputStream fileInputStream3 = inputStream;
            if (fileInputStream3 != null) {
                fileInputStream3.close();
            }
            ZipOutputStream zipOutputStream3 = zipOutputStream;
            if (zipOutputStream3 != null) {
                zipOutputStream3.close();
            }
            throw th;
        }
    }

    @NotNull
    public static final File createDir(@NotNull String filePath) {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        logger.debug("createDir filePath {}", filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    @NotNull
    public static final File createFile(@NotNull String filePath) throws IOException {
        Intrinsics.checkNotNullParameter(filePath, "filePath");
        logger.debug("createFile filePath {}", filePath);
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        Intrinsics.checkNotNull(parentFile);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    public static /* synthetic */ String getWorkDir$default(String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = PackageDocumentBase.PREFIX_OPF;
        }
        return getWorkDir(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x013d  */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final String getWorkDir(@NotNull String subPath) {
        Intrinsics.checkNotNullParameter(subPath, "subPath");
        if (!workDirInit) {
            if (workDirPath.length() == 0) {
                AppConfig appConfig = (AppConfig) SpringContextUtils.getBean("appConfig", AppConfig.class);
                if (appConfig != null) {
                    if ((appConfig.getWorkDir().length() > 0) && !appConfig.getWorkDir().equals(".")) {
                        File workDirFile = new File(appConfig.getWorkDir());
                        if (workDirFile.exists() && !workDirFile.isDirectory()) {
                            logger.error("reader.app.workDir={} is not a directory", appConfig.getWorkDir());
                        } else {
                            if (!workDirFile.exists()) {
                                logger.info("reader.app.workDir={} not exists, creating", appConfig.getWorkDir());
                                workDirFile.mkdirs();
                            }
                            String absolutePath = workDirFile.getAbsolutePath();
                            Intrinsics.checkNotNullExpressionValue(absolutePath, "workDirFile.absolutePath");
                            workDirPath = absolutePath;
                        }
                    }
                }
                if (workDirPath.length() == 0) {
                    String osName = System.getProperty("os.name");
                    String currentDir = System.getProperty("user.dir");
                    logger.info("osName: {} currentDir: {}", osName, currentDir);
                    Intrinsics.checkNotNullExpressionValue(osName, "osName");
                    if (StringsKt.startsWith(osName, "Mac OS", true)) {
                        Intrinsics.checkNotNullExpressionValue(currentDir, "currentDir");
                        if (StringsKt.startsWith$default(currentDir, "/Users/", false, 2, (Object) null)) {
                            Intrinsics.checkNotNullExpressionValue(currentDir, "currentDir");
                            workDirPath = currentDir;
                        } else {
                            workDirPath = Paths.get(System.getProperty("user.home"), ".reader").toString();
                        }
                    }
                }
                logger.info("Using workdir: {}", workDirPath);
                workDirInit = true;
            }
        }
        Path path = Paths.get(workDirPath, subPath);
        return path.toString();
    }

    @NotNull
    public static final String getWorkDir(@NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        return getWorkDir(getRelativePath((String[]) Arrays.copyOf(subDirFiles, subDirFiles.length)));
    }

    @NotNull
    public static final String getRelativePath(@NotNull String... subDirFiles) {
        Intrinsics.checkNotNullParameter(subDirFiles, "subDirFiles");
        StringBuilder path = new StringBuilder(PackageDocumentBase.PREFIX_OPF);
        for (String str : subDirFiles) {
            if (str.length() > 0) {
                path.append(File.separator).append(str);
            }
        }
        String it = path.toString();
        Intrinsics.checkNotNullExpressionValue(it, "it");
        if (StringsKt.startsWith$default(it, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
            String strSubstring = it.substring(1);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
            return strSubstring;
        }
        return it;
    }

    @NotNull
    public static final String getStoragePath() {
        String storagePath;
        if (storageFinalPath.length() > 0) {
            return storageFinalPath;
        }
        AppConfig appConfig = (AppConfig) SpringContextUtils.getBean("appConfig", AppConfig.class);
        if (appConfig != null) {
            storagePath = getWorkDir("storage");
            storageFinalPath = storagePath;
        } else {
            String path = new File("storage").getPath();
            Intrinsics.checkNotNullExpressionValue(path, "File(\"storage\").path");
            storagePath = path;
        }
        logger.info("Using storagePath: {}", storagePath);
        return storagePath;
    }

    public static /* synthetic */ void saveStorage$default(String[] strArr, Object obj, boolean z, String str, int i, Object obj2) {
        if ((i & 4) != 0) {
            z = false;
        }
        if ((i & 8) != 0) {
            str = ".json";
        }
        saveStorage(strArr, obj, z, str);
    }

    public static final void saveStorage(@NotNull String[] name, @NotNull Object value, boolean pretty, @NotNull String ext) {
        String string;
        Object obj;
        ReadWriteLock lock;
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(value, "value");
        Intrinsics.checkNotNullParameter(ext, "ext");
        if (value instanceof String) {
            string = (String) value;
        } else if ((value instanceof JsonObject) || (value instanceof JsonArray)) {
            string = value.toString();
        } else if (pretty) {
            String json = prettyGson.toJson(value);
            Intrinsics.checkNotNullExpressionValue(json, "{\n        prettyGson.toJson(value)\n    }");
            string = json;
        } else {
            String json2 = gson.toJson(value);
            Intrinsics.checkNotNullExpressionValue(json2, "{\n        gson.toJson(value)\n    }");
            string = json2;
        }
        String toJson = string;
        String storagePath = getStoragePath();
        File storageDir = new File(storagePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String filename = (String) ArraysKt.last(name);
        SpreadBuilder spreadBuilder = new SpreadBuilder(2);
        spreadBuilder.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
        spreadBuilder.add(Intrinsics.stringPlus(filename, ext));
        String path = getRelativePath((String[]) spreadBuilder.toArray(new String[spreadBuilder.size()]));
        File file = new File(storagePath + ((Object) File.separator) + path);
        logger.info("Save file to storage name: {} path: {}", name, file.getAbsoluteFile());
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        File absoluteFile = file.getAbsoluteFile();
        Intrinsics.checkNotNullExpressionValue(absoluteFile, "file.absoluteFile");
        String filename2 = FilesKt.getNameWithoutExtension(absoluteFile);
        synchronized (lockMap) {
            Map $this$getOrPut$iv = lockMap;
            String absolutePath = file.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue(absolutePath, "file.absolutePath");
            Object value$iv = $this$getOrPut$iv.get(absolutePath);
            if (value$iv == null) {
                ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
                $this$getOrPut$iv.put(absolutePath, reentrantReadWriteLock);
                obj = reentrantReadWriteLock;
            } else {
                obj = value$iv;
            }
            lock = (ReadWriteLock) obj;
        }
        try {
            try {
                boolean acquired = lock.writeLock().tryLock(10L, TimeUnit.SECONDS);
                if (!acquired) {
                    throw new Exception(Intrinsics.stringPlus("保存文件超时: ", file.getAbsolutePath()));
                }
                Path tmp = Files.createTempFile(Paths.get(file.getParentFile().getPath(), new String[0]).toAbsolutePath(), filename2, ".temp", new FileAttribute[0]);
                Charset charset = Charsets.UTF_8;
                if (toJson == null) {
                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                }
                byte[] bytes = toJson.getBytes(charset);
                Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
                Files.write(tmp, bytes, new OpenOption[0]);
                Path filePath = Paths.get(file.getPath(), new String[0]);
                Path backupPath = Paths.get(file.getParentFile().getPath(), Intrinsics.stringPlus(filename2, ".backup.json")).toAbsolutePath();
                if (Files.exists(filePath, new LinkOption[0])) {
                    Files.move(filePath, backupPath, StandardCopyOption.ATOMIC_MOVE);
                }
                Files.move(tmp, filePath, StandardCopyOption.ATOMIC_MOVE);
                Files.deleteIfExists(tmp);
                if (filename2.length() >= 32) {
                    Files.deleteIfExists(backupPath);
                }
                if ("users".equals(filename2)) {
                    int userCount = countOccurrences(toJson, "username");
                    SpreadBuilder spreadBuilder2 = new SpreadBuilder(2);
                    spreadBuilder2.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
                    spreadBuilder2.add('.' + filename2 + ".key");
                    String verifyKeyPath = getRelativePath((String[]) spreadBuilder2.toArray(new String[spreadBuilder2.size()]));
                    File verifyKeyFile = new File(storagePath + ((Object) File.separator) + verifyKeyPath);
                    if (!verifyKeyFile.exists()) {
                        verifyKeyFile.createNewFile();
                    }
                    String md5Encode = MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus("userCount=", Integer.valueOf(userCount))).toString();
                    int length = md5Encode.length() - 16;
                    if (md5Encode == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    String strSubstring = md5Encode.substring(length);
                    Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                    FilesKt.writeText$default(verifyKeyFile, strSubstring, (Charset) null, 2, (Object) null);
                }
                saveMongoFile(path, toJson);
                if (acquired) {
                    lock.writeLock().unlock();
                }
            } catch (Exception e) {
                logger.error("保存文件失败: ", e);
                throw new Exception(Intrinsics.stringPlus("保存文件失败: ", file.getAbsolutePath()));
            }
        } catch (Throwable th) {
            if (0 != 0) {
                lock.writeLock().unlock();
            }
            throw th;
        }
    }

    public static /* synthetic */ File getStorageFile$default(String[] strArr, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = ".json";
        }
        return getStorageFile(strArr, str);
    }

    @NotNull
    public static final File getStorageFile(@NotNull String[] name, @NotNull String ext) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(ext, "ext");
        String storagePath = getStoragePath();
        File storageDir = new File(storagePath);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String filename = (String) ArraysKt.last(name);
        SpreadBuilder spreadBuilder = new SpreadBuilder(2);
        spreadBuilder.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
        spreadBuilder.add(Intrinsics.stringPlus(filename, ext));
        String path = getRelativePath((String[]) spreadBuilder.toArray(new String[spreadBuilder.size()]));
        return new File(storagePath + ((Object) File.separator) + path);
    }

    public static /* synthetic */ String getStorage$default(String[] strArr, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = ".json";
        }
        return getStorage(strArr, str);
    }

    @Nullable
    public static final String getStorage(@NotNull String[] name, @NotNull String ext) throws IOException {
        Object obj;
        ReadWriteLock lock;
        String str;
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(ext, "ext");
        String storagePath = getStoragePath();
        String filename = (String) ArraysKt.last(name);
        SpreadBuilder spreadBuilder = new SpreadBuilder(2);
        spreadBuilder.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
        spreadBuilder.add(Intrinsics.stringPlus(filename, ext));
        String path = getRelativePath((String[]) spreadBuilder.toArray(new String[spreadBuilder.size()]));
        File file = getStorageFile((String[]) Arrays.copyOf(name, name.length), ext);
        logger.info("Read file from storage name: {} path: {}", name, file.getAbsoluteFile());
        if (!file.exists()) {
            String content = readMongoFile(path);
            if (content == null) {
                return null;
            }
            if (content.length() > 0) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                FilesKt.writeText$default(file, content, (Charset) null, 2, (Object) null);
            }
            return content;
        }
        synchronized (lockMap) {
            Map $this$getOrPut$iv = lockMap;
            String absolutePath = file.getAbsolutePath();
            Intrinsics.checkNotNullExpressionValue(absolutePath, "file.absolutePath");
            Object value$iv = $this$getOrPut$iv.get(absolutePath);
            if (value$iv == null) {
                ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
                $this$getOrPut$iv.put(absolutePath, reentrantReadWriteLock);
                obj = reentrantReadWriteLock;
            } else {
                obj = value$iv;
            }
            lock = (ReadWriteLock) obj;
        }
        boolean acquired = false;
        try {
            try {
                acquired = lock.readLock().tryLock(10L, TimeUnit.SECONDS);
                if (!acquired) {
                    throw new Exception(Intrinsics.stringPlus("读取文件超时: ", file.getAbsolutePath()));
                }
                FileReader reader = new FileReader(file);
                try {
                    String content2 = TextStreamsKt.readText(reader);
                    if (content2.length() == 0) {
                        String content3 = readMongoFile(path);
                        if (content3 == null) {
                            str = content2;
                        } else {
                            if (content3.length() > 0) {
                                if (!file.getParentFile().exists()) {
                                    file.getParentFile().mkdirs();
                                }
                                file.createNewFile();
                                FilesKt.writeText$default(file, content3, (Charset) null, 2, (Object) null);
                            }
                            str = content3;
                        }
                        String str2 = str;
                        if (acquired) {
                            lock.readLock().unlock();
                        }
                        return str2;
                    }
                    if ("users".equals(filename)) {
                        SpreadBuilder spreadBuilder2 = new SpreadBuilder(2);
                        spreadBuilder2.addSpread(ArraysKt.copyOfRange(name, 0, name.length - 1));
                        spreadBuilder2.add('.' + filename + ".key");
                        String verifyKeyPath = getRelativePath((String[]) spreadBuilder2.toArray(new String[spreadBuilder2.size()]));
                        File verifyKeyFile = new File(storagePath + ((Object) File.separator) + verifyKeyPath);
                        if (verifyKeyFile.exists()) {
                            String verifyKeyContent = FilesKt.readText$default(verifyKeyFile, (Charset) null, 1, (Object) null);
                            int userCount = countOccurrences(content2, "username");
                            String md5Encode = MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus("userCount=", Integer.valueOf(userCount))).toString();
                            int length = md5Encode.length() - 16;
                            if (md5Encode == null) {
                                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                            }
                            String strSubstring = md5Encode.substring(length);
                            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                            if (!verifyKeyContent.equals(strSubstring)) {
                                throw new Exception("用户数据被篡改，请联系开发者修复");
                            }
                        }
                    }
                    reader.close();
                    return content2;
                } finally {
                    reader.close();
                }
            } catch (Exception e) {
                logger.error("读取文件失败: ", e);
                throw new Exception(Intrinsics.stringPlus("读取文件失败: ", file.getAbsolutePath()));
            }
        } finally {
            if (acquired) {
                lock.readLock().unlock();
            }
        }
    }

    @Nullable
    public static final MongoCollection<MongoFile> getMongoFileStorage() {
        AppConfig appConfig = (AppConfig) SpringContextUtils.getBean("appConfig", AppConfig.class);
        return MongoManager.INSTANCE.fileStorage(appConfig.getMongoDbName(), "storage");
    }

    @Nullable
    public static final String readMongoFile(@NotNull String path) {
        FindIterable findIterableFind;
        Intrinsics.checkNotNullParameter(path, "path");
        if (MongoManager.INSTANCE.isInit()) {
            logger.info("Get mongoFile {}", path);
            MongoCollection<MongoFile> mongoFileStorage = getMongoFileStorage();
            MongoFile mongoFile = (mongoFileStorage == null || (findIterableFind = mongoFileStorage.find(Filters.eq("path", path))) == null) ? null : (MongoFile) findIterableFind.first();
            MongoFile doc = mongoFile;
            if (doc != null) {
                return doc.getContent();
            }
            return null;
        }
        return null;
    }

    public static final boolean saveMongoFile(@NotNull String path, @NotNull String content) {
        FindIterable findIterableFind;
        Intrinsics.checkNotNullParameter(path, "path");
        Intrinsics.checkNotNullParameter(content, "content");
        if (MongoManager.INSTANCE.isInit()) {
            logger.info("Save mongoFile {}", path);
            MongoCollection<MongoFile> mongoFileStorage = getMongoFileStorage();
            MongoFile mongoFile = (mongoFileStorage == null || (findIterableFind = mongoFileStorage.find(Filters.eq("path", path))) == null) ? null : (MongoFile) findIterableFind.first();
            MongoFile doc = mongoFile;
            if (doc != null) {
                doc.setContent(content);
                doc.setUpdated_at(System.currentTimeMillis());
                MongoCollection<MongoFile> mongoFileStorage2 = getMongoFileStorage();
                UpdateResult result = mongoFileStorage2 == null ? null : mongoFileStorage2.replaceOne(Filters.eq("path", path), doc, new ReplaceOptions().upsert(true));
                return result != null && result.getModifiedCount() > 0;
            }
            MongoFile doc2 = new MongoFile(path, content, 0L, 0L, 12, null);
            try {
                MongoCollection<MongoFile> mongoFileStorage3 = getMongoFileStorage();
                if (mongoFileStorage3 == null) {
                    return true;
                }
                mongoFileStorage3.insertOne(doc2);
                return true;
            } catch (Exception e) {
                logger.info("Save mongoFile {} failed", path);
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static final int countOccurrences(@NotNull String str, @NotNull String subStr) {
        int index;
        Intrinsics.checkNotNullParameter(str, "str");
        Intrinsics.checkNotNullParameter(subStr, "subStr");
        int count = 0;
        int length = 0;
        while (true) {
            int startIndex = length;
            if (startIndex >= str.length() || (index = StringsKt.indexOf$default(str, subStr, startIndex, false, 4, (Object) null)) == -1) {
                break;
            }
            count++;
            length = index + subStr.length();
        }
        return count;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:8:0x0013
            at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
            at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.Nullable
    public static final io.vertx.core.json.JsonArray asJsonArray(@org.jetbrains.annotations.Nullable java.lang.Object r5) {
        /*
            r0 = r5
            boolean r0 = r0 instanceof io.vertx.core.json.JsonArray
            if (r0 == 0) goto Lc
            r0 = r5
            io.vertx.core.json.JsonArray r0 = (io.vertx.core.json.JsonArray) r0
            return r0
        Lc:
            r0 = r5
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L30
        L14:
            io.vertx.core.json.JsonArray r0 = new io.vertx.core.json.JsonArray     // Catch: java.lang.Exception -> L20
            r1 = r0
            r2 = r5
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Exception -> L20
            r1.<init>(r2)     // Catch: java.lang.Exception -> L20
            return r0
        L20:
            r6 = move-exception
            mu.KLogger r0 = com.htmake.reader.utils.ExtKt.logger
            java.lang.String r1 = "解析内容出错: {}  内容: \n{}"
            r2 = r6
            r3 = r5
            r0.error(r1, r2, r3)
            r0 = r6
            throw r0
        L30:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.htmake.reader.utils.ExtKt.asJsonArray(java.lang.Object):io.vertx.core.json.JsonArray");
    }

    public static /* synthetic */ JsonArray parseJsonStringList$default(File file, Set set, Set set2, int i, int i2, Set set3, Function1 function1, int i3, Object obj) {
        if ((i3 & 2) != 0) {
            set = null;
        }
        if ((i3 & 4) != 0) {
            set2 = null;
        }
        if ((i3 & 8) != 0) {
            i = 0;
        }
        if ((i3 & 16) != 0) {
            i2 = Integer.MAX_VALUE;
        }
        if ((i3 & 32) != 0) {
            set3 = null;
        }
        if ((i3 & 64) != 0) {
            function1 = null;
        }
        return parseJsonStringList(file, set, set2, i, i2, set3, function1);
    }

    @Nullable
    public static final JsonArray parseJsonStringList(@NotNull File file, @Nullable Set<String> fields, @Nullable Set<String> exclude, int startIndex, int endIndex, @Nullable Set<String> checkNotEmpty, @Nullable Function1<? super ObjectNode, Boolean> filter) throws Exception {
        Intrinsics.checkNotNullParameter(file, "file");
        if (!file.exists()) {
            return null;
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory factory = objectMapper.getFactory();
            JsonArray resultList = new JsonArray();
            int currentIndex = -1;
            JsonParser jsonParser = (Closeable) factory.createParser(file);
            Throwable th = (Throwable) null;
            try {
                JsonParser parser = jsonParser;
                if (parser.nextToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() == JsonToken.START_OBJECT) {
                            Set<String> set = fields;
                            if (set == null || set.isEmpty()) {
                                if (filter == null) {
                                    currentIndex++;
                                    if (currentIndex < startIndex) {
                                        parser.skipChildren();
                                    } else {
                                        if (currentIndex > endIndex) {
                                            break;
                                        }
                                        ObjectNode valueAsTree = parser.readValueAsTree();
                                        Intrinsics.checkNotNullExpressionValue(valueAsTree, "parser.readValueAsTree()");
                                        ObjectNode objectNode = (JsonNode) valueAsTree;
                                        Set<String> set2 = exclude;
                                        if (!(set2 == null || set2.isEmpty())) {
                                            Set<String> $this$forEach$iv = exclude;
                                            for (Object element$iv : $this$forEach$iv) {
                                                String it = (String) element$iv;
                                                objectNode.remove(it);
                                            }
                                        }
                                        String jsonString = objectNode.toString();
                                        Intrinsics.checkNotNullExpressionValue(jsonString, "objectNode.toString()");
                                        resultList.add(jsonString);
                                    }
                                } else {
                                    ObjectNode valueAsTree2 = parser.readValueAsTree();
                                    Intrinsics.checkNotNullExpressionValue(valueAsTree2, "parser.readValueAsTree()");
                                    ObjectNode objectNode2 = (JsonNode) valueAsTree2;
                                    if (((Boolean) filter.invoke(objectNode2)).booleanValue()) {
                                        currentIndex++;
                                    }
                                    if (currentIndex >= startIndex) {
                                        if (currentIndex > endIndex) {
                                            break;
                                        }
                                        String jsonString2 = objectNode2.toString();
                                        Intrinsics.checkNotNullExpressionValue(jsonString2, "objectNode.toString()");
                                        resultList.add(jsonString2);
                                    }
                                }
                            } else {
                                currentIndex++;
                                if (currentIndex < startIndex) {
                                    parser.skipChildren();
                                } else {
                                    if (currentIndex > endIndex) {
                                        break;
                                    }
                                    JsonObject item = new JsonObject();
                                    while (parser.nextToken() != JsonToken.END_OBJECT) {
                                        String fieldName = parser.getCurrentName();
                                        parser.nextToken();
                                        if (fields.contains(fieldName)) {
                                            item.put(fieldName, parser.getValueAsString());
                                        } else if (checkNotEmpty != null && checkNotEmpty.contains(fieldName)) {
                                            String valueAsString = parser.getValueAsString();
                                            item.put(fieldName, Boolean.valueOf(!(valueAsString == null || valueAsString.length() == 0)));
                                        } else {
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
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(jsonParser, th);
                return resultList;
            } catch (Throwable th2) {
                CloseableKt.closeFinally(jsonParser, th);
                throw th2;
            }
        } catch (Exception e) {
            logger.error("解析文件内容出错: {}  文件: \n{}", e, file);
            throw e;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:8:0x0013
            at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
            at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.Nullable
    public static final io.vertx.core.json.JsonObject asJsonObject(@org.jetbrains.annotations.Nullable java.lang.Object r5) {
        /*
            r0 = r5
            boolean r0 = r0 instanceof io.vertx.core.json.JsonObject
            if (r0 == 0) goto Lc
            r0 = r5
            io.vertx.core.json.JsonObject r0 = (io.vertx.core.json.JsonObject) r0
            return r0
        Lc:
            r0 = r5
            boolean r0 = r0 instanceof java.lang.String
            if (r0 == 0) goto L30
        L14:
            io.vertx.core.json.JsonObject r0 = new io.vertx.core.json.JsonObject     // Catch: java.lang.Exception -> L20
            r1 = r0
            r2 = r5
            java.lang.String r2 = (java.lang.String) r2     // Catch: java.lang.Exception -> L20
            r1.<init>(r2)     // Catch: java.lang.Exception -> L20
            return r0
        L20:
            r6 = move-exception
            mu.KLogger r0 = com.htmake.reader.utils.ExtKt.logger
            java.lang.String r1 = "解析内容出错: {}  内容: \n{}"
            r2 = r6
            r3 = r5
            r0.error(r1, r2, r3)
            r0 = r6
            throw r0
        L30:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.htmake.reader.utils.ExtKt.asJsonObject(java.lang.Object):io.vertx.core.json.JsonObject");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.htmake.reader.utils.ExtKt$serializeToMap$$inlined$convert$1] */
    @NotNull
    public static final <T> Map<String, Object> serializeToMap(T $this$serializeToMap) {
        String json;
        if (!($this$serializeToMap instanceof String)) {
            json = getGson().toJson($this$serializeToMap);
        } else {
            json = (String) $this$serializeToMap;
        }
        String json$iv = json;
        return (Map) getGson().fromJson(json$iv, new TypeToken<Map<String, ? extends Object>>() { // from class: com.htmake.reader.utils.ExtKt$serializeToMap$$inlined$convert$1
        }.getType());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0, types: [com.htmake.reader.utils.ExtKt$toMap$$inlined$convert$1] */
    @NotNull
    public static final <T> Map<String, Object> toMap(T $this$toMap) {
        String json;
        if (!($this$toMap instanceof String)) {
            json = getGson().toJson($this$toMap);
        } else {
            json = (String) $this$toMap;
        }
        String json$iv = json;
        return (Map) getGson().fromJson(json$iv, new TypeToken<Map<String, ? extends Object>>() { // from class: com.htmake.reader.utils.ExtKt$toMap$$inlined$convert$1
        }.getType());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final /* synthetic */ <T> T toDataClass(Map<String, ? extends Object> map) {
        String json;
        Intrinsics.checkNotNullParameter(map, "<this>");
        if (!(map instanceof String)) {
            json = getGson().toJson(map);
        } else {
            json = (String) map;
        }
        String str = json;
        Gson gson2 = getGson();
        Intrinsics.needClassReification();
        return (T) gson2.fromJson(str, new TypeToken<T>() { // from class: com.htmake.reader.utils.ExtKt$toDataClass$$inlined$convert$1
        }.getType());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final /* synthetic */ <I, O> O convert(I i) {
        String json;
        if (i instanceof String) {
            json = (String) i;
        } else {
            json = getGson().toJson(i);
        }
        String str = json;
        Gson gson2 = getGson();
        Intrinsics.needClassReification();
        return (O) gson2.fromJson(str, new TypeToken<O>() { // from class: com.htmake.reader.utils.ExtKt.convert.1
        }.getType());
    }

    public static final /* synthetic */ <T> Class<Object> arrayType(Class<T> cls) {
        Intrinsics.checkNotNullParameter(cls, "<this>");
        return Array.newInstance((Class<?>) cls, 0).getClass();
    }

    public static final <R> R readInstanceProperty(@NotNull Object obj, @NotNull String str) {
        Intrinsics.checkNotNullParameter(obj, "instance");
        Intrinsics.checkNotNullParameter(str, "propertyName");
        for (Object obj2 : KClasses.getMemberProperties(Reflection.getOrCreateKotlinClass(obj.getClass()))) {
            if (Intrinsics.areEqual(((KProperty1) obj2).getName(), str)) {
                return (R) ((KProperty1) obj2).get(obj);
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    public static final void setInstanceProperty(@NotNull Object instance, @NotNull String propertyName, @NotNull Object propertyValue) {
        Intrinsics.checkNotNullParameter(instance, "instance");
        Intrinsics.checkNotNullParameter(propertyName, "propertyName");
        Intrinsics.checkNotNullParameter(propertyValue, "propertyValue");
        Iterable $this$first$iv = KClasses.getMemberProperties(Reflection.getOrCreateKotlinClass(instance.getClass()));
        for (Object element$iv : $this$first$iv) {
            KProperty1 it = (KProperty1) element$iv;
            if (Intrinsics.areEqual(it.getName(), propertyName)) {
                KMutableProperty kMutableProperty = (KProperty1) element$iv;
                if (kMutableProperty instanceof KMutableProperty) {
                    kMutableProperty.getSetter().call(new Object[]{instance, propertyValue});
                    return;
                }
                return;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    @NotNull
    public static final Book fillData(@NotNull Book $this$fillData, @NotNull Book newBook, @NotNull List<String> keys) {
        Intrinsics.checkNotNullParameter($this$fillData, "<this>");
        Intrinsics.checkNotNullParameter(newBook, "newBook");
        Intrinsics.checkNotNullParameter(keys, "keys");
        for (String key : keys) {
            String current = (String) readInstanceProperty($this$fillData, key);
            String str = current;
            if (str == null || str.length() == 0) {
                String cacheValue = (String) readInstanceProperty(newBook, key);
                String str2 = cacheValue;
                if (!(str2 == null || str2.length() == 0)) {
                    setInstanceProperty($this$fillData, key, cacheValue);
                }
            }
        }
        return $this$fillData;
    }

    @NotNull
    public static final String getRandomString(int length) {
        Iterable $this$map$iv = new IntRange(1, length);
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        IntIterator it = $this$map$iv.iterator();
        while (it.hasNext()) {
            it.nextInt();
            destination$iv$iv.add(Character.valueOf(StringsKt.random("ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789", Random.Default)));
        }
        return CollectionsKt.joinToString$default((List) destination$iv$iv, PackageDocumentBase.PREFIX_OPF, (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null);
    }

    @NotNull
    public static final String genEncryptedPassword(@NotNull String password, @NotNull String salt) {
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(salt, "salt");
        return MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(MD5Utils.INSTANCE.md5Encode(Intrinsics.stringPlus(password, salt)), salt)).toString();
    }

    public static /* synthetic */ String jsonEncode$default(Object obj, boolean z, int i, Object obj2) {
        if ((i & 2) != 0) {
            z = false;
        }
        return jsonEncode(obj, z);
    }

    @NotNull
    public static final String jsonEncode(@NotNull Object value, boolean pretty) {
        Intrinsics.checkNotNullParameter(value, "value");
        if (pretty) {
            String json = prettyGson.toJson(value);
            Intrinsics.checkNotNullExpressionValue(json, "prettyGson.toJson(value)");
            return json;
        }
        String json2 = gson.toJson(value);
        Intrinsics.checkNotNullExpressionValue(json2, "gson.toJson(value)");
        return json2;
    }

    @NotNull
    public static final List<File> deepListFiles(@NotNull File $this$deepListFiles, @Nullable String[] allowExtensions) {
        String strContentDeepToString;
        Intrinsics.checkNotNullParameter($this$deepListFiles, "<this>");
        ArrayList fileList = new ArrayList();
        Object[] objArrListFiles = $this$deepListFiles.listFiles();
        Intrinsics.checkNotNullExpressionValue(objArrListFiles, "this.listFiles()");
        Object[] $this$forEach$iv = objArrListFiles;
        for (Object element$iv : $this$forEach$iv) {
            File it = (File) element$iv;
            if (it.isDirectory()) {
                Intrinsics.checkNotNullExpressionValue(it, "it");
                fileList.addAll(deepListFiles(it, allowExtensions));
            } else {
                FileUtils fileUtils = FileUtils.INSTANCE;
                String name = it.getName();
                Intrinsics.checkNotNullExpressionValue(name, "it.name");
                String extension = fileUtils.getExtension(name);
                boolean z = (allowExtensions == null || (strContentDeepToString = ArraysKt.contentDeepToString(allowExtensions)) == null || !StringsKt.contains$default(strContentDeepToString, extension, false, 2, (Object) null)) ? false : true;
                if (z || allowExtensions == null) {
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
        return _licenseValid;
    }

    public static final void set_licenseValid(boolean z) {
        _licenseValid = z;
    }

    public static final void setLicenseValid(boolean isValid) {
        _licenseValid = isValid;
    }

    public static /* synthetic */ License getInstalledLicense$default(boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return getInstalledLicense(z);
    }

    @NotNull
    public static final License getInstalledLicense(boolean ignoreInvalid) throws IOException {
        String licenseKeyString = getStorage(new String[]{"data", "license"}, ".key");
        String str = licenseKeyString;
        if (str == null || str.length() == 0) {
            return new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
        }
        if (!ignoreInvalid && !_licenseValid) {
            return new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
        }
        License license = decryptToLicense(licenseKeyString);
        logger.info("license: {}", license);
        if (license == null || !license.getVerified()) {
            return new License(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
        }
        return license;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.htmake.reader.utils.ExtKt$decryptToLicense$lambda-19$$inlined$toDataClass$1] */
    @Nullable
    public static final License decryptToLicense(@NotNull String content) {
        String it;
        Intrinsics.checkNotNullParameter(content, "content");
        if ((content.length() == 0) || (it = decryptData(content)) == null) {
            return null;
        }
        Object map = toMap(it);
        String json$iv$iv = map instanceof String ? (String) map : getGson().toJson(map);
        License license = (License) getGson().fromJson(json$iv$iv, new TypeToken<License>() { // from class: com.htmake.reader.utils.ExtKt$decryptToLicense$lambda-19$$inlined$toDataClass$1
        }.getType());
        if (license == null) {
            return null;
        }
        return license;
    }

    @Nullable
    public static final String decryptData(@NotNull String content) throws InvalidKeySpecException {
        Intrinsics.checkNotNullParameter(content, "content");
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAj0G3qEPjVTvVd7pXFUVYZFHT8KaoG4onc5rLUKqFQ2DCh/5hFK9t2nKh2XB+C2Jp/GSK2ONwD7ceXenmA6uvr90uCK/gp6j62XFVRvc8sIm0d/bGbzZFJRk3HKtxEckBmASduPObY691DVVixxNtUrSJktx/TZaB42pUQk4j+7FuOVNNPra44hDdnyGhmYBBf2B4kjXVMjL+0NCblFIN1+qjmcol44k6NFKFF54q05bjR3CRyYdAnNTCOyt9va0oB6lDlKHplSZmAOH9JGMUki/HDJbABESXMnyIpux27w9SQ8aJStYttnJWHALO1hiFJsxbz5KUkldH6Ny1p/2W5QIDAQAB", 2)));
        EncoderUtils encoderUtils = EncoderUtils.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(publicKey, "publicKey");
        return EncoderUtils.decryptSegmentByPublicKey$default(encoderUtils, content, publicKey, 0, 4, null);
    }

    public static final boolean validateEmail(@NotNull String email) {
        Intrinsics.checkNotNullParameter(email, "email");
        Regex regex = new Regex("^[A-Za-z0-9._%+-]+@(163|126|qq|yahoo|sina|sohu|yeah|139|189|21cn|outlook|gmail|icloud).com$");
        return regex.matches(email);
    }

    public static final boolean sendEmail(@NotNull String toEmail, @NotNull String subject, @NotNull String body) {
        Intrinsics.checkNotNullParameter(toEmail, "toEmail");
        Intrinsics.checkNotNullParameter(subject, PackageDocumentBase.DCTags.subject);
        Intrinsics.checkNotNullParameter(body, NCXDocumentV3.XHTMLTgs.body);
        Function3 sendCommand = new Function3<OutputStreamWriter, BufferedReader, Pair<? extends String, ? extends Integer>, Boolean>() { // from class: com.htmake.reader.utils.ExtKt$sendEmail$sendCommand$1
            public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
                return Boolean.valueOf(invoke((OutputStreamWriter) p1, (BufferedReader) p2, (Pair<String, Integer>) p3));
            }

            public final boolean invoke(@NotNull OutputStreamWriter writer, @NotNull BufferedReader reader, @NotNull Pair<String, Integer> command) throws IOException {
                Intrinsics.checkNotNullParameter(writer, "writer");
                Intrinsics.checkNotNullParameter(reader, "reader");
                Intrinsics.checkNotNullParameter(command, "command");
                String cmd = (String) command.getFirst();
                int code = ((Number) command.getSecond()).intValue();
                KLogger logger2 = ExtKt.getLogger();
                if (cmd == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
                }
                logger2.debug("Send command {}, expect code {}", StringsKt.trim(cmd).toString(), Integer.valueOf(code));
                writer.write(String.valueOf(cmd));
                writer.flush();
                String response = reader.readLine();
                ExtKt.getLogger().debug("Response {}", response);
                String str = response;
                if (!(str == null || str.length() == 0)) {
                    Intrinsics.checkNotNullExpressionValue(response, "response");
                    if (!StringsKt.startsWith$default(response, Intrinsics.stringPlus(PackageDocumentBase.PREFIX_OPF, Integer.valueOf(code)), false, 2, (Object) null)) {
                        ExtKt.getLogger().error("Error response from SMTP server.");
                        return false;
                    }
                    return true;
                }
                ExtKt.getLogger().error("SMTP server no response.");
                return false;
            }
        };
        try {
            SocketFactory sslSocketFactory = SSLSocketFactory.getDefault();
            Socket socket = sslSocketFactory.createSocket("smtp.qiye.aliyun.com", 465);
            OutputStream outputStream = socket.getOutputStream();
            Intrinsics.checkNotNullExpressionValue(outputStream, "socket.getOutputStream()");
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            InputStream inputStream = socket.getInputStream();
            Intrinsics.checkNotNullExpressionValue(inputStream, "socket.getInputStream()");
            Reader inputStreamReader = new InputStreamReader(inputStream, Charsets.UTF_8);
            BufferedReader reader = inputStreamReader instanceof BufferedReader ? (BufferedReader) inputStreamReader : new BufferedReader(inputStreamReader, IOUtil.DEFAULT_BUFFER_SIZE);
            String response = reader.readLine();
            Intrinsics.checkNotNullExpressionValue(response, "response");
            if (!StringsKt.startsWith$default(response, "220", false, 2, (Object) null)) {
                logger.error("Error connecting to the SMTP server.");
                return false;
            }
            List<Pair<String, Integer>> command = getCommand(CollectionsKt.arrayListOf(new String[]{toEmail}), subject, body);
            boolean res = false;
            int i = 0;
            int size = command.size();
            if (0 < size) {
                do {
                    int i2 = i;
                    i++;
                    res = ((Boolean) sendCommand.invoke(writer, reader, command.get(i2))).booleanValue();
                    if (!res) {
                        break;
                    }
                } while (i < size);
            }
            writer.close();
            reader.close();
            socket.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @NotNull
    public static final List<Pair<String, Integer>> getCommand(@NotNull List<String> to, @NotNull String subject, @NotNull String body) {
        Intrinsics.checkNotNullParameter(to, "to");
        Intrinsics.checkNotNullParameter(subject, PackageDocumentBase.DCTags.subject);
        Intrinsics.checkNotNullParameter(body, NCXDocumentV3.XHTMLTgs.body);
        String separator = "----=_Part_" + System.currentTimeMillis() + UUID.randomUUID();
        List<Pair<String, Integer>> listMutableListOf = CollectionsKt.mutableListOf(new Pair[]{new Pair("HELO sendmail\r\n", 250)});
        if (!("no-reply@onmy.top".length() == 0)) {
            listMutableListOf.add(new Pair<>("AUTH LOGIN\r\n", 334));
            listMutableListOf.add(new Pair<>(Intrinsics.stringPlus(encodeBase64("no-reply@onmy.top"), "\r\n"), 334));
            listMutableListOf.add(new Pair<>(Intrinsics.stringPlus(encodeBase64("no-reply@1."), "\r\n"), 235));
        }
        listMutableListOf.add(new Pair<>("MAIL FROM: <no-reply@onmy.top>\r\n", 250));
        String header = "FROM: Reader<no-reply@onmy.top>\r\n";
        if (!(to.isEmpty())) {
            int count = to.size();
            if (count == 1) {
                listMutableListOf.add(new Pair<>("RCPT TO: <" + to.get(0) + ">\r\n", 250));
                header = header + "TO: <" + to.get(0) + ">\r\n";
            } else {
                int i = 0;
                if (0 < count) {
                    do {
                        int i2 = i;
                        i++;
                        listMutableListOf.add(new Pair<>("RCPT TO: <" + to.get(i2) + ">\r\n", 250));
                        if (i2 == 0) {
                            header = header + "TO: <" + to.get(i2) + '>';
                        } else if (i2 + 1 == count) {
                            header = header + ",<" + to.get(i2) + ">\r\n";
                        } else {
                            header = header + ",<" + to.get(i2) + '>';
                        }
                    } while (i < count);
                }
            }
        }
        String header2 = Intrinsics.stringPlus((Intrinsics.stringPlus(Intrinsics.stringPlus(Intrinsics.stringPlus(Intrinsics.stringPlus(header + "Subject: =?UTF-8?B?" + encodeBase64(subject) + "?=\r\n", "Content-Type: multipart/alternative;\r\n") + "\tboundary=\"" + separator + '\"', "\r\nMIME-Version: 1.0\r\n") + "\r\n--" + separator + "\r\n", "Content-Type:text/html; charset=utf-8\r\n"), "Content-Transfer-Encoding: base64\r\n\r\n") + encodeBase64(body) + "\r\n") + "--" + separator + "\r\n", "\r\n.\r\n");
        listMutableListOf.add(new Pair<>("DATA\r\n", 354));
        listMutableListOf.add(new Pair<>(header2, 250));
        listMutableListOf.add(new Pair<>("QUIT\r\n", 221));
        return listMutableListOf;
    }

    @NotNull
    public static final String encodeBase64(@NotNull String text) {
        Intrinsics.checkNotNullParameter(text, NCXDocumentV2.NCXTags.text);
        Base64.Encoder encoder = java.util.Base64.getEncoder();
        byte[] bytes = text.getBytes(Charsets.UTF_8);
        Intrinsics.checkNotNullExpressionValue(bytes, "(this as java.lang.String).getBytes(charset)");
        String strEncodeToString = encoder.encodeToString(bytes);
        Intrinsics.checkNotNullExpressionValue(strEncodeToString, "getEncoder().encodeToString(text.toByteArray())");
        return strEncodeToString;
    }
}
