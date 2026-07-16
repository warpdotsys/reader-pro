// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.entity;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003?\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003?\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¡§\u0006\u0013" }, d2 = { "Lcom/htmake/reader/entity/Size;", "", "width", "", "height", "(DD)V", "getHeight", "()D", "getWidth", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "reader-pro" })
public final class Size
{
    private final double width;
    private final double height;
    
    public Size(final double width, final double height) {
        this.width = width;
        this.height = height;
    }
    
    public final double getWidth() {
        return this.width;
    }
    
    public final double getHeight() {
        return this.height;
    }
    
    public final double component1() {
        return this.width;
    }
    
    public final double component2() {
        return this.height;
    }
    
    @NotNull
    public final Size copy(final double width, final double height) {
        return new Size(width, height);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "Size(width=" + this.width + ", height=" + this.height + ')';
    }
    
    @Override
    public int hashCode() {
        int result = Double.hashCode(this.width);
        result = result * 31 + Double.hashCode(this.height);
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Size)) {
            return false;
        }
        final Size size = (Size)other;
        return Intrinsics.areEqual((Object)this.width, (Object)size.width) && Intrinsics.areEqual((Object)this.height, (Object)size.height);
    }
}
