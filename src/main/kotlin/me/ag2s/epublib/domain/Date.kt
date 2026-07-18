package me.ag2s.epublib.domain

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Locale

class Date : Serializable {
    private var event: Event?
    private var dateString: String?
    constructor() : this(java.util.Date(), Event.CREATION)
    constructor(date: java.util.Date) : this(date, null as Event?)
    constructor(dateString: String?) : this(dateString, null as Event?)
    constructor(date: java.util.Date, event: Event?) : this(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date), event)
    constructor(dateString: String?, event: Event?) { this.dateString = dateString; this.event = event }
    constructor(date: java.util.Date, event: String?) : this(SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date), event)
    constructor(dateString: String?, event: String?) : this(checkDate(dateString), Event.fromValue(event)) { this.dateString = dateString }
    fun getValue(): String? = dateString
    fun getEvent(): Event? = event
    fun setEvent(event: Event?) { this.event = event }
    override fun toString(): String = if (event == null) dateString as String else "$event:$dateString"
    enum class Event(private val value: String) { PUBLICATION("publication"), MODIFICATION("modification"), CREATION("creation"); override fun toString() = value; companion object { @JvmStatic fun fromValue(value: String?): Event? = entries.firstOrNull { it.value == value } } }
    companion object { private const val serialVersionUID = 7533866830395120136L; private fun checkDate(dateString: String?): String { require(dateString != null) { "Cannot create a date from a blank string" }; return dateString } }
}
