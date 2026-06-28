package io.legado.app.data.entities;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: SearchResult.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/SearchResult.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001Bi\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003¢\u0006\u0002\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001d\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001e\u001a\u00020\u0006HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0006HÆ\u0003J\t\u0010 \u001a\u00020\u0006HÆ\u0003J\t\u0010!\u001a\u00020\u0003HÆ\u0003J\t\u0010\"\u001a\u00020\u0003HÆ\u0003J\t\u0010#\u001a\u00020\u0003HÆ\u0003J\t\u0010$\u001a\u00020\u0003HÆ\u0003Jm\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u0003HÆ\u0001J\u0013\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010)\u001a\u00020\u0003HÖ\u0001J\t\u0010*\u001a\u00020\u0006HÖ\u0001R\u0011\u0010\n\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012¨\u0006+"}, d2 = {"Lio/legado/app/data/entities/SearchResult;", PackageDocumentBase.PREFIX_OPF, "resultCount", PackageDocumentBase.PREFIX_OPF, "resultCountWithinChapter", "resultText", PackageDocumentBase.PREFIX_OPF, "chapterTitle", "query", "pageSize", "chapterIndex", "pageIndex", "queryIndexInResult", "queryIndexInChapter", "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIII)V", "getChapterIndex", "()I", "getChapterTitle", "()Ljava/lang/String;", "getPageIndex", "getPageSize", "getQuery", "getQueryIndexInChapter", "getQueryIndexInResult", "getResultCount", "getResultCountWithinChapter", "getResultText", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class SearchResult {
    private final int resultCount;
    private final int resultCountWithinChapter;

    @NotNull
    private final String resultText;

    @NotNull
    private final String chapterTitle;

    @NotNull
    private final String query;
    private final int pageSize;
    private final int chapterIndex;
    private final int pageIndex;
    private final int queryIndexInResult;
    private final int queryIndexInChapter;

    public final int component1() {
        return this.resultCount;
    }

    public final int component2() {
        return this.resultCountWithinChapter;
    }

    @NotNull
    public final String component3() {
        return this.resultText;
    }

    @NotNull
    public final String component4() {
        return this.chapterTitle;
    }

    @NotNull
    public final String component5() {
        return this.query;
    }

    public final int component6() {
        return this.pageSize;
    }

    public final int component7() {
        return this.chapterIndex;
    }

    public final int component8() {
        return this.pageIndex;
    }

    public final int component9() {
        return this.queryIndexInResult;
    }

    public final int component10() {
        return this.queryIndexInChapter;
    }

    @NotNull
    public final SearchResult copy(int resultCount, int resultCountWithinChapter, @NotNull String resultText, @NotNull String chapterTitle, @NotNull String query, int pageSize, int chapterIndex, int pageIndex, int queryIndexInResult, int queryIndexInChapter) {
        Intrinsics.checkNotNullParameter(resultText, "resultText");
        Intrinsics.checkNotNullParameter(chapterTitle, "chapterTitle");
        Intrinsics.checkNotNullParameter(query, "query");
        return new SearchResult(resultCount, resultCountWithinChapter, resultText, chapterTitle, query, pageSize, chapterIndex, pageIndex, queryIndexInResult, queryIndexInChapter);
    }

    public static /* synthetic */ SearchResult copy$default(SearchResult searchResult, int i, int i2, String str, String str2, String str3, int i3, int i4, int i5, int i6, int i7, int i8, Object obj) {
        if ((i8 & 1) != 0) {
            i = searchResult.resultCount;
        }
        if ((i8 & 2) != 0) {
            i2 = searchResult.resultCountWithinChapter;
        }
        if ((i8 & 4) != 0) {
            str = searchResult.resultText;
        }
        if ((i8 & 8) != 0) {
            str2 = searchResult.chapterTitle;
        }
        if ((i8 & 16) != 0) {
            str3 = searchResult.query;
        }
        if ((i8 & 32) != 0) {
            i3 = searchResult.pageSize;
        }
        if ((i8 & 64) != 0) {
            i4 = searchResult.chapterIndex;
        }
        if ((i8 & Wbxml.EXT_T_0) != 0) {
            i5 = searchResult.pageIndex;
        }
        if ((i8 & 256) != 0) {
            i6 = searchResult.queryIndexInResult;
        }
        if ((i8 & 512) != 0) {
            i7 = searchResult.queryIndexInChapter;
        }
        return searchResult.copy(i, i2, str, str2, str3, i3, i4, i5, i6, i7);
    }

    @NotNull
    public String toString() {
        return "SearchResult(resultCount=" + this.resultCount + ", resultCountWithinChapter=" + this.resultCountWithinChapter + ", resultText=" + this.resultText + ", chapterTitle=" + this.chapterTitle + ", query=" + this.query + ", pageSize=" + this.pageSize + ", chapterIndex=" + this.chapterIndex + ", pageIndex=" + this.pageIndex + ", queryIndexInResult=" + this.queryIndexInResult + ", queryIndexInChapter=" + this.queryIndexInChapter + ')';
    }

    public int hashCode() {
        int result = Integer.hashCode(this.resultCount);
        return (((((((((((((((((result * 31) + Integer.hashCode(this.resultCountWithinChapter)) * 31) + this.resultText.hashCode()) * 31) + this.chapterTitle.hashCode()) * 31) + this.query.hashCode()) * 31) + Integer.hashCode(this.pageSize)) * 31) + Integer.hashCode(this.chapterIndex)) * 31) + Integer.hashCode(this.pageIndex)) * 31) + Integer.hashCode(this.queryIndexInResult)) * 31) + Integer.hashCode(this.queryIndexInChapter);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SearchResult)) {
            return false;
        }
        SearchResult searchResult = (SearchResult) other;
        return this.resultCount == searchResult.resultCount && this.resultCountWithinChapter == searchResult.resultCountWithinChapter && Intrinsics.areEqual(this.resultText, searchResult.resultText) && Intrinsics.areEqual(this.chapterTitle, searchResult.chapterTitle) && Intrinsics.areEqual(this.query, searchResult.query) && this.pageSize == searchResult.pageSize && this.chapterIndex == searchResult.chapterIndex && this.pageIndex == searchResult.pageIndex && this.queryIndexInResult == searchResult.queryIndexInResult && this.queryIndexInChapter == searchResult.queryIndexInChapter;
    }

    public SearchResult() {
        this(0, 0, null, null, null, 0, 0, 0, 0, 0, 1023, null);
    }

    public SearchResult(int resultCount, int resultCountWithinChapter, @NotNull String resultText, @NotNull String chapterTitle, @NotNull String query, int pageSize, int chapterIndex, int pageIndex, int queryIndexInResult, int queryIndexInChapter) {
        Intrinsics.checkNotNullParameter(resultText, "resultText");
        Intrinsics.checkNotNullParameter(chapterTitle, "chapterTitle");
        Intrinsics.checkNotNullParameter(query, "query");
        this.resultCount = resultCount;
        this.resultCountWithinChapter = resultCountWithinChapter;
        this.resultText = resultText;
        this.chapterTitle = chapterTitle;
        this.query = query;
        this.pageSize = pageSize;
        this.chapterIndex = chapterIndex;
        this.pageIndex = pageIndex;
        this.queryIndexInResult = queryIndexInResult;
        this.queryIndexInChapter = queryIndexInChapter;
    }

    public /* synthetic */ SearchResult(int i, int i2, String str, String str2, String str3, int i3, int i4, int i5, int i6, int i7, int i8, DefaultConstructorMarker defaultConstructorMarker) {
        this((i8 & 1) != 0 ? 0 : i, (i8 & 2) != 0 ? 0 : i2, (i8 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i8 & 8) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i8 & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i8 & 32) != 0 ? 0 : i3, (i8 & 64) != 0 ? 0 : i4, (i8 & Wbxml.EXT_T_0) != 0 ? 0 : i5, (i8 & 256) != 0 ? 0 : i6, (i8 & 512) != 0 ? 0 : i7);
    }

    public final int getResultCount() {
        return this.resultCount;
    }

    public final int getResultCountWithinChapter() {
        return this.resultCountWithinChapter;
    }

    @NotNull
    public final String getResultText() {
        return this.resultText;
    }

    @NotNull
    public final String getChapterTitle() {
        return this.chapterTitle;
    }

    @NotNull
    public final String getQuery() {
        return this.query;
    }

    public final int getPageSize() {
        return this.pageSize;
    }

    public final int getChapterIndex() {
        return this.chapterIndex;
    }

    public final int getPageIndex() {
        return this.pageIndex;
    }

    public final int getQueryIndexInResult() {
        return this.queryIndexInResult;
    }

    public final int getQueryIndexInChapter() {
        return this.queryIndexInChapter;
    }
}
