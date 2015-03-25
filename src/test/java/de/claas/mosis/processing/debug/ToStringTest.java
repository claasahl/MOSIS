package de.claas.mosis.processing.debug;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.ToString}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest},
 * {@link de.claas.mosis.model.ProcessorAdapterTest} and {@link
 * de.claas.mosis.model.DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ToStringTest {

    private ToString _P;

    @Before
    public void before() throws Exception {
        _P = new ToString();
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void shouldForwardString() {
        assertEquals("tree", Utils.process(_P, "tree"));
        assertEquals("house", Utils.process(_P, "house"));
        assertEquals(Arrays.asList("tree", "house"), Utils.processAll(_P, "tree", "house"));
    }

    @Test
    public void shouldCallToString() {
        Object obj = new Object();
        List<Double> numbers = Arrays.asList(0.0, 12.4, -32.7);
        assertEquals("42", Utils.process(_P, 42));
        assertEquals(obj.toString(), Utils.process(_P, obj));
        assertEquals(numbers.toString(), Utils.process(_P, numbers));
        assertEquals(Arrays.asList(obj.toString(), "23", ""), Utils.processAll(_P, obj, 23L, ""));
    }

    @Test
    public void shouldHandleNullValues() {
        assertEquals(null, Utils.process(_P, (Object) null));
        assertEquals(Arrays.asList("tree", null, "house"), Utils.processAll(_P, "tree", null, "house"));
    }

}
