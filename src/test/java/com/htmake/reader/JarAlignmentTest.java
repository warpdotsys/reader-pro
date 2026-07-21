package com.htmake.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.htmake.reader.entity.ActiveLicense;
import com.htmake.reader.entity.License;
import io.legado.app.data.entities.SearchBook;
import io.legado.app.help.JsExtensions;
import io.legado.app.utils.EncoderUtils;
import java.io.File;
import java.lang.reflect.Method;
import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.util.List;
import org.junit.jupiter.api.Test;

class JarAlignmentTest {

    @Test
    void licenseRetainsValidityAndHostMatchingRules() {
        License license = new License();
        license.setHost("api.example.com,*.reader.test");

        assertTrue(license.isValid());
        assertTrue(license.validHost("api.example.com:8080"));
        assertTrue(license.validHost("web.reader.test"));
        assertFalse(license.validHost("reader.test"));
        assertFalse(license.validHost("API.example.com"));
        assertFalse(license.validHost(""));

        license.setExpiredAt(System.currentTimeMillis() - 1);
        assertFalse(license.isValid());
        assertFalse(license.validHost("api.example.com"));
    }

    @Test
    void licenseConvertsToActiveLicenseWithJarFieldMapping() {
        License license = new License(
                "reader.test",
                42,
                123456789L,
                true,
                987654321L,
                3,
                "enterprise",
                "license-id",
                "license-code",
                true,
                11223344L
        );

        ActiveLicense active = license.toActiveLicense();

        assertEquals(license.getHost(), active.getHost());
        assertEquals(license.getUserMaxLimit(), active.getUserMaxLimit());
        assertEquals(license.getExpiredAt(), active.getExpiredAt());
        assertEquals(license.getOpenApi(), active.getOpenApi());
        assertEquals(license.getSimpleWebExpiredAt(), active.getSimpleWebExpiredAt());
        assertEquals(license.getInstances(), active.getInstances());
        assertEquals(license.getType(), active.getType());
        assertEquals(license.getId(), active.getId());
        assertEquals(license.getCode(), active.getCode());
        assertEquals(license.getVerified(), active.getVerified());
        assertEquals(license.getVerifyTime(), active.getVerifyTime());
        assertEquals(1, active.getActiveOrder());
        assertEquals("", active.getActiveIp());
        assertEquals("", active.getActiveEmail());
        assertEquals("", active.getLastOnlineIp());
        assertNull(active.getLastOnlineTime());
        assertEquals("", active.getErrorMsg());
    }

    @Test
    void rsaSingleBlockOperationsRoundTrip() throws Exception {
        EncoderUtils encoder = EncoderUtils.INSTANCE;
        KeyPair keys = encoder.generateKeys();
        String value = "reader-pro RSA round trip";

        assertEquals("RSA", keys.getPublic().getAlgorithm());
        assertEquals(2048, ((RSAKey) keys.getPublic()).getModulus().bitLength());
        assertEquals(value, encoder.decryptByPublicKey(
                encoder.encryptByPrivateKey(value, keys.getPrivate()),
                keys.getPublic()
        ));
        assertEquals(value, encoder.decryptByPrivateKey(
                encoder.encryptByPublicKey(value, keys.getPublic()),
                keys.getPrivate()
        ));
    }

    @Test
    void rsaSegmentOperationsRoundTripMultipleBlocks() throws Exception {
        EncoderUtils encoder = EncoderUtils.INSTANCE;
        KeyPair keys = encoder.generateKeys();
        String value = repeat("segment-reader-pro-", 40);

        String privateEncrypted = invokeSegmentDefault(
                "encryptSegmentByPrivateKey", PrivateKey.class, value, keys.getPrivate()
        );
        assertEquals(value, invokeSegmentDefault(
                "decryptSegmentByPublicKey", PublicKey.class, privateEncrypted, keys.getPublic()
        ));

        String publicEncrypted = invokeSegmentDefault(
                "encryptSegmentByPublicKey", PublicKey.class, value, keys.getPublic()
        );
        assertEquals(value, invokeSegmentDefault(
                "decryptSegmentByPrivateKey", PrivateKey.class, publicEncrypted, keys.getPrivate()
        ));
    }

    @Test
    void searchBookOriginsHasNoPublicSetter() {
        assertThrows(
                NoSuchMethodException.class,
                () -> SearchBook.class.getDeclaredMethod("setOrigins", java.util.LinkedHashSet.class)
        );
    }

    @Test
    void jsExtensionsDoesNotExposeExtraSingleArgumentCookieMethod() throws NoSuchMethodException {
        assertThrows(
                NoSuchMethodException.class,
                () -> JsExtensions.class.getDeclaredMethod("getCookie", String.class)
        );
        assertEquals(
                String.class,
                JsExtensions.class.getDeclaredMethod("getCookie", String.class, String.class).getReturnType()
        );
    }

    @Test
    void extDeepListFilesMatchesTheJarMethodBoundary() throws ReflectiveOperationException {
        Method method = Class.forName("com.htmake.reader.utils.ExtKt").getDeclaredMethod(
                "deepListFiles",
                File.class,
                String[].class
        );
        assertEquals(List.class, method.getReturnType());
        assertThrows(
                NoSuchMethodException.class,
                () -> Class.forName("com.htmake.reader.utils.ExtKt").getDeclaredMethod(
                        "deepListFiles$default",
                        java.io.File.class,
                        String[].class,
                        int.class,
                        Object.class
                )
        );
    }

    private static String invokeSegmentDefault(
            String methodName,
            Class<? extends Key> keyType,
            String input,
            Key key
    ) throws Exception {
        Method method = EncoderUtils.class.getDeclaredMethod(
                methodName + "$default",
                EncoderUtils.class,
                String.class,
                keyType,
                int.class,
                int.class,
                Object.class
        );
        return (String) method.invoke(null, EncoderUtils.INSTANCE, input, key, 0, 4, null);
    }

    private static String repeat(String value, int count) {
        StringBuilder result = new StringBuilder(value.length() * count);
        for (int index = 0; index < count; index++) {
            result.append(value);
        }
        return result.toString();
    }
}
