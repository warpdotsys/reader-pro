package com.htmake.reader.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:com/htmake/reader/utils/SpringContextUtils.class */
@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        if (applicationContext != null) {
            return getApplicationContext().getBean(name);
        }
        return null;
    }

    public static <T> T getBean(Class<T> cls) {
        if (applicationContext != null) {
            return (T) getApplicationContext().getBean(cls);
        }
        return null;
    }

    public static <T> T getBean(String str, Class<T> cls) {
        if (applicationContext != null) {
            return (T) getApplicationContext().getBean(str, cls);
        }
        return null;
    }
}
