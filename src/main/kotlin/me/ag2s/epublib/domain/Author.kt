package me.ag2s.epublib.domain

import java.io.Serializable
import me.ag2s.epublib.util.StringUtil

class Author(firstname: String?, lastname: String?) : Serializable {
    private var firstname = firstname
    private var lastname = lastname
    private var relator = Relator.AUTHOR
    constructor(singleName: String?) : this("", singleName)
    fun getFirstname() = firstname; fun setFirstname(value: String?) { firstname = value }
    fun getLastname() = lastname; fun setLastname(value: String?) { lastname = value }
    override fun toString(): String = "$lastname, $firstname"
    override fun hashCode(): Int = StringUtil.hashCode(firstname, lastname)
    override fun equals(other: Any?): Boolean = other is Author && StringUtil.equals(firstname, other.firstname) && StringUtil.equals(lastname, other.lastname)
    fun setRole(code: String?) { relator = Relator.byCode(code) ?: Relator.AUTHOR }
    fun getRelator() = relator; fun setRelator(value: Relator) { relator = value }
    companion object { private const val serialVersionUID = 6663408501416574200L }
}
