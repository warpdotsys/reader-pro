package com.htmake.reader.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {
   private static ApplicationContext applicationContext;

   @Override
   public void setApplicationContext(ApplicationContext context) throws BeansException {
      applicationContext = context;
   }

   public static ApplicationContext getApplicationContext() {
      return applicationContext;
   }

   public static Object getBean(String name) {
      return applicationContext != null ? getApplicationContext().getBean(name) : null;
   }

   public static <T> T getBean(Class<T> clazz) {
      return applicationContext != null ? getApplicationContext().getBean(clazz) : null;
   }

   public static <T> T getBean(String name, Class<T> clazz) {
      return applicationContext != null ? getApplicationContext().getBean(name, clazz) : null;
   }
}
