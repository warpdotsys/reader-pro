// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities.rule;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b0\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u000eJ\u000b\u0010'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u008d\u0001\u00102\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u00103\u001a\u0002042\b\u00105\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u00106\u001a\u000207H\u00d6\u0001J\t\u00108\u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0010\"\u0004\b\u0016\u0010\u0012R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0010\"\u0004\b\u0018\u0010\u0012R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0010\"\u0004\b\u001a\u0010\u0012R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0010\"\u0004\b\u001c\u0010\u0012R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0010\"\u0004\b \u0010\u0012R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0010\"\u0004\b\"\u0010\u0012R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0010\"\u0004\b$\u0010\u0012R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0010\"\u0004\b&\u0010\u0012¡§\u00069" }, d2 = { "Lio/legado/app/data/entities/rule/BookInfoRule;", "", "init", "", "name", "author", "intro", "kind", "lastChapter", "updateTime", "coverUrl", "tocUrl", "wordCount", "canReName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getCanReName", "setCanReName", "getCoverUrl", "setCoverUrl", "getInit", "setInit", "getIntro", "setIntro", "getKind", "setKind", "getLastChapter", "setLastChapter", "getName", "setName", "getTocUrl", "setTocUrl", "getUpdateTime", "setUpdateTime", "getWordCount", "setWordCount", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class BookInfoRule
{
    @Nullable
    private String init;
    @Nullable
    private String name;
    @Nullable
    private String author;
    @Nullable
    private String intro;
    @Nullable
    private String kind;
    @Nullable
    private String lastChapter;
    @Nullable
    private String updateTime;
    @Nullable
    private String coverUrl;
    @Nullable
    private String tocUrl;
    @Nullable
    private String wordCount;
    @Nullable
    private String canReName;
    
    public BookInfoRule(@Nullable final String init, @Nullable final String name, @Nullable final String author, @Nullable final String intro, @Nullable final String kind, @Nullable final String lastChapter, @Nullable final String updateTime, @Nullable final String coverUrl, @Nullable final String tocUrl, @Nullable final String wordCount, @Nullable final String canReName) {
        this.init = init;
        this.name = name;
        this.author = author;
        this.intro = intro;
        this.kind = kind;
        this.lastChapter = lastChapter;
        this.updateTime = updateTime;
        this.coverUrl = coverUrl;
        this.tocUrl = tocUrl;
        this.wordCount = wordCount;
        this.canReName = canReName;
    }
    
    @Nullable
    public final String getInit() {
        return this.init;
    }
    
    public final void setInit(@Nullable final String <set-?>) {
        this.init = <set-?>;
    }
    
    @Nullable
    public final String getName() {
        return this.name;
    }
    
    public final void setName(@Nullable final String <set-?>) {
        this.name = <set-?>;
    }
    
    @Nullable
    public final String getAuthor() {
        return this.author;
    }
    
    public final void setAuthor(@Nullable final String <set-?>) {
        this.author = <set-?>;
    }
    
    @Nullable
    public final String getIntro() {
        return this.intro;
    }
    
    public final void setIntro(@Nullable final String <set-?>) {
        this.intro = <set-?>;
    }
    
    @Nullable
    public final String getKind() {
        return this.kind;
    }
    
    public final void setKind(@Nullable final String <set-?>) {
        this.kind = <set-?>;
    }
    
    @Nullable
    public final String getLastChapter() {
        return this.lastChapter;
    }
    
    public final void setLastChapter(@Nullable final String <set-?>) {
        this.lastChapter = <set-?>;
    }
    
    @Nullable
    public final String getUpdateTime() {
        return this.updateTime;
    }
    
    public final void setUpdateTime(@Nullable final String <set-?>) {
        this.updateTime = <set-?>;
    }
    
    @Nullable
    public final String getCoverUrl() {
        return this.coverUrl;
    }
    
    public final void setCoverUrl(@Nullable final String <set-?>) {
        this.coverUrl = <set-?>;
    }
    
    @Nullable
    public final String getTocUrl() {
        return this.tocUrl;
    }
    
    public final void setTocUrl(@Nullable final String <set-?>) {
        this.tocUrl = <set-?>;
    }
    
    @Nullable
    public final String getWordCount() {
        return this.wordCount;
    }
    
    public final void setWordCount(@Nullable final String <set-?>) {
        this.wordCount = <set-?>;
    }
    
    @Nullable
    public final String getCanReName() {
        return this.canReName;
    }
    
    public final void setCanReName(@Nullable final String <set-?>) {
        this.canReName = <set-?>;
    }
    
    @Nullable
    public final String component1() {
        return this.init;
    }
    
    @Nullable
    public final String component2() {
        return this.name;
    }
    
    @Nullable
    public final String component3() {
        return this.author;
    }
    
    @Nullable
    public final String component4() {
        return this.intro;
    }
    
    @Nullable
    public final String component5() {
        return this.kind;
    }
    
    @Nullable
    public final String component6() {
        return this.lastChapter;
    }
    
    @Nullable
    public final String component7() {
        return this.updateTime;
    }
    
    @Nullable
    public final String component8() {
        return this.coverUrl;
    }
    
    @Nullable
    public final String component9() {
        return this.tocUrl;
    }
    
    @Nullable
    public final String component10() {
        return this.wordCount;
    }
    
    @Nullable
    public final String component11() {
        return this.canReName;
    }
    
    @NotNull
    public final BookInfoRule copy(@Nullable final String init, @Nullable final String name, @Nullable final String author, @Nullable final String intro, @Nullable final String kind, @Nullable final String lastChapter, @Nullable final String updateTime, @Nullable final String coverUrl, @Nullable final String tocUrl, @Nullable final String wordCount, @Nullable final String canReName) {
        return new BookInfoRule(init, name, author, intro, kind, lastChapter, updateTime, coverUrl, tocUrl, wordCount, canReName);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BookInfoRule(init=").append((Object)this.init).append(", name=").append((Object)this.name).append(", author=").append((Object)this.author).append(", intro=").append((Object)this.intro).append(", kind=").append((Object)this.kind).append(", lastChapter=").append((Object)this.lastChapter).append(", updateTime=").append((Object)this.updateTime).append(", coverUrl=").append((Object)this.coverUrl).append(", tocUrl=").append((Object)this.tocUrl).append(", wordCount=").append((Object)this.wordCount).append(", canReName=").append((Object)this.canReName).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = (this.init == null) ? 0 : this.init.hashCode();
        result = result * 31 + ((this.name == null) ? 0 : this.name.hashCode());
        result = result * 31 + ((this.author == null) ? 0 : this.author.hashCode());
        result = result * 31 + ((this.intro == null) ? 0 : this.intro.hashCode());
        result = result * 31 + ((this.kind == null) ? 0 : this.kind.hashCode());
        result = result * 31 + ((this.lastChapter == null) ? 0 : this.lastChapter.hashCode());
        result = result * 31 + ((this.updateTime == null) ? 0 : this.updateTime.hashCode());
        result = result * 31 + ((this.coverUrl == null) ? 0 : this.coverUrl.hashCode());
        result = result * 31 + ((this.tocUrl == null) ? 0 : this.tocUrl.hashCode());
        result = result * 31 + ((this.wordCount == null) ? 0 : this.wordCount.hashCode());
        result = result * 31 + ((this.canReName == null) ? 0 : this.canReName.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BookInfoRule)) {
            return false;
        }
        final BookInfoRule bookInfoRule = (BookInfoRule)other;
        return Intrinsics.areEqual((Object)this.init, (Object)bookInfoRule.init) && Intrinsics.areEqual((Object)this.name, (Object)bookInfoRule.name) && Intrinsics.areEqual((Object)this.author, (Object)bookInfoRule.author) && Intrinsics.areEqual((Object)this.intro, (Object)bookInfoRule.intro) && Intrinsics.areEqual((Object)this.kind, (Object)bookInfoRule.kind) && Intrinsics.areEqual((Object)this.lastChapter, (Object)bookInfoRule.lastChapter) && Intrinsics.areEqual((Object)this.updateTime, (Object)bookInfoRule.updateTime) && Intrinsics.areEqual((Object)this.coverUrl, (Object)bookInfoRule.coverUrl) && Intrinsics.areEqual((Object)this.tocUrl, (Object)bookInfoRule.tocUrl) && Intrinsics.areEqual((Object)this.wordCount, (Object)bookInfoRule.wordCount) && Intrinsics.areEqual((Object)this.canReName, (Object)bookInfoRule.canReName);
    }
    
    public BookInfoRule() {
        this(null, null, null, null, null, null, null, null, null, null, null, 2047, null);
    }
}
