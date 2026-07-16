// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import java.util.List;
import kotlin.collections.SetsKt;
import io.legado.app.utils.GsonExtensionsKt;
import java.util.Map;
import java.util.HashMap;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.LazyKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import java.util.LinkedHashSet;
import kotlin.Lazy;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({ "variableMap", "infoHtml", "tocHtml", "origins", "kindList" })
@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00000\u0002B¡ì\u0001\u0012\b\b\u0002\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\u0004\u0012\b\b\u0002\u0010\n\u001a\u00020\u0004\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0004\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0004\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0004\u0012\b\b\u0002\u0010\u0014\u001a\u00020\b?\u0006\u0002\u0010\u0015J\u000e\u0010N\u001a\u00020O2\u0006\u0010\u0005\u001a\u00020\u0004J\u0011\u0010P\u001a\u00020\b2\u0006\u0010Q\u001a\u00020\u0000H\u0096\u0002J\t\u0010R\u001a\u00020\u0004H\u00c6\u0003J\u000b\u0010S\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\u000b\u0010T\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\t\u0010U\u001a\u00020\u0004H\u00c6\u0003J\t\u0010V\u001a\u00020\u0012H\u00c6\u0003J\u000b\u0010W\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\t\u0010X\u001a\u00020\bH\u00c6\u0003J\t\u0010Y\u001a\u00020\u0004H\u00c6\u0003J\t\u0010Z\u001a\u00020\u0004H\u00c6\u0003J\t\u0010[\u001a\u00020\bH\u00c6\u0003J\t\u0010\\\u001a\u00020\u0004H\u00c6\u0003J\t\u0010]\u001a\u00020\u0004H\u00c6\u0003J\u000b\u0010^\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\u000b\u0010_\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J\u000b\u0010`\u001a\u0004\u0018\u00010\u0004H\u00c6\u0003J?\u0001\u0010a\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\u00042\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0010\u001a\u00020\u00042\b\b\u0002\u0010\u0011\u001a\u00020\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00042\b\b\u0002\u0010\u0014\u001a\u00020\bH\u00c6\u0001J\u0013\u0010b\u001a\u00020c2\b\u0010Q\u001a\u0004\u0018\u00010dH\u0096\u0002J\b\u0010e\u001a\u00020\u0004H\u0016J\b\u0010f\u001a\u00020\bH\u0016J\u001a\u0010g\u001a\u00020O2\u0006\u0010h\u001a\u00020\u00042\b\u0010i\u001a\u0004\u0018\u00010\u0004H\u0016J\u000e\u0010j\u001a\u00020O2\u0006\u0010k\u001a\u00020\u0004J\u0006\u0010l\u001a\u00020mJ\t\u0010n\u001a\u00020\u0004H\u00d6\u0001R\u000e\u0010\u0016\u001a\u00020\u0004X\u0082\u000e?\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u0003\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0018\"\u0004\b\u001c\u0010\u001aR\u001c\u0010\f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0018\"\u0004\b\u001e\u0010\u001aR\u001c\u0010\u001f\u001a\u0004\u0018\u00010\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\u001aR\u001c\u0010\r\u001a\u0004\u0018\u00010\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u0018\"\u0004\b#\u0010\u001aR\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0018\"\u0004\b%\u0010\u001aR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0018\"\u0004\b'\u0010\u001aR\u001a\u0010\t\u001a\u00020\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0018\"\u0004\b)\u0010\u001aR\u001a\u0010\u0005\u001a\u00020\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u0018\"\u0004\b+\u0010\u001aR\u001a\u0010\u0006\u001a\u00020\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010\u0018\"\u0004\b-\u0010\u001aR\u001a\u0010\u0014\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101RF\u00105\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0018\u000103j\n\u0012\u0004\u0012\u00020\u0004\u0018\u0001`42\u001a\u00102\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0018\u000103j\n\u0012\u0004\u0012\u00020\u0004\u0018\u0001`4@BX\u0086\u000e?\u0006\b\n\u0000\u001a\u0004\b6\u00107R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b8\u00109\"\u0004\b:\u0010;R\u001c\u0010<\u001a\u0004\u0018\u00010\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b=\u0010\u0018\"\u0004\b>\u0010\u001aR\u001a\u0010\u0010\u001a\u00020\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\u0018\"\u0004\b@\u0010\u001aR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bA\u0010/\"\u0004\bB\u00101R\u001c\u0010\u0013\u001a\u0004\u0018\u00010\u0004X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0018\"\u0004\bD\u0010\u001aR7\u0010E\u001a\u001e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040Fj\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004`G8VX\u0096\u0084\u0002?\u0006\f\n\u0004\bJ\u0010K\u001a\u0004\bH\u0010IR\u001c\u0010\u000e\u001a\u0004\u0018\u00010\u0004X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\bL\u0010\u0018\"\u0004\bM\u0010\u001a¡§\u0006o" }, d2 = { "Lio/legado/app/data/entities/SearchBook;", "Lio/legado/app/data/entities/BaseBook;", "", "bookUrl", "", "origin", "originName", "type", "", "name", "author", "kind", "coverUrl", "intro", "wordCount", "latestChapterTitle", "tocUrl", "time", "", "variable", "originOrder", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/String;I)V", "_userNameSpace", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookUrl", "setBookUrl", "getCoverUrl", "setCoverUrl", "infoHtml", "getInfoHtml", "setInfoHtml", "getIntro", "setIntro", "getKind", "setKind", "getLatestChapterTitle", "setLatestChapterTitle", "getName", "setName", "getOrigin", "setOrigin", "getOriginName", "setOriginName", "getOriginOrder", "()I", "setOriginOrder", "(I)V", "<set-?>", "Ljava/util/LinkedHashSet;", "Lkotlin/collections/LinkedHashSet;", "origins", "getOrigins", "()Ljava/util/LinkedHashSet;", "getTime", "()J", "setTime", "(J)V", "tocHtml", "getTocHtml", "setTocHtml", "getTocUrl", "setTocUrl", "getType", "setType", "getVariable", "setVariable", "variableMap", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "getVariableMap", "()Ljava/util/HashMap;", "variableMap$delegate", "Lkotlin/Lazy;", "getWordCount", "setWordCount", "addOrigin", "", "compareTo", "other", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "", "getUserNameSpace", "hashCode", "putVariable", "key", "value", "setUserNameSpace", "nameSpace", "toBook", "Lio/legado/app/data/entities/Book;", "toString", "reader-pro" })
public final class SearchBook implements BaseBook, Comparable<SearchBook>
{
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
    @NotNull
    private final transient Lazy variableMap$delegate;
    @NotNull
    private transient String _userNameSpace;
    @Nullable
    private LinkedHashSet<String> origins;
    
