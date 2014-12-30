package de.claas.mosis.io;

import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link StreamHandler} classes. It is intended to collect
 * and document a set of test cases that are applicable to all
 * {@link StreamHandler} classes. Please refer to the individual tests for more
 * detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class StreamHandlerTest<S, T extends StreamHandler<S>> extends
        DataHandlerTest<S, T> {

    @Test
    public void assumptionsOnImpl() throws Exception {
        assertEquals(StandardInputOutputImpl.class.getName(),
                _H.getParameter(StreamHandler.IMPL));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterImplMayNotBeNull() throws Exception {
        _H.setParameter(StreamHandler.IMPL, null);
    }

    @Test
    public void shouldInitializeImplementation() throws Exception {
        T h = build();
        assertNull(h.getParameter(FileImpl.FILE));
        h.setParameter(StreamHandler.IMPL, FileImpl.class.getName());
        assertNotNull(h.getParameter(FileImpl.FILE));
    }


    @Test
    public void shouldInstantiateNewClass() throws Exception {
        _H.setParameter(StreamHandler.IMPL, FileImpl.class.getName());
        assertNotNull(_H.getImpl());
        assertEquals(FileImpl.class, _H.getImpl().getClass());
    }

    @Test
    public void shouldIncludeNonLocalParameters() throws Exception {
        FileImpl dummy = new FileImpl();
        assertTrue(dummy.getParameters().size() > 0);

        _H.setParameter(StreamHandler.IMPL, dummy.getClass().getName());
        assertTrue(_H.getParameters().containsAll(dummy.getParameters()));
    }

    @Test
    public void shouldNotForwardLocalParameters() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
        assertEquals(DataHandler.MODE_READ, _H.getParameter(DataHandler.MODE));
        _H.setParameter(StreamHandler.IMPL, FileImpl.class.getName());
        assertEquals(DataHandler.MODE_READ, _H.getParameter(DataHandler.MODE));
    }

    @Test
    public void shouldForwardImplParameters() throws Exception {
        String file1 = "should be ignored";
        String file2 = "should not be ignored";
        _H.setParameter(FileImpl.FILE, file1);
        _H.setParameter(StreamHandler.IMPL, FileImpl.class.getName());
        _H.setParameter(FileImpl.FILE, file2);
        assertEquals(file2, _H.getParameter(FileImpl.FILE));
        _H.setParameter(StreamHandler.IMPL, PipedImpl.class.getName());
        assertEquals(file1, _H.getParameter(FileImpl.FILE));
    }

    @Test
    public void shouldCacheInputStream() throws Exception {
        InputStream s1 = _H.getInputStream();
        InputStream s2 = _H.getInputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertTrue(s1.equals(s2));
    }

    @Test
    public void shouldCacheOutputStream() throws Exception {
        OutputStream s1 = _H.getOutputStream();
        OutputStream s2 = _H.getOutputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertTrue(s1.equals(s2));
    }

}
