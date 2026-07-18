package com.script;

import java.util.Map;

public interface Bindings extends Map<String, Object> {
   boolean containsKey(Object key);

   Object get(Object key);

   Object put(String name, Object value);

   void putAll(Map<? extends String, ? extends Object> toMerge);

   Object remove(Object key);
}
