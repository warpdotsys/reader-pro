package com.htmake.reader.lib.tts.service
import com.htmake.reader.lib.tts.constant.*
import com.htmake.reader.lib.tts.exceptions.TtsException
import com.htmake.reader.lib.tts.model.*
import com.htmake.reader.lib.tts.util.Tools
import java.util.concurrent.*
import okhttp3.*
import okio.*
import org.slf4j.LoggerFactory

class TTSService private constructor(private var outputFormat:OutputFormat?, private val usingAzureApi:Boolean) {
    private var synthesising=false; private var ws:WebSocket?=null; private var latch:CountDownLatch?=null; private val audioBuffer=Buffer(); private var client:OkHttpClient?=null
    private val listener=object:WebSocketListener(){ override fun onClosed(webSocket:WebSocket,code:Int,reason:String){ws=null;synthesising=false}; override fun onClosing(webSocket:WebSocket,code:Int,reason:String){ws=null;synthesising=false}; override fun onFailure(webSocket:WebSocket,t:Throwable,response:Response?){ws=null;synthesising=false}; override fun onMessage(webSocket:WebSocket,text:String){if(text.contains(TtsConstants.TURN_START))audioBuffer.clear() else if(text.contains(TtsConstants.TURN_END)){latch!!.countDown();synthesising=false}}; override fun onMessage(webSocket:WebSocket,bytes:ByteString){val audioIndex=bytes.lastIndexOf(TtsConstants.AUDIO_START.encodeToByteArray())+TtsConstants.AUDIO_START.length; val audioType=bytes.lastIndexOf(TtsConstants.AUDIO_CONTENT_TYPE.encodeToByteArray())+TtsConstants.AUDIO_CONTENT_TYPE.length!=-1; if(audioIndex!=-1&&audioType)try{audioBuffer.write(bytes.substring(audioIndex))}catch(_:Exception){}} }
    fun sendText(ssml:SSML):ByteArray { while(synthesising)Tools.sleep(1); latch=CountDownLatch(1);synthesising=true;if(ssml.getStyle()!=null&&!usingAzureApi)ssml.setStyle(null);if(ssml.getOutputFormat()!=null&&outputFormat!=ssml.getOutputFormat())sendConfig(ssml.getOutputFormat());if(!getOrCreateWs().send(ssml.toString()))throw TtsException.of("语音合成请求发送失败...");try{latch!!.await(30,TimeUnit.SECONDS);return audioBuffer.readByteArray()}catch(e:InterruptedException){throw RuntimeException(e)} }
    @Synchronized private fun getOrCreateWs():WebSocket { ws?.let{return it}; val url=if(usingAzureApi)"$AZURE?Retry-After=200&TrafficType=AzureDemo&Authorization=bearer undefined&X-ConnectionId=${Tools.getRandomId()}" else "$EDGE?Retry-After=200&TrustedClientToken=${TtsConstants.TRUSTED_CLIENT_TOKEN}&ConnectionId=${Tools.getRandomId()}"; val origin=if(usingAzureApi)TtsConstants.AZURE_SPEECH_ORIGIN else TtsConstants.EDGE_SPEECH_ORIGIN; return getClient().newWebSocket(Request.Builder().url(url).addHeader("User-Agent",TtsConstants.UA).addHeader("Origin",origin).build(),listener).also{ws=it;sendConfig(outputFormat)} }
    private fun getClient()=client?:OkHttpClient.Builder().pingInterval(20,TimeUnit.SECONDS).build().also{client=it}; private fun sendConfig(format:OutputFormat?){val config=SpeechConfig.of(format);if(!getOrCreateWs().send(config.toString()))throw TtsException.of("语音输出格式配置失败...");outputFormat=config.getOutputFormat()}
    class TTSServiceBuilder { private var outputFormat:OutputFormat?=null; private var usingAzureApi=false; fun usingOutputFormat(value:OutputFormat?)=apply{outputFormat=value}; fun usingAzureApi(value:Boolean)=apply{usingAzureApi=value}; fun build()=TTSService(outputFormat,usingAzureApi) }
    companion object { private const val EDGE="wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1"; private const val AZURE="wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1"; @JvmStatic fun builder()=TTSServiceBuilder() }
}
