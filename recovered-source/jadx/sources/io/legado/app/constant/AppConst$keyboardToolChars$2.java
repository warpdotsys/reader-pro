package io.legado.app.constant;

import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
import me.ag2s.epublib.domain.TableOfContents;
import me.ag2s.epublib.epub.NCXDocumentV2;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: AppConst.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/constant/AppConst$keyboardToolChars$2.class */
@Metadata(mv = {1, 5, 1}, k = 3, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\u0010\u0000\u001a\u0012\u0012\u0004\u0012\u00020\u00020\u0001j\b\u0012\u0004\u0012\u00020\u0002`\u0003H\n"}, d2 = {"<anonymous>", "Ljava/util/ArrayList;", PackageDocumentBase.PREFIX_OPF, "Lkotlin/collections/ArrayList;"})
final class AppConst$keyboardToolChars$2 extends Lambda implements Function0<ArrayList<String>> {
    public static final AppConst$keyboardToolChars$2 INSTANCE = new AppConst$keyboardToolChars$2();

    AppConst$keyboardToolChars$2() {
        super(0);
    }

    @NotNull
    /* JADX INFO: renamed from: invoke, reason: merged with bridge method [inline-methods] */
    public final ArrayList<String> m127invoke() {
        return CollectionsKt.arrayListOf(new String[]{"@", "&", "|", "%", TableOfContents.DEFAULT_PATH_SEPARATOR, ":", "[", "]", "{", "}", "<", ">", "\\", "$", "#", "!", ".", "href", NCXDocumentV2.NCXAttributes.src, "textNodes", "xpath", "json", "css", "id", NCXDocumentV2.NCXAttributes.clazz, "tag"});
    }
}
