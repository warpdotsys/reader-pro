package com.htmake.reader.entity;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: Size.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/entity/Size.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, d2 = {"Lcom/htmake/reader/entity/Size;", PackageDocumentBase.PREFIX_OPF, "width", PackageDocumentBase.PREFIX_OPF, "height", "(DD)V", "getHeight", "()D", "getWidth", "component1", "component2", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public final /* data */ class Size {
    private final double width;
    private final double height;

    public final double component1() {
        return this.width;
    }

    public final double component2() {
        return this.height;
    }

    @NotNull
    public final Size copy(double width, double height) {
        return new Size(width, height);
    }

    public static /* synthetic */ Size copy$default(Size size, double d, double d2, int i, Object obj) {
        if ((i & 1) != 0) {
            d = size.width;
        }
        if ((i & 2) != 0) {
            d2 = size.height;
        }
        return size.copy(d, d2);
    }

    @NotNull
    public String toString() {
        return "Size(width=" + this.width + ", height=" + this.height + ')';
    }

    public int hashCode() {
        int result = Double.hashCode(this.width);
        return (result * 31) + Double.hashCode(this.height);
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Size)) {
            return false;
        }
        Size size = (Size) other;
        return Intrinsics.areEqual(Double.valueOf(this.width), Double.valueOf(size.width)) && Intrinsics.areEqual(Double.valueOf(this.height), Double.valueOf(size.height));
    }

    public Size(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public final double getWidth() {
        return this.width;
    }

    public final double getHeight() {
        return this.height;
    }
}
