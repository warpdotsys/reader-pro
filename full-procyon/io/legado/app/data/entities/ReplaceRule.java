// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b1\b\u0086\b\u0018\u00002\u00020\u0001B\u0081\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u000b\u0012\b\b\u0002\u0010\r\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000b\u0012\b\b\u0002\u0010\u000f\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0011?\u0006\u0002\u0010\u0012J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u000bH\u00c6\u0003J\t\u00103\u001a\u00020\u0003H\u00c6\u0003J\t\u00104\u001a\u00020\u0011H\u00c6\u0003J\t\u00105\u001a\u00020\u0005H\u00c6\u0003J\u000b\u00106\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\u0005H\u00c6\u0003J\t\u00108\u001a\u00020\u0005H\u00c6\u0003J\u000b\u00109\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010:\u001a\u00020\u000bH\u00c6\u0003J\t\u0010;\u001a\u00020\u000bH\u00c6\u0003J\t\u0010<\u001a\u00020\u000bH\u00c6\u0003J\u0085\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000b2\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u00c6\u0001J\u0013\u0010>\u001a\u00020\u000b2\b\u0010?\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010@\u001a\u00020\u0011H\u00d6\u0001J\t\u0010A\u001a\u00020\u0005H\u00d6\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\r\u001a\u00020\u000b8\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u001b\"\u0004\b\u001c\u0010\u001dR\u001c\u0010\u000e\u001a\u00020\u000b8\u0007X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u001b\"\u0004\b\u001e\u0010\u001dR\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001f\u0010\u0014\"\u0004\b \u0010\u0016R\u001a\u0010\u0010\u001a\u00020\u0011X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001a\u0010\u0007\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b%\u0010\u0014\"\u0004\b&\u0010\u0016R\u001a\u0010\b\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b'\u0010\u0014\"\u0004\b(\u0010\u0016R\u001c\u0010\t\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b)\u0010\u0014\"\u0004\b*\u0010\u0016R\u001a\u0010\f\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b+\u0010\u001b\"\u0004\b,\u0010\u001dR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u001b\"\u0004\b.\u0010\u001dR\u001a\u0010\u000f\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b/\u0010\u0018\"\u0004\b0\u0010\u001a¡§\u0006B" }, d2 = { "Lio/legado/app/data/entities/ReplaceRule;", "", "id", "", "name", "", "group", "pattern", "replacement", "scope", "scopeTitle", "", "scopeContent", "isEnabled", "isRegex", "timeoutMillisecond", "order", "", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZJI)V", "getGroup", "()Ljava/lang/String;", "setGroup", "(Ljava/lang/String;)V", "getId", "()J", "setId", "(J)V", "()Z", "setEnabled", "(Z)V", "setRegex", "getName", "setName", "getOrder", "()I", "setOrder", "(I)V", "getPattern", "setPattern", "getReplacement", "setReplacement", "getScope", "setScope", "getScopeContent", "setScopeContent", "getScopeTitle", "setScopeTitle", "getTimeoutMillisecond", "setTimeoutMillisecond", "component1", "component10", "component11", "component12", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
public final class ReplaceRule
{
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
    
