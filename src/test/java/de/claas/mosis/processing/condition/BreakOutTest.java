package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Condition.BreakOut}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BreakOutTest {

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Condition.BreakOut}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Condition.BreakOut}
     * class
     */
    private Condition.BreakOut build() {
        return new Condition.BreakOut();
    }

    @Test
    public void assumeZeroCalls() {
        Condition.BreakOut c = build();
        assertEquals(0, c.getCalls());
    }

    @Test
    public void assumeZeroUpdates() {
        Condition.BreakOut c = build();
        assertEquals(0, c.getUpdates("random paramater"));
    }

    @Test
    public void assumeNull() {
        Condition.BreakOut c = build();
        assertNull(c.getValue("random parameter"));
    }

    @Test
    public void assumeNoParameters() {
        Condition.BreakOut c = build();
        assertTrue(c.getParameters().isEmpty());
    }

    @Test
    public void shouldCountCalls() {
        Condition.BreakOut c = build();
        c.complies("parameter1", "a");
        c.complies("parameter2", "b");
        assertEquals(2, c.getCalls());
        c.complies("parameter1", "c");
        c.complies("parameter2", "d");
        assertEquals(4, c.getCalls());
    }

    @Test
    public void shouldCountUpdates() {
        Condition.BreakOut c = build();
        c.complies("parameter1", "a");
        c.complies("parameter1", "b");
        assertEquals(2, c.getUpdates("parameter1"));
        assertEquals(0, c.getUpdates("parameter2"));
        c.complies("parameter2", "c");
        c.complies("parameter2", "d");
        assertEquals(2, c.getUpdates("parameter1"));
        assertEquals(2, c.getUpdates("parameter2"));
    }

    @Test
    public void shouldReturnLatestValue() {
        Condition.BreakOut c = build();
        c.complies("parameter1", "a");
        c.complies("parameter2", "b");
        assertEquals("a", c.getValue("parameter1"));
        assertEquals("b", c.getValue("parameter2"));
        c.complies("parameter1", "c");
        c.complies("parameter2", "d");
        assertEquals("c", c.getValue("parameter1"));
        assertEquals("d", c.getValue("parameter2"));
    }

    @Test
    public void shouldReturnParameters() {
        Condition.BreakOut c = build();
        c.complies("parameter1", "a");
        c.complies("parameter2", "b");
        assertEquals(2, c.getParameters().size());
        c.complies("parameter1", "c");
        c.complies("parameter2", "d");
        assertEquals(2, c.getParameters().size());
    }

}
