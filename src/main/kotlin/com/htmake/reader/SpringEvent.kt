package com.htmake.reader

import org.springframework.context.ApplicationEvent

class SpringEvent(source: Any, var event: String?, var message: String?) : ApplicationEvent(source)
