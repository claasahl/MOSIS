package de.claas.mosis.io;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * The JUnit test for class {@link UrlImpl}. It is intended to collect and
 * document a set of test cases for the tested class. Please refer to the
 * individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class UrlImplTest extends StreamHandlerImplTest {

    @Override
    public StreamHandlerImpl build() throws Exception {
        UrlImpl i = new UrlImpl();
        i.setParameter(UrlImpl.URL, "http://google.de");
        return i;
    }

    @Test
    public void assumptionsOnUrl() throws Exception {
        assertNotNull(_I.getParameter(UrlImpl.URL));
        assertTrue(_I.getParameter(UrlImpl.URL).startsWith("http://"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterUrlMayNotBeNull() throws Exception {
        _I.setParameter(UrlImpl.URL, null);
    }

    @Test
    public void shouldBeReadable() throws Exception {
        InputStream stream = _I.getInputStream();
        assertTrue(stream.read() >= 0);
        stream.close();
    }

    @Test(expected = IOException.class)
    public void shouldNotBeWritable() throws Exception {
        File file = File.createTempFile("hello", "world");
        _I.setParameter(UrlImpl.URL, "file://" + file.getAbsolutePath());
        _I.getOutputStream();
    }

}
