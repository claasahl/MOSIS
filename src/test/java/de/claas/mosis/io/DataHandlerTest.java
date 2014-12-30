package de.claas.mosis.io;

import de.claas.mosis.model.ProcessorAdapterTest;
import de.claas.mosis.model.ProcessorTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * The JUnit test for {@link DataHandler} classes. It is intended to collect and
 * document a set of test cases that are applicable to all {@link DataHandler}
 * classes. Please refer to the individual tests for more detailed information.
 * <p/>
 * Additional test cases can be found in {@link ProcessorTest} and
 * {@link ProcessorAdapterTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public abstract class DataHandlerTest<S, T extends DataHandler<S>> {

    protected T _H;

    /**
     * Returns an instantiated {@link DataHandler} class. If appropriate, the
     * instance is configured with default values.
     *
     * @return an instantiated {@link DataHandler} class
     */
    protected abstract T build() throws Exception;

    @Before
    public void before() throws Exception {
        _H = build();
        _H.setUp();
    }

    @After
    public void after() {
        _H.dismantle();
    }

    @Test
    public abstract void shouldRead() throws Exception;

    @Test
    public abstract void shouldWrite() throws Exception;

    @Test
    public abstract void shouldDetermineMode() throws Exception;

    @Test
    public void assumptionsOnMode() throws Exception {
        assertEquals(DataHandler.MODE_AUTO, _H.getParameter(DataHandler.MODE));
    }

    @Test
    public void assumptionsOnForwardInputs() throws Exception {
        assertEquals("true", _H.getParameter(DataHandler.FORWARD_INPUTS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterModeMayNotBeNull() throws Exception {
        _H.setParameter(DataHandler.MODE, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterCounterMustBeInWhiteList() throws Exception {
        try {
            _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);
            _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
            _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);
        } catch (Exception e) {
            fail(e.toString());
        }
        _H.setParameter(DataHandler.MODE, "hello world");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterForwardInputsMayNotBeNull() throws Exception {
        _H.setParameter(DataHandler.FORWARD_INPUTS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parameterForwardInputsMustBeBoolean() throws Exception {
        try {
            _H.setParameter(DataHandler.FORWARD_INPUTS, "true");
            _H.setParameter(DataHandler.FORWARD_INPUTS, "false");
        } catch (Exception e) {
            fail(e.toString());
        }
        _H.setParameter(DataHandler.FORWARD_INPUTS, "maybe");
    }

    @Test
    public void shouldBeReadOnly() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_READ);
        assertTrue(_H.isReadOnly(null));
        assertTrue(_H.isReadOnly(Arrays.asList()));
        assertTrue(_H.isReadOnly(Arrays.asList("hello world")));
        assertFalse(_H.isWriteOnly(null));
        assertFalse(_H.isWriteOnly(Arrays.asList()));
        assertFalse(_H.isWriteOnly(Arrays.asList("hello world")));
    }

    @Test
    public void shouldBeWriteOnly() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_WRITE);
        assertFalse(_H.isReadOnly(null));
        assertFalse(_H.isReadOnly(Arrays.asList()));
        assertFalse(_H.isReadOnly(Arrays.asList("hello world")));
        assertTrue(_H.isWriteOnly(null));
        assertTrue(_H.isWriteOnly(Arrays.asList()));
        assertTrue(_H.isWriteOnly(Arrays.asList("hello world")));
    }

    @Test
    public void shouldAutomaticallySwitchModes() throws Exception {
        _H.setParameter(DataHandler.MODE, DataHandler.MODE_AUTO);
        assertTrue(_H.isReadOnly(null));
        assertTrue(_H.isReadOnly(Arrays.asList()));
        assertFalse(_H.isReadOnly(Arrays.asList("hello world")));
        assertFalse(_H.isWriteOnly(null));
        assertFalse(_H.isWriteOnly(Arrays.asList()));
        assertTrue(_H.isWriteOnly(Arrays.asList("hello world")));
    }

}
