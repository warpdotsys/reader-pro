package io.legado.app.data.entities;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BookGroup.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BookGroup.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u001e\b\u0086\b\u0018\u00002\u00020\u0001B9\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\t\u0010\u001e\u001a\u00020\u0003HÆ\u0003J\t\u0010\u001f\u001a\u00020\u0005HÆ\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010!\u001a\u00020\bHÆ\u0003J\t\u0010\"\u001a\u00020\nHÆ\u0003J=\u0010#\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nHÆ\u0001J\u0013\u0010$\u001a\u00020\n2\b\u0010%\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010&\u001a\u00020\bHÖ\u0001J\t\u0010'\u001a\u00020\u0005HÖ\u0001R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0011\"\u0004\b\u0012\u0010\u0013R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001a\u0010\u0007\u001a\u00020\bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\t\u001a\u00020\nX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¨\u0006("}, d2 = {"Lio/legado/app/data/entities/BookGroup;", PackageDocumentBase.PREFIX_OPF, "groupId", PackageDocumentBase.PREFIX_OPF, "groupName", PackageDocumentBase.PREFIX_OPF, "cover", "order", PackageDocumentBase.PREFIX_OPF, "show", PackageDocumentBase.PREFIX_OPF, "(JLjava/lang/String;Ljava/lang/String;IZ)V", "getCover", "()Ljava/lang/String;", "setCover", "(Ljava/lang/String;)V", "getGroupId", "()J", "setGroupId", "(J)V", "getGroupName", "setGroupName", "getOrder", "()I", "setOrder", "(I)V", "getShow", "()Z", "setShow", "(Z)V", "component1", "component2", "component3", "component4", "component5", "copy", "equals", "other", "hashCode", "toString", "reader-pro"})
public final /* data */ class BookGroup {
    private long groupId;

    @NotNull
    private String groupName;

    @Nullable
    private String cover;
    private int order;
    private boolean show;

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
    public final BookGroup copy(long groupId, @NotNull String groupName, @Nullable String cover, int order, boolean show) {
        Intrinsics.checkNotNullParameter(groupName, "groupName");
        return new BookGroup(groupId, groupName, cover, order, show);
    }

    public static /* synthetic */ BookGroup copy$default(BookGroup bookGroup, long j, String str, String str2, int i, boolean z, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            j = bookGroup.groupId;
        }
        if ((i2 & 2) != 0) {
            str = bookGroup.groupName;
        }
        if ((i2 & 4) != 0) {
            str2 = bookGroup.cover;
        }
        if ((i2 & 8) != 0) {
            i = bookGroup.order;
        }
        if ((i2 & 16) != 0) {
            z = bookGroup.show;
        }
        return bookGroup.copy(j, str, str2, i, z);
    }

    @NotNull
    public String toString() {
        return "BookGroup(groupId=" + this.groupId + ", groupName=" + this.groupName + ", cover=" + ((Object) this.cover) + ", order=" + this.order + ", show=" + this.show + ')';
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v18, types: [int] */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v21 */
    public int hashCode() {
        int iHashCode = ((((((Long.hashCode(this.groupId) * 31) + this.groupName.hashCode()) * 31) + (this.cover == null ? 0 : this.cover.hashCode())) * 31) + Integer.hashCode(this.order)) * 31;
        boolean z = this.show;
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
        if (!(other instanceof BookGroup)) {
            return false;
        }
        BookGroup bookGroup = (BookGroup) other;
        return this.groupId == bookGroup.groupId && Intrinsics.areEqual(this.groupName, bookGroup.groupName) && Intrinsics.areEqual(this.cover, bookGroup.cover) && this.order == bookGroup.order && this.show == bookGroup.show;
    }

    public BookGroup() {
        this(0L, null, null, 0, false, 31, null);
    }

    public BookGroup(long groupId, @NotNull String groupName, @Nullable String cover, int order, boolean show) {
        Intrinsics.checkNotNullParameter(groupName, "groupName");
        this.groupId = groupId;
        this.groupName = groupName;
        this.cover = cover;
        this.order = order;
        this.show = show;
    }

    public /* synthetic */ BookGroup(long j, String str, String str2, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 0L : j, (i2 & 2) != 0 ? PackageDocumentBase.PREFIX_OPF : str, (i2 & 4) != 0 ? null : str2, (i2 & 8) != 0 ? 0 : i, (i2 & 16) != 0 ? true : z);
    }

    public final long getGroupId() {
        return this.groupId;
    }

    public final void setGroupId(long j) {
        this.groupId = j;
    }

    @NotNull
    public final String getGroupName() {
        return this.groupName;
    }

    public final void setGroupName(@NotNull String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.groupName = str;
    }

    @Nullable
    public final String getCover() {
        return this.cover;
    }

    public final void setCover(@Nullable String str) {
        this.cover = str;
    }

    public final int getOrder() {
        return this.order;
    }

    public final void setOrder(int i) {
        this.order = i;
    }

    public final boolean getShow() {
        return this.show;
    }

    public final void setShow(boolean z) {
        this.show = z;
    }
}
