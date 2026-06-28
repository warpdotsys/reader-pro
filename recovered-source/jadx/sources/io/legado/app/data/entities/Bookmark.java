package io.legado.app.data.entities;

import io.legado.app.constant.RSSKeywords;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: Bookmark.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/Bookmark.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b!\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BU\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0005¢\u0006\u0002\u0010\rJ\t\u0010 \u001a\u00020\u0003HÆ\u0003J\t\u0010!\u001a\u00020\u0005HÆ\u0003J\t\u0010\"\u001a\u00020\u0005HÆ\u0003J\t\u0010#\u001a\u00020\bHÆ\u0003J\t\u0010$\u001a\u00020\bHÆ\u0003J\t\u0010%\u001a\u00020\u0005HÆ\u0003J\t\u0010&\u001a\u00020\u0005HÆ\u0003J\t\u0010'\u001a\u00020\u0005HÆ\u0003JY\u0010(\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u0005HÆ\u0001J\u0013\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010,\u001a\u00020\bHÖ\u0001J\t\u0010-\u001a\u00020\u0005HÖ\u0001R\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u000f\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\n\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000f\"\u0004\b\u0019\u0010\u0013R\u001a\u0010\t\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0015\"\u0004\b\u001b\u0010\u0017R\u001a\u0010\f\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u000f\"\u0004\b\u001d\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f¨\u0006."}, d2 = {"Lio/legado/app/data/entities/Bookmark;", PackageDocumentBase.PREFIX_OPF, RSSKeywords.RSS_ITEM_TIME, PackageDocumentBase.PREFIX_OPF, "bookName", PackageDocumentBase.PREFIX_OPF, "bookAuthor", "chapterIndex", PackageDocumentBase.PREFIX_OPF, "chapterPos", "chapterName", "bookText", "content", "(JLjava/lang/String;Ljava/lang/String;IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBookAuthor", "()Ljava/lang/String;", "getBookName", "getBookText", "setBookText", "(Ljava/lang/String;)V", "getChapterIndex", "()I", "setChapterIndex", "(I)V", "getChapterName", "setChapterName", "getChapterPos", "setChapterPos", "getContent", "setContent", "getTime", "()J", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class Bookmark {
    private final long time;

    @NotNull
    private final String bookName;

    @NotNull
    private final String bookAuthor;
    private int chapterIndex;
    private int chapterPos;

    @NotNull
    private String chapterName;

    @NotNull
    private String bookText;

    @NotNull
    private String content;

    public final long component1() {
        return this.time;
    }

    @NotNull
    public final String component2() {
        return this.bookName;
    }

    @NotNull
    public final String component3() {
        return this.bookAuthor;
    }

    public final int component4() {
        return this.chapterIndex;
    }

    public final int component5() {
        return this.chapterPos;
    }

    @NotNull
    public final String component6() {
        return this.chapterName;
    }

    @NotNull
    public final String component7() {
        return this.bookText;
    }

    @NotNull
    public final String component8() {
        return this.content;
    }

    @NotNull
    public final Bookmark copy(long time, @NotNull String bookName, @NotNull String bookAuthor, int chapterIndex, int chapterPos, @NotNull String chapterName, @NotNull String bookText, @NotNull String content) {
        Intrinsics.checkNotNullParameter(bookName, "bookName");
        Intrinsics.checkNotNullParameter(bookAuthor, "bookAuthor");
        Intrinsics.checkNotNullParameter(chapterName, "chapterName");
        Intrinsics.checkNotNullParameter(bookText, "bookText");
        Intrinsics.checkNotNullParameter(content, "content");
        return new Bookmark(time, bookName, bookAuthor, chapterIndex, chapterPos, chapterName, bookText, content);
    }

    public static /* synthetic */ Bookmark copy$default(Bookmark bookmark, long j, String str, String str2, int i, int i2, String str3, String str4, String str5, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            j = bookmark.time;
        }
        if ((i3 & 2) != 0) {
            str = bookmark.bookName;
        }
        if ((i3 & 4) != 0) {
            str2 = bookmark.bookAuthor;
        }
        if ((i3 & 8) != 0) {
            i = bookmark.chapterIndex;
        }
        if ((i3 & 16) != 0) {
            i2 = bookmark.chapterPos;
        }
        if ((i3 & 32) != 0) {
            str3 = bookmark.chapterName;
        }
        if ((i3 & 64) != 0) {
            str4 = bookmark.bookText;
        }
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            str5 = bookmark.content;
        }
        return bookmark.copy(j, str, str2, i, i2, str3, str4, str5);
    }

    @NotNull
    public String toString() {
        return "Bookmark(time=" + this.time + ", bookName=" + this.bookName + ", bookAuthor=" + this.bookAuthor + ", chapterIndex=" + this.chapterIndex + ", chapterPos=" + this.chapterPos + ", chapterName=" + this.chapterName + ", bookText=" + this.bookText + ", content=" + this.content + ')';
    }

    public int hashCode() {
        int result = Long.hashCode(this.time);
        return (((((((((((((result * 31) + this.bookName.hashCode()) * 31) + this.bookAuthor.hashCode()) * 31) + Integer.hashCode(this.chapterIndex)) * 31) + Integer.hashCode(this.chapterPos)) * 31) + this.chapterName.hashCode()) * 31) + this.bookText.hashCode()) * 31) + this.content.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Bookmark)) {
            return false;
        }
        Bookmark bookmark = (Bookmark) other;
        return this.time == bookmark.time && Intrinsics.areEqual(this.bookName, bookmark.bookName) && Intrinsics.areEqual(this.bookAuthor, bookmark.bookAuthor) && this.chapterIndex == bookmark.chapterIndex && this.chapterPos == bookmark.chapterPos && Intrinsics.areEqual(this.chapterName, bookmark.chapterName) && Intrinsics.areEqual(this.bookText, bookmark.bookText) && Intrinsics.areEqual(this.content, bookmark.content);
    }

    public Bookmark() {
        this(0L, null, null, 0, 0, null, null, null, 255, null);
    }

    public Bookmark(long time, @NotNull String bookName, @NotNull String bookAuthor, int chapterIndex, int chapterPos, @NotNull String chapterName, @NotNull String bookText, @NotNull String content) {
        Intrinsics.checkNotNullParameter(bookName, "bookName");
        Intrinsics.checkNotNullParameter(bookAuthor, "bookAuthor");
        Intrinsics.checkNotNullParameter(chapterName, "chapterName");
        Intrinsics.checkNotNullParameter(bookText, "bookText");
        Intrinsics.checkNotNullParameter(content, "content");
        this.time = time;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.chapterIndex = chapterIndex;
        this.chapterPos = chapterPos;
        this.chapterName = chapterName;
        this.bookText = bookText;
        this.content = content;
    }

    public /* synthetic */ Bookmark(long j, String str, String str2, int i, int i2, String str3, String str4, String str5, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? System.currentTimeMillis() : j, (i3 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i3 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i3 & 8) != 0 ? 0 : i, (i3 & 16) != 0 ? 0 : i2, (i3 & 32) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i3 & 64) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i3 & Wbxml.EXT_T_0) != 0 ? PackageDocumentBase.PREFIX_OPF : str5);
    }

    public final long getTime() {
        return this.time;
    }

    @NotNull
    public final String getBookName() {
        return this.bookName;
    }

    @NotNull
    public final String getBookAuthor() {
        return this.bookAuthor;
    }

    public final int getChapterIndex() {
        return this.chapterIndex;
    }

    public final void setChapterIndex(int i) {
        this.chapterIndex = i;
    }

    public final int getChapterPos() {
        return this.chapterPos;
    }

    public final void setChapterPos(int i) {
        this.chapterPos = i;
    }

    @NotNull
    public final String getChapterName() {
        return this.chapterName;
    }

    public final void setChapterName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.chapterName = str;
    }

    @NotNull
    public final String getBookText() {
        return this.bookText;
    }

    public final void setBookText(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.bookText = str;
    }

    @NotNull
    public final String getContent() {
        return this.content;
    }

    public final void setContent(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.content = str;
    }
}
