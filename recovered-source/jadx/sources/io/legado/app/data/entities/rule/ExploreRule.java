package io.legado.app.data.entities.rule;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: ExploreRule.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/rule/ExploreRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b,\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B}\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\rJ\u000b\u0010$\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010&\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u0081\u0001\u0010.\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u000102HÖ\u0003J\t\u00103\u001a\u000204HÖ\u0001J\t\u00105\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000f\"\u0004\b\u0013\u0010\u0011R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000f\"\u0004\b\u0015\u0010\u0011R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000f\"\u0004\b\u0017\u0010\u0011R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000f\"\u0004\b\u0019\u0010\u0011R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000f\"\u0004\b\u001b\u0010\u0011R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u000f\"\u0004\b\u001d\u0010\u0011R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u000f\"\u0004\b\u001f\u0010\u0011R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u000f\"\u0004\b!\u0010\u0011R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0096\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010\u000f\"\u0004\b#\u0010\u0011¨\u00066"}, d2 = {"Lio/legado/app/data/entities/rule/ExploreRule;", "Lio/legado/app/data/entities/rule/BookListRule;", "bookList", PackageDocumentBase.PREFIX_OPF, "name", "author", "intro", "kind", "lastChapter", "updateTime", "bookUrl", "coverUrl", "wordCount", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getBookList", "setBookList", "getBookUrl", "setBookUrl", "getCoverUrl", "setCoverUrl", "getIntro", "setIntro", "getKind", "setKind", "getLastChapter", "setLastChapter", "getName", "setName", "getUpdateTime", "setUpdateTime", "getWordCount", "setWordCount", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", PackageDocumentBase.PREFIX_OPF, "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class ExploreRule implements BookListRule {

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

    @Nullable
    public final String component1() {
        return getBookList();
    }

    @Nullable
    public final String component2() {
        return getName();
    }

    @Nullable
    public final String component3() {
        return getAuthor();
    }

    @Nullable
    public final String component4() {
        return getIntro();
    }

    @Nullable
    public final String component5() {
        return getKind();
    }

    @Nullable
    public final String component6() {
        return getLastChapter();
    }

    @Nullable
    public final String component7() {
        return getUpdateTime();
    }

    @Nullable
    public final String component8() {
        return getBookUrl();
    }

    @Nullable
    public final String component9() {
        return getCoverUrl();
    }

    @Nullable
    public final String component10() {
        return getWordCount();
    }

    @NotNull
    public final ExploreRule copy(@Nullable String bookList, @Nullable String name, @Nullable String author, @Nullable String intro, @Nullable String kind, @Nullable String lastChapter, @Nullable String updateTime, @Nullable String bookUrl, @Nullable String coverUrl, @Nullable String wordCount) {
        return new ExploreRule(bookList, name, author, intro, kind, lastChapter, updateTime, bookUrl, coverUrl, wordCount);
    }

    public static /* synthetic */ ExploreRule copy$default(ExploreRule exploreRule, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i, Object obj) {
        if ((i & 1) != 0) {
            str = exploreRule.getBookList();
        }
        if ((i & 2) != 0) {
            str2 = exploreRule.getName();
        }
        if ((i & 4) != 0) {
            str3 = exploreRule.getAuthor();
        }
        if ((i & 8) != 0) {
            str4 = exploreRule.getIntro();
        }
        if ((i & 16) != 0) {
            str5 = exploreRule.getKind();
        }
        if ((i & 32) != 0) {
            str6 = exploreRule.getLastChapter();
        }
        if ((i & 64) != 0) {
            str7 = exploreRule.getUpdateTime();
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str8 = exploreRule.getBookUrl();
        }
        if ((i & 256) != 0) {
            str9 = exploreRule.getCoverUrl();
        }
        if ((i & 512) != 0) {
            str10 = exploreRule.getWordCount();
        }
        return exploreRule.copy(str, str2, str3, str4, str5, str6, str7, str8, str9, str10);
    }

    @NotNull
    public String toString() {
        return "ExploreRule(bookList=" + ((Object) getBookList()) + ", name=" + ((Object) getName()) + ", author=" + ((Object) getAuthor()) + ", intro=" + ((Object) getIntro()) + ", kind=" + ((Object) getKind()) + ", lastChapter=" + ((Object) getLastChapter()) + ", updateTime=" + ((Object) getUpdateTime()) + ", bookUrl=" + ((Object) getBookUrl()) + ", coverUrl=" + ((Object) getCoverUrl()) + ", wordCount=" + ((Object) getWordCount()) + ')';
    }

    public int hashCode() {
        int result = getBookList() == null ? 0 : getBookList().hashCode();
        return (((((((((((((((((result * 31) + (getName() == null ? 0 : getName().hashCode())) * 31) + (getAuthor() == null ? 0 : getAuthor().hashCode())) * 31) + (getIntro() == null ? 0 : getIntro().hashCode())) * 31) + (getKind() == null ? 0 : getKind().hashCode())) * 31) + (getLastChapter() == null ? 0 : getLastChapter().hashCode())) * 31) + (getUpdateTime() == null ? 0 : getUpdateTime().hashCode())) * 31) + (getBookUrl() == null ? 0 : getBookUrl().hashCode())) * 31) + (getCoverUrl() == null ? 0 : getCoverUrl().hashCode())) * 31) + (getWordCount() == null ? 0 : getWordCount().hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ExploreRule)) {
            return false;
        }
        ExploreRule exploreRule = (ExploreRule) other;
        return Intrinsics.areEqual(getBookList(), exploreRule.getBookList()) && Intrinsics.areEqual(getName(), exploreRule.getName()) && Intrinsics.areEqual(getAuthor(), exploreRule.getAuthor()) && Intrinsics.areEqual(getIntro(), exploreRule.getIntro()) && Intrinsics.areEqual(getKind(), exploreRule.getKind()) && Intrinsics.areEqual(getLastChapter(), exploreRule.getLastChapter()) && Intrinsics.areEqual(getUpdateTime(), exploreRule.getUpdateTime()) && Intrinsics.areEqual(getBookUrl(), exploreRule.getBookUrl()) && Intrinsics.areEqual(getCoverUrl(), exploreRule.getCoverUrl()) && Intrinsics.areEqual(getWordCount(), exploreRule.getWordCount());
    }

    public ExploreRule() {
        this(null, null, null, null, null, null, null, null, null, null, 1023, null);
    }

    public ExploreRule(@Nullable String bookList, @Nullable String name, @Nullable String author, @Nullable String intro, @Nullable String kind, @Nullable String lastChapter, @Nullable String updateTime, @Nullable String bookUrl, @Nullable String coverUrl, @Nullable String wordCount) {
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

    public /* synthetic */ ExploreRule(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6, (i & 64) != 0 ? null : str7, (i & Wbxml.EXT_T_0) != 0 ? null : str8, (i & 256) != 0 ? null : str9, (i & 512) != 0 ? null : str10);
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getBookList() {
        return this.bookList;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setBookList(@Nullable String str) {
        this.bookList = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getName() {
        return this.name;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setName(@Nullable String str) {
        this.name = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getAuthor() {
        return this.author;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setAuthor(@Nullable String str) {
        this.author = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getIntro() {
        return this.intro;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setIntro(@Nullable String str) {
        this.intro = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getKind() {
        return this.kind;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setKind(@Nullable String str) {
        this.kind = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getLastChapter() {
        return this.lastChapter;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setLastChapter(@Nullable String str) {
        this.lastChapter = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getUpdateTime() {
        return this.updateTime;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setUpdateTime(@Nullable String str) {
        this.updateTime = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getBookUrl() {
        return this.bookUrl;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setBookUrl(@Nullable String str) {
        this.bookUrl = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getCoverUrl() {
        return this.coverUrl;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setCoverUrl(@Nullable String str) {
        this.coverUrl = str;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    @Nullable
    public String getWordCount() {
        return this.wordCount;
    }

    @Override // io.legado.app.data.entities.rule.BookListRule
    public void setWordCount(@Nullable String str) {
        this.wordCount = str;
    }
}
