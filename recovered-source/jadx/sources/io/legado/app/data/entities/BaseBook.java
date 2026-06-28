package io.legado.app.data.entities;

import io.legado.app.model.analyzeRule.RuleDataInterface;
import io.legado.app.utils.StringExtensionsKt;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: BaseBook.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BaseBook.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010 \n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\u001bH\u0016R\u0018\u0010\u0002\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\t\u0010\u0005\"\u0004\b\n\u0010\u0007R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u0018\u0010\u0011\u001a\u00020\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007R\u001a\u0010\u0017\u001a\u0004\u0018\u00010\u0003X¦\u000e¢\u0006\f\u001a\u0004\b\u0018\u0010\u0005\"\u0004\b\u0019\u0010\u0007¨\u0006\u001c"}, d2 = {"Lio/legado/app/data/entities/BaseBook;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "author", PackageDocumentBase.PREFIX_OPF, "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "bookUrl", "getBookUrl", "setBookUrl", "infoHtml", "getInfoHtml", "setInfoHtml", "kind", "getKind", "setKind", "name", "getName", "setName", "tocHtml", "getTocHtml", "setTocHtml", "wordCount", "getWordCount", "setWordCount", "getKindList", PackageDocumentBase.PREFIX_OPF, "reader-pro"})
public interface BaseBook extends RuleDataInterface {
    @NotNull
    String getName();

    void setName(@NotNull String str);

    @NotNull
    String getAuthor();

    void setAuthor(@NotNull String str);

    @NotNull
    String getBookUrl();

    void setBookUrl(@NotNull String str);

    @Nullable
    String getKind();

    void setKind(@Nullable String str);

    @Nullable
    String getWordCount();

    void setWordCount(@Nullable String str);

    @Nullable
    String getInfoHtml();

    void setInfoHtml(@Nullable String str);

    @Nullable
    String getTocHtml();

    void setTocHtml(@Nullable String str);

    @NotNull
    List<String> getKindList();

    /* JADX INFO: compiled from: BaseBook.kt */
    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/data/entities/BaseBook$DefaultImpls.class */
    @Metadata(mv = {1, 5, 1}, k = 3, xi = 48)
    public static final class DefaultImpls {
        @Nullable
        public static String getVariable(@NotNull BaseBook baseBook, @NotNull String key) {
            Intrinsics.checkNotNullParameter(baseBook, "this");
            Intrinsics.checkNotNullParameter(key, "key");
            return RuleDataInterface.DefaultImpls.getVariable(baseBook, key);
        }

        @NotNull
        public static List<String> getKindList(@NotNull BaseBook baseBook) {
            Intrinsics.checkNotNullParameter(baseBook, "this");
            ArrayList kindList = new ArrayList();
            String it = baseBook.getWordCount();
            if (it != null) {
                if (!StringsKt.isBlank(it)) {
                    kindList.add(it);
                }
            }
            String it2 = baseBook.getKind();
            if (it2 != null) {
                String[] kinds = StringExtensionsKt.splitNotBlank(it2, ",", "\n");
                CollectionsKt.addAll(kindList, kinds);
            }
            return kindList;
        }
    }
}
