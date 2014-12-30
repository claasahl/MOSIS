package de.claas.mosis.io;

import de.claas.mosis.model.ConfigurableAdapterTest;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * The JUnit test for {@link StreamHandlerImpl} classes. It is intended to
 * collect and document a set of test cases that are applicable to all
 * {@link StreamHandlerImpl} classes. Please refer to the individual tests for
 * more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ConfigurableAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class StreamHandlerImplTest {

    protected StreamHandlerImpl _I;

    /**
     * Returns an instance of the tested {@link StreamHandlerImpl} class. Every
     * call will result in a new instance of the tested class.
     *
     * @return an instance of the tested {@link StreamHandlerImpl} class
     */
    public abstract StreamHandlerImpl build() throws Exception;

    @Before
    public void before() throws Exception {
        _I = build();
    }

    @Test
    public void shouldReturnNewInputStream() throws Exception {
        InputStream s1 = _I.getInputStream();
        InputStream s2 = _I.getInputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertFalse(s1.equals(s2));
    }

    @Test
    public void shouldReturnNewOuputStream() throws Exception {
        OutputStream s1 = _I.getOutputStream();
        OutputStream s2 = _I.getOutputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertFalse(s1.equals(s2));
    }

}
