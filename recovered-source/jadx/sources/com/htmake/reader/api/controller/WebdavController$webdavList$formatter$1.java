package com.htmake.reader.api.controller;

import io.legado.app.constant.RSSKeywords;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.StringCompanionObject;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: WebdavController.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/api/controller/WebdavController$webdavList$formatter$1.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0006H\n"}, d2 = {"<anonymous>", PackageDocumentBase.PREFIX_OPF, "f", "Ljava/io/File;", RSSKeywords.RSS_ITEM_URL, "showName", PackageDocumentBase.PREFIX_OPF})
final class WebdavController$webdavList$formatter$1 extends Lambda implements Function3<File, String, Boolean, String> {
    final /* synthetic */ Ref.ObjectRef<String> $fileResponse;
    final /* synthetic */ Ref.ObjectRef<String> $dirResponse;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    WebdavController$webdavList$formatter$1(Ref.ObjectRef<String> $fileResponse, Ref.ObjectRef<String> $dirResponse) {
        super(3);
        this.$fileResponse = $fileResponse;
        this.$dirResponse = $dirResponse;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object p1, Object p2, Object p3) {
        return invoke((File) p1, (String) p2, ((Boolean) p3).booleanValue());
    }

    @NotNull
    public final String invoke(@NotNull File f, @NotNull String url, boolean showName) {
        Intrinsics.checkNotNullParameter(f, "f");
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        String name = showName ? f.getName() : PackageDocumentBase.PREFIX_OPF;
        String modifiedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(f.lastModified()));
        if (f.isFile()) {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String str = (String) this.$fileResponse.element;
            Object[] objArr = {url, modifiedDate, modifiedDate, name, Long.valueOf(f.length()), PackageDocumentBase.PREFIX_OPF};
            String str2 = String.format(str, Arrays.copyOf(objArr, objArr.length));
            Intrinsics.checkNotNullExpressionValue(str2, "java.lang.String.format(format, *args)");
            return str2;
        }
        StringCompanionObject stringCompanionObject2 = StringCompanionObject.INSTANCE;
        Object[] objArr2 = {url, modifiedDate, modifiedDate, name};
        String str3 = String.format((String) this.$dirResponse.element, Arrays.copyOf(objArr2, objArr2.length));
        Intrinsics.checkNotNullExpressionValue(str3, "java.lang.String.format(format, *args)");
        return str3;
    }
}
