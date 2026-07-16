// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import java.util.UUID;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\bW\b\u0086\b\u0018\u00002\u00020\u0001B?\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\t\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003?\u0006\u0002\u0010\u0018J\t\u0010H\u001a\u00020\u0003H\u00c6\u0003J\t\u0010I\u001a\u00020\u0005H\u00c6\u0003J\t\u0010J\u001a\u00020\u0003H\u00c6\u0003J\t\u0010K\u001a\u00020\u0005H\u00c6\u0003J\t\u0010L\u001a\u00020\u0007H\u00c6\u0003J\t\u0010M\u001a\u00020\u0003H\u00c6\u0003J\t\u0010N\u001a\u00020\u0003H\u00c6\u0003J\t\u0010O\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010P\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003?\u0006\u0002\u00106J\t\u0010Q\u001a\u00020\u0003H\u00c6\u0003J\t\u0010R\u001a\u00020\u0005H\u00c6\u0003J\t\u0010S\u001a\u00020\u0007H\u00c6\u0003J\t\u0010T\u001a\u00020\tH\u00c6\u0003J\t\u0010U\u001a\u00020\u0007H\u00c6\u0003J\t\u0010V\u001a\u00020\u0003H\u00c6\u0003J\t\u0010W\u001a\u00020\u0003H\u00c6\u0003J\t\u0010X\u001a\u00020\tH\u00c6\u0003J\u0010\u0010Y\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003?\u0006\u0002\u00106J\u00c6\u0001\u0010Z\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00072\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u0017\u001a\u00020\u0003H\u00c6\u0001?\u0006\u0002\u0010[J\u0013\u0010\\\u001a\u00020\t2\b\u0010]\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010^\u001a\u00020\u0005H\u00d6\u0001J\t\u0010_\u001a\u00020\u0003H\u00d6\u0001R\u001a\u0010\u0014\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u0013\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001a\"\u0004\b\u001e\u0010\u001cR\u001a\u0010\u0011\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u0012\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u001a\"\u0004\b(\u0010\u001cR\u001a\u0010\u0017\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001a\"\u0004\b*\u0010\u001cR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b+\u0010$\"\u0004\b,\u0010&R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001a\"\u0004\b.\u0010\u001cR\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u001a\"\u0004\b0\u0010\u001cR\u001a\u0010\u000f\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b1\u0010 \"\u0004\b2\u0010\"R\u001a\u0010\u0015\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u001a\"\u0004\b4\u0010\u001cR\u001e\u0010\u0016\u001a\u0004\u0018\u00010\u0007X\u0086\u000e?\u0006\u0010\n\u0002\u00109\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=R\u001a\u0010\n\u001a\u00020\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b>\u0010$\"\u0004\b?\u0010&R\u001a\u0010\u0010\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\u001a\"\u0004\bA\u0010\u001cR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bB\u0010 \"\u0004\bC\u0010\"R\u001a\u0010\r\u001a\u00020\tX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\bD\u0010;\"\u0004\bE\u0010=R\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u0007X\u0086\u000e?\u0006\u0010\n\u0002\u00109\u001a\u0004\bF\u00106\"\u0004\bG\u00108¡§\u0006`" }, d2 = { "Lcom/htmake/reader/entity/ActiveLicense;", "", "host", "", "userMaxLimit", "", "expiredAt", "", "openApi", "", "simpleWebExpiredAt", "id", "code", "verified", "verifyTime", "instances", "type", "activeOrder", "activeTime", "activeIp", "activeEmail", "lastOnlineIp", "lastOnlineTime", "errorMsg", "(Ljava/lang/String;IJZJLjava/lang/String;Ljava/lang/String;ZLjava/lang/Long;ILjava/lang/String;IJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;)V", "getActiveEmail", "()Ljava/lang/String;", "setActiveEmail", "(Ljava/lang/String;)V", "getActiveIp", "setActiveIp", "getActiveOrder", "()I", "setActiveOrder", "(I)V", "getActiveTime", "()J", "setActiveTime", "(J)V", "getCode", "setCode", "getErrorMsg", "setErrorMsg", "getExpiredAt", "setExpiredAt", "getHost", "setHost", "getId", "setId", "getInstances", "setInstances", "getLastOnlineIp", "setLastOnlineIp", "getLastOnlineTime", "()Ljava/lang/Long;", "setLastOnlineTime", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getOpenApi", "()Z", "setOpenApi", "(Z)V", "getSimpleWebExpiredAt", "setSimpleWebExpiredAt", "getType", "setType", "getUserMaxLimit", "setUserMaxLimit", "getVerified", "setVerified", "getVerifyTime", "setVerifyTime", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IJZJLjava/lang/String;Ljava/lang/String;ZLjava/lang/Long;ILjava/lang/String;IJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;)Lcom/htmake/reader/entity/ActiveLicense;", "equals", "other", "hashCode", "toString", "reader-pro" })
public final class ActiveLicense
{
    @NotNull
    private String host;
    private int userMaxLimit;
    private long expiredAt;
    private boolean openApi;
    private long simpleWebExpiredAt;
    @NotNull
    private String id;
    @NotNull
    private String code;
    private boolean verified;
    @Nullable
    private Long verifyTime;
    private int instances;
    @NotNull
    private String type;
    private int activeOrder;
    private long activeTime;
    @NotNull
    private String activeIp;
    @NotNull
    private String activeEmail;
    @NotNull
    private String lastOnlineIp;
    @Nullable
    private Long lastOnlineTime;
    @NotNull
    private String errorMsg;
    
