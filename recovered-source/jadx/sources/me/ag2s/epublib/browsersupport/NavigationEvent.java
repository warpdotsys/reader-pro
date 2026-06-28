package me.ag2s.epublib.browsersupport;

import java.util.EventObject;
import me.ag2s.epublib.domain.EpubBook;
import me.ag2s.epublib.domain.Resource;
import me.ag2s.epublib.util.StringUtil;

/* JADX INFO: loaded from: reader-pro-classes-3.2.14.jar:me/ag2s/epublib/browsersupport/NavigationEvent.class */
public class NavigationEvent extends EventObject {
    private static final long serialVersionUID = -6346750144308952762L;
    private Resource oldResource;
    private int oldSpinePos;
    private Navigator navigator;
    private EpubBook oldBook;
    private int oldSectionPos;
    private String oldFragmentId;

    public NavigationEvent(Object source) {
        super(source);
    }

    public NavigationEvent(Object source, Navigator navigator) {
        super(source);
        this.navigator = navigator;
        this.oldBook = navigator.getBook();
        this.oldFragmentId = navigator.getCurrentFragmentId();
        this.oldSectionPos = navigator.getCurrentSectionPos();
        this.oldResource = navigator.getCurrentResource();
        this.oldSpinePos = navigator.getCurrentSpinePos();
    }

    public int getOldSectionPos() {
        return this.oldSectionPos;
    }

    public Navigator getNavigator() {
        return this.navigator;
    }

    public String getOldFragmentId() {
        return this.oldFragmentId;
    }

    void setOldFragmentId(String oldFragmentId) {
        this.oldFragmentId = oldFragmentId;
    }

    public EpubBook getOldBook() {
        return this.oldBook;
    }

    void setOldPagePos(int oldPagePos) {
        this.oldSectionPos = oldPagePos;
    }

    public int getCurrentSectionPos() {
        return this.navigator.getCurrentSectionPos();
    }

    public int getOldSpinePos() {
        return this.oldSpinePos;
    }

    public int getCurrentSpinePos() {
        return this.navigator.getCurrentSpinePos();
    }

    public String getCurrentFragmentId() {
        return this.navigator.getCurrentFragmentId();
    }

    public boolean isBookChanged() {
        return this.oldBook == null || this.oldBook != this.navigator.getBook();
    }

    public boolean isSpinePosChanged() {
        return getOldSpinePos() != getCurrentSpinePos();
    }

    public boolean isFragmentChanged() {
        return StringUtil.equals(getOldFragmentId(), getCurrentFragmentId());
    }

    public Resource getOldResource() {
        return this.oldResource;
    }

    public Resource getCurrentResource() {
        return this.navigator.getCurrentResource();
    }

    public void setOldResource(Resource oldResource) {
        this.oldResource = oldResource;
    }

    public void setOldSpinePos(int oldSpinePos) {
        this.oldSpinePos = oldSpinePos;
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public void setOldBook(EpubBook oldBook) {
        this.oldBook = oldBook;
    }

    public EpubBook getCurrentBook() {
        return getNavigator().getBook();
    }

    public boolean isResourceChanged() {
        return this.oldResource != getCurrentResource();
    }

    @Override // java.util.EventObject
    public String toString() {
        return StringUtil.toString("oldSectionPos", Integer.valueOf(this.oldSectionPos), "oldResource", this.oldResource, "oldBook", this.oldBook, "oldFragmentId", this.oldFragmentId, "oldSpinePos", Integer.valueOf(this.oldSpinePos), "currentPagePos", Integer.valueOf(getCurrentSectionPos()), "currentResource", getCurrentResource(), "currentBook", getCurrentBook(), "currentFragmentId", getCurrentFragmentId(), "currentSpinePos", Integer.valueOf(getCurrentSpinePos()));
    }

    public boolean isSectionPosChanged() {
        return this.oldSectionPos != getCurrentSectionPos();
    }
}
