package com.script;

import java.io.Reader;

public interface ScriptEngine {
   String FILENAME = "javax.script.filename";

   Bindings createBindings();

   Object eval(Reader reader) throws ScriptException;

   Object eval(Reader reader, Bindings n) throws ScriptException;

   Object eval(Reader reader, ScriptContext context) throws ScriptException;

   Object eval(String script) throws ScriptException;

   Object eval(String script, Bindings n) throws ScriptException;

   Object eval(String script, ScriptContext context) throws ScriptException;

   Object get(String key);

   Bindings getBindings(int scope);

   ScriptContext getContext();

   void put(String key, Object value);

   void setBindings(Bindings bindings, int scope);

   void setContext(ScriptContext context);
}
