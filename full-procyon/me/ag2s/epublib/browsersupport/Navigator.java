// 
// Decompiled by Procyon v0.6.0
// 

package me.ag2s.epublib.browsersupport;

import java.util.ArrayList;
import java.util.List;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.domain.EpubBook;
import java.io.Serializable;

public class Navigator implements Serializable
{
    private static final long serialVersionUID = 1076126986424925474L;
    private EpubBook book;
    private int currentSpinePos;
    private Resource currentResource;
    private int currentPagePos;
    private String currentFragmentId;
    private final List<NavigationEventListener> eventListeners;
    
    public Navigator() {
        this(null);
    }
    
    public Navigator(final EpubBook book) {
        this.eventListeners = new ArrayList<NavigationEventListener>();
        this.book = book;
        this.currentSpinePos = 0;
        if (book != null) {
            this.currentResource = book.getCoverPage();
        }
        this.currentPagePos = 0;
    }
    
    private synchronized void handleEventListeners(final NavigationEvent navigationEvent) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            final NavigationEventListener navigationEventListener = this.eventListeners.get(i);
            navigationEventListener.navigationPerformed(navigationEvent);
        }
    }
    
    public boolean addNavigationEventListener(final NavigationEventListener navigationEventListener) {
        return this.eventListeners.add(navigationEventListener);
    }
    
    public boolean removeNavigationEventListener(final NavigationEventListener navigationEventListener) {
        return this.eventListeners.remove(navigationEventListener);
    }
    
    public int gotoFirstSpineSection(final Object source) {
        return this.gotoSpineSection(0, source);
    }
    
    public int gotoPreviousSpineSection(final Object source) {
        return this.gotoPreviousSpineSection(0, source);
    }
    
    public int gotoPreviousSpineSection(final int pagePos, final Object source) {
        if (this.currentSpinePos < 0) {
            return this.gotoSpineSection(0, pagePos, source);
        }
        return this.gotoSpineSection(this.currentSpinePos - 1, pagePos, source);
    }
    
    public boolean hasNextSpineSection() {
        return this.currentSpinePos < this.book.getSpine().size() - 1;
    }
    
    public boolean hasPreviousSpineSection() {
        return this.currentSpinePos > 0;
    }
    
    public int gotoNextSpineSection(final Object source) {
        if (this.currentSpinePos < 0) {
            return this.gotoSpineSection(0, source);
        }
        return this.gotoSpineSection(this.currentSpinePos + 1, source);
    }
    
    public int gotoResource(final String resourceHref, final Object source) {
        final Resource resource = this.book.getResources().getByHref(resourceHref);
        return this.gotoResource(resource, source);
    }
    
    public int gotoResource(final Resource resource, final Object source) {
        return this.gotoResource(resource, 0, null, source);
    }
    
    public int gotoResource(final Resource resource, final String fragmentId, final Object source) {
        return this.gotoResource(resource, 0, fragmentId, source);
    }
    
    public int gotoResource(final Resource resource, final int pagePos, final Object source) {
        return this.gotoResource(resource, pagePos, null, source);
    }
    
    public int gotoResource(final Resource resource, final int pagePos, final String fragmentId, final Object source) {
        if (resource == null) {
            return -1;
        }
        final NavigationEvent navigationEvent = new NavigationEvent(source, this);
        this.currentResource = resource;
        this.currentSpinePos = this.book.getSpine().getResourceIndex(this.currentResource);
        this.currentPagePos = pagePos;
        this.currentFragmentId = fragmentId;
        this.handleEventListeners(navigationEvent);
        return this.currentSpinePos;
    }
    
    public int gotoResourceId(final String resourceId, final Object source) {
        return this.gotoSpineSection(this.book.getSpine().findFirstResourceById(resourceId), source);
    }
    
    public int gotoSpineSection(final int newSpinePos, final Object source) {
        return this.gotoSpineSection(newSpinePos, 0, source);
    }
    
    public int gotoSpineSection(final int newSpinePos, final int newPagePos, final Object source) {
        if (newSpinePos == this.currentSpinePos) {
            return this.currentSpinePos;
        }
        if (newSpinePos < 0 || newSpinePos >= this.book.getSpine().size()) {
            return this.currentSpinePos;
        }
        final NavigationEvent navigationEvent = new NavigationEvent(source, this);
        this.currentSpinePos = newSpinePos;
        this.currentPagePos = newPagePos;
        this.currentResource = this.book.getSpine().getResource(this.currentSpinePos);
        this.handleEventListeners(navigationEvent);
        return this.currentSpinePos;
    }
    
    public int gotoLastSpineSection(final Object source) {
        return this.gotoSpineSection(this.book.getSpine().size() - 1, source);
    }
    
    public void gotoBook(final EpubBook book, final Object source) {
        final NavigationEvent navigationEvent = new NavigationEvent(source, this);
        this.book = book;
        this.currentFragmentId = null;
        this.currentPagePos = 0;
        this.currentResource = null;
        this.currentSpinePos = book.getSpine().getResourceIndex(this.currentResource);
        this.handleEventListeners(navigationEvent);
    }
    
    public int getCurrentSpinePos() {
        return this.currentSpinePos;
    }
    
    public Resource getCurrentResource() {
        return this.currentResource;
    }
    
    public void setCurrentSpinePos(final int currentIndex) {
        this.currentSpinePos = currentIndex;
        this.currentResource = this.book.getSpine().getResource(currentIndex);
    }
    
    public EpubBook getBook() {
        return this.book;
    }
    
    public int setCurrentResource(final Resource currentResource) {
        this.currentSpinePos = this.book.getSpine().getResourceIndex(currentResource);
        this.currentResource = currentResource;
        return this.currentSpinePos;
    }
    
    public String getCurrentFragmentId() {
        return this.currentFragmentId;
    }
    
    public int getCurrentSectionPos() {
        return this.currentPagePos;
    }
}