    public SearchBook(@NotNull final String bookUrl, @NotNull final String origin, @NotNull final String originName, final int type, @NotNull final String name, @NotNull final String author, @Nullable final String kind, @Nullable final String coverUrl, @Nullable final String intro, @Nullable final String wordCount, @Nullable final String latestChapterTitle, @NotNull final String tocUrl, final long time, @Nullable final String variable, final int originOrder) {
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)originName, "originName");
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)author, "author");
        Intrinsics.checkNotNullParameter((Object)tocUrl, "tocUrl");
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
        this.variableMap$delegate = LazyKt.lazy((Function0)new SearchBook$variableMap.SearchBook$variableMap$2(this));
        this._userNameSpace = "";
    }
    
    @NotNull
    @Override
    public String getBookUrl() {
        return this.bookUrl;
    }
    
    @Override
    public void setBookUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.bookUrl = <set-?>;
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
    
    public final int getType() {
        return this.type;
    }
    
    public final void setType(final int <set-?>) {
        this.type = <set-?>;
    }
    
    @NotNull
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.name = <set-?>;
    }
    
    @NotNull
    @Override
    public String getAuthor() {
        return this.author;
    }
    
    @Override
    public void setAuthor(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.author = <set-?>;
    }
    
    @Nullable
    @Override
    public String getKind() {
        return this.kind;
    }
    
    @Override
    public void setKind(@Nullable final String <set-?>) {
        this.kind = <set-?>;
    }
    
    @Nullable
    public final String getCoverUrl() {
        return this.coverUrl;
    }
    
    public final void setCoverUrl(@Nullable final String <set-?>) {
        this.coverUrl = <set-?>;
    }
    
    @Nullable
    public final String getIntro() {
        return this.intro;
    }
    
    public final void setIntro(@Nullable final String <set-?>) {
        this.intro = <set-?>;
    }
    
    @Nullable
    @Override
    public String getWordCount() {
        return this.wordCount;
    }
    
    @Override
    public void setWordCount(@Nullable final String <set-?>) {
        this.wordCount = <set-?>;
    }
    
    @Nullable
    public final String getLatestChapterTitle() {
        return this.latestChapterTitle;
    }
    
    public final void setLatestChapterTitle(@Nullable final String <set-?>) {
        this.latestChapterTitle = <set-?>;
    }
    
    @NotNull
    public final String getTocUrl() {
        return this.tocUrl;
    }
    
    public final void setTocUrl(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.tocUrl = <set-?>;
    }
    
    public final long getTime() {
        return this.time;
    }
    
    public final void setTime(final long <set-?>) {
        this.time = <set-?>;
    }
    
    @Nullable
    public final String getVariable() {
        return this.variable;
    }
    
    public final void setVariable(@Nullable final String <set-?>) {
        this.variable = <set-?>;
    }
    
    public final int getOriginOrder() {
        return this.originOrder;
    }
    
    public final void setOriginOrder(final int <set-?>) {
        this.originOrder = <set-?>;
    }
    
    @Nullable
    @Override
    public String getInfoHtml() {
        return this.infoHtml;
    }
    
    @Override
    public void setInfoHtml(@Nullable final String <set-?>) {
        this.infoHtml = <set-?>;
    }
    
    @Nullable
    @Override
    public String getTocHtml() {
        return this.tocHtml;
    }
    
    @Override
    public void setTocHtml(@Nullable final String <set-?>) {
        this.tocHtml = <set-?>;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        return other instanceof SearchBook && Intrinsics.areEqual((Object)((SearchBook)other).getBookUrl(), (Object)this.getBookUrl());
    }
    
    @Override
    public int hashCode() {
        return this.getBookUrl().hashCode();
    }
    
    @Override
    public int compareTo(@NotNull final SearchBook other) {
        Intrinsics.checkNotNullParameter((Object)other, "other");
        return other.originOrder - this.originOrder;
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
    public final LinkedHashSet<String> getOrigins() {
        return this.origins;
    }
    
    public final void addOrigin(@NotNull final String origin) {
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        if (this.origins == null) {
            this.origins = SetsKt.linkedSetOf((Object[])new String[] { this.origin });
        }
        final LinkedHashSet<String> origins = this.origins;
        if (origins != null) {
            origins.add(origin);
        }
    }
    
    @NotNull
    public final Book toBook() {
        final Book $this$toBook_u24lambda_u2d0;
        final Book book = $this$toBook_u24lambda_u2d0 = new Book(this.getBookUrl(), this.tocUrl, this.origin, this.originName, this.getName(), this.getAuthor(), this.getKind(), null, this.coverUrl, null, this.intro, null, null, this.type, 0L, this.latestChapterTitle, 0L, 0L, 0, 0, null, 0, 0, 0L, this.getWordCount(), (boolean)(0 != 0), 0, 0, (boolean)(0 != 0), this.variable, null, (boolean)(0 != 0), null, -553690496, 1, null);
        final int n = 0;
        $this$toBook_u24lambda_u2d0.setInfoHtml(this.getInfoHtml());
        $this$toBook_u24lambda_u2d0.setTocUrl(this.getTocUrl());
        $this$toBook_u24lambda_u2d0.setUserNameSpace(this.getUserNameSpace());
        return book;
    }
    
    @NotNull
    @Override
    public List<String> getKindList() {
        return DefaultImpls.getKindList();
    }
    
    @Nullable
    @Override
    public String getVariable(@NotNull final String key) {
        return DefaultImpls.getVariable(key);
    }
    
    @NotNull
    public final String component1() {
        return this.getBookUrl();
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
        return this.coverUrl;
    }
    
    @Nullable
    public final String component9() {
        return this.intro;
    }
    
    @Nullable
    public final String component10() {
        return this.getWordCount();
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
    public final SearchBook copy(@NotNull final String bookUrl, @NotNull final String origin, @NotNull final String originName, final int type, @NotNull final String name, @NotNull final String author, @Nullable final String kind, @Nullable final String coverUrl, @Nullable final String intro, @Nullable final String wordCount, @Nullable final String latestChapterTitle, @NotNull final String tocUrl, final long time, @Nullable final String variable, final int originOrder) {
        Intrinsics.checkNotNullParameter((Object)bookUrl, "bookUrl");
        Intrinsics.checkNotNullParameter((Object)origin, "origin");
        Intrinsics.checkNotNullParameter((Object)originName, "originName");
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)author, "author");
        Intrinsics.checkNotNullParameter((Object)tocUrl, "tocUrl");
        return new SearchBook(bookUrl, origin, originName, type, name, author, kind, coverUrl, intro, wordCount, latestChapterTitle, tocUrl, time, variable, originOrder);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SearchBook(bookUrl=").append(this.getBookUrl()).append(", origin=").append(this.origin).append(", originName=").append(this.originName).append(", type=").append(this.type).append(", name=").append(this.getName()).append(", author=").append(this.getAuthor()).append(", kind=").append((Object)this.getKind()).append(", coverUrl=").append((Object)this.coverUrl).append(", intro=").append((Object)this.intro).append(", wordCount=").append((Object)this.getWordCount()).append(", latestChapterTitle=").append((Object)this.latestChapterTitle).append(", tocUrl=");
        sb.append(this.tocUrl).append(", time=").append(this.time).append(", variable=").append((Object)this.variable).append(", originOrder=").append(this.originOrder).append(')');
        return sb.toString();
    }
    
    public SearchBook() {
        this(null, null, null, 0, null, null, null, null, null, null, null, null, 0L, null, 0, 32767, null);
    }
}
