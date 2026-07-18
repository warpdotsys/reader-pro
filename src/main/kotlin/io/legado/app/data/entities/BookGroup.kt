package io.legado.app.data.entities

data class BookGroup(
    var groupId: Long = 0,
    var groupName: String = "",
    var cover: String? = null,
    var order: Int = 0,
    var show: Boolean = true
)
