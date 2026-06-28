package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.data.entities.BaseBook;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: SearchBook.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/SearchBook.class */
@JsonIgnoreProperties({"variableMap", "infoHtml", "tocHtml", "origins", "kindList"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00000\u0002B§\u0001\u0012\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0004\u0012\b\b\u0002\u0010\n\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0004\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0004\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b¢\u0006\u0002\u0010\u0015J\u000e\u0010N\u001a\u00020O2\u0006\u0010\u0005\u001a\u00020\u0004J\u0011\u0010P\u001a\u00020\b2\u0006\u0010Q\u001a\u00020\u0000H\u0096\u0002J\t\u0010R\u001a\u00020\u0004HÆ\u0003J\u000b\u0010S\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\t\u0010U\u001a\u00020\u0004HÆ\u0003J\t\u0010V\u001a\u00020\u0012HÆ\u0003J\u000b\u0010W\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\t\u0010X\u001a\u00020\bHÆ\u0003J\t\u0010Y\u001a\u00020\u0004HÆ\u0003J\t\u0010Z\u001a\u00020\u0004HÆ\u0003J\t\u0010[\u001a\u00020\bHÆ\u0003J\t\u0010\\\u001a\u00020\u0004HÆ\u0003J\t\u0010]\u001a\u00020\u0004HÆ\u0003J\u000b\u0010^\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u0004HÆ\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0004HÆ\u0003J«\u0001\u0010a\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\u00042\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0010\u001a\u00020\u00042\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0014\u001a\u00020\bHÆ\u0001J\u0013\u0010b\u001a\u00020c2\b\u0010Q\u001a\u0004\u0018\u00010dH\u0096\u0002J\b\u0010e\u001a\u00020\u0004H\u0016J\b\u0010f\u001a\u00020\bH\u0016J\u001a\u0010g\u001a\u00020O2\u0006\u0010h\u001a\u00020\u00042\b\u0010i\u001a\u0004\u0018\u00010\u0004H\u0016J\u000e\u0010j\u001a\u00020O2\u0006\u0010k\u001a\u00020\u0004J\u0006\u0010l\u001a\u00020mJ\t\u0010n\u001a\u00020\u0004HÖ\u0001R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0018\"\u0004\b\u001c\u0010\u001aR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0018\"\u0004\b\u001e\u0010\u001aR\u001c\u0010\u001f\u001a\u0004\u0018\u00010\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\u001aR\u001c\u0010\r\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0018\"\u0004\b#\u0010\u001aR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0018\"\u0004\b%\u0010\u001aR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0018\"\u0004\b'\u0010\u001aR\u001a\u0010\t\u001a\u00020\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0018\"\u0004\b)\u0010\u001aR\u001a\u0010\u0005\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0018\"\u0004\b+\u0010\u001aR\u001a\u0010\u0006\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0018\"\u0004\b-\u0010\u001aR\u001a\u0010\u0014\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101RF\u00105\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0018\u000103j\n\u0012\u0004\u0012\u00020\u0004\u0018\u0001`42\u001a\u00102\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0018\u000103j\n\u0012\u0004\u0012\u00020\u0004\u0018\u0001`4@BX\u0086\u000e¢\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b8\u00109\"\u0004\b:\u0010;R\u001c\u0010<\u001a\u0004\u0018\u00010\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u0018\"\u0004\b>\u0010\u001aR\u001a\u0010\u0010\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\u0018\"\u0004\b@\u0010\u001aR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bA\u0010/\"\u0004\bB\u00101R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0018\"\u0004\bD\u0010\u001aR7\u0010E\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040Fj\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004`G8VX\u0096\u0084\u0002¢\u0006\f\n\u0004\bJ\u0010K\u001a\u0004\bH\u0010IR\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0004X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bL\u0010\u0018\"\u0004\bM\u0010\u001a¨\u0006o"}, d2 = {"Lio/legado/app/data/entities/SearchBook;", "Lio/legado/app/data/entities/BaseBook;", PackageDocumentBase.PREFIX_OPF, "bookUrl", PackageDocumentBase.PREFIX_OPF, "origin", "originName", "type", PackageDocumentBase.PREFIX_OPF, "name", "author", "kind", "coverUrl", "intro", "wordCount", "latestChapterTitle", "tocUrl", RSSKeywords.RSS_ITEM_TIME, PackageDocumentBase.PREFIX_OPF, "variable", "originOrder", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;I)V", "_userNameSpace", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getCoverUrl", "setCoverUrl", "infoHtml", "getInfoHtml", "setInfoHtml", "getIntro", "setIntro", "getKind", "setKind", "getLatestChapterTitle", "setLatestChapterTitle", "getName", "setName", "getOrigin", "setOrigin", "getOriginName", "setOriginName", "getOriginOrder", "()I", "setOriginOrder", "(I)V", "<set-?>", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "origins", "getOrigins", "()Ljava/util/LinkedHashSet;", "getTime", "()J", "setTime", "(J)V", "tocHtml", "getTocHtml", "setTocHtml", "getTocUrl", "setTocUrl", "getType", "setType", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "getWordCount", "setWordCount", "addOrigin", PackageDocumentBase.PREFIX_OPF, "compareTo", "other", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "getUserNameSpace", "hashCode", "putVariable", "key", "value", "setUserNameSpace", "nameSpace", "toBook", "Lio/legado/app/data/entities/Book;", "toString", "reader-pro"})
public final /* data */ class SearchBook implements BaseBook, Comparable<SearchBook> {

    @NotNull
    private String bookUrl;

    @NotNull
    private String origin;

    @NotNull
    private String originName;
    private int type;

    @NotNull
    private String name;

    @NotNull
    private String author;

    @Nullable
    private String kind;

    @Nullable
    private String coverUrl;

    @Nullable
    private String intro;

    @Nullable
    private String wordCount;

    @Nullable
    private String latestChapterTitle;

    @NotNull
    private String tocUrl;
    private long time;

    @Nullable
    private String variable;
    private int originOrder;

    @Nullable
    private String infoHtml;

    @Nullable
    private String tocHtml;

    /* JADX INFO: renamed from: variableMap$delegate, reason: from kotlin metadata */
    @NotNull
    private final transient Lazy variableMap;

    @NotNull
    private transient String _userNameSpace;

    @Nullable
    private LinkedHashSet<String> origins;

    @NotNull
    public final String component1() {
        return getBookUrl();
    }

    @NotNull
    public final String component2() {
        return this.origin;
    }

    @NotNull
    public final String component3() {
        return this.originName;
    }

    public final int component4() {
        return this.type;
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
        return this.coverUrl;
    }

    @Nullable
    public final String component9() {
        return this.intro;
    }

    @Nullable
    public final String component10() {
        return getWordCount();
    }

    @Nullable
    public final String component11() {
        return this.latestChapterTitle;
    }

    @NotNull
    public final String component12() {
        return this.tocUrl;
    }

    public final long component13() {
        return this.time;
    }

    @Nullable
    public final String component14() {
        return this.variable;
    }

    public final int component15() {
        return this.originOrder;
    }

    @NotNull
    public final SearchBook copy(@NotNull String bookUrl, @NotNull String origin, @NotNull String originName, int type, @NotNull String name, @NotNull String author, @Nullable String kind, @Nullable String coverUrl, @Nullable String intro, @Nullable String wordCount, @Nullable String latestChapterTitle, @NotNull String tocUrl, long time, @Nullable String variable, int originOrder) {
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(originName, "originName");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(tocUrl, "tocUrl");
        return new SearchBook(bookUrl, origin, originName, type, name, author, kind, coverUrl, intro, wordCount, latestChapterTitle, tocUrl, time, variable, originOrder);
    }

    public static /* synthetic */ SearchBook copy$default(SearchBook searchBook, String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, long j, String str12, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = searchBook.getBookUrl();
        }
        if ((i3 & 2) != 0) {
            str2 = searchBook.origin;
        }
        if ((i3 & 4) != 0) {
            str3 = searchBook.originName;
        }
        if ((i3 & 8) != 0) {
            i = searchBook.type;
        }
        if ((i3 & 16) != 0) {
            str4 = searchBook.getName();
        }
        if ((i3 & 32) != 0) {
            str5 = searchBook.getAuthor();
        }
        if ((i3 & 64) != 0) {
            str6 = searchBook.getKind();
        }
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            str7 = searchBook.coverUrl;
        }
        if ((i3 & 256) != 0) {
            str8 = searchBook.intro;
        }
        if ((i3 & 512) != 0) {
            str9 = searchBook.getWordCount();
        }
        if ((i3 & 1024) != 0) {
            str10 = searchBook.latestChapterTitle;
        }
        if ((i3 & 2048) != 0) {
            str11 = searchBook.tocUrl;
        }
        if ((i3 & 4096) != 0) {
            j = searchBook.time;
        }
        if ((i3 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
            str12 = searchBook.variable;
        }
        if ((i3 & 16384) != 0) {
            i2 = searchBook.originOrder;
        }
        return searchBook.copy(str, str2, str3, i, str4, str5, str6, str7, str8, str9, str10, str11, j, str12, i2);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SearchBook(bookUrl=").append(getBookUrl()).append(", origin=").append(this.origin).append(", originName=").append(this.originName).append(", type=").append(this.type).append(", name=").append(getName()).append(", author=").append(getAuthor()).append(", kind=").append((Object) getKind()).append(", coverUrl=").append((Object) this.coverUrl).append(", intro=").append((Object) this.intro).append(", wordCount=").append((Object) getWordCount()).append(", latestChapterTitle=").append((Object) this.latestChapterTitle).append(", tocUrl=");
        sb.append(this.tocUrl).append(", time=").append(this.time).append(", variable=").append((Object) this.variable).append(", originOrder=").append(this.originOrder).append(')');
        return sb.toString();
    }

    public SearchBook() {
        this(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
    }

    public SearchBook(@NotNull String bookUrl, @NotNull String origin, @NotNull String originName, int type, @NotNull String name, @NotNull String author, @Nullable String kind, @Nullable String coverUrl, @Nullable String intro, @Nullable String wordCount, @Nullable String latestChapterTitle, @NotNull String tocUrl, long time, @Nullable String variable, int originOrder) {
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(originName, "originName");
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(author, "author");
        Intrinsics.checkNotNullParameter(tocUrl, "tocUrl");
        this.bookUrl = bookUrl;
        this.origin = origin;
        this.originName = originName;
        this.type = type;
        this.name = name;
        this.author = author;
        this.kind = kind;
        this.coverUrl = coverUrl;
        this.intro = intro;
        this.wordCount = wordCount;
        this.latestChapterTitle = latestChapterTitle;
        this.tocUrl = tocUrl;
        this.time = time;
        this.variable = variable;
        this.originOrder = originOrder;
        this.variableMap = LazyKt.lazy(new SearchBook$variableMap$2(this));
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

    public /* synthetic */ SearchBook(String str, String str2, String str3, int i, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, long j, String str12, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i3 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i3 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i3 & 8) != 0 ? 0 : i, (i3 & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i3 & 32) != 0 ? PackageDocumentBase.PREFIX_OPF : str5, (i3 & 64) != 0 ? null : str6, (i3 & Wbxml.EXT_T_0) != 0 ? null : str7, (i3 & 256) != 0 ? null : str8, (i3 & 512) != 0 ? null : str9, (i3 & 1024) != 0 ? null : str10, (i3 & 2048) != 0 ? PackageDocumentBase.PREFIX_OPF : str11, (i3 & 4096) != 0 ? 0L : j, (i3 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? null : str12, (i3 & 16384) != 0 ? 0 : i2);
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

    public final int getType() {
        return this.type;
    }

    public final void setType(int i) {
        this.type = i;
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
    public final String getCoverUrl() {
        return this.coverUrl;
    }

    public final void setCoverUrl(@Nullable String str) {
        this.coverUrl = str;
    }

    @Nullable
    public final String getIntro() {
        return this.intro;
    }

    public final void setIntro(@Nullable String str) {
        this.intro = str;
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

    @Nullable
    public final String getLatestChapterTitle() {
        return this.latestChapterTitle;
    }

    public final void setLatestChapterTitle(@Nullable String str) {
        this.latestChapterTitle = str;
    }

    @NotNull
    public final String getTocUrl() {
        return this.tocUrl;
    }

    public final void setTocUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.tocUrl = str;
    }

    public final long getTime() {
        return this.time;
    }

    public final void setTime(long j) {
        this.time = j;
    }

    @Nullable
    public final String getVariable() {
        return this.variable;
    }

    public final void setVariable(@Nullable String str) {
        this.variable = str;
    }

    public final int getOriginOrder() {
        return this.originOrder;
    }

    public final void setOriginOrder(int i) {
        this.originOrder = i;
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

    public boolean equals(@Nullable Object other) {
        if ((other instanceof SearchBook) && Intrinsics.areEqual(((SearchBook) other).getBookUrl(), getBookUrl())) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return getBookUrl().hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(@NotNull SearchBook other) {
        Intrinsics.checkNotNullParameter(other, "other");
        return other.originOrder - this.originOrder;
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

    public final void setUserNameSpace(@NotNull String nameSpace) {
        Intrinsics.checkNotNullParameter(nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @NotNull
    public String getUserNameSpace() {
        return this._userNameSpace;
    }

    @Nullable
    public final LinkedHashSet<String> getOrigins() {
        return this.origins;
    }

    public final void addOrigin(@NotNull String origin) {
        Intrinsics.checkNotNullParameter(origin, "origin");
        if (this.origins == null) {
            this.origins = SetsKt.linkedSetOf(new String[]{this.origin});
        }
        LinkedHashSet<String> linkedHashSet = this.origins;
        if (linkedHashSet == null) {
            return;
        }
        linkedHashSet.add(origin);
    }

    @NotNull
    public final Book toBook() {
        String name = getName();
        String author = getAuthor();
        String kind = getKind();
        String bookUrl = getBookUrl();
        String str = this.origin;
        String str2 = this.originName;
        int i = this.type;
        String wordCount = getWordCount();
        String str3 = this.latestChapterTitle;
        Book $this$toBook_u24lambda_u2d0 = new Book(bookUrl, this.tocUrl, str, str2, name, author, kind, null, this.coverUrl, null, this.intro, null, null, i, 0L, str3, 0L, 0L, 0, 0, null, 0, 0, 0L, wordCount, false, 0, 0, false, this.variable, null, false, null, -553690496, 1, null);
        $this$toBook_u24lambda_u2d0.setInfoHtml(getInfoHtml());
        $this$toBook_u24lambda_u2d0.setTocUrl(getTocUrl());
        $this$toBook_u24lambda_u2d0.setUserNameSpace(getUserNameSpace());
        return $this$toBook_u24lambda_u2d0;
    }
}
