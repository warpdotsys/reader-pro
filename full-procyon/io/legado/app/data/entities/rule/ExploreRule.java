// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities.rule;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b,\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B}\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\rJ\u000b\u0010$\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0081\u0001\u0010.\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u000102H\u00d6\u0003J\t\u00103\u001a\u000204H\u00d6\u0001J\t\u00105\u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000f\"\u0004\b\u0013\u0010\u0011R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000f\"\u0004\b\u0019\u0010\u0011R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000f\"\u0004\b\u001b\u0010\u0011R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u000f\"\u0004\b\u001d\u0010\u0011R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u000f\"\u0004\b\u001f\u0010\u0011R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u000f\"\u0004\b!\u0010\u0011R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u000f\"\u0004\b#\u0010\u0011¡§\u00066" }, d2 = { "Lio/legado/app/data/entities/rule/ExploreRule;", "Lio/legado/app/data/entities/rule/BookListRule;", "bookList", "", "name", "author", "intro", "kind", "lastChapter", "updateTime", "bookUrl", "coverUrl", "wordCount", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookList", "setBookList", "getBookUrl", "setBookUrl", "getCoverUrl", "setCoverUrl", "getIntro", "setIntro", "getKind", "setKind", "getLastChapter", "setLastChapter", "getName", "setName", "getUpdateTime", "setUpdateTime", "getWordCount", "setWordCount", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "", "hashCode", "", "toString", "reader-pro" })
public final class ExploreRule implements BookListRule
{
    @Nullable
    private String bookList;
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
    private String bookUrl;
    @Nullable
    private String coverUrl;
    @Nullable
    private String wordCount;
    
    public ExploreRule(@Nullable final String bookList, @Nullable final String name, @Nullable final String author, @Nullable final String intro, @Nullable final String kind, @Nullable final String lastChapter, @Nullable final String updateTime, @Nullable final String bookUrl, @Nullable final String coverUrl, @Nullable final String wordCount) {
        this.bookList = bookList;
        this.name = name;
        this.author = author;
        this.intro = intro;
        this.kind = kind;
        this.lastChapter = lastChapter;
        this.updateTime = updateTime;
        this.bookUrl = bookUrl;
        this.coverUrl = coverUrl;
        this.wordCount = wordCount;
    }
    
    @Nullable
    @Override
    public String getBookList() {
        return this.bookList;
    }
    
    @Override
    public void setBookList(@Nullable final String <set-?>) {
        this.bookList = <set-?>;
    }
    
    @Nullable
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(@Nullable final String <set-?>) {
        this.name = <set-?>;
    }
    
    @Nullable
    @Override
    public String getAuthor() {
        return this.author;
    }
    
    @Override
    public void setAuthor(@Nullable final String <set-?>) {
        this.author = <set-?>;
    }
    
    @Nullable
    @Override
    public String getIntro() {
        return this.intro;
    }
    
