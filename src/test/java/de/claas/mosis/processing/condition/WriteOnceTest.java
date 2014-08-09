package de.claas.mosis.processing.condition;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.WriteOnce;
import de.claas.mosis.model.ConditionTest;

/**
 * The JUnit test for class {@link WriteOnce}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ConditionTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class WriteOnceTest {

    /**
     * Returns an instantiated {@link WriteOnce} class. If appropriate, the
     * instance is configured with default values.
     * 
     * @return an instantiated {@link WriteOnce} class
     */
    private Condition build() {
	return new Condition.WriteOnce();
    }

    @Test
    public void shouldComplyOnce() {
	Condition c = build();
	assertTrue(c.complies(null, ""));
	assertFalse(c.complies(null, "1"));
	assertFalse(c.complies(null, "0"));
	assertFalse(c.complies(null, "test"));
	assertFalse(c.complies(null, null));
    }

}
