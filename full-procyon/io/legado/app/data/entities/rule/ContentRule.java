// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities.rule;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\tJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003JQ\u0010\u001e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\"\u001a\u00020#H\u00d6\u0001J\t\u0010$\u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u000b\"\u0004\b\u0011\u0010\rR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000b\"\u0004\b\u0015\u0010\rR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000b\"\u0004\b\u0017\u0010\r¡§\u0006%" }, d2 = { "Lio/legado/app/data/entities/rule/ContentRule;", "", "content", "", "nextContentUrl", "webJs", "sourceRegex", "replaceRegex", "imageStyle", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getImageStyle", "setImageStyle", "getNextContentUrl", "setNextContentUrl", "getReplaceRegex", "setReplaceRegex", "getSourceRegex", "setSourceRegex", "getWebJs", "setWebJs", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class ContentRule
{
    @Nullable
    private String content;
    @Nullable
    private String nextContentUrl;
    @Nullable
    private String webJs;
    @Nullable
    private String sourceRegex;
    @Nullable
    private String replaceRegex;
    @Nullable
    private String imageStyle;
    
    public ContentRule(@Nullable final String content, @Nullable final String nextContentUrl, @Nullable final String webJs, @Nullable final String sourceRegex, @Nullable final String replaceRegex, @Nullable final String imageStyle) {
        this.content = content;
        this.nextContentUrl = nextContentUrl;
        this.webJs = webJs;
        this.sourceRegex = sourceRegex;
        this.replaceRegex = replaceRegex;
        this.imageStyle = imageStyle;
    }
    
    @Nullable
    public final String getContent() {
        return this.content;
    }
    
    public final void setContent(@Nullable final String <set-?>) {
        this.content = <set-?>;
    }
    
    @Nullable
    public final String getNextContentUrl() {
        return this.nextContentUrl;
    }
    
    public final void setNextContentUrl(@Nullable final String <set-?>) {
        this.nextContentUrl = <set-?>;
    }
    
    @Nullable
    public final String getWebJs() {
        return this.webJs;
    }
    
    public final void setWebJs(@Nullable final String <set-?>) {
        this.webJs = <set-?>;
    }
    
    @Nullable
    public final String getSourceRegex() {
        return this.sourceRegex;
    }
    
    public final void setSourceRegex(@Nullable final String <set-?>) {
        this.sourceRegex = <set-?>;
    }
    
    @Nullable
    public final String getReplaceRegex() {
        return this.replaceRegex;
    }
    
    public final void setReplaceRegex(@Nullable final String <set-?>) {
        this.replaceRegex = <set-?>;
    }
    
    @Nullable
    public final String getImageStyle() {
        return this.imageStyle;
    }
    
    public final void setImageStyle(@Nullable final String <set-?>) {
        this.imageStyle = <set-?>;
    }
    
    @Nullable
    public final String component1() {
        return this.content;
    }
    
    @Nullable
    public final String component2() {
        return this.nextContentUrl;
    }
    
    @Nullable
    public final String component3() {
        return this.webJs;
    }
    
    @Nullable
    public final String component4() {
        return this.sourceRegex;
    }
    
    @Nullable
    public final String component5() {
        return this.replaceRegex;
    }
    
    @Nullable
    public final String component6() {
        return this.imageStyle;
    }
    
    @NotNull
    public final ContentRule copy(@Nullable final String content, @Nullable final String nextContentUrl, @Nullable final String webJs, @Nullable final String sourceRegex, @Nullable final String replaceRegex, @Nullable final String imageStyle) {
        return new ContentRule(content, nextContentUrl, webJs, sourceRegex, replaceRegex, imageStyle);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "ContentRule(content=" + (Object)this.content + ", nextContentUrl=" + (Object)this.nextContentUrl + ", webJs=" + (Object)this.webJs + ", sourceRegex=" + (Object)this.sourceRegex + ", replaceRegex=" + (Object)this.replaceRegex + ", imageStyle=" + (Object)this.imageStyle + ')';
    }
    
    @Override
    public int hashCode() {
        int result = (this.content == null) ? 0 : this.content.hashCode();
        result = result * 31 + ((this.nextContentUrl == null) ? 0 : this.nextContentUrl.hashCode());
        result = result * 31 + ((this.webJs == null) ? 0 : this.webJs.hashCode());
        result = result * 31 + ((this.sourceRegex == null) ? 0 : this.sourceRegex.hashCode());
        result = result * 31 + ((this.replaceRegex == null) ? 0 : this.replaceRegex.hashCode());
        result = result * 31 + ((this.imageStyle == null) ? 0 : this.imageStyle.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentRule)) {
            return false;
        }
        final ContentRule contentRule = (ContentRule)other;
        return Intrinsics.areEqual((Object)this.content, (Object)contentRule.content) && Intrinsics.areEqual((Object)this.nextContentUrl, (Object)contentRule.nextContentUrl) && Intrinsics.areEqual((Object)this.webJs, (Object)contentRule.webJs) && Intrinsics.areEqual((Object)this.sourceRegex, (Object)contentRule.sourceRegex) && Intrinsics.areEqual((Object)this.replaceRegex, (Object)contentRule.replaceRegex) && Intrinsics.areEqual((Object)this.imageStyle, (Object)contentRule.imageStyle);
    }
    
    public ContentRule() {
        this(null, null, null, null, null, null, 63, null);
    }
}
