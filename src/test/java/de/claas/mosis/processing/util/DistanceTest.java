package de.claas.mosis.processing.util;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.util.Distance}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class DistanceTest {

    private Distance _P;

    @Before
    public void before() throws Exception {
        _P = new Distance();
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterPortToUse() {
        assertEquals("0", _P.getParameter(Distance.PORT_TO_USE));
    }

    @Test
    public void shouldNotReturnDistance() {
        assertNull(Utils.process(_P, 0.0));
    }

    @Test
    public void shouldReturnDistance() {
        assertNull(Utils.process(_P, 10.0));
        assertEquals(new Double(-8.0), Utils.process(_P, 2.0));
        assertEquals(new Double(0.0), Utils.process(_P, 2.0));
        assertEquals(new Double(-5.0), Utils.process(_P, -3.0));
        assertEquals(new Double(13.0), Utils.process(_P, 10.0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void shouldThrowException() {
        assertNull(Utils.process(_P));
    }

    @Test
    public void shouldHandleNullValues() {
        assertNull(Utils.process(_P, null, null));
        assertNull(Utils.process(_P, null, null));
        assertNull(Utils.process(_P, null, null));
        assertNull(Utils.process(_P, null, null));
        assertNull(Utils.process(_P, 1.0));
        assertNull(Utils.process(_P, null, null));
        assertNull(Utils.process(_P, 2.0));
        assertEquals(new Double(-1), Utils.process(_P, 1.0));
        assertNull(Utils.process(_P, null, null));
    }

}
