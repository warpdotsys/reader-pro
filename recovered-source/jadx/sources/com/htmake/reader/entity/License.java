package com.htmake.reader.entity;

import java.util.List;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: License.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/entity/License.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b;\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001Bu\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u000f\u001a\u00020\t\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\u0011J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\tHÆ\u0003J\u0010\u00105\u001a\u0004\u0018\u00010\u0007HÆ\u0003¢\u0006\u0002\u0010/J\t\u00106\u001a\u00020\u0005HÆ\u0003J\t\u00107\u001a\u00020\u0007HÆ\u0003J\t\u00108\u001a\u00020\tHÆ\u0003J\t\u00109\u001a\u00020\u0007HÆ\u0003J\t\u0010:\u001a\u00020\u0005HÆ\u0003J\t\u0010;\u001a\u00020\u0003HÆ\u0003J\t\u0010<\u001a\u00020\u0003HÆ\u0003J\t\u0010=\u001a\u00020\u0003HÆ\u0003J~\u0010>\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\t2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u0007HÆ\u0001¢\u0006\u0002\u0010?J\u0013\u0010@\u001a\u00020\t2\b\u0010A\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010B\u001a\u00020\u0005HÖ\u0001J\u0006\u0010C\u001a\u00020\tJ\u0006\u0010D\u001a\u00020EJ\t\u0010F\u001a\u00020\u0003HÖ\u0001J\u000e\u0010G\u001a\u00020\t2\u0006\u0010H\u001a\u00020\u0003R\u001a\u0010\u000e\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u001a\u0010\u0006\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0013\"\u0004\b\u001b\u0010\u0015R\u001a\u0010\r\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u0013\"\u0004\b\u001d\u0010\u0015R\u001a\u0010\u000b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u001f\"\u0004\b \u0010!R\u001a\u0010\b\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010\n\u001a\u00020\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b&\u0010\u0017\"\u0004\b'\u0010\u0019R\u001a\u0010\f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010\u0013\"\u0004\b)\u0010\u0015R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010\u001f\"\u0004\b+\u0010!R\u001a\u0010\u000f\u001a\u00020\tX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010#\"\u0004\b-\u0010%R\u001e\u0010\u0010\u001a\u0004\u0018\u00010\u0007X\u0086\u000e¢\u0006\u0010\n\u0002\u00102\u001a\u0004\b.\u0010/\"\u0004\b0\u00101¨\u0006I"}, d2 = {"Lcom/htmake/reader/entity/License;", PackageDocumentBase.PREFIX_OPF, "host", PackageDocumentBase.PREFIX_OPF, "userMaxLimit", PackageDocumentBase.PREFIX_OPF, "expiredAt", PackageDocumentBase.PREFIX_OPF, "openApi", PackageDocumentBase.PREFIX_OPF, "simpleWebExpiredAt", "instances", "type", "id", "code", "verified", "verifyTime", "(Ljava/lang/String;IJZJILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/Long;)V", "getCode", "()Ljava/lang/String;", "setCode", "(Ljava/lang/String;)V", "getExpiredAt", "()J", "setExpiredAt", "(J)V", "getHost", "setHost", "getId", "setId", "getInstances", "()I", "setInstances", "(I)V", "getOpenApi", "()Z", "setOpenApi", "(Z)V", "getSimpleWebExpiredAt", "setSimpleWebExpiredAt", "getType", "setType", "getUserMaxLimit", "setUserMaxLimit", "getVerified", "setVerified", "getVerifyTime", "()Ljava/lang/Long;", "setVerifyTime", "(Ljava/lang/Long;)V", "Ljava/lang/Long;", "component1", "component10", "component11", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;IJZJILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/Long;)Lcom/htmake/reader/entity/License;", "equals", "other", "hashCode", "isValid", "toActiveLicense", "Lcom/htmake/reader/entity/ActiveLicense;", "toString", "validHost", "queryHost", "reader-pro"})
public final /* data */ class License {

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
    public final License copy(@NotNull String host, int userMaxLimit, long expiredAt, boolean openApi, long simpleWebExpiredAt, int instances, @NotNull String type, @NotNull String id, @NotNull String code, boolean verified, @Nullable Long verifyTime) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(code, "code");
        return new License(host, userMaxLimit, expiredAt, openApi, simpleWebExpiredAt, instances, type, id, code, verified, verifyTime);
    }

    public static /* synthetic */ License copy$default(License license, String str, int i, long j, boolean z, long j2, int i2, String str2, String str3, String str4, boolean z2, Long l, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = license.host;
        }
        if ((i3 & 2) != 0) {
            i = license.userMaxLimit;
        }
        if ((i3 & 4) != 0) {
            j = license.expiredAt;
        }
        if ((i3 & 8) != 0) {
            z = license.openApi;
        }
        if ((i3 & 16) != 0) {
            j2 = license.simpleWebExpiredAt;
        }
        if ((i3 & 32) != 0) {
            i2 = license.instances;
        }
        if ((i3 & 64) != 0) {
            str2 = license.type;
        }
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            str3 = license.id;
        }
        if ((i3 & 256) != 0) {
            str4 = license.code;
        }
        if ((i3 & 512) != 0) {
            z2 = license.verified;
        }
        if ((i3 & 1024) != 0) {
            l = license.verifyTime;
        }
        return license.copy(str, i, j, z, j2, i2, str2, str3, str4, z2, l);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("License(host=").append(this.host).append(", userMaxLimit=").append(this.userMaxLimit).append(", expiredAt=").append(this.expiredAt).append(", openApi=").append(this.openApi).append(", simpleWebExpiredAt=").append(this.simpleWebExpiredAt).append(", instances=").append(this.instances).append(", type=").append(this.type).append(", id=").append(this.id).append(", code=").append(this.code).append(", verified=").append(this.verified).append(", verifyTime=").append(this.verifyTime).append(')');
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [int] */
    /* JADX WARN: Type inference failed for: r1v35, types: [int] */
    /* JADX WARN: Type inference failed for: r1v44 */
    /* JADX WARN: Type inference failed for: r1v45 */
    /* JADX WARN: Type inference failed for: r1v46 */
    /* JADX WARN: Type inference failed for: r1v47 */
    public int hashCode() {
        int iHashCode = ((((this.host.hashCode() * 31) + Integer.hashCode(this.userMaxLimit)) * 31) + Long.hashCode(this.expiredAt)) * 31;
        boolean z = this.openApi;
        ?? r1 = z;
        if (z) {
            r1 = 1;
        }
        int iHashCode2 = (((((((((((iHashCode + r1) * 31) + Long.hashCode(this.simpleWebExpiredAt)) * 31) + Integer.hashCode(this.instances)) * 31) + this.type.hashCode()) * 31) + this.id.hashCode()) * 31) + this.code.hashCode()) * 31;
        boolean z2 = this.verified;
        ?? r12 = z2;
        if (z2) {
            r12 = 1;
        }
        return ((iHashCode2 + r12) * 31) + (this.verifyTime == null ? 0 : this.verifyTime.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof License)) {
            return false;
        }
        License license = (License) other;
        return Intrinsics.areEqual(this.host, license.host) && this.userMaxLimit == license.userMaxLimit && this.expiredAt == license.expiredAt && this.openApi == license.openApi && this.simpleWebExpiredAt == license.simpleWebExpiredAt && this.instances == license.instances && Intrinsics.areEqual(this.type, license.type) && Intrinsics.areEqual(this.id, license.id) && Intrinsics.areEqual(this.code, license.code) && this.verified == license.verified && Intrinsics.areEqual(this.verifyTime, license.verifyTime);
    }

    public License() {
        this(null, 0, 0L, false, 0L, 0, null, null, null, false, null, 2047, null);
    }

    public License(@NotNull String host, int userMaxLimit, long expiredAt, boolean openApi, long simpleWebExpiredAt, int instances, @NotNull String type, @NotNull String id, @NotNull String code, boolean verified, @Nullable Long verifyTime) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(type, "type");
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(code, "code");
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

    /* JADX WARN: Illegal instructions before constructor call */
    public /* synthetic */ License(String str, int i, long j, boolean z, long j2, int i2, String str2, String str3, String str4, boolean z2, Long l, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        str = (i3 & 1) != 0 ? "*" : str;
        i = (i3 & 2) != 0 ? 15 : i;
        j = (i3 & 4) != 0 ? 0L : j;
        z = (i3 & 8) != 0 ? false : z;
        j2 = (i3 & 16) != 0 ? 1688140799000L : j2;
        i2 = (i3 & 32) != 0 ? 1 : i2;
        str2 = (i3 & 64) != 0 ? "default" : str2;
        if ((i3 & Wbxml.EXT_T_0) != 0) {
            String string = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string, "randomUUID().toString()");
            str3 = string;
        }
        if ((i3 & 256) != 0) {
            String string2 = UUID.randomUUID().toString();
            Intrinsics.checkNotNullExpressionValue(string2, "randomUUID().toString()");
            str4 = string2;
        }
        this(str, i, j, z, j2, i2, str2, str3, str4, (i3 & 512) != 0 ? false : z2, (i3 & 1024) != 0 ? null : l);
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

    public final boolean isValid() {
        if (this.expiredAt != 0 && this.expiredAt < System.currentTimeMillis()) {
            return false;
        }
        return true;
    }

    public final boolean validHost(@NotNull String queryHost) {
        Intrinsics.checkNotNullParameter(queryHost, "queryHost");
        if (!isValid()) {
            return false;
        }
        if (queryHost.length() == 0) {
            return false;
        }
        if ("*".equals(this.host)) {
            return true;
        }
        List hostParts = StringsKt.split$default(queryHost, new String[]{":"}, false, 0, 6, (Object) null);
        List queryParts = StringsKt.split$default((CharSequence) hostParts.get(0), new String[]{"."}, false, 0, 6, (Object) null);
        List<String> hostList = StringsKt.split$default(this.host, new String[]{","}, false, 0, 6, (Object) null);
        for (String hostname : hostList) {
            List parts = StringsKt.split$default(hostname, new String[]{"."}, false, 0, 6, (Object) null);
            if (parts.size() == queryParts.size()) {
                boolean isValid = true;
                int i = 0;
                int size = parts.size();
                if (0 < size) {
                    do {
                        int i2 = i;
                        i++;
                        if (!"*".equals(parts.get(i2)) && !((String) parts.get(i2)).equals(queryParts.get(i2))) {
                            isValid = false;
                        }
                    } while (i < size);
                }
                if (isValid) {
                    return true;
                }
            }
        }
        return false;
    }

    @NotNull
    public final ActiveLicense toActiveLicense() {
        return new ActiveLicense(this.host, this.userMaxLimit, this.expiredAt, this.openApi, this.simpleWebExpiredAt, this.id, this.code, this.verified, this.verifyTime, this.instances, this.type, 0, 0L, null, null, null, null, null, 260096, null);
    }
}
