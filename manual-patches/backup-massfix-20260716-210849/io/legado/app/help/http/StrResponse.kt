package io.legado.app.help.http

import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.Response.Builder

public class StrResponse {
   public final var body: String?
      private set

   public final var errorBody: ResponseBody?
      private set

   public final var raw: Response
      private set

   public final val url: String
      public final get() {
         return this.url();
      }


   public constructor(rawResponse: Response, body: String?)  {
      this.raw = rawResponse;
      this.body = body;
   }

   public constructor(url: String, body: String?)  {
      this.raw = new Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new okhttp3.Request.Builder().url(url).build()).build();
      this.body = body;
   }

   public constructor(rawResponse: Response, errorBody: ResponseBody?)  {
      this.raw = rawResponse;
      this.errorBody = errorBody;
   }

   public fun raw(): Response {
      return this.raw;
   }

   public fun url(): String {
      val var1: Response = this.raw.networkResponse();
      return if (var1 == null) this.raw.request().url().toString() else var1.request().url().toString();
   }

   public fun body(): String? {
      return this.body;
   }

   public fun code(): Int {
      return this.raw.code();
   }

   public fun message(): String {
      return this.raw.message();
   }

   public fun headers(): Headers {
      return this.raw.headers();
   }

   public fun isSuccessful(): Boolean {
      return this.raw.isSuccessful();
   }

   public fun errorBody(): ResponseBody? {
      return this.errorBody;
   }

   public override fun toString(): String {
      return this.raw.toString();
   }
}
