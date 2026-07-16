// 
// Decompiled by Procyon v0.6.0
// 

package io.legado.app.constant;

import java.nio.charset.Charset;
import kotlin.io.FilesKt;
import java.io.File;
import kotlin.text.StringsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.Metadata;

@Metadata(mv = { 1, 5, 1 }, k = 1, xi = 48, d1 = { "\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002?\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¡§\u0006\t" }, d2 = { "Lio/legado/app/constant/DeepinkBookSource;", "", "()V", "generate", "", "name", "", "url", "md5", "reader-pro" })
public final class DeepinkBookSource
{
    @NotNull
    public static final DeepinkBookSource INSTANCE;
    
    private DeepinkBookSource() {
    }
    
    public final void generate(@NotNull final String name, @NotNull final String url, @NotNull final String md5) {
        Intrinsics.checkNotNullParameter((Object)name, "name");
        Intrinsics.checkNotNullParameter((Object)url, "url");
        Intrinsics.checkNotNullParameter((Object)md5, "md5");
        final String text = "{\n  \"name\": \"" + name + " by [yuedu.best]\",\n  \"url\": \"" + url + "\",\n  \"version\": 100,\n  \"search\": {\n    \"url\": \"http://api.yuedu.best/yuedu/searchBook@post->{\\\"key\\\":\\\"${key}\\\", \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\",\n    \"charset\": \"utf-8\",\n    \"list\": \"$.[*]\",\n    \"name\": \"$.name\",\n    \"author\": \"$.author\",\n    \"cover\": \"$.coverUrl\",\n    \"summary\": \"$.intro\",\n    \"detail\": \"http://api.yuedu.best/yuedu/getBookInfo@post->{\\\"searchBook\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"detail\": {\n    \"name\": \"$.name\",\n    \"author\": \"$.author\",\n    \"cover\": \"$.coverUrl\",\n    \"summary\": \"$.intro\",\n    \"status\": \"\",\n    \"update\": \"$.latestChapterTime\",\n    \"lastChapter\": \"$.latestChapterTitle\",\n    \"catalog\": \"http://api.yuedu.best/yuedu/getChapterList@post->{\\\"book\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"catalog\": {\n    \"list\": \"$.[*]\",\n    \"name\": \"$.title\",\n    \"chapter\": \"http://api.yuedu.best/yuedu/getContent@post->{\\\"bookChapter\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"chapter\": {\n    \"content\": \"$.text\"\n  }\n}";
        final File file = new File("repo/" + StringsKt.replace$default(StringsKt.replace$default(url, "https://", "", false, 4, (Object)null), "http://", "", false, 4, (Object)null) + ".json");
        System.out.println((Object)Intrinsics.stringPlus("file path: ", (Object)file.getAbsoluteFile()));
        file.createNewFile();
        FilesKt.writeText$default(file, text, (Charset)null, 2, (Object)null);
    }
    
    static {
        INSTANCE = new DeepinkBookSource();
    }
}
