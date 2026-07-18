package com.script;

public interface Invocable {
   <T> T getInterface(Class<T> clazz);

   <T> T getInterface(Object thiz, Class<T> clazz);

   Object invokeFunction(String name, Object... args) throws ScriptException, NoSuchMethodException;

   Object invokeMethod(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException;
}
