package de.claas.mosis.io.generator;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;

/**
 * The JUnit test for class {@link Time}. It is intended to collect and document
 * a set of test cases for the tested class. Please refer to the individual
 * tests for more detailed information.
 * 
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 * 
 * @author Claas Ahlrichs (claasahl@tzi.de)
 * 
 */
public class TimeTest {

    private Time _T;

    @Before
    public void before() throws Exception {
	_T = new Time();
	_T.setUp();
    }

    @After
    public void after() {
	_T.dismantle();
    }

    @Test
    public void shouldReturnCurrentTime() {
	long time = Utils.process(_T);
	assertTrue(time + 5 >= System.currentTimeMillis());
    }

}
