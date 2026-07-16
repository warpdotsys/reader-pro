package io.legado.app.utils

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

public class ParameterizedTypeImpl(clazz: Class<*>) : ParameterizedType {
   private final val clazz: Class<*>

   init {
      this.clazz = clazz;
   }

   public override fun getRawType(): Type {
      return java.util.List::class.java;
   }

   public override fun getOwnerType(): Type? {
      return null;
   }

   public override fun getActualTypeArguments(): Array<Type> {
      return new Type[]{this.clazz};
   }
}
