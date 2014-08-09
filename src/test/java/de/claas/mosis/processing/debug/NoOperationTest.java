package de.claas.mosis.processing.debug;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link NoOperation}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class NoOperationTest {

    private NoOperation _P;

    @Before
    public void before() throws Exception {
	_P = new NoOperation();
	_P.setUp();
    }

    @After
    public void after() {
	_P.dismantle();
    }

    @Test
    public void shouldNotReturnValues() {
	assertTrue(Utils.processAll(_P).isEmpty());
	assertTrue(Utils.processAll(_P, (Object) null).isEmpty());
	assertTrue(Utils.processAll(_P, "hello world").isEmpty());
	assertTrue(Utils.processAll(_P, 1, 23).isEmpty());
    }

}
