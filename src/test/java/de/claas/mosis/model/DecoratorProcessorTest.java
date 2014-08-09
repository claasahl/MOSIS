package de.claas.mosis.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.claas.mosis.io.generator.Function;
import de.claas.mosis.io.generator.Linear;
import de.claas.mosis.processing.debug.BreakOut;
import de.claas.mosis.processing.debug.Counter;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.Logger;
import de.claas.mosis.processing.debug.Null;
import de.claas.mosis.processing.debug.Sleep;
import de.claas.mosis.processing.debug.SystemOut;
import de.claas.mosis.processing.debug.Time;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for {@link DecoratorProcessor} classes. It is intended to
 * collect and document a set of test cases that are applicable to all
 * {@link DecoratorProcessor} classes. Please refer to the individual tests for
 * more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
@RunWith(Parameterized.class)
public class DecoratorProcessorTest {

    private Class<DecoratorProcessor<Object, Object>> _Clazz;
    private DecoratorProcessor<Object, Object> _P1;
    private Processor<Object, Object> _P2;

    /**
     * Initializes this JUnit test for an implementation of the
     * {@link DecoratorProcessor} class.
     * 
     * @param clazz
     *            implementation of {@link DecoratorProcessor} class
     */
    public DecoratorProcessorTest(
	    Class<DecoratorProcessor<Object, Object>> clazz) {
	_Clazz = clazz;
    }

    @Before
    public void before() throws Exception {
	_P1 = Utils.instance(_Clazz);
	_P1.setUp();
	_P2 = new SystemOut();
	_P2.setUp();
    }

    @After
    public void after() {
	_P1.dismantle();
	_P2.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() throws Exception {
	assertEquals("", _P1.getParameter(DecoratorProcessor.CLASS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterClassMayNotBeNull() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameteClassMustBeValidClass() throws Exception {
	try {
	    _P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
	    _P1.setParameter(DecoratorProcessor.CLASS, Forward.class.getName());
	} catch (Exception e) {
	    fail(e.toString());
	}
	_P1.setParameter(DecoratorProcessor.CLASS, "class.should.not.exist");
    }
    
    @Test
    public void shouldInitializeImplementation() throws Exception {
	DecoratorProcessor<Object,Object> p = Utils.instance(_Clazz);
	assertNull(p.getParameter(Linear.STEP));
	p.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
	assertNotNull(p.getParameter(Linear.STEP));
    }

    @Test
    public void shouldInstantiateNewClass() throws Exception {
	assertNull(_P1.getProcessor());
	_P1.setParameter(DecoratorProcessor.CLASS, SystemOut.class.getName());
	assertNotNull(_P1.getProcessor());
	assertEquals(SystemOut.class, _P1.getProcessor().getClass());
    }

    @Test
    public void shouldIncludeNonLocalParameters() throws Exception {
	assertTrue(_P2.getParameters().size() > 0);

	int before = _P1.getParameters().size();
	_P1.setParameter(DecoratorProcessor.CLASS, SystemOut.class.getName());
	int after = _P1.getParameters().size();
	assertTrue(after > before);
    }

    @Test
    public void shouldShadowNonLocalParameter() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS,
		DecoratorProcessor.class.getName());
	assertEquals(DecoratorProcessor.class.getName(),
		_P1.getParameter(DecoratorProcessor.CLASS));
    }

    @Test
    public void shouldAccessShadowedParameter() throws Exception {
	_P1.setParameter(Linear.B, "hello world");
	_P1.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
	assertEquals("hello world", _P1.getParameter(Linear.B));
	assertEquals("0.0",
		_P1.getParameter(DecoratorProcessor.SHADOWED + Linear.B));
    }

    @Test
    public void shouldFowardNonLocalParameter() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS, Function.class.getName());
	assertNotNull(_P1.getParameter(Function.FUNCTION));
	_P1.setParameter(Function.FUNCTION, "x^3");
	assertEquals("x^3", _P1.getParameter(Function.FUNCTION));
	_P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
	assertNull(_P1.getParameter(Function.FUNCTION));
    }

