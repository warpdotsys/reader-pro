package com.script.javascript;

import com.script.AbstractScriptEngine;
import com.script.Bindings;
import com.script.Compilable;
import com.script.CompiledScript;
import com.script.Invocable;
import com.script.ScriptContext;
import com.script.ScriptException;
import com.script.SimpleBindings;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;

public class RhinoScriptEngine extends AbstractScriptEngine implements Invocable, Compilable {
   private static final boolean DEBUG = false;
   private AccessControlContext accCtxt;
   private final RhinoTopLevel topLevel;
   private final Map<Object, Object> indexedProps;
   private final InterfaceImplementor implementor;
   private static final String printSource = "function print(str, newline) {                \n    if (typeof(str) == 'undefined') {         \n        str = 'undefined';                    \n    } else if (str == null) {                 \n        str = 'null';                         \n    }                                         \n    var out = context.getWriter();            \n    if (!(out instanceof java.io.PrintWriter))\n        out = new java.io.PrintWriter(out);   \n    out.print(String(str));                   \n    if (newline) out.print('\\n');            \n    out.flush();                              \n}\nfunction println(str) {                       \n    print(str, true);                         \n}";

   public RhinoScriptEngine() {
      if (System.getSecurityManager() != null) {
         try {
            AccessController.checkPermission(new AllPermission());
         } catch (AccessControlException var6) {
            this.accCtxt = AccessController.getContext();
         }
      }

      Context cx = Context.enter();

      try {
         this.topLevel = new RhinoTopLevel(cx, this);
      } finally {
         Context.exit();
      }

      this.indexedProps = new HashMap<>();
      this.implementor = new InterfaceImplementor(this) {
         protected boolean isImplemented(Object thiz, Class<?> iface) {
            try {
               if (thiz != null && !(thiz instanceof Scriptable)) {
                  thiz = Context.toObject(thiz, RhinoScriptEngine.this.topLevel);
               }

               Scriptable engineScope = RhinoScriptEngine.this.getRuntimeScope(RhinoScriptEngine.this.context);
               Scriptable localScope = thiz != null ? (Scriptable)thiz : engineScope;

               for(Method method : iface.getMethods()) {
                  if (method.getDeclaringClass() != Object.class) {
                     Object obj = ScriptableObject.getProperty(localScope, method.getName());
                     if (!(obj instanceof Function)) {
                        boolean var10 = false;
                        return var10;
                     }
                  }
               }

               boolean var14 = true;
               return var14;
            } finally {
               Context.exit();
            }
         }

         protected Object convertResult(Method method, Object res) {
            Class<?> desiredType = method.getReturnType();
            return desiredType == Void.TYPE ? null : Context.jsToJava(res, desiredType);
         }
      };
   }

   public Object eval(Reader reader, ScriptContext ctxt) throws ScriptException {
      Context cx = Context.enter();

      Object ret;
      try {
         Scriptable scope = this.getRuntimeScope(ctxt);
         String filename = (String)this.get("javax.script.filename");
         filename = filename == null ? "<Unknown source>" : filename;
         ret = cx.evaluateReader(scope, reader, filename, 1, (Object)null);
      } catch (RhinoException var13) {
         int line;
         line = (line = var13.lineNumber()) == 0 ? -1 : line;
         String msg;
         if (var13 instanceof JavaScriptException) {
            msg = String.valueOf(((JavaScriptException)var13).getValue());
         } else {
            msg = var13.toString();
         }

         ScriptException se = new ScriptException(msg, var13.sourceName(), line);
         se.initCause(var13);
         throw se;
      } catch (IOException ee) {
         throw new ScriptException(ee);
      } finally {
         Context.exit();
      }

      return this.unwrapReturnValue(ret);
   }

   public Object eval(String script, ScriptContext ctxt) throws ScriptException {
      if (script == null) {
         throw new NullPointerException("null script");
      } else {
         return this.eval((Reader)(new StringReader(script)), ctxt);
      }
   }

   public Bindings createBindings() {
      return new SimpleBindings();
   }

   public Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException {
      return this.invoke((Object)null, name, args);
   }

