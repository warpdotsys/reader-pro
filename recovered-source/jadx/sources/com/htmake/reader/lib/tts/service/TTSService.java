package com.htmake.reader.lib.tts.service;

import com.htmake.reader.lib.tts.constant.OutputFormat;
import com.htmake.reader.lib.tts.constant.TtsConstants;
import com.htmake.reader.lib.tts.exceptions.TtsException;
import com.htmake.reader.lib.tts.model.SSML;
import com.htmake.reader.lib.tts.model.SpeechConfig;
import com.htmake.reader.lib.tts.util.Tools;
import io.legado.app.constant.AppConst;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.Buffer;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/lib/tts/service/TTSService.class */
public class TTSService {
    public static final Logger log = LoggerFactory.getLogger(TTSService.class);
    private OutputFormat outputFormat;
    private boolean usingAzureApi;
    private volatile boolean synthesising;
    private String currentText;
    private final Buffer audioBuffer;
    private OkHttpClient okHttpClient;
    private WebSocket ws;
    private CountDownLatch latch;
    protected WebSocketListener webSocketListener;

    private TTSService(OutputFormat outputFormat, boolean usingAzureApi) {
        this.audioBuffer = new Buffer();
        this.webSocketListener = new WebSocketListener() { // from class: com.htmake.reader.lib.tts.service.TTSService.1
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                TTSService.log.debug("onClosed:" + reason);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }

            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                TTSService.log.debug("onClosing:" + reason);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }

            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                TTSService.log.debug("onFailure" + t.getMessage(), t);
                TTSService.this.ws = null;
                TTSService.this.synthesising = false;
            }

            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                if (text.contains(TtsConstants.TURN_START)) {
                    TTSService.this.audioBuffer.clear();
                } else if (text.contains(TtsConstants.TURN_END)) {
                    TTSService.this.latch.countDown();
                    TTSService.this.synthesising = false;
                }
            }

            public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
                super.onMessage(webSocket, bytes);
                int audioIndex = bytes.lastIndexOf(TtsConstants.AUDIO_START.getBytes(StandardCharsets.UTF_8)) + TtsConstants.AUDIO_START.length();
                boolean audioContentType = bytes.lastIndexOf(TtsConstants.AUDIO_CONTENT_TYPE.getBytes(StandardCharsets.UTF_8)) + TtsConstants.AUDIO_CONTENT_TYPE.length() != -1;
                if (audioIndex != -1 && audioContentType) {
                    try {
                        TTSService.this.audioBuffer.write(bytes.substring(audioIndex));
                    } catch (Exception e) {
                        TTSService.log.error("onMessage Error," + e.getMessage(), e);
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

    public byte[] sendText(SSML ssml) {
        while (this.synthesising) {
            log.info("空转等待上一个语音合成");
            Tools.sleep(1);
        }
        this.latch = new CountDownLatch(1);
        this.synthesising = true;
        if (Objects.nonNull(ssml.getStyle()) && !this.usingAzureApi) {
            ssml.setStyle(null);
        }
        if (Objects.nonNull(ssml.getOutputFormat()) && !this.outputFormat.equals(ssml.getOutputFormat())) {
            sendConfig(ssml.getOutputFormat());
        }
        log.info("ssml:{}", ssml);
        if (!getOrCreateWs().send(ssml.toString())) {
            throw TtsException.of("语音合成请求发送失败...");
        }
        this.currentText = ssml.getSynthesisText();
        try {
            this.latch.await(30L, TimeUnit.SECONDS);
            return this.audioBuffer.readByteArray();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized WebSocket getOrCreateWs() {
        String url;
        String origin;
        if (Objects.nonNull(this.ws)) {
            return this.ws;
        }
        if (this.usingAzureApi) {
            url = "wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1?Retry-After=200&TrafficType=AzureDemo&Authorization=bearer undefined&X-ConnectionId=" + Tools.getRandomId();
            origin = TtsConstants.AZURE_SPEECH_ORIGIN;
        } else {
            url = "wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1?Retry-After=200&TrustedClientToken=6A5AA1D4EAFF4E9FB37E23D68491D6F4&ConnectionId=" + Tools.getRandomId();
            origin = TtsConstants.EDGE_SPEECH_ORIGIN;
        }
        Request request = new Request.Builder().url(url).addHeader(AppConst.UA_NAME, TtsConstants.UA).addHeader("Origin", origin).build();
        this.ws = getOkHttpClient().newWebSocket(request, this.webSocketListener);
        sendConfig(this.outputFormat);
        return this.ws;
    }

    private OkHttpClient getOkHttpClient() {
        if (this.okHttpClient == null) {
            this.okHttpClient = new OkHttpClient.Builder().pingInterval(20L, TimeUnit.SECONDS).build();
        }
        return this.okHttpClient;
    }

    private void sendConfig(OutputFormat outputFormat) {
        SpeechConfig speechConfig = SpeechConfig.of(outputFormat);
        log.info("audio config:{}", speechConfig);
        if (!getOrCreateWs().send(speechConfig.toString())) {
            throw TtsException.of("语音输出格式配置失败...");
        }
        this.outputFormat = speechConfig.getOutputFormat();
    }

    /* JADX INFO: loaded from: app-classes.jar:com/htmake/reader/lib/tts/service/TTSService$TTSServiceBuilder.class */
    public static class TTSServiceBuilder {
        private OutputFormat outputFormat;
        private boolean usingAzureApi;

        public TTSServiceBuilder usingOutputFormat(OutputFormat usingOutputFormat) {
            this.outputFormat = usingOutputFormat;
            return this;
        }

        public TTSServiceBuilder usingAzureApi(boolean usingAzureApi) {
            this.usingAzureApi = usingAzureApi;
            return this;
        }

        public TTSService build() {
            return new TTSService(this.outputFormat, this.usingAzureApi);
        }
    }
}
