//
// Decompiled by Procyon v0.6.0
//

package me.ag2s.epublib.domain;

import me.ag2s.epublib.util.StringUtil;
import java.io.Serializable;

public class GuideReference extends TitledResourceReference implements Serializable
{
    private static final long serialVersionUID = -316179702440631834L;
    public static final String COVER = "cover";
    public static String TITLE_PAGE;
    public static String TOC;
    public static String INDEX;
    public static String GLOSSARY;
    public static String ACKNOWLEDGEMENTS;
    public static String BIBLIOGRAPHY;
    public static String COLOPHON;
    public static String COPYRIGHT_PAGE;
    public static String DEDICATION;
    public static String EPIGRAPH;
    public static String FOREWORD;
    public static String LOI;
    public static String LOT;
    public static String NOTES;
    public static String PREFACE;
    public static String TEXT;
    private String type;

    public GuideReference(final Resource resource) {
        this(resource, null);
    }

    public GuideReference(final Resource resource, final String title) {
        super(resource, title);
    }

    public GuideReference(final Resource resource, final String type, final String title) {
        this(resource, type, title, null);
    }

    public GuideReference(final Resource resource, final String type, final String title, final String fragmentId) {
        super(resource, title, fragmentId);
        this.type = (StringUtil.isNotBlank(type) ? type.toLowerCase() : null);
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    static {
        GuideReference.TITLE_PAGE = "title-page";
        GuideReference.TOC = "toc";
        GuideReference.INDEX = "index";
        GuideReference.GLOSSARY = "glossary";
        GuideReference.ACKNOWLEDGEMENTS = "acknowledgements";
        GuideReference.BIBLIOGRAPHY = "bibliography";
        GuideReference.COLOPHON = "colophon";
        GuideReference.COPYRIGHT_PAGE = "copyright-page";
        GuideReference.DEDICATION = "dedication";
        GuideReference.EPIGRAPH = "epigraph";
        GuideReference.FOREWORD = "foreword";
        GuideReference.LOI = "loi";
        GuideReference.LOT = "lot";
        GuideReference.NOTES = "notes";
        GuideReference.PREFACE = "preface";
        GuideReference.TEXT = "text";
    }
}
