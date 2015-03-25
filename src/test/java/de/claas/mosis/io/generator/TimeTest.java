package de.claas.mosis.io.generator;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.io.generator.Time}. It is
 * intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest}
 * and {@link de.claas.mosis.model.ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
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
