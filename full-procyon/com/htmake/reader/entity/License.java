// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import java.util.Iterator;
import java.util.List;
import kotlin.text.StringsKt;
import java.util.UUID;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b;\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001Bu\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\t\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0007?\u0006\u0002\u0010\u0011J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\tH\u00c6\u0003J\u0010\u00105\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003?\u0006\u0002\u0010/J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0007H\u00c6\u0003J\t\u00108\u001a\u00020\tH\u00c6\u0003J\t\u00109\u001a\u00020\u0007H\u00c6\u0003J\t\u0010:\u001a\u00020\u0005H\u00c6\u0003J\t\u0010;\u001a\u00020\u0003H\u00c6\u0003J\t\u0010<\u001a\u00020\u0003H\u00c6\u0003J\t\u0010=\u001a\u00020\u0003H\u00c6\u0003J~\u0010>\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\t2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0007H\u00c6\u0001?\u0006\u0002\u0010?J\u0013\u0010@\u001a\u00020\t2\b\u0010A\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010B\u001a\u00020\u0005H\u00d6\u0001J\u0006\u0010C\u001a\u00020\tJ\u0006\u0010D\u001a\u00020EJ\t\u0010F\u001a\u00020\u0003H\u00d6\u0001J\u000e\u0010G\u001a\u00020\t2\u0006\u0010H\u001a\u00020\u0003R\u001a\u0010\u000e\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R\u001a\u0010\r\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0013\"\u0004\b\u001d\u0010\u0015R\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010\n\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0017\"\u0004\b'\u0010\u0019R\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0013\"\u0004\b)\u0010\u0015R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u001f\"\u0004\b+\u0010!R\u001a\u0010\u000f\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b,\u0010#\"\u0004\b-\u0010%R\u001e\u0010\u0010\u001a\u0004\u0018\u00010\u0007X\u0086\u000e?\u0006\u0010\n\u0002\u00102\u001a\u0004\b.\u0010/\"\u0004\b0\u00101¡§\u0006I" }, d2 = { "Lcom/htmake/reader/entity/License;", "", "host", "", "userMaxLimit", "", "expiredAt", "", "openApi", "", "simpleWebExpiredAt", "instances", "type", "id", "code", "verified", "verifyTime", "(Ljava/lang/String;IJZJILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/Long;)V", "getCode", "()Ljava/lang/String;", "setCode", "(Ljava/lang/String;)V", "getExpiredAt", "()J", "setExpiredAt", "(J)V", "getHost", "setHost", "getId", "setId", "getInstances", "()I", "setInstances", "(I)V", "getOpenApi", "()Z", "setOpenApi", "(Z)V", "getSimpleWebExpiredAt", "setSimpleWebExpiredAt", "getType", "setType", "getUserMaxLimit", "setUserMaxLimit", "getVerified", "setVerified", "getVerifyTime", "()Ljava/lang/Long;", "setVerifyTime", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IJZJILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/Long;)Lcom/htmake/reader/entity/License;", "equals", "other", "hashCode", "isValid", "toActiveLicense", "Lcom/htmake/reader/entity/ActiveLicense;", "toString", "validHost", "queryHost", "reader-pro" })
public final class License
{
    @NotNull
    private String host;
    private int userMaxLimit;
    private long expiredAt;
    private boolean openApi;
    private long simpleWebExpiredAt;
    private int instances;
    @NotNull
    private String type;
    @NotNull
    private String id;
    @NotNull
    private String code;
    private boolean verified;
    @Nullable
    private Long verifyTime;
    
    public License(@NotNull final String host, final int userMaxLimit, final long expiredAt, final boolean openApi, final long simpleWebExpiredAt, final int instances, @NotNull final String type, @NotNull final String id, @NotNull final String code, final boolean verified, @Nullable final Long verifyTime) {
        Intrinsics.checkNotNullParameter((Object)host, "host");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        Intrinsics.checkNotNullParameter((Object)id, "id");
        Intrinsics.checkNotNullParameter((Object)code, "code");
        this.host = host;
        this.userMaxLimit = userMaxLimit;
        this.expiredAt = expiredAt;
        this.openApi = openApi;
        this.simpleWebExpiredAt = simpleWebExpiredAt;
        this.instances = instances;
        this.type = type;
        this.id = id;
        this.code = code;
        this.verified = verified;
        this.verifyTime = verifyTime;
    }
    
    @NotNull
    public final String getHost() {
        return this.host;
    }
    
    public final void setHost(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.host = <set-?>;
    }
    
    public final int getUserMaxLimit() {
        return this.userMaxLimit;
    }
    
    public final void setUserMaxLimit(final int <set-?>) {
        this.userMaxLimit = <set-?>;
    }
    
    public final long getExpiredAt() {
        return this.expiredAt;
    }
    
    public final void setExpiredAt(final long <set-?>) {
        this.expiredAt = <set-?>;
    }
    
    public final boolean getOpenApi() {
        return this.openApi;
    }
    
    public final void setOpenApi(final boolean <set-?>) {
        this.openApi = <set-?>;
    }
    
    public final long getSimpleWebExpiredAt() {
        return this.simpleWebExpiredAt;
    }
    
    public final void setSimpleWebExpiredAt(final long <set-?>) {
        this.simpleWebExpiredAt = <set-?>;
    }
    
    public final int getInstances() {
        return this.instances;
    }
    
    public final void setInstances(final int <set-?>) {
        this.instances = <set-?>;
    }
    
    @NotNull
    public final String getType() {
        return this.type;
    }
    
    public final void setType(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.type = <set-?>;
    }
    
    @NotNull
    public final String getId() {
        return this.id;
    }
    
    public final void setId(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.id = <set-?>;
    }
    
