// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities.rule;

import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b \bf\u0018\u00002\u00020\u0001R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u001a\u0010\b\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\t\u0010\u0005\"\u0004\b\n\u0010\u0007R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u001a\u0010\u0011\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007R\u001a\u0010\u0017\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0018\u0010\u0005\"\u0004\b\u0019\u0010\u0007R\u001a\u0010\u001a\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u001b\u0010\u0005\"\u0004\b\u001c\u0010\u0007R\u001a\u0010\u001d\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u001e\u0010\u0005\"\u0004\b\u001f\u0010\u0007R\u001a\u0010 \u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b!\u0010\u0005\"\u0004\b\"\u0010\u0007¡§\u0006#" }, d2 = { "Lio/legado/app/data/entities/rule/BookListRule;", "", "author", "", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "bookList", "getBookList", "setBookList", "bookUrl", "getBookUrl", "setBookUrl", "coverUrl", "getCoverUrl", "setCoverUrl", "intro", "getIntro", "setIntro", "kind", "getKind", "setKind", "lastChapter", "getLastChapter", "setLastChapter", "name", "getName", "setName", "updateTime", "getUpdateTime", "setUpdateTime", "wordCount", "getWordCount", "setWordCount", "reader-pro" })
public interface BookListRule
{
    @Nullable
    String getBookList();
    
    void setBookList(@Nullable final String <set-?>);
    
    @Nullable
    String getName();
    
    void setName(@Nullable final String <set-?>);
    
    @Nullable
    String getAuthor();
    
    void setAuthor(@Nullable final String <set-?>);
    
    @Nullable
    String getIntro();
    
    void setIntro(@Nullable final String <set-?>);
    
    @Nullable
    String getKind();
    
    void setKind(@Nullable final String <set-?>);
    
    @Nullable
    String getLastChapter();
    
    void setLastChapter(@Nullable final String <set-?>);
    
    @Nullable
    String getUpdateTime();
    
    void setUpdateTime(@Nullable final String <set-?>);
    
    @Nullable
    String getBookUrl();
    
    void setBookUrl(@Nullable final String <set-?>);
    
    @Nullable
    String getCoverUrl();
    
    void setCoverUrl(@Nullable final String <set-?>);
    
    @Nullable
    String getWordCount();
    
    void setWordCount(@Nullable final String <set-?>);
}
