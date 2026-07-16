// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities.rule;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.internal.DefaultConstructorMarker;
import org.jetbrains.annotations.Nullable;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\"\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Be\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003?\u0006\u0002\u0010\u000bJ\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003Ji\u0010$\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010%\u001a\u00020&2\b\u0010'\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010(\u001a\u00020)H\u00d6\u0001J\t\u0010*\u001a\u00020\u0003H\u00d6\u0001R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\r\"\u0004\b\u0014\u0010\u000fR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e?\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\r\"\u0004\b\u001b\u0010\u000f¡§\u0006+" }, d2 = { "Lio/legado/app/data/entities/rule/TocRule;", "", "preUpdateJs", "", "chapterList", "chapterName", "chapterUrl", "isVolume", "isVip", "updateTime", "nextTocUrl", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getChapterList", "()Ljava/lang/String;", "setChapterList", "(Ljava/lang/String;)V", "getChapterName", "setChapterName", "getChapterUrl", "setChapterUrl", "setVip", "setVolume", "getNextTocUrl", "setNextTocUrl", "getPreUpdateJs", "setPreUpdateJs", "getUpdateTime", "setUpdateTime", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", "", "other", "hashCode", "", "toString", "reader-pro" })
public final class TocRule
{
    @Nullable
    private String preUpdateJs;
    @Nullable
    private String chapterList;
    @Nullable
    private String chapterName;
    @Nullable
    private String chapterUrl;
    @Nullable
    private String isVolume;
    @Nullable
    private String isVip;
    @Nullable
    private String updateTime;
    @Nullable
    private String nextTocUrl;
    
    public TocRule(@Nullable final String preUpdateJs, @Nullable final String chapterList, @Nullable final String chapterName, @Nullable final String chapterUrl, @Nullable final String isVolume, @Nullable final String isVip, @Nullable final String updateTime, @Nullable final String nextTocUrl) {
        this.preUpdateJs = preUpdateJs;
        this.chapterList = chapterList;
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.isVolume = isVolume;
        this.isVip = isVip;
        this.updateTime = updateTime;
        this.nextTocUrl = nextTocUrl;
    }
    
    @Nullable
    public final String getPreUpdateJs() {
        return this.preUpdateJs;
    }
    
    public final void setPreUpdateJs(@Nullable final String <set-?>) {
        this.preUpdateJs = <set-?>;
    }
    
    @Nullable
    public final String getChapterList() {
        return this.chapterList;
    }
    
    public final void setChapterList(@Nullable final String <set-?>) {
        this.chapterList = <set-?>;
    }
    
    @Nullable
    public final String getChapterName() {
        return this.chapterName;
    }
    
    public final void setChapterName(@Nullable final String <set-?>) {
        this.chapterName = <set-?>;
    }
    
    @Nullable
    public final String getChapterUrl() {
        return this.chapterUrl;
    }
    
    public final void setChapterUrl(@Nullable final String <set-?>) {
        this.chapterUrl = <set-?>;
    }
    
    @Nullable
    public final String isVolume() {
        return this.isVolume;
    }
    
    public final void setVolume(@Nullable final String <set-?>) {
        this.isVolume = <set-?>;
    }
    
    @Nullable
    public final String isVip() {
        return this.isVip;
    }
    
    public final void setVip(@Nullable final String <set-?>) {
        this.isVip = <set-?>;
    }
    
    @Nullable
    public final String getUpdateTime() {
        return this.updateTime;
    }
    
    public final void setUpdateTime(@Nullable final String <set-?>) {
        this.updateTime = <set-?>;
    }
    
    @Nullable
    public final String getNextTocUrl() {
        return this.nextTocUrl;
    }
    
    public final void setNextTocUrl(@Nullable final String <set-?>) {
        this.nextTocUrl = <set-?>;
    }
    
    @Nullable
    public final String component1() {
        return this.preUpdateJs;
    }
    
    @Nullable
    public final String component2() {
        return this.chapterList;
    }
    
    @Nullable
    public final String component3() {
        return this.chapterName;
    }
    
    @Nullable
    public final String component4() {
        return this.chapterUrl;
    }
    
    @Nullable
    public final String component5() {
        return this.isVolume;
    }
    
    @Nullable
    public final String component6() {
        return this.isVip;
    }
    
    @Nullable
    public final String component7() {
        return this.updateTime;
    }
    
    @Nullable
    public final String component8() {
        return this.nextTocUrl;
    }
    
    @NotNull
    public final TocRule copy(@Nullable final String preUpdateJs, @Nullable final String chapterList, @Nullable final String chapterName, @Nullable final String chapterUrl, @Nullable final String isVolume, @Nullable final String isVip, @Nullable final String updateTime, @Nullable final String nextTocUrl) {
        return new TocRule(preUpdateJs, chapterList, chapterName, chapterUrl, isVolume, isVip, updateTime, nextTocUrl);
    }
    
    @NotNull
    @Override
    public String toString() {
        return "TocRule(preUpdateJs=" + (Object)this.preUpdateJs + ", chapterList=" + (Object)this.chapterList + ", chapterName=" + (Object)this.chapterName + ", chapterUrl=" + (Object)this.chapterUrl + ", isVolume=" + (Object)this.isVolume + ", isVip=" + (Object)this.isVip + ", updateTime=" + (Object)this.updateTime + ", nextTocUrl=" + (Object)this.nextTocUrl + ')';
    }
    
    @Override
    public int hashCode() {
        int result = (this.preUpdateJs == null) ? 0 : this.preUpdateJs.hashCode();
        result = result * 31 + ((this.chapterList == null) ? 0 : this.chapterList.hashCode());
        result = result * 31 + ((this.chapterName == null) ? 0 : this.chapterName.hashCode());
        result = result * 31 + ((this.chapterUrl == null) ? 0 : this.chapterUrl.hashCode());
        result = result * 31 + ((this.isVolume == null) ? 0 : this.isVolume.hashCode());
        result = result * 31 + ((this.isVip == null) ? 0 : this.isVip.hashCode());
        result = result * 31 + ((this.updateTime == null) ? 0 : this.updateTime.hashCode());
        result = result * 31 + ((this.nextTocUrl == null) ? 0 : this.nextTocUrl.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TocRule)) {
            return false;
        }
        final TocRule tocRule = (TocRule)other;
        return Intrinsics.areEqual((Object)this.preUpdateJs, (Object)tocRule.preUpdateJs) && Intrinsics.areEqual((Object)this.chapterList, (Object)tocRule.chapterList) && Intrinsics.areEqual((Object)this.chapterName, (Object)tocRule.chapterName) && Intrinsics.areEqual((Object)this.chapterUrl, (Object)tocRule.chapterUrl) && Intrinsics.areEqual((Object)this.isVolume, (Object)tocRule.isVolume) && Intrinsics.areEqual((Object)this.isVip, (Object)tocRule.isVip) && Intrinsics.areEqual((Object)this.updateTime, (Object)tocRule.updateTime) && Intrinsics.areEqual((Object)this.nextTocUrl, (Object)tocRule.nextTocUrl);
    }
    
    public TocRule() {
        this(null, null, null, null, null, null, null, null, 255, null);
    }
}
