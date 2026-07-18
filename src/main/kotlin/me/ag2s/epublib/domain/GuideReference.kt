package me.ag2s.epublib.domain

import java.io.Serializable
import me.ag2s.epublib.util.StringUtil

class GuideReference : TitledResourceReference, Serializable {
    private var type: String? = null
    constructor(resource: Resource?) : this(resource, null)
    constructor(resource: Resource?, title: String?) : super(resource, title)
    constructor(resource: Resource?, type: String?, title: String?) : this(resource, type, title, null)
    constructor(resource: Resource?, type: String?, title: String?, fragmentId: String?) : super(resource, title, fragmentId) { this.type = if (StringUtil.isNotBlank(type)) type!!.lowercase() else null }
    fun getType(): String? = type
    fun setType(type: String?) { this.type = type }
    companion object {
        private const val serialVersionUID = -316179702440631834L
        const val COVER = "cover"
        @JvmField var TITLE_PAGE = "title-page"; @JvmField var TOC = "toc"; @JvmField var INDEX = "index"; @JvmField var GLOSSARY = "glossary"; @JvmField var ACKNOWLEDGEMENTS = "acknowledgements"; @JvmField var BIBLIOGRAPHY = "bibliography"; @JvmField var COLOPHON = "colophon"; @JvmField var COPYRIGHT_PAGE = "copyright-page"; @JvmField var DEDICATION = "dedication"; @JvmField var EPIGRAPH = "epigraph"; @JvmField var FOREWORD = "foreword"; @JvmField var LOI = "loi"; @JvmField var LOT = "lot"; @JvmField var NOTES = "notes"; @JvmField var PREFACE = "preface"; @JvmField var TEXT = "text"
    }
}
