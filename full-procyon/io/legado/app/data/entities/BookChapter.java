// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import java.util.Arrays;
import io.legado.app.utils.MD5Utils;
import kotlin.jvm.internal.StringCompanionObject;
import java.util.regex.Matcher;
import io.legado.app.utils.NetworkUtils;
import io.legado.app.model.analyzeRule.AnalyzeUrl;
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
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b)\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0015\n\u0002\u0010\u0000\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0087\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0003\u0012\b\b\u0002\u0010\b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u0013J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010?\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003?\u0006\u0002\u0010\u001cJ\u000b\u0010@\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010A\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010B\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010C\u001a\u00020\u0003H\u00c6\u0003J\t\u0010D\u001a\u00020\u0006H\u00c6\u0003J\t\u0010E\u001a\u00020\u0003H\u00c6\u0003J\t\u0010F\u001a\u00020\u0003H\u00c6\u0003J\t\u0010G\u001a\u00020\nH\u00c6\u0003J\u000b\u0010H\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010I\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0010\u0010J\u001a\u0004\u0018\u00010\u000eH\u00c6\u0003?\u0006\u0002\u0010\u001cJ\u009e\u0001\u0010K\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0012\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001?\u0006\u0002\u0010LJ\u0013\u0010M\u001a\u00020\u00062\b\u0010N\u001a\u0004\u0018\u00010OH\u0096\u0002J\u0006\u0010P\u001a\u00020\u0003J\u0006\u0010Q\u001a\u00020\u0003J\b\u0010R\u001a\u00020\u0003H\u0016J\b\u0010S\u001a\u00020\nH\u0016J\u001a\u0010T\u001a\u00020U2\u0006\u0010V\u001a\u00020\u00032\b\u0010W\u001a\u0004\u0018\u00010\u0003H\u0016J\u000e\u0010X\u001a\u00020U2\u0006\u0010Y\u001a\u00020\u0003J\t\u0010Z\u001a\u00020\u0003H\u00d6\u0001R\u000e\u0010\u0014\u001a\u00020\u0003X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\b\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0016\"\u0004\b\u001a\u0010\u0018R\u001e\u0010\u000f\u001a\u0004\u0018\u00010\u000eX\u0086\u000e?\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001c\u0010\u0011\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0016\"\u0004\b!\u0010\u0018R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010&\"\u0004\b'\u0010(R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0016\"\u0004\b*\u0010\u0018R\u001e\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0086\u000e?\u0006\u0010\n\u0002\u0010\u001f\u001a\u0004\b+\u0010\u001c\"\u0004\b,\u0010\u001eR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0016\"\u0004\b.\u0010\u0018R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0016\"\u0004\b0\u0010\u0018R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b1\u0010\u0016\"\u0004\b2\u0010\u0018R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0016\"\u0004\b4\u0010\u0018R\u001c\u0010\u0012\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b5\u0010\u0016\"\u0004\b6\u0010\u0018R7\u00107\u001a\u001e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u000308j\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003`98VX\u0096\u0084\u0002?\u0006\f\n\u0004\b<\u0010=\u001a\u0004\b:\u0010;¡§\u0006[" }, d2 = { "Lio/legado/app/data/entities/BookChapter;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "url", "", "title", "isVolume", "", "baseUrl", "bookUrl", "index", "", "resourceUrl", "tag", "start", "", "end", "startFragmentId", "endFragmentId", "variable", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "_userNameSpace", "getBaseUrl", "()Ljava/lang/String;", "setBaseUrl", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getEnd", "()Ljava/lang/Long;", "setEnd", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getEndFragmentId", "setEndFragmentId", "getIndex", "()I", "setIndex", "(I)V", "()Z", "setVolume", "(Z)V", "getResourceUrl", "setResourceUrl", "getStart", "setStart", "getStartFragmentId", "setStartFragmentId", "getTag", "setTag", "getTitle", "setTitle", "getUrl", "setUrl", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lio/legado/app/data/entities/BookChapter;", "equals", "other", "", "getAbsoluteURL", "getFileName", "getUserNameSpace", "hashCode", "putVariable", "", "key", "value", "setUserNameSpace", "nameSpace", "toString", "reader-pro" })
public final class BookChapter implements RuleDataInterface
{
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
    @NotNull
    private final transient Lazy variableMap$delegate;
    @NotNull
    private transient String _userNameSpace;
    
