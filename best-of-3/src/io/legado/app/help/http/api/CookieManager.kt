package io.legado.app.help.http.api

public interface CookieManager {
   public abstract fun setCookie(url: String, cookie: String?) {
   }

   public abstract fun replaceCookie(url: String, cookie: String) {
   }

   public abstract fun getCookie(url: String): String {
   }

   public abstract fun removeCookie(url: String) {
   }

   public abstract fun cookieToMap(cookie: String): MutableMap<String, String> {
   }

   public abstract fun mapToCookie(cookieMap: Map<String, String>?): String? {
   }
}
