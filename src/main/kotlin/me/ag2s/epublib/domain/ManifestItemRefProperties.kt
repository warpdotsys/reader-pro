package me.ag2s.epublib.domain

enum class ManifestItemRefProperties(private val manifestName: String) : ManifestProperties {
    PAGE_SPREAD_LEFT("page-spread-left"),
    PAGE_SPREAD_RIGHT("page-spread-right");

    override fun getName(): String = manifestName
}
