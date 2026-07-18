package com.script.javascript;

import com.script.Bindings;
import com.script.ScriptContext;
import com.script.SimpleScriptContext;
import java.security.AccessControlContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.LazilyLoadedCtor;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Synchronizer;
import org.mozilla.javascript.Wrapper;

public final class RhinoTopLevel extends ImporterTopLevel {
   private final RhinoScriptEngine engine;

   RhinoTopLevel(Context cx, RhinoScriptEngine engine) {
      super(cx, System.getSecurityManager() != null);
      this.engine = engine;
      new LazilyLoadedCtor(this, "JSAdapter", "com.sun.script.javascript.JSAdapter", false);
      JavaAdapter.init(cx, this, false);
      String[] names = new String[]{"bindings", "scope", "sync"};
      this.defineFunctionProperties(names, RhinoTopLevel.class, 2);
   }

   public static Object bindings(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      if (args.length == 1) {
         Object arg = args[0];
         if (arg instanceof Wrapper) {
            arg = ((Wrapper)arg).unwrap();
         }

         if (arg instanceof ExternalScriptable) {
            ScriptContext ctx = ((ExternalScriptable)arg).getContext();
            Bindings bind = ctx.getBindings(100);
            return Context.javaToJS(bind, ScriptableObject.getTopLevelScope(thisObj));
         }
      }

      return Context.getUndefinedValue();
   }

   public static Object scope(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      if (args.length == 1) {
         Object arg = args[0];
         if (arg instanceof Wrapper) {
            arg = ((Wrapper)arg).unwrap();
         }

         if (arg instanceof Bindings) {
            ScriptContext ctx = new SimpleScriptContext();
            ctx.setBindings((Bindings)arg, 100);
            Scriptable res = new ExternalScriptable(ctx);
            res.setPrototype(ScriptableObject.getObjectPrototype(thisObj));
            res.setParentScope(ScriptableObject.getTopLevelScope(thisObj));
            return res;
         }
      }

      return Context.getUndefinedValue();
   }

   public static Object sync(Context cx, Scriptable thisObj, Object[] args, Function funObj) {
      if (args.length == 1 && args[0] instanceof Function) {
         return new Synchronizer((Function)args[0]);
      } else {
         throw Context.reportRuntimeError("wrong argument(s) for sync");
      }
   }

   RhinoScriptEngine getScriptEngine() {
      return this.engine;
   }

   public AccessControlContext getAccessContext() {
      return this.engine.getAccessContext();
   }
}
