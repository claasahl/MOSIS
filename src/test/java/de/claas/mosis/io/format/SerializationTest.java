package de.claas.mosis.io.format;

import de.claas.mosis.io.DataHandler;
import de.claas.mosis.io.PipedImpl;
import de.claas.mosis.io.StreamHandler;
import de.claas.mosis.io.StreamHandlerTest;
import de.claas.mosis.util.Utils;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.io.format.Serialization}. It
 * is intended to collect and document a set of test cases for the tested class.
 * Please refer to the individual tests for more detailed information.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class SerializationTest extends StreamHandlerTest<Object, Serialization<Object>> {

    private Serialization<Object> _H;

    @Override
    protected Serialization<Object> build() throws Exception {
        Serialization<Object> handler = new Serialization<>();
        handler.setParameter(StreamHandler.IMPL, PipedImpl.class.getName());
        return handler;
    }

    @Override
    public void before() throws Exception {
        super.before();
        _H = build();
        _H.setUp();
    }

    @Override
    public void after() {
        super.after();
        _H.dismantle();
    }

    @Override
    public void assumptionsOnImpl() throws Exception {
        assertEquals(PipedImpl.class.getName(),
                _H.getParameter(StreamHandler.IMPL));
    }

    @Override
    public void shouldCacheInputStream() throws Exception {
        // BUG: ObjectInputStream blocks immediately unless serialization header
        // can be read
        _H.getOutputStream();
        InputStream s1 = _H.getInputStream();
        _H.getOutputStream();
        InputStream s2 = _H.getInputStream();
        assertNotNull(s1);
        assertNotNull(s2);
        assertTrue(s1.equals(s2));
    }

    @Override
    public void shouldRead() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);

        ObjectOutputStream o = _H.getOutputStream();
        o.writeObject(23L);
        o.writeObject(42L);
        o.flush();
        assertEquals(23L, Utils.process(_H, (Object) null));
        assertEquals(42L, Utils.process(_H, 1, 2));
    }

    @Override
    public void shouldWrite() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);

        assertNull(Utils.process(_H, (Object) null));
        ObjectInputStream i = _H.getInputStream();
        assertEquals(0, i.available());
        assertNull(i.readObject());
        assertEquals(23L, Utils.process(_H, 23L, 1));
        assertEquals(23L, i.readObject());
        assertEquals(42L, Utils.process(_H, 42L, 444));
        assertEquals(1, i.readObject());
        assertEquals(0, i.available());
    }

    @Override
    public void shouldDetermineMode() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);

        ObjectOutputStream o = _H.getOutputStream();
        o.writeObject(10L);
        o.flush();
        ObjectInputStream i = _H.getInputStream();
        assertEquals(10L, Utils.process(_H));
        assertEquals(20L, Utils.process(_H, 20L, 6454));
        assertEquals(20L, i.readObject());
        assertEquals(0, i.available());
    }

}
