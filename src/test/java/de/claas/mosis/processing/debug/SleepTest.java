package de.claas.mosis.processing.debug;

import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.model.DecoratorProcessorTest;
import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link Sleep}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest},
 * {@link ProcessorAdapterTest} and {@link DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class SleepTest {

    private Sleep _P;

    @Before
    public void before() throws Exception {
        _P = new Sleep();
        _P.setParameter(Sleep.CLASS, Time.class.getName());
        _P.setParameter(Sleep.DELAY, "50");
        _P.setParameter(DecoratorProcessor.SHADOWED + Time.CLASS,
                Null.class.getName());
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() {
        assertEquals(Time.class.getName(), _P.getParameter(Sleep.CLASS));
        assertEquals(Null.class.getName(),
                _P.getParameter(DecoratorProcessor.SHADOWED + Time.CLASS));
    }

    @Test
    public void assumptionsOnParameterDelay() {
        assertEquals("50", _P.getParameter(Sleep.DELAY));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterDelayMayNotBeNull() throws Exception {
        _P.setParameter(Sleep.DELAY, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterDelayMustBeAnInteger() throws Exception {
        try {
            _P.setParameter(Sleep.DELAY, "1");
            _P.setParameter(Sleep.DELAY, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Sleep.DELAY, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterDelayMustBePostive() throws Exception {
        try {
            _P.setParameter(Sleep.DELAY, "0");
            _P.setParameter(Sleep.DELAY, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Sleep.DELAY, "-1");
    }

    @Test
    public void shouldTakeLonger() {
        long before = System.currentTimeMillis();
        Utils.process(_P);
        long after = System.currentTimeMillis();
        assertTrue(50 <= after - before);
    }

    @Test
    public void shouldDelay() {
        long start = System.currentTimeMillis();
        Utils.process(_P);
        long call = Long.parseLong(_P.getParameter(Time.FIRST_CALL));
        assertTrue(50 <= call - start);
    }

}