    public ReplaceRule(final long id, @NotNull final String name, @Nullable final String group, @NotNull final String pattern, @NotNull final String replacement, @Nullable final String scope, final boolean scopeTitle, final boolean scopeContent, final boolean isEnabled, final boolean isRegex, final long timeoutMillisecond, final int order) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)pattern, "pattern");
        Intrinsics.checkNotNullParameter((Object)replacement, "replacement");
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
    
    public final long getId() {
        return this.id;
    }
    
    public final void setId(final long <set-?>) {
        this.id = <set-?>;
    }
    
    @NotNull
    public final String getName() {
        return this.name;
    }
    
    public final void setName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.name = <set-?>;
    }
    
    @Nullable
    public final String getGroup() {
        return this.group;
    }
    
    public final void setGroup(@Nullable final String <set-?>) {
        this.group = <set-?>;
    }
    
    @NotNull
    public final String getPattern() {
        return this.pattern;
    }
    
    public final void setPattern(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.pattern = <set-?>;
    }
    
    @NotNull
    public final String getReplacement() {
        return this.replacement;
    }
    
    public final void setReplacement(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.replacement = <set-?>;
    }
    
    @Nullable
    public final String getScope() {
        return this.scope;
    }
    
    public final void setScope(@Nullable final String <set-?>) {
        this.scope = <set-?>;
    }
    
    public final boolean getScopeTitle() {
        return this.scopeTitle;
    }
    
    public final void setScopeTitle(final boolean <set-?>) {
        this.scopeTitle = <set-?>;
    }
    
    public final boolean getScopeContent() {
        return this.scopeContent;
    }
    
    public final void setScopeContent(final boolean <set-?>) {
        this.scopeContent = <set-?>;
    }
    
    @JsonProperty("isEnabled")
    public final boolean isEnabled() {
        return this.isEnabled;
    }
    
    public final void setEnabled(final boolean <set-?>) {
        this.isEnabled = <set-?>;
    }
    
    @JsonProperty("isRegex")
    public final boolean isRegex() {
        return this.isRegex;
    }
    
    public final void setRegex(final boolean <set-?>) {
        this.isRegex = <set-?>;
    }
    
    public final long getTimeoutMillisecond() {
        return this.timeoutMillisecond;
    }
    
    public final void setTimeoutMillisecond(final long <set-?>) {
        this.timeoutMillisecond = <set-?>;
    }
    
    public final int getOrder() {
        return this.order;
    }
    
    public final void setOrder(final int <set-?>) {
        this.order = <set-?>;
    }
    
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
    public final ReplaceRule copy(final long id, @NotNull final String name, @Nullable final String group, @NotNull final String pattern, @NotNull final String replacement, @Nullable final String scope, final boolean scopeTitle, final boolean scopeContent, final boolean isEnabled, final boolean isRegex, final long timeoutMillisecond, final int order) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)pattern, "pattern");
        Intrinsics.checkNotNullParameter((Object)replacement, "replacement");
        return new ReplaceRule(id, name, group, pattern, replacement, scope, scopeTitle, scopeContent, isEnabled, isRegex, timeoutMillisecond, order);
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ReplaceRule(id=").append(this.id).append(", name=").append(this.name).append(", group=").append((Object)this.group).append(", pattern=").append(this.pattern).append(", replacement=").append(this.replacement).append(", scope=").append((Object)this.scope).append(", scopeTitle=").append(this.scopeTitle).append(", scopeContent=").append(this.scopeContent).append(", isEnabled=").append(this.isEnabled).append(", isRegex=").append(this.isRegex).append(", timeoutMillisecond=").append(this.timeoutMillisecond).append(", order=");
        sb.append(this.order).append(')');
        return sb.toString();
    }
    
    @Override
    public int hashCode() {
        int result = Long.hashCode(this.id);
        result = result * 31 + this.name.hashCode();
        result = result * 31 + ((this.group == null) ? 0 : this.group.hashCode());
        result = result * 31 + this.pattern.hashCode();
        result = result * 31 + this.replacement.hashCode();
        result = result * 31 + ((this.scope == null) ? 0 : this.scope.hashCode());
        final int n = result * 31;
        int scopeTitle;
        if ((scopeTitle = (this.scopeTitle ? 1 : 0)) != 0) {
            scopeTitle = 1;
        }
        result = n + scopeTitle;
        final int n2 = result * 31;
        int scopeContent;
        if ((scopeContent = (this.scopeContent ? 1 : 0)) != 0) {
            scopeContent = 1;
        }
        result = n2 + scopeContent;
        final int n3 = result * 31;
        int isEnabled;
        if ((isEnabled = (this.isEnabled ? 1 : 0)) != 0) {
            isEnabled = 1;
        }
        result = n3 + isEnabled;
        final int n4 = result * 31;
        int isRegex;
        if ((isRegex = (this.isRegex ? 1 : 0)) != 0) {
            isRegex = 1;
        }
        result = n4 + isRegex;
        result = result * 31 + Long.hashCode(this.timeoutMillisecond);
        result = result * 31 + Integer.hashCode(this.order);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ReplaceRule)) {
            return false;
        }
        final ReplaceRule replaceRule = (ReplaceRule)other;
        return this.id == replaceRule.id && Intrinsics.areEqual((Object)this.name, (Object)replaceRule.name) && Intrinsics.areEqual((Object)this.group, (Object)replaceRule.group) && Intrinsics.areEqual((Object)this.pattern, (Object)replaceRule.pattern) && Intrinsics.areEqual((Object)this.replacement, (Object)replaceRule.replacement) && Intrinsics.areEqual((Object)this.scope, (Object)replaceRule.scope) && this.scopeTitle == replaceRule.scopeTitle && this.scopeContent == replaceRule.scopeContent && this.isEnabled == replaceRule.isEnabled && this.isRegex == replaceRule.isRegex && this.timeoutMillisecond == replaceRule.timeoutMillisecond && this.order == replaceRule.order;
    }
    
    public ReplaceRule() {
        this(0L, null, null, null, null, null, false, false, false, false, 0L, 0, 4095, null);
    }
}
