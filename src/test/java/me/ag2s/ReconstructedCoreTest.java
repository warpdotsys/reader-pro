package me.ag2s;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import io.legado.app.adapters.DefaultAdpater;
import io.legado.app.help.http.CookieStore;
import io.legado.app.help.http.StrResponse;
import io.legado.app.utils.EncodingDetect;
import com.htmake.reader.init.appCtx;
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
            assertArrayEquals(payload, readAllBytes(stream));
        }
    }

    @Test
    void mediaTypesResolveKnownAndUnknownExtensions() {
        assertEquals(MediaTypes.XHTML, MediaTypes.determineMediaType("chapter.xhtml"));
        assertFalse(Arrays.asList(MediaTypes.mediaTypes).contains(MediaTypes.determineMediaType("chapter.unknown")));
    }

    @Test
    void defaultAdapterRetainsWorkDirectoryLayout() {
        DefaultAdpater adapter = new DefaultAdpater();

        assertEquals(
                new File(System.getProperty("user.dir"), "storage" + File.separator + "cache").toPath().toString(),
                adapter.getCacheDir()
        );
        assertEquals("storage" + File.separator + "cache", adapter.getRelativePath("", "storage", "cache"));
        assertEquals(adapter.getCacheDir(), appCtx.INSTANCE.getCacheDir());
    }

    @Test
    void stringResponseBuildsSyntheticSuccessfulResponse() {
        StrResponse response = new StrResponse("https://example.com/chapter", "body");

        assertEquals(200, response.code());
        assertTrue(response.isSuccessful());
        assertEquals("https://example.com/chapter", response.getUrl());
        assertEquals("body", response.body());
    }

    @Test
    void cookieStoreKeepsJarParsingRules() {
        CookieStore cookieStore = new CookieStore("test");

        assertEquals("two", cookieStore.cookieToMap("one=two; blank=; null=null").get("one"));
        assertFalse(cookieStore.cookieToMap("one=two; blank=; null=null").containsKey("blank"));
        assertEquals("null", cookieStore.cookieToMap("one=two; blank=; null=null").get("null"));
    }

    @Test
    void encodingDetectPrefersHtmlMetaCharset() {
        byte[] html = "<html><head><meta charset=\"GBK\"></head><body></body></html>"
                .getBytes(StandardCharsets.US_ASCII);

        assertEquals("GBK", EncodingDetect.INSTANCE.getHtmlEncode(html));
    }

    @Test
    void encodingDetectScansMetaWithoutExplicitHeadElement() {
        byte[] html = "<meta charset=\"windows-1252\"><body>reader</body>"
                .getBytes(StandardCharsets.UTF_8);

        assertEquals("windows-1252", EncodingDetect.INSTANCE.getHtmlEncode(html));
    }

    private static byte[] readAllBytes(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
        return output.toByteArray();
    }
}
