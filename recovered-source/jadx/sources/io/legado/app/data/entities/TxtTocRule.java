package io.legado.app.data.entities;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: TxtTocRule.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/TxtTocRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\b\u0086\b\u0018\u00002\u00020\u0001B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\t\u0010 \u001a\u00020\u0005HÆ\u0003J\t\u0010!\u001a\u00020\bHÆ\u0003J\t\u0010\"\u001a\u00020\nHÆ\u0003J;\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001J\u0013\u0010$\u001a\u00020\n2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\bHÖ\u0001J\t\u0010'\u001a\u00020\u0005HÖ\u0001R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0015\"\u0004\b\u0019\u0010\u0017R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¨\u0006("}, d2 = {"Lio/legado/app/data/entities/TxtTocRule;", PackageDocumentBase.PREFIX_OPF, "id", PackageDocumentBase.PREFIX_OPF, "name", PackageDocumentBase.PREFIX_OPF, "rule", "serialNumber", PackageDocumentBase.PREFIX_OPF, "enable", PackageDocumentBase.PREFIX_OPF, "(JLjava/lang/String;Ljava/lang/String;IZ)V", "getEnable", "()Z", "setEnable", "(Z)V", "getId", "()J", "setId", "(J)V", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "getRule", "setRule", "getSerialNumber", "()I", "setSerialNumber", "(I)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class TxtTocRule {
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String rule;
    private int serialNumber;
    private boolean enable;

    public final long component1() {
        return this.id;
    }

    @NotNull
    public final String component2() {
        return this.name;
    }

    @NotNull
    public final String component3() {
        return this.rule;
    }

    public final int component4() {
        return this.serialNumber;
    }

    public final boolean component5() {
        return this.enable;
    }

    @NotNull
    public final TxtTocRule copy(long id, @NotNull String name, @NotNull String rule, int serialNumber, boolean enable) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(rule, "rule");
        return new TxtTocRule(id, name, rule, serialNumber, enable);
    }

    public static /* synthetic */ TxtTocRule copy$default(TxtTocRule txtTocRule, long j, String str, String str2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            j = txtTocRule.id;
        }
        if ((i2 & 2) != 0) {
            str = txtTocRule.name;
        }
        if ((i2 & 4) != 0) {
            str2 = txtTocRule.rule;
        }
        if ((i2 & 8) != 0) {
            i = txtTocRule.serialNumber;
        }
        if ((i2 & 16) != 0) {
            z = txtTocRule.enable;
        }
        return txtTocRule.copy(j, str, str2, i, z);
    }

    @NotNull
    public String toString() {
        return "TxtTocRule(id=" + this.id + ", name=" + this.name + ", rule=" + this.rule + ", serialNumber=" + this.serialNumber + ", enable=" + this.enable + ')';
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v15, types: [int] */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17 */
    public int hashCode() {
        int iHashCode = ((((((Long.hashCode(this.id) * 31) + this.name.hashCode()) * 31) + this.rule.hashCode()) * 31) + Integer.hashCode(this.serialNumber)) * 31;
        boolean z = this.enable;
        ?? r1 = z;
        if (z) {
            r1 = 1;
        }
        return iHashCode + r1;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TxtTocRule)) {
            return false;
        }
        TxtTocRule txtTocRule = (TxtTocRule) other;
        return this.id == txtTocRule.id && Intrinsics.areEqual(this.name, txtTocRule.name) && Intrinsics.areEqual(this.rule, txtTocRule.rule) && this.serialNumber == txtTocRule.serialNumber && this.enable == txtTocRule.enable;
    }

    public TxtTocRule() {
        this(0L, null, null, 0, false, 31, null);
    }

    public TxtTocRule(long id, @NotNull String name, @NotNull String rule, int serialNumber, boolean enable) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(rule, "rule");
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.serialNumber = serialNumber;
        this.enable = enable;
    }

    public /* synthetic */ TxtTocRule(long j, String str, String str2, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? System.currentTimeMillis() : j, (i2 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i2 & 4) != 0 ? PackageDocumentBase.PREFIX_OPF : str2, (i2 & 8) != 0 ? -1 : i, (i2 & 16) != 0 ? true : z);
    }

    public final long getId() {
        return this.id;
    }

    public final void setId(long j) {
        this.id = j;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.name = str;
    }

    @NotNull
    public final String getRule() {
        return this.rule;
    }

    public final void setRule(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.rule = str;
    }

    public final int getSerialNumber() {
        return this.serialNumber;
    }

    public final void setSerialNumber(int i) {
        this.serialNumber = i;
    }

    public final boolean getEnable() {
        return this.enable;
    }

    public final void setEnable(boolean z) {
        this.enable = z;
    }
}
