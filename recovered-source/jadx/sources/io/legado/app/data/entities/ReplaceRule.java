package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: ReplaceRule.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/ReplaceRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b1\b\u0086\b\u0018\u00002\u00020\u0001B\u0081\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u000bHÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0011HÆ\u0003J\t\u00105\u001a\u00020\u0005HÆ\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u00107\u001a\u00020\u0005HÆ\u0003J\t\u00108\u001a\u00020\u0005HÆ\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010:\u001a\u00020\u000bHÆ\u0003J\t\u0010;\u001a\u00020\u000bHÆ\u0003J\t\u0010<\u001a\u00020\u000bHÆ\u0003J\u0085\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u0011HÆ\u0001J\u0013\u0010>\u001a\u00020\u000b2\b\u0010?\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010@\u001a\u00020\u0011HÖ\u0001J\t\u0010A\u001a\u00020\u0005HÖ\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\r\u001a\u00020\u000b8\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001c\u0010\u000e\u001a\u00020\u000b8\u0007X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u001b\"\u0004\b\u001e\u0010\u001dR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0014\"\u0004\b \u0010\u0016R\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0014\"\u0004\b&\u0010\u0016R\u001a\u0010\b\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0014\"\u0004\b(\u0010\u0016R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0014\"\u0004\b*\u0010\u0016R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001b\"\u0004\b,\u0010\u001dR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001b\"\u0004\b.\u0010\u001dR\u001a\u0010\u000f\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0018\"\u0004\b0\u0010\u001a¨\u0006B"}, d2 = {"Lio/legado/app/data/entities/ReplaceRule;", PackageDocumentBase.PREFIX_OPF, "id", PackageDocumentBase.PREFIX_OPF, "name", PackageDocumentBase.PREFIX_OPF, "group", "pattern", "replacement", "scope", "scopeTitle", PackageDocumentBase.PREFIX_OPF, "scopeContent", "isEnabled", "isRegex", "timeoutMillisecond", "order", PackageDocumentBase.PREFIX_OPF, "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZJI)V", "getGroup", "()Ljava/lang/String;", "setGroup", "(Ljava/lang/String;)V", "getId", "()J", "setId", "(J)V", "()Z", "setEnabled", "(Z)V", "setRegex", "getName", "setName", "getOrder", "()I", "setOrder", "(I)V", "getPattern", "setPattern", "getReplacement", "setReplacement", "getScope", "setScope", "getScopeContent", "setScopeContent", "getScopeTitle", "setScopeTitle", "getTimeoutMillisecond", "setTimeoutMillisecond", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class ReplaceRule {
    private long id;

    @NotNull
    private String name;

    @Nullable
    private String group;

    @NotNull
    private String pattern;

    @NotNull
    private String replacement;

    @Nullable
    private String scope;
    private boolean scopeTitle;
    private boolean scopeContent;
    private boolean isEnabled;
    private boolean isRegex;
    private long timeoutMillisecond;
    private int order;

    public final long component1() {
        return this.id;
    }

    @NotNull
    public final String component2() {
        return this.name;
    }

    @Nullable
    public final String component3() {
        return this.group;
    }

    @NotNull
    public final String component4() {
        return this.pattern;
    }

    @NotNull
    public final String component5() {
        return this.replacement;
    }

    @Nullable
    public final String component6() {
        return this.scope;
    }

    public final boolean component7() {
        return this.scopeTitle;
    }

    public final boolean component8() {
        return this.scopeContent;
    }

    public final boolean component9() {
        return this.isEnabled;
    }

    public final boolean component10() {
        return this.isRegex;
    }

    public final long component11() {
        return this.timeoutMillisecond;
    }

    public final int component12() {
        return this.order;
    }

    @NotNull
    public final ReplaceRule copy(long id, @NotNull String name, @Nullable String group, @NotNull String pattern, @NotNull String replacement, @Nullable String scope, boolean scopeTitle, boolean scopeContent, boolean isEnabled, boolean isRegex, long timeoutMillisecond, int order) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        return new ReplaceRule(id, name, group, pattern, replacement, scope, scopeTitle, scopeContent, isEnabled, isRegex, timeoutMillisecond, order);
    }

    public static /* synthetic */ ReplaceRule copy$default(ReplaceRule replaceRule, long j, String str, String str2, String str3, String str4, String str5, boolean z, boolean z2, boolean z3, boolean z4, long j2, int i, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            j = replaceRule.id;
        }
        if ((i2 & 2) != 0) {
            str = replaceRule.name;
        }
        if ((i2 & 4) != 0) {
            str2 = replaceRule.group;
        }
        if ((i2 & 8) != 0) {
            str3 = replaceRule.pattern;
        }
        if ((i2 & 16) != 0) {
            str4 = replaceRule.replacement;
        }
        if ((i2 & 32) != 0) {
            str5 = replaceRule.scope;
        }
        if ((i2 & 64) != 0) {
            z = replaceRule.scopeTitle;
        }
        if ((i2 & Wbxml.EXT_T_0) != 0) {
            z2 = replaceRule.scopeContent;
        }
        if ((i2 & 256) != 0) {
            z3 = replaceRule.isEnabled;
        }
        if ((i2 & 512) != 0) {
            z4 = replaceRule.isRegex;
        }
        if ((i2 & 1024) != 0) {
            j2 = replaceRule.timeoutMillisecond;
        }
        if ((i2 & 2048) != 0) {
            i = replaceRule.order;
        }
        return replaceRule.copy(j, str, str2, str3, str4, str5, z, z2, z3, z4, j2, i);
    }

    @NotNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReplaceRule(id=").append(this.id).append(", name=").append(this.name).append(", group=").append((Object) this.group).append(", pattern=").append(this.pattern).append(", replacement=").append(this.replacement).append(", scope=").append((Object) this.scope).append(", scopeTitle=").append(this.scopeTitle).append(", scopeContent=").append(this.scopeContent).append(", isEnabled=").append(this.isEnabled).append(", isRegex=").append(this.isRegex).append(", timeoutMillisecond=").append(this.timeoutMillisecond).append(", order=");
        sb.append(this.order).append(')');
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v29, types: [int] */
    /* JADX WARN: Type inference failed for: r1v33, types: [int] */
    /* JADX WARN: Type inference failed for: r1v37, types: [int] */
    /* JADX WARN: Type inference failed for: r1v41, types: [int] */
    /* JADX WARN: Type inference failed for: r1v50 */
    /* JADX WARN: Type inference failed for: r1v51 */
    /* JADX WARN: Type inference failed for: r1v52 */
    /* JADX WARN: Type inference failed for: r1v53 */
    /* JADX WARN: Type inference failed for: r1v56 */
    /* JADX WARN: Type inference failed for: r1v57 */
    /* JADX WARN: Type inference failed for: r1v58 */
    /* JADX WARN: Type inference failed for: r1v59 */
    public int hashCode() {
        int iHashCode = ((((((((((Long.hashCode(this.id) * 31) + this.name.hashCode()) * 31) + (this.group == null ? 0 : this.group.hashCode())) * 31) + this.pattern.hashCode()) * 31) + this.replacement.hashCode()) * 31) + (this.scope == null ? 0 : this.scope.hashCode())) * 31;
        boolean z = this.scopeTitle;
        ?? r1 = z;
        if (z) {
            r1 = 1;
        }
        int i = (iHashCode + r1) * 31;
        boolean z2 = this.scopeContent;
        ?? r12 = z2;
        if (z2) {
            r12 = 1;
        }
        int i2 = (i + r12) * 31;
        boolean z3 = this.isEnabled;
        ?? r13 = z3;
        if (z3) {
            r13 = 1;
        }
        int i3 = (i2 + r13) * 31;
        boolean z4 = this.isRegex;
        ?? r14 = z4;
        if (z4) {
            r14 = 1;
        }
        return ((((i3 + r14) * 31) + Long.hashCode(this.timeoutMillisecond)) * 31) + Integer.hashCode(this.order);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ReplaceRule)) {
            return false;
        }
        ReplaceRule replaceRule = (ReplaceRule) other;
        return this.id == replaceRule.id && Intrinsics.areEqual(this.name, replaceRule.name) && Intrinsics.areEqual(this.group, replaceRule.group) && Intrinsics.areEqual(this.pattern, replaceRule.pattern) && Intrinsics.areEqual(this.replacement, replaceRule.replacement) && Intrinsics.areEqual(this.scope, replaceRule.scope) && this.scopeTitle == replaceRule.scopeTitle && this.scopeContent == replaceRule.scopeContent && this.isEnabled == replaceRule.isEnabled && this.isRegex == replaceRule.isRegex && this.timeoutMillisecond == replaceRule.timeoutMillisecond && this.order == replaceRule.order;
    }

    public ReplaceRule() {
        this(0L, null, null, null, null, null, false, false, false, false, 0L, 0, 4095, null);
    }

    public ReplaceRule(long id, @NotNull String name, @Nullable String group, @NotNull String pattern, @NotNull String replacement, @Nullable String scope, boolean scopeTitle, boolean scopeContent, boolean isEnabled, boolean isRegex, long timeoutMillisecond, int order) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(pattern, "pattern");
        Intrinsics.checkNotNullParameter(replacement, "replacement");
        this.id = id;
        this.name = name;
        this.group = group;
        this.pattern = pattern;
        this.replacement = replacement;
        this.scope = scope;
        this.scopeTitle = scopeTitle;
        this.scopeContent = scopeContent;
        this.isEnabled = isEnabled;
        this.isRegex = isRegex;
        this.timeoutMillisecond = timeoutMillisecond;
        this.order = order;
    }

    public /* synthetic */ ReplaceRule(long j, String str, String str2, String str3, String str4, String str5, boolean z, boolean z2, boolean z3, boolean z4, long j2, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? System.currentTimeMillis() : j, (i2 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i2 & 4) != 0 ? null : str2, (i2 & 8) != 0 ? PackageDocumentBase.PREFIX_OPF : str3, (i2 & 16) != 0 ? PackageDocumentBase.PREFIX_OPF : str4, (i2 & 32) != 0 ? null : str5, (i2 & 64) != 0 ? false : z, (i2 & Wbxml.EXT_T_0) != 0 ? true : z2, (i2 & 256) != 0 ? true : z3, (i2 & 512) != 0 ? false : z4, (i2 & 1024) != 0 ? 3000L : j2, (i2 & 2048) != 0 ? 0 : i);
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

    @Nullable
    public final String getGroup() {
        return this.group;
    }

    public final void setGroup(@Nullable String str) {
        this.group = str;
    }

    @NotNull
    public final String getPattern() {
        return this.pattern;
    }

    public final void setPattern(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.pattern = str;
    }

    @NotNull
    public final String getReplacement() {
        return this.replacement;
    }

    public final void setReplacement(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.replacement = str;
    }

    @Nullable
    public final String getScope() {
        return this.scope;
    }

    public final void setScope(@Nullable String str) {
        this.scope = str;
    }

    public final boolean getScopeTitle() {
        return this.scopeTitle;
    }

    public final void setScopeTitle(boolean z) {
        this.scopeTitle = z;
    }

    public final boolean getScopeContent() {
        return this.scopeContent;
    }

    public final void setScopeContent(boolean z) {
        this.scopeContent = z;
    }

    @JsonProperty("isEnabled")
    public final boolean isEnabled() {
        return this.isEnabled;
    }

    public final void setEnabled(boolean z) {
        this.isEnabled = z;
    }

    @JsonProperty("isRegex")
    public final boolean isRegex() {
        return this.isRegex;
    }

    public final void setRegex(boolean z) {
        this.isRegex = z;
    }

    public final long getTimeoutMillisecond() {
        return this.timeoutMillisecond;
    }

    public final void setTimeoutMillisecond(long j) {
        this.timeoutMillisecond = j;
    }

    public final int getOrder() {
        return this.order;
    }

    public final void setOrder(int i) {
        this.order = i;
    }
}
