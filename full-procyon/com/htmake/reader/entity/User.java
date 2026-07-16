// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b9\b\u0086\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012?\u0006\u0002\u0010\u0014J\t\u00109\u001a\u00020\u0003H\u00c6\u0003J\t\u0010:\u001a\u00020\u000bH\u00c6\u0003J\t\u0010;\u001a\u00020\u000bH\u00c6\u0003J\t\u0010<\u001a\u00020\u0012H\u00c6\u0003J\t\u0010=\u001a\u00020\u0012H\u00c6\u0003J\t\u0010>\u001a\u00020\u0003H\u00c6\u0003J\t\u0010?\u001a\u00020\u0003H\u00c6\u0003J\t\u0010@\u001a\u00020\u0003H\u00c6\u0003J\t\u0010A\u001a\u00020\bH\u00c6\u0003J\t\u0010B\u001a\u00020\bH\u00c6\u0003J\t\u0010C\u001a\u00020\u000bH\u00c6\u0003J\u0017\u0010D\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\rH\u00c6\u0003J\t\u0010E\u001a\u00020\u000bH\u00c6\u0003J\u0099\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0012H\u00c6\u0001J\u0013\u0010G\u001a\u00020\u000b2\b\u0010H\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010I\u001a\u00020\u0012H\u00d6\u0001J\t\u0010J\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0013\u001a\u00020\u0012X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0016\"\u0004\b\u001a\u0010\u0018R\u001a\u0010\t\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u000f\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u000e\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010 \"\u0004\b$\u0010\"R\u001a\u0010\u0010\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b%\u0010 \"\u0004\b&\u0010\"R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b'\u0010 \"\u0004\b(\u0010\"R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001c\"\u0004\b*\u0010\u001eR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u0010,\"\u0004\b0\u0010.R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b1\u0010,\"\u0004\b2\u0010.R(\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\rX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b7\u0010,\"\u0004\b8\u0010.¡§\u0006K" }, d2 = { "Lcom/htmake/reader/entity/User;", "", "username", "", "password", "salt", "token", "last_login_at", "", "created_at", "enable_webdav", "", "token_map", "", "enable_local_store", "enable_book_source", "enable_rss_source", "book_source_limit", "", "book_limit", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZLjava/util/Map;ZZZII)V", "getBook_limit", "()I", "setBook_limit", "(I)V", "getBook_source_limit", "setBook_source_limit", "getCreated_at", "()J", "setCreated_at", "(J)V", "getEnable_book_source", "()Z", "setEnable_book_source", "(Z)V", "getEnable_local_store", "setEnable_local_store", "getEnable_rss_source", "setEnable_rss_source", "getEnable_webdav", "setEnable_webdav", "getLast_login_at", "setLast_login_at", "getPassword", "()Ljava/lang/String;", "setPassword", "(Ljava/lang/String;)V", "getSalt", "setSalt", "getToken", "setToken", "getToken_map", "()Ljava/util/Map;", "setToken_map", "(Ljava/util/Map;)V", "getUsername", "setUsername", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
public final class User
{
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String salt;
    @NotNull
    private String token;
    private long last_login_at;
    private long created_at;
    private boolean enable_webdav;
    @Nullable
    private Map<String, Long> token_map;
    private boolean enable_local_store;
    private boolean enable_book_source;
    private boolean enable_rss_source;
    private int book_source_limit;
    private int book_limit;
    
    public User(@NotNull final String username, @NotNull final String password, @NotNull final String salt, @NotNull final String token, final long last_login_at, final long created_at, final boolean enable_webdav, @Nullable final Map<String, Long> token_map, final boolean enable_local_store, final boolean enable_book_source, final boolean enable_rss_source, final int book_source_limit, final int book_limit) {
        Intrinsics.checkNotNullParameter((Object)username, "username");
        Intrinsics.checkNotNullParameter((Object)password, "password");
        Intrinsics.checkNotNullParameter((Object)salt, "salt");
        Intrinsics.checkNotNullParameter((Object)token, "token");
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.token = token;
        this.last_login_at = last_login_at;
        this.created_at = created_at;
        this.enable_webdav = enable_webdav;
        this.token_map = token_map;
        this.enable_local_store = enable_local_store;
        this.enable_book_source = enable_book_source;
        this.enable_rss_source = enable_rss_source;
        this.book_source_limit = book_source_limit;
        this.book_limit = book_limit;
    }
    
    @NotNull
    public final String getUsername() {
        return this.username;
    }
    
    public final void setUsername(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.username = <set-?>;
    }
    
    @NotNull
    public final String getPassword() {
        return this.password;
    }
    
    public final void setPassword(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.password = <set-?>;
    }
    
    @NotNull
    public final String getSalt() {
        return this.salt;
    }
    
    public final void setSalt(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.salt = <set-?>;
    }
    
    @NotNull
    public final String getToken() {
        return this.token;
    }
    
    public final void setToken(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.token = <set-?>;
    }
    
    public final long getLast_login_at() {
        return this.last_login_at;
    }
    
    public final void setLast_login_at(final long <set-?>) {
        this.last_login_at = <set-?>;
    }
    
    public final long getCreated_at() {
        return this.created_at;
    }
    
    public final void setCreated_at(final long <set-?>) {
        this.created_at = <set-?>;
    }
    
    public final boolean getEnable_webdav() {
        return this.enable_webdav;
    }
    
    public final void setEnable_webdav(final boolean <set-?>) {
        this.enable_webdav = <set-?>;
    }
    
    @Nullable
    public final Map<String, Long> getToken_map() {
        return this.token_map;
    }
    
    public final void setToken_map(@Nullable final Map<String, Long> <set-?>) {
        this.token_map = <set-?>;
    }
    
