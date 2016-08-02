package de.claas.mosis.processing.observer;

import de.claas.mosis.model.ConfigurableAdapter;
import de.claas.mosis.model.Observer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The JUnit test for class {@link de.claas.mosis.model.Observer.ParameterHistory}.
 * It is intended to collect and document a set of test cases for the tested
 * class. Please refer to the individual tests for more detailed information.
 * <p>
 * Additional test cases can be found in {@link de.claas.mosis.model.ObserverTest}.
 *
 * @author Claas Ahlrichs (claasahl@tzi.de)
 */
public class ParameterHistoryTest {

    private static final String P = "paramXY";
    private static final String H = Observer.ParameterHistory.Prefix
            + "paramXY";
    private ConfigurableAdapter _Dummy;

    /**
     * Returns an instantiated {@link de.claas.mosis.model.Observer.ParameterHistory}
     * class. If appropriate, the instance is configured with default values.
     *
     * @return an instantiated {@link de.claas.mosis.model.Observer.ParameterHistory}
     * class
     */
    private Observer build() {
        return new Observer.ParameterHistory();
    }

    @Before
    public void setUp() {
        _Dummy = new ConfigurableAdapter();
    }

    @Test
    public void shouldAddParameter() {
        Observer o = build();
        assertFalse(_Dummy.getParameters().contains(H));
        o.update(_Dummy, P);
        assertTrue(_Dummy.getParameters().contains(H));
    }

    @Test
    public void shouldUpdateParameter() {
        Observer o = build();
        _Dummy.setParameter(P, "hello");
        o.update(_Dummy, P);
        assertTrue(_Dummy.getParameters().contains(H));
        assertTrue(_Dummy.getParameter(H).contains("hello@"));
        assertFalse(_Dummy.getParameter(H).contains("world@"));

        _Dummy.setParameter(P, "world");
        o.update(_Dummy, P);
        assertTrue(_Dummy.getParameter(H).contains("hello@"));
        assertTrue(_Dummy.getParameter(H).contains("world@"));
    }

    @Test
    public void shouldNotUpdateParameter() {
        Observer o = build();
        o.update(_Dummy, H);
        assertEquals(0, _Dummy.getParameters().size());
    }

    @Test
    public void shouldHandleNullValues() {
        Observer o = build();
        o.update(_Dummy, P);
        assertTrue(_Dummy.getParameter(H).contains("null@"));
    }

}
