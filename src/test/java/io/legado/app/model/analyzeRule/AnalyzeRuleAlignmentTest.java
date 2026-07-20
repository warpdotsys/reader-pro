package io.legado.app.model.analyzeRule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.legado.app.data.entities.BaseSource;
import io.legado.app.model.DebugLog;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class AnalyzeRuleAlignmentTest {

    @Test
    void exposesTheJarConstructorAndMutableLoggerShape() throws ReflectiveOperationException {
        RuleData ruleData = new RuleData("tenant-a");
        DebugLog logger = new TestDebugLog();
        AnalyzeRule analyzeRule = new AnalyzeRule(ruleData, null, logger);

        assertSame(ruleData, analyzeRule.getRuleData());
        assertEquals("tenant-a", analyzeRule.getUserNameSpace());
        assertSame(logger, analyzeRule.getDebugLog());
        assertSame(logger, analyzeRule.getLogger());

        analyzeRule.setDebugLog(null);
        assertNull(analyzeRule.getDebugLog());
        assertNull(analyzeRule.getLogger());

        AnalyzeRule.class.getDeclaredConstructor(
                RuleDataInterface.class,
                BaseSource.class,
                DebugLog.class
        );
        assertThrows(
                NoSuchMethodException.class,
                () -> AnalyzeRule.class.getDeclaredConstructor(
                        RuleDataInterface.class,
                        BaseSource.class
                )
        );
        assertEquals(
                void.class,
                AnalyzeRule.class.getDeclaredMethod("reGetBook").getReturnType()
        );
    }

    @Test
    void evalJsScopesCookieAndCacheToTheRuleNamespace() {
        AnalyzeRule analyzeRule = new AnalyzeRule(new RuleData("tenant-js"), null, null);

        Object result = analyzeRule.evalJS(
                "cookie.userNameSpace + ':' + cache.userNameSpace",
                null
        );

        assertEquals("tenant-js:tenant-js", String.valueOf(result));
    }

    @Test
    void setContentRejectsNullWithTheJarMessage() {
        AnalyzeRule analyzeRule = new AnalyzeRule(new RuleData("tenant-a"), null, null);

        AssertionError error = assertThrows(
                AssertionError.class,
                () -> analyzeRule.setContent(null)
        );

        assertEquals("内容不可空（Content cannot be null）", error.getMessage());
    }

    private static final class RuleData implements RuleDataInterface {
        private final HashMap<String, String> variableMap = new HashMap<>();
        private final String userNameSpace;

        private RuleData(String userNameSpace) {
            this.userNameSpace = userNameSpace;
        }

        @Override
        public HashMap<String, String> getVariableMap() {
            return variableMap;
        }

        @Override
        public void putVariable(String key, String value) {
            if (value == null) {
                variableMap.remove(key);
            } else {
                variableMap.put(key, value);
            }
        }

        @Override
        public String getVariable(String key) {
            return variableMap.get(key);
        }

        @Override
        public String getUserNameSpace() {
            return userNameSpace;
        }
    }

    private static final class TestDebugLog implements DebugLog {
        @Override
        public void log(String sourceUrl, String msg, boolean isHtml) {
        }

        @Override
        public void log(String message) {
        }
    }
}
