package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.VertExtKt
import io.legado.app.data.entities.Book
import io.legado.app.exception.TocEmptyException
import io.legado.app.model.localBook.LocalBook
import io.legado.app.utils.FileUtils
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.FileUpload
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URLEncoder
import java.util.ArrayList
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.internal.Intrinsics
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class FileController(coroutineContext: CoroutineContext) : BaseController(coroutineContext) {
   public suspend fun checkAccess(context: RoutingContext, isSave: Boolean = ..., isDelete: Boolean = ...): ReturnData? {
      var `$continuation`: Continuation;
      label113: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label113;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
            boolean Z$0;
            boolean Z$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.checkAccess(null, false, false, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var12: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.L$2 = returnData;
            `$continuation`.Z$0 = isSave;
            `$continuation`.Z$1 = isDelete;
            `$continuation`.label = 1;
            var10000 = this.checkAuth(context, `$continuation`);
            if (var10000 === var12) {
               return var12;
            }
            break;
         case 1:
            isDelete = `$continuation`.Z$1;
            isSave = `$continuation`.Z$0;
            returnData = `$continuation`.L$2 as ReturnData;
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as FileController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      if (!var10000 as java.lang.Boolean) {
         return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
      } else {
         context.put("__FILE_HOME__", null);
         val var13: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            if (context.fileUploads() != null && !context.fileUploads().isEmpty()) {
               val var14: java.lang.String = context.request().getParam("home");
               var13 = if (var14 == null) "" else var14;
            } else {
               val var7: java.lang.String = context.getBodyAsJson().getString("home");
               var13 = if (var7 == null) "" else var7;
            }
         } else {
            val userNameSpace: java.util.List = context.queryParam("home");
            val var15: java.lang.String = CollectionsKt.firstOrNull(userNameSpace);
            var13 = if (var15 == null) "" else var15;
         }

         switch (var13.hashCode()) {
            case -1571867763:
               if (!var13.equals("__LOCAL_STORE__")) {
                  return returnData.setErrorMsg("非法访问");
               }

               if (this.getAppConfig().getSecure()) {
                  val var18: User = context.get("userInfo");
                  if (var18 == null) {
                     return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                  }

                  if (!var18.getEnable_local_store()) {
                     return returnData.setErrorMsg("未开启本地书仓功能");
                  }
               }

               if ((isSave || isDelete) && !this.checkManagerAuth(context)) {
                  return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
               }

               context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage", "localStore"), false, 1, null));
               break;
            case -1386618657:
               if (!var13.equals("__HOME__")) {
                  return returnData.setErrorMsg("非法访问");
               }

               context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage", "data", this.getUserNameSpace(context)), false, 1, null));
               break;
            case -1330162107:
               if (!var13.equals("__WEBDAV__")) {
                  return returnData.setErrorMsg("非法访问");
               }

               if (this.getAppConfig().getSecure()) {
                  val userInfox: User = context.get("userInfo");
                  if (userInfox == null) {
                     return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
                  }

                  if (!userInfox.getEnable_webdav()) {
                     return returnData.setErrorMsg("未开启webdav功能");
                  }
               }

               context.put("__FILE_HOME__", ExtKt.toDir$default(this.getUserWebdavHome(context), false, 1, null));
               break;
            case -220135525:
               if (var13.equals("__STORAGE__")) {
                  if (!this.checkManagerAuth(context)) {
                     return ReturnData.setData$default(returnData, "NEED_SECURE_KEY", null, 2, null).setErrorMsg("请输入管理密码");
                  }

                  context.put("__FILE_HOME__", ExtKt.toDir$default(ExtKt.getWorkDir("storage"), false, 1, null));
                  break;
               }

               return returnData.setErrorMsg("非法访问");
            default:
               return returnData.setErrorMsg("非法访问");
         }

         FileControllerKt.access$getLogger$p().info("context.__FILE_HOME__ {}", context.get("__FILE_HOME__"));
         return null;
      }
   }

   public suspend fun list(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label75: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label75;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.list(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var10000 === var21) {
               return var21;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var var22: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val home: java.lang.String = context.getBodyAsJson().getString("path");
            var22 = if (home == null) "" else home;
         } else {
            val file: java.util.List = context.queryParam("path");
            val var24: java.lang.String = CollectionsKt.firstOrNull(file);
            var22 = if (var24 == null) "" else var24;
         }

         if (var22.length() == 0) {
            var22 = "/";
         }

         val var27: Any = context.get("__FILE_HOME__");
         if (var27 == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            var22 = ExtKt.toDir(var22, true);
            val var29: File = new File(Intrinsics.stringPlus(var27 as java.lang.String, var22));
            FileControllerKt.access$getLogger$p().info("file: {} {}", var22, var29);
            if (!var29.exists()) {
               if (!(var22 == "/")) {
                  return returnData.setErrorMsg("路径不存在");
               }

               var29.mkdirs();
            }

            if (!var29.isDirectory()) {
               return returnData.setErrorMsg("路径不是目录");
            } else {
               val var30: ArrayList = new ArrayList();
               val var31: Array<File> = var29.listFiles();
               val var11: Array<Any> = var31;
               val var12: Int = var31.length;

               for (int var13 = 0; var13 < var12; var13++) {
                  val it: File = var11[var13] as File;
                  val var17: java.lang.String = (var11[var13] as File).getName();
                  if (!StringsKt.startsWith$default(var17, ".", false, 2, null)) {
                     val var33: Array<Pair> = new Pair[]{TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length())), null, null, null};
                     val var18: java.lang.String = it.toString();
                     var33[2] = TuplesKt.to("path", StringsKt.replace$default(var18, var27 as java.lang.String, "", false, 4, null));
                     var33[3] = TuplesKt.to("lastModified", Boxing.boxLong(it.lastModified()));
                     var33[4] = TuplesKt.to("isDirectory", Boxing.boxBoolean(it.isDirectory()));
                     var30.add(MapsKt.mapOf(var33));
                  }
               }

               return ReturnData.setData$default(returnData, var30, null, 2, null);
            }
         }
      }
   }

   public suspend fun upload(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label74: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label74;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.upload(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var21: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var returnData: ReturnData;
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            returnData = new ReturnData();
            if (context.fileUploads() == null || context.fileUploads().isEmpty()) {
               return returnData.setErrorMsg("请上传文件");
            }

            `$continuation`.L$0 = context;
            `$continuation`.L$1 = returnData;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, true, false, `$continuation`, 4, null);
            if (var10000 === var21) {
               return var21;
            }
            break;
         case 1:
            returnData = `$continuation`.L$1 as ReturnData;
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         var var22: java.lang.String = context.request().getParam("path");
         if (var22 == null || var22.length() == 0) {
            var22 = "/";
         }

         var22 = ExtKt.toDir(var22, true);
         val var25: ArrayList = new ArrayList();
         val var28: Any = context.get("__FILE_HOME__");
         if (var28 == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var30: java.lang.Iterable;
            for (Object element$iv : var30) {
               val it: FileUpload = `element$iv` as FileUpload;
               val file: File = new File((`element$iv` as FileUpload).uploadedFileName());
               FileControllerKt.access$getLogger$p()
                  .info("uploadFile: {} {} {}", new Object[]{(`element$iv` as FileUpload).uploadedFileName(), (`element$iv` as FileUpload).fileName(), file});
               if (file.exists()) {
                  val newFile: File = new File("$var28$var22${File.separator}${it.fileName()}");
                  if (!newFile.getParentFile().exists()) {
                     newFile.getParentFile().mkdirs();
                  }

                  if (newFile.exists()) {
                     newFile.delete();
                  }

                  FileControllerKt.access$getLogger$p().info("moveTo: {}", newFile);
                  if (FilesKt.copyRecursively$default(file, newFile, false, null, 6, null)) {
                     val var17: Array<Pair> = new Pair[]{
                        TuplesKt.to("name", newFile.getName()), TuplesKt.to("size", Boxing.boxLong(newFile.length())), null, null, null
                     };
                     val var18: java.lang.String = newFile.toString();
                     var17[2] = TuplesKt.to("path", StringsKt.replace$default(var18, var28 as java.lang.String, "", false, 4, null));
                     var17[3] = TuplesKt.to("lastModified", Boxing.boxLong(newFile.lastModified()));
                     var17[4] = TuplesKt.to("isDirectory", Boxing.boxBoolean(newFile.isDirectory()));
                     var25.add(MapsKt.mapOf(var17));
                  }

                  FilesKt.deleteRecursively(file);
               }
            }

            return ReturnData.setData$default(returnData, var25, null, 2, null);
         }
      }
   }

   public suspend fun download(context: RoutingContext) {
      var `$continuation`: Continuation;
      label68: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label68;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.download(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var10000 === var13) {
               return var13;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         VertExtKt.success(context, checkResult);
         return Unit.INSTANCE;
      } else {
         val returnData: ReturnData = new ReturnData();
         var var14: java.lang.String;
         val var16: Int;
         if (context.request().method() === HttpMethod.POST) {
            val home: java.lang.String = context.getBodyAsJson().getString("path");
            var14 = if (home == null) "" else home;
            val var17: Int = context.getBodyAsJson().getInteger("stream", Boxing.boxInt(0));
            var16 = var17.intValue();
         } else {
            var file: java.util.List = context.queryParam("path");
            var var18: java.lang.String = CollectionsKt.firstOrNull(file);
            var14 = if (var18 == null) "" else var18;
            file = context.queryParam("stream");
            var18 = CollectionsKt.firstOrNull(file);
            val var26: Int;
            if (var18 == null) {
               var26 = 0;
            } else {
               val var23: Int = Boxing.boxInt(Integer.parseInt(var18));
               var26 = if (var23 == null) 0 else var23;
            }

            var16 = var26;
         }

         if (var14.length() == 0) {
            VertExtKt.success(context, returnData.setErrorMsg("参数错误"));
            return Unit.INSTANCE;
         } else {
            val var21: java.lang.String = context.get("__FILE_HOME__");
            if (var21 == null) {
               VertExtKt.success(context, returnData.setErrorMsg("参数错误"));
               return Unit.INSTANCE;
            } else {
               var14 = ExtKt.toDir(var14, true);
               val var25: File = new File(Intrinsics.stringPlus(var21, var14));
               FileControllerKt.access$getLogger$p().info("file: {} {}", var14, var25);
               if (!var25.exists()) {
                  VertExtKt.success(context, returnData.setErrorMsg("路径不存在"));
                  return Unit.INSTANCE;
               } else {
                  val response: HttpServerResponse = context.response().putHeader("Cache-Control", "86400");
                  if (var16 <= 0) {
                     response.putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var25.getName(), "UTF-8")));
                  }

                  response.sendFile(var25.toString());
                  return Unit.INSTANCE;
               }
            }
         }
      }
   }

   public suspend fun get(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label56: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label56;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.get(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var var11: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val home: java.lang.String = context.getBodyAsJson().getString("path");
            var11 = if (home == null) "" else home;
         } else {
            val file: java.util.List = context.queryParam("path");
            val var13: java.lang.String = CollectionsKt.firstOrNull(file);
            var11 = if (var13 == null) "" else var13;
         }

         if (var11.length() == 0) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var15: java.lang.String = context.get("__FILE_HOME__");
            if (var15 == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               var11 = ExtKt.toDir(var11, true);
               val var17: File = new File(Intrinsics.stringPlus(var15, var11));
               FileControllerKt.access$getLogger$p().info("file: {} {}", var11, var17);
               return if (!var17.exists())
                  returnData.setErrorMsg("路径不存在")
                  else
                  ReturnData.setData$default(returnData, FilesKt.readText$default(var17, null, 1, null), null, 2, null);
            }
         }
      }
   }

   public suspend fun save(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label47: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label47;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.save(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, true, false, `$continuation`, 4, null);
            if (var10000 === var11) {
               return var11;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var content: java.lang.String = context.getBodyAsJson().getString("path", "");
         var path: java.lang.String = if (content == null) "" else content;
         var home: java.lang.String = context.getBodyAsJson().getString("content", "");
         content = if (home == null) "" else home;
         if (path.length() == 0) {
            return returnData.setErrorMsg("参数错误");
         } else {
            home = context.get("__FILE_HOME__");
            if (home == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               path = ExtKt.toDir(path, true);
               val var16: File = FileUtils.INSTANCE.createFileWithReplace(Intrinsics.stringPlus(home, path));
               FileControllerKt.access$getLogger$p().info("file: {} {}", path, var16);
               FilesKt.writeText$default(var16, content, null, 2, null);
               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }
   }

   public suspend fun mkdir(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label62: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label62;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.mkdir(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var11: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, true, false, `$continuation`, 4, null);
            if (var10000 === var11) {
               return var11;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var name: java.lang.String = context.getBodyAsJson().getString("path", "");
         var path: java.lang.String = if (name == null) "" else name;
         if ((if (name == null) "" else name).length() == 0) {
            return returnData.setErrorMsg("参数错误");
         } else {
            var var15: java.lang.String = context.getBodyAsJson().getString("name", "");
            name = if (var15 == null) "" else var15;
            if ((if (var15 == null) "" else var15).length() != 0 && !StringsKt.startsWith$default(if (var15 == null) "" else var15, ".", false, 2, null)) {
               var15 = context.get("__FILE_HOME__");
               if (var15 == null) {
                  return returnData.setErrorMsg("参数错误");
               } else {
                  path = ExtKt.toDir(path, true);
                  val var18: File = new File("$var15$path${File.separator}$name");
                  FileControllerKt.access$getLogger$p().info("file: {} {}", path, var18);
                  if (var18.exists()) {
                     return returnData.setErrorMsg("路径已存在");
                  } else {
                     var18.mkdirs();
                     return ReturnData.setData$default(returnData, "", null, 2, null);
                  }
               }
            } else {
               return returnData.setErrorMsg("参数错误");
            }
         }
      }
   }

   public suspend fun delete(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label56: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label56;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.delete(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var10: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = this.checkAccess(context, false, true, `$continuation`);
            if (var10000 === var10) {
               return var10;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var var11: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val home: java.lang.String = context.getBodyAsJson().getString("path");
            var11 = if (home == null) "" else home;
         } else {
            val file: java.util.List = context.queryParam("path");
            val var13: java.lang.String = CollectionsKt.firstOrNull(file);
            var11 = if (var13 == null) "" else var13;
         }

         if (var11.length() == 0) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var15: java.lang.String = context.get("__FILE_HOME__");
            if (var15 == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               var11 = ExtKt.toDir(var11, true);
               val var17: File = new File(Intrinsics.stringPlus(var15, var11));
               FileControllerKt.access$getLogger$p().info("file: {} {}", var11, var17);
               if (!var17.exists()) {
                  return returnData.setErrorMsg("路径不存在");
               } else {
                  FilesKt.deleteRecursively(var17);
                  return ReturnData.setData$default(returnData, "", null, 2, null);
               }
            }
         }
      }
   }

   public suspend fun deleteMulti(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label53: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label53;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.deleteMulti(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var18: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = context;
            `$continuation`.label = 1;
            var10000 = this.checkAccess(context, false, true, `$continuation`);
            if (var10000 === var18) {
               return var18;
            }
            break;
         case 1:
            context = `$continuation`.L$0 as RoutingContext;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         if (context.getBodyAsJson().getJsonArray("path") == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var19: Any = context.get("__FILE_HOME__");
            if (var19 == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               val `$this$forEach$iv`: java.lang.Iterable;
               for (Object element$iv : $this$forEach$iv) {
                  val filePath: java.lang.String = if (`element$iv` as java.lang.String == null) "" else `element$iv` as java.lang.String;
                  if ((if (`element$iv` as java.lang.String == null) "" else `element$iv` as java.lang.String).length() > 0) {
                     FilesKt.deleteRecursively(new File(Intrinsics.stringPlus(var19 as java.lang.String, ExtKt.toDir(filePath, true))));
                  }
               }

               return ReturnData.setData$default(returnData, "", null, 2, null);
            }
         }
      }
   }

   public suspend fun importPreview(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label84: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label84;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.importPreview(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var28: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.label = 1;
            var10000 = checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var10000 === var28) {
               return var28;
            }
            break;
         case 1:
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as FileController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = `$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         if (context.getBodyAsJson().getJsonArray("path") == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            val var30: Any = context.get("__FILE_HOME__");
            if (var30 == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               val var31: ArrayList = new ArrayList();
               val var33: java.lang.String = this.getUserNameSpace(context);

               val `$this$forEach$iv`: java.lang.Iterable;
               for (Object element$iv : $this$forEach$iv) {
                  var path: java.lang.String = if (`element$iv` as java.lang.String == null) "" else `element$iv` as java.lang.String;
                  if ((if (`element$iv` as java.lang.String == null) "" else `element$iv` as java.lang.String).length() > 0) {
                     path = Intrinsics.stringPlus(var30 as java.lang.String, path);
                     val var35: File = new File(path);
                     FileControllerKt.access$getLogger$p().info("localFile: {} {}", path, var35);
                     if (var35.exists() && !var35.isDirectory()) {
                        val var37: java.lang.String = var35.getName();
                        var10000 = this;
                        val ext: java.lang.String = BaseController.getFileExt$default((BaseController)var10000, var37, null, 2, null);
                        if (!(ext == "txt") && !(ext == "epub") && !(ext == "umd") && !(ext == "cbz") && !(ext == "pdf")) {
                           return returnData.setErrorMsg("不支持导入$ext格式的书籍文件");
                        }

                        var rootDir: java.lang.String = ExtKt.getWorkDir$default(null, 1, null);
                        var relativePath: java.lang.String = File.separator;
                        if (!StringsKt.endsWith$default(rootDir, relativePath, false, 2, null)) {
                           rootDir = Intrinsics.stringPlus(rootDir, File.separator);
                        }

                        relativePath = path;
                        FileControllerKt.access$getLogger$p().info("rootDir: {} path: {}", rootDir, path);
                        if (StringsKt.startsWith$default(path, rootDir, false, 2, null)) {
                           relativePath = StringsKt.replaceFirst$default(path, rootDir, "", false, 4, null);
                        }

                        FileControllerKt.access$getLogger$p().info("relative path: {}", relativePath);
                        val book: Book = Book.Companion
                           .initLocalBook(StringsKt.replace$default(relativePath, "\\", "/", false, 4, null), relativePath, rootDir);
                        book.setUserNameSpace(var33);
                        FileControllerKt.access$getLogger$p().info("book {}", book);

                        try {
                           var31.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", LocalBook.INSTANCE.getChapterList(book))}));
                        } catch (var29: TocEmptyException) {
                           var31.add(MapsKt.mapOf(new Pair[]{TuplesKt.to("book", book), TuplesKt.to("chapters", new ArrayList())}));
                        }
                     }
                  }
               }

               return ReturnData.setData$default(returnData, var31, null, 2, null);
            }
         }
      }
   }

   public suspend fun restore(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label74: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label74;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.restore(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var13: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var20: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.label = 1;
            var20 = checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var20 === var13) {
               return var13;
            }
            break;
         case 1:
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as FileController;
            ResultKt.throwOnFailure(`$result`);
            var20 = `$result`;
            break;
         case 2:
            val returnData: ReturnData = `$continuation`.L$0 as ReturnData;
            ResultKt.throwOnFailure(`$result`);
            return if (!`$result` as java.lang.Boolean) returnData.setErrorMsg("恢复失败") else ReturnData.setData$default(returnData, "", null, 2, null);
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var20 as ReturnData;
      if (var20 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         var var14: java.lang.String;
         if (context.request().method() === HttpMethod.POST) {
            val ext: java.lang.String = context.getBodyAsJson().getString("path");
            var14 = if (ext == null) "" else ext;
         } else {
            val home: java.util.List = context.queryParam("path");
            val var15: java.lang.String = CollectionsKt.firstOrNull(home);
            var14 = if (var15 == null) "" else var15;
         }

         if (var14.length() == 0) {
            var14 = "/";
         }

         if (!(BaseController.getFileExt$default(this, var14, null, 2, null) == "zip")) {
            return returnData.setErrorMsg("路径不是zip备份文件");
         } else {
            val var19: java.lang.String = context.get("__FILE_HOME__");
            if (var19 == null) {
               return returnData.setErrorMsg("参数错误");
            } else {
               val file: File = new File(Intrinsics.stringPlus(var19, var14));
               FileControllerKt.access$getLogger$p().info("file: {} {}", var14, file);
               if (!file.exists()) {
                  return returnData.setErrorMsg("路径不存在");
               } else {
                  val bookController: BookController = new BookController(this.getCoroutineContext());
                  val var10: java.lang.String = file.toString();
                  val var10002: java.lang.String = this.getUserNameSpace(context);
                  `$continuation`.L$0 = returnData;
                  `$continuation`.L$1 = null;
                  `$continuation`.label = 2;
                  var20 = bookController.syncFromWebdav(var10, var10002, `$continuation`);
                  if (var20 === var13) {
                     return var13;
                  } else {
                     return if (!var20 as java.lang.Boolean) returnData.setErrorMsg("恢复失败") else ReturnData.setData$default(returnData, "", null, 2, null);
                  }
               }
            }
         }
      }
   }

   public suspend fun parse(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label117: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label117;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            int label;

            {
               super(`$completion`);
               this.this$0 = `this$0`;
            }

            @Nullable
            @Override
            public final Object invokeSuspend(@NotNull Object $result) {
               this.result = `$result`;
               this.label |= Integer.MIN_VALUE;
               return this.this$0.parse(null, this);
            }
         };
      }

      val `$result`: Any = `$continuation`.result;
      val var28: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
      var var10000: Any;
      switch ($continuation.label) {
         case 0:
            ResultKt.throwOnFailure(`$result`);
            `$continuation`.L$0 = this;
            `$continuation`.L$1 = context;
            `$continuation`.label = 1;
            var10000 = (Book.Companion)checkAccess$default(this, context, false, false, `$continuation`, 6, null);
            if (var10000 === var28) {
               return var28;
            }
            break;
         case 1:
            context = `$continuation`.L$1 as RoutingContext;
            this = `$continuation`.L$0 as FileController;
            ResultKt.throwOnFailure(`$result`);
            var10000 = (Book.Companion)`$result`;
            break;
         default:
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
      }

      val checkResult: ReturnData = var10000 as ReturnData;
      if (var10000 as ReturnData != null) {
         return checkResult;
      } else {
         val returnData: ReturnData = new ReturnData();
         val var29: Any = context.get("__FILE_HOME__");
         if (var29 == null) {
            return returnData.setErrorMsg("参数错误");
         } else {
            var var30: java.lang.String;
            val var31: Int;
            if (context.request().method() === HttpMethod.POST) {
               val file: java.lang.String = context.getBodyAsJson().getString("path");
               var30 = if (file == null) "" else file;
               val var32: Int = context.getBodyAsJson().getInteger("import", Boxing.boxInt(0));
               var31 = var32.intValue();
            } else {
               var fileList: java.util.List = context.queryParam("path");
               var var33: java.lang.String = CollectionsKt.firstOrNull(fileList);
               var30 = if (var33 == null) "" else var33;
               fileList = context.queryParam("import");
               var33 = CollectionsKt.firstOrNull(fileList);
               val var49: Int;
               if (var33 == null) {
                  var49 = 0;
               } else {
                  val var38: Int = Boxing.boxInt(Integer.parseInt(var33));
                  var49 = if (var38 == null) 0 else var38;
               }

               var31 = var49;
            }

            if (var30.length() == 0) {
               var30 = "/";
            }

            val var36: File = new File(Intrinsics.stringPlus(var29 as java.lang.String, var30));
            FileControllerKt.access$getLogger$p().info("file: {} {}", var30, var36);
            if (!var36.exists()) {
               return returnData.setErrorMsg("路径不存在");
            } else if (!var36.isDirectory()) {
               return returnData.setErrorMsg("路径不是目录");
            } else {
               val var41: ArrayList = new ArrayList();
               val var43: java.lang.String = this.getUserNameSpace(context);
               var var45: java.lang.String = ExtKt.getWorkDir$default(null, 1, null);
               val bookController: java.lang.String = File.separator;
               if (!StringsKt.endsWith$default(var45, bookController, false, 2, null)) {
                  var45 = Intrinsics.stringPlus(var45, File.separator);
               }

               val var46: BookController = new BookController(this.getCoroutineContext());

               val `$this$forEach$iv`: java.lang.Iterable;
               for (Object element$iv : $this$forEach$iv) {
                  val it: File = `element$iv` as File;
                  var fileName: java.lang.String = (`element$iv` as File).getName();
                  if (!StringsKt.startsWith$default(fileName, ".", false, 2, null) && it.isFile()) {
                     fileName = it.getName();
                     var10000 = this;
                     val ext: java.lang.String = BaseController.getFileExt$default((BaseController)var10000, fileName, null, 2, null);
                     switch (ext.hashCode()) {
                        case 98299:
                           if (!ext.equals("cbz")) {
                              continue;
                           }
                           break;
                        case 110834:
                           if (!ext.equals("pdf")) {
                              continue;
                           }
                           break;
                        case 115312:
                           if (!ext.equals("txt")) {
                              continue;
                           }
                           break;
                        case 115916:
                           if (!ext.equals("umd")) {
                              continue;
                           }
                           break;
                        case 3120248:
                           if (!ext.equals("epub")) {
                              continue;
                           }
                           break;
                        default:
                           continue;
                     }

                     var relativePath: java.lang.String = it.getPath();
                     FileControllerKt.access$getLogger$p().debug("rootDir: {} path: {}", var45, var30);
                     if (StringsKt.startsWith$default(relativePath, var45, false, 2, null)) {
                        relativePath = StringsKt.replaceFirst$default(relativePath, var45, "", false, 4, null);
                     }

                     FileControllerKt.access$getLogger$p().debug("relative path: {}", relativePath);
                     val url: java.lang.String = StringsKt.replace$default(relativePath, "\\", "/", false, 4, null);
                     var10000 = Book.Companion;
                     val book: Book = var10000.initLocalBook(url, relativePath, var45);
                     book.setUserNameSpace(var43);
                     FileControllerKt.access$getLogger$p().debug("book {}", book);
                     if (var31 > 0) {
                        val result: Pair = var46.saveBookToShelf(book, var43, context);
                        if (result.getSecond() == null && (result.getFirst() as Book).isInShelf()) {
                           var41.add(MapsKt.mapOf(TuplesKt.to("name", it.getName())));
                        }
                     } else {
                        val var48: Array<Pair> = new Pair[]{
                           TuplesKt.to("name", it.getName()), TuplesKt.to("size", Boxing.boxLong(it.length())), null, null, null
                        };
                        val var25: java.lang.String = it.toString();
                        var48[2] = TuplesKt.to("path", StringsKt.replaceFirst$default(var25, var29 as java.lang.String, "", false, 4, null));
                        var48[3] = TuplesKt.to("lastModified", Boxing.boxLong(it.lastModified()));
                        var48[4] = TuplesKt.to("book", book);
                        var41.add(MapsKt.mapOf(var48));
                     }
                  }
               }

               return ReturnData.setData$default(returnData, var41, null, 2, null);
            }
         }
      }
   }
}