   public Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
      if (thiz == null) {
         throw new IllegalArgumentException("\u811a\u672c\u5bf9\u8c61\u4e0d\u80fd\u4e3a\u7a7a");
      } else {
         return this.invoke(thiz, name, args);
      }
   }

   private Object invoke(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
      Context cx = Context.enter();

      Object var11;
      try {
         if (name == null) {
            throw new NullPointerException("\u65b9\u6cd5\u540d\u4e3a\u7a7a");
         }

         if (thiz != null && !(thiz instanceof Scriptable)) {
            thiz = Context.toObject(thiz, this.topLevel);
         }

         Scriptable engineScope = this.getRuntimeScope(this.context);
         Scriptable localScope = thiz != null ? (Scriptable)thiz : engineScope;
         Object obj = ScriptableObject.getProperty(localScope, name);
         if (!(obj instanceof Function)) {
            throw new NoSuchMethodException("no such method: " + name);
         }

         Function func = (Function)obj;
         Scriptable scope = func.getParentScope();
         if (scope == null) {
            scope = engineScope;
         }

         Object result = func.call(cx, scope, localScope, this.wrapArguments(args));
         var11 = this.unwrapReturnValue(result);
      } catch (RhinoException re) {
         int line;
         line = (line = re.lineNumber()) == 0 ? -1 : line;
         ScriptException se = new ScriptException(re.toString(), re.sourceName(), line);
         se.initCause(re);
         throw se;
      } finally {
         Context.exit();
      }

      return var11;
   }

   public <T> T getInterface(Class<T> clasz) {
      try {
         return (T)this.implementor.getInterface((Object)null, clasz);
      } catch (ScriptException var3) {
         return null;
      }
   }

   public <T> T getInterface(Object thiz, Class<T> clasz) {
      if (thiz == null) {
         throw new IllegalArgumentException("\u811a\u672c\u5bf9\u8c61\u4e0d\u80fd\u4e3a\u7a7a");
      } else {
         try {
            return (T)this.implementor.getInterface(thiz, clasz);
         } catch (ScriptException var4) {
            return null;
         }
      }
   }

   Scriptable getRuntimeScope(ScriptContext ctxt) {
      if (ctxt == null) {
         throw new NullPointerException("\u811a\u672ccontext\u4e3a\u7a7a");
      } else {
         Scriptable newScope = new ExternalScriptable(ctxt, this.indexedProps);
         newScope.setPrototype(this.topLevel);
         newScope.put("context", newScope, ctxt);
         Context cx = Context.enter();

         try {
            cx.evaluateString(newScope, "function print(str, newline) {                \n    if (typeof(str) == 'undefined') {         \n        str = 'undefined';                    \n    } else if (str == null) {                 \n        str = 'null';                         \n    }                                         \n    var out = context.getWriter();            \n    if (!(out instanceof java.io.PrintWriter))\n        out = new java.io.PrintWriter(out);   \n    out.print(String(str));                   \n    if (newline) out.print('\\n');            \n    out.flush();                              \n}\nfunction println(str) {                       \n    print(str, true);                         \n}", "print", 1, (Object)null);
         } finally {
            Context.exit();
         }

         return newScope;
      }
   }

   public CompiledScript compile(String script) throws ScriptException {
      return this.compile((Reader)(new StringReader(script)));
   }

   public CompiledScript compile(Reader script) throws ScriptException {
      Context cx = Context.enter();

      CompiledScript ret;
      try {
         String fileName = (String)this.get("javax.script.filename");
         if (fileName == null) {
            fileName = "<Unknown Source>";
         }

         Script scr = cx.compileReader(script, fileName, 1, (Object)null);
         ret = new RhinoCompiledScript(this, scr);
      } catch (Exception e) {
         throw new ScriptException(e);
      } finally {
         Context.exit();
      }

      return ret;
   }

   AccessControlContext getAccessContext() {
      return this.accCtxt;
   }

   Object[] wrapArguments(Object[] args) {
      if (args == null) {
         return Context.emptyArgs;
      } else {
         Object[] res = new Object[args.length];

         for(int i = 0; i < res.length; ++i) {
            res[i] = Context.javaToJS(args[i], this.topLevel);
         }

         return res;
      }
   }

   Object unwrapReturnValue(Object result) {
      if (result instanceof Wrapper) {
         result = ((Wrapper)result).unwrap();
      }

      return result instanceof Undefined ? null : result;
   }

   static {
      ContextFactory.initGlobal(new ContextFactory() {
         protected Context makeContext() {
            Context cx = super.makeContext();
            cx.setLanguageVersion(200);
            cx.setOptimizationLevel(-1);
            cx.setClassShutter(RhinoClassShutter.getInstance());
            cx.setWrapFactory(RhinoWrapFactory.getInstance());
            return cx;
         }

         protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            AccessControlContext accCtxt = null;
            Scriptable global = ScriptableObject.getTopLevelScope(scope);
            Scriptable globalProto = global.getPrototype();
            if (globalProto instanceof RhinoTopLevel) {
               accCtxt = ((RhinoTopLevel)globalProto).getAccessContext();
            }

            return accCtxt != null ? AccessController.doPrivileged((PrivilegedAction<Object>)() -> this.superDoTopCall(callable, cx, scope, thisObj, args), accCtxt) : this.superDoTopCall(callable, cx, scope, thisObj, args);
         }

         private Object superDoTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
            return super.doTopCall(callable, cx, scope, thisObj, args);
         }
      });
   }
}
