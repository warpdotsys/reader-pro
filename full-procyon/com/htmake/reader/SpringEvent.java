// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader;

import org.springframework.context.ApplicationEvent;

public class SpringEvent extends ApplicationEvent
{
    private String event;
    private String message;
    
    public SpringEvent(final Object source, final String event, final String message) {
        super(source);
        this.event = event;
        this.message = message;
    }
    
    public String getEvent() {
        return this.event;
    }
    
    public void setEvent(final String event) {
        this.event = event;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
