package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.Predicate;
import com.jayway.jsonpath.ReadContext;
import io.legado.app.constant.AppPattern;
import io.legado.app.constant.BookType;
import io.legado.app.data.entities.BaseBook;
import io.legado.app.model.localBook.CbzFile;
import io.legado.app.model.localBook.EpubFile;
import io.legado.app.model.localBook.LocalBook;
import io.legado.app.model.localBook.UmdFile;
import io.legado.app.utils.FileUtils;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.JsonExtensionsKt;
import io.legado.app.utils.MD5Utils;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.Constants;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: Book.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/Book.class */
@JsonIgnoreProperties({"variableMap", "infoHtml", "tocHtml", "config", "rootDir", "localBook", "epub", "epubRootDir", "onLineTxt", "localTxt", "umd", "realAuthor", "unreadChapterNum", "folderName", "pdfImageWidth", "localFile", "kindList", "_userNameSpace", "bookDir", "userNameSpace"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\bU\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b+\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\b\b\u0087\b\u0018\u0000 Õ\u00012\u00020\u0001:\u0006Õ\u0001Ö\u0001×\u0001Bé\u0002\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u0012\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0013\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u0018\u001a\u00020\u0011\u0012\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001a\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001b\u001a\u00020\u0011\u0012\b\b\u0002\u0010\u001c\u001a\u00020\u0013\u0012\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\u001e\u001a\u00020\u001f\u0012\b\b\u0002\u0010 \u001a\u00020\u0011\u0012\b\b\u0002\u0010!\u001a\u00020\u0011\u0012\b\b\u0002\u0010\"\u001a\u00020\u001f\u0012\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%\u0012\b\b\u0002\u0010&\u001a\u00020\u001f\u0012\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010(J\n\u0010\u0083\u0001\u001a\u00020\u0003HÆ\u0003J\f\u0010\u0084\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u0085\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u0086\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u0087\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u0088\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u0089\u0001\u001a\u00020\u0013HÆ\u0003J\f\u0010\u008a\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u008b\u0001\u001a\u00020\u0013HÆ\u0003J\n\u0010\u008c\u0001\u001a\u00020\u0013HÆ\u0003J\n\u0010\u008d\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u008e\u0001\u001a\u00020\u0003HÆ\u0003J\n\u0010\u008f\u0001\u001a\u00020\u0011HÆ\u0003J\f\u0010\u0090\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u0091\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u0092\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u0093\u0001\u001a\u00020\u0013HÆ\u0003J\f\u0010\u0094\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u0095\u0001\u001a\u00020\u001fHÆ\u0003J\n\u0010\u0096\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u0097\u0001\u001a\u00020\u0011HÆ\u0003J\n\u0010\u0098\u0001\u001a\u00020\u001fHÆ\u0003J\n\u0010\u0099\u0001\u001a\u00020\u0003HÆ\u0003J\f\u0010\u009a\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010\u009b\u0001\u001a\u0004\u0018\u00010%HÆ\u0003J\n\u0010\u009c\u0001\u001a\u00020\u001fHÆ\u0003J\f\u0010\u009d\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\n\u0010\u009e\u0001\u001a\u00020\u0003HÆ\u0003J\n\u0010\u009f\u0001\u001a\u00020\u0003HÆ\u0003J\n\u0010 \u0001\u001a\u00020\u0003HÆ\u0003J\f\u0010¡\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010¢\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\f\u0010£\u0001\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010¤\u0001\u001a\u00020%H\u0002Jî\u0002\u0010¥\u0001\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u00132\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u00132\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0018\u001a\u00020\u00112\n\b\u0002\u0010\u0019\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001a\u001a\u00020\u00112\b\b\u0002\u0010\u001b\u001a\u00020\u00112\b\b\u0002\u0010\u001c\u001a\u00020\u00132\n\b\u0002\u0010\u001d\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010 \u001a\u00020\u00112\b\b\u0002\u0010!\u001a\u00020\u00112\b\b\u0002\u0010\"\u001a\u00020\u001f2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%2\b\b\u0002\u0010&\u001a\u00020\u001f2\n\b\u0002\u0010'\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0016\u0010¦\u0001\u001a\u00020\u001f2\n\u0010§\u0001\u001a\u0005\u0018\u00010¨\u0001H\u0096\u0002J\b\u0010©\u0001\u001a\u00030ª\u0001J\u0007\u0010«\u0001\u001a\u00020\u0003J\u0010\u0010¬\u0001\u001a\u00020\u001f2\u0007\u0010\u00ad\u0001\u001a\u00020\u0013J\t\u0010®\u0001\u001a\u0004\u0018\u00010\u0003J\t\u0010¯\u0001\u001a\u0004\u0018\u00010\u0003J\u0007\u0010°\u0001\u001a\u00020\u0003J\u0007\u0010±\u0001\u001a\u00020\u0003J\b\u0010²\u0001\u001a\u00030³\u0001J\b\u0010´\u0001\u001a\u00030µ\u0001J\u0007\u0010¶\u0001\u001a\u00020\u0003J\u0007\u0010·\u0001\u001a\u00020\u001fJ\u0007\u0010¸\u0001\u001a\u00020\u0011J\t\u0010¹\u0001\u001a\u00020\u0003H\u0016J\t\u0010º\u0001\u001a\u00020\u0011H\u0016J\u0007\u0010»\u0001\u001a\u00020\u001fJ\u0007\u0010¼\u0001\u001a\u00020\u001fJ\u0007\u0010½\u0001\u001a\u00020\u001fJ\u0007\u0010¾\u0001\u001a\u00020\u001fJ\u0007\u0010¿\u0001\u001a\u00020\u001fJ\u0007\u0010À\u0001\u001a\u00020\u001fJ\u0007\u0010Á\u0001\u001a\u00020\u001fJ\u0007\u0010Â\u0001\u001a\u00020\u001fJ\u0007\u0010Ã\u0001\u001a\u00020\u001fJ\u001e\u0010Ä\u0001\u001a\u00030Å\u00012\u0007\u0010Æ\u0001\u001a\u00020\u00032\t\u0010Ç\u0001\u001a\u0004\u0018\u00010\u0003H\u0016J\u0011\u0010È\u0001\u001a\u00030Å\u00012\u0007\u0010\u00ad\u0001\u001a\u00020\u0013J\u0012\u0010É\u0001\u001a\u00030Å\u00012\b\u0010Ê\u0001\u001a\u00030µ\u0001J\u0011\u0010Ë\u0001\u001a\u00030Å\u00012\u0007\u0010Ì\u0001\u001a\u00020\u0003J\u0011\u0010Í\u0001\u001a\u00030Å\u00012\u0007\u0010Î\u0001\u001a\u00020\u0003J\b\u0010Ï\u0001\u001a\u00030Ð\u0001J\n\u0010Ñ\u0001\u001a\u00020\u0003HÖ\u0001J\u0013\u0010Ò\u0001\u001a\u00030Å\u00012\t\b\u0002\u0010Ó\u0001\u001a\u00020\u001fJ\u0007\u0010Ô\u0001\u001a\u00020\u0003R\u000e\u0010)\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u00020\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010+\"\u0004\b,\u0010-R\u001a\u0010\u0002\u001a\u00020\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010+\"\u0004\b/\u0010-R\u001a\u0010\u001e\u001a\u00020\u001fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b4\u0010+\"\u0004\b5\u0010-R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010+\"\u0004\b7\u0010-R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u0010+\"\u0004\b9\u0010-R\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010+\"\u0004\b;\u0010-R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b<\u0010+\"\u0004\b=\u0010-R\u001a\u0010\u001a\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010?\"\u0004\b@\u0010AR\u001a\u0010\u001b\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010?\"\u0004\bC\u0010AR\u001a\u0010\u001c\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010E\"\u0004\bF\u0010GR\u001c\u0010\u0019\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010+\"\u0004\bI\u0010-R\u001a\u0010\u0012\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bJ\u0010E\"\u0004\bK\u0010GR\u001c\u0010L\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bM\u0010+\"\u0004\bN\u0010-R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010+\"\u0004\bP\u0010-R\u001c\u0010&\u001a\u00020\u001f8\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u00101\"\u0004\bQ\u00103R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010+\"\u0004\bS\u0010-R\u001a\u0010\u0017\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bT\u0010?\"\u0004\bU\u0010AR\u001c\u0010'\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bV\u0010+\"\u0004\bW\u0010-R\u001a\u0010\u0016\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010E\"\u0004\bY\u0010GR\u001a\u0010\u0015\u001a\u00020\u0013X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bZ\u0010E\"\u0004\b[\u0010GR\u001c\u0010\u0014\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010+\"\u0004\b]\u0010-R\u001a\u0010\u0007\u001a\u00020\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b^\u0010+\"\u0004\b_\u0010-R\u001a\u0010 \u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b`\u0010?\"\u0004\ba\u0010AR\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bb\u0010+\"\u0004\bc\u0010-R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010+\"\u0004\be\u0010-R\u001a\u0010!\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bf\u0010?\"\u0004\bg\u0010AR\u001c\u0010$\u001a\u0004\u0018\u00010%X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bh\u0010i\"\u0004\bj\u0010kR\u000e\u0010l\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010m\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bn\u0010+\"\u0004\bo\u0010-R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bp\u0010+\"\u0004\bq\u0010-R\u001a\u0010\u0018\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\br\u0010?\"\u0004\bs\u0010AR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bt\u0010?\"\u0004\bu\u0010AR\u001a\u0010\"\u001a\u00020\u001fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bv\u00101\"\u0004\bw\u00103R\u001c\u0010#\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bx\u0010+\"\u0004\by\u0010-R8\u0010z\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030{j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`|8VX\u0096\u0084\u0002¢\u0006\r\n\u0005\b\u007f\u0010\u0080\u0001\u001a\u0004\b}\u0010~R\u001e\u0010\u001d\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u0010\n\u0000\u001a\u0005\b\u0081\u0001\u0010+\"\u0005\b\u0082\u0001\u0010-¨\u0006Ø\u0001"}, d2 = {"Lio/legado/app/data/entities/Book;", "Lio/legado/app/data/entities/BaseBook;", "bookUrl", PackageDocumentBase.PREFIX_OPF, "tocUrl", "origin", "originName", "name", "author", "kind", "customTag", "coverUrl", "customCoverUrl", "intro", "customIntro", "charset", "type", PackageDocumentBase.PREFIX_OPF, "group", PackageDocumentBase.PREFIX_OPF, "latestChapterTitle", "latestChapterTime", "lastCheckTime", "lastCheckCount", "totalChapterNum", "durChapterTitle", "durChapterIndex", "durChapterPos", "durChapterTime", "wordCount", "canUpdate", PackageDocumentBase.PREFIX_OPF, "order", "originOrder", "useReplaceRule", "variable", "readConfig", "Lio/legado/app/data/entities/Book$ReadConfig;", "isInShelf", "lastCheckError", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;IJLjava/lang/String;JJIILjava/lang/String;IIJLjava/lang/String;ZIIZLjava/lang/String;Lio/legado/app/data/entities/Book$ReadConfig;ZLjava/lang/String;)V", "_userNameSpace", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getCanUpdate", "()Z", "setCanUpdate", "(Z)V", "getCharset", "setCharset", "getCoverUrl", "setCoverUrl", "getCustomCoverUrl", "setCustomCoverUrl", "getCustomIntro", "setCustomIntro", "getCustomTag", "setCustomTag", "getDurChapterIndex", "()I", "setDurChapterIndex", "(I)V", "getDurChapterPos", "setDurChapterPos", "getDurChapterTime", "()J", "setDurChapterTime", "(J)V", "getDurChapterTitle", "setDurChapterTitle", "getGroup", "setGroup", "infoHtml", "getInfoHtml", "setInfoHtml", "getIntro", "setIntro", "setInShelf", "getKind", "setKind", "getLastCheckCount", "setLastCheckCount", "getLastCheckError", "setLastCheckError", "getLastCheckTime", "setLastCheckTime", "getLatestChapterTime", "setLatestChapterTime", "getLatestChapterTitle", "setLatestChapterTitle", "getName", "setName", "getOrder", "setOrder", "getOrigin", "setOrigin", "getOriginName", "setOriginName", "getOriginOrder", "setOriginOrder", "getReadConfig", "()Lio/legado/app/data/entities/Book$ReadConfig;", "setReadConfig", "(Lio/legado/app/data/entities/Book$ReadConfig;)V", "rootDir", "tocHtml", "getTocHtml", "setTocHtml", "getTocUrl", "setTocUrl", "getTotalChapterNum", "setTotalChapterNum", "getType", "setType", "getUseReplaceRule", "setUseReplaceRule", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "getWordCount", "setWordCount", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component22", "component23", "component24", "component25", "component26", "component27", "component28", "component29", "component3", "component30", "component31", "component32", "component33", "component4", "component5", "component6", "component7", "component8", "component9", "config", "copy", "equals", "other", PackageDocumentBase.PREFIX_OPF, "fileCharset", "Ljava/nio/charset/Charset;", "getBookDir", "getDelTag", "tag", "getDisplayCover", "getDisplayIntro", "getEpubRootDir", "getFolderName", "getLocalFile", "Ljava/io/File;", "getPdfImageWidth", PackageDocumentBase.PREFIX_OPF, "getRealAuthor", "getSplitLongChapter", "getUnreadChapterNum", "getUserNameSpace", "hashCode", "isCbz", "isEpub", "isLocalBook", "isLocalEpub", "isLocalPdf", "isLocalTxt", "isOnLineTxt", "isPdf", "isUmd", "putVariable", PackageDocumentBase.PREFIX_OPF, "key", "value", "setDelTag", "setPdfImageWidth", "pdfImageWidth", "setRootDir", "root", "setUserNameSpace", "nameSpace", "toSearchBook", "Lio/legado/app/data/entities/SearchBook;", "toString", "updateFromLocal", "onlyCover", "workRoot", "Companion", "Converters", "ReadConfig", "reader-pro"})
public final /* data */ class Book implements BaseBook {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    @NotNull
    public static final Companion INSTANCE = new Companion(null);

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

    /* JADX INFO: renamed from: variableMap$delegate, reason: from kotlin metadata */
    @NotNull
    private final transient Lazy variableMap;

    @Nullable
    private String infoHtml;

    @Nullable
    private String tocHtml;

    @NotNull
    private transient String rootDir;

    @NotNull
    private transient String _userNameSpace;
    public static final long hTag = 2;
    public static final long rubyTag = 4;
    public static final long imgTag = 8;

    @NotNull
    public static final String imgStyleDefault = "DEFAULT";

    @NotNull
    public static final String imgStyleFull = "FULL";

    @NotNull
    public static final String imgStyleText = "TEXT";

    @NotNull
    public final String component1() {
        return getBookUrl();
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
        return getName();
    }

    @NotNull
    public final String component6() {
        return getAuthor();
    }

    @Nullable
    public final String component7() {
        return getKind();
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
        return getWordCount();
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
    public final Book copy(@NotNull String bookUrl, @NotNull String tocUrl, @NotNull String origin, @NotNull String originName, @NotNull String name, @NotNull String author, @Nullable String kind, @Nullable String customTag, @Nullable String coverUrl, @Nullable String customCoverUrl, @Nullable String intro, @Nullable String customIntro, @Nullable String charset, int type, long group, @Nullable String latestChapterTitle, long latestChapterTime, long lastCheckTime, int lastCheckCount, int totalChapterNum, @Nullable String durChapterTitle, int durChapterIndex, int durChapterPos, long durChapterTime, @Nullable String wordCount, boolean canUpdate, int order, int originOrder, boolean useReplaceRule, @Nullable String variable, @Nullable ReadConfig readConfig, boolean isInShelf, @Nullable String lastCheckError) {
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter(tocUrl, "tocUrl");
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(originName, "originName");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(author, "author");
        return new Book(bookUrl, tocUrl, origin, originName, name, author, kind, customTag, coverUrl, customCoverUrl, intro, customIntro, charset, type, group, latestChapterTitle, latestChapterTime, lastCheckTime, lastCheckCount, totalChapterNum, durChapterTitle, durChapterIndex, durChapterPos, durChapterTime, wordCount, canUpdate, order, originOrder, useReplaceRule, variable, readConfig, isInShelf, lastCheckError);
    }

    public static /* synthetic */ Book copy$default(Book book, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, int i, long j, String str14, long j2, long j3, int i2, int i3, String str15, int i4, int i5, long j4, String str16, boolean z, int i6, int i7, boolean z2, String str17, ReadConfig readConfig, boolean z3, String str18, int i8, int i9, Object obj) {
        if ((i8 & 1) != 0) {
            str = book.getBookUrl();
        }
        if ((i8 & 2) != 0) {
            str2 = book.tocUrl;
        }
        if ((i8 & 4) != 0) {
            str3 = book.origin;
        }
        if ((i8 & 8) != 0) {
            str4 = book.originName;
        }
        if ((i8 & 16) != 0) {
            str5 = book.getName();
        }
        if ((i8 & 32) != 0) {
            str6 = book.getAuthor();
        }
        if ((i8 & 64) != 0) {
            str7 = book.getKind();
        }
        if ((i8 & Wbxml.EXT_T_0) != 0) {
            str8 = book.customTag;
        }
        if ((i8 & 256) != 0) {
            str9 = book.coverUrl;
        }
        if ((i8 & 512) != 0) {
            str10 = book.customCoverUrl;
        }
        if ((i8 & 1024) != 0) {
            str11 = book.intro;
        }
        if ((i8 & 2048) != 0) {
            str12 = book.customIntro;
        }
        if ((i8 & 4096) != 0) {
            str13 = book.charset;
        }
        if ((i8 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
            i = book.type;
        }
        if ((i8 & 16384) != 0) {
            j = book.group;
        }
        if ((i8 & 32768) != 0) {
            str14 = book.latestChapterTitle;
        }
        if ((i8 & 65536) != 0) {
            j2 = book.latestChapterTime;
        }
        if ((i8 & 131072) != 0) {
            j3 = book.lastCheckTime;
        }
        if ((i8 & 262144) != 0) {
            i2 = book.lastCheckCount;
        }
        if ((i8 & 524288) != 0) {
            i3 = book.totalChapterNum;
        }
        if ((i8 & 1048576) != 0) {
            str15 = book.durChapterTitle;
        }
        if ((i8 & 2097152) != 0) {
            i4 = book.durChapterIndex;
        }
        if ((i8 & 4194304) != 0) {
            i5 = book.durChapterPos;
        }
        if ((i8 & 8388608) != 0) {
            j4 = book.durChapterTime;
        }
        if ((i8 & 16777216) != 0) {
            str16 = book.getWordCount();
        }
        if ((i8 & 33554432) != 0) {
            z = book.canUpdate;
        }
        if ((i8 & 67108864) != 0) {
            i6 = book.order;
        }
        if ((i8 & 134217728) != 0) {
            i7 = book.originOrder;
        }
        if ((i8 & 268435456) != 0) {
            z2 = book.useReplaceRule;
        }
        if ((i8 & 536870912) != 0) {
            str17 = book.variable;
        }
        if ((i8 & 1073741824) != 0) {
            readConfig = book.readConfig;
        }
        if ((i8 & Integer.MIN_VALUE) != 0) {
            z3 = book.isInShelf;
        }
        if ((i9 & 1) != 0) {
            str18 = book.lastCheckError;
        }
        return book.copy(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, i, j, str14, j2, j3, i2, i3, str15, i4, i5, j4, str16, z, i6, i7, z2, str17, readConfig, z3, str18);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Book(bookUrl=").append(getBookUrl()).append(", tocUrl=").append(this.tocUrl).append(", origin=").append(this.origin).append(", originName=").append(this.originName).append(", name=").append(getName()).append(", author=").append(getAuthor()).append(", kind=").append((Object) getKind()).append(", customTag=").append((Object) this.customTag).append(", coverUrl=").append((Object) this.coverUrl).append(", customCoverUrl=").append((Object) this.customCoverUrl).append(", intro=").append((Object) this.intro).append(", customIntro=");
        sb.append((Object) this.customIntro).append(", charset=").append((Object) this.charset).append(", type=").append(this.type).append(", group=").append(this.group).append(", latestChapterTitle=").append((Object) this.latestChapterTitle).append(", latestChapterTime=").append(this.latestChapterTime).append(", lastCheckTime=").append(this.lastCheckTime).append(", lastCheckCount=").append(this.lastCheckCount).append(", totalChapterNum=").append(this.totalChapterNum).append(", durChapterTitle=").append((Object) this.durChapterTitle).append(", durChapterIndex=").append(this.durChapterIndex).append(", durChapterPos=").append(this.durChapterPos);
        sb.append(", durChapterTime=").append(this.durChapterTime).append(", wordCount=").append((Object) getWordCount()).append(", canUpdate=").append(this.canUpdate).append(", order=").append(this.order).append(", originOrder=").append(this.originOrder).append(", useReplaceRule=").append(this.useReplaceRule).append(", variable=").append((Object) this.variable).append(", readConfig=").append(this.readConfig).append(", isInShelf=").append(this.isInShelf).append(", lastCheckError=").append((Object) this.lastCheckError).append(')');
        return sb.toString();
    }

    public Book() {
        this(null, null, null, null, null, null, null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -1, 1, null);
    }

    public Book(@NotNull String bookUrl, @NotNull String tocUrl, @NotNull String origin, @NotNull String originName, @NotNull String name, @NotNull String author, @Nullable String kind, @Nullable String customTag, @Nullable String coverUrl, @Nullable String customCoverUrl, @Nullable String intro, @Nullable String customIntro, @Nullable String charset, int type, long group, @Nullable String latestChapterTitle, long latestChapterTime, long lastCheckTime, int lastCheckCount, int totalChapterNum, @Nullable String durChapterTitle, int durChapterIndex, int durChapterPos, long durChapterTime, @Nullable String wordCount, boolean canUpdate, int order, int originOrder, boolean useReplaceRule, @Nullable String variable, @Nullable ReadConfig readConfig, boolean isInShelf, @Nullable String lastCheckError) {
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter(tocUrl, "tocUrl");
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(originName, "originName");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(author, "author");
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
        this.variableMap = LazyKt.lazy(new Book$variableMap$2(this));
        this.rootDir = PackageDocumentBase.PREFIX_OPF;
        this._userNameSpace = PackageDocumentBase.PREFIX_OPF;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @NotNull
    public List<String> getKindList() {
        return BaseBook.DefaultImpls.getKindList(this);
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @Nullable
    public String getVariable(@NotNull String key) {
        return BaseBook.DefaultImpls.getVariable(this, key);
    }

    public /* synthetic */ Book(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, int i, long j, String str14, long j2, long j3, int i2, int i3, String str15, int i4, int i5, long j4, String str16, boolean z, int i6, int i7, boolean z2, String str17, ReadConfig readConfig, boolean z3, String str18, int i8, int i9, DefaultConstructorMarker defaultConstructorMarker) {
        this((i8 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i8 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i8 & 4) != 0 ? BookType.local : str3, (i8 & 8) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i8 & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str5, (i8 & 32) != 0 ? PackageDocumentBase.PREFIX_OPF : str6, (i8 & 64) != 0 ? null : str7, (i8 & Wbxml.EXT_T_0) != 0 ? null : str8, (i8 & 256) != 0 ? null : str9, (i8 & 512) != 0 ? null : str10, (i8 & 1024) != 0 ? null : str11, (i8 & 2048) != 0 ? null : str12, (i8 & 4096) != 0 ? null : str13, (i8 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? 0 : i, (i8 & 16384) != 0 ? 0L : j, (i8 & 32768) != 0 ? null : str14, (i8 & 65536) != 0 ? System.currentTimeMillis() : j2, (i8 & 131072) != 0 ? System.currentTimeMillis() : j3, (i8 & 262144) != 0 ? 0 : i2, (i8 & 524288) != 0 ? 0 : i3, (i8 & 1048576) != 0 ? null : str15, (i8 & 2097152) != 0 ? 0 : i4, (i8 & 4194304) != 0 ? 0 : i5, (i8 & 8388608) != 0 ? System.currentTimeMillis() : j4, (i8 & 16777216) != 0 ? null : str16, (i8 & 33554432) != 0 ? true : z, (i8 & 67108864) != 0 ? 0 : i6, (i8 & 134217728) != 0 ? 0 : i7, (i8 & 268435456) != 0 ? true : z2, (i8 & 536870912) != 0 ? null : str17, (i8 & 1073741824) != 0 ? null : readConfig, (i8 & Integer.MIN_VALUE) != 0 ? false : z3, (i9 & 1) != 0 ? null : str18);
    }

    @Override // io.legado.app.data.entities.BaseBook
    @NotNull
    public String getBookUrl() {
        return this.bookUrl;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setBookUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.bookUrl = str;
    }

    @NotNull
    public final String getTocUrl() {
        return this.tocUrl;
    }

    public final void setTocUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.tocUrl = str;
    }

    @NotNull
    public final String getOrigin() {
        return this.origin;
    }

    public final void setOrigin(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.origin = str;
    }

    @NotNull
    public final String getOriginName() {
        return this.originName;
    }

    public final void setOriginName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.originName = str;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @NotNull
    public String getName() {
        return this.name;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.name = str;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @NotNull
    public String getAuthor() {
        return this.author;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setAuthor(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.author = str;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @Nullable
    public String getKind() {
        return this.kind;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setKind(@Nullable String str) {
        this.kind = str;
    }

    @Nullable
    public final String getCustomTag() {
        return this.customTag;
    }

    public final void setCustomTag(@Nullable String str) {
        this.customTag = str;
    }

    @Nullable
    public final String getCoverUrl() {
        return this.coverUrl;
    }

    public final void setCoverUrl(@Nullable String str) {
        this.coverUrl = str;
    }

    @Nullable
    public final String getCustomCoverUrl() {
        return this.customCoverUrl;
    }

    public final void setCustomCoverUrl(@Nullable String str) {
        this.customCoverUrl = str;
    }

    @Nullable
    public final String getIntro() {
        return this.intro;
    }

    public final void setIntro(@Nullable String str) {
        this.intro = str;
    }

    @Nullable
    public final String getCustomIntro() {
        return this.customIntro;
    }

    public final void setCustomIntro(@Nullable String str) {
        this.customIntro = str;
    }

    @Nullable
    public final String getCharset() {
        return this.charset;
    }

    public final void setCharset(@Nullable String str) {
        this.charset = str;
    }

    public final int getType() {
        return this.type;
    }

    public final void setType(int i) {
        this.type = i;
    }

    public final long getGroup() {
        return this.group;
    }

    public final void setGroup(long j) {
        this.group = j;
    }

    @Nullable
    public final String getLatestChapterTitle() {
        return this.latestChapterTitle;
    }

    public final void setLatestChapterTitle(@Nullable String str) {
        this.latestChapterTitle = str;
    }

    public final long getLatestChapterTime() {
        return this.latestChapterTime;
    }

    public final void setLatestChapterTime(long j) {
        this.latestChapterTime = j;
    }

    public final long getLastCheckTime() {
        return this.lastCheckTime;
    }

    public final void setLastCheckTime(long j) {
        this.lastCheckTime = j;
    }

    public final int getLastCheckCount() {
        return this.lastCheckCount;
    }

    public final void setLastCheckCount(int i) {
        this.lastCheckCount = i;
    }

    public final int getTotalChapterNum() {
        return this.totalChapterNum;
    }

    public final void setTotalChapterNum(int i) {
        this.totalChapterNum = i;
    }

    @Nullable
    public final String getDurChapterTitle() {
        return this.durChapterTitle;
    }

    public final void setDurChapterTitle(@Nullable String str) {
        this.durChapterTitle = str;
    }

    public final int getDurChapterIndex() {
        return this.durChapterIndex;
    }

    public final void setDurChapterIndex(int i) {
        this.durChapterIndex = i;
    }

    public final int getDurChapterPos() {
        return this.durChapterPos;
    }

    public final void setDurChapterPos(int i) {
        this.durChapterPos = i;
    }

    public final long getDurChapterTime() {
        return this.durChapterTime;
    }

    public final void setDurChapterTime(long j) {
        this.durChapterTime = j;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @Nullable
    public String getWordCount() {
        return this.wordCount;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setWordCount(@Nullable String str) {
        this.wordCount = str;
    }

    public final boolean getCanUpdate() {
        return this.canUpdate;
    }

    public final void setCanUpdate(boolean z) {
        this.canUpdate = z;
    }

    public final int getOrder() {
        return this.order;
    }

    public final void setOrder(int i) {
        this.order = i;
    }

    public final int getOriginOrder() {
        return this.originOrder;
    }

    public final void setOriginOrder(int i) {
        this.originOrder = i;
    }

    public final boolean getUseReplaceRule() {
        return this.useReplaceRule;
    }

    public final void setUseReplaceRule(boolean z) {
        this.useReplaceRule = z;
    }

    @Nullable
    public final String getVariable() {
        return this.variable;
    }

    public final void setVariable(@Nullable String str) {
        this.variable = str;
    }

    @Nullable
    public final ReadConfig getReadConfig() {
        return this.readConfig;
    }

    public final void setReadConfig(@Nullable ReadConfig readConfig) {
        this.readConfig = readConfig;
    }

    @JsonProperty("isInShelf")
    public final boolean isInShelf() {
        return this.isInShelf;
    }

    public final void setInShelf(boolean z) {
        this.isInShelf = z;
    }

    @Nullable
    public final String getLastCheckError() {
        return this.lastCheckError;
    }

    public final void setLastCheckError(@Nullable String str) {
        this.lastCheckError = str;
    }

    public final boolean isLocalBook() {
        return Intrinsics.areEqual(this.origin, BookType.local);
    }

    public final boolean isLocalTxt() {
        return isLocalBook() && StringsKt.endsWith(this.originName, ".txt", true);
    }

    public final boolean isLocalEpub() {
        return isLocalBook() && StringsKt.endsWith(this.originName, ".epub", true);
    }

    public final boolean isLocalPdf() {
        return isLocalBook() && StringsKt.endsWith(this.originName, ".pdf", true);
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
        return !isLocalBook() && this.type == 0;
    }

    public boolean equals(@Nullable Object other) {
        if (other instanceof Book) {
            return Intrinsics.areEqual(((Book) other).getBookUrl(), getBookUrl());
        }
        return false;
    }

    public int hashCode() {
        return getBookUrl().hashCode();
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @NotNull
    public HashMap<String, String> getVariableMap() {
        return (HashMap) this.variableMap.getValue();
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    public void putVariable(@NotNull String key, @Nullable String value) {
        Intrinsics.checkNotNullParameter(key, "key");
        if (value != null) {
            getVariableMap().put(key, value);
        } else {
            getVariableMap().remove(key);
        }
        this.variable = GsonExtensionsKt.getGSON().toJson(getVariableMap());
    }

    @Override // io.legado.app.data.entities.BaseBook
    @Nullable
    public String getInfoHtml() {
        return this.infoHtml;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setInfoHtml(@Nullable String str) {
        this.infoHtml = str;
    }

    @Override // io.legado.app.data.entities.BaseBook
    @Nullable
    public String getTocHtml() {
        return this.tocHtml;
    }

    @Override // io.legado.app.data.entities.BaseBook
    public void setTocHtml(@Nullable String str) {
        this.tocHtml = str;
    }

    @NotNull
    public final String getRealAuthor() {
        return AppPattern.INSTANCE.getAuthorRegex().replace(getAuthor(), PackageDocumentBase.PREFIX_OPF);
    }

    public final int getUnreadChapterNum() {
        return Math.max((this.totalChapterNum - this.durChapterIndex) - 1, 0);
    }

    @Nullable
    public final String getDisplayCover() {
        String str = this.customCoverUrl;
        return str == null || str.length() == 0 ? this.coverUrl : this.customCoverUrl;
    }

    @Nullable
    public final String getDisplayIntro() {
        String str = this.customIntro;
        return str == null || str.length() == 0 ? this.intro : this.customIntro;
    }

    @NotNull
    public final Charset fileCharset() {
        String str = this.charset;
        Charset charsetForName = Charset.forName(str == null ? Constants.CHARACTER_ENCODING : str);
        Intrinsics.checkNotNullExpressionValue(charsetForName, "Charset.forName(charsetName)");
        return charsetForName;
    }

    private final ReadConfig config() {
        if (this.readConfig == null) {
            this.readConfig = new ReadConfig(false, 0, false, null, false, 0L, 0.0f, 127, null);
        }
        ReadConfig readConfig = this.readConfig;
        Intrinsics.checkNotNull(readConfig);
        return readConfig;
    }

    public final void setDelTag(long tag) {
        config().setDelTag((config().getDelTag() & tag) == tag ? config().getDelTag() & (tag ^ (-1)) : config().getDelTag() | tag);
    }

    public final boolean getDelTag(long tag) {
        return (config().getDelTag() & tag) == tag;
    }

    public final float getPdfImageWidth() {
        return config().getPdfImageWidth();
    }

    public final void setPdfImageWidth(float pdfImageWidth) {
        config().setPdfImageWidth(pdfImageWidth);
    }

    @NotNull
    public final String getFolderName() {
        String folderName = AppPattern.INSTANCE.getFileNameRegex().replace(getName(), PackageDocumentBase.PREFIX_OPF);
        int iMin = Math.min(9, folderName.length());
        if (folderName == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String folderName2 = folderName.substring(0, iMin);
        Intrinsics.checkNotNullExpressionValue(folderName2, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        return Intrinsics.stringPlus(folderName2, MD5Utils.INSTANCE.md5Encode16(getBookUrl()));
    }

    public final void setRootDir(@NotNull String root) {
        Intrinsics.checkNotNullParameter(root, "root");
        if (root.length() > 0) {
            String str = File.separator;
            Intrinsics.checkNotNullExpressionValue(str, "separator");
            if (!StringsKt.endsWith$default(root, str, false, 2, (Object) null)) {
                this.rootDir = Intrinsics.stringPlus(root, File.separator);
                return;
            }
        }
        this.rootDir = root;
    }

    @NotNull
    public final File getLocalFile() {
        if (StringsKt.startsWith$default(this.originName, this.rootDir, false, 2, (Object) null)) {
            this.originName = StringsKt.replaceFirst$default(this.originName, this.rootDir, PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null);
        }
        BookKt.getLogger().info("getLocalFile rootDir: {} originName: {}", this.rootDir, this.originName);
        return (!isEpub() || StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, (Object) null) >= 0 || StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, (Object) null) >= 0) ? (!isCbz() || StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, (Object) null) >= 0 || StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, (Object) null) >= 0) ? (!isPdf() || StringsKt.indexOf$default(this.originName, "localStore", 0, false, 6, (Object) null) >= 0 || StringsKt.indexOf$default(this.originName, "webdav", 0, false, 6, (Object) null) >= 0) ? new File(Intrinsics.stringPlus(this.rootDir, this.originName)) : FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.pdf") : FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.cbz") : FileUtils.INSTANCE.getFile(new File(Intrinsics.stringPlus(this.rootDir, this.originName)), "index.epub");
    }

    public final void setUserNameSpace(@NotNull String nameSpace) {
        Intrinsics.checkNotNullParameter(nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @NotNull
    public String getUserNameSpace() {
        return this._userNameSpace;
    }

    @NotNull
    public final String getBookDir() {
        return FileUtils.INSTANCE.getPath(new File(this.rootDir), "storage", "data", this._userNameSpace, getName() + '_' + getAuthor());
    }

    public final boolean getSplitLongChapter() {
        return false;
    }

    @NotNull
    public final SearchBook toSearchBook() {
        String name = getName();
        String author = getAuthor();
        String kind = getKind();
        SearchBook $this$toSearchBook_u24lambda_u2d0 = new SearchBook(getBookUrl(), this.origin, this.originName, this.type, name, author, kind, this.coverUrl, this.intro, getWordCount(), this.latestChapterTitle, this.tocUrl, 0L, this.variable, 0, 20480, null);
        $this$toSearchBook_u24lambda_u2d0.setInfoHtml(getInfoHtml());
        $this$toSearchBook_u24lambda_u2d0.setTocHtml(getTocHtml());
        $this$toSearchBook_u24lambda_u2d0.setUserNameSpace(getUserNameSpace());
        return $this$toSearchBook_u24lambda_u2d0;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Unexpected missing predecessor for block: B:4:0x0048
        	at jadx.core.dex.visitors.blocks.BlockSplitter.addTempConnectionsForExcHandlers(BlockSplitter.java:280)
        	at jadx.core.dex.visitors.blocks.BlockSplitter.visit(BlockSplitter.java:79)
        */
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getEpubRootDir() {
        /*
            Method dump skipped, instruction units count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: io.legado.app.data.entities.Book.getEpubRootDir():java.lang.String");
    }

    public static /* synthetic */ void updateFromLocal$default(Book book, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        book.updateFromLocal(z);
    }

    public final void updateFromLocal(boolean onlyCover) {
        try {
            if (isEpub()) {
                EpubFile.INSTANCE.upBookInfo(this, onlyCover);
            } else if (isUmd()) {
                UmdFile.INSTANCE.upBookInfo(this, onlyCover);
            } else if (isCbz()) {
                CbzFile.INSTANCE.upBookInfo(this, onlyCover);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NotNull
    public final String workRoot() {
        return this.rootDir;
    }

    /* JADX INFO: compiled from: Book.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/Book$Companion.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J$\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u000e\u001a\u00020\u0006ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u000f\u0010\u0010J4\u0010\u0011\u001a\u0018\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\r0\u0012j\b\u0012\u0004\u0012\u00020\r`\u00130\f2\u0006\u0010\u0014\u001a\u00020\u0006ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0015\u0010\u0010J$\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\f2\u0006\u0010\u0017\u001a\u00020\u0018ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\u0019\u0010\u001aJ \u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u00062\b\b\u0002\u0010\u001e\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\u001f"}, d2 = {"Lio/legado/app/data/entities/Book$Companion;", PackageDocumentBase.PREFIX_OPF, "()V", "hTag", PackageDocumentBase.PREFIX_OPF, "imgStyleDefault", PackageDocumentBase.PREFIX_OPF, "imgStyleFull", "imgStyleText", "imgTag", "rubyTag", "fromJson", "Lkotlin/Result;", "Lio/legado/app/data/entities/Book;", "json", "fromJson-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "fromJsonArray", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "jsonArray", "fromJsonArray-IoAF18A", "fromJsonDoc", "doc", "Lcom/jayway/jsonpath/DocumentContext;", "fromJsonDoc-IoAF18A", "(Lcom/jayway/jsonpath/DocumentContext;)Ljava/lang/Object;", "initLocalBook", "bookUrl", "localPath", "rootDir", "reader-pro"})
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ Book initLocalBook$default(Companion companion, String str, String str2, String str3, int i, Object obj) {
            if ((i & 4) != 0) {
                str3 = PackageDocumentBase.PREFIX_OPF;
            }
            return companion.initLocalBook(str, str2, str3);
        }

        @NotNull
        public final Book initLocalBook(@NotNull String bookUrl, @NotNull String localPath, @NotNull String rootDir) {
            Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
            Intrinsics.checkNotNullParameter(localPath, "localPath");
            Intrinsics.checkNotNullParameter(rootDir, "rootDir");
            String fileName = new File(localPath).getName();
            LocalBook localBook = LocalBook.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(fileName, "fileName");
            Pair<String, String> pairAnalyzeNameAuthor = localBook.analyzeNameAuthor(fileName);
            Book it = new Book(bookUrl, PackageDocumentBase.PREFIX_OPF, BookType.local, localPath, (String) pairAnalyzeNameAuthor.getFirst(), (String) pairAnalyzeNameAuthor.getSecond(), null, null, null, null, null, null, null, 0, 0L, null, 0L, 0L, 0, 0, null, 0, 0, 0L, null, false, 0, 0, false, null, null, false, null, -64, 1, null);
            it.setCanUpdate(false);
            it.setRootDir(rootDir);
            Book.updateFromLocal$default(it, false, 1, null);
            return it;
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonDoc-IoAF18A, reason: not valid java name */
        public final Object m140fromJsonDocIoAF18A(@NotNull DocumentContext doc) {
            Object obj;
            ReadConfig readConfig;
            Object obj2;
            Intrinsics.checkNotNullParameter(doc, "doc");
            try {
                Result.Companion companion = Result.Companion;
                Object readConfig2 = doc.read("$.readConfig", new Predicate[0]);
                String string = JsonExtensionsKt.readString((ReadContext) doc, "$.bookUrl");
                Intrinsics.checkNotNull(string);
                String string2 = JsonExtensionsKt.readString((ReadContext) doc, "$.tocUrl");
                Intrinsics.checkNotNull(string2);
                String string3 = JsonExtensionsKt.readString((ReadContext) doc, "$.origin");
                String str = string3 == null ? BookType.local : string3;
                String string4 = JsonExtensionsKt.readString((ReadContext) doc, "$.originName");
                String str2 = string4 == null ? PackageDocumentBase.PREFIX_OPF : string4;
                String string5 = JsonExtensionsKt.readString((ReadContext) doc, "$.name");
                Intrinsics.checkNotNull(string5);
                String string6 = JsonExtensionsKt.readString((ReadContext) doc, "$.author");
                String str3 = string6 == null ? PackageDocumentBase.PREFIX_OPF : string6;
                String string7 = JsonExtensionsKt.readString((ReadContext) doc, "$.kind");
                String string8 = JsonExtensionsKt.readString((ReadContext) doc, "$.customTag");
                String string9 = JsonExtensionsKt.readString((ReadContext) doc, "$.coverUrl");
                String string10 = JsonExtensionsKt.readString((ReadContext) doc, "$.customCoverUrl");
                String string11 = JsonExtensionsKt.readString((ReadContext) doc, "$.intro");
                String string12 = JsonExtensionsKt.readString((ReadContext) doc, "$.customIntro");
                String string13 = JsonExtensionsKt.readString((ReadContext) doc, "$.charset");
                Integer num = JsonExtensionsKt.readInt((ReadContext) doc, "$.type");
                int iIntValue = num == null ? 0 : num.intValue();
                Long l = JsonExtensionsKt.readLong((ReadContext) doc, "$.group");
                long jLongValue = l == null ? 0L : l.longValue();
                String string14 = JsonExtensionsKt.readString((ReadContext) doc, "$.latestChapterTitle");
                Long l2 = JsonExtensionsKt.readLong((ReadContext) doc, "$.latestChapterTime");
                long jCurrentTimeMillis = l2 == null ? System.currentTimeMillis() : l2.longValue();
                Long l3 = JsonExtensionsKt.readLong((ReadContext) doc, "$.lastCheckTime");
                long jCurrentTimeMillis2 = l3 == null ? System.currentTimeMillis() : l3.longValue();
                Integer num2 = JsonExtensionsKt.readInt((ReadContext) doc, "$.lastCheckCount");
                int iIntValue2 = num2 == null ? 0 : num2.intValue();
                Integer num3 = JsonExtensionsKt.readInt((ReadContext) doc, "$.totalChapterNum");
                int iIntValue3 = num3 == null ? 0 : num3.intValue();
                String string15 = JsonExtensionsKt.readString((ReadContext) doc, "$.durChapterTitle");
                Integer num4 = JsonExtensionsKt.readInt((ReadContext) doc, "$.durChapterIndex");
                int iIntValue4 = num4 == null ? 0 : num4.intValue();
                Integer num5 = JsonExtensionsKt.readInt((ReadContext) doc, "$.durChapterPos");
                int iIntValue5 = num5 == null ? 0 : num5.intValue();
                Long l4 = JsonExtensionsKt.readLong((ReadContext) doc, "$.durChapterTime");
                long jCurrentTimeMillis3 = l4 == null ? System.currentTimeMillis() : l4.longValue();
                String string16 = JsonExtensionsKt.readString((ReadContext) doc, "$.wordCount");
                Boolean bool = JsonExtensionsKt.readBool((ReadContext) doc, "$.canUpdate");
                boolean zBooleanValue = bool == null ? true : bool.booleanValue();
                Integer num6 = JsonExtensionsKt.readInt((ReadContext) doc, "$.order");
                int iIntValue6 = num6 == null ? 0 : num6.intValue();
                Integer num7 = JsonExtensionsKt.readInt((ReadContext) doc, "$.originOrder");
                int iIntValue7 = num7 == null ? 0 : num7.intValue();
                Boolean bool2 = JsonExtensionsKt.readBool((ReadContext) doc, "$.useReplaceRule");
                boolean zBooleanValue2 = bool2 == null ? true : bool2.booleanValue();
                String string17 = JsonExtensionsKt.readString((ReadContext) doc, "$.variable");
                if (readConfig2 != null) {
                    boolean z = zBooleanValue2;
                    int i = iIntValue7;
                    int i2 = iIntValue6;
                    boolean z2 = zBooleanValue;
                    long j = jCurrentTimeMillis3;
                    int i3 = iIntValue5;
                    int i4 = iIntValue4;
                    int i5 = iIntValue3;
                    int i6 = iIntValue2;
                    long j2 = jCurrentTimeMillis2;
                    long j3 = jCurrentTimeMillis;
                    long j4 = jLongValue;
                    int i7 = iIntValue;
                    String str4 = str3;
                    String str5 = str2;
                    String str6 = str;
                    try {
                        Result.Companion companion2 = Result.Companion;
                        Boolean bool3 = JsonExtensionsKt.readBool((ReadContext) doc, "$.readConfig.reverseToc");
                        boolean zBooleanValue3 = bool3 == null ? false : bool3.booleanValue();
                        Integer num8 = JsonExtensionsKt.readInt((ReadContext) doc, "$.readConfig.pageAnim");
                        int iIntValue8 = num8 == null ? -1 : num8.intValue();
                        Boolean bool4 = JsonExtensionsKt.readBool((ReadContext) doc, "$.readConfig.reSegment");
                        boolean zBooleanValue4 = bool4 == null ? false : bool4.booleanValue();
                        String string18 = JsonExtensionsKt.readString((ReadContext) doc, "$.readConfig.imageStyle");
                        Boolean bool5 = JsonExtensionsKt.readBool((ReadContext) doc, "$.readConfig.useReplaceRule");
                        boolean zBooleanValue5 = bool5 == null ? false : bool5.booleanValue();
                        Long l5 = JsonExtensionsKt.readLong((ReadContext) doc, "$.readConfig.delTag");
                        obj2 = Result.constructor-impl(new ReadConfig(zBooleanValue3, iIntValue8, zBooleanValue4, string18, zBooleanValue5, l5 == null ? 0L : l5.longValue(), 0.0f, 64, null));
                    } catch (Throwable th) {
                        Result.Companion companion3 = Result.Companion;
                        obj2 = Result.constructor-impl(ResultKt.createFailure(th));
                    }
                    Object obj3 = obj2;
                    string = string;
                    string2 = string2;
                    str = str6;
                    str2 = str5;
                    string5 = string5;
                    str3 = str4;
                    string7 = string7;
                    string8 = string8;
                    string9 = string9;
                    string10 = string10;
                    string11 = string11;
                    string12 = string12;
                    string13 = string13;
                    iIntValue = i7;
                    jLongValue = j4;
                    string14 = string14;
                    jCurrentTimeMillis = j3;
                    jCurrentTimeMillis2 = j2;
                    iIntValue2 = i6;
                    iIntValue3 = i5;
                    string15 = string15;
                    iIntValue4 = i4;
                    iIntValue5 = i3;
                    jCurrentTimeMillis3 = j;
                    string16 = string16;
                    zBooleanValue = z2;
                    iIntValue6 = i2;
                    iIntValue7 = i;
                    zBooleanValue2 = z;
                    string17 = string17;
                    readConfig = (ReadConfig) (Result.isFailure-impl(obj3) ? null : obj3);
                } else {
                    readConfig = null;
                }
                Boolean bool6 = JsonExtensionsKt.readBool((ReadContext) doc, "$.isInShelf");
                boolean z3 = zBooleanValue2;
                int i8 = iIntValue7;
                int i9 = iIntValue6;
                boolean z4 = zBooleanValue;
                String str7 = string16;
                long j5 = jCurrentTimeMillis3;
                int i10 = iIntValue5;
                int i11 = iIntValue4;
                String str8 = string15;
                int i12 = iIntValue3;
                int i13 = iIntValue2;
                long j6 = jCurrentTimeMillis2;
                long j7 = jCurrentTimeMillis;
                String str9 = string14;
                long j8 = jLongValue;
                int i14 = iIntValue;
                String str10 = string13;
                String str11 = string12;
                String str12 = string11;
                String str13 = string10;
                String str14 = string9;
                String str15 = string8;
                String str16 = string7;
                String str17 = str3;
                String str18 = string5;
                String str19 = str2;
                String str20 = str;
                String str21 = string2;
                String str22 = string;
                obj = Result.constructor-impl(new Book(str22, str21, str20, str19, str18, str17, str16, str15, str14, str13, str12, str11, str10, i14, j8, str9, j7, j6, i13, i12, str8, i11, i10, j5, str7, z4, i9, i8, z3, string17, readConfig, bool6 == null ? false : bool6.booleanValue(), null, 0, 1, null));
            } catch (Throwable th2) {
                Result.Companion companion4 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th2));
            }
            return obj;
        }

        @NotNull
        /* JADX INFO: renamed from: fromJson-IoAF18A, reason: not valid java name */
        public final Object m141fromJsonIoAF18A(@NotNull String json) {
            Intrinsics.checkNotNullParameter(json, "json");
            DocumentContext documentContext = JsonExtensionsKt.getJsonPath().parse(json);
            Intrinsics.checkNotNullExpressionValue(documentContext, "jsonPath.parse(json)");
            return m140fromJsonDocIoAF18A(documentContext);
        }

        @NotNull
        /* JADX INFO: renamed from: fromJsonArray-IoAF18A, reason: not valid java name */
        public final Object m142fromJsonArrayIoAF18A(@NotNull String jsonArray) {
            Object obj;
            Intrinsics.checkNotNullParameter(jsonArray, "jsonArray");
            try {
                Result.Companion companion = Result.Companion;
                ArrayList sources = new ArrayList();
                Iterable doc = (List) JsonExtensionsKt.getJsonPath().parse(jsonArray).read("$", new Predicate[0]);
                Intrinsics.checkNotNullExpressionValue(doc, "doc");
                Iterable $this$forEach$iv = doc;
                for (Object element$iv : $this$forEach$iv) {
                    DocumentContext jsonItem = JsonExtensionsKt.getJsonPath().parse(element$iv);
                    Companion companion2 = Book.INSTANCE;
                    Intrinsics.checkNotNullExpressionValue(jsonItem, "jsonItem");
                    Object objM140fromJsonDocIoAF18A = companion2.m140fromJsonDocIoAF18A(jsonItem);
                    ResultKt.throwOnFailure(objM140fromJsonDocIoAF18A);
                    Book source = (Book) objM140fromJsonDocIoAF18A;
                    sources.add(source);
                }
                obj = Result.constructor-impl(sources);
            } catch (Throwable th) {
                Result.Companion companion3 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            return obj;
        }
    }

    /* JADX INFO: compiled from: Book.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/Book$ReadConfig.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b&\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\t\u0010'\u001a\u00020\u0003HÆ\u0003J\t\u0010(\u001a\u00020\u0005HÆ\u0003J\t\u0010)\u001a\u00020\u0003HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\bHÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u000bHÆ\u0003J\t\u0010-\u001a\u00020\rHÆ\u0003JQ\u0010.\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\rHÆ\u0001J\u0013\u0010/\u001a\u00020\u00032\b\u00100\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00101\u001a\u00020\u0005HÖ\u0001J\t\u00102\u001a\u00020\bHÖ\u0001R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\f\u001a\u00020\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010 \"\u0004\b$\u0010\"R\u001a\u0010\t\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010 \"\u0004\b&\u0010\"¨\u00063"}, d2 = {"Lio/legado/app/data/entities/Book$ReadConfig;", PackageDocumentBase.PREFIX_OPF, "reverseToc", PackageDocumentBase.PREFIX_OPF, "pageAnim", PackageDocumentBase.PREFIX_OPF, "reSegment", "imageStyle", PackageDocumentBase.PREFIX_OPF, "useReplaceRule", "delTag", PackageDocumentBase.PREFIX_OPF, "pdfImageWidth", PackageDocumentBase.PREFIX_OPF, "(ZIZLjava/lang/String;ZJF)V", "getDelTag", "()J", "setDelTag", "(J)V", "getImageStyle", "()Ljava/lang/String;", "setImageStyle", "(Ljava/lang/String;)V", "getPageAnim", "()I", "setPageAnim", "(I)V", "getPdfImageWidth", "()F", "setPdfImageWidth", "(F)V", "getReSegment", "()Z", "setReSegment", "(Z)V", "getReverseToc", "setReverseToc", "getUseReplaceRule", "setUseReplaceRule", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
    public static final /* data */ class ReadConfig {
        private boolean reverseToc;
        private int pageAnim;
        private boolean reSegment;

        @Nullable
        private String imageStyle;
        private boolean useReplaceRule;
        private long delTag;
        private float pdfImageWidth;

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
        public final ReadConfig copy(boolean reverseToc, int pageAnim, boolean reSegment, @Nullable String imageStyle, boolean useReplaceRule, long delTag, float pdfImageWidth) {
            return new ReadConfig(reverseToc, pageAnim, reSegment, imageStyle, useReplaceRule, delTag, pdfImageWidth);
        }

        public static /* synthetic */ ReadConfig copy$default(ReadConfig readConfig, boolean z, int i, boolean z2, String str, boolean z3, long j, float f, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                z = readConfig.reverseToc;
            }
            if ((i2 & 2) != 0) {
                i = readConfig.pageAnim;
            }
            if ((i2 & 4) != 0) {
                z2 = readConfig.reSegment;
            }
            if ((i2 & 8) != 0) {
                str = readConfig.imageStyle;
            }
            if ((i2 & 16) != 0) {
                z3 = readConfig.useReplaceRule;
            }
            if ((i2 & 32) != 0) {
                j = readConfig.delTag;
            }
            if ((i2 & 64) != 0) {
                f = readConfig.pdfImageWidth;
            }
            return readConfig.copy(z, i, z2, str, z3, j, f);
        }

        @NotNull
        public String toString() {
            return "ReadConfig(reverseToc=" + this.reverseToc + ", pageAnim=" + this.pageAnim + ", reSegment=" + this.reSegment + ", imageStyle=" + ((Object) this.imageStyle) + ", useReplaceRule=" + this.useReplaceRule + ", delTag=" + this.delTag + ", pdfImageWidth=" + this.pdfImageWidth + ')';
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v19, types: [int] */
        /* JADX WARN: Type inference failed for: r1v28 */
        /* JADX WARN: Type inference failed for: r1v30 */
        /* JADX WARN: Type inference failed for: r1v31 */
        /* JADX WARN: Type inference failed for: r1v32 */
        /* JADX WARN: Type inference failed for: r1v8, types: [int] */
        public int hashCode() {
            boolean z = this.reverseToc;
            if (z) {
                z = true;
            }
            int iHashCode = (((z ? 1 : 0) * 31) + Integer.hashCode(this.pageAnim)) * 31;
            boolean z2 = this.reSegment;
            ?? r1 = z2;
            if (z2) {
                r1 = 1;
            }
            int iHashCode2 = (((iHashCode + r1) * 31) + (this.imageStyle == null ? 0 : this.imageStyle.hashCode())) * 31;
            boolean z3 = this.useReplaceRule;
            ?? r12 = z3;
            if (z3) {
                r12 = 1;
            }
            return ((((iHashCode2 + r12) * 31) + Long.hashCode(this.delTag)) * 31) + Float.hashCode(this.pdfImageWidth);
        }

        public boolean equals(@Nullable Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof ReadConfig)) {
                return false;
            }
            ReadConfig readConfig = (ReadConfig) other;
            return this.reverseToc == readConfig.reverseToc && this.pageAnim == readConfig.pageAnim && this.reSegment == readConfig.reSegment && Intrinsics.areEqual(this.imageStyle, readConfig.imageStyle) && this.useReplaceRule == readConfig.useReplaceRule && this.delTag == readConfig.delTag && Intrinsics.areEqual(Float.valueOf(this.pdfImageWidth), Float.valueOf(readConfig.pdfImageWidth));
        }

        public ReadConfig() {
            this(false, 0, false, null, false, 0L, 0.0f, 127, null);
        }

        public ReadConfig(boolean reverseToc, int pageAnim, boolean reSegment, @Nullable String imageStyle, boolean useReplaceRule, long delTag, float pdfImageWidth) {
            this.reverseToc = reverseToc;
            this.pageAnim = pageAnim;
            this.reSegment = reSegment;
            this.imageStyle = imageStyle;
            this.useReplaceRule = useReplaceRule;
            this.delTag = delTag;
            this.pdfImageWidth = pdfImageWidth;
        }

        public /* synthetic */ ReadConfig(boolean z, int i, boolean z2, String str, boolean z3, long j, float f, int i2, DefaultConstructorMarker defaultConstructorMarker) {
            this((i2 & 1) != 0 ? false : z, (i2 & 2) != 0 ? -1 : i, (i2 & 4) != 0 ? false : z2, (i2 & 8) != 0 ? null : str, (i2 & 16) != 0 ? false : z3, (i2 & 32) != 0 ? 0L : j, (i2 & 64) != 0 ? 800.0f : f);
        }

        public final boolean getReverseToc() {
            return this.reverseToc;
        }

        public final void setReverseToc(boolean z) {
            this.reverseToc = z;
        }

        public final int getPageAnim() {
            return this.pageAnim;
        }

        public final void setPageAnim(int i) {
            this.pageAnim = i;
        }

        public final boolean getReSegment() {
            return this.reSegment;
        }

        public final void setReSegment(boolean z) {
            this.reSegment = z;
        }

        @Nullable
        public final String getImageStyle() {
            return this.imageStyle;
        }

        public final void setImageStyle(@Nullable String str) {
            this.imageStyle = str;
        }

        public final boolean getUseReplaceRule() {
            return this.useReplaceRule;
        }

        public final void setUseReplaceRule(boolean z) {
            this.useReplaceRule = z;
        }

        public final long getDelTag() {
            return this.delTag;
        }

        public final void setDelTag(long j) {
            this.delTag = j;
        }

        public final float getPdfImageWidth() {
            return this.pdfImageWidth;
        }

        public final void setPdfImageWidth(float f) {
            this.pdfImageWidth = f;
        }
    }

    /* JADX INFO: compiled from: Book.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/Book$Converters.class */
    @Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006J(\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\b2\b\u0010\t\u001a\u0004\u0018\u00010\u0004ø\u0001\u0000ø\u0001\u0001ø\u0001\u0002¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b¡\u001e0\u0001¨\u0006\f"}, d2 = {"Lio/legado/app/data/entities/Book$Converters;", PackageDocumentBase.PREFIX_OPF, "()V", "readConfigToString", PackageDocumentBase.PREFIX_OPF, "config", "Lio/legado/app/data/entities/Book$ReadConfig;", "stringToReadConfig", "Lkotlin/Result;", "json", "stringToReadConfig-IoAF18A", "(Ljava/lang/String;)Ljava/lang/Object;", "reader-pro"})
    public static final class Converters {
        @NotNull
        public final String readConfigToString(@Nullable ReadConfig config) {
            String json = GsonExtensionsKt.getGSON().toJson(config);
            Intrinsics.checkNotNullExpressionValue(json, "GSON.toJson(config)");
            return json;
        }

        @NotNull
        /* JADX INFO: renamed from: stringToReadConfig-IoAF18A, reason: not valid java name */
        public final Object m143stringToReadConfigIoAF18A(@Nullable String json) {
            Object obj;
            Gson $this$fromJsonObject$iv = GsonExtensionsKt.getGSON();
            try {
                Result.Companion companion = Result.Companion;
                Type type = new Book$Converters$stringToReadConfigIoAF18A$$inlined$fromJsonObject$1().getType();
                Intrinsics.checkNotNullExpressionValue(type, "object : TypeToken<T>() {}.type");
                Object objFromJson = $this$fromJsonObject$iv.fromJson(json, type);
                if (!(objFromJson instanceof ReadConfig)) {
                    objFromJson = null;
                }
                obj = Result.constructor-impl((ReadConfig) objFromJson);
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                obj = Result.constructor-impl(ResultKt.createFailure(th));
            }
            return obj;
        }
    }
}
