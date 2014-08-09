package de.claas.mosis.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.processing.util.Distance;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for {@link ComparingProcessor} classes. It is intended to
 * collect and document a set of test cases that are applicable to all
 * {@link ComparingProcessor} classes. Please refer to the individual tests for
 * more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
@RunWith(Parameterized.class)
public class ComparingProcessorTest {

    private Class<ComparingProcessor<Object, Object>> _Clazz;
    private ComparingProcessor<Object, Object> _P;

    /**
     * Initializes this JUnit test for an implementation of the
     * {@link ComparingProcessor} class.
     * 
     * @param clazz
     *            implementation of {@link ComparingProcessor} class
     */
    public ComparingProcessorTest(
	    Class<ComparingProcessor<Object, Object>> clazz) {
	_Clazz = clazz;
    }

    @Before
    public void before() throws Exception {
	_P = Utils.instance(_Clazz);
	_P.setUp();
    }

    @After
    public void after() {
	_P.dismantle();
    }

    @Test
    public void assumptionsOnPortToUse() throws Exception {
	assertEquals("0", _P.getParameter(ComparingProcessor.PORT_TO_USE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMayNotBeNull() throws Exception {
	_P.setParameter(ComparingProcessor.PORT_TO_USE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBeAnInteger() throws Exception {
	try {
	    _P.setParameter(ComparingProcessor.PORT_TO_USE, "1");
	    _P.setParameter(ComparingProcessor.PORT_TO_USE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(ComparingProcessor.PORT_TO_USE, "1.2");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterPortToUseMustBePostive() throws Exception {
	try {
	    _P.setParameter(ComparingProcessor.PORT_TO_USE, "0");
	    _P.setParameter(ComparingProcessor.PORT_TO_USE, "12");
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P.setParameter(ComparingProcessor.PORT_TO_USE, "-1");
    }

    @Test
    public void shouldBeNull() throws Exception {
	assertNull(_P.replace(null));
	assertNull(_P.replace(null));
	assertNull(_P.replace(null));
    }

    @Test
    public void shouldBufferLastSample() throws Exception {
	assertNull(_P.replace(new Integer(23)));
	assertEquals(new Integer(23), _P.replace(new Integer(42)));
	assertEquals(new Integer(42), _P.replace(null));
	assertNull(_P.replace(null));
    }

    @Parameters
    public static Collection<?> implementations() {
	List<Object> impl = new Vector<Object>();
	impl.add(new Object[] { Distance.class });
	return impl;
    }

}