    public final boolean getEnable_local_store() {
        return this.enable_local_store;
    }
    
    public final void setEnable_local_store(final boolean <set-?>) {
        this.enable_local_store = <set-?>;
    }
    
    public final boolean getEnable_book_source() {
        return this.enable_book_source;
    }
    
    public final void setEnable_book_source(final boolean <set-?>) {
        this.enable_book_source = <set-?>;
    }
    
    public final boolean getEnable_rss_source() {
        return this.enable_rss_source;
    }
    
    public final void setEnable_rss_source(final boolean <set-?>) {
        this.enable_rss_source = <set-?>;
    }
    
    public final int getBook_source_limit() {
        return this.book_source_limit;
    }
    
    public final void setBook_source_limit(final int <set-?>) {
        this.book_source_limit = <set-?>;
    }
    
    public final int getBook_limit() {
        return this.book_limit;
    }
    
    public final void setBook_limit(final int <set-?>) {
        this.book_limit = <set-?>;
    }
    
    @NotNull
    public final String component1() {
        return this.username;
    }
    
    @NotNull
    public final String component2() {
        return this.password;
    }
    
    @NotNull
    public final String component3() {
        return this.salt;
    }
    
    @NotNull
    public final String component4() {
        return this.token;
    }
    
    public final long component5() {
        return this.last_login_at;
    }
    
    public final long component6() {
        return this.created_at;
    }
    
    public final boolean component7() {
        return this.enable_webdav;
    }
    
    @Nullable
    public final Map<String, Long> component8() {
        return this.token_map;
    }
    
    public final boolean component9() {
        return this.enable_local_store;
    }
    
    public final boolean component10() {
        return this.enable_book_source;
    }
    
    public final boolean component11() {
        return this.enable_rss_source;
    }
    
    public final int component12() {
        return this.book_source_limit;
    }
    
    public final int component13() {
        return this.book_limit;
    }
    
    @NotNull
    public final User copy(@NotNull final String username, @NotNull final String password, @NotNull final String salt, @NotNull final String token, final long last_login_at, final long created_at, final boolean enable_webdav, @Nullable final Map<String, Long> token_map, final boolean enable_local_store, final boolean enable_book_source, final boolean enable_rss_source, final int book_source_limit, final int book_limit) {
        Intrinsics.checkNotNullParameter((Object)username, "username");
        Intrinsics.checkNotNullParameter((Object)password, "password");
        Intrinsics.checkNotNullParameter((Object)salt, "salt");
        Intrinsics.checkNotNullParameter((Object)token, "token");
        return new User(username, password, salt, token, last_login_at, created_at, enable_webdav, token_map, enable_local_store, enable_book_source, enable_rss_source, book_source_limit, book_limit);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("User(username=").append(this.username).append(", password=").append(this.password).append(", salt=").append(this.salt).append(", token=").append(this.token).append(", last_login_at=").append(this.last_login_at).append(", created_at=").append(this.created_at).append(", enable_webdav=").append(this.enable_webdav).append(", token_map=").append(this.token_map).append(", enable_local_store=").append(this.enable_local_store).append(", enable_book_source=").append(this.enable_book_source).append(", enable_rss_source=").append(this.enable_rss_source).append(", book_source_limit=");
        sb.append(this.book_source_limit).append(", book_limit=").append(this.book_limit).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = this.username.hashCode();
        result = result * 31 + this.password.hashCode();
        result = result * 31 + this.salt.hashCode();
        result = result * 31 + this.token.hashCode();
        result = result * 31 + Long.hashCode(this.last_login_at);
        result = result * 31 + Long.hashCode(this.created_at);
        final int n = result * 31;
        int enable_webdav;
        if ((enable_webdav = (this.enable_webdav ? 1 : 0)) != 0) {
            enable_webdav = 1;
        }
        result = n + enable_webdav;
        result = result * 31 + ((this.token_map == null) ? 0 : this.token_map.hashCode());
        final int n2 = result * 31;
        int enable_local_store;
        if ((enable_local_store = (this.enable_local_store ? 1 : 0)) != 0) {
            enable_local_store = 1;
        }
        result = n2 + enable_local_store;
        final int n3 = result * 31;
        int enable_book_source;
        if ((enable_book_source = (this.enable_book_source ? 1 : 0)) != 0) {
            enable_book_source = 1;
        }
        result = n3 + enable_book_source;
        final int n4 = result * 31;
        int enable_rss_source;
        if ((enable_rss_source = (this.enable_rss_source ? 1 : 0)) != 0) {
            enable_rss_source = 1;
        }
        result = n4 + enable_rss_source;
        result = result * 31 + Integer.hashCode(this.book_source_limit);
        result = result * 31 + Integer.hashCode(this.book_limit);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        final User user = (User)other;
        return Intrinsics.areEqual((Object)this.username, (Object)user.username) && Intrinsics.areEqual((Object)this.password, (Object)user.password) && Intrinsics.areEqual((Object)this.salt, (Object)user.salt) && Intrinsics.areEqual((Object)this.token, (Object)user.token) && this.last_login_at == user.last_login_at && this.created_at == user.created_at && this.enable_webdav == user.enable_webdav && Intrinsics.areEqual((Object)this.token_map, (Object)user.token_map) && this.enable_local_store == user.enable_local_store && this.enable_book_source == user.enable_book_source && this.enable_rss_source == user.enable_rss_source && this.book_source_limit == user.book_source_limit && this.book_limit == user.book_limit;
    }
    
    public User() {
        this(null, null, null, null, 0L, 0L, false, null, false, false, false, 0, 0, 8191, null);
    }
}