    @Override
    public void setIntro(@Nullable final String <set-?>) {
        this.intro = <set-?>;
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
    @Override
    public String getLastChapter() {
        return this.lastChapter;
    }
    
    @Override
    public void setLastChapter(@Nullable final String <set-?>) {
        this.lastChapter = <set-?>;
    }
    
    @Nullable
    @Override
    public String getUpdateTime() {
        return this.updateTime;
    }
    
    @Override
    public void setUpdateTime(@Nullable final String <set-?>) {
        this.updateTime = <set-?>;
    }
    
    @Nullable
    @Override
    public String getBookUrl() {
        return this.bookUrl;
    }
    
    @Override
    public void setBookUrl(@Nullable final String <set-?>) {
        this.bookUrl = <set-?>;
    }
    
    @Nullable
    @Override
    public String getCoverUrl() {
        return this.coverUrl;
    }
    
    @Override
    public void setCoverUrl(@Nullable final String <set-?>) {
        this.coverUrl = <set-?>;
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
    public final String component1() {
        return this.getBookList();
    }
    
    @Nullable
    public final String component2() {
        return this.getName();
    }
    
    @Nullable
    public final String component3() {
        return this.getAuthor();
    }
    
    @Nullable
    public final String component4() {
        return this.getIntro();
    }
    
    @Nullable
    public final String component5() {
        return this.getKind();
    }
    
    @Nullable
    public final String component6() {
        return this.getLastChapter();
    }
    
    @Nullable
    public final String component7() {
        return this.getUpdateTime();
    }
    
    @Nullable
    public final String component8() {
        return this.getBookUrl();
    }
    
    @Nullable
    public final String component9() {
        return this.getCoverUrl();
    }
    
    @Nullable
    public final String component10() {
        return this.getWordCount();
    }
    
    @NotNull
    public final ExploreRule copy(@Nullable final String bookList, @Nullable final String name, @Nullable final String author, @Nullable final String intro, @Nullable final String kind, @Nullable final String lastChapter, @Nullable final String updateTime, @Nullable final String bookUrl, @Nullable final String coverUrl, @Nullable final String wordCount) {
        return new ExploreRule(bookList, name, author, intro, kind, lastChapter, updateTime, bookUrl, coverUrl, wordCount);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "ExploreRule(bookList=" + (Object)this.getBookList() + ", name=" + (Object)this.getName() + ", author=" + (Object)this.getAuthor() + ", intro=" + (Object)this.getIntro() + ", kind=" + (Object)this.getKind() + ", lastChapter=" + (Object)this.getLastChapter() + ", updateTime=" + (Object)this.getUpdateTime() + ", bookUrl=" + (Object)this.getBookUrl() + ", coverUrl=" + (Object)this.getCoverUrl() + ", wordCount=" + (Object)this.getWordCount() + ')';
    }
    
    @Override
    public int hashCode() {
        int result = (this.getBookList() == null) ? 0 : this.getBookList().hashCode();
        result = result * 31 + ((this.getName() == null) ? 0 : this.getName().hashCode());
        result = result * 31 + ((this.getAuthor() == null) ? 0 : this.getAuthor().hashCode());
        result = result * 31 + ((this.getIntro() == null) ? 0 : this.getIntro().hashCode());
        result = result * 31 + ((this.getKind() == null) ? 0 : this.getKind().hashCode());
        result = result * 31 + ((this.getLastChapter() == null) ? 0 : this.getLastChapter().hashCode());
        result = result * 31 + ((this.getUpdateTime() == null) ? 0 : this.getUpdateTime().hashCode());
        result = result * 31 + ((this.getBookUrl() == null) ? 0 : this.getBookUrl().hashCode());
        result = result * 31 + ((this.getCoverUrl() == null) ? 0 : this.getCoverUrl().hashCode());
        result = result * 31 + ((this.getWordCount() == null) ? 0 : this.getWordCount().hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExploreRule)) {
            return false;
        }
        final ExploreRule exploreRule = (ExploreRule)other;
        return Intrinsics.areEqual((Object)this.getBookList(), (Object)exploreRule.getBookList()) && Intrinsics.areEqual((Object)this.getName(), (Object)exploreRule.getName()) && Intrinsics.areEqual((Object)this.getAuthor(), (Object)exploreRule.getAuthor()) && Intrinsics.areEqual((Object)this.getIntro(), (Object)exploreRule.getIntro()) && Intrinsics.areEqual((Object)this.getKind(), (Object)exploreRule.getKind()) && Intrinsics.areEqual((Object)this.getLastChapter(), (Object)exploreRule.getLastChapter()) && Intrinsics.areEqual((Object)this.getUpdateTime(), (Object)exploreRule.getUpdateTime()) && Intrinsics.areEqual((Object)this.getBookUrl(), (Object)exploreRule.getBookUrl()) && Intrinsics.areEqual((Object)this.getCoverUrl(), (Object)exploreRule.getCoverUrl()) && Intrinsics.areEqual((Object)this.getWordCount(), (Object)exploreRule.getWordCount());
    }
    
    public ExploreRule() {
        this(null, null, null, null, null, null, null, null, null, null, 1023, null);
    }
}
