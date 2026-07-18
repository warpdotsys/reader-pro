package com.script;

import java.io.Reader;

public interface Compilable {
   CompiledScript compile(Reader script) throws ScriptException;

   CompiledScript compile(String script) throws ScriptException;
}
