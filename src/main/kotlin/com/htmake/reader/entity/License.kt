package com.htmake.reader.entity
import java.util.UUID
data class License(var host:String="*",var userMaxLimit:Int=15,var expiredAt:Long=0,var openApi:Boolean=false,var simpleWebExpiredAt:Long=1688140799000L,var instances:Int=1,var type:String="default",var id:String=UUID.randomUUID().toString(),var code:String=UUID.randomUUID().toString(),var verified:Boolean=false,var verifyTime:Long?=null)
