package de.claas.mosis.processing.debug;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.Logger}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LoggerTest {

    private Forward<Object> _P;

    @Before
    public void before() throws Exception {
        _P = new Forward<>();
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void shouldForwardValues() {
        assertEquals("hello world", Utils.process(_P, "hello world"));
        List<Object> results = Utils.processAll(_P, 23.4, 42.0, 0.0);
        assertTrue(results.containsAll(Arrays.asList(23.4, 42.0, 0.0)));
        assertTrue(Arrays.asList(23.4, 42.0, 0.0).containsAll(results));
    }

    @Test
    public void shouldForwardNullValues() {
        assertNull(Utils.process(_P, (Object) null));
        assertNull(Utils.process(_P, null, null));
        List<Object> results = Utils.processAll(_P, null, null);
        assertEquals(2, results.size());
        assertNull(results.get(0));
        assertNull(results.get(1));
    }

}
