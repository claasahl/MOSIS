package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.IsBoolean}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class IsBooleanTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.IsBoolean}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Condition.IsBoolean}
     * class
     */
    private Condition build() {
        return new Condition.IsBoolean();
    }

    @Test
    public void shouldBeBoolean() {
        Condition c = build();
        assertTrue(c.complies(null, "true"));
        assertTrue(c.complies(null, "false"));
    }

    @Test
    public void shouldNotBeBoolean() {
        Condition c = build();
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, "1"));
        assertFalse(c.complies(null, "0"));
        assertFalse(c.complies(null, "test"));
        assertFalse(c.complies(null, null));
    }

}
