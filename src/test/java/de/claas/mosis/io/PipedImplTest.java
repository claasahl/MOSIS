package de.claas.mosis.io;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

/**
 * The JUnit test for class {@link de.claas.mosis.io.PipedImpl}. It is intended
 * to collect and document a set of test cases for the tested class. Please
 * refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class PipedImplTest extends StreamHandlerImplTest {

    @Override
    public StreamHandlerImpl build() throws Exception {
        return new PipedImpl();
    }

    @Test
    public void shouldBeConnectedIO() throws Exception {
        InputStream sI = _I.getInputStream();
        OutputStream sO = _I.getOutputStream();

        sO.write(23);
        sO.write(42);
        sO.flush();
        assertEquals(23, sI.read());
        assertEquals(42, sI.read());
    }

    @Test
    public void shouldBeConnectedOI() throws Exception {
        OutputStream sO = _I.getOutputStream();
        InputStream sI = _I.getInputStream();

        sO.write(23);
        sO.write(42);
        sO.flush();
        assertEquals(23, sI.read());
        assertEquals(42, sI.read());
    }

}
