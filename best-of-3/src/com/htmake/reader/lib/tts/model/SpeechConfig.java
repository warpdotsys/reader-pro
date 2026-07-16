//
// Decompiled by Procyon v0.6.0
//

package com.htmake.reader.lib.tts.model;

import com.htmake.reader.lib.tts.util.Tools;
import java.util.Optional;
import com.htmake.reader.lib.tts.constant.OutputFormat;
import java.io.Serializable;

public class SpeechConfig implements Serializable
{
    public static final String CONFIG_PATTERN = "X-Timestamp:%s\r\nContent-Type:application/json; charset=utf-8\r\nPath:speech.config\r\n\r\n{\"context\":{\"synthesis\":{\"audio\":{\"metadataoptions\":{\"sentenceBoundaryEnabled\":\"false\",\"wordBoundaryEnabled\":\"true\"},\"outputFormat\":\"%s\"}}}}";
    private OutputFormat outputFormat;

    private SpeechConfig(final OutputFormat outputFormat) {
        this.outputFormat = Optional.ofNullable(outputFormat).orElse(OutputFormat.audio_24khz_48kbitrate_mono_mp3);
    }

    public static SpeechConfig of(final OutputFormat outputFormat) {
        return new SpeechConfig(outputFormat);
    }

    public OutputFormat getOutputFormat() {
        return this.outputFormat;
    }

    public void setOutputFormat(final OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    @Override
    public String toString() {
        return String.format("X-Timestamp:%s\r\nContent-Type:application/json; charset=utf-8\r\nPath:speech.config\r\n\r\n{\"context\":{\"synthesis\":{\"audio\":{\"metadataoptions\":{\"sentenceBoundaryEnabled\":\"false\",\"wordBoundaryEnabled\":\"true\"},\"outputFormat\":\"%s\"}}}}", Tools.date(), this.outputFormat.getValue());
    }
}
