package de.claas.mosis.io;

import de.claas.mosis.util.Utils;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link de.claas.mosis.io.StreamHandler} classes. It is
 * intended to collect and document a set of test cases that are applicable to
 * all {@link de.claas.mosis.io.StreamHandler} classes. Please refer to the
 * individual tests for more detailed information.
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
        Utils.updateParameter(_H, StreamHandler.IMPL, null);
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
        Utils.updateParameter(_H, StreamHandler.IMPL, FileImpl.class.getName());
        assertNotNull(_H.getImpl());
        assertEquals(FileImpl.class, _H.getImpl().getClass());
    }

    @Test
    public void shouldIncludeNonLocalParameters() throws Exception {
        FileImpl dummy = new FileImpl();
        assertTrue(dummy.getParameters().size() > 0);

        Utils.updateParameter(_H, StreamHandler.IMPL, dummy.getClass().getName());
        assertTrue(_H.getParameters().containsAll(dummy.getParameters()));
    }

    @Test
    public void shouldNotForwardLocalParameters() throws Exception {
        Utils.updateParameter(_H, DataHandler.MODE, DataHandler.MODE_READ);
        assertEquals(DataHandler.MODE_READ, _H.getParameter(DataHandler.MODE));
        Utils.updateParameter(_H, StreamHandler.IMPL, FileImpl.class.getName());
        assertEquals(DataHandler.MODE_READ, _H.getParameter(DataHandler.MODE));
    }

    @Test
    public void shouldForwardImplParameters() throws Exception {
        String file1 = "should be ignored";
        String file2 = "should not be ignored";
        Utils.updateParameters(_H,
                FileImpl.FILE, file1,
                StreamHandler.IMPL, FileImpl.class.getName(),
                FileImpl.FILE, file2);
        assertEquals(file2, _H.getParameter(FileImpl.FILE));
        Utils.updateParameter(_H, StreamHandler.IMPL, PipedImpl.class.getName());
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
