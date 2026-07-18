package com.script.javascript;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public final class JSAdapter implements Scriptable, Function {
   private Scriptable prototype;
   private Scriptable parent;
   private Scriptable adaptee;
   private boolean isPrototype;
   private static final String GET_PROP = "__get__";
   private static final String HAS_PROP = "__has__";
   private static final String PUT_PROP = "__put__";
   private static final String DEL_PROP = "__delete__";
   private static final String GET_PROPIDS = "__getIds__";

   private JSAdapter(Scriptable obj) {
      this.setAdaptee(obj);
   }

   public static void init(Context cx, Scriptable scope, boolean sealed) throws RhinoException {
      JSAdapter obj = new JSAdapter(cx.newObject(scope));
      obj.setParentScope(scope);
      obj.setPrototype(getFunctionPrototype(scope));
      obj.isPrototype = true;
      ScriptableObject.defineProperty(scope, "JSAdapter", obj, 2);
   }

   public String getClassName() {
      return "JSAdapter";
   }

   public Object get(String name, Scriptable start) {
      Function func = this.getAdapteeFunction("__get__");
      if (func != null) {
         return this.call(func, new Object[]{name});
      } else {
         start = this.getAdaptee();
         return start.get(name, start);
      }
   }

   public Object get(int index, Scriptable start) {
      Function func = this.getAdapteeFunction("__get__");
      if (func != null) {
         return this.call(func, new Object[]{index});
      } else {
         start = this.getAdaptee();
         return start.get(index, start);
      }
   }

   public boolean has(String name, Scriptable start) {
      Function func = this.getAdapteeFunction("__has__");
      if (func != null) {
         Object res = this.call(func, new Object[]{name});
         return Context.toBoolean(res);
      } else {
         start = this.getAdaptee();
         return start.has(name, start);
      }
   }

   public boolean has(int index, Scriptable start) {
      Function func = this.getAdapteeFunction("__has__");
      if (func != null) {
         Object res = this.call(func, new Object[]{index});
         return Context.toBoolean(res);
      } else {
         start = this.getAdaptee();
         return start.has(index, start);
      }
   }

   public void put(String name, Scriptable start, Object value) {
      if (start == this) {
         Function func = this.getAdapteeFunction("__put__");
         if (func != null) {
            this.call(func, new Object[]{name, value});
         } else {
            start = this.getAdaptee();
            start.put(name, start, value);
         }
      } else {
         start.put(name, start, value);
      }

   }

   public void put(int index, Scriptable start, Object value) {
      if (start == this) {
         Function func = this.getAdapteeFunction("__put__");
         if (func != null) {
            this.call(func, new Object[]{index, value});
         } else {
            start = this.getAdaptee();
            start.put(index, start, value);
         }
      } else {
         start.put(index, start, value);
      }

   }

   public void delete(String name) {
      Function func = this.getAdapteeFunction("__delete__");
      if (func != null) {
         this.call(func, new Object[]{name});
      } else {
         this.getAdaptee().delete(name);
      }

   }

   public void delete(int index) {
      Function func = this.getAdapteeFunction("__delete__");
      if (func != null) {
         this.call(func, new Object[]{index});
      } else {
         this.getAdaptee().delete(index);
      }

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

   public Object[] getIds() {
      Function func = this.getAdapteeFunction("__getIds__");
      if (func == null) {
         return this.getAdaptee().getIds();
      } else {
         Object val = this.call(func, new Object[0]);
         if (val instanceof NativeArray) {
            NativeArray array = (NativeArray)val;
            Object[] res = new Object[(int)array.getLength()];

            for(int index = 0; index < res.length; ++index) {
               res[index] = this.mapToId(array.get(index, array));
            }

            return res;
         } else if (!(val instanceof NativeJavaArray)) {
            return Context.emptyArgs;
         } else {
            Object tmp = ((NativeJavaArray)val).unwrap();
            Object[] res;
            if (tmp.getClass() == Object[].class) {
               Object[] array = (Object[])tmp;
               res = new Object[array.length];

               for(int index = 0; index < array.length; ++index) {
                  res[index] = this.mapToId(array[index]);
               }
            } else {
               res = Context.emptyArgs;
            }

            return res;
         }
      }
   }

   public boolean hasInstance(Scriptable scriptable) {
      if (scriptable instanceof JSAdapter) {
         return true;
      } else {
         for(Scriptable proto = scriptable.getPrototype(); proto != null; proto = proto.getPrototype()) {
            if (proto.equals(this)) {
               return true;
            }
         }

         return false;
      }
   }

   public Object getDefaultValue(Class hint) {
      return this.getAdaptee().getDefaultValue(hint);
   }

   public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) throws RhinoException {
      if (this.isPrototype) {
         return this.construct(cx, scope, args);
      } else {
         Scriptable tmp = this.getAdaptee();
         if (tmp instanceof Function) {
            return ((Function)tmp).call(cx, scope, tmp, args);
         } else {
            throw Context.reportRuntimeError("TypeError: not a function");
         }
      }
   }

   public Scriptable construct(Context cx, Scriptable scope, Object[] args) throws RhinoException {
      if (this.isPrototype) {
         Scriptable topLevel = ScriptableObject.getTopLevelScope(scope);
         if (args.length > 0) {
            JSAdapter newObj = new JSAdapter(Context.toObject(args[0], topLevel));
            return newObj;
         } else {
            throw Context.reportRuntimeError("JSAdapter requires adaptee");
         }
      } else {
         Scriptable tmp = this.getAdaptee();
         if (tmp instanceof Function) {
            return ((Function)tmp).construct(cx, scope, args);
         } else {
            throw Context.reportRuntimeError("TypeError: not a constructor");
         }
      }
   }

   public Scriptable getAdaptee() {
      return this.adaptee;
   }

   public void setAdaptee(Scriptable adaptee) {
      if (adaptee == null) {
         throw new NullPointerException("adaptee can not be null");
      } else {
         this.adaptee = adaptee;
      }
   }

   private Object mapToId(Object tmp) {
      return tmp instanceof Double ? ((Double)tmp).intValue() : Context.toString(tmp);
   }

   private static Scriptable getFunctionPrototype(Scriptable scope) {
      return ScriptableObject.getFunctionPrototype(scope);
   }

   private Function getAdapteeFunction(String name) {
      Object o = ScriptableObject.getProperty(this.getAdaptee(), name);
      return o instanceof Function ? (Function)o : null;
   }

   private Object call(Function func, Object[] args) {
      Context cx = Context.getCurrentContext();
      Scriptable thisObj = this.getAdaptee();
      Scriptable scope = func.getParentScope();

      try {
         return func.call(cx, scope, thisObj, args);
      } catch (RhinoException re) {
         throw Context.reportRuntimeError(re.getMessage());
      }
   }
}
