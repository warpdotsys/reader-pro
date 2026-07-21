package com.htmake.reader.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class BookConfigAlignmentTest {

    @Test
    void injectsTheTargetScriptOnlyOnce() throws Exception {
        Path chapter = Files.createTempFile("reader-pro-", ".html");
        try {
            Files.write(
                    chapter,
                    Arrays.asList("<html><head></head><body>chapter</body></html>"),
                    StandardCharsets.UTF_8
            );

            BookConfig.INSTANCE.injectJavascriptToEpubChapter(chapter.toString());
            String injected = String.join("\n", Files.readAllLines(chapter, StandardCharsets.UTF_8));

            assertTrue(injected.contains(BookConfig.INSTANCE.getJavascriptVersion()));
            assertTrue(injected.contains("reader_notifySize"));
            BookConfig.INSTANCE.injectJavascriptToEpubChapter(chapter.toString());
            assertEquals(injected, String.join("\n", Files.readAllLines(chapter, StandardCharsets.UTF_8)));
        } finally {
            Files.deleteIfExists(chapter);
        }
    }
}
