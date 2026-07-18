package me.ag2s.epublib.domain

import java.io.Serializable

class MediaType : Serializable {
    private val name: String?
    private val defaultExtension: String?
    private val extensions: Collection<String?>
    constructor(name: String?, defaultExtension: String?) : this(name, defaultExtension, arrayOf(defaultExtension))
    constructor(name: String?, defaultExtension: String?, extensions: Array<String?>) : this(name, defaultExtension, extensions.asList())
    constructor(name: String?, defaultExtension: String?, extensions: Collection<String?>) {
        this.name = name
        this.defaultExtension = defaultExtension
        this.extensions = extensions
    }
    fun getName(): String? = name
    fun getDefaultExtension(): String? = defaultExtension
    fun getExtensions(): Collection<String?> = extensions
    override fun hashCode(): Int = name?.hashCode() ?: 0
    override fun equals(other: Any?): Boolean = other is MediaType && name!!.equals(other.getName())
    @Suppress("UNCHECKED_CAST")
    override fun toString(): String = name as String
    companion object { private const val serialVersionUID = -7256091153727506788L }
}
