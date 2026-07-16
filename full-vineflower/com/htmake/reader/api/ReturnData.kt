package com.htmake.reader.api

public class ReturnData {
   public final var data: Any?
      private set

   public final var errorMsg: String = "未知错误,请联系开发者!"
      private set

   public final var isSuccess: Boolean
      private set

   public fun setErrorMsg(errorMsg: String): ReturnData {
      this.isSuccess = false;
      this.errorMsg = errorMsg;
      return this;
   }

   public fun setData(data: Any, msg: String = ""): ReturnData {
      this.isSuccess = true;
      this.errorMsg = msg;
      this.data = data;
      return this;
   }
}
