package com.script.javascript;

import com.script.Bindings;
import com.script.ScriptContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeJavaClass;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

final class ExternalScriptable implements Scriptable {
   private ScriptContext context;
   private Map<Object, Object> indexedProps;
   private Scriptable prototype;
   private Scriptable parent;

   ExternalScriptable(ScriptContext context) {
      this(context, new HashMap<>());
   }

   ExternalScriptable(ScriptContext context, Map<Object, Object> indexedProps) {
      if (context == null) {
         throw new NullPointerException("context is null");
      } else {
         this.context = context;
         this.indexedProps = indexedProps;
      }
   }

   ScriptContext getContext() {
      return this.context;
   }

   private boolean isEmpty(String name) {
      return name.equals("");
   }

   public String getClassName() {
      return "Global";
   }

   public synchronized Object get(String name, Scriptable start) {
      if (this.isEmpty(name)) {
         return this.indexedProps.getOrDefault(name, NOT_FOUND);
      } else {
         synchronized(this.context) {
            int scope = this.context.getAttributesScope(name);
            if (scope != -1) {
               Object value = this.context.getAttribute(name, scope);
               return Context.javaToJS(value, this);
            } else {
               return NOT_FOUND;
            }
         }
      }
   }

   public synchronized Object get(int index, Scriptable start) {
      Integer key = index;
      return this.indexedProps.containsKey(index) ? this.indexedProps.get(key) : NOT_FOUND;
   }

   public synchronized boolean has(String name, Scriptable start) {
      if (this.isEmpty(name)) {
         return this.indexedProps.containsKey(name);
      } else {
         synchronized(this.context) {
            return this.context.getAttributesScope(name) != -1;
         }
      }
   }

   public synchronized boolean has(int index, Scriptable start) {
      Integer key = index;
      return this.indexedProps.containsKey(key);
   }

   public void put(String name, Scriptable start, Object value) {
      if (start == this) {
         synchronized(this) {
            if (this.isEmpty(name)) {
               this.indexedProps.put(name, value);
            } else {
               synchronized(this.context) {
                  int scope = this.context.getAttributesScope(name);
                  if (scope == -1) {
                     scope = 100;
                  }

                  this.context.setAttribute(name, this.jsToJava(value), scope);
               }
            }
         }
      } else {
         start.put(name, start, value);
      }

   }

   public void put(int index, Scriptable start, Object value) {
      if (start == this) {
         synchronized(this) {
            this.indexedProps.put(index, value);
         }
      } else {
         start.put(index, start, value);
      }

   }

   public synchronized void delete(String name) {
      if (this.isEmpty(name)) {
         this.indexedProps.remove(name);
      } else {
         synchronized(this.context) {
            int scope = this.context.getAttributesScope(name);
            if (scope != -1) {
               this.context.removeAttribute(name, scope);
            }
         }
      }

   }

   public void delete(int index) {
      this.indexedProps.remove(index);
   }

   public Scriptable getPrototype() {
      return this.prototype;
   }

   public void setPrototype(Scriptable prototype) {
      this.prototype = prototype;
   }

   public Scriptable getParentScope() {
      return this.parent;
   }

   public void setParentScope(Scriptable parent) {
      this.parent = parent;
   }

   public synchronized Object[] getIds() {
      String[] keys = this.getAllKeys();
      int size = keys.length + this.indexedProps.size();
      Object[] res = new Object[size];
      System.arraycopy(keys, 0, res, 0, keys.length);
      int i = keys.length;

      for(Object index : this.indexedProps.keySet()) {
         res[i++] = index;
      }

      return res;
   }

   public Object getDefaultValue(Class typeHint) {
      for(int i = 0; i < 2; ++i) {
         boolean tryToString;
         if (typeHint == ScriptRuntime.StringClass) {
            tryToString = i == 0;
         } else {
            tryToString = i == 1;
         }

         String methodName;
         Object[] args;
         if (tryToString) {
            methodName = "toString";
            args = ScriptRuntime.emptyArgs;
         } else {
            methodName = "valueOf";
            args = new Object[1];
            String hint;
            if (typeHint == null) {
               hint = "undefined";
            } else if (typeHint == ScriptRuntime.StringClass) {
               hint = "string";
            } else if (typeHint == ScriptRuntime.ScriptableClass) {
               hint = "object";
            } else if (typeHint == ScriptRuntime.FunctionClass) {
               hint = "function";
            } else if (typeHint != ScriptRuntime.BooleanClass && typeHint != Boolean.TYPE) {
               if (typeHint != ScriptRuntime.NumberClass && typeHint != ScriptRuntime.ByteClass && typeHint != Byte.TYPE && typeHint != ScriptRuntime.ShortClass && typeHint != Short.TYPE && typeHint != ScriptRuntime.IntegerClass && typeHint != Integer.TYPE && typeHint != ScriptRuntime.FloatClass && typeHint != Float.TYPE && typeHint != ScriptRuntime.DoubleClass && typeHint != Double.TYPE) {
                  throw Context.reportRuntimeError("Invalid JavaScript value of type " + typeHint.toString());
               }

               hint = "number";
            } else {
               hint = "boolean";
            }

            args[0] = hint;
         }

         Object v = ScriptableObject.getProperty(this, methodName);
         if (v instanceof Function) {
            Function fun = (Function)v;
            Context cx = Context.enter();

            try {
               v = fun.call(cx, fun.getParentScope(), this, args);
            } finally {
               Context.exit();
            }

            if (v != null) {
               if (!(v instanceof Scriptable)) {
                  return v;
               }

               if (typeHint == ScriptRuntime.ScriptableClass || typeHint == ScriptRuntime.FunctionClass) {
                  return v;
               }

               if (tryToString && v instanceof Wrapper) {
                  Object u = ((Wrapper)v).unwrap();
                  if (u instanceof String) {
                     return u;
                  }
               }
            }
         }
      }

      String arg = typeHint == null ? "undefined" : typeHint.getName();
      throw Context.reportRuntimeError("\u627e\u4e0d\u5230\u5bf9\u8c61\u7684\u9ed8\u8ba4\u503c " + arg);
   }

   public boolean hasInstance(Scriptable instance) {
      for(Scriptable proto = instance.getPrototype(); proto != null; proto = proto.getPrototype()) {
         if (proto.equals(this)) {
            return true;
         }
      }

      return false;
   }

   private String[] getAllKeys() {
      ArrayList<String> list = new ArrayList<>();
      synchronized(this.context) {
         for(int scope : this.context.getScopes()) {
            Bindings bindings = this.context.getBindings(scope);
            if (bindings != null) {
               list.ensureCapacity(bindings.size());

               for(String key : bindings.keySet()) {
                  list.add(key);
               }
            }
         }
      }

      String[] res = new String[list.size()];
      list.toArray(res);
      return res;
   }

   private Object jsToJava(Object jsObj) {
      if (jsObj instanceof Wrapper) {
         Wrapper njb = (Wrapper)jsObj;
         if (njb instanceof NativeJavaClass) {
            return njb;
         } else {
            Object obj = njb.unwrap();
            return !(obj instanceof Number) && !(obj instanceof String) && !(obj instanceof Boolean) && !(obj instanceof Character) ? obj : njb;
         }
      } else {
         return jsObj;
      }
   }
}
