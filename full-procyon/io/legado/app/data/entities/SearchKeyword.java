// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0012\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007?\u0006\u0002\u0010\bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J'\u0010\u0018\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001d\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¡§\u0006\u001e" }, d2 = { "Lio/legado/app/data/entities/SearchKeyword;", "", "word", "", "usage", "", "lastUseTime", "", "(Ljava/lang/String;IJ)V", "getLastUseTime", "()J", "setLastUseTime", "(J)V", "getUsage", "()I", "setUsage", "(I)V", "getWord", "()Ljava/lang/String;", "setWord", "(Ljava/lang/String;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "reader-pro" })
public final class SearchKeyword
{
    @NotNull
    private String word;
    private int usage;
    private long lastUseTime;
    
    public SearchKeyword(@NotNull final String word, final int usage, final long lastUseTime) {
        Intrinsics.checkNotNullParameter((Object)word, "word");
        this.word = word;
        this.usage = usage;
        this.lastUseTime = lastUseTime;
    }
    
    @NotNull
    public final String getWord() {
        return this.word;
    }
    
    public final void setWord(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.word = <set-?>;
    }
    
    public final int getUsage() {
        return this.usage;
    }
    
    public final void setUsage(final int <set-?>) {
        this.usage = <set-?>;
    }
    
    public final long getLastUseTime() {
        return this.lastUseTime;
    }
    
    public final void setLastUseTime(final long <set-?>) {
        this.lastUseTime = <set-?>;
    }
    
    @NotNull
    public final String component1() {
        return this.word;
    }
    
    public final int component2() {
        return this.usage;
    }
    
    public final long component3() {
        return this.lastUseTime;
    }
    
    @NotNull
    public final SearchKeyword copy(@NotNull final String word, final int usage, final long lastUseTime) {
        Intrinsics.checkNotNullParameter((Object)word, "word");
        return new SearchKeyword(word, usage, lastUseTime);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "SearchKeyword(word=" + this.word + ", usage=" + this.usage + ", lastUseTime=" + this.lastUseTime + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.word.hashCode();
        result = result * 31 + Integer.hashCode(this.usage);
        result = result * 31 + Long.hashCode(this.lastUseTime);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SearchKeyword)) {
            return false;
        }
        final SearchKeyword searchKeyword = (SearchKeyword)other;
        return Intrinsics.areEqual((Object)this.word, (Object)searchKeyword.word) && this.usage == searchKeyword.usage && this.lastUseTime == searchKeyword.lastUseTime;
    }
    
    public SearchKeyword() {
        this(null, 0, 0L, 7, null);
    }
}
