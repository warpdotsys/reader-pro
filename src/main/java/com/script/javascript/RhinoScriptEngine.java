package com.script.javascript;

import java.io.IOException;
import java.io.Reader;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoScriptEngine extends AbstractScriptEngine {
    @Override
    public Object eval(String script, ScriptContext context) throws ScriptException {
        Context rhino = Context.enter();
        try {
            Scriptable scope = rhino.initStandardObjects();
            Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
            if (bindings != null) {
                for (var entry : bindings.entrySet()) {
                    ScriptableObject.putProperty(scope, entry.getKey(), entry.getValue());
                }
            }
            return Context.jsToJava(rhino.evaluateString(scope, script, "script", 1, null), Object.class);
        } catch (RhinoException error) {
            throw new ScriptException(error);
        } finally {
            Context.exit();
        }
    }

    @Override
    public Object eval(Reader reader, ScriptContext context) throws ScriptException {
        try {
            StringBuilder script = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                script.append(buffer, 0, read);
            }
            return eval(script.toString(), context);
        } catch (IOException error) {
            throw new ScriptException(error);
        }
    }

    @Override
    public Bindings createBindings() {
        return new SimpleBindings();
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return null;
    }
}
