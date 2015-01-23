package de.claas.mosis.processing.debug;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.BreakOut}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest},
 * {@link de.claas.mosis.model.ProcessorAdapterTest} and {@link
 * de.claas.mosis.model.DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class BreakOutTest {

    private BreakOut _P;

    @Before
    public void before() throws Exception {
        _P = new BreakOut();
        _P.setParameter(BreakOut.CLASS, Null.class.getName());
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() {
        assertEquals(Null.class.getName(), _P.getParameter(Counter.CLASS));
    }

    @Test
    public void assumeZeroCalls() {
        assertEquals(0, _P.getCallsToProcess());
        assertNull(_P.getLastInput());
        assertNull(_P.getLastInput());
    }

    @Test
    public void shouldCountCalls() {
        Utils.process(_P);
        assertEquals(1, _P.getCallsToProcess());
        Utils.process(_P, (Object) null);
        Utils.process(_P, 23, 42);
        Utils.process(_P);
        assertEquals(4, _P.getCallsToProcess());
    }

    @Test
    public void shouldReturnInput() {
        Utils.process(_P);
        assertEquals(0, _P.getLastInput().size());
        Utils.process(_P, 23);
        assertEquals(1, _P.getLastInput().size());
        Utils.process(_P, 1, 42);
        assertEquals(2, _P.getLastInput().size());
        assertEquals(1, _P.getLastInput().get(0));
        assertEquals(42, _P.getLastInput().get(1));
    }

    @Test
    public void shouldReturnOutput() {
        Utils.updateParameter(_P, BreakOut.CLASS, Forward.class.getName());
        Utils.process(_P, "hello world");
        assertFalse(_P.getLastOutput().isEmpty());
        assertEquals(1, _P.getLastOutput().size());
        assertEquals("hello world", _P.getLastOutput().get(0));
    }

}
