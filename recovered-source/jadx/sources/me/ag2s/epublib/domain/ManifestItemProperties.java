package me.ag2s.epublib.domain;

/* JADX INFO: loaded from: app-classes.jar:me/ag2s/epublib/domain/ManifestItemProperties.class */
public enum ManifestItemProperties implements ManifestProperties {
    COVER_IMAGE("cover-image"),
    MATHML("mathml"),
    NAV("nav"),
    REMOTE_RESOURCES("remote-resources"),
    SCRIPTED("scripted"),
    SVG("svg"),
    SWITCH("switch");

    private final String name;

    ManifestItemProperties(String name) {
        this.name = name;
    }

    @Override // me.ag2s.epublib.domain.ManifestProperties
    public String getName() {
        return this.name;
    }
}
