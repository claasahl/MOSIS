package de.claas.mosis.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.claas.mosis.io.generator.Time;
import de.claas.mosis.model.Configurable;
import de.claas.mosis.processing.debug.Forward;
import de.claas.mosis.processing.debug.Null;
import de.claas.mosis.processing.util.Distance;

/**
 * The JUnit test for class {@link Utils}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class UtilsTest {

    @Test
    public void shouldReturnFirstResult() throws Exception {
	assertNull(Utils.process(new Null()));
	long time1 = Utils.process(new Time());
	Thread.sleep(100);
	long time2 = Utils.process(new Time());
	assertTrue(time2 > time1);
    }

    @Test
    public void shouldReturnAllResults() {
	String[] tmp = new String[] { "hello", "world" };
	List<String> outputs = Utils.processAll(new Forward<String>(), tmp);
	assertEquals(2, outputs.size());
	assertTrue(outputs.contains("hello"));
	assertTrue(outputs.contains("world"));
    }

    @Test
    public void shouldInstantiateClass() throws Exception {
	String tmp = Utils.instance(String.class);
	assertNotNull(tmp);
	assertEquals(String.class, tmp.getClass());

	tmp = Utils.instance(String.class, "hello world");
	assertNotNull(tmp);
	assertEquals("hello world", tmp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotInstantiateClass() throws Exception {
	Utils.instance(Double.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAlsoNotInstantiateClass() throws Exception {
	Utils.instance(null);
    }

    @Test
    public void shouldReturnUnknownParameter() {
	Configurable c = new Distance();
	String param = Utils.unknownParameter(c);
	assertFalse(c.getParameters().contains(param));
    }

}
