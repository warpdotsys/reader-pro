package com.htmake.reader.entity;

import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import me.ag2s.epublib.util.IOUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: ActiveLicense.kt */
/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/entity/ActiveLicense.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\bW\b\u0086\b\u0018\u00002\u00020\u0001B½\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\t\u0012\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0013\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0014\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\u0017\u001a\u00020\u0003¢\u0006\u0002\u0010\u0018J\t\u0010H\u001a\u00020\u0003HÆ\u0003J\t\u0010I\u001a\u00020\u0005HÆ\u0003J\t\u0010J\u001a\u00020\u0003HÆ\u0003J\t\u0010K\u001a\u00020\u0005HÆ\u0003J\t\u0010L\u001a\u00020\u0007HÆ\u0003J\t\u0010M\u001a\u00020\u0003HÆ\u0003J\t\u0010N\u001a\u00020\u0003HÆ\u0003J\t\u0010O\u001a\u00020\u0003HÆ\u0003J\u0010\u0010P\u001a\u0004\u0018\u00010\u0007HÆ\u0003¢\u0006\u0002\u00106J\t\u0010Q\u001a\u00020\u0003HÆ\u0003J\t\u0010R\u001a\u00020\u0005HÆ\u0003J\t\u0010S\u001a\u00020\u0007HÆ\u0003J\t\u0010T\u001a\u00020\tHÆ\u0003J\t\u0010U\u001a\u00020\u0007HÆ\u0003J\t\u0010V\u001a\u00020\u0003HÆ\u0003J\t\u0010W\u001a\u00020\u0003HÆ\u0003J\t\u0010X\u001a\u00020\tHÆ\u0003J\u0010\u0010Y\u001a\u0004\u0018\u00010\u0007HÆ\u0003¢\u0006\u0002\u00106JÆ\u0001\u0010Z\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\t2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000f\u001a\u00020\u00052\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00072\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u00032\b\b\u0002\u0010\u0015\u001a\u00020\u00032\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u0017\u001a\u00020\u0003HÆ\u0001¢\u0006\u0002\u0010[J\u0013\u0010\\\u001a\u00020\t2\b\u0010]\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010^\u001a\u00020\u0005HÖ\u0001J\t\u0010_\u001a\u00020\u0003HÖ\u0001R\u001a\u0010\u0014\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001a\u0010\u0013\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001a\"\u0004\b\u001e\u0010\u001cR\u001a\u0010\u0011\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010 \"\u0004\b!\u0010\"R\u001a\u0010\u0012\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u001a\"\u0004\b(\u0010\u001cR\u001a\u0010\u0017\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u001a\"\u0004\b*\u0010\u001cR\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010$\"\u0004\b,\u0010&R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001a\"\u0004\b.\u0010\u001cR\u001a\u0010\u000b\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u001a\"\u0004\b0\u0010\u001cR\u001a\u0010\u000f\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b1\u0010 \"\u0004\b2\u0010\"R\u001a\u0010\u0015\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u001a\"\u0004\b4\u0010\u001cR\u001e\u0010\u0016\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u0010\n\u0002\u00109\u001a\u0004\b5\u00106\"\u0004\b7\u00108R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b:\u0010;\"\u0004\b<\u0010=R\u001a\u0010\n\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010$\"\u0004\b?\u0010&R\u001a\u0010\u0010\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\u001a\"\u0004\bA\u0010\u001cR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010 \"\u0004\bC\u0010\"R\u001a\u0010\r\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bD\u0010;\"\u0004\bE\u0010=R\u001e\u0010\u000e\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u0010\n\u0002\u00109\u001a\u0004\bF\u00106\"\u0004\bG\u00108¨\u0006`"}, d2 = {"Lcom/htmake/reader/entity/ActiveLicense;", PackageDocumentBase.PREFIX_OPF, "host", PackageDocumentBase.PREFIX_OPF, "userMaxLimit", PackageDocumentBase.PREFIX_OPF, "expiredAt", PackageDocumentBase.PREFIX_OPF, "openApi", PackageDocumentBase.PREFIX_OPF, "simpleWebExpiredAt", "id", "code", "verified", "verifyTime", "instances", "type", "activeOrder", "activeTime", "activeIp", "activeEmail", "lastOnlineIp", "lastOnlineTime", "errorMsg", "(Ljava/lang/String;IJZJLjava/lang/String;Ljava/lang/String;ZLjava/lang/Long;ILjava/lang/String;IJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;)V", "getActiveEmail", "()Ljava/lang/String;", "setActiveEmail", "(Ljava/lang/String;)V", "getActiveIp", "setActiveIp", "getActiveOrder", "()I", "setActiveOrder", "(I)V", "getActiveTime", "()J", "setActiveTime", "(J)V", "getCode", "setCode", "getErrorMsg", "setErrorMsg", "getExpiredAt", "setExpiredAt", "getHost", "setHost", "getId", "setId", "getInstances", "setInstances", "getLastOnlineIp", "setLastOnlineIp", "getLastOnlineTime", "()Ljava/lang/Long;", "setLastOnlineTime", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "getOpenApi", "()Z", "setOpenApi", "(Z)V", "getSimpleWebExpiredAt", "setSimpleWebExpiredAt", "getType", "setType", "getUserMaxLimit", "setUserMaxLimit", "getVerified", "setVerified", "getVerifyTime", "setVerifyTime", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IJZJLjava/lang/String;Ljava/lang/String;ZLjava/lang/Long;ILjava/lang/String;IJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Ljava/lang/String;)Lcom/htmake/reader/entity/ActiveLicense;", "equals", "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class ActiveLicense {

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
    public final ActiveLicense copy(@NotNull String host, int userMaxLimit, long expiredAt, boolean openApi, long simpleWebExpiredAt, @NotNull String id, @NotNull String code, boolean verified, @Nullable Long verifyTime, int instances, @NotNull String type, int activeOrder, long activeTime, @NotNull String activeIp, @NotNull String activeEmail, @NotNull String lastOnlineIp, @Nullable Long lastOnlineTime, @NotNull String errorMsg) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(code, "code");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(activeIp, "activeIp");
        Intrinsics.checkNotNullParameter(activeEmail, "activeEmail");
        Intrinsics.checkNotNullParameter(lastOnlineIp, "lastOnlineIp");
        Intrinsics.checkNotNullParameter(errorMsg, "errorMsg");
        return new ActiveLicense(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, id, code, verified, verifyTime, instances, type, activeOrder, activeTime, activeIp, activeEmail, lastOnlineIp, lastOnlineTime, errorMsg);
    }

    public static /* synthetic */ ActiveLicense copy$default(ActiveLicense activeLicense, String str, int i, long j, boolean z, long j2, String str2, String str3, boolean z2, Long l, int i2, String str4, int i3, long j3, String str5, String str6, String str7, Long l2, String str8, int i4, Object obj) {
        if ((i4 & 1) != 0) {
            str = activeLicense.host;
        }
        if ((i4 & 2) != 0) {
            i = activeLicense.userMaxLimit;
        }
        if ((i4 & 4) != 0) {
            j = activeLicense.expiredAt;
        }
        if ((i4 & 8) != 0) {
            z = activeLicense.openApi;
        }
        if ((i4 & 16) != 0) {
            j2 = activeLicense.simpleWebExpiredAt;
        }
        if ((i4 & 32) != 0) {
            str2 = activeLicense.id;
        }
        if ((i4 & 64) != 0) {
            str3 = activeLicense.code;
        }
        if ((i4 & Wbxml.EXT_T_0) != 0) {
            z2 = activeLicense.verified;
        }
        if ((i4 & 256) != 0) {
            l = activeLicense.verifyTime;
        }
        if ((i4 & 512) != 0) {
            i2 = activeLicense.instances;
        }
        if ((i4 & 1024) != 0) {
            str4 = activeLicense.type;
        }
        if ((i4 & 2048) != 0) {
            i3 = activeLicense.activeOrder;
        }
        if ((i4 & 4096) != 0) {
            j3 = activeLicense.activeTime;
        }
        if ((i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0) {
            str5 = activeLicense.activeIp;
        }
        if ((i4 & 16384) != 0) {
            str6 = activeLicense.activeEmail;
        }
        if ((i4 & 32768) != 0) {
            str7 = activeLicense.lastOnlineIp;
        }
        if ((i4 & 65536) != 0) {
            l2 = activeLicense.lastOnlineTime;
        }
        if ((i4 & 131072) != 0) {
            str8 = activeLicense.errorMsg;
        }
        return activeLicense.copy(str, i, j, z, j2, str2, str3, z2, l, i2, str4, i3, j3, str5, str6, str7, l2, str8);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ActiveLicense(host=").append(this.host).append(", userMaxLimit=").append(this.userMaxLimit).append(", expiredAt=").append(this.expiredAt).append(", openApi=").append(this.openApi).append(", simpleWebExpiredAt=").append(this.simpleWebExpiredAt).append(", id=").append(this.id).append(", code=").append(this.code).append(", verified=").append(this.verified).append(", verifyTime=").append(this.verifyTime).append(", instances=").append(this.instances).append(", type=").append(this.type).append(", activeOrder=");
        sb.append(this.activeOrder).append(", activeTime=").append(this.activeTime).append(", activeIp=").append(this.activeIp).append(", activeEmail=").append(this.activeEmail).append(", lastOnlineIp=").append(this.lastOnlineIp).append(", lastOnlineTime=").append(this.lastOnlineTime).append(", errorMsg=").append(this.errorMsg).append(')');
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [int] */
    /* JADX WARN: Type inference failed for: r1v27, types: [int] */
    /* JADX WARN: Type inference failed for: r1v76 */
    /* JADX WARN: Type inference failed for: r1v77 */
    /* JADX WARN: Type inference failed for: r1v78 */
    /* JADX WARN: Type inference failed for: r1v79 */
    public int hashCode() {
        int iHashCode = ((((this.host.hashCode() * 31) + Integer.hashCode(this.userMaxLimit)) * 31) + Long.hashCode(this.expiredAt)) * 31;
        boolean z = this.openApi;
        ?? r1 = z;
        if (z) {
            r1 = 1;
        }
        int iHashCode2 = (((((((iHashCode + r1) * 31) + Long.hashCode(this.simpleWebExpiredAt)) * 31) + this.id.hashCode()) * 31) + this.code.hashCode()) * 31;
        boolean z2 = this.verified;
        ?? r12 = z2;
        if (z2) {
            r12 = 1;
        }
        return ((((((((((((((((((((iHashCode2 + r12) * 31) + (this.verifyTime == null ? 0 : this.verifyTime.hashCode())) * 31) + Integer.hashCode(this.instances)) * 31) + this.type.hashCode()) * 31) + Integer.hashCode(this.activeOrder)) * 31) + Long.hashCode(this.activeTime)) * 31) + this.activeIp.hashCode()) * 31) + this.activeEmail.hashCode()) * 31) + this.lastOnlineIp.hashCode()) * 31) + (this.lastOnlineTime == null ? 0 : this.lastOnlineTime.hashCode())) * 31) + this.errorMsg.hashCode();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ActiveLicense)) {
            return false;
        }
        ActiveLicense activeLicense = (ActiveLicense) other;
        return Intrinsics.areEqual(this.host, activeLicense.host) && this.userMaxLimit == activeLicense.userMaxLimit && this.expiredAt == activeLicense.expiredAt && this.openApi == activeLicense.openApi && this.simpleWebExpiredAt == activeLicense.simpleWebExpiredAt && Intrinsics.areEqual(this.id, activeLicense.id) && Intrinsics.areEqual(this.code, activeLicense.code) && this.verified == activeLicense.verified && Intrinsics.areEqual(this.verifyTime, activeLicense.verifyTime) && this.instances == activeLicense.instances && Intrinsics.areEqual(this.type, activeLicense.type) && this.activeOrder == activeLicense.activeOrder && this.activeTime == activeLicense.activeTime && Intrinsics.areEqual(this.activeIp, activeLicense.activeIp) && Intrinsics.areEqual(this.activeEmail, activeLicense.activeEmail) && Intrinsics.areEqual(this.lastOnlineIp, activeLicense.lastOnlineIp) && Intrinsics.areEqual(this.lastOnlineTime, activeLicense.lastOnlineTime) && Intrinsics.areEqual(this.errorMsg, activeLicense.errorMsg);
    }

    public ActiveLicense() {
        this(null, 0, 0L, false, 0L, null, null, false, null, 0, null, 0, 0L, null, null, null, null, null, 262143, null);
    }

    public ActiveLicense(@NotNull String host, int userMaxLimit, long expiredAt, boolean openApi, long simpleWebExpiredAt, @NotNull String id, @NotNull String code, boolean verified, @Nullable Long verifyTime, int instances, @NotNull String type, int activeOrder, long activeTime, @NotNull String activeIp, @NotNull String activeEmail, @NotNull String lastOnlineIp, @Nullable Long lastOnlineTime, @NotNull String errorMsg) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(code, "code");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(activeIp, "activeIp");
        Intrinsics.checkNotNullParameter(activeEmail, "activeEmail");
        Intrinsics.checkNotNullParameter(lastOnlineIp, "lastOnlineIp");
        Intrinsics.checkNotNullParameter(errorMsg, "errorMsg");
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

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ ActiveLicense(String str, int i, long j, boolean z, long j2, String str2, String str3, boolean z2, Long l, int i2, String str4, int i3, long j3, String str5, String str6, String str7, Long l2, String str8, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i4 & 1) != 0 ? "*" : str;
        i = (i4 & 2) != 0 ? 15 : i;
        j = (i4 & 4) != 0 ? 0L : j;
        z = (i4 & 8) != 0 ? false : z;
        j2 = (i4 & 16) != 0 ? 1682870399000L : j2;
        if ((i4 & 32) != 0) {
            String string = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string, "randomUUID().toString()");
            str2 = string;
        }
        if ((i4 & 64) != 0) {
            String string2 = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string2, "randomUUID().toString()");
            str3 = string2;
        }
        this(str, i, j, z, j2, str2, str3, (i4 & Wbxml.EXT_T_0) != 0 ? false : z2, (i4 & 256) != 0 ? null : l, (i4 & 512) != 0 ? 1 : i2, (i4 & 1024) != 0 ? "default" : str4, (i4 & 2048) != 0 ? 1 : i3, (i4 & 4096) != 0 ? System.currentTimeMillis() : j3, (i4 & IOUtil.DEFAULT_BUFFER_SIZE) != 0 ? PackageDocumentBase.PREFIX_OPF : str5, (i4 & 16384) != 0 ? PackageDocumentBase.PREFIX_OPF : str6, (i4 & 32768) != 0 ? PackageDocumentBase.PREFIX_OPF : str7, (i4 & 65536) != 0 ? null : l2, (i4 & 131072) != 0 ? PackageDocumentBase.PREFIX_OPF : str8);
    }

    @NotNull
    public final String getHost() {
        return this.host;
    }

    public final void setHost(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.host = str;
    }

    public final int getUserMaxLimit() {
        return this.userMaxLimit;
    }

    public final void setUserMaxLimit(int i) {
        this.userMaxLimit = i;
    }

    public final long getExpiredAt() {
        return this.expiredAt;
    }

    public final void setExpiredAt(long j) {
        this.expiredAt = j;
    }

    public final boolean getOpenApi() {
        return this.openApi;
    }

    public final void setOpenApi(boolean z) {
        this.openApi = z;
    }

    public final long getSimpleWebExpiredAt() {
        return this.simpleWebExpiredAt;
    }

    public final void setSimpleWebExpiredAt(long j) {
        this.simpleWebExpiredAt = j;
    }

    @NotNull
    public final String getId() {
        return this.id;
    }

    public final void setId(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.id = str;
    }

    @NotNull
    public final String getCode() {
        return this.code;
    }

    public final void setCode(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.code = str;
    }

    public final boolean getVerified() {
        return this.verified;
    }

    public final void setVerified(boolean z) {
        this.verified = z;
    }

    @Nullable
    public final Long getVerifyTime() {
        return this.verifyTime;
    }

    public final void setVerifyTime(@Nullable Long l) {
        this.verifyTime = l;
    }

    public final int getInstances() {
        return this.instances;
    }

    public final void setInstances(int i) {
        this.instances = i;
    }

    @NotNull
    public final String getType() {
        return this.type;
    }

    public final void setType(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.type = str;
    }

    public final int getActiveOrder() {
        return this.activeOrder;
    }

    public final void setActiveOrder(int i) {
        this.activeOrder = i;
    }

    public final long getActiveTime() {
        return this.activeTime;
    }

    public final void setActiveTime(long j) {
        this.activeTime = j;
    }

    @NotNull
    public final String getActiveIp() {
        return this.activeIp;
    }

    public final void setActiveIp(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.activeIp = str;
    }

    @NotNull
    public final String getActiveEmail() {
        return this.activeEmail;
    }

    public final void setActiveEmail(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.activeEmail = str;
    }

    @NotNull
    public final String getLastOnlineIp() {
        return this.lastOnlineIp;
    }

    public final void setLastOnlineIp(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.lastOnlineIp = str;
    }

    @Nullable
    public final Long getLastOnlineTime() {
        return this.lastOnlineTime;
    }

    public final void setLastOnlineTime(@Nullable Long l) {
        this.lastOnlineTime = l;
    }

    @NotNull
    public final String getErrorMsg() {
        return this.errorMsg;
    }

    public final void setErrorMsg(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.errorMsg = str;
    }
}
