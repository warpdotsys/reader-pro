package me.ag2s.epublib.browsersupport;

import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Resource;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/browsersupport/NavigationHistory.class */
public class NavigationHistory implements NavigationEventListener {
    public static final int DEFAULT_MAX_HISTORY_SIZE = 1000;
    private static final long DEFAULT_HISTORY_WAIT_TIME = 1000;
    private final Navigator navigator;
    private long lastUpdateTime = 0;
    private List<Location> locations = new ArrayList();
    private int currentPos = -1;
    private int currentSize = 0;
    private int maxHistorySize = DEFAULT_MAX_HISTORY_SIZE;
    private long historyWaitTime = DEFAULT_HISTORY_WAIT_TIME;

    /* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/browsersupport/NavigationHistory$Location.class */
    private static class Location {
        private String href;

        public Location(String href) {
            this.href = href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getHref() {
            return this.href;
        }
    }

    public NavigationHistory(Navigator navigator) {
        this.navigator = navigator;
        navigator.addNavigationEventListener(this);
        initBook(navigator.getBook());
    }

    public int getCurrentPos() {
        return this.currentPos;
    }

    public int getCurrentSize() {
        return this.currentSize;
    }

    public void initBook(EpubBook book) {
        if (book == null) {
            return;
        }
        this.locations = new ArrayList();
        this.currentPos = -1;
        this.currentSize = 0;
        if (this.navigator.getCurrentResource() != null) {
            addLocation(this.navigator.getCurrentResource().getHref());
        }
    }

    public long getHistoryWaitTime() {
        return this.historyWaitTime;
    }

    public void setHistoryWaitTime(long historyWaitTime) {
        this.historyWaitTime = historyWaitTime;
    }

    public void addLocation(Resource resource) {
        if (resource == null) {
            return;
        }
        addLocation(resource.getHref());
    }

    public void addLocation(Location location) {
        if (!this.locations.isEmpty() && location.getHref().equals(this.locations.get(this.currentPos).getHref())) {
            return;
        }
        this.currentPos++;
        if (this.currentPos != this.currentSize) {
            this.locations.set(this.currentPos, location);
        } else {
            this.locations.add(location);
            checkHistorySize();
        }
        this.currentSize = this.currentPos + 1;
    }

    private void checkHistorySize() {
        while (this.locations.size() > this.maxHistorySize) {
            this.locations.remove(0);
            this.currentSize--;
            this.currentPos--;
        }
    }

    public void addLocation(String href) {
        addLocation(new Location(href));
    }

    private String getLocationHref(int pos) {
        if (pos < 0 || pos >= this.locations.size()) {
            return null;
        }
        return this.locations.get(this.currentPos).getHref();
    }

    public boolean move(int delta) {
        if (this.currentPos + delta < 0 || this.currentPos + delta >= this.currentSize) {
            return false;
        }
        this.currentPos += delta;
        this.navigator.gotoResource(getLocationHref(this.currentPos), this);
        return true;
    }

    @Override // me.ag2s.epublib.browsersupport.NavigationEventListener
    public void navigationPerformed(NavigationEvent navigationEvent) {
        if (this == navigationEvent.getSource() || navigationEvent.getCurrentResource() == null) {
            return;
        }
        if (System.currentTimeMillis() - this.lastUpdateTime > this.historyWaitTime) {
            addLocation(navigationEvent.getOldResource());
            addLocation(navigationEvent.getCurrentResource().getHref());
        }
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public String getCurrentHref() {
        if (this.currentPos < 0 || this.currentPos >= this.locations.size()) {
            return null;
        }
        return this.locations.get(this.currentPos).getHref();
    }

    public void setMaxHistorySize(int maxHistorySize) {
        this.maxHistorySize = maxHistorySize;
    }

    public int getMaxHistorySize() {
        return this.maxHistorySize;
    }
}
