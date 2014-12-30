package de.claas.mosis.processing.debug;

import de.claas.mosis.model.DecoratorProcessorTest;
import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link SystemOut}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest},
 * {@link ProcessorAdapterTest} and {@link DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class SystemOutTest {

    private SystemOut _P;

    @Before
    public void before() throws Exception {
        _P = new SystemOut();
        _P.setParameter(SystemOut.CLASS, Null.class.getName());
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
    }

    @Test
    public void assumptionsOnParameterClass() {
        assertNotNull(_P.getParameter(SystemOut.CLASS));
    }

    @Test
    public void assumptionsOnName() {
        assertNotNull(_P.getParameter(SystemOut.NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterNameMayNotBeNull() throws Exception {
        _P.setParameter(SystemOut.NAME, null);
    }

    @Test
    public void shouldPrintToSystemOut() throws IOException {
        PipedInputStream in = new PipedInputStream();
        PipedOutputStream out = new PipedOutputStream(in);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        PrintStream ps = new PrintStream(out);
        System.setOut(ps);

        assertFalse(br.ready());
        Utils.process(_P, (Object) null);
        ps.flush();
        assertTrue(br.ready());
        String line = br.readLine();
        assertTrue(line.contains("null"));
    }

}
