package com.htmake.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.htmake.reader.db.DB;
import com.htmake.reader.db.JSONTable;
import com.htmake.reader.db.SQLTable;
import com.htmake.reader.entity.User;
import com.htmake.reader.utils.ExtKt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.nio.file.Files;
import java.nio.file.Path;
import kotlin.jvm.functions.Function2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DataStackAlignmentTest {

    private static final Function2<JsonObject, Row, Boolean> SAME_ID =
            (stored, candidate) -> stored.getInteger("id") == candidate.getId();

    @TempDir
    Path storageRoot;

    private String previousStorageFinalPath;

    @BeforeEach
    void redirectStorage() {
        previousStorageFinalPath = ExtKt.getStorageFinalPath();
        ExtKt.setStorageFinalPath(storageRoot.toString());
    }

    @AfterEach
    void restoreStorage() {
        ExtKt.setStorageFinalPath(previousStorageFinalPath);
    }

    @Test
    void userDefaultsMatchTheJarDataClass() {
        long before = System.currentTimeMillis();
        User user = new User();
        long after = System.currentTimeMillis();

        assertEquals("", user.getUsername());
        assertEquals("", user.getPassword());
        assertEquals("", user.getSalt());
        assertEquals("", user.getToken());
        assertTrue(user.getLast_login_at() >= before && user.getLast_login_at() <= after);
        assertTrue(user.getCreated_at() >= before && user.getCreated_at() <= after);
        assertEquals(false, user.getEnable_webdav());
        assertNull(user.getToken_map());
        assertEquals(false, user.getEnable_local_store());
        assertTrue(user.getEnable_book_source());
        assertTrue(user.getEnable_rss_source());
        assertEquals(100, user.getBook_source_limit());
        assertEquals(200, user.getBook_limit());
    }

    @Test
    void tableFactoryUsesTheJarDriverSelection() {
        assertInstanceOf(JSONTable.class, DB.Companion.table("factory", "default", "json"));
        assertInstanceOf(JSONTable.class, DB.Companion.table("factory", "json", "JSON"));
        assertInstanceOf(SQLTable.class, DB.Companion.table("factory", "sql", "SQL"));
    }

    @Test
    void jsonTableRetainsReplacementAndBatchIndexBehavior() {
        JSONTable<Row> table = new JSONTable<>("json", "rows");
        table.save(new Row(1, "first"), null, SAME_ID);
        table.save(new Row(1, "replacement"), null, SAME_ID);

        Row found = table.findBy("id", 1, Row.class);
        assertNotNull(found);
        assertEquals("replacement", found.getValue());

        table.saveMulti(new Row[]{new Row(1, "first-next"), new Row(2, "second")}, null, SAME_ID);

        JsonArray persisted = readStored("json", "rows");
        assertEquals(1, persisted.size());
        assertEquals(2, persisted.getJsonObject(0).getInteger("id"));
        assertEquals("second", persisted.getJsonObject(0).getString("value"));
    }

    @Test
    void sqlTableKeepsTheJarSequentialIndexRemovalBehavior() {
        SQLTable<Row> table = new SQLTable<>("sql", "rows");
        table.save(new Row(1, "one"), null, SAME_ID);
        table.save(new Row(2, "two"), null, SAME_ID);
        table.save(new Row(3, "three"), null, SAME_ID);

        table.delete(stored -> stored.getInteger("id") <= 2);

        JsonArray persisted = readStored("sql", "rows");
        assertEquals(1, persisted.size());
        assertEquals(2, persisted.getJsonObject(0).getInteger("id"));
    }

    @Test
    void storageFailuresKeepTheJarMessages() throws Exception {
        Path blockedParent = storageRoot.resolve("data").resolve("blocked");
        Files.createDirectories(blockedParent.getParent());
        Files.writeString(blockedParent, "not a directory");

        Exception saveFailure = assertThrows(
                Exception.class,
                () -> ExtKt.saveStorage(
                        new String[]{"data", "blocked", "rows"},
                        new JsonArray(),
                        false,
                        ".json"
                )
        );
        assertEquals(
                "保存文件失败:" + blockedParent.resolve("rows.json").toFile().getAbsolutePath(),
                saveFailure.getMessage()
        );

        Path directoryAsFile = storageRoot.resolve("data").resolve("unreadable").resolve("rows.json");
        Files.createDirectories(directoryAsFile);
        Exception readFailure = assertThrows(
                Exception.class,
                () -> ExtKt.getStorage(new String[]{"data", "unreadable", "rows"}, ".json")
        );
        assertEquals("读取文件失败:" + directoryAsFile.toFile().getAbsolutePath(), readFailure.getMessage());
    }

    private static JsonArray readStored(String namespace, String name) {
        String encoded = ExtKt.getStorage(new String[]{"data", namespace, name}, ".json");
        assertNotNull(encoded);
        JsonArray data = ExtKt.asJsonArray(encoded);
        assertNotNull(data);
        return data;
    }

    public static final class Row {

        private int id;
        private String value;

        public Row() {
        }

        Row(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
