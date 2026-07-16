package io.legado.app.utils

import com.htmake.reader.synth.SyntheticContinuation
import com.htmake.reader.synth.SyntheticFunction0
import com.htmake.reader.synth.SyntheticType

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Reader
import java.lang.reflect.Type
import kotlin.Result.Companion
import kotlin.jvm.internal.Intrinsics

public final val GSON: Gson by LazyKt.lazy(SyntheticFunction0.INSTANCE)
   public final get() {
      val var0: Any = GSON$delegate.getValue();
      return var0 as Gson;
   }

@JvmSynthetic
public inline fun <reified T> genericType(): Type {
   Intrinsics.needClassReification();
   val var1: Type = (new TypeToken<T>() {}).getType();
   return var1;
}

@JvmSynthetic
public inline fun <reified T> Gson.fromJsonObject(json: String?): Result<T?> {
   var var4: Any;
   try {
      var4 = Result.Companion;
      Intrinsics.needClassReification();
      val var13: Type = new GsonExtensionsKt$fromJsonObject$lambda-0$$inlined$genericType$1().getType();
      val var10000: Any = `$this$fromJsonObject`.fromJson(json, var13);
      Intrinsics.reifiedOperationMarker(2, "T");
      var4 = Result.constructor-impl(var10000);
   } catch (var8: java.lang.Throwable) {
      val `$i$f$genericType`: Companion = Result.Companion;
      var4 = Result.constructor-impl(ResultKt.createFailure(var8));
   }

   return var4;
}

@JvmSynthetic
public inline fun <reified T> Gson.fromJsonArray(json: String?): Result<List<T>?> {
   var var4: Any;
   try {
      var4 = Result.Companion;
      Intrinsics.reifiedOperationMarker(4, "T");
      val var11: Any = `$this$fromJsonArray`.fromJson(json, new ParameterizedTypeImpl(Object::class.java));
      var4 = Result.constructor-impl(var11 as? java.util.List);
   } catch (var8: java.lang.Throwable) {
      val var6: Companion = Result.Companion;
      var4 = Result.constructor-impl(ResultKt.createFailure(var8));
   }

   return var4;
}

@JvmSynthetic
public inline fun <reified T> Gson.fromJsonObject(inputStream: InputStream?): Result<T?> {
   var var4: Any;
   try {
      var4 = Result.Companion;
      val var10001: Reader = new InputStreamReader(inputStream);
      Intrinsics.needClassReification();
      val var8: Type = new GsonExtensionsKt$fromJsonObject$lambda-2$$inlined$genericType$1().getType();
      val var10000: Any = `$this$fromJsonObject`.fromJson(var10001, var8);
      Intrinsics.reifiedOperationMarker(2, "T");
      var4 = Result.constructor-impl(var10000);
   } catch (var9: java.lang.Throwable) {
      val reader: Companion = Result.Companion;
      var4 = Result.constructor-impl(ResultKt.createFailure(var9));
   }

   return var4;
}

@JvmSynthetic
public inline fun <reified T> Gson.fromJsonArray(inputStream: InputStream?): Result<List<T>?> {
   var var4: Any;
   try {
      var4 = Result.Companion;
      val var10001: Reader = new InputStreamReader(inputStream);
      Intrinsics.reifiedOperationMarker(4, "T");
      val var13: Any = `$this$fromJsonArray`.fromJson(var10001, new ParameterizedTypeImpl(Object::class.java));
      var4 = Result.constructor-impl(var13 as? java.util.List);
   } catch (var8: java.lang.Throwable) {
      val reader: Companion = Result.Companion;
      var4 = Result.constructor-impl(ResultKt.createFailure(var8));
   }

   return var4;
}

public fun Gson.writeToOutputStream(out: OutputStream, any: Any) {
   val writer: JsonWriter = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
   writer.setIndent("  ");
   if (any is java.util.List) {
      writer.beginArray();

      val `$this$forEach$iv`: java.lang.Iterable;
      for (Object element$iv : $this$forEach$iv) {
         if (`element$iv` != null) {
            `$this$writeToOutputStream`.toJson(`element$iv`, `element$iv`.getClass(), writer);
         }
      }

      writer.endArray();
   } else {
      `$this$writeToOutputStream`.toJson(any, any.getClass(), writer);
   }

   writer.close();
}
