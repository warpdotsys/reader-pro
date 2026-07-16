// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContextAware;

@Component
public class SpringContextUtils implements ApplicationContextAware
{
    private static ApplicationContext applicationContext;
    
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        SpringContextUtils.applicationContext = context;
    }
    
    public static ApplicationContext getApplicationContext() {
        return SpringContextUtils.applicationContext;
    }
    
    public static Object getBean(final String name) {
        if (SpringContextUtils.applicationContext != null) {
            return getApplicationContext().getBean(name);
        }
        return null;
    }
    
    public static <T> T getBean(final Class<T> clazz) {
        if (SpringContextUtils.applicationContext != null) {
            return (T)getApplicationContext().getBean((Class)clazz);
        }
        return null;
    }
    
    public static <T> T getBean(final String name, final Class<T> clazz) {
        if (SpringContextUtils.applicationContext != null) {
            return (T)getApplicationContext().getBean(name, (Class)clazz);
        }
        return null;
    }
}
