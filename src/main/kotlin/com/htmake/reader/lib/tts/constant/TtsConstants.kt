package com.htmake.reader.lib.tts.constant

interface TtsConstants {
    companion object {
        const val TRUSTED_CLIENT_TOKEN = "6A5AA1D4EAFF4E9FB37E23D68491D6F4"
        const val VOICE_LIST_URL = "https://speech.platform.bing.com/consumer/speech/synthesize/readaloud/voices/list"
        const val EDGE_SPEECH_WSS = "wss://speech.platform.bing.com/consumer/speech/synthesize/readaloud/edge/v1"
        const val EDGE_SPEECH_ORIGIN = "chrome-extension://jdiccldimpdaibmpdkjnbmckianbfold"
        const val AZURE_SPEECH_WSS = "wss://eastus.api.speech.microsoft.com/cognitiveservices/websocket/v1"
        const val AZURE_SPEECH_ORIGIN = "https://azure.microsoft.com"
        const val UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36 Edg/111.0.1661.44"
        const val TURN_START = "turn.start"; const val TURN_END = "turn.end"; const val AUDIO_START = "Path:audio\r\n"; const val AUDIO_CONTENT_TYPE = "Content-Type:audio"
    }
}
