package de.claas.mosis.processing.observer;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Observer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Observer.LastChanged}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ObserverTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class LastChangedTest {

    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Observer.LastChanged}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Observer.LastChanged}
     * class
     */
    private Observer build() {
        return new Observer.LastChanged();
    }

    @Before
    public void setUp() {
        _Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
        Observer o = build();
        assertFalse(_Dummy.getParameters().contains(
                Observer.LastChanged.LastChanged));
        o.update(_Dummy, null);
        assertTrue(_Dummy.getParameters().contains(
                Observer.LastChanged.LastChanged));
    }

    @Test
    public void shouldUpdateParameter() throws Exception {
        Observer o = build();
        o.update(_Dummy, null);
        String d1 = _Dummy.getParameter(Observer.LastChanged.LastChanged);
        Thread.sleep(10);
        o.update(_Dummy, null);
        String d2 = _Dummy.getParameter(Observer.LastChanged.LastChanged);
        assertNotSame(d1, d2);
        assertTrue(10 <= Long.parseLong(d2) - Long.parseLong(d1));
    }

    @Test
    public void shouldNotUpdateParameter() {
        Observer o = build();
        o.update(_Dummy, null);
        String d1 = _Dummy.getParameter(Observer.LastChanged.LastChanged);
        o.update(_Dummy, Observer.LastChanged.LastChanged);
        String d2 = _Dummy.getParameter(Observer.LastChanged.LastChanged);
        assertEquals(d1, d2);
    }

}
