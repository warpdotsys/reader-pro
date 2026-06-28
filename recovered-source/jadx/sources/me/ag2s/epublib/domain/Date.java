package me.ag2s.epublib.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import me.ag2s.epublib.epub.PackageDocumentBase;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/Date.class */
public class Date implements Serializable {
    private static final long serialVersionUID = 7533866830395120136L;
    private Event event;
    private String dateString;

    /* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/Date$Event.class */
    public enum Event {
        PUBLICATION("publication"),
        MODIFICATION("modification"),
        CREATION("creation");

        private final String value;

        Event(String v) {
            this.value = v;
        }

        public static Event fromValue(String v) {
            for (Event c : values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            return null;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.value;
        }
    }

    public Date() {
        this(new java.util.Date(), Event.CREATION);
    }

    public Date(java.util.Date date) {
        this(date, (Event) null);
    }

    public Date(String dateString) {
        this(dateString, (Event) null);
    }

    public Date(java.util.Date date, Event event) {
        this(new SimpleDateFormat(PackageDocumentBase.dateFormat, Locale.US).format(date), event);
    }

    public Date(String dateString, Event event) {
        this.dateString = dateString;
        this.event = event;
    }

    public Date(java.util.Date date, String event) {
        this(new SimpleDateFormat(PackageDocumentBase.dateFormat, Locale.US).format(date), event);
    }

    public Date(String dateString, String event) {
        this(checkDate(dateString), Event.fromValue(event));
        this.dateString = dateString;
    }

    private static String checkDate(String dateString) {
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

    public void setEvent(Event event) {
        this.event = event;
    }

    public String toString() {
        if (this.event == null) {
            return this.dateString;
        }
        return PackageDocumentBase.PREFIX_OPF + this.event + ":" + this.dateString;
    }
}
