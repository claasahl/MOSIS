package de.claas.mosis.processing.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link Delay}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class DelayTest {

    private Delay<Object> _P;

    @Before
    public void before() throws Exception {
	_P = new Delay<Object>();
	_P.setParameter(Delay.WINDOW_SIZE, "2");
	_P.setUp();
    }

    @After
    public void after() {
	_P.dismantle();
    }

    @Test
    public void assumptionsOnParameterPortToUse() {
	assertEquals("0", _P.getParameter(Delay.PORT_TO_USE));
    }

    @Test
    public void assumptionsOnParameterWindowSize() {
	assertEquals("2", _P.getParameter(Delay.WINDOW_SIZE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMayNotBeNull() {
	_P.setParameter(Distance.PORT_TO_USE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBeAnInteger() {
	try {
	    _P.setParameter(Distance.PORT_TO_USE, "1");
	    _P.setParameter(Distance.PORT_TO_USE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(Distance.PORT_TO_USE, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBePostive() {
	try {
	    _P.setParameter(Distance.PORT_TO_USE, "0");
	    _P.setParameter(Distance.PORT_TO_USE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(Distance.PORT_TO_USE, "-1");
    }

    @Test
    public void shouldNotDelayInputValues() {
	_P.setParameter(Delay.WINDOW_SIZE, "0");
	assertEquals(10, Utils.process(_P, 10));
	assertEquals(23, Utils.process(_P, 23));
    }

    @Test
    public void shouldDelayInputValues() {
	assertNull(Utils.process(_P, 1));
	assertNull(Utils.process(_P, 2));
	assertEquals(1, Utils.process(_P, 3));
	assertEquals(2, Utils.process(_P, 4));

	_P.setParameter(Delay.WINDOW_SIZE, "1");
	assertEquals(4, Utils.process(_P, 5));
	assertEquals(5, Utils.process(_P, 6));

	_P.setParameter(Delay.WINDOW_SIZE, "0");
	assertEquals(42, Utils.process(_P, 42));
    }

    @Test
    public void shouldUseCorrespondingPort() {
	assertNull(Utils.process(_P, 1, 100));
	assertNull(Utils.process(_P, 2, 200));
	_P.setParameter(Delay.PORT_TO_USE, "1");
	assertEquals(100, Utils.process(_P, null, 300));
	assertEquals(200, Utils.process(_P, 4, 400));
	_P.setParameter(Delay.PORT_TO_USE, "0");
	assertNull(Utils.process(_P, 5, 500));
	assertEquals(4, Utils.process(_P, 6, 600));
    }

}
