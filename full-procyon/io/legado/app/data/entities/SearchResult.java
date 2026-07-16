// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b \n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001Bi\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\u0003\u0012\b\b\u0002\u0010\n\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003?\u0006\u0002\u0010\u000eJ\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0006H\u00c6\u0003J\t\u0010 \u001a\u00020\u0006H\u00c6\u0003J\t\u0010!\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\t\u0010#\u001a\u00020\u0003H\u00c6\u0003J\t\u0010$\u001a\u00020\u0003H\u00c6\u0003Jm\u0010%\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010)\u001a\u00020\u0003H\u00d6\u0001J\t\u0010*\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\n\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0007\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u000b\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010R\u0011\u0010\t\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\b\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0012R\u0011\u0010\r\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\f\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0010R\u0011\u0010\u0004\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006?\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012¡§\u0006+" }, d2 = { "Lio/legado/app/data/entities/SearchResult;", "", "resultCount", "", "resultCountWithinChapter", "resultText", "", "chapterTitle", "query", "pageSize", "chapterIndex", "pageIndex", "queryIndexInResult", "queryIndexInChapter", "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;IIIII)V", "getChapterIndex", "()I", "getChapterTitle", "()Ljava/lang/String;", "getPageIndex", "getPageSize", "getQuery", "getQueryIndexInChapter", "getQueryIndexInResult", "getResultCount", "getResultCountWithinChapter", "getResultText", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "", "other", "hashCode", "toString", "reader-pro" })
public final class SearchResult
{
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
    
    public SearchResult(final int resultCount, final int resultCountWithinChapter, @NotNull final String resultText, @NotNull final String chapterTitle, @NotNull final String query, final int pageSize, final int chapterIndex, final int pageIndex, final int queryIndexInResult, final int queryIndexInChapter) {
        Intrinsics.checkNotNullParameter((Object)resultText, "resultText");
        Intrinsics.checkNotNullParameter((Object)chapterTitle, "chapterTitle");
        Intrinsics.checkNotNullParameter((Object)query, "query");
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
    public final SearchResult copy(final int resultCount, final int resultCountWithinChapter, @NotNull final String resultText, @NotNull final String chapterTitle, @NotNull final String query, final int pageSize, final int chapterIndex, final int pageIndex, final int queryIndexInResult, final int queryIndexInChapter) {
        Intrinsics.checkNotNullParameter((Object)resultText, "resultText");
        Intrinsics.checkNotNullParameter((Object)chapterTitle, "chapterTitle");
        Intrinsics.checkNotNullParameter((Object)query, "query");
        return new SearchResult(resultCount, resultCountWithinChapter, resultText, chapterTitle, query, pageSize, chapterIndex, pageIndex, queryIndexInResult, queryIndexInChapter);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "SearchResult(resultCount=" + this.resultCount + ", resultCountWithinChapter=" + this.resultCountWithinChapter + ", resultText=" + this.resultText + ", chapterTitle=" + this.chapterTitle + ", query=" + this.query + ", pageSize=" + this.pageSize + ", chapterIndex=" + this.chapterIndex + ", pageIndex=" + this.pageIndex + ", queryIndexInResult=" + this.queryIndexInResult + ", queryIndexInChapter=" + this.queryIndexInChapter + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Integer.hashCode(this.resultCount);
        result = result * 31 + Integer.hashCode(this.resultCountWithinChapter);
        result = result * 31 + this.resultText.hashCode();
        result = result * 31 + this.chapterTitle.hashCode();
        result = result * 31 + this.query.hashCode();
        result = result * 31 + Integer.hashCode(this.pageSize);
        result = result * 31 + Integer.hashCode(this.chapterIndex);
        result = result * 31 + Integer.hashCode(this.pageIndex);
        result = result * 31 + Integer.hashCode(this.queryIndexInResult);
        result = result * 31 + Integer.hashCode(this.queryIndexInChapter);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SearchResult)) {
            return false;
        }
        final SearchResult searchResult = (SearchResult)other;
        return this.resultCount == searchResult.resultCount && this.resultCountWithinChapter == searchResult.resultCountWithinChapter && Intrinsics.areEqual((Object)this.resultText, (Object)searchResult.resultText) && Intrinsics.areEqual((Object)this.chapterTitle, (Object)searchResult.chapterTitle) && Intrinsics.areEqual((Object)this.query, (Object)searchResult.query) && this.pageSize == searchResult.pageSize && this.chapterIndex == searchResult.chapterIndex && this.pageIndex == searchResult.pageIndex && this.queryIndexInResult == searchResult.queryIndexInResult && this.queryIndexInChapter == searchResult.queryIndexInChapter;
    }
    
    public SearchResult() {
        this(0, 0, null, null, null, 0, 0, 0, 0, 0, 1023, null);
    }
}
