package de.claas.mosis.processing.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.IsNotNull;
import de.claas.mosis.model.ConditionTest;

/**
 * The JUnit test for class {@link IsNotNull}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ConditionTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class IsNotNullTest {

    /**
     * Returns an instantiated {@link IsNotNull} class. If appropriate, the
     * instance is configured with default values.
     * 
     * @return an instantiated {@link IsNotNull} class
     */
    private Condition build() {
	return new Condition.IsNotNull();
    }

    @Test
    public void shouldNotBeNull() {
	Condition c = build();
	assertTrue(c.complies(null, "a"));
	assertTrue(c.complies(null, "True"));
	assertTrue(c.complies(null, ""));
	assertTrue(c.complies(null, "324"));
	assertTrue(c.complies(null, "null"));
    }

    @Test
    public void shouldBeNull() {
	Condition c = build();
	assertFalse(c.complies(null, null));
	assertFalse(c.complies("test", null));
    }

}
