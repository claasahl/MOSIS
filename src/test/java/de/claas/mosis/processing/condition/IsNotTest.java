package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.IsNot}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class IsNotTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.IsNot}
     * class. If appropriate, the instance is configured with default values as
     * well as the given parameters.
     *
     * @param condition {@link de.claas.mosis.model.Condition} that should be
     *                  negated
     * @return an instantiated {@link de.claas.mosis.model.Condition.IsNot}
     * class
     */
    private Condition build(Condition condition) {
        return new Condition.IsNot(condition);
    }

    @Test
    public void shouldNegateCondition() {
        Condition c = build(new Condition.IsBoolean());
        assertFalse(c.complies(null, "true"));
        assertFalse(c.complies(null, "false"));
    }

    @Test
    public void shouldAlsoNegateCondition() {
        Condition c = build(new Condition.IsBoolean());
        assertTrue(c.complies(null, ""));
        assertTrue(c.complies(null, "0"));
        assertTrue(c.complies(null, "abc"));
        assertTrue(c.complies(null, null));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException() {
        Condition c = build(null);
        c.complies("hello", "world");
    }

}
