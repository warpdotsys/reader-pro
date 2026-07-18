package me.ag2s.epublib.domain

import java.io.Serializable
import java.util.UUID
import me.ag2s.epublib.util.StringUtil

class Identifier : Serializable {
    private var bookId = false
    private var scheme: String?
    private var value: String?
    constructor() : this("UUID", UUID.randomUUID().toString())
    constructor(scheme: String?, value: String?) { this.scheme = scheme; this.value = value }
    fun getScheme(): String? = scheme
    fun setScheme(scheme: String?) { this.scheme = scheme }
    fun getValue(): String? = value
    fun setValue(value: String?) { this.value = value }
    fun setBookId(bookId: Boolean) { this.bookId = bookId }
    fun isBookId(): Boolean = bookId
    override fun hashCode(): Int = StringUtil.defaultIfNull(scheme).hashCode() xor StringUtil.defaultIfNull(value).hashCode()
    override fun equals(other: Any?): Boolean = other is Identifier && StringUtil.equals(scheme, other.scheme) && StringUtil.equals(value, other.value)
    override fun toString(): String = if (StringUtil.isBlank(scheme)) "" + value else "" + scheme + ":" + value
    interface Scheme { companion object { const val UUID = "UUID"; const val ISBN = "ISBN"; const val URL = "URL"; const val URI = "URI" } }
    companion object { private const val serialVersionUID = 955949951416391810L; @JvmStatic fun getBookIdIdentifier(identifiers: List<Identifier>?): Identifier? { if (identifiers.isNullOrEmpty()) return null; return identifiers.firstOrNull { it.isBookId() } ?: identifiers[0] } }
}