    @NotNull
    public final String getCode() {
        return this.code;
    }
    
    public final void setCode(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.code = <set-?>;
    }
    
    public final boolean getVerified() {
        return this.verified;
    }
    
    public final void setVerified(final boolean <set-?>) {
        this.verified = <set-?>;
    }
    
    @Nullable
    public final Long getVerifyTime() {
        return this.verifyTime;
    }
    
    public final void setVerifyTime(@Nullable final Long <set-?>) {
        this.verifyTime = <set-?>;
    }
    
    public final boolean isValid() {
        return this.expiredAt == 0L || this.expiredAt >= System.currentTimeMillis();
    }
    
    public final boolean validHost(@NotNull final String queryHost) {
        Intrinsics.checkNotNullParameter((Object)queryHost, "queryHost");
        if (!this.isValid()) {
            return false;
        }
        if (queryHost.length() == 0) {
            return false;
        }
        if ("*".equals(this.host)) {
            return true;
        }
        final List hostParts = StringsKt.split$default((CharSequence)queryHost, new String[] { ":" }, false, 0, 6, (Object)null);
        final List queryParts = StringsKt.split$default((CharSequence)hostParts.get(0), new String[] { "." }, false, 0, 6, (Object)null);
        final List hostList = StringsKt.split$default((CharSequence)this.host, new String[] { "," }, false, 0, 6, (Object)null);
        for (final String hostname : hostList) {
            final List parts = StringsKt.split$default((CharSequence)hostname, new String[] { "." }, false, 0, 6, (Object)null);
            if (parts.size() != queryParts.size()) {
                continue;
            }
            boolean isValid = true;
            int j = 0;
            final int size = parts.size();
            if (j < size) {
                do {
                    final int i = j;
                    ++j;
                    if (!"*".equals(parts.get(i)) && !parts.get(i).equals(queryParts.get(i))) {
                        isValid = false;
                    }
                } while (j < size);
            }
            if (isValid) {
                return true;
            }
        }
        return false;
    }
    
    @NotNull
    public final ActiveLicense toActiveLicense() {
        return new ActiveLicense(this.host, this.userMaxLimit, this.expiredAt, this.openApi, this.simpleWebExpiredAt, this.id, this.code, this.verified, this.verifyTime, this.instances, this.type, 0, 0L, null, null, null, null, null, 260096, null);
    }
    
    @NotNull
    public final String component1() {
        return this.host;
    }
    
    public final int component2() {
        return this.userMaxLimit;
    }
    
    public final long component3() {
        return this.expiredAt;
    }
    
    public final boolean component4() {
        return this.openApi;
    }
    
    public final long component5() {
        return this.simpleWebExpiredAt;
    }
    
    public final int component6() {
        return this.instances;
    }
    
    @NotNull
    public final String component7() {
        return this.type;
    }
    
    @NotNull
    public final String component8() {
        return this.id;
    }
    
    @NotNull
    public final String component9() {
        return this.code;
    }
    
    public final boolean component10() {
        return this.verified;
    }
    
    @Nullable
    public final Long component11() {
        return this.verifyTime;
    }
    
    @NotNull
    public final License copy(@NotNull final String host, final int userMaxLimit, final long expiredAt, final boolean openApi, final long simpleWebExpiredAt, final int instances, @NotNull final String type, @NotNull final String id, @NotNull final String code, final boolean verified, @Nullable final Long verifyTime) {
        Intrinsics.checkNotNullParameter((Object)host, "host");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        Intrinsics.checkNotNullParameter((Object)id, "id");
        Intrinsics.checkNotNullParameter((Object)code, "code");
        return new License(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, instances, type, id, code, verified, verifyTime);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("License(host=").append(this.host).append(", userMaxLimit=").append(this.userMaxLimit).append(", expiredAt=").append(this.expiredAt).append(", openApi=").append(this.openApi).append(", simpleWebExpiredAt=").append(this.simpleWebExpiredAt).append(", instances=").append(this.instances).append(", type=").append(this.type).append(", id=").append(this.id).append(", code=").append(this.code).append(", verified=").append(this.verified).append(", verifyTime=").append(this.verifyTime).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = this.host.hashCode();
        result = result * 31 + Integer.hashCode(this.userMaxLimit);
        result = result * 31 + Long.hashCode(this.expiredAt);
        final int n = result * 31;
        int openApi;
        if ((openApi = (this.openApi ? 1 : 0)) != 0) {
            openApi = 1;
        }
        result = n + openApi;
        result = result * 31 + Long.hashCode(this.simpleWebExpiredAt);
        result = result * 31 + Integer.hashCode(this.instances);
        result = result * 31 + this.type.hashCode();
        result = result * 31 + this.id.hashCode();
        result = result * 31 + this.code.hashCode();
        final int n2 = result * 31;
        int verified;
        if ((verified = (this.verified ? 1 : 0)) != 0) {
            verified = 1;
        }
        result = n2 + verified;
        result = result * 31 + ((this.verifyTime == null) ? 0 : this.verifyTime.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof License)) {
            return false;
        }
        final License license = (License)other;
        return Intrinsics.areEqual((Object)this.host, (Object)license.host) && this.userMaxLimit == license.userMaxLimit && this.expiredAt == license.expiredAt && this.openApi == license.openApi && this.simpleWebExpiredAt == license.simpleWebExpiredAt && this.instances == license.instances && Intrinsics.areEqual((Object)this.type, (Object)license.type) && Intrinsics.areEqual((Object)this.id, (Object)license.id) && Intrinsics.areEqual((Object)this.code, (Object)license.code) && this.verified == license.verified && Intrinsics.areEqual((Object)this.verifyTime, (Object)license.verifyTime);
    }
    
    public License() {
        this(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
    }
}
