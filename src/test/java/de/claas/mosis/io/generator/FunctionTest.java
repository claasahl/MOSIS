package de.claas.mosis.io.generator;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The JUnit test for class {@link Function}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
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
        _F.setParameter(Function.FUNCTION, "1");
        assertNotNull(_F.getParameter(Function.FUNCTION));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable1() {
        _F.setParameter(Function.FUNCTION, "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable2() {
        _F.setParameter(Function.FUNCTION, "x1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidVariable3() {
        _F.setParameter(Function.FUNCTION, "x.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidParenthesis() {
        _F.setParameter(Function.FUNCTION, "((1)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidNumber1() {
        _F.setParameter(Function.FUNCTION, "23,0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void InvalidNumber2() {
        _F.setParameter(Function.FUNCTION, ".2");
    }

    @Test
    public void shouldBeValidExpression() {
        _F.setParameter(Function.FUNCTION, "1");
        _F.setParameter(Function.FUNCTION, "+23.42");
        _F.setParameter(Function.FUNCTION, "-42");
        _F.setParameter(Function.FUNCTION, "x");
        _F.setParameter(Function.FUNCTION, "x0");
        _F.setParameter(Function.FUNCTION, "x23");
        _F.setParameter(Function.FUNCTION, "1+2-5");
        _F.setParameter(Function.FUNCTION, "3*5/3");

        _F.setParameter(Function.FUNCTION, "-1+-2-+5");
        _F.setParameter(Function.FUNCTION, "-3*-5/+3");

        _F.setParameter(Function.FUNCTION, "(1)");
        _F.setParameter(Function.FUNCTION, "(23+x)*x");
    }

    @Test
    public void shouldBeConstant() {
        _F.setParameter(Function.FUNCTION, "0");
        assertEquals(new Double(0), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "1");
        assertEquals(new Double(1), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "-1");
        assertEquals(new Double(-1), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "(-1)");
        assertEquals(new Double(-1), Utils.process(_F));
    }

    @Test
    public void shouldAddAndSubtrack() {
        _F.setParameter(Function.FUNCTION, "0.5+3.5-4");
        assertEquals(new Double(0), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "3.33++6.67");
        assertEquals(new Double(10), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "2.5--2.5");
        assertEquals(new Double(5), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "((2.5)--2.5)");
        assertEquals(new Double(5), Utils.process(_F));
    }

    @Test
    public void shouldMultiplyAndDivide() {
        _F.setParameter(Function.FUNCTION, "3*-0.5/0.1");
        assertEquals(new Double(-15), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "-23/-10");
        assertEquals(new Double(2.3), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "(-23/(-10))");
        assertEquals(new Double(2.3), Utils.process(_F));
    }

    @Test
    public void shouldUseExponent() {
        _F.setParameter(Function.FUNCTION, "0^4");
        assertEquals(new Double(0), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "-2^3");
        assertEquals(new Double(-8), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "-2^2^2");
        assertEquals(new Double(16), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "2^-2");
        assertEquals(new Double(0.25), Utils.process(_F));
        _F.setParameter(Function.FUNCTION, "2^(2+3)");
        assertEquals(new Double(32), Utils.process(_F));
    }

    @Test
    public void shouldBeLinear() {
        _F.setParameter(Function.FUNCTION, "4+x*7");
        assertEquals(new Double(-3), Utils.process(_F, -1.0));
        assertEquals(new Double(4), Utils.process(_F, 0.0));
        assertEquals(new Double(7.5), Utils.process(_F, 0.5));
        assertEquals(new Double(11), Utils.process(_F, 1.0));

        _F.setParameter(Function.FUNCTION, "(4+x)*7");
        assertEquals(new Double(28), Utils.process(_F, 0.0));
        assertEquals(new Double(31.5), Utils.process(_F, 0.5));
        assertEquals(new Double(35), Utils.process(_F, 1.0));
    }

    @Test
    public void shouldBeExponential() {
        _F.setParameter(Function.FUNCTION, "4+x^2");
        assertEquals(new Double(4), Utils.process(_F, 0.0));
        assertEquals(new Double(4.25), Utils.process(_F, 0.5));
        assertEquals(new Double(13), Utils.process(_F, 3.0));

        _F.setParameter(Function.FUNCTION, "(4+x)^2");
        assertEquals(new Double(16), Utils.process(_F, 0.0));
        assertEquals(new Double(49), Utils.process(_F, 3.0));

        _F.setParameter(Function.FUNCTION, "x^(2+1)");
        assertEquals(new Double(0), Utils.process(_F, 0.0));
        assertEquals(new Double(27), Utils.process(_F, 3.0));

        _F.setParameter(Function.FUNCTION, "x^x");
        assertEquals(new Double(1), Utils.process(_F, 0.0));
        assertEquals(new Double(27), Utils.process(_F, 3.0));
    }

    @Test
    public void shouldBeSin() {
        double delta = 0.00001;
        _F.setParameter(Function.FUNCTION, "sin(x)");
        assertEquals(new Double(0), Utils.process(_F, 0.0), delta);
        assertEquals(new Double(1), Utils.process(_F, Math.PI * 0.5), delta);
        assertEquals(new Double(0), Utils.process(_F, Math.PI), delta);
        assertEquals(new Double(-1), Utils.process(_F, Math.PI * 1.5), delta);
        assertEquals(new Double(0), Utils.process(_F, Math.PI * 2), delta);
    }

    @Test
    public void shouldBeCos() {
        double delta = 0.00001;
        _F.setParameter(Function.FUNCTION, "cos(x)");
        assertEquals(new Double(1), Utils.process(_F, 0.0), delta);
        assertEquals(new Double(0), Utils.process(_F, Math.PI * 0.5), delta);
        assertEquals(new Double(-1), Utils.process(_F, Math.PI), delta);
        assertEquals(new Double(0), Utils.process(_F, Math.PI * 1.5), delta);
        assertEquals(new Double(1), Utils.process(_F, Math.PI * 2), delta);
    }
}
