package de.claas.mosis.io;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * The JUnit test for class {@link de.claas.mosis.io.StandardInputOutputImpl}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class StandardInputOutputImplTest extends StreamHandlerImplTest {

    @Override
    public StandardInputOutputImpl build() throws Exception {
        return new StandardInputOutputImpl();
    }

    @Override
    @Test
    public void shouldReturnNewInputStream() throws Exception {
        // INFO: System.in-stream does not change
        InputStream s1 = _I.getInputStream();
        InputStream s2 = _I.getInputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertEquals(s1, s2);
    }

    @Override
    @Test
    public void shouldReturnNewOutputStream() throws Exception {
        // INFO: System.out-stream does not change
        OutputStream s1 = _I.getOutputStream();
        OutputStream s2 = _I.getOutputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertEquals(s1, s2);
    }

    @Test
    public void shouldReturnSystemOut() throws Exception {
        OutputStream s1 = _I.getOutputStream();
        assertNotNull(s1);
        assertEquals(System.out, s1);
    }

    @Test
    public void shouldReturnSystemIn() throws Exception {
        InputStream s1 = _I.getInputStream();
        assertNotNull(s1);
        assertEquals(System.in, s1);
    }
}
