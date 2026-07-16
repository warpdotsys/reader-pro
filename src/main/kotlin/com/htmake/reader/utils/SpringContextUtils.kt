package com.htmake.reader.utils

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringContextUtils : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        ctx = applicationContext
    }

    companion object {
        @Volatile private var ctx: ApplicationContext? = null

        fun <T> getBean(name: String, type: Class<T>): T =
            ctx?.getBean(name, type)
                ?: error("Spring context not ready for bean $name")

        fun <T> getBean(type: Class<T>): T =
            ctx?.getBean(type)
                ?: error("Spring context not ready for type ${type.name}")
    }
}
