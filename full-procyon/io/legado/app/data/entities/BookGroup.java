// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n?\u0006\u0002\u0010\u000bJ\t\u0010\u001e\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010!\u001a\u00020\bH\u00c6\u0003J\t\u0010\"\u001a\u00020\nH\u00c6\u0003J=\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u00c6\u0001J\u0013\u0010$\u001a\u00020\n2\b\u0010%\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010&\u001a\u00020\bH\u00d6\u0001J\t\u0010'\u001a\u00020\u0005H\u00d6\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¡§\u0006(" }, d2 = { "Lio/legado/app/data/entities/BookGroup;", "", "groupId", "", "groupName", "", "cover", "order", "", "show", "", "(JLjava/lang/String;Ljava/lang/String;IZ)V", "getCover", "()Ljava/lang/String;", "setCover", "(Ljava/lang/String;)V", "getGroupId", "()J", "setGroupId", "(J)V", "getGroupName", "setGroupName", "getOrder", "()I", "setOrder", "(I)V", "getShow", "()Z", "setShow", "(Z)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "reader-pro" })
public final class BookGroup
{
    private long groupId;
    @NotNull
    private String groupName;
    @Nullable
    private String cover;
    private int order;
    private boolean show;
    
    public BookGroup(final long groupId, @NotNull final String groupName, @Nullable final String cover, final int order, final boolean show) {
        Intrinsics.checkNotNullParameter((Object)groupName, "groupName");
        this.groupId = groupId;
        this.groupName = groupName;
        this.cover = cover;
        this.order = order;
        this.show = show;
    }
    
    public final long getGroupId() {
        return this.groupId;
    }
    
    public final void setGroupId(final long <set-?>) {
        this.groupId = <set-?>;
    }
    
    @NotNull
    public final String getGroupName() {
        return this.groupName;
    }
    
    public final void setGroupName(@NotNull final String <set-?>) {
        Intrinsics.checkNotNullParameter((Object)<set-?>, "<set-?>");
        this.groupName = <set-?>;
    }
    
    @Nullable
    public final String getCover() {
        return this.cover;
    }
    
    public final void setCover(@Nullable final String <set-?>) {
        this.cover = <set-?>;
    }
    
    public final int getOrder() {
        return this.order;
    }
    
    public final void setOrder(final int <set-?>) {
        this.order = <set-?>;
    }
    
    public final boolean getShow() {
        return this.show;
    }
    
    public final void setShow(final boolean <set-?>) {
        this.show = <set-?>;
    }
    
    public final long component1() {
        return this.groupId;
    }
    
    @NotNull
    public final String component2() {
        return this.groupName;
    }
    
    @Nullable
    public final String component3() {
        return this.cover;
    }
    
    public final int component4() {
        return this.order;
    }
    
    public final boolean component5() {
        return this.show;
    }
    
    @NotNull
    public final BookGroup copy(final long groupId, @NotNull final String groupName, @Nullable final String cover, final int order, final boolean show) {
        Intrinsics.checkNotNullParameter((Object)groupName, "groupName");
        return new BookGroup(groupId, groupName, cover, order, show);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "BookGroup(groupId=" + this.groupId + ", groupName=" + this.groupName + ", cover=" + (Object)this.cover + ", order=" + this.order + ", show=" + this.show + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Long.hashCode(this.groupId);
        result = result * 31 + this.groupName.hashCode();
        result = result * 31 + ((this.cover == null) ? 0 : this.cover.hashCode());
        result = result * 31 + Integer.hashCode(this.order);
        final int n = result * 31;
        int show;
        if ((show = (this.show ? 1 : 0)) != 0) {
            show = 1;
        }
        result = n + show;
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BookGroup)) {
            return false;
        }
        final BookGroup bookGroup = (BookGroup)other;
        return this.groupId == bookGroup.groupId && Intrinsics.areEqual((Object)this.groupName, (Object)bookGroup.groupName) && Intrinsics.areEqual((Object)this.cover, (Object)bookGroup.cover) && this.order == bookGroup.order && this.show == bookGroup.show;
    }
    
    public BookGroup() {
        this(0L, null, null, 0, false, 31, null);
    }
}
