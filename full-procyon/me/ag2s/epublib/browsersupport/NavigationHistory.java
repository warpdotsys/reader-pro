// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.browsersupport;

import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.EpubBook;
import java.util.ArrayList;
import java.util.List;

public class NavigationHistory implements NavigationEventListener
{
    public static final int DEFAULT_MAX_HISTORY_SIZE = 1000;
    private static final long DEFAULT_HISTORY_WAIT_TIME = 1000L;
    private long lastUpdateTime;
    private List<Location> locations;
    private final Navigator navigator;
    private int currentPos;
    private int currentSize;
    private int maxHistorySize;
    private long historyWaitTime;
    
    public NavigationHistory(final Navigator navigator) {
        this.lastUpdateTime = 0L;
        this.locations = new ArrayList<Location>();
        this.currentPos = -1;
        this.currentSize = 0;
        this.maxHistorySize = 1000;
        this.historyWaitTime = 1000L;
        (this.navigator = navigator).addNavigationEventListener(this);
        this.initBook(navigator.getBook());
    }
    
    public int getCurrentPos() {
        return this.currentPos;
    }
    
    public int getCurrentSize() {
        return this.currentSize;
    }
    
    public void initBook(final EpubBook book) {
        if (book == null) {
            return;
        }
        this.locations = new ArrayList<Location>();
        this.currentPos = -1;
        this.currentSize = 0;
        if (this.navigator.getCurrentResource() != null) {
            this.addLocation(this.navigator.getCurrentResource().getHref());
        }
    }
    
    public long getHistoryWaitTime() {
        return this.historyWaitTime;
    }
    
    public void setHistoryWaitTime(final long historyWaitTime) {
        this.historyWaitTime = historyWaitTime;
    }
    
    public void addLocation(final Resource resource) {
        if (resource == null) {
            return;
        }
        this.addLocation(resource.getHref());
    }
    
    public void addLocation(final Location location) {
        if (!this.locations.isEmpty() && location.getHref().equals(this.locations.get(this.currentPos).getHref())) {
            return;
        }
        ++this.currentPos;
        if (this.currentPos != this.currentSize) {
            this.locations.set(this.currentPos, location);
        }
        else {
            this.locations.add(location);
            this.checkHistorySize();
        }
        this.currentSize = this.currentPos + 1;
    }
    
    private void checkHistorySize() {
        while (this.locations.size() > this.maxHistorySize) {
            this.locations.remove(0);
            --this.currentSize;
            --this.currentPos;
        }
    }
    
    public void addLocation(final String href) {
        this.addLocation(new Location(href));
    }
    
    private String getLocationHref(final int pos) {
        if (pos < 0 || pos >= this.locations.size()) {
            return null;
        }
        return this.locations.get(this.currentPos).getHref();
    }
    
    public boolean move(final int delta) {
        if (this.currentPos + delta < 0 || this.currentPos + delta >= this.currentSize) {
            return false;
        }
        this.currentPos += delta;
        this.navigator.gotoResource(this.getLocationHref(this.currentPos), this);
        return true;
    }
    
    @Override
    public void navigationPerformed(final NavigationEvent navigationEvent) {
        if (this == navigationEvent.getSource()) {
            return;
        }
        if (navigationEvent.getCurrentResource() == null) {
            return;
        }
        if (System.currentTimeMillis() - this.lastUpdateTime > this.historyWaitTime) {
            this.addLocation(navigationEvent.getOldResource());
            this.addLocation(navigationEvent.getCurrentResource().getHref());
        }
        this.lastUpdateTime = System.currentTimeMillis();
    }
    
    public String getCurrentHref() {
        if (this.currentPos < 0 || this.currentPos >= this.locations.size()) {
            return null;
        }
        return this.locations.get(this.currentPos).getHref();
    }
    
    public void setMaxHistorySize(final int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }
    
    public int getMaxHistorySize() {
        return this.maxHistorySize;
    }
    
    private static class Location
    {
        private String href;
        
        public Location(final String href) {
            this.href = href;
        }
        
        public void setHref(final String href) {
            this.href = href;
        }
        
        public String getHref() {
            return this.href;
        }
    }
}
