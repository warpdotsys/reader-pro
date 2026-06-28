package io.legado.app.data.entities.rule;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: ContentRule.kt */
/* JADX INFO: loaded from: app-classes.jar:io/legado/app/data/entities/rule/ContentRule.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u001c\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BM\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\tJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u0019\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001b\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u001d\u001a\u0004\u0018\u00010\u0003HÆ\u0003JQ\u0010\u001e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\"\u001a\u00020#HÖ\u0001J\t\u0010$\u001a\u00020\u0003HÖ\u0001R\u001c\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\rR\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u000b\"\u0004\b\u0011\u0010\rR\u001c\u0010\u0007\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\rR\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u000b\"\u0004\b\u0015\u0010\rR\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u000b\"\u0004\b\u0017\u0010\r¨\u0006%"}, d2 = {"Lio/legado/app/data/entities/rule/ContentRule;", PackageDocumentBase.PREFIX_OPF, "content", PackageDocumentBase.PREFIX_OPF, "nextContentUrl", "webJs", "sourceRegex", "replaceRegex", "imageStyle", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getContent", "()Ljava/lang/String;", "setContent", "(Ljava/lang/String;)V", "getImageStyle", "setImageStyle", "getNextContentUrl", "setNextContentUrl", "getReplaceRegex", "setReplaceRegex", "getSourceRegex", "setSourceRegex", "getWebJs", "setWebJs", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", PackageDocumentBase.PREFIX_OPF, "other", "hashCode", PackageDocumentBase.PREFIX_OPF, "toString", "reader-pro"})
public final /* data */ class ContentRule {

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
    public final ContentRule copy(@Nullable String content, @Nullable String nextContentUrl, @Nullable String webJs, @Nullable String sourceRegex, @Nullable String replaceRegex, @Nullable String imageStyle) {
        return new ContentRule(content, nextContentUrl, webJs, sourceRegex, replaceRegex, imageStyle);
    }

    public static /* synthetic */ ContentRule copy$default(ContentRule contentRule, String str, String str2, String str3, String str4, String str5, String str6, int i, Object obj) {
        if ((i & 1) != 0) {
            str = contentRule.content;
        }
        if ((i & 2) != 0) {
            str2 = contentRule.nextContentUrl;
        }
        if ((i & 4) != 0) {
            str3 = contentRule.webJs;
        }
        if ((i & 8) != 0) {
            str4 = contentRule.sourceRegex;
        }
        if ((i & 16) != 0) {
            str5 = contentRule.replaceRegex;
        }
        if ((i & 32) != 0) {
            str6 = contentRule.imageStyle;
        }
        return contentRule.copy(str, str2, str3, str4, str5, str6);
    }

    @NotNull
    public String toString() {
        return "ContentRule(content=" + ((Object) this.content) + ", nextContentUrl=" + ((Object) this.nextContentUrl) + ", webJs=" + ((Object) this.webJs) + ", sourceRegex=" + ((Object) this.sourceRegex) + ", replaceRegex=" + ((Object) this.replaceRegex) + ", imageStyle=" + ((Object) this.imageStyle) + ')';
    }

    public int hashCode() {
        int result = this.content == null ? 0 : this.content.hashCode();
        return (((((((((result * 31) + (this.nextContentUrl == null ? 0 : this.nextContentUrl.hashCode())) * 31) + (this.webJs == null ? 0 : this.webJs.hashCode())) * 31) + (this.sourceRegex == null ? 0 : this.sourceRegex.hashCode())) * 31) + (this.replaceRegex == null ? 0 : this.replaceRegex.hashCode())) * 31) + (this.imageStyle == null ? 0 : this.imageStyle.hashCode());
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ContentRule)) {
            return false;
        }
        ContentRule contentRule = (ContentRule) other;
        return Intrinsics.areEqual(this.content, contentRule.content) && Intrinsics.areEqual(this.nextContentUrl, contentRule.nextContentUrl) && Intrinsics.areEqual(this.webJs, contentRule.webJs) && Intrinsics.areEqual(this.sourceRegex, contentRule.sourceRegex) && Intrinsics.areEqual(this.replaceRegex, contentRule.replaceRegex) && Intrinsics.areEqual(this.imageStyle, contentRule.imageStyle);
    }

    public ContentRule() {
        this(null, null, null, null, null, null, 63, null);
    }

    public ContentRule(@Nullable String content, @Nullable String nextContentUrl, @Nullable String webJs, @Nullable String sourceRegex, @Nullable String replaceRegex, @Nullable String imageStyle) {
        this.content = content;
        this.nextContentUrl = nextContentUrl;
        this.webJs = webJs;
        this.sourceRegex = sourceRegex;
        this.replaceRegex = replaceRegex;
        this.imageStyle = imageStyle;
    }

    public /* synthetic */ ContentRule(String str, String str2, String str3, String str4, String str5, String str6, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : str, (i & 2) != 0 ? null : str2, (i & 4) != 0 ? null : str3, (i & 8) != 0 ? null : str4, (i & 16) != 0 ? null : str5, (i & 32) != 0 ? null : str6);
    }

    @Nullable
    public final String getContent() {
        return this.content;
    }

    public final void setContent(@Nullable String str) {
        this.content = str;
    }

    @Nullable
    public final String getNextContentUrl() {
        return this.nextContentUrl;
    }

    public final void setNextContentUrl(@Nullable String str) {
        this.nextContentUrl = str;
    }

    @Nullable
    public final String getWebJs() {
        return this.webJs;
    }

    public final void setWebJs(@Nullable String str) {
        this.webJs = str;
    }

    @Nullable
    public final String getSourceRegex() {
        return this.sourceRegex;
    }

    public final void setSourceRegex(@Nullable String str) {
        this.sourceRegex = str;
    }

    @Nullable
    public final String getReplaceRegex() {
        return this.replaceRegex;
    }

    public final void setReplaceRegex(@Nullable String str) {
        this.replaceRegex = str;
    }

    @Nullable
    public final String getImageStyle() {
        return this.imageStyle;
    }

    public final void setImageStyle(@Nullable String str) {
        this.imageStyle = str;
    }
}
