package io.legado.app.data.entities.rule;

import io.legado.app.constant.Action;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: BookInfoRule.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/rule/BookInfoRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b0\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0089\u0001\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u000eJ\u000b\u0010'\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010*\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010+\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010,\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010-\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010.\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010/\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u00101\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u008d\u0001\u00102\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u00103\u001a\u0002042\b\u00105\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u00106\u001a\u000207HÖ\u0001J\t\u00108\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\r\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0010\"\u0004\b\u0016\u0010\u0012R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0010\"\u0004\b\u0018\u0010\u0012R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0010\"\u0004\b\u001a\u0010\u0012R\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0010\"\u0004\b\u001c\u0010\u0012R\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u0010\"\u0004\b\u001e\u0010\u0012R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0010\"\u0004\b \u0010\u0012R\u001c\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0010\"\u0004\b\"\u0010\u0012R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010\u0010\"\u0004\b$\u0010\u0012R\u001c\u0010\f\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0010\"\u0004\b&\u0010\u0012¨\u00069"}, d2 = {"Lio/legado/app/data/entities/rule/BookInfoRule;", PackageDocumentBase.PREFIX_OPF, Action.init, PackageDocumentBase.PREFIX_OPF, "name", "author", "intro", "kind", "lastChapter", "updateTime", "coverUrl", "tocUrl", "wordCount", "canReName", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "getCanReName", "setCanReName", "getCoverUrl", "setCoverUrl", "getInit", "setInit", "getIntro", "setIntro", "getKind", "setKind", "getLastChapter", "setLastChapter", "getName", "setName", "getTocUrl", "setTocUrl", "getUpdateTime", "setUpdateTime", "getWordCount", "setWordCount", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class BookInfoRule {

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
    public final BookInfoRule copy(@Nullable String init, @Nullable String name, @Nullable String author, @Nullable String intro, @Nullable String kind, @Nullable String lastChapter, @Nullable String updateTime, @Nullable String coverUrl, @Nullable String tocUrl, @Nullable String wordCount, @Nullable String canReName) {
        return new BookInfoRule(init, name, author, intro, kind, lastChapter, updateTime, coverUrl, tocUrl, wordCount, canReName);
    }

    public static /* synthetic */ BookInfoRule copy$default(BookInfoRule bookInfoRule, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, int i, Object obj) {
        if ((i & 1) != 0) {
            str = bookInfoRule.init;
        }
        if ((i & 2) != 0) {
            str2 = bookInfoRule.name;
        }
        if ((i & 4) != 0) {
            str3 = bookInfoRule.author;
        }
        if ((i & 8) != 0) {
            str4 = bookInfoRule.intro;
        }
        if ((i & 16) != 0) {
            str5 = bookInfoRule.kind;
        }
        if ((i & 32) != 0) {
            str6 = bookInfoRule.lastChapter;
        }
        if ((i & 64) != 0) {
            str7 = bookInfoRule.updateTime;
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str8 = bookInfoRule.coverUrl;
        }
        if ((i & 256) != 0) {
            str9 = bookInfoRule.tocUrl;
        }
        if ((i & 512) != 0) {
            str10 = bookInfoRule.wordCount;
        }
        if ((i & 1024) != 0) {
            str11 = bookInfoRule.canReName;
        }
        return bookInfoRule.copy(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BookInfoRule(init=").append((Object) this.init).append(", name=").append((Object) this.name).append(", author=").append((Object) this.author).append(", intro=").append((Object) this.intro).append(", kind=").append((Object) this.kind).append(", lastChapter=").append((Object) this.lastChapter).append(", updateTime=").append((Object) this.updateTime).append(", coverUrl=").append((Object) this.coverUrl).append(", tocUrl=").append((Object) this.tocUrl).append(", wordCount=").append((Object) this.wordCount).append(", canReName=").append((Object) this.canReName).append(')');
        return sb.toString();
    }

    public int hashCode() {
        int result = this.init == null ? 0 : this.init.hashCode();
        return (((((((((((((((((((result * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.author == null ? 0 : this.author.hashCode())) * 31) + (this.intro == null ? 0 : this.intro.hashCode())) * 31) + (this.kind == null ? 0 : this.kind.hashCode())) * 31) + (this.lastChapter == null ? 0 : this.lastChapter.hashCode())) * 31) + (this.updateTime == null ? 0 : this.updateTime.hashCode())) * 31) + (this.coverUrl == null ? 0 : this.coverUrl.hashCode())) * 31) + (this.tocUrl == null ? 0 : this.tocUrl.hashCode())) * 31) + (this.wordCount == null ? 0 : this.wordCount.hashCode())) * 31) + (this.canReName == null ? 0 : this.canReName.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BookInfoRule)) {
            return false;
        }
        BookInfoRule bookInfoRule = (BookInfoRule) other;
        return Intrinsics.areEqual(this.init, bookInfoRule.init) && Intrinsics.areEqual(this.name, bookInfoRule.name) && Intrinsics.areEqual(this.author, bookInfoRule.author) && Intrinsics.areEqual(this.intro, bookInfoRule.intro) && Intrinsics.areEqual(this.kind, bookInfoRule.kind) && Intrinsics.areEqual(this.lastChapter, bookInfoRule.lastChapter) && Intrinsics.areEqual(this.updateTime, bookInfoRule.updateTime) && Intrinsics.areEqual(this.coverUrl, bookInfoRule.coverUrl) && Intrinsics.areEqual(this.tocUrl, bookInfoRule.tocUrl) && Intrinsics.areEqual(this.wordCount, bookInfoRule.wordCount) && Intrinsics.areEqual(this.canReName, bookInfoRule.canReName);
    }

    public BookInfoRule() {
        this(null, null, null, null, null, null, null, null, null, null, null, 2047, null);
    }

    public BookInfoRule(@Nullable String init, @Nullable String name, @Nullable String author, @Nullable String intro, @Nullable String kind, @Nullable String lastChapter, @Nullable String updateTime, @Nullable String coverUrl, @Nullable String tocUrl, @Nullable String wordCount, @Nullable String canReName) {
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

    public /* synthetic */ BookInfoRule(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6, (i & 64) != 0 ? null : str7, (i & Wbxml.EXT_T_0) != 0 ? null : str8, (i & 256) != 0 ? null : str9, (i & 512) != 0 ? null : str10, (i & 1024) != 0 ? null : str11);
    }

    @Nullable
    public final String getInit() {
        return this.init;
    }

    public final void setInit(@Nullable String str) {
        this.init = str;
    }

    @Nullable
    public final String getName() {
        return this.name;
    }

    public final void setName(@Nullable String str) {
        this.name = str;
    }

    @Nullable
    public final String getAuthor() {
        return this.author;
    }

    public final void setAuthor(@Nullable String str) {
        this.author = str;
    }

    @Nullable
    public final String getIntro() {
        return this.intro;
    }

    public final void setIntro(@Nullable String str) {
        this.intro = str;
    }

    @Nullable
    public final String getKind() {
        return this.kind;
    }

    public final void setKind(@Nullable String str) {
        this.kind = str;
    }

    @Nullable
    public final String getLastChapter() {
        return this.lastChapter;
    }

    public final void setLastChapter(@Nullable String str) {
        this.lastChapter = str;
    }

    @Nullable
    public final String getUpdateTime() {
        return this.updateTime;
    }

    public final void setUpdateTime(@Nullable String str) {
        this.updateTime = str;
    }

    @Nullable
    public final String getCoverUrl() {
        return this.coverUrl;
    }

    public final void setCoverUrl(@Nullable String str) {
        this.coverUrl = str;
    }

    @Nullable
    public final String getTocUrl() {
        return this.tocUrl;
    }

    public final void setTocUrl(@Nullable String str) {
        this.tocUrl = str;
    }

    @Nullable
    public final String getWordCount() {
        return this.wordCount;
    }

    public final void setWordCount(@Nullable String str) {
        this.wordCount = str;
    }

    @Nullable
    public final String getCanReName() {
        return this.canReName;
    }

    public final void setCanReName(@Nullable String str) {
        this.canReName = str;
    }
}
