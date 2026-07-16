// 
// Decompiled by Procyon v0.6.0
// 

package com.htmake.reader.lib.tts.model;

import java.util.Optional;
import com.htmake.reader.lib.tts.util.Tools;
import com.htmake.reader.lib.tts.constant.OutputFormat;
import com.htmake.reader.lib.tts.constant.TtsStyleEnum;
import com.htmake.reader.lib.tts.constant.VoiceEnum;
import java.io.Serializable;

public class SSML implements Serializable
{
    public static String SSML_PATTERN;
    private String synthesisText;
    private VoiceEnum voice;
    private String rate;
    private String pitch;
    private String volume;
    private TtsStyleEnum style;
    private OutputFormat outputFormat;
    
    private SSML(final String synthesisText, final VoiceEnum voice, final String rate, final String pitch, final String volume, final TtsStyleEnum style, final OutputFormat outputFormat) {
        this.synthesisText = synthesisText;
        this.voice = voice;
        this.rate = rate;
        this.pitch = pitch;
        this.volume = volume;
        this.style = style;
        this.outputFormat = outputFormat;
    }
    
    public static SSMLBuilder builder() {
        return new SSMLBuilder();
    }
    
    public String getSynthesisText() {
        return this.synthesisText;
    }
    
    public void setSynthesisText(final String synthesisText) {
        this.synthesisText = synthesisText;
    }
    
    public VoiceEnum getVoice() {
        return this.voice;
    }
    
    public void setVoice(final VoiceEnum voice) {
        this.voice = voice;
    }
    
    public String getRate() {
        return this.rate;
    }
    
    public void setRate(final String rate) {
        this.rate = rate;
    }
    
    public String getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final String pitch) {
        this.pitch = pitch;
    }
    
    public String getVolume() {
        return this.volume;
    }
    
    public void setVolume(final String volume) {
        this.volume = volume;
    }
    
    public TtsStyleEnum getStyle() {
        return this.style;
    }
    
    public void setStyle(final TtsStyleEnum style) {
        this.style = style;
    }
    
    public OutputFormat getOutputFormat() {
        return this.outputFormat;
    }
    
    public void setOutputFormat(final OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }
    
    @Override
    public String toString() {
        return String.format(SSML.SSML_PATTERN, Tools.getRandomId(), Tools.date(), Optional.ofNullable(this.voice).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getLocale(), Optional.ofNullable(this.voice).orElse(VoiceEnum.zh_CN_XiaoxiaoNeural).getShortName(), Optional.ofNullable(this.style).map(s -> String.format("<mstts:express-as style='%s'>\r\n", s.getValue())).orElse(""), Optional.ofNullable(this.pitch).orElse("+0Hz"), Optional.ofNullable(this.rate).orElse("+0%"), Optional.ofNullable(this.volume).orElse("+0%"), this.synthesisText, Optional.ofNullable(this.style).map(s -> "</mstts:express-as>").orElse(""));
    }
    
    static {
        SSML.SSML_PATTERN = "X-RequestId:%s\r\nContent-Type:application/ssml+xml\r\nX-Timestamp:%sZ\r\nPath:ssml\r\n\r\n<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xmlns:mstts='https://www.w3.org/2001/mstts' xml:lang='%s'>\r\n<voice name='%s'>\r\n%s<prosody pitch='%s' rate='%s' volume='%s'>%s</prosody>%s</voice></speak>";
    }
    
    public static class SSMLBuilder
    {
        private String synthesisText;
        private VoiceEnum voice;
        private String rate;
        private String pitch;
        private String volume;
        private TtsStyleEnum style;
        private OutputFormat outputFormat;
        
        public SSMLBuilder synthesisText(final String synthesisText) {
            this.synthesisText = synthesisText;
            return this;
        }
        
        public SSMLBuilder voice(final VoiceEnum voice) {
            this.voice = voice;
            return this;
        }
        
        public SSMLBuilder rate(final String rate) {
            this.rate = rate;
            return this;
        }
        
        public SSMLBuilder pitch(final String pitch) {
            this.pitch = pitch;
            return this;
        }
        
        public SSMLBuilder volume(final String volume) {
            this.volume = volume;
            return this;
        }
        
        public SSMLBuilder style(final TtsStyleEnum style) {
            this.style = style;
            return this;
        }
        
        public SSMLBuilder outputFormat(final OutputFormat outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }
        
        public SSML build() {
            return new SSML(this.synthesisText, this.voice, this.rate, this.pitch, this.volume, this.style, this.outputFormat, null);
        }
    }
}
