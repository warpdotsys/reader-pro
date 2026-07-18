package com.script;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleScriptContext implements ScriptContext {
   private static List<Integer> scopes = new ArrayList<>(2);
   protected Bindings engineScope = new SimpleBindings();
   protected Writer errorWriter;
   protected Bindings globalScope;
   protected Reader reader;
   protected Writer writer;

   public SimpleScriptContext() {
      this.errorWriter = new PrintWriter(System.err, true);
      this.globalScope = null;
      this.reader = new InputStreamReader(System.in);
      this.writer = new PrintWriter(System.out, true);
   }

   public void setBindings(Bindings bindings, int scope) {
      switch (scope) {
         case 100:
            if (bindings == null) {
               throw new NullPointerException("Engine scope cannot be null.");
            }

            this.engineScope = bindings;
            return;
         case 200:
            this.globalScope = bindings;
            return;
         default:
            throw new IllegalArgumentException("Invalid scope value.");
      }
   }

   public Object getAttribute(String name) {
      if (this.engineScope.containsKey(name)) {
         return this.getAttribute(name, 100);
      } else {
         return this.globalScope != null && this.globalScope.containsKey(name) ? this.getAttribute(name, 200) : null;
      }
   }

   public Object getAttribute(String name, int scope) {
      switch (scope) {
         case 100:
            return this.engineScope.get(name);
         case 200:
            if (this.globalScope != null) {
               return this.globalScope.get(name);
            }

            return null;
         default:
            throw new IllegalArgumentException("Illegal scope value.");
      }
   }

   public Object removeAttribute(String name, int scope) {
      switch (scope) {
         case 100:
            if (this.getBindings(100) != null) {
               return this.getBindings(100).remove(name);
            }

            return null;
         case 200:
            if (this.getBindings(200) != null) {
               return this.getBindings(200).remove(name);
            }

            return null;
         default:
            throw new IllegalArgumentException("Illegal scope value.");
      }
   }

   public void setAttribute(String name, Object value, int scope) {
      switch (scope) {
         case 100:
            this.engineScope.put(name, value);
            return;
         case 200:
            if (this.globalScope != null) {
               this.globalScope.put(name, value);
               return;
            }

            return;
         default:
            throw new IllegalArgumentException("Illegal scope value.");
      }
   }

   public Writer getWriter() {
      return this.writer;
   }

   public Reader getReader() {
      return this.reader;
   }

   public void setReader(Reader reader2) {
      this.reader = reader2;
   }

   public void setWriter(Writer writer2) {
      this.writer = writer2;
   }

   public Writer getErrorWriter() {
      return this.errorWriter;
   }

   public void setErrorWriter(Writer writer2) {
      this.errorWriter = writer2;
   }

   public int getAttributesScope(String name) {
      if (this.engineScope.containsKey(name)) {
         return 100;
      } else {
         return this.globalScope != null && this.globalScope.containsKey(name) ? 200 : -1;
      }
   }

   public Bindings getBindings(int scope) {
      if (scope == 100) {
         return this.engineScope;
      } else if (scope == 200) {
         return this.globalScope;
      } else {
         throw new IllegalArgumentException("Illegal scope value.");
      }
   }

   public List<Integer> getScopes() {
      return scopes;
   }

   static {
      scopes.add(100);
      scopes.add(200);
      scopes = Collections.unmodifiableList(scopes);
   }
}
