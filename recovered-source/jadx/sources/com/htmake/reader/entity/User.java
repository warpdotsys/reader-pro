package com.htmake.reader.entity;

import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: User.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/entity/User.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010$\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b9\b\u0086\b\u0018\u00002\u00020\u0001B\u0095\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0012¢\u0006\u0002\u0010\u0014J\t\u00109\u001a\u00020\u0003HÆ\u0003J\t\u0010:\u001a\u00020\u000bHÆ\u0003J\t\u0010;\u001a\u00020\u000bHÆ\u0003J\t\u0010<\u001a\u00020\u0012HÆ\u0003J\t\u0010=\u001a\u00020\u0012HÆ\u0003J\t\u0010>\u001a\u00020\u0003HÆ\u0003J\t\u0010?\u001a\u00020\u0003HÆ\u0003J\t\u0010@\u001a\u00020\u0003HÆ\u0003J\t\u0010A\u001a\u00020\bHÆ\u0003J\t\u0010B\u001a\u00020\bHÆ\u0003J\t\u0010C\u001a\u00020\u000bHÆ\u0003J\u0017\u0010D\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\rHÆ\u0003J\t\u0010E\u001a\u00020\u000bHÆ\u0003J\u0099\u0001\u0010F\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000b2\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u000b2\b\b\u0002\u0010\u0010\u001a\u00020\u000b2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u0012HÆ\u0001J\u0013\u0010G\u001a\u00020\u000b2\b\u0010H\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010I\u001a\u00020\u0012HÖ\u0001J\t\u0010J\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0013\u001a\u00020\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0011\u001a\u00020\u0012X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0016\"\u0004\b\u001a\u0010\u0018R\u001a\u0010\t\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u000f\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u000e\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010 \"\u0004\b$\u0010\"R\u001a\u0010\u0010\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010 \"\u0004\b&\u0010\"R\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010 \"\u0004\b(\u0010\"R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001c\"\u0004\b*\u0010\u001eR\u001a\u0010\u0004\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010,\"\u0004\b-\u0010.R\u001a\u0010\u0005\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010,\"\u0004\b0\u0010.R\u001a\u0010\u0006\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010,\"\u0004\b2\u0010.R(\u0010\f\u001a\u0010\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\b\u0018\u00010\rX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u00104\"\u0004\b5\u00106R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b7\u0010,\"\u0004\b8\u0010.¨\u0006K"}, d2 = {"Lcom/htmake/reader/entity/User;", PackageDocumentBase.PREFIX_OPF, "username", PackageDocumentBase.PREFIX_OPF, "password", "salt", "token", "last_login_at", PackageDocumentBase.PREFIX_OPF, "created_at", "enable_webdav", PackageDocumentBase.PREFIX_OPF, "token_map", PackageDocumentBase.PREFIX_OPF, "enable_local_store", "enable_book_source", "enable_rss_source", "book_source_limit", PackageDocumentBase.PREFIX_OPF, "book_limit", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJZLjava/util/Map;ZZZII)V", "getBook_limit", "()I", "setBook_limit", "(I)V", "getBook_source_limit", "setBook_source_limit", "getCreated_at", "()J", "setCreated_at", "(J)V", "getEnable_book_source", "()Z", "setEnable_book_source", "(Z)V", "getEnable_local_store", "setEnable_local_store", "getEnable_rss_source", "setEnable_rss_source", "getEnable_webdav", "setEnable_webdav", "getLast_login_at", "setLast_login_at", "getPassword", "()Ljava/lang/String;", "setPassword", "(Ljava/lang/String;)V", "getSalt", "setSalt", "getToken", "setToken", "getToken_map", "()Ljava/util/Map;", "setToken_map", "(Ljava/util/Map;)V", "getUsername", "setUsername", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class User {

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
    public final User copy(@NotNull String username, @NotNull String password, @NotNull String salt, @NotNull String token, long last_login_at, long created_at, boolean enable_webdav, @Nullable Map<String, Long> token_map, boolean enable_local_store, boolean enable_book_source, boolean enable_rss_source, int book_source_limit, int book_limit) {
        Intrinsics.checkNotNullParameter(username, "username");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(salt, "salt");
        Intrinsics.checkNotNullParameter(token, "token");
        return new User(username, password, salt, token, last_login_at, created_at, enable_webdav, token_map, enable_local_store, enable_book_source, enable_rss_source, book_source_limit, book_limit);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ User copy$default(User user, String str, String str2, String str3, String str4, long j, long j2, boolean z, Map map, boolean z2, boolean z3, boolean z4, int i, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = user.username;
        }
        if ((i3 & 2) != 0) {
            str2 = user.password;
        }
        if ((i3 & 4) != 0) {
            str3 = user.salt;
        }
        if ((i3 & 8) != 0) {
            str4 = user.token;
        }
        if ((i3 & 16) != 0) {
            j = user.last_login_at;
        }
        if ((i3 & 32) != 0) {
            j2 = user.created_at;
        }
        if ((i3 & 64) != 0) {
            z = user.enable_webdav;
        }
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            map = user.token_map;
        }
        if ((i3 & 256) != 0) {
            z2 = user.enable_local_store;
        }
        if ((i3 & 512) != 0) {
            z3 = user.enable_book_source;
        }
        if ((i3 & 1024) != 0) {
            z4 = user.enable_rss_source;
        }
        if ((i3 & 2048) != 0) {
            i = user.book_source_limit;
        }
        if ((i3 & 4096) != 0) {
            i2 = user.book_limit;
        }
        return user.copy(str, str2, str3, str4, j, j2, z, map, z2, z3, z4, i, i2);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User(username=").append(this.username).append(", password=").append(this.password).append(", salt=").append(this.salt).append(", token=").append(this.token).append(", last_login_at=").append(this.last_login_at).append(", created_at=").append(this.created_at).append(", enable_webdav=").append(this.enable_webdav).append(", token_map=").append(this.token_map).append(", enable_local_store=").append(this.enable_local_store).append(", enable_book_source=").append(this.enable_book_source).append(", enable_rss_source=").append(this.enable_rss_source).append(", book_source_limit=");
        sb.append(this.book_source_limit).append(", book_limit=").append(this.book_limit).append(')');
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v23, types: [int] */
    /* JADX WARN: Type inference failed for: r1v34, types: [int] */
    /* JADX WARN: Type inference failed for: r1v38, types: [int] */
    /* JADX WARN: Type inference failed for: r1v42, types: [int] */
    /* JADX WARN: Type inference failed for: r1v51 */
    /* JADX WARN: Type inference failed for: r1v52 */
    /* JADX WARN: Type inference failed for: r1v53 */
    /* JADX WARN: Type inference failed for: r1v55 */
    /* JADX WARN: Type inference failed for: r1v56 */
    /* JADX WARN: Type inference failed for: r1v57 */
    /* JADX WARN: Type inference failed for: r1v58 */
    /* JADX WARN: Type inference failed for: r1v59 */
    public int hashCode() {
        int iHashCode = ((((((((((this.username.hashCode() * 31) + this.password.hashCode()) * 31) + this.salt.hashCode()) * 31) + this.token.hashCode()) * 31) + Long.hashCode(this.last_login_at)) * 31) + Long.hashCode(this.created_at)) * 31;
        boolean z = this.enable_webdav;
        ?? r1 = z;
        if (z) {
            r1 = 1;
        }
        int iHashCode2 = (((iHashCode + r1) * 31) + (this.token_map == null ? 0 : this.token_map.hashCode())) * 31;
        boolean z2 = this.enable_local_store;
        ?? r12 = z2;
        if (z2) {
            r12 = 1;
        }
        int i = (iHashCode2 + r12) * 31;
        boolean z3 = this.enable_book_source;
        ?? r13 = z3;
        if (z3) {
            r13 = 1;
        }
        int i2 = (i + r13) * 31;
        boolean z4 = this.enable_rss_source;
        ?? r14 = z4;
        if (z4) {
            r14 = 1;
        }
        return ((((i2 + r14) * 31) + Integer.hashCode(this.book_source_limit)) * 31) + Integer.hashCode(this.book_limit);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        User user = (User) other;
        return Intrinsics.areEqual(this.username, user.username) && Intrinsics.areEqual(this.password, user.password) && Intrinsics.areEqual(this.salt, user.salt) && Intrinsics.areEqual(this.token, user.token) && this.last_login_at == user.last_login_at && this.created_at == user.created_at && this.enable_webdav == user.enable_webdav && Intrinsics.areEqual(this.token_map, user.token_map) && this.enable_local_store == user.enable_local_store && this.enable_book_source == user.enable_book_source && this.enable_rss_source == user.enable_rss_source && this.book_source_limit == user.book_source_limit && this.book_limit == user.book_limit;
    }

    public User() {
        this(null, null, null, null, 0L, 0L, false, null, false, false, false, 0, 0, 8191, null);
    }

    public User(@NotNull String username, @NotNull String password, @NotNull String salt, @NotNull String token, long last_login_at, long created_at, boolean enable_webdav, @Nullable Map<String, Long> token_map, boolean enable_local_store, boolean enable_book_source, boolean enable_rss_source, int book_source_limit, int book_limit) {
        Intrinsics.checkNotNullParameter(username, "username");
        Intrinsics.checkNotNullParameter(password, "password");
        Intrinsics.checkNotNullParameter(salt, "salt");
        Intrinsics.checkNotNullParameter(token, "token");
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

    public /* synthetic */ User(String str, String str2, String str3, String str4, long j, long j2, boolean z, Map map, boolean z2, boolean z3, boolean z4, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this((i3 & 1) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i3 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i3 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i3 & 8) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i3 & 16) != 0 ? System.currentTimeMillis() : j, (i3 & 32) != 0 ? System.currentTimeMillis() : j2, (i3 & 64) != 0 ? false : z, (i3 & Wbxml.EXT_T_0) != 0 ? null : map, (i3 & 256) != 0 ? false : z2, (i3 & 512) != 0 ? true : z3, (i3 & 1024) != 0 ? true : z4, (i3 & 2048) != 0 ? 100 : i, (i3 & 4096) != 0 ? 200 : i2);
    }

    @NotNull
    public final String getUsername() {
        return this.username;
    }

    public final void setUsername(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.username = str;
    }

    @NotNull
    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.password = str;
    }

    @NotNull
    public final String getSalt() {
        return this.salt;
    }

    public final void setSalt(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.salt = str;
    }

    @NotNull
    public final String getToken() {
        return this.token;
    }

    public final void setToken(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.token = str;
    }

    public final long getLast_login_at() {
        return this.last_login_at;
    }

    public final void setLast_login_at(long j) {
        this.last_login_at = j;
    }

    public final long getCreated_at() {
        return this.created_at;
    }

    public final void setCreated_at(long j) {
        this.created_at = j;
    }

    public final boolean getEnable_webdav() {
        return this.enable_webdav;
    }

    public final void setEnable_webdav(boolean z) {
        this.enable_webdav = z;
    }

    @Nullable
    public final Map<String, Long> getToken_map() {
        return this.token_map;
    }

    public final void setToken_map(@Nullable Map<String, Long> map) {
        this.token_map = map;
    }

    public final boolean getEnable_local_store() {
        return this.enable_local_store;
    }

    public final void setEnable_local_store(boolean z) {
        this.enable_local_store = z;
    }

    public final boolean getEnable_book_source() {
        return this.enable_book_source;
    }

    public final void setEnable_book_source(boolean z) {
        this.enable_book_source = z;
    }

    public final boolean getEnable_rss_source() {
        return this.enable_rss_source;
    }

    public final void setEnable_rss_source(boolean z) {
        this.enable_rss_source = z;
    }

    public final int getBook_source_limit() {
        return this.book_source_limit;
    }

    public final void setBook_source_limit(int i) {
        this.book_source_limit = i;
    }

    public final int getBook_limit() {
        return this.book_limit;
    }

    public final void setBook_limit(int i) {
        this.book_limit = i;
    }
}