    public ActiveLicense(@NotNull final String host, final int userMaxLimit, final long expiredAt, final boolean openApi, final long simpleWebExpiredAt, @NotNull final String id, @NotNull final String code, final boolean verified, @Nullable final Long verifyTime, final int instances, @NotNull final String type, final int activeOrder, final long activeTime, @NotNull final String activeIp, @NotNull final String activeEmail, @NotNull final String lastOnlineIp, @Nullable final Long lastOnlineTime, @NotNull final String errorMsg) {
        Intrinsics.checkNotNullParameter((Object)host, "host");
        Intrinsics.checkNotNullParameter((Object)id, "id");
        Intrinsics.checkNotNullParameter((Object)code, "code");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        Intrinsics.checkNotNullParameter((Object)activeIp, "activeIp");
        Intrinsics.checkNotNullParameter((Object)activeEmail, "activeEmail");
        Intrinsics.checkNotNullParameter((Object)lastOnlineIp, "lastOnlineIp");
        Intrinsics.checkNotNullParameter((Object)errorMsg, "errorMsg");
        this.host = host;
        this.userMaxLimit = userMaxLimit;
        this.expiredAt = expiredAt;
        this.openApi = openApi;
        this.simpleWebExpiredAt = simpleWebExpiredAt;
        this.id = id;
        this.code = code;
        this.verified = verified;
        this.verifyTime = verifyTime;
        this.instances = instances;
        this.type = type;
        this.activeOrder = activeOrder;
        this.activeTime = activeTime;
        this.activeIp = activeIp;
        this.activeEmail = activeEmail;
        this.lastOnlineIp = lastOnlineIp;
        this.lastOnlineTime = lastOnlineTime;
        this.errorMsg = errorMsg;
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
    
    public final int getActiveOrder() {
        return this.activeOrder;
    }
    
    public final void setActiveOrder(final int <set-?>) {
        this.activeOrder = <set-?>;
    }
    
    public final long getActiveTime() {
        return this.activeTime;
    }
    
    public final void setActiveTime(final long <set-?>) {
        this.activeTime = <set-?>;
    }
    
    @NotNull
    public final String getActiveIp() {
        return this.activeIp;
    }
    
    public final void setActiveIp(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.activeIp = <set-?>;
    }
    
    @NotNull
    public final String getActiveEmail() {
        return this.activeEmail;
    }
    
    public final void setActiveEmail(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.activeEmail = <set-?>;
    }
    
    @NotNull
    public final String getLastOnlineIp() {
        return this.lastOnlineIp;
    }
    
    public final void setLastOnlineIp(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.lastOnlineIp = <set-?>;
    }
    
    @Nullable
    public final Long getLastOnlineTime() {
        return this.lastOnlineTime;
    }
    
    public final void setLastOnlineTime(@Nullable final Long <set-?>) {
        this.lastOnlineTime = <set-?>;
    }
    
    @NotNull
    public final String getErrorMsg() {
        return this.errorMsg;
    }
    
    public final void setErrorMsg(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.errorMsg = <set-?>;
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
    
    @NotNull
    public final String component6() {
        return this.id;
    }
    
    @NotNull
    public final String component7() {
        return this.code;
    }
    
    public final boolean component8() {
        return this.verified;
    }
    
    @Nullable
    public final Long component9() {
        return this.verifyTime;
    }
    
    public final int component10() {
        return this.instances;
    }
    
    @NotNull
    public final String component11() {
        return this.type;
    }
    
    public final int component12() {
        return this.activeOrder;
    }
    
    public final long component13() {
        return this.activeTime;
    }
    
    @NotNull
    public final String component14() {
        return this.activeIp;
    }
    
    @NotNull
    public final String component15() {
        return this.activeEmail;
    }
    
    @NotNull
    public final String component16() {
        return this.lastOnlineIp;
    }
    
    @Nullable
    public final Long component17() {
        return this.lastOnlineTime;
    }
    
    @NotNull
    public final String component18() {
        return this.errorMsg;
    }
    
    @NotNull
    public final ActiveLicense copy(@NotNull final String host, final int userMaxLimit, final long expiredAt, final boolean openApi, final long simpleWebExpiredAt, @NotNull final String id, @NotNull final String code, final boolean verified, @Nullable final Long verifyTime, final int instances, @NotNull final String type, final int activeOrder, final long activeTime, @NotNull final String activeIp, @NotNull final String activeEmail, @NotNull final String lastOnlineIp, @Nullable final Long lastOnlineTime, @NotNull final String errorMsg) {
        Intrinsics.checkNotNullParameter((Object)host, "host");
        Intrinsics.checkNotNullParameter((Object)id, "id");
        Intrinsics.checkNotNullParameter((Object)code, "code");
        Intrinsics.checkNotNullParameter((Object)type, "type");
        Intrinsics.checkNotNullParameter((Object)activeIp, "activeIp");
        Intrinsics.checkNotNullParameter((Object)activeEmail, "activeEmail");
        Intrinsics.checkNotNullParameter((Object)lastOnlineIp, "lastOnlineIp");
        Intrinsics.checkNotNullParameter((Object)errorMsg, "errorMsg");
        return new ActiveLicense(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, id, code, verified, verifyTime, instances, type, activeOrder, activeTime, activeIp, activeEmail, lastOnlineIp, lastOnlineTime, errorMsg);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ActiveLicense(host=").append(this.host).append(", userMaxLimit=").append(this.userMaxLimit).append(", expiredAt=").append(this.expiredAt).append(", openApi=").append(this.openApi).append(", simpleWebExpiredAt=").append(this.simpleWebExpiredAt).append(", id=").append(this.id).append(", code=").append(this.code).append(", verified=").append(this.verified).append(", verifyTime=").append(this.verifyTime).append(", instances=").append(this.instances).append(", type=").append(this.type).append(", activeOrder=");
        sb.append(this.activeOrder).append(", activeTime=").append(this.activeTime).append(", activeIp=").append(this.activeIp).append(", activeEmail=").append(this.activeEmail).append(", lastOnlineIp=").append(this.lastOnlineIp).append(", lastOnlineTime=").append(this.lastOnlineTime).append(", errorMsg=").append(this.errorMsg).append(')');
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
        result = result * 31 + this.id.hashCode();
        result = result * 31 + this.code.hashCode();
        final int n2 = result * 31;
        int verified;
        if ((verified = (this.verified ? 1 : 0)) != 0) {
            verified = 1;
        }
        result = n2 + verified;
        result = result * 31 + ((this.verifyTime == null) ? 0 : this.verifyTime.hashCode());
        result = result * 31 + Integer.hashCode(this.instances);
        result = result * 31 + this.type.hashCode();
        result = result * 31 + Integer.hashCode(this.activeOrder);
        result = result * 31 + Long.hashCode(this.activeTime);
        result = result * 31 + this.activeIp.hashCode();
        result = result * 31 + this.activeEmail.hashCode();
        result = result * 31 + this.lastOnlineIp.hashCode();
        result = result * 31 + ((this.lastOnlineTime == null) ? 0 : this.lastOnlineTime.hashCode());
        result = result * 31 + this.errorMsg.hashCode();
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActiveLicense)) {
            return false;
        }
        final ActiveLicense activeLicense = (ActiveLicense)other;
        return Intrinsics.areEqual((Object)this.host, (Object)activeLicense.host) && this.userMaxLimit == activeLicense.userMaxLimit && this.expiredAt == activeLicense.expiredAt && this.openApi == activeLicense.openApi && this.simpleWebExpiredAt == activeLicense.simpleWebExpiredAt && Intrinsics.areEqual((Object)this.id, (Object)activeLicense.id) && Intrinsics.areEqual((Object)this.code, (Object)activeLicense.code) && this.verified == activeLicense.verified && Intrinsics.areEqual((Object)this.verifyTime, (Object)activeLicense.verifyTime) && this.instances == activeLicense.instances && Intrinsics.areEqual((Object)this.type, (Object)activeLicense.type) && this.activeOrder == activeLicense.activeOrder && this.activeTime == activeLicense.activeTime && Intrinsics.areEqual((Object)this.activeIp, (Object)activeLicense.activeIp) && Intrinsics.areEqual((Object)this.activeEmail, (Object)activeLicense.activeEmail) && Intrinsics.areEqual((Object)this.lastOnlineIp, (Object)activeLicense.lastOnlineIp) && Intrinsics.areEqual((Object)this.lastOnlineTime, (Object)activeLicense.lastOnlineTime) && Intrinsics.areEqual((Object)this.errorMsg, (Object)activeLicense.errorMsg);
    }
    
    public ActiveLicense() {
        this(null, 0, 0L, false, 0L, null, null, false, null, 0, null, 0, 0L, null, null, null, null, null, 262143, null);
    }
}
