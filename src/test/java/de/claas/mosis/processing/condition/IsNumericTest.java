package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.IsNumeric}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class IsNumericTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.IsNumeric}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Condition.IsNumeric}
     * class
     */
    private Condition build() {
        return new Condition.IsNumeric();
    }

    @Test
    public void shouldBeNumeric() {
        Condition c = build();
        assertTrue(c.complies(null, "0"));
        assertTrue(c.complies(null, "23"));
        assertTrue(c.complies(null, "-42"));
        assertTrue(c.complies(null, "0.0001"));
        assertTrue(c.complies(null, "-34.0001"));
    }

    @Test
    public void shouldNotBeNumeric() {
        Condition c = build();
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, ".3"));
        assertFalse(c.complies(null, "23.3.4"));
        assertFalse(c.complies(null, "NaN"));
        assertFalse(c.complies(null, "a"));
        assertFalse(c.complies(null, null));
    }

}
