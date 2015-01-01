package de.claas.mosis.processing.condition;

import de.claas.mosis.model.Condition;
import de.claas.mosis.model.Condition.IsInList;
import de.claas.mosis.model.ConditionTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link IsInList}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ConditionTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class IsInListTest {

    /**
     * Returns an instantiated {@link IsInList} class. If appropriate, the
     * instance is configured with default values as well as the given
     * parameters.
     *
     * @param list the predefined values
     * @return an instantiated {@link IsInList} class
     */
    private Condition build(List<String> list) {
        return new Condition.IsInList(list);
    }

    @Test
    public void shouldBeInList() {
        Condition c = build(Arrays.asList("a", "c", "l"));
        assertTrue(c.complies(null, "a"));
        assertTrue(c.complies(null, "c"));
        assertTrue(c.complies(null, "l"));
    }

    @Test
    public void shouldNotBeInList() {
        Condition c = build(Arrays.asList("a", "c", "l"));
        assertFalse(c.complies(null, "b"));
        assertFalse(c.complies(null, "0"));
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, "k"));
        assertFalse(c.complies(null, null));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowException() {
        Condition c = build(null);
        c.complies("hello", "world");
    }

    @Test
    public void shouldHandleExtremes() {
        Condition c = build(new Vector<String>());
        assertFalse(c.complies(null, "a"));
        assertFalse(c.complies(null, "0"));
        assertFalse(c.complies(null, ""));
        assertFalse(c.complies(null, "k"));
        assertFalse(c.complies(null, null));
    }

}
