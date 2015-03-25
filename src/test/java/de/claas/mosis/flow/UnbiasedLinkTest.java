package de.claas.mosis.flow;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.flow.UnbiasedLink}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.flow.LinkTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class UnbiasedLinkTest {

    private Link _L;

    @Before
    public void setUp() {
        _L = new UnbiasedLink();
    }

    @Test
    public void shouldBuffer() throws Exception {
        List<Object> list = Arrays.asList(-23, 1L, 42.3, "hello world", new Object());
        assertTrue(_L.push(list));
        for (Object arg : list) {
            assertEquals(arg, _L.poll());
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
