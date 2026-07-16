// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.data.entities;

import kotlin.collections.CollectionsKt;
import java.util.Collection;
import io.legado.app.utils.StringExtensionsKt;
import kotlin.text.StringsKt;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;
import io.legado.app.model.analyzeRule.RuleDataInterface;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0017\n\u0002\u0010 \n\u0000\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00030\u001bH\u0016R\u0018\u0010\u0002\u001a\u00020\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0018\u0010\b\u001a\u00020\u0003X?\u000e?\u0006\f\u001a\u0004\b\t\u0010\u0005\"\u0004\b\n\u0010\u0007R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007R\u001a\u0010\u000e\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u000f\u0010\u0005\"\u0004\b\u0010\u0010\u0007R\u0018\u0010\u0011\u001a\u00020\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0012\u0010\u0005\"\u0004\b\u0013\u0010\u0007R\u001a\u0010\u0014\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0015\u0010\u0005\"\u0004\b\u0016\u0010\u0007R\u001a\u0010\u0017\u001a\u0004\u0018\u00010\u0003X?\u000e?\u0006\f\u001a\u0004\b\u0018\u0010\u0005\"\u0004\b\u0019\u0010\u0007¡§\u0006\u001c" }, d2 = { "Lio/legado/app/data/entities/BaseBook;", "Lio/legado/app/model/analyzeRule/RuleDataInterface;", "author", "", "getAuthor", "()Ljava/lang/String;", "setAuthor", "(Ljava/lang/String;)V", "bookUrl", "getBookUrl", "setBookUrl", "infoHtml", "getInfoHtml", "setInfoHtml", "kind", "getKind", "setKind", "name", "getName", "setName", "tocHtml", "getTocHtml", "setTocHtml", "wordCount", "getWordCount", "setWordCount", "getKindList", "", "reader-pro" })
public interface BaseBook extends RuleDataInterface
{
    @NotNull
    String getName();
    
    void setName(@NotNull final String <set-?>);
    
    @NotNull
    String getAuthor();
    
    void setAuthor(@NotNull final String <set-?>);
    
    @NotNull
    String getBookUrl();
    
    void setBookUrl(@NotNull final String <set-?>);
    
    @Nullable
    String getKind();
    
    void setKind(@Nullable final String <set-?>);
    
    @Nullable
    String getWordCount();
    
    void setWordCount(@Nullable final String <set-?>);
    
    @Nullable
    String getInfoHtml();
    
    void setInfoHtml(@Nullable final String <set-?>);
    
    @Nullable
    String getTocHtml();
    
    void setTocHtml(@Nullable final String <set-?>);
    
    @NotNull
    List<String> getKindList();
    
    @Metadata(mv = { 1, 5, 1 }, k = 3, xi = 48)
    public static final class DefaultImpls
    {
        @NotNull
        public static List<String> getKindList(@NotNull final BaseBook this) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            final ArrayList kindList = new ArrayList();
            final String wordCount = this.getWordCount();
            if (wordCount != null) {
                final String it = wordCount;
                final int n = 0;
                if (!StringsKt.isBlank((CharSequence)it)) {
                    kindList.add(it);
                }
            }
            final String kind = this.getKind();
            if (kind != null) {
                final String it = kind;
                final int n2 = 0;
                final String[] kinds = StringExtensionsKt.splitNotBlank(it, ",", "\n");
                CollectionsKt.addAll((Collection)kindList, (Object[])kinds);
            }
            return kindList;
        }
        
        @Nullable
        public static String getVariable(@NotNull final BaseBook this, @NotNull final String key) {
            Intrinsics.checkNotNullParameter((Object)this, "this");
            Intrinsics.checkNotNullParameter((Object)key, "key");
            return RuleDataInterface.DefaultImpls.getVariable(key);
        }
    }
}
