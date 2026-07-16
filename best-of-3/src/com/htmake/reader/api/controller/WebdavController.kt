package com.htmake.reader.api.controller

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.htmake.reader.api.ReturnData
import com.htmake.reader.entity.User
import com.htmake.reader.utils.ExtKt
import com.htmake.reader.utils.VertExtKt
import io.legado.app.utils.EncoderUtils
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.io.File
import java.net.URL
import java.net.URLDecoder
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.LinkedHashMap
import java.util.UUID
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.IntrinsicsKt
import kotlin.coroutines.jvm.internal.Boxing
import kotlin.coroutines.jvm.internal.ContinuationImpl
import kotlin.jvm.functions.Function2
import kotlin.jvm.functions.Function3
import kotlin.jvm.internal.Intrinsics
import kotlin.jvm.internal.StringCompanionObject
import kotlin.jvm.internal.TypeIntrinsics
import kotlin.jvm.internal.Ref.ObjectRef
import kotlinx.coroutines.BuildersKt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.slf4j.MDCContext
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

public class WebdavController(coroutineContext: CoroutineContext, router: Router, onHandlerError: (RoutingContext, Exception) -> Unit) : BaseController(
      coroutineContext
   ) {
   init {
      val var4: Route = router.route("/reader3/webdav*");
      VertExtKt.globalHandler(var4, WebdavController::_init_$lambda-1);
   }

   public fun checkAuthorization(context: RoutingContext): Boolean {
      if (!this.getAppConfig().getSecure()) {
         return true;
      } else {
         val authorization: java.lang.String = context.request().getHeader("Authorization");
         WebdavControllerKt.access$getLogger$p().info("authorization: {}", authorization);
         if (authorization != null && authorization.length() != 0) {
            val var15: java.util.List = StringsKt.split$default(
               EncoderUtils.base64Decode$default(EncoderUtils.INSTANCE, StringsKt.replace(authorization, "Basic ", "", true), 0, 2, null),
               new java.lang.String[]{":"},
               false,
               2,
               2,
               null
            );
            if (var15.size() < 2) {
               return false;
            } else {
               val var17: java.lang.String = var15.get(0) as java.lang.String;
               val password: java.lang.String = var15.get(1) as java.lang.String;
               var userMap: java.util.Map = new LinkedHashMap();
               val var18: JsonObject = ExtKt.asJsonObject(ExtKt.getStorage$default(new java.lang.String[]{"data", "users"}, null, 2, null));
               if (var18 != null) {
                  val var19: java.util.Map = var18.getMap();
                  if (var19 == null) {
                     throw new NullPointerException(
                        "null cannot be cast to non-null type kotlin.collections.MutableMap<kotlin.String, kotlin.collections.Map<kotlin.String, kotlin.Any>>"
                     );
                  }

                  userMap = TypeIntrinsics.asMutableMap(var19);
               }

               if (userMap == null) {
                  throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
               } else {
                  val var20: java.util.Map = userMap.getOrDefault(var17, null) as java.util.Map;
                  if (var20 == null) {
                     return false;
                  } else {
                     val userInfo: User = ExtKt.getGson()
                        .fromJson(
                           if (var20 is java.lang.String) var20 as java.lang.String else ExtKt.getGson().toJson(var20),
                           new WebdavController$checkAuthorization$$inlined$toDataClass$1().getType()
                        );
                     if (userInfo == null) {
                        return false;
                     } else if (!(ExtKt.genEncryptedPassword(password, userInfo.getSalt()) == userInfo.getPassword())) {
                        WebdavControllerKt.access$getLogger$p().info("user: {} password error", userInfo.getUsername());
                        return false;
                     } else if (!userInfo.getEnable_webdav()) {
                        WebdavControllerKt.access$getLogger$p().info("user: {} enable_webdav: false", userInfo.getUsername());
                        return false;
                     } else {
                        context.put("username", userInfo.getUsername());
                        return true;
                     }
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   public suspend fun webdavList(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var22: File = new File(Intrinsics.stringPlus(home, file));
      if (!var22.exists()) {
         context.response().setStatusCode(404).end();
         return Unit.INSTANCE;
      } else {
         val dirResponse: ObjectRef = new ObjectRef();
         dirResponse.element = (T)"<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype>\n                            <D:collection />\n                        </D:resourcetype>\n                        <D:displayname>%s</D:displayname>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
         val fileResponse: ObjectRef = new ObjectRef();
         fileResponse.element = (T)"<D:response>\n                <D:href>%s</D:href>\n                <D:propstat>\n                    <D:status>HTTP/1.1 200 OK</D:status>\n                    <D:prop>\n                        <D:getlastmodified>%s</D:getlastmodified>\n                        <D:creationdate>%s</D:creationdate>\n                        <D:resourcetype />\n                        <D:displayname>%s</D:displayname>\n                        <D:getcontentlength>%s</D:getcontentlength>\n                        <D:getcontenttype>%s</D:getcontenttype>\n                    </D:prop>\n                </D:propstat>\n            </D:response>\n        ";
         var var23: java.lang.String = context.request().absoluteURI();
         val var25: Function3 = new Function3<File, java.lang.String, java.lang.Boolean, java.lang.String>(fileResponse, dirResponse) {
            {
               super(3);
               this.$fileResponse = `$fileResponse`;
               this.$dirResponse = `$dirResponse`;
            }

            @NotNull
            public final java.lang.String invoke(@NotNull File f, @NotNull java.lang.String url, boolean showName) {
               val name: java.lang.String = if (showName) f.getName() else "";
               val modifiedDate: java.lang.String = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(f.lastModified());
               val var10000: java.lang.String;
               if (f.isFile()) {
                  val var6: StringCompanionObject = StringCompanionObject.INSTANCE;
                  val var7: java.lang.String = this.$fileResponse.element;
                  val var8: Array<Any> = new Object[]{url, modifiedDate, modifiedDate, name, f.length(), ""};
                  var10000 = java.lang.String.format(var7, Arrays.copyOf(var8, var8.length));
               } else {
                  val var10: StringCompanionObject = StringCompanionObject.INSTANCE;
                  val var11: java.lang.String = this.$dirResponse.element;
                  val var12: Array<Any> = new Object[]{url, modifiedDate, modifiedDate, name};
                  var10000 = java.lang.String.format(var11, Arrays.copyOf(var12, var12.length));
               }

               return var10000;
            }
         };
         if (var22.isFile()) {
            val var32: StringCompanionObject = StringCompanionObject.INSTANCE;
            val var34: Array<Any> = new Object[1];
            val var10002: Function3 = var25;
            var34[0] = var10002.invoke(var22, var23, Boxing.boxBoolean(true));
            val var39: java.lang.String = java.lang.String.format(
               "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n            <D:multistatus xmlns:D=\"DAV:\">\n                %s\n            </D:multistatus>\n        ",
               Arrays.copyOf(var34, var34.length)
            );
            context.response().setStatusCode(207).end(var39);
            return Unit.INSTANCE;
         } else if (!var22.isDirectory()) {
            context.response().setStatusCode(404).end();
            return Unit.INSTANCE;
         } else {
            var23 = if (StringsKt.endsWith$default(var23, "/", false, 2, null)) var23 else Intrinsics.stringPlus(var23, "/");
            val var10000: Function3 = var25;
            var var27: Any = var10000.invoke(var22, var23, Boxing.boxBoolean(false));
            val `$this$forEach$iv`: Array<File> = var22.listFiles();
            val var14: Array<Any> = `$this$forEach$iv`;
            val var15: Int = `$this$forEach$iv`.length;

            for (int var16 = 0; var16 < var15; var16++) {
               val it: File = var14[var16] as File;
               val fileName: java.lang.String = URLEncoder.encode((var14[var16] as File).getName(), "UTF-8");
               val var37: java.lang.String = var27 as java.lang.String;
               val var10001: Function3 = var25;
               var27 = Intrinsics.stringPlus(var37, var10001.invoke(it, Intrinsics.stringPlus(var23, fileName), Boxing.boxBoolean(true)));
            }

            val var31: StringCompanionObject = StringCompanionObject.INSTANCE;
            val var33: Array<Any> = new Object[]{var27};
            val var38: java.lang.String = java.lang.String.format(
               "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n            <D:multistatus xmlns:D=\"DAV:\">\n                %s\n            </D:multistatus>\n        ",
               Arrays.copyOf(var33, var33.length)
            );
            context.response().setStatusCode(207).end(var38);
            return Unit.INSTANCE;
         }
      }
   }

   public suspend fun webdavMkdir(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var9: File = new File(Intrinsics.stringPlus(home, file));
      if (var9.exists()) {
         context.response().setStatusCode(201).end();
         return Unit.INSTANCE;
      } else {
         try {
            var9.mkdirs();
            context.response().setStatusCode(201).end();
         } catch (var7: Exception) {
            context.response().setStatusCode(500).end();
         }

         return Unit.INSTANCE;
      }
   }

   public suspend fun webdavUpload(context: RoutingContext) {
      var `$continuation`: Continuation;
      label66: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label66;
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
               return this.this$0.webdavUpload(null, this);
            }
         };
      }

      label58: {
         val `$result`: Any = `$continuation`.result;
         val var9: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               val home: java.lang.String = this.getUserWebdavHome(context);
               var file: java.lang.String = context.request().path();
               file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
               val var15: File = new File(Intrinsics.stringPlus(home, file));
               if (!var15.getParentFile().exists()) {
                  context.response().setStatusCode(409).end();
                  return Unit.INSTANCE;
               }

               if (var15.isDirectory()) {
                  context.response().setStatusCode(405).end();
                  return Unit.INSTANCE;
               }

               if (var15.exists()) {
                  var15.delete();
               }

               var var19: BookController;
               try {
                  val userNameSpace: ByteArray = context.getBody().getBytes();
                  FilesKt.writeBytes(var15, userNameSpace);
                  val var16: java.lang.String = var15.toString();
                  if (StringsKt.indexOf$default(var16, "bookProgress", 0, false, 6, null) <= 0) {
                     break label58;
                  }

                  val var17: java.lang.String = var15.toString();
                  if (StringsKt.indexOf$default(var17, ".json", 0, false, 6, null) <= 0) {
                     break label58;
                  }

                  val var18: java.lang.String = this.getUserNameSpace(context);
                  var19 = new BookController(this.getCoroutineContext());
                  `$continuation`.L$0 = context;
                  `$continuation`.label = 1;
                  var19 = (BookController)var19.syncBookProgressFromWebdav(var15, var18, `$continuation`);
               } catch (var13: Exception) {
                  context.response().setStatusCode(500).end();
                  return Unit.INSTANCE;
               }

               if (var19 === var9) {
                  return var9;
               }
               break;
            case 1:
               context = `$continuation`.L$0 as RoutingContext;

               try {
                  ResultKt.throwOnFailure(`$result`);
                  break;
               } catch (var12: Exception) {
                  (`$continuation`.L$0 as RoutingContext).response().setStatusCode(500).end();
                  return Unit.INSTANCE;
               }
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         try {
            ;
         } catch (var11: Exception) {
            context.response().setStatusCode(500).end();
            return Unit.INSTANCE;
         }
      }

      try {
         context.response().setStatusCode(201).end();
      } catch (var10: Exception) {
         context.response().setStatusCode(500).end();
      }

      return Unit.INSTANCE;
   }

   public suspend fun webdavDownload(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var7: File = new File(Intrinsics.stringPlus(home, file));
      if (!var7.exists()) {
         context.response().setStatusCode(404).end();
         return Unit.INSTANCE;
      } else if (var7.isDirectory()) {
         context.response().setStatusCode(405).end();
         return Unit.INSTANCE;
      } else {
         val var10000: HttpServerResponse = context.response()
            .putHeader("Cache-Control", "86400")
            .putHeader("Content-Disposition", Intrinsics.stringPlus("attachment; filename=", URLEncoder.encode(var7.getName(), "UTF-8")))
            .sendFile(var7.toString());
         return if (var10000 === IntrinsicsKt.getCOROUTINE_SUSPENDED()) var10000 else Unit.INSTANCE;
      }
   }

   public suspend fun webdavDelete(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var7: File = new File(Intrinsics.stringPlus(home, file));
      if (!var7.exists()) {
         context.response().setStatusCode(404).end();
         return Unit.INSTANCE;
      } else {
         ExtKt.deleteRecursively(var7);
         context.response().setStatusCode(200).end();
         return Unit.INSTANCE;
      }
   }

   public suspend fun webdavMove(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var13: File = new File(Intrinsics.stringPlus(home, file));
      if (!var13.exists()) {
         context.response().setStatusCode(412).end();
         return Unit.INSTANCE;
      } else {
         var destination: java.lang.String = context.request().getHeader("Destination");
         if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
         } else {
            var overwrite: java.lang.String = new URL(destination).getPath();
            destination = if (overwrite == null) null else StringsKt.replace(overwrite, "/reader3/webdav/", "/", true);
            if (destination == null) {
               context.response().setStatusCode(400).end();
               return Unit.INSTANCE;
            } else {
               label35: {
                  overwrite = context.request().getHeader("Overwrite");
                  val destinationFile: File = new File(Intrinsics.stringPlus(home, URLDecoder.decode(destination, "UTF-8")));
                  if (destinationFile.exists()) {
                     if (overwrite == null || overwrite.length() == 0) {
                        break label35;
                     }

                     ExtKt.deleteRecursively(destinationFile);
                  }

                  var13.renameTo(destinationFile);
                  context.response().setStatusCode(201).end();
                  return Unit.INSTANCE;
               }

               context.response().setStatusCode(412).end();
               return Unit.INSTANCE;
            }
         }
      }
   }

   public suspend fun webdavCopy(context: RoutingContext) {
      val home: java.lang.String = this.getUserWebdavHome(context);
      var file: java.lang.String = context.request().path();
      file = URLDecoder.decode(StringsKt.replace(file, "/reader3/webdav/", "/", true), "UTF-8");
      val var13: File = new File(Intrinsics.stringPlus(home, file));
      if (!var13.exists()) {
         context.response().setStatusCode(412).end();
         return Unit.INSTANCE;
      } else {
         var destination: java.lang.String = context.request().getHeader("Destination");
         if (destination == null) {
            context.response().setStatusCode(400).end();
            return Unit.INSTANCE;
         } else {
            var overwrite: java.lang.String = new URL(destination).getPath();
            destination = if (overwrite == null) null else StringsKt.replace(overwrite, "/reader3/webdav/", "/", true);
            if (destination == null) {
               context.response().setStatusCode(400).end();
               return Unit.INSTANCE;
            } else {
               label35: {
                  overwrite = context.request().getHeader("Overwrite");
                  val destinationFile: File = new File(Intrinsics.stringPlus(home, URLDecoder.decode(destination, "UTF-8")));
                  if (destinationFile.exists()) {
                     if (overwrite == null || overwrite.length() == 0) {
                        break label35;
                     }

                     ExtKt.deleteRecursively(destinationFile);
                  }

                  FilesKt.copyRecursively$default(var13, destinationFile, false, null, 6, null);
                  context.response().setStatusCode(201).end();
                  return Unit.INSTANCE;
               }

               context.response().setStatusCode(412).end();
               return Unit.INSTANCE;
            }
         }
      }
   }

   public suspend fun webdavLock(context: RoutingContext) {
      val timeout: UUID = UUID.randomUUID();
      val lockToken: java.lang.String = Intrinsics.stringPlus("urn:uuid:", timeout);
      var var10: java.lang.String = context.request().getHeader("Timeout");
      if (var10 == null) {
         var10 = "Second-3600";
      }

      val fileUrl: java.lang.String = context.request().absoluteURI();
      val var10000: HttpServerResponse = context.response().putHeader("Lock-Token", lockToken).setStatusCode(200);
      val var7: StringCompanionObject = StringCompanionObject.INSTANCE;
      val var8: Array<Any> = new Object[]{lockToken, fileUrl, var10};
      val var10001: java.lang.String = java.lang.String.format(
         "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n        <D:prop xmlns:D=\"DAV:\">\n            <D:lockdiscovery>\n                <D:activelock>\n                    <D:locktype>\n                        <write />\n                    </D:locktype>\n                    <D:lockscope>\n                        <exclusive />\n                    </D:lockscope>\n                    <D:locktoken>\n                        <D:href>%s</D:href>\n                    </D:locktoken>\n                    <D:lockroot>\n                        <D:href>%s</D:href>\n                    </D:lockroot>\n                    <D:depth>infinity</D:depth>\n                    <D:owner>\n                        <a:href xmlns:a=\"DAV:\">http://www.apple.com/webdav_fs/</a:href>\n                    </D:owner>\n                    <D:timeout>%s</D:timeout>\n                </D:activelock>\n            </D:lockdiscovery>\n        </D:prop>\n        ",
         Arrays.copyOf(var8, var8.length)
      );
      var10000.end(var10001);
      return Unit.INSTANCE;
   }

   public suspend fun webdavUnLock(context: RoutingContext) {
      val lockToken: java.lang.String = context.request().getHeader("Lock-Token");
      if (lockToken == null) {
         context.response().setStatusCode(400).end();
         return Unit.INSTANCE;
      } else {
         context.response().putHeader("Lock-Token", lockToken).setStatusCode(204).end();
         return Unit.INSTANCE;
      }
   }

   public suspend fun backupToWebdav(context: RoutingContext): ReturnData {
      var `$continuation`: Continuation;
      label57: {
         if (`$completion` is SyntheticContinuation) {
            `$continuation` = `$completion` as SyntheticContinuation;
            if (((`$completion` as SyntheticContinuation).label and Integer.MIN_VALUE) != 0) {
               `$continuation`.label -= Integer.MIN_VALUE;
               break label57;
            }
         }

         `$continuation` = new ContinuationImpl(this, `$completion`) {
            Object L$0;
            Object L$1;
            Object L$2;
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
               return this.this$0.backupToWebdav(null, this);
            }
         };
      }

      var bookController: BookController;
      var userNameSpace: java.lang.String;
      var var9: Any;
      var var11: Any;
      var returnData: ReturnData;
      label61: {
         val `$result`: Any = `$continuation`.result;
         var9 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
         switch ($continuation.label) {
            case 0:
               ResultKt.throwOnFailure(`$result`);
               returnData = new ReturnData();
               `$continuation`.L$0 = this;
               `$continuation`.L$1 = context;
               `$continuation`.L$2 = returnData;
               `$continuation`.label = 1;
               var11 = this.checkAuth(context, `$continuation`);
               if (var11 === var9) {
                  return var9;
               }
               break;
            case 1:
               returnData = `$continuation`.L$2 as ReturnData;
               context = `$continuation`.L$1 as RoutingContext;
               this = `$continuation`.L$0 as WebdavController;
               ResultKt.throwOnFailure(`$result`);
               var11 = `$result`;
               break;
            case 2:
               userNameSpace = `$continuation`.L$2 as java.lang.String;
               bookController = `$continuation`.L$1 as BookController;
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               var11 = `$result`;
               break label61;
            case 3:
               returnData = `$continuation`.L$0 as ReturnData;
               ResultKt.throwOnFailure(`$result`);
               return if (!`$result` as java.lang.Boolean) returnData.setErrorMsg("备份失败") else ReturnData.setData$default(returnData, "", null, 2, null);
            default:
               throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
         }

         if (!var11 as java.lang.Boolean) {
            return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
         }

         if (this.getAppConfig().getSecure()) {
            val var10: User = context.get("userInfo");
            if (var10 == null) {
               return ReturnData.setData$default(returnData, "NEED_LOGIN", null, 2, null).setErrorMsg("请登录后使用");
            }

            if (!var10.getEnable_webdav()) {
               return returnData.setErrorMsg("未开启webdav功能");
            }
         }

         bookController = new BookController(this.getCoroutineContext());
         userNameSpace = this.getUserNameSpace(context);
         `$continuation`.L$0 = returnData;
         `$continuation`.L$1 = bookController;
         `$continuation`.L$2 = userNameSpace;
         `$continuation`.label = 2;
         var11 = bookController.getLastBackFileFromWebdav(userNameSpace, `$continuation`);
         if (var11 === var9) {
            return var9;
         }
      }

      val latestZipFilePath: java.lang.String = var11 as java.lang.String;
      `$continuation`.L$0 = returnData;
      `$continuation`.L$1 = null;
      `$continuation`.L$2 = null;
      `$continuation`.label = 3;
      var11 = bookController.saveToWebdav(userNameSpace, latestZipFilePath, `$continuation`);
      if (var11 === var9) {
         return var9;
      } else {
         return if (!var11 as java.lang.Boolean) returnData.setErrorMsg("备份失败") else ReturnData.setData$default(returnData, "", null, 2, null);
      }
   }

   @JvmStatic
   fun `lambda-1$lambda-0`(`$it`: RoutingContext, `this$0`: WebdavController, `$noName_0`: Void) {
      val res: HttpServerResponse = `$it`.response();
      res.putHeader("DAV", "1,2");
      res.putHeader("Access-Control-Allow-Origin", "*");
      res.putHeader("Access-Control-Allow-Credentials", "true");
      res.putHeader("Access-Control-Expose-Headers", "DAV, content-length, Allow");
      res.putHeader("MS-Author-Via", "DAV");
      res.putHeader("Allow", "OPTIONS,DELETE,GET,PUT,PROPFIND,MKCOL,MOVE,COPY,LOCK,UNLOCK");
      if (`this$0`.getAppConfig().getSecure()) {
         res.putHeader("WWW-Authenticate", "Basic realm=\"Default realm\"");
      }
   }

   @JvmStatic
   fun `_init_$lambda-1`(`this$0`: WebdavController, `$onHandlerError`: Function2, it: RoutingContext) {
      it.addHeadersEndHandler(WebdavController::lambda-1$lambda-0);
      val rawMethod: java.lang.String = it.request().rawMethod();
      if (!`this$0`.checkAuthorization(it)) {
         if (rawMethod.equals("PROPFIND")
            || rawMethod.equals("MKCOL")
            || rawMethod.equals("PUT")
            || rawMethod.equals("GET")
            || rawMethod.equals("DELETE")
            || rawMethod.equals("MOVE")
            || rawMethod.equals("COPY")
            || rawMethod.equals("LOCK")
            || rawMethod.equals("UNLOCK")) {
            it.response().setStatusCode(401).end();
            return;
         }

         if (rawMethod.equals("OPTIONS") && it.request().getHeader("Authorization") != null) {
            it.response().setStatusCode(401).end();
            return;
         }
      }

      if (rawMethod != null) {
         switch (rawMethod.hashCode()) {
            case -1787112636:
               if (rawMethod.equals("UNLOCK")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavUnLock(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case -531492226:
               if (rawMethod.equals("OPTIONS")) {
                  it.response().setStatusCode(200).end();
                  return;
               }
               break;
            case -210493540:
               if (rawMethod.equals("PROPFIND")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavList(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 70454:
               if (rawMethod.equals("GET")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavDownload(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 79599:
               if (rawMethod.equals("PUT")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavUpload(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 2074485:
               if (rawMethod.equals("COPY")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavCopy(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 2342187:
               if (rawMethod.equals("LOCK")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavLock(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 2372561:
               if (rawMethod.equals("MOVE")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavMove(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 73412354:
               if (rawMethod.equals("MKCOL")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavMkdir(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
               break;
            case 2012838315:
               if (rawMethod.equals("DELETE")) {
                  BuildersKt.launch$default(
                     `this$0`,
                     new MDCContext(null, 1, null).plus(Dispatchers.getIO()),
                     null,
                     (new Function2<CoroutineScope, Continuation<? super Unit>, Object>(`this$0`, it, `$onHandlerError`, null) {
                        int label;

                        {
                           super(2, `$completionx`);
                           this.this$0 = `$receiver`;
                           this.$it = `$it`;
                           this.$onHandlerError = `$onHandlerError`;
                        }

                        @Nullable
                        @Override
                        public final Object invokeSuspend(@NotNull Object $result) {
                           val var4: Any = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                           switch (this.label) {
                              case 0:
                                 ResultKt.throwOnFailure(`$result`);

                                 var var12: WebdavController;
                                 try {
                                    var12 = this.this$0;
                                    val e: RoutingContext = this.$it;
                                    val var10002: Continuation = this;
                                    this.label = 1;
                                    var12 = (WebdavController)var12.webdavDelete(e, var10002);
                                 } catch (var6: Exception) {
                                    val var10: Function2 = this.$onHandlerError;
                                    val var8: RoutingContext = this.$it;
                                    var10.invoke(var8, var6);
                                    return Unit.INSTANCE;
                                 }

                                 if (var12 === var4) {
                                    return var4;
                                 }
                                 break;
                              case 1:
                                 try {
                                    ResultKt.throwOnFailure(`$result`);
                                    break;
                                 } catch (var7: Exception) {
                                    val var10000: Function2 = this.$onHandlerError;
                                    val var3: RoutingContext = this.$it;
                                    var10000.invoke(var3, var7);
                                    return Unit.INSTANCE;
                                 }
                              default:
                                 throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                           }

                           try {
                              ;
                           } catch (var5: Exception) {
                              val var13: Function2 = this.$onHandlerError;
                              val var9: RoutingContext = this.$it;
                              var13.invoke(var9, var5);
                           }

                           return Unit.INSTANCE;
                        }

                        @NotNull
                        @Override
                        public final Continuation<Unit> create(@Nullable Object value, @NotNull Continuation<?> $completion) {
                           return new <anonymous constructor>(this.this$0, this.$it, this.$onHandlerError, `$completion`);
                        }

                        @Nullable
                        public final Object invoke(@NotNull CoroutineScope p1, @Nullable Continuation<? super Unit> p2) {
                           return (this.create(p1, p2) as SyntheticContinuation).invokeSuspend(Unit.INSTANCE);
                        }
                     }) as Function2,
                     2,
                     null
                  );
                  return;
               }
            default:
         }
      }

      it.response().setStatusCode(405).end();
   }
}
