package de.claas.mosis.processing.debug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link Null}. It is intended to collect and document
 * a set of test cases for the tested class. Please refer to the individual
 * tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class NullTest {

    private Null _P;

    @Before
    public void before() throws Exception {
	_P = new Null();
	_P.setUp();
    }

    @After
    public void after() {
	_P.dismantle();
    }

    @Test
    public void shouldReturnNull() {
	assertNull(Utils.process(_P));
	assertNull(Utils.process(_P, (Object) null));
	assertNull(Utils.process(_P, "hello world"));
	assertNull(Utils.process(_P, 1, 2, 3, 4));

	Set<Object> tmp = new HashSet<>();
	tmp.addAll(Utils.processAll(_P, "hello", 23, "world"));
	assertTrue(tmp.contains(null));
	assertEquals(1, tmp.size());
    }

}
