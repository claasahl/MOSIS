package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.IsLessOrEqual}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class IsLessOrEqualTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.IsLessOrEqual}
     * class. If appropriate, the instance is configured with default values as
     * well as the given parameters.
     *
     * @param threshold the threshold
     * @return an instantiated {@link de.claas.mosis.model.Condition.IsLessOrEqual}
     * class
     */
    private Condition build(Double threshold) {
        return new Condition.IsLessOrEqual(threshold);
    }

    @Test
    public void shouldBeLessOrEqual() {
        Condition c = build(23.4);
        assertTrue(c.complies(null, "0"));
        assertTrue(c.complies(null, "-23.5"));
        assertTrue(c.complies(null, "-23.2"));
        assertTrue(c.complies(null, "23.4"));
    }

    @Test
    public void shouldNotBeLessOrEqual() {
        Condition c = build(23.4);
        assertFalse(c.complies(null, "24"));
        assertFalse(c.complies(null, "24.0"));
        assertFalse(c.complies(null, "23.41"));
        assertFalse(c.complies(null, null));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException() {
        Condition c = build(null);
        c.complies("hello", "world");
    }

    @Test
    public void shouldHandleNaN() {
        Condition c = build(Double.NaN);
        assertFalse(c.complies(null, "NaN"));
        assertFalse(c.complies(null, "23"));
        assertFalse(c.complies(null, "-5.3"));
    }

    @Test
    public void shouldHandleExtremes() {
        Condition c1 = build(Double.NEGATIVE_INFINITY);
        Condition c2 = build(Double.POSITIVE_INFINITY);
        assertFalse(c1.complies(null, Double.toString(Double.MAX_VALUE)));
        assertFalse(c1.complies(null, Double.toString(Double.MIN_VALUE)));
        assertFalse(c1.complies(null, "NaN"));
        assertTrue(c2.complies(null, Double.toString(Double.MAX_VALUE)));
        assertTrue(c2.complies(null, Double.toString(Double.MIN_VALUE)));
        assertFalse(c2.complies(null, "NaN"));
    }

}