    @Test
    public void shouldCallProcess() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS, BreakOut.class.getName());
	_P1.setParameter(DecoratorProcessor.SHADOWED + BreakOut.CLASS,
		Null.class.getName());
	assertEquals(0, ((BreakOut) _P1.getProcessor()).getCalls());
	Utils.process(_P1);
	assertEquals(1, ((BreakOut) _P1.getProcessor()).getCalls());
	Utils.process(_P1);
	Utils.process(_P1);
	assertEquals(3, ((BreakOut) _P1.getProcessor()).getCalls());
    }

    @Test
    public void shouldForwardInputValues() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS, BreakOut.class.getName());
	_P1.setParameter(DecoratorProcessor.SHADOWED + BreakOut.CLASS,
		Null.class.getName());
	assertNull(((BreakOut) _P1.getProcessor()).getLastInput());
	Utils.process(_P1, 1, null, 3);
	assertEquals(Arrays.asList(1, null, 3),
		((BreakOut) _P1.getProcessor()).getLastInput());
    }

    @Test
    public void shouldBeLocalParameter() throws Exception {
	String param = Utils.unknownParameter(_P1);
	assertTrue(_P1.isLocalParameter(DecoratorProcessor.CLASS));
	assertFalse(_P1.isLocalParameter(param));
	_P1.setParameter(param, "hello world");
	assertTrue(_P1.isLocalParameter(param));
    }

    @Test
    public void shouldBeNonLocalParameter() throws Exception {
	_P1.setParameter(Linear.B, "hello world");
	assertTrue(_P1.isLocalParameter(Linear.B));
	_P1.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
	assertTrue(_P1.isLocalParameter(Linear.B));
	assertFalse(_P1
		.isLocalParameter(DecoratorProcessor.SHADOWED + Linear.B));
    }

    @Test
    public void shouldShadowNonLocalCondition() throws Exception {
	_P1.setParameter(Linear.B, "");
	_P1.addCondition(Linear.B, new Condition.IsBoolean());
	_P1.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
	assertEquals("", _P1.getParameter(Linear.B));
	assertNotNull(_P1.getParameter(DecoratorProcessor.SHADOWED + Linear.B));

	_P1.setParameter(Linear.B, "false");
	try {
	    _P1.setParameter(Linear.B, "42.3");
	} catch (IllegalArgumentException iae) {
	    // Expected!
	}

	_P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
	try {
	    _P1.setParameter(Linear.B, "abc");
	} catch (IllegalArgumentException iae) {
	    // Expected!
	}
	_P1.removeCondition(Linear.B, new Condition.IsBoolean());
	_P1.setParameter(Linear.B, "abc");
    }

    @Test
    public void shouldAccessShadowedCondition() throws Exception {
	_P1.setParameter(SystemOut.NAME, "hello world");
	_P1.setParameter(DecoratorProcessor.CLASS, SystemOut.class.getName());
	_P1.addCondition(DecoratorProcessor.SHADOWED + SystemOut.NAME,
		new Condition.IsBoolean());
	_P1.setParameter(SystemOut.NAME, "not a boolean");
	try {
	    _P1.setParameter(DecoratorProcessor.SHADOWED + SystemOut.NAME,
		    "not a boolean");
	} catch (IllegalArgumentException e) {
	    // Expected!
	}
	_P1.removeCondition(DecoratorProcessor.SHADOWED + SystemOut.NAME,
		new Condition.IsBoolean());
	_P1.setParameter(DecoratorProcessor.SHADOWED + SystemOut.NAME,
		"hello world");
    }

    @Test
    public void shouldForwardCondition() throws Exception {
	_P1.setParameter(DecoratorProcessor.CLASS, Function.class.getName());
	_P1.addCondition(Function.FUNCTION, new Condition.IsNumeric());
	assertNotNull(_P1.getParameter(Function.FUNCTION));

	try {
	    _P1.setParameter(Function.FUNCTION, "x^3");
	} catch (IllegalArgumentException e) {
	    // Expected!
	}
	_P1.setParameter(Function.FUNCTION, "42.3");
	assertEquals("42.3", _P1.getParameter(Function.FUNCTION));
	_P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
	_P1.setParameter(Function.FUNCTION, "x^3");
    }

    @Test
    public void shouldForwardRelation() throws Exception {
	String param = Utils.unknownParameter(_P1);
	_P1.setParameter(Relation.UpdateVersion.Version, "5");
	_P1.addRelation(new Relation.UpdateVersion());
	_P1.setParameter(DecoratorProcessor.CLASS, Linear.class.getName());
	_P1.addRelation(new Relation.UpdateVersion());

	assertEquals("6", _P1.getParameter(Relation.UpdateVersion.Version));
	assertNull(_P1.getParameter(DecoratorProcessor.SHADOWED
		+ Relation.UpdateVersion.Version));

	_P1.setParameter(Linear.B, -10.0);
	_P1.setParameter(Linear.M, 123.0);
	assertEquals(
		"2",
		_P1.getParameter(DecoratorProcessor.SHADOWED
			+ Relation.UpdateVersion.Version));
	_P1.setParameter(DecoratorProcessor.CLASS, Null.class.getName());
	_P1.setParameter(param, "abc");
	assertEquals("7", _P1.getParameter(Relation.UpdateVersion.Version));
    }

    @Parameters
    public static Collection<?> implementations() {
	List<Object> impl = new Vector<Object>();
	impl.add(new Object[] { DecoratorProcessor.class });
	impl.add(new Object[] { BreakOut.class });
	impl.add(new Object[] { Counter.class });
	impl.add(new Object[] { Sleep.class });
	impl.add(new Object[] { SystemOut.class });
	impl.add(new Object[] { Time.class });
	impl.add(new Object[] { Logger.class });
	return impl;
    }

}
