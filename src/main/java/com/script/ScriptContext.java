package com.script;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

public interface ScriptContext {
   int ENGINE_SCOPE = 100;
   int GLOBAL_SCOPE = 200;

   Object getAttribute(String name);

   Object getAttribute(String name, int scope);

   int getAttributesScope(String name);

   Bindings getBindings(int scope);

   Writer getErrorWriter();

   Reader getReader();

   List<Integer> getScopes();

   Writer getWriter();

   Object removeAttribute(String name, int scope);

   void setAttribute(String name, Object value, int scope);

   void setBindings(Bindings bindings, int scope);

   void setErrorWriter(Writer writer);

   void setReader(Reader reader);

   void setWriter(Writer writer);
}
