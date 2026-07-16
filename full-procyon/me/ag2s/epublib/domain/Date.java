// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.domain;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.io.Serializable;

public class Date implements Serializable
{
    private static final long serialVersionUID = 7533866830395120136L;
    private Event event;
    private String dateString;
    
    public Date() {
        this(new java.util.Date(), Event.CREATION);
    }
    
    public Date(final java.util.Date date) {
        this(date, (Event)null);
    }
    
    public Date(final String dateString) {
        this(dateString, (Event)null);
    }
    
    public Date(final java.util.Date date, final Event event) {
        this(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date), event);
    }
    
    public Date(final String dateString, final Event event) {
        this.dateString = dateString;
        this.event = event;
    }
    
    public Date(final java.util.Date date, final String event) {
        this(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date), event);
    }
    
    public Date(final String dateString, final String event) {
        this(checkDate(dateString), Event.fromValue(event));
        this.dateString = dateString;
    }
    
    private static String checkDate(final String dateString) {
        if (dateString == null) {
            throw new IllegalArgumentException("Cannot create a date from a blank string");
        }
        return dateString;
    }
    
    public String getValue() {
        return this.dateString;
    }
    
    public Event getEvent() {
        return this.event;
    }
    
    public void setEvent(final Event event) {
        this.event = event;
    }
    
    @Override
    public String toString() {
        if (this.event == null) {
            return this.dateString;
        }
        return "" + this.event + ":" + this.dateString;
    }
    
    public enum Event
    {
        PUBLICATION("publication"), 
        MODIFICATION("modification"), 
        CREATION("creation");
        
        private final String value;
        
        private Event(final String v) {
            this.value = v;
        }
        
        public static Event fromValue(final String v) {
            for (final Event c : values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            return null;
        }
        
        @Override
        public String toString() {
            return this.value;
        }
    }
}
