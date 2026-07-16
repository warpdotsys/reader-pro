// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006?\u0006\u0002\u0010\bJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0006H\u00c6\u0003J1\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u0006H\u00c6\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u001eH\u00d6\u0001J\t\u0010\u001f\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\n\"\u0004\b\u0012\u0010\fR\u001a\u0010\u0007\u001a\u00020\u0006X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u000e\"\u0004\b\u0014\u0010\u0010¡§\u0006 " }, d2 = { "Lcom/htmake/reader/entity/MongoFile;", "", "path", "", "content", "created_at", "", "updated_at", "(Ljava/lang/String;Ljava/lang/String;JJ)V", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getCreated_at", "()J", "setCreated_at", "(J)V", "getPath", "setPath", "getUpdated_at", "setUpdated_at", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class MongoFile
{
    @NotNull
    private String path;
    @NotNull
    private String content;
    private long created_at;
    private long updated_at;
    
    public MongoFile(@NotNull final String path, @NotNull final String content, final long created_at, final long updated_at) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        this.path = path;
        this.content = content;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    
    @NotNull
    public final String getPath() {
        return this.path;
    }
    
    public final void setPath(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.path = <set-?>;
    }
    
    @NotNull
    public final String getContent() {
        return this.content;
    }
    
    public final void setContent(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.content = <set-?>;
    }
    
    public final long getCreated_at() {
        return this.created_at;
    }
    
    public final void setCreated_at(final long <set-?>) {
        this.created_at = <set-?>;
    }
    
    public final long getUpdated_at() {
        return this.updated_at;
    }
    
    public final void setUpdated_at(final long <set-?>) {
        this.updated_at = <set-?>;
    }
    
    @NotNull
    public final String component1() {
        return this.path;
    }
    
    @NotNull
    public final String component2() {
        return this.content;
    }
    
    public final long component3() {
        return this.created_at;
    }
    
    public final long component4() {
        return this.updated_at;
    }
    
    @NotNull
    public final MongoFile copy(@NotNull final String path, @NotNull final String content, final long created_at, final long updated_at) {
        Intrinsics.checkNotNullParameter((Object)path, "path");
        Intrinsics.checkNotNullParameter((Object)content, "content");
        return new MongoFile(path, content, created_at, updated_at);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "MongoFile(path=" + this.path + ", content=" + this.content + ", created_at=" + this.created_at + ", updated_at=" + this.updated_at + ')';
    }
    
    @Override
    public int hashCode() {
        int result = this.path.hashCode();
        result = result * 31 + this.content.hashCode();
        result = result * 31 + Long.hashCode(this.created_at);
        result = result * 31 + Long.hashCode(this.updated_at);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MongoFile)) {
            return false;
        }
        final MongoFile mongoFile = (MongoFile)other;
        return Intrinsics.areEqual((Object)this.path, (Object)mongoFile.path) && Intrinsics.areEqual((Object)this.content, (Object)mongoFile.content) && this.created_at == mongoFile.created_at && this.updated_at == mongoFile.updated_at;
    }
    
    public MongoFile() {
        this(null, null, 0L, 0L, 15, null);
    }
}
