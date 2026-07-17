package me.ag2s.epublib.domain

enum class ManifestItemProperties(private val manifestName: String) : ManifestProperties {
    COVER_IMAGE("cover-image"), MATHML("mathml"), NAV("nav"), REMOTE_RESOURCES("remote-resources"), SCRIPTED("scripted"), SVG("svg"), SWITCH("switch");

    override fun getName(): String = manifestName
}
