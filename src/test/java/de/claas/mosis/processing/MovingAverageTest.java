package de.claas.mosis.processing;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link MovingAverage}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class MovingAverageTest {

    private MovingAverage _P;

    @Before
    public void before() throws Exception {
        _P = new MovingAverage();
        _P.setParameter(MovingAverage.WINDOW_SIZE, "5");
        _P.setParameter(MovingAverage.MODE, MovingAverage.MODE_USE_ACTUAL_SIZE);
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnWindowSize() throws Exception {
        assertEquals("5", _P.getParameter(MovingAverage.WINDOW_SIZE));
    }

    @Test
    public void assumptionsOnMode() throws Exception {
        assertEquals(MovingAverage.MODE_USE_ACTUAL_SIZE,
                _P.getParameter(MovingAverage.MODE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterModeMayNotBeNull() throws Exception {
        _P.setParameter(MovingAverage.MODE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterModeMustInWhiteList() throws Exception {
        try {
            _P.setParameter(MovingAverage.MODE,
                    MovingAverage.MODE_USE_ACTUAL_SIZE);
            _P.setParameter(MovingAverage.MODE,
                    MovingAverage.MODE_USE_BUFFER_SIZE);
            _P.setParameter(MovingAverage.MODE,
                    MovingAverage.MODE_WAIT_FOR_BUFFER);
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(MovingAverage.MODE, "hello world");
    }

    @Test
    public void shouldWaitForBufferToFill() {
        _P.setParameter(MovingAverage.MODE, MovingAverage.MODE_WAIT_FOR_BUFFER);
        assertNull(Utils.process(_P, 23.0));
        assertNull(Utils.process(_P, 23.0));
        assertNull(Utils.process(_P, 23.0));
        assertNull(Utils.process(_P, 23.0));
        assertEquals(new Double(23), Utils.process(_P, 23.0));
        assertEquals(new Double(20), Utils.process(_P, 8.0));
    }

    @Test
    public void shouldUseActualSize() {
        assertEquals(new Double(23), Utils.process(_P, 23.0));
        assertEquals(new Double(0), Utils.process(_P, -23.0));
        assertEquals(new Double(4), Utils.process(_P, 12.0));
        assertEquals(new Double(3), Utils.process(_P, 0.0));
        assertEquals(new Double(4), Utils.process(_P, 8.0));
        assertEquals(new Double(-1), Utils.process(_P, -2.0));
    }

    @Test
    public void shouldUseBufferSize() {
        _P.setParameter(MovingAverage.MODE, MovingAverage.MODE_USE_BUFFER_SIZE);
        assertEquals(new Double(4.6), Utils.process(_P, 23.0));
        assertEquals(new Double(0), Utils.process(_P, -23.0));
        assertEquals(new Double(2.4), Utils.process(_P, 12.0));
        assertEquals(new Double(2.4), Utils.process(_P, 0.0));
        assertEquals(new Double(4), Utils.process(_P, 8.0));
        assertEquals(new Double(-1), Utils.process(_P, -2.0));
    }
}
