package io.legado.app.constant;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: DeepinkBookSource.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/constant/DeepinkBookSource.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\u0006¨\u0006\t"}, d2 = {"Lio/legado/app/constant/DeepinkBookSource;", PackageDocumentBase.PREFIX_OPF, "()V", "generate", PackageDocumentBase.PREFIX_OPF, "name", PackageDocumentBase.PREFIX_OPF, RSSKeywords.RSS_ITEM_URL, "md5", "reader-pro"})
public final class DeepinkBookSource {

    @NotNull
    public static final DeepinkBookSource INSTANCE = new DeepinkBookSource();

    private DeepinkBookSource() {
    }

    public final void generate(@NotNull String name, @NotNull String url, @NotNull String md5) throws IOException {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(url, RSSKeywords.RSS_ITEM_URL);
        Intrinsics.checkNotNullParameter(md5, "md5");
        String text = "{\n  \"name\": \"" + name + " by [yuedu.best]\",\n  \"url\": \"" + url + "\",\n  \"version\": 100,\n  \"search\": {\n    \"url\": \"http://api.yuedu.best/yuedu/searchBook@post->{\\\"key\\\":\\\"${key}\\\", \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\",\n    \"charset\": \"utf-8\",\n    \"list\": \"$.[*]\",\n    \"name\": \"$.name\",\n    \"author\": \"$.author\",\n    \"cover\": \"$.coverUrl\",\n    \"summary\": \"$.intro\",\n    \"detail\": \"http://api.yuedu.best/yuedu/getBookInfo@post->{\\\"searchBook\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"detail\": {\n    \"name\": \"$.name\",\n    \"author\": \"$.author\",\n    \"cover\": \"$.coverUrl\",\n    \"summary\": \"$.intro\",\n    \"status\": \"\",\n    \"update\": \"$.latestChapterTime\",\n    \"lastChapter\": \"$.latestChapterTitle\",\n    \"catalog\": \"http://api.yuedu.best/yuedu/getChapterList@post->{\\\"book\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"catalog\": {\n    \"list\": \"$.[*]\",\n    \"name\": \"$.title\",\n    \"chapter\": \"http://api.yuedu.best/yuedu/getContent@post->{\\\"bookChapter\\\":${$}, \\\"bookSourceCode\\\":\\\"" + md5 + "\\\"}\"\n  },\n  \"chapter\": {\n    \"content\": \"$.text\"\n  }\n}";
        File file = new File("repo/" + StringsKt.replace$default(StringsKt.replace$default(url, "https://", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null), "http://", PackageDocumentBase.PREFIX_OPF, false, 4, (Object) null) + ".json");
        System.out.println((Object) Intrinsics.stringPlus("file path: ", file.getAbsoluteFile()));
        file.createNewFile();
        FilesKt.writeText$default(file, text, (Charset) null, 2, (Object) null);
    }
}
