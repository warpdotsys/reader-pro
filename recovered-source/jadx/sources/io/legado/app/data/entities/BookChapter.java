package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.utils.GsonExtensionsKt;
import io.legado.app.utils.MD5Utils;
import io.legado.app.utils.NetworkUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookChapter.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookChapter.class */
@JsonIgnoreProperties({"variableMap", "_userNameSpace", "userNameSpace"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b)\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0013J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\u0010\u0010?\u001a\u0004\u0018\u00010\u000eHÆ\u0003¢\u0006\u0002\u0010\u001cJ\u000b\u0010@\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u0010C\u001a\u00020\u0003HÆ\u0003J\t\u0010D\u001a\u00020\u0006HÆ\u0003J\t\u0010E\u001a\u00020\u0003HÆ\u0003J\t\u0010F\u001a\u00020\u0003HÆ\u0003J\t\u0010G\u001a\u00020\nHÆ\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010I\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0010\u0010J\u001a\u0004\u0018\u00010\u000eHÆ\u0003¢\u0006\u0002\u0010\u001cJ\u009e\u0001\u0010K\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003HÆ\u0001¢\u0006\u0002\u0010LJ\u0013\u0010M\u001a\u00020\u00062\b\u0010N\u001a\u0004\u0018\u00010OH\u0096\u0002J\u0006\u0010P\u001a\u00020\u0003J\u0006\u0010Q\u001a\u00020\u0003J\b\u0010R\u001a\u00020\u0003H\u0016J\b\u0010S\u001a\u00020\nH\u0016J\u001a\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020\u00032\b\u0010W\u001a\u0004\u0018\u00010\u0003H\u0016J\u000e\u0010X\u001a\u00020U2\u0006\u0010Y\u001a\u00020\u0003J\t\u0010Z\u001a\u00020\u0003HÖ\u0001R\u000e\u0010\u0014\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0016\"\u0004\b\u001a\u0010\u0018R\u001e\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0016\"\u0004\b!\u0010\u0018R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010&\"\u0004\b'\u0010(R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0016\"\u0004\b*\u0010\u0018R\u001e\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e¢\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b+\u0010\u001c\"\u0004\b,\u0010\u001eR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0016\"\u0004\b.\u0010\u0018R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0016\"\u0004\b0\u0010\u0018R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0016\"\u0004\b2\u0010\u0018R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0016\"\u0004\b4\u0010\u0018R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0016\"\u0004\b6\u0010\u0018R7\u00107\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000308j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`98VX\u0096\u0084\u0002¢\u0006\f\n\u0004\b<\u0010=\u001a\u0004\b:\u0010;¨\u0006["}, d2 = {"Lio/legado/app/data/entities/BookChapter;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", RSSKeywords.RSS_ITEM_URL, PackageDocumentBase.PREFIX_OPF, "title", "isVolume", PackageDocumentBase.PREFIX_OPF, "baseUrl", "bookUrl", "index", PackageDocumentBase.PREFIX_OPF, "resourceUrl", "tag", "start", PackageDocumentBase.PREFIX_OPF, "end", "startFragmentId", "endFragmentId", "variable", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "_userNameSpace", "getBaseUrl", "()Ljava/lang/String;", "setBaseUrl", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getEnd", "()Ljava/lang/Long;", "setEnd", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getEndFragmentId", "setEndFragmentId", "getIndex", "()I", "setIndex", "(I)V", "()Z", "setVolume", "(Z)V", "getResourceUrl", "setResourceUrl", "getStart", "setStart", "getStartFragmentId", "setStartFragmentId", "getTag", "setTag", "getTitle", "setTitle", "getUrl", "setUrl", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/BookChapter;", "equals", "other", PackageDocumentBase.PREFIX_OPF, "getAbsoluteURL", "getFileName", "getUserNameSpace", "hashCode", "putVariable", PackageDocumentBase.PREFIX_OPF, "key", "value", "setUserNameSpace", "nameSpace", "toString", "reader-pro"})
public final /* data */ class BookChapter implements RuleDataInterface {

    @NotNull
    private String url;

    @NotNull
    private String title;
    private boolean isVolume;

    @NotNull
    private String baseUrl;

    @NotNull
    private String bookUrl;
    private int index;

    @Nullable
    private String resourceUrl;

    @Nullable
    private String tag;

    @Nullable
    private Long start;

    @Nullable
    private Long end;

    @Nullable
    private String startFragmentId;

    @Nullable
    private String endFragmentId;

    @Nullable
    private String variable;

    /* JADX INFO: renamed from: variableMap$delegate, reason: from kotlin metadata */
    @NotNull
    private final transient Lazy variableMap;

    @NotNull
    private transient String _userNameSpace;

    @NotNull
    public final String component1() {
        return this.url;
    }

    @NotNull
    public final String component2() {
        return this.title;
    }

    public final boolean component3() {
        return this.isVolume;
    }

    @NotNull
    public final String component4() {
        return this.baseUrl;
    }

    @NotNull
    public final String component5() {
        return this.bookUrl;
    }

    public final int component6() {
        return this.index;
    }

    @Nullable
    public final String component7() {
        return this.resourceUrl;
    }

    @Nullable
    public final String component8() {
        return this.tag;
    }

    @Nullable
    public final Long component9() {
        return this.start;
    }

    @Nullable
    public final Long component10() {
        return this.end;
    }

    @Nullable
    public final String component11() {
        return this.startFragmentId;
    }

    @Nullable
    public final String component12() {
        return this.endFragmentId;
    }

    @Nullable
    public final String component13() {
        return this.variable;
    }

    @NotNull
    public final BookChapter copy(@NotNull String url, @NotNull String title, boolean isVolume, @NotNull String baseUrl, @NotNull String bookUrl, int index, @Nullable String resourceUrl, @Nullable String tag, @Nullable Long start, @Nullable Long end, @Nullable String startFragmentId, @Nullable String endFragmentId, @Nullable String variable) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(baseUrl, "baseUrl");
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        return new BookChapter(url, title, isVolume, baseUrl, bookUrl, index, resourceUrl, tag, start, end, startFragmentId, endFragmentId, variable);
    }

    public static /* synthetic */ BookChapter copy$default(BookChapter bookChapter, String str, String str2, boolean z, String str3, String str4, int i, String str5, String str6, Long l, Long l2, String str7, String str8, String str9, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            str = bookChapter.url;
        }
        if ((i2 & 2) != 0) {
            str2 = bookChapter.title;
        }
        if ((i2 & 4) != 0) {
            z = bookChapter.isVolume;
        }
        if ((i2 & 8) != 0) {
            str3 = bookChapter.baseUrl;
        }
        if ((i2 & 16) != 0) {
            str4 = bookChapter.bookUrl;
        }
        if ((i2 & 32) != 0) {
            i = bookChapter.index;
        }
        if ((i2 & 64) != 0) {
            str5 = bookChapter.resourceUrl;
        }
        if ((i2 & Wbxml.EXT_T_0) != 0) {
            str6 = bookChapter.tag;
        }
        if ((i2 & 256) != 0) {
            l = bookChapter.start;
        }
        if ((i2 & 512) != 0) {
            l2 = bookChapter.end;
        }
        if ((i2 & 1024) != 0) {
            str7 = bookChapter.startFragmentId;
        }
        if ((i2 & 2048) != 0) {
            str8 = bookChapter.endFragmentId;
        }
        if ((i2 & 4096) != 0) {
            str9 = bookChapter.variable;
        }
        return bookChapter.copy(str, str2, z, str3, str4, i, str5, str6, l, l2, str7, str8, str9);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BookChapter(url=").append(this.url).append(", title=").append(this.title).append(", isVolume=").append(this.isVolume).append(", baseUrl=").append(this.baseUrl).append(", bookUrl=").append(this.bookUrl).append(", index=").append(this.index).append(", resourceUrl=").append((Object) this.resourceUrl).append(", tag=").append((Object) this.tag).append(", start=").append(this.start).append(", end=").append(this.end).append(", startFragmentId=").append((Object) this.startFragmentId).append(", endFragmentId=");
        sb.append((Object) this.endFragmentId).append(", variable=").append((Object) this.variable).append(')');
        return sb.toString();
    }

    public BookChapter() {
        this(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
    }

    public BookChapter(@NotNull String url, @NotNull String title, boolean isVolume, @NotNull String baseUrl, @NotNull String bookUrl, int index, @Nullable String resourceUrl, @Nullable String tag, @Nullable Long start, @Nullable Long end, @Nullable String startFragmentId, @Nullable String endFragmentId, @Nullable String variable) {
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(baseUrl, "baseUrl");
        Intrinsics.checkNotNullParameter(bookUrl, "bookUrl");
        this.url = url;
        this.title = title;
        this.isVolume = isVolume;
        this.baseUrl = baseUrl;
        this.bookUrl = bookUrl;
        this.index = index;
        this.resourceUrl = resourceUrl;
        this.tag = tag;
        this.start = start;
        this.end = end;
        this.startFragmentId = startFragmentId;
        this.endFragmentId = endFragmentId;
        this.variable = variable;
        this.variableMap = LazyKt.lazy(new BookChapter$variableMap$2(this));
        this._userNameSpace = PackageDocumentBase.PREFIX_OPF;
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @Nullable
    public String getVariable(@NotNull String key) {
        return RuleDataInterface.DefaultImpls.getVariable(this, key);
    }

    public /* synthetic */ BookChapter(String str, String str2, boolean z, String str3, String str4, int i, String str5, String str6, Long l, Long l2, String str7, String str8, String str9, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i2 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i2 & 4) != 0 ? false : z, (i2 & 8) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i2 & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i2 & 32) != 0 ? 0 : i, (i2 & 64) != 0 ? null : str5, (i2 & Wbxml.EXT_T_0) != 0 ? null : str6, (i2 & 256) != 0 ? null : l, (i2 & 512) != 0 ? null : l2, (i2 & 1024) != 0 ? null : str7, (i2 & 2048) != 0 ? null : str8, (i2 & 4096) != 0 ? null : str9);
    }

    @NotNull
    public final String getUrl() {
        return this.url;
    }

    public final void setUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.url = str;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.title = str;
    }

    public final boolean isVolume() {
        return this.isVolume;
    }

    public final void setVolume(boolean z) {
        this.isVolume = z;
    }

    @NotNull
    public final String getBaseUrl() {
        return this.baseUrl;
    }

    public final void setBaseUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.baseUrl = str;
    }

    @NotNull
    public final String getBookUrl() {
        return this.bookUrl;
    }

    public final void setBookUrl(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.bookUrl = str;
    }

    public final int getIndex() {
        return this.index;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    @Nullable
    public final String getResourceUrl() {
        return this.resourceUrl;
    }

    public final void setResourceUrl(@Nullable String str) {
        this.resourceUrl = str;
    }

    @Nullable
    public final String getTag() {
        return this.tag;
    }

    public final void setTag(@Nullable String str) {
        this.tag = str;
    }

    @Nullable
    public final Long getStart() {
        return this.start;
    }

    public final void setStart(@Nullable Long l) {
        this.start = l;
    }

    @Nullable
    public final Long getEnd() {
        return this.end;
    }

    public final void setEnd(@Nullable Long l) {
        this.end = l;
    }

    @Nullable
    public final String getStartFragmentId() {
        return this.startFragmentId;
    }

    public final void setStartFragmentId(@Nullable String str) {
        this.startFragmentId = str;
    }

    @Nullable
    public final String getEndFragmentId() {
        return this.endFragmentId;
    }

    public final void setEndFragmentId(@Nullable String str) {
        this.endFragmentId = str;
    }

    @Nullable
    public final String getVariable() {
        return this.variable;
    }

    public final void setVariable(@Nullable String str) {
        this.variable = str;
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

    public int hashCode() {
        return this.url.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (other instanceof BookChapter) {
            return Intrinsics.areEqual(((BookChapter) other).url, this.url);
        }
        return false;
    }

    @NotNull
    public final String getAbsoluteURL() {
        String strSubstring;
        Matcher urlMatcher = AnalyzeUrl.INSTANCE.getParamPattern().matcher(this.url);
        if (urlMatcher.find()) {
            String str = this.url;
            int iStart = urlMatcher.start();
            if (str == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            strSubstring = str.substring(0, iStart);
            Intrinsics.checkNotNullExpressionValue(strSubstring, "(this as java.lang.Strin…ing(startIndex, endIndex)");
        } else {
            strSubstring = this.url;
        }
        String urlBefore = strSubstring;
        String urlAbsoluteBefore = NetworkUtils.INSTANCE.getAbsoluteURL(this.baseUrl, urlBefore);
        if (urlBefore.length() == this.url.length()) {
            return urlAbsoluteBefore;
        }
        StringBuilder sbAppend = new StringBuilder().append(urlAbsoluteBefore).append(',');
        String str2 = this.url;
        int iEnd = urlMatcher.end();
        if (str2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
        }
        String strSubstring2 = str2.substring(iEnd);
        Intrinsics.checkNotNullExpressionValue(strSubstring2, "(this as java.lang.String).substring(startIndex)");
        return sbAppend.append(strSubstring2).toString();
    }

    @NotNull
    public final String getFileName() {
        StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
        Object[] objArr = {Integer.valueOf(this.index), MD5Utils.INSTANCE.md5Encode16(this.title)};
        String str = String.format("%05d-%s.nb", Arrays.copyOf(objArr, objArr.length));
        Intrinsics.checkNotNullExpressionValue(str, "java.lang.String.format(format, *args)");
        return str;
    }
}
