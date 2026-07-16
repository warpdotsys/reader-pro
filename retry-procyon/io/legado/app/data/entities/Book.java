// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Iterator;
import java.util.ArrayList;
import kotlin.Result$Companion;
import kotlin.ResultKt;
import io.legado.app.utils.JsonExtensionsKt;
import com.jayway.jsonpath.ReadContext;
import com.jayway.jsonpath.Predicate;
import kotlin.Result;
import com.jayway.jsonpath.DocumentContext;
import kotlin.Pair;
import io.legado.app.model.localBook.LocalBook;
import java.util.List;
import io.legado.app.model.localBook.CbzFile;
import io.legado.app.model.localBook.UmdFile;
import io.legado.app.model.localBook.EpubFile;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import kotlin.io.FilesKt;
import io.legado.app.utils.FileUtils;
import java.io.File;
import io.legado.app.utils.MD5Utils;
import java.nio.charset.Charset;
import io.legado.app.constant.AppPattern;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.Map;
import java.util.HashMap;
import kotlin.text.StringsKt;
import com.fasterxml.jackson.annotation.JsonProperty;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Lazy;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "variableMap", "infoHtml", "tocHtml", "config", "rootDir", "localBook", "epub", "epubRootDir", "onLineTxt", "localTxt", "umd", "realAuthor", "unreadChapterNum", "folderName", "pdfImageWidth", "localFile", "kindList", "_userNameSpace", "bookDir", "userNameSpace" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\bU\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b+\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\b\u0087\b\u0018\u0000 \u00d5\u00012\u00020\u0001:\u0006\u00d5\u0001\u00d6\u0001\u00d7\u0001B\u00e9\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0011\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0013\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001f\u0012\b\b\u0002\u0010 \u001a\u00020\u0011\u0012\b\b\u0002\u0010!\u001a\u00020\u0011\u0012\b\b\u0002\u0010\"\u001a\u00020\u001f\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%\u0012\b\b\u0002\u0010&\u001a\u00020\u001f\u0012\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010(J\n\u0010\u0083\u0001\u001a\u00020\u0003H\u00c6\u0003J\f\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u0088\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u0089\u0001\u001a\u00020\u0013H\u00c6\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u008b\u0001\u001a\u00020\u0013H\u00c6\u0003J\n\u0010\u008c\u0001\u001a\u00020\u0013H\u00c6\u0003J\n\u0010\u008d\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u008e\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u008f\u0001\u001a\u00020\u0011H\u00c6\u0003J\f\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u0091\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u0092\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u0093\u0001\u001a\u00020\u0013H\u00c6\u0003J\f\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u0095\u0001\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0096\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u0097\u0001\u001a\u00020\u0011H\u00c6\u0003J\n\u0010\u0098\u0001\u001a\u00020\u001fH\u00c6\u0003J\n\u0010\u0099\u0001\u001a\u00020\u0003H\u00c6\u0003J\f\u0010\u009a\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010\u009b\u0001\u001a\u0004\u0018\u00010%H\u00c6\u0003J\n\u0010\u009c\u0001\u001a\u00020\u001fH\u00c6\u0003J\f\u0010\u009d\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\n\u0010\u009e\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010\u009f\u0001\u001a\u00020\u0003H\u00c6\u0003J\n\u0010?\u0001\u001a\u00020\u0003H\u00c6\u0003J\f\u0010?\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010?\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\f\u0010?\u0001\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010¡è\u0001\u001a\u00020%H\u0002J\u00ee\u0002\u0010?\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0018\u001a\u00020\u00112\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00112\b\b\u0002\u0010\u001b\u001a\u00020\u00112\b\b\u0002\u0010\u001c\u001a\u00020\u00132\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020\u00112\b\b\u0002\u0010!\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020\u001f2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%2\b\b\u0002\u0010&\u001a\u00020\u001f2\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0016\u0010?\u0001\u001a\u00020\u001f2\n\u0010¡ì\u0001\u001a\u0005\u0018\u00010¡§\u0001H\u0096\u0002J\b\u0010?\u0001\u001a\u00030?\u0001J\u0007\u0010?\u0001\u001a\u00020\u0003J\u0010\u0010?\u0001\u001a\u00020\u001f2\u0007\u0010\u00ad\u0001\u001a\u00020\u0013J\t\u0010?\u0001\u001a\u0004\u0018\u00010\u0003J\t\u0010?\u0001\u001a\u0004\u0018\u00010\u0003J\u0007\u0010¡ã\u0001\u001a\u00020\u0003J\u0007\u0010¡À\u0001\u001a\u00020\u0003J\b\u0010?\u0001\u001a\u00030?\u0001J\b\u0010?\u0001\u001a\u00030?\u0001J\u0007\u0010?\u0001\u001a\u00020\u0003J\u0007\u0010¡¤\u0001\u001a\u00020\u001fJ\u0007\u0010?\u0001\u001a\u00020\u0011J\t\u0010?\u0001\u001a\u00020\u0003H\u0016J\t\u0010?\u0001\u001a\u00020\u0011H\u0016J\u0007\u0010?\u0001\u001a\u00020\u001fJ\u0007\u0010?\u0001\u001a\u00020\u001fJ\u0007\u0010?\u0001\u001a\u00020\u001fJ\u0007\u0010?\u0001\u001a\u00020\u001fJ\u0007\u0010?\u0001\u001a\u00020\u001fJ\u0007\u0010\u00c0\u0001\u001a\u00020\u001fJ\u0007\u0010\u00c1\u0001\u001a\u00020\u001fJ\u0007\u0010\u00c2\u0001\u001a\u00020\u001fJ\u0007\u0010\u00c3\u0001\u001a\u00020\u001fJ\u001e\u0010\u00c4\u0001\u001a\u00030\u00c5\u00012\u0007\u0010\u00c6\u0001\u001a\u00020\u00032\t\u0010\u00c7\u0001\u001a\u0004\u0018\u00010\u0003H\u0016J\u0011\u0010\u00c8\u0001\u001a\u00030\u00c5\u00012\u0007\u0010\u00ad\u0001\u001a\u00020\u0013J\u0012\u0010\u00c9\u0001\u001a\u00030\u00c5\u00012\b\u0010\u00ca\u0001\u001a\u00030?\u0001J\u0011\u0010\u00cb\u0001\u001a\u00030\u00c5\u00012\u0007\u0010\u00cc\u0001\u001a\u00020\u0003J\u0011\u0010\u00cd\u0001\u001a\u00030\u00c5\u00012\u0007\u0010\u00ce\u0001\u001a\u00020\u0003J\b\u0010\u00cf\u0001\u001a\u00030\u00d0\u0001J\n\u0010\u00d1\u0001\u001a\u00020\u0003H\u00d6\u0001J\u0013\u0010\u00d2\u0001\u001a\u00030\u00c5\u00012\t\b\u0002\u0010\u00d3\u0001\u001a\u00020\u001fJ\u0007\u0010\u00d4\u0001\u001a\u00020\u0003R\u000e\u0010)\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u001a\u0010\u0002\u001a\u00020\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b.\u0010+\"\u0004\b/\u0010-R\u001a\u0010\u001e\u001a\u00020\u001fX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b4\u0010+\"\u0004\b5\u0010-R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b6\u0010+\"\u0004\b7\u0010-R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b8\u0010+\"\u0004\b9\u0010-R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b:\u0010+\"\u0004\b;\u0010-R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b<\u0010+\"\u0004\b=\u0010-R\u001a\u0010\u001a\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u001a\u0010\u001b\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bB\u0010?\"\u0004\bC\u0010AR\u001a\u0010\u001c\u001a\u00020\u0013X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bD\u0010E\"\u0004\bF\u0010GR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bH\u0010+\"\u0004\bI\u0010-R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010E\"\u0004\bK\u0010GR\u001c\u0010L\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bM\u0010+\"\u0004\bN\u0010-R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bO\u0010+\"\u0004\bP\u0010-R\u001c\u0010&\u001a\u00020\u001f8\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b&\u00101\"\u0004\bQ\u00103R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bR\u0010+\"\u0004\bS\u0010-R\u001a\u0010\u0017\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bT\u0010?\"\u0004\bU\u0010AR\u001c\u0010'\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bV\u0010+\"\u0004\bW\u0010-R\u001a\u0010\u0016\u001a\u00020\u0013X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bX\u0010E\"\u0004\bY\u0010GR\u001a\u0010\u0015\u001a\u00020\u0013X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010E\"\u0004\b[\u0010GR\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010+\"\u0004\b]\u0010-R\u001a\u0010\u0007\u001a\u00020\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b^\u0010+\"\u0004\b_\u0010-R\u001a\u0010 \u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b`\u0010?\"\u0004\ba\u0010AR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bb\u0010+\"\u0004\bc\u0010-R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bd\u0010+\"\u0004\be\u0010-R\u001a\u0010!\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bf\u0010?\"\u0004\bg\u0010AR\u001c\u0010$\u001a\u0004\u0018\u00010%X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bh\u0010i\"\u0004\bj\u0010kR\u000e\u0010l\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010m\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bn\u0010+\"\u0004\bo\u0010-R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bp\u0010+\"\u0004\bq\u0010-R\u001a\u0010\u0018\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\br\u0010?\"\u0004\bs\u0010AR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bt\u0010?\"\u0004\bu\u0010AR\u001a\u0010\"\u001a\u00020\u001fX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bv\u00101\"\u0004\bw\u00103R\u001c\u0010#\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bx\u0010+\"\u0004\by\u0010-R8\u0010z\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030{j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`|8VX\u0096\u0084\u0002?\u0006\r\n\u0005\b\u007f\u0010\u0080\u0001\u001a\u0004\b}\u0010~R\u001e\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u0010\n\u0000\u001a\u0005\b\u0081\u0001\u0010+\"\u0005\b\u0082\u0001\u0010-¡§\u0006\u00d8\u0001" }, d2 = { "Lio/legado/app/data/entities/Book;", "Lio/legado/app/data/entities/BaseBook;", "bookUrl", "", "tocUrl", "origin", "originName", "name", "author", "kind", "customTag", "coverUrl", "customCoverUrl", "intro", "customIntro", "charset", "type", "", "group", "", "latestChapterTitle", "latestChapterTime", "lastCheckTime", "lastCheckCount", "totalChapterNum", "durChapterTitle", "durChapterIndex", "durChapterPos", "durChapterTime", "wordCount", "canUpdate", "", "order", "originOrder", "useReplaceRule", "variable", "readConfig", "Lio/legado/app/data/entities/Book$ReadConfig;", "isInShelf", "lastCheckError", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IJLjava/lang/String;JJIILjava/lang/String;IIJLjava/lang/String;ZIIZLjava/lang/String;Lio/legado/app/data/entities/Book$ReadConfig;ZLjava/lang/String;)V", "_userNameSpace", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getCanUpdate", "()Z", "setCanUpdate", "(Z)V", "getCharset", "setCharset", "getCoverUrl", "setCoverUrl", "getCustomCoverUrl", "setCustomCoverUrl", "getCustomIntro", "setCustomIntro", "getCustomTag", "setCustomTag", "getDurChapterIndex", "()I", "setDurChapterIndex", "(I)V", "getDurChapterPos", "setDurChapterPos", "getDurChapterTime", "()J", "setDurChapterTime", "(J)V", "getDurChapterTitle", "setDurChapterTitle", "getGroup", "setGroup", "infoHtml", "getInfoHtml", "setInfoHtml", "getIntro", "setIntro", "setInShelf", "getKind", "setKind", "getLastCheckCount", "setLastCheckCount", "getLastCheckError", "setLastCheckError", "getLastCheckTime", "setLastCheckTime", "getLatestChapterTime", "setLatestChapterTime", "getLatestChapterTitle", "setLatestChapterTitle", "getName", "setName", "getOrder", "setOrder", "getOrigin", "setOrigin", "getOriginName", "setOriginName", "getOriginOrder", "setOriginOrder", "getReadConfig", "()Lio/legado/app/data/entities/Book$ReadConfig;", "setReadConfig", "(Lio/legado/app/data/entities/Book$ReadConfig;)V", "rootDir", "tocHtml", "getTocHtml", "setTocHtml", "getTocUrl", "setTocUrl", "getTotalChapterNum", "setTotalChapterNum", "getType", "setType", "getUseReplaceRule", "setUseReplaceRule", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "getWordCount", "setWordCount", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component4", "component5", "component6", "component7", "component8", "component9", "config", "copy", "equals", "other", "", "fileCharset", "Ljava/nio/charset/Charset;", "getBookDir", "getDelTag", "tag", "getDisplayCover", "getDisplayIntro", "getEpubRootDir", "getFolderName", "getLocalFile", "Ljava/io/File;", "getPdfImageWidth", "", "getRealAuthor", "getSplitLongChapter", "getUnreadChapterNum", "getUserNameSpace", "hashCode", "isCbz", "isEpub", "isLocalBook", "isLocalEpub", "isLocalPdf", "isLocalTxt", "isOnLineTxt", "isPdf", "isUmd", "putVariable", "", "key", "value", "setDelTag", "setPdfImageWidth", "pdfImageWidth", "setRootDir", "root", "setUserNameSpace", "nameSpace", "toSearchBook", "Lio/legado/app/data/entities/SearchBook;", "toString", "updateFromLocal", "onlyCover", "workRoot", "Companion", "Converters", "ReadConfig", "reader-pro" })
public final class Book implements BaseBook
{
    @NotNull
    public static final Companion Companion;
    @NotNull
    private String bookUrl;
    @NotNull
    private String tocUrl;
    @NotNull
    private String origin;
    @NotNull
    private String originName;
    @NotNull
    private String name;
    @NotNull
    private String author;
    @Nullable
    private String kind;
    @Nullable
    private String customTag;
    @Nullable
    private String coverUrl;
    @Nullable
    private String customCoverUrl;
    @Nullable
    private String intro;
    @Nullable
    private String customIntro;
    @Nullable
    private String charset;
    private int type;
    private long group;
    @Nullable
    private String latestChapterTitle;
    private long latestChapterTime;
    private long lastCheckTime;
    private int lastCheckCount;
    private int totalChapterNum;
    @Nullable
    private String durChapterTitle;
    private int durChapterIndex;
    private int durChapterPos;
    private long durChapterTime;
    @Nullable
    private String wordCount;
    private boolean canUpdate;
    private int order;
    private int originOrder;
    private boolean useReplaceRule;
    @Nullable
    private String variable;
    @Nullable
    private ReadConfig readConfig;
    private boolean isInShelf;
    @Nullable
    private String lastCheckError;
    @NotNull
    private final transient Lazy variableMap$delegate;
    @Nullable
    private String infoHtml;
    @Nullable
    private String tocHtml;
    @NotNull
    private transient String rootDir;
    @NotNull
    private transient String _userNameSpace;
    public static final long hTag = 2L;
    public static final long rubyTag = 4L;
    public static final long imgTag = 8L;
    @NotNull
    public static final String imgStyleDefault = "DEFAULT";
    @NotNull
    public static final String imgStyleFull = "FULL";
    @NotNull
    public static final String imgStyleText = "TEXT";
    
