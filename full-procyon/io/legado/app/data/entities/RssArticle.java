// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import io.legado.app.utils.GsonExtensionsKt;
import java.util.Map;
import java.util.HashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.Lazy;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.legado.app.model.analyzeRule.RuleDataInterface;

@JsonIgnoreProperties({ "variableMap", "_userNameSpace", "userNameSpace" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u00002\u00020\u0001B}\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u0010J\t\u00105\u001a\u00020\u0003H\u00c6\u0003J\t\u00106\u001a\u00020\u000eH\u00c6\u0003J\u000b\u00107\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00108\u001a\u00020\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u0007H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010<\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010=\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010>\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010?\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0081\u0001\u0010@\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010A\u001a\u00020\u000e2\b\u0010B\u001a\u0004\u0018\u00010CH\u0096\u0002J\b\u0010D\u001a\u00020\u0003H\u0016J\b\u0010E\u001a\u00020FH\u0016J\u001a\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\u00032\b\u0010J\u001a\u0004\u0018\u00010\u0003H\u0016J\u000e\u0010K\u001a\u00020H2\u0006\u0010L\u001a\u00020\u0003J\t\u0010M\u001a\u00020\u0003H\u00d6\u0001R\u000e\u0010\u0011\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0013\"\u0004\b\u0017\u0010\u0015R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0013\"\u0004\b\u0019\u0010\u0015R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0013\"\u0004\b!\u0010\u0015R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0013\"\u0004\b#\u0010\u0015R\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010'R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0013\"\u0004\b)\u0010\u0015R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0013\"\u0004\b+\u0010\u0015R\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0013\"\u0004\b-\u0010\u0015R7\u0010.\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00030/j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`08VX\u0096\u0084\u0002?\u0006\f\n\u0004\b3\u00104\u001a\u0004\b1\u00102¡§\u0006N" }, d2 = { "Lio/legado/app/data/entities/RssArticle;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "origin", "", "sort", "title", "order", "", "link", "pubDate", "description", "content", "image", "read", "", "variable", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)V", "_userNameSpace", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getDescription", "setDescription", "getImage", "setImage", "getLink", "setLink", "getOrder", "()J", "setOrder", "(J)V", "getOrigin", "setOrigin", "getPubDate", "setPubDate", "getRead", "()Z", "setRead", "(Z)V", "getSort", "setSort", "getTitle", "setTitle", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "", "getUserNameSpace", "hashCode", "", "putVariable", "", "key", "value", "setUserNameSpace", "nameSpace", "toString", "reader-pro" })
public final class RssArticle implements RuleDataInterface
{
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
    @NotNull
    private final transient Lazy variableMap$delegate;
    @NotNull
    private transient String _userNameSpace;
    
    public RssArticle(@NotNull final String origin, @NotNull final String sort, @NotNull final String title, final long order, @NotNull final String link, @Nullable final String pubDate, @Nullable final String description, @Nullable final String content, @Nullable final String image, final boolean read, @Nullable final String variable) {
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)sort, "sort");
        Intrinsics.checkNotNullParameter((Object)title, "title");
        Intrinsics.checkNotNullParameter((Object)link, "link");
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
        this.variableMap$delegate = LazyKt.lazy((Function0)new RssArticle$variableMap.RssArticle$variableMap$2(this));
        this._userNameSpace = "";
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
    public final String getSort() {
        return this.sort;
    }
    
    public final void setSort(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.sort = <set-?>;
    }
    
    @NotNull
    public final String getTitle() {
        return this.title;
    }
    
    public final void setTitle(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.title = <set-?>;
    }
    
    public final long getOrder() {
        return this.order;
    }
    
    public final void setOrder(final long <set-?>) {
        this.order = <set-?>;
    }
    
    @NotNull
    public final String getLink() {
        return this.link;
    }
    
    public final void setLink(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.link = <set-?>;
    }
    
    @Nullable
    public final String getPubDate() {
        return this.pubDate;
    }
    
    public final void setPubDate(@Nullable final String <set-?>) {
        this.pubDate = <set-?>;
    }
    
    @Nullable
    public final String getDescription() {
        return this.description;
    }
    
    public final void setDescription(@Nullable final String <set-?>) {
        this.description = <set-?>;
    }
    
    @Nullable
    public final String getContent() {
        return this.content;
    }
    
    public final void setContent(@Nullable final String <set-?>) {
        this.content = <set-?>;
    }
    
    @Nullable
    public final String getImage() {
        return this.image;
    }
    
    public final void setImage(@Nullable final String <set-?>) {
        this.image = <set-?>;
    }
    
    public final boolean getRead() {
        return this.read;
    }
    
    public final void setRead(final boolean <set-?>) {
        this.read = <set-?>;
    }
    
    @Nullable
    public final String getVariable() {
        return this.variable;
    }
    
    public final void setVariable(@Nullable final String <set-?>) {
        this.variable = <set-?>;
    }
    
    @Override
    public int hashCode() {
        return this.link.hashCode();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other != null && other instanceof RssArticle && (Intrinsics.areEqual((Object)this.origin, (Object)((RssArticle)other).origin) && Intrinsics.areEqual((Object)this.link, (Object)((RssArticle)other).link));
    }
    
    @NotNull
    @Override
    public HashMap<String, String> getVariableMap() {
        return (HashMap)this.variableMap$delegate.getValue();
    }
    
    @Override
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
    
    public final void setUserNameSpace(@NotNull final String nameSpace) {
        Intrinsics.checkNotNullParameter((Object)nameSpace, "nameSpace");
        this._userNameSpace = nameSpace;
    }
    
    @NotNull
    @Override
    public String getUserNameSpace() {
        return this._userNameSpace;
    }
    
    @Nullable
    @Override
    public String getVariable(@NotNull final String key) {
        return DefaultImpls.getVariable(key);
    }
    
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
    public final RssArticle copy(@NotNull final String origin, @NotNull final String sort, @NotNull final String title, final long order, @NotNull final String link, @Nullable final String pubDate, @Nullable final String description, @Nullable final String content, @Nullable final String image, final boolean read, @Nullable final String variable) {
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)sort, "sort");
        Intrinsics.checkNotNullParameter((Object)title, "title");
        Intrinsics.checkNotNullParameter((Object)link, "link");
        return new RssArticle(origin, sort, title, order, link, pubDate, description, content, image, read, variable);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RssArticle(origin=").append(this.origin).append(", sort=").append(this.sort).append(", title=").append(this.title).append(", order=").append(this.order).append(", link=").append(this.link).append(", pubDate=").append((Object)this.pubDate).append(", description=").append((Object)this.description).append(", content=").append((Object)this.content).append(", image=").append((Object)this.image).append(", read=").append(this.read).append(", variable=").append((Object)this.variable).append(')');
        return sb.toString();
    }
    
    public RssArticle() {
        this(null, null, null, 0L, null, null, null, null, null, false, null, 2047, null);
    }
}
