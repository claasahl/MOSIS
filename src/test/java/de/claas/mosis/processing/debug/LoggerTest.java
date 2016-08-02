package de.claas.mosis.processing.debug;

import de.claas.mosis.util.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link de.claas.mosis.processing.debug.Logger}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ProcessorTest},
 * {@link de.claas.mosis.model.ProcessorAdapterTest} and {@link
 * de.claas.mosis.model.DecoratorProcessorTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LoggerTest {

    private PipedInputStream loggerStream;
    private Logger<Object, Object> _P;
    private Handler handler;
    private java.util.logging.Logger logger;

    @Before
    public void before() throws Exception {
        loggerStream = new PipedInputStream();

        handler = new StreamHandler(new PipedOutputStream(loggerStream), new SimpleFormatter());
        handler.setLevel(Level.ALL);
        logger = java.util.logging.Logger.getLogger("de.claas.mosis");
        logger.setLevel(Level.ALL);
        logger.addHandler(handler);

        _P = new Logger<>();
        _P.setParameter(Logger.CLASS, ToString.class.getName());
        _P.setUp();
    }

    @After
    public void after() {
        _P.dismantle();
        try {
            loggerStream.close();
            logger.removeHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assumptionsOnName() throws Exception {
        assertEquals("de.claas.mosis", _P.getParameter(Logger.NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterNameMayNotBeNull() throws Exception {
        Utils.updateParameter(_P, Logger.NAME, null);
    }

    @Test
    public void shouldContainData() throws IOException, InterruptedException {
        handler.flush();
        assertTrue(loggerStream.available() > 0);
    }

    @Test
    public void shouldAppendData() throws IOException {
        handler.flush();
        int available = loggerStream.available();
        Utils.process(_P);
        handler.flush();
        assertTrue(loggerStream.available() > available);
    }

}
