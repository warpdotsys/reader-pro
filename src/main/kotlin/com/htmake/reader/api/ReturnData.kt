package com.htmake.reader.api

data class ReturnData(
    var isSuccess: Boolean = true,
    var errorMsg: String = "",
    var data: Any? = null
) {
    fun setData(v: Any?): ReturnData {
        data = v
        if (v != null && errorMsg.isEmpty()) isSuccess = true
        return this
    }

    fun setErrorMsg(msg: String): ReturnData {
        errorMsg = msg
        isSuccess = false
        return this
    }

    fun toMap(): Map<String, Any?> = mapOf(
        "isSuccess" to isSuccess,
        "errorMsg" to errorMsg,
        "data" to data
    )
}
