// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\b\u0086\b\u0018\u00002\u00020\u0001B7\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n?\u0006\u0002\u0010\u000bJ\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\t\u0010 \u001a\u00020\u0005H\u00c6\u0003J\t\u0010!\u001a\u00020\bH\u00c6\u0003J\t\u0010\"\u001a\u00020\nH\u00c6\u0003J;\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010$\u001a\u00020\n2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\bH\u00d6\u0001J\t\u0010'\u001a\u00020\u0005H\u00d6\u0001R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u001a\u0010\u0006\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0015\"\u0004\b\u0019\u0010\u0017R\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¡§\u0006(" }, d2 = { "Lio/legado/app/data/entities/TxtTocRule;", "", "id", "", "name", "", "rule", "serialNumber", "", "enable", "", "(JLjava/lang/String;Ljava/lang/String;IZ)V", "getEnable", "()Z", "setEnable", "(Z)V", "getId", "()J", "setId", "(J)V", "getName", "()Ljava/lang/String;", "setName", "(Ljava/lang/String;)V", "getRule", "setRule", "getSerialNumber", "()I", "setSerialNumber", "(I)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
public final class TxtTocRule
{
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String rule;
    private int serialNumber;
    private boolean enable;
    
    public TxtTocRule(final long id, @NotNull final String name, @NotNull final String rule, final int serialNumber, final boolean enable) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        this.id = id;
        this.name = name;
        this.rule = rule;
        this.serialNumber = serialNumber;
        this.enable = enable;
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
    
    @NotNull
    public final String getRule() {
        return this.rule;
    }
    
    public final void setRule(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.rule = <set-?>;
    }
    
    public final int getSerialNumber() {
        return this.serialNumber;
    }
    
    public final void setSerialNumber(final int <set-?>) {
        this.serialNumber = <set-?>;
    }
    
    public final boolean getEnable() {
        return this.enable;
    }
    
    public final void setEnable(final boolean <set-?>) {
        this.enable = <set-?>;
    }
    
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
    public final TxtTocRule copy(final long id, @NotNull final String name, @NotNull final String rule, final int serialNumber, final boolean enable) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)rule, "rule");
        return new TxtTocRule(id, name, rule, serialNumber, enable);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "TxtTocRule(id=" + this.id + ", name=" + this.name + ", rule=" + this.rule + ", serialNumber=" + this.serialNumber + ", enable=" + this.enable + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Long.hashCode(this.id);
        result = result * 31 + this.name.hashCode();
        result = result * 31 + this.rule.hashCode();
        result = result * 31 + Integer.hashCode(this.serialNumber);
        final int n = result * 31;
        int enable;
        if ((enable = (this.enable ? 1 : 0)) != 0) {
            enable = 1;
        }
        result = n + enable;
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TxtTocRule)) {
            return false;
        }
        final TxtTocRule txtTocRule = (TxtTocRule)other;
        return this.id == txtTocRule.id && Intrinsics.areEqual((Object)this.name, (Object)txtTocRule.name) && Intrinsics.areEqual((Object)this.rule, (Object)txtTocRule.rule) && this.serialNumber == txtTocRule.serialNumber && this.enable == txtTocRule.enable;
    }
    
    public TxtTocRule() {
        this(0L, null, null, 0, false, 31, null);
    }
}
