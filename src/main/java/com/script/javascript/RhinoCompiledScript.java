package com.script.javascript;

import com.script.CompiledScript;
import com.script.ScriptContext;
import com.script.ScriptEngine;
import com.script.ScriptException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

final class RhinoCompiledScript extends CompiledScript {
   private RhinoScriptEngine engine;
   private Script script;

   RhinoCompiledScript(RhinoScriptEngine engine, Script script) {
      this.engine = engine;
      this.script = script;
   }

   public Object eval(ScriptContext context) throws ScriptException {
      Context cx = Context.enter();

      Object result;
      try {
         Scriptable scope = this.engine.getRuntimeScope(context);
         Object ret = this.script.exec(cx, scope);
         result = this.engine.unwrapReturnValue(ret);
      } catch (RhinoException var11) {
         int line;
         line = (line = var11.lineNumber()) == 0 ? -1 : line;
         String msg;
         if (var11 instanceof JavaScriptException) {
            msg = String.valueOf(((JavaScriptException)var11).getValue());
         } else {
            msg = var11.toString();
         }

         ScriptException se = new ScriptException(msg, var11.sourceName(), line);
         se.initCause(var11);
         throw se;
      } finally {
         Context.exit();
      }

      return result;
   }

   public ScriptEngine getEngine() {
      return this.engine;
   }
}
