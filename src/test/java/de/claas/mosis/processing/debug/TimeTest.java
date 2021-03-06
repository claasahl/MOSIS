package de.claas.mosis.processing.debug;

import de.claas.mosis.model.DecoratorProcessor;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.Time}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest},
 * {@link de.claas.mosis.model.ProcessorAdapterTest} and {@link
 * de.claas.mosis.model.DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class TimeTest {

    private Time _P;

    @Before
    public void before() throws Exception {
        _P = new Time();
        _P.setParameter(Time.CLASS, Sleep.class.getName());
        _P.setParameter(Sleep.DELAY, "100");
        _P.setParameter(DecoratorProcessor.SHADOWED + Sleep.CLASS,
                Null.class.getName());
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() {
        assertEquals(Sleep.class.getName(), _P.getParameter(Time.CLASS));
        assertEquals("100", _P.getParameter(Sleep.DELAY));
        assertEquals(Null.class.getName(),
                _P.getParameter(DecoratorProcessor.SHADOWED + Sleep.CLASS));
    }

    @Test
    public void assumptionsOnParameterTime() {
        assertTrue(_P.getParameter(Time.TIME).isEmpty());
    }

    @Test
    public void assumptionsOnParameterFirstCall() {
        assertTrue(_P.getParameter(Time.FIRST_CALL).isEmpty());
    }

    @Test
    public void assumptionsOnParameterLastCall() {
        assertTrue(_P.getParameter(Time.LAST_CALL).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterTimeMayNotBeNull() throws Exception {
        Utils.updateParameter(_P, Time.TIME, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterTimeMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.TIME, "1",
                    Time.TIME, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.TIME, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterTimeMustBePositive() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.TIME, "0",
                    Time.TIME, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.TIME, "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterFirstCallMayNotBeNull() throws Exception {
        Utils.updateParameter(_P, Time.FIRST_CALL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterFirstCallMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.FIRST_CALL, "1",
                    Time.FIRST_CALL, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.FIRST_CALL, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterFirstCallMustBePositive() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.FIRST_CALL, "0",
                    Time.FIRST_CALL, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.FIRST_CALL, "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLastCallMayNotBeNull() throws Exception {
        Utils.updateParameter(_P, Time.LAST_CALL, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLastCallMustBeAnInteger() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.LAST_CALL, "1",
                    Time.LAST_CALL, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.LAST_CALL, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterLastCallMustBePositive() throws Exception {
        try {
            Utils.updateParameters(_P,
                    Time.LAST_CALL, "0",
                    Time.LAST_CALL, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        Utils.updateParameter(_P, Time.LAST_CALL, "-1");
    }

    @Test
    public void shouldUpdateFirstAndLastCall() throws Exception {
        long time = System.currentTimeMillis();
        Utils.process(_P);
        long firstCall = Long.parseLong(_P.getParameter(Time.FIRST_CALL));
        long lastCall = Long.parseLong(_P.getParameter(Time.LAST_CALL));
        assertTrue(time <= firstCall);
        assertTrue(time <= lastCall);
        assertEquals(firstCall, lastCall);

        Utils.process(_P);
        assertEquals(firstCall,
                Long.parseLong(_P.getParameter(Time.FIRST_CALL)));
        assertTrue(lastCall < Long.parseLong(_P.getParameter(Time.LAST_CALL)));
    }

    @Test
    public void shouldUpdateTime() throws Exception {
        Utils.process(_P);
        assertTrue(Long.parseLong(_P.getParameter(Time.TIME)) >= 100);
    }

}
