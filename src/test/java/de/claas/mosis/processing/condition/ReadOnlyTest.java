package de.claas.mosis.processing.condition;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.ReadOnly;
import de.claas.mosis.model.ConditionTest;

/**
 * The JUnit test for class {@link ReadOnly}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ConditionTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class ReadOnlyTest {

    /**
     * Returns an instantiated {@link ReadOnly} class. If appropriate, the
     * instance is configured with default values.
     * 
     * @return an instantiated {@link ReadOnly} class
     */
    private Condition build() {
	return new Condition.ReadOnly();
    }

    @Test
    public void shouldAlwaysBeFalse() {
	Condition c = build();
	assertFalse(c.complies(null, ""));
	assertFalse(c.complies(null, "3"));
	assertFalse(c.complies(null, "-23.4"));
	assertFalse(c.complies(null, "abc"));
	assertFalse(c.complies(null, null));
    }

}
