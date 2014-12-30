package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.RegularExpression;
import de.claas.mosis.model.ConditionTest;
import org.junit.Test;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link RegularExpression}. It is intended to collect
 * and document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class RegularExpressionTest {

    private static final String Pattern = "[a-c0-9]+";
    private static final String[] Valid = new String[]{"c0", "1a", "b",
            "000ab432c"};
    private static final String[] Invalid = new String[]{"", null, "9z", "-",
            "A"};

    /**
     * Returns an instantiated {@link RegularExpression} class. If appropriate,
     * the instance is configured with default values as well as the given
     * parameters.
     *
     * @param p regular expression that the parameter should match (
     *          <code>null</code> if all parameters are accepted)
     * @param v regular expression that the value should match (
     *          <code>null</code> if all values are accepted)
     * @return an instantiated {@link RegularExpression} class
     */
    private Condition build(String p, String v) {
        return new Condition.RegularExpression(p, v);
    }

    @Test
    public void shouldHaveMatchingParameter() {
        Condition c = build(Pattern, null);
        for (String tmp : Valid) {
            assertTrue(c.complies(tmp, null));
        }
    }

    @Test
    public void shouldNotHaveMatchingParameter() {
        Condition c = build(Pattern, null);
        for (String tmp : Invalid) {
            assertFalse(c.complies(tmp, null));
        }
    }

    @Test
    public void shouldHaveMatchingValue() {
        Condition c = build(null, Pattern);
        for (String tmp : Valid) {
            assertTrue(c.complies(null, tmp));
        }
    }

    @Test
    public void shouldNotHaveMatchingValue() {
        Condition c = build(null, Pattern);
        for (String tmp : Invalid) {
            assertFalse(c.complies(null, tmp));
        }
    }

    @Test
    public void shouldAcceptEmptyPattern() {
        Condition c = build(null, "");
        assertTrue(c.complies(null, ""));
        assertFalse(c.complies(null, "a"));

        c = build("", null);
        assertTrue(c.complies("", null));
        assertFalse(c.complies("b", null));

        c = build("", "");
        assertTrue(c.complies("", ""));
        assertFalse(c.complies("a", "b"));
        assertFalse(c.complies("a", ""));
        assertFalse(c.complies("", "b"));
    }

    @Test(expected = PatternSyntaxException.class)
    public void shouldNotAcceptInvalidPatterns() {
        Condition c = build("(", "]");
        c.complies("hello", "world");
    }

}
