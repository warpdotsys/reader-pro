package io.legado.app.help.http.api;

import io.legado.app.constant.RSSKeywords;
import java.util.Map;
import kotlin.Metadata;
import me.ag2s.epublib.epub.PackageDocumentBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* JADX INFO: compiled from: CookieManager.kt */
/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:io/legado/app/help/http/api/CookieManager.class */
@Metadata(mv = {1, 5, 1}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0004H&J\u0010\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\u0004H&J \u0010\b\u001a\u0004\u0018\u00010\u00042\u0014\u0010\t\u001a\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\nH&J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u0004H&J\u0018\u0010\r\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\u001a\u0010\u000e\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H&¨\u0006\u000f"}, d2 = {"Lio/legado/app/help/http/api/CookieManager;", PackageDocumentBase.PREFIX_OPF, "cookieToMap", PackageDocumentBase.PREFIX_OPF, PackageDocumentBase.PREFIX_OPF, "cookie", "getCookie", RSSKeywords.RSS_ITEM_URL, "mapToCookie", "cookieMap", PackageDocumentBase.PREFIX_OPF, "removeCookie", PackageDocumentBase.PREFIX_OPF, "replaceCookie", "setCookie", "reader-pro"})
public interface CookieManager {
    void setCookie(@NotNull String url, @Nullable String cookie);

    void replaceCookie(@NotNull String url, @NotNull String cookie);

    @NotNull
    String getCookie(@NotNull String url);

    void removeCookie(@NotNull String url);

    @NotNull
    Map<String, String> cookieToMap(@NotNull String cookie);

    @Nullable
    String mapToCookie(@Nullable Map<String, String> cookieMap);
}
