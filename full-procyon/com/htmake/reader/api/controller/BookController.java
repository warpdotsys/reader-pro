// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.api.controller;

import com.htmake.reader.utils.MongoManager;
import io.legado.app.data.entities.SearchResult;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.Author;
import me.ag2s.epublib.domain.LazyResource;
import me.ag2s.epublib.domain.LazyResourceProvider;
import me.ag2s.epublib.domain.FileResourceProvider;
import java.net.URL;
import me.ag2s.epublib.domain.Resources;
import me.ag2s.epublib.util.ResourceUtil;
import kotlin.text.Charsets;
import me.ag2s.epublib.domain.Resource;
import kotlin.io.TextStreamsKt;
import java.io.FileOutputStream;
import java.io.OutputStream;
import me.ag2s.epublib.epub.EpubWriter;
import me.ag2s.epublib.domain.EpubBook;
import kotlin.Triple;
import kotlin.jvm.internal.TypeIntrinsics;
import java.util.function.BiConsumer;
import io.vertx.core.http.CaseInsensitiveHeaders;
import okhttp3.ResponseBody;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.mozilla.javascript.WrappedException;
import com.script.ScriptException;
import java.util.concurrent.CancellationException;
import kotlin.text.Regex;
import io.legado.app.exception.NoStackTraceException;
import kotlinx.coroutines.JobKt;
import okhttp3.Response;
import io.legado.app.model.Debug;
import kotlin.io.ByteStreamsKt;
import java.io.InputStream;
import io.legado.app.data.entities.HttpTTS;
import java.util.Base64;
import com.htmake.reader.lib.tts.constant.TtsStyleEnum;
import com.htmake.reader.lib.tts.model.SSML;
import com.htmake.reader.lib.tts.service.TTSService;
import com.htmake.reader.lib.tts.constant.VoiceEnum;
import java.util.LinkedHashSet;
import io.legado.app.model.Debugger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import io.legado.app.utils.ZipUtils;
import java.awt.Graphics2D;
import java.awt.Image;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPage;
import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import org.apache.pdfbox.rendering.ImageType;
import java.awt.Dimension;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.htmake.reader.utils.UserMutex;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.JsonFactory;
import kotlin.io.CloseableKt;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import java.io.Closeable;
import com.fasterxml.jackson.databind.ObjectMapper;
import kotlinx.coroutines.sync.Mutex$DefaultImpls;
import io.legado.app.model.analyzeRule.AnalyzeRule;
import kotlinx.coroutines.sync.MutexKt;
import kotlin.jvm.internal.Ref$LongRef;
import kotlin.jvm.internal.Ref$FloatRef;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.data.entities.BaseSource;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.kotlin.coroutines.VertxCoroutineKt;
import com.htmake.reader.entity.User;
import io.vertx.core.json.JsonArray;
import kotlin.jvm.functions.Function3;
import java.util.LinkedHashMap;
import kotlin.jvm.internal.Ref$BooleanRef;
import kotlin.jvm.internal.Ref$IntRef;
import kotlin.jvm.internal.Ref$ObjectRef;
import io.legado.app.data.entities.SearchBook;
import java.util.regex.Matcher;
import io.legado.app.utils.NetworkUtils;
import io.vertx.core.json.JsonObject;
import java.util.Locale;
import com.google.gson.reflect.TypeToken;
import io.legado.app.data.entities.rule.ContentRule;
import io.legado.app.data.entities.rule.TocRule;
import io.legado.app.data.entities.rule.BookInfoRule;
import io.legado.app.data.entities.rule.SearchRule;
import io.legado.app.data.entities.rule.ExploreRule;
import io.legado.app.help.BookHelp;
import java.nio.charset.Charset;
import io.legado.app.model.DebugLog;
import io.legado.app.model.webBook.WebBook;
import kotlinx.coroutines.sync.Mutex;
import io.legado.app.data.entities.BookChapter;
import kotlin.jvm.functions.Function1;
import io.vertx.core.http.HttpMethod;
import kotlin.Result$Companion;
import com.google.gson.Gson;
import io.legado.app.utils.ParameterizedTypeImpl;
import io.legado.app.data.entities.TxtTocRule;
import java.lang.reflect.Type;
import kotlin.Result;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.help.DefaultData;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import io.legado.app.exception.TocEmptyException;
import kotlin.collections.MapsKt;
import kotlin.TuplesKt;
import kotlin.Pair;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.data.entities.Book;
import kotlin.io.FilesKt;
import kotlin.text.StringsKt;
import java.nio.file.Paths;
import io.legado.app.constant.AppPattern;
import io.legado.app.utils.FileUtils;
import io.vertx.ext.web.FileUpload;
import kotlinx.coroutines.Job;
import io.vertx.core.http.HttpServerResponse;
import java.util.List;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.BuildersKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.Dispatchers;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlinx.coroutines.slf4j.MDCContext;
import kotlinx.coroutines.CoroutineScope;
import kotlin.coroutines.CoroutineContext$Key;
import kotlinx.coroutines.CoroutineExceptionHandler$Key;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlinx.coroutines.CoroutineExceptionHandler;
import io.legado.app.utils.MD5Utils;
import kotlin.collections.CollectionsKt;
import kotlin.Unit;
import org.jetbrains.annotations.Nullable;
import kotlin.coroutines.jvm.internal.Boxing;
import java.util.ArrayList;
import kotlin.ResultKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import com.htmake.reader.api.ReturnData;
import kotlin.coroutines.Continuation;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import io.legado.app.data.entities.BookSource;
import java.io.File;
import com.htmake.reader.utils.ExtKt;
import com.htmake.reader.utils.SpringContextUtils;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.coroutines.CoroutineContext;
import kotlin.Lazy;
import io.vertx.ext.web.client.WebClient;
import org.jetbrains.annotations.NotNull;
import io.legado.app.utils.ACache;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u008a\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010#\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003?\u0006\u0002\u0010\u0004J\u0019\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J,\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00072\u0012\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010\u001e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u0010\u001f\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J!\u0010 \u001a\u00020\u00182\u0006\u0010!\u001a\u00020\"2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010#J\u0019\u0010 \u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u0010$\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J \u0010%\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u000f2\b\b\u0002\u0010)\u001a\u00020*J\u0018\u0010+\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J/\u0010,\u001a\u0004\u0018\u00010-2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u00072\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u00100J\u0019\u00101\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u00102\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u00103\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J7\u00104\u001a\u0004\u0018\u00010'2\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\u0012\u00105\u001a\u000e\u0012\u0004\u0012\u00020'\u0012\u0004\u0012\u00020'06H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u00107J\u0019\u00108\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u00109\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J3\u0010:\u001a\u00020-2\u0006\u0010;\u001a\u00020-2\u0006\u0010&\u001a\u00020'2\b\u0010<\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010=J1\u0010>\u001a\u00020-2\u0006\u0010;\u001a\u00020-2\u0006\u0010?\u001a\u00020'2\u0006\u0010<\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010=J\u0018\u0010@\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J\u0018\u0010A\u001a\u00020*2\u0006\u0010&\u001a\u00020'2\b\b\u0002\u0010)\u001a\u00020*J(\u0010B\u001a\u00020\u00072\u0006\u0010C\u001a\u00020D2\u0006\u0010&\u001a\u00020'2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010F\u001a\u00020GH\u0002J\u0099\u0001\u0010H\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010I\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u00072n\u0010J\u001aj\u0012\u0013\u0012\u00110\u0007?\u0006\f\bL\u0012\b\bM\u0012\u0004\b\b(N\u0012K\u0012I\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070P\u0018\u00010Oj\u001c\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070P\u0018\u0001`Q?\u0006\f\bL\u0012\b\bM\u0012\u0004\b\b(R\u0012\u0004\u0012\u00020\u00180KH\u0082@\u00f8\u0001\u0000?\u0006\u0002\u0010SJ\u0019\u0010T\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0010\u0010U\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010V\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u0010W\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u0010X\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J)\u0010Y\u001a\b\u0012\u0004\u0012\u00020'0Z2\b\b\u0002\u0010[\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\\J/\u0010]\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0014\u001a\u00020\u00152\b\b\u0002\u0010\u0019\u001a\u00020\u00072\b\b\u0002\u0010^\u001a\u00020*H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010_J\u0018\u0010`\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010a\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001c\u0010b\u001a\b\u0012\u0004\u0012\u00020\u000f0c2\u0006\u0010?\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007J\u0016\u0010d\u001a\u00020-2\u0006\u0010?\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010e\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0019\u0010f\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0018\u0010g\u001a\u0004\u0018\u00010h2\u0006\u0010M\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0010\u0010i\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u0019\u0010j\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001b\u0010k\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010lJQ\u0010m\u001a\b\u0012\u0004\u0012\u00020G0Z2\u0006\u0010&\u001a\u00020'2\b\u0010<\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010[\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00072\b\b\u0002\u0010n\u001a\u00020*2\n\b\u0002\u0010o\u001a\u0004\u0018\u00010pH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010qJ,\u0010r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00070s2\u0006\u0010E\u001a\u00020\u00072\u0006\u0010t\u001a\u00020\u000f2\u0006\u0010u\u001a\u00020\u0007H\u0002J\u0019\u0010v\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u0018\u0010w\u001a\u0004\u0018\u00010'2\u0006\u0010x\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007J\u0019\u0010y\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J+\u0010z\u001a\u0004\u0018\u00010{2\u0006\u0010|\u001a\u00020h2\u0006\u0010}\u001a\u00020\u00072\u0006\u0010~\u001a\u00020\u000fH\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u007fJ\u001a\u0010\u0080\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u0081\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u0082\u0001\u001a\u00020*2\u0007\u0010<\u001a\u00030\u0083\u00012\u0006\u0010\u001d\u001a\u00020\u0007H\u0002J\u001b\u0010\u0084\u0001\u001a\u00020'2\u0006\u0010&\u001a\u00020'H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u0085\u0001J\u001a\u0010\u0086\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u0087\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u0088\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u0089\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u008a\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010\u008b\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J/\u0010\u008c\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010<\u001a\u0004\u0018\u00010\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u008d\u0001J\u001a\u0010\u008e\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J(\u0010\u008f\u0001\u001a\b\u0012\u0004\u0012\u00020'0Z2\r\u0010\u0090\u0001\u001a\b\u0012\u0004\u0012\u00020'0ZH\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u0091\u0001J\u001a\u0010\u0092\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J,\u0010\u0093\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0007\u0010\u0094\u0001\u001a\u00020G2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u0095\u0001J2\u0010\u0096\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u000e\u0010\u0097\u0001\u001a\t\u0012\u0005\u0012\u00030\u0098\u00010Z2\u0006\u0010\u001d\u001a\u00020\u00072\t\b\u0002\u0010\u0099\u0001\u001a\u00020*J.\u0010\u009a\u0001\u001a\u0010\u0012\u0004\u0012\u00020'\u0012\u0006\u0012\u0004\u0018\u00010\u00070s2\u0007\u0010\u009b\u0001\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u0015J#\u0010\u009c\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@\u00f8\u0001\u0000?\u0006\u0003\u0010\u009d\u0001J?\u0010\u009e\u0001\u001a\u00020\u00182\b\u0010\u009f\u0001\u001a\u00030?\u00012\b\u0010?\u0001\u001a\u00030?\u00012\u0006\u0010(\u001a\u00020\u000f2\b\u0010?\u0001\u001a\u00030¡è\u00012\u0007\u0010?\u0001\u001a\u00020\u00072\u0007\u0010?\u0001\u001a\u00020-J>\u0010¡ì\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\r\u0010¡§\u0001\u001a\b\u0012\u0004\u0012\u00020G0Z2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010o\u001a\u0004\u0018\u00010pH\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J,\u0010?\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0007\u0010\u0094\u0001\u001a\u00020G2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u0095\u0001J'\u0010?\u0001\u001a\u00020*2\u0006\u0010\u001d\u001a\u00020\u00072\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J\u001a\u0010\u00ad\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010?\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010?\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010¡ã\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010¡À\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J\u001a\u0010?\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016JJ\u0010?\u0001\u001a\u0014\u0012\u0005\u0012\u00030\u0098\u00010Oj\t\u0012\u0005\u0012\u00030\u0098\u0001`Q2\u0006\u0010I\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\t\b\u0002\u0010?\u0001\u001a\u00020*2\b\b\u0002\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J2\u0010?\u0001\u001a\t\u0012\u0005\u0012\u00030¡¤\u00010Z2\u0006\u0010&\u001a\u00020'2\u0006\u0010F\u001a\u00020G2\u0006\u0010u\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u0095\u0001J+\u0010?\u0001\u001a\b\u0012\u0004\u0012\u00020\u000f0Z2\u0007\u0010?\u0001\u001a\u00020\u00072\u0007\u0010?\u0001\u001a\u00020\u0007H\u0082@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J\u0019\u0010?\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020DH\u0002J\u001a\u0010?\u0001\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016J-\u0010?\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020D2\b\u0010I\u001a\u0004\u0018\u00010\u0007H\u0082@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J>\u0010?\u0001\u001a\u00020\u00182\u0007\u0010\u00c0\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020D2\b\u0010I\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0082@\u00f8\u0001\u0000?\u0006\u0003\u0010\u00c1\u0001J\u0019\u0010\u00c2\u0001\u001a\u00020\u00182\u0006\u0010&\u001a\u00020'2\u0006\u0010C\u001a\u00020DH\u0002J$\u0010\u00c3\u0001\u001a\u00020\u00182\u0007\u0010\u00c4\u0001\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u00c5\u0001J$\u0010\u00c6\u0001\u001a\u00020*2\u0007\u0010\u00c7\u0001\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010?\u0001J\u001a\u0010\u00c8\u0001\u001a\u00020\u00182\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00f8\u0001\u0000?\u0006\u0002\u0010\u0016JF\u0010\u00c9\u0001\u001a\u00020\u00182\b\u0010\u00ca\u0001\u001a\u00030\u00cb\u00012\u0006\u0010N\u001a\u00020\u00072\u0006\u0010\u001d\u001a\u00020\u00072\u0017\b\u0002\u0010\u00cc\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u00cd\u0001J>\u0010\u00ce\u0001\u001a\u00020\u00182\b\u0010\u00ca\u0001\u001a\u00030\u00cb\u00012\u0006\u0010N\u001a\u00020\u00072\u0017\b\u0002\u0010\u00cc\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u00cf\u0001J>\u0010\u00d0\u0001\u001a\u00020\u00182\b\u0010\u00ca\u0001\u001a\u00030\u00cb\u00012\u0006\u0010N\u001a\u00020\u00072\u0017\b\u0002\u0010\u00cc\u0001\u001a\u0010\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u001bH\u0086@\u00f8\u0001\u0000?\u0006\u0003\u0010\u00cf\u0001J!\u0010\u00d1\u0001\u001a\u00020\u00072\u0006\u0010&\u001a\u00020'2\u0006\u0010F\u001a\u00020G2\u0006\u0010E\u001a\u00020\u0007H\u0002R!\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068BX\u0082\u0084\u0002?\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\tR\u000e\u0010\f\u001a\u00020\rX\u0082\u000e?\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082D?\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e?\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¡§\u0006\u00d2\u0001" }, d2 = { "Lcom/htmake/reader/api/controller/BookController;", "Lcom/htmake/reader/api/controller/BaseController;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/coroutines/CoroutineContext;)V", "backupFileNames", "", "", "getBackupFileNames", "()[Ljava/lang/String;", "backupFileNames$delegate", "Lkotlin/Lazy;", "bookInfoCache", "Lio/legado/app/utils/ACache;", "concurrentLoopCount", "", "webClient", "Lio/vertx/ext/web/client/WebClient;", "addBookGroupMulti", "Lcom/htmake/reader/api/ReturnData;", "context", "Lio/vertx/ext/web/RoutingContext;", "(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addInvalidBookSource", "", "sourceUrl", "invalidInfo", "", "", "userNameSpace", "backupToMongodb", "bookSourceDebugSSE", "cacheBookOnServer", "bookUrlList", "Lio/vertx/core/json/JsonArray;", "(Lio/vertx/core/json/JsonArray;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cacheBookSSE", "convertPdfPageToImage", "book", "Lio/legado/app/data/entities/Book;", "index", "force", "", "convertPdfToImage", "createUserBackup", "Ljava/io/File;", "backupDir", "latestZipFilePath", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteBook", "deleteBookCache", "deleteBooks", "editShelfBook", "handler", "Lkotlin/Function1;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exploreBook", "exportBook", "exportToEpub", "exportDir", "bookSource", "(Ljava/io/File;Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "exportToTxt", "bookInfo", "extractCbz", "extractEpub", "fixPic", "epubBook", "Lme/ag2s/epublib/domain/EpubBook;", "content", "chapter", "Lio/legado/app/data/entities/BookChapter;", "getAllContents", "bookSourceString", "append", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "text", "Ljava/util/ArrayList;", "Lkotlin/Triple;", "Lkotlin/collections/ArrayList;", "srcList", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAvailableBookSource", "getBookChaptersCache", "getBookContent", "getBookCover", "getBookInfo", "getBookShelfBooks", "", "refresh", "(ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookSourceString", "withExploreUrl", "(Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getBookSourceStringBySourceURLOpt", "getBookshelf", "getCachedChapterContentSet", "", "getChapterCacheDir", "getChapterList", "getChapterListByRule", "getHttpTTSByName", "Lio/legado/app/data/entities/HttpTTS;", "getInvalidBookSourceCache", "getInvalidBookSources", "getLastBackFileFromWebdav", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLocalChapterList", "debugLog", "mutex", "Lkotlinx/coroutines/sync/Mutex;", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getResultAndQueryIndex", "Lkotlin/Pair;", "queryIndexInContent", "query", "getShelfBook", "getShelfBookByURL", "url", "getShelfBookWithCacheInfo", "getSpeakStream", "Ljava/io/InputStream;", "httpTts", "speakText", "speechRate", "(Lio/legado/app/data/entities/HttpTTS;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTxtTocRules", "importBookPreview", "isInvalidBookSource", "Lio/legado/app/data/entities/BookSource;", "mergeBookCacheInfo", "(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "refreshLocalBook", "removeBookGroupMulti", "restoreFromMongodb", "saveBook", "saveBookConfig", "saveBookContent", "saveBookCover", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookGroupId", "saveBookInfoCache", "bookList", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookProgress", "saveBookProgressToWebdav", "bookChapter", "(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveBookSources", "sourceList", "Lio/legado/app/data/entities/SearchBook;", "replace", "saveBookToShelf", "_book", "saveLocalBookCover", "(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "savePdfPageToImage", "document", "Lorg/apache/pdfbox/pdmodel/PDDocument;", "renderer", "Lorg/apache/pdfbox/rendering/PDFRenderer;", "targetWidth", "", "imageFormat", "output", "saveShelfBookLatestChapter", "bookChapterList", "(Lio/legado/app/data/entities/Book;Ljava/util/List;Ljava/lang/String;Lkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveShelfBookProgress", "saveToWebdav", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchBook", "searchBookContent", "searchBookMulti", "searchBookMultiSSE", "searchBookSource", "searchBookSourceSSE", "searchBookWithSource", "accurate", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;ZLjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchChapter", "Lio/legado/app/data/entities/SearchResult;", "searchPosition", "mContent", "pattern", "setAssets", "setBookSource", "setCover", "(Lio/legado/app/data/entities/Book;Lme/ag2s/epublib/domain/EpubBook;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEpubContent", "contentModel", "(Ljava/lang/String;Lio/legado/app/data/entities/Book;Lme/ag2s/epublib/domain/EpubBook;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "setEpubMetadata", "syncBookProgressFromWebdav", "progressFilePath", "(Ljava/lang/Object;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncFromWebdav", "zipFilePath", "textToSpeech", "ttsByApi", "response", "Lio/vertx/core/http/HttpServerResponse;", "options", "(Lio/vertx/core/http/HttpServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ttsByEdge", "(Lio/vertx/core/http/HttpServerResponse;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "ttsByTextToSpeechCn", "updateImageLinkInContent", "reader-pro" })
public final class BookController extends BaseController
{
    @NotNull
    private ACache bookInfoCache;
    private final int concurrentLoopCount;
    @NotNull
    private WebClient webClient;
    @NotNull
    private final Lazy backupFileNames$delegate;
    
    public BookController(@NotNull final CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter((Object)coroutineContext, "coroutineContext");
        super(coroutineContext);
        this.bookInfoCache = ACache.Companion.get("bookInfoCache", 2000000L, 10000);
        this.concurrentLoopCount = 8;
        this.backupFileNames$delegate = LazyKt.lazy((Function0)BookController$backupFileNames.BookController$backupFileNames$2.INSTANCE);
        final WebClient bean = SpringContextUtils.getBean("webClient", WebClient.class);
        Intrinsics.checkNotNullExpressionValue((Object)bean, "getBean(\"webClient\", WebClient::class.java)");
        this.webClient = bean;
    }
    
    private final String[] getBackupFileNames() {
        return (String[])this.backupFileNames$delegate.getValue();
    }
    
    private final ACache getInvalidBookSourceCache(final String userNameSpace) {
        final File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace));
        final ACache invalidBookSourceCache = ACache.Companion.get(cacheDir, 5000000L, 1000000);
        return invalidBookSourceCache;
    }
    
    private final boolean isInvalidBookSource(final BookSource bookSource, final String userNameSpace) {
        return this.getInvalidBookSourceCache(userNameSpace).getAsString(bookSource.getBookSourceUrl()) != null;
    }
    
    private final void addInvalidBookSource(final String sourceUrl, final Map<String, ?> invalidInfo, final String userNameSpace) {
        this.getInvalidBookSourceCache(userNameSpace).put(sourceUrl, ExtKt.jsonEncode$default(invalidInfo, false, 2, null), 600);
    }
    
    private final ACache getBookChaptersCache(final String userNameSpace) {
        final File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "bookChaptersCache", userNameSpace));
        final ACache bookChaptersCache = ACache.Companion.get(cacheDir, 5000000L, 1000000);
        return bookChaptersCache;
    }
    
    @Nullable
    public final Object getInvalidBookSources(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getInvalidBookSources.BookController$getInvalidBookSources$1) {
                final BookController$getInvalidBookSources.BookController$getInvalidBookSources$1 bookController$getInvalidBookSources$1 = (BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$completion;
                if ((bookController$getInvalidBookSources$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getInvalidBookSources.BookController$getInvalidBookSources$1 bookController$getInvalidBookSources$2 = bookController$getInvalidBookSources$1;
                    bookController$getInvalidBookSources$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getInvalidBookSources.BookController$getInvalidBookSources$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$0 = this;
                ((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$1 = context;
                ((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$2 = returnData;
                ((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$2;
                context = (RoutingContext)((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$1;
                this = (BookController)((BookController$getInvalidBookSources.BookController$getInvalidBookSources$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final ACache invalidBookSourceCache = this.getInvalidBookSourceCache(userNameSpace);
        final File cacheDir = new File(ExtKt.getWorkDir("storage", "cache", "invalidBookSourceCache", userNameSpace));
        final File[] files = cacheDir.listFiles();
        final ArrayList invalidBookSourceList = new ArrayList();
        if (files != null) {
            final File[] array = files;
            int i = 0;
            while (i < array.length) {
                final File f = array[i];
                ++i;
                final ACache aCache = invalidBookSourceCache;
                final String name = f.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name, "f.name");
                final String byHashCode = aCache.getByHashCode(name);
                if (byHashCode == null) {
                    continue;
                }
                final String info = byHashCode;
                final int n = 0;
                Boxing.boxBoolean(invalidBookSourceList.add(ExtKt.toMap(info)));
            }
        }
        return ReturnData.setData$default(returnData, invalidBookSourceList, null, 2, null);
    }
    
    @Nullable
    public final Object getBookInfo(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/BookController$getBookInfo$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/BookController$getBookInfo$1;
        //    11: astore          19
        //    13: aload           19
        //    15: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           19
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/BookController$getBookInfo$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/BookController$getBookInfo$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          20
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //    67: tableswitch {
        //                0: 108
        //                1: 360
        //                2: 611
        //                3: 723
        //                4: 891
        //                5: 973
        //                6: 1076
        //          default: 1113
        //        }
        //   108: aload           $result
        //   110: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   113: new             Lcom/htmake/reader/api/ReturnData;
        //   116: dup            
        //   117: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   120: astore_3        /* returnData */
        //   121: aconst_null    
        //   122: astore          4
        //   124: aload_1         /* context */
        //   125: invokeinterface io/vertx/ext/web/RoutingContext.request:()Lio/vertx/core/http/HttpServerRequest;
        //   130: invokeinterface io/vertx/core/http/HttpServerRequest.method:()Lio/vertx/core/http/HttpMethod;
        //   135: getstatic       io/vertx/core/http/HttpMethod.POST:Lio/vertx/core/http/HttpMethod;
        //   138: if_acmpne       203
        //   141: aload_1         /* context */
        //   142: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   147: ldc_w           "url"
        //   150: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   153: astore          6
        //   155: aload           6
        //   157: ifnonnull       181
        //   160: aload_1         /* context */
        //   161: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   166: ldc_w           "searchBook"
        //   169: invokevirtual   io/vertx/core/json/JsonObject.getJsonObject:(Ljava/lang/String;)Lio/vertx/core/json/JsonObject;
        //   172: ldc_w           "bookUrl"
        //   175: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   178: goto            183
        //   181: aload           6
        //   183: astore          5
        //   185: aload           5
        //   187: ifnonnull       196
        //   190: ldc_w           ""
        //   193: goto            198
        //   196: aload           5
        //   198: astore          4
        //   200: goto            247
        //   203: aload_1         /* context */
        //   204: ldc_w           "url"
        //   207: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   212: astore          6
        //   214: aload           6
        //   216: ldc_w           "context.queryParam(\"url\")"
        //   219: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   222: aload           6
        //   224: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   227: checkcast       Ljava/lang/String;
        //   230: astore          5
        //   232: aload           5
        //   234: ifnonnull       243
        //   237: ldc_w           ""
        //   240: goto            245
        //   243: aload           5
        //   245: astore          bookUrl
        //   247: aload           bookUrl
        //   249: checkcast       Ljava/lang/CharSequence;
        //   252: astore          5
        //   254: iconst_0       
        //   255: istore          6
        //   257: aload           5
        //   259: invokeinterface java/lang/CharSequence.length:()I
        //   264: ifne            271
        //   267: iconst_1       
        //   268: goto            272
        //   271: iconst_0       
        //   272: ifeq            283
        //   275: aload_3         /* returnData */
        //   276: ldc_w           "\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5"
        //   279: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   282: areturn        
        //   283: aload_0         /* this */
        //   284: aload_1         /* context */
        //   285: invokevirtual   com/htmake/reader/api/controller/BookController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //   288: astore          userNameSpace
        //   290: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
        //   293: ldc_w           "getBookInfo with bookUrl: {}"
        //   296: aload           bookUrl
        //   298: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
        //   303: aconst_null    
        //   304: astore          bookInfo
        //   306: aload_0         /* this */
        //   307: aload_1         /* context */
        //   308: aload           $continuation
        //   310: aload           $continuation
        //   312: aload_0         /* this */
        //   313: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   316: aload           $continuation
        //   318: aload_1         /* context */
        //   319: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   322: aload           $continuation
        //   324: aload_3         /* returnData */
        //   325: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   328: aload           $continuation
        //   330: aload           bookUrl
        //   332: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   335: aload           $continuation
        //   337: aload           userNameSpace
        //   339: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$4:Ljava/lang/Object;
        //   342: aload           $continuation
        //   344: iconst_1       
        //   345: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //   348: invokevirtual   com/htmake/reader/api/controller/BookController.checkAuth:(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   351: dup            
        //   352: aload           20
        //   354: if_acmpne       417
        //   357: aload           20
        //   359: areturn        
        //   360: aconst_null    
        //   361: astore          bookInfo
        //   363: aload           $continuation
        //   365: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$4:Ljava/lang/Object;
        //   368: checkcast       Ljava/lang/String;
        //   371: astore          userNameSpace
        //   373: aload           $continuation
        //   375: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   378: checkcast       Ljava/lang/String;
        //   381: astore          4
        //   383: aload           $continuation
        //   385: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   388: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   391: astore_3       
        //   392: aload           $continuation
        //   394: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   397: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   400: astore_1       
        //   401: aload           $continuation
        //   403: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   406: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   409: astore_0       
        //   410: aload           $result
        //   412: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   415: aload           $result
        //   417: checkcast       Ljava/lang/Boolean;
        //   420: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   423: ifeq            436
        //   426: aload_0        
        //   427: aload           4
        //   429: aload           userNameSpace
        //   431: invokevirtual   com/htmake/reader/api/controller/BookController.getShelfBookByURL:(Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/Book;
        //   434: astore          bookInfo
        //   436: aload           bookInfo
        //   438: ifnonnull       1003
        //   441: aconst_null    
        //   442: astore          7
        //   444: aload_0        
        //   445: getfield        com/htmake/reader/api/controller/BookController.bookInfoCache:Lio/legado/app/utils/ACache;
        //   448: aload           4
        //   450: invokevirtual   io/legado/app/utils/ACache.getAsString:(Ljava/lang/String;)Ljava/lang/String;
        //   453: astore          9
        //   455: aload           9
        //   457: ifnonnull       464
        //   460: aconst_null    
        //   461: goto            542
        //   464: aload           9
        //   466: invokestatic    com/htmake/reader/utils/ExtKt.toMap:(Ljava/lang/Object;)Ljava/util/Map;
        //   469: astore          10
        //   471: aload           10
        //   473: ifnonnull       480
        //   476: aconst_null    
        //   477: goto            542
        //   480: aload           10
        //   482: astore          $this$toDataClass$iv
        //   484: iconst_0       
        //   485: istore          $i$f$toDataClass
        //   487: aload           $this$toDataClass$iv
        //   489: astore          $this$convert$iv$iv
        //   491: iconst_0       
        //   492: istore          $i$f$convert
        //   494: aload           $this$convert$iv$iv
        //   496: instanceof      Ljava/lang/String;
        //   499: ifeq            510
        //   502: aload           $this$convert$iv$iv
        //   504: checkcast       Ljava/lang/String;
        //   507: goto            518
        //   510: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   513: aload           $this$convert$iv$iv
        //   515: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //   518: astore          json$iv$iv
        //   520: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   523: aload           json$iv$iv
        //   525: new             Lcom/htmake/reader/api/controller/BookController$getBookInfo$$inlined$toDataClass$1;
        //   528: dup            
        //   529: invokespecial   com/htmake/reader/api/controller/BookController$getBookInfo$$inlined$toDataClass$1.<init>:()V
        //   532: invokevirtual   com/htmake/reader/api/controller/BookController$getBookInfo$$inlined$toDataClass$1.getType:()Ljava/lang/reflect/Type;
        //   535: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   538: nop            
        //   539: checkcast       Lio/legado/app/data/entities/Book;
        //   542: astore          cacheInfo
        //   544: aload           cacheInfo
        //   546: ifnull          664
        //   549: aload_0        
        //   550: aload_1        
        //   551: aload           cacheInfo
        //   553: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //   556: iconst_0       
        //   557: aload           $continuation
        //   559: iconst_4       
        //   560: aconst_null    
        //   561: aload           $continuation
        //   563: aload_0        
        //   564: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   567: aload           $continuation
        //   569: aload_3        
        //   570: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   573: aload           $continuation
        //   575: aload           4
        //   577: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   580: aload           $continuation
        //   582: aload           userNameSpace
        //   584: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   587: aload           $continuation
        //   589: aconst_null    
        //   590: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$4:Ljava/lang/Object;
        //   593: aload           $continuation
        //   595: iconst_2       
        //   596: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //   599: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   602: dup            
        //   603: aload           20
        //   605: if_acmpne       656
        //   608: aload           20
        //   610: areturn        
        //   611: aload           $continuation
        //   613: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   616: checkcast       Ljava/lang/String;
        //   619: astore          5
        //   621: aload           $continuation
        //   623: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   626: checkcast       Ljava/lang/String;
        //   629: astore          4
        //   631: aload           $continuation
        //   633: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   636: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   639: astore_3       
        //   640: aload           $continuation
        //   642: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   645: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   648: astore_0       
        //   649: aload           $result
        //   651: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   654: aload           $result
        //   656: checkcast       Ljava/lang/String;
        //   659: astore          7
        //   661: goto            773
        //   664: aload_0        
        //   665: aload_1        
        //   666: aconst_null    
        //   667: iconst_0       
        //   668: aload           $continuation
        //   670: bipush          6
        //   672: aconst_null    
        //   673: aload           $continuation
        //   675: aload_0        
        //   676: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   679: aload           $continuation
        //   681: aload_3        
        //   682: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   685: aload           $continuation
        //   687: aload           4
        //   689: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   692: aload           $continuation
        //   694: aload           5
        //   696: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   699: aload           $continuation
        //   701: aconst_null    
        //   702: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$4:Ljava/lang/Object;
        //   705: aload           $continuation
        //   707: iconst_3       
        //   708: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //   711: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   714: dup            
        //   715: aload           20
        //   717: if_acmpne       768
        //   720: aload           20
        //   722: areturn        
        //   723: aload           $continuation
        //   725: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   728: checkcast       Ljava/lang/String;
        //   731: astore          5
        //   733: aload           $continuation
        //   735: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   738: checkcast       Ljava/lang/String;
        //   741: astore          4
        //   743: aload           $continuation
        //   745: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   748: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   751: astore_3       
        //   752: aload           $continuation
        //   754: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   757: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   760: astore_0       
        //   761: aload           $result
        //   763: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   766: aload           $result
        //   768: checkcast       Ljava/lang/String;
        //   771: astore          bookSource
        //   773: aload           bookSource
        //   775: checkcast       Ljava/lang/CharSequence;
        //   778: astore          9
        //   780: iconst_0       
        //   781: istore          10
        //   783: iconst_0       
        //   784: istore          11
        //   786: aload           9
        //   788: ifnull          801
        //   791: aload           9
        //   793: invokeinterface java/lang/CharSequence.length:()I
        //   798: ifne            805
        //   801: iconst_1       
        //   802: goto            806
        //   805: iconst_0       
        //   806: ifeq            817
        //   809: aload_3        
        //   810: ldc_w           "\u672a\u914d\u7f6e\u4e66\u6e90"
        //   813: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   816: areturn        
        //   817: aload_0        
        //   818: astore          16
        //   820: new             Lio/legado/app/model/webBook/WebBook;
        //   823: dup            
        //   824: aload           bookSource
        //   826: aload_0        
        //   827: invokevirtual   com/htmake/reader/api/controller/BookController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   830: invokevirtual   com/htmake/reader/config/AppConfig.getDebugLog:()Z
        //   833: aconst_null    
        //   834: aload           5
        //   836: iconst_4       
        //   837: aconst_null    
        //   838: invokespecial   io/legado/app/model/webBook/WebBook.<init>:(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   841: aload           4
        //   843: iconst_0       
        //   844: aload           $continuation
        //   846: iconst_2       
        //   847: aconst_null    
        //   848: aload           $continuation
        //   850: aload_0        
        //   851: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   854: aload           $continuation
        //   856: aload_3        
        //   857: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   860: aload           $continuation
        //   862: aload           16
        //   864: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   867: aload           $continuation
        //   869: aconst_null    
        //   870: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //   873: aload           $continuation
        //   875: iconst_4       
        //   876: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //   879: invokestatic    io/legado/app/model/webBook/WebBook.getBookInfo$default:(Lio/legado/app/model/webBook/WebBook;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   882: dup            
        //   883: aload           20
        //   885: if_acmpne       926
        //   888: aload           20
        //   890: areturn        
        //   891: aload           $continuation
        //   893: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   896: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   899: astore          16
        //   901: aload           $continuation
        //   903: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   906: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   909: astore_3       
        //   910: aload           $continuation
        //   912: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   915: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   918: astore_0       
        //   919: aload           $result
        //   921: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   924: aload           $result
        //   926: astore          17
        //   928: aload           16
        //   930: aload           17
        //   932: checkcast       Lio/legado/app/data/entities/Book;
        //   935: aload           $continuation
        //   937: aload           $continuation
        //   939: aload_0        
        //   940: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   943: aload           $continuation
        //   945: aload_3        
        //   946: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   949: aload           $continuation
        //   951: aconst_null    
        //   952: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //   955: aload           $continuation
        //   957: iconst_5       
        //   958: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //   961: invokevirtual   com/htmake/reader/api/controller/BookController.mergeBookCacheInfo:(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   964: dup            
        //   965: aload           20
        //   967: if_acmpne       998
        //   970: aload           20
        //   972: areturn        
        //   973: aload           $continuation
        //   975: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //   978: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   981: astore_3       
        //   982: aload           $continuation
        //   984: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //   987: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   990: astore_0       
        //   991: aload           $result
        //   993: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   996: aload           $result
        //   998: checkcast       Lio/legado/app/data/entities/Book;
        //  1001: astore          6
        //  1003: aload_0        
        //  1004: iconst_1       
        //  1005: anewarray       Lio/legado/app/data/entities/Book;
        //  1008: astore          7
        //  1010: aload           7
        //  1012: iconst_0       
        //  1013: aload           6
        //  1015: aastore        
        //  1016: aload           7
        //  1018: invokestatic    kotlin/collections/CollectionsKt.arrayListOf:([Ljava/lang/Object;)Ljava/util/ArrayList;
        //  1021: checkcast       Ljava/util/List;
        //  1024: aload           $continuation
        //  1026: aload           $continuation
        //  1028: aload_3        
        //  1029: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //  1032: aload           $continuation
        //  1034: aload           6
        //  1036: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //  1039: aload           $continuation
        //  1041: aconst_null    
        //  1042: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$2:Ljava/lang/Object;
        //  1045: aload           $continuation
        //  1047: aconst_null    
        //  1048: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$3:Ljava/lang/Object;
        //  1051: aload           $continuation
        //  1053: aconst_null    
        //  1054: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$4:Ljava/lang/Object;
        //  1057: aload           $continuation
        //  1059: bipush          6
        //  1061: putfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.label:I
        //  1064: invokevirtual   com/htmake/reader/api/controller/BookController.saveBookInfoCache:(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1067: dup            
        //  1068: aload           20
        //  1070: if_acmpne       1102
        //  1073: aload           20
        //  1075: areturn        
        //  1076: aload           $continuation
        //  1078: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$1:Ljava/lang/Object;
        //  1081: checkcast       Lio/legado/app/data/entities/Book;
        //  1084: astore          6
        //  1086: aload           $continuation
        //  1088: getfield        com/htmake/reader/api/controller/BookController$getBookInfo$1.L$0:Ljava/lang/Object;
        //  1091: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1094: astore_3       
        //  1095: aload           $result
        //  1097: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1100: aload           $result
        //  1102: pop            
        //  1103: aload_3        
        //  1104: aload           6
        //  1106: aconst_null    
        //  1107: iconst_2       
        //  1108: aconst_null    
        //  1109: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //  1112: areturn        
        //  1113: new             Ljava/lang/IllegalStateException;
        //  1116: dup            
        //  1117: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1120: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1123: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lcom/htmake/reader/api/ReturnData;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 28 27 FF 00 0A 00 14 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 17 00 00 FF 00 39 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 48 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 05 00 07 00 60 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 41 07 00 60 FF 00 0C 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 05 07 00 60 07 00 60 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 41 07 00 60 FF 00 04 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 05 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 27 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 05 07 00 60 07 01 A9 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 41 07 00 60 FF 00 01 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 13 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 17 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 01 4D 01 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 40 01 0A FF 00 4C 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 38 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 05 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 12 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 1B 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 05 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 0F 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 05 00 07 00 60 07 01 B7 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 1D 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 05 00 07 00 60 07 01 B7 07 01 B7 01 07 01 B7 01 00 00 00 07 01 13 07 01 17 07 01 13 00 00 47 07 00 60 FF 00 17 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 05 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 84 FF 00 44 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 2C 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 07 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 07 01 84 05 07 01 84 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 3A 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 2C 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 04 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 1B 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 07 00 60 00 07 00 60 00 07 01 4D 01 01 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 03 40 01 0A FF 00 49 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 22 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 00 00 00 00 00 00 07 00 02 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 2E 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 18 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 04 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 07 01 84 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 48 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00 FF 00 19 00 15 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 07 01 84 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 01 07 01 13 FF 00 0A 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 01 17 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException
        //     at java.base/java.util.Collections$1.remove(Collections.java:4821)
        //     at java.base/java.util.AbstractCollection.removeAll(AbstractCollection.java:369)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3018)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    public final Object getBookCover(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final List queryParam = context.queryParam("path");
        Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"path\")");
        final String s = (String)CollectionsKt.firstOrNull(queryParam);
        final String coverUrl = (s == null) ? "" : s;
        if (coverUrl.length() == 0) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
        }
        final String ext = this.getFileExt(coverUrl, "png");
        final String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl);
        final String cachePath = ExtKt.getWorkDir("storage", "cache", "bookCoverCache", md5Encode + '.' + ext);
        final File cacheFile = new File(cachePath);
        if (cacheFile.exists()) {
            BookControllerKt.access$getLogger$p().info("send cache: {}", (Object)cacheFile);
            final HttpServerResponse sendFile = context.response().putHeader("Cache-Control", "86400").sendFile(cacheFile.toString());
            if (sendFile == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return sendFile;
            }
            return Unit.INSTANCE;
        }
        else {
            if (!cacheFile.getParentFile().exists()) {
                cacheFile.getParentFile().mkdirs();
            }
            final int $i$f$CoroutineExceptionHandler = 0;
            final CoroutineExceptionHandler exceptionHandler = (CoroutineExceptionHandler)new CoroutineExceptionHandler(CoroutineExceptionHandler.Key, context) {
                public void handleException(@NotNull final CoroutineContext context, @NotNull final Throwable exception) {
                    // 
                    // This method could not be decompiled.
                    // 
                    // Original Bytecode:
                    // 
                    //     1: aload_2         /* exception */
                    //     2: astore_3       
                    //     3: astore          ctx
                    //     5: iconst_0       
                    //     6: istore          $i$a$-CoroutineExceptionHandler-BookController$getBookCover$exceptionHandler$1
                    //     8: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
                    //    11: ldc             "get cover error: {}"
                    //    13: aload_3         /* ex */
                    //    14: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                    //    17: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
                    //    22: aload_0         /* this */
                    //    23: getfield        com/htmake/reader/api/controller/BookController$getBookCover$$inlined$CoroutineExceptionHandler$1.$context$inlined:Lio/vertx/ext/web/RoutingContext;
                    //    26: invokeinterface io/vertx/ext/web/RoutingContext.response:()Lio/vertx/core/http/HttpServerResponse;
                    //    31: sipush          404
                    //    34: invokeinterface io/vertx/core/http/HttpServerResponse.setStatusCode:(I)Lio/vertx/core/http/HttpServerResponse;
                    //    39: invokeinterface io/vertx/core/http/HttpServerResponse.end:()V
                    //    44: nop            
                    //    45: return         
                    // 
                    // The error that occurred was:
                    // 
                    // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
                    //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
                    //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
                    //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                    //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:426)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                    //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
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
            };
            final Job launch$default = BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()).plus((CoroutineContext)exceptionHandler), (CoroutineStart)null, (Function2)new BookController$getBookCover.BookController$getBookCover$2(context, cacheFile, this, coverUrl, (Continuation)null), 2, (Object)null);
            if (launch$default == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                return launch$default;
            }
            return Unit.INSTANCE;
        }
    }
    
    @Nullable
    public final Object importBookPreview(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$importBookPreview.BookController$importBookPreview$1) {
                final BookController$importBookPreview.BookController$importBookPreview$1 bookController$importBookPreview$1 = (BookController$importBookPreview.BookController$importBookPreview$1)$completion;
                if ((bookController$importBookPreview$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$importBookPreview.BookController$importBookPreview$1 bookController$importBookPreview$2 = bookController$importBookPreview$1;
                    bookController$importBookPreview$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$importBookPreview.BookController$importBookPreview$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$0 = this;
                ((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$1 = context;
                ((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$2 = returnData;
                ((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$2;
                context = (RoutingContext)((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$1;
                this = (BookController)((BookController$importBookPreview.BookController$importBookPreview$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
            return returnData.setErrorMsg("\u8bf7\u4e0a\u4f20\u4e66\u7c4d\u6587\u4ef6");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final ArrayList fileList = new ArrayList();
        final Set fileUploads = context.fileUploads();
        Intrinsics.checkNotNullExpressionValue((Object)fileUploads, "context.fileUploads()");
        final Iterable $this$forEach$iv = fileUploads;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final FileUpload it = (FileUpload)element$iv;
            final int n = 0;
            final File file = new File(it.uploadedFileName());
            BookControllerKt.access$getLogger$p().info("uploadFile: {} {} {}", new Object[] { it.uploadedFileName(), it.fileName(), file });
            if (file.exists()) {
                String fileName = it.fileName();
                final BaseController baseController = this;
                final String s = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s, "fileName");
                final String ext = BaseController.getFileExt$default(baseController, s, null, 2, null);
                if (!Intrinsics.areEqual((Object)ext, (Object)"txt") && !Intrinsics.areEqual((Object)ext, (Object)"epub") && !Intrinsics.areEqual((Object)ext, (Object)"umd") && !Intrinsics.areEqual((Object)ext, (Object)"cbz") && !Intrinsics.areEqual((Object)ext, (Object)"pdf")) {
                    ExtKt.deleteRecursively(file);
                    return returnData.setErrorMsg("\u4e0d\u652f\u6301\u5bfc\u5165" + ext + "\u683c\u5f0f\u7684\u4e66\u7c4d\u6587\u4ef6");
                }
                final FileUtils instance = FileUtils.INSTANCE;
                final String path = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)path, "fileName");
                final String nameExcludeExtension;
                fileName = (nameExcludeExtension = instance.getNameExcludeExtension(path));
                Intrinsics.checkNotNullExpressionValue((Object)nameExcludeExtension, "fileName");
                fileName = AppPattern.INSTANCE.getFileNameRegex().replace((CharSequence)nameExcludeExtension, "");
                final StringBuilder sb = new StringBuilder();
                final String s2 = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s2, "fileName");
                final String substring = s2.substring(0, Math.min(50, fileName.length()));
                Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
                fileName = sb.append(substring).append('.').append(ext).toString();
                final String localFilePath = Paths.get("storage", "assets", userNameSpace, "book", fileName).toString();
                final String localFileUrl = "/assets/" + userNameSpace + "/book/" + (Object)fileName;
                String filePath = localFilePath;
                final String s3 = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s3, "fileName");
                if (StringsKt.endsWith(s3, ".epub", true)) {
                    filePath = filePath + (Object)File.separator + "index.epub";
                }
                final String s4 = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s4, "fileName");
                if (StringsKt.endsWith(s4, ".cbz", true)) {
                    filePath = filePath + (Object)File.separator + "index.cbz";
                }
                final String s5 = fileName;
                Intrinsics.checkNotNullExpressionValue((Object)s5, "fileName");
                if (StringsKt.endsWith(s5, ".pdf", true)) {
                    filePath = filePath + (Object)File.separator + "index.pdf";
                }
                final File newFile = new File(ExtKt.getWorkDir(filePath));
                if (!newFile.getParentFile().exists()) {
                    newFile.getParentFile().mkdirs();
                }
                if (newFile.exists()) {
                    newFile.delete();
                }
                BookControllerKt.access$getLogger$p().info("moveTo: {}", (Object)newFile);
                if (FilesKt.copyRecursively$default(file, newFile, false, (Function2)null, 6, (Object)null)) {
                    final Book book = Book.Companion.initLocalBook(localFileUrl, localFilePath, ExtKt.getWorkDir$default(null, 1, null));
                    book.setUserNameSpace(userNameSpace);
                    try {
                        final ArrayList chapters = LocalBook.INSTANCE.getChapterList(book);
                        fileList.add(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"book", (Object)book), TuplesKt.to((Object)"chapters", (Object)chapters) }));
                    }
                    catch (final TocEmptyException ex) {
                        fileList.add(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"book", (Object)book), TuplesKt.to((Object)"chapters", (Object)new ArrayList()) }));
                    }
                }
                ExtKt.deleteRecursively(file);
            }
        }
        return ReturnData.setData$default(returnData, fileList, null, 2, null);
    }
    
    @Nullable
    public final Object getTxtTocRules(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getTxtTocRules.BookController$getTxtTocRules$1) {
                final BookController$getTxtTocRules.BookController$getTxtTocRules$1 bookController$getTxtTocRules$1 = (BookController$getTxtTocRules.BookController$getTxtTocRules$1)$completion;
                if ((bookController$getTxtTocRules$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getTxtTocRules.BookController$getTxtTocRules$1 bookController$getTxtTocRules$2 = bookController$getTxtTocRules$1;
                    bookController$getTxtTocRules$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getTxtTocRules.BookController$getTxtTocRules$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$0 = this;
                ((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$1 = context;
                ((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$2 = returnData;
                ((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$2;
                context = (RoutingContext)((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$1;
                this = (BookController)((BookController$getTxtTocRules.BookController$getTxtTocRules$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final String txtTocRules = this.getUserStorage(userNameSpace, "txtTocRule");
        final List rules = new ArrayList();
        rules.addAll(DefaultData.INSTANCE.getTxtTocRules());
        if (txtTocRules != null) {
            final Gson $this$fromJsonArray$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonArray = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Object fromJson = $this$fromJsonArray$iv.fromJson(txtTocRules, (Type)new ParameterizedTypeImpl(TxtTocRule.class));
                o = Result.constructor-impl((Object)((fromJson instanceof List) ? ((List<?>)fromJson) : null));
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            final Object o2 = o;
            final List list = (List)(Result.isFailure-impl(o2) ? null : o2);
            final List customRule = (list == null) ? CollectionsKt.emptyList() : list;
            rules.addAll(customRule);
        }
        return ReturnData.setData$default(returnData, rules, null, 2, null);
    }
    
    @Nullable
    public final Object getChapterListByRule(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getChapterListByRule.BookController$getChapterListByRule$1) {
                final BookController$getChapterListByRule.BookController$getChapterListByRule$1 bookController$getChapterListByRule$1 = (BookController$getChapterListByRule.BookController$getChapterListByRule$1)$completion;
                if ((bookController$getChapterListByRule$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getChapterListByRule.BookController$getChapterListByRule$1 bookController$getChapterListByRule$2 = bookController$getChapterListByRule$1;
                    bookController$getChapterListByRule$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getChapterListByRule.BookController$getChapterListByRule$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$0 = this;
                ((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$1 = context;
                ((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$2 = returnData;
                ((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$2;
                context = (RoutingContext)((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$1;
                this = (BookController)((BookController$getChapterListByRule.BookController$getChapterListByRule$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final Book book = (Book)context.getBodyAsJson().mapTo((Class)Book.class);
        if (book.getOrigin().length() == 0) {
            return returnData.setErrorMsg("\u672a\u627e\u5230\u4e66\u6e90\u4fe1\u606f");
        }
        if (!book.isLocalTxt() && !book.isLocalEpub() && !book.isLocalPdf()) {
            return returnData.setErrorMsg("\u975e\u672c\u5730txt/epub/pdf\u4e66\u7c4d");
        }
        book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
        book.setUserNameSpace(this.getUserNameSpace(context));
        final LocalBook instance = LocalBook.INSTANCE;
        Intrinsics.checkNotNullExpressionValue((Object)book, "book");
        final ArrayList chapters = instance.getChapterList(book);
        return ReturnData.setData$default(returnData, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"book", (Object)book), TuplesKt.to((Object)"chapters", (Object)chapters) }), null, 2, null);
    }
    
    @Nullable
    public final Object refreshLocalBook(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$refreshLocalBook.BookController$refreshLocalBook$1) {
                final BookController$refreshLocalBook.BookController$refreshLocalBook$1 bookController$refreshLocalBook$1 = (BookController$refreshLocalBook.BookController$refreshLocalBook$1)$completion;
                if ((bookController$refreshLocalBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$refreshLocalBook.BookController$refreshLocalBook$1 bookController$refreshLocalBook$2 = bookController$refreshLocalBook$1;
                    bookController$refreshLocalBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$refreshLocalBook.BookController$refreshLocalBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        final Book book;
        final ReturnData returnData2;
        switch (((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$0 = this;
                ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$1 = context;
                ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$2 = returnData;
                ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$2;
                context = (RoutingContext)((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$1;
                this = (BookController)((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            case 2: {
                book = (Book)((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$1;
                returnData2 = (ReturnData)((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData2, book, null, 2, null);
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        String bookUrl = null;
        if (context.request().method() == HttpMethod.POST) {
            Intrinsics.checkNotNullExpressionValue((Object)context.getBodyAsJson().getString("bookUrl"), "context.bodyAsJson.getString(\"bookUrl\")");
        }
        else {
            final List queryParam = context.queryParam("bookUrl");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookUrl\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            bookUrl = ((s == null) ? "" : s);
        }
        if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
        if (bookInfo == null) {
            return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
        }
        bookInfo.updateFromLocal(true);
        final BookController bookController2 = this;
        final Book book2 = bookInfo;
        final String userNameSpace2 = userNameSpace;
        final Function1 handler = (Function1)new BookController$refreshLocalBook.BookController$refreshLocalBook$2(bookInfo);
        final Continuation $completion3 = $continuation;
        ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$0 = returnData;
        ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$1 = bookInfo;
        ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).L$2 = null;
        ((BookController$refreshLocalBook.BookController$refreshLocalBook$1)$continuation).label = 2;
        if (bookController2.editShelfBook(book2, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return ReturnData.setData$default(returnData2, book, null, 2, null);
    }
    
    @Nullable
    public final Object getChapterList(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/BookController$getChapterList$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/BookController$getChapterList$1;
        //    11: astore          20
        //    13: aload           20
        //    15: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           20
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/BookController$getChapterList$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/BookController$getChapterList$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          21
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //    67: tableswitch {
        //                0: 116
        //                1: 169
        //                2: 698
        //                3: 833
        //                4: 1038
        //                5: 1183
        //                6: 1337
        //                7: 1482
        //                8: 1772
        //          default: 1803
        //        }
        //   116: aload           $result
        //   118: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   121: new             Lcom/htmake/reader/api/ReturnData;
        //   124: dup            
        //   125: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   128: astore_3        /* returnData */
        //   129: aload_0         /* this */
        //   130: aload_1         /* context */
        //   131: aload           $continuation
        //   133: aload           $continuation
        //   135: aload_0         /* this */
        //   136: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   139: aload           $continuation
        //   141: aload_1         /* context */
        //   142: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   145: aload           $continuation
        //   147: aload_3         /* returnData */
        //   148: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   151: aload           $continuation
        //   153: iconst_1       
        //   154: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //   157: invokevirtual   com/htmake/reader/api/controller/BookController.checkAuth:(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   160: dup            
        //   161: aload           21
        //   163: if_acmpne       203
        //   166: aload           21
        //   168: areturn        
        //   169: aload           $continuation
        //   171: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   174: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   177: astore_3        /* returnData */
        //   178: aload           $continuation
        //   180: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   183: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   186: astore_1        /* context */
        //   187: aload           $continuation
        //   189: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   192: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   195: astore_0        /* this */
        //   196: aload           $result
        //   198: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   201: aload           $result
        //   203: checkcast       Ljava/lang/Boolean;
        //   206: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   209: ifne            227
        //   212: aload_3         /* returnData */
        //   213: ldc             "NEED_LOGIN"
        //   215: aconst_null    
        //   216: iconst_2       
        //   217: aconst_null    
        //   218: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   221: ldc             "\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"
        //   223: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   226: areturn        
        //   227: aconst_null    
        //   228: astore          4
        //   230: iconst_0       
        //   231: istore          5
        //   233: aload_1         /* context */
        //   234: invokeinterface io/vertx/ext/web/RoutingContext.request:()Lio/vertx/core/http/HttpServerRequest;
        //   239: invokeinterface io/vertx/core/http/HttpServerRequest.method:()Lio/vertx/core/http/HttpMethod;
        //   244: getstatic       io/vertx/core/http/HttpMethod.POST:Lio/vertx/core/http/HttpMethod;
        //   247: if_acmpne       348
        //   250: aload_1         /* context */
        //   251: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   256: ldc_w           "url"
        //   259: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   262: astore          7
        //   264: aload           7
        //   266: ifnonnull       290
        //   269: aload_1         /* context */
        //   270: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   275: ldc_w           "book"
        //   278: invokevirtual   io/vertx/core/json/JsonObject.getJsonObject:(Ljava/lang/String;)Lio/vertx/core/json/JsonObject;
        //   281: ldc_w           "bookUrl"
        //   284: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   287: goto            292
        //   290: aload           7
        //   292: astore          6
        //   294: aload           6
        //   296: ifnonnull       305
        //   299: ldc_w           ""
        //   302: goto            307
        //   305: aload           6
        //   307: astore          bookUrl
        //   309: aload_1         /* context */
        //   310: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   315: ldc_w           "refresh"
        //   318: iconst_0       
        //   319: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   322: invokevirtual   io/vertx/core/json/JsonObject.getInteger:(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Integer;
        //   325: astore          6
        //   327: aload           6
        //   329: ldc_w           "context.bodyAsJson.getInteger(\"refresh\", 0)"
        //   332: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   335: aload           6
        //   337: checkcast       Ljava/lang/Number;
        //   340: invokevirtual   java/lang/Number.intValue:()I
        //   343: istore          5
        //   345: goto            463
        //   348: aload_1         /* context */
        //   349: ldc_w           "url"
        //   352: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   357: astore          7
        //   359: aload           7
        //   361: ldc_w           "context.queryParam(\"url\")"
        //   364: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   367: aload           7
        //   369: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   372: checkcast       Ljava/lang/String;
        //   375: astore          6
        //   377: aload           6
        //   379: ifnonnull       388
        //   382: ldc_w           ""
        //   385: goto            390
        //   388: aload           6
        //   390: astore          bookUrl
        //   392: aload_1         /* context */
        //   393: ldc_w           "refresh"
        //   396: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   401: astore          7
        //   403: aload           7
        //   405: ldc_w           "context.queryParam(\"refresh\")"
        //   408: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   411: aload           7
        //   413: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   416: checkcast       Ljava/lang/String;
        //   419: astore          6
        //   421: aload           6
        //   423: ifnonnull       430
        //   426: iconst_0       
        //   427: goto            461
        //   430: aload           6
        //   432: astore          8
        //   434: iconst_0       
        //   435: istore          9
        //   437: aload           8
        //   439: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   442: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   445: astore          7
        //   447: aload           7
        //   449: ifnonnull       456
        //   452: iconst_0       
        //   453: goto            461
        //   456: aload           7
        //   458: invokevirtual   java/lang/Integer.intValue:()I
        //   461: istore          refresh
        //   463: aload           bookUrl
        //   465: checkcast       Ljava/lang/CharSequence;
        //   468: astore          6
        //   470: iconst_0       
        //   471: istore          7
        //   473: aload           6
        //   475: invokeinterface java/lang/CharSequence.length:()I
        //   480: ifne            487
        //   483: iconst_1       
        //   484: goto            488
        //   487: iconst_0       
        //   488: ifeq            499
        //   491: aload_3         /* returnData */
        //   492: ldc_w           "\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5"
        //   495: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   498: areturn        
        //   499: aload_0         /* this */
        //   500: aload_1         /* context */
        //   501: invokevirtual   com/htmake/reader/api/controller/BookController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //   504: astore          userNameSpace
        //   506: aload_0         /* this */
        //   507: aload           bookUrl
        //   509: aload           userNameSpace
        //   511: invokevirtual   com/htmake/reader/api/controller/BookController.getShelfBookByURL:(Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/Book;
        //   514: astore          bookInfo
        //   516: aconst_null    
        //   517: astore          8
        //   519: aload           bookInfo
        //   521: ifnonnull       1412
        //   524: aload_0         /* this */
        //   525: getfield        com/htmake/reader/api/controller/BookController.bookInfoCache:Lio/legado/app/utils/ACache;
        //   528: aload           bookUrl
        //   530: invokevirtual   io/legado/app/utils/ACache.getAsString:(Ljava/lang/String;)Ljava/lang/String;
        //   533: astore          10
        //   535: aload           10
        //   537: ifnonnull       544
        //   540: aconst_null    
        //   541: goto            622
        //   544: aload           10
        //   546: invokestatic    com/htmake/reader/utils/ExtKt.toMap:(Ljava/lang/Object;)Ljava/util/Map;
        //   549: astore          11
        //   551: aload           11
        //   553: ifnonnull       560
        //   556: aconst_null    
        //   557: goto            622
        //   560: aload           11
        //   562: astore          $this$toDataClass$iv
        //   564: iconst_0       
        //   565: istore          $i$f$toDataClass
        //   567: aload           $this$toDataClass$iv
        //   569: astore          $this$convert$iv$iv
        //   571: iconst_0       
        //   572: istore          $i$f$convert
        //   574: aload           $this$convert$iv$iv
        //   576: instanceof      Ljava/lang/String;
        //   579: ifeq            590
        //   582: aload           $this$convert$iv$iv
        //   584: checkcast       Ljava/lang/String;
        //   587: goto            598
        //   590: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   593: aload           $this$convert$iv$iv
        //   595: invokevirtual   com/google/gson/Gson.toJson:(Ljava/lang/Object;)Ljava/lang/String;
        //   598: astore          json$iv$iv
        //   600: invokestatic    com/htmake/reader/utils/ExtKt.getGson:()Lcom/google/gson/Gson;
        //   603: aload           json$iv$iv
        //   605: new             Lcom/htmake/reader/api/controller/BookController$getChapterList$$inlined$toDataClass$1;
        //   608: dup            
        //   609: invokespecial   com/htmake/reader/api/controller/BookController$getChapterList$$inlined$toDataClass$1.<init>:()V
        //   612: invokevirtual   com/htmake/reader/api/controller/BookController$getChapterList$$inlined$toDataClass$1.getType:()Ljava/lang/reflect/Type;
        //   615: invokevirtual   com/google/gson/Gson.fromJson:(Ljava/lang/String;Ljava/lang/reflect/Type;)Ljava/lang/Object;
        //   618: nop            
        //   619: checkcast       Lio/legado/app/data/entities/Book;
        //   622: astore          cacheInfo
        //   624: aload           cacheInfo
        //   626: ifnull          767
        //   629: aload_0         /* this */
        //   630: aload_1         /* context */
        //   631: aload           cacheInfo
        //   633: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //   636: iconst_0       
        //   637: aload           $continuation
        //   639: iconst_4       
        //   640: aconst_null    
        //   641: aload           $continuation
        //   643: aload_0         /* this */
        //   644: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   647: aload           $continuation
        //   649: aload_1         /* context */
        //   650: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   653: aload           $continuation
        //   655: aload_3         /* returnData */
        //   656: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   659: aload           $continuation
        //   661: aload           bookUrl
        //   663: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //   666: aload           $continuation
        //   668: aload           userNameSpace
        //   670: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //   673: aload           $continuation
        //   675: iload           refresh
        //   677: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //   680: aload           $continuation
        //   682: iconst_2       
        //   683: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //   686: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   689: dup            
        //   690: aload           21
        //   692: if_acmpne       759
        //   695: aload           21
        //   697: areturn        
        //   698: aload           $continuation
        //   700: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //   703: istore          5
        //   705: aload           $continuation
        //   707: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //   710: checkcast       Ljava/lang/String;
        //   713: astore          6
        //   715: aload           $continuation
        //   717: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //   720: checkcast       Ljava/lang/String;
        //   723: astore          4
        //   725: aload           $continuation
        //   727: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   730: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   733: astore_3       
        //   734: aload           $continuation
        //   736: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   739: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   742: astore_1       
        //   743: aload           $continuation
        //   745: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   748: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   751: astore_0       
        //   752: aload           $result
        //   754: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   757: aload           $result
        //   759: checkcast       Ljava/lang/String;
        //   762: astore          8
        //   764: goto            899
        //   767: aload_0        
        //   768: aload_1        
        //   769: aconst_null    
        //   770: iconst_0       
        //   771: aload           $continuation
        //   773: bipush          6
        //   775: aconst_null    
        //   776: aload           $continuation
        //   778: aload_0        
        //   779: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   782: aload           $continuation
        //   784: aload_1        
        //   785: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   788: aload           $continuation
        //   790: aload_3        
        //   791: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   794: aload           $continuation
        //   796: aload           4
        //   798: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //   801: aload           $continuation
        //   803: aload           6
        //   805: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //   808: aload           $continuation
        //   810: iload           5
        //   812: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //   815: aload           $continuation
        //   817: iconst_3       
        //   818: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //   821: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   824: dup            
        //   825: aload           21
        //   827: if_acmpne       894
        //   830: aload           21
        //   832: areturn        
        //   833: aload           $continuation
        //   835: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //   838: istore          5
        //   840: aload           $continuation
        //   842: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //   845: checkcast       Ljava/lang/String;
        //   848: astore          6
        //   850: aload           $continuation
        //   852: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //   855: checkcast       Ljava/lang/String;
        //   858: astore          4
        //   860: aload           $continuation
        //   862: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   865: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   868: astore_3       
        //   869: aload           $continuation
        //   871: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   874: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   877: astore_1       
        //   878: aload           $continuation
        //   880: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   883: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   886: astore_0       
        //   887: aload           $result
        //   889: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   892: aload           $result
        //   894: checkcast       Ljava/lang/String;
        //   897: astore          bookSource
        //   899: aload           bookSource
        //   901: checkcast       Ljava/lang/CharSequence;
        //   904: astore          10
        //   906: iconst_0       
        //   907: istore          11
        //   909: iconst_0       
        //   910: istore          12
        //   912: aload           10
        //   914: ifnull          927
        //   917: aload           10
        //   919: invokeinterface java/lang/CharSequence.length:()I
        //   924: ifne            931
        //   927: iconst_1       
        //   928: goto            932
        //   931: iconst_0       
        //   932: ifeq            943
        //   935: aload_3        
        //   936: ldc_w           "\u672a\u914d\u7f6e\u4e66\u6e90"
        //   939: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   942: areturn        
        //   943: aload_0        
        //   944: astore          17
        //   946: new             Lio/legado/app/model/webBook/WebBook;
        //   949: dup            
        //   950: aload           bookSource
        //   952: aload_0        
        //   953: invokevirtual   com/htmake/reader/api/controller/BookController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   956: invokevirtual   com/htmake/reader/config/AppConfig.getDebugLog:()Z
        //   959: aconst_null    
        //   960: aload           6
        //   962: iconst_4       
        //   963: aconst_null    
        //   964: invokespecial   io/legado/app/model/webBook/WebBook.<init>:(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   967: aload           4
        //   969: iconst_0       
        //   970: aload           $continuation
        //   972: iconst_2       
        //   973: aconst_null    
        //   974: aload           $continuation
        //   976: aload_0        
        //   977: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //   980: aload           $continuation
        //   982: aload_1        
        //   983: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //   986: aload           $continuation
        //   988: aload_3        
        //   989: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //   992: aload           $continuation
        //   994: aload           6
        //   996: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //   999: aload           $continuation
        //  1001: aload           bookSource
        //  1003: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1006: aload           $continuation
        //  1008: aload           17
        //  1010: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1013: aload           $continuation
        //  1015: iload           5
        //  1017: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1020: aload           $continuation
        //  1022: iconst_4       
        //  1023: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //  1026: invokestatic    io/legado/app/model/webBook/WebBook.getBookInfo$default:(Lio/legado/app/model/webBook/WebBook;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //  1029: dup            
        //  1030: aload           21
        //  1032: if_acmpne       1109
        //  1035: aload           21
        //  1037: areturn        
        //  1038: aload           $continuation
        //  1040: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1043: istore          5
        //  1045: aload           $continuation
        //  1047: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1050: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //  1053: astore          17
        //  1055: aload           $continuation
        //  1057: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1060: checkcast       Ljava/lang/String;
        //  1063: astore          8
        //  1065: aload           $continuation
        //  1067: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1070: checkcast       Ljava/lang/String;
        //  1073: astore          6
        //  1075: aload           $continuation
        //  1077: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1080: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1083: astore_3       
        //  1084: aload           $continuation
        //  1086: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1089: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1092: astore_1       
        //  1093: aload           $continuation
        //  1095: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1098: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //  1101: astore_0       
        //  1102: aload           $result
        //  1104: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1107: aload           $result
        //  1109: astore          18
        //  1111: aload           17
        //  1113: aload           18
        //  1115: checkcast       Lio/legado/app/data/entities/Book;
        //  1118: aload           $continuation
        //  1120: aload           $continuation
        //  1122: aload_0        
        //  1123: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1126: aload           $continuation
        //  1128: aload_1        
        //  1129: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1132: aload           $continuation
        //  1134: aload_3        
        //  1135: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1138: aload           $continuation
        //  1140: aload           6
        //  1142: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1145: aload           $continuation
        //  1147: aload           8
        //  1149: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1152: aload           $continuation
        //  1154: aconst_null    
        //  1155: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1158: aload           $continuation
        //  1160: iload           5
        //  1162: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1165: aload           $continuation
        //  1167: iconst_5       
        //  1168: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //  1171: invokevirtual   com/htmake/reader/api/controller/BookController.mergeBookCacheInfo:(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1174: dup            
        //  1175: aload           21
        //  1177: if_acmpne       1244
        //  1180: aload           21
        //  1182: areturn        
        //  1183: aload           $continuation
        //  1185: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1188: istore          5
        //  1190: aload           $continuation
        //  1192: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1195: checkcast       Ljava/lang/String;
        //  1198: astore          8
        //  1200: aload           $continuation
        //  1202: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1205: checkcast       Ljava/lang/String;
        //  1208: astore          6
        //  1210: aload           $continuation
        //  1212: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1215: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1218: astore_3       
        //  1219: aload           $continuation
        //  1221: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1224: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1227: astore_1       
        //  1228: aload           $continuation
        //  1230: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1233: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //  1236: astore_0       
        //  1237: aload           $result
        //  1239: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1242: aload           $result
        //  1244: checkcast       Lio/legado/app/data/entities/Book;
        //  1247: astore          7
        //  1249: aload_0        
        //  1250: iconst_1       
        //  1251: anewarray       Lio/legado/app/data/entities/Book;
        //  1254: astore          10
        //  1256: aload           10
        //  1258: iconst_0       
        //  1259: aload           7
        //  1261: aastore        
        //  1262: aload           10
        //  1264: invokestatic    kotlin/collections/CollectionsKt.arrayListOf:([Ljava/lang/Object;)Ljava/util/ArrayList;
        //  1267: checkcast       Ljava/util/List;
        //  1270: aload           $continuation
        //  1272: aload           $continuation
        //  1274: aload_0        
        //  1275: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1278: aload           $continuation
        //  1280: aload_1        
        //  1281: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1284: aload           $continuation
        //  1286: aload_3        
        //  1287: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1290: aload           $continuation
        //  1292: aload           6
        //  1294: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1297: aload           $continuation
        //  1299: aload           7
        //  1301: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1304: aload           $continuation
        //  1306: aload           8
        //  1308: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1311: aload           $continuation
        //  1313: iload           5
        //  1315: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1318: aload           $continuation
        //  1320: bipush          6
        //  1322: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //  1325: invokevirtual   com/htmake/reader/api/controller/BookController.saveBookInfoCache:(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1328: dup            
        //  1329: aload           21
        //  1331: if_acmpne       1408
        //  1334: aload           21
        //  1336: areturn        
        //  1337: aload           $continuation
        //  1339: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1342: istore          5
        //  1344: aload           $continuation
        //  1346: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1349: checkcast       Ljava/lang/String;
        //  1352: astore          8
        //  1354: aload           $continuation
        //  1356: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1359: checkcast       Lio/legado/app/data/entities/Book;
        //  1362: astore          7
        //  1364: aload           $continuation
        //  1366: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1369: checkcast       Ljava/lang/String;
        //  1372: astore          6
        //  1374: aload           $continuation
        //  1376: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1379: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1382: astore_3       
        //  1383: aload           $continuation
        //  1385: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1388: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1391: astore_1       
        //  1392: aload           $continuation
        //  1394: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1397: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //  1400: astore_0       
        //  1401: aload           $result
        //  1403: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1406: aload           $result
        //  1408: pop            
        //  1409: goto            1548
        //  1412: aload_0        
        //  1413: aload_1        
        //  1414: aload           7
        //  1416: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //  1419: iconst_0       
        //  1420: aload           $continuation
        //  1422: iconst_4       
        //  1423: aconst_null    
        //  1424: aload           $continuation
        //  1426: aload_0        
        //  1427: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1430: aload           $continuation
        //  1432: aload_1        
        //  1433: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1436: aload           $continuation
        //  1438: aload_3        
        //  1439: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1442: aload           $continuation
        //  1444: aload           6
        //  1446: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1449: aload           $continuation
        //  1451: aload           7
        //  1453: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1456: aload           $continuation
        //  1458: iload           5
        //  1460: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1463: aload           $continuation
        //  1465: bipush          7
        //  1467: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //  1470: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //  1473: dup            
        //  1474: aload           21
        //  1476: if_acmpne       1543
        //  1479: aload           21
        //  1481: areturn        
        //  1482: aload           $continuation
        //  1484: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.I$0:I
        //  1487: istore          5
        //  1489: aload           $continuation
        //  1491: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1494: checkcast       Lio/legado/app/data/entities/Book;
        //  1497: astore          7
        //  1499: aload           $continuation
        //  1501: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1504: checkcast       Ljava/lang/String;
        //  1507: astore          6
        //  1509: aload           $continuation
        //  1511: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1514: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1517: astore_3       
        //  1518: aload           $continuation
        //  1520: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1523: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1526: astore_1       
        //  1527: aload           $continuation
        //  1529: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1532: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //  1535: astore_0       
        //  1536: aload           $result
        //  1538: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1541: aload           $result
        //  1543: checkcast       Ljava/lang/String;
        //  1546: astore          8
        //  1548: aload           7
        //  1550: invokevirtual   io/legado/app/data/entities/Book.isLocalBook:()Z
        //  1553: ifne            1600
        //  1556: aload           8
        //  1558: checkcast       Ljava/lang/CharSequence;
        //  1561: astore          9
        //  1563: iconst_0       
        //  1564: istore          10
        //  1566: iconst_0       
        //  1567: istore          11
        //  1569: aload           9
        //  1571: ifnull          1584
        //  1574: aload           9
        //  1576: invokeinterface java/lang/CharSequence.length:()I
        //  1581: ifne            1588
        //  1584: iconst_1       
        //  1585: goto            1589
        //  1588: iconst_0       
        //  1589: ifeq            1600
        //  1592: aload_3        
        //  1593: ldc_w           "\u672a\u914d\u7f6e\u4e66\u6e90"
        //  1596: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //  1599: areturn        
        //  1600: aload           7
        //  1602: aconst_null    
        //  1603: iconst_1       
        //  1604: aconst_null    
        //  1605: invokestatic    com/htmake/reader/utils/ExtKt.getWorkDir$default:(Ljava/lang/String;ILjava/lang/Object;)Ljava/lang/String;
        //  1608: invokevirtual   io/legado/app/data/entities/Book.setRootDir:(Ljava/lang/String;)V
        //  1611: aload           7
        //  1613: aload           6
        //  1615: invokevirtual   io/legado/app/data/entities/Book.setUserNameSpace:(Ljava/lang/String;)V
        //  1618: aload           7
        //  1620: invokevirtual   io/legado/app/data/entities/Book.isLocalBook:()Z
        //  1623: ifeq            1662
        //  1626: aload           7
        //  1628: invokevirtual   io/legado/app/data/entities/Book.getLocalFile:()Ljava/io/File;
        //  1631: astore          localFile
        //  1633: aload           localFile
        //  1635: invokevirtual   java/io/File.exists:()Z
        //  1638: ifne            1662
        //  1641: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
        //  1644: ldc_w           "localFile: {} not exists"
        //  1647: aload           localFile
        //  1649: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
        //  1654: aload_3        
        //  1655: ldc_w           "\u672c\u5730\u4e66\u7c4d\u6e90\u6587\u4ef6\u4e0d\u5b58\u5728"
        //  1658: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //  1661: areturn        
        //  1662: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
        //  1665: ldc_w           "bookInfo: {}"
        //  1668: aload           7
        //  1670: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
        //  1675: aload_0        
        //  1676: aload           7
        //  1678: aload           8
        //  1680: astore          10
        //  1682: aload           10
        //  1684: ifnonnull       1693
        //  1687: ldc_w           ""
        //  1690: goto            1695
        //  1693: aload           10
        //  1695: iload           5
        //  1697: ifle            1704
        //  1700: iconst_1       
        //  1701: goto            1705
        //  1704: iconst_0       
        //  1705: aload_0        
        //  1706: aload_1        
        //  1707: invokevirtual   com/htmake/reader/api/controller/BookController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //  1710: iconst_0       
        //  1711: aconst_null    
        //  1712: aload           $continuation
        //  1714: bipush          48
        //  1716: aconst_null    
        //  1717: aload           $continuation
        //  1719: aload_3        
        //  1720: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1723: aload           $continuation
        //  1725: aconst_null    
        //  1726: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$1:Ljava/lang/Object;
        //  1729: aload           $continuation
        //  1731: aconst_null    
        //  1732: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$2:Ljava/lang/Object;
        //  1735: aload           $continuation
        //  1737: aconst_null    
        //  1738: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$3:Ljava/lang/Object;
        //  1741: aload           $continuation
        //  1743: aconst_null    
        //  1744: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$4:Ljava/lang/Object;
        //  1747: aload           $continuation
        //  1749: aconst_null    
        //  1750: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$5:Ljava/lang/Object;
        //  1753: aload           $continuation
        //  1755: bipush          8
        //  1757: putfield        com/htmake/reader/api/controller/BookController$getChapterList$1.label:I
        //  1760: invokestatic    com/htmake/reader/api/controller/BookController.getLocalChapterList$default:(Lcom/htmake/reader/api/controller/BookController;Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //  1763: dup            
        //  1764: aload           21
        //  1766: if_acmpne       1788
        //  1769: aload           21
        //  1771: areturn        
        //  1772: aload           $continuation
        //  1774: getfield        com/htmake/reader/api/controller/BookController$getChapterList$1.L$0:Ljava/lang/Object;
        //  1777: checkcast       Lcom/htmake/reader/api/ReturnData;
        //  1780: astore_3       
        //  1781: aload           $result
        //  1783: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1786: aload           $result
        //  1788: checkcast       Ljava/util/List;
        //  1791: astore          chapterList
        //  1793: aload_3        
        //  1794: aload           chapterList
        //  1796: aconst_null    
        //  1797: iconst_2       
        //  1798: aconst_null    
        //  1799: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //  1802: areturn        
        //  1803: new             Ljava/lang/IllegalStateException;
        //  1806: dup            
        //  1807: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1810: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1813: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lcom/htmake/reader/api/ReturnData;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 39 27 FF 00 0A 00 15 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 03 9E 00 00 FF 00 41 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 34 FF 00 21 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 17 FF 00 3E 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 00 07 00 60 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 41 07 00 60 FF 00 0C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 07 00 60 07 00 60 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 41 07 00 60 FF 00 28 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 27 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 07 00 60 07 01 A9 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 41 07 00 60 FF 00 27 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 A9 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 19 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 03 B9 07 00 60 01 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 04 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 13 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 01 FF 00 01 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 01 13 07 01 13 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 17 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 01 4D 01 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 40 01 0A FF 00 2C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 0F 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 00 07 00 60 07 01 B7 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 1D 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 00 07 00 60 07 01 B7 07 01 B7 01 07 01 B7 01 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 47 07 00 60 FF 00 17 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 84 FF 00 4B 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 3C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 07 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 07 01 84 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 41 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 3C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 04 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 1B 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 00 07 00 60 00 07 01 4D 01 01 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 03 40 01 0A FF 00 5E 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 46 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 07 00 02 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 49 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 3C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 5C 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 46 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 03 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 05 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 45 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 3C 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 04 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 23 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 07 01 4D 01 01 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 03 40 01 FF 00 0A 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 3D FF 00 1E 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 02 07 00 02 07 01 84 FF 00 01 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 03 07 00 02 07 01 84 07 00 60 FF 00 08 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 03 07 00 02 07 01 84 07 00 60 FF 00 00 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 07 00 60 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 04 07 00 02 07 01 84 07 00 60 01 FF 00 42 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00 FF 00 0F 00 16 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 01 07 01 13 FF 00 0E 00 16 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 03 9E 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.UnsupportedOperationException
        //     at java.base/java.util.Collections$1.remove(Collections.java:4821)
        //     at java.base/java.util.AbstractCollection.removeAll(AbstractCollection.java:369)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:3018)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    public final Object saveBookProgress(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveBookProgress.BookController$saveBookProgress$1) {
                final BookController$saveBookProgress.BookController$saveBookProgress$1 bookController$saveBookProgress$1 = (BookController$saveBookProgress.BookController$saveBookProgress$1)$completion;
                if ((bookController$saveBookProgress$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveBookProgress.BookController$saveBookProgress$1 bookController$saveBookProgress$2 = bookController$saveBookProgress$1;
                    bookController$saveBookProgress$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveBookProgress.BookController$saveBookProgress$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Book l$3 = null;
        String l$4 = null;
        ReturnData returnData2 = null;
        BookChapter chapterInfo = null;
        Label_0896: {
            int n = 0;
            Object localChapterList$default = null;
            Label_0742: {
                ReturnData returnData = null;
                Object checkAuth = null;
                switch (((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final BookController bookController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0 = this;
                        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1 = context;
                        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2 = returnData;
                        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).label = 1;
                        if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1;
                        this = (BookController)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        checkAuth = $result;
                        break;
                    }
                    case 2: {
                        n = ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).I$0;
                        l$3 = (Book)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$3;
                        l$4 = (String)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2;
                        returnData2 = (ReturnData)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1;
                        this = (BookController)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        localChapterList$default = $result;
                        break Label_0742;
                    }
                    case 3: {
                        chapterInfo = (BookChapter)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$4;
                        l$3 = (Book)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$3;
                        l$4 = (String)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2;
                        returnData2 = (ReturnData)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1;
                        this = (BookController)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break Label_0896;
                    }
                    case 4: {
                        returnData2 = (ReturnData)((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        return ReturnData.setData$default(returnData2, "", null, 2, null);
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (!(boolean)checkAuth) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                }
                String bookUrl;
                int chapterIndex = 0;
                if (context.request().method() == HttpMethod.POST) {
                    final String string = context.getBodyAsJson().getString("url");
                    final String s = (string == null) ? context.getBodyAsJson().getJsonObject("searchBook").getString("bookUrl") : string;
                    bookUrl = ((s == null) ? "" : s);
                    final Integer integer = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
                    Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"index\", -1)");
                    n = integer.intValue();
                }
                else {
                    final List queryParam = context.queryParam("url");
                    Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                    final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                    bookUrl = ((s2 == null) ? "" : s2);
                    final List queryParam2 = context.queryParam("index");
                    Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"index\")");
                    final String s3 = (String)CollectionsKt.firstOrNull(queryParam2);
                    int n2;
                    if (s3 == null) {
                        n2 = -1;
                    }
                    else {
                        final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                        n2 = ((boxInt == null) ? -1 : boxInt);
                    }
                    chapterIndex = n2;
                }
                if (bookUrl.length() == 0) {
                    return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
                }
                final String userNameSpace = this.getUserNameSpace(context);
                final Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                if (bookInfo == null || bookInfo.getOrigin().length() == 0) {
                    return returnData.setErrorMsg("\u4e66\u7c4d\u672a\u52a0\u5165\u4e66\u67b6");
                }
                final String bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                if (!bookInfo.isLocalBook()) {
                    final CharSequence charSequence = bookSource;
                    if (charSequence == null || charSequence.length() == 0) {
                        return returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
                    }
                }
                final BookController bookController2 = this;
                final Book book = bookInfo;
                final String s4 = bookSource;
                final String s5 = (s4 == null) ? "" : s4;
                final boolean b = false;
                final String s6 = userNameSpace;
                final boolean b2 = false;
                final Mutex mutex = null;
                final Continuation continuation = $continuation;
                final int n3 = 48;
                final Object o = null;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0 = this;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1 = returnData;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2 = userNameSpace;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$3 = bookInfo;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).I$0 = chapterIndex;
                ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).label = 2;
                if ((localChapterList$default = getLocalChapterList$default(bookController2, book, s5, b, s6, b2, mutex, continuation, n3, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final List chapterList = (List)localChapterList$default;
            if (n >= chapterList.size()) {
                return returnData2.setErrorMsg("\u7ae0\u8282\u4e0d\u5b58\u5728");
            }
            chapterInfo = (BookChapter)chapterList.get(n);
            final BookController bookController3 = this;
            final Book book2 = l$3;
            final BookChapter bookChapter = chapterInfo;
            final String userNameSpace2 = l$4;
            final Continuation $completion3 = $continuation;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0 = this;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1 = returnData2;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2 = l$4;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$3 = l$3;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$4 = chapterInfo;
            ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).label = 3;
            if (bookController3.saveShelfBookProgress(book2, bookChapter, userNameSpace2, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final BookController bookController4 = this;
        final Book book3 = l$3;
        final BookChapter bookChapter2 = chapterInfo;
        final String userNameSpace3 = l$4;
        final Continuation $completion4 = $continuation;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$0 = returnData2;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$1 = null;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$2 = null;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$3 = null;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).L$4 = null;
        ((BookController$saveBookProgress.BookController$saveBookProgress$1)$continuation).label = 4;
        if (bookController4.saveBookProgressToWebdav(book3, bookChapter2, userNameSpace3, (Continuation<? super Unit>)$completion4) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final Object getBookContent(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getBookContent.BookController$getBookContent$1) {
                final BookController$getBookContent.BookController$getBookContent$1 bookController$getBookContent$1 = (BookController$getBookContent.BookController$getBookContent$1)$completion;
                if ((bookController$getBookContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getBookContent.BookController$getBookContent$1 bookController$getBookContent$2 = bookController$getBookContent$1;
                    bookController$getBookContent$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getBookContent.BookController$getBookContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        BookController bookController;
        RoutingContext context2;
        Continuation $completion2;
        Object checkAuth = null;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int j = 0;
        String l$3 = null;
        String url = null;
        ReturnData returnData2 = null;
        Object bookSourceString$default = null;
        int i$4 = 0;
        String url2;
        BookChapter chapter = null;
        Book book = null;
        String s = null;
        Object bookSourceString$default2 = null;
        BookController l$4 = null;
        String s2 = null;
        Object bookInfo$default = null;
        Object mergeBookCacheInfo = null;
        Object localChapterList$default = null;
        List chapterList = null;
        String s3;
        WebBook webBook;
        Book book2;
        BookChapter bookChapter;
        String nextChapterUrl2;
        Continuation $completion3;
        File chapterCacheFile;
        Object bookContent2;
        String updateImageLinkInContent = null;
        File file;
        BookHelp instance;
        CoroutineScope scope;
        BookSource.Companion companion;
        String s4;
        Object fromJson-IoAF18A;
        BookSource bookSource2;
        BookSource bookSource3;
        Book book3;
        BookChapter bookChapter2;
        String content2;
        Continuation $completion4;
        CharSequence charSequence;
        Object fromJson-IoAF18A2;
        BookSource bookSourceObject;
        Map info;
        String string;
        JsonObject jsonObject;
        String s5;
        String string2;
        String chapterUrl;
        String string3;
        JsonObject jsonObject2;
        String s6;
        String string4;
        String bookUrl;
        Integer integer;
        int chapterIndex;
        Integer integer2;
        int cache;
        Integer integer3;
        int refresh;
        Integer integer4;
        List queryParam;
        String s7;
        List queryParam2;
        String s8;
        List queryParam3;
        String s9;
        int n4;
        Integer boxInt;
        List queryParam4;
        String s10;
        int n5;
        Integer boxInt2;
        List queryParam5;
        String s11;
        int n6;
        Integer boxInt3;
        List queryParam6;
        String s12;
        int n7;
        Integer boxInt4;
        int epubContent = 0;
        BookController bookController2;
        RoutingContext routingContext;
        String s13;
        boolean b;
        Continuation continuation;
        int n8;
        Object o;
        String bookSource;
        String userNameSpace;
        boolean isInBookShelf;
        Book bookInfo;
        BookChapter chapterInfo;
        String nextChapterUrl;
        String asString;
        Book book4;
        Map<String, Object> map;
        Map $this$toDataClass$iv;
        int $i$f$toDataClass;
        Object $this$convert$iv$iv;
        int $i$f$convert;
        String json$iv$iv;
        Book cacheInfo;
        BookController bookController3;
        RoutingContext routingContext2;
        String origin;
        boolean b2;
        Continuation continuation2;
        int n9;
        Object o2;
        CharSequence charSequence2;
        Book book5;
        Book book6 = null;
        String s14;
        WebBook webBook2;
        String s15;
        boolean b3;
        Continuation continuation3;
        int n10;
        Object o3;
        Object o4;
        BookController bookController4;
        Book book7;
        Continuation $completion5;
        BookController bookController5;
        Book book8;
        String s16;
        String s17;
        boolean b4;
        String s18;
        boolean b5;
        Mutex mutex;
        Continuation continuation4;
        int n11;
        Object o5;
        BookController bookController6;
        Book book9;
        BookChapter bookChapter3;
        String userNameSpace2;
        Continuation $completion6;
        BookController bookController7;
        Book book10;
        BookChapter bookChapter4;
        String userNameSpace3;
        Continuation $completion7;
        BookChapter nextChapterInfo;
        CharSequence charSequence3;
        File localFile;
        String epubRootDir;
        String chapterFilePath;
        String content = null;
        String chapterFilePath2;
        File chapterFile;
        BaseController baseController;
        String name;
        String fileExt$default;
        String lowerCase;
        String ext;
        List imageExt;
        String fileUrl;
        Long start;
        long longValue;
        Long end;
        Long start2;
        long longValue2;
        Long end2;
        long longValue3;
        long i;
        String chapterFilePath3;
        File chapterFile2;
        String fileUrl2;
        String content3;
        String bookContent;
        File localCacheDir;
        Label_4760_Outer:Label_4684_Outer:
        while (true) {
            Label_2872: {
                Label_2827: {
                    Label_2631: {
                        Label_2394: {
                            Label_2162: {
                                Label_2154: {
                                    Label_1955: {
                                        Label_1575: {
                                            Label_1570: {
                                                Label_1140: {
                                                    while (true) {
                                                        switch (((BookController$getBookContent.BookController$getBookContent$1)$continuation).label) {
                                                            case 0: {
                                                                ResultKt.throwOnFailure($result);
                                                                returnData = new ReturnData();
                                                                bookController = this;
                                                                context2 = context;
                                                                $completion2 = $continuation;
                                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = context;
                                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = returnData;
                                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 1;
                                                                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                                                                    return coroutine_SUSPENDED;
                                                                }
                                                                break;
                                                            }
                                                            case 1: {
                                                                returnData = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                context = (RoutingContext)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                checkAuth = $result;
                                                                break;
                                                            }
                                                            case 2: {
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n3 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                l$3 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                url = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                context = (RoutingContext)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                bookSourceString$default = $result;
                                                                break Label_1140;
                                                            }
                                                            case 3: {
                                                                i$4 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4;
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n3 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                url2 = null;
                                                                chapter = null;
                                                                book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                l$3 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                url = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                bookSourceString$default2 = $result;
                                                                break Label_1570;
                                                            }
                                                            case 4: {
                                                                i$4 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4;
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n3 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                l$4 = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                url2 = null;
                                                                chapter = null;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                url = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                bookInfo$default = $result;
                                                                break Label_1955;
                                                            }
                                                            case 5: {
                                                                i$4 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4;
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n3 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                url2 = null;
                                                                chapter = null;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                url = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                mergeBookCacheInfo = $result;
                                                                break Label_2154;
                                                            }
                                                            case 6: {
                                                                i$4 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4;
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n3 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                url2 = null;
                                                                chapter = null;
                                                                book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                url = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                localChapterList$default = $result;
                                                                break Label_2394;
                                                            }
                                                            case 7: {
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                chapterList = (List)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6;
                                                                url2 = null;
                                                                chapter = (BookChapter)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                break Label_2631;
                                                            }
                                                            case 8: {
                                                                n = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2;
                                                                n2 = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1;
                                                                j = ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0;
                                                                chapterList = (List)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6;
                                                                url2 = null;
                                                                chapter = (BookChapter)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                ResultKt.throwOnFailure($result);
                                                                break Label_2827;
                                                            }
                                                            case 9: {
                                                                Label_4403: {
                                                                    break Label_4403;
                                                                    try {
                                                                        s3 = s2;
                                                                        webBook = new WebBook((s3 == null) ? "" : s3, this.getAppConfig().getDebugLog(), null, s, 4, null);
                                                                        book2 = book;
                                                                        bookChapter = chapter;
                                                                        nextChapterUrl2 = url2;
                                                                        $completion3 = $continuation;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = s2;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = book;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = chapter;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6 = chapterCacheFile;
                                                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 9;
                                                                        if ((bookContent2 = webBook.getBookContent(book2, bookChapter, nextChapterUrl2, (Continuation<? super String>)$completion3)) == coroutine_SUSPENDED) {
                                                                            return coroutine_SUSPENDED;
                                                                        }
                                                                        while (true) {
                                                                            while (true) {
                                                                                updateImageLinkInContent = (String)bookContent2;
                                                                                if (this.getAppConfig().getCacheChapterContent() && file != null) {
                                                                                    FilesKt.writeText$default(file, updateImageLinkInContent, (Charset)null, 2, (Object)null);
                                                                                    instance = BookHelp.INSTANCE;
                                                                                    scope = (CoroutineScope)this;
                                                                                    companion = BookSource.Companion;
                                                                                    s4 = s2;
                                                                                    fromJson-IoAF18A = companion.fromJson-IoAF18A((s4 == null) ? "" : s4);
                                                                                    bookSource2 = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                                                                                    bookSource3 = ((bookSource2 == null) ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource2);
                                                                                    book3 = book;
                                                                                    bookChapter2 = chapter;
                                                                                    content2 = updateImageLinkInContent;
                                                                                    $completion4 = $continuation;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = s2;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = book;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = chapter;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6 = updateImageLinkInContent;
                                                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 10;
                                                                                    if (instance.saveImages(scope, bookSource3, book3, bookChapter2, content2, (Continuation<? super Unit>)$completion4) == coroutine_SUSPENDED) {
                                                                                        return coroutine_SUSPENDED;
                                                                                    }
                                                                                    updateImageLinkInContent = this.updateImageLinkInContent(book, chapter, updateImageLinkInContent);
                                                                                    return ReturnData.setData$default(returnData2, updateImageLinkInContent, null, 2, null);
                                                                                }
                                                                                return ReturnData.setData$default(returnData2, updateImageLinkInContent, null, 2, null);
                                                                                file = (File)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6;
                                                                                chapter = (BookChapter)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                                book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                                s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                                s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                                returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                                this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                                ResultKt.throwOnFailure($result);
                                                                                bookContent2 = $result;
                                                                                continue Label_4760_Outer;
                                                                            }
                                                                            updateImageLinkInContent = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6;
                                                                            chapter = (BookChapter)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5;
                                                                            book = (Book)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4;
                                                                            s = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3;
                                                                            s2 = (String)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2;
                                                                            returnData2 = (ReturnData)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1;
                                                                            this = (BookController)((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0;
                                                                            ResultKt.throwOnFailure($result);
                                                                            continue Label_4684_Outer;
                                                                        }
                                                                    }
                                                                    catch (final Exception e) {
                                                                        charSequence = s2;
                                                                        if (charSequence != null && charSequence.length() != 0) {
                                                                            fromJson-IoAF18A2 = BookSource.Companion.fromJson-IoAF18A(s2);
                                                                            bookSourceObject = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A2) ? null : fromJson-IoAF18A2);
                                                                            if (bookSourceObject != null) {
                                                                                info = MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)"sourceUrl", (Object)bookSourceObject.getBookSourceUrl()), TuplesKt.to((Object)"time", (Object)Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to((Object)"error", (Object)e.toString()) });
                                                                                this.addInvalidBookSource(bookSourceObject.getBookSourceUrl(), info, s);
                                                                            }
                                                                        }
                                                                        throw e;
                                                                    }
                                                                }
                                                                return ReturnData.setData$default(returnData2, updateImageLinkInContent, null, 2, null);
                                                            }
                                                            case 10: {
                                                                continue;
                                                            }
                                                            default: {
                                                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    if (!(boolean)checkAuth) {
                                                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                                                    }
                                                    if (context.request().method() == HttpMethod.POST) {
                                                        string = context.getBodyAsJson().getString("chapterUrl");
                                                        if (string == null) {
                                                            jsonObject = context.getBodyAsJson().getJsonObject("bookChapter");
                                                            if (jsonObject == null) {
                                                                s5 = "";
                                                            }
                                                            else {
                                                                string2 = jsonObject.getString("url");
                                                                s5 = ((string2 == null) ? "" : string2);
                                                            }
                                                        }
                                                        else {
                                                            s5 = string;
                                                        }
                                                        chapterUrl = s5;
                                                        string3 = context.getBodyAsJson().getString("url");
                                                        if (string3 == null) {
                                                            jsonObject2 = context.getBodyAsJson().getJsonObject("searchBook");
                                                            if (jsonObject2 == null) {
                                                                s6 = "";
                                                            }
                                                            else {
                                                                string4 = jsonObject2.getString("bookUrl");
                                                                s6 = ((string4 == null) ? "" : string4);
                                                            }
                                                        }
                                                        else {
                                                            s6 = string3;
                                                        }
                                                        bookUrl = s6;
                                                        integer = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
                                                        Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"index\", -1)");
                                                        chapterIndex = integer.intValue();
                                                        integer2 = context.getBodyAsJson().getInteger("cache", Boxing.boxInt(0));
                                                        Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"cache\", 0)");
                                                        cache = integer2.intValue();
                                                        integer3 = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                                                        Intrinsics.checkNotNullExpressionValue((Object)integer3, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                                                        refresh = integer3.intValue();
                                                        integer4 = context.getBodyAsJson().getInteger("epubContent", Boxing.boxInt(0));
                                                        Intrinsics.checkNotNullExpressionValue((Object)integer4, "context.bodyAsJson.getInteger(\"epubContent\", 0)");
                                                        n = integer4.intValue();
                                                    }
                                                    else {
                                                        queryParam = context.queryParam("chapterUrl");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"chapterUrl\")");
                                                        s7 = (String)CollectionsKt.firstOrNull(queryParam);
                                                        chapterUrl = ((s7 == null) ? "" : s7);
                                                        queryParam2 = context.queryParam("url");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"url\")");
                                                        s8 = (String)CollectionsKt.firstOrNull(queryParam2);
                                                        bookUrl = ((s8 == null) ? "" : s8);
                                                        queryParam3 = context.queryParam("index");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"index\")");
                                                        s9 = (String)CollectionsKt.firstOrNull(queryParam3);
                                                        if (s9 == null) {
                                                            n4 = -1;
                                                        }
                                                        else {
                                                            boxInt = Boxing.boxInt(Integer.parseInt(s9));
                                                            n4 = ((boxInt == null) ? -1 : boxInt);
                                                        }
                                                        chapterIndex = n4;
                                                        queryParam4 = context.queryParam("cache");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"cache\")");
                                                        s10 = (String)CollectionsKt.firstOrNull(queryParam4);
                                                        if (s10 == null) {
                                                            n5 = 0;
                                                        }
                                                        else {
                                                            boxInt2 = Boxing.boxInt(Integer.parseInt(s10));
                                                            n5 = ((boxInt2 == null) ? 0 : boxInt2);
                                                        }
                                                        cache = n5;
                                                        queryParam5 = context.queryParam("refresh");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"refresh\")");
                                                        s11 = (String)CollectionsKt.firstOrNull(queryParam5);
                                                        if (s11 == null) {
                                                            n6 = 0;
                                                        }
                                                        else {
                                                            boxInt3 = Boxing.boxInt(Integer.parseInt(s11));
                                                            n6 = ((boxInt3 == null) ? 0 : boxInt3);
                                                        }
                                                        refresh = n6;
                                                        queryParam6 = context.queryParam("epubContent");
                                                        Intrinsics.checkNotNullExpressionValue((Object)queryParam6, "context.queryParam(\"epubContent\")");
                                                        s12 = (String)CollectionsKt.firstOrNull(queryParam6);
                                                        if (s12 == null) {
                                                            n7 = 0;
                                                        }
                                                        else {
                                                            boxInt4 = Boxing.boxInt(Integer.parseInt(s12));
                                                            n7 = ((boxInt4 == null) ? 0 : boxInt4);
                                                        }
                                                        epubContent = n7;
                                                    }
                                                    if (bookUrl.length() == 0) {
                                                        return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
                                                    }
                                                    bookController2 = this;
                                                    routingContext = context;
                                                    s13 = null;
                                                    b = false;
                                                    continuation = $continuation;
                                                    n8 = 6;
                                                    o = null;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = context;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = returnData;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = chapterUrl;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = bookUrl;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = chapterIndex;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = cache;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = refresh;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3 = epubContent;
                                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 2;
                                                    if ((bookSourceString$default = getBookSourceString$default(bookController2, routingContext, s13, b, continuation, n8, o)) == coroutine_SUSPENDED) {
                                                        return coroutine_SUSPENDED;
                                                    }
                                                }
                                                bookSource = (String)bookSourceString$default;
                                                userNameSpace = this.getUserNameSpace(context);
                                                isInBookShelf = false;
                                                bookInfo = null;
                                                chapterInfo = null;
                                                nextChapterUrl = null;
                                                if (l$3.length() <= 0) {
                                                    break Label_2872;
                                                }
                                                bookInfo = this.getShelfBookByURL(l$3, userNameSpace);
                                                if (bookInfo != null && bookInfo.getOrigin().length() > 0) {
                                                    isInBookShelf = true;
                                                    bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                                                }
                                                asString = this.bookInfoCache.getAsString(l$3);
                                                if (asString == null) {
                                                    book4 = null;
                                                }
                                                else {
                                                    map = ExtKt.toMap(asString);
                                                    if (map == null) {
                                                        book4 = null;
                                                    }
                                                    else {
                                                        $this$toDataClass$iv = map;
                                                        $i$f$toDataClass = 0;
                                                        $this$convert$iv$iv = $this$toDataClass$iv;
                                                        $i$f$convert = 0;
                                                        json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                                                        book4 = (Book)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
                                                    }
                                                }
                                                cacheInfo = book4;
                                                if (cacheInfo == null) {
                                                    break Label_1575;
                                                }
                                                bookController3 = this;
                                                routingContext2 = context;
                                                origin = cacheInfo.getOrigin();
                                                b2 = false;
                                                continuation2 = $continuation;
                                                n9 = 4;
                                                o2 = null;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = url;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = l$3;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = userNameSpace;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = bookInfo;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n3;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n2;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3 = n;
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4 = (isInBookShelf ? 1 : 0);
                                                ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 3;
                                                if ((bookSourceString$default2 = getBookSourceString$default(bookController3, routingContext2, origin, b2, continuation2, n9, o2)) == coroutine_SUSPENDED) {
                                                    return coroutine_SUSPENDED;
                                                }
                                            }
                                            s2 = (String)bookSourceString$default2;
                                        }
                                        if (url.length() != 0 || j < 0) {
                                            break Label_2872;
                                        }
                                        if (l$3.length() == 0) {
                                            return returnData2.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
                                        }
                                        if (book != null && !book.isLocalBook()) {
                                            charSequence2 = s2;
                                            if (charSequence2 == null || charSequence2.length() == 0) {
                                                return returnData2.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
                                            }
                                        }
                                        book5 = book;
                                        if (book5 != null) {
                                            book6 = book5;
                                            break Label_2162;
                                        }
                                        l$4 = this;
                                        s14 = s2;
                                        webBook2 = new WebBook((s14 == null) ? "" : s14, this.getAppConfig().getDebugLog(), null, s, 4, null);
                                        s15 = l$3;
                                        b3 = false;
                                        continuation3 = $continuation;
                                        n10 = 2;
                                        o3 = null;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = url;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s2;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = s;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = l$4;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n3;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n2;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3 = n;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4 = i$4;
                                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 4;
                                        if ((bookInfo$default = WebBook.getBookInfo$default(webBook2, s15, b3, continuation3, n10, o3)) == coroutine_SUSPENDED) {
                                            return coroutine_SUSPENDED;
                                        }
                                    }
                                    o4 = bookInfo$default;
                                    bookController4 = l$4;
                                    book7 = (Book)o4;
                                    $completion5 = $continuation;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = url;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s2;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = s;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = null;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n3;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n2;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3 = n;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4 = i$4;
                                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 5;
                                    if ((mergeBookCacheInfo = bookController4.mergeBookCacheInfo(book7, (Continuation<? super Book>)$completion5)) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                }
                                book6 = (Book)mergeBookCacheInfo;
                            }
                            book = book6;
                            bookController5 = this;
                            book8 = book;
                            s16 = s2;
                            s17 = ((s16 == null) ? "" : s16);
                            b4 = false;
                            s18 = s;
                            b5 = false;
                            mutex = null;
                            continuation4 = $continuation;
                            n11 = 48;
                            o5 = null;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = url;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s2;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = s;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = book;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n3;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n2;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$3 = n;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$4 = i$4;
                            ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 6;
                            if ((localChapterList$default = getLocalChapterList$default(bookController5, book8, s17, b4, s18, b5, mutex, continuation4, n11, o5)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                        }
                        chapterList = (List)localChapterList$default;
                        if (j >= chapterList.size()) {
                            break Label_2872;
                        }
                        chapter = chapterList.get(j);
                        if (i$4 == 0 || n3 == 1) {
                            break Label_2827;
                        }
                        bookController6 = this;
                        book9 = book;
                        bookChapter3 = chapter;
                        userNameSpace2 = s;
                        $completion6 = $continuation;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = s2;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = book;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = chapter;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6 = chapterList;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n2;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n;
                        ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 7;
                        if (bookController6.saveShelfBookProgress(book9, bookChapter3, userNameSpace2, (Continuation<? super Unit>)$completion6) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                    }
                    bookController7 = this;
                    book10 = book;
                    bookChapter4 = chapter;
                    userNameSpace3 = s;
                    $completion7 = $continuation;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$0 = this;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$1 = returnData2;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$2 = s2;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$3 = s;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$4 = book;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$5 = chapter;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).L$6 = chapterList;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$0 = j;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$1 = n2;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).I$2 = n;
                    ((BookController$getBookContent.BookController$getBookContent$1)$continuation).label = 8;
                    if (bookController7.saveBookProgressToWebdav(book10, bookChapter4, userNameSpace3, (Continuation<? super Unit>)$completion7) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                url = chapter.getUrl();
                if (j + 1 < chapterList.size()) {
                    nextChapterInfo = (BookChapter)chapterList.get(j + 1);
                    url2 = nextChapterInfo.getUrl();
                }
            }
            if (book == null) {
                return returnData2.setErrorMsg("\u83b7\u53d6\u4e66\u7c4d\u4fe1\u606f\u5931\u8d25");
            }
            if (!book.isLocalBook()) {
                charSequence3 = s2;
                if (charSequence3 == null || charSequence3.length() == 0) {
                    return returnData2.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
                }
            }
            if (chapter == null || url.length() == 0) {
                return returnData2.setErrorMsg("\u83b7\u53d6\u7ae0\u8282\u94fe\u63a5\u5931\u8d25");
            }
            book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
            book.setUserNameSpace(s);
            if (book.isLocalBook()) {
                localFile = book.getLocalFile();
                if (!localFile.exists()) {
                    return returnData2.setErrorMsg("\u672c\u5730\u6e90\u4e66\u7c4d\u6587\u4ef6\u4e0d\u5b58\u5728");
                }
                if (book.isEpub()) {
                    if (!extractEpub$default(this, book, false, 2, null)) {
                        return returnData2.setErrorMsg("Epub\u4e66\u7c4d\u89e3\u538b\u5931\u8d25");
                    }
                    epubRootDir = book.getEpubRootDir();
                    chapterFilePath = ExtKt.getWorkDir(book.getBookUrl(), "index", epubRootDir, chapter.getUrl());
                    BookControllerKt.access$getLogger$p().info("chapterFilePath: {} {}", (Object)chapterFilePath, (Object)epubRootDir);
                    if (!new File(chapterFilePath).exists()) {
                        return returnData2.setErrorMsg("\u7ae0\u8282\u6587\u4ef6\u4e0d\u5b58\u5728");
                    }
                    if (epubRootDir.length() == 0) {
                        new StringBuilder().append(StringsKt.replace$default(StringsKt.replace$default(book.getBookUrl(), "\\", "/", false, 4, (Object)null), "storage/data/", "/book-assets/", false, 4, (Object)null)).append("/index/").append(chapter.getUrl()).toString();
                    }
                    else {
                        content = StringsKt.replace$default(StringsKt.replace$default(book.getBookUrl(), "\\", "/", false, 4, (Object)null), "storage/data/", "/book-assets/", false, 4, (Object)null) + "/index/" + epubRootDir + '/' + chapter.getUrl();
                    }
                    if (n > 0) {
                        return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"url", (Object)Intrinsics.stringPlus("__API_ROOT__", (Object)content)), TuplesKt.to((Object)"content", (Object)FilesKt.readText$default(new File(chapterFilePath), (Charset)null, 1, (Object)null)) }), null, 2, null);
                    }
                    return ReturnData.setData$default(returnData2, content, null, 2, null);
                }
                else if (book.isCbz()) {
                    if (!extractCbz$default(this, book, false, 2, null)) {
                        return returnData2.setErrorMsg("CBZ\u4e66\u7c4d\u89e3\u538b\u5931\u8d25");
                    }
                    chapterFilePath2 = ExtKt.getWorkDir(book.getBookUrl(), "index", chapter.getUrl());
                    BookControllerKt.access$getLogger$p().info("chapterFilePath: {}", (Object)chapterFilePath2);
                    chapterFile = new File(chapterFilePath2);
                    if (!chapterFile.exists()) {
                        return returnData2.setErrorMsg("\u7ae0\u8282\u6587\u4ef6\u4e0d\u5b58\u5728");
                    }
                    baseController = this;
                    name = chapterFile.getName();
                    Intrinsics.checkNotNullExpressionValue((Object)name, "chapterFile.name");
                    fileExt$default = BaseController.getFileExt$default(baseController, name, null, 2, null);
                    if (fileExt$default == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    lowerCase = fileExt$default.toLowerCase(Locale.ROOT);
                    Intrinsics.checkNotNullExpressionValue((Object)lowerCase, "(this as java.lang.Strin\u2026.toLowerCase(Locale.ROOT)");
                    ext = lowerCase;
                    imageExt = CollectionsKt.listOf((Object[])new String[] { "jpg", "jpeg", "gif", "png", "bmp", "webp", "svg" });
                    fileUrl = "__API_ROOT__" + StringsKt.replace$default(StringsKt.replace$default(book.getBookUrl(), "\\", "/", false, 4, (Object)null), "storage/data/", "/book-assets/", false, 4, (Object)null) + "/index/" + chapter.getUrl();
                    if (!imageExt.contains(ext)) {
                        return ReturnData.setData$default(returnData2, fileUrl, null, 2, null);
                    }
                    content = "<img src='" + fileUrl + "' />";
                    return ReturnData.setData$default(returnData2, content, null, 2, null);
                }
                else if (book.isPdf()) {
                    if (!convertPdfToImage$default(this, book, false, 2, null)) {
                        return returnData2.setErrorMsg("PDF\u751f\u6210\u56fe\u7247\u5931\u8d25");
                    }
                    content = "";
                    if (chapter.getStart() != null && chapter.getEnd() != null) {
                        start = chapter.getStart();
                        Intrinsics.checkNotNull((Object)start);
                        longValue = start;
                        end = chapter.getEnd();
                        Intrinsics.checkNotNull((Object)end);
                        if (longValue <= end) {
                            start2 = chapter.getStart();
                            Intrinsics.checkNotNull((Object)start2);
                            longValue2 = start2;
                            end2 = chapter.getEnd();
                            Intrinsics.checkNotNull((Object)end2);
                            longValue3 = end2;
                            if (longValue2 <= longValue3) {
                                do {
                                    i = longValue2;
                                    ++longValue2;
                                    this.convertPdfPageToImage(book, (int)i, n2 > 0);
                                    chapterFilePath3 = ExtKt.getWorkDir(book.getBookUrl(), "index", "output-" + i + ".png");
                                    BookControllerKt.access$getLogger$p().info("chapterFilePath: {}", (Object)chapterFilePath3);
                                    chapterFile2 = new File(chapterFilePath3);
                                    if (!chapterFile2.exists()) {
                                        return returnData2.setErrorMsg("\u7ae0\u8282\u6587\u4ef6\u4e0d\u5b58\u5728");
                                    }
                                    fileUrl2 = "__API_ROOT__" + StringsKt.replace$default(StringsKt.replace$default(book.getBookUrl(), "\\", "/", false, 4, (Object)null), "storage/data/", "/book-assets/", false, 4, (Object)null) + "/index/output-" + i + ".png";
                                    content = content + "<img src='" + fileUrl2 + "' />";
                                } while (i != longValue3);
                            }
                        }
                    }
                    return ReturnData.setData$default(returnData2, content, null, 2, null);
                }
                else {
                    content3 = LocalBook.INSTANCE.getContent(book, chapter);
                    if (content3 == null) {
                        return returnData2.setErrorMsg("\u83b7\u53d6\u7ae0\u8282\u5185\u5bb9\u5931\u8d25");
                    }
                    bookContent = (content = content3);
                }
            }
            else {
                chapterCacheFile = null;
                if (!book.isInShelf() || !this.getAppConfig().getCacheChapterContent()) {
                    continue;
                }
                localCacheDir = this.getChapterCacheDir(book, s);
                chapterCacheFile = new File(localCacheDir.getAbsolutePath() + (Object)File.separator + j + ".txt");
                if (n2 <= 0 && chapterCacheFile.exists()) {
                    content = FilesKt.readText$default(chapterCacheFile, (Charset)null, 1, (Object)null);
                    if (StringsKt.indexOf$default((CharSequence)content, "<img", 0, false, 6, (Object)null) >= 0) {
                        content = this.updateImageLinkInContent(book, chapter, content);
                    }
                    BookControllerKt.access$getLogger$p().info("\u4f7f\u7528\u7f13\u5b58\u7684\u7ae0\u8282\u5185\u5bb9: {}", (Object)chapterCacheFile.toString());
                    return ReturnData.setData$default(returnData2, content, null, 2, null);
                }
                continue;
            }
            break;
        }
        return ReturnData.setData$default(returnData2, updateImageLinkInContent, null, 2, null);
    }
    
    @Nullable
    public final Object saveBookContent(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveBookContent.BookController$saveBookContent$1) {
                final BookController$saveBookContent.BookController$saveBookContent$1 bookController$saveBookContent$1 = (BookController$saveBookContent.BookController$saveBookContent$1)$completion;
                if ((bookController$saveBookContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveBookContent.BookController$saveBookContent$1 bookController$saveBookContent$2 = bookController$saveBookContent$1;
                    bookController$saveBookContent$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveBookContent.BookController$saveBookContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$0 = this;
                ((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$1 = context;
                ((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$2 = returnData;
                ((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$2;
                context = (RoutingContext)((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$1;
                this = (BookController)((BookController$saveBookContent.BookController$saveBookContent$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final String string = context.getBodyAsJson().getString("url");
        final String bookUrl = (string == null) ? "" : string;
        final Integer chapterIndex = context.getBodyAsJson().getInteger("index", Boxing.boxInt(-1));
        final String string2 = context.getBodyAsJson().getString("content");
        final String content = (string2 == null) ? "" : string2;
        if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
        if (bookInfo == null) {
            return returnData.setErrorMsg("\u83b7\u53d6\u4e66\u7c4d\u4fe1\u606f\u5931\u8d25");
        }
        final File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
        final File chapterCacheFile = new File(localCacheDir.getAbsolutePath() + (Object)File.separator + chapterIndex + ".txt");
        FilesKt.writeText$default(chapterCacheFile, content, (Charset)null, 2, (Object)null);
        final String customCacheDirPath = ExtKt.getWorkDir("storage", "data", userNameSpace, bookInfo.getName() + '_' + bookInfo.getAuthor(), "custom");
        final File customCacheDir = new File(customCacheDirPath);
        if (!customCacheDir.exists()) {
            customCacheDir.mkdirs();
        }
        final File cacheFile = new File(customCacheDir.getAbsolutePath() + (Object)File.separator + chapterIndex + ".txt");
        FilesKt.writeText$default(cacheFile, content, (Charset)null, 2, (Object)null);
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    private final String updateImageLinkInContent(final Book book, final BookChapter chapter, final String content) {
        final StringBuilder data = new StringBuilder("");
        final String dataDir = ExtKt.getWorkDir("storage", "data");
        final Iterable $this$forEach$iv = StringsKt.split$default((CharSequence)content, new String[] { "\n" }, false, 0, 6, (Object)null);
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final String text = (String)element$iv;
            final int n = 0;
            Object text2 = null;
            text2 = text;
            final Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(text);
            while (matcher.find()) {
                final String group = matcher.group(1);
                if (group == null) {
                    continue;
                }
                final String it = group;
                final int n2 = 0;
                if (StringsKt.indexOf$default((CharSequence)it, "__API_ROOT__", 0, false, 6, (Object)null) >= 0) {
                    continue;
                }
                final String src = NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), it);
                final File imageFile = BookHelp.INSTANCE.getImage(book, src);
                if (!imageFile.exists()) {
                    continue;
                }
                final String s = "__API_ROOT__";
                final String path = imageFile.getPath();
                Intrinsics.checkNotNullExpressionValue((Object)path, "imageFile.path");
                final String imageUrl = Intrinsics.stringPlus(s, (Object)StringsKt.replace$default(path, dataDir, "/book-assets", false, 4, (Object)null));
                text2 = StringsKt.replace$default((String)text2, it, imageUrl + "\" data-error=\"" + it, false, 4, (Object)null);
            }
            data.append((String)text2).append("\n");
        }
        final String string = data.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "data.toString()");
        return string;
    }
    
    @Nullable
    public final Object exploreBook(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$exploreBook.BookController$exploreBook$1) {
                final BookController$exploreBook.BookController$exploreBook$1 bookController$exploreBook$1 = (BookController$exploreBook.BookController$exploreBook$1)$completion;
                if ((bookController$exploreBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$exploreBook.BookController$exploreBook$1 bookController$exploreBook$2 = bookController$exploreBook$1;
                    bookController$exploreBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$exploreBook.BookController$exploreBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$exploreBook.BookController$exploreBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object exploreBook = null;
        Label_0598: {
            ReturnData returnData = null;
            Object bookSourceString$default = null;
            Label_0263: {
                switch (((BookController$exploreBook.BookController$exploreBook$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final BookController bookController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0 = this;
                        ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$1 = context;
                        ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$2 = returnData;
                        ((BookController$exploreBook.BookController$exploreBook$1)$continuation).label = 1;
                        if (bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$1;
                        this = (BookController)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break;
                    }
                    case 2: {
                        returnData = (ReturnData)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$1;
                        this = (BookController)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        bookSourceString$default = $result;
                        break Label_0263;
                    }
                    case 3: {
                        returnData2 = (ReturnData)((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        exploreBook = $result;
                        break Label_0598;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                final BookController bookController2 = this;
                final RoutingContext routingContext = context;
                final String s = null;
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0 = this;
                ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$1 = context;
                ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$2 = returnData;
                ((BookController$exploreBook.BookController$exploreBook$1)$continuation).label = 2;
                if ((bookSourceString$default = getBookSourceString$default(bookController2, routingContext, s, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final String bookSource = (String)bookSourceString$default;
            final CharSequence charSequence = bookSource;
            if (charSequence == null || charSequence.length() == 0) {
                return returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
            }
            String ruleFindUrl;
            int page = 0;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("ruleFindUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"ruleFindUrl\")");
                ruleFindUrl = string;
                final Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                integer.intValue();
            }
            else {
                final List queryParam = context.queryParam("ruleFindUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"ruleFindUrl\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                ruleFindUrl = ((s2 == null) ? "" : s2);
                final List queryParam2 = context.queryParam("page");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"page\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam2);
                int n2;
                if (s3 == null) {
                    n2 = 1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                    n2 = ((boxInt == null) ? 1 : boxInt);
                }
                page = n2;
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final WebBook webBook = new WebBook(bookSource, false, null, userNameSpace, 4, null);
            final String url = ruleFindUrl;
            final Integer boxInt2 = Boxing.boxInt(page);
            final Continuation $completion3 = $continuation;
            ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$0 = returnData;
            ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$1 = null;
            ((BookController$exploreBook.BookController$exploreBook$1)$continuation).L$2 = null;
            ((BookController$exploreBook.BookController$exploreBook$1)$continuation).label = 3;
            if ((exploreBook = webBook.exploreBook(url, boxInt2, (Continuation<? super List<SearchBook>>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final List result = (List)exploreBook;
        return ReturnData.setData$default(returnData2, result, null, 2, null);
    }
    
    @Nullable
    public final Object searchBook(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBook.BookController$searchBook$1) {
                final BookController$searchBook.BookController$searchBook$1 bookController$searchBook$1 = (BookController$searchBook.BookController$searchBook$1)$completion;
                if ((bookController$searchBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBook.BookController$searchBook$1 bookController$searchBook$2 = bookController$searchBook$1;
                    bookController$searchBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBook.BookController$searchBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBook.BookController$searchBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object searchBook = null;
        Label_0643: {
            ReturnData returnData = null;
            Object bookSourceString$default = null;
            Label_0263: {
                switch (((BookController$searchBook.BookController$searchBook$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final BookController bookController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((BookController$searchBook.BookController$searchBook$1)$continuation).L$0 = this;
                        ((BookController$searchBook.BookController$searchBook$1)$continuation).L$1 = context;
                        ((BookController$searchBook.BookController$searchBook$1)$continuation).L$2 = returnData;
                        ((BookController$searchBook.BookController$searchBook$1)$continuation).label = 1;
                        if (bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((BookController$searchBook.BookController$searchBook$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$searchBook.BookController$searchBook$1)$continuation).L$1;
                        this = (BookController)((BookController$searchBook.BookController$searchBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break;
                    }
                    case 2: {
                        returnData = (ReturnData)((BookController$searchBook.BookController$searchBook$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$searchBook.BookController$searchBook$1)$continuation).L$1;
                        this = (BookController)((BookController$searchBook.BookController$searchBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        bookSourceString$default = $result;
                        break Label_0263;
                    }
                    case 3: {
                        returnData2 = (ReturnData)((BookController$searchBook.BookController$searchBook$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        searchBook = $result;
                        break Label_0643;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                final BookController bookController2 = this;
                final RoutingContext routingContext = context;
                final String s = null;
                final boolean b = false;
                final Continuation continuation = $continuation;
                final int n = 6;
                final Object o = null;
                ((BookController$searchBook.BookController$searchBook$1)$continuation).L$0 = this;
                ((BookController$searchBook.BookController$searchBook$1)$continuation).L$1 = context;
                ((BookController$searchBook.BookController$searchBook$1)$continuation).L$2 = returnData;
                ((BookController$searchBook.BookController$searchBook$1)$continuation).label = 2;
                if ((bookSourceString$default = getBookSourceString$default(bookController2, routingContext, s, b, continuation, n, o)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final String bookSource = (String)bookSourceString$default;
            final CharSequence charSequence = bookSource;
            if (charSequence == null || charSequence.length() == 0) {
                return returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
            }
            String key;
            int page = 0;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("key");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"key\")");
                key = string;
                final Integer integer = context.getBodyAsJson().getInteger("page", Boxing.boxInt(1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"page\", 1)");
                integer.intValue();
            }
            else {
                final List queryParam = context.queryParam("key");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"key\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                key = ((s2 == null) ? "" : s2);
                final List queryParam2 = context.queryParam("page");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"page\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam2);
                int n2;
                if (s3 == null) {
                    n2 = 1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                    n2 = ((boxInt == null) ? 1 : boxInt);
                }
                page = n2;
            }
            if (key.length() == 0) {
                return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u5b57");
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final WebBook webBook = new WebBook(bookSource, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
            final String key2 = key;
            final Integer boxInt2 = Boxing.boxInt(page);
            final Continuation $completion3 = $continuation;
            ((BookController$searchBook.BookController$searchBook$1)$continuation).L$0 = returnData;
            ((BookController$searchBook.BookController$searchBook$1)$continuation).L$1 = null;
            ((BookController$searchBook.BookController$searchBook$1)$continuation).L$2 = null;
            ((BookController$searchBook.BookController$searchBook$1)$continuation).label = 3;
            if ((searchBook = webBook.searchBook(key2, boxInt2, (Continuation<? super List<SearchBook>>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final List result = (List)searchBook;
        return ReturnData.setData$default(returnData2, result, null, 2, null);
    }
    
    @Nullable
    public final Object searchBookMulti(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBookMulti.BookController$searchBookMulti$1) {
                final BookController$searchBookMulti.BookController$searchBookMulti$1 bookController$searchBookMulti$1 = (BookController$searchBookMulti.BookController$searchBookMulti$1)$completion;
                if ((bookController$searchBookMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookMulti.BookController$searchBookMulti$1 bookController$searchBookMulti$2 = bookController$searchBookMulti$1;
                    bookController$searchBookMulti$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBookMulti.BookController$searchBookMulti$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        final Ref$ObjectRef ref$ObjectRef;
        final Ref$IntRef ref$IntRef;
        final ReturnData returnData2;
        switch (((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$0 = this;
                ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$1 = context;
                ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$2 = returnData;
                ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$2;
                context = (RoutingContext)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$1;
                this = (BookController)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            case 2: {
                ref$ObjectRef = (Ref$ObjectRef)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$2;
                ref$IntRef = (Ref$IntRef)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$1;
                returnData2 = (ReturnData)((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(ref$IntRef.element)), TuplesKt.to((Object)"list", ref$ObjectRef.element) }), null, 2, null);
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final Ref$IntRef lastIndex = new Ref$IntRef();
        final Ref$IntRef searchSize = new Ref$IntRef();
        final Ref$ObjectRef bookSourceGroup = new Ref$ObjectRef();
        String key;
        int concurrentCount = 0;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("key", "");
            Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"key\", \"\")");
            key = string;
            final Ref$ObjectRef ref$ObjectRef2 = bookSourceGroup;
            final String string2 = context.getBodyAsJson().getString("bookSourceGroup", "");
            Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
            ref$ObjectRef2.element = string2;
            final Ref$IntRef ref$IntRef2 = lastIndex;
            final Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
            Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
            ref$IntRef2.element = integer.intValue();
            final Ref$IntRef ref$IntRef3 = searchSize;
            final Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(20));
            Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"searchSize\", 20)");
            ref$IntRef3.element = integer2.intValue();
            final Integer integer3 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(36));
            Intrinsics.checkNotNullExpressionValue((Object)integer3, "context.bodyAsJson.getInteger(\"concurrentCount\", 36)");
            integer3.intValue();
        }
        else {
            final List queryParam = context.queryParam("key");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"key\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            key = ((s == null) ? "" : s);
            final Ref$ObjectRef ref$ObjectRef3 = bookSourceGroup;
            final List queryParam2 = context.queryParam("bookSourceGroup");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"bookSourceGroup\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
            ref$ObjectRef3.element = ((s2 == null) ? "" : s2);
            final Ref$IntRef ref$IntRef4 = lastIndex;
            final List queryParam3 = context.queryParam("lastIndex");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"lastIndex\")");
            final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
            int element;
            if (s3 == null) {
                element = -1;
            }
            else {
                final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                element = ((boxInt == null) ? -1 : boxInt);
            }
            ref$IntRef4.element = element;
            final Ref$IntRef ref$IntRef5 = searchSize;
            final List queryParam4 = context.queryParam("searchSize");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"searchSize\")");
            final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
            int element2;
            if (s4 == null) {
                element2 = 20;
            }
            else {
                final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s4));
                element2 = ((boxInt2 == null) ? 20 : boxInt2);
            }
            ref$IntRef5.element = element2;
            final List queryParam5 = context.queryParam("concurrentCount");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"concurrentCount\")");
            final String s5 = (String)CollectionsKt.firstOrNull(queryParam5);
            int n;
            if (s5 == null) {
                n = 36;
            }
            else {
                final Integer boxInt3 = Boxing.boxInt(Integer.parseInt(s5));
                n = ((boxInt3 == null) ? 36 : boxInt3);
            }
            concurrentCount = n;
        }
        final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
        userNameSpace.element = this.getUserNameSpace(context);
        final Map urlMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String)userNameSpace.element);
        if (urlMap.size() <= 0) {
            return returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
        }
        if (key.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u5b57");
        }
        final Ref$BooleanRef accurate = new Ref$BooleanRef();
        if (StringsKt.startsWith(key, "=", true)) {
            accurate.element = true;
            key = StringsKt.replaceFirst$default(key, "=", "", false, 4, (Object)null);
        }
        final CharSequence charSequence = key;
        if (charSequence == null || charSequence.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u5b57");
        }
        if (lastIndex.element >= urlMap.size() - 1) {
            return returnData.setErrorMsg("\u6ca1\u6709\u66f4\u591a\u4e86");
        }
        searchSize.element = ((searchSize.element > 0) ? searchSize.element : 20);
        concurrentCount = ((concurrentCount > 0) ? concurrentCount : 36);
        BookControllerKt.access$getLogger$p().info("searchBookMulti from lastIndex: {} searchSize: {}", (Object)Boxing.boxInt(lastIndex.element), (Object)Boxing.boxInt(searchSize.element));
        final Ref$BooleanRef isEnd = new Ref$BooleanRef();
        context.request().connection().closeHandler(BookController::searchBookMulti$lambda-5);
        final Ref$ObjectRef resultList = new Ref$ObjectRef();
        resultList.element = new ArrayList();
        final Ref$ObjectRef resultMap = new Ref$ObjectRef();
        resultMap.element = new LinkedHashMap<Object, Object>();
        final Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
        book.setName(key);
        final Ref$IntRef maxSize = new Ref$IntRef();
        maxSize.element = urlMap.size();
        final Ref$ObjectRef bookSourceFile = new Ref$ObjectRef();
        bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", (String)userNameSpace.element, "bookSource" }, null, 2, null);
        if (!((File)bookSourceFile.element).exists()) {
            bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
        }
        final BookController bookController2 = this;
        final int concurrentCount2 = concurrentCount;
        final int startIndex = lastIndex.element + 1;
        final int size = urlMap.size();
        final Function3 handler = (Function3)new BookController$searchBookMulti.BookController$searchBookMulti$3(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, accurate, userNameSpace, (Continuation)null);
        final Function2 needContinue = (Function2)new BookController$searchBookMulti.BookController$searchBookMulti$4(resultList, isEnd, this, searchSize, resultMap);
        final Continuation $completion3 = $continuation;
        ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$0 = returnData;
        ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$1 = lastIndex;
        ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).L$2 = resultList;
        ((BookController$searchBookMulti.BookController$searchBookMulti$1)$continuation).label = 2;
        if (bookController2.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(ref$IntRef.element)), TuplesKt.to((Object)"list", ref$ObjectRef.element) }), null, 2, null);
    }
    
    @Nullable
    public final Object searchBookMultiSSE(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1) {
                final BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1 bookController$searchBookMultiSSE$1 = (BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$completion;
                if ((bookController$searchBookMultiSSE$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1 bookController$searchBookMultiSSE$2 = bookController$searchBookMultiSSE$1;
                    bookController$searchBookMultiSSE$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Ref$IntRef maxSize = null;
        final Ref$IntRef ref$IntRef;
        final HttpServerResponse httpServerResponse;
        Label_1738: {
            ReturnData returnData = null;
            HttpServerResponse response = null;
            Object checkAuth = null;
            switch (((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$0 = this;
                    ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$1 = context;
                    ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$2 = returnData;
                    ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$3 = response;
                    ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    response = (HttpServerResponse)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$3;
                    returnData = (ReturnData)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$1;
                    this = (BookController)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    maxSize = (Ref$IntRef)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$2;
                    ref$IntRef = (Ref$IntRef)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$1;
                    httpServerResponse = (HttpServerResponse)((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_1738;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final Ref$IntRef lastIndex = new Ref$IntRef();
            final Ref$IntRef searchSize = new Ref$IntRef();
            final Ref$ObjectRef bookSourceGroup = new Ref$ObjectRef();
            String key;
            int concurrentCount = 0;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("key", "");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"key\", \"\")");
                key = string;
                final Ref$ObjectRef ref$ObjectRef = bookSourceGroup;
                final String string2 = context.getBodyAsJson().getString("bookSourceGroup", "");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                ref$ObjectRef.element = string2;
                final Ref$IntRef ref$IntRef2 = lastIndex;
                final Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                ref$IntRef2.element = integer.intValue();
                final Ref$IntRef ref$IntRef3 = searchSize;
                final Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(50));
                Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"searchSize\", 50)");
                ref$IntRef3.element = integer2.intValue();
                final Integer integer3 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
                Intrinsics.checkNotNullExpressionValue((Object)integer3, "context.bodyAsJson.getInteger(\"concurrentCount\", 24)");
                integer3.intValue();
            }
            else {
                final List queryParam = context.queryParam("key");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"key\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                key = ((s == null) ? "" : s);
                final Ref$ObjectRef ref$ObjectRef2 = bookSourceGroup;
                final List queryParam2 = context.queryParam("bookSourceGroup");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"bookSourceGroup\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                ref$ObjectRef2.element = ((s2 == null) ? "" : s2);
                final Ref$IntRef ref$IntRef4 = lastIndex;
                final List queryParam3 = context.queryParam("lastIndex");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"lastIndex\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
                int element;
                if (s3 == null) {
                    element = -1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                    element = ((boxInt == null) ? -1 : boxInt);
                }
                ref$IntRef4.element = element;
                final Ref$IntRef ref$IntRef5 = searchSize;
                final List queryParam4 = context.queryParam("searchSize");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"searchSize\")");
                final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
                int element2;
                if (s4 == null) {
                    element2 = 50;
                }
                else {
                    final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s4));
                    element2 = ((boxInt2 == null) ? 50 : boxInt2);
                }
                ref$IntRef5.element = element2;
                final List queryParam5 = context.queryParam("concurrentCount");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"concurrentCount\")");
                final String s5 = (String)CollectionsKt.firstOrNull(queryParam5);
                int n;
                if (s5 == null) {
                    n = 24;
                }
                else {
                    final Integer boxInt3 = Boxing.boxInt(Integer.parseInt(s5));
                    n = ((boxInt3 == null) ? 24 : boxInt3);
                }
                concurrentCount = n;
            }
            final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
            userNameSpace.element = this.getUserNameSpace(context);
            final Map urlMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String)userNameSpace.element);
            if (urlMap.size() <= 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            if (key.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u5b57"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final Ref$BooleanRef accurate = new Ref$BooleanRef();
            if (StringsKt.startsWith(key, "=", true)) {
                accurate.element = true;
                key = StringsKt.replaceFirst$default(key, "=", "", false, 4, (Object)null);
            }
            final CharSequence charSequence = key;
            if (charSequence == null || charSequence.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u5b57"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            if (lastIndex.element >= urlMap.size() - 1) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u6ca1\u6709\u66f4\u591a\u4e86"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            searchSize.element = ((searchSize.element > 0) ? searchSize.element : 50);
            concurrentCount = ((concurrentCount > 0) ? concurrentCount : 24);
            BookControllerKt.access$getLogger$p().info("searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}", new Object[] { Boxing.boxInt(lastIndex.element), Boxing.boxInt(concurrentCount), Boxing.boxInt(searchSize.element) });
            final Ref$BooleanRef isEnd = new Ref$BooleanRef();
            context.request().connection().closeHandler(BookController::searchBookMultiSSE$lambda-6);
            final Ref$ObjectRef resultList = new Ref$ObjectRef();
            resultList.element = new ArrayList();
            final Book book = new Book(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
            book.setName(key);
            maxSize = new Ref$IntRef();
            maxSize.element = urlMap.size();
            final Ref$ObjectRef bookSourceFile = new Ref$ObjectRef();
            bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", (String)userNameSpace.element, "bookSource" }, null, 2, null);
            if (!((File)bookSourceFile.element).exists()) {
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
            }
            final BookController bookController2 = this;
            final int concurrentCount2 = concurrentCount;
            final int startIndex = lastIndex.element + 1;
            final int size = urlMap.size();
            final Function3 handler = (Function3)new BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$3(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, accurate, userNameSpace, (Continuation)null);
            final Function2 needContinue = (Function2)new BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$4(response, lastIndex, resultList, isEnd, this, searchSize);
            final Continuation $completion3 = $continuation;
            ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$0 = response;
            ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$1 = lastIndex;
            ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$2 = maxSize;
            ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).L$3 = null;
            ((BookController$searchBookMultiSSE.BookController$searchBookMultiSSE$1)$continuation).label = 2;
            if (bookController2.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        httpServerResponse.write("event: end\n");
        httpServerResponse.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(ref$IntRef.element)), TuplesKt.to((Object)"isEnd", (Object)Boxing.boxBoolean(ref$IntRef.element >= maxSize.element)) }), false) + "\n\n");
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object searchBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBookSource.BookController$searchBookSource$1) {
                final BookController$searchBookSource.BookController$searchBookSource$1 bookController$searchBookSource$1 = (BookController$searchBookSource.BookController$searchBookSource$1)$completion;
                if ((bookController$searchBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookSource.BookController$searchBookSource$1 bookController$searchBookSource$2 = bookController$searchBookSource$1;
                    bookController$searchBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBookSource.BookController$searchBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Ref$ObjectRef ref$ObjectRef;
        final Ref$ObjectRef ref$ObjectRef2;
        final Ref$ObjectRef ref$ObjectRef3;
        final Ref$IntRef ref$IntRef;
        final ReturnData returnData2;
        Label_1333: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$0 = this;
                    ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$1 = context;
                    ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$2 = returnData;
                    ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$1;
                    this = (BookController)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    ref$ObjectRef = (Ref$ObjectRef)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$5;
                    ref$ObjectRef2 = (Ref$ObjectRef)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$4;
                    ref$ObjectRef3 = (Ref$ObjectRef)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$3;
                    ref$IntRef = (Ref$IntRef)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$2;
                    returnData2 = (ReturnData)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$1;
                    this = (BookController)((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_1333;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final Ref$IntRef lastIndex = new Ref$IntRef();
            final Ref$IntRef searchSize = new Ref$IntRef();
            final Ref$ObjectRef bookSourceGroup = new Ref$ObjectRef();
            String bookUrl;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("url");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"url\")");
                bookUrl = string;
                final Ref$IntRef ref$IntRef2 = lastIndex;
                final Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                ref$IntRef2.element = integer.intValue();
                final Ref$IntRef ref$IntRef3 = searchSize;
                final Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(5));
                Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"searchSize\", 5)");
                ref$IntRef3.element = integer2.intValue();
                final Ref$ObjectRef ref$ObjectRef4 = bookSourceGroup;
                final String string2 = context.getBodyAsJson().getString("bookSourceGroup", "");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                ref$ObjectRef4.element = string2;
            }
            else {
                final List queryParam = context.queryParam("url");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookUrl = ((s == null) ? "" : s);
                final Ref$IntRef ref$IntRef4 = lastIndex;
                final List queryParam2 = context.queryParam("lastIndex");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"lastIndex\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                int element;
                if (s2 == null) {
                    element = -1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s2));
                    element = ((boxInt == null) ? -1 : boxInt);
                }
                ref$IntRef4.element = element;
                final Ref$IntRef ref$IntRef5 = searchSize;
                final List queryParam3 = context.queryParam("searchSize");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"searchSize\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
                int element2;
                if (s3 == null) {
                    element2 = 5;
                }
                else {
                    final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s3));
                    element2 = ((boxInt2 == null) ? 5 : boxInt2);
                }
                ref$IntRef5.element = element2;
                final Ref$ObjectRef ref$ObjectRef5 = bookSourceGroup;
                final List queryParam4 = context.queryParam("bookSourceGroup");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"bookSourceGroup\")");
                final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
                ref$ObjectRef5.element = ((s4 == null) ? "" : s4);
            }
            final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
            userNameSpace.element = this.getUserNameSpace(context);
            final Map urlMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String)userNameSpace.element);
            if (urlMap.size() <= 0) {
                return returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
            }
            if (bookUrl.length() == 0) {
                return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
            }
            if (lastIndex.element >= urlMap.size() - 1) {
                return returnData.setErrorMsg("\u6ca1\u6709\u66f4\u591a\u4e86");
            }
            final Ref$ObjectRef book = new Ref$ObjectRef();
            book.element = this.getShelfBookByURL(bookUrl, (String)userNameSpace.element);
            if (book.element == null) {
                final Ref$ObjectRef ref$ObjectRef6 = book;
                final String asString = this.bookInfoCache.getAsString(bookUrl);
                Object element3;
                if (asString == null) {
                    element3 = null;
                }
                else {
                    final Map<String, Object> map = ExtKt.toMap(asString);
                    if (map == null) {
                        element3 = null;
                    }
                    else {
                        final Map $this$toDataClass$iv = map;
                        final int $i$f$toDataClass = 0;
                        final Object $this$convert$iv$iv = $this$toDataClass$iv;
                        final int $i$f$convert = 0;
                        final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                        element3 = ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
                    }
                }
                ref$ObjectRef6.element = element3;
            }
            if (book.element == null) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
            }
            BookControllerKt.access$getLogger$p().info("searchBookSource from lastIndex: {}", (Object)Boxing.boxInt(lastIndex.element));
            final Ref$BooleanRef isEnd = new Ref$BooleanRef();
            context.request().connection().closeHandler(BookController::searchBookSource$lambda-7);
            searchSize.element = ((searchSize.element > 0) ? searchSize.element : 5);
            final Ref$ObjectRef resultList = new Ref$ObjectRef();
            resultList.element = new ArrayList();
            final int concurrentCount = Math.max(searchSize.element * 2, 24);
            final Ref$IntRef maxSize = new Ref$IntRef();
            maxSize.element = urlMap.size();
            final Ref$ObjectRef bookSourceFile = new Ref$ObjectRef();
            bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", (String)userNameSpace.element, "bookSource" }, null, 2, null);
            if (!((File)bookSourceFile.element).exists()) {
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
            }
            final BookController bookController2 = this;
            final int concurrentCount2 = concurrentCount;
            final int startIndex = lastIndex.element + 1;
            final int size = urlMap.size();
            final Function3 handler = (Function3)new BookController$searchBookSource.BookController$searchBookSource$3(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, userNameSpace, (Continuation)null);
            final Function2 needContinue = (Function2)new BookController$searchBookSource.BookController$searchBookSource$4(isEnd, this, resultList, searchSize);
            final Continuation $completion3 = $continuation;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$0 = this;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$1 = returnData;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$2 = lastIndex;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$3 = userNameSpace;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$4 = book;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).L$5 = resultList;
            ((BookController$searchBookSource.BookController$searchBookSource$1)$continuation).label = 2;
            if (bookController2.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        saveBookSources$default(this, (Book)ref$ObjectRef2.element, (List)ref$ObjectRef.element, (String)ref$ObjectRef3.element, false, 8, null);
        return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(ref$IntRef.element)), TuplesKt.to((Object)"list", ref$ObjectRef.element) }), null, 2, null);
    }
    
    @Nullable
    public final Object searchBookSourceSSE(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1) {
                final BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1 bookController$searchBookSourceSSE$1 = (BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$completion;
                if ((bookController$searchBookSourceSSE$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1 bookController$searchBookSourceSSE$2 = bookController$searchBookSourceSSE$1;
                    bookController$searchBookSourceSSE$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Ref$IntRef maxSize = null;
        final Ref$ObjectRef ref$ObjectRef;
        final Ref$ObjectRef ref$ObjectRef2;
        final Ref$ObjectRef ref$ObjectRef3;
        final Ref$IntRef ref$IntRef;
        final HttpServerResponse httpServerResponse;
        Label_1826: {
            ReturnData returnData = null;
            HttpServerResponse response = null;
            Object checkAuth = null;
            switch (((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$0 = this;
                    ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$1 = context;
                    ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$2 = returnData;
                    ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$3 = response;
                    ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    response = (HttpServerResponse)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$3;
                    returnData = (ReturnData)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$1;
                    this = (BookController)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    maxSize = (Ref$IntRef)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$6;
                    ref$ObjectRef = (Ref$ObjectRef)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$5;
                    ref$ObjectRef2 = (Ref$ObjectRef)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$4;
                    ref$ObjectRef3 = (Ref$ObjectRef)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$3;
                    ref$IntRef = (Ref$IntRef)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$2;
                    httpServerResponse = (HttpServerResponse)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$1;
                    this = (BookController)((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_1826;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final Ref$IntRef lastIndex = new Ref$IntRef();
            final Ref$IntRef searchSize = new Ref$IntRef();
            final Ref$ObjectRef bookSourceGroup = new Ref$ObjectRef();
            int refresh = 0;
            String bookUrl;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("url");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"url\")");
                bookUrl = string;
                final Ref$IntRef ref$IntRef2 = lastIndex;
                final Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(-1));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"lastIndex\", -1)");
                ref$IntRef2.element = integer.intValue();
                final Ref$IntRef ref$IntRef3 = searchSize;
                final Integer integer2 = context.getBodyAsJson().getInteger("searchSize", Boxing.boxInt(30));
                Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"searchSize\", 30)");
                ref$IntRef3.element = integer2.intValue();
                final Ref$ObjectRef ref$ObjectRef4 = bookSourceGroup;
                final String string2 = context.getBodyAsJson().getString("bookSourceGroup", "");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"bookSourceGroup\", \"\")");
                ref$ObjectRef4.element = string2;
                final Integer integer3 = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                Intrinsics.checkNotNullExpressionValue((Object)integer3, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                refresh = integer3.intValue();
            }
            else {
                final List queryParam = context.queryParam("url");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookUrl = ((s == null) ? "" : s);
                final Ref$IntRef ref$IntRef4 = lastIndex;
                final List queryParam2 = context.queryParam("lastIndex");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"lastIndex\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                int element;
                if (s2 == null) {
                    element = -1;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s2));
                    element = ((boxInt == null) ? -1 : boxInt);
                }
                ref$IntRef4.element = element;
                final Ref$IntRef ref$IntRef5 = searchSize;
                final List queryParam3 = context.queryParam("searchSize");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"searchSize\")");
                final String s3 = (String)CollectionsKt.firstOrNull(queryParam3);
                int element2;
                if (s3 == null) {
                    element2 = 30;
                }
                else {
                    final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s3));
                    element2 = ((boxInt2 == null) ? 30 : boxInt2);
                }
                ref$IntRef5.element = element2;
                final Ref$ObjectRef ref$ObjectRef5 = bookSourceGroup;
                final List queryParam4 = context.queryParam("bookSourceGroup");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"bookSourceGroup\")");
                final String s4 = (String)CollectionsKt.firstOrNull(queryParam4);
                ref$ObjectRef5.element = ((s4 == null) ? "" : s4);
                final List queryParam5 = context.queryParam("refresh");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"refresh\")");
                final String s5 = (String)CollectionsKt.firstOrNull(queryParam5);
                int n;
                if (s5 == null) {
                    n = 0;
                }
                else {
                    final Integer boxInt3 = Boxing.boxInt(Integer.parseInt(s5));
                    n = ((boxInt3 == null) ? 0 : boxInt3);
                }
                refresh = n;
            }
            final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
            userNameSpace.element = this.getUserNameSpace(context);
            final Map urlMap = new BookSourceController(this.getCoroutineContext()).getBookSourceMap((String)userNameSpace.element);
            if (urlMap.size() <= 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            if (bookUrl.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final Ref$ObjectRef book = new Ref$ObjectRef();
            book.element = this.getShelfBookByURL(bookUrl, (String)userNameSpace.element);
            if (book.element == null) {
                final Ref$ObjectRef ref$ObjectRef6 = book;
                final String asString = this.bookInfoCache.getAsString(bookUrl);
                Object element3;
                if (asString == null) {
                    element3 = null;
                }
                else {
                    final Map<String, Object> map = ExtKt.toMap(asString);
                    if (map == null) {
                        element3 = null;
                    }
                    else {
                        final Map $this$toDataClass$iv = map;
                        final int $i$f$toDataClass = 0;
                        final Object $this$convert$iv$iv = $this$toDataClass$iv;
                        final int $i$f$convert = 0;
                        final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                        element3 = ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
                    }
                }
                ref$ObjectRef6.element = element3;
            }
            if (book.element == null) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            if (lastIndex.element >= urlMap.size() - 1) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, MapsKt.mapOf(TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(lastIndex.element))), null, 2, null).setErrorMsg("\u6ca1\u6709\u66f4\u591a\u4e86"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            searchSize.element = ((searchSize.element > 0) ? searchSize.element : 30);
            final Ref$ObjectRef resultList = new Ref$ObjectRef();
            resultList.element = new ArrayList();
            final int concurrentCount = Math.max(searchSize.element * 2, 24);
            BookControllerKt.access$getLogger$p().info("searchBookMulti from lastIndex: {} concurrentCount: {} searchSize: {}", new Object[] { Boxing.boxInt(lastIndex.element), Boxing.boxInt(concurrentCount), Boxing.boxInt(searchSize.element) });
            final Ref$BooleanRef isEnd = new Ref$BooleanRef();
            context.request().connection().closeHandler(BookController::searchBookSourceSSE$lambda-8);
            final Ref$ObjectRef bookSourceFile = new Ref$ObjectRef();
            bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", (String)userNameSpace.element, "bookSource" }, null, 2, null);
            if (!((File)bookSourceFile.element).exists()) {
                bookSourceFile.element = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
            }
            maxSize = new Ref$IntRef();
            maxSize.element = urlMap.size();
            final BookController bookController2 = this;
            final int concurrentCount2 = concurrentCount;
            final int startIndex = lastIndex.element + 1;
            final int size = urlMap.size();
            final Function3 handler = (Function3)new BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$3(maxSize, lastIndex, bookSourceFile, bookSourceGroup, this, book, userNameSpace, (Continuation)null);
            final Function2 needContinue = (Function2)new BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$4(response, lastIndex, resultList, isEnd, this, searchSize);
            final Continuation $completion3 = $continuation;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$0 = this;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$1 = response;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$2 = lastIndex;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$3 = userNameSpace;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$4 = book;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$5 = resultList;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).L$6 = maxSize;
            ((BookController$searchBookSourceSSE.BookController$searchBookSourceSSE$1)$continuation).label = 2;
            if (bookController2.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        saveBookSources$default(this, (Book)ref$ObjectRef2.element, (List)ref$ObjectRef.element, (String)ref$ObjectRef3.element, false, 8, null);
        httpServerResponse.write("event: end\n");
        httpServerResponse.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(ref$IntRef.element)), TuplesKt.to((Object)"isEnd", (Object)Boxing.boxBoolean(ref$IntRef.element >= maxSize.element)) }), false) + "\n\n");
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object searchBookWithSource(@NotNull final String bookSourceString, @NotNull final Book book, final boolean accurate, @NotNull final String userNameSpace, @NotNull final Continuation<? super ArrayList<SearchBook>> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$searchBookWithSource.BookController$searchBookWithSource$1) {
                final BookController$searchBookWithSource.BookController$searchBookWithSource$1 bookController$searchBookWithSource$1 = (BookController$searchBookWithSource.BookController$searchBookWithSource$1)$completion;
                if ((bookController$searchBookWithSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookWithSource.BookController$searchBookWithSource$1 bookController$searchBookWithSource$2 = bookController$searchBookWithSource$1;
                    bookController$searchBookWithSource$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$searchBookWithSource.BookController$searchBookWithSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookWithSource.BookController$searchBookWithSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Ref$ObjectRef ref$ObjectRef2;
        switch (((BookController$searchBookWithSource.BookController$searchBookWithSource$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final Ref$ObjectRef resultList = new Ref$ObjectRef();
                resultList.element = new ArrayList();
                final Ref$ObjectRef ref$ObjectRef;
                final Ref$ObjectRef bookSource = ref$ObjectRef = new Ref$ObjectRef();
                final Object fromJson-IoAF18A = BookSource.Companion.fromJson-IoAF18A(bookSourceString);
                ref$ObjectRef.element = (Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                if (bookSource.element == null) {
                    return resultList.element;
                }
                if (this.isInvalidBookSource((BookSource)bookSource.element, userNameSpace)) {
                    return resultList.element;
                }
                final CoroutineContext coroutineContext = (CoroutineContext)Dispatchers.getIO();
                final Function2 function2 = (Function2)new BookController$searchBookWithSource.BookController$searchBookWithSource$2(bookSource, userNameSpace, book, accurate, resultList, this, (Continuation)null);
                final Continuation continuation = $continuation;
                ((BookController$searchBookWithSource.BookController$searchBookWithSource$1)$continuation).L$0 = resultList;
                ((BookController$searchBookWithSource.BookController$searchBookWithSource$1)$continuation).label = 1;
                if (BuildersKt.withContext(coroutineContext, function2, continuation) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                ref$ObjectRef2 = (Ref$ObjectRef)((BookController$searchBookWithSource.BookController$searchBookWithSource$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return ref$ObjectRef2.element;
    }
    
    @Nullable
    public final Object getAvailableBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getAvailableBookSource.BookController$getAvailableBookSource$1) {
                final BookController$getAvailableBookSource.BookController$getAvailableBookSource$1 bookController$getAvailableBookSource$1 = (BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$completion;
                if ((bookController$getAvailableBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getAvailableBookSource.BookController$getAvailableBookSource$1 bookController$getAvailableBookSource$2 = bookController$getAvailableBookSource$1;
                    bookController$getAvailableBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getAvailableBookSource.BookController$getAvailableBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Ref$ObjectRef ref$ObjectRef;
        final Ref$ObjectRef ref$ObjectRef2;
        final Ref$ObjectRef ref$ObjectRef3;
        final ReturnData returnData2;
        Label_0946: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$0 = this;
                    ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$1 = context;
                    ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$2 = returnData;
                    ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$1;
                    this = (BookController)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    ref$ObjectRef = (Ref$ObjectRef)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$4;
                    ref$ObjectRef2 = (Ref$ObjectRef)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$3;
                    ref$ObjectRef3 = (Ref$ObjectRef)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$2;
                    returnData2 = (ReturnData)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$1;
                    this = (BookController)((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0946;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            String bookUrl;
            int refresh = 0;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("url");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"url\")");
                bookUrl = string;
                final Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                integer.intValue();
            }
            else {
                final List queryParam = context.queryParam("url");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookUrl = ((s == null) ? "" : s);
                final List queryParam2 = context.queryParam("refresh");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"refresh\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                int n;
                if (s2 == null) {
                    n = 0;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s2));
                    n = ((boxInt == null) ? 0 : boxInt);
                }
                refresh = n;
            }
            if (bookUrl.length() == 0) {
                return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
            }
            final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
            userNameSpace.element = this.getUserNameSpace(context);
            final Ref$ObjectRef book = new Ref$ObjectRef();
            book.element = this.getShelfBookByURL(bookUrl, (String)userNameSpace.element);
            if (book.element == null) {
                final Ref$ObjectRef ref$ObjectRef4 = book;
                final String asString = this.bookInfoCache.getAsString(bookUrl);
                Object element;
                if (asString == null) {
                    element = null;
                }
                else {
                    final Map<String, Object> map = ExtKt.toMap(asString);
                    if (map == null) {
                        element = null;
                    }
                    else {
                        final Map $this$toDataClass$iv = map;
                        final int $i$f$toDataClass = 0;
                        final Object $this$convert$iv$iv = $this$toDataClass$iv;
                        final int $i$f$convert = 0;
                        final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                        element = ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
                    }
                }
                ref$ObjectRef4.element = element;
            }
            if (book.element == null) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
            }
            final Ref$ObjectRef bookSourceList = new Ref$ObjectRef();
            bookSourceList.element = ExtKt.asJsonArray(this.getUserStorage(userNameSpace.element, ((Book)book.element).getName() + '_' + ((Book)book.element).getAuthor(), "bookSource"));
            if (bookSourceList.element == null || ((JsonArray)bookSourceList.element).size() <= 0) {
                return ReturnData.setData$default(returnData2, new ArrayList(), null, 2, null);
            }
            if (refresh <= 0) {
                final ReturnData returnData3 = returnData;
                final List list = ((JsonArray)bookSourceList.element).getList();
                Intrinsics.checkNotNullExpressionValue((Object)list, "bookSourceList.getList()");
                return ReturnData.setData$default(returnData3, list, null, 2, null);
            }
            final Ref$ObjectRef resultList = new Ref$ObjectRef();
            resultList.element = new ArrayList();
            final int concurrentCount = 16;
            final BookController bookController2 = this;
            final int concurrentCount2 = concurrentCount;
            final int startIndex = 0;
            final int size = ((JsonArray)bookSourceList.element).size();
            final Function3 handler = (Function3)new BookController$getAvailableBookSource.BookController$getAvailableBookSource$2(bookSourceList, this, userNameSpace, book, (Continuation)null);
            final Function2 needContinue = (Function2)new BookController$getAvailableBookSource.BookController$getAvailableBookSource$3(resultList);
            final Continuation $completion3 = $continuation;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$0 = this;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$1 = returnData;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$2 = userNameSpace;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$3 = book;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).L$4 = resultList;
            ((BookController$getAvailableBookSource.BookController$getAvailableBookSource$1)$continuation).label = 2;
            if (bookController2.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        this.saveBookSources((Book)ref$ObjectRef2.element, (List<SearchBook>)ref$ObjectRef.element, (String)ref$ObjectRef3.element, true);
        return ReturnData.setData$default(returnData2, ref$ObjectRef.element, null, 2, null);
    }
    
    @Nullable
    public final Object getBookshelf(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getBookshelf.BookController$getBookshelf$1) {
                final BookController$getBookshelf.BookController$getBookshelf$1 bookController$getBookshelf$1 = (BookController$getBookshelf.BookController$getBookshelf$1)$completion;
                if ((bookController$getBookshelf$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getBookshelf.BookController$getBookshelf$1 bookController$getBookshelf$2 = bookController$getBookshelf$1;
                    bookController$getBookshelf$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getBookshelf.BookController$getBookshelf$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final ReturnData returnData2;
        Object bookShelfBooks = null;
        Label_0403: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$0 = this;
                    ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$1 = context;
                    ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$2 = returnData;
                    ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$1;
                    this = (BookController)((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    returnData2 = (ReturnData)((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    bookShelfBooks = $result;
                    break Label_0403;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            int refresh = 0;
            if (context.request().method() == HttpMethod.POST) {
                final Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                integer.intValue();
            }
            else {
                final List queryParam = context.queryParam("refresh");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"refresh\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                int n;
                if (s == null) {
                    n = 0;
                }
                else {
                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s));
                    n = ((boxInt == null) ? 0 : boxInt);
                }
                refresh = n;
            }
            final BookController bookController2 = this;
            final boolean refresh2 = refresh > 0;
            final String userNameSpace = this.getUserNameSpace(context);
            final Continuation $completion3 = $continuation;
            ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$0 = returnData;
            ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$1 = null;
            ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).L$2 = null;
            ((BookController$getBookshelf.BookController$getBookshelf$1)$continuation).label = 2;
            if ((bookShelfBooks = bookController2.getBookShelfBooks(refresh2, userNameSpace, (Continuation<? super List<Book>>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final List bookList = (List)bookShelfBooks;
        return ReturnData.setData$default(returnData2, bookList, null, 2, null);
    }
    
    @Nullable
    public final Object getShelfBook(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getShelfBook.BookController$getShelfBook$1) {
                final BookController$getShelfBook.BookController$getShelfBook$1 bookController$getShelfBook$1 = (BookController$getShelfBook.BookController$getShelfBook$1)$completion;
                if ((bookController$getShelfBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getShelfBook.BookController$getShelfBook$1 bookController$getShelfBook$2 = bookController$getShelfBook$1;
                    bookController$getShelfBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getShelfBook.BookController$getShelfBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$0 = this;
                ((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$1 = context;
                ((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$2 = returnData;
                ((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$2;
                context = (RoutingContext)((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$1;
                this = (BookController)((BookController$getShelfBook.BookController$getShelfBook$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        String url = null;
        if (context.request().method() == HttpMethod.POST) {
            Intrinsics.checkNotNullExpressionValue((Object)context.getBodyAsJson().getString("url"), "context.bodyAsJson.getString(\"url\")");
        }
        else {
            final List queryParam = context.queryParam("url");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            url = ((s == null) ? "" : s);
        }
        if (url.length() == 0) {
            return returnData.setErrorMsg("\u4e66\u6e90\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
        }
        final Book book = this.getShelfBookByURL(url, this.getUserNameSpace(context));
        if (book == null) {
            return returnData.setErrorMsg("\u4e66\u7c4d\u4e0d\u5b58\u5728");
        }
        return ReturnData.setData$default(returnData, book, null, 2, null);
    }
    
    @Nullable
    public final Object saveBook(@NotNull final RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/BookController$saveBook$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/BookController$saveBook$1;
        //    11: astore          11
        //    13: aload           11
        //    15: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           11
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/BookController$saveBook$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/BookController$saveBook$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          12
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //    67: tableswitch {
        //                0: 104
        //                1: 157
        //                2: 409
        //                3: 547
        //                4: 677
        //                5: 804
        //          default: 931
        //        }
        //   104: aload           $result
        //   106: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   109: new             Lcom/htmake/reader/api/ReturnData;
        //   112: dup            
        //   113: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   116: astore_3        /* returnData */
        //   117: aload_0         /* this */
        //   118: aload_1         /* context */
        //   119: aload           $continuation
        //   121: aload           $continuation
        //   123: aload_0         /* this */
        //   124: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   127: aload           $continuation
        //   129: aload_1         /* context */
        //   130: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   133: aload           $continuation
        //   135: aload_3         /* returnData */
        //   136: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   139: aload           $continuation
        //   141: iconst_1       
        //   142: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //   145: invokevirtual   com/htmake/reader/api/controller/BookController.checkAuth:(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   148: dup            
        //   149: aload           12
        //   151: if_acmpne       191
        //   154: aload           12
        //   156: areturn        
        //   157: aload           $continuation
        //   159: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   162: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   165: astore_3        /* returnData */
        //   166: aload           $continuation
        //   168: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   171: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   174: astore_1        /* context */
        //   175: aload           $continuation
        //   177: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   180: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   183: astore_0        /* this */
        //   184: aload           $result
        //   186: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   189: aload           $result
        //   191: checkcast       Ljava/lang/Boolean;
        //   194: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   197: ifne            215
        //   200: aload_3         /* returnData */
        //   201: ldc             "NEED_LOGIN"
        //   203: aconst_null    
        //   204: iconst_2       
        //   205: aconst_null    
        //   206: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   209: ldc             "\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"
        //   211: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   214: areturn        
        //   215: aload_1         /* context */
        //   216: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   221: ldc_w           Lio/legado/app/data/entities/Book;.class
        //   224: invokevirtual   io/vertx/core/json/JsonObject.mapTo:(Ljava/lang/Class;)Ljava/lang/Object;
        //   227: checkcast       Lio/legado/app/data/entities/Book;
        //   230: astore          book
        //   232: aload_0         /* this */
        //   233: aload_1         /* context */
        //   234: invokevirtual   com/htmake/reader/api/controller/BookController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //   237: astore          userNameSpace
        //   239: aload           book
        //   241: invokevirtual   io/legado/app/data/entities/Book.isLocalBook:()Z
        //   244: ifne            735
        //   247: aload_0         /* this */
        //   248: aload           book
        //   250: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //   253: aload           userNameSpace
        //   255: invokevirtual   com/htmake/reader/api/controller/BookController.getBookSourceStringBySourceURLOpt:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   258: astore          bookSource
        //   260: aload           bookSource
        //   262: ifnonnull       273
        //   265: aload_3         /* returnData */
        //   266: ldc_w           "\u4e66\u6e90\u4fe1\u606f\u9519\u8bef"
        //   269: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   272: areturn        
        //   273: aload           book
        //   275: invokevirtual   io/legado/app/data/entities/Book.getTocUrl:()Ljava/lang/String;
        //   278: checkcast       Ljava/lang/CharSequence;
        //   281: astore          7
        //   283: iconst_0       
        //   284: istore          8
        //   286: iconst_0       
        //   287: istore          9
        //   289: aload           7
        //   291: ifnull          304
        //   294: aload           7
        //   296: invokeinterface java/lang/CharSequence.length:()I
        //   301: ifne            308
        //   304: iconst_1       
        //   305: goto            309
        //   308: iconst_0       
        //   309: ifeq            606
        //   312: new             Lio/legado/app/model/webBook/WebBook;
        //   315: dup            
        //   316: aload           bookSource
        //   318: aload_0         /* this */
        //   319: invokevirtual   com/htmake/reader/api/controller/BookController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   322: invokevirtual   com/htmake/reader/config/AppConfig.getDebugLog:()Z
        //   325: aconst_null    
        //   326: aload           userNameSpace
        //   328: iconst_4       
        //   329: aconst_null    
        //   330: invokespecial   io/legado/app/model/webBook/WebBook.<init>:(Ljava/lang/String;ZLio/legado/app/model/DebugLog;Ljava/lang/String;ILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   333: aload           book
        //   335: astore          7
        //   337: aload           7
        //   339: ldc_w           "book"
        //   342: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   345: aload           7
        //   347: iconst_0       
        //   348: aload           $continuation
        //   350: iconst_2       
        //   351: aconst_null    
        //   352: aload           $continuation
        //   354: aload_0         /* this */
        //   355: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   358: aload           $continuation
        //   360: aload_1         /* context */
        //   361: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   364: aload           $continuation
        //   366: aload_3         /* returnData */
        //   367: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   370: aload           $continuation
        //   372: aload           book
        //   374: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   377: aload           $continuation
        //   379: aload           userNameSpace
        //   381: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   384: aload           $continuation
        //   386: aload           bookSource
        //   388: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$5:Ljava/lang/Object;
        //   391: aload           $continuation
        //   393: iconst_2       
        //   394: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //   397: invokestatic    io/legado/app/model/webBook/WebBook.getBookInfo$default:(Lio/legado/app/model/webBook/WebBook;Lio/legado/app/data/entities/Book;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   400: dup            
        //   401: aload           12
        //   403: if_acmpne       473
        //   406: aload           12
        //   408: areturn        
        //   409: aload           $continuation
        //   411: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$5:Ljava/lang/Object;
        //   414: checkcast       Ljava/lang/String;
        //   417: astore          6
        //   419: aload           $continuation
        //   421: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   424: checkcast       Ljava/lang/String;
        //   427: astore          5
        //   429: aload           $continuation
        //   431: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   434: checkcast       Lio/legado/app/data/entities/Book;
        //   437: astore          4
        //   439: aload           $continuation
        //   441: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   444: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   447: astore_3       
        //   448: aload           $continuation
        //   450: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   453: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   456: astore_1       
        //   457: aload           $continuation
        //   459: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   462: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   465: astore_0       
        //   466: aload           $result
        //   468: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   471: aload           $result
        //   473: pop            
        //   474: aload_0        
        //   475: aload           4
        //   477: astore          7
        //   479: aload           7
        //   481: ldc_w           "book"
        //   484: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   487: aload           7
        //   489: aload           $continuation
        //   491: aload           $continuation
        //   493: aload_0        
        //   494: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   497: aload           $continuation
        //   499: aload_1        
        //   500: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   503: aload           $continuation
        //   505: aload_3        
        //   506: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   509: aload           $continuation
        //   511: aload           5
        //   513: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   516: aload           $continuation
        //   518: aload           6
        //   520: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   523: aload           $continuation
        //   525: aconst_null    
        //   526: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$5:Ljava/lang/Object;
        //   529: aload           $continuation
        //   531: iconst_3       
        //   532: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //   535: invokevirtual   com/htmake/reader/api/controller/BookController.mergeBookCacheInfo:(Lio/legado/app/data/entities/Book;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   538: dup            
        //   539: aload           12
        //   541: if_acmpne       601
        //   544: aload           12
        //   546: areturn        
        //   547: aload           $continuation
        //   549: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   552: checkcast       Ljava/lang/String;
        //   555: astore          6
        //   557: aload           $continuation
        //   559: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   562: checkcast       Ljava/lang/String;
        //   565: astore          5
        //   567: aload           $continuation
        //   569: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   572: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   575: astore_3       
        //   576: aload           $continuation
        //   578: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   581: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   584: astore_1       
        //   585: aload           $continuation
        //   587: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   590: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   593: astore_0       
        //   594: aload           $result
        //   596: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   599: aload           $result
        //   601: checkcast       Lio/legado/app/data/entities/Book;
        //   604: astore          4
        //   606: aload_0        
        //   607: aload           4
        //   609: astore          7
        //   611: aload           7
        //   613: ldc_w           "book"
        //   616: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   619: aload           7
        //   621: aload           5
        //   623: aload           6
        //   625: aload           $continuation
        //   627: aload           $continuation
        //   629: aload_0        
        //   630: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   633: aload           $continuation
        //   635: aload_1        
        //   636: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   639: aload           $continuation
        //   641: aload_3        
        //   642: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   645: aload           $continuation
        //   647: aload           4
        //   649: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   652: aload           $continuation
        //   654: aload           5
        //   656: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   659: aload           $continuation
        //   661: iconst_4       
        //   662: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //   665: invokevirtual   com/htmake/reader/api/controller/BookController.saveBookCover:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   668: dup            
        //   669: aload           12
        //   671: if_acmpne       731
        //   674: aload           12
        //   676: areturn        
        //   677: aload           $continuation
        //   679: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   682: checkcast       Ljava/lang/String;
        //   685: astore          5
        //   687: aload           $continuation
        //   689: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   692: checkcast       Lio/legado/app/data/entities/Book;
        //   695: astore          4
        //   697: aload           $continuation
        //   699: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   702: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   705: astore_3       
        //   706: aload           $continuation
        //   708: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   711: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   714: astore_1       
        //   715: aload           $continuation
        //   717: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   720: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   723: astore_0       
        //   724: aload           $result
        //   726: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   729: aload           $result
        //   731: pop            
        //   732: goto            859
        //   735: aload_0        
        //   736: aload           4
        //   738: astore          6
        //   740: aload           6
        //   742: ldc_w           "book"
        //   745: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   748: aload           6
        //   750: aload           5
        //   752: aload           $continuation
        //   754: aload           $continuation
        //   756: aload_0        
        //   757: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   760: aload           $continuation
        //   762: aload_1        
        //   763: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   766: aload           $continuation
        //   768: aload_3        
        //   769: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   772: aload           $continuation
        //   774: aload           4
        //   776: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   779: aload           $continuation
        //   781: aload           5
        //   783: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   786: aload           $continuation
        //   788: iconst_5       
        //   789: putfield        com/htmake/reader/api/controller/BookController$saveBook$1.label:I
        //   792: invokespecial   com/htmake/reader/api/controller/BookController.saveLocalBookCover:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   795: dup            
        //   796: aload           12
        //   798: if_acmpne       858
        //   801: aload           12
        //   803: areturn        
        //   804: aload           $continuation
        //   806: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$4:Ljava/lang/Object;
        //   809: checkcast       Ljava/lang/String;
        //   812: astore          5
        //   814: aload           $continuation
        //   816: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$3:Ljava/lang/Object;
        //   819: checkcast       Lio/legado/app/data/entities/Book;
        //   822: astore          4
        //   824: aload           $continuation
        //   826: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$2:Ljava/lang/Object;
        //   829: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   832: astore_3       
        //   833: aload           $continuation
        //   835: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$1:Ljava/lang/Object;
        //   838: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   841: astore_1       
        //   842: aload           $continuation
        //   844: getfield        com/htmake/reader/api/controller/BookController$saveBook$1.L$0:Ljava/lang/Object;
        //   847: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   850: astore_0       
        //   851: aload           $result
        //   853: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   856: aload           $result
        //   858: pop            
        //   859: aload_0        
        //   860: aload           4
        //   862: astore          7
        //   864: aload           7
        //   866: ldc_w           "book"
        //   869: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   872: aload           7
        //   874: aload           5
        //   876: aload_1        
        //   877: invokevirtual   com/htmake/reader/api/controller/BookController.saveBookToShelf:(Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/vertx/ext/web/RoutingContext;)Lkotlin/Pair;
        //   880: astore          result
        //   882: aload           result
        //   884: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   887: ifnull          918
        //   890: aload_3        
        //   891: aload           result
        //   893: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   896: checkcast       Ljava/lang/String;
        //   899: astore          7
        //   901: aload           7
        //   903: ifnonnull       912
        //   906: ldc_w           ""
        //   909: goto            914
        //   912: aload           7
        //   914: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   917: areturn        
        //   918: aload_3        
        //   919: aload           result
        //   921: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   924: aconst_null    
        //   925: iconst_2       
        //   926: aconst_null    
        //   927: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   930: areturn        
        //   931: new             Ljava/lang/IllegalStateException;
        //   934: dup            
        //   935: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   938: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   941: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lcom/htmake/reader/api/ReturnData;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 19 27 FF 00 0A 00 0C 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 07 07 29 00 00 FF 00 35 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00 34 FF 00 21 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 01 07 01 13 17 FF 00 39 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 00 60 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 1E 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 00 60 07 01 4D 01 01 07 01 13 07 07 29 07 01 13 00 00 03 40 01 FF 00 63 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 3F 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 00 60 00 00 00 07 01 13 07 07 29 07 01 13 00 01 07 01 13 FF 00 49 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 35 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 00 07 00 60 07 00 60 00 00 00 07 01 13 07 07 29 07 01 13 00 01 07 01 13 FF 00 04 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 00 60 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 46 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 35 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 00 00 00 00 07 01 13 07 07 29 07 01 13 00 01 07 01 13 03 FF 00 44 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 35 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 00 00 00 00 07 01 13 07 07 29 07 01 13 00 01 07 01 13 00 FF 00 34 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 02 FC 07 00 60 00 00 07 01 13 07 07 29 07 01 13 00 01 07 00 B9 FF 00 01 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 02 FC 07 00 60 00 00 07 01 13 07 07 29 07 01 13 00 02 07 00 B9 07 00 60 FF 00 03 00 0D 07 00 02 07 00 CA 07 01 11 07 00 B9 07 01 84 07 00 60 07 02 FC 07 01 84 00 00 07 01 13 07 07 29 07 01 13 00 00 FF 00 0C 00 0D 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 07 01 13 07 07 29 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    public final Pair<Book, String> saveBookToShelf(@NotNull final Book _book, @NotNull final String userNameSpace, @NotNull final RoutingContext context) {
        Intrinsics.checkNotNullParameter((Object)_book, "_book");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        Intrinsics.checkNotNullParameter((Object)context, "context");
        final Book book = _book;
        final CharSequence charSequence = book.getOrigin();
        if (charSequence == null || charSequence.length() == 0) {
            return (Pair<Book, String>)new Pair((Object)book, (Object)"\u672a\u627e\u5230\u4e66\u6e90\u4fe1\u606f");
        }
        final CharSequence charSequence2 = book.getBookUrl();
        if (charSequence2 == null || charSequence2.length() == 0) {
            return (Pair<Book, String>)new Pair((Object)book, (Object)"\u4e66\u7c4d\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
        }
        JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        int existIndex = -1;
        int j = 0;
        final int size = bookshelf.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final String name = bookshelf.getJsonObject(i).getString("name", "");
                final String author = bookshelf.getJsonObject(i).getString("author", "");
                if (name.equals(book.getName()) && author.equals(book.getAuthor())) {
                    existIndex = i;
                    break;
                }
            } while (j < size);
        }
        if (existIndex < 0) {
            final User userInfo = (User)context.get("userInfo");
            if (userInfo != null && bookshelf.size() >= userInfo.getBook_limit()) {
                return (Pair<Book, String>)new Pair((Object)book, (Object)"\u4f60\u5df2\u8fbe\u5230\u4e66\u7c4d\u6570\u4e0a\u9650\uff0c\u8bf7\u8054\u7cfb\u7ba1\u7406\u5458");
            }
        }
        if (book.isLocalBook()) {
            if (StringsKt.startsWith$default(book.getBookUrl(), "/assets/", false, 2, (Object)null) || StringsKt.startsWith$default(book.getBookUrl(), "assets/", false, 2, (Object)null)) {
                final File tempFile = new File(ExtKt.getWorkDir(Intrinsics.stringPlus("storage", (Object)book.getBookUrl())));
                if (!tempFile.exists()) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u4e0a\u4f20\u4e66\u7c4d\u4e0d\u5b58\u5728");
                }
                final String relativeLocalFilePath = Paths.get("storage", "data", userNameSpace, book.getName() + '_' + book.getAuthor(), tempFile.getName()).toString();
                final String relativeLocalFileUrl = "storage/data/" + userNameSpace + '/' + book.getName() + '_' + book.getAuthor() + '/' + (Object)tempFile.getName();
                final String localFilePath = ExtKt.getWorkDir(relativeLocalFilePath);
                BookControllerKt.access$getLogger$p().info("localFilePath: {}", (Object)localFilePath);
                final File localFile = new File(localFilePath);
                ExtKt.deleteRecursively(localFile);
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdirs();
                }
                if (!FilesKt.copyRecursively$default(tempFile, localFile, false, (Function2)null, 6, (Object)null)) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730\u4e66\u7c4d\u5931\u8d25");
                }
                ExtKt.deleteRecursively(tempFile);
                book.setBookUrl(relativeLocalFileUrl);
                book.setOriginName(relativeLocalFilePath);
                if (book.isEpub()) {
                    if (!extractEpub$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730Epub\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isCbz()) {
                    if (!extractCbz$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730CBZ\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u672c\u5730PDF\u4e66\u7c4d\u8f6c\u6362\u5931\u8d25");
                }
            }
            else if (StringsKt.indexOf$default((CharSequence)book.getBookUrl(), "localStore", 0, false, 6, (Object)null) >= 0) {
                final File tempFile = new File(ExtKt.getWorkDir(book.getBookUrl()));
                if (!tempFile.exists()) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u672c\u5730\u4e66\u4ed3\u4e66\u7c4d\u4e0d\u5b58\u5728");
                }
                final String relativeLocalFileUrl2 = "storage/data/" + userNameSpace + '/' + book.getName() + '_' + book.getAuthor() + '/' + (Object)tempFile.getName();
                book.setBookUrl(relativeLocalFileUrl2);
                if (book.isEpub()) {
                    if (!extractEpub$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730Epub\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isCbz()) {
                    if (!extractCbz$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730CBZ\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u672c\u5730PDF\u4e66\u7c4d\u8f6c\u6362\u5931\u8d25");
                }
            }
            else if (StringsKt.indexOf$default((CharSequence)book.getBookUrl(), "webdav", 0, false, 6, (Object)null) >= 0) {
                final File tempFile = new File(ExtKt.getWorkDir(book.getBookUrl()));
                if (!tempFile.exists()) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"webdav\u4e66\u4ed3\u4e66\u7c4d\u4e0d\u5b58\u5728");
                }
                final String relativeLocalFileUrl2 = "storage/data/" + userNameSpace + '/' + book.getName() + '_' + book.getAuthor() + '/' + (Object)tempFile.getName();
                book.setBookUrl(relativeLocalFileUrl2);
                if (book.isEpub()) {
                    if (!extractEpub$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730Epub\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isCbz()) {
                    if (!extractCbz$default(this, book, false, 2, null)) {
                        return (Pair<Book, String>)new Pair((Object)book, (Object)"\u5bfc\u5165\u672c\u5730CBZ\u4e66\u7c4d\u5931\u8d25");
                    }
                }
                else if (book.isPdf() && !convertPdfToImage$default(this, book, false, 2, null)) {
                    return (Pair<Book, String>)new Pair((Object)book, (Object)"\u672c\u5730PDF\u4e66\u7c4d\u8f6c\u6362\u5931\u8d25");
                }
            }
        }
        book.setInShelf(true);
        if (existIndex >= 0) {
            final List bookList = bookshelf.getList();
            final Book existBook = (Book)bookshelf.getJsonObject(existIndex).mapTo((Class)Book.class);
            book.setDurChapterIndex(existBook.getDurChapterIndex());
            book.setDurChapterTitle(existBook.getDurChapterTitle());
            book.setDurChapterTime(existBook.getDurChapterTime());
            final CharSequence charSequence3 = existBook.getDisplayCover();
            if (charSequence3 != null && charSequence3.length() != 0) {
                final String displayCover = existBook.getDisplayCover();
                Intrinsics.checkNotNull((Object)displayCover);
                if (StringsKt.startsWith$default(displayCover, "/", false, 2, (Object)null)) {
                    final String displayCover2 = existBook.getDisplayCover();
                    Intrinsics.checkNotNull((Object)displayCover2);
                    if (!displayCover2.equals(book.getDisplayCover())) {
                        final String[] subDirFiles = { "storage", null };
                        final int n = 1;
                        final String displayCover3 = existBook.getDisplayCover();
                        Intrinsics.checkNotNull((Object)displayCover3);
                        subDirFiles[n] = displayCover3;
                        final String cachePath = ExtKt.getWorkDir(subDirFiles);
                        FileUtils.INSTANCE.deleteFile(cachePath);
                    }
                }
            }
            bookList.set(existIndex, JsonObject.mapFrom((Object)book));
            bookshelf = new JsonArray(bookList);
        }
        else {
            bookshelf.add(JsonObject.mapFrom((Object)book));
        }
        final List sourceList = CollectionsKt.listOf((Object)book.toSearchBook());
        saveBookSources$default(this, book, sourceList, userNameSpace, false, 8, null);
        this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        return (Pair<Book, String>)new Pair((Object)book, (Object)null);
    }
    
    private final Object saveLocalBookCover(Book book, final String userNameSpace, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveLocalBookCover.BookController$saveLocalBookCover$1) {
                final BookController$saveLocalBookCover.BookController$saveLocalBookCover$1 bookController$saveLocalBookCover$1 = (BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$completion;
                if ((bookController$saveLocalBookCover$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveLocalBookCover.BookController$saveLocalBookCover$1 bookController$saveLocalBookCover$2 = bookController$saveLocalBookCover$1;
                    bookController$saveLocalBookCover$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveLocalBookCover.BookController$saveLocalBookCover$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object awaitResult = null;
        final File file;
        final String coverUrl2;
        switch (((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final String coverUrl = book.getDisplayCover();
                if (coverUrl == null || StringsKt.startsWith$default(coverUrl, "/", false, 2, (Object)null)) {
                    return Unit.INSTANCE;
                }
                final String ext = this.getFileExt(coverUrl, "jpg");
                final String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                final String cachePath = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", md5Encode + '.' + ext);
                final String cachedCoverUrl = "/assets/" + userNameSpace + "/covers/" + md5Encode + '.' + ext;
                final File cacheFile = new File(cachePath);
                if (cacheFile.exists()) {
                    book.setCoverUrl(cachedCoverUrl);
                    return Unit.INSTANCE;
                }
                final Function1 function1 = (Function1)new BookController$saveLocalBookCover$result.BookController$saveLocalBookCover$result$1(this, coverUrl);
                final Continuation continuation = $continuation;
                ((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$0 = book;
                ((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$1 = cachedCoverUrl;
                ((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$2 = cacheFile;
                ((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).label = 1;
                if ((awaitResult = VertxCoroutineKt.awaitResult(function1, continuation)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                file = (File)((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$2;
                coverUrl2 = (String)((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$1;
                book = (Book)((BookController$saveLocalBookCover.BookController$saveLocalBookCover$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                awaitResult = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final HttpResponse result = (HttpResponse)awaitResult;
        final Buffer bodyAsBuffer = result.bodyAsBuffer();
        final byte[] bodyBytes = (byte[])((bodyAsBuffer == null) ? null : bodyAsBuffer.getBytes());
        if (bodyBytes != null) {
            FilesKt.writeBytes(file, bodyBytes);
            book.setCoverUrl(coverUrl2);
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object saveBookCover(@NotNull Book book, @NotNull final String userNameSpace, @Nullable final String bookSource, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$saveBookCover.BookController$saveBookCover$1) {
                final BookController$saveBookCover.BookController$saveBookCover$1 bookController$saveBookCover$1 = (BookController$saveBookCover.BookController$saveBookCover$1)$completion;
                if ((bookController$saveBookCover$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveBookCover.BookController$saveBookCover$1 bookController$saveBookCover$2 = bookController$saveBookCover$1;
                    bookController$saveBookCover$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$saveBookCover.BookController$saveBookCover$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final String coverUrl = book.getDisplayCover();
                if (coverUrl == null || StringsKt.startsWith$default(coverUrl, "/", false, 2, (Object)null)) {
                    break;
                }
                final String bookSource2 = (bookSource == null) ? this.getBookSourceStringBySourceURLOpt(book.getOrigin(), userNameSpace) : bookSource;
                final String ext = this.getFileExt(coverUrl, "jpg");
                final String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                final String cachePath = ExtKt.getWorkDir("storage", "assets", userNameSpace, "covers", md5Encode + '.' + ext);
                final String cachedCoverUrl = "/assets/" + userNameSpace + "/covers/" + md5Encode + '.' + ext;
                final File cacheFile = new File(cachePath);
                if (cacheFile.exists()) {
                    book.setCoverUrl(cachedCoverUrl);
                    return Unit.INSTANCE;
                }
            }
            case 1: {
                Label_0410: {
                    break Label_0410;
                    final String coverUrl;
                    final String s = coverUrl;
                    final String s2 = null;
                    final Integer n = null;
                    final String s3 = null;
                    final Integer n2 = null;
                    final String s4 = null;
                    final BookSource.Companion companion = BookSource.Companion;
                    final String bookSource2;
                    final String json = bookSource2;
                    Intrinsics.checkNotNull((Object)json);
                    final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(json);
                    final AnalyzeUrl analyzeUrl = new AnalyzeUrl(s, s2, n, s3, n2, s4, (BaseSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A), null, null, null, null, 1982, null);
                    try {
                        final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                        final Continuation $completion2 = $continuation;
                        ((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$0 = book;
                        final String cachePath;
                        ((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$1 = cachePath;
                        final String cachedCoverUrl;
                        ((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$2 = cachedCoverUrl;
                        ((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).label = 1;
                        Object byteArrayAwait;
                        if ((byteArrayAwait = analyzeUrl2.getByteArrayAwait((Continuation<? super byte[]>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        while (true) {
                            final byte[] it = (byte[])byteArrayAwait;
                            final int n3 = 0;
                            final String filepath;
                            FileUtils.INSTANCE.writeBytes(filepath, it);
                            final String coverUrl2;
                            book.setCoverUrl(coverUrl2);
                            break;
                            coverUrl2 = (String)((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$2;
                            filepath = (String)((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$1;
                            book = (Book)((BookController$saveBookCover.BookController$saveBookCover$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            byteArrayAwait = $result;
                            continue;
                        }
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object setBookSource(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$setBookSource.BookController$setBookSource$1) {
                final BookController$setBookSource.BookController$setBookSource$1 bookController$setBookSource$1 = (BookController$setBookSource.BookController$setBookSource$1)$completion;
                if ((bookController$setBookSource$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$setBookSource.BookController$setBookSource$1 bookController$setBookSource$2 = bookController$setBookSource$1;
                    bookController$setBookSource$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$setBookSource.BookController$setBookSource$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$setBookSource.BookController$setBookSource$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            Ref$ObjectRef ref$ObjectRef = null;
            String l$7 = null;
            final Book book2;
            String l$8 = null;
            ReturnData returnData2 = null;
            Object o2 = null;
            Book element = null;
            Label_1041: {
                Object l$6 = null;
                Object bookInfo$default = null;
                Label_1032: {
                    ReturnData returnData = null;
                    Object checkAuth = null;
                    switch (((BookController$setBookSource.BookController$setBookSource$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            returnData = new ReturnData();
                            final BookController bookController = this;
                            final RoutingContext context2 = context;
                            final Continuation $completion2 = $continuation;
                            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0 = this;
                            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1 = context;
                            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2 = returnData;
                            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).label = 1;
                            if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break;
                        }
                        case 1: {
                            returnData = (ReturnData)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2;
                            context = (RoutingContext)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1;
                            this = (BookController)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            checkAuth = $result;
                            break;
                        }
                        case 2: {
                            l$6 = ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$6;
                            ref$ObjectRef = (Ref$ObjectRef)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$5;
                            l$7 = (String)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$4;
                            book2 = (Book)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$3;
                            l$8 = (String)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2;
                            returnData2 = (ReturnData)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1;
                            this = (BookController)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            bookInfo$default = $result;
                            break Label_1032;
                        }
                        case 3: {
                            ref$ObjectRef = (Ref$ObjectRef)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$4;
                            l$7 = (String)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$3;
                            l$8 = (String)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2;
                            returnData2 = (ReturnData)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1;
                            this = (BookController)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            break Label_1181;
                        }
                        case 4: {
                            Label_1268: {
                                break Label_1268;
                                try {
                                    final BookController bookController2 = this;
                                    final Book book3 = (Book)ref$ObjectRef.element;
                                    final String s = l$7;
                                    final String s2 = (s == null) ? "" : s;
                                    final boolean b = true;
                                    final String s3 = l$8;
                                    final boolean b2 = false;
                                    final Mutex mutex = null;
                                    final Continuation continuation = $continuation;
                                    final int n = 48;
                                    final Object o = null;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0 = returnData2;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1 = ref$ObjectRef;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2 = null;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$3 = null;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$4 = null;
                                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).label = 4;
                                    if (getLocalChapterList$default(bookController2, book3, s2, b, s3, b2, mutex, continuation, n, o) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                    return ReturnData.setData$default(returnData2, ref$ObjectRef.element, null, 2, null);
                                    ref$ObjectRef = (Ref$ObjectRef)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1;
                                    returnData2 = (ReturnData)((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    return ReturnData.setData$default(returnData2, ref$ObjectRef.element, null, 2, null);
                                }
                                catch (final Exception ex) {}
                            }
                            return ReturnData.setData$default(returnData2, ref$ObjectRef.element, null, 2, null);
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    if (!(boolean)checkAuth) {
                        return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                    }
                    String bookUrl;
                    String newBookUrl;
                    String bookSourceUrl = null;
                    if (context.request().method() == HttpMethod.POST) {
                        final String string = context.getBodyAsJson().getString("bookUrl");
                        Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"bookUrl\")");
                        bookUrl = string;
                        final String string2 = context.getBodyAsJson().getString("newUrl");
                        Intrinsics.checkNotNullExpressionValue((Object)string2, "context.bodyAsJson.getString(\"newUrl\")");
                        newBookUrl = string2;
                        Intrinsics.checkNotNullExpressionValue((Object)context.getBodyAsJson().getString("bookSourceUrl"), "context.bodyAsJson.getString(\"bookSourceUrl\")");
                    }
                    else {
                        final List queryParam = context.queryParam("bookUrl");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookUrl\")");
                        final String s4 = (String)CollectionsKt.firstOrNull(queryParam);
                        bookUrl = ((s4 == null) ? "" : s4);
                        final List queryParam2 = context.queryParam("newUrl");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"newUrl\")");
                        final String s5 = (String)CollectionsKt.firstOrNull(queryParam2);
                        newBookUrl = ((s5 == null) ? "" : s5);
                        final List queryParam3 = context.queryParam("bookSourceUrl");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"bookSourceUrl\")");
                        final String s6 = (String)CollectionsKt.firstOrNull(queryParam3);
                        bookSourceUrl = ((s6 == null) ? "" : s6);
                    }
                    if (bookUrl.length() == 0) {
                        return returnData.setErrorMsg("\u4e66\u7c4d\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
                    }
                    if (newBookUrl.length() == 0) {
                        return returnData.setErrorMsg("\u65b0\u6e90\u4e66\u7c4d\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
                    }
                    if (bookSourceUrl.length() == 0) {
                        return returnData.setErrorMsg("\u4e66\u6e90\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
                    }
                    final String userNameSpace = this.getUserNameSpace(context);
                    final Book book = this.getShelfBookByURL(bookUrl, userNameSpace);
                    if (book == null) {
                        return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
                    }
                    final String bookSourceString = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
                    Book searchBook = null;
                    final CharSequence charSequence = bookSourceString;
                    if (charSequence == null || charSequence.length() == 0) {
                        final JsonArray localBookSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), "bookSource"));
                        if (localBookSourceList != null) {
                            int j = 0;
                            final int size = localBookSourceList.size();
                            if (j < size) {
                                do {
                                    final int i = j;
                                    ++j;
                                    final SearchBook _searchBook = (SearchBook)localBookSourceList.getJsonObject(i).mapTo((Class)SearchBook.class);
                                    if (_searchBook.getBookUrl().equals(newBookUrl)) {
                                        searchBook = _searchBook.toBook();
                                        break;
                                    }
                                } while (j < size);
                            }
                        }
                        if (searchBook == null) {
                            return returnData.setErrorMsg("\u4e66\u6e90\u4fe1\u606f\u9519\u8bef");
                        }
                    }
                    final Ref$ObjectRef newBookInfo = new Ref$ObjectRef();
                    final Ref$ObjectRef ref$ObjectRef2 = (Ref$ObjectRef)(o2 = newBookInfo);
                    if (searchBook != null) {
                        element = searchBook;
                        break Label_1041;
                    }
                    final CharSequence charSequence2 = bookSourceString;
                    if (charSequence2 == null || charSequence2.length() == 0) {
                        return returnData.setErrorMsg("\u4e66\u6e90\u4fe1\u606f\u9519\u8bef");
                    }
                    l$6 = ref$ObjectRef2;
                    final WebBook webBook = new WebBook(bookSourceString, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
                    final String s7 = newBookUrl;
                    final boolean b3 = false;
                    final Continuation continuation2 = $continuation;
                    final int n2 = 2;
                    final Object o3 = null;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0 = this;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1 = returnData;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2 = userNameSpace;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$3 = book;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$4 = bookSourceString;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$5 = newBookInfo;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$6 = l$6;
                    ((BookController$setBookSource.BookController$setBookSource$1)$continuation).label = 2;
                    if ((bookInfo$default = WebBook.getBookInfo$default(webBook, s7, b3, continuation2, n2, o3)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                final Object o4 = bookInfo$default;
                o2 = l$6;
                element = (Book)o4;
            }
            ((Ref$ObjectRef)o2).element = element;
            final BookController bookController3 = this;
            final Book book4 = book2;
            final String userNameSpace2 = l$8;
            final Function1 handler = (Function1)new BookController$setBookSource.BookController$setBookSource$2(ref$ObjectRef);
            final Continuation $completion3 = $continuation;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$0 = this;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$1 = returnData2;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$2 = l$8;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$3 = l$7;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$4 = ref$ObjectRef;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$5 = null;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).L$6 = null;
            ((BookController$setBookSource.BookController$setBookSource$1)$continuation).label = 3;
            if (bookController3.editShelfBook(book4, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
            continue;
        }
    }
    
    @Nullable
    public final Object saveBookConfig(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveBookConfig.BookController$saveBookConfig$1) {
                final BookController$saveBookConfig.BookController$saveBookConfig$1 bookController$saveBookConfig$1 = (BookController$saveBookConfig.BookController$saveBookConfig$1)$completion;
                if ((bookController$saveBookConfig$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveBookConfig.BookController$saveBookConfig$1 bookController$saveBookConfig$2 = bookController$saveBookConfig$1;
                    bookController$saveBookConfig$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveBookConfig.BookController$saveBookConfig$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Book book2;
        final ReturnData returnData2;
        Object editShelfBook = null;
        Label_0587: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$0 = this;
                    ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$1 = context;
                    ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$2 = returnData;
                    ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$1;
                    this = (BookController)((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    book2 = (Book)((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$1;
                    returnData2 = (ReturnData)((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    editShelfBook = $result;
                    break Label_0587;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final Ref$FloatRef pdfImageWidth = new Ref$FloatRef();
            String bookUrl;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("bookUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"bookUrl\")");
                bookUrl = string;
                final Ref$FloatRef ref$FloatRef = pdfImageWidth;
                final Float float1 = context.getBodyAsJson().getFloat("pdfImageWidth", Boxing.boxFloat(0.0f));
                Intrinsics.checkNotNullExpressionValue((Object)float1, "context.bodyAsJson.getFloat(\"pdfImageWidth\", 0f)");
                ref$FloatRef.element = float1.floatValue();
            }
            else {
                final List queryParam = context.queryParam("bookUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookUrl\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookUrl = ((s == null) ? "" : s);
                final Ref$FloatRef ref$FloatRef2 = pdfImageWidth;
                final List queryParam2 = context.queryParam("pdfImageWidth");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"pdfImageWidth\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                float element;
                if (s2 == null) {
                    element = 0.0f;
                }
                else {
                    final Float boxFloat = Boxing.boxFloat(Float.parseFloat(s2));
                    element = ((boxFloat == null) ? 0.0f : boxFloat);
                }
                ref$FloatRef2.element = element;
            }
            if (bookUrl.length() == 0) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final Book book = this.getShelfBookByURL(bookUrl, userNameSpace);
            if (book == null) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
            }
            if (pdfImageWidth.element <= 0.0f) {
                return returnData.setErrorMsg("pdf\u56fe\u7247\u5bbd\u5ea6\u9519\u8bef");
            }
            final BookController bookController2 = this;
            final Book book3 = book;
            final String userNameSpace2 = userNameSpace;
            final Function1 handler = (Function1)new BookController$saveBookConfig$newBook.BookController$saveBookConfig$newBook$1(pdfImageWidth);
            final Continuation $completion3 = $continuation;
            ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$0 = returnData;
            ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$1 = book;
            ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).L$2 = null;
            ((BookController$saveBookConfig.BookController$saveBookConfig$1)$continuation).label = 2;
            if ((editShelfBook = bookController2.editShelfBook(book3, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final Book newBook = (Book)editShelfBook;
        final ReturnData returnData3 = returnData2;
        final Book book4 = newBook;
        return ReturnData.setData$default(returnData3, (book4 == null) ? book2 : book4, null, 2, null);
    }
    
    @Nullable
    public final Object saveBookGroupId(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveBookGroupId.BookController$saveBookGroupId$1) {
                final BookController$saveBookGroupId.BookController$saveBookGroupId$1 bookController$saveBookGroupId$1 = (BookController$saveBookGroupId.BookController$saveBookGroupId$1)$completion;
                if ((bookController$saveBookGroupId$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveBookGroupId.BookController$saveBookGroupId$1 bookController$saveBookGroupId$2 = bookController$saveBookGroupId$1;
                    bookController$saveBookGroupId$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveBookGroupId.BookController$saveBookGroupId$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Book book2;
        final Ref$LongRef ref$LongRef;
        final ReturnData returnData2;
        Label_0598: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$0 = this;
                    ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$1 = context;
                    ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$2 = returnData;
                    ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$1;
                    this = (BookController)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    book2 = (Book)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$2;
                    ref$LongRef = (Ref$LongRef)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$1;
                    returnData2 = (ReturnData)((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0598;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            final Ref$LongRef groupId = new Ref$LongRef();
            String bookUrl;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("bookUrl");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"bookUrl\")");
                bookUrl = string;
                final Ref$LongRef ref$LongRef2 = groupId;
                final Long long1 = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                Intrinsics.checkNotNullExpressionValue((Object)long1, "context.bodyAsJson.getLong(\"groupId\", 0)");
                ref$LongRef2.element = long1.longValue();
            }
            else {
                final List queryParam = context.queryParam("bookUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookUrl\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookUrl = ((s == null) ? "" : s);
                final Ref$LongRef ref$LongRef3 = groupId;
                final List queryParam2 = context.queryParam("groupId");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"groupId\")");
                final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
                long element;
                if (s2 == null) {
                    element = 0L;
                }
                else {
                    final Long boxLong = Boxing.boxLong(Long.parseLong(s2));
                    element = ((boxLong == null) ? 0L : boxLong);
                }
                ref$LongRef3.element = element;
            }
            if (bookUrl.length() == 0) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u94fe\u63a5\u4e0d\u80fd\u4e3a\u7a7a");
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final Book book = this.getShelfBookByURL(bookUrl, userNameSpace);
            if (book == null) {
                return returnData.setErrorMsg("\u4e66\u7c4d\u4fe1\u606f\u9519\u8bef");
            }
            if (groupId.element <= 0L) {
                return returnData.setErrorMsg("\u5206\u7ec4\u4fe1\u606f\u9519\u8bef");
            }
            final BookController bookController2 = this;
            final Book book3 = book;
            final String userNameSpace2 = userNameSpace;
            final Function1 handler = (Function1)new BookController$saveBookGroupId.BookController$saveBookGroupId$2(groupId);
            final Continuation $completion3 = $continuation;
            ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$0 = returnData;
            ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$1 = groupId;
            ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).L$2 = book;
            ((BookController$saveBookGroupId.BookController$saveBookGroupId$1)$continuation).label = 2;
            if (bookController2.editShelfBook(book3, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        book2.setGroup(ref$LongRef.element);
        return ReturnData.setData$default(returnData2, book2, null, 2, null);
    }
    
    @Nullable
    public final Object addBookGroupMulti(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$addBookGroupMulti.BookController$addBookGroupMulti$1) {
                final BookController$addBookGroupMulti.BookController$addBookGroupMulti$1 bookController$addBookGroupMulti$1 = (BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$completion;
                if ((bookController$addBookGroupMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$addBookGroupMulti.BookController$addBookGroupMulti$1 bookController$addBookGroupMulti$2 = bookController$addBookGroupMulti$1;
                    bookController$addBookGroupMulti$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$addBookGroupMulti.BookController$addBookGroupMulti$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData2 = null;
        while (true) {
            int i$1 = 0;
            int i$2 = 0;
            Label_0486: {
                ReturnData returnData = null;
                Object checkAuth = null;
                switch (((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final BookController bookController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$0 = this;
                        ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$1 = context;
                        ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$2 = returnData;
                        ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).label = 1;
                        if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$1;
                        this = (BookController)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        checkAuth = $result;
                        break;
                    }
                    case 2: {
                        i$1 = ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).I$1;
                        i$2 = ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).I$0;
                        final long j$0 = ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).J$0;
                        final JsonArray bookJsonArray = (JsonArray)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$3;
                        final String userNameSpace = (String)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$2;
                        returnData2 = (ReturnData)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$1;
                        this = (BookController)((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break Label_0486;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (!(boolean)checkAuth) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                }
                final Long long1 = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                Intrinsics.checkNotNullExpressionValue((Object)long1, "context.bodyAsJson.getLong(\"groupId\", 0)");
                final long groupId = long1.longValue();
                if (groupId <= 0L) {
                    return returnData.setErrorMsg("\u5206\u7ec4\u4fe1\u606f\u9519\u8bef");
                }
                final String userNameSpace = this.getUserNameSpace(context);
                final JsonArray bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
                i$2 = 0;
                i$1 = bookJsonArray.size();
                if (i$2 >= i$1) {
                    return ReturnData.setData$default(returnData2, "", null, 2, null);
                }
                final int k = i$2;
                ++i$2;
                final Book book = (Book)bookJsonArray.getJsonObject(k).mapTo((Class)Book.class);
                final BookController bookController2 = this;
                final Book book2 = book;
                Intrinsics.checkNotNullExpressionValue((Object)book2, "book");
                final Book book3 = book2;
                final String userNameSpace2 = userNameSpace;
                final Function1 handler = (Function1)new BookController$addBookGroupMulti.BookController$addBookGroupMulti$2(groupId);
                final Continuation $completion3 = $continuation;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$0 = this;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$1 = returnData;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$2 = userNameSpace;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).L$3 = bookJsonArray;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).J$0 = groupId;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).I$0 = i$2;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).I$1 = i$1;
                ((BookController$addBookGroupMulti.BookController$addBookGroupMulti$1)$continuation).label = 2;
                if (bookController2.editShelfBook(book3, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            if (i$2 < i$1) {
                continue;
            }
            break;
        }
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final Object removeBookGroupMulti(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1) {
                final BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1 bookController$removeBookGroupMulti$1 = (BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$completion;
                if ((bookController$removeBookGroupMulti$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1 bookController$removeBookGroupMulti$2 = bookController$removeBookGroupMulti$1;
                    bookController$removeBookGroupMulti$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData2 = null;
        while (true) {
            int i$1 = 0;
            int i$2 = 0;
            Label_0486: {
                ReturnData returnData = null;
                Object checkAuth = null;
                switch (((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        returnData = new ReturnData();
                        final BookController bookController = this;
                        final RoutingContext context2 = context;
                        final Continuation $completion2 = $continuation;
                        ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$0 = this;
                        ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$1 = context;
                        ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$2 = returnData;
                        ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).label = 1;
                        if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        break;
                    }
                    case 1: {
                        returnData = (ReturnData)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$2;
                        context = (RoutingContext)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$1;
                        this = (BookController)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        checkAuth = $result;
                        break;
                    }
                    case 2: {
                        i$1 = ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).I$1;
                        i$2 = ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).I$0;
                        final long j$0 = ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).J$0;
                        final JsonArray bookJsonArray = (JsonArray)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$3;
                        final String userNameSpace = (String)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$2;
                        returnData2 = (ReturnData)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$1;
                        this = (BookController)((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$0;
                        ResultKt.throwOnFailure($result);
                        break Label_0486;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                if (!(boolean)checkAuth) {
                    return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                }
                final Long long1 = context.getBodyAsJson().getLong("groupId", Boxing.boxLong(0L));
                Intrinsics.checkNotNullExpressionValue((Object)long1, "context.bodyAsJson.getLong(\"groupId\", 0)");
                final long groupId = long1.longValue();
                if (groupId <= 0L) {
                    return returnData.setErrorMsg("\u5206\u7ec4\u4fe1\u606f\u9519\u8bef");
                }
                final String userNameSpace = this.getUserNameSpace(context);
                final JsonArray bookJsonArray = context.getBodyAsJson().getJsonArray("bookList", new JsonArray());
                i$2 = 0;
                i$1 = bookJsonArray.size();
                if (i$2 >= i$1) {
                    return ReturnData.setData$default(returnData2, "", null, 2, null);
                }
                final int k = i$2;
                ++i$2;
                final Book book = (Book)bookJsonArray.getJsonObject(k).mapTo((Class)Book.class);
                final BookController bookController2 = this;
                final Book book2 = book;
                Intrinsics.checkNotNullExpressionValue((Object)book2, "book");
                final Book book3 = book2;
                final String userNameSpace2 = userNameSpace;
                final Function1 handler = (Function1)new BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$2(groupId);
                final Continuation $completion3 = $continuation;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$0 = this;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$1 = returnData;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$2 = userNameSpace;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).L$3 = bookJsonArray;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).J$0 = groupId;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).I$0 = i$2;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).I$1 = i$1;
                ((BookController$removeBookGroupMulti.BookController$removeBookGroupMulti$1)$continuation).label = 2;
                if (bookController2.editShelfBook(book3, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            if (i$2 < i$1) {
                continue;
            }
            break;
        }
        return ReturnData.setData$default(returnData2, "", null, 2, null);
    }
    
    @Nullable
    public final Object deleteBook(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$deleteBook.BookController$deleteBook$1) {
                final BookController$deleteBook.BookController$deleteBook$1 bookController$deleteBook$1 = (BookController$deleteBook.BookController$deleteBook$1)$completion;
                if ((bookController$deleteBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$deleteBook.BookController$deleteBook$1 bookController$deleteBook$2 = bookController$deleteBook$1;
                    bookController$deleteBook$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$deleteBook.BookController$deleteBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$deleteBook.BookController$deleteBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$deleteBook.BookController$deleteBook$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$0 = this;
                ((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$1 = context;
                ((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$2 = returnData;
                ((BookController$deleteBook.BookController$deleteBook$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$2;
                context = (RoutingContext)((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$1;
                this = (BookController)((BookController$deleteBook.BookController$deleteBook$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final Book book = (Book)context.getBodyAsJson().mapTo((Class)Book.class);
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        int existIndex = -1;
        String bookName = "";
        String bookAuthor = "";
        int j = 0;
        final int size = bookshelf.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final String string = bookshelf.getJsonObject(i).getString("name", "");
                Intrinsics.checkNotNullExpressionValue((Object)string, "bookshelf.getJsonObject(i).getString(\"name\", \"\")");
                bookName = string;
                final String string2 = bookshelf.getJsonObject(i).getString("author", "");
                Intrinsics.checkNotNullExpressionValue((Object)string2, "bookshelf.getJsonObject(i).getString(\"author\", \"\")");
                bookAuthor = string2;
                final String string3 = bookshelf.getJsonObject(i).getString("bookUrl", "");
                Intrinsics.checkNotNullExpressionValue((Object)string3, "bookshelf.getJsonObject(i).getString(\"bookUrl\", \"\")");
                final String bookUrl = string3;
                if (bookUrl.equals(book.getBookUrl())) {
                    existIndex = i;
                    break;
                }
                if (bookName.equals(book.getName()) && bookAuthor.equals(book.getAuthor())) {
                    existIndex = i;
                    break;
                }
            } while (j < size);
        }
        if (existIndex < 0) {
            return returnData.setErrorMsg("\u4e66\u67b6\u4e66\u7c4d\u4e0d\u5b58\u5728");
        }
        final JsonObject existBook = bookshelf.getJsonObject(existIndex);
        bookshelf.remove(existIndex);
        this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        final File localBookPath = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, bookName + '_' + bookAuthor));
        ExtKt.deleteRecursively(localBookPath);
        final CharSequence charSequence = existBook.getString("coverUrl");
        if (charSequence != null && charSequence.length() != 0) {
            final String string4 = existBook.getString("coverUrl");
            Intrinsics.checkNotNull((Object)string4);
            if (StringsKt.startsWith$default(string4, "/", false, 2, (Object)null)) {
                final String[] subDirFiles = { "storage", null };
                final int n = 1;
                final String string5 = existBook.getString("coverUrl");
                Intrinsics.checkNotNull((Object)string5);
                subDirFiles[n] = string5;
                final String cachePath = ExtKt.getWorkDir(subDirFiles);
                FileUtils.INSTANCE.deleteFile(cachePath);
            }
        }
        return ReturnData.setData$default(returnData, "\u5220\u9664\u4e66\u7c4d\u6210\u529f", null, 2, null);
    }
    
    @Nullable
    public final Object deleteBooks(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$deleteBooks.BookController$deleteBooks$1) {
                final BookController$deleteBooks.BookController$deleteBooks$1 bookController$deleteBooks$1 = (BookController$deleteBooks.BookController$deleteBooks$1)$completion;
                if ((bookController$deleteBooks$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$deleteBooks.BookController$deleteBooks$1 bookController$deleteBooks$2 = bookController$deleteBooks$1;
                    bookController$deleteBooks$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$deleteBooks.BookController$deleteBooks$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$0 = this;
                ((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$1 = context;
                ((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$2 = returnData;
                ((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$2;
                context = (RoutingContext)((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$1;
                this = (BookController)((BookController$deleteBooks.BookController$deleteBooks$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final JsonArray bookJsonArray = context.getBodyAsJsonArray();
        final String userNameSpace = this.getUserNameSpace(context);
        JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            bookshelf = new JsonArray();
        }
        final Map infoMap = new LinkedHashMap();
        int j = 0;
        final int size = bookJsonArray.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = infoMap;
                final String string = bookJsonArray.getJsonObject(i).getString("bookUrl", "");
                Intrinsics.checkNotNullExpressionValue((Object)string, "bookJsonArray.getJsonObject(i).getString(\"bookUrl\", \"\")");
                map.put(string, Boxing.boxInt(i));
                infoMap.put(bookJsonArray.getJsonObject(i).getString("name", "") + '_' + (Object)bookshelf.getJsonObject(i).getString("author", ""), Boxing.boxInt(i));
            } while (j < size);
        }
        final Iterator iterator2 = bookshelf.iterator();
        Intrinsics.checkNotNullExpressionValue((Object)iterator2, "bookshelf.iterator()");
        final Iterator iterator = iterator2;
        while (iterator.hasNext()) {
            final JsonObject next = iterator.next();
            if (next == null) {
                throw new NullPointerException("null cannot be cast to non-null type io.vertx.core.json.JsonObject");
            }
            final JsonObject book = next;
            final String bookName = book.getString("name", "");
            final String bookAuthor = book.getString("author", "");
            final String bookUrl = book.getString("bookUrl", "");
            final Map map2 = infoMap;
            final String key = bookUrl;
            Intrinsics.checkNotNullExpressionValue((Object)key, "bookUrl");
            final int existIndex = map2.getOrDefault(key, infoMap.getOrDefault(bookName + '_' + (Object)bookAuthor, Boxing.boxInt(-1))).intValue();
            if (existIndex < 0) {
                continue;
            }
            iterator.remove();
            final File localBookPath = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, bookName + '_' + (Object)bookAuthor));
            ExtKt.deleteRecursively(localBookPath);
        }
        this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    @Nullable
    public final Object saveBookInfoCache(@NotNull final List<Book> bookList, @NotNull final Continuation<? super List<Book>> $completion) {
        if (bookList.size() > 0) {
            int j = 0;
            final int size = bookList.size();
            if (j < size) {
                do {
                    final int i = j;
                    ++j;
                    final Book book = (Book)bookList.get(i);
                    final ACache bookInfoCache = this.bookInfoCache;
                    final String bookUrl = book.getBookUrl();
                    final Map map = JsonObject.mapFrom((Object)book).getMap();
                    Intrinsics.checkNotNullExpressionValue((Object)map, "mapFrom(book).map");
                    bookInfoCache.put(bookUrl, ExtKt.jsonEncode$default(map, false, 2, null));
                } while (j < size);
            }
        }
        return bookList;
    }
    
    @Nullable
    public final Object mergeBookCacheInfo(@NotNull final Book book, @NotNull final Continuation<? super Book> $completion) {
        final String asString = this.bookInfoCache.getAsString(book.getBookUrl());
        Book book2;
        if (asString == null) {
            book2 = null;
        }
        else {
            final Map<String, Object> map = ExtKt.toMap(asString);
            if (map == null) {
                book2 = null;
            }
            else {
                final Map $this$toDataClass$iv = map;
                final int $i$f$toDataClass = 0;
                final Object $this$convert$iv$iv = $this$toDataClass$iv;
                final int $i$f$convert = 0;
                final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                book2 = (Book)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
            }
        }
        final Book cacheInfo = book2;
        if (cacheInfo != null) {
            return ExtKt.fillData(book, cacheInfo, CollectionsKt.listOf((Object[])new String[] { "name", "author", "coverUrl", "tocUrl", "intro", "latestChapterTitle", "wordCount" }));
        }
        return book;
    }
    
    @Nullable
    public final Object getBookShelfBooks(final boolean refresh, @NotNull final String userNameSpace, @NotNull final Continuation<? super List<Book>> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getBookShelfBooks.BookController$getBookShelfBooks$1) {
                final BookController$getBookShelfBooks.BookController$getBookShelfBooks$1 bookController$getBookShelfBooks$1 = (BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$completion;
                if ((bookController$getBookShelfBooks$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getBookShelfBooks.BookController$getBookShelfBooks$1 bookController$getBookShelfBooks$2 = bookController$getBookShelfBooks$1;
                    bookController$getBookShelfBooks$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getBookShelfBooks.BookController$getBookShelfBooks$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final Ref$ObjectRef ref$ObjectRef;
        switch (((BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final Ref$ObjectRef bookshelf = new Ref$ObjectRef();
                bookshelf.element = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
                if (bookshelf.element == null) {
                    return new ArrayList();
                }
                if (((JsonArray)bookshelf.element).size() == 0) {
                    return new ArrayList();
                }
                final Ref$ObjectRef bookList = new Ref$ObjectRef();
                bookList.element = new ArrayList();
                final int concurrentCount = 16;
                final Mutex mutex = MutexKt.Mutex$default(false, 1, (Object)null);
                final Mutex syncMutex = MutexKt.Mutex$default(false, 1, (Object)null);
                final int concurrentCount2 = concurrentCount;
                final int startIndex = 0;
                final int size = ((JsonArray)bookshelf.element).size();
                final Function3 handler = (Function3)new BookController$getBookShelfBooks.BookController$getBookShelfBooks$2(bookshelf, refresh, this, userNameSpace, syncMutex, bookList, mutex, (Continuation)null);
                final Continuation $completion2 = $continuation;
                ((BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$continuation).L$0 = bookList;
                ((BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$continuation).label = 1;
                if (this.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                ref$ObjectRef = (Ref$ObjectRef)((BookController$getBookShelfBooks.BookController$getBookShelfBooks$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return ref$ObjectRef.element;
    }
    
    @Nullable
    public final Object getLocalChapterList(@NotNull Book book, @Nullable String bookSource, final boolean refresh, @NotNull String userNameSpace, boolean var_5_2AA, @Nullable Mutex mutex, @NotNull final Continuation<? super List<BookChapter>> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$getLocalChapterList.BookController$getLocalChapterList$1) {
                final BookController$getLocalChapterList.BookController$getLocalChapterList$1 bookController$getLocalChapterList$1 = (BookController$getLocalChapterList.BookController$getLocalChapterList$1)$completion;
                if ((bookController$getLocalChapterList$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getLocalChapterList.BookController$getLocalChapterList$1 bookController$getLocalChapterList$2 = bookController$getLocalChapterList$1;
                    bookController$getLocalChapterList$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$getLocalChapterList.BookController$getLocalChapterList$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        List list = null;
        String s2 = null;
        ACache l$6 = null;
    Label_1311_Outer:
        while (true) {
            Label_1221: {
                while (true) {
                    switch (((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            final String md5Encode = MD5Utils.INSTANCE.md5Encode(book.getBookUrl()).toString();
                            final ACache bookChaptersCache = this.getBookChaptersCache(userNameSpace);
                            JsonArray jsonArray = null;
                            JsonArray chapterList = null;
                            if (book.isInShelf()) {
                                jsonArray = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), md5Encode));
                            }
                            else {
                                chapterList = ExtKt.asJsonArray(bookChaptersCache.getAsString(book.getName() + '_' + book.getAuthor() + md5Encode));
                            }
                            if (chapterList != null && !refresh) {
                                final ArrayList localChapterList = new ArrayList();
                                int j = 0;
                                final int size = jsonArray.size();
                                if (j < size) {
                                    do {
                                        final int i = j;
                                        ++j;
                                        final BookChapter _chapter = (BookChapter)jsonArray.getJsonObject(i).mapTo((Class)BookChapter.class);
                                        localChapterList.add(_chapter);
                                    } while (j < size);
                                }
                                return localChapterList;
                            }
                            book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                            book.setUserNameSpace(userNameSpace);
                            if (!book.isLocalBook()) {
                                break Label_1311_Outer;
                            }
                            if (book.isEpub() && !this.extractEpub(book, refresh)) {
                                throw new Exception("Epub\u4e66\u7c4d\u89e3\u538b\u5931\u8d25");
                            }
                            if (book.isCbz() && !this.extractCbz(book, refresh)) {
                                throw new Exception("CBZ\u4e66\u7c4d\u89e3\u538b\u5931\u8d25");
                            }
                            if (book.isPdf() && !this.convertPdfToImage(book, refresh)) {
                                throw new Exception("PDF\u4e66\u7c4d\u8f6c\u6362\u5931\u8d25");
                            }
                            list = LocalBook.INSTANCE.getChapterList(book);
                            break;
                        }
                        case 2: {
                            continue Label_1311_Outer;
                        }
                        case 1: {
                            Label_0677: {
                                break Label_0677;
                                try {
                                    final CharSequence charSequence = bookSource;
                                Label_0929_Outer:
                                    while (true) {
                                        if (charSequence != null && charSequence.length() != 0) {
                                            Object bookSourceObject = null;
                                            final Object fromJson-IoAF18A = BookSource.Companion.fromJson-IoAF18A(bookSource);
                                            bookSourceObject = (Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                                            final BookSource bookSource2 = (BookSource)bookSourceObject;
                                            if (bookSource2 != null) {
                                                final TocRule ruleToc = bookSource2.getRuleToc();
                                                if (ruleToc != null) {
                                                    final String preUpdateJs = ruleToc.getPreUpdateJs();
                                                    if (preUpdateJs != null) {
                                                        final String it = preUpdateJs;
                                                        final int n = 0;
                                                        AnalyzeRule.evalJS$default(new AnalyzeRule(book, (BaseSource)bookSourceObject, null, 4, null), it, null, 2, null);
                                                    }
                                                }
                                            }
                                            if (StringsKt.isBlank((CharSequence)book.getTocUrl())) {
                                                final WebBook webBook = new WebBook(bookSource, debugLog, null, userNameSpace, 4, null);
                                                final boolean b = false;
                                                final Continuation continuation = $continuation;
                                                final int n2 = 2;
                                                final Object o = null;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0 = this;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1 = book;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2 = bookSource;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3 = userNameSpace;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4 = mutex;
                                                final String md5Encode;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5 = md5Encode;
                                                final ACache bookChaptersCache;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6 = bookChaptersCache;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).Z$0 = debugLog;
                                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label = 1;
                                                if (WebBook.getBookInfo$default(webBook, book, b, continuation, n2, o) == coroutine_SUSPENDED) {
                                                    return coroutine_SUSPENDED;
                                                }
                                            }
                                        }
                                        final String s = bookSource;
                                        Intrinsics.checkNotNull((Object)s);
                                        final WebBook webBook2 = new WebBook(s, var_5_2AA, null, userNameSpace, 4, null);
                                        final Book book2 = book;
                                        final Continuation $completion2 = $continuation;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0 = this;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1 = book;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2 = bookSource;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3 = userNameSpace;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4 = mutex;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5 = s2;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6 = l$6;
                                        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label = 2;
                                        Object chapterList2;
                                        if ((chapterList2 = webBook2.getChapterList(book2, (Continuation<? super List<BookChapter>>)$completion2)) == coroutine_SUSPENDED) {
                                            return coroutine_SUSPENDED;
                                        }
                                        while (true) {
                                            list = (List)chapterList2;
                                            break;
                                            l$6 = (ACache)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6;
                                            s2 = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5;
                                            mutex = (Mutex)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4;
                                            userNameSpace = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3;
                                            bookSource = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2;
                                            book = (Book)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1;
                                            this = (BookController)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0;
                                            ResultKt.throwOnFailure($result);
                                            chapterList2 = $result;
                                            continue Label_1311_Outer;
                                        }
                                        var_5_2AA = ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).Z$0;
                                        l$6 = (ACache)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6;
                                        s2 = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5;
                                        mutex = (Mutex)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4;
                                        userNameSpace = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3;
                                        bookSource = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2;
                                        book = (Book)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1;
                                        this = (BookController)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0;
                                        ResultKt.throwOnFailure($result);
                                        continue Label_0929_Outer;
                                    }
                                }
                                catch (final Exception e) {
                                    final CharSequence charSequence2 = bookSource;
                                    if (charSequence2 != null && charSequence2.length() != 0) {
                                        final Object fromJson-IoAF18A2 = BookSource.Companion.fromJson-IoAF18A(bookSource);
                                        final BookSource bookSourceObject2 = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A2) ? null : fromJson-IoAF18A2);
                                        if (bookSourceObject2 != null) {
                                            final Map info = MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)"sourceUrl", (Object)bookSourceObject2.getBookSourceUrl()), TuplesKt.to((Object)"time", (Object)Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to((Object)"error", (Object)e.toString()) });
                                            this.addInvalidBookSource(bookSourceObject2.getBookSourceUrl(), info, userNameSpace);
                                        }
                                    }
                                    final Mutex mutex2 = mutex;
                                    if (mutex2 == null) {
                                        break Label_1221;
                                    }
                                    final Mutex mutex3 = mutex2;
                                    final Object o2 = null;
                                    final Continuation continuation2 = $continuation;
                                    final int n3 = 1;
                                    final Object o3 = null;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0 = this;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1 = book;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2 = userNameSpace;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3 = mutex;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4 = e;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5 = null;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6 = null;
                                    ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label = 3;
                                    if (Mutex$DefaultImpls.lock$default(mutex3, o2, continuation2, n3, o3) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                    break Label_1221;
                                }
                            }
                        }
                        case 3: {
                            Exception l$7;
                            try {
                                l$7 = (Exception)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4;
                                mutex = (Mutex)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3;
                                userNameSpace = (String)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2;
                                book = (Book)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1;
                                this = (BookController)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                book.setLastCheckError(l$7.toString());
                                final BookController bookController = this;
                                final Book book3 = book;
                                final String userNameSpace2 = userNameSpace;
                                final Function1 handler = (Function1)new BookController$getLocalChapterList.BookController$getLocalChapterList$3(l$7);
                                final Continuation $completion3 = $continuation;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0 = mutex;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1 = l$7;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2 = null;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3 = null;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4 = null;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5 = null;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6 = null;
                                ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label = 4;
                                if (bookController.editShelfBook(book3, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion3) == coroutine_SUSPENDED) {
                                    return coroutine_SUSPENDED;
                                }
                                throw l$7;
                                l$7 = (Exception)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1;
                                mutex = (Mutex)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                throw l$7;
                            }
                            finally {
                                final Mutex mutex4 = mutex;
                                if (mutex4 != null) {
                                    Mutex$DefaultImpls.unlock$default(mutex4, (Object)null, 1, (Object)null);
                                }
                            }
                            throw l$7;
                        }
                        case 4: {
                            continue;
                        }
                        case 5: {
                            list = (List)((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            return list;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    break;
                }
            }
            break;
        }
        if (book.isInShelf()) {
            this.saveUserStorage(userNameSpace, ExtKt.getRelativePath(book.getName() + '_' + book.getAuthor(), s2), list);
        }
        else {
            l$6.put(book.getName() + '_' + book.getAuthor() + s2, ExtKt.jsonEncode$default(list, false, 2, null), 3600);
        }
        final BookController bookController2 = this;
        final Book book4 = book;
        final List bookChapterList = list;
        final String userNameSpace3 = userNameSpace;
        final Mutex mutex5 = mutex;
        final Continuation $completion4 = $continuation;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$0 = list;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$1 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$2 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$3 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$4 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$5 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).L$6 = null;
        ((BookController$getLocalChapterList.BookController$getLocalChapterList$1)$continuation).label = 5;
        if (bookController2.saveShelfBookLatestChapter(book4, bookChapterList, userNameSpace3, mutex5, (Continuation<? super Unit>)$completion4) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return list;
    }
    
    public static /* synthetic */ Object getLocalChapterList$default(final BookController bookController, final Book book, final String bookSource, boolean refresh, final String userNameSpace, boolean debugLog, Mutex mutex, final Continuation $completion, final int n, final Object o) {
        if ((n & 0x4) != 0x0) {
            refresh = false;
        }
        if ((n & 0x10) != 0x0) {
            debugLog = true;
        }
        if ((n & 0x20) != 0x0) {
            mutex = null;
        }
        return bookController.getLocalChapterList(book, bookSource, refresh, userNameSpace, debugLog, mutex, (Continuation<? super List<BookChapter>>)$completion);
    }
    
    @Nullable
    public final Object getBookSourceString(@NotNull final RoutingContext context, @NotNull final String sourceUrl, final boolean withExploreUrl, @NotNull final Continuation<? super String> $completion) {
        String bookSourceString = null;
        if (context.request().method() == HttpMethod.POST) {
            final JsonObject bookSource = context.getBodyAsJson().getJsonObject("bookSource");
            if (bookSource != null) {
                bookSourceString = bookSource.toString();
            }
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final CharSequence charSequence = bookSourceString;
        if (charSequence == null || charSequence.length() == 0) {
            String bookSourceUrl = null;
            if (context.request().method() == HttpMethod.POST) {
                final String string = context.getBodyAsJson().getString("bookSourceUrl", "");
                Intrinsics.checkNotNullExpressionValue((Object)string, "context.bodyAsJson.getString(\"bookSourceUrl\", \"\")");
                bookSourceUrl = string;
            }
            else {
                final List queryParam = context.queryParam("bookSourceUrl");
                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookSourceUrl\")");
                final String s = (String)CollectionsKt.firstOrNull(queryParam);
                bookSourceUrl = ((s == null) ? "" : s);
            }
            if (!StringsKt.isBlank((CharSequence)bookSourceUrl)) {
                bookSourceString = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
            }
        }
        final CharSequence charSequence2 = bookSourceString;
        if (charSequence2 == null || charSequence2.length() == 0) {
            final CharSequence charSequence3 = sourceUrl;
            if (charSequence3 != null && charSequence3.length() != 0) {
                bookSourceString = this.getBookSourceStringBySourceURLOpt(sourceUrl, userNameSpace);
            }
        }
        return bookSourceString;
    }
    
    public static /* synthetic */ Object getBookSourceString$default(final BookController bookController, final RoutingContext context, String sourceUrl, boolean withExploreUrl, final Continuation $completion, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            sourceUrl = "";
        }
        if ((n & 0x4) != 0x0) {
            withExploreUrl = false;
        }
        return bookController.getBookSourceString(context, sourceUrl, withExploreUrl, (Continuation<? super String>)$completion);
    }
    
    @Nullable
    public final String getBookSourceStringBySourceURLOpt(@NotNull final String sourceUrl, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)sourceUrl, "sourceUrl");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (StringsKt.isBlank((CharSequence)sourceUrl)) {
            return null;
        }
        File file = ExtKt.getStorageFile$default(new String[] { "data", userNameSpace, "bookSource" }, null, 2, null);
        if (!file.exists()) {
            file = ExtKt.getStorageFile$default(new String[] { "data", "default", "bookSource" }, null, 2, null);
            if (!file.exists()) {
                return null;
            }
        }
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final JsonFactory factory = objectMapper.getFactory();
            final Ref$ObjectRef bookSourceString = new Ref$ObjectRef();
            final Closeable closeable = (Closeable)factory.createParser(file);
            Throwable t = null;
            try {
                final JsonParser parser = (JsonParser)closeable;
                final int n = 0;
                if (parser.nextToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() == JsonToken.START_OBJECT) {
                            final TreeNode valueAsTree = parser.readValueAsTree();
                            Intrinsics.checkNotNullExpressionValue((Object)valueAsTree, "parser.readValueAsTree()");
                            final JsonNode jsonNode = (JsonNode)valueAsTree;
                            if (sourceUrl.equals(jsonNode.get("bookSourceUrl").asText())) {
                                bookSourceString.element = jsonNode.toString();
                                break;
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
            BookControllerKt.access$getLogger$p().info((Function0)new BookController$getBookSourceStringBySourceURLOpt.BookController$getBookSourceStringBySourceURLOpt$2(bookSourceString));
            return (String)bookSourceString.element;
        }
        catch (final Exception e) {
            BookControllerKt.access$getLogger$p().error("\u89e3\u6790\u6587\u4ef6\u5185\u5bb9\u51fa\u9519: {}  \u6587\u4ef6: \n{}", (Object)e, (Object)file);
            throw e;
        }
    }
    
    @Nullable
    public final Book getShelfBookByURL(@NotNull final String url, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (url.length() == 0) {
            return null;
        }
        final JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
        if (bookshelf == null) {
            return null;
        }
        int j = 0;
        final int size = bookshelf.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Map map = bookshelf.getJsonObject(i).getMap();
                Intrinsics.checkNotNullExpressionValue((Object)map, "bookshelf.getJsonObject(i).map");
                final Map $this$toDataClass$iv = map;
                final int $i$f$toDataClass = 0;
                final Object $this$convert$iv$iv = $this$toDataClass$iv;
                final int $i$f$convert = 0;
                final String json$iv$iv = (String)(($this$convert$iv$iv instanceof String) ? $this$convert$iv$iv : ExtKt.getGson().toJson($this$convert$iv$iv));
                final Book _book = (Book)ExtKt.getGson().fromJson(json$iv$iv, new TypeToken<Book>() {}.getType());
                if (_book.getBookUrl().equals(url)) {
                    _book.setRootDir(ExtKt.getWorkDir$default(null, 1, null));
                    _book.setUserNameSpace(userNameSpace);
                    _book.setInShelf(true);
                    return _book;
                }
            } while (j < size);
        }
        return null;
    }
    
    @Nullable
    public final Object saveShelfBookProgress(@NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final String userNameSpace, @NotNull final Continuation<? super Unit> $completion) {
        final Object editShelfBook = this.editShelfBook(book, userNameSpace, (Function1<? super Book, Book>)new BookController$saveShelfBookProgress.BookController$saveShelfBookProgress$2(bookChapter), (Continuation<? super Book>)$completion);
        if (editShelfBook == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return editShelfBook;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object saveShelfBookLatestChapter(@NotNull Book book, @NotNull List<BookChapter> bookChapterList, @NotNull String userNameSpace, @Nullable Mutex mutex, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1) {
                final BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1 bookController$saveShelfBookLatestChapter$1 = (BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$completion;
                if ((bookController$saveShelfBookLatestChapter$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1 bookController$saveShelfBookLatestChapter$2 = bookController$saveShelfBookLatestChapter$1;
                    bookController$saveShelfBookLatestChapter$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
    Label_0170_Outer:
        while (true) {
            while (true) {
                switch (((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).label) {
                    case 0: {
                        ResultKt.throwOnFailure($result);
                        try {
                            final Mutex mutex2 = mutex;
                            while (true) {
                                if (mutex2 != null) {
                                    final Mutex mutex3 = mutex2;
                                    final Object o = null;
                                    final Continuation continuation = $continuation;
                                    final int n = 1;
                                    final Object o2 = null;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$0 = this;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$1 = book;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$2 = bookChapterList;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$3 = userNameSpace;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$4 = mutex;
                                    ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).label = 1;
                                    if (Mutex$DefaultImpls.lock$default(mutex3, o, continuation, n, o2) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                }
                                final BookController bookController = this;
                                final Book book2 = book;
                                final String userNameSpace2 = userNameSpace;
                                final Function1 handler = (Function1)new BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$2(bookChapterList, book);
                                final Continuation $completion2 = $continuation;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$0 = mutex;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$1 = null;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$2 = null;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$3 = null;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$4 = null;
                                ((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).label = 2;
                                if (bookController.editShelfBook(book2, userNameSpace2, (Function1<? super Book, Book>)handler, (Continuation<? super Book>)$completion2) == coroutine_SUSPENDED) {
                                    return coroutine_SUSPENDED;
                                }
                                return Unit.INSTANCE;
                                mutex = (Mutex)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                return Unit.INSTANCE;
                                mutex = (Mutex)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$4;
                                userNameSpace = (String)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$3;
                                bookChapterList = (List)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$2;
                                book = (Book)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$1;
                                this = (BookController)((BookController$saveShelfBookLatestChapter.BookController$saveShelfBookLatestChapter$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                continue Label_0170_Outer;
                            }
                        }
                        finally {
                            final Mutex mutex4 = mutex;
                            if (mutex4 != null) {
                                Mutex$DefaultImpls.unlock$default(mutex4, (Object)null, 1, (Object)null);
                            }
                        }
                        return Unit.INSTANCE;
                    }
                    case 1: {
                        continue;
                    }
                    case 2: {
                        continue Label_0170_Outer;
                    }
                    default: {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
                break;
            }
            break;
        }
    }
    
    @Nullable
    public final Object editShelfBook(@NotNull Book book, @NotNull String userNameSpace, @NotNull Function1<? super Book, Book> handler, @NotNull final Continuation<? super Book> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$editShelfBook.BookController$editShelfBook$1) {
                final BookController$editShelfBook.BookController$editShelfBook$1 bookController$editShelfBook$1 = (BookController$editShelfBook.BookController$editShelfBook$1)$completion;
                if ((bookController$editShelfBook$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$editShelfBook.BookController$editShelfBook$1 bookController$editShelfBook$2 = bookController$editShelfBook$1;
                    bookController$editShelfBook$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$editShelfBook.BookController$editShelfBook$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0198: {
            switch (((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final UserMutex instance = UserMutex.INSTANCE;
                    final String stringPlus = Intrinsics.stringPlus(userNameSpace, (Object)"@bookshelf");
                    final Continuation $completion2 = $continuation;
                    ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$0 = this;
                    ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$1 = book;
                    ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$2 = userNameSpace;
                    ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$3 = handler;
                    ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).label = 1;
                    final Object locker;
                    if ((locker = instance.getLocker(stringPlus, (Continuation<? super Mutex>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break Label_0198;
                }
                case 1: {
                    handler = (Function1)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$3;
                    userNameSpace = (String)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$2;
                    book = (Book)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$1;
                    this = (BookController)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    final Object locker = $result;
                    break Label_0198;
                }
                case 2: {
                    Label_0278: {
                        break Label_0278;
                        final Object locker;
                        Mutex mutex = (Mutex)locker;
                        try {
                            BookControllerKt.access$getLogger$p().info("wait for lock {}", (Object)Intrinsics.stringPlus(userNameSpace, (Object)"@bookshelf"));
                            final Mutex mutex2 = mutex;
                            final Object o = null;
                            final Continuation continuation = $continuation;
                            final int n = 1;
                            final Object o2 = null;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$0 = this;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$1 = book;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$2 = userNameSpace;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$3 = handler;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$4 = mutex;
                            ((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).label = 2;
                            if (Mutex$DefaultImpls.lock$default(mutex2, o, continuation, n, o2) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            while (true) {
                                BookControllerKt.access$getLogger$p().info("lock success");
                                JsonArray bookshelf = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "bookshelf"));
                                if (bookshelf == null) {
                                    bookshelf = new JsonArray();
                                }
                                int existIndex = -1;
                                int j = 0;
                                final int size = bookshelf.size();
                                if (j < size) {
                                    do {
                                        final int i = j;
                                        ++j;
                                        final Book _book = (Book)bookshelf.getJsonObject(i).mapTo((Class)Book.class);
                                        if (book.getBookUrl().length() > 0 && _book.getBookUrl().equals(book.getBookUrl())) {
                                            existIndex = i;
                                            break;
                                        }
                                        if (book.getName().length() > 0 && _book.getName().equals(book.getName()) && book.getAuthor().length() > 0 && _book.getAuthor().equals(book.getAuthor())) {
                                            existIndex = i;
                                            break;
                                        }
                                    } while (j < size);
                                }
                                if (existIndex >= 0) {
                                    final List bookList = bookshelf.getList();
                                    Book existBook = (Book)bookshelf.getJsonObject(existIndex).mapTo((Class)Book.class);
                                    final Function1 function1 = handler;
                                    final Book book2 = existBook;
                                    Intrinsics.checkNotNullExpressionValue((Object)book2, "existBook");
                                    existBook = (Book)function1.invoke((Object)book2);
                                    bookList.set(existIndex, JsonObject.mapFrom((Object)existBook));
                                    bookshelf = new JsonArray(bookList);
                                    this.saveUserStorage(userNameSpace, "bookshelf", bookshelf);
                                    return existBook;
                                }
                                return null;
                                mutex = (Mutex)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$4;
                                handler = (Function1)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$3;
                                userNameSpace = (String)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$2;
                                book = (Book)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$1;
                                this = (BookController)((BookController$editShelfBook.BookController$editShelfBook$1)$continuation).L$0;
                                ResultKt.throwOnFailure($result);
                                continue;
                            }
                        }
                        finally {
                            Mutex$DefaultImpls.unlock$default(mutex, (Object)null, 1, (Object)null);
                        }
                    }
                    return null;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
        }
    }
    
    public final void saveBookSources(@NotNull final Book book, @NotNull final List<SearchBook> sourceList, @NotNull final String userNameSpace, final boolean replace) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        Intrinsics.checkNotNullParameter((Object)sourceList, "sourceList");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (book.getName().length() == 0) {
            return;
        }
        JsonArray bookSourceList = new JsonArray();
        if (!replace) {
            final JsonArray localBookSourceList = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, book.getName() + '_' + book.getAuthor(), "bookSource"));
            if (localBookSourceList != null) {
                bookSourceList = localBookSourceList;
            }
        }
        final Map urlMap = new LinkedHashMap();
        int j = 0;
        final int size = bookSourceList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final String bookUrl = bookSourceList.getJsonObject(i).getString("bookUrl");
                final Map map = urlMap;
                final String s = bookUrl;
                Intrinsics.checkNotNullExpressionValue((Object)s, "bookUrl");
                map.put(s, i);
            } while (j < size);
        }
        int l = 0;
        final int size2 = sourceList.size();
        if (l < size2) {
            do {
                final int k = l;
                ++l;
                final SearchBook searchBook = (SearchBook)sourceList.get(k);
                final int existIndex = urlMap.getOrDefault(searchBook.getBookUrl(), -1).intValue();
                if (existIndex >= 0) {
                    bookSourceList.set(existIndex, JsonObject.mapFrom((Object)searchBook));
                }
                else {
                    bookSourceList.add(JsonObject.mapFrom((Object)searchBook));
                    urlMap.put(searchBook.getBookUrl(), bookSourceList.size() - 1);
                }
            } while (l < size2);
        }
        this.saveUserStorage(userNameSpace, ExtKt.getRelativePath(book.getName() + '_' + book.getAuthor(), "bookSource"), bookSourceList);
    }
    
    public static /* synthetic */ void saveBookSources$default(final BookController bookController, final Book book, final List sourceList, final String userNameSpace, boolean replace, final int n, final Object o) {
        if ((n & 0x8) != 0x0) {
            replace = false;
        }
        bookController.saveBookSources(book, sourceList, userNameSpace, replace);
    }
    
    public final boolean extractEpub(@NotNull final Book book, final boolean force) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final File epubExtractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + (Object)File.separator + "index"));
        if (force || !epubExtractDir.exists()) {
            ExtKt.deleteRecursively(epubExtractDir);
            File localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName() + (Object)File.separator + "index.epub"));
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "localStore", 0, false, 6, (Object)null) > 0) {
                localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "webdav", 0, false, 6, (Object)null) > 0) {
                localEpubFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            BookControllerKt.access$getLogger$p().info("extractEpub from {} to {}", (Object)localEpubFile, (Object)epubExtractDir);
            final File $this$unzip = localEpubFile;
            final String string = epubExtractDir.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "epubExtractDir.toString()");
            if (!ExtKt.unzip($this$unzip, string)) {
                return false;
            }
        }
        return true;
    }
    
    public static /* synthetic */ boolean extractEpub$default(final BookController bookController, final Book book, boolean force, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            force = false;
        }
        return bookController.extractEpub(book, force);
    }
    
    public final boolean extractCbz(@NotNull final Book book, final boolean force) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final File extractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + (Object)File.separator + "index"));
        if (force || !extractDir.exists()) {
            ExtKt.deleteRecursively(extractDir);
            File localFile = new File(ExtKt.getWorkDir(book.getOriginName() + (Object)File.separator + "index.cbz"));
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "localStore", 0, false, 6, (Object)null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "webdav", 0, false, 6, (Object)null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            final File $this$unzip = localFile;
            final String string = extractDir.toString();
            Intrinsics.checkNotNullExpressionValue((Object)string, "extractDir.toString()");
            if (!ExtKt.unzip($this$unzip, string)) {
                return false;
            }
        }
        return true;
    }
    
    public static /* synthetic */ boolean extractCbz$default(final BookController bookController, final Book book, boolean force, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            force = false;
        }
        return bookController.extractCbz(book, force);
    }
    
    public final boolean convertPdfToImage(@NotNull final Book book, final boolean force) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        return true;
    }
    
    public static /* synthetic */ boolean convertPdfToImage$default(final BookController bookController, final Book book, boolean force, final int n, final Object o) {
        if ((n & 0x2) != 0x0) {
            force = false;
        }
        return bookController.convertPdfToImage(book, force);
    }
    
    public final void convertPdfPageToImage(@NotNull final Book book, final int index, final boolean force) {
        Intrinsics.checkNotNullParameter((Object)book, "book");
        final File extractDir = new File(ExtKt.getWorkDir(book.getBookUrl() + (Object)File.separator + "index"));
        if (!extractDir.exists()) {
            extractDir.mkdirs();
        }
        final String imageFormat = "png";
        final File output = new File(extractDir.toString() + (Object)File.separator + "output-" + index + '.' + imageFormat);
        if (force || !output.exists()) {
            ExtKt.deleteRecursively(output);
            File localFile = new File(ExtKt.getWorkDir(book.getOriginName() + (Object)File.separator + "index.pdf"));
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "localStore", 0, false, 6, (Object)null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            if (StringsKt.indexOf$default((CharSequence)book.getOriginName(), "webdav", 0, false, 6, (Object)null) > 0) {
                localFile = new File(ExtKt.getWorkDir(book.getOriginName()));
            }
            final PDDocument document = PDDocument.load(localFile);
            final PDFRenderer renderer = new PDFRenderer(document);
            final float targetWidth = book.getPdfImageWidth();
            Intrinsics.checkNotNullExpressionValue((Object)document, "document");
            this.savePdfPageToImage(document, renderer, index, targetWidth, imageFormat, output);
        }
    }
    
    public final void savePdfPageToImage(@NotNull final PDDocument document, @NotNull final PDFRenderer renderer, final int index, final float targetWidth, @NotNull final String imageFormat, @NotNull final File output) {
        Intrinsics.checkNotNullParameter((Object)document, "document");
        Intrinsics.checkNotNullParameter((Object)renderer, "renderer");
        Intrinsics.checkNotNullParameter((Object)imageFormat, "imageFormat");
        Intrinsics.checkNotNullParameter((Object)output, "output");
        final float dpi = 300.0f;
        final PDPage page = document.getPage(index);
        final PDRectangle pageSize = page.getCropBox();
        final float targetHeight = 0.0f;
        final float scaleFactor = targetWidth / pageSize.getWidth();
        final float scaledHeight = pageSize.getHeight() * scaleFactor;
        final int targetHeightDimension = (targetHeight == 0.0f) ? ((int)scaledHeight) : ((int)targetHeight);
        final Dimension targetDimension = new Dimension((int)targetWidth, targetHeightDimension);
        final BufferedImage image = renderer.renderImageWithDPI(index, dpi, ImageType.RGB);
        final Image scaledImage = image.getScaledInstance(targetDimension.width, targetDimension.height, 4);
        final BufferedImage scaledBufferedImage = new BufferedImage(targetDimension.width, targetDimension.height, 1);
        final Graphics2D graphics = scaledBufferedImage.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();
        ImageIO.write(scaledBufferedImage, imageFormat, output);
    }
    
    @Nullable
    public final Object syncBookProgressFromWebdav(@NotNull final Object progressFilePath, @NotNull final String userNameSpace, @NotNull final Continuation<? super Unit> $completion) {
        File progressFile = null;
        if (progressFilePath instanceof File) {
            progressFile = (File)progressFilePath;
        }
        else if (progressFilePath instanceof String) {
            progressFile = new File((String)progressFilePath);
        }
        if (progressFile == null) {
            return Unit.INSTANCE;
        }
        final Ref$ObjectRef ref$ObjectRef;
        final Ref$ObjectRef book = ref$ObjectRef = new Ref$ObjectRef();
        final JsonObject jsonObject = ExtKt.asJsonObject(FilesKt.readText$default(progressFile, (Charset)null, 1, (Object)null));
        ref$ObjectRef.element = ((jsonObject == null) ? null : jsonObject.mapTo((Class)Book.class));
        if (book.element == null) {
            return Unit.INSTANCE;
        }
        final Object editShelfBook = this.editShelfBook((Book)book.element, userNameSpace, (Function1<? super Book, Book>)new BookController$syncBookProgressFromWebdav.BookController$syncBookProgressFromWebdav$2(book), (Continuation<? super Book>)$completion);
        if (editShelfBook == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            return editShelfBook;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object saveBookProgressToWebdav(@NotNull final Book book, @NotNull final BookChapter bookChapter, @NotNull final String userNameSpace, @NotNull final Continuation<? super Unit> $completion) {
        final String userHome = this.getUserWebdavHome(userNameSpace);
        File bookProgressDir = new File(userHome + (Object)File.separator + "bookProgress");
        if (!bookProgressDir.exists()) {
            bookProgressDir = new File(userHome + (Object)File.separator + "legado" + (Object)File.separator + "bookProgress");
            if (!bookProgressDir.exists()) {
                return Unit.INSTANCE;
            }
        }
        final File progressFile = new File(bookProgressDir.toString() + (Object)File.separator + book.getName() + '_' + book.getAuthor() + ".json");
        FilesKt.writeText$default(progressFile, ExtKt.jsonEncode(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"name", (Object)book.getName()), TuplesKt.to((Object)"author", (Object)book.getAuthor()), TuplesKt.to((Object)"durChapterIndex", (Object)Boxing.boxInt(bookChapter.getIndex())), TuplesKt.to((Object)"durChapterPos", (Object)Boxing.boxInt(0)), TuplesKt.to((Object)"durChapterTime", (Object)Boxing.boxLong(System.currentTimeMillis())), TuplesKt.to((Object)"durChapterTitle", (Object)bookChapter.getTitle()) }), true), (Charset)null, 2, (Object)null);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object syncFromWebdav(@NotNull final String zipFilePath, @NotNull String userNameSpace, @NotNull final Continuation<? super Boolean> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$syncFromWebdav.BookController$syncFromWebdav$1) {
                final BookController$syncFromWebdav.BookController$syncFromWebdav$1 bookController$syncFromWebdav$1 = (BookController$syncFromWebdav.BookController$syncFromWebdav$1)$completion;
                if ((bookController$syncFromWebdav$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$syncFromWebdav.BookController$syncFromWebdav$1 bookController$syncFromWebdav$2 = bookController$syncFromWebdav$1;
                    bookController$syncFromWebdav$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$syncFromWebdav.BookController$syncFromWebdav$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        while (true) {
            switch (((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    Object descDir = null;
                    descDir = ExtKt.getWorkDir("storage", "data", userNameSpace, "tmp");
                    final File descDirFile = new File((String)descDir);
                    try {
                        final String userHome = this.getUserWebdavHome(userNameSpace);
                        final File zipFile = new File(zipFilePath);
                        if (!zipFile.exists()) {
                            return Boxing.boxBoolean(false);
                        }
                        ExtKt.deleteRecursively(descDirFile);
                        ZipUtils.INSTANCE.unzipFile(zipFile, descDirFile);
                        final String[] backupFileNames = this.getBackupFileNames();
                        final ArrayList syncDataFileList = CollectionsKt.arrayListOf((Object[])Arrays.copyOf(backupFileNames, backupFileNames.length));
                        final Iterable $this$forEach$iv = syncDataFileList;
                        final int $i$f$forEach = 0;
                        for (final Object element$iv : $this$forEach$iv) {
                            final String it = (String)element$iv;
                            final int n = 0;
                            final File backupFile = new File((String)descDir + (Object)File.separator + it);
                            if (backupFile.exists()) {
                                final File userDataFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, it));
                                ExtKt.deleteRecursively(userDataFile);
                                FilesKt.copyRecursively$default(backupFile, userDataFile, false, (Function2)null, 6, (Object)null);
                            }
                        }
                        final File backupBooksDir = new File((String)descDir + (Object)File.separator + "books");
                        if (backupBooksDir.exists()) {
                            final File webdavBooksDir = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"));
                            ExtKt.deleteRecursively(webdavBooksDir);
                            FilesKt.copyRecursively$default(backupBooksDir, webdavBooksDir, false, (Function2)null, 6, (Object)null);
                        }
                        File bookProgressDir = new File(userHome + (Object)File.separator + "bookProgress");
                        if (!bookProgressDir.exists()) {
                            bookProgressDir = new File(userHome + (Object)File.separator + "legado" + (Object)File.separator + "bookProgress");
                        }
                        while (true) {
                            if (bookProgressDir.exists() && bookProgressDir.isDirectory()) {
                                final File[] listFiles = bookProgressDir.listFiles();
                                Intrinsics.checkNotNullExpressionValue((Object)listFiles, "bookProgressDir.listFiles()");
                                final Object[] $this$forEach$iv2 = listFiles;
                                final int $i$f$forEach2 = 0;
                                final Object[] l$3 = $this$forEach$iv2;
                                for (int i$0 = l$3.length, i = 0; i < i$0; ++i) {
                                    final Object element$iv2 = l$3[i];
                                    final File it2 = (File)element$iv2;
                                    final int n2 = 0;
                                    Intrinsics.checkNotNullExpressionValue((Object)it2, "it");
                                    final File progressFilePath = it2;
                                    final Continuation $completion2 = $continuation;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$0 = this;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$1 = userNameSpace;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$2 = descDirFile;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$3 = l$3;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).I$0 = i$0;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).I$1 = i;
                                    ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).label = 1;
                                    if (this.syncBookProgressFromWebdav(progressFilePath, userNameSpace, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                }
                            }
                            return Boxing.boxBoolean(true);
                            final int $i$f$forEach2 = 0;
                            final int n2 = 0;
                            int i = ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).I$1;
                            final int i$0 = ((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).I$0;
                            final Object[] l$3 = (Object[])((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$3;
                            final File $this$deleteRecursively = (File)((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$2;
                            userNameSpace = (String)((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$1;
                            this = (BookController)((BookController$syncFromWebdav.BookController$syncFromWebdav$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            continue;
                        }
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        final File $this$deleteRecursively;
                        ExtKt.deleteRecursively($this$deleteRecursively);
                    }
                    return Boxing.boxBoolean(false);
                }
                case 1: {
                    continue;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            break;
        }
    }
    
    @Nullable
    public final Object saveToWebdav(@NotNull String userNameSpace, @Nullable final String latestZipFilePath, @NotNull final Continuation<? super Boolean> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$saveToWebdav.BookController$saveToWebdav$1) {
                final BookController$saveToWebdav.BookController$saveToWebdav$1 bookController$saveToWebdav$1 = (BookController$saveToWebdav.BookController$saveToWebdav$1)$completion;
                if ((bookController$saveToWebdav$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$saveToWebdav.BookController$saveToWebdav$1 bookController$saveToWebdav$2 = bookController$saveToWebdav$1;
                    bookController$saveToWebdav$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$saveToWebdav.BookController$saveToWebdav$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        String legadoHome = null;
        String userHome = null;
        String s = null;
        Label_0212: {
            Object lastBackFileFromWebdav = null;
            switch (((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    userHome = (legadoHome = this.getUserWebdavHome(userNameSpace));
                    if (latestZipFilePath != null) {
                        s = latestZipFilePath;
                        break Label_0212;
                    }
                    final BookController bookController = this;
                    final String userNameSpace2 = userNameSpace;
                    final Continuation $completion2 = $continuation;
                    ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$0 = this;
                    ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$1 = userNameSpace;
                    ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$2 = userHome;
                    ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$3 = legadoHome;
                    ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).label = 1;
                    if ((lastBackFileFromWebdav = bookController.getLastBackFileFromWebdav(userNameSpace2, (Continuation<? super String>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    legadoHome = (String)((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$3;
                    userHome = (String)((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$2;
                    userNameSpace = (String)((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$1;
                    this = (BookController)((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    lastBackFileFromWebdav = $result;
                    break;
                }
                case 2: {
                    ResultKt.throwOnFailure($result);
                    final Object userBackup = $result;
                    return Boxing.boxBoolean(userBackup != null);
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            s = (String)lastBackFileFromWebdav;
        }
        final String _latestZipFilePath = s;
        if (_latestZipFilePath == null) {
            legadoHome = userHome + (Object)File.separator + "legado";
        }
        else if (StringsKt.indexOf$default((CharSequence)_latestZipFilePath, "legado", 0, false, 6, (Object)null) > 0) {
            legadoHome = userHome + (Object)File.separator + "legado";
        }
        final BookController bookController2 = this;
        final String userNameSpace3 = userNameSpace;
        final String backupDir = legadoHome;
        final String latestZipFilePath2 = _latestZipFilePath;
        final Continuation $completion3 = $continuation;
        ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$0 = null;
        ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$1 = null;
        ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$2 = null;
        ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).L$3 = null;
        ((BookController$saveToWebdav.BookController$saveToWebdav$1)$continuation).label = 2;
        Object userBackup;
        if ((userBackup = bookController2.createUserBackup(userNameSpace3, backupDir, latestZipFilePath2, (Continuation<? super File>)$completion3)) == coroutine_SUSPENDED) {
            return coroutine_SUSPENDED;
        }
        return Boxing.boxBoolean(userBackup != null);
    }
    
    @Nullable
    public final Object createUserBackup(@NotNull final String userNameSpace, @NotNull final String backupDir, @Nullable final String latestZipFilePath, @NotNull final Continuation<? super File> $completion) {
        final String today = new SimpleDateFormat("yyyy-MM-dd").format(Boxing.boxLong(System.currentTimeMillis()));
        Object descDir = null;
        descDir = ExtKt.getWorkDir("storage", "data", userNameSpace, Intrinsics.stringPlus("backup", (Object)today));
        final File descDirFile = new File((String)descDir);
        ExtKt.deleteRecursively(descDirFile);
        try {
            if (latestZipFilePath != null && !ExtKt.unzip(new File(latestZipFilePath), (String)descDir)) {
                return null;
            }
            final String[] backupFileNames = this.getBackupFileNames();
            final ArrayList syncDataFileList = CollectionsKt.arrayListOf((Object[])Arrays.copyOf(backupFileNames, backupFileNames.length));
            final Iterable $this$forEach$iv = syncDataFileList;
            final int $i$f$forEach = 0;
            for (final Object element$iv : $this$forEach$iv) {
                final String it = (String)element$iv;
                final int n = 0;
                final File userDataFile = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, it));
                if (userDataFile.exists()) {
                    final File backupFile = new File((String)descDir + (Object)File.separator + it);
                    ExtKt.deleteRecursively(backupFile);
                    FilesKt.copyRecursively$default(userDataFile, backupFile, false, (Function2)null, 6, (Object)null);
                }
            }
            final File webdavBooksDir = new File(ExtKt.getWorkDir("storage", "data", userNameSpace, "webdav", "books"));
            if (webdavBooksDir.exists()) {
                final File backupBooksDir = new File((String)descDir + (Object)File.separator + "books");
                ExtKt.deleteRecursively(backupBooksDir);
                FilesKt.copyRecursively$default(webdavBooksDir, backupBooksDir, false, (Function2)null, 6, (Object)null);
            }
            final File backupFile2 = FileUtils.INSTANCE.createFileWithReplace(backupDir + (Object)File.separator + "backup" + (Object)today + ".zip");
            final ZipUtils instance = ZipUtils.INSTANCE;
            final File[] listFiles = descDirFile.listFiles();
            Intrinsics.checkNotNullExpressionValue((Object)listFiles, "descDirFile.listFiles()");
            final File[] original = listFiles;
            return instance.zipFiles(CollectionsKt.arrayListOf((Object[])Arrays.copyOf(original, original.length)), backupFile2, null) ? backupFile2 : null;
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        finally {
            ExtKt.deleteRecursively(descDirFile);
        }
        return null;
    }
    
    @Nullable
    public final Object getLastBackFileFromWebdav(@NotNull final String userNameSpace, @NotNull final Continuation<? super String> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: aload_1         /* userNameSpace */
        //     2: invokevirtual   com/htmake/reader/api/controller/BookController.getUserWebdavHome:(Ljava/lang/Object;)Ljava/lang/String;
        //     5: astore_3        /* userHome */
        //     6: new             Ljava/io/File;
        //     9: dup            
        //    10: new             Ljava/lang/StringBuilder;
        //    13: dup            
        //    14: invokespecial   java/lang/StringBuilder.<init>:()V
        //    17: aload_3         /* userHome */
        //    18: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    21: getstatic       java/io/File.separator:Ljava/lang/String;
        //    24: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    27: ldc_w           "legado"
        //    30: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    33: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    36: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    39: astore          legadoHome
        //    41: aload           legadoHome
        //    43: invokevirtual   java/io/File.exists:()Z
        //    46: ifne            59
        //    49: new             Ljava/io/File;
        //    52: dup            
        //    53: aload_3         /* userHome */
        //    54: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    57: astore          legadoHome
        //    59: aload           legadoHome
        //    61: invokevirtual   java/io/File.exists:()Z
        //    64: ifne            69
        //    67: aconst_null    
        //    68: areturn        
        //    69: aconst_null    
        //    70: astore          latestZipFile
        //    72: new             Lkotlin/text/Regex;
        //    75: dup            
        //    76: ldc_w           "^backup[0-9-]+.zip$"
        //    79: getstatic       kotlin/text/RegexOption.IGNORE_CASE:Lkotlin/text/RegexOption;
        //    82: invokespecial   kotlin/text/Regex.<init>:(Ljava/lang/String;Lkotlin/text/RegexOption;)V
        //    85: astore          zipFileReg
        //    87: aload           legadoHome
        //    89: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //    92: astore          8
        //    94: iconst_0       
        //    95: istore          9
        //    97: iconst_0       
        //    98: istore          10
        //   100: aload           8
        //   102: astore          it
        //   104: iconst_0       
        //   105: istore          $i$a$-also-BookController$getLastBackFileFromWebdav$2
        //   107: aload           it
        //   109: ldc_w           "it"
        //   112: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   115: aload           it
        //   117: astore          $this$sortByDescending$iv
        //   119: iconst_0       
        //   120: istore          $i$f$sortByDescending
        //   122: aload           $this$sortByDescending$iv
        //   124: arraylength    
        //   125: iconst_1       
        //   126: if_icmple       147
        //   129: aload           $this$sortByDescending$iv
        //   131: iconst_0       
        //   132: istore          15
        //   134: new             Lcom/htmake/reader/api/controller/BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1;
        //   137: dup            
        //   138: invokespecial   com/htmake/reader/api/controller/BookController$getLastBackFileFromWebdav$lambda-16$$inlined$sortByDescending$1.<init>:()V
        //   141: checkcast       Ljava/util/Comparator;
        //   144: invokestatic    kotlin/collections/ArraysKt.sortWith:([Ljava/lang/Object;Ljava/util/Comparator;)V
        //   147: nop            
        //   148: nop            
        //   149: aload           8
        //   151: astore          7
        //   153: aload           7
        //   155: ldc_w           "legadoHome.listFiles().also{\n            it.sortByDescending {\n                it.lastModified()\n            }\n        }"
        //   158: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   161: aload           7
        //   163: checkcast       [Ljava/lang/Object;
        //   166: astore          7
        //   168: nop            
        //   169: iconst_0       
        //   170: istore          $i$f$forEach
        //   172: aload           $this$forEach$iv
        //   174: astore          9
        //   176: aload           9
        //   178: arraylength    
        //   179: istore          10
        //   181: iconst_0       
        //   182: istore          11
        //   184: iload           11
        //   186: iload           10
        //   188: if_icmpge       253
        //   191: aload           9
        //   193: iload           11
        //   195: aaload         
        //   196: astore          element$iv
        //   198: aload           element$iv
        //   200: checkcast       Ljava/io/File;
        //   203: astore          it
        //   205: iconst_0       
        //   206: istore          $i$a$-forEach-BookController$getLastBackFileFromWebdav$3
        //   208: aload           zipFileReg
        //   210: aload           it
        //   212: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   215: astore          15
        //   217: aload           15
        //   219: ldc_w           "it.name"
        //   222: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   225: aload           15
        //   227: checkcast       Ljava/lang/CharSequence;
        //   230: invokevirtual   kotlin/text/Regex.matches:(Ljava/lang/CharSequence;)Z
        //   233: ifeq            246
        //   236: aload           it
        //   238: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   241: astore          latestZipFile
        //   243: goto            247
        //   246: nop            
        //   247: iinc            11, 1
        //   250: goto            184
        //   253: nop            
        //   254: aload           latestZipFile
        //   256: areturn        
        //    Signature:
        //  (Ljava/lang/String;Lkotlin/coroutines/Continuation<-Ljava/lang/String;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name           Flags  
        //  -------------  -----
        //  userNameSpace  
        //  $completion    
        //    StackMapTable: 00 07 FD 00 3B 07 00 60 07 00 5E 09 FF 00 4D 00 0F 07 00 02 07 00 60 07 01 11 07 00 60 07 00 5E 05 07 02 9D 00 07 01 14 01 01 07 01 14 01 07 01 14 01 00 00 FF 00 24 00 0F 07 00 02 07 00 60 07 01 11 07 00 60 07 00 5E 07 00 60 07 02 9D 07 0B 02 01 07 0B 02 01 01 00 07 01 13 01 00 00 FF 00 3D 00 10 07 00 02 07 00 60 07 01 11 07 00 60 07 00 5E 07 00 60 07 02 9D 07 0B 02 01 07 0B 02 01 01 07 01 13 07 00 5E 01 07 00 60 00 00 00 FF 00 05 00 0F 07 00 02 07 00 60 07 01 11 07 00 60 07 00 5E 07 00 60 07 02 9D 07 0B 02 01 07 0B 02 01 01 00 07 01 13 01 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    public final Object bookSourceDebugSSE(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1) {
                final BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1 bookController$bookSourceDebugSSE$1 = (BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$completion;
                if ((bookController$bookSourceDebugSSE$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1 bookController$bookSourceDebugSSE$2 = bookController$bookSourceDebugSSE$1;
                    bookController$bookSourceDebugSSE$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        final HttpServerResponse httpServerResponse;
        Label_0828: {
            ReturnData returnData = null;
            HttpServerResponse response = null;
            Object checkAuth = null;
            switch (((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$0 = this;
                    ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$1 = context;
                    ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$2 = returnData;
                    ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$3 = response;
                    ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    response = (HttpServerResponse)((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$3;
                    returnData = (ReturnData)((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$1;
                    this = (BookController)((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    httpServerResponse = (HttpServerResponse)((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0828;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final List queryParam = context.queryParam("bookSourceUrl");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"bookSourceUrl\")");
            final String s = (String)CollectionsKt.firstOrNull(queryParam);
            final String bookSourceUrl = (s == null) ? "" : s;
            final List queryParam2 = context.queryParam("keyword");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"keyword\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam2);
            final String keyword = (s2 == null) ? "" : s2;
            if (bookSourceUrl.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            if (keyword.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u8bcd"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            final String userNameSpace = this.getUserNameSpace(context);
            final String bookSourceString = this.getBookSourceStringBySourceURLOpt(bookSourceUrl, userNameSpace);
            final CharSequence charSequence = bookSourceString;
            if (charSequence == null || charSequence.length() == 0) {
                response.write("event: error\n");
                response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90"), false) + "\n\n");
                return Unit.INSTANCE;
            }
            context.request().connection().closeHandler(BookController::bookSourceDebugSSE$lambda-18);
            BookControllerKt.access$getLogger$p().info("bookSourceDebugSSE bookSource: {} keyword: {}", (Object)bookSourceString, (Object)keyword);
            final Debugger debugger = new Debugger((Function1<? super String, Unit>)new BookController$bookSourceDebugSSE$debugger.BookController$bookSourceDebugSSE$debugger$1(response));
            final WebBook webBook = new WebBook(bookSourceString, false, null, userNameSpace, 6, null);
            final Debugger debugger2 = debugger;
            final WebBook webBook2 = webBook;
            final String key = keyword;
            final Continuation $completion3 = $continuation;
            ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$0 = response;
            ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$1 = null;
            ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$2 = null;
            ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).L$3 = null;
            ((BookController$bookSourceDebugSSE.BookController$bookSourceDebugSSE$1)$continuation).label = 2;
            if (debugger2.startDebug(webBook2, key, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        httpServerResponse.write("event: end\n");
        httpServerResponse.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(TuplesKt.to((Object)"end", (Object)Boxing.boxBoolean(true))), false) + "\n\n");
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object cacheBookSSE(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$cacheBookSSE.BookController$cacheBookSSE$1) {
                final BookController$cacheBookSSE.BookController$cacheBookSSE$1 bookController$cacheBookSSE$1 = (BookController$cacheBookSSE.BookController$cacheBookSSE$1)$completion;
                if ((bookController$cacheBookSSE$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$cacheBookSSE.BookController$cacheBookSSE$1 bookController$cacheBookSSE$2 = bookController$cacheBookSSE$1;
                    bookController$cacheBookSSE$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$cacheBookSSE.BookController$cacheBookSSE$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        HttpServerResponse httpServerResponse = null;
        Ref$IntRef failedCount = null;
        Ref$IntRef successCount = null;
        final Ref$ObjectRef ref$ObjectRef2;
        Label_1798: {
            int i$1 = 0;
            int i$2 = 0;
            Book bookInfo2 = null;
            Ref$ObjectRef l$8 = null;
            Object l$9 = null;
            Ref$ObjectRef chapterList = null;
            final Ref$ObjectRef ref$ObjectRef;
            Object localChapterList$default = null;
            Label_1449: {
                Object l$7 = null;
                Ref$ObjectRef bookSource = null;
                final ReturnData returnData2;
                Object bookSourceString$default = null;
                Label_1118: {
                    ReturnData returnData = null;
                    HttpServerResponse response = null;
                    Object checkAuth = null;
                    switch (((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).label) {
                        case 0: {
                            ResultKt.throwOnFailure($result);
                            returnData = new ReturnData();
                            response = context.response().putHeader("Content-Type", "text/event-stream").putHeader("Cache-Control", "no-cache").setChunked(true);
                            final BookController bookController = this;
                            final RoutingContext context2 = context;
                            final Continuation $completion2 = $continuation;
                            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0 = this;
                            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1 = context;
                            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2 = returnData;
                            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3 = response;
                            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).label = 1;
                            if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                            break;
                        }
                        case 1: {
                            response = (HttpServerResponse)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3;
                            returnData = (ReturnData)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2;
                            context = (RoutingContext)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1;
                            this = (BookController)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            checkAuth = $result;
                            break;
                        }
                        case 2: {
                            i$1 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$1;
                            i$2 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$0;
                            l$7 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$7;
                            bookSource = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$6;
                            bookInfo2 = (Book)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$5;
                            l$8 = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$4;
                            httpServerResponse = (HttpServerResponse)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3;
                            returnData2 = (ReturnData)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2;
                            context = (RoutingContext)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1;
                            this = (BookController)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            bookSourceString$default = $result;
                            break Label_1118;
                        }
                        case 3: {
                            i$1 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$1;
                            i$2 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$0;
                            l$9 = ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$7;
                            chapterList = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$6;
                            ref$ObjectRef = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$5;
                            bookInfo2 = (Book)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$4;
                            l$8 = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3;
                            httpServerResponse = (HttpServerResponse)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2;
                            context = (RoutingContext)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1;
                            this = (BookController)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            localChapterList$default = $result;
                            break Label_1449;
                        }
                        case 4: {
                            failedCount = (Ref$IntRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3;
                            successCount = (Ref$IntRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2;
                            ref$ObjectRef2 = (Ref$ObjectRef)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1;
                            httpServerResponse = (HttpServerResponse)((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            break Label_1798;
                        }
                        default: {
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                        }
                    }
                    if (!(boolean)checkAuth) {
                        response.write("event: error\n");
                        response.end("data: " + ExtKt.jsonEncode(ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"), false) + "\n\n");
                        return Unit.INSTANCE;
                    }
                    String bookUrl;
                    int refresh;
                    int concurrentCount = 0;
                    if (context.request().method() == HttpMethod.POST) {
                        final String string = context.getBodyAsJson().getString("url");
                        final String s = (string == null) ? context.getBodyAsJson().getString("bookUrl") : string;
                        bookUrl = ((s == null) ? "" : s);
                        final Integer integer = context.getBodyAsJson().getInteger("refresh", Boxing.boxInt(0));
                        Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"refresh\", 0)");
                        refresh = integer.intValue();
                        final Integer integer2 = context.getBodyAsJson().getInteger("concurrentCount", Boxing.boxInt(24));
                        Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"concurrentCount\", 24)");
                        i$1 = integer2.intValue();
                    }
                    else {
                        final List queryParam = context.queryParam("url");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                        final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
                        bookUrl = ((s2 == null) ? "" : s2);
                        final List queryParam2 = context.queryParam("refresh");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"refresh\")");
                        final String s3 = (String)CollectionsKt.firstOrNull(queryParam2);
                        int n;
                        if (s3 == null) {
                            n = 0;
                        }
                        else {
                            final Integer boxInt = Boxing.boxInt(Integer.parseInt(s3));
                            n = ((boxInt == null) ? 0 : boxInt);
                        }
                        refresh = n;
                        final List queryParam3 = context.queryParam("concurrentCount");
                        Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"concurrentCount\")");
                        final String s4 = (String)CollectionsKt.firstOrNull(queryParam3);
                        int n2;
                        if (s4 == null) {
                            n2 = 24;
                        }
                        else {
                            final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s4));
                            n2 = ((boxInt2 == null) ? 24 : boxInt2);
                        }
                        concurrentCount = n2;
                    }
                    if (bookUrl.length() == 0) {
                        response.write("event: error\n");
                        response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5"), false) + "\n\n");
                        return Unit.INSTANCE;
                    }
                    final Ref$ObjectRef userNameSpace = new Ref$ObjectRef();
                    userNameSpace.element = this.getUserNameSpace(context);
                    final Book bookInfo = this.getShelfBookByURL(bookUrl, (String)userNameSpace.element);
                    if (bookInfo == null) {
                        response.write("event: error\n");
                        response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u8bf7\u5148\u52a0\u5165\u4e66\u67b6"), false) + "\n\n");
                        return Unit.INSTANCE;
                    }
                    if (bookInfo.isLocalBook()) {
                        response.write("event: error\n");
                        response.end("data: " + ExtKt.jsonEncode(returnData.setErrorMsg("\u672c\u5730\u4e66\u7c4d\u65e0\u9700\u7f13\u5b58"), false) + "\n\n");
                        return Unit.INSTANCE;
                    }
                    bookSource = (Ref$ObjectRef)(l$7 = new Ref$ObjectRef());
                    final BookController bookController2 = this;
                    final RoutingContext routingContext = context;
                    final String origin = bookInfo.getOrigin();
                    final boolean b = false;
                    final Continuation continuation = $continuation;
                    final int n3 = 4;
                    final Object o = null;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0 = this;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1 = context;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2 = returnData;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3 = response;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$4 = userNameSpace;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$5 = bookInfo;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$6 = bookSource;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$7 = l$7;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$0 = refresh;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$1 = concurrentCount;
                    ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).label = 2;
                    if ((bookSourceString$default = getBookSourceString$default(bookController2, routingContext, origin, b, continuation, n3, o)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                ((Ref$ObjectRef)l$7).element = bookSourceString$default;
                final CharSequence charSequence = (CharSequence)bookSource.element;
                if (charSequence == null || charSequence.length() == 0) {
                    httpServerResponse.write("event: error\n");
                    httpServerResponse.end("data: " + ExtKt.jsonEncode(returnData2.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90"), false) + "\n\n");
                    return Unit.INSTANCE;
                }
                chapterList = (Ref$ObjectRef)(l$9 = new Ref$ObjectRef());
                final BookController bookController3 = this;
                final Book book = bookInfo2;
                final String s5 = (String)bookSource.element;
                final boolean b2 = false;
                final String s6 = (String)l$8.element;
                final boolean b3 = false;
                final Mutex mutex = null;
                final Continuation continuation2 = $continuation;
                final int n4 = 48;
                final Object o2 = null;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0 = this;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1 = context;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2 = httpServerResponse;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3 = l$8;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$4 = bookInfo2;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$5 = bookSource;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$6 = chapterList;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$7 = l$9;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$0 = i$2;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).I$1 = i$1;
                ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).label = 3;
                if ((localChapterList$default = getLocalChapterList$default(bookController3, book, s5, b2, s6, b3, mutex, continuation2, n4, o2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            ((Ref$ObjectRef)l$9).element = localChapterList$default;
            final Ref$ObjectRef cachedChapterContentSet = new Ref$ObjectRef();
            cachedChapterContentSet.element = new LinkedHashSet<Object>();
            if (i$2 <= 0) {
                cachedChapterContentSet.element = this.getCachedChapterContentSet(bookInfo2, (String)l$8.element);
            }
            final File localCacheDir = this.getChapterCacheDir(bookInfo2, (String)l$8.element);
            final Ref$BooleanRef isEnd = new Ref$BooleanRef();
            successCount = new Ref$IntRef();
            failedCount = new Ref$IntRef();
            context.request().connection().closeHandler(BookController::cacheBookSSE$lambda-19);
            final int n5 = (i$1 > 0) ? i$1 : 24;
            BookControllerKt.access$getLogger$p().info("cacheBookSSE concurrentCount: {} refresh: {}", (Object)Boxing.boxInt(n5), (Object)Boxing.boxInt(i$2));
            final BookController bookController4 = this;
            final int concurrentCount2 = n5;
            final int startIndex = 0;
            final int size = ((List)chapterList.element).size();
            final Function3 handler = (Function3)new BookController$cacheBookSSE.BookController$cacheBookSSE$3(cachedChapterContentSet, chapterList, ref$ObjectRef, this, l$8, bookInfo2, localCacheDir, successCount, isEnd, failedCount, (Continuation)null);
            final Function2 needContinue = (Function2)new BookController$cacheBookSSE.BookController$cacheBookSSE$4(isEnd, cachedChapterContentSet, successCount, failedCount, httpServerResponse);
            final Continuation $completion3 = $continuation;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$0 = httpServerResponse;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$1 = cachedChapterContentSet;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$2 = successCount;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$3 = failedCount;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$4 = null;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$5 = null;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$6 = null;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).L$7 = null;
            ((BookController$cacheBookSSE.BookController$cacheBookSSE$1)$continuation).label = 4;
            if (bookController4.limitConcurrent(concurrentCount2, startIndex, size, (Function3<? super CoroutineScope, ? super Integer, ? super Continuation<Object>, ?>)handler, (Function2<? super ArrayList<Object>, ? super Integer, Boolean>)needContinue, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        httpServerResponse.write("event: end\n");
        httpServerResponse.end("data: " + ExtKt.jsonEncode(MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"cachedCount", (Object)Boxing.boxInt(((Set)ref$ObjectRef2.element).size())), TuplesKt.to((Object)"successCount", (Object)Boxing.boxInt(successCount.element)), TuplesKt.to((Object)"failedCount", (Object)Boxing.boxInt(failedCount.element)) }), false) + "\n\n");
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object cacheBookOnServer(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$cacheBookOnServer.BookController$cacheBookOnServer$1) {
                final BookController$cacheBookOnServer.BookController$cacheBookOnServer$1 bookController$cacheBookOnServer$1 = (BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$completion;
                if ((bookController$cacheBookOnServer$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$cacheBookOnServer.BookController$cacheBookOnServer$1 bookController$cacheBookOnServer$2 = bookController$cacheBookOnServer$1;
                    bookController$cacheBookOnServer$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$cacheBookOnServer.BookController$cacheBookOnServer$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$0 = this;
                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$1 = context;
                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$2 = returnData;
                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$2;
                context = (RoutingContext)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$1;
                this = (BookController)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        final JsonArray jsonArray = context.getBodyAsJson().getJsonArray("bookUrlList");
        final JsonArray bookUrlList = (jsonArray == null) ? new JsonArray() : jsonArray;
        if (bookUrlList.size() <= 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
        }
        final int $i$f$CoroutineExceptionHandler = 0;
        final CoroutineExceptionHandler exceptionHandler = (CoroutineExceptionHandler)new CoroutineExceptionHandler(CoroutineExceptionHandler.Key) {
            public void handleException(@NotNull final CoroutineContext context, @NotNull final Throwable exception) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_2         /* exception */
                //     2: astore_3       
                //     3: astore          ctx
                //     5: iconst_0       
                //     6: istore          $i$a$-CoroutineExceptionHandler-BookController$cacheBookOnServer$exceptionHandler$1
                //     8: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
                //    11: ldc             "cacheBookOnServer error: {}"
                //    13: aload_3         /* ex */
                //    14: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                //    17: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
                //    22: nop            
                //    23: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
                //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
                //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
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
        };
        final String userNameSpace = this.getUserNameSpace(context);
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()).plus((CoroutineContext)exceptionHandler), (CoroutineStart)null, (Function2)new BookController$cacheBookOnServer.BookController$cacheBookOnServer$2(this, bookUrlList, userNameSpace, (Continuation)null), 2, (Object)null);
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    @Nullable
    public final Object cacheBookOnServer(@NotNull JsonArray bookUrlList, @NotNull String userNameSpace, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$cacheBookOnServer.BookController$cacheBookOnServer$3) {
                final BookController$cacheBookOnServer.BookController$cacheBookOnServer$3 bookController$cacheBookOnServer$3 = (BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$completion;
                if ((bookController$cacheBookOnServer$3.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$cacheBookOnServer.BookController$cacheBookOnServer$3 bookController$cacheBookOnServer$4 = bookController$cacheBookOnServer$3;
                    bookController$cacheBookOnServer$4.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$cacheBookOnServer.BookController$cacheBookOnServer$3(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
    Label_0485_Outer:
        while (true) {
            int i$0 = 0;
            int i$2 = 0;
            Label_1277: {
                Book book = null;
                Label_1264: {
                    while (true) {
                        String l$4;
                        Object localChapterList$default = null;
                        int j;
                        int chapterIndex;
                        List chapterList;
                        BookChapter chapterInfo;
                        String nextChapterUrl;
                        BookChapter nextChapterInfo;
                        WebBook webBook;
                        Book book2;
                        BookChapter bookChapter;
                        String nextChapterUrl2;
                        Continuation $completion2;
                        Set cachedChapterContentSet;
                        File localCacheDir;
                        int n = 0;
                        int n2 = 0;
                        Object bookContent;
                        String content;
                        File chapterCacheFile;
                        BookHelp instance;
                        CoroutineScope scope;
                        Object fromJson-IoAF18A;
                        BookSource bookSource2;
                        BookSource bookSource3;
                        Book book3;
                        BookChapter bookChapter2;
                        String content2;
                        Continuation $completion3;
                        Set set;
                        int i$3;
                        File file;
                        List list;
                        int i;
                        String bookUrl;
                        Book bookInfo;
                        String bookSource;
                        CharSequence charSequence;
                        Book book4;
                        String s;
                        boolean b;
                        boolean b2;
                        Mutex mutex;
                        Continuation continuation;
                        int n3;
                        Object o;
                        Label_1257:Label_1102_Outer:
                        while (true) {
                            Label_0441: {
                                while (true) {
                                    switch (((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).label) {
                                        case 0: {
                                            ResultKt.throwOnFailure($result);
                                            i$0 = 0;
                                            i$2 = bookUrlList.size();
                                            if (i$0 < i$2) {
                                                break;
                                            }
                                            return Unit.INSTANCE;
                                        }
                                        case 1: {
                                            i$2 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1;
                                            i$0 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0;
                                            l$4 = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4;
                                            book = (Book)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3;
                                            userNameSpace = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2;
                                            bookUrlList = (JsonArray)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1;
                                            this = (BookController)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0;
                                            ResultKt.throwOnFailure($result);
                                            localChapterList$default = $result;
                                            break Label_0441;
                                        }
                                        case 2: {
                                            Label_0707: {
                                                break Label_0707;
                                                chapterIndex = j;
                                                chapterInfo = chapterList.get(j);
                                                try {
                                                    nextChapterUrl = null;
                                                    if (chapterIndex + 1 < chapterList.size()) {
                                                        nextChapterInfo = chapterList.get(chapterIndex + 1);
                                                        nextChapterUrl = nextChapterInfo.getUrl();
                                                    }
                                                    webBook = new WebBook(l$4, this.getAppConfig().getDebugLog(), null, userNameSpace, 4, null);
                                                    book2 = book;
                                                    bookChapter = chapterInfo;
                                                    nextChapterUrl2 = nextChapterUrl;
                                                    $completion2 = $continuation;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0 = this;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1 = bookUrlList;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2 = userNameSpace;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3 = book;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4 = l$4;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$5 = chapterList;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$6 = cachedChapterContentSet;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$7 = localCacheDir;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$8 = chapterInfo;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0 = i$0;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1 = i$2;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$2 = n;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$3 = n2;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$4 = chapterIndex;
                                                    ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).label = 2;
                                                    if ((bookContent = webBook.getBookContent(book2, bookChapter, nextChapterUrl2, (Continuation<? super String>)$completion2)) == coroutine_SUSPENDED) {
                                                        return coroutine_SUSPENDED;
                                                    }
                                                Label_1222_Outer:
                                                    while (true) {
                                                        content = (String)bookContent;
                                                        chapterCacheFile = new File(localCacheDir.getAbsolutePath() + (Object)File.separator + chapterIndex + ".txt");
                                                        FilesKt.writeText$default(chapterCacheFile, content, (Charset)null, 2, (Object)null);
                                                        instance = BookHelp.INSTANCE;
                                                        scope = (CoroutineScope)this;
                                                        fromJson-IoAF18A = BookSource.Companion.fromJson-IoAF18A(l$4);
                                                        bookSource2 = (BookSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                                                        bookSource3 = ((bookSource2 == null) ? new BookSource(null, null, null, 0, null, 0, false, false, null, null, null, null, null, null, null, null, 0L, 0L, 0, null, null, null, null, null, null, null, 67108863, null) : bookSource2);
                                                        book3 = book;
                                                        bookChapter2 = chapterInfo;
                                                        content2 = content;
                                                        $completion3 = $continuation;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0 = this;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1 = bookUrlList;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2 = userNameSpace;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3 = book;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4 = l$4;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$5 = chapterList;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$6 = cachedChapterContentSet;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$7 = localCacheDir;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$8 = null;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0 = i$0;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1 = i$2;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$2 = n;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$3 = n2;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$4 = chapterIndex;
                                                        ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).label = 3;
                                                        if (instance.saveImages(scope, bookSource3, book3, bookChapter2, content2, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                                                            return coroutine_SUSPENDED;
                                                        }
                                                        while (true) {
                                                            set.add(Boxing.boxInt(i$3));
                                                            break Label_1257;
                                                            i$3 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$4;
                                                            n2 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$3;
                                                            n = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$2;
                                                            i$2 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1;
                                                            i$0 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0;
                                                            file = (File)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$7;
                                                            set = (Set)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$6;
                                                            list = (List)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$5;
                                                            l$4 = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4;
                                                            book = (Book)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3;
                                                            userNameSpace = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2;
                                                            bookUrlList = (JsonArray)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1;
                                                            this = (BookController)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0;
                                                            ResultKt.throwOnFailure($result);
                                                            continue Label_1102_Outer;
                                                        }
                                                        chapterIndex = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$4;
                                                        n2 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$3;
                                                        n = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$2;
                                                        i$2 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1;
                                                        i$0 = ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0;
                                                        chapterInfo = (BookChapter)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$8;
                                                        localCacheDir = (File)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$7;
                                                        cachedChapterContentSet = (Set)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$6;
                                                        chapterList = (List)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$5;
                                                        l$4 = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4;
                                                        book = (Book)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3;
                                                        userNameSpace = (String)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2;
                                                        bookUrlList = (JsonArray)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1;
                                                        this = (BookController)((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0;
                                                        ResultKt.throwOnFailure($result);
                                                        bookContent = $result;
                                                        continue Label_1222_Outer;
                                                    }
                                                }
                                                catch (final Exception e) {
                                                    BookControllerKt.access$getLogger$p().info("cacheBookOnServer error: {}", (Object)e.getMessage());
                                                }
                                            }
                                            break Label_1257;
                                        }
                                        case 3: {
                                            continue Label_0485_Outer;
                                        }
                                        default: {
                                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                        }
                                    }
                                    break;
                                }
                                i = i$0;
                                ++i$0;
                                bookUrl = bookUrlList.getString(i);
                                Intrinsics.checkNotNullExpressionValue((Object)bookUrl, "bookUrl");
                                bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                                if (bookInfo == null) {
                                    BookControllerKt.access$getLogger$p().info("\u672a\u627e\u5230\u4e66\u7c4d\u4fe1\u606f: {}", (Object)bookUrl);
                                    break Label_1277;
                                }
                                if (bookInfo.isLocalBook()) {
                                    BookControllerKt.access$getLogger$p().info("\u672c\u5730\u4e66\u7c4d\u8df3\u8fc7\u7f13\u5b58: {}", (Object)bookUrl);
                                    break Label_1277;
                                }
                                BookControllerKt.access$getLogger$p().info("\u5f00\u59cb\u7f13\u5b58\u4e66\u7c4d: {}", (Object)bookInfo);
                                bookSource = this.getBookSourceStringBySourceURLOpt(bookInfo.getOrigin(), userNameSpace);
                                charSequence = bookSource;
                                if (charSequence == null || charSequence.length() == 0) {
                                    BookControllerKt.access$getLogger$p().info("\u672a\u627e\u5230\u4e66\u6e90\u4fe1\u606f: {}", (Object)bookUrl);
                                    break Label_1277;
                                }
                                book4 = bookInfo;
                                s = bookSource;
                                b = false;
                                b2 = false;
                                mutex = null;
                                continuation = $continuation;
                                n3 = 48;
                                o = null;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$0 = this;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$1 = bookUrlList;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$2 = userNameSpace;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$3 = bookInfo;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$4 = bookSource;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$5 = null;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$6 = null;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$7 = null;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).L$8 = null;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$0 = i$0;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).I$1 = i$2;
                                ((BookController$cacheBookOnServer.BookController$cacheBookOnServer$3)$continuation).label = 1;
                                if ((localChapterList$default = getLocalChapterList$default(this, book4, s, b, userNameSpace, b2, mutex, continuation, n3, o)) == coroutine_SUSPENDED) {
                                    return coroutine_SUSPENDED;
                                }
                            }
                            chapterList = (List)localChapterList$default;
                            cachedChapterContentSet = this.getCachedChapterContentSet(book, userNameSpace);
                            localCacheDir = this.getChapterCacheDir(book, userNameSpace);
                            n = 0;
                            n2 = chapterList.size() - 1;
                            if (n > n2) {
                                break Label_1264;
                            }
                            j = n;
                            ++n;
                            if (!cachedChapterContentSet.contains(Boxing.boxInt(j))) {
                                continue Label_0485_Outer;
                            }
                            break;
                        }
                        if (n <= n2) {
                            continue;
                        }
                        break;
                    }
                }
                BookControllerKt.access$getLogger$p().info("\u7f13\u5b58\u4e66\u7c4d\u5b8c\u6210: {}", (Object)book);
            }
            if (i$0 < i$2) {
                continue;
            }
            break;
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object deleteBookCache(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$deleteBookCache.BookController$deleteBookCache$1) {
                final BookController$deleteBookCache.BookController$deleteBookCache$1 bookController$deleteBookCache$1 = (BookController$deleteBookCache.BookController$deleteBookCache$1)$completion;
                if ((bookController$deleteBookCache$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$deleteBookCache.BookController$deleteBookCache$1 bookController$deleteBookCache$2 = bookController$deleteBookCache$1;
                    bookController$deleteBookCache$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$deleteBookCache.BookController$deleteBookCache$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$0 = this;
                ((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$1 = context;
                ((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$2 = returnData;
                ((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$2;
                context = (RoutingContext)((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$1;
                this = (BookController)((BookController$deleteBookCache.BookController$deleteBookCache$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        String bookUrl = null;
        if (context.request().method() == HttpMethod.POST) {
            final String string = context.getBodyAsJson().getString("url");
            final String s = (string == null) ? context.getBodyAsJson().getString("bookUrl") : string;
            final String s2 = (s == null) ? "" : s;
        }
        else {
            final List queryParam = context.queryParam("url");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
            final String s3 = (String)CollectionsKt.firstOrNull(queryParam);
            bookUrl = ((s3 == null) ? "" : s3);
        }
        if (bookUrl.length() == 0) {
            return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
        }
        final String userNameSpace = this.getUserNameSpace(context);
        final Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
        if (bookInfo == null) {
            return returnData.setErrorMsg("\u8bf7\u5148\u52a0\u5165\u4e66\u67b6");
        }
        if (bookInfo.isLocalBook()) {
            return returnData.setErrorMsg("\u672c\u5730\u4e66\u7c4d\u65e0\u9700\u5220\u9664\u7f13\u5b58");
        }
        final File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
        ExtKt.deleteRecursively(localCacheDir);
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    @Nullable
    public final Object textToSpeech(@NotNull RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$textToSpeech.BookController$textToSpeech$1) {
                final BookController$textToSpeech.BookController$textToSpeech$1 bookController$textToSpeech$1 = (BookController$textToSpeech.BookController$textToSpeech$1)$completion;
                if ((bookController$textToSpeech$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$textToSpeech.BookController$textToSpeech$1 bookController$textToSpeech$2 = bookController$textToSpeech$1;
                    bookController$textToSpeech$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$textToSpeech.BookController$textToSpeech$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        HttpServerResponse response = null;
        Object checkAuth = null;
        switch (((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                response = context.response();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$0 = this;
                ((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$1 = context;
                ((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$2 = response;
                ((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                response = (HttpServerResponse)((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$2;
                context = (RoutingContext)((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$1;
                this = (BookController)((BookController$textToSpeech.BookController$textToSpeech$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            response.setStatusCode(403).end("\u672a\u767b\u5f55");
            return Unit.INSTANCE;
        }
        final Ref$ObjectRef text = new Ref$ObjectRef();
        final Ref$ObjectRef type = new Ref$ObjectRef();
        String voice;
        String pitch;
        String rate;
        String base64 = null;
        if (context.request().method() == HttpMethod.POST) {
            final Ref$ObjectRef ref$ObjectRef = text;
            final String string = context.getBodyAsJson().getString("text");
            ref$ObjectRef.element = ((string == null) ? "" : string);
            final Ref$ObjectRef ref$ObjectRef2 = type;
            final String string2 = context.getBodyAsJson().getString("type");
            ref$ObjectRef2.element = ((string2 == null) ? "" : string2);
            final String string3 = context.getBodyAsJson().getString("voice");
            voice = ((string3 == null) ? "" : string3);
            final String string4 = context.getBodyAsJson().getString("pitch");
            pitch = ((string4 == null) ? "" : string4);
            final String string5 = context.getBodyAsJson().getString("rate");
            rate = ((string5 == null) ? "" : string5);
            final String string6 = context.getBodyAsJson().getString("base64");
            final String s = (string6 == null) ? "" : string6;
        }
        else {
            final Ref$ObjectRef ref$ObjectRef3 = text;
            final List queryParam = context.queryParam("text");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"text\")");
            final String s2 = (String)CollectionsKt.firstOrNull(queryParam);
            ref$ObjectRef3.element = ((s2 == null) ? "" : s2);
            final Ref$ObjectRef ref$ObjectRef4 = type;
            final List queryParam2 = context.queryParam("type");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"type\")");
            final String s3 = (String)CollectionsKt.firstOrNull(queryParam2);
            ref$ObjectRef4.element = ((s3 == null) ? "" : s3);
            final List queryParam3 = context.queryParam("voice");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"voice\")");
            final String s4 = (String)CollectionsKt.firstOrNull(queryParam3);
            voice = ((s4 == null) ? "" : s4);
            final List queryParam4 = context.queryParam("pitch");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"pitch\")");
            final String s5 = (String)CollectionsKt.firstOrNull(queryParam4);
            pitch = ((s5 == null) ? "" : s5);
            final List queryParam5 = context.queryParam("rate");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam5, "context.queryParam(\"rate\")");
            final String s6 = (String)CollectionsKt.firstOrNull(queryParam5);
            rate = ((s6 == null) ? "" : s6);
            final List queryParam6 = context.queryParam("base64");
            Intrinsics.checkNotNullExpressionValue((Object)queryParam6, "context.queryParam(\"base64\")");
            final String s7 = (String)CollectionsKt.firstOrNull(queryParam6);
            base64 = ((s7 == null) ? "" : s7);
        }
        final CharSequence charSequence = (CharSequence)type.element;
        if (charSequence == null || charSequence.length() == 0) {
            type.element = "edge";
        }
        final CharSequence charSequence2 = (CharSequence)text.element;
        if (charSequence2 == null || charSequence2.length() == 0) {
            response.setStatusCode(404).end("\u53c2\u6570\u9519\u8bef");
            return Unit.INSTANCE;
        }
        final int $i$f$CoroutineExceptionHandler = 0;
        final CoroutineExceptionHandler exceptionHandler = (CoroutineExceptionHandler)new CoroutineExceptionHandler(CoroutineExceptionHandler.Key, response) {
            public void handleException(@NotNull final CoroutineContext context, @NotNull final Throwable exception) {
                // 
                // This method could not be decompiled.
                // 
                // Original Bytecode:
                // 
                //     1: aload_2         /* exception */
                //     2: astore_3       
                //     3: astore          ctx
                //     5: iconst_0       
                //     6: istore          $i$a$-CoroutineExceptionHandler-BookController$textToSpeech$exceptionHandler$1
                //     8: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
                //    11: ldc             "tts error: {}"
                //    13: aload_3         /* ex */
                //    14: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
                //    17: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
                //    22: aload_0         /* this */
                //    23: getfield        com/htmake/reader/api/controller/BookController$textToSpeech$$inlined$CoroutineExceptionHandler$1.$response$inlined:Lio/vertx/core/http/HttpServerResponse;
                //    26: sipush          404
                //    29: invokeinterface io/vertx/core/http/HttpServerResponse.setStatusCode:(I)Lio/vertx/core/http/HttpServerResponse;
                //    34: invokeinterface io/vertx/core/http/HttpServerResponse.end:()V
                //    39: nop            
                //    40: return         
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
                //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
                //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
                //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
                //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1151)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:993)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:548)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:534)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:377)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:318)
                //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:213)
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
        };
        final Map options = MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"voice", (Object)voice), TuplesKt.to((Object)"pitch", (Object)pitch), TuplesKt.to((Object)"rate", (Object)rate), TuplesKt.to((Object)"base64", (Object)base64) });
        BuildersKt.launch$default((CoroutineScope)this, new MDCContext((Map)null, 1, (DefaultConstructorMarker)null).plus((CoroutineContext)Dispatchers.getIO()).plus((CoroutineContext)exceptionHandler), (CoroutineStart)null, (Function2)new BookController$textToSpeech.BookController$textToSpeech$2(type, this, response, text, options, context, (Continuation)null), 2, (Object)null);
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object ttsByEdge(@NotNull final HttpServerResponse response, @NotNull final String text, @Nullable final Map<String, String> options, @NotNull final Continuation<? super Unit> $completion) {
        VoiceEnum voice = VoiceEnum.zh_CN_XiaoxiaoNeural;
        String rate = "0";
        String pitch = "0%";
        if (options != null) {
            if (options.containsKey("voice")) {
                final VoiceEnum fromSortName = VoiceEnum.fromSortName(options.get("voice"));
                voice = ((fromSortName == null) ? VoiceEnum.zh_CN_XiaoxiaoNeural : fromSortName);
            }
            if (options.containsKey("rate")) {
                final String s = options.get("rate");
                rate = ((s == null) ? "0" : s);
            }
            if (options.containsKey("pitch")) {
                pitch = Intrinsics.stringPlus((String)options.get("pitch"), (Object)"%");
            }
        }
        final TTSService ts = TTSService.builder().build();
        final SSML ssml = SSML.builder().synthesisText(text).voice(voice).rate(rate).pitch(pitch).style(TtsStyleEnum.chat).build();
        final byte[] mp3byte = ts.sendText(ssml);
        if (options != null && "1".equals(options.get("base64"))) {
            final ReturnData returnData = new ReturnData();
            final HttpServerResponse putHeader = response.putHeader("content-type", "application/json; charset=utf-8");
            final ReturnData returnData2 = returnData;
            final String encodeToString = Base64.getEncoder().encodeToString(mp3byte);
            Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "getEncoder().encodeToString(mp3byte)");
            putHeader.end(ExtKt.jsonEncode$default(ReturnData.setData$default(returnData2, encodeToString, null, 2, null), false, 2, null));
        }
        else {
            response.putHeader("Content-Type", "audio/mpeg").end(Buffer.buffer(mp3byte));
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final HttpTTS getHttpTTSByName(@NotNull final String name, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        if (name.length() == 0) {
            return null;
        }
        final JsonArray list = ExtKt.asJsonArray(this.getUserStorage(userNameSpace, "httpTTS"));
        if (list == null) {
            return null;
        }
        int j = 0;
        final int size = list.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final HttpTTS.Companion companion = HttpTTS.Companion;
                final String string = list.getJsonObject(i).toString();
                Intrinsics.checkNotNullExpressionValue((Object)string, "list.getJsonObject(i).toString()");
                final Object fromJson-IoAF18A = companion.fromJson-IoAF18A(string);
                final HttpTTS httpTTS = (HttpTTS)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A);
                if (httpTTS != null && httpTTS.getName().equals(name)) {
                    return httpTTS;
                }
            } while (j < size);
        }
        return null;
    }
    
    @Nullable
    public final Object ttsByApi(@NotNull HttpServerResponse response, @NotNull final String text, @NotNull final String userNameSpace, @Nullable Map<String, String> var_4_169, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$ttsByApi.BookController$ttsByApi$1) {
                final BookController$ttsByApi.BookController$ttsByApi$1 bookController$ttsByApi$1 = (BookController$ttsByApi.BookController$ttsByApi$1)$completion;
                if ((bookController$ttsByApi$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$ttsByApi.BookController$ttsByApi$1 bookController$ttsByApi$2 = bookController$ttsByApi$1;
                    bookController$ttsByApi$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$ttsByApi.BookController$ttsByApi$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object speakStream = null;
        final HttpTTS httpTTS2;
        switch (((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final String voice = (options == null) ? null : options.get("voice");
                final CharSequence charSequence = voice;
                if (charSequence == null || charSequence.length() == 0) {
                    response.setStatusCode(404).end();
                    return Unit.INSTANCE;
                }
                final HttpTTS httpTTS = this.getHttpTTSByName(voice, userNameSpace);
                if (httpTTS == null) {
                    response.setStatusCode(404).end();
                    return Unit.INSTANCE;
                }
                double n;
                if (options == null) {
                    n = 1.0;
                }
                else {
                    final String s = options.get("rate");
                    if (s == null) {
                        n = 1.0;
                    }
                    else {
                        final Double boxDouble = Boxing.boxDouble(Double.parseDouble(s));
                        n = ((boxDouble == null) ? 1.0 : boxDouble);
                    }
                }
                double speechRate = n;
                speechRate = 5 + (speechRate - 0.5) * 30;
                final HttpTTS httpTts = httpTTS;
                final int speechRate2 = (int)speechRate;
                final Continuation $completion2 = $continuation;
                ((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$0 = response;
                ((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$1 = options;
                ((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$2 = httpTTS;
                ((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).label = 1;
                if ((speakStream = this.getSpeakStream(httpTts, text, speechRate2, (Continuation<? super InputStream>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                httpTTS2 = (HttpTTS)((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$2;
                var_4_169 = (Map)((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$1;
                response = (HttpServerResponse)((BookController$ttsByApi.BookController$ttsByApi$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                speakStream = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final InputStream stream = (InputStream)speakStream;
        if (stream != null) {
            if (var_4_169 != null && "1".equals(var_4_169.get("base64"))) {
                final ReturnData returnData = new ReturnData();
                final HttpServerResponse putHeader = response.putHeader("content-type", "application/json; charset=utf-8");
                final ReturnData returnData2 = returnData;
                final String encodeToString = Base64.getEncoder().encodeToString(ByteStreamsKt.readBytes(stream));
                Intrinsics.checkNotNullExpressionValue((Object)encodeToString, "getEncoder().encodeToString(stream.readBytes())");
                putHeader.end(ExtKt.jsonEncode$default(ReturnData.setData$default(returnData2, encodeToString, null, 2, null), false, 2, null));
            }
            else {
                final HttpServerResponse httpServerResponse = response;
                final String s2 = "Content-Type";
                final String contentType = httpTTS2.getContentType();
                httpServerResponse.putHeader(s2, (contentType == null) ? "audio/mpeg" : contentType).end(Buffer.buffer(ByteStreamsKt.readBytes(stream)));
            }
        }
        else {
            response.setStatusCode(404).end();
        }
        return Unit.INSTANCE;
    }
    
    @Nullable
    public final Object getSpeakStream(@NotNull HttpTTS httpTts, @NotNull String speakText, int speechRate, @NotNull final Continuation<? super InputStream> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$getSpeakStream.BookController$getSpeakStream$1) {
                final BookController$getSpeakStream.BookController$getSpeakStream$1 bookController$getSpeakStream$1 = (BookController$getSpeakStream.BookController$getSpeakStream$1)$completion;
                if ((bookController$getSpeakStream$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getSpeakStream.BookController$getSpeakStream$1 bookController$getSpeakStream$2 = bookController$getSpeakStream$1;
                    bookController$getSpeakStream$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$getSpeakStream.BookController$getSpeakStream$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Label_0919: {
            switch (((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final Ref$IntRef downloadErrorNo = new Ref$IntRef();
                }
                case 1: {
                    Label_0240: {
                        break Label_0240;
                        while (true) {
                            try {
                                AnalyzeUrl analyzeUrl = new AnalyzeUrl(httpTts.getUrl(), null, null, speakText, Boxing.boxInt(speechRate), null, httpTts, null, null, httpTts.getHeaderMap(true), Debug.INSTANCE, 422, null);
                                Object l$6;
                                Ref$ObjectRef response = (Ref$ObjectRef)(l$6 = new Ref$ObjectRef());
                                final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                                final Continuation $completion2 = $continuation;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$0 = this;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$1 = httpTts;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$2 = speakText;
                                Ref$IntRef downloadErrorNo = null;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$3 = downloadErrorNo;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$4 = analyzeUrl;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$5 = response;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$6 = l$6;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).I$0 = speechRate;
                                ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).label = 1;
                                Object responseAwait;
                                if ((responseAwait = analyzeUrl2.getResponseAwait((Continuation<? super Response>)$completion2)) == coroutine_SUSPENDED) {
                                    return coroutine_SUSPENDED;
                                }
                                while (true) {
                                    ((Ref$ObjectRef)l$6).element = responseAwait;
                                    JobKt.ensureActive(this.getCoroutineContext());
                                    final String loginCheckJs;
                                    final String checkJs = loginCheckJs = httpTts.getLoginCheckJs();
                                    if (loginCheckJs != null && !StringsKt.isBlank((CharSequence)loginCheckJs)) {
                                        final Ref$ObjectRef ref$ObjectRef = response;
                                        final Object evalJS = analyzeUrl.evalJS(checkJs, response.element);
                                        if (evalJS == null) {
                                            throw new NullPointerException("null cannot be cast to non-null type okhttp3.Response");
                                        }
                                        ref$ObjectRef.element = evalJS;
                                    }
                                    final String value = ((Response)response.element).headers().get("Content-Type");
                                    if (value != null) {
                                        final String contentType = value;
                                        final int n = 0;
                                        final String ct = httpTts.getContentType();
                                        if (Intrinsics.areEqual((Object)contentType, (Object)"application/json")) {
                                            final ResponseBody body = ((Response)response.element).body();
                                            Intrinsics.checkNotNull((Object)body);
                                            throw new NoStackTraceException(body.string());
                                        }
                                        final String s = ct;
                                        if (s != null && !StringsKt.isBlank((CharSequence)s) && !new Regex(ct).matches((CharSequence)contentType)) {
                                            final String s2 = "TTS\u670d\u52a1\u5668\u8fd4\u56de\u9519\u8bef\uff1a";
                                            final ResponseBody body2 = ((Response)response.element).body();
                                            Intrinsics.checkNotNull((Object)body2);
                                            throw new NoStackTraceException(Intrinsics.stringPlus(s2, (Object)body2.string()));
                                        }
                                    }
                                    JobKt.ensureActive(this.getCoroutineContext());
                                    final ResponseBody body3 = ((Response)response.element).body();
                                    Intrinsics.checkNotNull((Object)body3);
                                    final InputStream stream = body3.byteStream();
                                    final int n2 = 0;
                                    downloadErrorNo.element = 0;
                                    return stream;
                                    speechRate = ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).I$0;
                                    l$6 = ((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$6;
                                    response = (Ref$ObjectRef)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$5;
                                    analyzeUrl = (AnalyzeUrl)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$4;
                                    downloadErrorNo = (Ref$IntRef)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$3;
                                    speakText = (String)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$2;
                                    httpTts = (HttpTTS)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$1;
                                    this = (BookController)((BookController$getSpeakStream.BookController$getSpeakStream$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    responseAwait = $result;
                                    continue;
                                }
                            }
                            catch (final Exception e) {
                                final Exception ex = e;
                                if (ex instanceof CancellationException) {
                                    throw e;
                                }
                                if (ex instanceof ScriptException || ex instanceof WrappedException) {
                                    BookControllerKt.access$getLogger$p().error(Intrinsics.stringPlus("js\u9519\u8bef\n", (Object)e.getLocalizedMessage()), (Throwable)e);
                                    throw e;
                                }
                                if (ex instanceof SocketTimeoutException || ex instanceof ConnectException) {
                                    final Ref$IntRef downloadErrorNo;
                                    ++downloadErrorNo.element;
                                    if (downloadErrorNo.element > 5) {
                                        final String msg = Intrinsics.stringPlus("tts\u8d85\u65f6\u6216\u8fde\u63a5\u9519\u8bef\u8d85\u8fc75\u6b21\n", (Object)e.getLocalizedMessage());
                                        BookControllerKt.access$getLogger$p().error(msg, (Throwable)e);
                                        throw e;
                                    }
                                    continue;
                                }
                                else {
                                    final Ref$IntRef downloadErrorNo;
                                    ++downloadErrorNo.element;
                                    BookControllerKt.access$getLogger$p().error(Intrinsics.stringPlus("tts\u4e0b\u8f7d\u9519\u8bef\n", (Object)e.getLocalizedMessage()), (Throwable)e);
                                    if (downloadErrorNo.element > 5) {
                                        final String msg2 = "TTS\u670d\u52a1\u5668\u8fde\u7eed5\u6b21\u9519\u8bef\uff0c\u5df2\u6682\u505c\u9605\u8bfb\u3002";
                                        BookControllerKt.access$getLogger$p().error(msg2, (Throwable)e);
                                        throw e;
                                    }
                                    BookControllerKt.access$getLogger$p().error(Intrinsics.stringPlus("TTS\u4e0b\u8f7d\u97f3\u9891\u51fa\u9519\uff0c\u4f7f\u7528\u65e0\u58f0\u97f3\u9891\u4ee3\u66ff\u3002\n\u6717\u8bfb\u6587\u672c\uff1a", (Object)speakText));
                                    return null;
                                }
                            }
                            break Label_0919;
                        }
                    }
                    break;
                }
            }
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
    
    @Nullable
    public final Object ttsByTextToSpeechCn(@NotNull HttpServerResponse response, @NotNull final String text, @Nullable final Map<String, String> options, @NotNull final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1) {
                final BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1 bookController$ttsByTextToSpeechCn$1 = (BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$completion;
                if ((bookController$ttsByTextToSpeechCn$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1 bookController$ttsByTextToSpeechCn$2 = bookController$ttsByTextToSpeechCn$1;
                    bookController$ttsByTextToSpeechCn$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        Object awaitResult = null;
        switch (((BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final Map map = MapsKt.mutableMapOf(new Pair[] { TuplesKt.to((Object)"language", (Object)"\u4e2d\u6587\uff08\u666e\u901a\u8bdd\uff0c\u7b80\u4f53\uff09"), TuplesKt.to((Object)"voice", (Object)"zh-CN-XiaoxiaoNeural"), TuplesKt.to((Object)"text", (Object)text), TuplesKt.to((Object)"role", (Object)"0"), TuplesKt.to((Object)"style", (Object)"0"), TuplesKt.to((Object)"rate", (Object)"0"), TuplesKt.to((Object)"pitch", (Object)"0"), TuplesKt.to((Object)"kbitrate", (Object)"audio-16khz-32kbitrate-mono-mp3"), TuplesKt.to((Object)"silence", (Object)""), TuplesKt.to((Object)"styledegree", (Object)"1"), TuplesKt.to((Object)"user_id", (Object)""), TuplesKt.to((Object)"yzm", (Object)"") });
                if (options != null) {
                    map.putAll(options);
                }
                final CaseInsensitiveHeaders multiMap = new CaseInsensitiveHeaders();
                map.forEach(new BiConsumer((Function2)new BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$2(multiMap)));
                final String ttsUrl = "https://www.text-to-speech.cn/getSpeek.php";
                final Function1 function1 = (Function1)new BookController$ttsByTextToSpeechCn$result.BookController$ttsByTextToSpeechCn$result$1(this, ttsUrl, multiMap);
                final Continuation continuation = $continuation;
                ((BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$continuation).L$0 = response;
                ((BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$continuation).label = 1;
                if ((awaitResult = VertxCoroutineKt.awaitResult(function1, continuation)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                response = (HttpServerResponse)((BookController$ttsByTextToSpeechCn.BookController$ttsByTextToSpeechCn$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                awaitResult = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        final HttpResponse result = (HttpResponse)awaitResult;
        BookControllerKt.access$getLogger$p().info("res: {}", (Object)result);
        if (result != null) {
            final JsonObject jsonRes = result.bodyAsJsonObject();
            BookControllerKt.access$getLogger$p().info("jsonRes: {}", (Object)jsonRes);
            if (jsonRes != null && jsonRes.getString("download") != null) {
                response.setStatusCode(302).putHeader("Location", jsonRes.getString("download")).end();
            }
            else {
                response.setStatusCode(404).end();
            }
        }
        else {
            response.setStatusCode(404).end();
        }
        return Unit.INSTANCE;
    }
    
    @NotNull
    public final File getChapterCacheDir(@NotNull final Book bookInfo, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)bookInfo, "bookInfo");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        final String md5Encode = MD5Utils.INSTANCE.md5Encode(bookInfo.getBookUrl()).toString();
        final String localCacheDirPath = ExtKt.getWorkDir("storage", "data", userNameSpace, bookInfo.getName() + '_' + bookInfo.getAuthor(), md5Encode);
        final File localCacheDir = new File(localCacheDirPath);
        if (!localCacheDir.exists()) {
            localCacheDir.mkdirs();
        }
        return localCacheDir;
    }
    
    @NotNull
    public final Set<Integer> getCachedChapterContentSet(@NotNull final Book bookInfo, @NotNull final String userNameSpace) {
        Intrinsics.checkNotNullParameter((Object)bookInfo, "bookInfo");
        Intrinsics.checkNotNullParameter((Object)userNameSpace, "userNameSpace");
        final File localCacheDir = this.getChapterCacheDir(bookInfo, userNameSpace);
        final Set cachedChapterContentSet = new LinkedHashSet();
        final File[] listFiles = localCacheDir.listFiles();
        Intrinsics.checkNotNullExpressionValue((Object)listFiles, "localCacheDir.listFiles()");
        final Object[] $this$forEach$iv = listFiles;
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final File it = (File)element$iv;
            final int n = 0;
            final String name = it.getName();
            Intrinsics.checkNotNullExpressionValue((Object)name, "it.name");
            if (!StringsKt.startsWith$default(name, ".", false, 2, (Object)null)) {
                final String name2 = it.getName();
                Intrinsics.checkNotNullExpressionValue((Object)name2, "it.name");
                if (StringsKt.endsWith$default(name2, ".txt", false, 2, (Object)null)) {
                    final Set set = cachedChapterContentSet;
                    final String name3 = it.getName();
                    Intrinsics.checkNotNullExpressionValue((Object)name3, "it.name");
                    set.add(Integer.parseInt(StringsKt.replace$default(name3, ".txt", "", false, 4, (Object)null)));
                }
            }
        }
        return cachedChapterContentSet;
    }
    
    @Nullable
    public final Object getShelfBookWithCacheInfo(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1) {
                final BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1 bookController$getShelfBookWithCacheInfo$1 = (BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$completion;
                if ((bookController$getShelfBookWithCacheInfo$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1 bookController$getShelfBookWithCacheInfo$2 = bookController$getShelfBookWithCacheInfo$1;
                    bookController$getShelfBookWithCacheInfo$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        String userNameSpace = null;
        final ReturnData returnData2;
        Object bookShelfBooks = null;
        Label_0288: {
            ReturnData returnData = null;
            Object checkAuth = null;
            switch (((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    returnData = new ReturnData();
                    final BookController bookController = this;
                    final RoutingContext context2 = context;
                    final Continuation $completion2 = $continuation;
                    ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$0 = this;
                    ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$1 = context;
                    ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$2 = returnData;
                    ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).label = 1;
                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    returnData = (ReturnData)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$2;
                    context = (RoutingContext)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$1;
                    this = (BookController)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    checkAuth = $result;
                    break;
                }
                case 2: {
                    userNameSpace = (String)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$2;
                    returnData2 = (ReturnData)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$1;
                    this = (BookController)((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    bookShelfBooks = $result;
                    break Label_0288;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            if (!(boolean)checkAuth) {
                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
            }
            userNameSpace = this.getUserNameSpace(context);
            final BookController bookController2 = this;
            final boolean refresh = false;
            final String userNameSpace2 = userNameSpace;
            final Continuation $completion3 = $continuation;
            ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$0 = this;
            ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$1 = returnData;
            ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).L$2 = userNameSpace;
            ((BookController$getShelfBookWithCacheInfo.BookController$getShelfBookWithCacheInfo$1)$continuation).label = 2;
            if ((bookShelfBooks = bookController2.getBookShelfBooks(refresh, userNameSpace2, (Continuation<? super List<Book>>)$completion3)) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        final List bookList = (List)bookShelfBooks;
        final List result = new ArrayList();
        int j = 0;
        final int size = bookList.size();
        if (j < size) {
            do {
                final int i = j;
                ++j;
                final Book bookInfo = bookList.get(i);
                if (!bookInfo.isLocalBook()) {
                    final Set cachedSet = this.getCachedChapterContentSet(bookInfo, userNameSpace);
                    final Map bookInfoMap = TypeIntrinsics.asMutableMap((Object)ExtKt.toMap(bookInfo));
                    bookInfoMap.put("cachedChapterCount", Boxing.boxInt(cachedSet.size()));
                    result.add(bookInfoMap);
                }
                else {
                    result.add(bookInfo);
                }
            } while (j < size);
        }
        return ReturnData.setData$default(returnData2, result, null, 2, null);
    }
    
    @Nullable
    public final Object exportBook(@NotNull final RoutingContext context, @NotNull final Continuation<? super Unit> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: instanceof      Lcom/htmake/reader/api/controller/BookController$exportBook$1;
        //     4: ifeq            39
        //     7: aload_2        
        //     8: checkcast       Lcom/htmake/reader/api/controller/BookController$exportBook$1;
        //    11: astore          13
        //    13: aload           13
        //    15: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //    18: ldc             -2147483648
        //    20: iand           
        //    21: ifeq            39
        //    24: aload           13
        //    26: dup            
        //    27: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //    30: ldc             -2147483648
        //    32: isub           
        //    33: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //    36: goto            50
        //    39: new             Lcom/htmake/reader/api/controller/BookController$exportBook$1;
        //    42: dup            
        //    43: aload_0        
        //    44: aload_2        
        //    45: invokespecial   com/htmake/reader/api/controller/BookController$exportBook$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    48: astore          $continuation
        //    50: aload           $continuation
        //    52: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.result:Ljava/lang/Object;
        //    55: astore          $result
        //    57: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    60: astore          14
        //    62: aload           $continuation
        //    64: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //    67: tableswitch {
        //                0: 100
        //                1: 153
        //                2: 757
        //                3: 992
        //                4: 1077
        //          default: 1155
        //        }
        //   100: aload           $result
        //   102: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   105: new             Lcom/htmake/reader/api/ReturnData;
        //   108: dup            
        //   109: invokespecial   com/htmake/reader/api/ReturnData.<init>:()V
        //   112: astore_3        /* returnData */
        //   113: aload_0         /* this */
        //   114: aload_1         /* context */
        //   115: aload           $continuation
        //   117: aload           $continuation
        //   119: aload_0         /* this */
        //   120: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   123: aload           $continuation
        //   125: aload_1         /* context */
        //   126: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //   129: aload           $continuation
        //   131: aload_3         /* returnData */
        //   132: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //   135: aload           $continuation
        //   137: iconst_1       
        //   138: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //   141: invokevirtual   com/htmake/reader/api/controller/BookController.checkAuth:(Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   144: dup            
        //   145: aload           14
        //   147: if_acmpne       187
        //   150: aload           14
        //   152: areturn        
        //   153: aload           $continuation
        //   155: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //   158: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   161: astore_3        /* returnData */
        //   162: aload           $continuation
        //   164: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //   167: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   170: astore_1        /* context */
        //   171: aload           $continuation
        //   173: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   176: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   179: astore_0        /* this */
        //   180: aload           $result
        //   182: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   185: aload           $result
        //   187: checkcast       Ljava/lang/Boolean;
        //   190: invokevirtual   java/lang/Boolean.booleanValue:()Z
        //   193: ifne            218
        //   196: aload_1         /* context */
        //   197: aload_3         /* returnData */
        //   198: ldc             "NEED_LOGIN"
        //   200: aconst_null    
        //   201: iconst_2       
        //   202: aconst_null    
        //   203: invokestatic    com/htmake/reader/api/ReturnData.setData$default:(Lcom/htmake/reader/api/ReturnData;Ljava/lang/Object;Ljava/lang/String;ILjava/lang/Object;)Lcom/htmake/reader/api/ReturnData;
        //   206: ldc             "\u8bf7\u767b\u5f55\u540e\u4f7f\u7528"
        //   208: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   211: invokestatic    com/htmake/reader/utils/VertExtKt.success:(Lio/vertx/ext/web/RoutingContext;Ljava/lang/Object;)V
        //   214: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   217: areturn        
        //   218: aconst_null    
        //   219: astore          4
        //   221: iconst_0       
        //   222: istore          5
        //   224: aload_1         /* context */
        //   225: invokeinterface io/vertx/ext/web/RoutingContext.request:()Lio/vertx/core/http/HttpServerRequest;
        //   230: invokeinterface io/vertx/core/http/HttpServerRequest.method:()Lio/vertx/core/http/HttpMethod;
        //   235: getstatic       io/vertx/core/http/HttpMethod.POST:Lio/vertx/core/http/HttpMethod;
        //   238: if_acmpne       333
        //   241: aload_1         /* context */
        //   242: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   247: ldc_w           "url"
        //   250: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   253: astore          7
        //   255: aload           7
        //   257: ifnonnull       275
        //   260: aload_1         /* context */
        //   261: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   266: ldc_w           "bookUrl"
        //   269: invokevirtual   io/vertx/core/json/JsonObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   272: goto            277
        //   275: aload           7
        //   277: astore          6
        //   279: aload           6
        //   281: ifnonnull       290
        //   284: ldc_w           ""
        //   287: goto            292
        //   290: aload           6
        //   292: astore          bookUrl
        //   294: aload_1         /* context */
        //   295: invokeinterface io/vertx/ext/web/RoutingContext.getBodyAsJson:()Lio/vertx/core/json/JsonObject;
        //   300: ldc_w           "isEpub"
        //   303: iconst_0       
        //   304: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   307: invokevirtual   io/vertx/core/json/JsonObject.getInteger:(Ljava/lang/String;Ljava/lang/Integer;)Ljava/lang/Integer;
        //   310: astore          6
        //   312: aload           6
        //   314: ldc_w           "context.bodyAsJson.getInteger(\"isEpub\", 0)"
        //   317: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   320: aload           6
        //   322: checkcast       Ljava/lang/Number;
        //   325: invokevirtual   java/lang/Number.intValue:()I
        //   328: istore          5
        //   330: goto            448
        //   333: aload_1         /* context */
        //   334: ldc_w           "url"
        //   337: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   342: astore          7
        //   344: aload           7
        //   346: ldc_w           "context.queryParam(\"url\")"
        //   349: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   352: aload           7
        //   354: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   357: checkcast       Ljava/lang/String;
        //   360: astore          6
        //   362: aload           6
        //   364: ifnonnull       373
        //   367: ldc_w           ""
        //   370: goto            375
        //   373: aload           6
        //   375: astore          bookUrl
        //   377: aload_1         /* context */
        //   378: ldc_w           "isEpub"
        //   381: invokeinterface io/vertx/ext/web/RoutingContext.queryParam:(Ljava/lang/String;)Ljava/util/List;
        //   386: astore          7
        //   388: aload           7
        //   390: ldc_w           "context.queryParam(\"isEpub\")"
        //   393: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNullExpressionValue:(Ljava/lang/Object;Ljava/lang/String;)V
        //   396: aload           7
        //   398: invokestatic    kotlin/collections/CollectionsKt.firstOrNull:(Ljava/util/List;)Ljava/lang/Object;
        //   401: checkcast       Ljava/lang/String;
        //   404: astore          6
        //   406: aload           6
        //   408: ifnonnull       415
        //   411: iconst_0       
        //   412: goto            446
        //   415: aload           6
        //   417: astore          8
        //   419: iconst_0       
        //   420: istore          9
        //   422: aload           8
        //   424: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   427: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   430: astore          7
        //   432: aload           7
        //   434: ifnonnull       441
        //   437: iconst_0       
        //   438: goto            446
        //   441: aload           7
        //   443: invokevirtual   java/lang/Integer.intValue:()I
        //   446: istore          isEpub
        //   448: aload           bookUrl
        //   450: checkcast       Ljava/lang/CharSequence;
        //   453: astore          6
        //   455: iconst_0       
        //   456: istore          7
        //   458: iconst_0       
        //   459: istore          8
        //   461: aload           6
        //   463: invokeinterface java/lang/CharSequence.length:()I
        //   468: ifne            475
        //   471: iconst_1       
        //   472: goto            476
        //   475: iconst_0       
        //   476: ifeq            494
        //   479: aload_1         /* context */
        //   480: aload_3         /* returnData */
        //   481: ldc_w           "\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5"
        //   484: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   487: invokestatic    com/htmake/reader/utils/VertExtKt.success:(Lio/vertx/ext/web/RoutingContext;Ljava/lang/Object;)V
        //   490: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   493: areturn        
        //   494: aload_0         /* this */
        //   495: aload_1         /* context */
        //   496: invokevirtual   com/htmake/reader/api/controller/BookController.getUserNameSpace:(Lio/vertx/ext/web/RoutingContext;)Ljava/lang/String;
        //   499: astore          userNameSpace
        //   501: aload_0         /* this */
        //   502: aload           bookUrl
        //   504: aload           userNameSpace
        //   506: invokevirtual   com/htmake/reader/api/controller/BookController.getShelfBookByURL:(Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/Book;
        //   509: astore          bookInfo
        //   511: aload           bookInfo
        //   513: ifnonnull       531
        //   516: aload_1         /* context */
        //   517: aload_3         /* returnData */
        //   518: ldc_w           "\u8bf7\u5148\u52a0\u5165\u4e66\u67b6"
        //   521: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   524: invokestatic    com/htmake/reader/utils/VertExtKt.success:(Lio/vertx/ext/web/RoutingContext;Ljava/lang/Object;)V
        //   527: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   530: areturn        
        //   531: aload           bookInfo
        //   533: invokevirtual   io/legado/app/data/entities/Book.isLocalBook:()Z
        //   536: ifeq            611
        //   539: aload           bookInfo
        //   541: invokevirtual   io/legado/app/data/entities/Book.isLocalTxt:()Z
        //   544: ifne            611
        //   547: aload           bookInfo
        //   549: invokevirtual   io/legado/app/data/entities/Book.getLocalFile:()Ljava/io/File;
        //   552: astore          localFile
        //   554: aload_1         /* context */
        //   555: invokeinterface io/vertx/ext/web/RoutingContext.response:()Lio/vertx/core/http/HttpServerResponse;
        //   560: ldc_w           "Cache-Control"
        //   563: ldc_w           "300"
        //   566: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   571: ldc_w           "Content-Disposition"
        //   574: ldc_w           "attachment; filename="
        //   577: aload           localFile
        //   579: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   582: ldc_w           "UTF-8"
        //   585: invokestatic    java/net/URLEncoder.encode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   588: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   591: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   596: aload           localFile
        //   598: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   601: invokeinterface io/vertx/core/http/HttpServerResponse.sendFile:(Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   606: pop            
        //   607: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   610: areturn        
        //   611: aload           bookInfo
        //   613: invokevirtual   io/legado/app/data/entities/Book.isLocalTxt:()Z
        //   616: ifeq            688
        //   619: iload           isEpub
        //   621: ifgt            688
        //   624: aload           bookInfo
        //   626: invokevirtual   io/legado/app/data/entities/Book.getLocalFile:()Ljava/io/File;
        //   629: astore          localFile
        //   631: aload_1         /* context */
        //   632: invokeinterface io/vertx/ext/web/RoutingContext.response:()Lio/vertx/core/http/HttpServerResponse;
        //   637: ldc_w           "Cache-Control"
        //   640: ldc_w           "300"
        //   643: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   648: ldc_w           "Content-Disposition"
        //   651: ldc_w           "attachment; filename="
        //   654: aload           localFile
        //   656: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   659: ldc_w           "UTF-8"
        //   662: invokestatic    java/net/URLEncoder.encode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   665: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   668: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   673: aload           localFile
        //   675: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //   678: invokeinterface io/vertx/core/http/HttpServerResponse.sendFile:(Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //   683: pop            
        //   684: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   687: areturn        
        //   688: aload_0         /* this */
        //   689: aload_1         /* context */
        //   690: aload           bookInfo
        //   692: invokevirtual   io/legado/app/data/entities/Book.getOrigin:()Ljava/lang/String;
        //   695: iconst_0       
        //   696: aload           $continuation
        //   698: iconst_4       
        //   699: aconst_null    
        //   700: aload           $continuation
        //   702: aload_0         /* this */
        //   703: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   706: aload           $continuation
        //   708: aload_1         /* context */
        //   709: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //   712: aload           $continuation
        //   714: aload_3         /* returnData */
        //   715: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //   718: aload           $continuation
        //   720: aload           userNameSpace
        //   722: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$3:Ljava/lang/Object;
        //   725: aload           $continuation
        //   727: aload           bookInfo
        //   729: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$4:Ljava/lang/Object;
        //   732: aload           $continuation
        //   734: iload           isEpub
        //   736: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.I$0:I
        //   739: aload           $continuation
        //   741: iconst_2       
        //   742: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //   745: invokestatic    com/htmake/reader/api/controller/BookController.getBookSourceString$default:(Lcom/htmake/reader/api/controller/BookController;Lio/vertx/ext/web/RoutingContext;Ljava/lang/String;ZLkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   748: dup            
        //   749: aload           14
        //   751: if_acmpne       818
        //   754: aload           14
        //   756: areturn        
        //   757: aload           $continuation
        //   759: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.I$0:I
        //   762: istore          5
        //   764: aload           $continuation
        //   766: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$4:Ljava/lang/Object;
        //   769: checkcast       Lio/legado/app/data/entities/Book;
        //   772: astore          7
        //   774: aload           $continuation
        //   776: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$3:Ljava/lang/Object;
        //   779: checkcast       Ljava/lang/String;
        //   782: astore          6
        //   784: aload           $continuation
        //   786: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //   789: checkcast       Lcom/htmake/reader/api/ReturnData;
        //   792: astore_3       
        //   793: aload           $continuation
        //   795: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //   798: checkcast       Lio/vertx/ext/web/RoutingContext;
        //   801: astore_1       
        //   802: aload           $continuation
        //   804: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   807: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   810: astore_0       
        //   811: aload           $result
        //   813: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   816: aload           $result
        //   818: checkcast       Ljava/lang/String;
        //   821: astore          bookSource
        //   823: aload           7
        //   825: invokevirtual   io/legado/app/data/entities/Book.isLocalBook:()Z
        //   828: ifne            882
        //   831: aload           bookSource
        //   833: checkcast       Ljava/lang/CharSequence;
        //   836: astore          9
        //   838: iconst_0       
        //   839: istore          10
        //   841: iconst_0       
        //   842: istore          11
        //   844: aload           9
        //   846: ifnull          859
        //   849: aload           9
        //   851: invokeinterface java/lang/CharSequence.length:()I
        //   856: ifne            863
        //   859: iconst_1       
        //   860: goto            864
        //   863: iconst_0       
        //   864: ifeq            882
        //   867: aload_1        
        //   868: aload_3        
        //   869: ldc_w           "\u672a\u914d\u7f6e\u4e66\u6e90"
        //   872: invokevirtual   com/htmake/reader/api/ReturnData.setErrorMsg:(Ljava/lang/String;)Lcom/htmake/reader/api/ReturnData;
        //   875: invokestatic    com/htmake/reader/utils/VertExtKt.success:(Lio/vertx/ext/web/RoutingContext;Ljava/lang/Object;)V
        //   878: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   881: areturn        
        //   882: new             Ljava/io/File;
        //   885: dup            
        //   886: iconst_4       
        //   887: anewarray       Ljava/lang/String;
        //   890: astore          10
        //   892: aload           10
        //   894: iconst_0       
        //   895: ldc             "storage"
        //   897: aastore        
        //   898: aload           10
        //   900: iconst_1       
        //   901: ldc_w           "assets"
        //   904: aastore        
        //   905: aload           10
        //   907: iconst_2       
        //   908: aload           6
        //   910: aastore        
        //   911: aload           10
        //   913: iconst_3       
        //   914: ldc_w           "export"
        //   917: aastore        
        //   918: aload           10
        //   920: invokestatic    com/htmake/reader/utils/ExtKt.getWorkDir:([Ljava/lang/String;)Ljava/lang/String;
        //   923: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   926: astore          exportDir
        //   928: iload           5
        //   930: ifle            1014
        //   933: aload_0        
        //   934: aload           exportDir
        //   936: aload           7
        //   938: aload           bookSource
        //   940: aload           6
        //   942: aload           $continuation
        //   944: aload           $continuation
        //   946: aload_1        
        //   947: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   950: aload           $continuation
        //   952: aconst_null    
        //   953: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //   956: aload           $continuation
        //   958: aconst_null    
        //   959: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //   962: aload           $continuation
        //   964: aconst_null    
        //   965: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$3:Ljava/lang/Object;
        //   968: aload           $continuation
        //   970: aconst_null    
        //   971: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$4:Ljava/lang/Object;
        //   974: aload           $continuation
        //   976: iconst_3       
        //   977: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //   980: invokespecial   com/htmake/reader/api/controller/BookController.exportToEpub:(Ljava/io/File;Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   983: dup            
        //   984: aload           14
        //   986: if_acmpne       1008
        //   989: aload           14
        //   991: areturn        
        //   992: aload           $continuation
        //   994: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //   997: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1000: astore_1       
        //  1001: aload           $result
        //  1003: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1006: aload           $result
        //  1008: checkcast       Ljava/io/File;
        //  1011: goto            1096
        //  1014: aload_0        
        //  1015: aload           exportDir
        //  1017: aload           7
        //  1019: aload           8
        //  1021: dup            
        //  1022: invokestatic    kotlin/jvm/internal/Intrinsics.checkNotNull:(Ljava/lang/Object;)V
        //  1025: aload           6
        //  1027: aload           $continuation
        //  1029: aload           $continuation
        //  1031: aload_1        
        //  1032: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //  1035: aload           $continuation
        //  1037: aconst_null    
        //  1038: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$1:Ljava/lang/Object;
        //  1041: aload           $continuation
        //  1043: aconst_null    
        //  1044: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$2:Ljava/lang/Object;
        //  1047: aload           $continuation
        //  1049: aconst_null    
        //  1050: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$3:Ljava/lang/Object;
        //  1053: aload           $continuation
        //  1055: aconst_null    
        //  1056: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$4:Ljava/lang/Object;
        //  1059: aload           $continuation
        //  1061: iconst_4       
        //  1062: putfield        com/htmake/reader/api/controller/BookController$exportBook$1.label:I
        //  1065: invokevirtual   com/htmake/reader/api/controller/BookController.exportToTxt:(Ljava/io/File;Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //  1068: dup            
        //  1069: aload           14
        //  1071: if_acmpne       1093
        //  1074: aload           14
        //  1076: areturn        
        //  1077: aload           $continuation
        //  1079: getfield        com/htmake/reader/api/controller/BookController$exportBook$1.L$0:Ljava/lang/Object;
        //  1082: checkcast       Lio/vertx/ext/web/RoutingContext;
        //  1085: astore_1       
        //  1086: aload           $result
        //  1088: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //  1091: aload           $result
        //  1093: checkcast       Ljava/io/File;
        //  1096: astore          bookFile
        //  1098: aload_1        
        //  1099: invokeinterface io/vertx/ext/web/RoutingContext.response:()Lio/vertx/core/http/HttpServerResponse;
        //  1104: ldc_w           "Cache-Control"
        //  1107: ldc_w           "300"
        //  1110: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //  1115: ldc_w           "Content-Disposition"
        //  1118: ldc_w           "attachment; filename="
        //  1121: aload           bookFile
        //  1123: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //  1126: ldc_w           "UTF-8"
        //  1129: invokestatic    java/net/URLEncoder.encode:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1132: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //  1135: invokeinterface io/vertx/core/http/HttpServerResponse.putHeader:(Ljava/lang/String;Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //  1140: aload           bookFile
        //  1142: invokevirtual   java/io/File.toString:()Ljava/lang/String;
        //  1145: invokeinterface io/vertx/core/http/HttpServerResponse.sendFile:(Ljava/lang/String;)Lio/vertx/core/http/HttpServerResponse;
        //  1150: pop            
        //  1151: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //  1154: areturn        
        //  1155: new             Ljava/lang/IllegalStateException;
        //  1158: dup            
        //  1159: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //  1162: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //  1165: athrow         
        //    Signature:
        //  (Lio/vertx/ext/web/RoutingContext;Lkotlin/coroutines/Continuation<-Lkotlin/Unit;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  context      
        //  $completion  
        //    StackMapTable: 00 24 27 FF 00 0A 00 0E 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 00 07 0D E2 00 00 FF 00 31 00 0F 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 34 FF 00 21 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 00 00 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 01 07 01 13 1E FF 00 38 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 00 07 00 60 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 41 07 00 60 FF 00 0C 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 07 00 60 07 00 60 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 41 07 00 60 FF 00 28 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 27 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 05 01 07 00 60 07 01 A9 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 41 07 00 60 FF 00 27 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 A9 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 19 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 03 B9 07 00 60 01 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 04 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 13 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 01 01 FF 00 01 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 01 13 07 01 13 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 1A 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 01 4D 01 01 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 40 01 11 FF 00 24 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 07 00 60 01 07 00 60 07 01 84 01 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FB 00 4F FB 00 4C FF 00 44 00 0F 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 3C 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 01 07 01 13 FF 00 28 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 07 01 4D 01 01 07 01 13 07 0D E2 07 01 13 00 00 03 40 01 FF 00 11 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 6D 00 0F 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 4F 07 01 13 FF 00 05 00 0F 07 00 02 07 00 CA 07 01 11 07 00 B9 00 01 07 00 60 07 01 84 07 00 60 07 00 5E 07 00 59 00 07 01 13 07 0D E2 07 01 13 00 00 FF 00 3E 00 0F 07 00 02 07 00 CA 07 01 11 00 00 00 00 00 00 00 00 00 07 01 13 07 0D E2 07 01 13 00 00 4F 07 01 13 42 07 00 5E 3A
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    public final Object exportToTxt(@NotNull final File exportDir, @NotNull final Book bookInfo, @NotNull final String bookSource, @NotNull final String userNameSpace, @NotNull final Continuation<? super File> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$exportToTxt.BookController$exportToTxt$1) {
                final BookController$exportToTxt.BookController$exportToTxt$1 bookController$exportToTxt$1 = (BookController$exportToTxt.BookController$exportToTxt$1)$completion;
                if ((bookController$exportToTxt$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$exportToTxt.BookController$exportToTxt$1 bookController$exportToTxt$2 = bookController$exportToTxt$1;
                    bookController$exportToTxt$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$exportToTxt.BookController$exportToTxt$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$exportToTxt.BookController$exportToTxt$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        File bookFile = null;
        switch (((BookController$exportToTxt.BookController$exportToTxt$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final String filename = '\u300a' + bookInfo.getName() + "\u300b\u4f5c\u8005\uff1a" + bookInfo.getRealAuthor() + ".txt";
                final String bookPath = FileUtils.INSTANCE.getPath(exportDir, filename);
                bookFile = FileUtils.INSTANCE.createFileWithReplace(bookPath);
                final Function2 append = (Function2)new BookController$exportToTxt.BookController$exportToTxt$2(bookFile, this);
                final Continuation $completion2 = $continuation;
                ((BookController$exportToTxt.BookController$exportToTxt$1)$continuation).L$0 = bookFile;
                ((BookController$exportToTxt.BookController$exportToTxt$1)$continuation).label = 1;
                if (this.getAllContents(bookInfo, bookSource, userNameSpace, (Function2<? super String, ? super ArrayList<Triple<String, Integer, String>>, Unit>)append, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                bookFile = (File)((BookController$exportToTxt.BookController$exportToTxt$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return bookFile;
    }
    
    private final Object getAllContents(final Book book, final String bookSourceString, final String userNameSpace, final Function2<? super String, ? super ArrayList<Triple<String, Integer, String>>, Unit> append, final Continuation<? super Unit> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lcom/htmake/reader/api/controller/BookController$getAllContents$1;
        //     5: ifeq            41
        //     8: aload           5
        //    10: checkcast       Lcom/htmake/reader/api/controller/BookController$getAllContents$1;
        //    13: astore          22
        //    15: aload           22
        //    17: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           22
        //    28: dup            
        //    29: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.label:I
        //    38: goto            53
        //    41: new             Lcom/htmake/reader/api/controller/BookController$getAllContents$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           5
        //    48: invokespecial   com/htmake/reader/api/controller/BookController$getAllContents$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          23
        //    65: aload           $continuation
        //    67: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.label:I
        //    70: tableswitch {
        //                0: 92
        //                1: 217
        //          default: 525
        //        }
        //    92: aload           $result
        //    94: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    97: new             Ljava/lang/StringBuilder;
        //   100: dup            
        //   101: invokespecial   java/lang/StringBuilder.<init>:()V
        //   104: aload_1         /* book */
        //   105: invokevirtual   io/legado/app/data/entities/Book.getName:()Ljava/lang/String;
        //   108: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   111: ldc_w           "\n\u4f5c\u8005\uff1a"
        //   114: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   117: aload_1         /* book */
        //   118: invokevirtual   io/legado/app/data/entities/Book.getRealAuthor:()Ljava/lang/String;
        //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   124: ldc_w           "\n\u7b80\u4ecb\uff1a"
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: getstatic       io/legado/app/utils/HtmlFormatter.INSTANCE:Lio/legado/app/utils/HtmlFormatter;
        //   133: aload_1         /* book */
        //   134: invokevirtual   io/legado/app/data/entities/Book.getDisplayIntro:()Ljava/lang/String;
        //   137: aconst_null    
        //   138: iconst_2       
        //   139: aconst_null    
        //   140: invokestatic    io/legado/app/utils/HtmlFormatter.format$default:(Lio/legado/app/utils/HtmlFormatter;Ljava/lang/String;Lkotlin/text/Regex;ILjava/lang/Object;)Ljava/lang/String;
        //   143: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   146: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   149: astore          qy
        //   151: aload           append
        //   153: aload           qy
        //   155: aconst_null    
        //   156: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   161: pop            
        //   162: aload_0         /* this */
        //   163: aload_1         /* book */
        //   164: aload_2         /* bookSourceString */
        //   165: iconst_0       
        //   166: aload_3         /* userNameSpace */
        //   167: iconst_0       
        //   168: aconst_null    
        //   169: aload           $continuation
        //   171: bipush          48
        //   173: aconst_null    
        //   174: aload           $continuation
        //   176: aload_0         /* this */
        //   177: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$0:Ljava/lang/Object;
        //   180: aload           $continuation
        //   182: aload_1         /* book */
        //   183: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$1:Ljava/lang/Object;
        //   186: aload           $continuation
        //   188: aload_3         /* userNameSpace */
        //   189: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$2:Ljava/lang/Object;
        //   192: aload           $continuation
        //   194: aload           append
        //   196: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$3:Ljava/lang/Object;
        //   199: aload           $continuation
        //   201: iconst_1       
        //   202: putfield        com/htmake/reader/api/controller/BookController$getAllContents$1.label:I
        //   205: invokestatic    com/htmake/reader/api/controller/BookController.getLocalChapterList$default:(Lcom/htmake/reader/api/controller/BookController;Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   208: dup            
        //   209: aload           23
        //   211: if_acmpne       261
        //   214: aload           23
        //   216: areturn        
        //   217: aload           $continuation
        //   219: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$3:Ljava/lang/Object;
        //   222: checkcast       Lkotlin/jvm/functions/Function2;
        //   225: astore          append
        //   227: aload           $continuation
        //   229: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$2:Ljava/lang/Object;
        //   232: checkcast       Ljava/lang/String;
        //   235: astore_3        /* userNameSpace */
        //   236: aload           $continuation
        //   238: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$1:Ljava/lang/Object;
        //   241: checkcast       Lio/legado/app/data/entities/Book;
        //   244: astore_1        /* book */
        //   245: aload           $continuation
        //   247: getfield        com/htmake/reader/api/controller/BookController$getAllContents$1.L$0:Ljava/lang/Object;
        //   250: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   253: astore_0        /* this */
        //   254: aload           $result
        //   256: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   259: aload           $result
        //   261: checkcast       Ljava/util/List;
        //   264: astore          chapterList
        //   266: aload_0         /* this */
        //   267: aload_1         /* book */
        //   268: aload_3         /* userNameSpace */
        //   269: invokevirtual   com/htmake/reader/api/controller/BookController.getChapterCacheDir:(Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/io/File;
        //   272: astore          localCacheDir
        //   274: aload           chapterList
        //   276: checkcast       Ljava/lang/Iterable;
        //   279: astore          $this$forEachIndexed$iv
        //   281: iconst_0       
        //   282: istore          $i$f$forEachIndexed
        //   284: iconst_0       
        //   285: istore          index$iv
        //   287: aload           $this$forEachIndexed$iv
        //   289: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   294: astore          12
        //   296: aload           12
        //   298: invokeinterface java/util/Iterator.hasNext:()Z
        //   303: ifeq            520
        //   306: aload           12
        //   308: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   313: astore          item$iv
        //   315: iload           index$iv
        //   317: iinc            index$iv, 1
        //   320: istore          14
        //   322: iconst_0       
        //   323: istore          15
        //   325: iload           14
        //   327: ifge            333
        //   330: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   333: iload           14
        //   335: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   338: aload           item$iv
        //   340: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   343: astore          16
        //   345: checkcast       Ljava/lang/Number;
        //   348: invokevirtual   java/lang/Number.intValue:()I
        //   351: istore          index
        //   353: iconst_0       
        //   354: istore          $i$a$-forEachIndexed-BookController$getAllContents$2
        //   356: new             Ljava/io/File;
        //   359: dup            
        //   360: new             Ljava/lang/StringBuilder;
        //   363: dup            
        //   364: invokespecial   java/lang/StringBuilder.<init>:()V
        //   367: aload           localCacheDir
        //   369: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   372: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   375: getstatic       java/io/File.separator:Ljava/lang/String;
        //   378: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   381: iload           index
        //   383: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   386: ldc_w           ".txt"
        //   389: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   392: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   395: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   398: astore          chapterCacheFile
        //   400: ldc_w           ""
        //   403: astore          content
        //   405: aload_0         /* this */
        //   406: invokevirtual   com/htmake/reader/api/controller/BookController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   409: invokevirtual   com/htmake/reader/config/AppConfig.getExportNoChapterName:()Z
        //   412: ifne            445
        //   415: new             Ljava/lang/StringBuilder;
        //   418: dup            
        //   419: invokespecial   java/lang/StringBuilder.<init>:()V
        //   422: aload           content
        //   424: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   427: aload           chapter
        //   429: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   432: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   435: bipush          10
        //   437: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   440: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   443: astore          content
        //   445: aload           chapterCacheFile
        //   447: invokevirtual   java/io/File.exists:()Z
        //   450: ifeq            489
        //   453: new             Ljava/lang/StringBuilder;
        //   456: dup            
        //   457: invokespecial   java/lang/StringBuilder.<init>:()V
        //   460: aload           content
        //   462: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   465: aload           chapterCacheFile
        //   467: aconst_null    
        //   468: iconst_1       
        //   469: aconst_null    
        //   470: invokestatic    kotlin/io/FilesKt.readText$default:(Ljava/io/File;Ljava/nio/charset/Charset;ILjava/lang/Object;)Ljava/lang/String;
        //   473: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   476: bipush          10
        //   478: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   481: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   484: astore          content
        //   486: goto            499
        //   489: aload           content
        //   491: ldc_w           "\u6682\u65e0\u7f13\u5b58\u5185\u5bb9\u3002\n"
        //   494: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   497: astore          content
        //   499: aload           append
        //   501: ldc_w           "\n\n"
        //   504: aload           content
        //   506: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   509: aconst_null    
        //   510: invokeinterface kotlin/jvm/functions/Function2.invoke:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   515: pop            
        //   516: nop            
        //   517: goto            296
        //   520: nop            
        //   521: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   524: areturn        
        //   525: new             Ljava/lang/IllegalStateException;
        //   528: dup            
        //   529: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   532: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   535: athrow         
        //    Signature:
        //  (Lio/legado/app/data/entities/Book;Ljava/lang/String;Ljava/lang/String;Lkotlin/jvm/functions/Function2<-Ljava/lang/String;-Ljava/util/ArrayList<Lkotlin/Triple<Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/String;>;>;Lkotlin/Unit;>;Lkotlin/coroutines/Continuation<-Lkotlin/Unit;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name              Flags  
        //  ----------------  -----
        //  book              
        //  bookSourceString  
        //  userNameSpace     
        //  append            
        //  $completion       
        //    StackMapTable: 00 0C 29 FF 00 0B 00 17 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 0E 2B 00 00 FF 00 26 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0E 2B 07 01 13 00 00 FB 00 7C 6B 07 01 13 FF 00 22 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 07 01 13 07 0E 2B 07 01 13 00 00 FF 00 24 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 00 00 00 00 00 07 01 13 07 0E 2B 07 01 13 00 00 FF 00 6F 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 5E 07 00 60 07 01 13 07 0E 2B 07 01 13 00 00 2B 09 FF 00 14 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 07 01 13 07 0E 2B 07 01 13 00 00 FF 00 04 00 18 07 00 02 07 01 84 07 00 60 07 00 60 07 02 2B 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0E 2B 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    
    private final Object exportToEpub(final File exportDir, Book book, String bookSource, String userNameSpace, final Continuation<? super File> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$exportToEpub.BookController$exportToEpub$1) {
                final BookController$exportToEpub.BookController$exportToEpub$1 bookController$exportToEpub$1 = (BookController$exportToEpub.BookController$exportToEpub$1)$completion;
                if ((bookController$exportToEpub$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$exportToEpub.BookController$exportToEpub$1 bookController$exportToEpub$2 = bookController$exportToEpub$1;
                    bookController$exportToEpub$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$exportToEpub.BookController$exportToEpub$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        File bookFile = null;
        EpubBook epubBook = null;
        Label_0434: {
            switch (((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).label) {
                case 0: {
                    ResultKt.throwOnFailure($result);
                    final String filename = '\u300a' + book.getName() + "\u300b\u4f5c\u8005\uff1a" + book.getRealAuthor() + ".epub";
                    final String bookPath = FileUtils.INSTANCE.getPath(exportDir, filename);
                    bookFile = FileUtils.INSTANCE.createFileWithReplace(bookPath);
                    epubBook = new EpubBook();
                    epubBook.setVersion("2.0");
                    this.setEpubMetadata(book, epubBook);
                    final BookController bookController = this;
                    final Book book2 = book;
                    final EpubBook epubBook2 = epubBook;
                    final String bookSourceString = bookSource;
                    final Continuation $completion2 = $continuation;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$0 = this;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$1 = book;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$2 = bookSource;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$3 = userNameSpace;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$4 = bookFile;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$5 = epubBook;
                    ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).label = 1;
                    if (bookController.setCover(book2, epubBook2, bookSourceString, (Continuation<? super Unit>)$completion2) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                    break;
                }
                case 1: {
                    epubBook = (EpubBook)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$5;
                    bookFile = (File)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$4;
                    userNameSpace = (String)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$3;
                    bookSource = (String)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$2;
                    book = (Book)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$1;
                    this = (BookController)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break;
                }
                case 2: {
                    epubBook = (EpubBook)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$1;
                    bookFile = (File)((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$0;
                    ResultKt.throwOnFailure($result);
                    break Label_0434;
                }
                default: {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
            }
            final String contentModel = this.setAssets(book, epubBook);
            final BookController bookController2 = this;
            final String contentModel2 = contentModel;
            final Book book3 = book;
            final EpubBook epubBook3 = epubBook;
            final String bookSourceString2 = bookSource;
            final String userNameSpace2 = userNameSpace;
            final Continuation $completion3 = $continuation;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$0 = bookFile;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$1 = epubBook;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$2 = null;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$3 = null;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$4 = null;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).L$5 = null;
            ((BookController$exportToEpub.BookController$exportToEpub$1)$continuation).label = 2;
            if (bookController2.setEpubContent(contentModel2, book3, epubBook3, bookSourceString2, userNameSpace2, (Continuation<? super Unit>)$completion3) == coroutine_SUSPENDED) {
                return coroutine_SUSPENDED;
            }
        }
        new EpubWriter().write(epubBook, new FileOutputStream(bookFile));
        return bookFile;
    }
    
    private final String setAssets(final Book book, final EpubBook epubBook) {
        final Resources resources = epubBook.getResources();
        final URL resource = BookController.class.getResource("/epub/fonts.css");
        Intrinsics.checkNotNullExpressionValue((Object)resource, "BookController::class.java.getResource(\"/epub/fonts.css\")");
        resources.add(new Resource(TextStreamsKt.readBytes(resource), "Styles/fonts.css"));
        final Resources resources2 = epubBook.getResources();
        final URL resource2 = BookController.class.getResource("/epub/main.css");
        Intrinsics.checkNotNullExpressionValue((Object)resource2, "BookController::class.java.getResource(\"/epub/main.css\")");
        resources2.add(new Resource(TextStreamsKt.readBytes(resource2), "Styles/main.css"));
        final Resources resources3 = epubBook.getResources();
        final URL resource3 = BookController.class.getResource("/epub/logo.png");
        Intrinsics.checkNotNullExpressionValue((Object)resource3, "BookController::class.java.getResource(\"/epub/logo.png\")");
        resources3.add(new Resource(TextStreamsKt.readBytes(resource3), "Images/logo.png"));
        final String title = "\u5c01\u9762";
        final String name = book.getName();
        final String realAuthor = book.getRealAuthor();
        final String displayIntro = book.getDisplayIntro();
        final String kind = book.getKind();
        final String wordCount = book.getWordCount();
        final URL resource4 = BookController.class.getResource("/epub/cover.html");
        Intrinsics.checkNotNullExpressionValue((Object)resource4, "BookController::class.java.getResource(\"/epub/cover.html\")");
        epubBook.addSection(title, ResourceUtil.createPublicResource(name, realAuthor, displayIntro, kind, wordCount, new String(TextStreamsKt.readBytes(resource4), Charsets.UTF_8), "Text/cover.html"));
        final String title2 = "\u7b80\u4ecb";
        final String name2 = book.getName();
        final String realAuthor2 = book.getRealAuthor();
        final String displayIntro2 = book.getDisplayIntro();
        final String kind2 = book.getKind();
        final String wordCount2 = book.getWordCount();
        final URL resource5 = BookController.class.getResource("/epub/intro.html");
        Intrinsics.checkNotNullExpressionValue((Object)resource5, "BookController::class.java.getResource(\"/epub/intro.html\")");
        epubBook.addSection(title2, ResourceUtil.createPublicResource(name2, realAuthor2, displayIntro2, kind2, wordCount2, new String(TextStreamsKt.readBytes(resource5), Charsets.UTF_8), "Text/intro.html"));
        final URL resource6 = BookController.class.getResource("/epub/chapter.html");
        Intrinsics.checkNotNullExpressionValue((Object)resource6, "BookController::class.java.getResource(\"/epub/chapter.html\")");
        return new String(TextStreamsKt.readBytes(resource6), Charsets.UTF_8);
    }
    
    private final Object setCover(final Book book, EpubBook epubBook, final String bookSourceString, final Continuation<? super Unit> $completion) {
        final Continuation $continuation;
        Label_0053: {
            if ($completion instanceof BookController$setCover.BookController$setCover$1) {
                final BookController$setCover.BookController$setCover$1 bookController$setCover$1 = (BookController$setCover.BookController$setCover$1)$completion;
                if ((bookController$setCover$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$setCover.BookController$setCover$1 bookController$setCover$2 = bookController$setCover$1;
                    bookController$setCover$2.label -= Integer.MIN_VALUE;
                    break Label_0053;
                }
            }
            $continuation = (Continuation)new BookController$setCover.BookController$setCover$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$setCover.BookController$setCover$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        switch (((BookController$setCover.BookController$setCover$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                final String coverUrl = book.getDisplayCover();
                if (coverUrl == null) {
                    break;
                }
                if (StringsKt.startsWith$default(coverUrl, "/", false, 2, (Object)null)) {
                    final String[] subDirFiles = { "storage", null };
                    final int n = 1;
                    final String s = coverUrl;
                    final String s2 = "/";
                    final String separator = File.separator;
                    Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
                    final String replace$default = StringsKt.replace$default(s, s2, separator, false, 4, (Object)null);
                    final int beginIndex = 1;
                    final String s3 = replace$default;
                    if (s3 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
                    }
                    final String substring = s3.substring(beginIndex);
                    Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
                    subDirFiles[n] = substring;
                    final File coverFile = new File(ExtKt.getWorkDir(subDirFiles));
                    final byte[] byteArray = FilesKt.readBytes(coverFile);
                    epubBook.setCoverImage(new Resource(byteArray, "Images/cover.jpg"));
                    break;
                }
                else {
                    if (bookSourceString == null) {
                        break;
                    }
                    final String ext = this.getFileExt(coverUrl, "jpg");
                    final String md5Encode = MD5Utils.INSTANCE.md5Encode(coverUrl).toString();
                    final String cachePath = ExtKt.getWorkDir("storage", "cache", md5Encode + '.' + ext);
                    final File cacheFile = new File(cachePath);
                    if (cacheFile.exists()) {
                        final byte[] byteArray2 = FilesKt.readBytes(cacheFile);
                        epubBook.setCoverImage(new Resource(byteArray2, "Images/cover.jpg"));
                        return Unit.INSTANCE;
                    }
                }
                break;
            }
            case 1: {
                Label_0454: {
                    break Label_0454;
                    final String coverUrl;
                    final String s4 = coverUrl;
                    final String s5 = null;
                    final Integer n2 = null;
                    final String s6 = null;
                    final Integer n3 = null;
                    final String s7 = null;
                    final Object fromJson-IoAF18A = BookSource.Companion.fromJson-IoAF18A(bookSourceString);
                    final AnalyzeUrl analyzeUrl = new AnalyzeUrl(s4, s5, n2, s6, n3, s7, (BaseSource)(Result.isFailure-impl(fromJson-IoAF18A) ? null : fromJson-IoAF18A), null, null, null, null, 1982, null);
                    try {
                        final AnalyzeUrl analyzeUrl2 = analyzeUrl;
                        final Continuation $completion2 = $continuation;
                        ((BookController$setCover.BookController$setCover$1)$continuation).L$0 = epubBook;
                        ((BookController$setCover.BookController$setCover$1)$continuation).label = 1;
                        Object byteArrayAwait;
                        if ((byteArrayAwait = analyzeUrl2.getByteArrayAwait((Continuation<? super byte[]>)$completion2)) == coroutine_SUSPENDED) {
                            return coroutine_SUSPENDED;
                        }
                        while (true) {
                            final byte[] it = (byte[])byteArrayAwait;
                            final int n4 = 0;
                            epubBook.setCoverImage(new Resource(it, "Images/cover.jpg"));
                            break;
                            epubBook = (EpubBook)((BookController$setCover.BookController$setCover$1)$continuation).L$0;
                            ResultKt.throwOnFailure($result);
                            byteArrayAwait = $result;
                            continue;
                        }
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        return Unit.INSTANCE;
    }
    
    private final Object setEpubContent(final String contentModel, final Book book, final EpubBook epubBook, final String bookSourceString, final String userNameSpace, final Continuation<? super Unit> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lcom/htmake/reader/api/controller/BookController$setEpubContent$1;
        //     5: ifeq            41
        //     8: aload           6
        //    10: checkcast       Lcom/htmake/reader/api/controller/BookController$setEpubContent$1;
        //    13: astore          23
        //    15: aload           23
        //    17: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           23
        //    28: dup            
        //    29: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.label:I
        //    38: goto            53
        //    41: new             Lcom/htmake/reader/api/controller/BookController$setEpubContent$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           6
        //    48: invokespecial   com/htmake/reader/api/controller/BookController$setEpubContent$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          24
        //    65: aload           $continuation
        //    67: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.label:I
        //    70: tableswitch {
        //                0: 92
        //                1: 160
        //          default: 575
        //        }
        //    92: aload           $result
        //    94: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    97: aload_0         /* this */
        //    98: aload_2         /* book */
        //    99: aload           bookSourceString
        //   101: iconst_0       
        //   102: aload           userNameSpace
        //   104: iconst_0       
        //   105: aconst_null    
        //   106: aload           $continuation
        //   108: bipush          48
        //   110: aconst_null    
        //   111: aload           $continuation
        //   113: aload_0         /* this */
        //   114: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$0:Ljava/lang/Object;
        //   117: aload           $continuation
        //   119: aload_1         /* contentModel */
        //   120: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$1:Ljava/lang/Object;
        //   123: aload           $continuation
        //   125: aload_2         /* book */
        //   126: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$2:Ljava/lang/Object;
        //   129: aload           $continuation
        //   131: aload_3         /* epubBook */
        //   132: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$3:Ljava/lang/Object;
        //   135: aload           $continuation
        //   137: aload           userNameSpace
        //   139: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$4:Ljava/lang/Object;
        //   142: aload           $continuation
        //   144: iconst_1       
        //   145: putfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.label:I
        //   148: invokestatic    com/htmake/reader/api/controller/BookController.getLocalChapterList$default:(Lcom/htmake/reader/api/controller/BookController;Lio/legado/app/data/entities/Book;Ljava/lang/String;ZLjava/lang/String;ZLkotlinx/coroutines/sync/Mutex;Lkotlin/coroutines/Continuation;ILjava/lang/Object;)Ljava/lang/Object;
        //   151: dup            
        //   152: aload           24
        //   154: if_acmpne       213
        //   157: aload           24
        //   159: areturn        
        //   160: aload           $continuation
        //   162: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$4:Ljava/lang/Object;
        //   165: checkcast       Ljava/lang/String;
        //   168: astore          userNameSpace
        //   170: aload           $continuation
        //   172: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$3:Ljava/lang/Object;
        //   175: checkcast       Lme/ag2s/epublib/domain/EpubBook;
        //   178: astore_3        /* epubBook */
        //   179: aload           $continuation
        //   181: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$2:Ljava/lang/Object;
        //   184: checkcast       Lio/legado/app/data/entities/Book;
        //   187: astore_2        /* book */
        //   188: aload           $continuation
        //   190: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$1:Ljava/lang/Object;
        //   193: checkcast       Ljava/lang/String;
        //   196: astore_1        /* contentModel */
        //   197: aload           $continuation
        //   199: getfield        com/htmake/reader/api/controller/BookController$setEpubContent$1.L$0:Ljava/lang/Object;
        //   202: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   205: astore_0        /* this */
        //   206: aload           $result
        //   208: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   211: aload           $result
        //   213: checkcast       Ljava/util/List;
        //   216: astore          chapterList
        //   218: aload_0         /* this */
        //   219: aload_2         /* book */
        //   220: aload           userNameSpace
        //   222: invokevirtual   com/htmake/reader/api/controller/BookController.getChapterCacheDir:(Lio/legado/app/data/entities/Book;Ljava/lang/String;)Ljava/io/File;
        //   225: astore          localCacheDir
        //   227: aload           chapterList
        //   229: checkcast       Ljava/lang/Iterable;
        //   232: astore          $this$forEachIndexed$iv
        //   234: iconst_0       
        //   235: istore          $i$f$forEachIndexed
        //   237: iconst_0       
        //   238: istore          index$iv
        //   240: aload           $this$forEachIndexed$iv
        //   242: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   247: astore          12
        //   249: aload           12
        //   251: invokeinterface java/util/Iterator.hasNext:()Z
        //   256: ifeq            570
        //   259: aload           12
        //   261: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   266: astore          item$iv
        //   268: iload           index$iv
        //   270: iinc            index$iv, 1
        //   273: istore          14
        //   275: iconst_0       
        //   276: istore          15
        //   278: iload           14
        //   280: ifge            286
        //   283: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   286: iload           14
        //   288: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   291: aload           item$iv
        //   293: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   296: astore          16
        //   298: checkcast       Ljava/lang/Number;
        //   301: invokevirtual   java/lang/Number.intValue:()I
        //   304: istore          index
        //   306: iconst_0       
        //   307: istore          $i$a$-forEachIndexed-BookController$setEpubContent$2
        //   309: ldc_w           ""
        //   312: astore          content
        //   314: aload_0         /* this */
        //   315: invokevirtual   com/htmake/reader/api/controller/BookController.getAppConfig:()Lcom/htmake/reader/config/AppConfig;
        //   318: invokevirtual   com/htmake/reader/config/AppConfig.getExportNoChapterName:()Z
        //   321: ifne            354
        //   324: new             Ljava/lang/StringBuilder;
        //   327: dup            
        //   328: invokespecial   java/lang/StringBuilder.<init>:()V
        //   331: aload           content
        //   333: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   336: aload           chapter
        //   338: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   341: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   344: bipush          10
        //   346: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   349: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   352: astore          content
        //   354: aload_2         /* book */
        //   355: invokevirtual   io/legado/app/data/entities/Book.isLocalTxt:()Z
        //   358: ifeq            395
        //   361: aload           content
        //   363: getstatic       io/legado/app/model/localBook/LocalBook.INSTANCE:Lio/legado/app/model/localBook/LocalBook;
        //   366: aload_2         /* book */
        //   367: aload           chapter
        //   369: invokevirtual   io/legado/app/model/localBook/LocalBook.getContent:(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;)Ljava/lang/String;
        //   372: astore          20
        //   374: aload           20
        //   376: ifnonnull       385
        //   379: ldc_w           ""
        //   382: goto            387
        //   385: aload           20
        //   387: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   390: astore          content
        //   392: goto            493
        //   395: new             Ljava/io/File;
        //   398: dup            
        //   399: new             Ljava/lang/StringBuilder;
        //   402: dup            
        //   403: invokespecial   java/lang/StringBuilder.<init>:()V
        //   406: aload           localCacheDir
        //   408: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //   411: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   414: getstatic       java/io/File.separator:Ljava/lang/String;
        //   417: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   420: iload           index
        //   422: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   425: ldc_w           ".txt"
        //   428: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   431: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   434: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   437: astore          chapterCacheFile
        //   439: aload           chapterCacheFile
        //   441: invokevirtual   java/io/File.exists:()Z
        //   444: ifeq            483
        //   447: new             Ljava/lang/StringBuilder;
        //   450: dup            
        //   451: invokespecial   java/lang/StringBuilder.<init>:()V
        //   454: aload           content
        //   456: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   459: aload           chapterCacheFile
        //   461: aconst_null    
        //   462: iconst_1       
        //   463: aconst_null    
        //   464: invokestatic    kotlin/io/FilesKt.readText$default:(Ljava/io/File;Ljava/nio/charset/Charset;ILjava/lang/Object;)Ljava/lang/String;
        //   467: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   470: bipush          10
        //   472: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //   475: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   478: astore          content
        //   480: goto            493
        //   483: aload           content
        //   485: ldc_w           "\u6682\u65e0\u7f13\u5b58\u5185\u5bb9\u3002\n"
        //   488: invokestatic    kotlin/jvm/internal/Intrinsics.stringPlus:(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/String;
        //   491: astore          content
        //   493: aload_0         /* this */
        //   494: aload_3         /* epubBook */
        //   495: aload_2         /* book */
        //   496: aload           content
        //   498: aload           chapter
        //   500: invokespecial   com/htmake/reader/api/controller/BookController.fixPic:(Lme/ag2s/epublib/domain/EpubBook;Lio/legado/app/data/entities/Book;Ljava/lang/String;Lio/legado/app/data/entities/BookChapter;)Ljava/lang/String;
        //   503: astore          content1
        //   505: aload           chapter
        //   507: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   510: astore          title
        //   512: aload_3         /* epubBook */
        //   513: aload           title
        //   515: aload           title
        //   517: ldc_w           "\ud83d\udd12"
        //   520: ldc_w           ""
        //   523: iconst_0       
        //   524: iconst_4       
        //   525: aconst_null    
        //   526: invokestatic    kotlin/text/StringsKt.replace$default:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZILjava/lang/Object;)Ljava/lang/String;
        //   529: aload           content1
        //   531: aload_1         /* contentModel */
        //   532: new             Ljava/lang/StringBuilder;
        //   535: dup            
        //   536: invokespecial   java/lang/StringBuilder.<init>:()V
        //   539: ldc_w           "Text/chapter_"
        //   542: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   545: iload           index
        //   547: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   550: ldc_w           ".html"
        //   553: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   556: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   559: invokestatic    me/ag2s/epublib/util/ResourceUtil.createChapterResource:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lme/ag2s/epublib/domain/Resource;
        //   562: invokevirtual   me/ag2s/epublib/domain/EpubBook.addSection:(Ljava/lang/String;Lme/ag2s/epublib/domain/Resource;)Lme/ag2s/epublib/domain/TOCReference;
        //   565: pop            
        //   566: nop            
        //   567: goto            249
        //   570: nop            
        //   571: getstatic       kotlin/Unit.INSTANCE:Lkotlin/Unit;
        //   574: areturn        
        //   575: new             Ljava/lang/IllegalStateException;
        //   578: dup            
        //   579: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   582: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   585: athrow         
        //    Signature:
        //  (Ljava/lang/String;Lio/legado/app/data/entities/Book;Lme/ag2s/epublib/domain/EpubBook;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation<-Lkotlin/Unit;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name              Flags  
        //  ----------------  -----
        //  contentModel      
        //  book              
        //  epubBook          
        //  bookSourceString  
        //  userNameSpace     
        //  $completion       
        //    StackMapTable: 00 0F 29 FF 00 0B 00 18 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 0E F8 00 00 FF 00 26 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0E F8 07 01 13 00 00 FB 00 43 74 07 01 13 FF 00 23 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 24 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 00 00 00 00 00 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 43 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 00 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 1E 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 07 00 60 00 07 01 13 07 0E F8 07 01 13 00 01 07 00 60 FF 00 01 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 07 00 60 00 07 01 13 07 0E F8 07 01 13 00 02 07 00 60 07 00 60 FF 00 07 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 00 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 57 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 07 00 5E 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 09 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 07 01 13 01 01 07 03 FC 01 01 07 00 60 07 01 13 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 4C 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 07 01 A9 07 00 5E 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 00 07 01 13 07 0E F8 07 01 13 00 00 FF 00 04 00 19 07 00 02 07 00 60 07 01 84 07 0E 5A 07 00 60 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0E F8 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    
    private final String fixPic(final EpubBook epubBook, final Book book, final String content, final BookChapter chapter) {
        final StringBuilder data = new StringBuilder("");
        final Iterable $this$forEach$iv = StringsKt.split$default((CharSequence)content, new String[] { "\n" }, false, 0, 6, (Object)null);
        final int $i$f$forEach = 0;
        for (final Object element$iv : $this$forEach$iv) {
            final String text = (String)element$iv;
            final int n = 0;
            Object text2 = null;
            text2 = text;
            final Matcher matcher = AppPattern.INSTANCE.getImgPattern().matcher(text);
            while (matcher.find()) {
                final String group = matcher.group(1);
                if (group == null) {
                    continue;
                }
                final String it = group;
                final int n2 = 0;
                final String src = NetworkUtils.INSTANCE.getAbsoluteURL(chapter.getUrl(), it);
                final String originalHref = MD5Utils.INSTANCE.md5Encode16(src) + '.' + BookHelp.INSTANCE.getImageSuffix(src);
                final String href = Intrinsics.stringPlus("Images/", (Object)originalHref);
                final File vFile = BookHelp.INSTANCE.getImage(book, src);
                if (!vFile.exists()) {
                    continue;
                }
                final FileResourceProvider fp = new FileResourceProvider(vFile.getParent());
                final LazyResource img = new LazyResource(fp, href, originalHref);
                epubBook.getResources().add(img);
                text2 = StringsKt.replace$default((String)text2, it, Intrinsics.stringPlus("../", (Object)href), false, 4, (Object)null);
            }
            data.append((String)text2).append("\n");
        }
        final String string = data.toString();
        Intrinsics.checkNotNullExpressionValue((Object)string, "data.toString()");
        return string;
    }
    
    private final void setEpubMetadata(final Book book, final EpubBook epubBook) {
        final me.ag2s.epublib.domain.Metadata metadata = new me.ag2s.epublib.domain.Metadata();
        metadata.getTitles().add(book.getName());
        metadata.getAuthors().add(new Author(book.getRealAuthor()));
        metadata.setLanguage("zh");
        metadata.getDates().add(new Date());
        metadata.getPublishers().add("Legado");
        metadata.getDescriptions().add(book.getDisplayIntro());
        epubBook.setMetadata(metadata);
    }
    
    @Nullable
    public final Object searchBookContent(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$searchBookContent.BookController$searchBookContent$1) {
                final BookController$searchBookContent.BookController$searchBookContent$1 bookController$searchBookContent$1 = (BookController$searchBookContent.BookController$searchBookContent$1)$completion;
                if ((bookController$searchBookContent$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$searchBookContent.BookController$searchBookContent$1 bookController$searchBookContent$2 = bookController$searchBookContent$1;
                    bookController$searchBookContent$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$searchBookContent.BookController$searchBookContent$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData2 = null;
        int i$4 = 0;
        List resultList = null;
        while (true) {
            int n = 0;
            int i$2 = 0;
            int i$3 = 0;
            final Ref$BooleanRef ref$BooleanRef;
            Object searchChapter = null;
            Label_1476: {
                int i$0 = 0;
                Book book = null;
                String s2 = null;
                Object localChapterList$default = null;
                Label_1127: {
                    final String s;
                    String bookSource = null;
                    Label_0959: {
                        Object bookSourceString$default = null;
                        Label_0910: {
                            ReturnData returnData = null;
                            Object checkAuth = null;
                            switch (((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).label) {
                                case 0: {
                                    ResultKt.throwOnFailure($result);
                                    returnData = new ReturnData();
                                    final BookController bookController = this;
                                    final RoutingContext context2 = context;
                                    final Continuation $completion2 = $continuation;
                                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0 = this;
                                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1 = context;
                                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2 = returnData;
                                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).label = 1;
                                    if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                                        return coroutine_SUSPENDED;
                                    }
                                    break;
                                }
                                case 1: {
                                    returnData = (ReturnData)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2;
                                    context = (RoutingContext)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1;
                                    this = (BookController)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    checkAuth = $result;
                                    break;
                                }
                                case 2: {
                                    n = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1;
                                    i$0 = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0;
                                    book = (Book)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$5;
                                    s = (String)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4;
                                    s2 = (String)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3;
                                    returnData2 = (ReturnData)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2;
                                    context = (RoutingContext)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1;
                                    this = (BookController)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    bookSourceString$default = $result;
                                    break Label_0910;
                                }
                                case 3: {
                                    n = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1;
                                    i$0 = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0;
                                    book = (Book)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4;
                                    s2 = (String)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3;
                                    returnData2 = (ReturnData)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2;
                                    context = (RoutingContext)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1;
                                    this = (BookController)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    localChapterList$default = $result;
                                    break Label_1127;
                                }
                                case 4: {
                                    i$2 = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$3;
                                    i$3 = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$2;
                                    i$4 = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1;
                                    n = ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0;
                                    resultList = (List)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$6;
                                    ref$BooleanRef = (Ref$BooleanRef)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$5;
                                    final List list = (List)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4;
                                    book = (Book)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3;
                                    s2 = (String)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2;
                                    returnData2 = (ReturnData)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1;
                                    this = (BookController)((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0;
                                    ResultKt.throwOnFailure($result);
                                    searchChapter = $result;
                                    break Label_1476;
                                }
                                default: {
                                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                                }
                            }
                            if (!(boolean)checkAuth) {
                                return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
                            }
                            String bookUrl;
                            String keyword;
                            int lastIndex;
                            int size = 0;
                            if (context.request().method() == HttpMethod.POST) {
                                final String string = context.getBodyAsJson().getString("url");
                                final String s3 = (string == null) ? context.getBodyAsJson().getString("bookUrl") : string;
                                bookUrl = ((s3 == null) ? "" : s3);
                                final String string2 = context.getBodyAsJson().getString("keyword");
                                keyword = ((string2 == null) ? "" : string2);
                                final Integer integer = context.getBodyAsJson().getInteger("lastIndex", Boxing.boxInt(0));
                                Intrinsics.checkNotNullExpressionValue((Object)integer, "context.bodyAsJson.getInteger(\"lastIndex\", 0)");
                                lastIndex = integer.intValue();
                                final Integer integer2 = context.getBodyAsJson().getInteger("size", Boxing.boxInt(20));
                                Intrinsics.checkNotNullExpressionValue((Object)integer2, "context.bodyAsJson.getInteger(\"size\", 20)");
                                n = integer2.intValue();
                            }
                            else {
                                final List queryParam = context.queryParam("url");
                                Intrinsics.checkNotNullExpressionValue((Object)queryParam, "context.queryParam(\"url\")");
                                final String s4 = (String)CollectionsKt.firstOrNull(queryParam);
                                bookUrl = ((s4 == null) ? "" : s4);
                                final List queryParam2 = context.queryParam("keyword");
                                Intrinsics.checkNotNullExpressionValue((Object)queryParam2, "context.queryParam(\"keyword\")");
                                final String s5 = (String)CollectionsKt.firstOrNull(queryParam2);
                                keyword = ((s5 == null) ? "" : s5);
                                final List queryParam3 = context.queryParam("lastIndex");
                                Intrinsics.checkNotNullExpressionValue((Object)queryParam3, "context.queryParam(\"lastIndex\")");
                                final String s6 = (String)CollectionsKt.firstOrNull(queryParam3);
                                int n2;
                                if (s6 == null) {
                                    n2 = 0;
                                }
                                else {
                                    final Integer boxInt = Boxing.boxInt(Integer.parseInt(s6));
                                    n2 = ((boxInt == null) ? 0 : boxInt);
                                }
                                lastIndex = n2;
                                final List queryParam4 = context.queryParam("size");
                                Intrinsics.checkNotNullExpressionValue((Object)queryParam4, "context.queryParam(\"size\")");
                                final String s7 = (String)CollectionsKt.firstOrNull(queryParam4);
                                int n3;
                                if (s7 == null) {
                                    n3 = 20;
                                }
                                else {
                                    final Integer boxInt2 = Boxing.boxInt(Integer.parseInt(s7));
                                    n3 = ((boxInt2 == null) ? 20 : boxInt2);
                                }
                                size = n3;
                            }
                            if (bookUrl.length() == 0) {
                                return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u4e66\u7c4d\u94fe\u63a5");
                            }
                            if (keyword.length() == 0) {
                                return returnData.setErrorMsg("\u8bf7\u8f93\u5165\u641c\u7d22\u5173\u952e\u8bcd");
                            }
                            final String userNameSpace = this.getUserNameSpace(context);
                            final Book bookInfo = this.getShelfBookByURL(bookUrl, userNameSpace);
                            if (bookInfo == null) {
                                return returnData.setErrorMsg("\u8bf7\u5148\u52a0\u5165\u4e66\u67b6");
                            }
                            bookSource = null;
                            if (bookInfo.isLocalBook()) {
                                break Label_0959;
                            }
                            final BookController bookController2 = this;
                            final RoutingContext routingContext = context;
                            final String origin = bookInfo.getOrigin();
                            final boolean b = false;
                            final Continuation continuation = $continuation;
                            final int n4 = 4;
                            final Object o = null;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0 = this;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1 = context;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2 = returnData;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3 = keyword;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4 = userNameSpace;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$5 = bookInfo;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0 = lastIndex;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1 = size;
                            ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).label = 2;
                            if ((bookSourceString$default = getBookSourceString$default(bookController2, routingContext, origin, b, continuation, n4, o)) == coroutine_SUSPENDED) {
                                return coroutine_SUSPENDED;
                            }
                        }
                        bookSource = (String)bookSourceString$default;
                        final CharSequence charSequence = bookSource;
                        if (charSequence == null || charSequence.length() == 0) {
                            return returnData2.setErrorMsg("\u672a\u914d\u7f6e\u4e66\u6e90");
                        }
                    }
                    final BookController bookController3 = this;
                    final Book book2 = book;
                    final String s8 = bookSource;
                    final String s9 = (s8 == null) ? "" : s8;
                    final boolean b2 = false;
                    final String s10 = s;
                    final boolean b3 = false;
                    final Mutex mutex = null;
                    final Continuation continuation2 = $continuation;
                    final int n5 = 48;
                    final Object o2 = null;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0 = this;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1 = context;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2 = returnData2;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3 = s2;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4 = book;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$5 = null;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0 = i$0;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1 = n;
                    ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).label = 3;
                    if ((localChapterList$default = getLocalChapterList$default(bookController3, book2, s9, b2, s10, b3, mutex, continuation2, n5, o2)) == coroutine_SUSPENDED) {
                        return coroutine_SUSPENDED;
                    }
                }
                final List chapterList = (List)localChapterList$default;
                if (i$0 >= chapterList.size()) {
                    return returnData2.setErrorMsg("\u6ca1\u6709\u66f4\u591a\u4e86");
                }
                final Ref$BooleanRef isEnd = new Ref$BooleanRef();
                context.request().connection().closeHandler(BookController::searchBookContent$lambda-30);
                BookControllerKt.access$getLogger$p().info("searchBookContent keyword: {} lastIndex: {}", (Object)s2, (Object)Boxing.boxInt(i$0));
                resultList = new ArrayList();
                int currentIndex = ++i$0;
                i$3 = i$0;
                i$2 = chapterList.size();
                if (i$3 >= i$2) {
                    return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"list", (Object)resultList), TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(i$4)) }), null, 2, null);
                }
                final int chapterIndex = i$3;
                ++i$3;
                currentIndex = chapterIndex;
                final BookChapter chapter = chapterList.get(chapterIndex);
                final BookController bookController4 = this;
                final Book book3 = book;
                final BookChapter chapter2 = chapter;
                final String query = s2;
                final Continuation $completion3 = $continuation;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$0 = this;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$1 = returnData2;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$2 = s2;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$3 = book;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$4 = chapterList;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$5 = isEnd;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).L$6 = resultList;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$0 = n;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$1 = currentIndex;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$2 = i$3;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).I$3 = i$2;
                ((BookController$searchBookContent.BookController$searchBookContent$1)$continuation).label = 4;
                if ((searchChapter = bookController4.searchChapter(book3, chapter2, query, (Continuation<? super List<SearchResult>>)$completion3)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
            }
            final List chapterResult = (List)searchChapter;
            if (chapterResult.size() > 0) {
                resultList.addAll(chapterResult);
            }
            if (resultList.size() < n && !ref$BooleanRef.element) {
                if (i$3 < i$2) {
                    continue;
                }
            }
            break;
        }
        return ReturnData.setData$default(returnData2, MapsKt.mapOf(new Pair[] { TuplesKt.to((Object)"list", (Object)resultList), TuplesKt.to((Object)"lastIndex", (Object)Boxing.boxInt(i$4)) }), null, 2, null);
    }
    
    @Nullable
    public final Object searchChapter(@NotNull final Book book, @NotNull final BookChapter chapter, @NotNull final String query, @NotNull final Continuation<? super List<SearchResult>> $completion) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: instanceof      Lcom/htmake/reader/api/controller/BookController$searchChapter$1;
        //     5: ifeq            41
        //     8: aload           4
        //    10: checkcast       Lcom/htmake/reader/api/controller/BookController$searchChapter$1;
        //    13: astore          21
        //    15: aload           21
        //    17: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.label:I
        //    20: ldc             -2147483648
        //    22: iand           
        //    23: ifeq            41
        //    26: aload           21
        //    28: dup            
        //    29: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.label:I
        //    32: ldc             -2147483648
        //    34: isub           
        //    35: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.label:I
        //    38: goto            53
        //    41: new             Lcom/htmake/reader/api/controller/BookController$searchChapter$1;
        //    44: dup            
        //    45: aload_0        
        //    46: aload           4
        //    48: invokespecial   com/htmake/reader/api/controller/BookController$searchChapter$1.<init>:(Lcom/htmake/reader/api/controller/BookController;Lkotlin/coroutines/Continuation;)V
        //    51: astore          $continuation
        //    53: aload           $continuation
        //    55: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.result:Ljava/lang/Object;
        //    58: astore          $result
        //    60: invokestatic    kotlin/coroutines/intrinsics/IntrinsicsKt.getCOROUTINE_SUSPENDED:()Ljava/lang/Object;
        //    63: astore          22
        //    65: aload           $continuation
        //    67: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.label:I
        //    70: tableswitch {
        //                0: 92
        //                1: 183
        //          default: 417
        //        }
        //    92: aload           $result
        //    94: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //    97: iconst_0       
        //    98: istore          6
        //   100: new             Ljava/util/ArrayList;
        //   103: dup            
        //   104: invokespecial   java/util/ArrayList.<init>:()V
        //   107: checkcast       Ljava/util/List;
        //   110: astore          searchResultsWithinChapter
        //   112: getstatic       io/legado/app/help/BookHelp.INSTANCE:Lio/legado/app/help/BookHelp;
        //   115: aload_1         /* book */
        //   116: aload_2         /* chapter */
        //   117: invokevirtual   io/legado/app/help/BookHelp.getContent:(Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;)Ljava/lang/String;
        //   120: astore          chapterContent
        //   122: aload           chapterContent
        //   124: ifnull          414
        //   127: aload_0         /* this */
        //   128: aload           chapterContent
        //   130: aload_3         /* query */
        //   131: aload           $continuation
        //   133: aload           $continuation
        //   135: aload_0         /* this */
        //   136: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$0:Ljava/lang/Object;
        //   139: aload           $continuation
        //   141: aload_2         /* chapter */
        //   142: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$1:Ljava/lang/Object;
        //   145: aload           $continuation
        //   147: aload_3         /* query */
        //   148: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$2:Ljava/lang/Object;
        //   151: aload           $continuation
        //   153: aload           searchResultsWithinChapter
        //   155: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$3:Ljava/lang/Object;
        //   158: aload           $continuation
        //   160: aload           chapterContent
        //   162: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$4:Ljava/lang/Object;
        //   165: aload           $continuation
        //   167: iconst_1       
        //   168: putfield        com/htmake/reader/api/controller/BookController$searchChapter$1.label:I
        //   171: invokespecial   com/htmake/reader/api/controller/BookController.searchPosition:(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;
        //   174: dup            
        //   175: aload           22
        //   177: if_acmpne       237
        //   180: aload           22
        //   182: areturn        
        //   183: aload           $continuation
        //   185: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$4:Ljava/lang/Object;
        //   188: checkcast       Ljava/lang/String;
        //   191: astore          chapterContent
        //   193: aload           $continuation
        //   195: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$3:Ljava/lang/Object;
        //   198: checkcast       Ljava/util/List;
        //   201: astore          searchResultsWithinChapter
        //   203: aload           $continuation
        //   205: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$2:Ljava/lang/Object;
        //   208: checkcast       Ljava/lang/String;
        //   211: astore_3       
        //   212: aload           $continuation
        //   214: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$1:Ljava/lang/Object;
        //   217: checkcast       Lio/legado/app/data/entities/BookChapter;
        //   220: astore_2       
        //   221: aload           $continuation
        //   223: getfield        com/htmake/reader/api/controller/BookController$searchChapter$1.L$0:Ljava/lang/Object;
        //   226: checkcast       Lcom/htmake/reader/api/controller/BookController;
        //   229: astore_0       
        //   230: aload           $result
        //   232: invokestatic    kotlin/ResultKt.throwOnFailure:(Ljava/lang/Object;)V
        //   235: aload           $result
        //   237: checkcast       Ljava/util/List;
        //   240: astore          positions
        //   242: invokestatic    com/htmake/reader/api/controller/BookControllerKt.access$getLogger$p:()Lmu/KLogger;
        //   245: ldc_w           "positions: {}"
        //   248: aload           positions
        //   250: invokeinterface mu/KLogger.info:(Ljava/lang/String;Ljava/lang/Object;)V
        //   255: aload           positions
        //   257: checkcast       Ljava/lang/Iterable;
        //   260: astore          $this$forEachIndexed$iv
        //   262: iconst_0       
        //   263: istore          $i$f$forEachIndexed
        //   265: iconst_0       
        //   266: istore          index$iv
        //   268: aload           $this$forEachIndexed$iv
        //   270: invokeinterface java/lang/Iterable.iterator:()Ljava/util/Iterator;
        //   275: astore          11
        //   277: aload           11
        //   279: invokeinterface java/util/Iterator.hasNext:()Z
        //   284: ifeq            413
        //   287: aload           11
        //   289: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   294: astore          item$iv
        //   296: iload           index$iv
        //   298: iinc            index$iv, 1
        //   301: istore          13
        //   303: iconst_0       
        //   304: istore          14
        //   306: iload           13
        //   308: ifge            314
        //   311: invokestatic    kotlin/collections/CollectionsKt.throwIndexOverflow:()V
        //   314: iload           13
        //   316: invokestatic    kotlin/coroutines/jvm/internal/Boxing.boxInt:(I)Ljava/lang/Integer;
        //   319: aload           item$iv
        //   321: checkcast       Ljava/lang/Number;
        //   324: invokevirtual   java/lang/Number.intValue:()I
        //   327: istore          15
        //   329: checkcast       Ljava/lang/Number;
        //   332: invokevirtual   java/lang/Number.intValue:()I
        //   335: istore          index
        //   337: iconst_0       
        //   338: istore          $i$a$-forEachIndexed-BookController$searchChapter$2
        //   340: aload_0        
        //   341: aload           chapterContent
        //   343: iload           position
        //   345: aload_3        
        //   346: invokespecial   com/htmake/reader/api/controller/BookController.getResultAndQueryIndex:(Ljava/lang/String;ILjava/lang/String;)Lkotlin/Pair;
        //   349: astore          construct
        //   351: new             Lio/legado/app/data/entities/SearchResult;
        //   354: dup            
        //   355: iconst_0       
        //   356: iload           index
        //   358: aload           construct
        //   360: invokevirtual   kotlin/Pair.getSecond:()Ljava/lang/Object;
        //   363: checkcast       Ljava/lang/String;
        //   366: aload_2        
        //   367: invokevirtual   io/legado/app/data/entities/BookChapter.getTitle:()Ljava/lang/String;
        //   370: aload_3        
        //   371: iconst_0       
        //   372: aload_2        
        //   373: invokevirtual   io/legado/app/data/entities/BookChapter.getIndex:()I
        //   376: iconst_0       
        //   377: aload           construct
        //   379: invokevirtual   kotlin/Pair.getFirst:()Ljava/lang/Object;
        //   382: checkcast       Ljava/lang/Number;
        //   385: invokevirtual   java/lang/Number.intValue:()I
        //   388: iload           position
        //   390: sipush          161
        //   393: aconst_null    
        //   394: invokespecial   io/legado/app/data/entities/SearchResult.<init>:(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIIIILkotlin/jvm/internal/DefaultConstructorMarker;)V
        //   397: astore          result
        //   399: aload           searchResultsWithinChapter
        //   401: aload           result
        //   403: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   408: pop            
        //   409: nop            
        //   410: goto            277
        //   413: nop            
        //   414: aload           searchResultsWithinChapter
        //   416: areturn        
        //   417: new             Ljava/lang/IllegalStateException;
        //   420: dup            
        //   421: ldc_w           "call to 'resume' before 'invoke' with coroutine"
        //   424: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //   427: athrow         
        //    Signature:
        //  (Lio/legado/app/data/entities/Book;Lio/legado/app/data/entities/BookChapter;Ljava/lang/String;Lkotlin/coroutines/Continuation<-Ljava/util/List<Lio/legado/app/data/entities/SearchResult;>;>;)Ljava/lang/Object;
        //    MethodParameters:
        //  Name         Flags  
        //  -----------  -----
        //  book         
        //  chapter      
        //  query        
        //  $completion  
        //    StackMapTable: 00 0A 29 FF 00 0B 00 16 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 0F 7E 00 00 FF 00 26 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00 FB 00 5A FF 00 35 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 07 01 A9 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 01 07 01 13 FF 00 27 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 07 01 A9 07 00 60 07 01 A9 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00 FF 00 24 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 07 01 A9 07 00 60 07 01 A9 07 02 56 01 01 07 02 5C 07 01 13 01 01 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00 FF 00 62 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 07 01 A9 07 00 60 07 01 A9 07 02 56 01 01 07 02 5C 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00 FF 00 00 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 07 01 A9 07 00 60 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00 FF 00 02 00 17 07 00 02 07 01 84 07 03 FC 07 00 60 07 01 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 07 01 13 07 0F 7E 07 01 13 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException: Cannot read field "references" because "newVariable" is null
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2945)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
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
    
    private final Object searchPosition(final String mContent, final String pattern, final Continuation<? super List<Integer>> $completion) {
        final List position = new ArrayList();
        int index = StringsKt.indexOf$default((CharSequence)mContent, pattern, 0, false, 6, (Object)null);
        if (index >= 0) {
            while (index >= 0) {
                position.add(Boxing.boxInt(index));
                index = StringsKt.indexOf$default((CharSequence)mContent, pattern, index + 1, false, 4, (Object)null);
            }
        }
        return position;
    }
    
    private final Pair<Integer, String> getResultAndQueryIndex(final String content, final int queryIndexInContent, final String query) {
        final int length = 20;
        int po1 = queryIndexInContent - length;
        int po2 = queryIndexInContent + query.length() + length;
        if (po1 < 0) {
            po1 = 0;
        }
        if (po2 > content.length()) {
            po2 = content.length();
        }
        final int queryIndexInResult = queryIndexInContent - po1;
        if (content == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = content.substring(po1, po2);
        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        final String newText = substring;
        return (Pair<Integer, String>)TuplesKt.to((Object)queryIndexInResult, (Object)newText);
    }
    
    @Nullable
    public final Object backupToMongodb(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$backupToMongodb.BookController$backupToMongodb$1) {
                final BookController$backupToMongodb.BookController$backupToMongodb$1 bookController$backupToMongodb$1 = (BookController$backupToMongodb.BookController$backupToMongodb$1)$completion;
                if ((bookController$backupToMongodb$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$backupToMongodb.BookController$backupToMongodb$1 bookController$backupToMongodb$2 = bookController$backupToMongodb$1;
                    bookController$backupToMongodb$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$backupToMongodb.BookController$backupToMongodb$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$0 = this;
                ((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$1 = context;
                ((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$2 = returnData;
                ((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$2;
                context = (RoutingContext)((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$1;
                this = (BookController)((BookController$backupToMongodb.BookController$backupToMongodb$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        if (!MongoManager.INSTANCE.isInit()) {
            return returnData.setErrorMsg("\u8bf7\u5148\u8bbe\u7f6e mongoUri");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final String[] backupFileNames = this.getBackupFileNames();
        final ArrayList syncDataFileList = CollectionsKt.arrayListOf((Object[])Arrays.copyOf(backupFileNames, backupFileNames.length));
        final Function1 handler = (Function1)new BookController$backupToMongodb$handler.BookController$backupToMongodb$handler$1(syncDataFileList, this);
        handler.invoke((Object)"default");
        if (this.getAppConfig().getSecure()) {
            Map userMap = new LinkedHashMap();
            final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
            if (userMapJson != null) {
                final Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap((Object)map);
            }
            final Map $this$forEach$iv = userMap;
            final int $i$f$forEach = 0;
            for (final Map.Entry it : $this$forEach$iv.entrySet()) {
                final Map.Entry element$iv = it;
                final int n = 0;
                try {
                    final String s = it.getValue().getOrDefault("username", "");
                    final String ns = (s == null) ? "" : s;
                    if (ns.length() <= 0) {
                        continue;
                    }
                    handler.invoke((Object)ns);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final String storage$default = ExtKt.getStorage$default(new String[] { "users" }, null, 2, null);
        if (storage$default != null) {
            final String content = storage$default;
            final int n2 = 0;
            ExtKt.saveStorage$default(new String[] { "users" }, content, false, null, 12, null);
        }
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    @Nullable
    public final Object restoreFromMongodb(@NotNull RoutingContext context, @NotNull final Continuation<? super ReturnData> $completion) {
        final Continuation $continuation;
        Label_0050: {
            if ($completion instanceof BookController$restoreFromMongodb.BookController$restoreFromMongodb$1) {
                final BookController$restoreFromMongodb.BookController$restoreFromMongodb$1 bookController$restoreFromMongodb$1 = (BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$completion;
                if ((bookController$restoreFromMongodb$1.label & Integer.MIN_VALUE) != 0x0) {
                    final BookController$restoreFromMongodb.BookController$restoreFromMongodb$1 bookController$restoreFromMongodb$2 = bookController$restoreFromMongodb$1;
                    bookController$restoreFromMongodb$2.label -= Integer.MIN_VALUE;
                    break Label_0050;
                }
            }
            $continuation = (Continuation)new BookController$restoreFromMongodb.BookController$restoreFromMongodb$1(this, (Continuation)$completion);
        }
        final Object $result = ((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).result;
        final Object coroutine_SUSPENDED = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        ReturnData returnData = null;
        Object checkAuth = null;
        switch (((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).label) {
            case 0: {
                ResultKt.throwOnFailure($result);
                returnData = new ReturnData();
                final BookController bookController = this;
                final RoutingContext context2 = context;
                final Continuation $completion2 = $continuation;
                ((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$0 = this;
                ((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$1 = context;
                ((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$2 = returnData;
                ((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).label = 1;
                if ((checkAuth = bookController.checkAuth(context2, (Continuation<? super Boolean>)$completion2)) == coroutine_SUSPENDED) {
                    return coroutine_SUSPENDED;
                }
                break;
            }
            case 1: {
                returnData = (ReturnData)((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$2;
                context = (RoutingContext)((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$1;
                this = (BookController)((BookController$restoreFromMongodb.BookController$restoreFromMongodb$1)$continuation).L$0;
                ResultKt.throwOnFailure($result);
                checkAuth = $result;
                break;
            }
            default: {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
        if (!(boolean)checkAuth) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("\u8bf7\u767b\u5f55\u540e\u4f7f\u7528");
        }
        if (!MongoManager.INSTANCE.isInit()) {
            return returnData.setErrorMsg("\u8bf7\u5148\u8bbe\u7f6e mongoUri");
        }
        if (!this.checkManagerAuth(context)) {
            return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("\u8bf7\u8f93\u5165\u7ba1\u7406\u5bc6\u7801");
        }
        final String[] backupFileNames = this.getBackupFileNames();
        final ArrayList syncDataFileList = CollectionsKt.arrayListOf((Object[])Arrays.copyOf(backupFileNames, backupFileNames.length));
        final Function1 handler = (Function1)new BookController$restoreFromMongodb$handler.BookController$restoreFromMongodb$handler$1(syncDataFileList);
        handler.invoke((Object)"default");
        if (this.getAppConfig().getSecure()) {
            Map userMap = new LinkedHashMap();
            final JsonObject userMapJson = ExtKt.asJsonObject(ExtKt.getStorage$default(new String[] { "data", "users" }, null, 2, null));
            if (userMapJson != null) {
                final Map map = userMapJson.getMap();
                if (map == null) {
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>");
                }
                userMap = TypeIntrinsics.asMutableMap((Object)map);
            }
            final Map $this$forEach$iv = userMap;
            final int $i$f$forEach = 0;
            for (final Map.Entry it : $this$forEach$iv.entrySet()) {
                final Map.Entry element$iv = it;
                final int n = 0;
                try {
                    final String s = it.getValue().getOrDefault("username", "");
                    final String ns = (s == null) ? "" : s;
                    if (ns.length() <= 0) {
                        continue;
                    }
                    handler.invoke((Object)ns);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final File usersFile = new File(ExtKt.getWorkDir("storage", "users.json"));
        if (usersFile.exists()) {
            usersFile.delete();
            ExtKt.getStorage$default(new String[] { "users" }, null, 2, null);
        }
        return ReturnData.setData$default(returnData, "", null, 2, null);
    }
    
    private static final void searchBookMulti$lambda-5(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 searchBookMulti");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void searchBookMultiSSE$lambda-6(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 searchBookMultiSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void searchBookSource$lambda-7(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 searchBookSource");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void searchBookSourceSSE$lambda-8(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 searchBookSourceSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void bookSourceDebugSSE$lambda-18(final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 bookSourceDebugSSE");
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void cacheBookSSE$lambda-19(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 cacheBookSSE");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
    
    private static final void searchBookContent$lambda-30(final Ref$BooleanRef $isEnd, final BookController this$0, final Void it) {
        Intrinsics.checkNotNullParameter((Object)$isEnd, "$isEnd");
        Intrinsics.checkNotNullParameter((Object)this$0, "this$0");
        BookControllerKt.access$getLogger$p().info("\u5ba2\u6237\u7aef\u5df2\u65ad\u5f00\u94fe\u63a5\uff0c\u505c\u6b62 searchBookContent");
        $isEnd.element = true;
        JobKt.cancel$default(this$0.getCoroutineContext(), (CancellationException)null, 1, (Object)null);
    }
}
