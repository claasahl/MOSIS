package de.claas.mosis.flow;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.flow.BiasedLink}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.flow.LinkTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BiasedLinkTest {

    private Link _L;

    @Before
    public void setUp() {
        _L = new BiasedLink();
        _L.setParameter(BiasedLink.CLASS, Number.class.getName());
    }

    @Test
    public void shouldBuffer() throws Exception {
        List<Object> list = Arrays.<Object>asList(1, 2d, 3l, -5);
        assertTrue(_L.push(list));
        for (Object item : list) {
            assertEquals(item, _L.poll());
        }
        assertTrue(_L.isEmpty());
    }

    @Test
    public void shouldNotBuffer() throws Exception {
        List<Object> list = Arrays.asList("one", new Object());
        for (Object item : list) {
            assertFalse(_L.push(Arrays.asList(item)));
        }
        assertTrue(_L.isEmpty());
    }

    @Test
    public void shouldHandleNullValues() {
        assertTrue(_L.push(Arrays.asList((Object) null)));
        assertNull(_L.poll());
        assertTrue(_L.isEmpty());
    }

}
