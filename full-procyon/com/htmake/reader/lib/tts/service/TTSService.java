// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.lib.tts.service;

import org.slf4j.LoggerFactory;
import com.htmake.reader.lib.tts.model.SpeechConfig;
import okhttp3.Request;
import java.util.concurrent.TimeUnit;
import com.htmake.reader.lib.tts.exceptions.TtsException;
import com.htmake.reader.lib.tts.constant.TtsStyleEnum;
import java.util.Objects;
import com.htmake.reader.lib.tts.util.Tools;
import com.htmake.reader.lib.tts.model.SSML;
import java.nio.charset.StandardCharsets;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import okhttp3.Response;
import okhttp3.WebSocketListener;
import java.util.concurrent.CountDownLatch;
import okhttp3.WebSocket;
import okhttp3.OkHttpClient;
import okio.Buffer;
import com.htmake.reader.lib.tts.constant.OutputFormat;
import org.slf4j.Logger;

public class TTSService
{
    public static final Logger log;
    private OutputFormat outputFormat;
    private boolean usingAzureApi;
    private volatile boolean synthesising;
    private String currentText;
    private final Buffer audioBuffer;
    private OkHttpClient okHttpClient;
    private WebSocket ws;
    private CountDownLatch latch;
    protected WebSocketListener webSocketListener;
    
    private TTSService(final OutputFormat outputFormat, final boolean usingAzureApi) {
        this.audioBuffer = new Buffer();
        this.webSocketListener = new WebSocketListener() {
            public void onClosed(final WebSocket webSocket, final int code, final String reason) {
                super.onClosed(webSocket, code, reason);
                TTSService.log.debug("onClosed:" + reason);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }
            
            public void onClosing(final WebSocket webSocket, final int code, final String reason) {
                super.onClosing(webSocket, code, reason);
                TTSService.log.debug("onClosing:" + reason);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }
            
            public void onFailure(final WebSocket webSocket, final Throwable t, final Response response) {
                super.onFailure(webSocket, t, response);
                TTSService.log.debug("onFailure" + t.getMessage(), t);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }
            
            public void onMessage(final WebSocket webSocket, final String text) {
                super.onMessage(webSocket, text);
                if (text.contains("turn.start")) {
                    TTSService.this.audioBuffer.clear();
                }
                else if (text.contains("turn.end")) {
                    TTSService.this.latch.countDown();
                    TTSService.this.synthesising = false;
                }
            }
            
            public void onMessage(@NotNull final WebSocket webSocket, @NotNull final ByteString bytes) {
                super.onMessage(webSocket, bytes);
                final int audioIndex = bytes.lastIndexOf("Path:audio\r\n".getBytes(StandardCharsets.UTF_8)) + "Path:audio\r\n".length();
                final boolean audioContentType = bytes.lastIndexOf("Content-Type:audio".getBytes(StandardCharsets.UTF_8)) + "Content-Type:audio".length() != -1;
                if (audioIndex != -1 && audioContentType) {
                    try {
                        TTSService.this.audioBuffer.write(bytes.substring(audioIndex));
                    }
                    catch (final Exception e) {
                        TTSService.log.error("onMessage Error," + e.getMessage(), (Throwable)e);
                    }
                }
            }
        };
        this.outputFormat = outputFormat;
        this.usingAzureApi = usingAzureApi;
    }
    
    public static TTSServiceBuilder builder() {
        return new TTSServiceBuilder();
    }
    
    public byte[] sendText(final SSML ssml) {
        while (this.synthesising) {
            TTSService.log.info("\u7a7a\u8f6c\u7b49\u5f85\u4e0a\u4e00\u4e2a\u8bed\u97f3\u5408\u6210");
            Tools.sleep(1);
        }
        this.latch = new CountDownLatch(1);
        this.synthesising = true;
        if (Objects.nonNull(ssml.getStyle()) && !this.usingAzureApi) {
            ssml.setStyle(null);
        }
        if (Objects.nonNull(ssml.getOutputFormat()) && !this.outputFormat.equals(ssml.getOutputFormat())) {
            this.sendConfig(ssml.getOutputFormat());
        }
        TTSService.log.info("ssml:{}", (Object)ssml);
        if (!this.getOrCreateWs().send(ssml.toString())) {
            throw TtsException.of("\u8bed\u97f3\u5408\u6210\u8bf7\u6c42\u53d1\u9001\u5931\u8d25...");
        }
        this.currentText = ssml.getSynthesisText();
        try {
            this.latch.await(30L, TimeUnit.SECONDS);
            return this.audioBuffer.readByteArray();
        }
        catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    
    private synchronized WebSocket getOrCreateWs() {
        if (Objects.nonNull(this.ws)) {
            return this.ws;
        }
        String url;
        String origin;
        if (this.usingAzureApi) {
            url = "wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1?Retry-After=200&TrafficType=AzureDemo&Authorization=bearer undefined&X-ConnectionId=" + Tools.getRandomId();
            origin = "https://azure.microsoft.com";
        }
        else {
            url = "wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1?Retry-After=200&TrustedClientToken=6A5AA1D4EAFF4E9FB37E23D68491D6F4&ConnectionId=" + Tools.getRandomId();
            origin = "chrome-extension://jdiccldimpdaibmpdkjnbmckianbfold";
        }
        final Request request = new Request.Builder().url(url).addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44").addHeader("Origin", origin).build();
        this.ws = this.getOkHttpClient().newWebSocket(request, this.webSocketListener);
        this.sendConfig(this.outputFormat);
        return this.ws;
    }
    
    private OkHttpClient getOkHttpClient() {
        if (this.okHttpClient == null) {
            this.okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
        }
        return this.okHttpClient;
    }
    
    private void sendConfig(final OutputFormat outputFormat) {
        final SpeechConfig speechConfig = SpeechConfig.of(outputFormat);
        TTSService.log.info("audio config:{}", (Object)speechConfig);
        if (!this.getOrCreateWs().send(speechConfig.toString())) {
            throw TtsException.of("\u8bed\u97f3\u8f93\u51fa\u683c\u5f0f\u914d\u7f6e\u5931\u8d25...");
        }
        this.outputFormat = speechConfig.getOutputFormat();
    }
    
    static {
        log = LoggerFactory.getLogger((Class)TTSService.class);
    }
    
    public static class TTSServiceBuilder
    {
        private OutputFormat outputFormat;
        private boolean usingAzureApi;
        
        public TTSServiceBuilder usingOutputFormat(final OutputFormat usingOutputFormat) {
            this.outputFormat = usingOutputFormat;
            return this;
        }
        
        public TTSServiceBuilder usingAzureApi(final boolean usingAzureApi) {
            this.usingAzureApi = usingAzureApi;
            return this;
        }
        
        public TTSService build() {
            return new TTSService(this.outputFormat, this.usingAzureApi, null);
        }
    }
}
