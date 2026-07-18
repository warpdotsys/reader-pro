package me.ag2s;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import me.ag2s.epublib.domain.Date;
import me.ag2s.epublib.domain.MediaTypes;
import me.ag2s.epublib.util.commons.io.BOMInputStream;
import me.ag2s.umdlib.tool.UmdUtils;
import org.junit.jupiter.api.Test;

class ReconstructedCoreTest {

    @Test
    void dateRetainsJarFormatAndEvent() {
        Date date = new Date(new java.util.Date(0L), Date.Event.CREATION);

        assertEquals("1970-01-01", date.getValue());
        assertEquals(Date.Event.CREATION, date.getEvent());
        assertEquals("creation:1970-01-01", date.toString());
    }

    @Test
    void unicodeBytesRoundTrip() {
        String value = "Reader-阅";

        assertEquals(value, UmdUtils.unicodeBytesToString(UmdUtils.stringToUnicodeBytes(value)));
    }

    @Test
    void bomStreamExcludesUtf8Bom() throws Exception {
        byte[] payload = "reader".getBytes(StandardCharsets.UTF_8);
        byte[] input = new byte[payload.length + 3];
        input[0] = (byte) 0xEF;
        input[1] = (byte) 0xBB;
        input[2] = (byte) 0xBF;
        System.arraycopy(payload, 0, input, 3, payload.length);

        try (BOMInputStream stream = new BOMInputStream(new ByteArrayInputStream(input))) {
            assertTrue(stream.hasBOM());
            assertArrayEquals(payload, stream.readAllBytes());
        }
    }

    @Test
    void mediaTypesResolveKnownAndUnknownExtensions() {
        assertEquals(MediaTypes.XHTML, MediaTypes.determineMediaType("chapter.xhtml"));
        assertFalse(Arrays.asList(MediaTypes.mediaTypes).contains(MediaTypes.determineMediaType("chapter.unknown")));
    }
}
