package de.claas.mosis.io.generator;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The JUnit test for class {@link de.claas.mosis.io.generator.Function}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class FunctionTest {

    private Function _F;

    @Before
    public void before() throws Exception {
        _F = new Function();
        _F.setUp();
    }

    @After
    public void after() {
        _F.dismantle();
    }

    @Test
    public void assumptionsOnFunction() {
        Utils.updateParameter(_F, Function.FUNCTION, "1");
        assertNotNull(_F.getParameter(Function.FUNCTION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable1() {
        Utils.updateParameter(_F, Function.FUNCTION, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable2() {
        Utils.updateParameter(_F, Function.FUNCTION, "x1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable3() {
        Utils.updateParameter(_F, Function.FUNCTION, "x.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidParenthesis() {
        Utils.updateParameter(_F, Function.FUNCTION, "((1)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidNumber1() {
        Utils.updateParameter(_F, Function.FUNCTION, "23,0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidNumber2() {
        Utils.updateParameter(_F, Function.FUNCTION, ".2");
    }

    @Test
    public void shouldBeValidExpression() {
        Utils.updateParameters(_F,
                Function.FUNCTION, "1",
                Function.FUNCTION, "+23.42",
                Function.FUNCTION, "-42",
                Function.FUNCTION, "x",
                Function.FUNCTION, "x0",
                Function.FUNCTION, "x23",
                Function.FUNCTION, "1+2-5",
                Function.FUNCTION, "3*5/3",
                Function.FUNCTION, "-1+-2-+5",
                Function.FUNCTION, "-3*-5/+3",
                Function.FUNCTION, "(1)",
                Function.FUNCTION, "(23+x)*x");
    }

    @Test
    public void shouldBeConstant() {
        Utils.updateParameter(_F, Function.FUNCTION, "0");
        assertEquals(new Double(0), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "1");
        assertEquals(new Double(1), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "-1");
        assertEquals(new Double(-1), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "(-1)");
        assertEquals(new Double(-1), Utils.process(_F));
    }

    @Test
    public void shouldAddAndSubtract() {
        Utils.updateParameter(_F, Function.FUNCTION, "0.5+3.5-4");
        assertEquals(new Double(0), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "3.33++6.67");
        assertEquals(new Double(10), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "2.5--2.5");
        assertEquals(new Double(5), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "((2.5)--2.5)");
        assertEquals(new Double(5), Utils.process(_F));
    }

    @Test
    public void shouldMultiplyAndDivide() {
        Utils.updateParameter(_F, Function.FUNCTION, "3*-0.5/0.1");
        assertEquals(new Double(-15), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "-23/-10");
        assertEquals(new Double(2.3), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "(-23/(-10))");
        assertEquals(new Double(2.3), Utils.process(_F));
    }

    @Test
    public void shouldUseExponent() {
        Utils.updateParameter(_F, Function.FUNCTION, "0^4");
        assertEquals(new Double(0), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "-2^3");
        assertEquals(new Double(-8), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "-2^2^2");
        assertEquals(new Double(16), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "2^-2");
        assertEquals(new Double(0.25), Utils.process(_F));
        Utils.updateParameter(_F, Function.FUNCTION, "2^(2+3)");
        assertEquals(new Double(32), Utils.process(_F));
    }

    @Test
    public void shouldBeLinear() {
        Utils.updateParameter(_F, Function.FUNCTION, "4+x*7");
        assertEquals(new Double(-3), Utils.process(_F, -1.0));
        assertEquals(new Double(4), Utils.process(_F, 0.0));
        assertEquals(new Double(7.5), Utils.process(_F, 0.5));
        assertEquals(new Double(11), Utils.process(_F, 1.0));

        Utils.updateParameter(_F, Function.FUNCTION, "(4+x)*7");
        assertEquals(new Double(28), Utils.process(_F, 0.0));
        assertEquals(new Double(31.5), Utils.process(_F, 0.5));
        assertEquals(new Double(35), Utils.process(_F, 1.0));
    }

    @Test
    public void shouldBeExponential() {
        Utils.updateParameter(_F, Function.FUNCTION, "4+x^2");
        assertEquals(new Double(4), Utils.process(_F, 0.0));
        assertEquals(new Double(4.25), Utils.process(_F, 0.5));
        assertEquals(new Double(13), Utils.process(_F, 3.0));

        Utils.updateParameter(_F, Function.FUNCTION, "(4+x)^2");
        assertEquals(new Double(16), Utils.process(_F, 0.0));
        assertEquals(new Double(49), Utils.process(_F, 3.0));

        Utils.updateParameter(_F, Function.FUNCTION, "x^(2+1)");
        assertEquals(new Double(0), Utils.process(_F, 0.0));
        assertEquals(new Double(27), Utils.process(_F, 3.0));

        Utils.updateParameter(_F, Function.FUNCTION, "x^x");
        assertEquals(new Double(1), Utils.process(_F, 0.0));
        assertEquals(new Double(27), Utils.process(_F, 3.0));
    }

    @Test
    public void shouldBeSin() {
        double delta = 0.00001;
        Utils.updateParameter(_F, Function.FUNCTION, "sin(x)");
        assertEquals(0d, Utils.process(_F, 0.0), delta);
        assertEquals(1d, Utils.process(_F, Math.PI * 0.5), delta);
        assertEquals(0d, Utils.process(_F, Math.PI), delta);
        assertEquals(-1d, Utils.process(_F, Math.PI * 1.5), delta);
        assertEquals(0d, Utils.process(_F, Math.PI * 2), delta);
    }

    @Test
    public void shouldBeCos() {
        double delta = 0.00001;
        Utils.updateParameter(_F, Function.FUNCTION, "cos(x)");
        assertEquals(1d, Utils.process(_F, 0.0), delta);
        assertEquals(0d, Utils.process(_F, Math.PI * 0.5), delta);
        assertEquals(-1d, Utils.process(_F, Math.PI), delta);
        assertEquals(0d, Utils.process(_F, Math.PI * 1.5), delta);
        assertEquals(1d, Utils.process(_F, Math.PI * 2), delta);
    }
}