    public BookChapter(@NotNull final String url, @NotNull final String title, final boolean isVolume, @NotNull final String baseUrl, @NotNull final String bookUrl, final int index, @Nullable final String resourceUrl, @Nullable final String tag, @Nullable final Long start, @Nullable final Long end, @Nullable final String startFragmentId, @Nullable final String endFragmentId, @Nullable final String variable) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)title, "title");
        Intrinsics.checkNotNullParameter((Object)baseUrl, "baseUrl");
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
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
        this.variableMap$delegate = LazyKt.lazy((Function0)new BookChapter$variableMap.BookChapter$variableMap$2(this));
        this._userNameSpace = "";
    }
    
    @NotNull
    public final String getUrl() {
        return this.url;
    }
    
    public final void setUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.url = <set-?>;
    }
    
    @NotNull
    public final String getTitle() {
        return this.title;
    }
    
    public final void setTitle(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.title = <set-?>;
    }
    
    public final boolean isVolume() {
        return this.isVolume;
    }
    
    public final void setVolume(final boolean <set-?>) {
        this.isVolume = <set-?>;
    }
    
    @NotNull
    public final String getBaseUrl() {
        return this.baseUrl;
    }
    
    public final void setBaseUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.baseUrl = <set-?>;
    }
    
    @NotNull
    public final String getBookUrl() {
        return this.bookUrl;
    }
    
    public final void setBookUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.bookUrl = <set-?>;
    }
    
    public final int getIndex() {
        return this.index;
    }
    
    public final void setIndex(final int <set-?>) {
        this.index = <set-?>;
    }
    
    @Nullable
    public final String getResourceUrl() {
        return this.resourceUrl;
    }
    
    public final void setResourceUrl(@Nullable final String <set-?>) {
        this.resourceUrl = <set-?>;
    }
    
    @Nullable
    public final String getTag() {
        return this.tag;
    }
    
    public final void setTag(@Nullable final String <set-?>) {
        this.tag = <set-?>;
    }
    
    @Nullable
    public final Long getStart() {
        return this.start;
    }
    
    public final void setStart(@Nullable final Long <set-?>) {
        this.start = <set-?>;
    }
    
    @Nullable
    public final Long getEnd() {
        return this.end;
    }
    
    public final void setEnd(@Nullable final Long <set-?>) {
        this.end = <set-?>;
    }
    
    @Nullable
    public final String getStartFragmentId() {
        return this.startFragmentId;
    }
    
    public final void setStartFragmentId(@Nullable final String <set-?>) {
        this.startFragmentId = <set-?>;
    }
    
    @Nullable
    public final String getEndFragmentId() {
        return this.endFragmentId;
    }
    
    public final void setEndFragmentId(@Nullable final String <set-?>) {
        this.endFragmentId = <set-?>;
    }
    
    @Nullable
    public final String getVariable() {
        return this.variable;
    }
    
    public final void setVariable(@Nullable final String <set-?>) {
        this.variable = <set-?>;
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
    
    @Override
    public int hashCode() {
        return this.url.hashCode();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof BookChapter && Intrinsics.areEqual((Object)((BookChapter)other).url, (Object)this.url);
    }
    
    @NotNull
    public final String getAbsoluteURL() {
        final Matcher urlMatcher = AnalyzeUrl.Companion.getParamPattern().matcher(this.url);
        String s2;
        if (urlMatcher.find()) {
            final String url = this.url;
            final int beginIndex = 0;
            final int start = urlMatcher.start();
            final String s = url;
            if (s == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            Intrinsics.checkNotNullExpressionValue((Object)(s2 = s.substring(beginIndex, start)), "(this as java.lang.Strin\u2026ing(startIndex, endIndex)");
        }
        else {
            s2 = this.url;
        }
        final String urlBefore = s2;
        final String urlAbsoluteBefore = NetworkUtils.INSTANCE.getAbsoluteURL(this.baseUrl, urlBefore);
        String string;
        if (urlBefore.length() == this.url.length()) {
            string = urlAbsoluteBefore;
        }
        else {
            final StringBuilder append = new StringBuilder().append(urlAbsoluteBefore).append(',');
            final String url2 = this.url;
            final int end = urlMatcher.end();
            final String s3 = url2;
            if (s3 == null) {
                throw new NullPointerException("null cannot be cast to non-null type java.lang.String");
            }
            final String substring = s3.substring(end);
            Intrinsics.checkNotNullExpressionValue((Object)substring, "(this as java.lang.String).substring(startIndex)");
            string = append.append(substring).toString();
        }
        return string;
    }
    
    @NotNull
    public final String getFileName() {
        final StringCompanionObject instance = StringCompanionObject.INSTANCE;
        final String s = "%05d-%s.nb";
        final Object[] array = { this.index, MD5Utils.INSTANCE.md5Encode16(this.title) };
        final String format = s;
        final Object[] original = array;
        final String format2 = String.format(format, Arrays.copyOf(original, original.length));
        Intrinsics.checkNotNullExpressionValue((Object)format2, "java.lang.String.format(format, *args)");
        return format2;
    }
    
    @Nullable
    @Override
    public String getVariable(@NotNull final String key) {
        return DefaultImpls.getVariable(key);
    }
    
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
    public final BookChapter copy(@NotNull final String url, @NotNull final String title, final boolean isVolume, @NotNull final String baseUrl, @NotNull final String bookUrl, final int index, @Nullable final String resourceUrl, @Nullable final String tag, @Nullable final Long start, @Nullable final Long end, @Nullable final String startFragmentId, @Nullable final String endFragmentId, @Nullable final String variable) {
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)title, "title");
        Intrinsics.checkNotNullParameter((Object)baseUrl, "baseUrl");
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
        return new BookChapter(url, title, isVolume, baseUrl, bookUrl, index, resourceUrl, tag, start, end, startFragmentId, endFragmentId, variable);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BookChapter(url=").append(this.url).append(", title=").append(this.title).append(", isVolume=").append(this.isVolume).append(", baseUrl=").append(this.baseUrl).append(", bookUrl=").append(this.bookUrl).append(", index=").append(this.index).append(", resourceUrl=").append((Object)this.resourceUrl).append(", tag=").append((Object)this.tag).append(", start=").append(this.start).append(", end=").append(this.end).append(", startFragmentId=").append((Object)this.startFragmentId).append(", endFragmentId=");
        sb.append((Object)this.endFragmentId).append(", variable=").append((Object)this.variable).append(')');
        return sb.toString();
    }
    
    public BookChapter() {
        this(null, null, false, null, null, 0, null, null, null, null, null, null, null, 8191, null);
    }
}
