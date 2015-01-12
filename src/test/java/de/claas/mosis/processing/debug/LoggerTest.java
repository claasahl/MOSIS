package de.claas.mosis.processing.debug;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.Logger}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LoggerTest {

    private Logger<Object, Object> _P;

    @Before
    public void before() throws Exception {
        _P = new Logger<>();
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void dummy() {
        fail();
    }

}
