/** Business rewrite from reader-pro-3.2.14.jar — readability / audit. */

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
        private lateinit var ctx: ApplicationContext
        @JvmStatic fun <T> getBean(clazz: Class<T>): T = ctx.getBean(clazz)
        @JvmStatic fun <T> getBean(name: String, clazz: Class<T>): T = ctx.getBean(name, clazz)
    }
}
