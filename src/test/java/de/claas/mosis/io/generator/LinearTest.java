package de.claas.mosis.io.generator;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * The JUnit test for class {@link Linear}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LinearTest {

    private Linear _L;

    @Before
    public void before() throws Exception {
        _L = new Linear();
        _L.setParameter(Linear.M, Double.toString(1));
        _L.setParameter(Linear.B, Double.toString(2));
        _L.setParameter(Linear.STEP, Double.toString(3));
        _L.setUp();
    }

    @After
    public void after() {
        _L.dismantle();
    }

    @Test
    public void assumptionsOnM() throws Exception {
        assertEquals("1.0", _L.getParameter(Linear.M));
    }

    @Test
    public void assumptionsOnX() throws Exception {
        assertEquals("0.0", _L.getParameter(Linear.X));
    }

    @Test
    public void assumptionsOnB() throws Exception {
        assertEquals("2.0", _L.getParameter(Linear.B));
    }

    @Test
    public void assumptionsOnStep() throws Exception {
        assertEquals("3.0", _L.getParameter(Linear.STEP));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterMMayNotBeNull() throws Exception {
        _L.setParameter(Linear.M, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterMMustBeNummeric() throws Exception {
        try {
            _L.setParameter(Linear.M, "0.0");
            _L.setParameter(Linear.M, "-23");
            _L.setParameter(Linear.M, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _L.setParameter(Linear.M, "maybe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterXMayNotBeNull() throws Exception {
        _L.setParameter(Linear.X, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterXMustBeNummeric() throws Exception {
        try {
            _L.setParameter(Linear.X, "0.0");
            _L.setParameter(Linear.X, "-23");
            _L.setParameter(Linear.X, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _L.setParameter(Linear.X, "maybe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBMayNotBeNull() throws Exception {
        _L.setParameter(Linear.B, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterBMustBeNummeric() throws Exception {
        try {
            _L.setParameter(Linear.B, "0.0");
            _L.setParameter(Linear.B, "-23");
            _L.setParameter(Linear.B, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _L.setParameter(Linear.B, "maybe");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterStepMayNotBeNull() throws Exception {
        _L.setParameter(Linear.STEP, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterStepMustBeNummeric() throws Exception {
        try {
            _L.setParameter(Linear.STEP, "0.0");
            _L.setParameter(Linear.STEP, "-23");
            _L.setParameter(Linear.STEP, "42");
        } catch (Exception e) {
            fail(e.toString());
        }
        _L.setParameter(Linear.STEP, "maybe");
    }

    @Test
    public void shouldHavePositiveSlope() {
        _L.setParameter(Linear.M, Double.toString(1.5));
        _L.setParameter(Linear.B, Double.toString(3));
        _L.setParameter(Linear.STEP, Double.toString(0.5));
        assertEquals(new Double(3.0), Utils.process(_L));
        assertEquals(new Double(3.75), Utils.process(_L));
        assertEquals(new Double(4.5), Utils.process(_L));
        assertEquals(new Double(5.25), Utils.process(_L));
    }

    @Test
    public void shouldHaveNegativeSlope() {
        _L.setParameter(Linear.M, Double.toString(-1.5));
        _L.setParameter(Linear.B, Double.toString(-3));
        _L.setParameter(Linear.STEP, Double.toString(0.5));
        assertEquals(new Double(-3.0), Utils.process(_L));
        assertEquals(new Double(-3.75), Utils.process(_L));
        assertEquals(new Double(-4.5), Utils.process(_L));
        assertEquals(new Double(-5.25), Utils.process(_L));
    }

    @Test
    public void shouldHaveOffset() {
        _L.setParameter(Linear.M, Double.toString(-1.5));
        _L.setParameter(Linear.B, Double.toString(-3));
        _L.setParameter(Linear.STEP, Double.toString(0.5));
        _L.setParameter(Linear.X, "2");
        assertEquals(new Double(-6.0), Utils.process(_L));
        assertEquals(new Double(-6.75), Utils.process(_L));
        assertEquals(new Double(-7.5), Utils.process(_L));
        assertEquals(new Double(-8.25), Utils.process(_L));
    }

}
