package de.claas.mosis.processing.debug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.DecoratorProcessorTest;
import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link Counter}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest},
 * {@link ProcessorAdapterTest} and {@link DecoratorProcessorTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class CounterTest {

    private Counter _P;

    @Before
    public void before() throws Exception {
	_P = new Counter();
	_P.setParameter(Counter.CLASS, Null.class.getName());
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
    public void assumptionsOnParameterCounter() {
	assertEquals("0", _P.getParameter(Counter.COUNTER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterCounterMayNotBeNull() throws Exception {
	_P.setParameter(Counter.COUNTER, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterCounterMustBeAnInteger() throws Exception {
	try {
	    _P.setParameter(Counter.COUNTER, "1");
	    _P.setParameter(Counter.COUNTER, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(Counter.COUNTER, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterCounterMustBePostive() throws Exception {
	try {
	    _P.setParameter(Counter.COUNTER, "0");
	    _P.setParameter(Counter.COUNTER, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(Counter.COUNTER, "-1");
    }

    @Test
    public void shouldCount() {
	Utils.process(_P);
	assertEquals("1", _P.getParameter(Counter.COUNTER));
	Utils.process(_P, (Object) null);
	Utils.process(_P, 23, 42);
	Utils.process(_P);
	assertEquals("4", _P.getParameter(Counter.COUNTER));
    }
}
