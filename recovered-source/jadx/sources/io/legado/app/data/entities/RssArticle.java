package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.legado.app.constant.RSSKeywords;
import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: RssArticle.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/RssArticle.class */
@JsonIgnoreProperties({"variableMap", "_userNameSpace", "userNameSpace"})
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u00002\u00020\u0001B}\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0010J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u000eHÆ\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u0007HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0081\u0001\u0010@\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010A\u001a\u00020\u000e2\b\u0010B\u001a\u0004\u0018\u00010CH\u0096\u0002J\b\u0010D\u001a\u00020\u0003H\u0016J\b\u0010E\u001a\u00020FH\u0016J\u001a\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\u00032\b\u0010J\u001a\u0004\u0018\u00010\u0003H\u0016J\u000e\u0010K\u001a\u00020H2\u0006\u0010L\u001a\u00020\u0003J\t\u0010M\u001a\u00020\u0003HÖ\u0001R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0013\"\u0004\b\u0017\u0010\u0015R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0013\"\u0004\b\u0019\u0010\u0015R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0013\"\u0004\b!\u0010\u0015R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0013\"\u0004\b#\u0010\u0015R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0013\"\u0004\b)\u0010\u0015R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0013\"\u0004\b+\u0010\u0015R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0013\"\u0004\b-\u0010\u0015R7\u0010.\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030/j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`08VX\u0096\u0084\u0002¢\u0006\f\n\u0004\b3\u00104\u001a\u0004\b1\u00102¨\u0006N"}, d2 = {"Lio/legado/app/data/entities/RssArticle;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "origin", PackageDocumentBase.PREFIX_OPF, "sort", "title", "order", PackageDocumentBase.PREFIX_OPF, "link", RSSKeywords.RSS_ITEM_PUB_DATE, "description", "content", "image", "read", PackageDocumentBase.PREFIX_OPF, "variable", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", "_userNameSpace", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getDescription", "setDescription", "getImage", "setImage", "getLink", "setLink", "getOrder", "()J", "setOrder", "(J)V", "getOrigin", "setOrigin", "getPubDate", "setPubDate", "getRead", "()Z", "setRead", "(Z)V", "getSort", "setSort", "getTitle", "setTitle", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", PackageDocumentBase.PREFIX_OPF, "getUserNameSpace", "hashCode", PackageDocumentBase.PREFIX_OPF, "putVariable", PackageDocumentBase.PREFIX_OPF, "key", "value", "setUserNameSpace", "nameSpace", "toString", "reader-pro"})
public final /* data */ class RssArticle implements RuleDataInterface {

    @NotNull
    private String origin;

    @NotNull
    private String sort;

    @NotNull
    private String title;
    private long order;

    @NotNull
    private String link;

    @Nullable
    private String pubDate;

    @Nullable
    private String description;

    @Nullable
    private String content;

    @Nullable
    private String image;
    private boolean read;

    @Nullable
    private String variable;

    /* JADX INFO: renamed from: variableMap$delegate, reason: from kotlin metadata */
    @NotNull
    private final transient Lazy variableMap;

    @NotNull
    private transient String _userNameSpace;

    @NotNull
    public final String component1() {
        return this.origin;
    }

    @NotNull
    public final String component2() {
        return this.sort;
    }

    @NotNull
    public final String component3() {
        return this.title;
    }

    public final long component4() {
        return this.order;
    }

    @NotNull
    public final String component5() {
        return this.link;
    }

    @Nullable
    public final String component6() {
        return this.pubDate;
    }

    @Nullable
    public final String component7() {
        return this.description;
    }

    @Nullable
    public final String component8() {
        return this.content;
    }

    @Nullable
    public final String component9() {
        return this.image;
    }

    public final boolean component10() {
        return this.read;
    }

    @Nullable
    public final String component11() {
        return this.variable;
    }

    @NotNull
    public final RssArticle copy(@NotNull String origin, @NotNull String sort, @NotNull String title, long order, @NotNull String link, @Nullable String pubDate, @Nullable String description, @Nullable String content, @Nullable String image, boolean read, @Nullable String variable) {
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(sort, "sort");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(link, "link");
        return new RssArticle(origin, sort, title, order, link, pubDate, description, content, image, read, variable);
    }

    public static /* synthetic */ RssArticle copy$default(RssArticle rssArticle, String str, String str2, String str3, long j, String str4, String str5, String str6, String str7, String str8, boolean z, String str9, int i, Object obj) {
        if ((i & 1) != 0) {
            str = rssArticle.origin;
        }
        if ((i & 2) != 0) {
            str2 = rssArticle.sort;
        }
        if ((i & 4) != 0) {
            str3 = rssArticle.title;
        }
        if ((i & 8) != 0) {
            j = rssArticle.order;
        }
        if ((i & 16) != 0) {
            str4 = rssArticle.link;
        }
        if ((i & 32) != 0) {
            str5 = rssArticle.pubDate;
        }
        if ((i & 64) != 0) {
            str6 = rssArticle.description;
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str7 = rssArticle.content;
        }
        if ((i & 256) != 0) {
            str8 = rssArticle.image;
        }
        if ((i & 512) != 0) {
            z = rssArticle.read;
        }
        if ((i & 1024) != 0) {
            str9 = rssArticle.variable;
        }
        return rssArticle.copy(str, str2, str3, j, str4, str5, str6, str7, str8, z, str9);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RssArticle(origin=").append(this.origin).append(", sort=").append(this.sort).append(", title=").append(this.title).append(", order=").append(this.order).append(", link=").append(this.link).append(", pubDate=").append((Object) this.pubDate).append(", description=").append((Object) this.description).append(", content=").append((Object) this.content).append(", image=").append((Object) this.image).append(", read=").append(this.read).append(", variable=").append((Object) this.variable).append(')');
        return sb.toString();
    }

    public RssArticle() {
        this(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
    }

    public RssArticle(@NotNull String origin, @NotNull String sort, @NotNull String title, long order, @NotNull String link, @Nullable String pubDate, @Nullable String description, @Nullable String content, @Nullable String image, boolean read, @Nullable String variable) {
        Intrinsics.checkNotNullParameter(origin, "origin");
        Intrinsics.checkNotNullParameter(sort, "sort");
        Intrinsics.checkNotNullParameter(title, "title");
        Intrinsics.checkNotNullParameter(link, "link");
        this.origin = origin;
        this.sort = sort;
        this.title = title;
        this.order = order;
        this.link = link;
        this.pubDate = pubDate;
        this.description = description;
        this.content = content;
        this.image = image;
        this.read = read;
        this.variable = variable;
        this.variableMap = LazyKt.lazy(new RssArticle$variableMap$2(this));
        this._userNameSpace = PackageDocumentBase.PREFIX_OPF;
    }

    @Override // io.legado.app.model.analyzeRule.RuleDataInterface
    @Nullable
    public String getVariable(@NotNull String key) {
        return RuleDataInterface.DefaultImpls.getVariable(this, key);
    }

    public /* synthetic */ RssArticle(String str, String str2, String str3, long j, String str4, String str5, String str6, String str7, String str8, boolean z, String str9, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i & 8) != 0 ? 0L : j, (i & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i & 32) != 0 ? null : str5, (i & 64) != 0 ? null : str6, (i & Wbxml.EXT_T_0) != 0 ? null : str7, (i & 256) != 0 ? null : str8, (i & 512) != 0 ? false : z, (i & 1024) != 0 ? null : str9);
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
    public final String getSort() {
        return this.sort;
    }

    public final void setSort(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.sort = str;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    public final void setTitle(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.title = str;
    }

    public final long getOrder() {
        return this.order;
    }

    public final void setOrder(long j) {
        this.order = j;
    }

    @NotNull
    public final String getLink() {
        return this.link;
    }

    public final void setLink(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.link = str;
    }

    @Nullable
    public final String getPubDate() {
        return this.pubDate;
    }

    public final void setPubDate(@Nullable String str) {
        this.pubDate = str;
    }

    @Nullable
    public final String getDescription() {
        return this.description;
    }

    public final void setDescription(@Nullable String str) {
        this.description = str;
    }

    @Nullable
    public final String getContent() {
        return this.content;
    }

    public final void setContent(@Nullable String str) {
        this.content = str;
    }

    @Nullable
    public final String getImage() {
        return this.image;
    }

    public final void setImage(@Nullable String str) {
        this.image = str;
    }

    public final boolean getRead() {
        return this.read;
    }

    public final void setRead(boolean z) {
        this.read = z;
    }

    @Nullable
    public final String getVariable() {
        return this.variable;
    }

    public final void setVariable(@Nullable String str) {
        this.variable = str;
    }

    public int hashCode() {
        return this.link.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        return other != null && (other instanceof RssArticle) && Intrinsics.areEqual(this.origin, ((RssArticle) other).origin) && Intrinsics.areEqual(this.link, ((RssArticle) other).link);
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
}
