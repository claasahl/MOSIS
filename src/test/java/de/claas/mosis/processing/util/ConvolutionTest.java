package de.claas.mosis.processing.util;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.util.Convolution}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ConvolutionTest {

    private Convolution _P;

    @Before
    public void before() throws Exception {
        _P = new Convolution();
        _P.setParameter(Convolution.WINDOW_SIZE, "2");
        _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, "false");
        _P.setParameter(Convolution.WEIGHTS, "0.5,-0.5");
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterWindowSize() {
        assertEquals("2", _P.getParameter(Convolution.WINDOW_SIZE));
    }

    @Test
    public void assumptionsOnParameterRequiresFullBuffer() {
        assertEquals("false", _P.getParameter(Convolution.REQUIRES_FULL_BUFFER));
    }

    @Test
    public void assumptionsOnParameterPortToUse() {
        assertEquals("0", _P.getParameter(Convolution.PORT_TO_USE));
    }

    @Test
    public void assumptionsOnParameterWeights() {
        assertEquals("0.5,-0.5", _P.getParameter(Convolution.WEIGHTS));
    }

    @Test
    public void assumptionsOnParameterSeparator() {
        assertEquals(",", _P.getParameter(Convolution.SEPARATOR));
    }

    @Test
    public void assumptionsOnParameterDefaultValue() {
        assertEquals("1", _P.getParameter(Convolution.DEFAULT_VALUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterRequiresFullBufferMayNotBeNull() throws Exception {
        _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterRequiresFullBufferMustBeBoolean() throws Exception {
        try {
            _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, "true");
            _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, "false");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, "maybe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMayNotBeNull() throws Exception {
        _P.setParameter(Convolution.PORT_TO_USE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBeAnInteger() throws Exception {
        try {
            _P.setParameter(Convolution.PORT_TO_USE, "1");
            _P.setParameter(Convolution.PORT_TO_USE, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Convolution.PORT_TO_USE, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBePositive() throws Exception {
        try {
            _P.setParameter(Convolution.PORT_TO_USE, "0");
            _P.setParameter(Convolution.PORT_TO_USE, "12");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Convolution.PORT_TO_USE, "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterWeightsMayNotBeNull() throws Exception {
        _P.setParameter(Convolution.WEIGHTS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterSeparatorMayNotBeNull() throws Exception {
        _P.setParameter(Convolution.WEIGHTS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterDefaultValueMayNotBeNull() throws Exception {
        _P.setParameter(Convolution.DEFAULT_VALUE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterDefaultValueMustBeNumeric() throws Exception {
        try {
            _P.setParameter(Convolution.DEFAULT_VALUE, "0.0");
            _P.setParameter(Convolution.DEFAULT_VALUE, "-23");
            _P.setParameter(Convolution.DEFAULT_VALUE, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _P.setParameter(Convolution.DEFAULT_VALUE, "maybe");
    }

    @Test
    public void shouldHandleExtremeValue() {
        _P.setParameter(Convolution.WINDOW_SIZE, "0");
    }

    @Test
    public void shouldUseCorrespondingPort() {
        _P.setParameter(Convolution.PORT_TO_USE, "1");
        assertEquals(-0.5, Utils.process(_P, 0.0, 1.0, 0.0), 0.0001);
        assertEquals(-0.5, Utils.process(_P, 0.0, 2.0, 0.0), 0.0001);
        assertEquals(-1.0, Utils.process(_P, 0.0, 4.0, 0.0), 0.0001);
        _P.setParameter(Convolution.PORT_TO_USE, "0");
        assertEquals(-4.0, Utils.process(_P, 8.0, 0.0, 0.0), 0.0001);
        assertEquals(+1.0, Utils.process(_P, 6.0, 0.0, 0.0), 0.0001);
        _P.setParameter(Convolution.PORT_TO_USE, "1");
        assertEquals(-1.0, Utils.process(_P, 0.0, 2.0, 0.0), 0.0001);
        assertEquals(+0.5, Utils.process(_P, 0.0, 1.0, 0.0), 0.0001);
    }

    @Test
    public void shouldWaitForFullBuffer() {
        _P.setParameter(Convolution.REQUIRES_FULL_BUFFER, "true");
        assertNull(Utils.process(_P, 1.0, 0.0));
        assertEquals(-0.5, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(-1.0, Utils.process(_P, 4.0, 0.0), 0.0001);
        assertEquals(-2.0, Utils.process(_P, 8.0, 0.0), 0.0001);
        assertEquals(+1.0, Utils.process(_P, 6.0, 0.0), 0.0001);
        assertEquals(+2.0, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(+0.5, Utils.process(_P, 1.0, 0.0), 0.0001);
    }

    @Test
    public void shouldUseCorrespondingWeights() {
        _P.setParameter(Convolution.WINDOW_SIZE, "4");
        _P.setParameter(Convolution.WEIGHTS, "0.25,0.25,0.25,0.25");
        assertEquals(0.25, Utils.process(_P, 1.0, 0.0), 0.0001);
        assertEquals(0.75, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(1.75, Utils.process(_P, 4.0, 0.0), 0.0001);
        assertEquals(3.75, Utils.process(_P, 8.0, 0.0), 0.0001);
        assertEquals(5.00, Utils.process(_P, 6.0, 0.0), 0.0001);
        assertEquals(5.00, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(4.25, Utils.process(_P, 1.0, 0.0), 0.0001);
    }

    @Test
    public void shouldUseCorrespondingDefaultValue() {
        _P.setParameter(Convolution.WEIGHTS, ",-0.5");
        assertEquals(-0.5, Utils.process(_P, 1.0, 0.0), 0.0001);
        assertEquals(+0.0, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(+0.0, Utils.process(_P, 4.0, 0.0), 0.0001);

        _P.setParameter(Convolution.DEFAULT_VALUE, "0.5");
        assertEquals(-2.0, Utils.process(_P, 8.0, 0.0), 0.0001);
        assertEquals(+1.0, Utils.process(_P, 6.0, 0.0), 0.0001);
        assertEquals(+2.0, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(+0.5, Utils.process(_P, 1.0, 0.0), 0.0001);
    }

    @Test
    public void shouldUseCorrespondingSeparator() {
        _P.setParameter(Convolution.SEPARATOR, "#");
        _P.setParameter(Convolution.WEIGHTS, "0.5#-0.5");
        assertEquals(-0.5, Utils.process(_P, 1.0, 0.0), 0.0001);
        assertEquals(-0.5, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(-1.0, Utils.process(_P, 4.0, 0.0), 0.0001);
        assertEquals(-2.0, Utils.process(_P, 8.0, 0.0), 0.0001);
        assertEquals(+1.0, Utils.process(_P, 6.0, 0.0), 0.0001);
        assertEquals(+2.0, Utils.process(_P, 2.0, 0.0), 0.0001);
        assertEquals(+0.5, Utils.process(_P, 1.0, 0.0), 0.0001);
    }

}
