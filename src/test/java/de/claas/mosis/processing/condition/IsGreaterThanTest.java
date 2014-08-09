package de.claas.mosis.processing.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.IsGreaterThan;
import de.claas.mosis.model.ConditionTest;

/**
 * The JUnit test for class {@link IsGreaterThan}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ConditionTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class IsGreaterThanTest {

    /**
     * Returns an instantiated {@link IsGreaterThan} class. If appropriate, the
     * instance is configured with default values as well as the given
     * parameters.
     * 
     * @param threshold
     *            the threshold
     * @return an instantiated {@link IsGreaterThan} class
     */
    private Condition build(Double threshold) {
	return new Condition.IsGreaterThan(threshold);
    }

    @Test
    public void shouldBeGreater() {
	Condition c = build(23.4);
	assertTrue(c.complies(null, "24"));
	assertTrue(c.complies(null, "24.0"));
	assertTrue(c.complies(null, "23.41"));
    }

    @Test
    public void shouldNotBeGreater() {
	Condition c = build(23.4);
	assertFalse(c.complies(null, "0"));
	assertFalse(c.complies(null, "-23.5"));
	assertFalse(c.complies(null, "-23.2"));
	assertFalse(c.complies(null, "23.4"));
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
	assertTrue(c1.complies(null, Double.toString(Double.MAX_VALUE)));
	assertTrue(c1.complies(null, Double.toString(Double.MIN_VALUE)));
	assertFalse(c1.complies(null, "NaN"));
	assertFalse(c2.complies(null, Double.toString(Double.MAX_VALUE)));
	assertFalse(c2.complies(null, Double.toString(Double.MIN_VALUE)));
	assertFalse(c2.complies(null, "NaN"));
    }

}
