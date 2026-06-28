package com.htmake.reader.api.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htmake.reader.api.ReturnData;
import com.htmake.reader.entity.User;
import com.htmake.reader.lib.tts.constant.TtsStyleEnum;
import com.htmake.reader.lib.tts.constant.VoiceEnum;
import com.htmake.reader.lib.tts.model.SSML;
import com.htmake.reader.lib.tts.service.TTSService;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.MongoManager;
import com.htmake.reader.utils.SpringContextUtils;
import com.htmake.reader.utils.UserMutex;
import com.htmake.reader.utils.VertExtKt;
import com.script.ScriptException;
import io.legado.app.constant.AppConst;
import io.legado.app.constant.AppPattern;
import io.legado.app.constant.BookType;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BaseSource;
import io.legado.app.data.entities.Book;
import io.legado.app.data.entities.BookChapter;
import io.legado.app.data.entities.BookSource;
import io.legado.app.data.entities.HttpTTS;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.data.entities.SearchResult;
import io.legado.app.data.entities.TxtTocRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.exception.NoStackTraceException;
import io.legado.app.exception.TocEmptyException;
import io.legado.app.help.BookHelp;
import io.legado.app.help.DefaultData;
import io.legado.app.model.Debug;
import io.legado.app.model.DebugLog;
import io.legado.app.model.Debugger;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.model.webBook.WebBook;
import io.legado.app.utils.ACache;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.HtmlFormatter;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.utils.ParameterizedTypeImpl;
import io.legado.app.utils.ZipUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.kotlin.coroutines.VertxCoroutineKt;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import javax.imageio.ImageIO;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.Triple;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.comparisons.ComparisonsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.io.ByteStreamsKt;
import kotlin.io.CloseableKt;
import kotlin.io.FilesKt;
import kotlin.io.TextStreamsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.AdaptedFunctionReference;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.TypeIntrinsics;
import kotlin.text.Charsets;
import kotlin.text.Regex;
import kotlin.text.RegexOption;
import kotlin.text.StringsKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineExceptionHandler;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexKt;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.FileResourceProvider;
import me.ag2s.epublib.domain.LazyResource;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.EpubWriter;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.NCXDocumentV3;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.ResourceUtil;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.kdom.Node;
import org.mozilla.javascript.WrappedException;

/* JADX INFO: compiled from: BookController.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u008a\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010#\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J,\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00072\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u0010\u001f\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J!\u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010#J\u0019\u0010 \u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u0010$\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J \u0010%\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u000f2\b\b\u0002\u0010)\u001a\u00020*J\u0018\u0010+\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J/\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u00072\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0007H\u0086@ø\u0001\u0000¢\u0006\u0002\u00100J\u0019\u00101\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u00102\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u00103\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J7\u00104\u001a\u0004\u0018\u00010'2\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020'\u0012\u0004\u0012\u00020'06H\u0086@ø\u0001\u0000¢\u0006\u0002\u00107J\u0019\u00108\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u00109\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J3\u0010:\u001a\u00020-2\u0006\u0010;\u001a\u00020-2\u0006\u0010&\u001a\u00020'2\b\u0010<\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010=J1\u0010>\u001a\u00020-2\u0006\u0010;\u001a\u00020-2\u0006\u0010?\u001a\u00020'2\u0006\u0010<\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010=J\u0018\u0010@\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J\u0018\u0010A\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J(\u0010B\u001a\u00020\u00072\u0006\u0010C\u001a\u00020D2\u0006\u0010&\u001a\u00020'2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020GH\u0002J\u0099\u0001\u0010H\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010I\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u00072n\u0010J\u001aj\u0012\u0013\u0012\u00110\u0007¢\u0006\f\bL\u0012\b\bM\u0012\u0004\b\b(N\u0012K\u0012I\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070P\u0018\u00010Oj\u001c\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070P\u0018\u0001`Q¢\u0006\f\bL\u0012\b\bM\u0012\u0004\b\b(R\u0012\u0004\u0012\u00020\u00180KH\u0082@ø\u0001\u0000¢\u0006\u0002\u0010SJ\u0019\u0010T\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0010\u0010U\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010V\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u0010W\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u0010X\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J)\u0010Y\u001a\b\u0012\u0004\u0012\u00020'0Z2\b\b\u0002\u0010[\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\\J/\u0010]\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0019\u001a\u00020\u00072\b\b\u0002\u0010^\u001a\u00020*H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010_J\u0018\u0010`\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010a\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001c\u0010b\u001a\b\u0012\u0004\u0012\u00020\u000f0c2\u0006\u0010?\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007J\u0016\u0010d\u001a\u00020-2\u0006\u0010?\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0019\u0010f\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0018\u0010g\u001a\u0004\u0018\u00010h2\u0006\u0010M\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0010\u0010i\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010j\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001b\u0010k\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010lJQ\u0010m\u001a\b\u0012\u0004\u0012\u00020G0Z2\u0006\u0010&\u001a\u00020'2\b\u0010<\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010[\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00072\b\b\u0002\u0010n\u001a\u00020*2\n\b\u0002\u0010o\u001a\u0004\u0018\u00010pH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010qJ,\u0010r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070s2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010t\u001a\u00020\u000f2\u0006\u0010u\u001a\u00020\u0007H\u0002J\u0019\u0010v\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u0018\u0010w\u001a\u0004\u0018\u00010'2\u0006\u0010x\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010y\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J+\u0010z\u001a\u0004\u0018\u00010{2\u0006\u0010|\u001a\u00020h2\u0006\u0010}\u001a\u00020\u00072\u0006\u0010~\u001a\u00020\u000fH\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u007fJ\u001a\u0010\u0080\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0081\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0082\u0001\u001a\u00020*2\u0007\u0010<\u001a\u00030\u0083\u00012\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u001b\u0010\u0084\u0001\u001a\u00020'2\u0006\u0010&\u001a\u00020'H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u0085\u0001J\u001a\u0010\u0086\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0087\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0088\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u0089\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u008a\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010\u008b\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J/\u0010\u008c\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u008d\u0001J\u001a\u0010\u008e\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J(\u0010\u008f\u0001\u001a\b\u0012\u0004\u0012\u00020'0Z2\r\u0010\u0090\u0001\u001a\b\u0012\u0004\u0012\u00020'0ZH\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u0091\u0001J\u001a\u0010\u0092\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J,\u0010\u0093\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0007\u0010\u0094\u0001\u001a\u00020G2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u0095\u0001J2\u0010\u0096\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u000e\u0010\u0097\u0001\u001a\t\u0012\u0005\u0012\u00030\u0098\u00010Z2\u0006\u0010\u001d\u001a\u00020\u00072\t\b\u0002\u0010\u0099\u0001\u001a\u00020*J.\u0010\u009a\u0001\u001a\u0010\u0012\u0004\u0012\u00020'\u0012\u0006\u0012\u0004\u0018\u00010\u00070s2\u0007\u0010\u009b\u0001\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0015J#\u0010\u009c\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@ø\u0001\u0000¢\u0006\u0003\u0010\u009d\u0001J?\u0010\u009e\u0001\u001a\u00020\u00182\b\u0010\u009f\u0001\u001a\u00030 \u00012\b\u0010¡\u0001\u001a\u00030¢\u00012\u0006\u0010(\u001a\u00020\u000f2\b\u0010£\u0001\u001a\u00030¤\u00012\u0007\u0010¥\u0001\u001a\u00020\u00072\u0007\u0010¦\u0001\u001a\u00020-J>\u0010§\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\r\u0010¨\u0001\u001a\b\u0012\u0004\u0012\u00020G0Z2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010o\u001a\u0004\u0018\u00010pH\u0086@ø\u0001\u0000¢\u0006\u0003\u0010©\u0001J,\u0010ª\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0007\u0010\u0094\u0001\u001a\u00020G2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u0095\u0001J'\u0010«\u0001\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010¬\u0001J\u001a\u0010\u00ad\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010®\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010¯\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010°\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010±\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J\u001a\u0010²\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016JJ\u0010³\u0001\u001a\u0014\u0012\u0005\u0012\u00030\u0098\u00010Oj\t\u0012\u0005\u0012\u00030\u0098\u0001`Q2\u0006\u0010I\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\t\b\u0002\u0010´\u0001\u001a\u00020*2\b\b\u0002\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010µ\u0001J2\u0010¶\u0001\u001a\t\u0012\u0005\u0012\u00030·\u00010Z2\u0006\u0010&\u001a\u00020'2\u0006\u0010F\u001a\u00020G2\u0006\u0010u\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010\u0095\u0001J+\u0010¸\u0001\u001a\b\u0012\u0004\u0012\u00020\u000f0Z2\u0007\u0010¹\u0001\u001a\u00020\u00072\u0007\u0010º\u0001\u001a\u00020\u0007H\u0082@ø\u0001\u0000¢\u0006\u0003\u0010¬\u0001J\u0019\u0010»\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020DH\u0002J\u001a\u0010¼\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016J-\u0010½\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020D2\b\u0010I\u001a\u0004\u0018\u00010\u0007H\u0082@ø\u0001\u0000¢\u0006\u0003\u0010¾\u0001J>\u0010¿\u0001\u001a\u00020\u00182\u0007\u0010À\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020D2\b\u0010I\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@ø\u0001\u0000¢\u0006\u0003\u0010Á\u0001J\u0019\u0010Â\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020DH\u0002J$\u0010Ã\u0001\u001a\u00020\u00182\u0007\u0010Ä\u0001\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010Å\u0001J$\u0010Æ\u0001\u001a\u00020*2\u0007\u0010Ç\u0001\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@ø\u0001\u0000¢\u0006\u0003\u0010¬\u0001J\u001a\u0010È\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0016JF\u0010É\u0001\u001a\u00020\u00182\b\u0010Ê\u0001\u001a\u00030Ë\u00012\u0006\u0010N\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u00072\u0017\b\u0002\u0010Ì\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@ø\u0001\u0000¢\u0006\u0003\u0010Í\u0001J>\u0010Î\u0001\u001a\u00020\u00182\b\u0010Ê\u0001\u001a\u00030Ë\u00012\u0006\u0010N\u001a\u00020\u00072\u0017\b\u0002\u0010Ì\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@ø\u0001\u0000¢\u0006\u0003\u0010Ï\u0001J>\u0010Ð\u0001\u001a\u00020\u00182\b\u0010Ê\u0001\u001a\u00030Ë\u00012\u0006\u0010N\u001a\u00020\u00072\u0017\b\u0002\u0010Ì\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@ø\u0001\u0000¢\u0006\u0003\u0010Ï\u0001J!\u0010Ñ\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010F\u001a\u00020G2\u0006\u0010E\u001a\u00020\u0007H\u0002R!\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082D¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006Ò\u0001"}, d2 = {"Lcom/htmake/reader/api/controller/BookController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "backupFileNames", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "getBackupFileNames", "()[Ljava/lang/String;", "backupFileNames$delegate", "Lkotlin/Lazy;", "bookInfoCache", "Lio/legado/app/utils/ACache;", "concurrentLoopCount", PackageDocumentBase.PREFIX_OPF, "webClient", "Lio/vertx/ext/web/client/WebClient;", "addBookGroupMulti", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addInvalidBookSource", PackageDocumentBase.PREFIX_OPF, "sourceUrl", "invalidInfo", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "userNameSpace", "backupToMongodb", "bookSourceDebugSSE", "cacheBookOnServer", "bookUrlList", "Lio/vertx/core/json/JsonArray;", "(Lio/vertx/core/json/JsonArray;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cacheBookSSE", "convertPdfPageToImage", "book", "Lio/legado/app/data/entities/Book;", "index", "force", PackageDocumentBase.PREFIX_OPF, "convertPdfToImage", "createUserBackup", "Ljava/io/File;", "backupDir", "latestZipFilePath", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBook", "deleteBookCache", "deleteBooks", "editShelfBook", "handler", "Lkotlin/Function1;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exploreBook", "exportBook", "exportToEpub", "exportDir", "bookSource", "(Ljava/io/File;Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToTxt", "bookInfo", "extractCbz", "extractEpub", "fixPic", "epubBook", "Lme/ag2s/epublib/domain/EpubBook;", "content", NCXDocumentV2.NCXAttributeValues.chapter, "Lio/legado/app/data/entities/BookChapter;", "getAllContents", "bookSourceString", "append", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", NCXDocumentV2.NCXTags.text, "Ljava/util/ArrayList;", "Lkotlin/Triple;", "Lkotlin/collections/ArrayList;", "srcList", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAvailableBookSource", "getBookChaptersCache", "getBookContent", "getBookCover", "getBookInfo", "getBookShelfBooks", PackageDocumentBase.PREFIX_OPF, "refresh", "(ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookSourceString", "withExploreUrl", "(Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookSourceStringBySourceURLOpt", "getBookshelf", "getCachedChapterContentSet", PackageDocumentBase.PREFIX_OPF, "getChapterCacheDir", "getChapterList", "getChapterListByRule", "getHttpTTSByName", "Lio/legado/app/data/entities/HttpTTS;", "getInvalidBookSourceCache", "getInvalidBookSources", "getLastBackFileFromWebdav", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLocalChapterList", "debugLog", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getResultAndQueryIndex", "Lkotlin/Pair;", "queryIndexInContent", "query", "getShelfBook", "getShelfBookByURL", RSSKeywords.RSS_ITEM_URL, "getShelfBookWithCacheInfo", "getSpeakStream", "Ljava/io/InputStream;", "httpTts", "speakText", "speechRate", "(Lio/legado/app/data/entities/HttpTTS;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTxtTocRules", "importBookPreview", "isInvalidBookSource", "Lio/legado/app/data/entities/BookSource;", "mergeBookCacheInfo", "(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshLocalBook", "removeBookGroupMulti", "restoreFromMongodb", "saveBook", "saveBookConfig", "saveBookContent", "saveBookCover", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookGroupId", "saveBookInfoCache", "bookList", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookProgress", "saveBookProgressToWebdav", "bookChapter", "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookSources", "sourceList", "Lio/legado/app/data/entities/SearchBook;", "replace", "saveBookToShelf", "_book", "saveLocalBookCover", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "savePdfPageToImage", "document", "Lorg/apache/pdfbox/pdmodel/PDDocument;", "renderer", "Lorg/apache/pdfbox/rendering/PDFRenderer;", "targetWidth", PackageDocumentBase.PREFIX_OPF, "imageFormat", "output", "saveShelfBookLatestChapter", "bookChapterList", "(Lio/legado/app/data/entities/Book;Ljava/util/List;Ljava/lang/String;Lkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveShelfBookProgress", "saveToWebdav", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchBook", "searchBookContent", "searchBookMulti", "searchBookMultiSSE", "searchBookSource", "searchBookSourceSSE", "searchBookWithSource", "accurate", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchChapter", "Lio/legado/app/data/entities/SearchResult;", "searchPosition", "mContent", "pattern", "setAssets", "setBookSource", "setCover", "(Lio/legado/app/data/entities/Book;Lme/ag2s/epublib/domain/EpubBook;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEpubContent", "contentModel", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;Lme/ag2s/epublib/domain/EpubBook;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEpubMetadata", "syncBookProgressFromWebdav", "progressFilePath", "(Ljava/lang/Object;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncFromWebdav", "zipFilePath", "textToSpeech", "ttsByApi", "response", "Lio/vertx/core/http/HttpServerResponse;", "options", "(Lio/vertx/core/http/HttpServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ttsByEdge", "(Lio/vertx/core/http/HttpServerResponse;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ttsByTextToSpeechCn", "updateImageLinkInContent", "reader-pro"})
public final class BookController extends BaseController {

    @NotNull
    private ACache bookInfoCache;
    private final int concurrentLoopCount;

    @NotNull
    private WebClient webClient;

    /* JADX INFO: renamed from: backupFileNames$delegate, reason: from kotlin metadata */
    @NotNull
    private final Lazy backupFileNames;

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$addBookGroupMulti$1, reason: invalid class name */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$addBookGroupMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1792, 1803}, i = {0, 0, 0, 1, 1}, s = {"L$0", "L$1", "L$2", "L$2", "L$3"}, n = {"this", "context", "returnData", "userNameSpace", "bookJsonArray"}, m = "addBookGroupMulti", c = "com.htmake.reader.api.controller.BookController")
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        long J$0;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        AnonymousClass1(Continuation<? super AnonymousClass1> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.addBookGroupMulti(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$backupToMongodb$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$backupToMongodb$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3700}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "backupToMongodb", c = "com.htmake.reader.api.controller.BookController")
    static final class C00121 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00121(Continuation<? super C00121> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.backupToMongodb(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$bookSourceDebugSSE$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$bookSourceDebugSSE$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2617, 2656}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"this", "context", "returnData", "response"}, m = "bookSourceDebugSSE", c = "com.htmake.reader.api.controller.BookController")
    static final class C00131 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C00131(Continuation<? super C00131> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.bookSourceDebugSSE(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$cacheBookOnServer$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookOnServer$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2785}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "cacheBookOnServer", c = "com.htmake.reader.api.controller.BookController")
    static final class C00141 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00141(Continuation<? super C00141> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.cacheBookOnServer(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$cacheBookOnServer$3, reason: invalid class name */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookOnServer$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2823, 2836, 2840}, i = {1, 1, 1, 1, 1}, s = {"L$5", "L$6", "L$7", "L$8", "I$4"}, n = {"chapterList", "cachedChapterContentSet", "localCacheDir", "chapterInfo", "chapterIndex"}, m = "cacheBookOnServer", c = "com.htmake.reader.api.controller.BookController")
    static final class AnonymousClass3 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        Object L$8;
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        int I$4;
        /* synthetic */ Object result;
        int label;

        AnonymousClass3(Continuation<? super AnonymousClass3> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.cacheBookOnServer(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$cacheBookSSE$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookSSE$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2669, 2706, 2713, 2731}, i = {0, 0, 0, 0, 1, 2, 3, 3}, s = {"L$0", "L$1", "L$2", "L$3", "L$6", "L$6", "L$2", "L$3"}, n = {"this", "context", "returnData", "response", "bookSource", "chapterList", "successCount", "failedCount"}, m = "cacheBookSSE", c = "com.htmake.reader.api.controller.BookController")
    static final class C00161 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        Object L$7;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        C00161(Continuation<? super C00161> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.cacheBookSSE(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$deleteBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$deleteBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1838}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00181 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00181(Continuation<? super C00181> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.deleteBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$deleteBookCache$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$deleteBookCache$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2859}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBookCache", c = "com.htmake.reader.api.controller.BookController")
    static final class C00191 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00191(Continuation<? super C00191> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.deleteBookCache(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$deleteBooks$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$deleteBooks$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1888}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "deleteBooks", c = "com.htmake.reader.api.controller.BookController")
    static final class C00201 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00201(Continuation<? super C00201> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.deleteBooks(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$editShelfBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$editShelfBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2236, 2239}, i = {0, 0, 0, 0, 1, 1, 1, 1, 1}, s = {"L$0", "L$1", "L$2", "L$3", "L$0", "L$1", "L$2", "L$3", "L$4"}, n = {"this", "book", "userNameSpace", "handler", "this", "book", "userNameSpace", "handler", "mutex"}, m = "editShelfBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00211 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00211(Continuation<? super C00211> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.editShelfBook(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$exploreBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$exploreBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {727, 728, 745}, i = {0, 0, 0, 1, 1, 1}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2"}, n = {"this", "context", "returnData", "this", "context", "returnData"}, m = "exploreBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00221 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00221(Continuation<? super C00221> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.exploreBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$exportBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$exportBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3201, 3242, 3250, 3252}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "exportBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00231 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int I$0;
        /* synthetic */ Object result;
        int label;

        C00231(Continuation<? super C00231> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.exportBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$exportToEpub$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$exportToEpub$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3356, 3361}, i = {0, 0, 0, 0, 0, 0, 1, 1}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "L$0", "L$1"}, n = {"this", "book", "bookSource", "userNameSpace", "bookFile", "epubBook", "bookFile", "epubBook"}, m = "exportToEpub", c = "com.htmake.reader.api.controller.BookController")
    static final class C00241 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        /* synthetic */ Object result;
        int label;

        C00241(Continuation<? super C00241> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.exportToEpub(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$exportToTxt$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$exportToTxt$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3264}, i = {0}, s = {"L$0"}, n = {"bookFile"}, m = "exportToTxt", c = "com.htmake.reader.api.controller.BookController")
    static final class C00251 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C00251(Continuation<? super C00251> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.exportToTxt(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getAllContents$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getAllContents$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3298}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"this", "book", "userNameSpace", "append"}, m = "getAllContents", c = "com.htmake.reader.api.controller.BookController")
    static final class C00271 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C00271(Continuation<? super C00271> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getAllContents(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getAvailableBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getAvailableBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1280, 1314}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getAvailableBookSource", c = "com.htmake.reader.api.controller.BookController")
    static final class C00281 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00281(Continuation<? super C00281> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getAvailableBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {456, 485, 502, 512, 512, 513, 518, 520, 632, 636}, i = {0, 0, 0, 6, 7}, s = {"L$0", "L$1", "L$2", "L$6", "L$6"}, n = {"this", "context", "returnData", "chapterList", "chapterList"}, m = "getBookContent", c = "com.htmake.reader.api.controller.BookController")
    static final class C00311 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        int I$4;
        /* synthetic */ Object result;
        int label;

        C00311(Continuation<? super C00311> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getBookContent(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookInfo$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookInfo$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {147, 156, 158, 163, 163, 167}, i = {0}, s = {"L$4"}, n = {"userNameSpace"}, m = "getBookInfo", c = "com.htmake.reader.api.controller.BookController")
    static final class C00331 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00331(Continuation<? super C00331> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getBookInfo(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookShelfBooks$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookShelfBooks$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1954}, i = {}, s = {}, n = {}, m = "getBookShelfBooks", c = "com.htmake.reader.api.controller.BookController")
    static final class C00341 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C00341(Continuation<? super C00341> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getBookShelfBooks(false, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookshelf$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookshelf$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1345, 1356}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getBookshelf", c = "com.htmake.reader.api.controller.BookController")
    static final class C00371 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00371(Continuation<? super C00371> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getBookshelf(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getChapterList$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getChapterList$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {353, 379, 382, 387, 387, 389, 391, 407}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getChapterList", c = "com.htmake.reader.api.controller.BookController")
    static final class C00381 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int I$0;
        /* synthetic */ Object result;
        int label;

        C00381(Continuation<? super C00381> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getChapterList(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getChapterListByRule$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getChapterListByRule$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {302}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getChapterListByRule", c = "com.htmake.reader.api.controller.BookController")
    static final class C00391 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00391(Continuation<? super C00391> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getChapterListByRule(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getInvalidBookSources$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getInvalidBookSources$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {112}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getInvalidBookSources", c = "com.htmake.reader.api.controller.BookController")
    static final class C00401 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00401(Continuation<? super C00401> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getInvalidBookSources(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getLocalChapterList$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getLocalChapterList$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2019, 2022, 2033, 2035, 2051}, i = {}, s = {}, n = {}, m = "getLocalChapterList", c = "com.htmake.reader.api.controller.BookController")
    static final class C00411 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        boolean Z$0;
        /* synthetic */ Object result;
        int label;

        C00411(Continuation<? super C00411> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getLocalChapterList(null, null, false, null, false, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getShelfBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getShelfBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1362}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getShelfBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00431 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00431(Continuation<? super C00431> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getShelfBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getShelfBookWithCacheInfo$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getShelfBookWithCacheInfo$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3179, 3183}, i = {0, 0, 0, 1}, s = {"L$0", "L$1", "L$2", "L$2"}, n = {"this", "context", "returnData", "userNameSpace"}, m = "getShelfBookWithCacheInfo", c = "com.htmake.reader.api.controller.BookController")
    static final class C00441 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00441(Continuation<? super C00441> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getShelfBookWithCacheInfo(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getSpeakStream$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getSpeakStream$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3048}, i = {0, 0, 0, 0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$5", "I$0"}, n = {"this", "httpTts", "speakText", "downloadErrorNo", "analyzeUrl", "response", "speechRate"}, m = "getSpeakStream", c = "com.htmake.reader.api.controller.BookController")
    static final class C00451 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        int I$0;
        /* synthetic */ Object result;
        int label;

        C00451(Continuation<? super C00451> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getSpeakStream(null, null, 0, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getTxtTocRules$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getTxtTocRules$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {285}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "getTxtTocRules", c = "com.htmake.reader.api.controller.BookController")
    static final class C00461 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00461(Continuation<? super C00461> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.getTxtTocRules(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$importBookPreview$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$importBookPreview$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {224}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "importBookPreview", c = "com.htmake.reader.api.controller.BookController")
    static final class C00471 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00471(Continuation<? super C00471> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.importBookPreview(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$refreshLocalBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$refreshLocalBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {320, 342}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "refreshLocalBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00481 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00481(Continuation<? super C00481> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.refreshLocalBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$removeBookGroupMulti$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$removeBookGroupMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1815, 1826}, i = {0, 0, 0, 1, 1}, s = {"L$0", "L$1", "L$2", "L$2", "L$3"}, n = {"this", "context", "returnData", "userNameSpace", "bookJsonArray"}, m = "removeBookGroupMulti", c = "com.htmake.reader.api.controller.BookController")
    static final class C00501 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        long J$0;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        C00501(Continuation<? super C00501> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.removeBookGroupMulti(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$restoreFromMongodb$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$restoreFromMongodb$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3748}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "restoreFromMongodb", c = "com.htmake.reader.api.controller.BookController")
    static final class C00521 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00521(Continuation<? super C00521> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.restoreFromMongodb(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1386, 1399, 1400, 1404, 1407}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00531 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        /* synthetic */ Object result;
        int label;

        C00531(Continuation<? super C00531> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBookConfig$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBookConfig$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1715, 1742}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookConfig", c = "com.htmake.reader.api.controller.BookController")
    static final class C00541 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00541(Continuation<? super C00541> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBookConfig(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBookContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBookContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {665}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookContent", c = "com.htmake.reader.api.controller.BookController")
    static final class C00551 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00551(Continuation<? super C00551> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBookContent(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBookCover$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBookCover$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1610}, i = {}, s = {}, n = {}, m = "saveBookCover", c = "com.htmake.reader.api.controller.BookController")
    static final class C00561 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00561(Continuation<? super C00561> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBookCover(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBookGroupId$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBookGroupId$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1753, 1780}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "saveBookGroupId", c = "com.htmake.reader.api.controller.BookController")
    static final class C00571 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00571(Continuation<? super C00571> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBookGroupId(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveBookProgress$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveBookProgress$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {414, 442, 448, 450}, i = {0, 0, 0, 2}, s = {"L$0", "L$1", "L$2", "L$4"}, n = {"this", "context", "returnData", "chapterInfo"}, m = "saveBookProgress", c = "com.htmake.reader.api.controller.BookController")
    static final class C00591 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int I$0;
        /* synthetic */ Object result;
        int label;

        C00591(Continuation<? super C00591> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveBookProgress(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveLocalBookCover$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveLocalBookCover$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1583}, i = {}, s = {}, n = {}, m = "saveLocalBookCover", c = "com.htmake.reader.api.controller.BookController")
    static final class C00601 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00601(Continuation<? super C00601> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveLocalBookCover(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveShelfBookLatestChapter$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveShelfBookLatestChapter$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2206, 2207}, i = {0, 0, 0, 0, 0, 1}, s = {"L$0", "L$1", "L$2", "L$3", "L$4", "L$0"}, n = {"this", "book", "bookChapterList", "userNameSpace", "mutex", "mutex"}, m = "saveShelfBookLatestChapter", c = "com.htmake.reader.api.controller.BookController")
    static final class C00611 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00611(Continuation<? super C00611> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveShelfBookLatestChapter(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$saveToWebdav$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$saveToWebdav$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2528, 2539}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3"}, n = {"this", "userNameSpace", "userHome", "legadoHome"}, m = "saveToWebdav", c = "com.htmake.reader.api.controller.BookController")
    static final class C00641 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C00641(Continuation<? super C00641> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.saveToWebdav(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBook$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBook$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {752, 753, 773}, i = {0, 0, 0, 1, 1, 1}, s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2"}, n = {"this", "context", "returnData", "this", "context", "returnData"}, m = "searchBook", c = "com.htmake.reader.api.controller.BookController")
    static final class C00651 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00651(Continuation<? super C00651> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBook(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3551, 3585, 3591, 3610}, i = {0, 0, 0, 3}, s = {"L$0", "L$1", "L$2", "L$6"}, n = {"this", "context", "returnData", "resultList"}, m = "searchBookContent", c = "com.htmake.reader.api.controller.BookController")
    static final class C00661 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        int I$0;
        int I$1;
        int I$2;
        int I$3;
        /* synthetic */ Object result;
        int label;

        C00661(Continuation<? super C00661> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookContent(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookMulti$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookMulti$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {779, 840}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "searchBookMulti", c = "com.htmake.reader.api.controller.BookController")
    static final class C00671 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00671(Continuation<? super C00671> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookMulti(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookMultiSSE$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookMultiSSE$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {887, 959}, i = {0, 0, 0, 0, 1}, s = {"L$0", "L$1", "L$2", "L$3", "L$2"}, n = {"this", "context", "returnData", "response", "maxSize"}, m = "searchBookMultiSSE", c = "com.htmake.reader.api.controller.BookController")
    static final class C00701 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        /* synthetic */ Object result;
        int label;

        C00701(Continuation<? super C00701> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookMultiSSE(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1007, 1071}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "searchBookSource", c = "com.htmake.reader.api.controller.BookController")
    static final class C00731 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        /* synthetic */ Object result;
        int label;

        C00731(Continuation<? super C00731> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookSourceSSE$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookSourceSSE$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1113, 1196}, i = {0, 0, 0, 0, 1}, s = {"L$0", "L$1", "L$2", "L$3", "L$6"}, n = {"this", "context", "returnData", "response", "maxSize"}, m = "searchBookSourceSSE", c = "com.htmake.reader.api.controller.BookController")
    static final class C00761 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        /* synthetic */ Object result;
        int label;

        C00761(Continuation<? super C00761> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookSourceSSE(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookWithSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookWithSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1247}, i = {}, s = {}, n = {}, m = "searchBookWithSource", c = "com.htmake.reader.api.controller.BookController")
    static final class C00791 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C00791(Continuation<? super C00791> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchBookWithSource(null, null, false, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchChapter$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchChapter$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3639}, i = {0, 0}, s = {"L$3", "L$4"}, n = {"searchResultsWithinChapter", "chapterContent"}, m = "searchChapter", c = "com.htmake.reader.api.controller.BookController")
    static final class C00811 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00811(Continuation<? super C00811> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.searchChapter(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$setBookSource$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$setBookSource$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {1624, 1684, 1687, 1706}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "returnData"}, m = "setBookSource", c = "com.htmake.reader.api.controller.BookController")
    static final class C00821 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        Object L$6;
        /* synthetic */ Object result;
        int label;

        C00821(Continuation<? super C00821> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.setBookSource(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$setCover$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$setCover$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3436}, i = {}, s = {}, n = {}, m = "setCover", c = "com.htmake.reader.api.controller.BookController")
    static final class C00841 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C00841(Continuation<? super C00841> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.setCover(null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$setEpubContent$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$setEpubContent$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3462}, i = {0, 0, 0, 0, 0}, s = {"L$0", "L$1", "L$2", "L$3", "L$4"}, n = {"this", "contentModel", "book", "epubBook", "userNameSpace"}, m = "setEpubContent", c = "com.htmake.reader.api.controller.BookController")
    static final class C00851 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        /* synthetic */ Object result;
        int label;

        C00851(Continuation<? super C00851> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.setEpubContent(null, null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$syncFromWebdav$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$syncFromWebdav$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2512}, i = {}, s = {}, n = {}, m = "syncFromWebdav", c = "com.htmake.reader.api.controller.BookController")
    static final class C00871 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int I$0;
        int I$1;
        /* synthetic */ Object result;
        int label;

        C00871(Continuation<? super C00871> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.syncFromWebdav(null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$textToSpeech$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$textToSpeech$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {2891}, i = {0, 0, 0}, s = {"L$0", "L$1", "L$2"}, n = {"this", "context", "response"}, m = "textToSpeech", c = "com.htmake.reader.api.controller.BookController")
    static final class C00881 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00881(Continuation<? super C00881> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.textToSpeech(null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$ttsByApi$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$ttsByApi$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3018}, i = {}, s = {}, n = {}, m = "ttsByApi", c = "com.htmake.reader.api.controller.BookController")
    static final class C00901 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        /* synthetic */ Object result;
        int label;

        C00901(Continuation<? super C00901> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.ttsByApi(null, null, null, null, (Continuation) this);
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$ttsByTextToSpeechCn$1, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$ttsByTextToSpeechCn$1.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    @DebugMetadata(f = "BookController.kt", l = {3124}, i = {0}, s = {"L$0"}, n = {"response"}, m = "ttsByTextToSpeechCn", c = "com.htmake.reader.api.controller.BookController")
    static final class C00911 extends ContinuationImpl {
        Object L$0;
        /* synthetic */ Object result;
        int label;

        C00911(Continuation<? super C00911> $completion) {
            super($completion);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            this.result = $result;
            this.label |= Integer.MIN_VALUE;
            return BookController.this.ttsByTextToSpeechCn(null, null, null, (Continuation) this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BookController(@NotNull CoroutineContext coroutineContext) {
        super(coroutineContext);
        Intrinsics.checkNotNullParameter(coroutineContext, "coroutineContext");
        this.bookInfoCache = ACache.INSTANCE.get("bookInfoCache", 2000000L, 10000);
        this.concurrentLoopCount = 8;
        this.backupFileNames = LazyKt.lazy(new Function0<String[]>() { // from class: com.htmake.reader.api.controller.BookController$backupFileNames$2
            @NotNull
            /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
            public final String[] m27invoke() {
                return new String[]{"bookSource.json", "bookshelf.json", "bookGroup.json", "rssSources.json", "replaceRule.json", "bookmark.json", "userConfig.json", "httpTTS.json", "remoteBookSourceSub.json", DefaultData.txtTocRuleFileName};
            }
        });
        Object bean = SpringContextUtils.getBean("webClient", WebClient.class);
        Intrinsics.checkNotNullExpressionValue(bean, "getBean(\"webClient\", WebClient::class.java)");
        this.webClient = (WebClient) bean;
    }

    private final String[] getBackupFileNames() {
        return (String[]) this.backupFileNames.getValue();
    }

    private final ACache getInvalidBookSourceCache(String userNameSpace) {
        File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace));
        ACache invalidBookSourceCache = ACache.INSTANCE.get(cacheDir, 5000000L, 1000000);
        return invalidBookSourceCache;
    }

    private final boolean isInvalidBookSource(BookSource bookSource, String userNameSpace) {
        return getInvalidBookSourceCache(userNameSpace).getAsString(bookSource.getBookSourceUrl()) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void addInvalidBookSource(String sourceUrl, Map<String, ? extends Object> invalidInfo, String userNameSpace) {
        getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode$default(invalidInfo, false, 2, null), 600);
    }

    private final ACache getBookChaptersCache(String userNameSpace) {
        File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "bookChaptersCache", userNameSpace));
        ACache bookChaptersCache = ACache.INSTANCE.get(cacheDir, 5000000L, 1000000);
        return bookChaptersCache;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getInvalidBookSources(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00401 c00401;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00401) {
            c00401 = (C00401) $completion;
            if ((c00401.label & Integer.MIN_VALUE) != 0) {
                c00401.label -= Integer.MIN_VALUE;
            } else {
                c00401 = new C00401($completion);
            }
        }
        Object $result = c00401.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00401.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00401.L$0 = this;
                c00401.L$1 = context;
                c00401.L$2 = returnData;
                c00401.label = 1;
                objCheckAuth = checkAuth(context, c00401);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00401.L$2;
                context = (RoutingContext) c00401.L$1;
                this = (BookController) c00401.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        String userNameSpace = this.getUserNameSpace(context);
        ACache invalidBookSourceCache = this.getInvalidBookSourceCache(userNameSpace);
        File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace));
        File[] files = cacheDir.listFiles();
        ArrayList invalidBookSourceList = new ArrayList();
        if (files != null) {
            int i = 0;
            int length = files.length;
            while (i < length) {
                File f = files[i];
                i++;
                String name = f.getName();
                Intrinsics.checkNotNullExpressionValue(name, "f.name");
                String info = invalidBookSourceCache.getByHashCode(name);
                if (info != null) {
                    Boxing.boxBoolean(invalidBookSourceList.add(ExtKt.toMap(info)));
                }
            }
        }
        return ReturnData.setData$default(returnData, invalidBookSourceList, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x01aa  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x01b9  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0317  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0321  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0329  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0331  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x03ca  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0431  */
    /* JADX WARN: Type inference failed for: r2v11, types: [com.htmake.reader.api.controller.BookController$getBookInfo$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookInfo(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00331 c00331;
        Book bookInfo;
        ReturnData returnData;
        Object objMergeBookCacheInfo;
        BookController bookController;
        Object bookInfo$default;
        String userNameSpace;
        String bookUrl;
        Object bookSourceString$default;
        Object bookSourceString$default2;
        Object objCheckAuth;
        Book[] bookArr;
        String bookSource;
        String str;
        Object map;
        Book book;
        if ($completion instanceof C00331) {
            c00331 = (C00331) $completion;
            if ((c00331.label & Integer.MIN_VALUE) != 0) {
                c00331.label -= Integer.MIN_VALUE;
            } else {
                c00331 = new C00331($completion);
            }
        }
        Object $result = c00331.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00331.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getJsonObject("searchBook").getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                userNameSpace = getUserNameSpace(context);
                BookControllerKt.logger.info("getBookInfo with bookUrl: {}", bookUrl);
                bookInfo = null;
                c00331.L$0 = this;
                c00331.L$1 = context;
                c00331.L$2 = returnData;
                c00331.L$3 = bookUrl;
                c00331.L$4 = userNameSpace;
                c00331.label = 1;
                objCheckAuth = checkAuth(context, c00331);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                }
                if (bookInfo == null) {
                    String asString = this.bookInfoCache.getAsString(bookUrl);
                    if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                        book = null;
                    } else {
                        String json$iv$iv = map instanceof String ? (String) map : ExtKt.getGson().toJson(map);
                        book = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$getBookInfo$$inlined$toDataClass$1
                        }.getType());
                    }
                    Book cacheInfo = book;
                    if (cacheInfo == null) {
                        c00331.L$0 = this;
                        c00331.L$1 = returnData;
                        c00331.L$2 = bookUrl;
                        c00331.L$3 = userNameSpace;
                        c00331.L$4 = null;
                        c00331.label = 3;
                        bookSourceString$default = getBookSourceString$default(this, context, null, false, c00331, 6, null);
                        if (bookSourceString$default == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        bookSource = (String) bookSourceString$default;
                        str = bookSource;
                        if (str != null) {
                            if (!(str != null || str.length() == 0)) {
                            }
                        }
                        return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
                    }
                    c00331.L$0 = this;
                    c00331.L$1 = returnData;
                    c00331.L$2 = bookUrl;
                    c00331.L$3 = userNameSpace;
                    c00331.L$4 = null;
                    c00331.label = 2;
                    bookSourceString$default2 = getBookSourceString$default(this, context, cacheInfo.getOrigin(), false, c00331, 4, null);
                    if (bookSourceString$default2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookSource = (String) bookSourceString$default2;
                    str = bookSource;
                    if (!(str != null || str.length() == 0)) {
                        return returnData.setErrorMsg("未配置书源");
                    }
                    bookController = this;
                    c00331.L$0 = this;
                    c00331.L$1 = returnData;
                    c00331.L$2 = bookController;
                    c00331.L$3 = null;
                    c00331.label = 4;
                    bookInfo$default = WebBook.getBookInfo$default(new WebBook(bookSource, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null), bookUrl, false, (Continuation) c00331, 2, (Object) null);
                    if (bookInfo$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c00331.L$0 = this;
                    c00331.L$1 = returnData;
                    c00331.L$2 = null;
                    c00331.label = 5;
                    objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00331);
                    if (objMergeBookCacheInfo == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookInfo = (Book) objMergeBookCacheInfo;
                }
                bookArr = new Book[]{bookInfo};
                c00331.L$0 = returnData;
                c00331.L$1 = bookInfo;
                c00331.L$2 = null;
                c00331.L$3 = null;
                c00331.L$4 = null;
                c00331.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00331) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 1:
                bookInfo = null;
                userNameSpace = (String) c00331.L$4;
                bookUrl = (String) c00331.L$3;
                returnData = (ReturnData) c00331.L$2;
                context = (RoutingContext) c00331.L$1;
                this = (BookController) c00331.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                if (bookInfo == null) {
                }
                bookArr = new Book[]{bookInfo};
                c00331.L$0 = returnData;
                c00331.L$1 = bookInfo;
                c00331.L$2 = null;
                c00331.L$3 = null;
                c00331.L$4 = null;
                c00331.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00331) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 2:
                userNameSpace = (String) c00331.L$3;
                bookUrl = (String) c00331.L$2;
                returnData = (ReturnData) c00331.L$1;
                this = (BookController) c00331.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default2 = $result;
                bookSource = (String) bookSourceString$default2;
                str = bookSource;
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 3:
                userNameSpace = (String) c00331.L$3;
                bookUrl = (String) c00331.L$2;
                returnData = (ReturnData) c00331.L$1;
                this = (BookController) c00331.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                bookSource = (String) bookSourceString$default;
                str = bookSource;
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 4:
                bookController = (BookController) c00331.L$2;
                returnData = (ReturnData) c00331.L$1;
                this = (BookController) c00331.L$0;
                ResultKt.throwOnFailure($result);
                bookInfo$default = $result;
                c00331.L$0 = this;
                c00331.L$1 = returnData;
                c00331.L$2 = null;
                c00331.label = 5;
                objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00331);
                if (objMergeBookCacheInfo == coroutine_suspended) {
                }
                bookInfo = (Book) objMergeBookCacheInfo;
                bookArr = new Book[]{bookInfo};
                c00331.L$0 = returnData;
                c00331.L$1 = bookInfo;
                c00331.L$2 = null;
                c00331.L$3 = null;
                c00331.L$4 = null;
                c00331.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00331) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 5:
                returnData = (ReturnData) c00331.L$1;
                this = (BookController) c00331.L$0;
                ResultKt.throwOnFailure($result);
                objMergeBookCacheInfo = $result;
                bookInfo = (Book) objMergeBookCacheInfo;
                bookArr = new Book[]{bookInfo};
                c00331.L$0 = returnData;
                c00331.L$1 = bookInfo;
                c00331.L$2 = null;
                c00331.L$3 = null;
                c00331.L$4 = null;
                c00331.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00331) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 6:
                bookInfo = (Book) c00331.L$1;
                returnData = (ReturnData) c00331.L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @Nullable
    public final Object getBookCover(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        List listQueryParam = context.queryParam("path");
        Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"path\")");
        String str = (String) CollectionsKt.firstOrNull(listQueryParam);
        String coverUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        if (coverUrl.length() == 0) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        String ext = getFileExt(coverUrl, "png");
        String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl);
        String cachePath = ExtKt.getWorkDir("storage", "cache", "bookCoverCache", md5Encode + '.' + ext);
        File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {
            BookControllerKt.logger.info("send cache: {}", cacheFile);
            HttpServerResponse httpServerResponseSendFile = context.response().putHeader("Cache-Control", "86400").sendFile(cacheFile.toString());
            return httpServerResponseSendFile == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? httpServerResponseSendFile : Unit.INSTANCE;
        }
        if (!cacheFile.getParentFile().exists()) {
            cacheFile.getParentFile().mkdirs();
        }
        Job jobLaunch$default = BuildersKt.launch$default(this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()).plus((CoroutineExceptionHandler) new BookController$getBookCover$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key, context)), (CoroutineStart) null, new C00322(context, cacheFile, this, coverUrl, null), 2, (Object) null);
        return jobLaunch$default == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? jobLaunch$default : Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookCover$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookCover$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookController.kt", l = {197}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$getBookCover$2")
    static final class C00322 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        final /* synthetic */ RoutingContext $context;
        final /* synthetic */ File $cacheFile;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ String $coverUrl;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00322(RoutingContext $context, File $cacheFile, BookController this$0, String $coverUrl, Continuation<? super C00322> $completion) {
            super(2, $completion);
            this.$context = $context;
            this.$cacheFile = $cacheFile;
            this.this$0 = this$0;
            this.$coverUrl = $coverUrl;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new C00322(this.$context, this.$cacheFile, this.this$0, this.$coverUrl, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object objAwaitResult;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    final BookController bookController = this.this$0;
                    final String str = this.$coverUrl;
                    this.label = 1;
                    objAwaitResult = VertxCoroutineKt.awaitResult(new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>() { // from class: com.htmake.reader.api.controller.BookController$getBookCover$2$result$1
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                            invoke((Handler<AsyncResult<HttpResponse<Buffer>>>) p1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                            Intrinsics.checkNotNullParameter(handler, "handler");
                            bookController.webClient.getAbs(str).timeout(3000L).send(handler);
                        }
                    }, (Continuation) this);
                    if (objAwaitResult == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objAwaitResult = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            HttpResponse result = (HttpResponse) objAwaitResult;
            Buffer bufferBodyAsBuffer = result.bodyAsBuffer();
            byte[] bodyBytes = bufferBodyAsBuffer == null ? null : bufferBodyAsBuffer.getBytes();
            if (bodyBytes != null) {
                HttpServerResponse res = this.$context.response().putHeader("Cache-Control", "86400");
                FilesKt.writeBytes(this.$cacheFile, bodyBytes);
                res.sendFile(this.$cacheFile.toString());
            } else {
                this.$context.response().setStatusCode(404).end();
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object importBookPreview(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00471 c00471;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00471) {
            c00471 = (C00471) $completion;
            if ((c00471.label & Integer.MIN_VALUE) != 0) {
                c00471.label -= Integer.MIN_VALUE;
            } else {
                c00471 = new C00471($completion);
            }
        }
        Object $result = c00471.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00471.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00471.L$0 = this;
                c00471.L$1 = context;
                c00471.L$2 = returnData;
                c00471.label = 1;
                objCheckAuth = checkAuth(context, c00471);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00471.L$2;
                context = (RoutingContext) c00471.L$1;
                this = (BookController) c00471.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("请上传书籍文件");
        }
        String userNameSpace = this.getUserNameSpace(context);
        ArrayList fileList = new ArrayList();
        Iterable iterableFileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue(iterableFileUploads, "context.fileUploads()");
        Iterable $this$forEach$iv = iterableFileUploads;
        for (Object element$iv : $this$forEach$iv) {
            FileUpload it = (FileUpload) element$iv;
            File file = new File(it.uploadedFileName());
            BookControllerKt.logger.info("uploadFile: {} {} {}", new Object[]{it.uploadedFileName(), it.fileName(), file});
            if (file.exists()) {
                String fileName = it.fileName();
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                String ext = BaseController.getFileExt$default(this, fileName, null, 2, null);
                if (!Intrinsics.areEqual(ext, "txt") && !Intrinsics.areEqual(ext, "epub") && !Intrinsics.areEqual(ext, "umd") && !Intrinsics.areEqual(ext, "cbz") && !Intrinsics.areEqual(ext, "pdf")) {
                    ExtKt.deleteRecursively(file);
                    return returnData.setErrorMsg("不支持导入" + ext + "格式的书籍文件");
                }
                FileUtils fileUtils = FileUtils.INSTANCE;
                Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
                String fileName2 = fileUtils.getNameExcludeExtension(fileName);
                Intrinsics.checkNotNullExpressionValue(fileName2, "fileName");
                String fileName3 = AppPattern.INSTANCE.getFileNameRegex().replace(fileName2, PackageDocumentBase.PREFIX_OPF);
                StringBuilder sb = new StringBuilder();
                Intrinsics.checkNotNullExpressionValue(fileName3, "fileName");
                String strSubstring = fileName3.substring(0, Math.min(50, fileName3.length()));
                Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                String fileName4 = sb.append(strSubstring).append('.').append(ext).toString();
                String localFilePath = Paths.get("storage", "assets", userNameSpace, "book", fileName4).toString();
                String localFileUrl = "/assets/" + userNameSpace + "/book/" + ((Object) fileName4);
                String filePath = localFilePath;
                Intrinsics.checkNotNullExpressionValue(fileName4, "fileName");
                if (StringsKt.endsWith(fileName4, ".epub", true)) {
                    filePath = filePath + ((Object) File.separator) + "index.epub";
                }
                Intrinsics.checkNotNullExpressionValue(fileName4, "fileName");
                if (StringsKt.endsWith(fileName4, ".cbz", true)) {
                    filePath = filePath + ((Object) File.separator) + "index.cbz";
                }
                Intrinsics.checkNotNullExpressionValue(fileName4, "fileName");
                if (StringsKt.endsWith(fileName4, ".pdf", true)) {
                    filePath = filePath + ((Object) File.separator) + "index.pdf";
                }
                File newFile = new File(ExtKt.getWorkDir(filePath));
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                BookControllerKt.logger.info("moveTo: {}", newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2) null, 6, (Object) null)) {
                    Book book = Book.INSTANCE.initLocalBook(localFileUrl, localFilePath, ExtKt.getWorkDir$default(null, 1, null));
                    book.setUserNameSpace(userNameSpace);
                    try {
                        fileList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", LocalBook.INSTANCE.getChapterList(book))}));
                    } catch (TocEmptyException e) {
                        fileList.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", new ArrayList())}));
                    }
                }
                ExtKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData, fileList, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getTxtTocRules(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00461 c00461;
        ReturnData returnData;
        Object objCheckAuth;
        Object obj;
        if ($completion instanceof C00461) {
            c00461 = (C00461) $completion;
            if ((c00461.label & Integer.MIN_VALUE) != 0) {
                c00461.label -= Integer.MIN_VALUE;
            } else {
                c00461 = new C00461($completion);
            }
        }
        Object $result = c00461.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00461.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00461.L$0 = this;
                c00461.L$1 = context;
                c00461.L$2 = returnData;
                c00461.label = 1;
                objCheckAuth = checkAuth(context, c00461);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00461.L$2;
                context = (RoutingContext) c00461.L$1;
                this = (BookController) c00461.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        String userNameSpace = this.getUserNameSpace(context);
        String txtTocRules = this.getUserStorage(userNameSpace, "txtTocRule");
        List rules = new ArrayList();
        rules.addAll(DefaultData.INSTANCE.getTxtTocRules());
        if (txtTocRules != null) {
            Gson $this$fromJsonArray$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Object objFromJson = $this$fromJsonArray$iv.fromJson(txtTocRules, new ParameterizedTypeImpl(TxtTocRule.class));
                obj = Result.constructor-impl(objFromJson instanceof List ? (List) objFromJson : null);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            Object obj2 = obj;
            List list = (List) (Result.isFailure-impl(obj2) ? null : obj2);
            List customRule = list == null ? CollectionsKt.emptyList() : list;
            rules.addAll(customRule);
        }
        return ReturnData.setData$default(returnData, rules, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getChapterListByRule(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00391 c00391;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00391) {
            c00391 = (C00391) $completion;
            if ((c00391.label & Integer.MIN_VALUE) != 0) {
                c00391.label -= Integer.MIN_VALUE;
            } else {
                c00391 = new C00391($completion);
            }
        }
        Object $result = c00391.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00391.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00391.L$0 = this;
                c00391.L$1 = context;
                c00391.L$2 = returnData;
                c00391.label = 1;
                objCheckAuth = checkAuth(context, c00391);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00391.L$2;
                context = (RoutingContext) c00391.L$1;
                this = (BookController) c00391.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        Book book = (Book) context.getBodyAsJson().mapTo(Book.class);
        if (book.getOrigin().length() == 0) {
            return returnData.setErrorMsg("未找到书源信息");
        }
        if (!book.isLocalTxt() && !book.isLocalEpub() && !book.isLocalPdf()) {
            return returnData.setErrorMsg("非本地txt/epub/pdf书籍");
        }
        book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
        book.setUserNameSpace(this.getUserNameSpace(context));
        LocalBook localBook = LocalBook.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(book, "book");
        return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", localBook.getChapterList(book))}), null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object refreshLocalBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00481 c00481;
        final Book bookInfo;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        if ($completion instanceof C00481) {
            c00481 = (C00481) $completion;
            if ((c00481.label & Integer.MIN_VALUE) != 0) {
                c00481.label -= Integer.MIN_VALUE;
            } else {
                c00481 = new C00481($completion);
            }
        }
        Object $result = c00481.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00481.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00481.L$0 = this;
                c00481.L$1 = context;
                c00481.L$2 = returnData;
                c00481.label = 1;
                objCheckAuth = checkAuth(context, c00481);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookUrl\")");
                    bookUrl = string;
                } else {
                    List listQueryParam = context.queryParam("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookUrl\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                String userNameSpace = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo == null) {
                    return returnData.setErrorMsg("书籍信息错误");
                }
                bookInfo.updateFromLocal(true);
                c00481.L$0 = returnData;
                c00481.L$1 = bookInfo;
                c00481.L$2 = null;
                c00481.label = 2;
                if (this.editShelfBook(bookInfo, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.refreshLocalBook.2
                    {
                        super(1);
                    }

                    @NotNull
                    public final Book invoke(@NotNull Book existBook) {
                        Intrinsics.checkNotNullParameter(existBook, "existBook");
                        existBook.setCoverUrl(bookInfo.getCoverUrl());
                        BookControllerKt.logger.info("refreshLocalBook: {}", existBook);
                        return existBook;
                    }
                }, c00481) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            case 1:
                returnData = (ReturnData) c00481.L$2;
                context = (RoutingContext) c00481.L$1;
                this = (BookController) c00481.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                bookInfo = (Book) c00481.L$1;
                returnData = (ReturnData) c00481.L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData, bookInfo, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:109:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x065a  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0697  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x069d  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x06a4  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x06a8  */
    /* JADX WARN: Removed duplicated region for block: B:134:0x06e9  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00d4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x0395  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x039f  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x03af  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x049c  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0536  */
    /* JADX WARN: Type inference failed for: r2v24, types: [com.htmake.reader.api.controller.BookController$getChapterList$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getChapterList(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00381 c00381;
        ReturnData returnData;
        Object localChapterList$default;
        int refresh;
        Book bookInfo;
        String userNameSpace;
        Object bookSourceString$default;
        String bookSource;
        Object objMergeBookCacheInfo;
        BookController bookController;
        Object bookInfo$default;
        String bookUrl;
        Object bookSourceString$default2;
        Object bookSourceString$default3;
        Object objCheckAuth;
        Book[] bookArr;
        String str;
        Integer numBoxInt;
        Object map;
        Book book;
        if ($completion instanceof C00381) {
            c00381 = (C00381) $completion;
            if ((c00381.label & Integer.MIN_VALUE) != 0) {
                c00381.label -= Integer.MIN_VALUE;
            } else {
                c00381 = new C00381($completion);
            }
        }
        Object $result = c00381.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00381.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.label = 1;
                objCheckAuth = checkAuth(context, c00381);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getJsonObject("book").getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                    Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                    refresh = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam2 = context.queryParam("refresh");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"refresh\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str3 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 0 : numBoxInt.intValue();
                    refresh = iIntValue;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                userNameSpace = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo != null) {
                    c00381.L$0 = this;
                    c00381.L$1 = context;
                    c00381.L$2 = returnData;
                    c00381.L$3 = userNameSpace;
                    c00381.L$4 = bookInfo;
                    c00381.I$0 = refresh;
                    c00381.label = 7;
                    bookSourceString$default = getBookSourceString$default(this, context, bookInfo.getOrigin(), false, c00381, 4, null);
                    if (bookSourceString$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookSource = (String) bookSourceString$default;
                    if (!bookInfo.isLocalBook()) {
                    }
                    bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                    bookInfo.setUserNameSpace(userNameSpace);
                    if (bookInfo.isLocalBook()) {
                    }
                    BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                    BookController bookController2 = this;
                    Book book2 = bookInfo;
                    String str4 = bookSource;
                    if (str4 != null) {
                    }
                    if (refresh <= 0) {
                    }
                    c00381.L$0 = returnData;
                    c00381.L$1 = null;
                    c00381.L$2 = null;
                    c00381.L$3 = null;
                    c00381.L$4 = null;
                    c00381.L$5 = null;
                    c00381.label = 8;
                    localChapterList$default = getLocalChapterList$default(bookController2, book2, str, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                    if (localChapterList$default == coroutine_suspended) {
                    }
                    List chapterList = (List) localChapterList$default;
                    return ReturnData.setData$default(returnData, chapterList, null, 2, null);
                }
                String asString = this.bookInfoCache.getAsString(bookUrl);
                if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                    book = null;
                } else {
                    String json$iv$iv = map instanceof String ? (String) map : ExtKt.getGson().toJson(map);
                    book = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$getChapterList$$inlined$toDataClass$1
                    }.getType());
                }
                Book cacheInfo = book;
                if (cacheInfo == null) {
                    c00381.L$0 = this;
                    c00381.L$1 = context;
                    c00381.L$2 = returnData;
                    c00381.L$3 = bookUrl;
                    c00381.L$4 = userNameSpace;
                    c00381.I$0 = refresh;
                    c00381.label = 3;
                    bookSourceString$default2 = getBookSourceString$default(this, context, null, false, c00381, 6, null);
                    if (bookSourceString$default2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookSource = (String) bookSourceString$default2;
                    str = bookSource;
                    if (str != null) {
                        if (!(str != null || str.length() == 0)) {
                        }
                    }
                    List chapterList2 = (List) localChapterList$default;
                    return ReturnData.setData$default(returnData, chapterList2, null, 2, null);
                }
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = bookUrl;
                c00381.L$4 = userNameSpace;
                c00381.I$0 = refresh;
                c00381.label = 2;
                bookSourceString$default3 = getBookSourceString$default(this, context, cacheInfo.getOrigin(), false, c00381, 4, null);
                if (bookSourceString$default3 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                bookSource = (String) bookSourceString$default3;
                str = bookSource;
                if (!(str != null || str.length() == 0)) {
                    return returnData.setErrorMsg("未配置书源");
                }
                bookController = this;
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookSource;
                c00381.L$5 = bookController;
                c00381.I$0 = refresh;
                c00381.label = 4;
                bookInfo$default = WebBook.getBookInfo$default(new WebBook(bookSource, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null), bookUrl, false, (Continuation) c00381, 2, (Object) null);
                if (bookInfo$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookSource;
                c00381.L$5 = null;
                c00381.I$0 = refresh;
                c00381.label = 5;
                objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00381);
                if (objMergeBookCacheInfo == coroutine_suspended) {
                    return coroutine_suspended;
                }
                bookInfo = (Book) objMergeBookCacheInfo;
                bookArr = new Book[]{bookInfo};
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookInfo;
                c00381.L$5 = bookSource;
                c00381.I$0 = refresh;
                c00381.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00381) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (!bookInfo.isLocalBook()) {
                    String str5 = bookSource;
                    if (str5 == null || str5.length() == 0) {
                        return returnData.setErrorMsg("未配置书源");
                    }
                }
                bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                bookInfo.setUserNameSpace(userNameSpace);
                if (bookInfo.isLocalBook()) {
                    File localFile = bookInfo.getLocalFile();
                    if (!localFile.exists()) {
                        BookControllerKt.logger.info("localFile: {} not exists", localFile);
                        return returnData.setErrorMsg("本地书籍源文件不存在");
                    }
                }
                BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                BookController bookController22 = this;
                Book book22 = bookInfo;
                String str42 = bookSource;
                String str6 = str42 != null ? PackageDocumentBase.PREFIX_OPF : str42;
                boolean z = refresh <= 0;
                c00381.L$0 = returnData;
                c00381.L$1 = null;
                c00381.L$2 = null;
                c00381.L$3 = null;
                c00381.L$4 = null;
                c00381.L$5 = null;
                c00381.label = 8;
                localChapterList$default = getLocalChapterList$default(bookController22, book22, str6, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                List chapterList22 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList22, null, 2, null);
            case 1:
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                refresh = c00381.I$0;
                userNameSpace = (String) c00381.L$4;
                bookUrl = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default3 = $result;
                bookSource = (String) bookSourceString$default3;
                str = bookSource;
                List chapterList222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList222, null, 2, null);
            case 3:
                refresh = c00381.I$0;
                userNameSpace = (String) c00381.L$4;
                bookUrl = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default2 = $result;
                bookSource = (String) bookSourceString$default2;
                str = bookSource;
                List chapterList2222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList2222, null, 2, null);
            case 4:
                refresh = c00381.I$0;
                bookController = (BookController) c00381.L$5;
                bookSource = (String) c00381.L$4;
                userNameSpace = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                bookInfo$default = $result;
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookSource;
                c00381.L$5 = null;
                c00381.I$0 = refresh;
                c00381.label = 5;
                objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00381);
                if (objMergeBookCacheInfo == coroutine_suspended) {
                }
                bookInfo = (Book) objMergeBookCacheInfo;
                bookArr = new Book[]{bookInfo};
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookInfo;
                c00381.L$5 = bookSource;
                c00381.I$0 = refresh;
                c00381.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00381) == coroutine_suspended) {
                }
                if (!bookInfo.isLocalBook()) {
                }
                bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                bookInfo.setUserNameSpace(userNameSpace);
                if (bookInfo.isLocalBook()) {
                }
                BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                BookController bookController222 = this;
                Book book222 = bookInfo;
                String str422 = bookSource;
                if (str422 != null) {
                }
                if (refresh <= 0) {
                }
                c00381.L$0 = returnData;
                c00381.L$1 = null;
                c00381.L$2 = null;
                c00381.L$3 = null;
                c00381.L$4 = null;
                c00381.L$5 = null;
                c00381.label = 8;
                localChapterList$default = getLocalChapterList$default(bookController222, book222, str6, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                }
                List chapterList22222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList22222, null, 2, null);
            case 5:
                refresh = c00381.I$0;
                bookSource = (String) c00381.L$4;
                userNameSpace = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                objMergeBookCacheInfo = $result;
                bookInfo = (Book) objMergeBookCacheInfo;
                bookArr = new Book[]{bookInfo};
                c00381.L$0 = this;
                c00381.L$1 = context;
                c00381.L$2 = returnData;
                c00381.L$3 = userNameSpace;
                c00381.L$4 = bookInfo;
                c00381.L$5 = bookSource;
                c00381.I$0 = refresh;
                c00381.label = 6;
                if (this.saveBookInfoCache(CollectionsKt.arrayListOf(bookArr), c00381) == coroutine_suspended) {
                }
                if (!bookInfo.isLocalBook()) {
                }
                bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                bookInfo.setUserNameSpace(userNameSpace);
                if (bookInfo.isLocalBook()) {
                }
                BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                BookController bookController2222 = this;
                Book book2222 = bookInfo;
                String str4222 = bookSource;
                if (str4222 != null) {
                }
                if (refresh <= 0) {
                }
                c00381.L$0 = returnData;
                c00381.L$1 = null;
                c00381.L$2 = null;
                c00381.L$3 = null;
                c00381.L$4 = null;
                c00381.L$5 = null;
                c00381.label = 8;
                localChapterList$default = getLocalChapterList$default(bookController2222, book2222, str6, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                }
                List chapterList222222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList222222, null, 2, null);
            case 6:
                refresh = c00381.I$0;
                bookSource = (String) c00381.L$5;
                bookInfo = (Book) c00381.L$4;
                userNameSpace = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                if (!bookInfo.isLocalBook()) {
                }
                bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                bookInfo.setUserNameSpace(userNameSpace);
                if (bookInfo.isLocalBook()) {
                }
                BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                BookController bookController22222 = this;
                Book book22222 = bookInfo;
                String str42222 = bookSource;
                if (str42222 != null) {
                }
                if (refresh <= 0) {
                }
                c00381.L$0 = returnData;
                c00381.L$1 = null;
                c00381.L$2 = null;
                c00381.L$3 = null;
                c00381.L$4 = null;
                c00381.L$5 = null;
                c00381.label = 8;
                localChapterList$default = getLocalChapterList$default(bookController22222, book22222, str6, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                }
                List chapterList2222222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList2222222, null, 2, null);
            case 7:
                refresh = c00381.I$0;
                bookInfo = (Book) c00381.L$4;
                userNameSpace = (String) c00381.L$3;
                returnData = (ReturnData) c00381.L$2;
                context = (RoutingContext) c00381.L$1;
                this = (BookController) c00381.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                bookSource = (String) bookSourceString$default;
                if (!bookInfo.isLocalBook()) {
                }
                bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                bookInfo.setUserNameSpace(userNameSpace);
                if (bookInfo.isLocalBook()) {
                }
                BookControllerKt.logger.info("bookInfo: {}", bookInfo);
                BookController bookController222222 = this;
                Book book222222 = bookInfo;
                String str422222 = bookSource;
                if (str422222 != null) {
                }
                if (refresh <= 0) {
                }
                c00381.L$0 = returnData;
                c00381.L$1 = null;
                c00381.L$2 = null;
                c00381.L$3 = null;
                c00381.L$4 = null;
                c00381.L$5 = null;
                c00381.label = 8;
                localChapterList$default = getLocalChapterList$default(bookController222222, book222222, str6, z, this.getUserNameSpace(context), false, null, c00381, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                }
                List chapterList22222222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList22222222, null, 2, null);
            case 8:
                returnData = (ReturnData) c00381.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                List chapterList222222222 = (List) localChapterList$default;
                return ReturnData.setData$default(returnData, chapterList222222222, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02f7  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x02ff  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03b7  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookProgress(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00591 c00591;
        ReturnData returnData;
        BookChapter chapterInfo;
        Book bookInfo;
        String userNameSpace;
        int chapterIndex;
        Object localChapterList$default;
        Object objCheckAuth;
        List chapterList;
        String bookUrl;
        Integer numBoxInt;
        if ($completion instanceof C00591) {
            c00591 = (C00591) $completion;
            if ((c00591.label & Integer.MIN_VALUE) != 0) {
                c00591.label -= Integer.MIN_VALUE;
            } else {
                c00591 = new C00591($completion);
            }
        }
        Object $result = c00591.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00591.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00591.L$0 = this;
                c00591.L$1 = context;
                c00591.L$2 = returnData;
                c00591.label = 1;
                objCheckAuth = checkAuth(context, c00591);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getJsonObject("searchBook").getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                    Integer integer = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"index\", -1)");
                    chapterIndex = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("index");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"index\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? -1 : numBoxInt.intValue();
                    chapterIndex = iIntValue;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                userNameSpace = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo != null) {
                    if (!(bookInfo.getOrigin().length() == 0)) {
                        String bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                        if (!bookInfo.isLocalBook()) {
                            String str3 = bookSource;
                            if (str3 == null || str3.length() == 0) {
                                return returnData.setErrorMsg("未配置书源");
                            }
                        }
                        BookController bookController = this;
                        String str4 = bookSource == null ? PackageDocumentBase.PREFIX_OPF : bookSource;
                        c00591.L$0 = this;
                        c00591.L$1 = returnData;
                        c00591.L$2 = userNameSpace;
                        c00591.L$3 = bookInfo;
                        c00591.I$0 = chapterIndex;
                        c00591.label = 2;
                        localChapterList$default = getLocalChapterList$default(bookController, bookInfo, str4, false, userNameSpace, false, null, c00591, 48, null);
                        if (localChapterList$default == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        chapterList = (List) localChapterList$default;
                        if (chapterIndex < chapterList.size()) {
                            return returnData.setErrorMsg("章节不存在");
                        }
                        chapterInfo = (BookChapter) chapterList.get(chapterIndex);
                        c00591.L$0 = this;
                        c00591.L$1 = returnData;
                        c00591.L$2 = userNameSpace;
                        c00591.L$3 = bookInfo;
                        c00591.L$4 = chapterInfo;
                        c00591.label = 3;
                        if (this.saveShelfBookProgress(bookInfo, chapterInfo, userNameSpace, c00591) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        c00591.L$0 = returnData;
                        c00591.L$1 = null;
                        c00591.L$2 = null;
                        c00591.L$3 = null;
                        c00591.L$4 = null;
                        c00591.label = 4;
                        if (this.saveBookProgressToWebdav(bookInfo, chapterInfo, userNameSpace, c00591) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
                    }
                }
                return returnData.setErrorMsg("书籍未加入书架");
            case 1:
                returnData = (ReturnData) c00591.L$2;
                context = (RoutingContext) c00591.L$1;
                this = (BookController) c00591.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                chapterIndex = c00591.I$0;
                bookInfo = (Book) c00591.L$3;
                userNameSpace = (String) c00591.L$2;
                returnData = (ReturnData) c00591.L$1;
                this = (BookController) c00591.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                chapterList = (List) localChapterList$default;
                if (chapterIndex < chapterList.size()) {
                }
                break;
            case 3:
                chapterInfo = (BookChapter) c00591.L$4;
                bookInfo = (Book) c00591.L$3;
                userNameSpace = (String) c00591.L$2;
                returnData = (ReturnData) c00591.L$1;
                this = (BookController) c00591.L$0;
                ResultKt.throwOnFailure($result);
                c00591.L$0 = returnData;
                c00591.L$1 = null;
                c00591.L$2 = null;
                c00591.L$3 = null;
                c00591.L$4 = null;
                c00591.label = 4;
                if (this.saveBookProgressToWebdav(bookInfo, chapterInfo, userNameSpace, c00591) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            case 4:
                returnData = (ReturnData) c00591.L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:125:0x063b  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x063f  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x065c  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x0660  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0664  */
    /* JADX WARN: Removed duplicated region for block: B:137:0x066c  */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0807  */
    /* JADX WARN: Removed duplicated region for block: B:170:0x0880  */
    /* JADX WARN: Removed duplicated region for block: B:171:0x0886  */
    /* JADX WARN: Removed duplicated region for block: B:174:0x08ed  */
    /* JADX WARN: Removed duplicated region for block: B:179:0x096b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:190:0x0aa5  */
    /* JADX WARN: Removed duplicated region for block: B:196:0x0b21  */
    /* JADX WARN: Removed duplicated region for block: B:199:0x0b3d  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0b45  */
    /* JADX WARN: Removed duplicated region for block: B:331:0x11b0  */
    /* JADX WARN: Removed duplicated region for block: B:332:0x11b6  */
    /* JADX WARN: Removed duplicated region for block: B:335:0x11c8  */
    /* JADX WARN: Removed duplicated region for block: B:336:0x11cc  */
    /* JADX WARN: Removed duplicated region for block: B:339:0x11d8 A[Catch: Exception -> 0x12a8, TryCatch #0 {Exception -> 0x12a8, blocks: (B:314:0x10c4, B:318:0x10da, B:325:0x117f, B:329:0x1193, B:333:0x11b8, B:337:0x11ce, B:339:0x11d8, B:341:0x1202, B:348:0x1298, B:324:0x1177, B:347:0x1290), top: B:370:0x0043 }] */
    /* JADX WARN: Removed duplicated region for block: B:340:0x1200  */
    /* JADX WARN: Removed duplicated region for block: B:344:0x1249  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x04a0  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x04a4  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x04a8  */
    /* JADX WARN: Type inference failed for: r2v126, types: [com.htmake.reader.api.controller.BookController$getBookContent$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookContent(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00311 c00311;
        String content;
        BookChapter bookChapter;
        Book bookInfo;
        String userNameSpace;
        String bookSource;
        ReturnData returnData;
        File chapterCacheFile;
        Object bookContent;
        int epubContent;
        int refresh;
        int chapterIndex;
        List chapterList;
        String url;
        int i;
        int cache;
        String chapterUrl;
        Object localChapterList$default;
        Object objMergeBookCacheInfo;
        BookController bookController;
        Object bookInfo$default;
        String bookUrl;
        Object bookSourceString$default;
        Object bookSourceString$default2;
        Object objCheckAuth;
        BookHelp bookHelp;
        BookController bookController2;
        BookSource bookSource2;
        long i2;
        Book book;
        Object map;
        Book book2;
        Integer numBoxInt;
        Integer numBoxInt2;
        Integer numBoxInt3;
        Integer numBoxInt4;
        String str;
        String str2;
        if ($completion instanceof C00311) {
            c00311 = (C00311) $completion;
            if ((c00311.label & Integer.MIN_VALUE) != 0) {
                c00311.label -= Integer.MIN_VALUE;
            } else {
                c00311 = new C00311($completion);
            }
        }
        Object $result = c00311.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c00311.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    c00311.L$0 = this;
                    c00311.L$1 = context;
                    c00311.L$2 = returnData;
                    c00311.label = 1;
                    objCheckAuth = checkAuth(context, c00311);
                    if (objCheckAuth == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (((Boolean) objCheckAuth).booleanValue()) {
                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                    }
                    if (context.request().method() == HttpMethod.POST) {
                        String string = context.getBodyAsJson().getString("chapterUrl");
                        if (string == null) {
                            JsonObject jsonObject = context.getBodyAsJson().getJsonObject("bookChapter");
                            if (jsonObject == null) {
                                str = PackageDocumentBase.PREFIX_OPF;
                            } else {
                                String string2 = jsonObject.getString(RSSKeywords.RSS_ITEM_URL);
                                str = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                            }
                        } else {
                            str = string;
                        }
                        chapterUrl = str;
                        String string3 = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                        if (string3 == null) {
                            JsonObject jsonObject2 = context.getBodyAsJson().getJsonObject("searchBook");
                            if (jsonObject2 == null) {
                                str2 = PackageDocumentBase.PREFIX_OPF;
                            } else {
                                String string4 = jsonObject2.getString("bookUrl");
                                str2 = string4 == null ? PackageDocumentBase.PREFIX_OPF : string4;
                            }
                        } else {
                            str2 = string3;
                        }
                        bookUrl = str2;
                        Integer integer = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
                        Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"index\", -1)");
                        chapterIndex = integer.intValue();
                        Integer integer2 = context.getBodyAsJson().getInteger("cache", Boxing.boxInt(0));
                        Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"cache\", 0)");
                        cache = integer2.intValue();
                        Integer integer3 = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                        Intrinsics.checkNotNullExpressionValue(integer3, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                        refresh = integer3.intValue();
                        Integer integer4 = context.getBodyAsJson().getInteger("epubContent", Boxing.boxInt(0));
                        Intrinsics.checkNotNullExpressionValue(integer4, "context.bodyAsJson.getInteger(\"epubContent\", 0)");
                        epubContent = integer4.intValue();
                    } else {
                        List listQueryParam = context.queryParam("chapterUrl");
                        Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"chapterUrl\")");
                        String str3 = (String) CollectionsKt.firstOrNull(listQueryParam);
                        chapterUrl = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
                        List listQueryParam2 = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                        Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"url\")");
                        String str4 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                        bookUrl = str4 == null ? PackageDocumentBase.PREFIX_OPF : str4;
                        List listQueryParam3 = context.queryParam("index");
                        Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"index\")");
                        String str5 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                        int iIntValue = (str5 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str5))) == null) ? -1 : numBoxInt.intValue();
                        chapterIndex = iIntValue;
                        List listQueryParam4 = context.queryParam("cache");
                        Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"cache\")");
                        String str6 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                        int iIntValue2 = (str6 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str6))) == null) ? 0 : numBoxInt2.intValue();
                        cache = iIntValue2;
                        List listQueryParam5 = context.queryParam("refresh");
                        Intrinsics.checkNotNullExpressionValue(listQueryParam5, "context.queryParam(\"refresh\")");
                        String str7 = (String) CollectionsKt.firstOrNull(listQueryParam5);
                        int iIntValue3 = (str7 == null || (numBoxInt3 = Boxing.boxInt(Integer.parseInt(str7))) == null) ? 0 : numBoxInt3.intValue();
                        refresh = iIntValue3;
                        List listQueryParam6 = context.queryParam("epubContent");
                        Intrinsics.checkNotNullExpressionValue(listQueryParam6, "context.queryParam(\"epubContent\")");
                        String str8 = (String) CollectionsKt.firstOrNull(listQueryParam6);
                        int iIntValue4 = (str8 == null || (numBoxInt4 = Boxing.boxInt(Integer.parseInt(str8))) == null) ? 0 : numBoxInt4.intValue();
                        epubContent = iIntValue4;
                    }
                    if (bookUrl.length() == 0) {
                        return returnData.setErrorMsg("请输入书籍链接");
                    }
                    c00311.L$0 = this;
                    c00311.L$1 = context;
                    c00311.L$2 = returnData;
                    c00311.L$3 = chapterUrl;
                    c00311.L$4 = bookUrl;
                    c00311.I$0 = chapterIndex;
                    c00311.I$1 = cache;
                    c00311.I$2 = refresh;
                    c00311.I$3 = epubContent;
                    c00311.label = 2;
                    bookSourceString$default2 = getBookSourceString$default(this, context, null, false, c00311, 6, null);
                    if (bookSourceString$default2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookSource = (String) bookSourceString$default2;
                    userNameSpace = this.getUserNameSpace(context);
                    i = 0;
                    bookInfo = null;
                    bookChapter = null;
                    url = null;
                    if (bookUrl.length() <= 0) {
                        bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                        if (bookInfo != null) {
                            if (bookInfo.getOrigin().length() > 0) {
                                i = 1;
                                bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                            }
                        }
                        String asString = this.bookInfoCache.getAsString(bookUrl);
                        if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                            book2 = null;
                        } else {
                            String json$iv$iv = map instanceof String ? (String) map : ExtKt.getGson().toJson(map);
                            book2 = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$getBookContent$$inlined$toDataClass$1
                            }.getType());
                        }
                        Book cacheInfo = book2;
                        if (cacheInfo != null) {
                            c00311.L$0 = this;
                            c00311.L$1 = returnData;
                            c00311.L$2 = chapterUrl;
                            c00311.L$3 = bookUrl;
                            c00311.L$4 = userNameSpace;
                            c00311.L$5 = bookInfo;
                            c00311.I$0 = chapterIndex;
                            c00311.I$1 = cache;
                            c00311.I$2 = refresh;
                            c00311.I$3 = epubContent;
                            c00311.I$4 = i;
                            c00311.label = 3;
                            bookSourceString$default = getBookSourceString$default(this, context, cacheInfo.getOrigin(), false, c00311, 4, null);
                            if (bookSourceString$default == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            bookSource = (String) bookSourceString$default;
                        }
                        if ((chapterUrl.length() != 0) && chapterIndex >= 0) {
                            if (!(bookUrl.length() != 0)) {
                                return returnData.setErrorMsg("请输入书籍链接");
                            }
                            if (bookInfo != null && !bookInfo.isLocalBook()) {
                                String str9 = bookSource;
                                if (str9 == null || str9.length() == 0) {
                                    return returnData.setErrorMsg("未配置书源");
                                }
                            }
                            Book book3 = bookInfo;
                            if (book3 == null) {
                                bookController = this;
                                String str10 = bookSource;
                                String str11 = str10 == null ? PackageDocumentBase.PREFIX_OPF : str10;
                                c00311.L$0 = this;
                                c00311.L$1 = returnData;
                                c00311.L$2 = chapterUrl;
                                c00311.L$3 = bookSource;
                                c00311.L$4 = userNameSpace;
                                c00311.L$5 = bookController;
                                c00311.I$0 = chapterIndex;
                                c00311.I$1 = cache;
                                c00311.I$2 = refresh;
                                c00311.I$3 = epubContent;
                                c00311.I$4 = i;
                                c00311.label = 4;
                                bookInfo$default = WebBook.getBookInfo$default(new WebBook(str11, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null), bookUrl, false, (Continuation) c00311, 2, (Object) null);
                                if (bookInfo$default == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                c00311.L$0 = this;
                                c00311.L$1 = returnData;
                                c00311.L$2 = chapterUrl;
                                c00311.L$3 = bookSource;
                                c00311.L$4 = userNameSpace;
                                c00311.L$5 = null;
                                c00311.I$0 = chapterIndex;
                                c00311.I$1 = cache;
                                c00311.I$2 = refresh;
                                c00311.I$3 = epubContent;
                                c00311.I$4 = i;
                                c00311.label = 5;
                                objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00311);
                                if (objMergeBookCacheInfo == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                book = (Book) objMergeBookCacheInfo;
                                bookInfo = book;
                                BookController bookController3 = this;
                                String str12 = bookSource;
                                String str13 = str12 != null ? PackageDocumentBase.PREFIX_OPF : str12;
                                c00311.L$0 = this;
                                c00311.L$1 = returnData;
                                c00311.L$2 = chapterUrl;
                                c00311.L$3 = bookSource;
                                c00311.L$4 = userNameSpace;
                                c00311.L$5 = bookInfo;
                                c00311.I$0 = chapterIndex;
                                c00311.I$1 = cache;
                                c00311.I$2 = refresh;
                                c00311.I$3 = epubContent;
                                c00311.I$4 = i;
                                c00311.label = 6;
                                localChapterList$default = getLocalChapterList$default(bookController3, bookInfo, str13, false, userNameSpace, false, null, c00311, 48, null);
                                if (localChapterList$default == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                chapterList = (List) localChapterList$default;
                                if (chapterIndex < chapterList.size()) {
                                    bookChapter = (BookChapter) chapterList.get(chapterIndex);
                                    if (i != 0 && cache != 1) {
                                        c00311.L$0 = this;
                                        c00311.L$1 = returnData;
                                        c00311.L$2 = bookSource;
                                        c00311.L$3 = userNameSpace;
                                        c00311.L$4 = bookInfo;
                                        c00311.L$5 = bookChapter;
                                        c00311.L$6 = chapterList;
                                        c00311.I$0 = chapterIndex;
                                        c00311.I$1 = refresh;
                                        c00311.I$2 = epubContent;
                                        c00311.label = 7;
                                        if (this.saveShelfBookProgress(bookInfo, bookChapter, userNameSpace, c00311) == coroutine_suspended) {
                                            return coroutine_suspended;
                                        }
                                        c00311.L$0 = this;
                                        c00311.L$1 = returnData;
                                        c00311.L$2 = bookSource;
                                        c00311.L$3 = userNameSpace;
                                        c00311.L$4 = bookInfo;
                                        c00311.L$5 = bookChapter;
                                        c00311.L$6 = chapterList;
                                        c00311.I$0 = chapterIndex;
                                        c00311.I$1 = refresh;
                                        c00311.I$2 = epubContent;
                                        c00311.label = 8;
                                        if (this.saveBookProgressToWebdav(bookInfo, bookChapter, userNameSpace, c00311) == coroutine_suspended) {
                                            return coroutine_suspended;
                                        }
                                    }
                                    chapterUrl = bookChapter.getUrl();
                                    if (chapterIndex + 1 < chapterList.size()) {
                                        BookChapter nextChapterInfo = (BookChapter) chapterList.get(chapterIndex + 1);
                                        url = nextChapterInfo.getUrl();
                                    }
                                }
                            } else {
                                book = book3;
                                bookInfo = book;
                                BookController bookController32 = this;
                                String str122 = bookSource;
                                if (str122 != null) {
                                }
                                c00311.L$0 = this;
                                c00311.L$1 = returnData;
                                c00311.L$2 = chapterUrl;
                                c00311.L$3 = bookSource;
                                c00311.L$4 = userNameSpace;
                                c00311.L$5 = bookInfo;
                                c00311.I$0 = chapterIndex;
                                c00311.I$1 = cache;
                                c00311.I$2 = refresh;
                                c00311.I$3 = epubContent;
                                c00311.I$4 = i;
                                c00311.label = 6;
                                localChapterList$default = getLocalChapterList$default(bookController32, bookInfo, str13, false, userNameSpace, false, null, c00311, 48, null);
                                if (localChapterList$default == coroutine_suspended) {
                                }
                                chapterList = (List) localChapterList$default;
                                if (chapterIndex < chapterList.size()) {
                                }
                            }
                        }
                    }
                    if (bookInfo != null) {
                        return returnData.setErrorMsg("获取书籍信息失败");
                    }
                    if (!bookInfo.isLocalBook()) {
                        String str14 = bookSource;
                        if (str14 == null || str14.length() == 0) {
                            return returnData.setErrorMsg("未配置书源");
                        }
                    }
                    if (bookChapter != null) {
                        if (!(chapterUrl.length() == 0)) {
                            bookInfo.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                            bookInfo.setUserNameSpace(userNameSpace);
                            if (!bookInfo.isLocalBook()) {
                                chapterCacheFile = null;
                                if (bookInfo.isInShelf() && this.getAppConfig().getCacheChapterContent()) {
                                    File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
                                    chapterCacheFile = new File(localCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
                                    if (refresh <= 0 && chapterCacheFile.exists()) {
                                        String content2 = FilesKt.readText$default(chapterCacheFile, (Charset) null, 1, (Object) null);
                                        if (StringsKt.indexOf$default(content2, "<img", 0, false, 6, (Object) null) >= 0) {
                                            content2 = this.updateImageLinkInContent(bookInfo, bookChapter, content2);
                                        }
                                        BookControllerKt.logger.info("使用缓存的章节内容: {}", chapterCacheFile.toString());
                                        return ReturnData.setData$default(returnData, content2, null, 2, null);
                                    }
                                }
                                String str15 = bookSource;
                                String str16 = str15 == null ? PackageDocumentBase.PREFIX_OPF : str15;
                                c00311.L$0 = this;
                                c00311.L$1 = returnData;
                                c00311.L$2 = bookSource;
                                c00311.L$3 = userNameSpace;
                                c00311.L$4 = bookInfo;
                                c00311.L$5 = bookChapter;
                                c00311.L$6 = chapterCacheFile;
                                c00311.label = 9;
                                bookContent = new WebBook(str16, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null).getBookContent(bookInfo, bookChapter, url, c00311);
                                if (bookContent == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                content = (String) bookContent;
                                if (this.getAppConfig().getCacheChapterContent() && chapterCacheFile != null) {
                                    FilesKt.writeText$default(chapterCacheFile, content, (Charset) null, 2, (Object) null);
                                    bookHelp = BookHelp.INSTANCE;
                                    bookController2 = this;
                                    String str17 = bookSource;
                                    Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(str17 != null ? PackageDocumentBase.PREFIX_OPF : str17);
                                    BookSource bookSource3 = (BookSource) (!Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                                    bookSource2 = bookSource3 != null ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource3;
                                    c00311.L$0 = this;
                                    c00311.L$1 = returnData;
                                    c00311.L$2 = bookSource;
                                    c00311.L$3 = userNameSpace;
                                    c00311.L$4 = bookInfo;
                                    c00311.L$5 = bookChapter;
                                    c00311.L$6 = content;
                                    c00311.label = 10;
                                    if (bookHelp.saveImages(bookController2, bookSource2, bookInfo, bookChapter, content, c00311) == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    content = this.updateImageLinkInContent(bookInfo, bookChapter, content);
                                    break;
                                }
                                return ReturnData.setData$default(returnData, content, null, 2, null);
                            }
                            File localFile = bookInfo.getLocalFile();
                            if (!localFile.exists()) {
                                return returnData.setErrorMsg("本地源书籍文件不存在");
                            }
                            if (bookInfo.isEpub()) {
                                if (!extractEpub$default(this, bookInfo, false, 2, null)) {
                                    return returnData.setErrorMsg("Epub书籍解压失败");
                                }
                                String epubRootDir = bookInfo.getEpubRootDir();
                                String chapterFilePath = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", epubRootDir, bookChapter.getUrl());
                                BookControllerKt.logger.info("chapterFilePath: {} {}", chapterFilePath, epubRootDir);
                                if (!new File(chapterFilePath).exists()) {
                                    return returnData.setErrorMsg("章节文件不存在");
                                }
                                String content3 = epubRootDir.length() == 0 ? StringsKt.replace$default(StringsKt.replace$default(bookInfo.getBookUrl(), "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null), "storage/data/", "/book-assets/", false, 4, (Object) null) + "/index/" + bookChapter.getUrl() : StringsKt.replace$default(StringsKt.replace$default(bookInfo.getBookUrl(), "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null), "storage/data/", "/book-assets/", false, 4, (Object) null) + "/index/" + epubRootDir + '/' + bookChapter.getUrl();
                                return epubContent > 0 ? ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to(RSSKeywords.RSS_ITEM_URL, Intrinsics.stringPlus("__API_ROOT__", content3)), TuplesKt.to("content", FilesKt.readText$default(new File(chapterFilePath), (Charset) null, 1, (Object) null))}), null, 2, null) : ReturnData.setData$default(returnData, content3, null, 2, null);
                            }
                            if (bookInfo.isCbz()) {
                                if (!extractCbz$default(this, bookInfo, false, 2, null)) {
                                    return returnData.setErrorMsg("CBZ书籍解压失败");
                                }
                                String chapterFilePath2 = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", bookChapter.getUrl());
                                BookControllerKt.logger.info("chapterFilePath: {}", chapterFilePath2);
                                File chapterFile = new File(chapterFilePath2);
                                if (!chapterFile.exists()) {
                                    return returnData.setErrorMsg("章节文件不存在");
                                }
                                String name = chapterFile.getName();
                                Intrinsics.checkNotNullExpressionValue(name, "chapterFile.name");
                                String fileExt$default = BaseController.getFileExt$default(this, name, null, 2, null);
                                if (fileExt$default == null) {
                                    throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                                }
                                String ext = fileExt$default.toLowerCase(Locale.ROOT);
                                Intrinsics.checkNotNullExpressionValue(ext, "(this as java.lang.Strin….toLowerCase(Locale.ROOT)");
                                List imageExt = CollectionsKt.listOf(new String[]{"jpg", "jpeg", "gif", "png", "bmp", "webp", "svg"});
                                String fileUrl = "__API_ROOT__" + StringsKt.replace$default(StringsKt.replace$default(bookInfo.getBookUrl(), "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null), "storage/data/", "/book-assets/", false, 4, (Object) null) + "/index/" + bookChapter.getUrl();
                                if (!imageExt.contains(ext)) {
                                    return ReturnData.setData$default(returnData, fileUrl, null, 2, null);
                                }
                                String content4 = "<img src='" + fileUrl + "' />";
                                return ReturnData.setData$default(returnData, content4, null, 2, null);
                            }
                            if (!bookInfo.isPdf()) {
                                String bookContent2 = LocalBook.INSTANCE.getContent(bookInfo, bookChapter);
                                if (bookContent2 == null) {
                                    return returnData.setErrorMsg("获取章节内容失败");
                                }
                                content = bookContent2;
                                return ReturnData.setData$default(returnData, content, null, 2, null);
                            }
                            if (!convertPdfToImage$default(this, bookInfo, false, 2, null)) {
                                return returnData.setErrorMsg("PDF生成图片失败");
                            }
                            String content5 = PackageDocumentBase.PREFIX_OPF;
                            if (bookChapter.getStart() != null && bookChapter.getEnd() != null) {
                                Long start = bookChapter.getStart();
                                Intrinsics.checkNotNull(start);
                                long jLongValue = start.longValue();
                                Long end = bookChapter.getEnd();
                                Intrinsics.checkNotNull(end);
                                if (jLongValue <= end.longValue()) {
                                    Long start2 = bookChapter.getStart();
                                    Intrinsics.checkNotNull(start2);
                                    long jLongValue2 = start2.longValue();
                                    Long end2 = bookChapter.getEnd();
                                    Intrinsics.checkNotNull(end2);
                                    long jLongValue3 = end2.longValue();
                                    if (jLongValue2 <= jLongValue3) {
                                        do {
                                            i2 = jLongValue2;
                                            jLongValue2++;
                                            this.convertPdfPageToImage(bookInfo, (int) i2, refresh > 0);
                                            String chapterFilePath3 = ExtKt.getWorkDir(bookInfo.getBookUrl(), "index", "output-" + i2 + ".png");
                                            BookControllerKt.logger.info("chapterFilePath: {}", chapterFilePath3);
                                            if (!new File(chapterFilePath3).exists()) {
                                                return returnData.setErrorMsg("章节文件不存在");
                                            }
                                            content5 = content5 + "<img src='" + ("__API_ROOT__" + StringsKt.replace$default(StringsKt.replace$default(bookInfo.getBookUrl(), "\\", TableOfContents.DEFAULT_PATH_SEPARATOR, false, 4, (Object) null), "storage/data/", "/book-assets/", false, 4, (Object) null) + "/index/output-" + i2 + ".png") + "' />";
                                        } while (i2 != jLongValue3);
                                    }
                                }
                            }
                            return ReturnData.setData$default(returnData, content5, null, 2, null);
                        }
                    }
                    return returnData.setErrorMsg("获取章节链接失败");
                case 1:
                    returnData = (ReturnData) c00311.L$2;
                    context = (RoutingContext) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    objCheckAuth = $result;
                    if (((Boolean) objCheckAuth).booleanValue()) {
                    }
                    break;
                case 2:
                    epubContent = c00311.I$3;
                    refresh = c00311.I$2;
                    cache = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    bookUrl = (String) c00311.L$4;
                    chapterUrl = (String) c00311.L$3;
                    returnData = (ReturnData) c00311.L$2;
                    context = (RoutingContext) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    bookSourceString$default2 = $result;
                    bookSource = (String) bookSourceString$default2;
                    userNameSpace = this.getUserNameSpace(context);
                    i = 0;
                    bookInfo = null;
                    bookChapter = null;
                    url = null;
                    if (bookUrl.length() <= 0) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 3:
                    i = c00311.I$4;
                    epubContent = c00311.I$3;
                    refresh = c00311.I$2;
                    cache = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    url = null;
                    bookChapter = null;
                    bookInfo = (Book) c00311.L$5;
                    userNameSpace = (String) c00311.L$4;
                    bookUrl = (String) c00311.L$3;
                    chapterUrl = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    bookSourceString$default = $result;
                    bookSource = (String) bookSourceString$default;
                    if (chapterUrl.length() != 0) {
                        if (!(bookUrl.length() != 0)) {
                        }
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 4:
                    i = c00311.I$4;
                    epubContent = c00311.I$3;
                    refresh = c00311.I$2;
                    cache = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    bookController = (BookController) c00311.L$5;
                    url = null;
                    bookChapter = null;
                    userNameSpace = (String) c00311.L$4;
                    bookSource = (String) c00311.L$3;
                    chapterUrl = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    bookInfo$default = $result;
                    c00311.L$0 = this;
                    c00311.L$1 = returnData;
                    c00311.L$2 = chapterUrl;
                    c00311.L$3 = bookSource;
                    c00311.L$4 = userNameSpace;
                    c00311.L$5 = null;
                    c00311.I$0 = chapterIndex;
                    c00311.I$1 = cache;
                    c00311.I$2 = refresh;
                    c00311.I$3 = epubContent;
                    c00311.I$4 = i;
                    c00311.label = 5;
                    objMergeBookCacheInfo = bookController.mergeBookCacheInfo((Book) bookInfo$default, c00311);
                    if (objMergeBookCacheInfo == coroutine_suspended) {
                    }
                    book = (Book) objMergeBookCacheInfo;
                    bookInfo = book;
                    BookController bookController322 = this;
                    String str1222 = bookSource;
                    if (str1222 != null) {
                    }
                    c00311.L$0 = this;
                    c00311.L$1 = returnData;
                    c00311.L$2 = chapterUrl;
                    c00311.L$3 = bookSource;
                    c00311.L$4 = userNameSpace;
                    c00311.L$5 = bookInfo;
                    c00311.I$0 = chapterIndex;
                    c00311.I$1 = cache;
                    c00311.I$2 = refresh;
                    c00311.I$3 = epubContent;
                    c00311.I$4 = i;
                    c00311.label = 6;
                    localChapterList$default = getLocalChapterList$default(bookController322, bookInfo, str13, false, userNameSpace, false, null, c00311, 48, null);
                    if (localChapterList$default == coroutine_suspended) {
                    }
                    chapterList = (List) localChapterList$default;
                    if (chapterIndex < chapterList.size()) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 5:
                    i = c00311.I$4;
                    epubContent = c00311.I$3;
                    refresh = c00311.I$2;
                    cache = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    url = null;
                    bookChapter = null;
                    userNameSpace = (String) c00311.L$4;
                    bookSource = (String) c00311.L$3;
                    chapterUrl = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    objMergeBookCacheInfo = $result;
                    book = (Book) objMergeBookCacheInfo;
                    bookInfo = book;
                    BookController bookController3222 = this;
                    String str12222 = bookSource;
                    if (str12222 != null) {
                    }
                    c00311.L$0 = this;
                    c00311.L$1 = returnData;
                    c00311.L$2 = chapterUrl;
                    c00311.L$3 = bookSource;
                    c00311.L$4 = userNameSpace;
                    c00311.L$5 = bookInfo;
                    c00311.I$0 = chapterIndex;
                    c00311.I$1 = cache;
                    c00311.I$2 = refresh;
                    c00311.I$3 = epubContent;
                    c00311.I$4 = i;
                    c00311.label = 6;
                    localChapterList$default = getLocalChapterList$default(bookController3222, bookInfo, str13, false, userNameSpace, false, null, c00311, 48, null);
                    if (localChapterList$default == coroutine_suspended) {
                    }
                    chapterList = (List) localChapterList$default;
                    if (chapterIndex < chapterList.size()) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 6:
                    i = c00311.I$4;
                    epubContent = c00311.I$3;
                    refresh = c00311.I$2;
                    cache = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    url = null;
                    bookChapter = null;
                    bookInfo = (Book) c00311.L$5;
                    userNameSpace = (String) c00311.L$4;
                    bookSource = (String) c00311.L$3;
                    chapterUrl = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    localChapterList$default = $result;
                    chapterList = (List) localChapterList$default;
                    if (chapterIndex < chapterList.size()) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 7:
                    epubContent = c00311.I$2;
                    refresh = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    chapterList = (List) c00311.L$6;
                    url = null;
                    bookChapter = (BookChapter) c00311.L$5;
                    bookInfo = (Book) c00311.L$4;
                    userNameSpace = (String) c00311.L$3;
                    bookSource = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    c00311.L$0 = this;
                    c00311.L$1 = returnData;
                    c00311.L$2 = bookSource;
                    c00311.L$3 = userNameSpace;
                    c00311.L$4 = bookInfo;
                    c00311.L$5 = bookChapter;
                    c00311.L$6 = chapterList;
                    c00311.I$0 = chapterIndex;
                    c00311.I$1 = refresh;
                    c00311.I$2 = epubContent;
                    c00311.label = 8;
                    if (this.saveBookProgressToWebdav(bookInfo, bookChapter, userNameSpace, c00311) == coroutine_suspended) {
                    }
                    chapterUrl = bookChapter.getUrl();
                    if (chapterIndex + 1 < chapterList.size()) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case 8:
                    epubContent = c00311.I$2;
                    refresh = c00311.I$1;
                    chapterIndex = c00311.I$0;
                    chapterList = (List) c00311.L$6;
                    url = null;
                    bookChapter = (BookChapter) c00311.L$5;
                    bookInfo = (Book) c00311.L$4;
                    userNameSpace = (String) c00311.L$3;
                    bookSource = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    chapterUrl = bookChapter.getUrl();
                    if (chapterIndex + 1 < chapterList.size()) {
                    }
                    if (bookInfo != null) {
                    }
                    break;
                case Node.COMMENT /* 9 */:
                    chapterCacheFile = (File) c00311.L$6;
                    bookChapter = (BookChapter) c00311.L$5;
                    bookInfo = (Book) c00311.L$4;
                    userNameSpace = (String) c00311.L$3;
                    bookSource = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    bookContent = $result;
                    content = (String) bookContent;
                    if (this.getAppConfig().getCacheChapterContent()) {
                        FilesKt.writeText$default(chapterCacheFile, content, (Charset) null, 2, (Object) null);
                        bookHelp = BookHelp.INSTANCE;
                        bookController2 = this;
                        String str172 = bookSource;
                        Object objM151fromJsonIoAF18A2 = BookSource.INSTANCE.m151fromJsonIoAF18A(str172 != null ? PackageDocumentBase.PREFIX_OPF : str172);
                        BookSource bookSource32 = (BookSource) (!Result.isFailure-impl(objM151fromJsonIoAF18A2) ? null : objM151fromJsonIoAF18A2);
                        if (bookSource32 != null) {
                        }
                        c00311.L$0 = this;
                        c00311.L$1 = returnData;
                        c00311.L$2 = bookSource;
                        c00311.L$3 = userNameSpace;
                        c00311.L$4 = bookInfo;
                        c00311.L$5 = bookChapter;
                        c00311.L$6 = content;
                        c00311.label = 10;
                        if (bookHelp.saveImages(bookController2, bookSource2, bookInfo, bookChapter, content, c00311) == coroutine_suspended) {
                        }
                        content = this.updateImageLinkInContent(bookInfo, bookChapter, content);
                        break;
                    }
                    return ReturnData.setData$default(returnData, content, null, 2, null);
                case Node.DOCDECL /* 10 */:
                    content = (String) c00311.L$6;
                    bookChapter = (BookChapter) c00311.L$5;
                    bookInfo = (Book) c00311.L$4;
                    userNameSpace = (String) c00311.L$3;
                    bookSource = (String) c00311.L$2;
                    returnData = (ReturnData) c00311.L$1;
                    this = (BookController) c00311.L$0;
                    ResultKt.throwOnFailure($result);
                    content = this.updateImageLinkInContent(bookInfo, bookChapter, content);
                    return ReturnData.setData$default(returnData, content, null, 2, null);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Exception e) {
            String str18 = bookSource;
            if (!(str18 == null || str18.length() == 0)) {
                Object objM151fromJsonIoAF18A3 = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSource);
                BookSource bookSourceObject = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A3) ? null : objM151fromJsonIoAF18A3);
                if (bookSourceObject != null) {
                    this.addInvalidBookSource(bookSourceObject.getBookSourceUrl(), MapsKt.mutableMapOf(new Pair[]{TuplesKt.to("sourceUrl", bookSourceObject.getBookSourceUrl()), TuplesKt.to(RSSKeywords.RSS_ITEM_TIME, Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to("error", e.toString())}), userNameSpace);
                }
            }
            throw e;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookContent(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00551 c00551;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00551) {
            c00551 = (C00551) $completion;
            if ((c00551.label & Integer.MIN_VALUE) != 0) {
                c00551.label -= Integer.MIN_VALUE;
            } else {
                c00551 = new C00551($completion);
            }
        }
        Object $result = c00551.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00551.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00551.L$0 = this;
                c00551.L$1 = context;
                c00551.L$2 = returnData;
                c00551.label = 1;
                objCheckAuth = checkAuth(context, c00551);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00551.L$2;
                context = (RoutingContext) c00551.L$1;
                this = (BookController) c00551.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
        String bookUrl = string == null ? PackageDocumentBase.PREFIX_OPF : string;
        Integer chapterIndex = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
        String string2 = context.getBodyAsJson().getString("content");
        String content = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
        if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
        }
        String userNameSpace = this.getUserNameSpace(context);
        Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
        if (bookInfo == null) {
            return returnData.setErrorMsg("获取书籍信息失败");
        }
        File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
        File chapterCacheFile = new File(localCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
        FilesKt.writeText$default(chapterCacheFile, content, (Charset) null, 2, (Object) null);
        String customCacheDirPath = ExtKt.getWorkDir("storage", "data", userNameSpace, bookInfo.getName() + '_' + bookInfo.getAuthor(), "custom");
        File customCacheDir = new File(customCacheDirPath);
        if (!customCacheDir.exists()) {
            customCacheDir.mkdirs();
        }
        File cacheFile = new File(customCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
        FilesKt.writeText$default(cacheFile, content, (Charset) null, 2, (Object) null);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    private final String updateImageLinkInContent(Book book, BookChapter chapter, String content) {
        StringBuilder data = new StringBuilder(PackageDocumentBase.PREFIX_OPF);
        String dataDir = ExtKt.getWorkDir("storage", "data");
        Iterable $this$forEach$iv = StringsKt.split$default(content, new String[]{"\n"}, false, 0, 6, (Object) null);
        for (Object element$iv : $this$forEach$iv) {
            String text = (String) element$iv;
            String strReplace$default = text;
            Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(text);
            while (matcher.find()) {
                String it = matcher.group(1);
                if (it != null && StringsKt.indexOf$default(it, "__API_ROOT__", 0, false, 6, (Object) null) < 0) {
                    String src = NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), it);
                    File imageFile = BookHelp.INSTANCE.getImage(book, src);
                    if (imageFile.exists()) {
                        String path = imageFile.getPath();
                        Intrinsics.checkNotNullExpressionValue(path, "imageFile.path");
                        String imageUrl = Intrinsics.stringPlus("__API_ROOT__", StringsKt.replace$default(path, dataDir, "/book-assets", false, 4, (Object) null));
                        strReplace$default = StringsKt.replace$default(strReplace$default, it, imageUrl + "\" data-error=\"" + it, false, 4, (Object) null);
                    }
                }
            }
            data.append(strReplace$default).append("\n");
        }
        String string = data.toString();
        Intrinsics.checkNotNullExpressionValue(string, "data.toString()");
        return string;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exploreBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00221 c00221;
        ReturnData returnData;
        Object objExploreBook;
        Object bookSourceString$default;
        String str;
        String ruleFindUrl;
        Integer numBoxInt;
        int page;
        if ($completion instanceof C00221) {
            c00221 = (C00221) $completion;
            if ((c00221.label & Integer.MIN_VALUE) != 0) {
                c00221.label -= Integer.MIN_VALUE;
            } else {
                c00221 = new C00221($completion);
            }
        }
        Object $result = c00221.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00221.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00221.L$0 = this;
                c00221.L$1 = context;
                c00221.L$2 = returnData;
                c00221.label = 1;
                if (checkAuth(context, c00221) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                c00221.L$0 = this;
                c00221.L$1 = context;
                c00221.L$2 = returnData;
                c00221.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, null, false, c00221, 6, null);
                if (bookSourceString$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String bookSource = (String) bookSourceString$default;
                str = bookSource;
                if (!(str == null || str.length() == 0)) {
                    return returnData.setErrorMsg("未配置书源");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("ruleFindUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"ruleFindUrl\")");
                    ruleFindUrl = string;
                    Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                    page = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam("ruleFindUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"ruleFindUrl\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    ruleFindUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam2 = context.queryParam("page");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"page\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str3 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 1 : numBoxInt.intValue();
                    page = iIntValue;
                }
                String userNameSpace = this.getUserNameSpace(context);
                c00221.L$0 = returnData;
                c00221.L$1 = null;
                c00221.L$2 = null;
                c00221.label = 3;
                objExploreBook = new WebBook(bookSource, false, (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null).exploreBook(ruleFindUrl, Boxing.boxInt(page), c00221);
                if (objExploreBook == coroutine_suspended) {
                    return coroutine_suspended;
                }
                List result = (List) objExploreBook;
                return ReturnData.setData$default(returnData, result, null, 2, null);
            case 1:
                returnData = (ReturnData) c00221.L$2;
                context = (RoutingContext) c00221.L$1;
                this = (BookController) c00221.L$0;
                ResultKt.throwOnFailure($result);
                c00221.L$0 = this;
                c00221.L$1 = context;
                c00221.L$2 = returnData;
                c00221.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, null, false, c00221, 6, null);
                if (bookSourceString$default == coroutine_suspended) {
                }
                String bookSource2 = (String) bookSourceString$default;
                str = bookSource2;
                if (str == null) {
                    if (!(str == null || str.length() == 0)) {
                    }
                }
                List result2 = (List) objExploreBook;
                return ReturnData.setData$default(returnData, result2, null, 2, null);
            case 2:
                returnData = (ReturnData) c00221.L$2;
                context = (RoutingContext) c00221.L$1;
                this = (BookController) c00221.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                String bookSource22 = (String) bookSourceString$default;
                str = bookSource22;
                List result22 = (List) objExploreBook;
                return ReturnData.setData$default(returnData, result22, null, 2, null);
            case 3:
                returnData = (ReturnData) c00221.L$0;
                ResultKt.throwOnFailure($result);
                objExploreBook = $result;
                List result222 = (List) objExploreBook;
                return ReturnData.setData$default(returnData, result222, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0128  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0138  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00651 c00651;
        ReturnData returnData;
        Object objSearchBook;
        Object bookSourceString$default;
        String str;
        String key;
        Integer numBoxInt;
        int page;
        if ($completion instanceof C00651) {
            c00651 = (C00651) $completion;
            if ((c00651.label & Integer.MIN_VALUE) != 0) {
                c00651.label -= Integer.MIN_VALUE;
            } else {
                c00651 = new C00651($completion);
            }
        }
        Object $result = c00651.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00651.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00651.L$0 = this;
                c00651.L$1 = context;
                c00651.L$2 = returnData;
                c00651.label = 1;
                if (checkAuth(context, c00651) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                c00651.L$0 = this;
                c00651.L$1 = context;
                c00651.L$2 = returnData;
                c00651.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, null, false, c00651, 6, null);
                if (bookSourceString$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String bookSource = (String) bookSourceString$default;
                str = bookSource;
                if (!(str == null || str.length() == 0)) {
                    return returnData.setErrorMsg("未配置书源");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("key");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"key\")");
                    key = string;
                    Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                    page = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam("key");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"key\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    key = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam2 = context.queryParam("page");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"page\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str3 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 1 : numBoxInt.intValue();
                    page = iIntValue;
                }
                if (key.length() == 0) {
                    return returnData.setErrorMsg("请输入搜索关键字");
                }
                String userNameSpace = this.getUserNameSpace(context);
                c00651.L$0 = returnData;
                c00651.L$1 = null;
                c00651.L$2 = null;
                c00651.label = 3;
                objSearchBook = new WebBook(bookSource, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null).searchBook(key, Boxing.boxInt(page), c00651);
                if (objSearchBook == coroutine_suspended) {
                    return coroutine_suspended;
                }
                List result = (List) objSearchBook;
                return ReturnData.setData$default(returnData, result, null, 2, null);
            case 1:
                returnData = (ReturnData) c00651.L$2;
                context = (RoutingContext) c00651.L$1;
                this = (BookController) c00651.L$0;
                ResultKt.throwOnFailure($result);
                c00651.L$0 = this;
                c00651.L$1 = context;
                c00651.L$2 = returnData;
                c00651.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, null, false, c00651, 6, null);
                if (bookSourceString$default == coroutine_suspended) {
                }
                String bookSource2 = (String) bookSourceString$default;
                str = bookSource2;
                if (str == null) {
                    if (!(str == null || str.length() == 0)) {
                    }
                }
                List result2 = (List) objSearchBook;
                return ReturnData.setData$default(returnData, result2, null, 2, null);
            case 2:
                returnData = (ReturnData) c00651.L$2;
                context = (RoutingContext) c00651.L$1;
                this = (BookController) c00651.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                String bookSource22 = (String) bookSourceString$default;
                str = bookSource22;
                List result22 = (List) objSearchBook;
                return ReturnData.setData$default(returnData, result22, null, 2, null);
            case 3:
                returnData = (ReturnData) c00651.L$0;
                ResultKt.throwOnFailure($result);
                objSearchBook = $result;
                List result222 = (List) objSearchBook;
                return ReturnData.setData$default(returnData, result222, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00671 c00671;
        final Ref.ObjectRef resultList;
        Ref.IntRef lastIndex;
        ReturnData returnData;
        Object objCheckAuth;
        String key;
        Integer numBoxInt;
        Integer numBoxInt2;
        Integer numBoxInt3;
        int concurrentCount;
        if ($completion instanceof C00671) {
            c00671 = (C00671) $completion;
            if ((c00671.label & Integer.MIN_VALUE) != 0) {
                c00671.label -= Integer.MIN_VALUE;
            } else {
                c00671 = new C00671($completion);
            }
        }
        Object $result = c00671.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00671.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00671.L$0 = this;
                c00671.L$1 = context;
                c00671.L$2 = returnData;
                c00671.label = 1;
                objCheckAuth = checkAuth(context, c00671);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                lastIndex = new Ref.IntRef();
                final Ref.IntRef searchSize = new Ref.IntRef();
                Ref.ObjectRef bookSourceGroup = new Ref.ObjectRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("key", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"key\", \"\")");
                    key = string;
                    String string2 = context.getBodyAsJson().getString("bookSourceGroup", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                    bookSourceGroup.element = string2;
                    Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                    lastIndex.element = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(20));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"searchSize\", 20)");
                    searchSize.element = integer2.intValue();
                    Integer integer3 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(36));
                    Intrinsics.checkNotNullExpressionValue(integer3, "context.bodyAsJson.getInteger(\"concurrentCount\", 36)");
                    concurrentCount = integer3.intValue();
                } else {
                    List listQueryParam = context.queryParam("key");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"key\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    key = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("bookSourceGroup");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"bookSourceGroup\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    bookSourceGroup.element = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam3 = context.queryParam("lastIndex");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"lastIndex\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue = (str3 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str3))) == null) ? -1 : numBoxInt.intValue();
                    lastIndex.element = iIntValue;
                    List listQueryParam4 = context.queryParam("searchSize");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"searchSize\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    int iIntValue2 = (str4 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str4))) == null) ? 20 : numBoxInt2.intValue();
                    searchSize.element = iIntValue2;
                    List listQueryParam5 = context.queryParam("concurrentCount");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam5, "context.queryParam(\"concurrentCount\")");
                    String str5 = (String) CollectionsKt.firstOrNull(listQueryParam5);
                    int iIntValue3 = (str5 == null || (numBoxInt3 = Boxing.boxInt(Integer.parseInt(str5))) == null) ? 36 : numBoxInt3.intValue();
                    concurrentCount = iIntValue3;
                }
                Ref.ObjectRef userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                Map<String, Integer> bookSourceMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String) userNameSpace.element);
                if (bookSourceMap.size() <= 0) {
                    return returnData.setErrorMsg("未配置书源");
                }
                if (key.length() == 0) {
                    return returnData.setErrorMsg("请输入搜索关键字");
                }
                Ref.BooleanRef accurate = new Ref.BooleanRef();
                if (StringsKt.startsWith(key, "=", true)) {
                    accurate.element = true;
                    key = StringsKt.replaceFirst$default(key, "=", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
                }
                String str6 = key;
                if (str6 == null || str6.length() == 0) {
                    return returnData.setErrorMsg("请输入搜索关键字");
                }
                if (lastIndex.element >= bookSourceMap.size() - 1) {
                    return returnData.setErrorMsg("没有更多了");
                }
                searchSize.element = searchSize.element > 0 ? searchSize.element : 20;
                int concurrentCount2 = concurrentCount > 0 ? concurrentCount : 36;
                BookControllerKt.logger.info("searchBookMulti from lastIndex: {} searchSize: {}", Boxing.boxInt(lastIndex.element), Boxing.boxInt(searchSize.element));
                final Ref.BooleanRef isEnd = new Ref.BooleanRef();
                BookController bookController = this;
                context.request().connection().closeHandler((v2) -> {
                    m18searchBookMulti$lambda5(r1, r2, v2);
                });
                resultList = new Ref.ObjectRef();
                resultList.element = new ArrayList();
                final Ref.ObjectRef resultMap = new Ref.ObjectRef();
                resultMap.element = new LinkedHashMap();
                Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
                book.setName(key);
                Ref.IntRef maxSize = new Ref.IntRef();
                maxSize.element = bookSourceMap.size();
                Ref.ObjectRef bookSourceFile = new Ref.ObjectRef();
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", (String) userNameSpace.element, "bookSource"}, null, 2, null);
                if (!((File) bookSourceFile.element).exists()) {
                    bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
                }
                final BookController bookController2 = this;
                c00671.L$0 = returnData;
                c00671.L$1 = lastIndex;
                c00671.L$2 = resultList;
                c00671.label = 2;
                if (this.limitConcurrent(concurrentCount2, lastIndex.element + 1, bookSourceMap.size(), new C00683(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, accurate, userNameSpace, null), new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.searchBookMulti.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                    }

                    public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                        Intrinsics.checkNotNullParameter(list, "list");
                        ArrayList<Object> $this$forEach$iv = list;
                        Ref.ObjectRef<Map<String, Integer>> objectRef = resultMap;
                        Ref.ObjectRef<ArrayList<SearchBook>> objectRef2 = resultList;
                        for (Object element$iv : $this$forEach$iv) {
                            Iterable bookList = element$iv instanceof Collection ? (Collection) element$iv : null;
                            if (bookList != null) {
                                Iterable<SearchBook> $this$forEach$iv2 = bookList;
                                for (SearchBook book2 : $this$forEach$iv2) {
                                    String bookKey = book2.getName() + '_' + book2.getAuthor();
                                    if (!((Map) objectRef.element).containsKey(bookKey)) {
                                        ((ArrayList) objectRef2.element).add(book2);
                                        ((Map) objectRef.element).put(bookKey, 1);
                                    }
                                }
                            }
                        }
                        BookControllerKt.logger.info("Loop: {} resultList.size: {}", Integer.valueOf(loopCount), Integer.valueOf(((ArrayList) resultList.element).size()));
                        return !isEnd.element && loopCount < bookController2.concurrentLoopCount && ((ArrayList) resultList.element).size() < searchSize.element;
                    }
                }, c00671) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}), null, 2, null);
            case 1:
                returnData = (ReturnData) c00671.L$2;
                context = (RoutingContext) c00671.L$1;
                this = (BookController) c00671.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                resultList = (Ref.ObjectRef) c00671.L$2;
                lastIndex = (Ref.IntRef) c00671.L$1;
                returnData = (ReturnData) c00671.L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}), null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: searchBookMulti$lambda-5, reason: not valid java name */
    private static final void m18searchBookMulti$lambda5(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 searchBookMulti");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookMulti$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookMulti$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {853}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$searchBookMulti$3")
    static final class C00683 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.IntRef $maxSize;
        final /* synthetic */ Ref.IntRef $lastIndex;
        final /* synthetic */ Ref.ObjectRef<File> $bookSourceFile;
        final /* synthetic */ Ref.ObjectRef<String> $bookSourceGroup;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Book $book;
        final /* synthetic */ Ref.BooleanRef $accurate;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00683(Ref.IntRef $maxSize, Ref.IntRef $lastIndex, Ref.ObjectRef<File> $bookSourceFile, Ref.ObjectRef<String> $bookSourceGroup, BookController this$0, Book $book, Ref.BooleanRef $accurate, Ref.ObjectRef<String> $userNameSpace, Continuation<? super C00683> $completion) {
            super(3, $completion);
            this.$maxSize = $maxSize;
            this.$lastIndex = $lastIndex;
            this.$bookSourceFile = $bookSourceFile;
            this.$bookSourceGroup = $bookSourceGroup;
            this.this$0 = this$0;
            this.$book = $book;
            this.$accurate = $accurate;
            this.$userNameSpace = $userNameSpace;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00683 c00683 = new C00683(this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$accurate, this.$userNameSpace, p3);
            c00683.I$0 = p2;
            return c00683.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object objSearchBookWithSource;
            Function1<ObjectNode, Boolean> function1;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    int it = this.I$0;
                    if (it > this.$maxSize.element) {
                        return new ArrayList();
                    }
                    this.$lastIndex.element = Math.max(this.$lastIndex.element, it);
                    File file = (File) this.$bookSourceFile.element;
                    if (((CharSequence) this.$bookSourceGroup.element).length() == 0) {
                        function1 = null;
                    } else {
                        final Ref.ObjectRef<String> objectRef = this.$bookSourceGroup;
                        function1 = new Function1<ObjectNode, Boolean>() { // from class: com.htmake.reader.api.controller.BookController$searchBookMulti$3$bookSourceList$1
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                                return Boolean.valueOf(invoke((ObjectNode) p1));
                            }

                            public final boolean invoke(@NotNull ObjectNode it2) {
                                Intrinsics.checkNotNullParameter(it2, "it");
                                String _bookSourceGroup = it2.get("bookSourceGroup").asText();
                                String str = _bookSourceGroup;
                                return !(str == null || str.length() == 0) && StringsKt.indexOf$default(Intrinsics.stringPlus(_bookSourceGroup, ","), Intrinsics.stringPlus((String) objectRef.element, ","), 0, false, 6, (Object) null) >= 0;
                            }
                        };
                    }
                    JsonArray bookSourceList = ExtKt.parseJsonStringList$default(file, null, null, it, it, null, function1, 38, null);
                    if (bookSourceList == null || bookSourceList.isEmpty()) {
                        this.$maxSize.element = it;
                        return new ArrayList();
                    }
                    BookController bookController = this.this$0;
                    String string = bookSourceList.getString(0);
                    Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getString(0)");
                    this.label = 1;
                    objSearchBookWithSource = bookController.searchBookWithSource(string, this.$book, this.$accurate.element, (String) this.$userNameSpace.element, (Continuation) this);
                    if (objSearchBookWithSource == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSearchBookWithSource = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return (ArrayList) objSearchBookWithSource;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0714  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookMultiSSE(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C00701 c00701;
        Ref.IntRef maxSize;
        final Ref.IntRef lastIndex;
        HttpServerResponse response;
        ReturnData returnData;
        Object objCheckAuth;
        String key;
        Integer numBoxInt;
        Integer numBoxInt2;
        Integer numBoxInt3;
        int concurrentCount;
        if ($completion instanceof C00701) {
            c00701 = (C00701) $completion;
            if ((c00701.label & Integer.MIN_VALUE) != 0) {
                c00701.label -= Integer.MIN_VALUE;
            } else {
                c00701 = new C00701($completion);
            }
        }
        Object $result = c00701.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00701.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                response = context.response().putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                c00701.L$0 = this;
                c00701.L$1 = context;
                c00701.L$2 = returnData;
                c00701.L$3 = response;
                c00701.label = 1;
                objCheckAuth = checkAuth(context, c00701);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                lastIndex = new Ref.IntRef();
                final Ref.IntRef searchSize = new Ref.IntRef();
                Ref.ObjectRef bookSourceGroup = new Ref.ObjectRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("key", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"key\", \"\")");
                    key = string;
                    String string2 = context.getBodyAsJson().getString("bookSourceGroup", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                    bookSourceGroup.element = string2;
                    Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                    lastIndex.element = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(50));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"searchSize\", 50)");
                    searchSize.element = integer2.intValue();
                    Integer integer3 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
                    Intrinsics.checkNotNullExpressionValue(integer3, "context.bodyAsJson.getInteger(\"concurrentCount\", 24)");
                    concurrentCount = integer3.intValue();
                } else {
                    List listQueryParam = context.queryParam("key");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"key\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    key = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("bookSourceGroup");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"bookSourceGroup\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    bookSourceGroup.element = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam3 = context.queryParam("lastIndex");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"lastIndex\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue = (str3 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str3))) == null) ? -1 : numBoxInt.intValue();
                    lastIndex.element = iIntValue;
                    List listQueryParam4 = context.queryParam("searchSize");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"searchSize\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    int iIntValue2 = (str4 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str4))) == null) ? 50 : numBoxInt2.intValue();
                    searchSize.element = iIntValue2;
                    List listQueryParam5 = context.queryParam("concurrentCount");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam5, "context.queryParam(\"concurrentCount\")");
                    String str5 = (String) CollectionsKt.firstOrNull(listQueryParam5);
                    int iIntValue3 = (str5 == null || (numBoxInt3 = Boxing.boxInt(Integer.parseInt(str5))) == null) ? 24 : numBoxInt3.intValue();
                    concurrentCount = iIntValue3;
                }
                Ref.ObjectRef userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                Map<String, Integer> bookSourceMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String) userNameSpace.element);
                if (bookSourceMap.size() <= 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (key.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键字"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                Ref.BooleanRef accurate = new Ref.BooleanRef();
                if (StringsKt.startsWith(key, "=", true)) {
                    accurate.element = true;
                    key = StringsKt.replaceFirst$default(key, "=", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
                }
                String str6 = key;
                if (str6 == null || str6.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键字"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (lastIndex.element >= bookSourceMap.size() - 1) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("没有更多了"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                searchSize.element = searchSize.element > 0 ? searchSize.element : 50;
                int concurrentCount2 = concurrentCount > 0 ? concurrentCount : 24;
                BookControllerKt.logger.info("searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}", new Object[]{Boxing.boxInt(lastIndex.element), Boxing.boxInt(concurrentCount2), Boxing.boxInt(searchSize.element)});
                final Ref.BooleanRef isEnd = new Ref.BooleanRef();
                BookController bookController = this;
                context.request().connection().closeHandler((v2) -> {
                    m19searchBookMultiSSE$lambda6(r1, r2, v2);
                });
                final Ref.ObjectRef resultList = new Ref.ObjectRef();
                resultList.element = new ArrayList();
                Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
                book.setName(key);
                maxSize = new Ref.IntRef();
                maxSize.element = bookSourceMap.size();
                Ref.ObjectRef bookSourceFile = new Ref.ObjectRef();
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", (String) userNameSpace.element, "bookSource"}, null, 2, null);
                if (!((File) bookSourceFile.element).exists()) {
                    bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
                }
                final HttpServerResponse httpServerResponse = response;
                final BookController bookController2 = this;
                c00701.L$0 = response;
                c00701.L$1 = lastIndex;
                c00701.L$2 = maxSize;
                c00701.L$3 = null;
                c00701.label = 2;
                if (this.limitConcurrent(concurrentCount2, lastIndex.element + 1, bookSourceMap.size(), new C00713(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, accurate, userNameSpace, null), new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.searchBookMultiSSE.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                    }

                    public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                        Intrinsics.checkNotNullParameter(list, "list");
                        ArrayList loopResult = new ArrayList();
                        ArrayList<Object> $this$forEach$iv = list;
                        Ref.ObjectRef<ArrayList<SearchBook>> objectRef = resultList;
                        for (Object element$iv : $this$forEach$iv) {
                            Iterable bookList = element$iv instanceof Collection ? (Collection) element$iv : null;
                            if (bookList != null) {
                                Iterable<SearchBook> $this$forEach$iv2 = bookList;
                                for (SearchBook book2 : $this$forEach$iv2) {
                                    String str7 = book2.getName() + '_' + book2.getAuthor();
                                    ((ArrayList) objectRef.element).add(book2);
                                    loopResult.add(book2);
                                }
                            }
                        }
                        httpServerResponse.write("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Integer.valueOf(lastIndex.element)), TuplesKt.to("data", loopResult)}), false) + "\n\n");
                        BookControllerKt.logger.info("Loop: {} resultList.size: {}", Integer.valueOf(loopCount), Integer.valueOf(((ArrayList) resultList.element).size()));
                        return !isEnd.element && loopCount < bookController2.concurrentLoopCount && ((ArrayList) resultList.element).size() < searchSize.element;
                    }
                }, c00701) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                response.write("event: end\n");
                HttpServerResponse httpServerResponse2 = response;
                StringBuilder sbAppend = new StringBuilder().append("data: ");
                Pair[] pairArr = new Pair[2];
                pairArr[0] = TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element));
                pairArr[1] = TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element < maxSize.element));
                httpServerResponse2.end(sbAppend.append(ExtKt.jsonEncode(MapsKt.mapOf(pairArr), false)).append("\n\n").toString());
                return Unit.INSTANCE;
            case 1:
                response = (HttpServerResponse) c00701.L$3;
                returnData = (ReturnData) c00701.L$2;
                context = (RoutingContext) c00701.L$1;
                this = (BookController) c00701.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                maxSize = (Ref.IntRef) c00701.L$2;
                lastIndex = (Ref.IntRef) c00701.L$1;
                response = (HttpServerResponse) c00701.L$0;
                ResultKt.throwOnFailure($result);
                response.write("event: end\n");
                HttpServerResponse httpServerResponse22 = response;
                StringBuilder sbAppend2 = new StringBuilder().append("data: ");
                Pair[] pairArr2 = new Pair[2];
                pairArr2[0] = TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element));
                pairArr2[1] = TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element < maxSize.element));
                httpServerResponse22.end(sbAppend2.append(ExtKt.jsonEncode(MapsKt.mapOf(pairArr2), false)).append("\n\n").toString());
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: searchBookMultiSSE$lambda-6, reason: not valid java name */
    private static final void m19searchBookMultiSSE$lambda6(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 searchBookMultiSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookMultiSSE$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookMultiSSE$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {972}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$searchBookMultiSSE$3")
    static final class C00713 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.IntRef $maxSize;
        final /* synthetic */ Ref.IntRef $lastIndex;
        final /* synthetic */ Ref.ObjectRef<File> $bookSourceFile;
        final /* synthetic */ Ref.ObjectRef<String> $bookSourceGroup;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Book $book;
        final /* synthetic */ Ref.BooleanRef $accurate;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00713(Ref.IntRef $maxSize, Ref.IntRef $lastIndex, Ref.ObjectRef<File> $bookSourceFile, Ref.ObjectRef<String> $bookSourceGroup, BookController this$0, Book $book, Ref.BooleanRef $accurate, Ref.ObjectRef<String> $userNameSpace, Continuation<? super C00713> $completion) {
            super(3, $completion);
            this.$maxSize = $maxSize;
            this.$lastIndex = $lastIndex;
            this.$bookSourceFile = $bookSourceFile;
            this.$bookSourceGroup = $bookSourceGroup;
            this.this$0 = this$0;
            this.$book = $book;
            this.$accurate = $accurate;
            this.$userNameSpace = $userNameSpace;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00713 c00713 = new C00713(this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$accurate, this.$userNameSpace, p3);
            c00713.I$0 = p2;
            return c00713.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object objSearchBookWithSource;
            Function1<ObjectNode, Boolean> function1;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    int it = this.I$0;
                    if (it > this.$maxSize.element) {
                        return new ArrayList();
                    }
                    this.$lastIndex.element = Math.max(this.$lastIndex.element, it);
                    File file = (File) this.$bookSourceFile.element;
                    if (((CharSequence) this.$bookSourceGroup.element).length() == 0) {
                        function1 = null;
                    } else {
                        final Ref.ObjectRef<String> objectRef = this.$bookSourceGroup;
                        function1 = new Function1<ObjectNode, Boolean>() { // from class: com.htmake.reader.api.controller.BookController$searchBookMultiSSE$3$bookSourceList$1
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                                return Boolean.valueOf(invoke((ObjectNode) p1));
                            }

                            public final boolean invoke(@NotNull ObjectNode it2) {
                                Intrinsics.checkNotNullParameter(it2, "it");
                                String _bookSourceGroup = it2.get("bookSourceGroup").asText();
                                String str = _bookSourceGroup;
                                return !(str == null || str.length() == 0) && StringsKt.indexOf$default(Intrinsics.stringPlus(_bookSourceGroup, ","), Intrinsics.stringPlus((String) objectRef.element, ","), 0, false, 6, (Object) null) >= 0;
                            }
                        };
                    }
                    JsonArray bookSourceList = ExtKt.parseJsonStringList$default(file, null, null, it, it, null, function1, 38, null);
                    if (bookSourceList == null || bookSourceList.isEmpty()) {
                        this.$maxSize.element = it;
                        return new ArrayList();
                    }
                    BookController bookController = this.this$0;
                    String string = bookSourceList.getString(0);
                    Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getString(0)");
                    this.label = 1;
                    objSearchBookWithSource = bookController.searchBookWithSource(string, this.$book, this.$accurate.element, (String) this.$userNameSpace.element, (Continuation) this);
                    if (objSearchBookWithSource == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSearchBookWithSource = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return (ArrayList) objSearchBookWithSource;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Type inference failed for: r3v25, types: [com.htmake.reader.api.controller.BookController$searchBookSource$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00731 c00731;
        final Ref.ObjectRef resultList;
        Ref.ObjectRef book;
        Ref.ObjectRef userNameSpace;
        Ref.IntRef lastIndex;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        Integer numBoxInt;
        Integer numBoxInt2;
        Object map;
        String json;
        Book book2;
        if ($completion instanceof C00731) {
            c00731 = (C00731) $completion;
            if ((c00731.label & Integer.MIN_VALUE) != 0) {
                c00731.label -= Integer.MIN_VALUE;
            } else {
                c00731 = new C00731($completion);
            }
        }
        Object $result = c00731.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00731.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00731.L$0 = this;
                c00731.L$1 = context;
                c00731.L$2 = returnData;
                c00731.label = 1;
                objCheckAuth = checkAuth(context, c00731);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                lastIndex = new Ref.IntRef();
                final Ref.IntRef searchSize = new Ref.IntRef();
                Ref.ObjectRef bookSourceGroup = new Ref.ObjectRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"url\")");
                    bookUrl = string;
                    Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                    lastIndex.element = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(5));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"searchSize\", 5)");
                    searchSize.element = integer2.intValue();
                    String string2 = context.getBodyAsJson().getString("bookSourceGroup", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                    bookSourceGroup.element = string2;
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("lastIndex");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"lastIndex\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? -1 : numBoxInt.intValue();
                    lastIndex.element = iIntValue;
                    List listQueryParam3 = context.queryParam("searchSize");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"searchSize\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue2 = (str3 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 5 : numBoxInt2.intValue();
                    searchSize.element = iIntValue2;
                    List listQueryParam4 = context.queryParam("bookSourceGroup");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"bookSourceGroup\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    bookSourceGroup.element = str4 == null ? PackageDocumentBase.PREFIX_OPF : str4;
                }
                userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                Map<String, Integer> bookSourceMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String) userNameSpace.element);
                if (bookSourceMap.size() <= 0) {
                    return returnData.setErrorMsg("未配置书源");
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                if (lastIndex.element >= bookSourceMap.size() - 1) {
                    return returnData.setErrorMsg("没有更多了");
                }
                book = new Ref.ObjectRef();
                book.element = this.getShelfBookByURL(bookUrl, (String) userNameSpace.element);
                if (book.element == null) {
                    String asString = this.bookInfoCache.getAsString(bookUrl);
                    if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                        book2 = null;
                    } else {
                        if (!(map instanceof String)) {
                            json = ExtKt.getGson().toJson(map);
                        } else {
                            json = (String) map;
                        }
                        String json$iv$iv = json;
                        book2 = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$searchBookSource$$inlined$toDataClass$1
                        }.getType());
                    }
                    book.element = book2;
                }
                if (book.element != null) {
                    BookControllerKt.logger.info("searchBookSource from lastIndex: {}", Boxing.boxInt(lastIndex.element));
                    final Ref.BooleanRef isEnd = new Ref.BooleanRef();
                    BookController bookController = this;
                    context.request().connection().closeHandler((v2) -> {
                        m20searchBookSource$lambda7(r1, r2, v2);
                    });
                    searchSize.element = searchSize.element > 0 ? searchSize.element : 5;
                    resultList = new Ref.ObjectRef();
                    resultList.element = new ArrayList();
                    int concurrentCount = Math.max(searchSize.element * 2, 24);
                    Ref.IntRef maxSize = new Ref.IntRef();
                    maxSize.element = bookSourceMap.size();
                    Ref.ObjectRef bookSourceFile = new Ref.ObjectRef();
                    bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", (String) userNameSpace.element, "bookSource"}, null, 2, null);
                    if (!((File) bookSourceFile.element).exists()) {
                        bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
                    }
                    final BookController bookController2 = this;
                    c00731.L$0 = this;
                    c00731.L$1 = returnData;
                    c00731.L$2 = lastIndex;
                    c00731.L$3 = userNameSpace;
                    c00731.L$4 = book;
                    c00731.L$5 = resultList;
                    c00731.label = 2;
                    if (this.limitConcurrent(concurrentCount, lastIndex.element + 1, bookSourceMap.size(), new C00743(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, userNameSpace, null), new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.searchBookSource.4
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(2);
                        }

                        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                            return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                        }

                        public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                            Intrinsics.checkNotNullParameter(list, "list");
                            ArrayList<Object> $this$forEach$iv = list;
                            Ref.ObjectRef<ArrayList<SearchBook>> objectRef = resultList;
                            for (Object element$iv : $this$forEach$iv) {
                                Collection bookList = element$iv instanceof Collection ? (Collection) element$iv : null;
                                if (bookList != null) {
                                    ((ArrayList) objectRef.element).addAll(bookList);
                                }
                            }
                            return !isEnd.element && loopCount < bookController2.concurrentLoopCount && ((ArrayList) resultList.element).size() < searchSize.element;
                        }
                    }, c00731) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    saveBookSources$default(this, (Book) book.element, (List) resultList.element, (String) userNameSpace.element, false, 8, null);
                    return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}), null, 2, null);
                }
                return returnData.setErrorMsg("书籍信息错误");
            case 1:
                returnData = (ReturnData) c00731.L$2;
                context = (RoutingContext) c00731.L$1;
                this = (BookController) c00731.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                resultList = (Ref.ObjectRef) c00731.L$5;
                book = (Ref.ObjectRef) c00731.L$4;
                userNameSpace = (Ref.ObjectRef) c00731.L$3;
                lastIndex = (Ref.IntRef) c00731.L$2;
                returnData = (ReturnData) c00731.L$1;
                this = (BookController) c00731.L$0;
                ResultKt.throwOnFailure($result);
                saveBookSources$default(this, (Book) book.element, (List) resultList.element, (String) userNameSpace.element, false, 8, null);
                return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element)), TuplesKt.to("list", resultList.element)}), null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: searchBookSource$lambda-7, reason: not valid java name */
    private static final void m20searchBookSource$lambda7(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 searchBookSource");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookSource$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookSource$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {1084}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$searchBookSource$3")
    static final class C00743 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.IntRef $maxSize;
        final /* synthetic */ Ref.IntRef $lastIndex;
        final /* synthetic */ Ref.ObjectRef<File> $bookSourceFile;
        final /* synthetic */ Ref.ObjectRef<String> $bookSourceGroup;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Ref.ObjectRef<Book> $book;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00743(Ref.IntRef $maxSize, Ref.IntRef $lastIndex, Ref.ObjectRef<File> $bookSourceFile, Ref.ObjectRef<String> $bookSourceGroup, BookController this$0, Ref.ObjectRef<Book> $book, Ref.ObjectRef<String> $userNameSpace, Continuation<? super C00743> $completion) {
            super(3, $completion);
            this.$maxSize = $maxSize;
            this.$lastIndex = $lastIndex;
            this.$bookSourceFile = $bookSourceFile;
            this.$bookSourceGroup = $bookSourceGroup;
            this.this$0 = this$0;
            this.$book = $book;
            this.$userNameSpace = $userNameSpace;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00743 c00743 = new C00743(this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$userNameSpace, p3);
            c00743.I$0 = p2;
            return c00743.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object objSearchBookWithSource$default;
            Function1<ObjectNode, Boolean> function1;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    int it = this.I$0;
                    if (it > this.$maxSize.element) {
                        return new ArrayList();
                    }
                    this.$lastIndex.element = Math.max(this.$lastIndex.element, it);
                    File file = (File) this.$bookSourceFile.element;
                    if (((CharSequence) this.$bookSourceGroup.element).length() == 0) {
                        function1 = null;
                    } else {
                        final Ref.ObjectRef<String> objectRef = this.$bookSourceGroup;
                        function1 = new Function1<ObjectNode, Boolean>() { // from class: com.htmake.reader.api.controller.BookController$searchBookSource$3$bookSourceList$1
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                                return Boolean.valueOf(invoke((ObjectNode) p1));
                            }

                            public final boolean invoke(@NotNull ObjectNode it2) {
                                Intrinsics.checkNotNullParameter(it2, "it");
                                String _bookSourceGroup = it2.get("bookSourceGroup").asText();
                                String str = _bookSourceGroup;
                                return !(str == null || str.length() == 0) && StringsKt.indexOf$default(Intrinsics.stringPlus(_bookSourceGroup, ","), Intrinsics.stringPlus((String) objectRef.element, ","), 0, false, 6, (Object) null) >= 0;
                            }
                        };
                    }
                    JsonArray bookSourceList = ExtKt.parseJsonStringList$default(file, null, null, it, it, null, function1, 38, null);
                    if (bookSourceList == null || bookSourceList.isEmpty()) {
                        this.$maxSize.element = it;
                        return new ArrayList();
                    }
                    BookController bookController = this.this$0;
                    String string = bookSourceList.getString(0);
                    Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getString(0)");
                    this.label = 1;
                    objSearchBookWithSource$default = BookController.searchBookWithSource$default(bookController, string, (Book) this.$book.element, false, (String) this.$userNameSpace.element, (Continuation) this, 4, null);
                    if (objSearchBookWithSource$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSearchBookWithSource$default = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return (ArrayList) objSearchBookWithSource$default;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x078c  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0788  */
    /* JADX WARN: Type inference failed for: r3v33, types: [com.htmake.reader.api.controller.BookController$searchBookSourceSSE$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookSourceSSE(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C00761 c00761;
        Ref.IntRef maxSize;
        final Ref.ObjectRef resultList;
        Ref.ObjectRef book;
        Ref.ObjectRef userNameSpace;
        final Ref.IntRef lastIndex;
        HttpServerResponse response;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        Integer numBoxInt;
        Integer numBoxInt2;
        Integer numBoxInt3;
        Object map;
        String json;
        Book book2;
        if ($completion instanceof C00761) {
            c00761 = (C00761) $completion;
            if ((c00761.label & Integer.MIN_VALUE) != 0) {
                c00761.label -= Integer.MIN_VALUE;
            } else {
                c00761 = new C00761($completion);
            }
        }
        Object $result = c00761.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00761.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                response = context.response().putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                c00761.L$0 = this;
                c00761.L$1 = context;
                c00761.L$2 = returnData;
                c00761.L$3 = response;
                c00761.label = 1;
                objCheckAuth = checkAuth(context, c00761);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                lastIndex = new Ref.IntRef();
                final Ref.IntRef searchSize = new Ref.IntRef();
                Ref.ObjectRef bookSourceGroup = new Ref.ObjectRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"url\")");
                    bookUrl = string;
                    Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                    lastIndex.element = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(30));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"searchSize\", 30)");
                    searchSize.element = integer2.intValue();
                    String string2 = context.getBodyAsJson().getString("bookSourceGroup", PackageDocumentBase.PREFIX_OPF);
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                    bookSourceGroup.element = string2;
                    Integer integer3 = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer3, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                    integer3.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("lastIndex");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"lastIndex\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? -1 : numBoxInt.intValue();
                    lastIndex.element = iIntValue;
                    List listQueryParam3 = context.queryParam("searchSize");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"searchSize\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue2 = (str3 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 30 : numBoxInt2.intValue();
                    searchSize.element = iIntValue2;
                    List listQueryParam4 = context.queryParam("bookSourceGroup");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"bookSourceGroup\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    bookSourceGroup.element = str4 == null ? PackageDocumentBase.PREFIX_OPF : str4;
                    List listQueryParam5 = context.queryParam("refresh");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam5, "context.queryParam(\"refresh\")");
                    String str5 = (String) CollectionsKt.firstOrNull(listQueryParam5);
                    int iIntValue3 = (str5 == null || (numBoxInt3 = Boxing.boxInt(Integer.parseInt(str5))) == null) ? 0 : numBoxInt3.intValue();
                }
                userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                Map<String, Integer> bookSourceMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String) userNameSpace.element);
                if (bookSourceMap.size() <= 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (bookUrl.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请输入书籍链接"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                book = new Ref.ObjectRef();
                book.element = this.getShelfBookByURL(bookUrl, (String) userNameSpace.element);
                if (book.element == null) {
                    String asString = this.bookInfoCache.getAsString(bookUrl);
                    if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                        book2 = null;
                    } else {
                        if (!(map instanceof String)) {
                            json = ExtKt.getGson().toJson(map);
                        } else {
                            json = (String) map;
                        }
                        String json$iv$iv = json;
                        book2 = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$searchBookSourceSSE$$inlined$toDataClass$1
                        }.getType());
                    }
                    book.element = book2;
                }
                if (book.element == null) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("书籍信息错误"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (lastIndex.element >= bookSourceMap.size() - 1) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, MapsKt.mapOf(TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element))), null, 2, null).setErrorMsg("没有更多了"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                searchSize.element = searchSize.element > 0 ? searchSize.element : 30;
                resultList = new Ref.ObjectRef();
                resultList.element = new ArrayList();
                int concurrentCount = Math.max(searchSize.element * 2, 24);
                BookControllerKt.logger.info("searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}", new Object[]{Boxing.boxInt(lastIndex.element), Boxing.boxInt(concurrentCount), Boxing.boxInt(searchSize.element)});
                final Ref.BooleanRef isEnd = new Ref.BooleanRef();
                BookController bookController = this;
                context.request().connection().closeHandler((v2) -> {
                    m21searchBookSourceSSE$lambda8(r1, r2, v2);
                });
                Ref.ObjectRef bookSourceFile = new Ref.ObjectRef();
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", (String) userNameSpace.element, "bookSource"}, null, 2, null);
                if (!((File) bookSourceFile.element).exists()) {
                    bookSourceFile.element = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
                }
                maxSize = new Ref.IntRef();
                maxSize.element = bookSourceMap.size();
                final HttpServerResponse httpServerResponse = response;
                final BookController bookController2 = this;
                c00761.L$0 = this;
                c00761.L$1 = response;
                c00761.L$2 = lastIndex;
                c00761.L$3 = userNameSpace;
                c00761.L$4 = book;
                c00761.L$5 = resultList;
                c00761.L$6 = maxSize;
                c00761.label = 2;
                if (this.limitConcurrent(concurrentCount, lastIndex.element + 1, bookSourceMap.size(), new C00773(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, userNameSpace, null), new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.searchBookSourceSSE.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                    }

                    public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                        Intrinsics.checkNotNullParameter(list, "list");
                        ArrayList loopResult = new ArrayList();
                        ArrayList<Object> $this$forEach$iv = list;
                        Ref.ObjectRef<ArrayList<SearchBook>> objectRef = resultList;
                        for (Object element$iv : $this$forEach$iv) {
                            Collection bookList = element$iv instanceof Collection ? (Collection) element$iv : null;
                            if (bookList != null) {
                                ((ArrayList) objectRef.element).addAll(bookList);
                                loopResult.addAll(bookList);
                            }
                        }
                        httpServerResponse.write("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("lastIndex", Integer.valueOf(lastIndex.element)), TuplesKt.to("data", loopResult)}), false) + "\n\n");
                        BookControllerKt.logger.info("Loop: {} resultList.size: {}", Integer.valueOf(loopCount), Integer.valueOf(((ArrayList) resultList.element).size()));
                        return !isEnd.element && loopCount < bookController2.concurrentLoopCount && ((ArrayList) resultList.element).size() < searchSize.element;
                    }
                }, c00761) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                saveBookSources$default(this, (Book) book.element, (List) resultList.element, (String) userNameSpace.element, false, 8, null);
                response.write("event: end\n");
                HttpServerResponse httpServerResponse2 = response;
                StringBuilder sbAppend = new StringBuilder().append("data: ");
                Pair[] pairArr = new Pair[2];
                pairArr[0] = TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element));
                pairArr[1] = TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element < maxSize.element));
                httpServerResponse2.end(sbAppend.append(ExtKt.jsonEncode(MapsKt.mapOf(pairArr), false)).append("\n\n").toString());
                return Unit.INSTANCE;
            case 1:
                response = (HttpServerResponse) c00761.L$3;
                returnData = (ReturnData) c00761.L$2;
                context = (RoutingContext) c00761.L$1;
                this = (BookController) c00761.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                maxSize = (Ref.IntRef) c00761.L$6;
                resultList = (Ref.ObjectRef) c00761.L$5;
                book = (Ref.ObjectRef) c00761.L$4;
                userNameSpace = (Ref.ObjectRef) c00761.L$3;
                lastIndex = (Ref.IntRef) c00761.L$2;
                response = (HttpServerResponse) c00761.L$1;
                this = (BookController) c00761.L$0;
                ResultKt.throwOnFailure($result);
                saveBookSources$default(this, (Book) book.element, (List) resultList.element, (String) userNameSpace.element, false, 8, null);
                response.write("event: end\n");
                HttpServerResponse httpServerResponse22 = response;
                StringBuilder sbAppend2 = new StringBuilder().append("data: ");
                Pair[] pairArr2 = new Pair[2];
                pairArr2[0] = TuplesKt.to("lastIndex", Boxing.boxInt(lastIndex.element));
                pairArr2[1] = TuplesKt.to("isEnd", Boxing.boxBoolean(lastIndex.element < maxSize.element));
                httpServerResponse22.end(sbAppend2.append(ExtKt.jsonEncode(MapsKt.mapOf(pairArr2), false)).append("\n\n").toString());
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: searchBookSourceSSE$lambda-8, reason: not valid java name */
    private static final void m21searchBookSourceSSE$lambda8(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 searchBookSourceSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookSourceSSE$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookSourceSSE$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {1209}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$searchBookSourceSSE$3")
    static final class C00773 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.IntRef $maxSize;
        final /* synthetic */ Ref.IntRef $lastIndex;
        final /* synthetic */ Ref.ObjectRef<File> $bookSourceFile;
        final /* synthetic */ Ref.ObjectRef<String> $bookSourceGroup;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Ref.ObjectRef<Book> $book;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00773(Ref.IntRef $maxSize, Ref.IntRef $lastIndex, Ref.ObjectRef<File> $bookSourceFile, Ref.ObjectRef<String> $bookSourceGroup, BookController this$0, Ref.ObjectRef<Book> $book, Ref.ObjectRef<String> $userNameSpace, Continuation<? super C00773> $completion) {
            super(3, $completion);
            this.$maxSize = $maxSize;
            this.$lastIndex = $lastIndex;
            this.$bookSourceFile = $bookSourceFile;
            this.$bookSourceGroup = $bookSourceGroup;
            this.this$0 = this$0;
            this.$book = $book;
            this.$userNameSpace = $userNameSpace;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00773 c00773 = new C00773(this.$maxSize, this.$lastIndex, this.$bookSourceFile, this.$bookSourceGroup, this.this$0, this.$book, this.$userNameSpace, p3);
            c00773.I$0 = p2;
            return c00773.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object objSearchBookWithSource$default;
            Function1<ObjectNode, Boolean> function1;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    int it = this.I$0;
                    if (it > this.$maxSize.element) {
                        return new ArrayList();
                    }
                    this.$lastIndex.element = Math.max(this.$lastIndex.element, it);
                    File file = (File) this.$bookSourceFile.element;
                    if (((CharSequence) this.$bookSourceGroup.element).length() == 0) {
                        function1 = null;
                    } else {
                        final Ref.ObjectRef<String> objectRef = this.$bookSourceGroup;
                        function1 = new Function1<ObjectNode, Boolean>() { // from class: com.htmake.reader.api.controller.BookController$searchBookSourceSSE$3$bookSourceList$1
                            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                            {
                                super(1);
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                                return Boolean.valueOf(invoke((ObjectNode) p1));
                            }

                            public final boolean invoke(@NotNull ObjectNode it2) {
                                Intrinsics.checkNotNullParameter(it2, "it");
                                String _bookSourceGroup = it2.get("bookSourceGroup").asText();
                                String str = _bookSourceGroup;
                                return !(str == null || str.length() == 0) && StringsKt.indexOf$default(Intrinsics.stringPlus(_bookSourceGroup, ","), Intrinsics.stringPlus((String) objectRef.element, ","), 0, false, 6, (Object) null) >= 0;
                            }
                        };
                    }
                    JsonArray bookSourceList = ExtKt.parseJsonStringList$default(file, null, null, it, it, null, function1, 38, null);
                    if (bookSourceList == null || bookSourceList.isEmpty()) {
                        this.$maxSize.element = it;
                        return new ArrayList();
                    }
                    BookController bookController = this.this$0;
                    String string = bookSourceList.getString(0);
                    Intrinsics.checkNotNullExpressionValue(string, "bookSourceList.getString(0)");
                    this.label = 1;
                    objSearchBookWithSource$default = BookController.searchBookWithSource$default(bookController, string, (Book) this.$book.element, false, (String) this.$userNameSpace.element, (Continuation) this, 4, null);
                    if (objSearchBookWithSource$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSearchBookWithSource$default = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return (ArrayList) objSearchBookWithSource$default;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookWithSource(@NotNull String bookSourceString, @NotNull Book book, boolean accurate, @NotNull String userNameSpace, @NotNull Continuation<? super ArrayList<SearchBook>> $completion) {
        C00791 c00791;
        Ref.ObjectRef resultList;
        if ($completion instanceof C00791) {
            c00791 = (C00791) $completion;
            if ((c00791.label & Integer.MIN_VALUE) != 0) {
                c00791.label -= Integer.MIN_VALUE;
            } else {
                c00791 = new C00791($completion);
            }
        }
        Object $result = c00791.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00791.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                resultList = new Ref.ObjectRef();
                resultList.element = new ArrayList();
                Ref.ObjectRef bookSource = new Ref.ObjectRef();
                Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSourceString);
                bookSource.element = Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A;
                if (bookSource.element == null) {
                    return resultList.element;
                }
                if (isInvalidBookSource((BookSource) bookSource.element, userNameSpace)) {
                    return resultList.element;
                }
                CoroutineContext io2 = Dispatchers.getIO();
                boolean z = accurate;
                c00791.L$0 = resultList;
                c00791.label = 1;
                if (BuildersKt.withContext(io2, new C00802(bookSource, userNameSpace, book, z, resultList, this, null), c00791) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                resultList = (Ref.ObjectRef) c00791.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return resultList.element;
    }

    public static /* synthetic */ Object searchBookWithSource$default(BookController bookController, String str, Book book, boolean z, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            z = true;
        }
        if ((i & 8) != 0) {
            str2 = "default";
        }
        return bookController.searchBookWithSource(str, book, z, str2, continuation);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$searchBookWithSource$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$searchBookWithSource$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookController.kt", l = {1251}, i = {0}, s = {"J$0"}, n = {"start"}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$searchBookWithSource$2")
    static final class C00802 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        long J$0;
        int label;
        final /* synthetic */ Ref.ObjectRef<BookSource> $bookSource;
        final /* synthetic */ String $userNameSpace;
        final /* synthetic */ Book $book;
        final /* synthetic */ boolean $accurate;
        final /* synthetic */ Ref.ObjectRef<ArrayList<SearchBook>> $resultList;
        final /* synthetic */ BookController this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00802(Ref.ObjectRef<BookSource> $bookSource, String $userNameSpace, Book $book, boolean $accurate, Ref.ObjectRef<ArrayList<SearchBook>> $resultList, BookController this$0, Continuation<? super C00802> $completion) {
            super(2, $completion);
            this.$bookSource = $bookSource;
            this.$userNameSpace = $userNameSpace;
            this.$book = $book;
            this.$accurate = $accurate;
            this.$resultList = $resultList;
            this.this$0 = this$0;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new C00802(this.$bookSource, this.$userNameSpace, this.$book, this.$accurate, this.$resultList, this.this$0, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0116 A[Catch: Exception -> 0x0172, TryCatch #0 {Exception -> 0x0172, blocks: (B:5:0x0024, B:12:0x0072, B:14:0x0086, B:16:0x0099, B:18:0x00b5, B:20:0x00c7, B:25:0x00e8, B:27:0x00fa, B:28:0x0116, B:30:0x011d, B:32:0x0136, B:34:0x014f, B:11:0x006c), top: B:44:0x0009 }] */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            long start;
            Object objSearchBook;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        start = System.currentTimeMillis();
                        this.J$0 = start;
                        this.label = 1;
                        objSearchBook = new WebBook((BookSource) this.$bookSource.element, false, (DebugLog) null, this.$userNameSpace, 4, (DefaultConstructorMarker) null).searchBook(this.$book.getName(), Boxing.boxInt(1), (Continuation) this);
                        if (objSearchBook == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        break;
                    case 1:
                        start = this.J$0;
                        ResultKt.throwOnFailure($result);
                        objSearchBook = $result;
                        break;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                List result = (List) objSearchBook;
                long end = System.currentTimeMillis();
                if (result.size() > 0) {
                    int i = 0;
                    int size = result.size();
                    if (0 < size) {
                        do {
                            int j = i;
                            i++;
                            SearchBook _book = (SearchBook) result.get(j);
                            if (this.$accurate && _book.getName().equals(this.$book.getName())) {
                                if ((this.$book.getAuthor().length() == 0) || _book.getAuthor().equals(this.$book.getAuthor())) {
                                    _book.setTime(end - start);
                                    ((ArrayList) this.$resultList.element).add(_book);
                                }
                            } else if (!this.$accurate && (StringsKt.indexOf$default(_book.getName(), this.$book.getName(), 0, true, 2, (Object) null) >= 0 || StringsKt.indexOf$default(_book.getAuthor(), this.$book.getName(), 0, true, 2, (Object) null) >= 0)) {
                                _book.setTime(end - start);
                                ((ArrayList) this.$resultList.element).add(_book);
                            }
                        } while (i < size);
                    }
                }
            } catch (Exception e) {
                Map info = MapsKt.mutableMapOf(new Pair[]{TuplesKt.to("sourceUrl", ((BookSource) this.$bookSource.element).getBookSourceUrl()), TuplesKt.to(RSSKeywords.RSS_ITEM_TIME, Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to("error", e.toString())});
                this.this$0.addInvalidBookSource(((BookSource) this.$bookSource.element).getBookSourceUrl(), info, this.$userNameSpace);
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Type inference failed for: r3v19, types: [com.htmake.reader.api.controller.BookController$getAvailableBookSource$$inlined$toDataClass$1] */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getAvailableBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00281 c00281;
        final Ref.ObjectRef resultList;
        Ref.ObjectRef book;
        Ref.ObjectRef userNameSpace;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        Integer numBoxInt;
        int refresh;
        Object map;
        String json;
        Book book2;
        if ($completion instanceof C00281) {
            c00281 = (C00281) $completion;
            if ((c00281.label & Integer.MIN_VALUE) != 0) {
                c00281.label -= Integer.MIN_VALUE;
            } else {
                c00281 = new C00281($completion);
            }
        }
        Object $result = c00281.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00281.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00281.L$0 = this;
                c00281.L$1 = context;
                c00281.L$2 = returnData;
                c00281.label = 1;
                objCheckAuth = checkAuth(context, c00281);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"url\")");
                    bookUrl = string;
                    Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                    refresh = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("refresh");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"refresh\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? 0 : numBoxInt.intValue();
                    refresh = iIntValue;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                book = new Ref.ObjectRef();
                book.element = this.getShelfBookByURL(bookUrl, (String) userNameSpace.element);
                if (book.element == null) {
                    String asString = this.bookInfoCache.getAsString(bookUrl);
                    if (asString == null || (map = ExtKt.toMap(asString)) == null) {
                        book2 = null;
                    } else {
                        if (!(map instanceof String)) {
                            json = ExtKt.getGson().toJson(map);
                        } else {
                            json = (String) map;
                        }
                        String json$iv$iv = json;
                        book2 = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$getAvailableBookSource$$inlined$toDataClass$1
                        }.getType());
                    }
                    book.element = book2;
                }
                if (book.element == null) {
                    return returnData.setErrorMsg("书籍信息错误");
                }
                Ref.ObjectRef bookSourceList = new Ref.ObjectRef();
                bookSourceList.element = ExtKt.asJsonArray(this.getUserStorage(userNameSpace.element, ((Book) book.element).getName() + '_' + ((Book) book.element).getAuthor(), "bookSource"));
                if (bookSourceList.element != null && ((JsonArray) bookSourceList.element).size() > 0) {
                    if (refresh <= 0) {
                        List list = ((JsonArray) bookSourceList.element).getList();
                        Intrinsics.checkNotNullExpressionValue(list, "bookSourceList.getList()");
                        return ReturnData.setData$default(returnData, list, null, 2, null);
                    }
                    resultList = new Ref.ObjectRef();
                    resultList.element = new ArrayList();
                    c00281.L$0 = this;
                    c00281.L$1 = returnData;
                    c00281.L$2 = userNameSpace;
                    c00281.L$3 = book;
                    c00281.L$4 = resultList;
                    c00281.label = 2;
                    if (this.limitConcurrent(16, 0, ((JsonArray) bookSourceList.element).size(), new C00292(bookSourceList, this, userNameSpace, book, null), new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.getAvailableBookSource.3
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(2);
                        }

                        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                            return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                        }

                        public final boolean invoke(@NotNull ArrayList<Object> list2, int $noName_1) {
                            Intrinsics.checkNotNullParameter(list2, "list");
                            ArrayList<Object> $this$forEach$iv = list2;
                            Ref.ObjectRef<ArrayList<SearchBook>> objectRef = resultList;
                            for (Object element$iv : $this$forEach$iv) {
                                Collection bookList = element$iv instanceof Collection ? (Collection) element$iv : null;
                                if (bookList != null) {
                                    ((ArrayList) objectRef.element).addAll(bookList);
                                }
                            }
                            return true;
                        }
                    }, c00281) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    this.saveBookSources((Book) book.element, (List) resultList.element, (String) userNameSpace.element, true);
                    return ReturnData.setData$default(returnData, resultList.element, null, 2, null);
                }
                return ReturnData.setData$default(returnData, new ArrayList(), null, 2, null);
            case 1:
                returnData = (ReturnData) c00281.L$2;
                context = (RoutingContext) c00281.L$1;
                this = (BookController) c00281.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                resultList = (Ref.ObjectRef) c00281.L$4;
                book = (Ref.ObjectRef) c00281.L$3;
                userNameSpace = (Ref.ObjectRef) c00281.L$2;
                returnData = (ReturnData) c00281.L$1;
                this = (BookController) c00281.L$0;
                ResultKt.throwOnFailure($result);
                this.saveBookSources((Book) book.element, (List) resultList.element, (String) userNameSpace.element, true);
                return ReturnData.setData$default(returnData, resultList.element, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getAvailableBookSource$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getAvailableBookSource$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {1321}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$getAvailableBookSource$2")
    static final class C00292 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.ObjectRef<JsonArray> $bookSourceList;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;
        final /* synthetic */ Ref.ObjectRef<Book> $book;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00292(Ref.ObjectRef<JsonArray> $bookSourceList, BookController this$0, Ref.ObjectRef<String> $userNameSpace, Ref.ObjectRef<Book> $book, Continuation<? super C00292> $completion) {
            super(3, $completion);
            this.$bookSourceList = $bookSourceList;
            this.this$0 = this$0;
            this.$userNameSpace = $userNameSpace;
            this.$book = $book;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00292 c00292 = new C00292(this.$bookSourceList, this.this$0, this.$userNameSpace, this.$book, p3);
            c00292.I$0 = p2;
            return c00292.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) throws Exception {
            Object objSearchBookWithSource$default;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    int it = this.I$0;
                    SearchBook searchBook = (SearchBook) ((JsonArray) this.$bookSourceList.element).getJsonObject(it).mapTo(SearchBook.class);
                    if (searchBook.getOrigin().equals(BookType.local)) {
                        return CollectionsKt.arrayListOf(new SearchBook[]{searchBook});
                    }
                    String bookSource = this.this$0.getBookSourceStringBySourceURLOpt(searchBook.getOrigin(), (String) this.$userNameSpace.element);
                    if (bookSource == null) {
                        return new ArrayList();
                    }
                    this.label = 1;
                    objSearchBookWithSource$default = BookController.searchBookWithSource$default(this.this$0, bookSource, (Book) this.$book.element, false, (String) this.$userNameSpace.element, (Continuation) this, 4, null);
                    if (objSearchBookWithSource$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    objSearchBookWithSource$default = $result;
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return (ArrayList) objSearchBookWithSource$default;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookshelf(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00371 c00371;
        ReturnData returnData;
        Object bookShelfBooks;
        Object objCheckAuth;
        Integer numBoxInt;
        int refresh;
        if ($completion instanceof C00371) {
            c00371 = (C00371) $completion;
            if ((c00371.label & Integer.MIN_VALUE) != 0) {
                c00371.label -= Integer.MIN_VALUE;
            } else {
                c00371 = new C00371($completion);
            }
        }
        Object $result = c00371.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00371.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00371.L$0 = this;
                c00371.L$1 = context;
                c00371.L$2 = returnData;
                c00371.label = 1;
                objCheckAuth = checkAuth(context, c00371);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                    refresh = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam("refresh");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"refresh\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    int iIntValue = (str == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str))) == null) ? 0 : numBoxInt.intValue();
                    refresh = iIntValue;
                }
                BookController bookController = this;
                boolean z = refresh > 0;
                c00371.L$0 = returnData;
                c00371.L$1 = null;
                c00371.L$2 = null;
                c00371.label = 2;
                bookShelfBooks = bookController.getBookShelfBooks(z, this.getUserNameSpace(context), c00371);
                if (bookShelfBooks == coroutine_suspended) {
                    return coroutine_suspended;
                }
                List bookList = (List) bookShelfBooks;
                return ReturnData.setData$default(returnData, bookList, null, 2, null);
            case 1:
                returnData = (ReturnData) c00371.L$2;
                context = (RoutingContext) c00371.L$1;
                this = (BookController) c00371.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                returnData = (ReturnData) c00371.L$0;
                ResultKt.throwOnFailure($result);
                bookShelfBooks = $result;
                List bookList2 = (List) bookShelfBooks;
                return ReturnData.setData$default(returnData, bookList2, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getShelfBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00431 c00431;
        ReturnData returnData;
        Object objCheckAuth;
        String url;
        if ($completion instanceof C00431) {
            c00431 = (C00431) $completion;
            if ((c00431.label & Integer.MIN_VALUE) != 0) {
                c00431.label -= Integer.MIN_VALUE;
            } else {
                c00431 = new C00431($completion);
            }
        }
        Object $result = c00431.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00431.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00431.L$0 = this;
                c00431.L$1 = context;
                c00431.L$2 = returnData;
                c00431.label = 1;
                objCheckAuth = checkAuth(context, c00431);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00431.L$2;
                context = (RoutingContext) c00431.L$1;
                this = (BookController) c00431.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"url\")");
            url = string;
        } else {
            List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            url = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (url.length() == 0) {
            return returnData.setErrorMsg("书源链接不能为空");
        }
        Book book = this.getShelfBookByURL(url, this.getUserNameSpace(context));
        if (book == null) {
            return returnData.setErrorMsg("书籍不存在");
        }
        return ReturnData.setData$default(returnData, book, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0220  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x02a2  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x037a  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0396  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00531 c00531;
        String userNameSpace;
        Book book;
        ReturnData returnData;
        String bookSource;
        Object objMergeBookCacheInfo;
        Object objCheckAuth;
        Pair<Book, String> pairSaveBookToShelf;
        Book book2;
        if ($completion instanceof C00531) {
            c00531 = (C00531) $completion;
            if ((c00531.label & Integer.MIN_VALUE) != 0) {
                c00531.label -= Integer.MIN_VALUE;
            } else {
                c00531 = new C00531($completion);
            }
        }
        Object $result = c00531.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00531.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00531.L$0 = this;
                c00531.L$1 = context;
                c00531.L$2 = returnData;
                c00531.label = 1;
                objCheckAuth = checkAuth(context, c00531);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                book = (Book) context.getBodyAsJson().mapTo(Book.class);
                userNameSpace = this.getUserNameSpace(context);
                if (!book.isLocalBook()) {
                    bookSource = this.getBookSourceStringBySourceURLOpt(book.getOrigin(), userNameSpace);
                    if (bookSource == null) {
                        return returnData.setErrorMsg("书源信息错误");
                    }
                    String tocUrl = book.getTocUrl();
                    if (tocUrl == null || tocUrl.length() == 0) {
                        WebBook webBook = new WebBook(bookSource, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null);
                        Intrinsics.checkNotNullExpressionValue(book, "book");
                        c00531.L$0 = this;
                        c00531.L$1 = context;
                        c00531.L$2 = returnData;
                        c00531.L$3 = book;
                        c00531.L$4 = userNameSpace;
                        c00531.L$5 = bookSource;
                        c00531.label = 2;
                        if (WebBook.getBookInfo$default(webBook, book, false, (Continuation) c00531, 2, (Object) null) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        Book book3 = book;
                        Intrinsics.checkNotNullExpressionValue(book3, "book");
                        c00531.L$0 = this;
                        c00531.L$1 = context;
                        c00531.L$2 = returnData;
                        c00531.L$3 = userNameSpace;
                        c00531.L$4 = bookSource;
                        c00531.L$5 = null;
                        c00531.label = 3;
                        objMergeBookCacheInfo = this.mergeBookCacheInfo(book3, c00531);
                        if (objMergeBookCacheInfo == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        book = (Book) objMergeBookCacheInfo;
                    }
                    book2 = book;
                    Intrinsics.checkNotNullExpressionValue(book2, "book");
                    c00531.L$0 = this;
                    c00531.L$1 = context;
                    c00531.L$2 = returnData;
                    c00531.L$3 = book;
                    c00531.L$4 = userNameSpace;
                    c00531.label = 4;
                    if (this.saveBookCover(book2, userNameSpace, bookSource, c00531) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Book book4 = book;
                    Intrinsics.checkNotNullExpressionValue(book4, "book");
                    pairSaveBookToShelf = this.saveBookToShelf(book4, userNameSpace, context);
                    if (pairSaveBookToShelf.getSecond() != null) {
                        return ReturnData.setData$default(returnData, pairSaveBookToShelf.getFirst(), null, 2, null);
                    }
                    ReturnData returnData2 = returnData;
                    String str = (String) pairSaveBookToShelf.getSecond();
                    return returnData2.setErrorMsg(str == null ? PackageDocumentBase.PREFIX_OPF : str);
                }
                Intrinsics.checkNotNullExpressionValue(book, "book");
                c00531.L$0 = this;
                c00531.L$1 = context;
                c00531.L$2 = returnData;
                c00531.L$3 = book;
                c00531.L$4 = userNameSpace;
                c00531.label = 5;
                if (this.saveLocalBookCover(book, userNameSpace, c00531) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Book book42 = book;
                Intrinsics.checkNotNullExpressionValue(book42, "book");
                pairSaveBookToShelf = this.saveBookToShelf(book42, userNameSpace, context);
                if (pairSaveBookToShelf.getSecond() != null) {
                }
                break;
            case 1:
                returnData = (ReturnData) c00531.L$2;
                context = (RoutingContext) c00531.L$1;
                this = (BookController) c00531.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                bookSource = (String) c00531.L$5;
                userNameSpace = (String) c00531.L$4;
                book = (Book) c00531.L$3;
                returnData = (ReturnData) c00531.L$2;
                context = (RoutingContext) c00531.L$1;
                this = (BookController) c00531.L$0;
                ResultKt.throwOnFailure($result);
                Book book32 = book;
                Intrinsics.checkNotNullExpressionValue(book32, "book");
                c00531.L$0 = this;
                c00531.L$1 = context;
                c00531.L$2 = returnData;
                c00531.L$3 = userNameSpace;
                c00531.L$4 = bookSource;
                c00531.L$5 = null;
                c00531.label = 3;
                objMergeBookCacheInfo = this.mergeBookCacheInfo(book32, c00531);
                if (objMergeBookCacheInfo == coroutine_suspended) {
                }
                book = (Book) objMergeBookCacheInfo;
                book2 = book;
                Intrinsics.checkNotNullExpressionValue(book2, "book");
                c00531.L$0 = this;
                c00531.L$1 = context;
                c00531.L$2 = returnData;
                c00531.L$3 = book;
                c00531.L$4 = userNameSpace;
                c00531.label = 4;
                if (this.saveBookCover(book2, userNameSpace, bookSource, c00531) == coroutine_suspended) {
                }
                Book book422 = book;
                Intrinsics.checkNotNullExpressionValue(book422, "book");
                pairSaveBookToShelf = this.saveBookToShelf(book422, userNameSpace, context);
                if (pairSaveBookToShelf.getSecond() != null) {
                }
                break;
            case 3:
                bookSource = (String) c00531.L$4;
                userNameSpace = (String) c00531.L$3;
                returnData = (ReturnData) c00531.L$2;
                context = (RoutingContext) c00531.L$1;
                this = (BookController) c00531.L$0;
                ResultKt.throwOnFailure($result);
                objMergeBookCacheInfo = $result;
                book = (Book) objMergeBookCacheInfo;
                book2 = book;
                Intrinsics.checkNotNullExpressionValue(book2, "book");
                c00531.L$0 = this;
                c00531.L$1 = context;
                c00531.L$2 = returnData;
                c00531.L$3 = book;
                c00531.L$4 = userNameSpace;
                c00531.label = 4;
                if (this.saveBookCover(book2, userNameSpace, bookSource, c00531) == coroutine_suspended) {
                }
                Book book4222 = book;
                Intrinsics.checkNotNullExpressionValue(book4222, "book");
                pairSaveBookToShelf = this.saveBookToShelf(book4222, userNameSpace, context);
                if (pairSaveBookToShelf.getSecond() != null) {
                }
                break;
            case 4:
                userNameSpace = (String) c00531.L$4;
                book = (Book) c00531.L$3;
                returnData = (ReturnData) c00531.L$2;
                context = (RoutingContext) c00531.L$1;
                this = (BookController) c00531.L$0;
                ResultKt.throwOnFailure($result);
                Book book42222 = book;
                Intrinsics.checkNotNullExpressionValue(book42222, "book");
                pairSaveBookToShelf = this.saveBookToShelf(book42222, userNameSpace, context);
                if (pairSaveBookToShelf.getSecond() != null) {
                }
                break;
            case 5:
                userNameSpace = (String) c00531.L$4;
                book = (Book) c00531.L$3;
                returnData = (ReturnData) c00531.L$2;
                context = (RoutingContext) c00531.L$1;
                this = (BookController) c00531.L$0;
                ResultKt.throwOnFailure($result);
                Book book422222 = book;
                Intrinsics.checkNotNullExpressionValue(book422222, "book");
                pairSaveBookToShelf = this.saveBookToShelf(book422222, userNameSpace, context);
                if (pairSaveBookToShelf.getSecond() != null) {
                }
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    @NotNull
    public final Pair<Book, String> saveBookToShelf(@NotNull Book _book, @NotNull String userNameSpace, @NotNull RoutingContext context) {
        User userInfo;
        Intrinsics.checkNotNullParameter(_book, "_book");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter(context, "context");
        String origin = _book.getOrigin();
        if (origin == null || origin.length() == 0) {
            return new Pair<>(_book, "未找到书源信息");
        }
        String bookUrl = _book.getBookUrl();
        if (bookUrl == null || bookUrl.length() == 0) {
            return new Pair<>(_book, "书籍链接不能为空");
        }
        JsonArray bookshelf = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        int existIndex = -1;
        int i = 0;
        int size = bookshelf.size();
        if (0 < size) {
            while (true) {
                int i2 = i;
                i++;
                String name = bookshelf.getJsonObject(i2).getString("name", PackageDocumentBase.PREFIX_OPF);
                String author = bookshelf.getJsonObject(i2).getString("author", PackageDocumentBase.PREFIX_OPF);
                if (!name.equals(_book.getName()) || !author.equals(_book.getAuthor())) {
                    if (i >= size) {
                        break;
                    }
                } else {
                    existIndex = i2;
                    break;
                }
            }
        }
        if (existIndex < 0 && (userInfo = (User) context.get("userInfo")) != null && bookshelf.size() >= userInfo.getBook_limit()) {
            return new Pair<>(_book, "你已达到书籍数上限，请联系管理员");
        }
        if (_book.isLocalBook()) {
            if (StringsKt.startsWith$default(_book.getBookUrl(), "/assets/", false, 2, (Object) null) || StringsKt.startsWith$default(_book.getBookUrl(), "assets/", false, 2, (Object) null)) {
                File tempFile = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", _book.getBookUrl())));
                if (!tempFile.exists()) {
                    return new Pair<>(_book, "上传书籍不存在");
                }
                String relativeLocalFilePath = Paths.get("storage", "data", userNameSpace, _book.getName() + '_' + _book.getAuthor(), tempFile.getName()).toString();
                String relativeLocalFileUrl = "storage/data/" + userNameSpace + '/' + _book.getName() + '_' + _book.getAuthor() + '/' + ((Object) tempFile.getName());
                String localFilePath = ExtKt.getWorkDir(relativeLocalFilePath);
                BookControllerKt.logger.info("localFilePath: {}", localFilePath);
                File localFile = new File(localFilePath);
                ExtKt.deleteRecursively(localFile);
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
                if (!FilesKt.copyRecursively$default(tempFile, localFile, false, (Function2) null, 6, (Object) null)) {
                    return new Pair<>(_book, "导入本地书籍失败");
                }
                ExtKt.deleteRecursively(tempFile);
                _book.setBookUrl(relativeLocalFileUrl);
                _book.setOriginName(relativeLocalFilePath);
                if (_book.isEpub()) {
                    if (!extractEpub$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地Epub书籍失败");
                    }
                } else if (_book.isCbz()) {
                    if (!extractCbz$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地CBZ书籍失败");
                    }
                } else if (_book.isPdf() && !convertPdfToImage$default(this, _book, false, 2, null)) {
                    return new Pair<>(_book, "本地PDF书籍转换失败");
                }
            } else if (StringsKt.indexOf$default(_book.getBookUrl(), "localStore", 0, false, 6, (Object) null) >= 0) {
                File tempFile2 = new File(ExtKt.getWorkDir(_book.getBookUrl()));
                if (!tempFile2.exists()) {
                    return new Pair<>(_book, "本地书仓书籍不存在");
                }
                String relativeLocalFileUrl2 = "storage/data/" + userNameSpace + '/' + _book.getName() + '_' + _book.getAuthor() + '/' + ((Object) tempFile2.getName());
                _book.setBookUrl(relativeLocalFileUrl2);
                if (_book.isEpub()) {
                    if (!extractEpub$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地Epub书籍失败");
                    }
                } else if (_book.isCbz()) {
                    if (!extractCbz$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地CBZ书籍失败");
                    }
                } else if (_book.isPdf() && !convertPdfToImage$default(this, _book, false, 2, null)) {
                    return new Pair<>(_book, "本地PDF书籍转换失败");
                }
            } else if (StringsKt.indexOf$default(_book.getBookUrl(), "webdav", 0, false, 6, (Object) null) >= 0) {
                File tempFile3 = new File(ExtKt.getWorkDir(_book.getBookUrl()));
                if (!tempFile3.exists()) {
                    return new Pair<>(_book, "webdav书仓书籍不存在");
                }
                String relativeLocalFileUrl3 = "storage/data/" + userNameSpace + '/' + _book.getName() + '_' + _book.getAuthor() + '/' + ((Object) tempFile3.getName());
                _book.setBookUrl(relativeLocalFileUrl3);
                if (_book.isEpub()) {
                    if (!extractEpub$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地Epub书籍失败");
                    }
                } else if (_book.isCbz()) {
                    if (!extractCbz$default(this, _book, false, 2, null)) {
                        return new Pair<>(_book, "导入本地CBZ书籍失败");
                    }
                } else if (_book.isPdf() && !convertPdfToImage$default(this, _book, false, 2, null)) {
                    return new Pair<>(_book, "本地PDF书籍转换失败");
                }
            }
        }
        _book.setInShelf(true);
        if (existIndex < 0) {
            bookshelf.add(JsonObject.mapFrom(_book));
        } else {
            List bookList = bookshelf.getList();
            Book existBook = (Book) bookshelf.getJsonObject(existIndex).mapTo(Book.class);
            _book.setDurChapterIndex(existBook.getDurChapterIndex());
            _book.setDurChapterTitle(existBook.getDurChapterTitle());
            _book.setDurChapterTime(existBook.getDurChapterTime());
            String displayCover = existBook.getDisplayCover();
            if (!(displayCover == null || displayCover.length() == 0)) {
                String displayCover2 = existBook.getDisplayCover();
                Intrinsics.checkNotNull(displayCover2);
                if (StringsKt.startsWith$default(displayCover2, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                    String displayCover3 = existBook.getDisplayCover();
                    Intrinsics.checkNotNull(displayCover3);
                    if (!displayCover3.equals(_book.getDisplayCover())) {
                        String displayCover4 = existBook.getDisplayCover();
                        Intrinsics.checkNotNull(displayCover4);
                        String cachePath = ExtKt.getWorkDir("storage", displayCover4);
                        FileUtils.INSTANCE.deleteFile(cachePath);
                    }
                }
            }
            bookList.set(existIndex, JsonObject.mapFrom(_book));
            bookshelf = new JsonArray(bookList);
        }
        List sourceList = CollectionsKt.listOf(_book.toSearchBook());
        saveBookSources$default(this, _book, sourceList, userNameSpace, false, 8, null);
        saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        return new Pair<>(_book, (Object) null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0185  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0189  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveLocalBookCover(Book book, String userNameSpace, Continuation<? super Unit> $completion) {
        C00601 c00601;
        File cacheFile;
        String cachedCoverUrl;
        Object objAwaitResult;
        byte[] bodyBytes;
        if ($completion instanceof C00601) {
            c00601 = (C00601) $completion;
            if ((c00601.label & Integer.MIN_VALUE) != 0) {
                c00601.label -= Integer.MIN_VALUE;
            } else {
                c00601 = new C00601($completion);
            }
        }
        Object $result = c00601.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00601.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                final String coverUrl = book.getDisplayCover();
                if (coverUrl != null && !StringsKt.startsWith$default(coverUrl, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                    String ext = getFileExt(coverUrl, "jpg");
                    String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                    String cachePath = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", md5Encode + '.' + ext);
                    cachedCoverUrl = "/assets/" + userNameSpace + "/covers/" + md5Encode + '.' + ext;
                    cacheFile = new File(cachePath);
                    if (cacheFile.exists()) {
                        book.setCoverUrl(cachedCoverUrl);
                        return Unit.INSTANCE;
                    }
                    c00601.L$0 = book;
                    c00601.L$1 = cachedCoverUrl;
                    c00601.L$2 = cacheFile;
                    c00601.label = 1;
                    objAwaitResult = VertxCoroutineKt.awaitResult(new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>() { // from class: com.htmake.reader.api.controller.BookController$saveLocalBookCover$result$1
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                            invoke((Handler<AsyncResult<HttpResponse<Buffer>>>) p1);
                            return Unit.INSTANCE;
                        }

                        public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                            Intrinsics.checkNotNullParameter(handler, "handler");
                            this.this$0.webClient.getAbs(coverUrl).timeout(3000L).send(handler);
                        }
                    }, c00601);
                    if (objAwaitResult == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    HttpResponse result = (HttpResponse) objAwaitResult;
                    Buffer bufferBodyAsBuffer = result.bodyAsBuffer();
                    bodyBytes = bufferBodyAsBuffer != null ? null : bufferBodyAsBuffer.getBytes();
                    if (bodyBytes != null) {
                        FilesKt.writeBytes(cacheFile, bodyBytes);
                        book.setCoverUrl(cachedCoverUrl);
                    }
                }
                return Unit.INSTANCE;
            case 1:
                cacheFile = (File) c00601.L$2;
                cachedCoverUrl = (String) c00601.L$1;
                book = (Book) c00601.L$0;
                ResultKt.throwOnFailure($result);
                objAwaitResult = $result;
                HttpResponse result2 = (HttpResponse) objAwaitResult;
                Buffer bufferBodyAsBuffer2 = result2.bodyAsBuffer();
                bodyBytes = bufferBodyAsBuffer2 != null ? null : bufferBodyAsBuffer2.getBytes();
                if (bodyBytes != null) {
                }
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookCover(@NotNull Book book, @NotNull String userNameSpace, @Nullable String bookSource, @NotNull Continuation<? super Unit> $completion) {
        C00561 c00561;
        String cachedCoverUrl;
        String cachePath;
        Object byteArrayAwait;
        if ($completion instanceof C00561) {
            c00561 = (C00561) $completion;
            if ((c00561.label & Integer.MIN_VALUE) != 0) {
                c00561.label -= Integer.MIN_VALUE;
            } else {
                c00561 = new C00561($completion);
            }
        }
        Object $result = c00561.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (c00561.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String coverUrl = book.getDisplayCover();
                if (coverUrl != null && !StringsKt.startsWith$default(coverUrl, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                    String bookSource2 = bookSource == null ? getBookSourceStringBySourceURLOpt(book.getOrigin(), userNameSpace) : bookSource;
                    String ext = getFileExt(coverUrl, "jpg");
                    String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                    cachePath = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", md5Encode + '.' + ext);
                    cachedCoverUrl = "/assets/" + userNameSpace + "/covers/" + md5Encode + '.' + ext;
                    File cacheFile = new File(cachePath);
                    if (cacheFile.exists()) {
                        book.setCoverUrl(cachedCoverUrl);
                        return Unit.INSTANCE;
                    }
                    String str = null;
                    Integer num = null;
                    String str2 = null;
                    Integer num2 = null;
                    String str3 = null;
                    BookSource.Companion companion = BookSource.INSTANCE;
                    Intrinsics.checkNotNull(bookSource2);
                    Object objM151fromJsonIoAF18A = companion.m151fromJsonIoAF18A(bookSource2);
                    AnalyzeUrl analyzeUrl = new AnalyzeUrl(coverUrl, str, num, str2, num2, str3, (BaseSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A), null, null, null, null, 1982, null);
                    c00561.L$0 = book;
                    c00561.L$1 = cachePath;
                    c00561.L$2 = cachedCoverUrl;
                    c00561.label = 1;
                    byteArrayAwait = analyzeUrl.getByteArrayAwait(c00561);
                    if (byteArrayAwait == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    byte[] it = (byte[]) byteArrayAwait;
                    FileUtils.INSTANCE.writeBytes(cachePath, it);
                    book.setCoverUrl(cachedCoverUrl);
                    break;
                }
                return Unit.INSTANCE;
            case 1:
                cachedCoverUrl = (String) c00561.L$2;
                cachePath = (String) c00561.L$1;
                book = (Book) c00561.L$0;
                ResultKt.throwOnFailure($result);
                byteArrayAwait = $result;
                byte[] it2 = (byte[]) byteArrayAwait;
                FileUtils.INSTANCE.writeBytes(cachePath, it2);
                book.setCoverUrl(cachedCoverUrl);
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object saveBookCover$default(BookController bookController, Book book, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            str2 = null;
        }
        return bookController.saveBookCover(book, str, str2, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x04b1  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x04b7  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x04f1  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object setBookSource(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) throws Exception {
        C00821 c00821;
        Ref.ObjectRef newBookInfo;
        ReturnData returnData;
        String bookSourceString;
        String userNameSpace;
        Ref.ObjectRef objectRef;
        Book book;
        Object bookInfo$default;
        Object objCheckAuth;
        Ref.ObjectRef objectRef2;
        Book book2;
        BookController bookController;
        Book book3;
        String str;
        final Ref.ObjectRef objectRef3;
        String bookUrl;
        String newBookUrl;
        String bookSourceUrl;
        if ($completion instanceof C00821) {
            c00821 = (C00821) $completion;
            if ((c00821.label & Integer.MIN_VALUE) != 0) {
                c00821.label -= Integer.MIN_VALUE;
            } else {
                c00821 = new C00821($completion);
            }
        }
        Object $result = c00821.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00821.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00821.L$0 = this;
                c00821.L$1 = context;
                c00821.L$2 = returnData;
                c00821.label = 1;
                objCheckAuth = checkAuth(context, c00821);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookUrl\")");
                    bookUrl = string;
                    String string2 = context.getBodyAsJson().getString("newUrl");
                    Intrinsics.checkNotNullExpressionValue(string2, "context.bodyAsJson.getString(\"newUrl\")");
                    newBookUrl = string2;
                    String string3 = context.getBodyAsJson().getString("bookSourceUrl");
                    Intrinsics.checkNotNullExpressionValue(string3, "context.bodyAsJson.getString(\"bookSourceUrl\")");
                    bookSourceUrl = string3;
                } else {
                    List listQueryParam = context.queryParam("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookUrl\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam2 = context.queryParam("newUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"newUrl\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    newBookUrl = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
                    List listQueryParam3 = context.queryParam("bookSourceUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"bookSourceUrl\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    bookSourceUrl = str4 == null ? PackageDocumentBase.PREFIX_OPF : str4;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("书籍链接不能为空");
                }
                if (newBookUrl.length() == 0) {
                    return returnData.setErrorMsg("新源书籍链接不能为空");
                }
                if (bookSourceUrl.length() == 0) {
                    return returnData.setErrorMsg("书源链接不能为空");
                }
                userNameSpace = this.getUserNameSpace(context);
                book = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (book == null) {
                    return returnData.setErrorMsg("书籍信息错误");
                }
                bookSourceString = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
                Book searchBook = null;
                String str5 = bookSourceString;
                if (str5 == null || str5.length() == 0) {
                    JsonArray localBookSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), "bookSource"));
                    if (localBookSourceList != null) {
                        int i = 0;
                        int size = localBookSourceList.size();
                        if (0 < size) {
                            while (true) {
                                int i2 = i;
                                i++;
                                SearchBook _searchBook = (SearchBook) localBookSourceList.getJsonObject(i2).mapTo(SearchBook.class);
                                if (_searchBook.getBookUrl().equals(newBookUrl)) {
                                    searchBook = _searchBook.toBook();
                                } else if (i >= size) {
                                }
                            }
                        }
                    }
                    if (searchBook == null) {
                        return returnData.setErrorMsg("书源信息错误");
                    }
                }
                newBookInfo = new Ref.ObjectRef();
                objectRef2 = newBookInfo;
                if (searchBook != null) {
                    book2 = searchBook;
                    objectRef2.element = book2;
                    objectRef3 = newBookInfo;
                    c00821.L$0 = this;
                    c00821.L$1 = returnData;
                    c00821.L$2 = userNameSpace;
                    c00821.L$3 = bookSourceString;
                    c00821.L$4 = newBookInfo;
                    c00821.L$5 = null;
                    c00821.L$6 = null;
                    c00821.label = 3;
                    if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.setBookSource.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @NotNull
                        public final Book invoke(@NotNull Book existBook) {
                            Intrinsics.checkNotNullParameter(existBook, "existBook");
                            existBook.setOrigin(((Book) objectRef3.element).getOrigin());
                            existBook.setOriginName(((Book) objectRef3.element).getOriginName());
                            existBook.setBookUrl(((Book) objectRef3.element).getBookUrl());
                            existBook.setTocUrl(((Book) objectRef3.element).getTocUrl());
                            existBook.setInShelf(true);
                            String coverUrl = existBook.getCoverUrl();
                            if (coverUrl == null || coverUrl.length() == 0) {
                                String coverUrl2 = ((Book) objectRef3.element).getCoverUrl();
                                if (!(coverUrl2 == null || coverUrl2.length() == 0)) {
                                    existBook.setCoverUrl(((Book) objectRef3.element).getCoverUrl());
                                }
                            }
                            BookControllerKt.logger.info("setBookSource: {}", existBook);
                            objectRef3.element = existBook;
                            return existBook;
                        }
                    }, c00821) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookController = this;
                    book3 = (Book) newBookInfo.element;
                    String str6 = bookSourceString;
                    str = str6 != null ? PackageDocumentBase.PREFIX_OPF : str6;
                    c00821.L$0 = returnData;
                    c00821.L$1 = newBookInfo;
                    c00821.L$2 = null;
                    c00821.L$3 = null;
                    c00821.L$4 = null;
                    c00821.label = 4;
                    if (getLocalChapterList$default(bookController, book3, str, true, userNameSpace, false, null, c00821, 48, null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
                }
                String str7 = bookSourceString;
                if (str7 == null || str7.length() == 0) {
                    return returnData.setErrorMsg("书源信息错误");
                }
                objectRef = objectRef2;
                c00821.L$0 = this;
                c00821.L$1 = returnData;
                c00821.L$2 = userNameSpace;
                c00821.L$3 = book;
                c00821.L$4 = bookSourceString;
                c00821.L$5 = newBookInfo;
                c00821.L$6 = objectRef;
                c00821.label = 2;
                bookInfo$default = WebBook.getBookInfo$default(new WebBook(bookSourceString, this.getAppConfig().getDebugLog(), (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null), newBookUrl, false, (Continuation) c00821, 2, (Object) null);
                if (bookInfo$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Object obj = bookInfo$default;
                objectRef2 = objectRef;
                book2 = (Book) obj;
                objectRef2.element = book2;
                objectRef3 = newBookInfo;
                c00821.L$0 = this;
                c00821.L$1 = returnData;
                c00821.L$2 = userNameSpace;
                c00821.L$3 = bookSourceString;
                c00821.L$4 = newBookInfo;
                c00821.L$5 = null;
                c00821.L$6 = null;
                c00821.label = 3;
                if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.setBookSource.2
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    @NotNull
                    public final Book invoke(@NotNull Book existBook) {
                        Intrinsics.checkNotNullParameter(existBook, "existBook");
                        existBook.setOrigin(((Book) objectRef3.element).getOrigin());
                        existBook.setOriginName(((Book) objectRef3.element).getOriginName());
                        existBook.setBookUrl(((Book) objectRef3.element).getBookUrl());
                        existBook.setTocUrl(((Book) objectRef3.element).getTocUrl());
                        existBook.setInShelf(true);
                        String coverUrl = existBook.getCoverUrl();
                        if (coverUrl == null || coverUrl.length() == 0) {
                            String coverUrl2 = ((Book) objectRef3.element).getCoverUrl();
                            if (!(coverUrl2 == null || coverUrl2.length() == 0)) {
                                existBook.setCoverUrl(((Book) objectRef3.element).getCoverUrl());
                            }
                        }
                        BookControllerKt.logger.info("setBookSource: {}", existBook);
                        objectRef3.element = existBook;
                        return existBook;
                    }
                }, c00821) == coroutine_suspended) {
                }
                bookController = this;
                book3 = (Book) newBookInfo.element;
                String str62 = bookSourceString;
                if (str62 != null) {
                }
                c00821.L$0 = returnData;
                c00821.L$1 = newBookInfo;
                c00821.L$2 = null;
                c00821.L$3 = null;
                c00821.L$4 = null;
                c00821.label = 4;
                if (getLocalChapterList$default(bookController, book3, str, true, userNameSpace, false, null, c00821, 48, null) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
            case 1:
                returnData = (ReturnData) c00821.L$2;
                context = (RoutingContext) c00821.L$1;
                this = (BookController) c00821.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                objectRef = (Ref.ObjectRef) c00821.L$6;
                newBookInfo = (Ref.ObjectRef) c00821.L$5;
                bookSourceString = (String) c00821.L$4;
                book = (Book) c00821.L$3;
                userNameSpace = (String) c00821.L$2;
                returnData = (ReturnData) c00821.L$1;
                this = (BookController) c00821.L$0;
                ResultKt.throwOnFailure($result);
                bookInfo$default = $result;
                Object obj2 = bookInfo$default;
                objectRef2 = objectRef;
                book2 = (Book) obj2;
                objectRef2.element = book2;
                objectRef3 = newBookInfo;
                c00821.L$0 = this;
                c00821.L$1 = returnData;
                c00821.L$2 = userNameSpace;
                c00821.L$3 = bookSourceString;
                c00821.L$4 = newBookInfo;
                c00821.L$5 = null;
                c00821.L$6 = null;
                c00821.label = 3;
                if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.setBookSource.2
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    @NotNull
                    public final Book invoke(@NotNull Book existBook) {
                        Intrinsics.checkNotNullParameter(existBook, "existBook");
                        existBook.setOrigin(((Book) objectRef3.element).getOrigin());
                        existBook.setOriginName(((Book) objectRef3.element).getOriginName());
                        existBook.setBookUrl(((Book) objectRef3.element).getBookUrl());
                        existBook.setTocUrl(((Book) objectRef3.element).getTocUrl());
                        existBook.setInShelf(true);
                        String coverUrl = existBook.getCoverUrl();
                        if (coverUrl == null || coverUrl.length() == 0) {
                            String coverUrl2 = ((Book) objectRef3.element).getCoverUrl();
                            if (!(coverUrl2 == null || coverUrl2.length() == 0)) {
                                existBook.setCoverUrl(((Book) objectRef3.element).getCoverUrl());
                            }
                        }
                        BookControllerKt.logger.info("setBookSource: {}", existBook);
                        objectRef3.element = existBook;
                        return existBook;
                    }
                }, c00821) == coroutine_suspended) {
                }
                bookController = this;
                book3 = (Book) newBookInfo.element;
                String str622 = bookSourceString;
                if (str622 != null) {
                }
                c00821.L$0 = returnData;
                c00821.L$1 = newBookInfo;
                c00821.L$2 = null;
                c00821.L$3 = null;
                c00821.L$4 = null;
                c00821.label = 4;
                if (getLocalChapterList$default(bookController, book3, str, true, userNameSpace, false, null, c00821, 48, null) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
            case 3:
                newBookInfo = (Ref.ObjectRef) c00821.L$4;
                bookSourceString = (String) c00821.L$3;
                userNameSpace = (String) c00821.L$2;
                returnData = (ReturnData) c00821.L$1;
                this = (BookController) c00821.L$0;
                ResultKt.throwOnFailure($result);
                bookController = this;
                book3 = (Book) newBookInfo.element;
                String str6222 = bookSourceString;
                if (str6222 != null) {
                }
                c00821.L$0 = returnData;
                c00821.L$1 = newBookInfo;
                c00821.L$2 = null;
                c00821.L$3 = null;
                c00821.L$4 = null;
                c00821.label = 4;
                if (getLocalChapterList$default(bookController, book3, str, true, userNameSpace, false, null, c00821, 48, null) == coroutine_suspended) {
                }
                return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
            case 4:
                newBookInfo = (Ref.ObjectRef) c00821.L$1;
                returnData = (ReturnData) c00821.L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData, newBookInfo.element, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookConfig(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00541 c00541;
        Book book;
        ReturnData returnData;
        Object objEditShelfBook;
        Object objCheckAuth;
        String bookUrl;
        Float fBoxFloat;
        if ($completion instanceof C00541) {
            c00541 = (C00541) $completion;
            if ((c00541.label & Integer.MIN_VALUE) != 0) {
                c00541.label -= Integer.MIN_VALUE;
            } else {
                c00541 = new C00541($completion);
            }
        }
        Object $result = c00541.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00541.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00541.L$0 = this;
                c00541.L$1 = context;
                c00541.L$2 = returnData;
                c00541.label = 1;
                objCheckAuth = checkAuth(context, c00541);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                final Ref.FloatRef pdfImageWidth = new Ref.FloatRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookUrl\")");
                    bookUrl = string;
                    Float f = context.getBodyAsJson().getFloat("pdfImageWidth", Boxing.boxFloat(0.0f));
                    Intrinsics.checkNotNullExpressionValue(f, "context.bodyAsJson.getFloat(\"pdfImageWidth\", 0f)");
                    pdfImageWidth.element = f.floatValue();
                } else {
                    List listQueryParam = context.queryParam("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookUrl\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("pdfImageWidth");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"pdfImageWidth\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    float fFloatValue = (str2 == null || (fBoxFloat = Boxing.boxFloat(Float.parseFloat(str2))) == null) ? 0.0f : fBoxFloat.floatValue();
                    pdfImageWidth.element = fFloatValue;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("书籍链接不能为空");
                }
                String userNameSpace = this.getUserNameSpace(context);
                book = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (book == null) {
                    return returnData.setErrorMsg("书籍信息错误");
                }
                if (pdfImageWidth.element <= 0.0f) {
                    return returnData.setErrorMsg("pdf图片宽度错误");
                }
                c00541.L$0 = returnData;
                c00541.L$1 = book;
                c00541.L$2 = null;
                c00541.label = 2;
                objEditShelfBook = this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController$saveBookConfig$newBook$1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    @NotNull
                    public final Book invoke(@NotNull Book existBook) {
                        Intrinsics.checkNotNullParameter(existBook, "existBook");
                        existBook.setPdfImageWidth(pdfImageWidth.element);
                        BookControllerKt.logger.info("saveBookConfig: {}", existBook);
                        return existBook;
                    }
                }, c00541);
                if (objEditShelfBook == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Book newBook = (Book) objEditShelfBook;
                return ReturnData.setData$default(returnData, newBook != null ? book : newBook, null, 2, null);
            case 1:
                returnData = (ReturnData) c00541.L$2;
                context = (RoutingContext) c00541.L$1;
                this = (BookController) c00541.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                book = (Book) c00541.L$1;
                returnData = (ReturnData) c00541.L$0;
                ResultKt.throwOnFailure($result);
                objEditShelfBook = $result;
                Book newBook2 = (Book) objEditShelfBook;
                return ReturnData.setData$default(returnData, newBook2 != null ? book : newBook2, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveBookGroupId(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00571 c00571;
        Book book;
        final Ref.LongRef groupId;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        Long lBoxLong;
        if ($completion instanceof C00571) {
            c00571 = (C00571) $completion;
            if ((c00571.label & Integer.MIN_VALUE) != 0) {
                c00571.label -= Integer.MIN_VALUE;
            } else {
                c00571 = new C00571($completion);
            }
        }
        Object $result = c00571.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00571.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00571.L$0 = this;
                c00571.L$1 = context;
                c00571.L$2 = returnData;
                c00571.label = 1;
                objCheckAuth = checkAuth(context, c00571);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                groupId = new Ref.LongRef();
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookUrl\")");
                    bookUrl = string;
                    Long l = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                    Intrinsics.checkNotNullExpressionValue(l, "context.bodyAsJson.getLong(\"groupId\", 0)");
                    groupId.element = l.longValue();
                } else {
                    List listQueryParam = context.queryParam("bookUrl");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookUrl\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("groupId");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"groupId\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    long jLongValue = (str2 == null || (lBoxLong = Boxing.boxLong(Long.parseLong(str2))) == null) ? 0L : lBoxLong.longValue();
                    groupId.element = jLongValue;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("书籍链接不能为空");
                }
                String userNameSpace = this.getUserNameSpace(context);
                book = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (book == null) {
                    return returnData.setErrorMsg("书籍信息错误");
                }
                if (groupId.element > 0) {
                    c00571.L$0 = returnData;
                    c00571.L$1 = groupId;
                    c00571.L$2 = book;
                    c00571.label = 2;
                    if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.saveBookGroupId.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @NotNull
                        public final Book invoke(@NotNull Book existBook) {
                            Intrinsics.checkNotNullParameter(existBook, "existBook");
                            existBook.setGroup(groupId.element);
                            BookControllerKt.logger.info("saveBookGroupId: {}", existBook);
                            return existBook;
                        }
                    }, c00571) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    book.setGroup(groupId.element);
                    return ReturnData.setData$default(returnData, book, null, 2, null);
                }
                return returnData.setErrorMsg("分组信息错误");
            case 1:
                returnData = (ReturnData) c00571.L$2;
                context = (RoutingContext) c00571.L$1;
                this = (BookController) c00571.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                book = (Book) c00571.L$2;
                groupId = (Ref.LongRef) c00571.L$1;
                returnData = (ReturnData) c00571.L$0;
                ResultKt.throwOnFailure($result);
                book.setGroup(groupId.element);
                return ReturnData.setData$default(returnData, book, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0128, code lost:

        if (0 < r18) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x012b, code lost:

        r0 = r17;
        r17 = r17 + 1;
        r0 = (io.legado.app.data.entities.Book) r16.getJsonObject(r0).mapTo(io.legado.app.data.entities.Book.class);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "book");
        r5 = r13;
        r23.L$0 = r9;
        r23.L$1 = r12;
        r23.L$2 = r15;
        r23.L$3 = r16;
        r23.J$0 = r13;
        r23.I$0 = r17;
        r23.I$1 = r18;
        r23.label = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x019e, code lost:

        if (r9.editShelfBook(r0, r15, new com.htmake.reader.api.controller.BookController.AnonymousClass2(), r23) != r0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x01a3, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x01eb, code lost:

        if (r17 >= r18) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x01f8, code lost:

        return com.htmake.reader.api.ReturnData.setData$default(r12, me.ag2s.epublib.epub.PackageDocumentBase.PREFIX_OPF, null, 2, null);
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x01eb -> B:25:0x012b). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object addBookGroupMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        AnonymousClass1 anonymousClass1;
        int size;
        int i;
        long groupId;
        JsonArray bookJsonArray;
        String userNameSpace;
        ReturnData returnData;
        Object objCheckAuth;
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
        switch (anonymousClass1.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                anonymousClass1.L$0 = this;
                anonymousClass1.L$1 = context;
                anonymousClass1.L$2 = returnData;
                anonymousClass1.label = 1;
                objCheckAuth = checkAuth(context, anonymousClass1);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                Long l = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                Intrinsics.checkNotNullExpressionValue(l, "context.bodyAsJson.getLong(\"groupId\", 0)");
                groupId = l.longValue();
                if (groupId <= 0) {
                    return returnData.setErrorMsg("分组信息错误");
                }
                userNameSpace = this.getUserNameSpace(context);
                bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
                i = 0;
                size = bookJsonArray.size();
                break;
                break;
            case 1:
                returnData = (ReturnData) anonymousClass1.L$2;
                context = (RoutingContext) anonymousClass1.L$1;
                this = (BookController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                size = anonymousClass1.I$1;
                i = anonymousClass1.I$0;
                groupId = anonymousClass1.J$0;
                bookJsonArray = (JsonArray) anonymousClass1.L$3;
                userNameSpace = (String) anonymousClass1.L$2;
                returnData = (ReturnData) anonymousClass1.L$1;
                this = (BookController) anonymousClass1.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0128, code lost:

        if (0 < r18) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x012b, code lost:

        r0 = r17;
        r17 = r17 + 1;
        r0 = (io.legado.app.data.entities.Book) r16.getJsonObject(r0).mapTo(io.legado.app.data.entities.Book.class);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "book");
        r5 = r13;
        r23.L$0 = r9;
        r23.L$1 = r12;
        r23.L$2 = r15;
        r23.L$3 = r16;
        r23.J$0 = r13;
        r23.I$0 = r17;
        r23.I$1 = r18;
        r23.label = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x019e, code lost:

        if (r9.editShelfBook(r0, r15, new com.htmake.reader.api.controller.BookController.C00512(), r23) != r0) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x01a3, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x01eb, code lost:

        if (r17 >= r18) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x01f8, code lost:

        return com.htmake.reader.api.ReturnData.setData$default(r12, me.ag2s.epublib.epub.PackageDocumentBase.PREFIX_OPF, null, 2, null);
     */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x01eb -> B:25:0x012b). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object removeBookGroupMulti(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00501 c00501;
        int size;
        int i;
        long groupId;
        JsonArray bookJsonArray;
        String userNameSpace;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00501) {
            c00501 = (C00501) $completion;
            if ((c00501.label & Integer.MIN_VALUE) != 0) {
                c00501.label -= Integer.MIN_VALUE;
            } else {
                c00501 = new C00501($completion);
            }
        }
        Object $result = c00501.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00501.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00501.L$0 = this;
                c00501.L$1 = context;
                c00501.L$2 = returnData;
                c00501.label = 1;
                objCheckAuth = checkAuth(context, c00501);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                Long l = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                Intrinsics.checkNotNullExpressionValue(l, "context.bodyAsJson.getLong(\"groupId\", 0)");
                groupId = l.longValue();
                if (groupId <= 0) {
                    return returnData.setErrorMsg("分组信息错误");
                }
                userNameSpace = this.getUserNameSpace(context);
                bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
                i = 0;
                size = bookJsonArray.size();
                break;
                break;
            case 1:
                returnData = (ReturnData) c00501.L$2;
                context = (RoutingContext) c00501.L$1;
                this = (BookController) c00501.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                size = c00501.I$1;
                i = c00501.I$0;
                groupId = c00501.J$0;
                bookJsonArray = (JsonArray) c00501.L$3;
                userNameSpace = (String) c00501.L$2;
                returnData = (ReturnData) c00501.L$1;
                this = (BookController) c00501.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBook(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00181 c00181;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00181) {
            c00181 = (C00181) $completion;
            if ((c00181.label & Integer.MIN_VALUE) != 0) {
                c00181.label -= Integer.MIN_VALUE;
            } else {
                c00181 = new C00181($completion);
            }
        }
        Object $result = c00181.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00181.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00181.L$0 = this;
                c00181.L$1 = context;
                c00181.L$2 = returnData;
                c00181.label = 1;
                objCheckAuth = checkAuth(context, c00181);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00181.L$2;
                context = (RoutingContext) c00181.L$1;
                this = (BookController) c00181.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        Book book = (Book) context.getBodyAsJson().mapTo(Book.class);
        String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        int existIndex = -1;
        String bookName = PackageDocumentBase.PREFIX_OPF;
        String bookAuthor = PackageDocumentBase.PREFIX_OPF;
        int i = 0;
        int size = bookshelf.size();
        if (0 < size) {
            while (true) {
                int i2 = i;
                i++;
                String string = bookshelf.getJsonObject(i2).getString("name", PackageDocumentBase.PREFIX_OPF);
                Intrinsics.checkNotNullExpressionValue(string, "bookshelf.getJsonObject(i).getString(\"name\", \"\")");
                bookName = string;
                String string2 = bookshelf.getJsonObject(i2).getString("author", PackageDocumentBase.PREFIX_OPF);
                Intrinsics.checkNotNullExpressionValue(string2, "bookshelf.getJsonObject(i).getString(\"author\", \"\")");
                bookAuthor = string2;
                String bookUrl = bookshelf.getJsonObject(i2).getString("bookUrl", PackageDocumentBase.PREFIX_OPF);
                Intrinsics.checkNotNullExpressionValue(bookUrl, "bookshelf.getJsonObject(i).getString(\"bookUrl\", \"\")");
                if (bookUrl.equals(book.getBookUrl())) {
                    existIndex = i2;
                } else if (!bookName.equals(book.getName()) || !bookAuthor.equals(book.getAuthor())) {
                    if (i >= size) {
                    }
                } else {
                    existIndex = i2;
                }
            }
        }
        if (existIndex < 0) {
            return returnData.setErrorMsg("书架书籍不存在");
        }
        JsonObject existBook = bookshelf.getJsonObject(existIndex);
        bookshelf.remove(existIndex);
        this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        File localBookPath = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, bookName + '_' + bookAuthor));
        ExtKt.deleteRecursively(localBookPath);
        String string3 = existBook.getString("coverUrl");
        if (!(string3 == null || string3.length() == 0)) {
            String string4 = existBook.getString("coverUrl");
            Intrinsics.checkNotNull(string4);
            if (StringsKt.startsWith$default(string4, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                String string5 = existBook.getString("coverUrl");
                Intrinsics.checkNotNull(string5);
                String cachePath = ExtKt.getWorkDir("storage", string5);
                FileUtils.INSTANCE.deleteFile(cachePath);
            }
        }
        return ReturnData.setData$default(returnData, "删除书籍成功", null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBooks(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00201 c00201;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00201) {
            c00201 = (C00201) $completion;
            if ((c00201.label & Integer.MIN_VALUE) != 0) {
                c00201.label -= Integer.MIN_VALUE;
            } else {
                c00201 = new C00201($completion);
            }
        }
        Object $result = c00201.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00201.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00201.L$0 = this;
                c00201.L$1 = context;
                c00201.L$2 = returnData;
                c00201.label = 1;
                objCheckAuth = checkAuth(context, c00201);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00201.L$2;
                context = (RoutingContext) c00201.L$1;
                this = (BookController) c00201.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        JsonArray bookJsonArray = context.getBodyAsJsonArray();
        String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        Map infoMap = new LinkedHashMap();
        int i = 0;
        int size = bookJsonArray.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String string = bookJsonArray.getJsonObject(i2).getString("bookUrl", PackageDocumentBase.PREFIX_OPF);
                Intrinsics.checkNotNullExpressionValue(string, "bookJsonArray.getJsonObject(i).getString(\"bookUrl\", \"\")");
                infoMap.put(string, Boxing.boxInt(i2));
                infoMap.put(bookJsonArray.getJsonObject(i2).getString("name", PackageDocumentBase.PREFIX_OPF) + '_' + ((Object) bookshelf.getJsonObject(i2).getString("author", PackageDocumentBase.PREFIX_OPF)), Boxing.boxInt(i2));
            } while (i < size);
        }
        Iterator iterator = bookshelf.iterator();
        Intrinsics.checkNotNullExpressionValue(iterator, "bookshelf.iterator()");
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next == null) {
                throw new NullPointerException("null cannot be cast to non-null type io.vertx.core.json.JsonObject");
            }
            JsonObject book = (JsonObject) next;
            String bookName = book.getString("name", PackageDocumentBase.PREFIX_OPF);
            String bookAuthor = book.getString("author", PackageDocumentBase.PREFIX_OPF);
            String bookUrl = book.getString("bookUrl", PackageDocumentBase.PREFIX_OPF);
            Intrinsics.checkNotNullExpressionValue(bookUrl, "bookUrl");
            int existIndex = ((Number) infoMap.getOrDefault(bookUrl, infoMap.getOrDefault(bookName + '_' + ((Object) bookAuthor), Boxing.boxInt(-1)))).intValue();
            if (existIndex >= 0) {
                iterator.remove();
                File localBookPath = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, bookName + '_' + ((Object) bookAuthor)));
                ExtKt.deleteRecursively(localBookPath);
            }
        }
        this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    @Nullable
    public final Object saveBookInfoCache(@NotNull List<Book> bookList, @NotNull Continuation<? super List<Book>> $completion) {
        if (bookList.size() > 0) {
            int i = 0;
            int size = bookList.size();
            if (0 < size) {
                do {
                    int i2 = i;
                    i++;
                    Book book = bookList.get(i2);
                    ACache aCache = this.bookInfoCache;
                    String bookUrl = book.getBookUrl();
                    Map map = JsonObject.mapFrom(book).getMap();
                    Intrinsics.checkNotNullExpressionValue(map, "mapFrom(book).map");
                    aCache.put(bookUrl, ExtKt.jsonEncode$default(map, false, 2, null));
                } while (i < size);
            }
        }
        return bookList;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.htmake.reader.api.controller.BookController$mergeBookCacheInfo$$inlined$toDataClass$1] */
    @Nullable
    public final Object mergeBookCacheInfo(@NotNull Book book, @NotNull Continuation<? super Book> $completion) {
        Object map;
        String json;
        Book book2;
        String asString = this.bookInfoCache.getAsString(book.getBookUrl());
        if (asString == null || (map = ExtKt.toMap(asString)) == null) {
            book2 = null;
        } else {
            if (!(map instanceof String)) {
                json = ExtKt.getGson().toJson(map);
            } else {
                json = (String) map;
            }
            String json$iv$iv = json;
            book2 = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$mergeBookCacheInfo$$inlined$toDataClass$1
            }.getType());
        }
        Book cacheInfo = book2;
        if (cacheInfo != null) {
            return ExtKt.fillData(book, cacheInfo, CollectionsKt.listOf(new String[]{"name", "author", "coverUrl", "tocUrl", "intro", "latestChapterTitle", "wordCount"}));
        }
        return book;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getBookShelfBooks(boolean refresh, @NotNull String userNameSpace, @NotNull Continuation<? super List<Book>> $completion) {
        C00341 c00341;
        Ref.ObjectRef bookList;
        if ($completion instanceof C00341) {
            c00341 = (C00341) $completion;
            if ((c00341.label & Integer.MIN_VALUE) != 0) {
                c00341.label -= Integer.MIN_VALUE;
            } else {
                c00341 = new C00341($completion);
            }
        }
        Object $result = c00341.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00341.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                Ref.ObjectRef bookshelf = new Ref.ObjectRef();
                bookshelf.element = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf"));
                if (bookshelf.element != null && ((JsonArray) bookshelf.element).size() != 0) {
                    bookList = new Ref.ObjectRef();
                    bookList.element = new ArrayList();
                    Mutex mutex = MutexKt.Mutex$default(false, 1, (Object) null);
                    Mutex syncMutex = MutexKt.Mutex$default(false, 1, (Object) null);
                    int size = ((JsonArray) bookshelf.element).size();
                    boolean z = refresh;
                    c00341.L$0 = bookList;
                    c00341.label = 1;
                    if (limitConcurrent(16, 0, size, new C00352(bookshelf, z, this, userNameSpace, syncMutex, bookList, mutex, null), c00341) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
                return new ArrayList();
            case 1:
                bookList = (Ref.ObjectRef) c00341.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return bookList.element;
    }

    public static /* synthetic */ Object getBookShelfBooks$default(BookController bookController, boolean z, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return bookController.getBookShelfBooks(z, str, continuation);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookShelfBooks$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookShelfBooks$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {1961, 1970}, i = {0, 1}, s = {"L$0", "L$0"}, n = {"book", "book"}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$getBookShelfBooks$2")
    static final class C00352 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        Object L$0;
        int label;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.ObjectRef<JsonArray> $bookshelf;
        final /* synthetic */ boolean $refresh;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ String $userNameSpace;
        final /* synthetic */ Mutex $syncMutex;
        final /* synthetic */ Ref.ObjectRef<ArrayList<Book>> $bookList;
        final /* synthetic */ Mutex $mutex;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00352(Ref.ObjectRef<JsonArray> $bookshelf, boolean $refresh, BookController this$0, String $userNameSpace, Mutex $syncMutex, Ref.ObjectRef<ArrayList<Book>> $bookList, Mutex $mutex, Continuation<? super C00352> $completion) {
            super(3, $completion);
            this.$bookshelf = $bookshelf;
            this.$refresh = $refresh;
            this.this$0 = this$0;
            this.$userNameSpace = $userNameSpace;
            this.$syncMutex = $syncMutex;
            this.$bookList = $bookList;
            this.$mutex = $mutex;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00352 c00352 = new C00352(this.$bookshelf, this.$refresh, this.this$0, this.$userNameSpace, this.$syncMutex, this.$bookList, this.$mutex, p3);
            c00352.I$0 = p2;
            return c00352.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
            jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:10:0x0078
                at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
                at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
            */
        @org.jetbrains.annotations.Nullable
        public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r12) {
            /*
                Method dump skipped, instruction units count: 371
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.htmake.reader.api.controller.BookController.C00352.invokeSuspend(java.lang.Object):java.lang.Object");
        }

        /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$getBookShelfBooks$2$1, reason: invalid class name */
        /* JADX INFO: compiled from: BookController.kt */
        /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$getBookShelfBooks$2$1.class */
        @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lio/legado/app/data/entities/BookChapter;", "Lkotlinx/coroutines/CoroutineScope;"})
        @DebugMetadata(f = "BookController.kt", l = {1962}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$getBookShelfBooks$2$1")
        static final class AnonymousClass1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super List<? extends BookChapter>>, Object> {
            int label;
            final /* synthetic */ BookController this$0;
            final /* synthetic */ Ref.ObjectRef<Book> $book;
            final /* synthetic */ Ref.ObjectRef<String> $bookSource;
            final /* synthetic */ boolean $refresh;
            final /* synthetic */ String $userNameSpace;
            final /* synthetic */ Mutex $mutex;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(BookController this$0, Ref.ObjectRef<Book> $book, Ref.ObjectRef<String> $bookSource, boolean $refresh, String $userNameSpace, Mutex $mutex, Continuation<? super AnonymousClass1> $completion) {
                super(2, $completion);
                this.this$0 = this$0;
                this.$book = $book;
                this.$bookSource = $bookSource;
                this.$refresh = $refresh;
                this.$userNameSpace = $userNameSpace;
                this.$mutex = $mutex;
            }

            @NotNull
            public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                return new AnonymousClass1(this.this$0, this.$book, this.$bookSource, this.$refresh, this.$userNameSpace, this.$mutex, $completion);
            }

            @Nullable
            public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super List<BookChapter>> p2) {
                return create(p1, p2).invokeSuspend(Unit.INSTANCE);
            }

            @Nullable
            public final Object invokeSuspend(@NotNull Object $result) throws Exception {
                Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                switch (this.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        BookController bookController = this.this$0;
                        Object obj = this.$book.element;
                        Intrinsics.checkNotNullExpressionValue(obj, "book");
                        this.label = 1;
                        Object localChapterList = bookController.getLocalChapterList((Book) obj, (String) this.$bookSource.element, this.$refresh, this.$userNameSpace, false, this.$mutex, (Continuation) this);
                        if (localChapterList == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return localChapterList;
                    case 1:
                        ResultKt.throwOnFailure($result);
                        return $result;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:135:0x0548  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x0576  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x05b3  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0629  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0306  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x030a  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x0353  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getLocalChapterList(@NotNull Book book, @Nullable String bookSource, boolean refresh, @NotNull String userNameSpace, boolean debugLog, @Nullable Mutex mutex, @NotNull Continuation<? super List<BookChapter>> $completion) throws Exception {
        C00411 c00411;
        Mutex mutex2;
        List<BookChapter> chapterList;
        ACache bookChaptersCache;
        String md5Encode;
        Object chapterList2;
        TocRule ruleToc;
        String it;
        if ($completion instanceof C00411) {
            c00411 = (C00411) $completion;
            if ((c00411.label & Integer.MIN_VALUE) != 0) {
                c00411.label -= Integer.MIN_VALUE;
            } else {
                c00411 = new C00411($completion);
            }
        }
        Object $result = c00411.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            try {
            } catch (Exception e) {
                e = e;
                String str = bookSource;
                if (!(str == null || str.length() == 0)) {
                    Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSource);
                    BookSource bookSourceObject = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                    if (bookSourceObject != null) {
                        this.addInvalidBookSource(bookSourceObject.getBookSourceUrl(), MapsKt.mutableMapOf(new Pair[]{TuplesKt.to("sourceUrl", bookSourceObject.getBookSourceUrl()), TuplesKt.to(RSSKeywords.RSS_ITEM_TIME, Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to("error", e.toString())}), userNameSpace);
                    }
                }
                Mutex mutex3 = mutex;
                if (mutex3 != null) {
                    c00411.L$0 = this;
                    c00411.L$1 = book;
                    c00411.L$2 = userNameSpace;
                    c00411.L$3 = mutex;
                    c00411.L$4 = e;
                    c00411.L$5 = null;
                    c00411.L$6 = null;
                    c00411.label = 3;
                    if (Mutex.DefaultImpls.lock$default(mutex3, (Object) null, c00411, 1, (Object) null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
            }
            switch (c00411.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    md5Encode = MD5Utils.INSTANCE.md5Encode(book.getBookUrl()).toString();
                    bookChaptersCache = getBookChaptersCache(userNameSpace);
                    JsonArray chapterList3 = book.isInShelf() ? ExtKt.asJsonArray(getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), md5Encode)) : ExtKt.asJsonArray(bookChaptersCache.getAsString(book.getName() + '_' + book.getAuthor() + md5Encode));
                    if (chapterList3 != null && !refresh) {
                        ArrayList localChapterList = new ArrayList();
                        int i = 0;
                        int size = chapterList3.size();
                        if (0 < size) {
                            do {
                                int i2 = i;
                                i++;
                                BookChapter _chapter = (BookChapter) chapterList3.getJsonObject(i2).mapTo(BookChapter.class);
                                localChapterList.add(_chapter);
                            } while (i < size);
                        }
                        return localChapterList;
                    }
                    book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                    book.setUserNameSpace(userNameSpace);
                    if (book.isLocalBook()) {
                        if (book.isEpub()) {
                            if (!extractEpub(book, refresh)) {
                                throw new Exception("Epub书籍解压失败");
                            }
                        }
                        if (book.isCbz()) {
                            if (!extractCbz(book, refresh)) {
                                throw new Exception("CBZ书籍解压失败");
                            }
                        }
                        if (book.isPdf()) {
                            if (!convertPdfToImage(book, refresh)) {
                                throw new Exception("PDF书籍转换失败");
                            }
                        }
                        chapterList = LocalBook.INSTANCE.getChapterList(book);
                        if (book.isInShelf()) {
                            bookChaptersCache.put(book.getName() + '_' + book.getAuthor() + md5Encode, ExtKt.jsonEncode$default(chapterList, false, 2, null), ACache.TIME_HOUR);
                        } else {
                            this.saveUserStorage(userNameSpace, ExtKt.getRelativePath(book.getName() + '_' + book.getAuthor(), md5Encode), chapterList);
                        }
                        c00411.L$0 = chapterList;
                        c00411.L$1 = null;
                        c00411.L$2 = null;
                        c00411.L$3 = null;
                        c00411.L$4 = null;
                        c00411.L$5 = null;
                        c00411.L$6 = null;
                        c00411.label = 5;
                        if (this.saveShelfBookLatestChapter(book, chapterList, userNameSpace, mutex, c00411) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        return chapterList;
                    }
                    String str2 = bookSource;
                    if (!(str2 == null || str2.length() == 0)) {
                        Object objM151fromJsonIoAF18A2 = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSource);
                        Object bookSourceObject2 = Result.isFailure-impl(objM151fromJsonIoAF18A2) ? null : objM151fromJsonIoAF18A2;
                        BookSource bookSource2 = (BookSource) bookSourceObject2;
                        if (bookSource2 != null && (ruleToc = bookSource2.getRuleToc()) != null && (it = ruleToc.getPreUpdateJs()) != null) {
                            AnalyzeRule.evalJS$default(new AnalyzeRule(book, (BaseSource) bookSourceObject2, null, 4, null), it, null, 2, null);
                        }
                        if (StringsKt.isBlank(book.getTocUrl())) {
                            boolean z = debugLog;
                            c00411.L$0 = this;
                            c00411.L$1 = book;
                            c00411.L$2 = bookSource;
                            c00411.L$3 = userNameSpace;
                            c00411.L$4 = mutex;
                            c00411.L$5 = md5Encode;
                            c00411.L$6 = bookChaptersCache;
                            c00411.Z$0 = debugLog;
                            c00411.label = 1;
                            if (WebBook.getBookInfo$default(new WebBook(bookSource, z, (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null), book, false, (Continuation) c00411, 2, (Object) null) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                        }
                    }
                    String str3 = bookSource;
                    Intrinsics.checkNotNull(str3);
                    boolean z2 = !debugLog;
                    c00411.L$0 = this;
                    c00411.L$1 = book;
                    c00411.L$2 = bookSource;
                    c00411.L$3 = userNameSpace;
                    c00411.L$4 = mutex;
                    c00411.L$5 = md5Encode;
                    c00411.L$6 = bookChaptersCache;
                    c00411.label = 2;
                    chapterList2 = new WebBook(str3, z2, (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null).getChapterList(book, c00411);
                    if (chapterList2 == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    chapterList = (List) chapterList2;
                    if (book.isInShelf()) {
                    }
                    c00411.L$0 = chapterList;
                    c00411.L$1 = null;
                    c00411.L$2 = null;
                    c00411.L$3 = null;
                    c00411.L$4 = null;
                    c00411.L$5 = null;
                    c00411.L$6 = null;
                    c00411.label = 5;
                    if (this.saveShelfBookLatestChapter(book, chapterList, userNameSpace, mutex, c00411) == coroutine_suspended) {
                    }
                    return chapterList;
                case 1:
                    debugLog = c00411.Z$0;
                    bookChaptersCache = (ACache) c00411.L$6;
                    md5Encode = (String) c00411.L$5;
                    mutex = (Mutex) c00411.L$4;
                    userNameSpace = (String) c00411.L$3;
                    bookSource = (String) c00411.L$2;
                    book = (Book) c00411.L$1;
                    this = (BookController) c00411.L$0;
                    ResultKt.throwOnFailure($result);
                    String str32 = bookSource;
                    Intrinsics.checkNotNull(str32);
                    if (!debugLog) {
                    }
                    c00411.L$0 = this;
                    c00411.L$1 = book;
                    c00411.L$2 = bookSource;
                    c00411.L$3 = userNameSpace;
                    c00411.L$4 = mutex;
                    c00411.L$5 = md5Encode;
                    c00411.L$6 = bookChaptersCache;
                    c00411.label = 2;
                    chapterList2 = new WebBook(str32, z2, (DebugLog) null, userNameSpace, 4, (DefaultConstructorMarker) null).getChapterList(book, c00411);
                    if (chapterList2 == coroutine_suspended) {
                    }
                    chapterList = (List) chapterList2;
                    if (book.isInShelf()) {
                    }
                    c00411.L$0 = chapterList;
                    c00411.L$1 = null;
                    c00411.L$2 = null;
                    c00411.L$3 = null;
                    c00411.L$4 = null;
                    c00411.L$5 = null;
                    c00411.L$6 = null;
                    c00411.label = 5;
                    if (this.saveShelfBookLatestChapter(book, chapterList, userNameSpace, mutex, c00411) == coroutine_suspended) {
                    }
                    return chapterList;
                case 2:
                    bookChaptersCache = (ACache) c00411.L$6;
                    md5Encode = (String) c00411.L$5;
                    mutex = (Mutex) c00411.L$4;
                    userNameSpace = (String) c00411.L$3;
                    bookSource = (String) c00411.L$2;
                    book = (Book) c00411.L$1;
                    this = (BookController) c00411.L$0;
                    ResultKt.throwOnFailure($result);
                    chapterList2 = $result;
                    chapterList = (List) chapterList2;
                    if (book.isInShelf()) {
                    }
                    c00411.L$0 = chapterList;
                    c00411.L$1 = null;
                    c00411.L$2 = null;
                    c00411.L$3 = null;
                    c00411.L$4 = null;
                    c00411.L$5 = null;
                    c00411.L$6 = null;
                    c00411.label = 5;
                    if (this.saveShelfBookLatestChapter(book, chapterList, userNameSpace, mutex, c00411) == coroutine_suspended) {
                    }
                    return chapterList;
                case 3:
                    e = (Exception) c00411.L$4;
                    mutex = (Mutex) c00411.L$3;
                    userNameSpace = (String) c00411.L$2;
                    book = (Book) c00411.L$1;
                    this = (BookController) c00411.L$0;
                    ResultKt.throwOnFailure($result);
                    book.setLastCheckError(e.toString());
                    final Exception exc = e;
                    c00411.L$0 = mutex;
                    c00411.L$1 = e;
                    c00411.L$2 = null;
                    c00411.L$3 = null;
                    c00411.L$4 = null;
                    c00411.L$5 = null;
                    c00411.L$6 = null;
                    c00411.label = 4;
                    if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.getLocalChapterList.3
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @NotNull
                        public final Book invoke(@NotNull Book existBook) {
                            Intrinsics.checkNotNullParameter(existBook, "existBook");
                            existBook.setLastCheckError(exc.toString());
                            return existBook;
                        }
                    }, c00411) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    mutex2 = mutex;
                    if (mutex2 != null) {
                        Mutex.DefaultImpls.unlock$default(mutex2, (Object) null, 1, (Object) null);
                    }
                    throw e;
                case 4:
                    e = (Exception) c00411.L$1;
                    mutex = (Mutex) c00411.L$0;
                    ResultKt.throwOnFailure($result);
                    mutex2 = mutex;
                    if (mutex2 != null) {
                    }
                    throw e;
                case 5:
                    chapterList = (List) c00411.L$0;
                    ResultKt.throwOnFailure($result);
                    return chapterList;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            Mutex mutex4 = mutex;
            if (mutex4 != null) {
                Mutex.DefaultImpls.unlock$default(mutex4, (Object) null, 1, (Object) null);
            }
            throw th;
        }
    }

    public static /* synthetic */ Object getLocalChapterList$default(BookController bookController, Book book, String str, boolean z, String str2, boolean z2, Mutex mutex, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        if ((i & 16) != 0) {
            z2 = true;
        }
        if ((i & 32) != 0) {
            mutex = null;
        }
        return bookController.getLocalChapterList(book, str, z, str2, z2, mutex, continuation);
    }

    public static /* synthetic */ Object getBookSourceString$default(BookController bookController, RoutingContext routingContext, String str, boolean z, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            str = PackageDocumentBase.PREFIX_OPF;
        }
        if ((i & 4) != 0) {
            z = false;
        }
        return bookController.getBookSourceString(routingContext, str, z, continuation);
    }

    @Nullable
    public final Object getBookSourceString(@NotNull RoutingContext context, @NotNull String sourceUrl, boolean withExploreUrl, @NotNull Continuation<? super String> $completion) throws Exception {
        String bookSourceUrl;
        JsonObject bookSource;
        String bookSourceString = null;
        if (context.request().method() == HttpMethod.POST && (bookSource = context.getBodyAsJson().getJsonObject("bookSource")) != null) {
            bookSourceString = bookSource.toString();
        }
        String userNameSpace = getUserNameSpace(context);
        String str = bookSourceString;
        if (str == null || str.length() == 0) {
            if (context.request().method() == HttpMethod.POST) {
                String string = context.getBodyAsJson().getString("bookSourceUrl", PackageDocumentBase.PREFIX_OPF);
                Intrinsics.checkNotNullExpressionValue(string, "context.bodyAsJson.getString(\"bookSourceUrl\", \"\")");
                bookSourceUrl = string;
            } else {
                List listQueryParam = context.queryParam("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookSourceUrl\")");
                String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                bookSourceUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
            }
            if (!StringsKt.isBlank(bookSourceUrl)) {
                bookSourceString = getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
            }
        }
        String str3 = bookSourceString;
        if (str3 == null || str3.length() == 0) {
            String str4 = sourceUrl;
            if (!(str4 == null || str4.length() == 0)) {
                bookSourceString = getBookSourceStringBySourceURLOpt(sourceUrl, userNameSpace);
            }
        }
        return bookSourceString;
    }

    @Nullable
    public final String getBookSourceStringBySourceURLOpt(@NotNull String sourceUrl, @NotNull String userNameSpace) throws Exception {
        Intrinsics.checkNotNullParameter(sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if (StringsKt.isBlank(sourceUrl)) {
            return null;
        }
        File file = ExtKt.getStorageFile$default(new String[]{"data", userNameSpace, "bookSource"}, null, 2, null);
        if (!file.exists()) {
            file = ExtKt.getStorageFile$default(new String[]{"data", "default", "bookSource"}, null, 2, null);
            if (!file.exists()) {
                return null;
            }
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonFactory factory = objectMapper.getFactory();
            final Ref.ObjectRef bookSourceString = new Ref.ObjectRef();
            JsonParser jsonParser = (Closeable) factory.createParser(file);
            Throwable th = (Throwable) null;
            try {
                try {
                    JsonParser parser = jsonParser;
                    if (parser.nextToken() == JsonToken.START_ARRAY) {
                        while (true) {
                            if (parser.nextToken() == JsonToken.END_ARRAY) {
                                break;
                            }
                            if (parser.currentToken() == JsonToken.START_OBJECT) {
                                JsonNode valueAsTree = parser.readValueAsTree();
                                Intrinsics.checkNotNullExpressionValue(valueAsTree, "parser.readValueAsTree()");
                                JsonNode jsonNode = valueAsTree;
                                if (sourceUrl.equals(jsonNode.get("bookSourceUrl").asText())) {
                                    bookSourceString.element = jsonNode.toString();
                                    break;
                                }
                            }
                        }
                    }
                    Unit unit = Unit.INSTANCE;
                    CloseableKt.closeFinally(jsonParser, th);
                    BookControllerKt.logger.info(new Function0<Object>() { // from class: com.htmake.reader.api.controller.BookController.getBookSourceStringBySourceURLOpt.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(0);
                        }

                        @Nullable
                        public final Object invoke() {
                            return bookSourceString.element;
                        }
                    });
                    return (String) bookSourceString.element;
                } finally {
                }
            } catch (Throwable th2) {
                CloseableKt.closeFinally(jsonParser, th);
                throw th2;
            }
        } catch (Exception e) {
            BookControllerKt.logger.error("解析文件内容出错: {}  文件: \n{}", e, file);
            throw e;
        }
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [com.htmake.reader.api.controller.BookController$getShelfBookByURL$$inlined$toDataClass$1] */
    @Nullable
    public final Book getShelfBookByURL(@NotNull String url, @NotNull String userNameSpace) {
        JsonArray bookshelf;
        String json;
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if ((url.length() == 0) || (bookshelf = ExtKt.asJsonArray(getUserStorage(userNameSpace, "bookshelf"))) == null) {
            return null;
        }
        int i = 0;
        int size = bookshelf.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                Object map = bookshelf.getJsonObject(i2).getMap();
                Intrinsics.checkNotNullExpressionValue(map, "bookshelf.getJsonObject(i).map");
                if (!(map instanceof String)) {
                    json = ExtKt.getGson().toJson(map);
                } else {
                    json = (String) map;
                }
                String json$iv$iv = json;
                Book _book = (Book) ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() { // from class: com.htmake.reader.api.controller.BookController$getShelfBookByURL$$inlined$toDataClass$1
                }.getType());
                if (_book.getBookUrl().equals(url)) {
                    _book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                    _book.setUserNameSpace(userNameSpace);
                    _book.setInShelf(true);
                    return _book;
                }
            } while (i < size);
            return null;
        }
        return null;
    }

    @Nullable
    public final Object saveShelfBookProgress(@NotNull Book book, @NotNull final BookChapter bookChapter, @NotNull String userNameSpace, @NotNull Continuation<? super Unit> $completion) {
        Object objEditShelfBook = editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.saveShelfBookProgress.2
            {
                super(1);
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
                Intrinsics.checkNotNullParameter(existBook, "existBook");
                existBook.setDurChapterIndex(bookChapter.getIndex());
                existBook.setDurChapterTitle(bookChapter.getTitle());
                existBook.setDurChapterTime(System.currentTimeMillis());
                return existBook;
            }
        }, $completion);
        return objEditShelfBook == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEditShelfBook : Unit.INSTANCE;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveShelfBookLatestChapter(@NotNull Book book, @NotNull List<BookChapter> bookChapterList, @NotNull String userNameSpace, @Nullable Mutex mutex, @NotNull Continuation<? super Unit> $completion) {
        C00611 c00611;
        Mutex mutex2;
        final List<BookChapter> list;
        final Book book2;
        if ($completion instanceof C00611) {
            c00611 = (C00611) $completion;
            if ((c00611.label & Integer.MIN_VALUE) != 0) {
                c00611.label -= Integer.MIN_VALUE;
            } else {
                c00611 = new C00611($completion);
            }
        }
        Object $result = c00611.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c00611.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    if (mutex != null) {
                        c00611.L$0 = this;
                        c00611.L$1 = book;
                        c00611.L$2 = bookChapterList;
                        c00611.L$3 = userNameSpace;
                        c00611.L$4 = mutex;
                        c00611.label = 1;
                        if (Mutex.DefaultImpls.lock$default(mutex, (Object) null, c00611, 1, (Object) null) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    list = bookChapterList;
                    book2 = book;
                    c00611.L$0 = mutex;
                    c00611.L$1 = null;
                    c00611.L$2 = null;
                    c00611.L$3 = null;
                    c00611.L$4 = null;
                    c00611.label = 2;
                    if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.saveShelfBookLatestChapter.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @NotNull
                        public final Book invoke(@NotNull Book existBook) {
                            Intrinsics.checkNotNullParameter(existBook, "existBook");
                            if (list.size() > 0) {
                                BookChapter bookChapter = (BookChapter) CollectionsKt.last(list);
                                existBook.setLatestChapterTitle(bookChapter.getTitle());
                            }
                            if (list.size() - existBook.getTotalChapterNum() > 0) {
                                existBook.setLastCheckCount(list.size() - existBook.getTotalChapterNum());
                                existBook.setLastCheckTime(System.currentTimeMillis());
                            }
                            existBook.setLastCheckError(null);
                            existBook.setTotalChapterNum(list.size());
                            book2.setLatestChapterTitle(existBook.getLatestChapterTitle());
                            book2.setLastCheckCount(existBook.getLastCheckCount());
                            book2.setLastCheckTime(existBook.getLastCheckTime());
                            book2.setLastCheckError(existBook.getLastCheckError());
                            book2.setTotalChapterNum(existBook.getTotalChapterNum());
                            return existBook;
                        }
                    }, c00611) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    mutex2 = mutex;
                    if (mutex2 != null) {
                        Mutex.DefaultImpls.unlock$default(mutex2, (Object) null, 1, (Object) null);
                    }
                    return Unit.INSTANCE;
                case 1:
                    mutex = (Mutex) c00611.L$4;
                    userNameSpace = (String) c00611.L$3;
                    bookChapterList = (List) c00611.L$2;
                    book = (Book) c00611.L$1;
                    this = (BookController) c00611.L$0;
                    ResultKt.throwOnFailure($result);
                    list = bookChapterList;
                    book2 = book;
                    c00611.L$0 = mutex;
                    c00611.L$1 = null;
                    c00611.L$2 = null;
                    c00611.L$3 = null;
                    c00611.L$4 = null;
                    c00611.label = 2;
                    if (this.editShelfBook(book, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.saveShelfBookLatestChapter.2
                        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                        {
                            super(1);
                        }

                        @NotNull
                        public final Book invoke(@NotNull Book existBook) {
                            Intrinsics.checkNotNullParameter(existBook, "existBook");
                            if (list.size() > 0) {
                                BookChapter bookChapter = (BookChapter) CollectionsKt.last(list);
                                existBook.setLatestChapterTitle(bookChapter.getTitle());
                            }
                            if (list.size() - existBook.getTotalChapterNum() > 0) {
                                existBook.setLastCheckCount(list.size() - existBook.getTotalChapterNum());
                                existBook.setLastCheckTime(System.currentTimeMillis());
                            }
                            existBook.setLastCheckError(null);
                            existBook.setTotalChapterNum(list.size());
                            book2.setLatestChapterTitle(existBook.getLatestChapterTitle());
                            book2.setLastCheckCount(existBook.getLastCheckCount());
                            book2.setLastCheckTime(existBook.getLastCheckTime());
                            book2.setLastCheckError(existBook.getLastCheckError());
                            book2.setTotalChapterNum(existBook.getTotalChapterNum());
                            return existBook;
                        }
                    }, c00611) == coroutine_suspended) {
                    }
                    mutex2 = mutex;
                    if (mutex2 != null) {
                    }
                    return Unit.INSTANCE;
                case 2:
                    mutex = (Mutex) c00611.L$0;
                    ResultKt.throwOnFailure($result);
                    mutex2 = mutex;
                    if (mutex2 != null) {
                    }
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            if (mutex != null) {
                Mutex.DefaultImpls.unlock$default(mutex, (Object) null, 1, (Object) null);
            }
            throw th;
        }
    }

    public static /* synthetic */ Object saveShelfBookLatestChapter$default(BookController bookController, Book book, List list, String str, Mutex mutex, Continuation continuation, int i, Object obj) {
        if ((i & 8) != 0) {
            mutex = null;
        }
        return bookController.saveShelfBookLatestChapter(book, list, str, mutex, continuation);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0176 A[Catch: all -> 0x02bd, TryCatch #0 {all -> 0x02bd, blocks: (B:16:0x00cb, B:23:0x014c, B:25:0x0176, B:26:0x017f, B:28:0x0193, B:33:0x01ca, B:36:0x01e0, B:41:0x01fe, B:43:0x020d, B:48:0x022b, B:55:0x024d, B:22:0x0144), top: B:67:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0193 A[Catch: all -> 0x02bd, LOOP:0: B:28:0x0193->B:72:?, LOOP_START, PHI: r16
      0x0193: PHI (r16v2 int) = (r16v0 int), (r16v3 int) binds: [B:27:0x0190, B:72:?] A[DONT_GENERATE, DONT_INLINE], TryCatch #0 {all -> 0x02bd, blocks: (B:16:0x00cb, B:23:0x014c, B:25:0x0176, B:26:0x017f, B:28:0x0193, B:33:0x01ca, B:36:0x01e0, B:41:0x01fe, B:43:0x020d, B:48:0x022b, B:55:0x024d, B:22:0x0144), top: B:67:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0241  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x024d A[Catch: all -> 0x02bd, TRY_LEAVE, TryCatch #0 {all -> 0x02bd, blocks: (B:16:0x00cb, B:23:0x014c, B:25:0x0176, B:26:0x017f, B:28:0x0193, B:33:0x01ca, B:36:0x01e0, B:41:0x01fe, B:43:0x020d, B:48:0x022b, B:55:0x024d, B:22:0x0144), top: B:67:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x02b2  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object editShelfBook(@NotNull Book book, @NotNull String userNameSpace, @NotNull Function1<? super Book, Book> handler, @NotNull Continuation<? super Book> $completion) {
        C00211 c00211;
        Mutex mutex;
        Object locker;
        JsonArray bookshelf;
        int existIndex;
        int size;
        if ($completion instanceof C00211) {
            c00211 = (C00211) $completion;
            if ((c00211.label & Integer.MIN_VALUE) != 0) {
                c00211.label -= Integer.MIN_VALUE;
            } else {
                c00211 = new C00211($completion);
            }
        }
        Object $result = c00211.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            switch (c00211.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    c00211.L$0 = this;
                    c00211.L$1 = book;
                    c00211.L$2 = userNameSpace;
                    c00211.L$3 = handler;
                    c00211.label = 1;
                    locker = UserMutex.INSTANCE.getLocker(Intrinsics.stringPlus(userNameSpace, "@bookshelf"), c00211);
                    if (locker == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    mutex = (Mutex) locker;
                    BookControllerKt.logger.info("wait for lock {}", Intrinsics.stringPlus(userNameSpace, "@bookshelf"));
                    c00211.L$0 = this;
                    c00211.L$1 = book;
                    c00211.L$2 = userNameSpace;
                    c00211.L$3 = handler;
                    c00211.L$4 = mutex;
                    c00211.label = 2;
                    if (Mutex.DefaultImpls.lock$default(mutex, (Object) null, c00211, 1, (Object) null) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    BookControllerKt.logger.info("lock success");
                    bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
                    if (bookshelf == null) {
                        bookshelf = new JsonArray();
                    }
                    existIndex = -1;
                    int i = 0;
                    size = bookshelf.size();
                    if (0 < size) {
                        while (true) {
                            int i2 = i;
                            i++;
                            Book _book = (Book) bookshelf.getJsonObject(i2).mapTo(Book.class);
                            if ((book.getBookUrl().length() > 0) && _book.getBookUrl().equals(book.getBookUrl())) {
                                existIndex = i2;
                            } else {
                                if (!(book.getName().length() > 0) || !_book.getName().equals(book.getName())) {
                                    if (i >= size) {
                                    }
                                } else {
                                    if ((book.getAuthor().length() > 0) && _book.getAuthor().equals(book.getAuthor())) {
                                        existIndex = i2;
                                    }
                                }
                            }
                        }
                    }
                    if (existIndex >= 0) {
                        List bookList = bookshelf.getList();
                        Book existBook = (Book) bookshelf.getJsonObject(existIndex).mapTo(Book.class);
                        Intrinsics.checkNotNullExpressionValue(existBook, "existBook");
                        Book existBook2 = (Book) handler.invoke(existBook);
                        bookList.set(existIndex, JsonObject.mapFrom(existBook2));
                        this.saveUserStorage(userNameSpace, "bookshelf", new JsonArray(bookList));
                        Mutex.DefaultImpls.unlock$default(mutex, (Object) null, 1, (Object) null);
                        return existBook2;
                    }
                    Mutex.DefaultImpls.unlock$default(mutex, (Object) null, 1, (Object) null);
                    return null;
                case 1:
                    handler = (Function1) c00211.L$3;
                    userNameSpace = (String) c00211.L$2;
                    book = (Book) c00211.L$1;
                    this = (BookController) c00211.L$0;
                    ResultKt.throwOnFailure($result);
                    locker = $result;
                    mutex = (Mutex) locker;
                    BookControllerKt.logger.info("wait for lock {}", Intrinsics.stringPlus(userNameSpace, "@bookshelf"));
                    c00211.L$0 = this;
                    c00211.L$1 = book;
                    c00211.L$2 = userNameSpace;
                    c00211.L$3 = handler;
                    c00211.L$4 = mutex;
                    c00211.label = 2;
                    if (Mutex.DefaultImpls.lock$default(mutex, (Object) null, c00211, 1, (Object) null) == coroutine_suspended) {
                    }
                    BookControllerKt.logger.info("lock success");
                    bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
                    if (bookshelf == null) {
                    }
                    existIndex = -1;
                    int i3 = 0;
                    size = bookshelf.size();
                    if (0 < size) {
                    }
                    if (existIndex >= 0) {
                    }
                    break;
                case 2:
                    mutex = (Mutex) c00211.L$4;
                    handler = (Function1) c00211.L$3;
                    userNameSpace = (String) c00211.L$2;
                    book = (Book) c00211.L$1;
                    this = (BookController) c00211.L$0;
                    ResultKt.throwOnFailure($result);
                    BookControllerKt.logger.info("lock success");
                    bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
                    if (bookshelf == null) {
                    }
                    existIndex = -1;
                    int i32 = 0;
                    size = bookshelf.size();
                    if (0 < size) {
                    }
                    if (existIndex >= 0) {
                    }
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th) {
            Mutex.DefaultImpls.unlock$default(mutex, (Object) null, 1, (Object) null);
            throw th;
        }
    }

    public static /* synthetic */ void saveBookSources$default(BookController bookController, Book book, List list, String str, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        bookController.saveBookSources(book, list, str, z);
    }

    public final void saveBookSources(@NotNull Book book, @NotNull List<SearchBook> sourceList, @NotNull String userNameSpace, boolean replace) {
        JsonArray localBookSourceList;
        Intrinsics.checkNotNullParameter(book, "book");
        Intrinsics.checkNotNullParameter(sourceList, "sourceList");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if (book.getName().length() == 0) {
            return;
        }
        JsonArray bookSourceList = new JsonArray();
        if (!replace && (localBookSourceList = ExtKt.asJsonArray(getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), "bookSource"))) != null) {
            bookSourceList = localBookSourceList;
        }
        Map urlMap = new LinkedHashMap();
        int i = 0;
        int size = bookSourceList.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                String bookUrl = bookSourceList.getJsonObject(i2).getString("bookUrl");
                Intrinsics.checkNotNullExpressionValue(bookUrl, "bookUrl");
                urlMap.put(bookUrl, Integer.valueOf(i2));
            } while (i < size);
        }
        int i3 = 0;
        int size2 = sourceList.size();
        if (0 < size2) {
            do {
                int k = i3;
                i3++;
                SearchBook searchBook = sourceList.get(k);
                int existIndex = ((Number) urlMap.getOrDefault(searchBook.getBookUrl(), -1)).intValue();
                if (existIndex >= 0) {
                    bookSourceList.set(existIndex, JsonObject.mapFrom(searchBook));
                } else {
                    bookSourceList.add(JsonObject.mapFrom(searchBook));
                    urlMap.put(searchBook.getBookUrl(), Integer.valueOf(bookSourceList.size() - 1));
                }
            } while (i3 < size2);
        }
        saveUserStorage(userNameSpace, ExtKt.getRelativePath(book.getName() + '_' + book.getAuthor(), "bookSource"), bookSourceList);
    }

    public static /* synthetic */ boolean extractEpub$default(BookController bookController, Book book, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return bookController.extractEpub(book, z);
    }

    public final boolean extractEpub(@NotNull Book book, boolean force) {
        Intrinsics.checkNotNullParameter(book, "book");
        File epubExtractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + ((Object) File.separator) + "index"));
        if (force || !epubExtractDir.exists()) {
            ExtKt.deleteRecursively(epubExtractDir);
            File localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName() + ((Object) File.separator) + "index.epub"));
            if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, (Object) null) > 0) {
                localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, (Object) null) > 0) {
                localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            BookControllerKt.logger.info("extractEpub from {} to {}", localEpubFile, epubExtractDir);
            String string = epubExtractDir.toString();
            Intrinsics.checkNotNullExpressionValue(string, "epubExtractDir.toString()");
            if (!ExtKt.unzip(localEpubFile, string)) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static /* synthetic */ boolean extractCbz$default(BookController bookController, Book book, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return bookController.extractCbz(book, z);
    }

    public final boolean extractCbz(@NotNull Book book, boolean force) {
        Intrinsics.checkNotNullParameter(book, "book");
        File extractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + ((Object) File.separator) + "index"));
        if (force || !extractDir.exists()) {
            ExtKt.deleteRecursively(extractDir);
            File localFile = new File(ExtKt.getWorkDir(book.getOriginName() + ((Object) File.separator) + "index.cbz"));
            if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, (Object) null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, (Object) null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            String string = extractDir.toString();
            Intrinsics.checkNotNullExpressionValue(string, "extractDir.toString()");
            if (!ExtKt.unzip(localFile, string)) {
                return false;
            }
            return true;
        }
        return true;
    }

    public static /* synthetic */ boolean convertPdfToImage$default(BookController bookController, Book book, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return bookController.convertPdfToImage(book, z);
    }

    public final boolean convertPdfToImage(@NotNull Book book, boolean force) {
        Intrinsics.checkNotNullParameter(book, "book");
        return true;
    }

    public static /* synthetic */ void convertPdfPageToImage$default(BookController bookController, Book book, int i, boolean z, int i2, Object obj) {
        if ((i2 & 4) != 0) {
            z = false;
        }
        bookController.convertPdfPageToImage(book, i, z);
    }

    public final void convertPdfPageToImage(@NotNull Book book, int index, boolean force) {
        Intrinsics.checkNotNullParameter(book, "book");
        File extractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + ((Object) File.separator) + "index"));
        if (!extractDir.exists()) {
            extractDir.mkdirs();
        }
        File output = new File(extractDir.toString() + ((Object) File.separator) + "output-" + index + ".png");
        if (force || !output.exists()) {
            ExtKt.deleteRecursively(output);
            File localFile = new File(ExtKt.getWorkDir(book.getOriginName() + ((Object) File.separator) + "index.pdf"));
            if (StringsKt.indexOf$default(book.getOriginName(), "localStore", 0, false, 6, (Object) null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default(book.getOriginName(), "webdav", 0, false, 6, (Object) null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            PDDocument document = PDDocument.load(localFile);
            PDFRenderer renderer = new PDFRenderer(document);
            float targetWidth = book.getPdfImageWidth();
            Intrinsics.checkNotNullExpressionValue(document, "document");
            savePdfPageToImage(document, renderer, index, targetWidth, "png", output);
        }
    }

    public final void savePdfPageToImage(@NotNull PDDocument document, @NotNull PDFRenderer renderer, int index, float targetWidth, @NotNull String imageFormat, @NotNull File output) {
        Intrinsics.checkNotNullParameter(document, "document");
        Intrinsics.checkNotNullParameter(renderer, "renderer");
        Intrinsics.checkNotNullParameter(imageFormat, "imageFormat");
        Intrinsics.checkNotNullParameter(output, "output");
        PDPage page = document.getPage(index);
        PDRectangle pageSize = page.getCropBox();
        float scaleFactor = targetWidth / pageSize.getWidth();
        float scaledHeight = pageSize.getHeight() * scaleFactor;
        int targetHeightDimension = (0.0f > 0.0f ? 1 : (0.0f == 0.0f ? 0 : -1)) == 0 ? (int) scaledHeight : (int) 0.0f;
        Dimension targetDimension = new Dimension((int) targetWidth, targetHeightDimension);
        BufferedImage image = renderer.renderImageWithDPI(index, 300.0f, ImageType.RGB);
        Image scaledImage = image.getScaledInstance(targetDimension.width, targetDimension.height, 4);
        RenderedImage bufferedImage = new BufferedImage(targetDimension.width, targetDimension.height, 1);
        Graphics2D graphics = bufferedImage.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, (ImageObserver) null);
        graphics.dispose();
        ImageIO.write(bufferedImage, imageFormat, output);
    }

    @Nullable
    public final Object syncBookProgressFromWebdav(@NotNull Object progressFilePath, @NotNull String userNameSpace, @NotNull Continuation<? super Unit> $completion) {
        File progressFile = null;
        if (progressFilePath instanceof File) {
            progressFile = (File) progressFilePath;
        } else if (progressFilePath instanceof String) {
            progressFile = new File((String) progressFilePath);
        }
        if (progressFile == null) {
            return Unit.INSTANCE;
        }
        final Ref.ObjectRef book = new Ref.ObjectRef();
        JsonObject jsonObjectAsJsonObject = ExtKt.asJsonObject(FilesKt.readText$default(progressFile, (Charset) null, 1, (Object) null));
        book.element = jsonObjectAsJsonObject == null ? null : (Book) jsonObjectAsJsonObject.mapTo(Book.class);
        if (book.element == null) {
            return Unit.INSTANCE;
        }
        Object objEditShelfBook = editShelfBook((Book) book.element, userNameSpace, new Function1<Book, Book>() { // from class: com.htmake.reader.api.controller.BookController.syncBookProgressFromWebdav.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @NotNull
            public final Book invoke(@NotNull Book existBook) {
                Intrinsics.checkNotNullParameter(existBook, "existBook");
                existBook.setDurChapterIndex(((Book) book.element).getDurChapterIndex());
                existBook.setDurChapterPos(((Book) book.element).getDurChapterPos());
                existBook.setDurChapterTime(((Book) book.element).getDurChapterTime());
                existBook.setDurChapterTitle(((Book) book.element).getDurChapterTitle());
                BookControllerKt.logger.info("syncShelfBookProgress: {}", existBook);
                return existBook;
            }
        }, $completion);
        return objEditShelfBook == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objEditShelfBook : Unit.INSTANCE;
    }

    @Nullable
    public final Object saveBookProgressToWebdav(@NotNull Book book, @NotNull BookChapter bookChapter, @NotNull String userNameSpace, @NotNull Continuation<? super Unit> $completion) {
        String userHome = getUserWebdavHome(userNameSpace);
        File bookProgressDir = new File(userHome + ((Object) File.separator) + "bookProgress");
        if (!bookProgressDir.exists()) {
            bookProgressDir = new File(userHome + ((Object) File.separator) + "legado" + ((Object) File.separator) + "bookProgress");
            if (!bookProgressDir.exists()) {
                return Unit.INSTANCE;
            }
        }
        File progressFile = new File(bookProgressDir.toString() + ((Object) File.separator) + book.getName() + '_' + book.getAuthor() + ".json");
        FilesKt.writeText$default(progressFile, ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("name", book.getName()), TuplesKt.to("author", book.getAuthor()), TuplesKt.to("durChapterIndex", Boxing.boxInt(bookChapter.getIndex())), TuplesKt.to("durChapterPos", Boxing.boxInt(0)), TuplesKt.to("durChapterTime", Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to("durChapterTitle", bookChapter.getTitle())}), true), (Charset) null, 2, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX WARN: Not initialized variable reg: 12, insn: 0x033c: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE] (LINE:2519), block:B:49:0x0337 */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0346: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE] (LINE:2519), block:B:52:0x0346 */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0284 A[Catch: Exception -> 0x0335, all -> 0x0344, TRY_LEAVE, TryCatch #1 {Exception -> 0x0335, blocks: (B:11:0x0091, B:13:0x00ab, B:16:0x00b9, B:17:0x00ef, B:19:0x00f9, B:21:0x0137, B:23:0x0179, B:25:0x01a6, B:26:0x01ec, B:28:0x0218, B:29:0x0248, B:31:0x0250, B:33:0x0258, B:36:0x0284, B:43:0x031f, B:45:0x0327, B:42:0x0317), top: B:59:0x0043, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object syncFromWebdav(@NotNull String zipFilePath, @NotNull String userNameSpace, @NotNull Continuation<? super Boolean> $completion) {
        C00871 c00871;
        File file;
        File file2;
        int length;
        Object[] objArr;
        File descDirFile;
        int i;
        if ($completion instanceof C00871) {
            c00871 = (C00871) $completion;
            if ((c00871.label & Integer.MIN_VALUE) != 0) {
                c00871.label -= Integer.MIN_VALUE;
            } else {
                c00871 = new C00871($completion);
            }
        }
        Object $result = c00871.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
            try {
                switch (c00871.label) {
                    case 0:
                        ResultKt.throwOnFailure($result);
                        String workDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "tmp");
                        descDirFile = new File(workDir);
                        String userHome = getUserWebdavHome(userNameSpace);
                        File zipFile = new File(zipFilePath);
                        if (!zipFile.exists()) {
                            Boolean boolBoxBoolean = Boxing.boxBoolean(false);
                            ExtKt.deleteRecursively(descDirFile);
                            return boolBoxBoolean;
                        }
                        ExtKt.deleteRecursively(descDirFile);
                        ZipUtils.INSTANCE.unzipFile(zipFile, descDirFile);
                        String[] backupFileNames = getBackupFileNames();
                        Iterable syncDataFileList = CollectionsKt.arrayListOf(Arrays.copyOf(backupFileNames, backupFileNames.length));
                        Iterable $this$forEach$iv = syncDataFileList;
                        for (Object element$iv : $this$forEach$iv) {
                            String it = (String) element$iv;
                            File backupFile = new File(workDir + ((Object) File.separator) + it);
                            if (backupFile.exists()) {
                                File userDataFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, it));
                                ExtKt.deleteRecursively(userDataFile);
                                FilesKt.copyRecursively$default(backupFile, userDataFile, false, (Function2) null, 6, (Object) null);
                            }
                        }
                        File backupBooksDir = new File(workDir + ((Object) File.separator) + "books");
                        if (backupBooksDir.exists()) {
                            File webdavBooksDir = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"));
                            ExtKt.deleteRecursively(webdavBooksDir);
                            FilesKt.copyRecursively$default(backupBooksDir, webdavBooksDir, false, (Function2) null, 6, (Object) null);
                        }
                        File bookProgressDir = new File(userHome + ((Object) File.separator) + "bookProgress");
                        if (!bookProgressDir.exists()) {
                            bookProgressDir = new File(userHome + ((Object) File.separator) + "legado" + ((Object) File.separator) + "bookProgress");
                        }
                        if (bookProgressDir.exists() && bookProgressDir.isDirectory()) {
                            Object[] objArrListFiles = bookProgressDir.listFiles();
                            Intrinsics.checkNotNullExpressionValue(objArrListFiles, "bookProgressDir.listFiles()");
                            Object[] $this$forEach$iv2 = objArrListFiles;
                            objArr = $this$forEach$iv2;
                            length = objArr.length;
                            i = 0;
                            while (i < length) {
                                Object element$iv2 = objArr[i];
                                File it2 = (File) element$iv2;
                                Intrinsics.checkNotNullExpressionValue(it2, "it");
                                c00871.L$0 = this;
                                c00871.L$1 = userNameSpace;
                                c00871.L$2 = descDirFile;
                                c00871.L$3 = objArr;
                                c00871.I$0 = length;
                                c00871.I$1 = i;
                                c00871.label = 1;
                                if (this.syncBookProgressFromWebdav(it2, userNameSpace, c00871) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                                i++;
                            }
                        }
                        Boolean boolBoxBoolean2 = Boxing.boxBoolean(true);
                        ExtKt.deleteRecursively(descDirFile);
                        return boolBoxBoolean2;
                    case 1:
                        int i2 = c00871.I$1;
                        length = c00871.I$0;
                        objArr = (Object[]) c00871.L$3;
                        descDirFile = (File) c00871.L$2;
                        userNameSpace = (String) c00871.L$1;
                        this = (BookController) c00871.L$0;
                        ResultKt.throwOnFailure($result);
                        i = i2 + 1;
                        while (i < length) {
                        }
                        Boolean boolBoxBoolean22 = Boxing.boxBoolean(true);
                        ExtKt.deleteRecursively(descDirFile);
                        return boolBoxBoolean22;
                    default:
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ExtKt.deleteRecursively(file);
                return Boxing.boxBoolean(false);
            }
        } catch (Throwable th) {
            ExtKt.deleteRecursively(file2);
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00db A[ADDED_TO_REGION, REMOVE] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x015a  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0167  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x016b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object saveToWebdav(@NotNull String userNameSpace, @Nullable String latestZipFilePath, @NotNull Continuation<? super Boolean> $completion) {
        C00641 c00641;
        Object objCreateUserBackup;
        String legadoHome;
        String userHome;
        Object lastBackFileFromWebdav;
        String str;
        String _latestZipFilePath;
        if ($completion instanceof C00641) {
            c00641 = (C00641) $completion;
            if ((c00641.label & Integer.MIN_VALUE) != 0) {
                c00641.label -= Integer.MIN_VALUE;
            } else {
                c00641 = new C00641($completion);
            }
        }
        Object $result = c00641.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00641.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                userHome = getUserWebdavHome(userNameSpace);
                legadoHome = userHome;
                if (latestZipFilePath == null) {
                    c00641.L$0 = this;
                    c00641.L$1 = userNameSpace;
                    c00641.L$2 = userHome;
                    c00641.L$3 = legadoHome;
                    c00641.label = 1;
                    lastBackFileFromWebdav = getLastBackFileFromWebdav(userNameSpace, c00641);
                    if (lastBackFileFromWebdav == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    str = (String) lastBackFileFromWebdav;
                    _latestZipFilePath = str;
                    if (_latestZipFilePath != null || StringsKt.indexOf$default(_latestZipFilePath, "legado", 0, false, 6, (Object) null) > 0) {
                        legadoHome = userHome + ((Object) File.separator) + "legado";
                    }
                    c00641.L$0 = null;
                    c00641.L$1 = null;
                    c00641.L$2 = null;
                    c00641.L$3 = null;
                    c00641.label = 2;
                    objCreateUserBackup = this.createUserBackup(userNameSpace, legadoHome, _latestZipFilePath, c00641);
                    if (objCreateUserBackup == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    return Boxing.boxBoolean(objCreateUserBackup == null);
                }
                str = latestZipFilePath;
                _latestZipFilePath = str;
                if (_latestZipFilePath != null) {
                    legadoHome = userHome + ((Object) File.separator) + "legado";
                } else {
                    legadoHome = userHome + ((Object) File.separator) + "legado";
                }
                c00641.L$0 = null;
                c00641.L$1 = null;
                c00641.L$2 = null;
                c00641.L$3 = null;
                c00641.label = 2;
                objCreateUserBackup = this.createUserBackup(userNameSpace, legadoHome, _latestZipFilePath, c00641);
                if (objCreateUserBackup == coroutine_suspended) {
                }
                return Boxing.boxBoolean(objCreateUserBackup == null);
            case 1:
                legadoHome = (String) c00641.L$3;
                userHome = (String) c00641.L$2;
                userNameSpace = (String) c00641.L$1;
                this = (BookController) c00641.L$0;
                ResultKt.throwOnFailure($result);
                lastBackFileFromWebdav = $result;
                str = (String) lastBackFileFromWebdav;
                _latestZipFilePath = str;
                if (_latestZipFilePath != null) {
                }
                c00641.L$0 = null;
                c00641.L$1 = null;
                c00641.L$2 = null;
                c00641.L$3 = null;
                c00641.label = 2;
                objCreateUserBackup = this.createUserBackup(userNameSpace, legadoHome, _latestZipFilePath, c00641);
                if (objCreateUserBackup == coroutine_suspended) {
                }
                return Boxing.boxBoolean(objCreateUserBackup == null);
            case 2:
                ResultKt.throwOnFailure($result);
                objCreateUserBackup = $result;
                return Boxing.boxBoolean(objCreateUserBackup == null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    public static /* synthetic */ Object saveToWebdav$default(BookController bookController, String str, String str2, Continuation continuation, int i, Object obj) {
        if ((i & 2) != 0) {
            str2 = null;
        }
        return bookController.saveToWebdav(str, str2, continuation);
    }

    public static /* synthetic */ Object createUserBackup$default(BookController bookController, String str, String str2, String str3, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            str3 = null;
        }
        return bookController.createUserBackup(str, str2, str3, continuation);
    }

    @Nullable
    public final Object createUserBackup(@NotNull String userNameSpace, @NotNull String backupDir, @Nullable String latestZipFilePath, @NotNull Continuation<? super File> $completion) {
        String today = new SimpleDateFormat(PackageDocumentBase.dateFormat).format(Boxing.boxLong(System.currentTimeMillis()));
        String workDir = ExtKt.getWorkDir("storage", "data", userNameSpace, Intrinsics.stringPlus("backup", today));
        File descDirFile = new File(workDir);
        ExtKt.deleteRecursively(descDirFile);
        try {
            if (latestZipFilePath != null) {
                try {
                    if (!ExtKt.unzip(new File(latestZipFilePath), workDir)) {
                        ExtKt.deleteRecursively(descDirFile);
                        return null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ExtKt.deleteRecursively(descDirFile);
                    return null;
                }
            }
            String[] backupFileNames = getBackupFileNames();
            Iterable syncDataFileList = CollectionsKt.arrayListOf(Arrays.copyOf(backupFileNames, backupFileNames.length));
            Iterable $this$forEach$iv = syncDataFileList;
            for (Object element$iv : $this$forEach$iv) {
                String it = (String) element$iv;
                File userDataFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, it));
                if (userDataFile.exists()) {
                    File backupFile = new File(workDir + ((Object) File.separator) + it);
                    ExtKt.deleteRecursively(backupFile);
                    FilesKt.copyRecursively$default(userDataFile, backupFile, false, (Function2) null, 6, (Object) null);
                }
            }
            File webdavBooksDir = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"));
            if (webdavBooksDir.exists()) {
                File backupBooksDir = new File(workDir + ((Object) File.separator) + "books");
                ExtKt.deleteRecursively(backupBooksDir);
                FilesKt.copyRecursively$default(webdavBooksDir, backupBooksDir, false, (Function2) null, 6, (Object) null);
            }
            File backupFile2 = FileUtils.INSTANCE.createFileWithReplace(backupDir + ((Object) File.separator) + "backup" + ((Object) today) + ".zip");
            ZipUtils zipUtils = ZipUtils.INSTANCE;
            File[] fileArrListFiles = descDirFile.listFiles();
            Intrinsics.checkNotNullExpressionValue(fileArrListFiles, "descDirFile.listFiles()");
            File file = zipUtils.zipFiles(CollectionsKt.arrayListOf(Arrays.copyOf(fileArrListFiles, fileArrListFiles.length)), backupFile2, (String) null) ? backupFile2 : (File) null;
            ExtKt.deleteRecursively(descDirFile);
            return file;
        } catch (Throwable th) {
            ExtKt.deleteRecursively(descDirFile);
            throw th;
        }
    }

    @Nullable
    public final Object getLastBackFileFromWebdav(@NotNull String userNameSpace, @NotNull Continuation<? super String> $completion) {
        String userHome = getUserWebdavHome(userNameSpace);
        File legadoHome = new File(userHome + ((Object) File.separator) + "legado");
        if (!legadoHome.exists()) {
            legadoHome = new File(userHome);
        }
        if (!legadoHome.exists()) {
            return null;
        }
        Object latestZipFile = null;
        Regex zipFileReg = new Regex("^backup[0-9-]+.zip$", RegexOption.IGNORE_CASE);
        Object[] it = legadoHome.listFiles();
        Intrinsics.checkNotNullExpressionValue(it, "it");
        if (it.length > 1) {
            ArraysKt.sortWith(it, new Comparator<T>() { // from class: com.htmake.reader.api.controller.BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    File it2 = (File) t2;
                    File it3 = (File) t;
                    return ComparisonsKt.compareValues(Long.valueOf(it2.lastModified()), Long.valueOf(it3.lastModified()));
                }
            });
        }
        Intrinsics.checkNotNullExpressionValue(it, "legadoHome.listFiles().also{\n            it.sortByDescending {\n                it.lastModified()\n            }\n        }");
        Object[] $this$forEach$iv = it;
        for (Object element$iv : $this$forEach$iv) {
            File it2 = (File) element$iv;
            String name = it2.getName();
            Intrinsics.checkNotNullExpressionValue(name, "it.name");
            if (zipFileReg.matches(name)) {
                latestZipFile = it2.toString();
            }
        }
        return latestZipFile;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00f1  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0132  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object bookSourceDebugSSE(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws Exception {
        C00131 c00131;
        HttpServerResponse response;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00131) {
            c00131 = (C00131) $completion;
            if ((c00131.label & Integer.MIN_VALUE) != 0) {
                c00131.label -= Integer.MIN_VALUE;
            } else {
                c00131 = new C00131($completion);
            }
        }
        Object $result = c00131.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00131.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                response = context.response().putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                c00131.L$0 = this;
                c00131.L$1 = context;
                c00131.L$2 = returnData;
                c00131.L$3 = response;
                c00131.label = 1;
                objCheckAuth = checkAuth(context, c00131);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                List listQueryParam = context.queryParam("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"bookSourceUrl\")");
                String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                String bookSourceUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                List listQueryParam2 = context.queryParam("keyword");
                Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"keyword\")");
                String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                String keyword = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                if (bookSourceUrl.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (keyword.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请输入搜索关键词"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                String userNameSpace = this.getUserNameSpace(context);
                String bookSourceString = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
                String str3 = bookSourceString;
                if (str3 == null || str3.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                BookController bookController = this;
                context.request().connection().closeHandler((v1) -> {
                    m22bookSourceDebugSSE$lambda18(r1, v1);
                });
                BookControllerKt.logger.info("bookSourceDebugSSE bookSource: {} keyword: {}", bookSourceString, keyword);
                final HttpServerResponse httpServerResponse = response;
                Debugger debugger = new Debugger(new Function1<String, Unit>() { // from class: com.htmake.reader.api.controller.BookController$bookSourceDebugSSE$debugger$1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                        invoke((String) p1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull String msg) {
                        Intrinsics.checkNotNullParameter(msg, "msg");
                        httpServerResponse.write("data: " + ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("msg", msg)), false) + "\n\n");
                    }
                });
                WebBook webBook = new WebBook(bookSourceString, false, (DebugLog) null, userNameSpace, 6, (DefaultConstructorMarker) null);
                c00131.L$0 = response;
                c00131.L$1 = null;
                c00131.L$2 = null;
                c00131.L$3 = null;
                c00131.label = 2;
                if (debugger.startDebug(webBook, keyword, c00131) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("end", Boxing.boxBoolean(true))), false) + "\n\n");
                return Unit.INSTANCE;
            case 1:
                response = (HttpServerResponse) c00131.L$3;
                returnData = (ReturnData) c00131.L$2;
                context = (RoutingContext) c00131.L$1;
                this = (BookController) c00131.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                response = (HttpServerResponse) c00131.L$0;
                ResultKt.throwOnFailure($result);
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to("end", Boxing.boxBoolean(true))), false) + "\n\n");
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: bookSourceDebugSSE$lambda-18, reason: not valid java name */
    private static final void m22bookSourceDebugSSE$lambda18(BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 bookSourceDebugSSE");
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x013a  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0486  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x048e  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x04c8  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x05d2  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x062e  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0633  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x06d4  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object cacheBookSSE(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C00161 c00161;
        final Ref.IntRef failedCount;
        final Ref.IntRef successCount;
        final Ref.ObjectRef cachedChapterContentSet;
        HttpServerResponse response;
        int concurrentCount;
        int refresh;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef chapterList;
        Ref.ObjectRef bookSource;
        Book bookInfo;
        Ref.ObjectRef userNameSpace;
        Object localChapterList$default;
        Ref.ObjectRef objectRef2;
        ReturnData returnData;
        Object bookSourceString$default;
        Object objCheckAuth;
        final Ref.BooleanRef isEnd;
        int i;
        int size;
        C00173 c00173;
        final HttpServerResponse httpServerResponse;
        CharSequence charSequence;
        String bookUrl;
        Integer numBoxInt;
        Integer numBoxInt2;
        if ($completion instanceof C00161) {
            c00161 = (C00161) $completion;
            if ((c00161.label & Integer.MIN_VALUE) != 0) {
                c00161.label -= Integer.MIN_VALUE;
            } else {
                c00161 = new C00161($completion);
            }
        }
        Object $result = c00161.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00161.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                response = context.response().putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                c00161.L$0 = this;
                c00161.L$1 = context;
                c00161.L$2 = returnData;
                c00161.L$3 = response;
                c00161.label = 1;
                objCheckAuth = checkAuth(context, c00161);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                    Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                    refresh = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"concurrentCount\", 24)");
                    concurrentCount = integer2.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("refresh");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"refresh\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? 0 : numBoxInt.intValue();
                    refresh = iIntValue;
                    List listQueryParam3 = context.queryParam("concurrentCount");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"concurrentCount\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue2 = (str3 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str3))) == null) ? 24 : numBoxInt2.intValue();
                    concurrentCount = iIntValue2;
                }
                if (bookUrl.length() == 0) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请输入书籍链接"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                userNameSpace = new Ref.ObjectRef();
                userNameSpace.element = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, (String) userNameSpace.element);
                if (bookInfo == null) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("请先加入书架"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                if (bookInfo.isLocalBook()) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("本地书籍无需缓存"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                bookSource = new Ref.ObjectRef();
                objectRef2 = bookSource;
                c00161.L$0 = this;
                c00161.L$1 = context;
                c00161.L$2 = returnData;
                c00161.L$3 = response;
                c00161.L$4 = userNameSpace;
                c00161.L$5 = bookInfo;
                c00161.L$6 = bookSource;
                c00161.L$7 = objectRef2;
                c00161.I$0 = refresh;
                c00161.I$1 = concurrentCount;
                c00161.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, bookInfo.getOrigin(), false, c00161, 4, null);
                if (bookSourceString$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objectRef2.element = bookSourceString$default;
                charSequence = (CharSequence) bookSource.element;
                if (!(charSequence != null || charSequence.length() == 0)) {
                    response.write("event: error\n");
                    response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("未配置书源"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                chapterList = new Ref.ObjectRef();
                objectRef = chapterList;
                c00161.L$0 = this;
                c00161.L$1 = context;
                c00161.L$2 = response;
                c00161.L$3 = userNameSpace;
                c00161.L$4 = bookInfo;
                c00161.L$5 = bookSource;
                c00161.L$6 = chapterList;
                c00161.L$7 = objectRef;
                c00161.I$0 = refresh;
                c00161.I$1 = concurrentCount;
                c00161.label = 3;
                localChapterList$default = getLocalChapterList$default(this, bookInfo, (String) bookSource.element, false, (String) userNameSpace.element, false, null, c00161, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objectRef.element = localChapterList$default;
                cachedChapterContentSet = new Ref.ObjectRef();
                cachedChapterContentSet.element = new LinkedHashSet();
                if (refresh <= 0) {
                    cachedChapterContentSet.element = this.getCachedChapterContentSet(bookInfo, (String) userNameSpace.element);
                }
                File localCacheDir = this.getChapterCacheDir(bookInfo, (String) userNameSpace.element);
                isEnd = new Ref.BooleanRef();
                successCount = new Ref.IntRef();
                failedCount = new Ref.IntRef();
                BookController bookController = this;
                context.request().connection().closeHandler((v2) -> {
                    m23cacheBookSSE$lambda19(r1, r2, v2);
                });
                i = concurrentCount <= 0 ? concurrentCount : 24;
                BookControllerKt.logger.info("cacheBookSSE concurrentCount: {} refresh: {}", Boxing.boxInt(i), Boxing.boxInt(refresh));
                size = ((List) chapterList.element).size();
                c00173 = new C00173(cachedChapterContentSet, chapterList, bookSource, this, userNameSpace, bookInfo, localCacheDir, successCount, isEnd, failedCount, null);
                httpServerResponse = response;
                c00161.L$0 = response;
                c00161.L$1 = cachedChapterContentSet;
                c00161.L$2 = successCount;
                c00161.L$3 = failedCount;
                c00161.L$4 = null;
                c00161.L$5 = null;
                c00161.L$6 = null;
                c00161.L$7 = null;
                c00161.label = 4;
                if (this.limitConcurrent(i, 0, size, c00173, new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.cacheBookSSE.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                    }

                    public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                        Intrinsics.checkNotNullParameter(list, "list");
                        if (isEnd.element) {
                            return false;
                        }
                        Map result = MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Integer.valueOf(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Integer.valueOf(successCount.element)), TuplesKt.to("failedCount", Integer.valueOf(failedCount.element))});
                        httpServerResponse.write("data: " + ExtKt.jsonEncode(result, false) + "\n\n");
                        BookControllerKt.logger.info("Loop: {} list.size: {} result: {}", new Object[]{Integer.valueOf(loopCount), Integer.valueOf(list.size()), result});
                        return true;
                    }
                }, c00161) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Boxing.boxInt(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Boxing.boxInt(successCount.element)), TuplesKt.to("failedCount", Boxing.boxInt(failedCount.element))}), false) + "\n\n");
                return Unit.INSTANCE;
            case 1:
                response = (HttpServerResponse) c00161.L$3;
                returnData = (ReturnData) c00161.L$2;
                context = (RoutingContext) c00161.L$1;
                this = (BookController) c00161.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                concurrentCount = c00161.I$1;
                refresh = c00161.I$0;
                objectRef2 = (Ref.ObjectRef) c00161.L$7;
                bookSource = (Ref.ObjectRef) c00161.L$6;
                bookInfo = (Book) c00161.L$5;
                userNameSpace = (Ref.ObjectRef) c00161.L$4;
                response = (HttpServerResponse) c00161.L$3;
                returnData = (ReturnData) c00161.L$2;
                context = (RoutingContext) c00161.L$1;
                this = (BookController) c00161.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                objectRef2.element = bookSourceString$default;
                charSequence = (CharSequence) bookSource.element;
                if (charSequence != null) {
                    if (!(charSequence != null || charSequence.length() == 0)) {
                    }
                }
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Boxing.boxInt(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Boxing.boxInt(successCount.element)), TuplesKt.to("failedCount", Boxing.boxInt(failedCount.element))}), false) + "\n\n");
                return Unit.INSTANCE;
            case 3:
                concurrentCount = c00161.I$1;
                refresh = c00161.I$0;
                objectRef = (Ref.ObjectRef) c00161.L$7;
                chapterList = (Ref.ObjectRef) c00161.L$6;
                bookSource = (Ref.ObjectRef) c00161.L$5;
                bookInfo = (Book) c00161.L$4;
                userNameSpace = (Ref.ObjectRef) c00161.L$3;
                response = (HttpServerResponse) c00161.L$2;
                context = (RoutingContext) c00161.L$1;
                this = (BookController) c00161.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                objectRef.element = localChapterList$default;
                cachedChapterContentSet = new Ref.ObjectRef();
                cachedChapterContentSet.element = new LinkedHashSet();
                if (refresh <= 0) {
                }
                File localCacheDir2 = this.getChapterCacheDir(bookInfo, (String) userNameSpace.element);
                isEnd = new Ref.BooleanRef();
                successCount = new Ref.IntRef();
                failedCount = new Ref.IntRef();
                BookController bookController2 = this;
                context.request().connection().closeHandler((v2) -> {
                    m23cacheBookSSE$lambda19(r1, r2, v2);
                });
                i = concurrentCount <= 0 ? concurrentCount : 24;
                BookControllerKt.logger.info("cacheBookSSE concurrentCount: {} refresh: {}", Boxing.boxInt(i), Boxing.boxInt(refresh));
                size = ((List) chapterList.element).size();
                c00173 = new C00173(cachedChapterContentSet, chapterList, bookSource, this, userNameSpace, bookInfo, localCacheDir2, successCount, isEnd, failedCount, null);
                httpServerResponse = response;
                c00161.L$0 = response;
                c00161.L$1 = cachedChapterContentSet;
                c00161.L$2 = successCount;
                c00161.L$3 = failedCount;
                c00161.L$4 = null;
                c00161.L$5 = null;
                c00161.L$6 = null;
                c00161.L$7 = null;
                c00161.label = 4;
                if (this.limitConcurrent(i, 0, size, c00173, new Function2<ArrayList<Object>, Integer, Boolean>() { // from class: com.htmake.reader.api.controller.BookController.cacheBookSSE.4
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        return Boolean.valueOf(invoke((ArrayList<Object>) p1, ((Number) p2).intValue()));
                    }

                    public final boolean invoke(@NotNull ArrayList<Object> list, int loopCount) {
                        Intrinsics.checkNotNullParameter(list, "list");
                        if (isEnd.element) {
                            return false;
                        }
                        Map result = MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Integer.valueOf(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Integer.valueOf(successCount.element)), TuplesKt.to("failedCount", Integer.valueOf(failedCount.element))});
                        httpServerResponse.write("data: " + ExtKt.jsonEncode(result, false) + "\n\n");
                        BookControllerKt.logger.info("Loop: {} list.size: {} result: {}", new Object[]{Integer.valueOf(loopCount), Integer.valueOf(list.size()), result});
                        return true;
                    }
                }, c00161) == coroutine_suspended) {
                }
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Boxing.boxInt(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Boxing.boxInt(successCount.element)), TuplesKt.to("failedCount", Boxing.boxInt(failedCount.element))}), false) + "\n\n");
                return Unit.INSTANCE;
            case 4:
                failedCount = (Ref.IntRef) c00161.L$3;
                successCount = (Ref.IntRef) c00161.L$2;
                cachedChapterContentSet = (Ref.ObjectRef) c00161.L$1;
                response = (HttpServerResponse) c00161.L$0;
                ResultKt.throwOnFailure($result);
                response.write("event: end\n");
                response.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[]{TuplesKt.to("cachedCount", Boxing.boxInt(((Set) cachedChapterContentSet.element).size())), TuplesKt.to("successCount", Boxing.boxInt(successCount.element)), TuplesKt.to("failedCount", Boxing.boxInt(failedCount.element))}), false) + "\n\n");
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: cacheBookSSE$lambda-19, reason: not valid java name */
    private static final void m23cacheBookSSE$lambda19(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 cacheBookSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$cacheBookSSE$3, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookSSE$3.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;", "it", PackageDocumentBase.PREFIX_OPF})
    @DebugMetadata(f = "BookController.kt", l = {2741, 2745}, i = {0, 0, 0, 0}, s = {"L$0", "L$1", "I$0", "I$1"}, n = {"$this$limitConcurrent", "chapterInfo", "it", "chapterIndex"}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$cacheBookSSE$3")
    static final class C00173 extends SuspendLambda implements Function3<CoroutineScope, Integer, Continuation<? super Object>, Object> {
        int I$1;
        Object L$1;
        int label;
        private /* synthetic */ Object L$0;
        /* synthetic */ int I$0;
        final /* synthetic */ Ref.ObjectRef<Set<Integer>> $cachedChapterContentSet;
        final /* synthetic */ Ref.ObjectRef<List<BookChapter>> $chapterList;
        final /* synthetic */ Ref.ObjectRef<String> $bookSource;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ Ref.ObjectRef<String> $userNameSpace;
        final /* synthetic */ Book $bookInfo;
        final /* synthetic */ File $localCacheDir;
        final /* synthetic */ Ref.IntRef $successCount;
        final /* synthetic */ Ref.BooleanRef $isEnd;
        final /* synthetic */ Ref.IntRef $failedCount;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00173(Ref.ObjectRef<Set<Integer>> $cachedChapterContentSet, Ref.ObjectRef<List<BookChapter>> $chapterList, Ref.ObjectRef<String> $bookSource, BookController this$0, Ref.ObjectRef<String> $userNameSpace, Book $bookInfo, File $localCacheDir, Ref.IntRef $successCount, Ref.BooleanRef $isEnd, Ref.IntRef $failedCount, Continuation<? super C00173> $completion) {
            super(3, $completion);
            this.$cachedChapterContentSet = $cachedChapterContentSet;
            this.$chapterList = $chapterList;
            this.$bookSource = $bookSource;
            this.this$0 = this$0;
            this.$userNameSpace = $userNameSpace;
            this.$bookInfo = $bookInfo;
            this.$localCacheDir = $localCacheDir;
            this.$successCount = $successCount;
            this.$isEnd = $isEnd;
            this.$failedCount = $failedCount;
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, int p2, @Nullable Continuation<Object> p3) {
            C00173 c00173 = new C00173(this.$cachedChapterContentSet, this.$chapterList, this.$bookSource, this.this$0, this.$userNameSpace, this.$bookInfo, this.$localCacheDir, this.$successCount, this.$isEnd, this.$failedCount, p3);
            c00173.L$0 = p1;
            c00173.I$0 = p2;
            return c00173.invokeSuspend(Unit.INSTANCE);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
            return invoke((CoroutineScope) p1, ((Number) p2).intValue(), (Continuation<Object>) p3);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0176  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x017a  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0186 A[Catch: Exception -> 0x021f, TryCatch #0 {Exception -> 0x021f, blocks: (B:7:0x0063, B:9:0x007d, B:10:0x009c, B:17:0x0119, B:21:0x017c, B:23:0x0186, B:25:0x01af, B:32:0x01f2, B:16:0x0113, B:31:0x01ec), top: B:39:0x0009 }] */
        /* JADX WARN: Removed duplicated region for block: B:24:0x01ad  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x01de  */
        @Nullable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(@NotNull Object $result) {
            int it;
            int chapterIndex;
            BookChapter chapterInfo;
            CoroutineScope $this$limitConcurrent;
            Object bookContent;
            String content;
            BookHelp bookHelp;
            CoroutineScope coroutineScope;
            BookSource bookSource;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            try {
            } catch (Exception e) {
                this.$isEnd.element = true;
                this.$failedCount.element++;
            }
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    $this$limitConcurrent = (CoroutineScope) this.L$0;
                    it = this.I$0;
                    if (!((Set) this.$cachedChapterContentSet.element).contains(Boxing.boxInt(it))) {
                        chapterIndex = it;
                        chapterInfo = (BookChapter) ((List) this.$chapterList.element).get(it);
                        String nextChapterUrl = null;
                        if (chapterIndex + 1 < ((List) this.$chapterList.element).size()) {
                            BookChapter nextChapterInfo = (BookChapter) ((List) this.$chapterList.element).get(chapterIndex + 1);
                            nextChapterUrl = nextChapterInfo.getUrl();
                        }
                        this.L$0 = $this$limitConcurrent;
                        this.L$1 = chapterInfo;
                        this.I$0 = it;
                        this.I$1 = chapterIndex;
                        this.label = 1;
                        bookContent = new WebBook((String) this.$bookSource.element, this.this$0.getAppConfig().getDebugLog(), (DebugLog) null, (String) this.$userNameSpace.element, 4, (DefaultConstructorMarker) null).getBookContent(this.$bookInfo, chapterInfo, nextChapterUrl, (Continuation) this);
                        if (bookContent == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        content = (String) bookContent;
                        File chapterCacheFile = new File(this.$localCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
                        FilesKt.writeText$default(chapterCacheFile, content, (Charset) null, 2, (Object) null);
                        bookHelp = BookHelp.INSTANCE;
                        coroutineScope = $this$limitConcurrent;
                        Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A((String) this.$bookSource.element);
                        BookSource bookSource2 = (BookSource) (!Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                        bookSource = bookSource2 != null ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource2;
                        this.L$0 = null;
                        this.L$1 = null;
                        this.I$0 = it;
                        this.I$1 = chapterIndex;
                        this.label = 2;
                        if (bookHelp.saveImages(coroutineScope, bookSource, this.$bookInfo, chapterInfo, content, (Continuation) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        this.$successCount.element++;
                        ((Set) this.$cachedChapterContentSet.element).add(Boxing.boxInt(chapterIndex));
                        break;
                    }
                    return Boxing.boxInt(it);
                case 1:
                    chapterIndex = this.I$1;
                    it = this.I$0;
                    chapterInfo = (BookChapter) this.L$1;
                    $this$limitConcurrent = (CoroutineScope) this.L$0;
                    ResultKt.throwOnFailure($result);
                    bookContent = $result;
                    content = (String) bookContent;
                    File chapterCacheFile2 = new File(this.$localCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
                    FilesKt.writeText$default(chapterCacheFile2, content, (Charset) null, 2, (Object) null);
                    bookHelp = BookHelp.INSTANCE;
                    coroutineScope = $this$limitConcurrent;
                    Object objM151fromJsonIoAF18A2 = BookSource.INSTANCE.m151fromJsonIoAF18A((String) this.$bookSource.element);
                    BookSource bookSource22 = (BookSource) (!Result.isFailure-impl(objM151fromJsonIoAF18A2) ? null : objM151fromJsonIoAF18A2);
                    if (bookSource22 != null) {
                    }
                    this.L$0 = null;
                    this.L$1 = null;
                    this.I$0 = it;
                    this.I$1 = chapterIndex;
                    this.label = 2;
                    if (bookHelp.saveImages(coroutineScope, bookSource, this.$bookInfo, chapterInfo, content, (Continuation) this) == coroutine_suspended) {
                    }
                    this.$successCount.element++;
                    ((Set) this.$cachedChapterContentSet.element).add(Boxing.boxInt(chapterIndex));
                    return Boxing.boxInt(it);
                case 2:
                    chapterIndex = this.I$1;
                    it = this.I$0;
                    ResultKt.throwOnFailure($result);
                    this.$successCount.element++;
                    ((Set) this.$cachedChapterContentSet.element).add(Boxing.boxInt(chapterIndex));
                    return Boxing.boxInt(it);
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object cacheBookOnServer(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00141 c00141;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00141) {
            c00141 = (C00141) $completion;
            if ((c00141.label & Integer.MIN_VALUE) != 0) {
                c00141.label -= Integer.MIN_VALUE;
            } else {
                c00141 = new C00141($completion);
            }
        }
        Object $result = c00141.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00141.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00141.L$0 = this;
                c00141.L$1 = context;
                c00141.L$2 = returnData;
                c00141.label = 1;
                objCheckAuth = checkAuth(context, c00141);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00141.L$2;
                context = (RoutingContext) c00141.L$1;
                this = (BookController) c00141.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        JsonArray jsonArray = context.getBodyAsJson().getJsonArray("bookUrlList");
        JsonArray bookUrlList = jsonArray == null ? new JsonArray() : jsonArray;
        if (bookUrlList.size() <= 0) {
            return returnData.setErrorMsg("请输入书籍链接");
        }
        CoroutineContext coroutineContext = (CoroutineExceptionHandler) new BookController$cacheBookOnServer$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key);
        String userNameSpace = this.getUserNameSpace(context);
        BuildersKt.launch$default(this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()).plus(coroutineContext), (CoroutineStart) null, this.new C00152(bookUrlList, userNameSpace, null), 2, (Object) null);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$cacheBookOnServer$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$cacheBookOnServer$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookController.kt", l = {2800}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$cacheBookOnServer$2")
    static final class C00152 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        final /* synthetic */ JsonArray $bookUrlList;
        final /* synthetic */ String $userNameSpace;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00152(JsonArray $bookUrlList, String $userNameSpace, Continuation<? super C00152> $completion) {
            super(2, $completion);
            this.$bookUrlList = $bookUrlList;
            this.$userNameSpace = $userNameSpace;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return BookController.this.new C00152(this.$bookUrlList, this.$userNameSpace, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    this.label = 1;
                    if (BookController.this.cacheBookOnServer(this.$bookUrlList, this.$userNameSpace, (Continuation) this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0072, code lost:

        if (0 < r40) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0075, code lost:

        r0 = r39;
        r39 = r39 + 1;
        r0 = r36.getString(r0);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, "bookUrl");
        r43 = r35.getShelfBookByURL(r0, r37);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0097, code lost:

        if (r43 != null) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x009a, code lost:

        com.htmake.reader.api.controller.BookControllerKt.logger.info("未找到书籍信息: {}", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00af, code lost:

        if (r43.isLocalBook() == false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b2, code lost:

        com.htmake.reader.api.controller.BookControllerKt.logger.info("本地书籍跳过缓存: {}", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00c2, code lost:

        com.htmake.reader.api.controller.BookControllerKt.logger.info("开始缓存书籍: {}", r43);
        r44 = r35.getBookSourceStringBySourceURLOpt(r43.getOrigin(), r37);
        r0 = r44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00ea, code lost:

        if (r0 == null) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00f4, code lost:

        if (r0.length() != 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00f7, code lost:

        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00fb, code lost:

        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00fc, code lost:

        if (r0 == false) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00ff, code lost:

        com.htmake.reader.api.controller.BookControllerKt.logger.info("未找到书源信息: {}", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x010f, code lost:

        r60.L$0 = r35;
        r60.L$1 = r36;
        r60.L$2 = r37;
        r60.L$3 = r43;
        r60.L$4 = r44;
        r60.L$5 = null;
        r60.L$6 = null;
        r60.L$7 = null;
        r60.L$8 = null;
        r60.I$0 = r39;
        r60.I$1 = r40;
        r60.label = 1;
        r0 = getLocalChapterList$default(r35, r43, r44, false, r37, false, null, r60, 48, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x016f, code lost:

        if (r0 != r0) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0174, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x01e2, code lost:

        if (0 <= r49) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x01e5, code lost:

        r0 = r48;
        r48 = r48 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x01f8, code lost:

        if (r46.contains(kotlin.coroutines.jvm.internal.Boxing.boxInt(r0)) != false) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x01fb, code lost:

        r51 = r0;
        r52 = (io.legado.app.data.entities.BookChapter) r45.get(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x020d, code lost:

        r53 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x021c, code lost:

        if ((r51 + 1) >= r45.size()) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x021f, code lost:

        r0 = (io.legado.app.data.entities.BookChapter) r45.get(r51 + 1);
        r53 = r0.getUrl();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0236, code lost:

        r60.L$0 = r35;
        r60.L$1 = r36;
        r60.L$2 = r37;
        r60.L$3 = r43;
        r60.L$4 = r44;
        r60.L$5 = r45;
        r60.L$6 = r46;
        r60.L$7 = r47;
        r60.L$8 = r52;
        r60.I$0 = r39;
        r60.I$1 = r40;
        r60.I$2 = r48;
        r60.I$3 = r49;
        r60.I$4 = r51;
        r60.label = 2;
        r0 = new io.legado.app.model.webBook.WebBook(r44, r35.getAppConfig().getDebugLog(), (io.legado.app.model.DebugLog) null, r37, 4, (kotlin.jvm.internal.DefaultConstructorMarker) null).getBookContent(r43, r52, r53, r60);
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x02bd, code lost:

        if (r0 != r0) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x02c2, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x04ed, code lost:

        if (r48 <= r49) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x04f0, code lost:

        com.htmake.reader.api.controller.BookControllerKt.logger.info("缓存书籍完成: {}", r43);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0501, code lost:

        if (r39 < r40) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0507, code lost:

        return kotlin.Unit.INSTANCE;
     */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:65:0x04ed -> B:33:0x01e5). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x0501 -> B:12:0x0075). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object cacheBookOnServer(@NotNull JsonArray bookUrlList, @NotNull String userNameSpace, @NotNull Continuation<? super Unit> $completion) throws Exception {
        AnonymousClass3 anonymousClass3;
        Book bookInfo;
        int size;
        int i;
        int size2;
        int i2;
        File localCacheDir;
        Set<Integer> cachedChapterContentSet;
        List chapterList;
        String bookSource;
        int chapterIndex;
        if ($completion instanceof AnonymousClass3) {
            anonymousClass3 = (AnonymousClass3) $completion;
            if ((anonymousClass3.label & Integer.MIN_VALUE) != 0) {
                anonymousClass3.label -= Integer.MIN_VALUE;
            } else {
                anonymousClass3 = new AnonymousClass3($completion);
            }
        }
        Object $result = anonymousClass3.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Exception e) {
            BookControllerKt.logger.info("cacheBookOnServer error: {}", e.getMessage());
        }
        switch (anonymousClass3.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                i = 0;
                size = bookUrlList.size();
                break;
            case 1:
                size = anonymousClass3.I$1;
                i = anonymousClass3.I$0;
                bookSource = (String) anonymousClass3.L$4;
                bookInfo = (Book) anonymousClass3.L$3;
                userNameSpace = (String) anonymousClass3.L$2;
                bookUrlList = (JsonArray) anonymousClass3.L$1;
                this = (BookController) anonymousClass3.L$0;
                ResultKt.throwOnFailure($result);
                Object localChapterList$default = $result;
                chapterList = (List) localChapterList$default;
                cachedChapterContentSet = this.getCachedChapterContentSet(bookInfo, userNameSpace);
                localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
                i2 = 0;
                size2 = chapterList.size() - 1;
                break;
            case 2:
                chapterIndex = anonymousClass3.I$4;
                size2 = anonymousClass3.I$3;
                i2 = anonymousClass3.I$2;
                size = anonymousClass3.I$1;
                i = anonymousClass3.I$0;
                BookChapter chapterInfo = (BookChapter) anonymousClass3.L$8;
                localCacheDir = (File) anonymousClass3.L$7;
                cachedChapterContentSet = (Set) anonymousClass3.L$6;
                chapterList = (List) anonymousClass3.L$5;
                bookSource = (String) anonymousClass3.L$4;
                bookInfo = (Book) anonymousClass3.L$3;
                userNameSpace = (String) anonymousClass3.L$2;
                bookUrlList = (JsonArray) anonymousClass3.L$1;
                this = (BookController) anonymousClass3.L$0;
                ResultKt.throwOnFailure($result);
                Object bookContent = $result;
                String content = (String) bookContent;
                File chapterCacheFile = new File(localCacheDir.getAbsolutePath() + ((Object) File.separator) + chapterIndex + ".txt");
                FilesKt.writeText$default(chapterCacheFile, content, (Charset) null, 2, (Object) null);
                BookHelp bookHelp = BookHelp.INSTANCE;
                BookController bookController = this;
                Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSource);
                BookSource bookSource2 = (BookSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A);
                BookSource bookSource3 = bookSource2 == null ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource2;
                anonymousClass3.L$0 = this;
                anonymousClass3.L$1 = bookUrlList;
                anonymousClass3.L$2 = userNameSpace;
                anonymousClass3.L$3 = bookInfo;
                anonymousClass3.L$4 = bookSource;
                anonymousClass3.L$5 = chapterList;
                anonymousClass3.L$6 = cachedChapterContentSet;
                anonymousClass3.L$7 = localCacheDir;
                anonymousClass3.L$8 = null;
                anonymousClass3.I$0 = i;
                anonymousClass3.I$1 = size;
                anonymousClass3.I$2 = i2;
                anonymousClass3.I$3 = size2;
                anonymousClass3.I$4 = chapterIndex;
                anonymousClass3.label = 3;
                if (bookHelp.saveImages(bookController, bookSource3, bookInfo, chapterInfo, content, anonymousClass3) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                cachedChapterContentSet.add(Boxing.boxInt(chapterIndex));
                break;
            case 3:
                chapterIndex = anonymousClass3.I$4;
                size2 = anonymousClass3.I$3;
                i2 = anonymousClass3.I$2;
                size = anonymousClass3.I$1;
                i = anonymousClass3.I$0;
                localCacheDir = (File) anonymousClass3.L$7;
                cachedChapterContentSet = (Set) anonymousClass3.L$6;
                chapterList = (List) anonymousClass3.L$5;
                bookSource = (String) anonymousClass3.L$4;
                bookInfo = (Book) anonymousClass3.L$3;
                userNameSpace = (String) anonymousClass3.L$2;
                bookUrlList = (JsonArray) anonymousClass3.L$1;
                this = (BookController) anonymousClass3.L$0;
                ResultKt.throwOnFailure($result);
                cachedChapterContentSet.add(Boxing.boxInt(chapterIndex));
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object deleteBookCache(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00191 c00191;
        ReturnData returnData;
        Object objCheckAuth;
        String bookUrl;
        if ($completion instanceof C00191) {
            c00191 = (C00191) $completion;
            if ((c00191.label & Integer.MIN_VALUE) != 0) {
                c00191.label -= Integer.MIN_VALUE;
            } else {
                c00191 = new C00191($completion);
            }
        }
        Object $result = c00191.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00191.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00191.L$0 = this;
                c00191.L$1 = context;
                c00191.L$2 = returnData;
                c00191.label = 1;
                objCheckAuth = checkAuth(context, c00191);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00191.L$2;
                context = (RoutingContext) c00191.L$1;
                this = (BookController) c00191.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
            String string2 = string == null ? context.getBodyAsJson().getString("bookUrl") : string;
            bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
        } else {
            List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
        }
        if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("请输入书籍链接");
        }
        String userNameSpace = this.getUserNameSpace(context);
        Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
        if (bookInfo == null) {
            return returnData.setErrorMsg("请先加入书架");
        }
        if (bookInfo.isLocalBook()) {
            return returnData.setErrorMsg("本地书籍无需删除缓存");
        }
        File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
        ExtKt.deleteRecursively(localCacheDir);
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object textToSpeech(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) {
        C00881 c00881;
        HttpServerResponse response;
        Object objCheckAuth;
        String voice;
        String pitch;
        String rate;
        String base64;
        if ($completion instanceof C00881) {
            c00881 = (C00881) $completion;
            if ((c00881.label & Integer.MIN_VALUE) != 0) {
                c00881.label -= Integer.MIN_VALUE;
            } else {
                c00881 = new C00881($completion);
            }
        }
        Object $result = c00881.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00881.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                response = context.response();
                c00881.L$0 = this;
                c00881.L$1 = context;
                c00881.L$2 = response;
                c00881.label = 1;
                objCheckAuth = checkAuth(context, c00881);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                response = (HttpServerResponse) c00881.L$2;
                context = (RoutingContext) c00881.L$1;
                this = (BookController) c00881.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            response.setStatusCode(403).end("未登录");
            return Unit.INSTANCE;
        }
        Ref.ObjectRef text = new Ref.ObjectRef();
        Ref.ObjectRef type = new Ref.ObjectRef();
        if (context.request().method() == HttpMethod.POST) {
            String string = context.getBodyAsJson().getString(NCXDocumentV2.NCXTags.text);
            text.element = string == null ? PackageDocumentBase.PREFIX_OPF : string;
            String string2 = context.getBodyAsJson().getString("type");
            type.element = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
            String string3 = context.getBodyAsJson().getString("voice");
            voice = string3 == null ? PackageDocumentBase.PREFIX_OPF : string3;
            String string4 = context.getBodyAsJson().getString("pitch");
            pitch = string4 == null ? PackageDocumentBase.PREFIX_OPF : string4;
            String string5 = context.getBodyAsJson().getString("rate");
            rate = string5 == null ? PackageDocumentBase.PREFIX_OPF : string5;
            String string6 = context.getBodyAsJson().getString("base64");
            base64 = string6 == null ? PackageDocumentBase.PREFIX_OPF : string6;
        } else {
            List listQueryParam = context.queryParam(NCXDocumentV2.NCXTags.text);
            Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"text\")");
            String str = (String) CollectionsKt.firstOrNull(listQueryParam);
            text.element = str == null ? PackageDocumentBase.PREFIX_OPF : str;
            List listQueryParam2 = context.queryParam("type");
            Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"type\")");
            String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
            type.element = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
            List listQueryParam3 = context.queryParam("voice");
            Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"voice\")");
            String str3 = (String) CollectionsKt.firstOrNull(listQueryParam3);
            voice = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
            List listQueryParam4 = context.queryParam("pitch");
            Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"pitch\")");
            String str4 = (String) CollectionsKt.firstOrNull(listQueryParam4);
            pitch = str4 == null ? PackageDocumentBase.PREFIX_OPF : str4;
            List listQueryParam5 = context.queryParam("rate");
            Intrinsics.checkNotNullExpressionValue(listQueryParam5, "context.queryParam(\"rate\")");
            String str5 = (String) CollectionsKt.firstOrNull(listQueryParam5);
            rate = str5 == null ? PackageDocumentBase.PREFIX_OPF : str5;
            List listQueryParam6 = context.queryParam("base64");
            Intrinsics.checkNotNullExpressionValue(listQueryParam6, "context.queryParam(\"base64\")");
            String str6 = (String) CollectionsKt.firstOrNull(listQueryParam6);
            base64 = str6 == null ? PackageDocumentBase.PREFIX_OPF : str6;
        }
        CharSequence charSequence = (CharSequence) type.element;
        if (charSequence == null || charSequence.length() == 0) {
            type.element = "edge";
        }
        CharSequence charSequence2 = (CharSequence) text.element;
        if (charSequence2 == null || charSequence2.length() == 0) {
            response.setStatusCode(404).end("参数错误");
            return Unit.INSTANCE;
        }
        CoroutineContext coroutineContext = (CoroutineExceptionHandler) new BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key, response);
        Map options = MapsKt.mapOf(new Pair[]{TuplesKt.to("voice", voice), TuplesKt.to("pitch", pitch), TuplesKt.to("rate", rate), TuplesKt.to("base64", base64)});
        BuildersKt.launch$default(this, new MDCContext((Map) null, 1, (DefaultConstructorMarker) null).plus(Dispatchers.getIO()).plus(coroutineContext), (CoroutineStart) null, new C00892(type, this, response, text, options, context, null), 2, (Object) null);
        return Unit.INSTANCE;
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$textToSpeech$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$textToSpeech$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "Lkotlinx/coroutines/CoroutineScope;"})
    @DebugMetadata(f = "BookController.kt", l = {2942, 2943, 2944}, i = {}, s = {}, n = {}, m = "invokeSuspend", c = "com.htmake.reader.api.controller.BookController$textToSpeech$2")
    static final class C00892 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        int label;
        final /* synthetic */ Ref.ObjectRef<String> $type;
        final /* synthetic */ BookController this$0;
        final /* synthetic */ HttpServerResponse $response;
        final /* synthetic */ Ref.ObjectRef<String> $text;
        final /* synthetic */ Map<String, String> $options;
        final /* synthetic */ RoutingContext $context;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C00892(Ref.ObjectRef<String> $type, BookController this$0, HttpServerResponse $response, Ref.ObjectRef<String> $text, Map<String, String> $options, RoutingContext $context, Continuation<? super C00892> $completion) {
            super(2, $completion);
            this.$type = $type;
            this.this$0 = this$0;
            this.$response = $response;
            this.$text = $text;
            this.$options = $options;
            this.$context = $context;
        }

        @NotNull
        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
            return new C00892(this.$type, this.this$0, this.$response, this.$text, this.$options, this.$context, $completion);
        }

        @Nullable
        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
            return create(p1, p2).invokeSuspend(Unit.INSTANCE);
        }

        @Nullable
        public final Object invokeSuspend(@NotNull Object $result) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    String str = (String) this.$type.element;
                    if (Intrinsics.areEqual(str, "edge")) {
                        BookController bookController = this.this$0;
                        HttpServerResponse httpServerResponse = this.$response;
                        Intrinsics.checkNotNullExpressionValue(httpServerResponse, "response");
                        this.label = 1;
                        if (bookController.ttsByEdge(httpServerResponse, (String) this.$text.element, this.$options, (Continuation) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else if (Intrinsics.areEqual(str, "textToSpeechCn")) {
                        BookController bookController2 = this.this$0;
                        HttpServerResponse httpServerResponse2 = this.$response;
                        Intrinsics.checkNotNullExpressionValue(httpServerResponse2, "response");
                        this.label = 2;
                        if (bookController2.ttsByTextToSpeechCn(httpServerResponse2, (String) this.$text.element, this.$options, (Continuation) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    } else {
                        BookController bookController3 = this.this$0;
                        HttpServerResponse httpServerResponse3 = this.$response;
                        Intrinsics.checkNotNullExpressionValue(httpServerResponse3, "response");
                        this.label = 3;
                        if (bookController3.ttsByApi(httpServerResponse3, (String) this.$text.element, this.this$0.getUserNameSpace(this.$context), this.$options, (Continuation) this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                    }
                    break;
                case 1:
                    ResultKt.throwOnFailure($result);
                    break;
                case 2:
                    ResultKt.throwOnFailure($result);
                    break;
                case 3:
                    ResultKt.throwOnFailure($result);
                    break;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object ttsByEdge$default(BookController bookController, HttpServerResponse httpServerResponse, String str, Map map, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            map = null;
        }
        return bookController.ttsByEdge(httpServerResponse, str, map, continuation);
    }

    @Nullable
    public final Object ttsByEdge(@NotNull HttpServerResponse response, @NotNull String text, @Nullable Map<String, String> options, @NotNull Continuation<? super Unit> $completion) {
        VoiceEnum voice = VoiceEnum.zh_CN_XiaoxiaoNeural;
        String rate = "0";
        String pitch = "0%";
        if (options != null) {
            if (options.containsKey("voice")) {
                VoiceEnum voiceEnumFromSortName = VoiceEnum.fromSortName(options.get("voice"));
                voice = voiceEnumFromSortName == null ? VoiceEnum.zh_CN_XiaoxiaoNeural : voiceEnumFromSortName;
            }
            if (options.containsKey("rate")) {
                String str = options.get("rate");
                rate = str == null ? "0" : str;
            }
            if (options.containsKey("pitch")) {
                pitch = Intrinsics.stringPlus(options.get("pitch"), "%");
            }
        }
        TTSService ts = TTSService.builder().build();
        SSML ssml = SSML.builder().synthesisText(text).voice(voice).rate(rate).pitch(pitch).style(TtsStyleEnum.chat).build();
        byte[] mp3byte = ts.sendText(ssml);
        if (options != null && "1".equals(options.get("base64"))) {
            ReturnData returnData = new ReturnData();
            HttpServerResponse httpServerResponsePutHeader = response.putHeader("content-type", "application/json; charset=utf-8");
            String strEncodeToString = Base64.getEncoder().encodeToString(mp3byte);
            Intrinsics.checkNotNullExpressionValue(strEncodeToString, "getEncoder().encodeToString(mp3byte)");
            httpServerResponsePutHeader.end(ExtKt.jsonEncode$default(ReturnData.setData$default(returnData, strEncodeToString, null, 2, null), false, 2, null));
        } else {
            response.putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, "audio/mpeg").end(Buffer.buffer(mp3byte));
        }
        return Unit.INSTANCE;
    }

    @Nullable
    public final HttpTTS getHttpTTSByName(@NotNull String name, @NotNull String userNameSpace) {
        JsonArray list;
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        if ((name.length() == 0) || (list = ExtKt.asJsonArray(getUserStorage(userNameSpace, "httpTTS"))) == null) {
            return null;
        }
        int i = 0;
        int size = list.size();
        if (0 < size) {
            do {
                int i2 = i;
                i++;
                HttpTTS.Companion companion = HttpTTS.INSTANCE;
                String string = list.getJsonObject(i2).toString();
                Intrinsics.checkNotNullExpressionValue(string, "list.getJsonObject(i).toString()");
                Object objM156fromJsonIoAF18A = companion.m156fromJsonIoAF18A(string);
                HttpTTS httpTTS = (HttpTTS) (Result.isFailure-impl(objM156fromJsonIoAF18A) ? null : objM156fromJsonIoAF18A);
                if (httpTTS != null && httpTTS.getName().equals(name)) {
                    return httpTTS;
                }
            } while (i < size);
            return null;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object ttsByApi(@NotNull HttpServerResponse response, @NotNull String text, @NotNull String userNameSpace, @Nullable Map<String, String> options, @NotNull Continuation<? super Unit> $completion) throws Exception {
        C00901 c00901;
        HttpTTS httpTTS;
        Object speakStream;
        String str;
        Double dBoxDouble;
        if ($completion instanceof C00901) {
            c00901 = (C00901) $completion;
            if ((c00901.label & Integer.MIN_VALUE) != 0) {
                c00901.label -= Integer.MIN_VALUE;
            } else {
                c00901 = new C00901($completion);
            }
        }
        Object $result = c00901.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00901.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String voice = options == null ? null : options.get("voice");
                String str2 = voice;
                if (str2 == null || str2.length() == 0) {
                    response.setStatusCode(404).end();
                    return Unit.INSTANCE;
                }
                httpTTS = getHttpTTSByName(voice, userNameSpace);
                if (httpTTS == null) {
                    response.setStatusCode(404).end();
                    return Unit.INSTANCE;
                }
                double dDoubleValue = (options == null || (str = options.get("rate")) == null || (dBoxDouble = Boxing.boxDouble(Double.parseDouble(str))) == null) ? 1.0d : dBoxDouble.doubleValue();
                double speechRate = dDoubleValue;
                c00901.L$0 = response;
                c00901.L$1 = options;
                c00901.L$2 = httpTTS;
                c00901.label = 1;
                speakStream = getSpeakStream(httpTTS, text, (int) (((double) 5) + ((speechRate - 0.5d) * ((double) 30))), c00901);
                if (speakStream == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                httpTTS = (HttpTTS) c00901.L$2;
                options = (Map) c00901.L$1;
                response = (HttpServerResponse) c00901.L$0;
                ResultKt.throwOnFailure($result);
                speakStream = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        InputStream stream = (InputStream) speakStream;
        if (stream != null) {
            if (options != null && "1".equals(options.get("base64"))) {
                ReturnData returnData = new ReturnData();
                HttpServerResponse httpServerResponsePutHeader = response.putHeader("content-type", "application/json; charset=utf-8");
                String strEncodeToString = Base64.getEncoder().encodeToString(ByteStreamsKt.readBytes(stream));
                Intrinsics.checkNotNullExpressionValue(strEncodeToString, "getEncoder().encodeToString(stream.readBytes())");
                httpServerResponsePutHeader.end(ExtKt.jsonEncode$default(ReturnData.setData$default(returnData, strEncodeToString, null, 2, null), false, 2, null));
            } else {
                HttpServerResponse httpServerResponse = response;
                String contentType = httpTTS.getContentType();
                httpServerResponse.putHeader(NCXDocumentV3.XHTMLAttributeValues.Content_Type, contentType == null ? "audio/mpeg" : contentType).end(Buffer.buffer(ByteStreamsKt.readBytes(stream)));
            }
        } else {
            response.setStatusCode(404).end();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object ttsByApi$default(BookController bookController, HttpServerResponse httpServerResponse, String str, String str2, Map map, Continuation continuation, int i, Object obj) {
        if ((i & 8) != 0) {
            map = null;
        }
        return bookController.ttsByApi(httpServerResponse, str, str2, map, continuation);
    }

    /* JADX WARN: Not initialized variable reg: 21, insn: 0x0303: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) =
      (r21 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('downloadErrorNo' kotlin.jvm.internal.Ref$IntRef)])
     (LINE:3078), block:B:79:0x0303 */
    /* JADX WARN: Not initialized variable reg: 21, insn: 0x033b: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) =
      (r21 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('downloadErrorNo' kotlin.jvm.internal.Ref$IntRef)])
     (LINE:3085), block:B:83:0x033b */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0160  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0164 A[Catch: Exception -> 0x02af, TryCatch #0 {Exception -> 0x02af, blocks: (B:12:0x006b, B:19:0x0141, B:31:0x0187, B:33:0x019c, B:34:0x01a6, B:35:0x01a7, B:36:0x01af, B:60:0x027e, B:39:0x01ca, B:41:0x01ec, B:42:0x0205, B:55:0x0236, B:57:0x025c, B:58:0x027b, B:46:0x0213, B:22:0x0164, B:18:0x0139), top: B:92:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0187 A[Catch: Exception -> 0x02af, TryCatch #0 {Exception -> 0x02af, blocks: (B:12:0x006b, B:19:0x0141, B:31:0x0187, B:33:0x019c, B:34:0x01a6, B:35:0x01a7, B:36:0x01af, B:60:0x027e, B:39:0x01ca, B:41:0x01ec, B:42:0x0205, B:55:0x0236, B:57:0x025c, B:58:0x027b, B:46:0x0213, B:22:0x0164, B:18:0x0139), top: B:92:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01ca A[Catch: Exception -> 0x02af, TryCatch #0 {Exception -> 0x02af, blocks: (B:12:0x006b, B:19:0x0141, B:31:0x0187, B:33:0x019c, B:34:0x01a6, B:35:0x01a7, B:36:0x01af, B:60:0x027e, B:39:0x01ca, B:41:0x01ec, B:42:0x0205, B:55:0x0236, B:57:0x025c, B:58:0x027b, B:46:0x0213, B:22:0x0164, B:18:0x0139), top: B:92:0x0046 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getSpeakStream(@NotNull HttpTTS httpTts, @NotNull String speakText, int speechRate, @NotNull Continuation<? super InputStream> $completion) throws Exception {
        C00451 c00451;
        Ref.IntRef downloadErrorNo;
        Ref.IntRef downloadErrorNo2;
        Ref.ObjectRef objectRef;
        Ref.ObjectRef response;
        AnalyzeUrl analyzeUrl;
        Object responseAwait;
        String checkJs;
        boolean z;
        String contentType;
        boolean z2;
        if ($completion instanceof C00451) {
            c00451 = (C00451) $completion;
            if ((c00451.label & Integer.MIN_VALUE) != 0) {
                c00451.label -= Integer.MIN_VALUE;
            } else {
                c00451 = new C00451($completion);
            }
        }
        Object $result = c00451.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Exception e) {
            if (e instanceof CancellationException) {
                throw e;
            }
            if (e instanceof ScriptException ? true : e instanceof WrappedException) {
                BookControllerKt.logger.error(Intrinsics.stringPlus("js错误\n", e.getLocalizedMessage()), e);
                throw e;
            }
            if (!(e instanceof SocketTimeoutException ? true : e instanceof ConnectException)) {
                downloadErrorNo.element++;
                BookControllerKt.logger.error(Intrinsics.stringPlus("tts下载错误\n", e.getLocalizedMessage()), e);
                if (downloadErrorNo.element > 5) {
                    BookControllerKt.logger.error("TTS服务器连续5次错误，已暂停阅读。", e);
                    throw e;
                }
                BookControllerKt.logger.error(Intrinsics.stringPlus("TTS下载音频出错，使用无声音频代替。\n朗读文本：", speakText));
                return null;
            }
            downloadErrorNo2.element++;
            if (downloadErrorNo2.element > 5) {
                String msg = Intrinsics.stringPlus("tts超时或连接错误超过5次\n", e.getLocalizedMessage());
                BookControllerKt.logger.error(msg, e);
                throw e;
            }
        }
        switch (c00451.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                downloadErrorNo2 = new Ref.IntRef();
                analyzeUrl = new AnalyzeUrl(httpTts.getUrl(), null, null, speakText, Boxing.boxInt(speechRate), null, httpTts, null, null, httpTts.getHeaderMap(true), Debug.INSTANCE, 422, null);
                response = new Ref.ObjectRef();
                objectRef = response;
                c00451.L$0 = this;
                c00451.L$1 = httpTts;
                c00451.L$2 = speakText;
                c00451.L$3 = downloadErrorNo2;
                c00451.L$4 = analyzeUrl;
                c00451.L$5 = response;
                c00451.L$6 = objectRef;
                c00451.I$0 = speechRate;
                c00451.label = 1;
                responseAwait = analyzeUrl.getResponseAwait(c00451);
                if (responseAwait == coroutine_suspended) {
                    return coroutine_suspended;
                }
                objectRef.element = responseAwait;
                JobKt.ensureActive(this.getCoroutineContext());
                checkJs = httpTts.getLoginCheckJs();
                if (checkJs != null) {
                    z = false;
                } else {
                    z = !StringsKt.isBlank(checkJs);
                }
                if (z) {
                    Ref.ObjectRef objectRef2 = response;
                    Object objEvalJS = analyzeUrl.evalJS(checkJs, response.element);
                    if (objEvalJS == null) {
                        throw new NullPointerException("null cannot be cast to non-null type okhttp3.Response");
                    }
                    objectRef2.element = (Response) objEvalJS;
                }
                contentType = ((Response) response.element).headers().get(NCXDocumentV3.XHTMLAttributeValues.Content_Type);
                if (contentType == null) {
                    String ct = httpTts.getContentType();
                    if (Intrinsics.areEqual(contentType, "application/json")) {
                        ResponseBody responseBodyBody = ((Response) response.element).body();
                        Intrinsics.checkNotNull(responseBodyBody);
                        throw new NoStackTraceException(responseBodyBody.string());
                    }
                    if (ct == null) {
                        z2 = false;
                    } else {
                        z2 = !StringsKt.isBlank(ct);
                    }
                    if (z2) {
                        if (!new Regex(ct).matches(contentType)) {
                            ResponseBody responseBodyBody2 = ((Response) response.element).body();
                            Intrinsics.checkNotNull(responseBodyBody2);
                            throw new NoStackTraceException(Intrinsics.stringPlus("TTS服务器返回错误：", responseBodyBody2.string()));
                        }
                    }
                }
                JobKt.ensureActive(this.getCoroutineContext());
                ResponseBody responseBodyBody3 = ((Response) response.element).body();
                Intrinsics.checkNotNull(responseBodyBody3);
                InputStream stream = responseBodyBody3.byteStream();
                downloadErrorNo2.element = 0;
                return stream;
            case 1:
                int i = c00451.I$0;
                objectRef = (Ref.ObjectRef) c00451.L$6;
                response = (Ref.ObjectRef) c00451.L$5;
                analyzeUrl = (AnalyzeUrl) c00451.L$4;
                downloadErrorNo2 = (Ref.IntRef) c00451.L$3;
                httpTts = (HttpTTS) c00451.L$1;
                this = (BookController) c00451.L$0;
                ResultKt.throwOnFailure($result);
                responseAwait = $result;
                objectRef.element = responseAwait;
                JobKt.ensureActive(this.getCoroutineContext());
                checkJs = httpTts.getLoginCheckJs();
                if (checkJs != null) {
                }
                if (z) {
                }
                contentType = ((Response) response.element).headers().get(NCXDocumentV3.XHTMLAttributeValues.Content_Type);
                if (contentType == null) {
                }
                JobKt.ensureActive(this.getCoroutineContext());
                ResponseBody responseBodyBody32 = ((Response) response.element).body();
                Intrinsics.checkNotNull(responseBodyBody32);
                InputStream stream2 = responseBodyBody32.byteStream();
                downloadErrorNo2.element = 0;
                return stream2;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object ttsByTextToSpeechCn(@NotNull HttpServerResponse response, @NotNull String text, @Nullable Map<String, String> options, @NotNull Continuation<? super Unit> $completion) {
        C00911 c00911;
        Object objAwaitResult;
        if ($completion instanceof C00911) {
            c00911 = (C00911) $completion;
            if ((c00911.label & Integer.MIN_VALUE) != 0) {
                c00911.label -= Integer.MIN_VALUE;
            } else {
                c00911 = new C00911($completion);
            }
        }
        Object $result = c00911.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00911.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                Map map = MapsKt.mutableMapOf(new Pair[]{TuplesKt.to(PackageDocumentBase.DCTags.language, "中文（普通话，简体）"), TuplesKt.to("voice", "zh-CN-XiaoxiaoNeural"), TuplesKt.to(NCXDocumentV2.NCXTags.text, text), TuplesKt.to("role", "0"), TuplesKt.to("style", "0"), TuplesKt.to("rate", "0"), TuplesKt.to("pitch", "0"), TuplesKt.to("kbitrate", "audio-16khz-32kbitrate-mono-mp3"), TuplesKt.to("silence", PackageDocumentBase.PREFIX_OPF), TuplesKt.to("styledegree", "1"), TuplesKt.to("user_id", PackageDocumentBase.PREFIX_OPF), TuplesKt.to("yzm", PackageDocumentBase.PREFIX_OPF)});
                if (options != null) {
                    map.putAll(options);
                }
                final CaseInsensitiveHeaders multiMap = new CaseInsensitiveHeaders();
                final C00922 c00922 = new C00922(multiMap);
                map.forEach(new BiConsumer() { // from class: com.htmake.reader.api.controller.BookControllerKt$sam$java_util_function_BiConsumer$0
                    @Override // java.util.function.BiConsumer
                    public final /* synthetic */ void accept(Object p0, Object p1) {
                        c00922.invoke(p0, p1);
                    }
                });
                final String ttsUrl = "https://www.text-to-speech.cn/getSpeek.php";
                c00911.L$0 = response;
                c00911.label = 1;
                objAwaitResult = VertxCoroutineKt.awaitResult(new Function1<Handler<AsyncResult<HttpResponse<Buffer>>>, Unit>() { // from class: com.htmake.reader.api.controller.BookController$ttsByTextToSpeechCn$result$1
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(1);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                        invoke((Handler<AsyncResult<HttpResponse<Buffer>>>) p1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull Handler<AsyncResult<HttpResponse<Buffer>>> handler) {
                        Intrinsics.checkNotNullParameter(handler, "handler");
                        this.this$0.webClient.postAbs(ttsUrl).timeout(5000L).putHeader("Origin", "https://www.text-to-speech.cn").putHeader("Referer", "https://www.text-to-speech.cn/").putHeader(AppConst.UA_NAME, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36").sendForm(multiMap, handler);
                    }
                }, c00911);
                if (objAwaitResult == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                response = (HttpServerResponse) c00911.L$0;
                ResultKt.throwOnFailure($result);
                objAwaitResult = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        HttpResponse result = (HttpResponse) objAwaitResult;
        BookControllerKt.logger.info("res: {}", result);
        if (result != null) {
            JsonObject jsonRes = result.bodyAsJsonObject();
            BookControllerKt.logger.info("jsonRes: {}", jsonRes);
            if (jsonRes == null || jsonRes.getString("download") == null) {
                response.setStatusCode(404).end();
            } else {
                response.setStatusCode(302).putHeader("Location", jsonRes.getString("download")).end();
            }
        } else {
            response.setStatusCode(404).end();
        }
        return Unit.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ Object ttsByTextToSpeechCn$default(BookController bookController, HttpServerResponse httpServerResponse, String str, Map map, Continuation continuation, int i, Object obj) {
        if ((i & 4) != 0) {
            map = null;
        }
        return bookController.ttsByTextToSpeechCn(httpServerResponse, str, map, continuation);
    }

    /* JADX INFO: renamed from: com.htmake.reader.api.controller.BookController$ttsByTextToSpeechCn$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: BookController.kt */
    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/api/controller/BookController$ttsByTextToSpeechCn$2.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    /* synthetic */ class C00922 extends AdaptedFunctionReference implements Function2<String, String, Unit> {
        C00922(CaseInsensitiveHeaders receiver) {
            super(2, receiver, CaseInsensitiveHeaders.class, "add", "add(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/MultiMap;", 8);
        }

        public final void invoke(String p0, String p1) {
            BookController.ttsByTextToSpeechCn$add((CaseInsensitiveHeaders) this.receiver, p0, p1);
        }

        public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
            invoke((String) p1, (String) p2);
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final /* synthetic */ void ttsByTextToSpeechCn$add(CaseInsensitiveHeaders $this$ttsByTextToSpeechCn_u24add, String p0, String p1) {
        $this$ttsByTextToSpeechCn_u24add.add(p0, p1);
    }

    @NotNull
    public final File getChapterCacheDir(@NotNull Book bookInfo, @NotNull String userNameSpace) {
        Intrinsics.checkNotNullParameter(bookInfo, "bookInfo");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        String md5Encode = MD5Utils.INSTANCE.md5Encode(bookInfo.getBookUrl()).toString();
        String localCacheDirPath = ExtKt.getWorkDir("storage", "data", userNameSpace, bookInfo.getName() + '_' + bookInfo.getAuthor(), md5Encode);
        File localCacheDir = new File(localCacheDirPath);
        if (!localCacheDir.exists()) {
            localCacheDir.mkdirs();
        }
        return localCacheDir;
    }

    @NotNull
    public final Set<Integer> getCachedChapterContentSet(@NotNull Book bookInfo, @NotNull String userNameSpace) {
        Intrinsics.checkNotNullParameter(bookInfo, "bookInfo");
        Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
        File localCacheDir = getChapterCacheDir(bookInfo, userNameSpace);
        Set cachedChapterContentSet = new LinkedHashSet();
        Object[] objArrListFiles = localCacheDir.listFiles();
        Intrinsics.checkNotNullExpressionValue(objArrListFiles, "localCacheDir.listFiles()");
        Object[] $this$forEach$iv = objArrListFiles;
        for (Object element$iv : $this$forEach$iv) {
            File it = (File) element$iv;
            String name = it.getName();
            Intrinsics.checkNotNullExpressionValue(name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object) null)) {
                String name2 = it.getName();
                Intrinsics.checkNotNullExpressionValue(name2, "it.name");
                if (StringsKt.endsWith$default(name2, ".txt", false, 2, (Object) null)) {
                    String name3 = it.getName();
                    Intrinsics.checkNotNullExpressionValue(name3, "it.name");
                    cachedChapterContentSet.add(Integer.valueOf(Integer.parseInt(StringsKt.replace$default(name3, ".txt", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null))));
                }
            }
        }
        return cachedChapterContentSet;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00bc  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0147 A[LOOP:0: B:26:0x0147->B:37:?, LOOP_START, PHI: r14
      0x0147: PHI (r14v2 int) = (r14v1 int), (r14v3 int) binds: [B:25:0x0144, B:37:?] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getShelfBookWithCacheInfo(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00441 c00441;
        String userNameSpace;
        ReturnData returnData;
        Object bookShelfBooks;
        Object objCheckAuth;
        int size;
        if ($completion instanceof C00441) {
            c00441 = (C00441) $completion;
            if ((c00441.label & Integer.MIN_VALUE) != 0) {
                c00441.label -= Integer.MIN_VALUE;
            } else {
                c00441 = new C00441($completion);
            }
        }
        Object $result = c00441.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00441.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00441.L$0 = this;
                c00441.L$1 = context;
                c00441.L$2 = returnData;
                c00441.label = 1;
                objCheckAuth = checkAuth(context, c00441);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                userNameSpace = this.getUserNameSpace(context);
                c00441.L$0 = this;
                c00441.L$1 = returnData;
                c00441.L$2 = userNameSpace;
                c00441.label = 2;
                bookShelfBooks = this.getBookShelfBooks(false, userNameSpace, c00441);
                if (bookShelfBooks == coroutine_suspended) {
                    return coroutine_suspended;
                }
                List bookList = (List) bookShelfBooks;
                List result = new ArrayList();
                int i = 0;
                size = bookList.size();
                if (0 < size) {
                    do {
                        int i2 = i;
                        i++;
                        Book bookInfo = (Book) bookList.get(i2);
                        if (!bookInfo.isLocalBook()) {
                            Set<Integer> cachedChapterContentSet = this.getCachedChapterContentSet(bookInfo, userNameSpace);
                            Map bookInfoMap = TypeIntrinsics.asMutableMap(ExtKt.toMap(bookInfo));
                            bookInfoMap.put("cachedChapterCount", Boxing.boxInt(cachedChapterContentSet.size()));
                            result.add(bookInfoMap);
                        } else {
                            result.add(bookInfo);
                        }
                    } while (i < size);
                }
                return ReturnData.setData$default(returnData, result, null, 2, null);
            case 1:
                returnData = (ReturnData) c00441.L$2;
                context = (RoutingContext) c00441.L$1;
                this = (BookController) c00441.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                userNameSpace = (String) c00441.L$2;
                returnData = (ReturnData) c00441.L$1;
                this = (BookController) c00441.L$0;
                ResultKt.throwOnFailure($result);
                bookShelfBooks = $result;
                List bookList2 = (List) bookShelfBooks;
                List result2 = new ArrayList();
                int i3 = 0;
                size = bookList2.size();
                if (0 < size) {
                }
                return ReturnData.setData$default(returnData, result2, null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x03a5  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03f6  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exportBook(@NotNull RoutingContext context, @NotNull Continuation<? super Unit> $completion) throws IOException {
        C00231 c00231;
        Object objExportToTxt;
        Object objExportToEpub;
        int isEpub;
        Book bookInfo;
        String userNameSpace;
        ReturnData returnData;
        Object bookSourceString$default;
        Object objCheckAuth;
        File file;
        String bookUrl;
        Integer numBoxInt;
        if ($completion instanceof C00231) {
            c00231 = (C00231) $completion;
            if ((c00231.label & Integer.MIN_VALUE) != 0) {
                c00231.label -= Integer.MIN_VALUE;
            } else {
                c00231 = new C00231($completion);
            }
        }
        Object $result = c00231.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00231.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00231.L$0 = this;
                c00231.L$1 = context;
                c00231.L$2 = returnData;
                c00231.label = 1;
                objCheckAuth = checkAuth(context, c00231);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    VertExtKt.success(context, ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用"));
                    return Unit.INSTANCE;
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                    Integer integer = context.getBodyAsJson().getInteger("isEpub", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"isEpub\", 0)");
                    isEpub = integer.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    List listQueryParam2 = context.queryParam("isEpub");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"isEpub\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    int iIntValue = (str2 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str2))) == null) ? 0 : numBoxInt.intValue();
                    isEpub = iIntValue;
                }
                if (bookUrl.length() == 0) {
                    VertExtKt.success(context, returnData.setErrorMsg("请输入书籍链接"));
                    return Unit.INSTANCE;
                }
                userNameSpace = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo == null) {
                    VertExtKt.success(context, returnData.setErrorMsg("请先加入书架"));
                    return Unit.INSTANCE;
                }
                if (bookInfo.isLocalBook() && !bookInfo.isLocalTxt()) {
                    File localFile = bookInfo.getLocalFile();
                    context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(localFile.getName(), Constants.CHARACTER_ENCODING))).sendFile(localFile.toString());
                    return Unit.INSTANCE;
                }
                if (bookInfo.isLocalTxt() && isEpub <= 0) {
                    File localFile2 = bookInfo.getLocalFile();
                    context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(localFile2.getName(), Constants.CHARACTER_ENCODING))).sendFile(localFile2.toString());
                    return Unit.INSTANCE;
                }
                c00231.L$0 = this;
                c00231.L$1 = context;
                c00231.L$2 = returnData;
                c00231.L$3 = userNameSpace;
                c00231.L$4 = bookInfo;
                c00231.I$0 = isEpub;
                c00231.label = 2;
                bookSourceString$default = getBookSourceString$default(this, context, bookInfo.getOrigin(), false, c00231, 4, null);
                if (bookSourceString$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                String bookSource = (String) bookSourceString$default;
                if (!bookInfo.isLocalBook()) {
                    String str3 = bookSource;
                    if (str3 == null || str3.length() == 0) {
                        VertExtKt.success(context, returnData.setErrorMsg("未配置书源"));
                        return Unit.INSTANCE;
                    }
                }
                File exportDir = new File(ExtKt.getWorkDir("storage", "assets", userNameSpace, "export"));
                if (isEpub <= 0) {
                    c00231.L$0 = context;
                    c00231.L$1 = null;
                    c00231.L$2 = null;
                    c00231.L$3 = null;
                    c00231.L$4 = null;
                    c00231.label = 3;
                    objExportToEpub = this.exportToEpub(exportDir, bookInfo, bookSource, userNameSpace, c00231);
                    if (objExportToEpub == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    file = (File) objExportToEpub;
                    File bookFile = file;
                    context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(bookFile.getName(), Constants.CHARACTER_ENCODING))).sendFile(bookFile.toString());
                    return Unit.INSTANCE;
                }
                Intrinsics.checkNotNull(bookSource);
                c00231.L$0 = context;
                c00231.L$1 = null;
                c00231.L$2 = null;
                c00231.L$3 = null;
                c00231.L$4 = null;
                c00231.label = 4;
                objExportToTxt = this.exportToTxt(exportDir, bookInfo, bookSource, userNameSpace, c00231);
                if (objExportToTxt == coroutine_suspended) {
                    return coroutine_suspended;
                }
                file = (File) objExportToTxt;
                File bookFile2 = file;
                context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(bookFile2.getName(), Constants.CHARACTER_ENCODING))).sendFile(bookFile2.toString());
                return Unit.INSTANCE;
            case 1:
                returnData = (ReturnData) c00231.L$2;
                context = (RoutingContext) c00231.L$1;
                this = (BookController) c00231.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                isEpub = c00231.I$0;
                bookInfo = (Book) c00231.L$4;
                userNameSpace = (String) c00231.L$3;
                returnData = (ReturnData) c00231.L$2;
                context = (RoutingContext) c00231.L$1;
                this = (BookController) c00231.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                String bookSource2 = (String) bookSourceString$default;
                if (!bookInfo.isLocalBook()) {
                }
                File exportDir2 = new File(ExtKt.getWorkDir("storage", "assets", userNameSpace, "export"));
                if (isEpub <= 0) {
                }
                break;
            case 3:
                context = (RoutingContext) c00231.L$0;
                ResultKt.throwOnFailure($result);
                objExportToEpub = $result;
                file = (File) objExportToEpub;
                File bookFile22 = file;
                context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(bookFile22.getName(), Constants.CHARACTER_ENCODING))).sendFile(bookFile22.toString());
                return Unit.INSTANCE;
            case 4:
                context = (RoutingContext) c00231.L$0;
                ResultKt.throwOnFailure($result);
                objExportToTxt = $result;
                file = (File) objExportToTxt;
                File bookFile222 = file;
                context.response().putHeader("Cache-Control", "300").putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(bookFile222.getName(), Constants.CHARACTER_ENCODING))).sendFile(bookFile222.toString());
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exportToTxt(@NotNull File exportDir, @NotNull Book bookInfo, @NotNull String bookSource, @NotNull String userNameSpace, @NotNull Continuation<? super File> $completion) throws IOException {
        C00251 c00251;
        final File bookFile;
        if ($completion instanceof C00251) {
            c00251 = (C00251) $completion;
            if ((c00251.label & Integer.MIN_VALUE) != 0) {
                c00251.label -= Integer.MIN_VALUE;
            } else {
                c00251 = new C00251($completion);
            }
        }
        Object $result = c00251.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00251.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String filename = (char) 12298 + bookInfo.getName() + "》作者：" + bookInfo.getRealAuthor() + ".txt";
                String bookPath = FileUtils.INSTANCE.getPath(exportDir, filename);
                bookFile = FileUtils.INSTANCE.createFileWithReplace(bookPath);
                c00251.L$0 = bookFile;
                c00251.label = 1;
                if (getAllContents(bookInfo, bookSource, userNameSpace, new Function2<String, ArrayList<Triple<? extends String, ? extends Integer, ? extends String>>, Unit>() { // from class: com.htmake.reader.api.controller.BookController.exportToTxt.2
                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super(2);
                    }

                    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2) {
                        invoke((String) p1, (ArrayList<Triple<String, Integer, String>>) p2);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull String text, @Nullable ArrayList<Triple<String, Integer, String>> srcList) {
                        Intrinsics.checkNotNullParameter(text, NCXDocumentV2.NCXTags.text);
                        File file = bookFile;
                        Charset charsetForName = Charset.forName(this.getAppConfig().getExportCharset());
                        Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(appConfig.exportCharset)");
                        FilesKt.appendText(file, text, charsetForName);
                    }
                }, c00251) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                bookFile = (File) c00251.L$0;
                ResultKt.throwOnFailure($result);
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        return bookFile;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object getAllContents(Book book, String bookSourceString, String userNameSpace, Function2<? super String, ? super ArrayList<Triple<String, Integer, String>>, Unit> append, Continuation<? super Unit> $completion) {
        C00271 c00271;
        Object localChapterList$default;
        if ($completion instanceof C00271) {
            c00271 = (C00271) $completion;
            if ((c00271.label & Integer.MIN_VALUE) != 0) {
                c00271.label -= Integer.MIN_VALUE;
            } else {
                c00271 = new C00271($completion);
            }
        }
        Object $result = c00271.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00271.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String qy = book.getName() + "\n作者：" + book.getRealAuthor() + "\n简介：" + HtmlFormatter.format$default(HtmlFormatter.INSTANCE, book.getDisplayIntro(), null, 2, null);
                append.invoke(qy, (Object) null);
                c00271.L$0 = this;
                c00271.L$1 = book;
                c00271.L$2 = userNameSpace;
                c00271.L$3 = append;
                c00271.label = 1;
                localChapterList$default = getLocalChapterList$default(this, book, bookSourceString, false, userNameSpace, false, null, c00271, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                append = (Function2) c00271.L$3;
                userNameSpace = (String) c00271.L$2;
                book = (Book) c00271.L$1;
                this = (BookController) c00271.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Iterable chapterList = (List) localChapterList$default;
        File localCacheDir = this.getChapterCacheDir(book, userNameSpace);
        Iterable $this$forEachIndexed$iv = chapterList;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int i = index$iv;
            index$iv++;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            BookChapter chapter = (BookChapter) item$iv;
            int index = Boxing.boxInt(i).intValue();
            File chapterCacheFile = new File(localCacheDir.getAbsolutePath() + ((Object) File.separator) + index + ".txt");
            String content = PackageDocumentBase.PREFIX_OPF;
            if (!this.getAppConfig().getExportNoChapterName()) {
                content = content + chapter.getTitle() + '\n';
            }
            append.invoke(Intrinsics.stringPlus("\n\n", chapterCacheFile.exists() ? content + FilesKt.readText$default(chapterCacheFile, (Charset) null, 1, (Object) null) + '\n' : Intrinsics.stringPlus(content, "暂无缓存内容。\n")), (Object) null);
        }
        return Unit.INSTANCE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0194  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object exportToEpub(File exportDir, Book book, String bookSource, String userNameSpace, Continuation<? super File> $completion) throws IOException {
        C00241 c00241;
        EpubBook epubBook;
        File bookFile;
        String contentModel;
        if ($completion instanceof C00241) {
            c00241 = (C00241) $completion;
            if ((c00241.label & Integer.MIN_VALUE) != 0) {
                c00241.label -= Integer.MIN_VALUE;
            } else {
                c00241 = new C00241($completion);
            }
        }
        Object $result = c00241.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00241.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String filename = (char) 12298 + book.getName() + "》作者：" + book.getRealAuthor() + ".epub";
                String bookPath = FileUtils.INSTANCE.getPath(exportDir, filename);
                bookFile = FileUtils.INSTANCE.createFileWithReplace(bookPath);
                epubBook = new EpubBook();
                epubBook.setVersion("2.0");
                setEpubMetadata(book, epubBook);
                c00241.L$0 = this;
                c00241.L$1 = book;
                c00241.L$2 = bookSource;
                c00241.L$3 = userNameSpace;
                c00241.L$4 = bookFile;
                c00241.L$5 = epubBook;
                c00241.label = 1;
                if (setCover(book, epubBook, bookSource, c00241) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                contentModel = this.setAssets(book, epubBook);
                c00241.L$0 = bookFile;
                c00241.L$1 = epubBook;
                c00241.L$2 = null;
                c00241.L$3 = null;
                c00241.L$4 = null;
                c00241.L$5 = null;
                c00241.label = 2;
                if (this.setEpubContent(contentModel, book, epubBook, bookSource, userNameSpace, c00241) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                new EpubWriter().write(epubBook, new FileOutputStream(bookFile));
                return bookFile;
            case 1:
                epubBook = (EpubBook) c00241.L$5;
                bookFile = (File) c00241.L$4;
                userNameSpace = (String) c00241.L$3;
                bookSource = (String) c00241.L$2;
                book = (Book) c00241.L$1;
                this = (BookController) c00241.L$0;
                ResultKt.throwOnFailure($result);
                contentModel = this.setAssets(book, epubBook);
                c00241.L$0 = bookFile;
                c00241.L$1 = epubBook;
                c00241.L$2 = null;
                c00241.L$3 = null;
                c00241.L$4 = null;
                c00241.L$5 = null;
                c00241.label = 2;
                if (this.setEpubContent(contentModel, book, epubBook, bookSource, userNameSpace, c00241) == coroutine_suspended) {
                }
                new EpubWriter().write(epubBook, new FileOutputStream(bookFile));
                return bookFile;
            case 2:
                epubBook = (EpubBook) c00241.L$1;
                bookFile = (File) c00241.L$0;
                ResultKt.throwOnFailure($result);
                new EpubWriter().write(epubBook, new FileOutputStream(bookFile));
                return bookFile;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    private final String setAssets(Book book, EpubBook epubBook) {
        Resources resources = epubBook.getResources();
        URL resource = BookController.class.getResource("/epub/fonts.css");
        Intrinsics.checkNotNullExpressionValue(resource, "BookController::class.java.getResource(\"/epub/fonts.css\")");
        resources.add(new Resource(TextStreamsKt.readBytes(resource), "Styles/fonts.css"));
        Resources resources2 = epubBook.getResources();
        URL resource2 = BookController.class.getResource("/epub/main.css");
        Intrinsics.checkNotNullExpressionValue(resource2, "BookController::class.java.getResource(\"/epub/main.css\")");
        resources2.add(new Resource(TextStreamsKt.readBytes(resource2), "Styles/main.css"));
        Resources resources3 = epubBook.getResources();
        URL resource3 = BookController.class.getResource("/epub/logo.png");
        Intrinsics.checkNotNullExpressionValue(resource3, "BookController::class.java.getResource(\"/epub/logo.png\")");
        resources3.add(new Resource(TextStreamsKt.readBytes(resource3), "Images/logo.png"));
        String name = book.getName();
        String realAuthor = book.getRealAuthor();
        String displayIntro = book.getDisplayIntro();
        String kind = book.getKind();
        String wordCount = book.getWordCount();
        URL resource4 = BookController.class.getResource("/epub/cover.html");
        Intrinsics.checkNotNullExpressionValue(resource4, "BookController::class.java.getResource(\"/epub/cover.html\")");
        epubBook.addSection("封面", ResourceUtil.createPublicResource(name, realAuthor, displayIntro, kind, wordCount, new String(TextStreamsKt.readBytes(resource4), Charsets.UTF_8), "Text/cover.html"));
        String name2 = book.getName();
        String realAuthor2 = book.getRealAuthor();
        String displayIntro2 = book.getDisplayIntro();
        String kind2 = book.getKind();
        String wordCount2 = book.getWordCount();
        URL resource5 = BookController.class.getResource("/epub/intro.html");
        Intrinsics.checkNotNullExpressionValue(resource5, "BookController::class.java.getResource(\"/epub/intro.html\")");
        epubBook.addSection("简介", ResourceUtil.createPublicResource(name2, realAuthor2, displayIntro2, kind2, wordCount2, new String(TextStreamsKt.readBytes(resource5), Charsets.UTF_8), "Text/intro.html"));
        URL resource6 = BookController.class.getResource("/epub/chapter.html");
        Intrinsics.checkNotNullExpressionValue(resource6, "BookController::class.java.getResource(\"/epub/chapter.html\")");
        return new String(TextStreamsKt.readBytes(resource6), Charsets.UTF_8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object setCover(Book book, EpubBook epubBook, String bookSourceString, Continuation<? super Unit> $completion) {
        C00841 c00841;
        Object byteArrayAwait;
        if ($completion instanceof C00841) {
            c00841 = (C00841) $completion;
            if ((c00841.label & Integer.MIN_VALUE) != 0) {
                c00841.label -= Integer.MIN_VALUE;
            } else {
                c00841 = new C00841($completion);
            }
        }
        Object $result = c00841.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (c00841.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                String coverUrl = book.getDisplayCover();
                if (coverUrl != null) {
                    if (StringsKt.startsWith$default(coverUrl, TableOfContents.DEFAULT_PATH_SEPARATOR, false, 2, (Object) null)) {
                        String[] strArr = new String[2];
                        strArr[0] = "storage";
                        String str = File.separator;
                        Intrinsics.checkNotNullExpressionValue(str, "separator");
                        String strReplace$default = StringsKt.replace$default(coverUrl, TableOfContents.DEFAULT_PATH_SEPARATOR, str, false, 4, (Object) null);
                        if (strReplace$default == null) {
                            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                        }
                        String strSubstring = strReplace$default.substring(1);
                        Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.String).substring(startIndex)");
                        strArr[1] = strSubstring;
                        File coverFile = new File(ExtKt.getWorkDir(strArr));
                        byte[] byteArray = FilesKt.readBytes(coverFile);
                        epubBook.setCoverImage(new Resource(byteArray, "Images/cover.jpg"));
                    } else if (bookSourceString != null) {
                        String ext = getFileExt(coverUrl, "jpg");
                        String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                        String cachePath = ExtKt.getWorkDir("storage", "cache", md5Encode + '.' + ext);
                        File cacheFile = new File(cachePath);
                        if (cacheFile.exists()) {
                            byte[] byteArray2 = FilesKt.readBytes(cacheFile);
                            epubBook.setCoverImage(new Resource(byteArray2, "Images/cover.jpg"));
                            return Unit.INSTANCE;
                        }
                        String str2 = null;
                        Integer num = null;
                        String str3 = null;
                        Integer num2 = null;
                        String str4 = null;
                        Object objM151fromJsonIoAF18A = BookSource.INSTANCE.m151fromJsonIoAF18A(bookSourceString);
                        AnalyzeUrl analyzeUrl = new AnalyzeUrl(coverUrl, str2, num, str3, num2, str4, (BaseSource) (Result.isFailure-impl(objM151fromJsonIoAF18A) ? null : objM151fromJsonIoAF18A), null, null, null, null, 1982, null);
                        c00841.L$0 = epubBook;
                        c00841.label = 1;
                        byteArrayAwait = analyzeUrl.getByteArrayAwait(c00841);
                        if (byteArrayAwait == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        byte[] it = (byte[]) byteArrayAwait;
                        epubBook.setCoverImage(new Resource(it, "Images/cover.jpg"));
                    }
                    break;
                }
                return Unit.INSTANCE;
            case 1:
                epubBook = (EpubBook) c00841.L$0;
                ResultKt.throwOnFailure($result);
                byteArrayAwait = $result;
                byte[] it2 = (byte[]) byteArrayAwait;
                epubBook.setCoverImage(new Resource(it2, "Images/cover.jpg"));
                return Unit.INSTANCE;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object setEpubContent(String contentModel, Book book, EpubBook epubBook, String bookSourceString, String userNameSpace, Continuation<? super Unit> $completion) {
        C00851 c00851;
        Object localChapterList$default;
        String content;
        if ($completion instanceof C00851) {
            c00851 = (C00851) $completion;
            if ((c00851.label & Integer.MIN_VALUE) != 0) {
                c00851.label -= Integer.MIN_VALUE;
            } else {
                c00851 = new C00851($completion);
            }
        }
        Object $result = c00851.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00851.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                c00851.L$0 = this;
                c00851.L$1 = contentModel;
                c00851.L$2 = book;
                c00851.L$3 = epubBook;
                c00851.L$4 = userNameSpace;
                c00851.label = 1;
                localChapterList$default = getLocalChapterList$default(this, book, bookSourceString, false, userNameSpace, false, null, c00851, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                userNameSpace = (String) c00851.L$4;
                epubBook = (EpubBook) c00851.L$3;
                book = (Book) c00851.L$2;
                contentModel = (String) c00851.L$1;
                this = (BookController) c00851.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        Iterable chapterList = (List) localChapterList$default;
        File localCacheDir = this.getChapterCacheDir(book, userNameSpace);
        Iterable $this$forEachIndexed$iv = chapterList;
        int index$iv = 0;
        for (Object item$iv : $this$forEachIndexed$iv) {
            int i = index$iv;
            index$iv++;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            BookChapter chapter = (BookChapter) item$iv;
            int index = Boxing.boxInt(i).intValue();
            String content2 = PackageDocumentBase.PREFIX_OPF;
            if (!this.getAppConfig().getExportNoChapterName()) {
                content2 = content2 + chapter.getTitle() + '\n';
            }
            if (book.isLocalTxt()) {
                String str = content2;
                String content3 = LocalBook.INSTANCE.getContent(book, chapter);
                content = Intrinsics.stringPlus(str, content3 == null ? PackageDocumentBase.PREFIX_OPF : content3);
            } else {
                File chapterCacheFile = new File(localCacheDir.getAbsolutePath() + ((Object) File.separator) + index + ".txt");
                if (chapterCacheFile.exists()) {
                    content = content2 + FilesKt.readText$default(chapterCacheFile, (Charset) null, 1, (Object) null) + '\n';
                } else {
                    content = Intrinsics.stringPlus(content2, "暂无缓存内容。\n");
                }
            }
            String content1 = this.fixPic(epubBook, book, content, chapter);
            String title = chapter.getTitle();
            epubBook.addSection(title, ResourceUtil.createChapterResource(StringsKt.replace$default(title, "🔒", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null), content1, contentModel, "Text/chapter_" + index + ".html"));
        }
        return Unit.INSTANCE;
    }

    private final String fixPic(EpubBook epubBook, Book book, String content, BookChapter chapter) {
        StringBuilder data = new StringBuilder(PackageDocumentBase.PREFIX_OPF);
        Iterable $this$forEach$iv = StringsKt.split$default(content, new String[]{"\n"}, false, 0, 6, (Object) null);
        for (Object element$iv : $this$forEach$iv) {
            String text = (String) element$iv;
            String strReplace$default = text;
            Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(text);
            while (matcher.find()) {
                String it = matcher.group(1);
                if (it != null) {
                    String src = NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), it);
                    String originalHref = MD5Utils.INSTANCE.md5Encode16(src) + '.' + BookHelp.INSTANCE.getImageSuffix(src);
                    String href = Intrinsics.stringPlus("Images/", originalHref);
                    File vFile = BookHelp.INSTANCE.getImage(book, src);
                    if (vFile.exists()) {
                        FileResourceProvider fp = new FileResourceProvider(vFile.getParent());
                        LazyResource img = new LazyResource(fp, href, originalHref);
                        epubBook.getResources().add(img);
                        strReplace$default = StringsKt.replace$default(strReplace$default, it, Intrinsics.stringPlus("../", href), false, 4, (Object) null);
                    }
                }
            }
            data.append(strReplace$default).append("\n");
        }
        String string = data.toString();
        Intrinsics.checkNotNullExpressionValue(string, "data.toString()");
        return string;
    }

    private final void setEpubMetadata(Book book, EpubBook epubBook) {
        me.ag2s.epublib.domain.Metadata metadata = new me.ag2s.epublib.domain.Metadata();
        metadata.getTitles().add(book.getName());
        metadata.getAuthors().add(new Author(book.getRealAuthor()));
        metadata.setLanguage("zh");
        metadata.getDates().add(new Date());
        metadata.getPublishers().add("Legado");
        metadata.getDescriptions().add(book.getDisplayIntro());
        epubBook.setMetadata(metadata);
    }

    /* JADX WARN: Code restructure failed: missing block: B:105:0x04db, code lost:

        if (r28 < r29) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x04de, code lost:

        r0 = r28;
        r28 = r28 + 1;
        r27 = r0;
        r0 = (io.legado.app.data.entities.BookChapter) r24.get(r0);
        r34.L$0 = r13;
        r34.L$1 = r16;
        r34.L$2 = r18;
        r34.L$3 = r22;
        r34.L$4 = r24;
        r34.L$5 = r25;
        r34.L$6 = r26;
        r34.I$0 = r20;
        r34.I$1 = r27;
        r34.I$2 = r28;
        r34.I$3 = r29;
        r34.label = 4;
        r0 = r13.searchChapter(r22, r0, r18, r34);
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0557, code lost:

        if (r0 != r0) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x055c, code lost:

        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x05fb, code lost:

        if (r28 < r29) goto L106;
     */
    /* JADX WARN: Path cross not found for [B:84:0x03a5, B:86:0x03af], limit reached: 124 */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0478  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x0480  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x03b7  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x03cb  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x0420  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:120:0x05fb -> B:106:0x04de). Please report as a decompilation issue!!! */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchBookContent(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00661 c00661;
        int size;
        int i;
        int currentIndex;
        int size2;
        List resultList;
        Ref.BooleanRef isEnd;
        List chapterList;
        Book bookInfo;
        String keyword;
        ReturnData returnData;
        int lastIndex;
        Object localChapterList$default;
        String userNameSpace;
        Object bookSourceString$default;
        Object objCheckAuth;
        String bookSource;
        String str;
        String bookUrl;
        Integer numBoxInt;
        Integer numBoxInt2;
        if ($completion instanceof C00661) {
            c00661 = (C00661) $completion;
            if ((c00661.label & Integer.MIN_VALUE) != 0) {
                c00661.label -= Integer.MIN_VALUE;
            } else {
                c00661 = new C00661($completion);
            }
        }
        Object $result = c00661.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00661.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00661.L$0 = this;
                c00661.L$1 = context;
                c00661.L$2 = returnData;
                c00661.label = 1;
                objCheckAuth = checkAuth(context, c00661);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) objCheckAuth).booleanValue()) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                }
                if (context.request().method() == HttpMethod.POST) {
                    String string = context.getBodyAsJson().getString(RSSKeywords.RSS_ITEM_URL);
                    String string2 = string == null ? context.getBodyAsJson().getString("bookUrl") : string;
                    bookUrl = string2 == null ? PackageDocumentBase.PREFIX_OPF : string2;
                    String string3 = context.getBodyAsJson().getString("keyword");
                    keyword = string3 == null ? PackageDocumentBase.PREFIX_OPF : string3;
                    Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(0));
                    Intrinsics.checkNotNullExpressionValue(integer, "context.bodyAsJson.getInteger(\"lastIndex\", 0)");
                    lastIndex = integer.intValue();
                    Integer integer2 = context.getBodyAsJson().getInteger("size", Boxing.boxInt(20));
                    Intrinsics.checkNotNullExpressionValue(integer2, "context.bodyAsJson.getInteger(\"size\", 20)");
                    size2 = integer2.intValue();
                } else {
                    List listQueryParam = context.queryParam(RSSKeywords.RSS_ITEM_URL);
                    Intrinsics.checkNotNullExpressionValue(listQueryParam, "context.queryParam(\"url\")");
                    String str2 = (String) CollectionsKt.firstOrNull(listQueryParam);
                    bookUrl = str2 == null ? PackageDocumentBase.PREFIX_OPF : str2;
                    List listQueryParam2 = context.queryParam("keyword");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam2, "context.queryParam(\"keyword\")");
                    String str3 = (String) CollectionsKt.firstOrNull(listQueryParam2);
                    keyword = str3 == null ? PackageDocumentBase.PREFIX_OPF : str3;
                    List listQueryParam3 = context.queryParam("lastIndex");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam3, "context.queryParam(\"lastIndex\")");
                    String str4 = (String) CollectionsKt.firstOrNull(listQueryParam3);
                    int iIntValue = (str4 == null || (numBoxInt = Boxing.boxInt(Integer.parseInt(str4))) == null) ? 0 : numBoxInt.intValue();
                    lastIndex = iIntValue;
                    List listQueryParam4 = context.queryParam("size");
                    Intrinsics.checkNotNullExpressionValue(listQueryParam4, "context.queryParam(\"size\")");
                    String str5 = (String) CollectionsKt.firstOrNull(listQueryParam4);
                    int iIntValue2 = (str5 == null || (numBoxInt2 = Boxing.boxInt(Integer.parseInt(str5))) == null) ? 20 : numBoxInt2.intValue();
                    size2 = iIntValue2;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("请输入书籍链接");
                }
                if (keyword.length() == 0) {
                    return returnData.setErrorMsg("请输入搜索关键词");
                }
                userNameSpace = this.getUserNameSpace(context);
                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo == null) {
                    return returnData.setErrorMsg("请先加入书架");
                }
                bookSource = null;
                if (!bookInfo.isLocalBook()) {
                    c00661.L$0 = this;
                    c00661.L$1 = context;
                    c00661.L$2 = returnData;
                    c00661.L$3 = keyword;
                    c00661.L$4 = userNameSpace;
                    c00661.L$5 = bookInfo;
                    c00661.I$0 = lastIndex;
                    c00661.I$1 = size2;
                    c00661.label = 2;
                    bookSourceString$default = getBookSourceString$default(this, context, bookInfo.getOrigin(), false, c00661, 4, null);
                    if (bookSourceString$default == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    bookSource = (String) bookSourceString$default;
                    str = bookSource;
                    if (str != null || str.length() == 0) {
                        return returnData.setErrorMsg("未配置书源");
                    }
                }
                BookController bookController = this;
                Book book = bookInfo;
                String str6 = bookSource;
                String str7 = str6 != null ? PackageDocumentBase.PREFIX_OPF : str6;
                c00661.L$0 = this;
                c00661.L$1 = context;
                c00661.L$2 = returnData;
                c00661.L$3 = keyword;
                c00661.L$4 = bookInfo;
                c00661.L$5 = null;
                c00661.I$0 = lastIndex;
                c00661.I$1 = size2;
                c00661.label = 3;
                localChapterList$default = getLocalChapterList$default(bookController, book, str7, false, userNameSpace, false, null, c00661, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                    return coroutine_suspended;
                }
                chapterList = (List) localChapterList$default;
                if (lastIndex >= chapterList.size()) {
                    return returnData.setErrorMsg("没有更多了");
                }
                isEnd = new Ref.BooleanRef();
                BookController bookController2 = this;
                context.request().connection().closeHandler((v2) -> {
                    m24searchBookContent$lambda30(r1, r2, v2);
                });
                BookControllerKt.logger.info("searchBookContent keyword: {} lastIndex: {}", keyword, Boxing.boxInt(lastIndex));
                resultList = new ArrayList();
                int i2 = lastIndex + 1;
                currentIndex = i2;
                i = i2;
                size = chapterList.size();
                break;
            case 1:
                returnData = (ReturnData) c00661.L$2;
                context = (RoutingContext) c00661.L$1;
                this = (BookController) c00661.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                if (((Boolean) objCheckAuth).booleanValue()) {
                }
                break;
            case 2:
                size2 = c00661.I$1;
                lastIndex = c00661.I$0;
                bookInfo = (Book) c00661.L$5;
                userNameSpace = (String) c00661.L$4;
                keyword = (String) c00661.L$3;
                returnData = (ReturnData) c00661.L$2;
                context = (RoutingContext) c00661.L$1;
                this = (BookController) c00661.L$0;
                ResultKt.throwOnFailure($result);
                bookSourceString$default = $result;
                bookSource = (String) bookSourceString$default;
                str = bookSource;
                if (str != null) {
                }
                if (str != null || str.length() == 0) {
                }
                BookController bookController3 = this;
                Book book2 = bookInfo;
                String str62 = bookSource;
                if (str62 != null) {
                }
                c00661.L$0 = this;
                c00661.L$1 = context;
                c00661.L$2 = returnData;
                c00661.L$3 = keyword;
                c00661.L$4 = bookInfo;
                c00661.L$5 = null;
                c00661.I$0 = lastIndex;
                c00661.I$1 = size2;
                c00661.label = 3;
                localChapterList$default = getLocalChapterList$default(bookController3, book2, str7, false, userNameSpace, false, null, c00661, 48, null);
                if (localChapterList$default == coroutine_suspended) {
                }
                chapterList = (List) localChapterList$default;
                if (lastIndex >= chapterList.size()) {
                }
                break;
            case 3:
                size2 = c00661.I$1;
                lastIndex = c00661.I$0;
                bookInfo = (Book) c00661.L$4;
                keyword = (String) c00661.L$3;
                returnData = (ReturnData) c00661.L$2;
                context = (RoutingContext) c00661.L$1;
                this = (BookController) c00661.L$0;
                ResultKt.throwOnFailure($result);
                localChapterList$default = $result;
                chapterList = (List) localChapterList$default;
                if (lastIndex >= chapterList.size()) {
                }
                break;
            case 4:
                size = c00661.I$3;
                i = c00661.I$2;
                currentIndex = c00661.I$1;
                size2 = c00661.I$0;
                resultList = (List) c00661.L$6;
                isEnd = (Ref.BooleanRef) c00661.L$5;
                chapterList = (List) c00661.L$4;
                bookInfo = (Book) c00661.L$3;
                keyword = (String) c00661.L$2;
                returnData = (ReturnData) c00661.L$1;
                this = (BookController) c00661.L$0;
                ResultKt.throwOnFailure($result);
                Object objSearchChapter = $result;
                List chapterResult = (List) objSearchChapter;
                if (chapterResult.size() > 0) {
                    resultList.addAll(chapterResult);
                }
                if (resultList.size() < size2) {
                    if (!isEnd.element) {
                    }
                    break;
                }
                return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[]{TuplesKt.to("list", resultList), TuplesKt.to("lastIndex", Boxing.boxInt(currentIndex))}), null, 2, null);
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: renamed from: searchBookContent$lambda-30, reason: not valid java name */
    private static final void m24searchBookContent$lambda30(Ref.BooleanRef $isEnd, BookController this$0, Void it) {
        Intrinsics.checkNotNullParameter($isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        BookControllerKt.logger.info("客户端已断开链接，停止 searchBookContent");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException) null, 1, (Object) null);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x011f  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0029  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object searchChapter(@NotNull Book book, @NotNull BookChapter chapter, @NotNull String query, @NotNull Continuation<? super List<SearchResult>> $completion) {
        C00811 c00811;
        String chapterContent;
        List searchResultsWithinChapter;
        Object objSearchPosition;
        if ($completion instanceof C00811) {
            c00811 = (C00811) $completion;
            if ((c00811.label & Integer.MIN_VALUE) != 0) {
                c00811.label -= Integer.MIN_VALUE;
            } else {
                c00811 = new C00811($completion);
            }
        }
        Object $result = c00811.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00811.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                searchResultsWithinChapter = new ArrayList();
                chapterContent = BookHelp.INSTANCE.getContent(book, chapter);
                if (chapterContent != null) {
                    c00811.L$0 = this;
                    c00811.L$1 = chapter;
                    c00811.L$2 = query;
                    c00811.L$3 = searchResultsWithinChapter;
                    c00811.L$4 = chapterContent;
                    c00811.label = 1;
                    objSearchPosition = searchPosition(chapterContent, query, c00811);
                    if (objSearchPosition == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    Iterable positions = (List) objSearchPosition;
                    BookControllerKt.logger.info("positions: {}", positions);
                    Iterable $this$forEachIndexed$iv = positions;
                    int index$iv = 0;
                    for (Object item$iv : $this$forEachIndexed$iv) {
                        int i = index$iv;
                        index$iv++;
                        if (i < 0) {
                            CollectionsKt.throwIndexOverflow();
                        }
                        Integer numBoxInt = Boxing.boxInt(i);
                        int position = ((Number) item$iv).intValue();
                        int index = numBoxInt.intValue();
                        Pair<Integer, String> resultAndQueryIndex = this.getResultAndQueryIndex(chapterContent, position, query);
                        SearchResult result = new SearchResult(0, index, (String) resultAndQueryIndex.getSecond(), chapter.getTitle(), query, 0, chapter.getIndex(), 0, ((Number) resultAndQueryIndex.getFirst()).intValue(), position, 161, null);
                        searchResultsWithinChapter.add(result);
                    }
                }
                return searchResultsWithinChapter;
            case 1:
                chapterContent = (String) c00811.L$4;
                searchResultsWithinChapter = (List) c00811.L$3;
                query = (String) c00811.L$2;
                chapter = (BookChapter) c00811.L$1;
                this = (BookController) c00811.L$0;
                ResultKt.throwOnFailure($result);
                objSearchPosition = $result;
                Iterable positions2 = (List) objSearchPosition;
                BookControllerKt.logger.info("positions: {}", positions2);
                Iterable $this$forEachIndexed$iv2 = positions2;
                int index$iv2 = 0;
                while (r0.hasNext()) {
                }
                return searchResultsWithinChapter;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object searchPosition(String mContent, String pattern, Continuation<? super List<Integer>> $completion) {
        List position = new ArrayList();
        int index = StringsKt.indexOf$default(mContent, pattern, 0, false, 6, (Object) null);
        if (index >= 0) {
            while (index >= 0) {
                position.add(Boxing.boxInt(index));
                index = StringsKt.indexOf$default(mContent, pattern, index + 1, false, 4, (Object) null);
            }
        }
        return position;
    }

    private final Pair<Integer, String> getResultAndQueryIndex(String content, int queryIndexInContent, String query) {
        int po1 = queryIndexInContent - 20;
        int po2 = queryIndexInContent + query.length() + 20;
        if (po1 < 0) {
            po1 = 0;
        }
        if (po2 > content.length()) {
            po2 = content.length();
        }
        int queryIndexInResult = queryIndexInContent - po1;
        if (content == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String newText = content.substring(po1, po2);
        Intrinsics.checkNotNullExpressionValue(newText, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return TuplesKt.to(Integer.valueOf(queryIndexInResult), newText);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object backupToMongodb(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00121 c00121;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00121) {
            c00121 = (C00121) $completion;
            if ((c00121.label & Integer.MIN_VALUE) != 0) {
                c00121.label -= Integer.MIN_VALUE;
            } else {
                c00121 = new C00121($completion);
            }
        }
        Object $result = c00121.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00121.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00121.L$0 = this;
                c00121.L$1 = context;
                c00121.L$2 = returnData;
                c00121.label = 1;
                objCheckAuth = checkAuth(context, c00121);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00121.L$2;
                context = (RoutingContext) c00121.L$1;
                this = (BookController) c00121.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (!MongoManager.INSTANCE.isInit()) {
            return returnData.setErrorMsg("请先设置 mongoUri");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
        }
        String[] backupFileNames = this.getBackupFileNames();
        final ArrayList syncDataFileList = CollectionsKt.arrayListOf(Arrays.copyOf(backupFileNames, backupFileNames.length));
        final BookController bookController = this;
        Function1<String, Unit> function1 = new Function1<String, Unit>() { // from class: com.htmake.reader.api.controller.BookController$backupToMongodb$handler$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                invoke((String) p1);
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull String userNameSpace) {
                Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
                Iterable $this$forEach$iv = syncDataFileList;
                BookController bookController2 = bookController;
                for (Object element$iv : $this$forEach$iv) {
                    String it = (String) element$iv;
                    String content = bookController2.getUserStorage(userNameSpace, it);
                    if (content != null) {
                        bookController2.saveUserStorage(userNameSpace, it, content);
                    }
                }
            }
        };
        function1.invoke("default");
        if (this.getAppConfig().getSecure()) {
            Map userMap = new LinkedHashMap();
            JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
            if (userMapJson != null) {
                Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap(map);
            }
            Map $this$forEach$iv = userMap;
            for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                try {
                    String str = (String) ((Map) element$iv.getValue()).getOrDefault("username", PackageDocumentBase.PREFIX_OPF);
                    String ns = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    if (ns.length() > 0) {
                        function1.invoke(ns);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        String content = ExtKt.getStorage$default(new String[]{"users"}, null, 2, null);
        if (content != null) {
            ExtKt.saveStorage$default(new String[]{"users"}, content, false, null, 12, null);
        }
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object restoreFromMongodb(@NotNull RoutingContext context, @NotNull Continuation<? super ReturnData> $completion) {
        C00521 c00521;
        ReturnData returnData;
        Object objCheckAuth;
        if ($completion instanceof C00521) {
            c00521 = (C00521) $completion;
            if ((c00521.label & Integer.MIN_VALUE) != 0) {
                c00521.label -= Integer.MIN_VALUE;
            } else {
                c00521 = new C00521($completion);
            }
        }
        Object $result = c00521.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (c00521.label) {
            case 0:
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                c00521.L$0 = this;
                c00521.L$1 = context;
                c00521.L$2 = returnData;
                c00521.label = 1;
                objCheckAuth = checkAuth(context, c00521);
                if (objCheckAuth == coroutine_suspended) {
                    return coroutine_suspended;
                }
                break;
            case 1:
                returnData = (ReturnData) c00521.L$2;
                context = (RoutingContext) c00521.L$1;
                this = (BookController) c00521.L$0;
                ResultKt.throwOnFailure($result);
                objCheckAuth = $result;
                break;
            default:
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) objCheckAuth).booleanValue()) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
        }
        if (!MongoManager.INSTANCE.isInit()) {
            return returnData.setErrorMsg("请先设置 mongoUri");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
        }
        String[] backupFileNames = this.getBackupFileNames();
        final ArrayList syncDataFileList = CollectionsKt.arrayListOf(Arrays.copyOf(backupFileNames, backupFileNames.length));
        Function1<String, Unit> function1 = new Function1<String, Unit>() { // from class: com.htmake.reader.api.controller.BookController$restoreFromMongodb$handler$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            public /* bridge */ /* synthetic */ Object invoke(Object p1) {
                invoke((String) p1);
                return Unit.INSTANCE;
            }

            public final void invoke(@NotNull String userNameSpace) {
                Intrinsics.checkNotNullParameter(userNameSpace, "userNameSpace");
                Iterable $this$forEach$iv = syncDataFileList;
                for (Object element$iv : $this$forEach$iv) {
                    String it = (String) element$iv;
                    File file = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, Intrinsics.stringPlus(it, ".json")));
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        };
        function1.invoke("default");
        if (this.getAppConfig().getSecure()) {
            Map userMap = new LinkedHashMap();
            JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[]{"data", "users"}, null, 2, null));
            if (userMapJson != null) {
                Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap(map);
            }
            Map $this$forEach$iv = userMap;
            for (Map.Entry element$iv : $this$forEach$iv.entrySet()) {
                try {
                    String str = (String) ((Map) element$iv.getValue()).getOrDefault("username", PackageDocumentBase.PREFIX_OPF);
                    String ns = str == null ? PackageDocumentBase.PREFIX_OPF : str;
                    if (ns.length() > 0) {
                        function1.invoke(ns);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        File usersFile = new File(ExtKt.getWorkDir("storage", "users.json"));
        if (usersFile.exists()) {
            usersFile.delete();
            ExtKt.getStorage$default(new String[]{"users"}, null, 2, null);
        }
        return ReturnData.setData$default(returnData, PackageDocumentBase.PREFIX_OPF, null, 2, null);
    }
}
