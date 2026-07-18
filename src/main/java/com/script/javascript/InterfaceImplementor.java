package com.script.javascript;

import com.script.Invocable;
import com.script.ScriptException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class InterfaceImplementor {
   private Invocable engine;

   public InterfaceImplementor(Invocable engine) {
      this.engine = engine;
   }

   public <T> T getInterface(Object thiz, Class<T> iface) throws ScriptException {
      if (iface != null && iface.isInterface()) {
         if (!this.isImplemented(thiz, iface)) {
            return null;
         } else {
            AccessControlContext accCtxt = AccessController.getContext();
            return (T)iface.cast(Proxy.newProxyInstance(iface.getClassLoader(), new Class[]{iface}, new InterfaceImplementorInvocationHandler(thiz, accCtxt)));
         }
      } else {
         throw new IllegalArgumentException("interface Class expected");
      }
   }

   protected boolean isImplemented(Object thiz, Class<?> iface) {
      return true;
   }

   protected Object convertResult(Method method, Object res) throws ScriptException {
      return res;
   }

   protected Object[] convertArguments(Method method, Object[] args) throws ScriptException {
      return args;
   }

   private final class InterfaceImplementorInvocationHandler implements InvocationHandler {
      private Object thiz;
      private AccessControlContext accCtxt;

      public InterfaceImplementorInvocationHandler(Object thiz, AccessControlContext accCtxt) {
         this.thiz = thiz;
         this.accCtxt = accCtxt;
      }

      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         final Object[] fargs = InterfaceImplementor.this.convertArguments(method, args);
         Object result = AccessController.doPrivileged((PrivilegedExceptionAction<Object>)() -> this.thiz == null ? InterfaceImplementor.this.engine.invokeFunction(method.getName(), fargs) : InterfaceImplementor.this.engine.invokeMethod(this.thiz, method.getName(), fargs), this.accCtxt);
         return InterfaceImplementor.this.convertResult(method, result);
      }
   }
}