    public Book(@NotNull final String bookUrl, @NotNull final String tocUrl, @NotNull final String origin, @NotNull final String originName, @NotNull final String name, @NotNull final String author, @Nullable final String kind, @Nullable final String customTag, @Nullable final String coverUrl, @Nullable final String customCoverUrl, @Nullable final String intro, @Nullable final String customIntro, @Nullable final String charset, final int type, final long group, @Nullable final String latestChapterTitle, final long latestChapterTime, final long lastCheckTime, final int lastCheckCount, final int totalChapterNum, @Nullable final String durChapterTitle, final int durChapterIndex, final int durChapterPos, final long durChapterTime, @Nullable final String wordCount, final boolean canUpdate, final int order, final int originOrder, final boolean useReplaceRule, @Nullable final String variable, @Nullable final ReadConfig readConfig, final boolean isInShelf, @Nullable final String lastCheckError) {
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter((Object)tocUrl, "tocUrl");
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)originName, "originName");
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)author, "author");
        this.bookUrl = bookUrl;
        this.tocUrl = tocUrl;
        this.origin = origin;
        this.originName = originName;
        this.name = name;
        this.author = author;
        this.kind = kind;
        this.customTag = customTag;
        this.coverUrl = coverUrl;
        this.customCoverUrl = customCoverUrl;
        this.intro = intro;
        this.customIntro = customIntro;
        this.charset = charset;
        this.type = type;
        this.group = group;
        this.latestChapterTitle = latestChapterTitle;
        this.latestChapterTime = latestChapterTime;
        this.lastCheckTime = lastCheckTime;
        this.lastCheckCount = lastCheckCount;
        this.totalChapterNum = totalChapterNum;
        this.durChapterTitle = durChapterTitle;
        this.durChapterIndex = durChapterIndex;
        this.durChapterPos = durChapterPos;
        this.durChapterTime = durChapterTime;
        this.wordCount = wordCount;
        this.canUpdate = canUpdate;
        this.order = order;
        this.originOrder = originOrder;
        this.useReplaceRule = useReplaceRule;
        this.variable = variable;
        this.readConfig = readConfig;
        this.isInShelf = isInShelf;
        this.lastCheckError = lastCheckError;
        this.variableMap$delegate = LazyKt.lazy((Function0)new Book$variableMap.Book$variableMap$2(this));
        this.rootDir = "";
        this._userNameSpace = "";
    }
    
    @NotNull
    public String getBookUrl() {
        return this.bookUrl;
    }
    
    public void setBookUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.bookUrl = <set-?>;
    }
    
    @NotNull
    public final String getTocUrl() {
        return this.tocUrl;
    }
    
    public final void setTocUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.tocUrl = <set-?>;
    }
    
    @NotNull
    public final String getOrigin() {
        return this.origin;
    }
    
    public final void setOrigin(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.origin = <set-?>;
    }
    
    @NotNull
    public final String getOriginName() {
        return this.originName;
    }
    
    public final void setOriginName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.originName = <set-?>;
    }
    
    @NotNull
    public String getName() {
        return this.name;
    }
    
    public void setName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.name = <set-?>;
    }
    
    @NotNull
    public String getAuthor() {
        return this.author;
    }
    
    public void setAuthor(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.author = <set-?>;
    }
    
    @Nullable
    public String getKind() {
        return this.kind;
    }
    
    public void setKind(@Nullable final String <set-?>) {
        this.kind = <set-?>;
    }
    
    @Nullable
    public final String getCustomTag() {
        return this.customTag;
    }
    
    public final void setCustomTag(@Nullable final String <set-?>) {
        this.customTag = <set-?>;
    }
    
    @Nullable
    public final String getCoverUrl() {
        return this.coverUrl;
    }
    
    public final void setCoverUrl(@Nullable final String <set-?>) {
        this.coverUrl = <set-?>;
    }
    
    @Nullable
    public final String getCustomCoverUrl() {
        return this.customCoverUrl;
    }
    
    public final void setCustomCoverUrl(@Nullable final String <set-?>) {
        this.customCoverUrl = <set-?>;
    }
    
    @Nullable
    public final String getIntro() {
        return this.intro;
    }
    
    public final void setIntro(@Nullable final String <set-?>) {
        this.intro = <set-?>;
    }
    
    @Nullable
    public final String getCustomIntro() {
        return this.customIntro;
    }
    
    public final void setCustomIntro(@Nullable final String <set-?>) {
        this.customIntro = <set-?>;
    }
    
    @Nullable
    public final String getCharset() {
        return this.charset;
    }
    
    public final void setCharset(@Nullable final String <set-?>) {
        this.charset = <set-?>;
    }
    
    public final int getType() {
        return this.type;
    }
    
    public final void setType(final int <set-?>) {
        this.type = <set-?>;
    }
    
    public final long getGroup() {
        return this.group;
    }
    
    public final void setGroup(final long <set-?>) {
        this.group = <set-?>;
    }
    
    @Nullable
    public final String getLatestChapterTitle() {
        return this.latestChapterTitle;
    }
    
    public final void setLatestChapterTitle(@Nullable final String <set-?>) {
        this.latestChapterTitle = <set-?>;
    }
    
    public final long getLatestChapterTime() {
        return this.latestChapterTime;
    }
    
    public final void setLatestChapterTime(final long <set-?>) {
        this.latestChapterTime = <set-?>;
    }
    
    public final long getLastCheckTime() {
        return this.lastCheckTime;
    }
    
    public final void setLastCheckTime(final long <set-?>) {
        this.lastCheckTime = <set-?>;
    }
    
    public final int getLastCheckCount() {
        return this.lastCheckCount;
    }
    
    public final void setLastCheckCount(final int <set-?>) {
        this.lastCheckCount = <set-?>;
    }
    
    public final int getTotalChapterNum() {
        return this.totalChapterNum;
    }
    
    public final void setTotalChapterNum(final int <set-?>) {
        this.totalChapterNum = <set-?>;
    }
    
    @Nullable
    public final String getDurChapterTitle() {
        return this.durChapterTitle;
    }
    
    public final void setDurChapterTitle(@Nullable final String <set-?>) {
        this.durChapterTitle = <set-?>;
    }
    
    public final int getDurChapterIndex() {
        return this.durChapterIndex;
    }
    
    public final void setDurChapterIndex(final int <set-?>) {
        this.durChapterIndex = <set-?>;
    }
    
    public final int getDurChapterPos() {
        return this.durChapterPos;
    }
    
    public final void setDurChapterPos(final int <set-?>) {
        this.durChapterPos = <set-?>;
    }
    
    public final long getDurChapterTime() {
        return this.durChapterTime;
    }
    
    public final void setDurChapterTime(final long <set-?>) {
        this.durChapterTime = <set-?>;
    }
    
    @Nullable
    public String getWordCount() {
        return this.wordCount;
    }
    
    public void setWordCount(@Nullable final String <set-?>) {
        this.wordCount = <set-?>;
    }
    
    public final boolean getCanUpdate() {
        return this.canUpdate;
    }
    
    public final void setCanUpdate(final boolean <set-?>) {
        this.canUpdate = <set-?>;
    }
    
    public final int getOrder() {
        return this.order;
    }
    
    public final void setOrder(final int <set-?>) {
        this.order = <set-?>;
    }
    
    public final int getOriginOrder() {
        return this.originOrder;
    }
    
    public final void setOriginOrder(final int <set-?>) {
        this.originOrder = <set-?>;
    }
    
    public final boolean getUseReplaceRule() {
        return this.useReplaceRule;
    }
    
    public final void setUseReplaceRule(final boolean <set-?>) {
        this.useReplaceRule = <set-?>;
    }
    
    @Nullable
    public final String getVariable() {
        return this.variable;
    }
    
    public final void setVariable(@Nullable final String <set-?>) {
        this.variable = <set-?>;
    }
    
    @Nullable
    public final ReadConfig getReadConfig() {
        return this.readConfig;
    }
    
    public final void setReadConfig(@Nullable final ReadConfig <set-?>) {
        this.readConfig = <set-?>;
    }
    
    @JsonProperty("isInShelf")
    public final boolean isInShelf() {
        return this.isInShelf;
    }
    
    public final void setInShelf(final boolean <set-?>) {
        this.isInShelf = <set-?>;
    }
    
    @Nullable
    public final String getLastCheckError() {
        return this.lastCheckError;
    }
    
    public final void setLastCheckError(@Nullable final String <set-?>) {
        this.lastCheckError = <set-?>;
    }
    
    public final boolean isLocalBook() {
        return Intrinsics.areEqual((Object)this.origin, (Object)"loc_book");
    }
    
    public final boolean isLocalTxt() {
        return this.isLocalBook() && StringsKt.endsWith(this.originName, ".txt", true);
    }
    
    public final boolean isLocalEpub() {
        return this.isLocalBook() && StringsKt.endsWith(this.originName, ".epub", true);
    }
    
    public final boolean isLocalPdf() {
        return this.isLocalBook() && StringsKt.endsWith(this.originName, ".pdf", true);
    }
    
    public final boolean isEpub() {
        return StringsKt.endsWith(this.originName, ".epub", true);
    }
    
    public final boolean isCbz() {
        return StringsKt.endsWith(this.originName, ".cbz", true);
    }
    
    public final boolean isPdf() {
        return StringsKt.endsWith(this.originName, ".pdf", true);
    }
    
    public final boolean isUmd() {
        return StringsKt.endsWith(this.originName, ".umd", true);
    }
    
    public final boolean isOnLineTxt() {
        return !this.isLocalBook() && this.type == 0;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof Book && Intrinsics.areEqual((Object)((Book)other).getBookUrl(), (Object)this.getBookUrl());
    }
    
    @Override
    public int hashCode() {
        return this.getBookUrl().hashCode();
    }
    
    @NotNull
    public HashMap<String, String> getVariableMap() {
        return (HashMap)this.variableMap$delegate.getValue();
    }
    
    public void putVariable(@NotNull final String key, @Nullable final String value) {
        Intrinsics.checkNotNullParameter((Object)key, "key");
        if (value != null) {
            this.getVariableMap().put(key, value);
        }
        else {
            this.getVariableMap().remove(key);
        }
        this.variable = GsonExtensionsKt.getGSON().toJson((Object)this.getVariableMap());
    }
    
    @Nullable
    public String getInfoHtml() {
        return this.infoHtml;
    }
    
    public void setInfoHtml(@Nullable final String <set-?>) {
        this.infoHtml = <set-?>;
    }
    
    @Nullable
    public String getTocHtml() {
        return this.tocHtml;
    }
    
    public void setTocHtml(@Nullable final String <set-?>) {
        this.tocHtml = <set-?>;
    }
    
    @NotNull
    public final String getRealAuthor() {
        return AppPattern.INSTANCE.getAuthorRegex().replace((CharSequence)this.getAuthor(), "");
    }
    
    public final int getUnreadChapterNum() {
        return Math.max(this.totalChapterNum - this.durChapterIndex - 1, 0);
    }
    
    @Nullable
    public final String getDisplayCover() {
        final CharSequence charSequence = this.customCoverUrl;
        return (charSequence == null || charSequence.length() == 0) ? this.coverUrl : this.customCoverUrl;
    }
    
    @Nullable
    public final String getDisplayIntro() {
        final CharSequence charSequence = this.customIntro;
        return (charSequence == null || charSequence.length() == 0) ? this.intro : this.customIntro;
    }
    
    @NotNull
    public final Charset fileCharset() {
        final String charset = this.charset;
        final Charset forName = Charset.forName((charset == null) ? "UTF-8" : charset);
        Intrinsics.checkNotNullExpressionValue((Object)forName, "Charset.forName(charsetName)");
        return forName;
    }
    
    private final ReadConfig config() {
        if (this.readConfig == null) {
            this.readConfig = new ReadConfig(false, 0, false, null, false, 0L, 0.0f, 127, null);
        }
        final ReadConfig readConfig = this.readConfig;
        Intrinsics.checkNotNull((Object)readConfig);
        return readConfig;
    }
    
    public final void setDelTag(final long tag) {
        this.config().setDelTag(((this.config().getDelTag() & tag) == tag) ? (this.config().getDelTag() & ~tag) : (this.config().getDelTag() | tag));
    }
    
    public final boolean getDelTag(final long tag) {
        return (this.config().getDelTag() & tag) == tag;
    }
    
    public final float getPdfImageWidth() {
        return this.config().getPdfImageWidth();
    }
    
    public final void setPdfImageWidth(final float pdfImageWidth) {
        this.config().setPdfImageWidth(pdfImageWidth);
    }
    
    @NotNull
    public final String getFolderName() {
        final String replace;
        String folderName = replace = AppPattern.INSTANCE.getFileNameRegex().replace((CharSequence)this.getName(), "");
        final int beginIndex = 0;
        final int min = Math.min(9, folderName.length());
        final String s = replace;
        if (s == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        final String substring = s.substring(beginIndex, min);
        Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        folderName = substring;
        return Intrinsics.stringPlus(folderName, (Object)MD5Utils.INSTANCE.md5Encode16(this.getBookUrl()));
    }
    
    public final void setRootDir(@NotNull final String root) {
        Intrinsics.checkNotNullParameter((Object)root, "root");
        if (root.length() > 0) {
            final String separator = File.separator;
            Intrinsics.checkNotNullExpressionValue((Object)separator, "separator");
            if (!StringsKt.endsWith$default(root, separator, false, 2, (Object)null)) {
                this.rootDir = Intrinsics.stringPlus(root, (Object)File.separator);
                return;
            }
        }
        this.rootDir = root;
    }
    
    @NotNull
    public final File getLocalFile() {
        if (StringsKt.startsWith$default(this.originName, this.rootDir, false, 2, (Object)null)) {
            this.originName = StringsKt.replaceFirst$default(this.originName, this.rootDir, "", false, 4, (Object)null);
        }
        BookKt.getLogger().info("getLocalFile rootDir: {} originName: {}", (Object)this.rootDir, (Object)this.originName);
        if (this.isEpub() && StringsKt.indexOf$default((CharSequence)this.originName, "localStore", 0, false, 6, (Object)null) < 0 && StringsKt.indexOf$default((CharSequence)this.originName, "webdav", 0, false, 6, (Object)null) < 0) {
            return FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, (Object)this.originName)), "index.epub");
        }
        if (this.isCbz() && StringsKt.indexOf$default((CharSequence)this.originName, "localStore", 0, false, 6, (Object)null) < 0 && StringsKt.indexOf$default((CharSequence)this.originName, "webdav", 0, false, 6, (Object)null) < 0) {
            return FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, (Object)this.originName)), "index.cbz");
        }
        if (this.isPdf() && StringsKt.indexOf$default((CharSequence)this.originName, "localStore", 0, false, 6, (Object)null) < 0 && StringsKt.indexOf$default((CharSequence)this.originName, "webdav", 0, false, 6, (Object)null) < 0) {
            return FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, (Object)this.originName)), "index.pdf");
        }
        return new File(Intrinsics.stringPlus(this.rootDir, (Object)this.originName));
    }
    
    public final void setUserNameSpace(@NotNull final String nameSpace) {
        Intrinsics.checkNotNullParameter((Object)nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }
    
    @NotNull
    public String getUserNameSpace() {
        return this._userNameSpace;
    }
    
    @NotNull
    public final String getBookDir() {
        return FileUtils.INSTANCE.getPath(new File(this.rootDir), "storage", "data", this._userNameSpace, this.getName() + '_' + this.getAuthor());
    }
    
    public final boolean getSplitLongChapter() {
        return false;
    }
    
    @NotNull
    public final SearchBook toSearchBook() {
        final SearchBook $this$toSearchBook_u24lambda_u2d0;
        final SearchBook searchBook = $this$toSearchBook_u24lambda_u2d0 = new SearchBook(this.getBookUrl(), this.origin, this.originName, this.type, this.getName(), this.getAuthor(), this.getKind(), this.coverUrl, this.intro, this.getWordCount(), this.latestChapterTitle, this.tocUrl, 0L, this.variable, 0, 20480, (DefaultConstructorMarker)null);
        final int n = 0;
        $this$toSearchBook_u24lambda_u2d0.setInfoHtml(this.getInfoHtml());
        $this$toSearchBook_u24lambda_u2d0.setTocHtml(this.getTocHtml());
        $this$toSearchBook_u24lambda_u2d0.setUserNameSpace(this.getUserNameSpace());
        return searchBook;
    }
    
    @NotNull
    public final String getEpubRootDir() {
        final String defaultPath = "OEBPS";
        final File containerRes = new File(this.getBookUrl() + (Object)File.separator + "index" + (Object)File.separator + "META-INF" + (Object)File.separator + "container.xml");
        if (containerRes.exists()) {
            try {
                final Document document = Jsoup.parse(FilesKt.readText$default(containerRes, (Charset)null, 1, (Object)null));
                final Element rootFileElement = (Element)((Element)document.getElementsByTag("rootfiles").get(0)).getElementsByTag("rootfile").get(0);
                final String result = rootFileElement.attr("full-path");
                System.out.println(Intrinsics.stringPlus("result: ", (Object)result));
                if (result != null && result.length() > 0) {
                    final File parentFile = new File(result).getParentFile();
                    String s;
                    if (parentFile == null) {
                        s = "";
                    }
                    else {
                        final File it = parentFile;
                        final int n = 0;
                        final String string = it.toString();
                        s = ((string == null) ? "" : string);
                    }
                    return s;
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return defaultPath;
    }
    
    public final void updateFromLocal(final boolean onlyCover) {
        try {
            if (this.isEpub()) {
                EpubFile.Companion.upBookInfo(this, onlyCover);
            }
            else if (this.isUmd()) {
                UmdFile.Companion.upBookInfo(this, onlyCover);
            }
            else if (this.isCbz()) {
                CbzFile.Companion.upBookInfo(this, onlyCover);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static /* synthetic */ void updateFromLocal$default(final Book book, boolean onlyCover, final int n, final Object o) {
        if ((n & 0x1) != 0x0) {
            onlyCover = false;
        }
        book.updateFromLocal(onlyCover);
    }
    
    @NotNull
    public final String workRoot() {
        return this.rootDir;
    }
    
    @NotNull
    public List<String> getKindList() {
        return BaseBook$DefaultImpls.getKindList((BaseBook)this);
    }
    
    @Nullable
    public String getVariable(@NotNull final String key) {
        return BaseBook$DefaultImpls.getVariable((BaseBook)this, key);
    }
    
    @NotNull
    public final String component1() {
        return this.getBookUrl();
    }
    
    @NotNull
    public final String component2() {
        return this.tocUrl;
    }
    
    @NotNull
    public final String component3() {
        return this.origin;
    }
    
    @NotNull
    public final String component4() {
        return this.originName;
    }
    
    @NotNull
    public final String component5() {
        return this.getName();
    }
    
    @NotNull
    public final String component6() {
        return this.getAuthor();
    }
    
    @Nullable
    public final String component7() {
        return this.getKind();
    }
    
    @Nullable
    public final String component8() {
        return this.customTag;
    }
    
    @Nullable
    public final String component9() {
        return this.coverUrl;
    }
    
    @Nullable
    public final String component10() {
        return this.customCoverUrl;
    }
    
    @Nullable
    public final String component11() {
        return this.intro;
    }
    
    @Nullable
    public final String component12() {
        return this.customIntro;
    }
    
    @Nullable
    public final String component13() {
        return this.charset;
    }
    
    public final int component14() {
        return this.type;
    }
    
    public final long component15() {
        return this.group;
    }
    
    @Nullable
    public final String component16() {
        return this.latestChapterTitle;
    }
    
    public final long component17() {
        return this.latestChapterTime;
    }
    
    public final long component18() {
        return this.lastCheckTime;
    }
    
    public final int component19() {
        return this.lastCheckCount;
    }
    
    public final int component20() {
        return this.totalChapterNum;
    }
    
    @Nullable
    public final String component21() {
        return this.durChapterTitle;
    }
    
    public final int component22() {
        return this.durChapterIndex;
    }
    
    public final int component23() {
        return this.durChapterPos;
    }
    
    public final long component24() {
        return this.durChapterTime;
    }
    
    @Nullable
    public final String component25() {
        return this.getWordCount();
    }
    
    public final boolean component26() {
        return this.canUpdate;
    }
    
    public final int component27() {
        return this.order;
    }
    
    public final int component28() {
        return this.originOrder;
    }
    
    public final boolean component29() {
        return this.useReplaceRule;
    }
    
    @Nullable
    public final String component30() {
        return this.variable;
    }
    
    @Nullable
    public final ReadConfig component31() {
        return this.readConfig;
    }
    
    public final boolean component32() {
        return this.isInShelf;
    }
    
    @Nullable
    public final String component33() {
        return this.lastCheckError;
    }
    
    @NotNull
    public final Book copy(@NotNull final String bookUrl, @NotNull final String tocUrl, @NotNull final String origin, @NotNull final String originName, @NotNull final String name, @NotNull final String author, @Nullable final String kind, @Nullable final String customTag, @Nullable final String coverUrl, @Nullable final String customCoverUrl, @Nullable final String intro, @Nullable final String customIntro, @Nullable final String charset, final int type, final long group, @Nullable final String latestChapterTitle, final long latestChapterTime, final long lastCheckTime, final int lastCheckCount, final int totalChapterNum, @Nullable final String durChapterTitle, final int durChapterIndex, final int durChapterPos, final long durChapterTime, @Nullable final String wordCount, final boolean canUpdate, final int order, final int originOrder, final boolean useReplaceRule, @Nullable final String variable, @Nullable final ReadConfig readConfig, final boolean isInShelf, @Nullable final String lastCheckError) {
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter((Object)tocUrl, "tocUrl");
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)originName, "originName");
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)author, "author");
        return new Book(bookUrl, tocUrl, origin, originName, name, author, kind, customTag, coverUrl, customCoverUrl, intro, customIntro, charset, type, group, latestChapterTitle, latestChapterTime, lastCheckTime, lastCheckCount, totalChapterNum, durChapterTitle, durChapterIndex, durChapterPos, durChapterTime, wordCount, canUpdate, order, originOrder, useReplaceRule, variable, readConfig, isInShelf, lastCheckError);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Book(bookUrl=").append(this.getBookUrl()).append(", tocUrl=").append(this.tocUrl).append(", origin=").append(this.origin).append(", originName=").append(this.originName).append(", name=").append(this.getName()).append(", author=").append(this.getAuthor()).append(", kind=").append((Object)this.getKind()).append(", customTag=").append((Object)this.customTag).append(", coverUrl=").append((Object)this.coverUrl).append(", customCoverUrl=").append((Object)this.customCoverUrl).append(", intro=").append((Object)this.intro).append(", customIntro=");
        sb.append((Object)this.customIntro).append(", charset=").append((Object)this.charset).append(", type=").append(this.type).append(", group=").append(this.group).append(", latestChapterTitle=").append((Object)this.latestChapterTitle).append(", latestChapterTime=").append(this.latestChapterTime).append(", lastCheckTime=").append(this.lastCheckTime).append(", lastCheckCount=").append(this.lastCheckCount).append(", totalChapterNum=").append(this.totalChapterNum).append(", durChapterTitle=").append((Object)this.durChapterTitle).append(", durChapterIndex=").append(this.durChapterIndex).append(", durChapterPos=").append(this.durChapterPos);
        sb.append(", durChapterTime=").append(this.durChapterTime).append(", wordCount=").append((Object)this.getWordCount()).append(", canUpdate=").append(this.canUpdate).append(", order=").append(this.order).append(", originOrder=").append(this.originOrder).append(", useReplaceRule=").append(this.useReplaceRule).append(", variable=").append((Object)this.variable).append(", readConfig=").append(this.readConfig).append(", isInShelf=").append(this.isInShelf).append(", lastCheckError=").append((Object)this.lastCheckError).append(')');
        return sb.toString();
    }
    
    public Book() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
    }
    
    static {
        Companion = new Companion(null);
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J$\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u0006\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u000f\u0010\u0010J4\u0010\u0011\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\r0\u0012j\b\u0012\u0004\u0012\u00020\r`\u00130\f2\u0006\u0010\u0014\u001a\u00020\u0006\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0015\u0010\u0010J$\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u0017\u001a\u00020\u0018\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\u0019\u0010\u001aJ \u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u00062\b\b\u0002\u0010\u001e\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T?\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006\u001f" }, d2 = { "Lio/legado/app/data/entities/Book$Companion;", "", "()V", "hTag", "", "imgStyleDefault", "", "imgStyleFull", "imgStyleText", "imgTag", "rubyTag", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/Book;", "json", "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "initLocalBook", "bookUrl", "localPath", "rootDir", "reader-pro" })
    public static final class Companion
    {
        private Companion() {
        }
        
        @NotNull
        public final Book initLocalBook(@NotNull final String bookUrl, @NotNull final String localPath, @NotNull final String rootDir) {
            Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
            Intrinsics.checkNotNullParameter((Object)localPath, "localPath");
            Intrinsics.checkNotNullParameter((Object)rootDir, "rootDir");
            final String fileName = new File(localPath).getName();
            final LocalBook instance = LocalBook.INSTANCE;
            Intrinsics.checkNotNullExpressionValue((Object)fileName, "fileName");
            final Pair nameAuthor = instance.analyzeNameAuthor(fileName);
            final Book it = new Book(bookUrl, "", "loc_book", localPath, (String)nameAuthor.getFirst(), (String)nameAuthor.getSecond(), null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -64, 1, null);
            final int n = 0;
            it.setCanUpdate(false);
            final Book book = it;
            book.setRootDir(rootDir);
            Book.updateFromLocal$default(book, false, 1, null);
            return book;
        }
        
        @NotNull
        public final Object fromJsonDoc-IoAF18A(@NotNull final DocumentContext doc) {
            Intrinsics.checkNotNullParameter((Object)doc, "doc");
            Object o3;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Object readConfig = doc.read("$.readConfig", new Predicate[0]);
                String string;
                final String s = string = JsonExtensionsKt.readString((ReadContext)doc, "$.bookUrl");
                Intrinsics.checkNotNull((Object)s);
                String string2;
                final String s2 = string2 = JsonExtensionsKt.readString((ReadContext)doc, "$.tocUrl");
                Intrinsics.checkNotNull((Object)s2);
                final String string3 = JsonExtensionsKt.readString((ReadContext)doc, "$.origin");
                final String s4;
                String s3 = s4 = ((string3 == null) ? "loc_book" : string3);
                final String string4 = JsonExtensionsKt.readString((ReadContext)doc, "$.originName");
                final String s6;
                String s5 = s6 = ((string4 == null) ? "" : string4);
                String string5;
                final String s7 = string5 = JsonExtensionsKt.readString((ReadContext)doc, "$.name");
                Intrinsics.checkNotNull((Object)s7);
                final String string6 = JsonExtensionsKt.readString((ReadContext)doc, "$.author");
                final String s9;
                String s8 = s9 = ((string6 == null) ? "" : string6);
                final String string7 = JsonExtensionsKt.readString((ReadContext)doc, "$.kind");
                final String string8 = JsonExtensionsKt.readString((ReadContext)doc, "$.customTag");
                final String string9 = JsonExtensionsKt.readString((ReadContext)doc, "$.coverUrl");
                final String string10 = JsonExtensionsKt.readString((ReadContext)doc, "$.customCoverUrl");
                final String string11 = JsonExtensionsKt.readString((ReadContext)doc, "$.intro");
                final String string12 = JsonExtensionsKt.readString((ReadContext)doc, "$.customIntro");
                final String string13 = JsonExtensionsKt.readString((ReadContext)doc, "$.charset");
                final Integer int1 = JsonExtensionsKt.readInt((ReadContext)doc, "$.type");
                final int n3;
                int n2 = n3 = ((int1 == null) ? 0 : int1);
                final Long long1 = JsonExtensionsKt.readLong((ReadContext)doc, "$.group");
                final long n5;
                long n4 = n5 = ((long1 == null) ? 0L : long1);
                final String string14 = JsonExtensionsKt.readString((ReadContext)doc, "$.latestChapterTitle");
                final Long long2 = JsonExtensionsKt.readLong((ReadContext)doc, "$.latestChapterTime");
                final long n7;
                long n6 = n7 = ((long2 == null) ? System.currentTimeMillis() : long2);
                final Long long3 = JsonExtensionsKt.readLong((ReadContext)doc, "$.lastCheckTime");
                final long n9;
                long n8 = n9 = ((long3 == null) ? System.currentTimeMillis() : long3);
                final Integer int2 = JsonExtensionsKt.readInt((ReadContext)doc, "$.lastCheckCount");
                final int n11;
                int n10 = n11 = ((int2 == null) ? 0 : int2);
                final Integer int3 = JsonExtensionsKt.readInt((ReadContext)doc, "$.totalChapterNum");
                final int n13;
                int n12 = n13 = ((int3 == null) ? 0 : int3);
                final String string15 = JsonExtensionsKt.readString((ReadContext)doc, "$.durChapterTitle");
                final Integer int4 = JsonExtensionsKt.readInt((ReadContext)doc, "$.durChapterIndex");
                final int n15;
                int n14 = n15 = ((int4 == null) ? 0 : int4);
                final Integer int5 = JsonExtensionsKt.readInt((ReadContext)doc, "$.durChapterPos");
                final int n17;
                int n16 = n17 = ((int5 == null) ? 0 : int5);
                final Long long4 = JsonExtensionsKt.readLong((ReadContext)doc, "$.durChapterTime");
                final long n19;
                long n18 = n19 = ((long4 == null) ? System.currentTimeMillis() : long4);
                final String string16 = JsonExtensionsKt.readString((ReadContext)doc, "$.wordCount");
                final Boolean bool = JsonExtensionsKt.readBool((ReadContext)doc, "$.canUpdate");
                final boolean b2;
                boolean b = b2 = (((bool == null) ? 1 : ((boolean)bool)) != 0);
                final Integer int6 = JsonExtensionsKt.readInt((ReadContext)doc, "$.order");
                final int n21;
                int n20 = n21 = ((int6 == null) ? 0 : int6);
                final Integer int7 = JsonExtensionsKt.readInt((ReadContext)doc, "$.originOrder");
                final int n23;
                int n22 = n23 = ((int7 == null) ? 0 : int7);
                final Boolean bool2 = JsonExtensionsKt.readBool((ReadContext)doc, "$.useReplaceRule");
                final boolean b4;
                boolean b3 = b4 = (((bool2 == null) ? 1 : ((boolean)bool2)) != 0);
                final String string17 = JsonExtensionsKt.readString((ReadContext)doc, "$.variable");
                ReadConfig readConfig3;
                if (readConfig != null) {
                    final boolean b5 = b4;
                    final int n24 = n23;
                    final int n25 = n21;
                    final boolean b6 = b2;
                    final long n26 = n19;
                    final int n27 = n17;
                    final int n28 = n15;
                    final int n29 = n13;
                    final int n30 = n11;
                    final long n31 = n9;
                    final long n32 = n7;
                    final long n33 = n5;
                    final int n34 = n3;
                    final String s10 = s9;
                    final String s11 = s7;
                    final String s12 = s6;
                    final String s13 = s4;
                    final String s14 = s2;
                    final String s15 = s;
                    Object o;
                    try {
                        final Result$Companion companion2 = Result.Companion;
                        final int n35 = 0;
                        final Boolean bool3 = JsonExtensionsKt.readBool((ReadContext)doc, "$.readConfig.reverseToc");
                        final boolean b7 = bool3 != null && bool3;
                        final Integer int8 = JsonExtensionsKt.readInt((ReadContext)doc, "$.readConfig.pageAnim");
                        final int n36 = (int8 == null) ? -1 : int8;
                        final Boolean bool4 = JsonExtensionsKt.readBool((ReadContext)doc, "$.readConfig.reSegment");
                        final boolean b8 = bool4 != null && bool4;
                        final String string18 = JsonExtensionsKt.readString((ReadContext)doc, "$.readConfig.imageStyle");
                        final Boolean bool5 = JsonExtensionsKt.readBool((ReadContext)doc, "$.readConfig.useReplaceRule");
                        final boolean b9 = bool5 != null && bool5;
                        final Long long5 = JsonExtensionsKt.readLong((ReadContext)doc, "$.readConfig.delTag");
                        o = Result.constructor-impl((Object)new ReadConfig(b7, n36, b8, string18, b9, (long5 == null) ? 0L : long5, 0.0f, 64, null));
                    }
                    catch (final Throwable t) {
                        final Result$Companion companion3 = Result.Companion;
                        o = Result.constructor-impl(ResultKt.createFailure(t));
                    }
                    final Object o2 = o;
                    string = s15;
                    string2 = s14;
                    s3 = s13;
                    s5 = s12;
                    string5 = s11;
                    s8 = s10;
                    n2 = n34;
                    n4 = n33;
                    n6 = n32;
                    n8 = n31;
                    n10 = n30;
                    n12 = n29;
                    n14 = n28;
                    n16 = n27;
                    n18 = n26;
                    b = b6;
                    n20 = n25;
                    n22 = n24;
                    b3 = b5;
                    final ReadConfig readConfig2 = (ReadConfig)o2;
                    readConfig3 = (Result.isFailure-impl((Object)readConfig2) ? null : readConfig2);
                }
                else {
                    readConfig3 = null;
                }
                final Boolean bool6 = JsonExtensionsKt.readBool((ReadContext)doc, "$.isInShelf");
                o3 = Result.constructor-impl((Object)new Book(string, string2, s3, s5, string5, s8, string7, string8, string9, string10, string11, string12, string13, n2, n4, string14, n6, n8, n10, n12, string15, n14, n16, n18, string16, b, n20, n22, b3, string17, readConfig3, bool6 != null && bool6, null, 0, 1, null));
            }
            catch (final Throwable t2) {
                final Result$Companion companion4 = Result.Companion;
                o3 = Result.constructor-impl(ResultKt.createFailure(t2));
            }
            return o3;
        }
        
        @NotNull
        public final Object fromJson-IoAF18A(@NotNull final String json) {
            Intrinsics.checkNotNullParameter((Object)json, "json");
            final DocumentContext parse = JsonExtensionsKt.getJsonPath().parse(json);
            Intrinsics.checkNotNullExpressionValue((Object)parse, "jsonPath.parse(json)");
            return this.fromJsonDoc-IoAF18A(parse);
        }
        
        @NotNull
        public final Object fromJsonArray-IoAF18A(@NotNull final String jsonArray) {
            Intrinsics.checkNotNullParameter((Object)jsonArray, "jsonArray");
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final ArrayList sources = new ArrayList();
                final List doc = (List)JsonExtensionsKt.getJsonPath().parse(jsonArray).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue((Object)doc, "doc");
                final Iterable $this$forEach$iv = doc;
                final int $i$f$forEach = 0;
                for (final Object it : $this$forEach$iv) {
                    final Object element$iv = it;
                    final int n2 = 0;
                    final DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(it);
                    final Companion companion2 = Book.Companion;
                    Intrinsics.checkNotNullExpressionValue((Object)jsonItem, "jsonItem");
                    final Object fromJsonDoc-IoAF18A = companion2.fromJsonDoc-IoAF18A(jsonItem);
                    ResultKt.throwOnFailure(fromJsonDoc-IoAF18A);
                    final Book source = (Book)fromJsonDoc-IoAF18A;
                    final int n3 = 0;
                    sources.add(source);
                }
                o = Result.constructor-impl((Object)sources);
            }
            catch (final Throwable t) {
                final Result$Companion companion3 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            return o;
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b&\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r?\u0006\u0002\u0010\u000eJ\t\u0010'\u001a\u00020\u0003H\u00c6\u0003J\t\u0010(\u001a\u00020\u0005H\u00c6\u0003J\t\u0010)\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\bH\u00c6\u0003J\t\u0010+\u001a\u00020\u0003H\u00c6\u0003J\t\u0010,\u001a\u00020\u000bH\u00c6\u0003J\t\u0010-\u001a\u00020\rH\u00c6\u0003JQ\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u00c6\u0001J\u0013\u0010/\u001a\u00020\u00032\b\u00100\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00101\u001a\u00020\u0005H\u00d6\u0001J\t\u00102\u001a\u00020\bH\u00d6\u0001R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010 \"\u0004\b$\u0010\"R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b%\u0010 \"\u0004\b&\u0010\"¡§\u00063" }, d2 = { "Lio/legado/app/data/entities/Book$ReadConfig;", "", "reverseToc", "", "pageAnim", "", "reSegment", "imageStyle", "", "useReplaceRule", "delTag", "", "pdfImageWidth", "", "(ZIZLjava/lang/String;ZJF)V", "getDelTag", "()J", "setDelTag", "(J)V", "getImageStyle", "()Ljava/lang/String;", "setImageStyle", "(Ljava/lang/String;)V", "getPageAnim", "()I", "setPageAnim", "(I)V", "getPdfImageWidth", "()F", "setPdfImageWidth", "(F)V", "getReSegment", "()Z", "setReSegment", "(Z)V", "getReverseToc", "setReverseToc", "getUseReplaceRule", "setUseReplaceRule", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
    public static final class ReadConfig
    {
        private boolean reverseToc;
        private int pageAnim;
        private boolean reSegment;
        @Nullable
        private String imageStyle;
        private boolean useReplaceRule;
        private long delTag;
        private float pdfImageWidth;
        
        public ReadConfig(final boolean reverseToc, final int pageAnim, final boolean reSegment, @Nullable final String imageStyle, final boolean useReplaceRule, final long delTag, final float pdfImageWidth) {
            this.reverseToc = reverseToc;
            this.pageAnim = pageAnim;
            this.reSegment = reSegment;
            this.imageStyle = imageStyle;
            this.useReplaceRule = useReplaceRule;
            this.delTag = delTag;
            this.pdfImageWidth = pdfImageWidth;
        }
        
        public final boolean getReverseToc() {
            return this.reverseToc;
        }
        
        public final void setReverseToc(final boolean <set-?>) {
            this.reverseToc = <set-?>;
        }
        
        public final int getPageAnim() {
            return this.pageAnim;
        }
        
        public final void setPageAnim(final int <set-?>) {
            this.pageAnim = <set-?>;
        }
        
        public final boolean getReSegment() {
            return this.reSegment;
        }
        
        public final void setReSegment(final boolean <set-?>) {
            this.reSegment = <set-?>;
        }
        
        @Nullable
        public final String getImageStyle() {
            return this.imageStyle;
        }
        
        public final void setImageStyle(@Nullable final String <set-?>) {
            this.imageStyle = <set-?>;
        }
        
        public final boolean getUseReplaceRule() {
            return this.useReplaceRule;
        }
        
        public final void setUseReplaceRule(final boolean <set-?>) {
            this.useReplaceRule = <set-?>;
        }
        
        public final long getDelTag() {
            return this.delTag;
        }
        
        public final void setDelTag(final long <set-?>) {
            this.delTag = <set-?>;
        }
        
        public final float getPdfImageWidth() {
            return this.pdfImageWidth;
        }
        
        public final void setPdfImageWidth(final float <set-?>) {
            this.pdfImageWidth = <set-?>;
        }
        
        public final boolean component1() {
            return this.reverseToc;
        }
        
        public final int component2() {
            return this.pageAnim;
        }
        
        public final boolean component3() {
            return this.reSegment;
        }
        
        @Nullable
        public final String component4() {
            return this.imageStyle;
        }
        
        public final boolean component5() {
            return this.useReplaceRule;
        }
        
        public final long component6() {
            return this.delTag;
        }
        
        public final float component7() {
            return this.pdfImageWidth;
        }
        
        @NotNull
        public final ReadConfig copy(final boolean reverseToc, final int pageAnim, final boolean reSegment, @Nullable final String imageStyle, final boolean useReplaceRule, final long delTag, final float pdfImageWidth) {
            return new ReadConfig(reverseToc, pageAnim, reSegment, imageStyle, useReplaceRule, delTag, pdfImageWidth);
        }
        
        @NotNull
        @Override
        public String toString() {
            return "ReadConfig(reverseToc=" + this.reverseToc + ", pageAnim=" + this.pageAnim + ", reSegment=" + this.reSegment + ", imageStyle=" + (Object)this.imageStyle + ", useReplaceRule=" + this.useReplaceRule + ", delTag=" + this.delTag + ", pdfImageWidth=" + this.pdfImageWidth + ')';
        }
        
        @Override
        public int hashCode() {
            int reverseToc;
            if ((reverseToc = (this.reverseToc ? 1 : 0)) != 0) {
                reverseToc = 1;
            }
            int result = reverseToc;
            result = result * 31 + Integer.hashCode(this.pageAnim);
            final int n = result * 31;
            int reSegment;
            if ((reSegment = (this.reSegment ? 1 : 0)) != 0) {
                reSegment = 1;
            }
            result = n + reSegment;
            result = result * 31 + ((this.imageStyle == null) ? 0 : this.imageStyle.hashCode());
            final int n2 = result * 31;
            int useReplaceRule;
            if ((useReplaceRule = (this.useReplaceRule ? 1 : 0)) != 0) {
                useReplaceRule = 1;
            }
            result = n2 + useReplaceRule;
            result = result * 31 + Long.hashCode(this.delTag);
            result = result * 31 + Float.hashCode(this.pdfImageWidth);
            return result;
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ReadConfig)) {
                return false;
            }
            final ReadConfig readConfig = (ReadConfig)other;
            return this.reverseToc == readConfig.reverseToc && this.pageAnim == readConfig.pageAnim && this.reSegment == readConfig.reSegment && Intrinsics.areEqual((Object)this.imageStyle, (Object)readConfig.imageStyle) && this.useReplaceRule == readConfig.useReplaceRule && this.delTag == readConfig.delTag && Intrinsics.areEqual((Object)this.pdfImageWidth, (Object)readConfig.pdfImageWidth);
        }
        
        public ReadConfig() {
            this(false, 0, false, null, false, 0L, 0.0f, 127, null);
        }
    }
    
    @Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005?\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J(\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002?\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b?\u001e0\u0001¡§\u0006\f" }, d2 = { "Lio/legado/app/data/entities/Book$Converters;", "", "()V", "readConfigToString", "", "config", "Lio/legado/app/data/entities/Book$ReadConfig;", "stringToReadConfig", "Lkotlin/Result;", "json", "stringToReadConfig-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "reader-pro" })
    public static final class Converters
    {
        @NotNull
        public final String readConfigToString(@Nullable final ReadConfig config) {
            final String json = GsonExtensionsKt.getGSON().toJson((Object)config);
            Intrinsics.checkNotNullExpressionValue((Object)json, "GSON.toJson(config)");
            return json;
        }
        
        @NotNull
        public final Object stringToReadConfig-IoAF18A(@Nullable final String json) {
            final Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            final int $i$f$fromJsonObject = 0;
            Object o;
            try {
                final Result$Companion companion = Result.Companion;
                final int n = 0;
                final Gson gson = $this$fromJsonObject$iv;
                final int $i$f$genericType = 0;
                final Type type = new TypeToken<ReadConfig>() {}.getType();
                Intrinsics.checkNotNullExpressionValue((Object)type, "object : TypeToken<T>() {}.type");
                Object fromJson;
                if (!((fromJson = gson.fromJson(json, type)) instanceof ReadConfig)) {
                    fromJson = null;
                }
                o = Result.constructor-impl((Object)fromJson);
            }
            catch (final Throwable t) {
                final Result$Companion companion2 = Result.Companion;
                o = Result.constructor-impl(ResultKt.createFailure(t));
            }
            return o;
        }
    }
}
