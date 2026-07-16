/**
 * Business-oriented rewrite from reader-pro-3.2.14.jar reverse engineering.
 * Source: CFR/Vineflower decompilation + manual semantic cleanup.
 * Purpose: readability / audit — not drop-in recompilation of the original APK/JAR.
 */
package com.htmake.reader.api

/**
 * Uniform API response envelope used by /reader3/* handlers.
 */
class ReturnData {
    var isSuccess: Boolean = true
    var errorMsg: String = ""
    var data: Any? = null

    fun setErrorMsg(msg: String): ReturnData {
        isSuccess = false
        errorMsg = msg
        return this
    }

    fun setData(value: Any?, ignored: Any? = null): ReturnData {
        data = value
        isSuccess = true
        return this
    }

    companion object {
        /** CFR: ReturnData.setData$default */
        @JvmStatic
        fun setDataDefault(rd: ReturnData, value: Any?, ignored: Any? = null, mask: Int = 2, conf: Any? = null): ReturnData {
            return rd.setData(value)
        }
    }
}
