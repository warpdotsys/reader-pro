package io.legado.app.data.entities.rule;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kxml2.wap.Wbxml;

/* JADX INFO: compiled from: TocRule.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/rule/TocRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\"\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Be\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u000bJ\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001e\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010 \u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\"\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0003HÆ\u0003Ji\u0010$\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\n\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010%\u001a\u00020&2\b\u0010'\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010(\u001a\u00020)HÖ\u0001J\t\u0010*\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\r\"\u0004\b\u0011\u0010\u000fR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u000fR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\r\"\u0004\b\u0014\u0010\u000fR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\r\"\u0004\b\u0015\u0010\u000fR\u001c\u0010\n\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\r\"\u0004\b\u0017\u0010\u000fR\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\r\"\u0004\b\u0019\u0010\u000fR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\r\"\u0004\b\u001b\u0010\u000f¨\u0006+"}, d2 = {"Lio/legado/app/data/entities/rule/TocRule;", PackageDocumentBase.PREFIX_OPF, "preUpdateJs", PackageDocumentBase.PREFIX_OPF, "chapterList", "chapterName", "chapterUrl", "isVolume", "isVip", "updateTime", "nextTocUrl", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getChapterList", "()Ljava/lang/String;", "setChapterList", "(Ljava/lang/String;)V", "getChapterName", "setChapterName", "getChapterUrl", "setChapterUrl", "setVip", "setVolume", "getNextTocUrl", "setNextTocUrl", "getPreUpdateJs", "setPreUpdateJs", "getUpdateTime", "setUpdateTime", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class TocRule {

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
    public final TocRule copy(@Nullable String preUpdateJs, @Nullable String chapterList, @Nullable String chapterName, @Nullable String chapterUrl, @Nullable String isVolume, @Nullable String isVip, @Nullable String updateTime, @Nullable String nextTocUrl) {
        return new TocRule(preUpdateJs, chapterList, chapterName, chapterUrl, isVolume, isVip, updateTime, nextTocUrl);
    }

    public static /* synthetic */ TocRule copy$default(TocRule tocRule, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i, Object obj) {
        if ((i & 1) != 0) {
            str = tocRule.preUpdateJs;
        }
        if ((i & 2) != 0) {
            str2 = tocRule.chapterList;
        }
        if ((i & 4) != 0) {
            str3 = tocRule.chapterName;
        }
        if ((i & 8) != 0) {
            str4 = tocRule.chapterUrl;
        }
        if ((i & 16) != 0) {
            str5 = tocRule.isVolume;
        }
        if ((i & 32) != 0) {
            str6 = tocRule.isVip;
        }
        if ((i & 64) != 0) {
            str7 = tocRule.updateTime;
        }
        if ((i & Wbxml.EXT_T_0) != 0) {
            str8 = tocRule.nextTocUrl;
        }
        return tocRule.copy(str, str2, str3, str4, str5, str6, str7, str8);
    }

    @NotNull
    public String toString() {
        return "TocRule(preUpdateJs=" + ((Object) this.preUpdateJs) + ", chapterList=" + ((Object) this.chapterList) + ", chapterName=" + ((Object) this.chapterName) + ", chapterUrl=" + ((Object) this.chapterUrl) + ", isVolume=" + ((Object) this.isVolume) + ", isVip=" + ((Object) this.isVip) + ", updateTime=" + ((Object) this.updateTime) + ", nextTocUrl=" + ((Object) this.nextTocUrl) + ')';
    }

    public int hashCode() {
        int result = this.preUpdateJs == null ? 0 : this.preUpdateJs.hashCode();
        return (((((((((((((result * 31) + (this.chapterList == null ? 0 : this.chapterList.hashCode())) * 31) + (this.chapterName == null ? 0 : this.chapterName.hashCode())) * 31) + (this.chapterUrl == null ? 0 : this.chapterUrl.hashCode())) * 31) + (this.isVolume == null ? 0 : this.isVolume.hashCode())) * 31) + (this.isVip == null ? 0 : this.isVip.hashCode())) * 31) + (this.updateTime == null ? 0 : this.updateTime.hashCode())) * 31) + (this.nextTocUrl == null ? 0 : this.nextTocUrl.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TocRule)) {
            return false;
        }
        TocRule tocRule = (TocRule) other;
        return Intrinsics.areEqual(this.preUpdateJs, tocRule.preUpdateJs) && Intrinsics.areEqual(this.chapterList, tocRule.chapterList) && Intrinsics.areEqual(this.chapterName, tocRule.chapterName) && Intrinsics.areEqual(this.chapterUrl, tocRule.chapterUrl) && Intrinsics.areEqual(this.isVolume, tocRule.isVolume) && Intrinsics.areEqual(this.isVip, tocRule.isVip) && Intrinsics.areEqual(this.updateTime, tocRule.updateTime) && Intrinsics.areEqual(this.nextTocUrl, tocRule.nextTocUrl);
    }

    public TocRule() {
        this(null, null, null, null, null, null, null, null, 255, null);
    }

    public TocRule(@Nullable String preUpdateJs, @Nullable String chapterList, @Nullable String chapterName, @Nullable String chapterUrl, @Nullable String isVolume, @Nullable String isVip, @Nullable String updateTime, @Nullable String nextTocUrl) {
        this.preUpdateJs = preUpdateJs;
        this.chapterList = chapterList;
        this.chapterName = chapterName;
        this.chapterUrl = chapterUrl;
        this.isVolume = isVolume;
        this.isVip = isVip;
        this.updateTime = updateTime;
        this.nextTocUrl = nextTocUrl;
    }

    public /* synthetic */ TocRule(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6, (i & 64) != 0 ? null : str7, (i & Wbxml.EXT_T_0) != 0 ? null : str8);
    }

    @Nullable
    public final String getPreUpdateJs() {
        return this.preUpdateJs;
    }

    public final void setPreUpdateJs(@Nullable String str) {
        this.preUpdateJs = str;
    }

    @Nullable
    public final String getChapterList() {
        return this.chapterList;
    }

    public final void setChapterList(@Nullable String str) {
        this.chapterList = str;
    }

    @Nullable
    public final String getChapterName() {
        return this.chapterName;
    }

    public final void setChapterName(@Nullable String str) {
        this.chapterName = str;
    }

    @Nullable
    public final String getChapterUrl() {
        return this.chapterUrl;
    }

    public final void setChapterUrl(@Nullable String str) {
        this.chapterUrl = str;
    }

    @Nullable
    public final String isVolume() {
        return this.isVolume;
    }

    public final void setVolume(@Nullable String str) {
        this.isVolume = str;
    }

    @Nullable
    public final String isVip() {
        return this.isVip;
    }

    public final void setVip(@Nullable String str) {
        this.isVip = str;
    }

    @Nullable
    public final String getUpdateTime() {
        return this.updateTime;
    }

    public final void setUpdateTime(@Nullable String str) {
        this.updateTime = str;
    }

    @Nullable
    public final String getNextTocUrl() {
        return this.nextTocUrl;
    }

    public final void setNextTocUrl(@Nullable String str) {
        this.nextTocUrl = str;
    }
}
