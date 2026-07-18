package com.script.javascript;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

final class RhinoWrapFactory extends WrapFactory {
   private static RhinoWrapFactory theInstance;

   private RhinoWrapFactory() {
   }

   static synchronized WrapFactory getInstance() {
      if (theInstance == null) {
         theInstance = new RhinoWrapFactory();
      }

      return theInstance;
   }

   public Scriptable wrapAsJavaObject(Context cx, Scriptable scope, Object javaObject, Class staticType) {
      if (scope != null) {
         scope.delete("Packages");
      }

      SecurityManager sm = System.getSecurityManager();
      ClassShutter classShutter = RhinoClassShutter.getInstance();
      if (javaObject instanceof ClassLoader) {
         if (sm != null) {
            sm.checkPermission(new RuntimePermission("getClassLoader"));
         }

         return super.wrapAsJavaObject(cx, scope, javaObject, staticType);
      } else {
         String name = null;
         if (javaObject instanceof Class) {
            name = ((Class)javaObject).getName();
         } else if (javaObject instanceof Member) {
            Member member = (Member)javaObject;
            if (sm != null && !Modifier.isPublic(member.getModifiers())) {
               return null;
            }

            name = member.getDeclaringClass().getName();
         }

         if (name != null) {
            return !classShutter.visibleToScripts(name) ? null : super.wrapAsJavaObject(cx, scope, javaObject, staticType);
         } else {
            Class dynamicType = javaObject.getClass();
            name = dynamicType.getName();
            if (classShutter.visibleToScripts(name)) {
               return super.wrapAsJavaObject(cx, scope, javaObject, staticType);
            } else {
               Class type = null;
               if (staticType != null && staticType.isInterface()) {
                  type = staticType;
               } else {
                  while(dynamicType != null) {
                     dynamicType = dynamicType.getSuperclass();
                     name = dynamicType.getName();
                     if (classShutter.visibleToScripts(name)) {
                        type = dynamicType;
                        break;
                     }
                  }

                  assert type != null : "java.lang.Object \u4e0d\u53ef\u8bbf\u95ee";
               }

               return new RhinoJavaObject(scope, javaObject, type);
            }
         }
      }
   }

   private static class RhinoJavaObject extends NativeJavaObject {
      RhinoJavaObject(Scriptable scope, Object obj, Class type) {
         super(scope, (Object)null, type);
         this.javaObject = obj;
      }

      public Object get(String name, Scriptable start) {
         return !name.equals("getClass") && !name.equals("exec") ? super.get(name, start) : NOT_FOUND;
      }
   }
}
